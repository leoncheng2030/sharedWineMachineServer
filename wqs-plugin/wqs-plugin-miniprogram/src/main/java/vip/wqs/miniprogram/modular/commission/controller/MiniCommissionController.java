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
package vip.wqs.miniprogram.modular.commission.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vip.wqs.auth.core.annotation.SaClientCheckLogin;
import vip.wqs.auth.core.util.StpClientUtil;
import vip.wqs.commission.api.WineCommissionApi;
import vip.wqs.commission.api.WineWithdrawApi;
import vip.wqs.commission.api.dto.MiniCommissionPageDto;
import vip.wqs.commission.api.dto.MiniWithdrawApplyDto;
import vip.wqs.commission.api.dto.WineCommissionRecordDto;
import vip.wqs.commission.api.dto.WineWithdrawRecordDto;
import vip.wqs.common.annotation.CommonLog;
import vip.wqs.common.pojo.CommonResult;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 小程序佣金管理控制器
 * 
 * @author WQS_TEAM
 * @date 2025-01-30
 */
@Tag(name = "小程序佣金管理控制器")
@ApiSupport(author = "WQS_TEAM", order = 8)
@RestController
@Validated
@Slf4j
public class MiniCommissionController {

    @Resource
    private WineCommissionApi wineCommissionApi;

    @Resource
    private WineWithdrawApi wineWithdrawApi;

    // ======================== 佣金记录相关接口 ========================

    /**
     * 获取用户佣金记录分页列表
     * 注意：需要登录验证，获取当前用户的佣金记录
     *
     * @author WQS_TEAM
     * @date 2025-01-30
     */
    @ApiOperationSupport(order = 1)
    @Operation(summary = "获取用户佣金记录分页列表")
    @CommonLog("小程序获取用户佣金记录")
    @SaClientCheckLogin
    @GetMapping("/miniprogram/commission/records")
    public CommonResult<Page<WineCommissionRecordDto>> getCommissionRecords(@Valid MiniCommissionPageDto param) {
        try {
            // 获取当前登录用户ID
            String userId = StpClientUtil.getLoginIdAsString();
            param.setUserId(userId);
            
            log.info("小程序获取用户佣金记录，用户ID：{}，参数：{}", userId, param);
            
            // 调用佣金API获取分页数据
            Page<WineCommissionRecordDto> result = wineCommissionApi.getCommissionRecords(param);
            
            if (result == null) {
                return CommonResult.error("获取佣金记录失败");
            }
            
            log.info("小程序获取用户佣金记录成功，用户ID：{}，返回{}条数据", userId, result.getRecords().size());
            return CommonResult.data(result);
            
        } catch (Exception e) {
            log.error("获取用户佣金记录异常", e);
            return CommonResult.error("获取佣金记录失败：" + e.getMessage());
        }
    }

    /**
     * 获取用户佣金统计信息
     * 注意：需要登录验证，获取当前用户的佣金统计
     *
     * @author WQS_TEAM
     * @date 2025-01-30
     */
    @ApiOperationSupport(order = 2)
    @Operation(summary = "获取用户佣金统计信息")
    @CommonLog("小程序获取用户佣金统计")
    @SaClientCheckLogin
    @GetMapping("/miniprogram/commission/statistics")
    public CommonResult<Map<String, Object>> getCommissionStatistics() {
        try {
            // 获取当前登录用户ID
            String userId = StpClientUtil.getLoginIdAsString();
            
            log.info("小程序获取用户佣金统计，用户ID：{}", userId);
            
            // 调用佣金API获取统计数据
            Map<String, Object> result = wineCommissionApi.getCommissionStatistics(userId);
            
            if (result == null) {
                return CommonResult.error("获取佣金统计失败");
            }
            
            log.info("小程序获取用户佣金统计成功，用户ID：{}", userId);
            return CommonResult.data(result);
            
        } catch (Exception e) {
            log.error("获取用户佣金统计异常", e);
            return CommonResult.error("获取佣金统计失败：" + e.getMessage());
        }
    }

    /**
     * 获取用户佣金汇总信息
     * 注意：需要登录验证，获取当前用户的佣金汇总
     *
     * @author WQS_TEAM
     * @date 2025-01-30
     */
    @ApiOperationSupport(order = 3)
    @Operation(summary = "获取用户佣金汇总信息")
    @CommonLog("小程序获取用户佣金汇总")
    @SaClientCheckLogin
    @GetMapping("/miniprogram/commission/summary")
    public CommonResult<Map<String, BigDecimal>> getCommissionSummary() {
        try {
            // 获取当前登录用户ID
            String userId = StpClientUtil.getLoginIdAsString();
            
            log.info("小程序获取用户佣金汇总，用户ID：{}", userId);
            
            // 调用佣金API获取汇总数据
            Map<String, BigDecimal> result = wineCommissionApi.getCommissionSummary(userId);
            
            if (result == null) {
                return CommonResult.error("获取佣金汇总失败");
            }
            
            log.info("小程序获取用户佣金汇总成功，用户ID：{}", userId);
            return CommonResult.data(result);
            
        } catch (Exception e) {
            log.error("获取用户佣金汇总异常", e);
            return CommonResult.error("获取佣金汇总失败：" + e.getMessage());
        }
    }

