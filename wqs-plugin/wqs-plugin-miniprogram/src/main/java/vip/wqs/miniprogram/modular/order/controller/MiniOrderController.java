/*
 * Copyright [2025] [https://www.wqs.vip]
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
package vip.wqs.miniprogram.modular.order.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vip.wqs.auth.core.annotation.SaClientCheckLogin;
import vip.wqs.auth.core.util.StpClientUtil;
import vip.wqs.common.pojo.CommonResult;
import vip.wqs.miniprogram.modular.order.param.MiniOrderCancelParam;
import vip.wqs.miniprogram.modular.order.param.MiniOrderCreateParam;
import vip.wqs.miniprogram.modular.order.param.MiniOrderPageParam;
import vip.wqs.miniprogram.modular.order.provider.MiniOrderApiProvider;
import vip.wqs.order.pojo.WineOrderPojo;
import vip.wqs.order.pojo.WineOrderStatisticsPojo;

import java.util.List;

/**
 * 小程序订单控制器
 *
 * @author wqs
 * &#064;date  2025/01/30
 */
@RestController
@RequestMapping("/miniprogram/order")
@Tag(name = "小程序订单接口")
@Validated
public class MiniOrderController {

    private static final Logger log = LoggerFactory.getLogger(MiniOrderController.class);

    @Resource
    private MiniOrderApiProvider miniOrderApiProvider;

    /**
     * 获取用户订单分页列表
     */
    @GetMapping("/page")
    @Operation(summary = "获取用户订单分页列表")
     @SaClientCheckLogin  // 暂时注释掉用于测试
    public CommonResult<Page<WineOrderPojo>> getOrderPage(@Valid MiniOrderPageParam param) {
        try {
             String userId = StpClientUtil.getLoginIdAsString();
            Page<WineOrderPojo> result = miniOrderApiProvider.getOrderPage(userId, param);
            return CommonResult.data(result);
        } catch (Exception e) {
            log.error("获取用户订单分页列表失败", e);
            return CommonResult.error("获取订单列表失败");
        }
    }

    /**
     * 获取订单详情
     */
    @GetMapping("/detail")
    @Operation(summary = "获取订单详情")
    @SaClientCheckLogin
    public CommonResult<WineOrderPojo> getOrderDetail(@RequestParam @NotBlank(message = "订单ID不能为空") String orderId) {
        try {
            String userId = StpClientUtil.getLoginIdAsString();
            WineOrderPojo result = miniOrderApiProvider.getOrderDetail(userId, orderId);
            return CommonResult.data(result);
        } catch (Exception e) {
            log.error("获取订单详情失败，订单ID：{}", orderId, e);
            return CommonResult.error("获取订单详情失败");
        }
    }

    /**
     * 根据订单号获取订单详情
     */
    @GetMapping("/orderNo/{orderNo}")
    @Operation(summary = "根据订单号获取订单详情")
    @SaClientCheckLogin
    public CommonResult<WineOrderPojo> getOrderByOrderNo(@PathVariable @NotBlank(message = "订单号不能为空") String orderNo) {
        try {
            String userId = StpClientUtil.getLoginIdAsString();
            WineOrderPojo result = miniOrderApiProvider.getOrderByOrderNo(userId, orderNo);
            return CommonResult.data(result);
        } catch (Exception e) {
            log.error("根据订单号获取订单详情失败，订单号：{}", orderNo, e);
            return CommonResult.error("获取订单详情失败");
        }
    }

    /**
     * 创建订单
     */
    @PostMapping("/create")
    @Operation(summary = "创建订单")
    @SaClientCheckLogin
    public CommonResult<String> createOrder(@RequestBody @Valid MiniOrderCreateParam param) {
        try {
            String userId = StpClientUtil.getLoginIdAsString();
            String orderId = miniOrderApiProvider.createOrder(userId, param);
            return CommonResult.data(orderId);
        } catch (Exception e) {
            log.error("创建订单失败", e);
            return CommonResult.error("创建订单失败");
        }
    }

