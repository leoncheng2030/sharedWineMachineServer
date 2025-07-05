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
package vip.wqs.miniprogram.modular;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
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
import vip.wqs.common.exception.CommonException;
import vip.wqs.common.pojo.CommonResult;
import vip.wqs.device.api.WineDeviceStockApi;
import vip.wqs.miniprogram.modular.param.MiniOrderCancelParam;
import vip.wqs.miniprogram.modular.param.MiniOrderCreateParam;
import vip.wqs.order.api.WineOrderApi;
import vip.wqs.order.pojo.WineOrderApiPageParam;
import vip.wqs.order.pojo.WineOrderPojo;
import vip.wqs.order.pojo.WineOrderStatisticsPojo;

import java.math.BigDecimal;
import java.util.List;

/**
 * 小程序订单控制器
 * 直接调用后端API，专注于小程序适配和权限控制
 *
 * @author wqs
 * @date 2025/01/30
 */
@RestController
@RequestMapping("/miniprogram/order")
@Tag(name = "小程序订单接口")
@Validated
public class MiniOrderController {

    private static final Logger log = LoggerFactory.getLogger(MiniOrderController.class);

    @Resource
    private WineOrderApi wineOrderApi;
    
    @Resource
    private WineDeviceStockApi wineDeviceStockApi;

