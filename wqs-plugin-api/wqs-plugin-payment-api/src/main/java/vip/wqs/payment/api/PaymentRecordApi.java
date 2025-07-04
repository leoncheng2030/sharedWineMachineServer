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
package vip.wqs.payment.api;

import cn.hutool.json.JSONObject;

/**
 * 支付记录Api
 *
 * @author WQS_TEAM
 * @date 2025/01/30 支付管理模块开发
 **/
public interface PaymentRecordApi {

    /**
     * 创建支付记录
     *
     * @param orderId 订单ID
     * @param orderNo 订单号
     * @param userId 用户ID
     * @param payType 支付方式
     * @param payAmount 支付金额
     * @return 支付记录信息
     * @author WQS_TEAM
     * @date 2025/01/30
     */
    JSONObject createPaymentRecord(String orderId, String orderNo, String userId, String payType, String payAmount);

    /**
     * 根据支付单号获取支付记录
     *
     * @param paymentNo 支付单号
     * @return 支付记录信息
     * @author WQS_TEAM
     * @date 2025/01/30
     */
    JSONObject getPaymentRecordByNo(String paymentNo);

    /**
     * 根据订单ID获取支付记录
     *
     * @param orderId 订单ID
     * @return 支付记录信息
     * @author WQS_TEAM
     * @date 2025/01/30
     */
    JSONObject getPaymentRecordByOrderId(String orderId);

    /**
     * 更新支付状态
     *
     * @param paymentNo 支付单号
     * @param payStatus 支付状态
     * @param transactionId 第三方交易流水号
     * @param callbackContent 回调内容
     * @return 是否成功
     * @author WQS_TEAM
     * @date 2025/01/30
     */
    Boolean updatePaymentStatus(String paymentNo, String payStatus, String transactionId, String callbackContent);

    /**
     * 处理退款
     *
     * @param paymentNo 支付单号
     * @param refundAmount 退款金额
     * @param refundReason 退款原因
     * @return 是否成功
     * @author WQS_TEAM
     * @date 2025/01/30
     */
    Boolean processRefund(String paymentNo, String refundAmount, String refundReason);

    /**
     * 检查支付记录是否存在
     *
     * @param paymentNo 支付单号
     * @return 是否存在
     * @author WQS_TEAM
     * @date 2025/01/30
     */
    Boolean existsPaymentRecord(String paymentNo);
} 