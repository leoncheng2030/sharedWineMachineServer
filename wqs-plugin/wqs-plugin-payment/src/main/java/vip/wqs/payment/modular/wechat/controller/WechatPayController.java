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
package vip.wqs.payment.modular.wechat.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vip.wqs.common.annotation.CommonLog;
import vip.wqs.common.pojo.CommonResult;
import vip.wqs.payment.api.param.WechatPayCreateParam;
import vip.wqs.payment.api.param.WechatPayQueryParam;
import vip.wqs.payment.api.param.WechatRefundParam;
import vip.wqs.payment.api.result.WechatPayCreateResult;
import vip.wqs.payment.api.result.WechatPayQueryResult;
import vip.wqs.payment.api.result.WechatRefundResult;
import vip.wqs.payment.modular.wechat.service.WechatPayService;

/**
 * 微信支付控制器
 *
 * @author wqs
 * @date 2025/01/30 16:45
 */
@Tag(name = "微信支付控制器")
@ApiSupport(author = "WQS_TEAM", order = 18)
@RestController
@Validated
@Slf4j
public class WechatPayController {

    @Resource
    private WechatPayService wechatPayService;

    /**
     * 创建微信支付订单
     *
     * @author wqs
     * @date 2025/01/30
     */
    @ApiOperationSupport(order = 1)
    @Operation(summary = "创建微信支付订单")
    @CommonLog("创建微信支付订单")
    @SaCheckPermission("/payment/wechat/create")
    @PostMapping("/payment/wechat/create")
    public CommonResult<WechatPayCreateResult> createPayOrder(@RequestBody @Valid WechatPayCreateParam wechatPayCreateParam) {
        return CommonResult.data(wechatPayService.createPayOrder(wechatPayCreateParam));
    }

    /**
     * 查询微信支付订单
     *
     * @author wqs
     * @date 2025/01/30
     */
    @ApiOperationSupport(order = 2)
    @Operation(summary = "查询微信支付订单")
    @CommonLog("查询微信支付订单")
    @SaCheckPermission("/payment/wechat/query")
    @PostMapping("/payment/wechat/query")
    public CommonResult<WechatPayQueryResult> queryPayOrder(@RequestBody @Valid WechatPayQueryParam wechatPayQueryParam) {
        return CommonResult.data(wechatPayService.queryPayOrder(wechatPayQueryParam));
    }

    /**
     * 申请微信退款
     *
     * @author wqs
     * @date 2025/01/30
     */
    @ApiOperationSupport(order = 3)
    @Operation(summary = "申请微信退款")
    @CommonLog("申请微信退款")
    @SaCheckPermission("/payment/wechat/refund")
    @PostMapping("/payment/wechat/refund")
    public CommonResult<WechatRefundResult> refundOrder(@RequestBody @Valid WechatRefundParam wechatRefundParam) {
        return CommonResult.data(wechatPayService.refundOrder(wechatRefundParam));
    }

    /**
     * 查询微信退款
     *
     * @author wqs
     * @date 2025/01/30
     */
    @ApiOperationSupport(order = 4)
    @Operation(summary = "查询微信退款")
    @CommonLog("查询微信退款")
    @SaCheckPermission("/payment/wechat/refund/query")
    @GetMapping("/payment/wechat/refund/query")
    public CommonResult<WechatRefundResult> queryRefund(@RequestParam String refundId) {
        return CommonResult.data(wechatPayService.queryRefund(refundId));
    }

    /**
     * 关闭微信支付订单
     *
     * @author wqs
     * @date 2025/01/30
     */
    @ApiOperationSupport(order = 5)
    @Operation(summary = "关闭微信支付订单")
    @CommonLog("关闭微信支付订单")
    @SaCheckPermission("/payment/wechat/close")
    @PostMapping("/payment/wechat/close")
    public CommonResult<Boolean> closeOrder(@RequestParam String outTradeNo) {
        return CommonResult.data(wechatPayService.closeOrder(outTradeNo));
    }

    /**
     * 微信支付回调通知
     *
     * @author wqs
     * @date 2025/01/30
     */
    @ApiOperationSupport(order = 6)
    @Operation(summary = "微信支付回调通知")
    @PostMapping("/payment/wechat/notify")
    public String payNotify(HttpServletRequest request, @RequestBody String requestBody) {
        try {
            log.info("收到微信支付回调通知");
            
            // 获取回调头信息
            String signature = request.getHeader("Wechatpay-Signature");
            String timestamp = request.getHeader("Wechatpay-Timestamp");
            String nonce = request.getHeader("Wechatpay-Nonce");
            String serial = request.getHeader("Wechatpay-Serial");
            
            // 调用统一的支付回调处理方法
            String response = wechatPayService.handlePayNotify(requestBody, signature, timestamp, nonce);
            
            log.info("微信支付回调处理完成");
            return response;
        } catch (Exception e) {
            log.error("处理微信支付回调失败：{}", e.getMessage(), e);
            return "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[" + e.getMessage() + "]]></return_msg></xml>";
        }
    }

    /**
     * 微信退款回调通知
     *
     * @author wqs
     * @date 2025/01/30
     */
    @ApiOperationSupport(order = 7)
    @Operation(summary = "微信退款回调通知")
    @PostMapping("/payment/wechat/refund/notify")
    public String refundNotify(HttpServletRequest request, @RequestBody String requestBody) {
        try {
            log.info("收到微信退款回调通知");
            
            // 获取回调头信息
            String signature = request.getHeader("Wechatpay-Signature");
            String timestamp = request.getHeader("Wechatpay-Timestamp");
            String nonce = request.getHeader("Wechatpay-Nonce");
            String serial = request.getHeader("Wechatpay-Serial");
            
            // 调用统一的退款回调处理方法
            String response = wechatPayService.handleRefundNotify(requestBody, signature, timestamp, nonce);
            
            log.info("微信退款回调处理完成");
            return response;
        } catch (Exception e) {
            log.error("处理微信退款回调失败：{}", e.getMessage(), e);
            return "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[" + e.getMessage() + "]]></return_msg></xml>";
        }
    }
} 