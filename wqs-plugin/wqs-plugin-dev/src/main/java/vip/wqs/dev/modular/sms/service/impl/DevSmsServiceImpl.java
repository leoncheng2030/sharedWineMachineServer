/*
 * Copyright [2022] [https://www.xiaonuo.vip]
 *
 * Snowy采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改Snowy源码头部的版权声明。
 * 3.本项目代码可免费商业使用，商业使用请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://www.xiaonuo.vip
 * 5.不可二次分发开源参与同类竞品，如有想法可联系团队xiaonuobase@qq.com商议合作。
 * 6.若您的项目无法满足以上几点，需要更多功能代码，获取Snowy商业授权许可，请在官网购买授权，地址为 https://www.xiaonuo.vip
 */
package vip.wqs.dev.modular.sms.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.wqs.common.enums.CommonSortOrderEnum;
import vip.wqs.common.exception.CommonException;
import vip.wqs.common.page.CommonPageRequest;
import vip.wqs.dev.api.DevConfigApi;
import vip.wqs.dev.modular.sms.entity.DevSms;
import vip.wqs.dev.modular.sms.enums.DevSmsEngineTypeEnum;
import vip.wqs.dev.modular.sms.mapper.DevSmsMapper;
import vip.wqs.dev.modular.sms.param.*;
import vip.wqs.dev.modular.sms.service.DevSmsService;
import vip.wqs.dev.modular.sms.util.DevSmsAliyunUtil;
import vip.wqs.dev.modular.sms.util.DevSmsTencentUtil;
import vip.wqs.dev.modular.sms.util.DevSmsXiaonuoUtil;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 短信Service接口实现类
 *
 * @author xuyuxiang
 * @date 2022/2/23 18:43
 **/
@Service
public class DevSmsServiceImpl extends ServiceImpl<DevSmsMapper, DevSms> implements DevSmsService {

    /** 默认短信引擎 */
    private static final String WQS_SYS_DEFAULT_SMS_ENGINE_KEY = "WQS_SYS_DEFAULT_SMS_ENGINE";

