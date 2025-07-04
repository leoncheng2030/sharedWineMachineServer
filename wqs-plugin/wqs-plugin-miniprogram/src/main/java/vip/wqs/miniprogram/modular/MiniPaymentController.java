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
package vip.wqs.miniprogram.modular;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import vip.wqs.auth.core.annotation.SaClientCheckLogin;
import vip.wqs.auth.core.util.StpClientUtil;
import vip.wqs.common.annotation.CommonLog;
import vip.wqs.common.pojo.CommonResult;
import vip.wqs.payment.api.WechatPayApi;
import vip.wqs.payment.api.param.WechatPayCreateParam;
import vip.wqs.payment.api.param.WechatPayQueryParam;
import vip.wqs.payment.api.param.WechatRefundParam;
import vip.wqs.payment.api.result.WechatPayCreateResult;
import vip.wqs.payment.api.result.WechatPayQueryResult;
import vip.wqs.payment.api.result.WechatRefundResult;

/**
 * 小程序支付控制器
 *
 * @author AI Assistant
 * @date 2025/01/30
 */
@Tag(name = "小程序支付控制器")
@RestController
@Validated
@Slf4j
public class MiniPaymentController {

    @Resource
    private WechatPayApi wechatPayApi;

    /**
     * 创建微信支付订单（小程序专用）
     *
     * @author AI Assistant
     * @date 2025/01/30
     */
    @Operation(summary = "创建微信支付订单")
    @CommonLog("小程序创建微信支付订单")
    @SaClientCheckLogin
    @PostMapping("/miniprogram/payment/wechat/create")
    public CommonResult<WechatPayCreateResult> createPayOrder(@RequestBody @Valid WechatPayCreateParam param) {
        try {
            log.info("小程序用户创建支付订单，用户ID：{}，订单号：{}", StpClientUtil.getLoginIdAsString(), param.getOutTradeNo());
            
            // 设置支付类型为小程序支付
            param.setPayType("WECHAT_MINI");
            
            // 通过API调用支付服务
            WechatPayCreateResult result = wechatPayApi.createPayOrder(param);
            
            log.info("小程序支付订单创建成功，用户ID：{}，订单号：{}", StpClientUtil.getLoginIdAsString(), param.getOutTradeNo());
            return CommonResult.data(result);
            
        } catch (Exception e) {
            log.error("小程序创建支付订单失败，用户ID：{}，订单号：{}", StpClientUtil.getLoginIdAsString(), param.getOutTradeNo(), e);
            return CommonResult.error("创建支付订单失败：" + e.getMessage());
        }
    }

    /**
     * 查询微信支付订单状态（小程序专用）
     *
     * @author AI Assistant
     * @date 2025/01/30
     */
    @Operation(summary = "查询微信支付订单状态")
    @CommonLog("小程序查询微信支付订单")
    @SaClientCheckLogin
    @PostMapping("/miniprogram/payment/wechat/query")
    public CommonResult<WechatPayQueryResult> queryPayOrder(@RequestBody @Valid WechatPayQueryParam param) {
        try {
            log.info("小程序用户查询支付订单，用户ID：{}，订单号：{}", StpClientUtil.getLoginIdAsString(), param.getOutTradeNo());
            
            // 设置支付类型
            param.setPayType("WECHAT_MINI");
            
            // 通过API调用支付服务
            WechatPayQueryResult result = wechatPayApi.queryPayOrder(param);
            
            log.info("小程序查询支付订单成功，用户ID：{}，支付状态：{}", StpClientUtil.getLoginIdAsString(), result.getTradeState());
            return CommonResult.data(result);
            
        } catch (Exception e) {
            log.error("小程序查询支付订单失败，用户ID：{}，订单号：{}", StpClientUtil.getLoginIdAsString(), param.getOutTradeNo(), e);
            return CommonResult.error("查询支付订单失败：" + e.getMessage());
        }
    }

    /**
     * 申请微信退款（小程序专用）
     *
     * @author AI Assistant
     * @date 2025/01/30
     */
    @Operation(summary = "申请微信退款")
    @CommonLog("小程序申请微信退款")
    @SaClientCheckLogin
    @PostMapping("/miniprogram/payment/wechat/refund")
    public CommonResult<WechatRefundResult> refundOrder(@RequestBody @Valid WechatRefundParam param) {
        try {
            log.info("小程序用户申请退款，用户ID：{}，订单号：{}", StpClientUtil.getLoginIdAsString(), param.getOutTradeNo());
            
            // 设置支付类型
            param.setPayType("WECHAT_MINI");
            
            // 通过API调用支付服务
            WechatRefundResult result = wechatPayApi.refundOrder(param);
            
                        log.info("小程序申请退款成功，用户ID：{}，退款单号：{}", StpClientUtil.getLoginIdAsString(), result.getOutRefundNo());
            return CommonResult.data(result);

        } catch (Exception e) {
            log.error("小程序申请退款失败，用户ID：{}，订单号：{}", StpClientUtil.getLoginIdAsString(), param.getOutTradeNo(), e);
            return CommonResult.error("申请退款失败：" + e.getMessage());
        }
    }

    /**
     * 查询微信退款状态（小程序专用）
     *
     * @author AI Assistant
     * @date 2025/01/30
     */
    @Operation(summary = "查询微信退款状态")
    @CommonLog("小程序查询微信退款")
    @SaClientCheckLogin
    @GetMapping("/miniprogram/payment/wechat/refund/query")
    public CommonResult<WechatRefundResult> queryRefund(@RequestParam String refundId) {
        try {
            log.info("小程序用户查询退款，用户ID：{}，退款ID：{}", StpClientUtil.getLoginIdAsString(), refundId);
            
            // 通过API调用支付服务
            WechatRefundResult result = wechatPayApi.queryRefund(refundId);
            
            log.info("小程序查询退款成功，用户ID：{}，退款状态：{}", StpClientUtil.getLoginIdAsString(), result.getStatus());
            return CommonResult.data(result);

        } catch (Exception e) {
            log.error("小程序查询退款失败，用户ID：{}，退款ID：{}", StpClientUtil.getLoginIdAsString(), refundId, e);
            return CommonResult.error("查询退款失败：" + e.getMessage());
        }
    }

    /**
     * 关闭微信支付订单（小程序专用）
     *
     * @author AI Assistant
     * @date 2025/01/30
     */
    @Operation(summary = "关闭微信支付订单")
    @CommonLog("小程序关闭微信支付订单")
    @SaClientCheckLogin
    @PostMapping("/miniprogram/payment/wechat/close")
    public CommonResult<Boolean> closeOrder(@RequestParam String outTradeNo) {
        try {
            log.info("小程序用户关闭支付订单，用户ID：{}，订单号：{}", StpClientUtil.getLoginIdAsString(), outTradeNo);
            
            // 通过API调用支付服务
            Boolean result = wechatPayApi.closeOrder(outTradeNo);
            
            log.info("小程序关闭支付订单成功，用户ID：{}，订单号：{}", StpClientUtil.getLoginIdAsString(), outTradeNo);
            return CommonResult.data(result);
            
        } catch (Exception e) {
            log.error("小程序关闭支付订单失败，用户ID：{}，订单号：{}", StpClientUtil.getLoginIdAsString(), outTradeNo, e);
            return CommonResult.error("关闭支付订单失败：" + e.getMessage());
        }
    }
}