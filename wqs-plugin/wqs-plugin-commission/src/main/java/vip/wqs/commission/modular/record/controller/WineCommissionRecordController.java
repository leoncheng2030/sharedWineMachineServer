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
package vip.wqs.commission.modular.record.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vip.wqs.commission.modular.record.entity.WineCommissionRecord;
import vip.wqs.commission.modular.record.param.WineCommissionRecordIdParam;
import vip.wqs.commission.modular.record.param.WineCommissionRecordPageParam;
import vip.wqs.commission.modular.record.service.WineCommissionRecordService;
import vip.wqs.common.annotation.CommonLog;
import vip.wqs.common.pojo.CommonResult;

import java.util.List;

/**
 * 佣金记录控制器
 *
 * @author WQS_TEAM
 * @date 2025-01-29
 **/
@Tag(name = "佣金记录控制器")
@ApiSupport(author = "WQS_TEAM", order = 1)
@RestController
@Validated
public class WineCommissionRecordController {

    @Resource
    private WineCommissionRecordService wineCommissionRecordService;

    /**
     * 获取佣金记录分页
     *
     * @author WQS_TEAM
     * @date 2025-01-29
     */
    @ApiOperationSupport(order = 1)
    @Operation(summary = "获取佣金记录分页")
    @SaCheckPermission("/commission/record/page")
    @GetMapping("/commission/record/page")
    public CommonResult<Page<WineCommissionRecord>> page(WineCommissionRecordPageParam wineCommissionRecordPageParam) {
        return CommonResult.data(wineCommissionRecordService.page(wineCommissionRecordPageParam));
    }

    /**
     * 获取佣金记录详情
     *
     * @author WQS_TEAM
     * @date 2025-01-29
     */
    @ApiOperationSupport(order = 2)
    @Operation(summary = "获取佣金记录详情")
    @SaCheckPermission("/commission/record/detail")
    @GetMapping("/commission/record/detail")
    public CommonResult<WineCommissionRecord> detail(@Valid WineCommissionRecordIdParam wineCommissionRecordIdParam) {
        return CommonResult.data(wineCommissionRecordService.detail(wineCommissionRecordIdParam));
    }

    /**
     * 导出佣金记录
     *
     * @author WQS_TEAM
     * @date 2025-01-29
     */
    @ApiOperationSupport(order = 3)
    @Operation(summary = "导出佣金记录")
    @CommonLog("导出佣金记录")
    @SaCheckPermission("/commission/record/export")
    @GetMapping("/commission/record/export")
    public void export(WineCommissionRecordPageParam wineCommissionRecordPageParam) {
        wineCommissionRecordService.export(wineCommissionRecordPageParam);
    }

    /**
     * 发放佣金
     *
     * @author WQS_TEAM
     * @date 2025-01-29
     */
    @ApiOperationSupport(order = 4)
    @Operation(summary = "发放佣金")
    @CommonLog("发放佣金")
    @SaCheckPermission("/commission/record/settle")
    @PostMapping("/commission/record/settle")
    public CommonResult<String> settle(@RequestBody @Valid WineCommissionRecordIdParam wineCommissionRecordIdParam) {
        wineCommissionRecordService.settle(wineCommissionRecordIdParam);
        return CommonResult.ok();
    }

    /**
     * 冻结佣金
     *
     * @author WQS_TEAM
     * @date 2025-01-29
     */
    @ApiOperationSupport(order = 5)
    @Operation(summary = "冻结佣金")
    @CommonLog("冻结佣金")
    @SaCheckPermission("/commission/record/freeze")
    @PostMapping("/commission/record/freeze")
    public CommonResult<String> freeze(@RequestBody @Valid WineCommissionRecordIdParam wineCommissionRecordIdParam) {
        wineCommissionRecordService.freeze(wineCommissionRecordIdParam);
        return CommonResult.ok();
    }

    /**
     * 取消佣金
     *
     * @author WQS_TEAM
     * @date 2025-01-29
     */
    @ApiOperationSupport(order = 6)
    @Operation(summary = "取消佣金")
    @CommonLog("取消佣金")
    @SaCheckPermission("/commission/record/cancel")
    @PostMapping("/commission/record/cancel")
    public CommonResult<String> cancel(@RequestBody @Valid WineCommissionRecordIdParam wineCommissionRecordIdParam) {
        wineCommissionRecordService.cancel(wineCommissionRecordIdParam);
        return CommonResult.ok();
    }