    @Resource
    private DevConfigApi devConfigApi;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void sendDynamic(String engine, String phoneNumbers, String templateCodeOrId, JSONObject templateParam) {
        if(engine.equals(DevSmsEngineTypeEnum.XIAONUO.getValue())) {
            DevSmsSendXiaonuoParam devSmsSendXiaonuoParam = new DevSmsSendXiaonuoParam();
            devSmsSendXiaonuoParam.setPhoneNumbers(phoneNumbers);
            devSmsSendXiaonuoParam.setTemplateCode(templateCodeOrId);
            devSmsSendXiaonuoParam.setTemplateParam(JSONUtil.toJsonStr(templateParam));
            this.sendXiaonuo(devSmsSendXiaonuoParam);
        } else if (engine.equals(DevSmsEngineTypeEnum.ALIYUN.getValue())) {
            DevSmsSendAliyunParam devSmsSendAliyunParam = new DevSmsSendAliyunParam();
            devSmsSendAliyunParam.setPhoneNumbers(phoneNumbers);
            devSmsSendAliyunParam.setTemplateCode(templateCodeOrId);
            devSmsSendAliyunParam.setTemplateParam(JSONUtil.toJsonStr(templateParam));
            this.sendAliyun(devSmsSendAliyunParam);
        } else if (engine.equals(DevSmsEngineTypeEnum.TENCENT.getValue())) {
            DevSmsSendTencentParam devSmsSendTencentParam = new DevSmsSendTencentParam();
            devSmsSendTencentParam.setPhoneNumbers(phoneNumbers);
            devSmsSendTencentParam.setTemplateCode(templateCodeOrId);
            devSmsSendTencentParam.setTemplateParam(JSONUtil.toJsonStr(templateParam));
            this.sendTencent(devSmsSendTencentParam);
        } else {
            throw new CommonException("不支持的短信引擎：{}", engine);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void sendDynamic(DevSmsSendDynamicParam devSmsSendDynamicParam) {
        String defaultSmsEngine = devConfigApi.getValueByKey(WQS_SYS_DEFAULT_SMS_ENGINE_KEY);
        if(ObjectUtil.isEmpty(defaultSmsEngine)) {
            throw new CommonException("请联系管理员配置默认短信发送引擎");
        }
        String phoneNumbers = devSmsSendDynamicParam.getPhoneNumbers();
        String templateCodeOrId = devSmsSendDynamicParam.getTemplateCodeOrId();
        String templateParamStr = devSmsSendDynamicParam.getTemplateParam();
        JSONObject templateParam = JSONUtil.parseObj(templateParamStr);
        this.sendDynamic(defaultSmsEngine, phoneNumbers, templateCodeOrId, templateParam);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void sendAliyun(DevSmsSendAliyunParam devSmsSendAliyunParam) {
        validPhone(devSmsSendAliyunParam.getPhoneNumbers());
        String receiptInfo = DevSmsAliyunUtil.sendSms(devSmsSendAliyunParam.getPhoneNumbers(), devSmsSendAliyunParam.getSignName(),
                devSmsSendAliyunParam.getTemplateCode(), devSmsSendAliyunParam.getTemplateParam());
        DevSms devSms = new DevSms();
        BeanUtil.copyProperties(devSmsSendAliyunParam, devSms);
        devSms.setSignName(ObjectUtil.isNotEmpty(devSms.getSignName())?devSms.getSignName():DevSmsAliyunUtil.getDefaultSignName());
        devSms.setEngine(DevSmsEngineTypeEnum.ALIYUN.getValue());
        devSms.setReceiptInfo(receiptInfo);
        this.save(devSms);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void sendTencent(DevSmsSendTencentParam smsSendTencentParam) {
        validPhone(smsSendTencentParam.getPhoneNumbers());
        String receiptInfo =DevSmsTencentUtil.sendSms(smsSendTencentParam.getPhoneNumbers(),
                smsSendTencentParam.getSignName(), smsSendTencentParam.getTemplateCode(), smsSendTencentParam.getTemplateParam());
        DevSms devSms = new DevSms();
        BeanUtil.copyProperties(smsSendTencentParam, devSms);
        devSms.setSignName(ObjectUtil.isNotEmpty(devSms.getSignName())?devSms.getSignName():DevSmsTencentUtil.getDefaultSignName());
        devSms.setEngine(DevSmsEngineTypeEnum.TENCENT.getValue());
        devSms.setReceiptInfo(receiptInfo);
        this.save(devSms);
    }

    @Override
    public void sendXiaonuo(DevSmsSendXiaonuoParam devSmsSendXiaonuoParam) {
        validPhone(devSmsSendXiaonuoParam.getPhoneNumbers());
        String receiptInfo;
        if(ObjectUtil.isEmpty(devSmsSendXiaonuoParam.getTemplateCode())) {
            receiptInfo = DevSmsXiaonuoUtil.sendSms(devSmsSendXiaonuoParam.getPhoneNumbers(), devSmsSendXiaonuoParam.getSignName(),
                    devSmsSendXiaonuoParam.getMessage());
        } else {
            LinkedHashMap<String, String> paramMap = new LinkedHashMap<>();
            JSONUtil.parseObj(devSmsSendXiaonuoParam.getTemplateParam()).forEach((k, v) -> paramMap.put(k, Convert.toStr(v)));
            receiptInfo = DevSmsXiaonuoUtil.sendSms(devSmsSendXiaonuoParam.getPhoneNumbers(), devSmsSendXiaonuoParam.getSignName(),
                    devSmsSendXiaonuoParam.getTemplateCode(), paramMap);
        }
        DevSms devSms = new DevSms();
        BeanUtil.copyProperties(devSmsSendXiaonuoParam, devSms);
        devSms.setSignName(ObjectUtil.isNotEmpty(devSms.getSignName())?devSms.getSignName():DevSmsXiaonuoUtil.getDefaultSignName());
        devSms.setEngine(DevSmsEngineTypeEnum.XIAONUO.getValue());
        devSms.setReceiptInfo(receiptInfo);
        this.save(devSms);
    }

    @Override
    public Page<DevSms> page(DevSmsPageParam devSmsPageParam) {
        QueryWrapper<DevSms> queryWrapper = new QueryWrapper<DevSms>().checkSqlInjection();
        if(ObjectUtil.isNotEmpty(devSmsPageParam.getEngine())) {
            queryWrapper.lambda().eq(DevSms::getEngine, devSmsPageParam.getEngine());
        }
        if(ObjectUtil.isNotEmpty(devSmsPageParam.getSearchKey())) {
            queryWrapper.lambda().like(DevSms::getPhoneNumbers, devSmsPageParam.getSearchKey());
        }
        if(ObjectUtil.isAllNotEmpty(devSmsPageParam.getSortField(), devSmsPageParam.getSortOrder())) {
            CommonSortOrderEnum.validate(devSmsPageParam.getSortOrder());
            queryWrapper.orderBy(true, devSmsPageParam.getSortOrder().equals(CommonSortOrderEnum.ASC.getValue()),
                    StrUtil.toUnderlineCase(devSmsPageParam.getSortField()));
        }
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(List<DevSmsIdParam> devSmsIdParamList) {
        this.removeByIds(CollStreamUtil.toList(devSmsIdParamList, DevSmsIdParam::getId));
    }

    @Override
    public DevSms detail(DevSmsIdParam devSmsIdParam) {
        return this.queryEntity(devSmsIdParam.getId());
    }

    @Override
    public DevSms queryEntity(String id) {
        DevSms devSms = this.getById(id);
        if(ObjectUtil.isEmpty(devSms)) {
            throw new CommonException("短信不存在，id值为：{}", id);
        }
        return devSms;
    }

    /**
     * 校验手机格式
     *
     * @author xuyuxiang
     * @date 2022/8/15 13:32
     **/
    private void validPhone(String phones) {
        StrUtil.split(phones, StrUtil.COMMA).forEach(phone -> {
            if(!PhoneUtil.isMobile(phone)) {
                throw new CommonException("手机号码：{}格式错误", phone);
            }
        });
    }
}
