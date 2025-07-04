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
import vip.wqs.payment.modular.manage.entity.PaymentRecord;
import vip.wqs.payment.modular.manage.param.PaymentRecordIdParam;
import vip.wqs.payment.modular.manage.param.PaymentRecordPageParam;

import java.math.BigDecimal;

/**
 * 支付记录服务接口
 *
 * @author wqs
 * @date 2025/01/30 17:05
 **/
public interface PaymentRecordService extends IService<PaymentRecord> {

    /**
     * 获取支付记录分页
     *
     * @param paymentRecordPageParam 分页参数
     * @return 分页结果
     * @author wqs
     * @date 2025/01/30
     */
    Page<PaymentRecord> page(PaymentRecordPageParam paymentRecordPageParam);

    /**
     * 获取支付记录详情
     *
     * @param paymentRecordIdParam ID参数
     * @return 支付记录详情
     * @author wqs
     * @date 2025/01/30
     */
    PaymentRecord detail(PaymentRecordIdParam paymentRecordIdParam);

    /**
     * 获取支付记录详情
     *
     * @param id 主键ID
     * @return 支付记录详情
     * @author wqs
     * @date 2025/01/30
     */
    PaymentRecord queryEntity(String id);

    /**
     * 创建支付记录
     *
     * @param orderId 订单ID
     * @param orderNo 订单号
     * @param userId 用户ID
     * @param payType 支付方式
     * @param payAmount 支付金额
     * @param clientIp 客户端IP
     * @param body 商品描述
     * @return 支付记录ID
     */
    String createPaymentRecord(String orderId, String orderNo, String userId, 
                              String payType, BigDecimal payAmount, String clientIp, String body);

    /**
     * 根据订单ID获取支付记录
     *
     * @param orderId 订单ID
     * @return 支付记录
     */
    PaymentRecord getByOrderId(String orderId);

    /**
     * 根据支付单号获取支付记录
     *
     * @param paymentNo 支付单号
     * @return 支付记录
     */
    PaymentRecord getByPaymentNo(String paymentNo);

    /**
     * 更新支付状态为成功
     *
     * @param paymentId 支付记录ID
     * @param transactionId 第三方交易流水号
     * @param actualAmount 实际支付金额
     * @param callbackContent 回调内容
     * @return 是否成功
     */
    Boolean updatePaymentSuccess(String paymentId, String transactionId, 
                                BigDecimal actualAmount, String callbackContent);

    /**
     * 更新支付状态为失败
     *
     * @param paymentId 支付记录ID
     * @param failReason 失败原因
     * @return 是否成功
     */
    Boolean updatePaymentFailed(String paymentId, String failReason);

    /**
     * 更新预支付ID
     *
     * @param paymentId 支付记录ID
     * @param prepayId 预支付ID
     * @return 是否成功
     */
    Boolean updatePrepayId(String paymentId, String prepayId);

    /**
     * 申请退款
     *
     * @param paymentId 支付记录ID
     * @param refundAmount 退款金额
     * @param refundReason 退款原因
     * @return 是否成功
     */
    Boolean applyRefund(String paymentId, BigDecimal refundAmount, String refundReason);

    /**
     * 更新退款状态
     *
     * @param paymentId 支付记录ID
     * @param refundNo 退款单号
     * @param refundAmount 退款金额
     * @return 是否成功
     */
    Boolean updateRefundStatus(String paymentId, String refundNo, BigDecimal refundAmount);

    /**
     * 取消支付
     *
     * @param paymentId 支付记录ID
     * @return 是否成功
     */
    Boolean cancelPayment(String paymentId);

    /**
     * 设置支付过期
     *
     * @param paymentId 支付记录ID
     * @return 是否成功
     */
    Boolean expirePayment(String paymentId);
} 