    /**
     * 重新计算佣金
     *
     * @author WQS_TEAM
     * @date 2025-01-29
     */
    @ApiOperationSupport(order = 7)
    @Operation(summary = "重新计算佣金")
    @CommonLog("重新计算佣金")
    @SaCheckPermission("/commission/record/recalculate")
    @PostMapping("/commission/record/recalculate")
    public CommonResult<String> recalculate(@RequestBody @Valid WineCommissionRecordIdParam wineCommissionRecordIdParam) {
        wineCommissionRecordService.recalculate(wineCommissionRecordIdParam);
        return CommonResult.ok();
    }

    /**
     * 批量发放佣金
     *
     * @author WQS_TEAM
     * @date 2025-01-29
     */
    @ApiOperationSupport(order = 8)
    @Operation(summary = "批量发放佣金")
    @CommonLog("批量发放佣金")
    @SaCheckPermission("/commission/record/batchSettle")
    @PostMapping("/commission/record/batchSettle")
    public CommonResult<String> batchSettle(@RequestBody @Valid @NotEmpty(message = "集合不能为空")
                                                   List<WineCommissionRecordIdParam> wineCommissionRecordIdParamList) {
        wineCommissionRecordService.batchSettle(wineCommissionRecordIdParamList);
        return CommonResult.ok();
    }

    /**
     * 批量冻结佣金
     *
     * @author WQS_TEAM
     * @date 2025-01-29
     */
    @ApiOperationSupport(order = 9)
    @Operation(summary = "批量冻结佣金")
    @CommonLog("批量冻结佣金")
    @SaCheckPermission("/commission/record/batchFreeze")
    @PostMapping("/commission/record/batchFreeze")
    public CommonResult<String> batchFreeze(@RequestBody @Valid @NotEmpty(message = "集合不能为空")
                                                   List<WineCommissionRecordIdParam> wineCommissionRecordIdParamList) {
        wineCommissionRecordService.batchFreeze(wineCommissionRecordIdParamList);
        return CommonResult.ok();
    }

    /**
     * 批量取消佣金
     *
     * @author WQS_TEAM
     * @date 2025-01-29
     */
    @ApiOperationSupport(order = 10)
    @Operation(summary = "批量取消佣金")
    @CommonLog("批量取消佣金")
    @SaCheckPermission("/commission/record/batchCancel")
    @PostMapping("/commission/record/batchCancel")
    public CommonResult<String> batchCancel(@RequestBody @Valid @NotEmpty(message = "集合不能为空")
                                                   List<WineCommissionRecordIdParam> wineCommissionRecordIdParamList) {
        wineCommissionRecordService.batchCancel(wineCommissionRecordIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取佣金记录列表
     *
     * @author WQS_TEAM
     * @date 2025-01-29
     */
    @ApiOperationSupport(order = 11)
    @Operation(summary = "获取佣金记录列表")
    @SaCheckPermission("/commission/record/list")
    @GetMapping("/commission/record/list")
    public CommonResult<List<WineCommissionRecord>> list(WineCommissionRecordPageParam wineCommissionRecordPageParam) {
        return CommonResult.data(wineCommissionRecordService.list(wineCommissionRecordPageParam));
    }

    @ApiOperationSupport(order = 8)
    @Operation(summary = "订单完成后分配佣金")
    @CommonLog("订单完成后分配佣金")
    @PostMapping("/commission/record/distribute")
    public CommonResult<Integer> distributeCommissionForOrder(
            @RequestParam String orderId,
            @RequestParam String orderNo,
            @RequestParam java.math.BigDecimal orderAmount,
            @RequestParam String deviceId,
            @RequestParam String wineId,
            @RequestParam(required = false) String storeId) {
        
        int count = wineCommissionRecordService.distributeCommissionForOrder(
                orderId, orderNo, orderAmount, deviceId, wineId, storeId);
        return CommonResult.data(count);
    }

    @ApiOperationSupport(order = 9)
    @Operation(summary = "查找角色归属用户ID")
    @CommonLog("查找角色归属用户ID")
    @GetMapping("/commission/record/findRoleOwner")
    public CommonResult<String> findRoleOwnerId(
            @RequestParam String commissionType,
            @RequestParam(required = false) String deviceId,
            @RequestParam(required = false) String storeId,
            @RequestParam(required = false) String wineId) {
        
        String userId = wineCommissionRecordService.findRoleOwnerId(commissionType, deviceId, storeId, wineId);
        return CommonResult.data(userId);
    }
} 