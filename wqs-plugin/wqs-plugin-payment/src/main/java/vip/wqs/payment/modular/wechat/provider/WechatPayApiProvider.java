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
package vip.wqs.payment.modular.wechat.provider;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vip.wqs.payment.api.WechatPayApi;
import vip.wqs.payment.api.param.WechatPayCreateParam;
import vip.wqs.payment.api.param.WechatPayQueryParam;
import vip.wqs.payment.api.param.WechatRefundParam;
import vip.wqs.payment.api.result.WechatPayCreateResult;
import vip.wqs.payment.api.result.WechatPayQueryResult;
import vip.wqs.payment.api.result.WechatRefundResult;
import vip.wqs.payment.modular.wechat.service.WechatPayService;

/**
 * 微信支付API实现
 *
 * @author AI Assistant
 * @date 2025/01/30
 */
@Slf4j
@Service
public class WechatPayApiProvider implements WechatPayApi {

    @Resource
    private WechatPayService wechatPayService;

    @Override
    public WechatPayCreateResult createPayOrder(WechatPayCreateParam param) {
        log.info("API调用：创建微信支付订单，订单号：{}", param.getOutTradeNo());
        
        // 直接调用服务，无需类型转换
        WechatPayCreateResult result = wechatPayService.createPayOrder(param);
        
        log.info("API调用成功：创建微信支付订单，prepayId：{}", result.getPrepayId());
        return result;
    }

    @Override
    public WechatPayQueryResult queryPayOrder(WechatPayQueryParam param) {
        log.info("API调用：查询微信支付订单，订单号：{}", param.getOutTradeNo());
        
        // 直接调用服务，无需类型转换
        WechatPayQueryResult result = wechatPayService.queryPayOrder(param);
        
        log.info("API调用成功：查询微信支付订单，状态：{}", result.getTradeState());
        return result;
    }

    @Override
    public WechatRefundResult refundOrder(WechatRefundParam param) {
        log.info("API调用：申请微信退款，订单号：{}", param.getOutTradeNo());
        
        // 直接调用服务，无需类型转换
        WechatRefundResult result = wechatPayService.refundOrder(param);
        
        log.info("API调用成功：申请微信退款，退款单号：{}", result.getOutRefundNo());
        return result;
    }

    @Override
    public WechatRefundResult queryRefund(String refundId) {
        log.info("API调用：查询微信退款，退款ID：{}", refundId);
        
        // 调用服务
        WechatRefundResult result = wechatPayService.queryRefund(refundId);
        
        log.info("API调用成功：查询微信退款，状态：{}", result.getStatus());
        return result;
    }

    @Override
    public Boolean closeOrder(String outTradeNo) {
        log.info("API调用：关闭微信支付订单，订单号：{}", outTradeNo);
        
        // 调用服务
        Boolean result = wechatPayService.closeOrder(outTradeNo);
        
        log.info("API调用成功：关闭微信支付订单，结果：{}", result);
        return result;
    }

    @Override
    public String verifyNotify(String requestBody, String signature, String timestamp, String nonce) {
        log.info("API调用：验证微信支付回调");
        
        // 调用服务
        String result = wechatPayService.verifyNotify(requestBody, signature, timestamp, nonce);
        
        log.info("API调用成功：验证微信支付回调");
        return result;
    }

    @Override
    public String verifyRefundNotify(String requestBody, String signature, String timestamp, String nonce) {
        log.info("API调用：验证微信退款回调");
        
        // 调用服务
        String result = wechatPayService.verifyRefundNotify(requestBody, signature, timestamp, nonce);
        
        log.info("API调用成功：验证微信退款回调");
        return result;
    }

    @Override
    public String handlePayNotify(String requestBody, String signature, String timestamp, String nonce) {
        log.info("API调用：处理微信支付回调业务逻辑");
        
        // 调用服务
        String result = wechatPayService.handlePayNotify(requestBody, signature, timestamp, nonce);
        
        log.info("API调用成功：处理微信支付回调业务逻辑");
        return result;
    }

    @Override
    public String handleRefundNotify(String requestBody, String signature, String timestamp, String nonce) {
        log.info("API调用：处理微信退款回调业务逻辑");
        
        // 调用服务
        String result = wechatPayService.handleRefundNotify(requestBody, signature, timestamp, nonce);
        
        log.info("API调用成功：处理微信退款回调业务逻辑");
        return result;
    }
}