    /**
     * 获取用户账户信息
     * 注意：需要登录验证，获取当前用户的账户信息
     *
     * @author WQS_TEAM
     * @date 2025-01-30
     */
    @ApiOperationSupport(order = 4)
    @Operation(summary = "获取用户账户信息")
    @CommonLog("小程序获取用户账户信息")
    @SaClientCheckLogin
    @GetMapping("/miniprogram/commission/account")
    public CommonResult<Map<String, Object>> getAccountInfo() {
        try {
            // 获取当前登录用户ID
            String userId = StpClientUtil.getLoginIdAsString();
            
            log.info("小程序获取用户账户信息，用户ID：{}", userId);
            
            // 调用佣金API获取账户信息
            Map<String, Object> result = wineCommissionApi.getUserAccount(userId);
            
            if (result == null) {
                return CommonResult.error("获取账户信息失败");
            }
            
            log.info("小程序获取用户账户信息成功，用户ID：{}", userId);
            return CommonResult.data(result);
            
        } catch (Exception e) {
            log.error("获取用户账户信息异常", e);
            return CommonResult.error("获取账户信息失败：" + e.getMessage());
        }
    }

    // ======================== 提现管理相关接口 ========================

    /**
     * 申请提现
     * 注意：需要登录验证，为当前用户申请提现
     *
     * @author WQS_TEAM
     * @date 2025-01-30
     */
    @ApiOperationSupport(order = 5)
    @Operation(summary = "申请提现")
    @CommonLog("小程序申请提现")
    @SaClientCheckLogin
    @PostMapping("/miniprogram/commission/withdraw/apply")
    public CommonResult<String> applyWithdraw(@Valid @RequestBody MiniWithdrawApplyDto param) {
        try {
            // 获取当前登录用户ID
            String userId = StpClientUtil.getLoginIdAsString();
            param.setUserId(userId);
            
            log.info("小程序申请提现，用户ID：{}，参数：{}", userId, param);
            
            // 调用提现API申请提现
            String withdrawId = wineWithdrawApi.applyWithdraw(param);
            
            if (withdrawId == null) {
                return CommonResult.error("申请提现失败");
            }
            
            log.info("小程序申请提现成功，用户ID：{}，提现ID：{}", userId, withdrawId);
            return CommonResult.data(withdrawId);
            
        } catch (Exception e) {
            log.error("申请提现异常", e);
            return CommonResult.error("申请提现失败：" + e.getMessage());
        }
    }

    /**
     * 获取用户提现记录分页列表
     * 注意：需要登录验证，获取当前用户的提现记录
     *
     * @author WQS_TEAM
     * @date 2025-01-30
     */
    @ApiOperationSupport(order = 6)
    @Operation(summary = "获取用户提现记录分页列表")
    @CommonLog("小程序获取用户提现记录")
    @SaClientCheckLogin
    @GetMapping("/miniprogram/commission/withdraw/records")
    public CommonResult<Page<WineWithdrawRecordDto>> getWithdrawRecords(
            @RequestParam(defaultValue = "1") Long pageNum,
            @RequestParam(defaultValue = "10") Long pageSize) {
        try {
            // 获取当前登录用户ID
            String userId = StpClientUtil.getLoginIdAsString();
            
            log.info("小程序获取用户提现记录，用户ID：{}，页码：{}，页大小：{}", userId, pageNum, pageSize);
            
            // 调用提现API获取提现记录
            Page<WineWithdrawRecordDto> result = wineWithdrawApi.getWithdrawRecords(userId, pageNum, pageSize);
            
            if (result == null) {
                return CommonResult.error("获取提现记录失败");
            }
            
            log.info("小程序获取用户提现记录成功，用户ID：{}，返回{}条数据", userId, result.getRecords().size());
            return CommonResult.data(result);
            
        } catch (Exception e) {
            log.error("获取用户提现记录异常", e);
            return CommonResult.error("获取提现记录失败：" + e.getMessage());
        }
    }

    /**
     * 获取提现配置信息
     * 注意：需要登录验证，获取提现相关配置
     *
     * @author WQS_TEAM
     * @date 2025-01-30
     */
    @ApiOperationSupport(order = 7)
    @Operation(summary = "获取提现配置信息")
    @CommonLog("小程序获取提现配置")
    @SaClientCheckLogin
    @GetMapping("/miniprogram/commission/withdraw/config")
    public CommonResult<Map<String, Object>> getWithdrawConfig() {
        try {
            log.info("小程序获取提现配置信息");
            
            // 调用提现API获取提现配置
            Map<String, Object> result = wineWithdrawApi.getWithdrawConfig();
            
            if (result == null) {
                return CommonResult.error("获取提现配置失败");
            }
            
            log.info("小程序获取提现配置信息成功");
            return CommonResult.data(result);
            
        } catch (Exception e) {
            log.error("获取提现配置信息异常", e);
            return CommonResult.error("获取提现配置失败：" + e.getMessage());
        }
    }
} 