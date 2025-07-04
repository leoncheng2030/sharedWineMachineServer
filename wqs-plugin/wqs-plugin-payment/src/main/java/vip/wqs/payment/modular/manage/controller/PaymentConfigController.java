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
package vip.wqs.payment.modular.manage.controller;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vip.wqs.common.annotation.CommonLog;
import vip.wqs.common.pojo.CommonResult;
import vip.wqs.payment.modular.manage.entity.PaymentConfig;
import vip.wqs.payment.modular.manage.param.PaymentConfigCreateParam;
import vip.wqs.payment.modular.manage.param.PaymentConfigEditParam;
import vip.wqs.payment.modular.manage.param.PaymentConfigIdParam;
import vip.wqs.payment.modular.manage.param.PaymentConfigPageParam;
import vip.wqs.payment.modular.manage.service.PaymentConfigService;

import java.util.List;

/**
 * 支付配置控制器
 *
 * @author wqs
 * @date 2025/01/30
 */
@Tag(name = "支付配置控制器")
@ApiSupport(author = "WQS_TEAM", order = 17)
@RestController
@Validated
public class PaymentConfigController {

    @Resource
    private PaymentConfigService paymentConfigService;

    /**
     * 获取支付配置分页
     *
     * @author wqs
     * @date 2025/01/30
     */
    @ApiOperationSupport(order = 1)
    @Operation(summary = "获取支付配置分页")
    @SaCheckPermission("/payment/record/page")
    @GetMapping("/payment/config/page")
    public CommonResult<Page<PaymentConfig>> page(PaymentConfigPageParam paymentConfigPageParam) {
        return CommonResult.data(paymentConfigService.page(paymentConfigPageParam));
    }

    /**
     * 添加支付配置
     *
     * @author wqs
     * @date 2025/01/30
     */
    @ApiOperationSupport(order = 2)
    @Operation(summary = "添加支付配置")
    @CommonLog("添加支付配置")
    @SaCheckPermission("/payment/record/add")
    @PostMapping("/payment/config/add")
    public CommonResult<String> add(@RequestBody @Valid PaymentConfigCreateParam paymentConfigCreateParam) {
        paymentConfigService.add(paymentConfigCreateParam);
        return CommonResult.ok();
    }

    /**
     * 编辑支付配置
     *
     * @author wqs
     * @date 2025/01/30
     */
    @ApiOperationSupport(order = 3)
    @Operation(summary = "编辑支付配置")
    @CommonLog("编辑支付配置")
    @SaCheckPermission("/payment/record/edit")
    @PostMapping("/payment/config/edit")
    public CommonResult<String> edit(@RequestBody @Valid PaymentConfigEditParam paymentConfigEditParam) {
        paymentConfigService.edit(paymentConfigEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除支付配置
     *
     * @author wqs
     * @date 2025/01/30
     */
    @ApiOperationSupport(order = 4)
    @Operation(summary = "删除支付配置")
    @CommonLog("删除支付配置")
    @SaCheckPermission("/payment/record/delete")
    @PostMapping("/payment/config/delete")
    public CommonResult<String> delete(@RequestBody @Valid PaymentConfigIdParam paymentConfigIdParam) {
        paymentConfigService.delete(paymentConfigIdParam);
        return CommonResult.ok();
    }

    /**
     * 获取支付配置详情
     *
     * @author wqs
     * @date 2025/01/30
     */
    @ApiOperationSupport(order = 5)
    @Operation(summary = "获取支付配置详情")
    @SaCheckPermission("/payment/record/detail")
    @GetMapping("/payment/config/detail")
    public CommonResult<PaymentConfig> detail(@Valid PaymentConfigIdParam paymentConfigIdParam) {
        return CommonResult.data(paymentConfigService.detail(paymentConfigIdParam));
    }

    /**
     * 禁用支付配置
     *
     * @author wqs
     * @date 2025/01/30
     */
    @ApiOperationSupport(order = 6)
    @Operation(summary = "禁用支付配置")
    @CommonLog("禁用支付配置")
    @SaCheckPermission("/payment/record/disable")
    @PostMapping("/payment/config/disable")
    public CommonResult<String> disable(@RequestBody @Valid PaymentConfigIdParam paymentConfigIdParam) {
        paymentConfigService.disable(paymentConfigIdParam);
        return CommonResult.ok();
    }

    /**
     * 启用支付配置
     *
     * @author wqs
     * @date 2025/01/30
     */
    @ApiOperationSupport(order = 7)
    @Operation(summary = "启用支付配置")
    @CommonLog("启用支付配置")
    @SaCheckPermission("/payment/record/enable")
    @PostMapping("/payment/config/enable")
    public CommonResult<String> enable(@RequestBody @Valid PaymentConfigIdParam paymentConfigIdParam) {
        paymentConfigService.enable(paymentConfigIdParam);
        return CommonResult.ok();
    }

    /**
     * 批量删除支付配置
     *
     * @author wqs
     * @date 2025/01/30
     */
    @ApiOperationSupport(order = 8)
    @Operation(summary = "批量删除支付配置")
    @CommonLog("批量删除支付配置")
    @SaCheckPermission("/payment/record/batchDelete")
    @PostMapping("/payment/config/batchDelete")
    public CommonResult<String> batchDelete(@RequestBody @Valid @NotEmpty(message = "集合不能为空")
                                           List<PaymentConfigIdParam> paymentConfigIdParamList) {
        paymentConfigService.batchDelete(paymentConfigIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取支付配置选择器
     *
     * @author wqs
     * @date 2025/01/30
     */
    @ApiOperationSupport(order = 9)
    @Operation(summary = "获取支付配置选择器")
    @SaCheckPermission("/payment/record/selector")
    @GetMapping("/payment/config/selector")
    public CommonResult<List<PaymentConfig>> selector(PaymentConfigPageParam paymentConfigPageParam) {
        return CommonResult.data(paymentConfigService.selector(paymentConfigPageParam));
    }

    /**
     * 获取所有启用的支付配置
     *
     * @author wqs
     * @date 2025/01/30
     */
    @ApiOperationSupport(order = 10)
    @Operation(summary = "获取所有启用的支付配置")
    @SaCheckPermission("/payment/record/enabled")
    @GetMapping("/payment/config/enabled")
    public CommonResult<List<PaymentConfig>> enabled() {
        return CommonResult.data(paymentConfigService.getEnabledConfigs());
    }
} 