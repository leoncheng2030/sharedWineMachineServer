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
package vip.wqs.order.modular.record.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vip.wqs.common.annotation.CommonLog;
import vip.wqs.common.pojo.CommonResult;
import vip.wqs.order.modular.record.entity.WineOrder;
import vip.wqs.order.modular.record.param.WineOrderIdParam;
import vip.wqs.order.modular.record.param.WineOrderPageParam;
import vip.wqs.order.modular.record.service.WineOrderService;

/**
 * 订单控制器
 *
 * @author wqs
 * @date 2025/01/30
 */
@Tag(name = "订单控制器")
@ApiSupport(author = "WQS_TEAM", order = 15)
@RestController
@Validated
public class WineOrderController {

    @Resource
    private WineOrderService wineOrderService;

    /**
     * 获取订单分页
     *
     * @author wqs
     * @date 2025/01/30
     */
    @ApiOperationSupport(order = 1)
    @Operation(summary = "获取订单分页")
    @GetMapping("/order/manage/page")
    public CommonResult<Page<WineOrder>> page(WineOrderPageParam wineOrderPageParam) {
        return CommonResult.data(wineOrderService.page(wineOrderPageParam));
    }


    /**
     * 获取订单详情
     *
     * @author wqs
     * @date 2025/01/30
     */
    @ApiOperationSupport(order = 3)
    @Operation(summary = "获取订单详情")
    @GetMapping("/order/manage/detail")
    public CommonResult<WineOrder> detail(@Valid WineOrderIdParam wineOrderIdParam) {
        return CommonResult.data(wineOrderService.detail(wineOrderIdParam));
    }

    /**
     * 更新订单状态
     *
     * @author wqs
     * @date 2025/01/30
     */
    @ApiOperationSupport(order = 4)
    @Operation(summary = "更新订单状态")
    @CommonLog("更新订单状态")
    @PostMapping("/order/manage/updateStatus")
    public CommonResult<String> updateStatus(@RequestParam @NotEmpty(message = "orderId不能为空") String orderId,
                                            @RequestParam @NotEmpty(message = "status不能为空") String status) {
        Boolean result = wineOrderService.updateOrderStatus(orderId, status);
        if (result) {
            return CommonResult.ok();
        } else {
            return CommonResult.error("更新订单状态失败");
        }
    }
    /**
     * 取消订单
     *
     * @author wqs
     * @date 2025/01/30
     */
    @ApiOperationSupport(order = 6)
    @Operation(summary = "取消订单")
    @CommonLog("取消订单")
    @PostMapping("/order/manage/cancel")
    public CommonResult<String> cancel(@RequestParam @NotEmpty(message = "orderId不能为空") String orderId,
                                     @RequestParam String cancelReason) {
        Boolean result = wineOrderService.cancelOrder(orderId, cancelReason);
        if (result) {
            return CommonResult.ok();
        } else {
            return CommonResult.error("取消订单失败");
        }
    }

} 