    /**
     * 获取用户订单分页列表
     */
    @GetMapping("/page")
    @Operation(summary = "获取用户订单分页列表")
    @SaClientCheckLogin
    public CommonResult<Page<WineOrderPojo>> getOrderPage(@Valid WineOrderApiPageParam param,
                                                         @RequestParam(required = false) String startTime,
                                                         @RequestParam(required = false) String endTime) {
        try {
            String userId = StpClientUtil.getLoginIdAsString();
            log.info("小程序获取订单列表，用户ID：{}，参数：{}", userId, param);

            // 小程序业务逻辑：设置用户ID
            param.setUserId(userId);
            
            // 小程序特有逻辑：处理时间范围筛选
            if (StrUtil.isNotBlank(startTime) || StrUtil.isNotBlank(endTime)) {
                // 这里可以扩展API参数类来支持时间范围，或者在后续版本中处理
                log.info("小程序时间范围筛选：{} - {}", startTime, endTime);
            }
            
            // 调用订单API
            Page<WineOrderPojo> result = wineOrderApi.getOrderPage(param);
            
            // 为每个订单设置小程序专用字段
            if (result != null && result.getRecords() != null) {
                result.getRecords().forEach(this::setMiniProgramFields);
            }
            
            log.info("小程序获取订单列表成功，用户ID：{}，总数：{}", userId, result != null ? result.getTotal() : 0);
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
            log.info("小程序获取订单详情，用户ID：{}，订单ID：{}", userId, orderId);

            // 调用订单API
            WineOrderPojo wineOrder = wineOrderApi.getOrderById(orderId);
            if (ObjectUtil.isNull(wineOrder)) {
                return CommonResult.error("订单不存在");
            }

            // 小程序权限验证：验证订单是否属于当前用户
            if (!userId.equals(wineOrder.getUserId())) {
                return CommonResult.error("无权查看此订单");
            }

            // 设置小程序专用字段
            setMiniProgramFields(wineOrder);
            
            log.info("小程序获取订单详情成功，用户ID：{}，订单ID：{}", userId, orderId);
            return CommonResult.data(wineOrder);
            
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
            log.info("小程序根据订单号获取详情，用户ID：{}，订单号：{}", userId, orderNo);

            // 调用订单API
            WineOrderPojo wineOrder = wineOrderApi.getOrderByOrderNo(orderNo);
            if (ObjectUtil.isNull(wineOrder)) {
                return CommonResult.error("订单不存在");
            }

            // 小程序权限验证：验证订单是否属于当前用户
            if (!userId.equals(wineOrder.getUserId())) {
                return CommonResult.error("无权查看此订单");
            }

            // 设置小程序专用字段
            setMiniProgramFields(wineOrder);
            
            log.info("小程序根据订单号获取详情成功，用户ID：{}，订单号：{}", userId, orderNo);
            return CommonResult.data(wineOrder);
            
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
            log.info("小程序创建订单，用户ID：{}，参数：{}", userId, param);

            // 小程序业务逻辑：检查库存是否充足
            Boolean isStockSufficient = wineDeviceStockApi.checkStockSufficient(
                param.getDeviceId(),
                param.getWineId(),
                BigDecimal.valueOf(param.getAmount())
            );
            
            if (!isStockSufficient) {
                return CommonResult.error("库存不足，无法创建订单");
            }

            // 调用订单API创建订单
            String orderId = wineOrderApi.createOrder(
                    userId,
                    param.getDeviceId(),
                    param.getWineId(),
                    param.getAmount(),
                    param.getUnitPrice()
            );

            // 小程序业务逻辑：扣减库存
            try {
                Boolean deductResult = wineDeviceStockApi.deductStock(
                    param.getDeviceId(),
                    param.getWineId(),
                    BigDecimal.valueOf(param.getAmount()),
                    userId,
                    "订单销售扣减，订单号：" + orderId
                );
                
                if (!deductResult) {
                    // 库存扣减失败，需要取消订单
                    wineOrderApi.cancelOrder(orderId, "库存扣减失败");
                    return CommonResult.error("库存扣减失败，订单已取消");
                }
            } catch (Exception stockException) {
                // 库存扣减异常，取消订单
                try {
                    wineOrderApi.cancelOrder(orderId, "库存扣减异常：" + stockException.getMessage());
                } catch (Exception cancelException) {
                    log.error("取消订单失败，订单ID：{}，错误：{}", orderId, cancelException.getMessage());
                }
                return CommonResult.error("库存扣减失败：" + stockException.getMessage());
            }

            log.info("小程序创建订单成功，用户ID：{}，订单ID：{}", userId, orderId);
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
            log.info("小程序取消订单，用户ID：{}，参数：{}", userId, param);

            // 小程序权限验证：验证订单是否属于当前用户
            WineOrderPojo wineOrder = wineOrderApi.getOrderById(param.getOrderId());
            if (ObjectUtil.isNull(wineOrder)) {
                return CommonResult.error("订单不存在");
            }
            if (!userId.equals(wineOrder.getUserId())) {
                return CommonResult.error("无权操作此订单");
            }

            // 调用订单API取消订单
            Boolean result = wineOrderApi.cancelOrder(param.getOrderId(), param.getCancelReason());
            
            log.info("小程序取消订单成功，用户ID：{}，订单ID：{}", userId, param.getOrderId());
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
            log.info("小程序获取订单统计，用户ID：{}", userId);

            // 调用订单API获取统计信息
            WineOrderStatisticsPojo statistics = wineOrderApi.getOrderStatistics(userId);
            
            // 小程序数据适配：计算进行中订单数
            if (statistics != null) {
                statistics.setProcessingCount(
                    (statistics.getPaidCount() != null ? statistics.getPaidCount() : 0) + 
                    (statistics.getDispensingCount() != null ? statistics.getDispensingCount() : 0)
                );
            }

            log.info("小程序获取订单统计成功，用户ID：{}", userId);
            return CommonResult.data(statistics);
            
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
            log.info("小程序搜索订单，用户ID：{}，关键词：{}", userId, keyword);

            // 构建搜索参数
            WineOrderApiPageParam searchParam = new WineOrderApiPageParam();
            searchParam.setUserId(userId);
            searchParam.setPageNum(1);
            searchParam.setPageSize(limit != null ? limit : 50);
            searchParam.setKeyword(keyword);
            searchParam.setSortField("createTime");
            searchParam.setSortOrder("desc");
            
            // 调用订单API分页查询
            Page<WineOrderPojo> orderPage = wineOrderApi.getOrderPage(searchParam);
            
            List<WineOrderPojo> result = orderPage != null ? orderPage.getRecords() : List.of();
            
            // 为每个订单设置小程序专用字段
            result.forEach(this::setMiniProgramFields);

            log.info("小程序搜索订单成功，用户ID：{}，找到{}条记录", userId, result.size());
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
            log.info("小程序确认订单，用户ID：{}，订单ID：{}", userId, orderId);

            // 小程序权限验证：验证订单是否属于当前用户
            WineOrderPojo wineOrder = wineOrderApi.getOrderById(orderId);
            if (ObjectUtil.isNull(wineOrder)) {
                return CommonResult.error("订单不存在");
            }
            if (!userId.equals(wineOrder.getUserId())) {
                return CommonResult.error("无权操作此订单");
            }

            // 小程序业务逻辑：检查订单状态是否可以确认
            if (!"DISPENSING".equals(wineOrder.getStatus())) {
                return CommonResult.error("订单状态不允许确认操作");
            }

            // 调用订单API完成出酒
            Boolean result = wineOrderApi.completeDispense(orderId);
            
            log.info("小程序确认订单成功，用户ID：{}，订单ID：{}", userId, orderId);
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
            log.info("小程序申请退款，用户ID：{}，订单ID：{}", userId, orderId);

            // 小程序权限验证：验证订单是否属于当前用户
            WineOrderPojo wineOrder = wineOrderApi.getOrderById(orderId);
            if (ObjectUtil.isNull(wineOrder)) {
                return CommonResult.error("订单不存在");
            }
            if (!userId.equals(wineOrder.getUserId())) {
                return CommonResult.error("无权操作此订单");
            }

            // 小程序业务逻辑：检查订单状态是否可以退款
            String status = wineOrder.getStatus();
            if (!"PAID".equals(status) && !"DISPENSING".equals(status) && !"COMPLETED".equals(status)) {
                return CommonResult.error("订单状态不允许退款操作，当前状态：" + status);
            }

            // 调用订单API更新状态为退款
            Boolean result = wineOrderApi.updateOrderStatus(orderId, "REFUNDED");
            
            log.info("小程序申请退款成功，用户ID：{}，订单ID：{}", userId, orderId);
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
            log.info("小程序重新下单，用户ID：{}，原订单ID：{}", userId, orderId);

            // 小程序权限验证：验证原订单是否属于当前用户
            WineOrderPojo originalOrder = wineOrderApi.getOrderById(orderId);
            if (ObjectUtil.isNull(originalOrder)) {
                return CommonResult.error("原订单不存在");
            }
            if (!userId.equals(originalOrder.getUserId())) {
                return CommonResult.error("无权操作此订单");
            }

            // 调用订单API基于原订单信息创建新订单
            String newOrderId = wineOrderApi.createOrder(
                    userId,
                    originalOrder.getDeviceId(),
                    originalOrder.getWineId(),
                    originalOrder.getAmount(),
                    originalOrder.getUnitPrice()
            );

            log.info("小程序重新下单成功，用户ID：{}，新订单ID：{}", userId, newOrderId);
            return CommonResult.data(newOrderId);
            
        } catch (Exception e) {
            log.error("重新下单失败，原订单ID：{}", orderId, e);
            return CommonResult.error("重新下单失败");
        }
    }

