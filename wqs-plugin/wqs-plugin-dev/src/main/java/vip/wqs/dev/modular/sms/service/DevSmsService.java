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
package vip.wqs.dev.modular.sms.service;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import vip.wqs.dev.modular.sms.entity.DevSms;
import vip.wqs.dev.modular.sms.param.*;

import java.util.List;

/**
 * 短信Service接口
 *
 * @author xuyuxiang
 * @date 2022/2/23 18:27
 **/
public interface DevSmsService extends IService<DevSms> {

    /**
     * 动态发送短信
     *
     * @param engine 发送引擎
     * @param phoneNumbers 手机号
     * @param templateCodeOrId 模板id或编码
     * @param templateParam 发送参数
     * @author xuyuxiang
     * @date 2022/2/7 22:29
     */
    void sendDynamic(String engine, String phoneNumbers, String templateCodeOrId, JSONObject templateParam);

    /**
     * 动态发送短信
     *
     * @author xuyuxiang
     * @date 2022/6/21 18:37
     **/
    void sendDynamic(DevSmsSendDynamicParam devSmsSendDynamicParam);

    /**
     * 发送短信——阿里云
     *
     * @author xuyuxiang
     * @date 2022/6/21 18:37
     **/
    void sendAliyun(DevSmsSendAliyunParam devSmsSendAliyunParam);

    /**
     * 发送短信——腾讯云
     *
     * @author xuyuxiang
     * @date 2022/6/21 18:37
     **/
    void sendTencent(DevSmsSendTencentParam devSmsSendTencentParam);

    /**
     * 发送短信——小诺短信
     *
     * @author xuyuxiang
     * @date 2024/1/25 14:08
     **/
    void sendXiaonuo(DevSmsSendXiaonuoParam devSmsSendXiaonuoParam);

    /**
     * 获取短信分页
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:08
     */
    Page<DevSms> page(DevSmsPageParam devSmsPageParam);

    /**
     * 删除短信
     *
     * @author xuyuxiang
     * @date 2022/8/4 10:36
     **/
    void delete(List<DevSmsIdParam> devSmsIdParamList);

    /**
     * 获取短信详情
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:08
     */
    DevSms detail(DevSmsIdParam devSmsIdParam);

    /**
     * 获取短信详情
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:08
     */
    DevSms queryEntity(String id);
}
