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
package vip.wqs.commission.modular.withdraw.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vip.wqs.commission.modular.withdraw.entity.WineWithdrawRecord;
import vip.wqs.commission.modular.withdraw.param.WineWithdrawRecordAuditParam;
import vip.wqs.commission.modular.withdraw.param.WineWithdrawRecordIdParam;
import vip.wqs.commission.modular.withdraw.param.WineWithdrawRecordPageParam;
import vip.wqs.commission.modular.withdraw.service.WineWithdrawRecordService;
import vip.wqs.common.pojo.CommonResult;

import java.util.List;

/**
 * 提现记录管理控制器
 *
 * @author WQS_TEAM
 * @date 2025-01-29
 */
@Tag(name = "提现记录管理")
@RestController
@Validated
public class WineWithdrawRecordController {

    @Resource
    private WineWithdrawRecordService wineWithdrawRecordService;

    /**
     * 获取提现记录分页列表
     */
    @Operation(summary = "获取提现记录分页列表")
    @SaCheckPermission("/commission/withdraw/page")
    @GetMapping("/commission/withdraw/page")
    public CommonResult<Page<WineWithdrawRecord>> page(@Valid WineWithdrawRecordPageParam wineWithdrawRecordPageParam) {
        return CommonResult.data(wineWithdrawRecordService.page(wineWithdrawRecordPageParam));
    }

    /**
     * 获取提现记录详情
     */
    @Operation(summary = "获取提现记录详情")
    @SaCheckPermission("/commission/withdraw/detail")
    @GetMapping("/commission/withdraw/detail")
    public CommonResult<WineWithdrawRecord> detail(@Valid WineWithdrawRecordIdParam wineWithdrawRecordIdParam) {
        return CommonResult.data(wineWithdrawRecordService.detail(wineWithdrawRecordIdParam));
    }

    /**
     * 审核提现申请
     */
    @Operation(summary = "审核提现申请")
    @SaCheckPermission("/commission/withdraw/audit")
    @PostMapping("/commission/withdraw/audit")
    public CommonResult<String> auditWithdraw(@RequestBody @Valid WineWithdrawRecordAuditParam wineWithdrawRecordAuditParam) {
        wineWithdrawRecordService.auditWithdraw(wineWithdrawRecordAuditParam);
        return CommonResult.ok();
    }

    /**
     * 取消提现申请
     */
    @Operation(summary = "取消提现申请")
    @SaCheckPermission("/commission/withdraw/cancel")
    @PostMapping("/commission/withdraw/cancel")
    public CommonResult<String> cancelWithdraw(@RequestBody @Valid WineWithdrawRecordIdParam wineWithdrawRecordIdParam) {
        wineWithdrawRecordService.cancelWithdraw(wineWithdrawRecordIdParam);
        return CommonResult.ok();
    }

    /**
     * 确认提现成功
     */
    @Operation(summary = "确认提现成功")
    @SaCheckPermission("/commission/withdraw/confirmSuccess")
    @PostMapping("/commission/withdraw/confirmSuccess")
    public CommonResult<String> confirmWithdrawSuccess(@RequestBody @Valid WineWithdrawRecordIdParam wineWithdrawRecordIdParam, 
                                                        @RequestParam(required = false) String thirdPartyTransNo) {
        wineWithdrawRecordService.confirmWithdrawSuccess(wineWithdrawRecordIdParam, thirdPartyTransNo);
        return CommonResult.ok();
    }

    /**
     * 标记提现失败
     */
    @Operation(summary = "标记提现失败")
    @SaCheckPermission("/commission/withdraw/markFailed")
    @PostMapping("/commission/withdraw/markFailed")
    public CommonResult<String> markWithdrawFailed(@RequestBody @Valid WineWithdrawRecordIdParam wineWithdrawRecordIdParam, 
                                                    @RequestParam String failReason) {
        wineWithdrawRecordService.markWithdrawFailed(wineWithdrawRecordIdParam, failReason);
        return CommonResult.ok();
    }

    /**
     * 获取用户提现记录列表
     */
    @Operation(summary = "获取用户提现记录列表")
    @SaCheckPermission("/commission/withdraw/userWithdrawList")
    @GetMapping("/commission/withdraw/userWithdrawList")
    public CommonResult<List<WineWithdrawRecord>> userWithdrawList(@RequestParam String userId, 
                                                                   @RequestParam(required = false, defaultValue = "10") Integer limit) {
        return CommonResult.data(wineWithdrawRecordService.getUserWithdrawList(userId, limit));
    }

    /**
     * 统计用户提现汇总信息
     */
    @Operation(summary = "统计用户提现汇总信息")
    @SaCheckPermission("/commission/withdraw/userWithdrawSummary")
    @GetMapping("/commission/withdraw/userWithdrawSummary")
    public CommonResult<WineWithdrawRecordService.WineWithdrawSummary> userWithdrawSummary(@RequestParam String userId) {
        return CommonResult.data(wineWithdrawRecordService.getUserWithdrawSummary(userId));
    }

    /**
     * 统计待审核提现数量
     */
    @Operation(summary = "统计待审核提现数量")
    @SaCheckPermission("/commission/withdraw/countPending")
    @GetMapping("/commission/withdraw/countPending")
    public CommonResult<Long> countPendingWithdraw() {
        return CommonResult.data(wineWithdrawRecordService.countPendingWithdraw());
    }

    /**
     * 导出提现记录数据
     */
    @Operation(summary = "导出提现记录数据")
    @SaCheckPermission("/commission/withdraw/export")
    @GetMapping("/commission/withdraw/export")
    public void exportWithdrawRecord(@Valid WineWithdrawRecordPageParam wineWithdrawRecordPageParam) {
        wineWithdrawRecordService.exportWithdrawRecord(wineWithdrawRecordPageParam);
    }
} 