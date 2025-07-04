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
package vip.wqs.payment.modular.manage.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import vip.wqs.payment.modular.manage.entity.PaymentConfig;
import vip.wqs.payment.modular.manage.param.PaymentConfigCreateParam;
import vip.wqs.payment.modular.manage.param.PaymentConfigEditParam;
import vip.wqs.payment.modular.manage.param.PaymentConfigIdParam;
import vip.wqs.payment.modular.manage.param.PaymentConfigPageParam;

import java.util.List;

/**
 * 支付配置Service接口
 *
 * @author wqs
 * @date 2025/01/30
 */
public interface PaymentConfigService extends IService<PaymentConfig> {

    /**
     * 获取支付配置分页
     *
     * @param paymentConfigPageParam 分页参数
     * @return 分页结果
     * @author wqs
     * @date 2025/01/30
     */
    Page<PaymentConfig> page(PaymentConfigPageParam paymentConfigPageParam);

    /**
     * 添加支付配置
     *
     * @param paymentConfigCreateParam 创建参数
     * @return 配置ID
     * @author wqs
     * @date 2025/01/30
     */
    String add(PaymentConfigCreateParam paymentConfigCreateParam);

    /**
     * 编辑支付配置
     *
     * @param paymentConfigEditParam 编辑参数
     * @author wqs
     * @date 2025/01/30
     */
    void edit(PaymentConfigEditParam paymentConfigEditParam);

    /**
     * 删除支付配置
     *
     * @param paymentConfigIdParamList ID参数列表
     * @author wqs
     * @date 2025/01/30
     */
    void delete(List<PaymentConfigIdParam> paymentConfigIdParamList);

    /**
     * 获取支付配置详情
     *
     * @param paymentConfigIdParam ID参数
     * @return 配置详情
     * @author wqs
     * @date 2025/01/30
     */
    PaymentConfig detail(PaymentConfigIdParam paymentConfigIdParam);

    /**
     * 获取支付配置详情
     *
     * @param paymentConfigIdParam ID参数
     * @return 配置详情
     * @author wqs
     * @date 2025/01/30
     */
    PaymentConfig queryEntity(String id);

    /**
     * 根据支付方式获取配置
     *
     * @param payType 支付方式
     * @return 支付配置
     * @author wqs
     * @date 2025/01/30
     */
    PaymentConfig getByPayType(String payType);

    /**
     * 获取所有启用的支付配置
     *
     * @return 支付配置列表
     * @author wqs
     * @date 2025/01/30
     */
    List<PaymentConfig> getEnabledConfigs();

    /**
     * 启用/禁用支付配置
     *
     * @param paymentConfigIdParam ID参数
     * @param status 状态
     * @author wqs
     * @date 2025/01/30
     */
    void updateStatus(PaymentConfigIdParam paymentConfigIdParam, String status);

    /**
     * 删除支付配置（单个）
     *
     * @param paymentConfigIdParam ID参数
     * @author wqs
     * @date 2025/01/30
     */
    void delete(PaymentConfigIdParam paymentConfigIdParam);

    /**
     * 禁用支付配置
     *
     * @param paymentConfigIdParam ID参数
     * @author wqs
     * @date 2025/01/30
     */
    void disable(PaymentConfigIdParam paymentConfigIdParam);

    /**
     * 启用支付配置
     *
     * @param paymentConfigIdParam ID参数
     * @author wqs
     * @date 2025/01/30
     */
    void enable(PaymentConfigIdParam paymentConfigIdParam);

    /**
     * 批量删除支付配置
     *
     * @param paymentConfigIdParamList ID参数列表
     * @author wqs
     * @date 2025/01/30
     */
    void batchDelete(List<PaymentConfigIdParam> paymentConfigIdParamList);

    /**
     * 支付配置选择器
     *
     * @param paymentConfigPageParam 查询参数
     * @return 配置列表
     * @author wqs
     * @date 2025/01/30
     */
    List<PaymentConfig> selector(PaymentConfigPageParam paymentConfigPageParam);
} 