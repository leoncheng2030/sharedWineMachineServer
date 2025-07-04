/*
 * Copyright [2022] [https://www.wqs.vip]
 *
 * WQS采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改WQS源码头部的版权声明。
 * 3.本项目代码可免费商业使用，商业使用请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://www.wqs.vip
 * 5.不可二次分发开源参与同类竞品，如有想法可联系团队wqs@wqs.vip商议合作。
 * 6.若您的项目无法满足以上几点，需要更多功能代码，获取WQS商业授权许可，请在官网购买授权，地址为 https://www.wqs.vip
 */
package vip.wqs.payment.modular.wechat.service;

import vip.wqs.payment.modular.wechat.param.WechatPayCreateParam;
import vip.wqs.payment.modular.wechat.param.WechatPayQueryParam;
import vip.wqs.payment.modular.wechat.param.WechatRefundParam;
import vip.wqs.payment.modular.wechat.result.WechatPayCreateResult;
import vip.wqs.payment.modular.wechat.result.WechatPayQueryResult;
import vip.wqs.payment.modular.wechat.result.WechatRefundResult;

/**
 * 微信支付服务接口
 *
 * @author wqs
 * @date 2025/01/30 16:30
 **/
public interface WechatPayService {

    /**
     * 创建支付订单（统一下单）
     *
     * @param wechatPayCreateParam 支付创建参数
     * @return 支付创建结果
     */
    WechatPayCreateResult createPayOrder(WechatPayCreateParam wechatPayCreateParam);

    /**
     * 查询支付订单
     *
     * @param wechatPayQueryParam 支付查询参数
     * @return 支付查询结果
     */
    WechatPayQueryResult queryPayOrder(WechatPayQueryParam wechatPayQueryParam);

    /**
     * 申请退款
     *
     * @param wechatRefundParam 退款参数
     * @return 退款结果
     */
    WechatRefundResult refundOrder(WechatRefundParam wechatRefundParam);

    /**
     * 查询退款
     *
     * @param refundId 退款单号
     * @return 退款查询结果
     */
    WechatRefundResult queryRefund(String refundId);

    /**
     * 关闭订单
     *
     * @param outTradeNo 商户订单号
     * @return 是否成功
     */
    Boolean closeOrder(String outTradeNo);

    /**
     * 验证支付回调
     *
     * @param requestBody 回调请求体
     * @param signature 签名
     * @param timestamp 时间戳
     * @param nonce 随机数
     * @return 回调数据
     */
    String verifyNotify(String requestBody, String signature, String timestamp, String nonce);

    /**
     * 验证退款回调
     *
     * @param requestBody 回调请求体
     * @param signature 签名
     * @param timestamp 时间戳
     * @param nonce 随机数
     * @return 回调数据
     */
    String verifyRefundNotify(String requestBody, String signature, String timestamp, String nonce);
} 