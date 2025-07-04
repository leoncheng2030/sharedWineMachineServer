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
package vip.wqs.commission.modular.account.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vip.wqs.commission.modular.account.entity.WineUserAccount;
import vip.wqs.commission.modular.account.param.WineUserAccountAdjustParam;
import vip.wqs.commission.modular.account.param.WineUserAccountIdParam;
import vip.wqs.commission.modular.account.param.WineUserAccountPageParam;
import vip.wqs.commission.modular.account.param.WineUserAccountStatusParam;
import vip.wqs.commission.modular.account.service.WineUserAccountService;
import vip.wqs.common.pojo.CommonResult;

import java.util.List;

/**
 * 用户账户Controller
 *
 * @author WQS_TEAM
 * @date 2025-01-29
 */
@Tag(name = "用户账户管理")
@RestController
@Validated
public class WineUserAccountController {

    @Resource
    private WineUserAccountService wineUserAccountService;

    /**
     * 获取用户账户分页列表
     */
    @Operation(summary = "获取用户账户分页列表")
    @SaCheckPermission("/commission/account/page")
    @GetMapping("/commission/account/page")
    public CommonResult<Page<WineUserAccount>> page(@Valid WineUserAccountPageParam wineUserAccountPageParam) {
        return CommonResult.data(wineUserAccountService.page(wineUserAccountPageParam));
    }

    /**
     * 获取用户账户详情
     */
    @Operation(summary = "获取用户账户详情")
    @SaCheckPermission("/commission/account/detail")
    @GetMapping("/commission/account/detail")
    public CommonResult<WineUserAccount> detail(@Valid WineUserAccountIdParam wineUserAccountIdParam) {
        return CommonResult.data(wineUserAccountService.detail(wineUserAccountIdParam));
    }

    /**
     * 创建用户账户
     */
    @Operation(summary = "创建用户账户")
    @SaCheckPermission("/commission/account/create")
    @PostMapping("/commission/account/create")
    public CommonResult<String> createAccount(@RequestParam @NotEmpty(message = "用户ID不能为空") String userId) {
        wineUserAccountService.createAccount(userId);
        return CommonResult.ok();
    }

    /**
     * 调整账户余额
     */
    @Operation(summary = "调整账户余额")
    @SaCheckPermission("/commission/account/adjust")
    @PostMapping("/commission/account/adjust")
    public CommonResult<String> adjustBalance(@RequestBody @Valid WineUserAccountAdjustParam wineUserAccountAdjustParam) {
        wineUserAccountService.adjustBalance(wineUserAccountAdjustParam);
        return CommonResult.ok();
    }

    /**
     * 修改账户状态
     */
    @Operation(summary = "修改账户状态")
    @SaCheckPermission("/commission/account/updateStatus")
    @PostMapping("/commission/account/updateStatus")
    public CommonResult<String> updateStatus(@RequestBody @Valid WineUserAccountStatusParam wineUserAccountStatusParam) {
        wineUserAccountService.updateStatus(wineUserAccountStatusParam);
        return CommonResult.ok();
    }

    /**
     * 冻结账户
     */
    @Operation(summary = "冻结账户")
    @SaCheckPermission("/commission/account/freeze")
    @PostMapping("/commission/account/freeze")
    public CommonResult<String> freezeAccount(@RequestBody @Valid WineUserAccountIdParam wineUserAccountIdParam) {
        wineUserAccountService.freezeAccount(wineUserAccountIdParam);
        return CommonResult.ok();
    }

    /**
     * 解冻账户
     */
    @Operation(summary = "解冻账户")
    @SaCheckPermission("/commission/account/unfreeze")
    @PostMapping("/commission/account/unfreeze")
    public CommonResult<String> unfreezeAccount(@RequestBody @Valid WineUserAccountIdParam wineUserAccountIdParam) {
        wineUserAccountService.unfreezeAccount(wineUserAccountIdParam);
        return CommonResult.ok();
    }

    /**
     * 禁用账户
     */
    @Operation(summary = "禁用账户")
    @SaCheckPermission("/commission/account/disable")
    @PostMapping("/commission/account/disable")
    public CommonResult<String> disableAccount(@RequestBody @Valid WineUserAccountIdParam wineUserAccountIdParam) {
        wineUserAccountService.disableAccount(wineUserAccountIdParam);
        return CommonResult.ok();
    }

    /**
     * 启用账户
     */
    @Operation(summary = "启用账户")
    @SaCheckPermission("/commission/account/enable")
    @PostMapping("/commission/account/enable")
    public CommonResult<String> enableAccount(@RequestBody @Valid WineUserAccountIdParam wineUserAccountIdParam) {
        wineUserAccountService.enableAccount(wineUserAccountIdParam);
        return CommonResult.ok();
    }

    /**
     * 获取用户账户选择器列表
     */
    @Operation(summary = "获取用户账户选择器列表")
    @SaCheckPermission("/commission/account/selector")
    @GetMapping("/commission/account/selector")
    public CommonResult<List<WineUserAccount>> accountSelector(@RequestParam(required = false) String searchKey) {
        return CommonResult.data(wineUserAccountService.accountSelector(searchKey));
    }

    /**
     * 导出用户账户数据
     */
    @Operation(summary = "导出用户账户数据")
    @SaCheckPermission("/commission/account/export")
    @GetMapping("/commission/account/export")
    public void exportUserAccount(@Valid WineUserAccountPageParam wineUserAccountPageParam) {
        wineUserAccountService.exportUserAccount(wineUserAccountPageParam);
    }
} 