    /**
     * 取消订单
     */
    @PostMapping("/cancel")
    @Operation(summary = "取消订单")
    @SaClientCheckLogin
    public CommonResult<Boolean> cancelOrder(@RequestBody @Valid MiniOrderCancelParam param) {
        try {
            String userId = StpClientUtil.getLoginIdAsString();
            Boolean result = miniOrderApiProvider.cancelOrder(userId, param);
            return CommonResult.data(result);
        } catch (Exception e) {
            log.error("取消订单失败", e);
            return CommonResult.error("取消订单失败");
        }
    }

    /**
     * 获取订单统计信息
     */
    @GetMapping("/statistics")
    @Operation(summary = "获取订单统计信息")
    @SaClientCheckLogin
    public CommonResult<WineOrderStatisticsPojo> getOrderStatistics() {
        try {
            String userId = StpClientUtil.getLoginIdAsString();
            WineOrderStatisticsPojo result = miniOrderApiProvider.getOrderStatistics(userId);
            return CommonResult.data(result);
        } catch (Exception e) {
            log.error("获取订单统计信息失败", e);
            return CommonResult.error("获取订单统计信息失败");
        }
    }

    /**
     * 搜索订单
     */
    @GetMapping("/search")
    @Operation(summary = "搜索订单")
    @SaClientCheckLogin
    public CommonResult<List<WineOrderPojo>> searchOrders(
            @RequestParam @NotBlank(message = "搜索关键词不能为空") String keyword,
            @RequestParam(defaultValue = "20") Integer limit) {
        try {
            String userId = StpClientUtil.getLoginIdAsString();
            List<WineOrderPojo> result = miniOrderApiProvider.searchOrders(userId, keyword, limit);
            return CommonResult.data(result);
        } catch (Exception e) {
            log.error("搜索订单失败，关键词：{}", keyword, e);
            return CommonResult.error("搜索订单失败");
        }
    }

    /**
     * 确认订单
     */
    @PostMapping("/confirm")
    @Operation(summary = "确认订单")
    @SaClientCheckLogin
    public CommonResult<Boolean> confirmOrder(@RequestParam @NotBlank(message = "订单ID不能为空") String orderId) {
        try {
            String userId = StpClientUtil.getLoginIdAsString();
            Boolean result = miniOrderApiProvider.confirmOrder(userId, orderId);
            return CommonResult.data(result);
        } catch (Exception e) {
            log.error("确认订单失败，订单ID：{}", orderId, e);
            return CommonResult.error("确认订单失败");
        }
    }

    /**
     * 申请退款
     */
    @PostMapping("/refund")
    @Operation(summary = "申请退款")
    @SaClientCheckLogin
    public CommonResult<Boolean> refundOrder(@RequestParam @NotBlank(message = "订单ID不能为空") String orderId, 
                                           @RequestParam(required = false) String reason) {
        try {
            String userId = StpClientUtil.getLoginIdAsString();
            Boolean result = miniOrderApiProvider.refundOrder(userId, orderId, reason);
            return CommonResult.data(result);
        } catch (Exception e) {
            log.error("申请退款失败，订单ID：{}", orderId, e);
            return CommonResult.error("申请退款失败");
        }
    }

    /**
     * 重新下单
     */
    @PostMapping("/reorder")
    @Operation(summary = "重新下单")
    @SaClientCheckLogin
    public CommonResult<String> reorder(@RequestParam @NotBlank(message = "订单ID不能为空") String orderId) {
        try {
            String userId = StpClientUtil.getLoginIdAsString();
            String newOrderId = miniOrderApiProvider.reorder(userId, orderId);
            return CommonResult.data(newOrderId);
        } catch (Exception e) {
            log.error("重新下单失败，订单ID：{}", orderId, e);
            return CommonResult.error("重新下单失败");
        }
    }
}