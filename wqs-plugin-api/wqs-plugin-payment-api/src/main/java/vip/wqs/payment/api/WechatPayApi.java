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

import vip.wqs.payment.api.param.WechatPayCreateParam;
import vip.wqs.payment.api.param.WechatPayQueryParam;
import vip.wqs.payment.api.param.WechatRefundParam;
import vip.wqs.payment.api.result.WechatPayCreateResult;
import vip.wqs.payment.api.result.WechatPayQueryResult;
import vip.wqs.payment.api.result.WechatRefundResult;

/**
 * 微信支付API接口
 *
 * @author AI Assistant
 * @date 2025/01/30
 */
public interface WechatPayApi {

    /**
     * 创建微信支付订单
     *
     * @param param 支付创建参数
     * @return 支付创建结果
     */
    WechatPayCreateResult createPayOrder(WechatPayCreateParam param);

    /**
     * 查询微信支付订单
     *
     * @param param 支付查询参数
     * @return 支付查询结果
     */
    WechatPayQueryResult queryPayOrder(WechatPayQueryParam param);

    /**
     * 申请微信退款
     *
     * @param param 退款参数
     * @return 退款结果
     */
    WechatRefundResult refundOrder(WechatRefundParam param);

    /**
     * 查询微信退款
     *
     * @param refundId 退款单号
     * @return 退款查询结果
     */
    WechatRefundResult queryRefund(String refundId);

    /**
     * 关闭微信支付订单
     *
     * @param outTradeNo 商户订单号
     * @return 是否成功
     */
    Boolean closeOrder(String outTradeNo);

    /**
     * 验证微信支付回调
     *
     * @param requestBody 回调请求体
     * @param signature 签名
     * @param timestamp 时间戳
     * @param nonce 随机数
     * @return 回调数据
     */
    String verifyNotify(String requestBody, String signature, String timestamp, String nonce);

    /**
     * 验证微信退款回调
     *
     * @param requestBody 回调请求体
     * @param signature 签名
     * @param timestamp 时间戳
     * @param nonce 随机数
     * @return 回调数据
     */
    String verifyRefundNotify(String requestBody, String signature, String timestamp, String nonce);

    /**
     * 处理微信支付成功回调业务逻辑
     *
     * @param requestBody 回调请求体
     * @param signature 签名
     * @param timestamp 时间戳
     * @param nonce 随机数
     * @return 处理结果（返回给微信的响应）
     */
    String handlePayNotify(String requestBody, String signature, String timestamp, String nonce);

    /**
     * 处理微信退款成功回调业务逻辑
     *
     * @param requestBody 回调请求体
     * @param signature 签名
     * @param timestamp 时间戳
     * @param nonce 随机数
     * @return 处理结果（返回给微信的响应）
     */
    String handleRefundNotify(String requestBody, String signature, String timestamp, String nonce);
}