    // ========== 小程序专用的私有方法 ==========



    /**
     * 为订单设置小程序专用字段
     * 小程序数据适配：设置UI显示相关的字段
     */
    private void setMiniProgramFields(WineOrderPojo order) {
        if (order == null) {
            return;
        }

        try {
            // 设置状态文本
            order.setStatusText(getStatusText(order.getStatus()));

            // 设置操作权限
            setOrderOperationPermissions(order, order.getStatus());
            
            // 设置小程序专用显示字段
            setMiniProgramDisplayFields(order);
            
        } catch (Exception e) {
            log.warn("设置小程序专用字段失败，订单ID：{}，错误：{}", order.getId(), e.getMessage());
            // 设置默认值，确保不影响基础功能
            order.setStatusText("未知");
            order.setCanPay(false);
            order.setCanCancel(false);
            order.setCanRefund(false);
        }
    }

    /**
     * 获取状态文本
     * 小程序数据适配：将状态码转换为中文显示
     */
    private String getStatusText(String status) {
        if (StrUtil.isBlank(status)) {
            return "未知";
        }
        return switch (status) {
            case "PENDING" -> "待支付";
            case "PAID" -> "已支付";
            case "DISPENSING" -> "待取酒";
            case "COMPLETED" -> "已完成";
            case "CANCELLED" -> "已取消";
            case "REFUNDED" -> "已退款";
            default -> "未知";
        };
    }

    /**
     * 设置订单操作权限
     * 小程序业务逻辑：根据状态设置可执行的操作
     */
    private void setOrderOperationPermissions(WineOrderPojo order, String status) {
        if (StrUtil.isBlank(status)) {
            // 默认权限：都不可操作
            order.setCanPay(false);
            order.setCanCancel(false);
            order.setCanRefund(false);
            return;
        }

        switch (status) {
            case "PENDING":
                order.setCanPay(true);
                order.setCanCancel(true);
                order.setCanRefund(false);
                break;
            case "PAID":
                order.setCanPay(false);
                order.setCanCancel(false);
                order.setCanRefund(true);
                break;
            case "DISPENSING":
                order.setCanPay(false);
                order.setCanCancel(false);
                order.setCanRefund(true);
                break;
            case "COMPLETED":
                order.setCanPay(false);
                order.setCanCancel(false);
                order.setCanRefund(false);
                break;
            case "CANCELLED":
            case "REFUNDED":
                order.setCanPay(false);
                order.setCanCancel(false);
                order.setCanRefund(false);
                break;
            default:
                order.setCanPay(false);
                order.setCanCancel(false);
                order.setCanRefund(false);
                break;
        }
    }
    
    /**
     * 设置小程序专用显示字段
     * 小程序UI适配：设置界面显示相关的字段
     */
    private void setMiniProgramDisplayFields(WineOrderPojo order) {
        try {
            // 设置是否显示支付按钮
            order.setShowPayButton(order.getCanPay() != null && order.getCanPay());
            
            // 设置是否显示取消按钮
            order.setShowCancelButton(order.getCanCancel() != null && order.getCanCancel());
            
            // 设置是否显示退款按钮
            order.setShowRefundButton(order.getCanRefund() != null && order.getCanRefund());
            
            // 设置订单进度百分比（用于进度条显示）
            order.setProgressPercent(calculateProgressPercent(order.getStatus()));
            
        } catch (Exception e) {
            log.warn("设置小程序显示字段失败，订单ID：{}，错误：{}", order.getId(), e.getMessage());
        }
    }
    
    /**
     * 计算订单进度百分比
     * 小程序UI适配：为进度条提供百分比数据
     */
    private Integer calculateProgressPercent(String status) {
        if (StrUtil.isBlank(status)) {
            return 0;
        }
        
        return switch (status) {
            case "PENDING" -> 20;
            case "PAID" -> 50;
            case "DISPENSING" -> 80;
            case "COMPLETED" -> 100;
            case "CANCELLED", "REFUNDED" -> 0;
            default -> 0;
        };
    }
}