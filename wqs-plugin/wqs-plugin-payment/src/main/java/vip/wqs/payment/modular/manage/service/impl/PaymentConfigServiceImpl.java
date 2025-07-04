/*
 * Copyright [2025] [https://www.wqs.vip]
 *
 * WQS采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改WQS源码头部的版权声明。
 * 3.本项目代码可免费商业使用，商业使用请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://www.wqs.vip
 * 5.若您的项目无法满足以上几点，需要更多功能代码，获取WQS商业授权许可，请联系团队获取授权。
 */
package vip.wqs.payment.modular.manage.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.wqs.common.exception.CommonException;
import vip.wqs.common.page.CommonPageRequest;
import vip.wqs.payment.modular.manage.entity.PaymentConfig;
import vip.wqs.payment.modular.manage.mapper.PaymentConfigMapper;
import vip.wqs.payment.modular.manage.param.PaymentConfigCreateParam;
import vip.wqs.payment.modular.manage.param.PaymentConfigEditParam;
import vip.wqs.payment.modular.manage.param.PaymentConfigIdParam;
import vip.wqs.payment.modular.manage.param.PaymentConfigPageParam;
import vip.wqs.payment.modular.manage.service.PaymentConfigService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 支付配置Service接口实现类
 *
 * @author wqs
 * @date 2025/01/30
 */
@Service
public class PaymentConfigServiceImpl extends ServiceImpl<PaymentConfigMapper, PaymentConfig> implements PaymentConfigService {

    @Override
    public Page<PaymentConfig> page(PaymentConfigPageParam paymentConfigPageParam) {
        QueryWrapper<PaymentConfig> queryWrapper = new QueryWrapper<>();
        
        // 条件查询
        if (StrUtil.isNotBlank(paymentConfigPageParam.getConfigName())) {
            queryWrapper.like("config_name", paymentConfigPageParam.getConfigName());
        }
        if (StrUtil.isNotBlank(paymentConfigPageParam.getPayType())) {
            queryWrapper.eq("pay_type", paymentConfigPageParam.getPayType());
        }
        if (StrUtil.isNotBlank(paymentConfigPageParam.getStatus())) {
            queryWrapper.eq("status", paymentConfigPageParam.getStatus());
        }
        
        // 按排序码升序，创建时间倒序排列
        queryWrapper.orderByAsc("sort_code").orderByDesc("create_time");
        
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String add(PaymentConfigCreateParam paymentConfigCreateParam) {
        PaymentConfig paymentConfig = BeanUtil.toBean(paymentConfigCreateParam, PaymentConfig.class);
        
        // 检查支付方式是否已存在
        checkPayTypeExists(paymentConfigCreateParam.getPayType(), null);
        
        // 设置默认状态
        if (StrUtil.isBlank(paymentConfig.getStatus())) {
            paymentConfig.setStatus("ENABLE");
        }
        
        // 设置默认排序码
        if (ObjectUtil.isNull(paymentConfig.getSortCode())) {
            paymentConfig.setSortCode(100);
        }
        
        this.save(paymentConfig);
        return paymentConfig.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void edit(PaymentConfigEditParam paymentConfigEditParam) {
        PaymentConfig paymentConfig = this.queryEntity(paymentConfigEditParam.getId());
        
        // 检查支付方式是否已存在（排除自己）
        checkPayTypeExists(paymentConfigEditParam.getPayType(), paymentConfigEditParam.getId());
        
        BeanUtil.copyProperties(paymentConfigEditParam, paymentConfig);
        this.updateById(paymentConfig);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(List<PaymentConfigIdParam> paymentConfigIdParamList) {
        List<String> paymentConfigIdList = paymentConfigIdParamList.stream()
                .map(PaymentConfigIdParam::getId).collect(Collectors.toList());
        this.removeByIds(paymentConfigIdList);
    }

    @Override
    public PaymentConfig detail(PaymentConfigIdParam paymentConfigIdParam) {
        return this.queryEntity(paymentConfigIdParam.getId());
    }

    @Override
    public PaymentConfig queryEntity(String id) {
        PaymentConfig paymentConfig = this.getById(id);
        if (ObjectUtil.isNull(paymentConfig)) {
            throw new CommonException("支付配置不存在，id值为：{}", id);
        }
        return paymentConfig;
    }

    @Override
    public PaymentConfig getByPayType(String payType) {
        QueryWrapper<PaymentConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pay_type", payType);
        queryWrapper.eq("status", "ENABLE");
        return this.getOne(queryWrapper);
    }

    @Override
    public List<PaymentConfig> getEnabledConfigs() {
        QueryWrapper<PaymentConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", "ENABLE");
        queryWrapper.orderByAsc("sort_code");
        return this.list(queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateStatus(PaymentConfigIdParam paymentConfigIdParam, String status) {
        PaymentConfig paymentConfig = this.queryEntity(paymentConfigIdParam.getId());
        paymentConfig.setStatus(status);
        this.updateById(paymentConfig);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(PaymentConfigIdParam paymentConfigIdParam) {
        this.removeById(paymentConfigIdParam.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void disable(PaymentConfigIdParam paymentConfigIdParam) {
        this.updateStatus(paymentConfigIdParam, "DISABLE");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void enable(PaymentConfigIdParam paymentConfigIdParam) {
        this.updateStatus(paymentConfigIdParam, "ENABLE");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchDelete(List<PaymentConfigIdParam> paymentConfigIdParamList) {
        this.delete(paymentConfigIdParamList);
    }

    @Override
    public List<PaymentConfig> selector(PaymentConfigPageParam paymentConfigPageParam) {
        QueryWrapper<PaymentConfig> queryWrapper = new QueryWrapper<>();
        
        // 条件查询
        if (StrUtil.isNotBlank(paymentConfigPageParam.getConfigName())) {
            queryWrapper.like("config_name", paymentConfigPageParam.getConfigName());
        }
        if (StrUtil.isNotBlank(paymentConfigPageParam.getPayType())) {
            queryWrapper.eq("pay_type", paymentConfigPageParam.getPayType());
        }
        if (StrUtil.isNotBlank(paymentConfigPageParam.getStatus())) {
            queryWrapper.eq("status", paymentConfigPageParam.getStatus());
        }
        
        // 按排序码升序排列
        queryWrapper.orderByAsc("sort_code");
        
        // 限制返回字段
        queryWrapper.select("id", "config_name", "pay_type", "status", "sort_code");
        
        return this.list(queryWrapper);
    }

    /**
     * 检查支付方式是否已存在
     *
     * @param payType 支付方式
     * @param excludeId 排除的ID
     */
    private void checkPayTypeExists(String payType, String excludeId) {
        QueryWrapper<PaymentConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pay_type", payType);
        if (StrUtil.isNotBlank(excludeId)) {
            queryWrapper.ne("id", excludeId);
        }
        
        long count = this.count(queryWrapper);
        if (count > 0) {
            throw new CommonException("支付方式已存在：{}", payType);
        }
    }
} 