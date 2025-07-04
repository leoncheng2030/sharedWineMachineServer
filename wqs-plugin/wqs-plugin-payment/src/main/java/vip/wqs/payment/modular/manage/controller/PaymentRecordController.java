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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.wqs.common.pojo.CommonResult;
import vip.wqs.payment.modular.manage.entity.PaymentRecord;
import vip.wqs.payment.modular.manage.param.PaymentRecordIdParam;
import vip.wqs.payment.modular.manage.param.PaymentRecordPageParam;
import vip.wqs.payment.modular.manage.service.PaymentRecordService;

/**
 * 支付记录控制器
 *
 * @author wqs
 * @date 2025/01/30
 */
@Tag(name = "支付记录控制器")
@ApiSupport(author = "WQS_TEAM", order = 16)
@RestController
@Validated
public class PaymentRecordController {

    @Resource
    private PaymentRecordService paymentRecordService;

    /**
     * 获取支付记录分页
     *
     * @author wqs
     * @date 2025/01/30
     */
    @ApiOperationSupport(order = 1)
    @Operation(summary = "获取支付记录分页")
    @SaCheckPermission("/payment/record/page")
    @GetMapping("/payment/record/page")
    public CommonResult<Page<PaymentRecord>> page(PaymentRecordPageParam paymentRecordPageParam) {
        return CommonResult.data(paymentRecordService.page(paymentRecordPageParam));
    }

    /**
     * 获取支付记录详情
     *
     * @author wqs
     * @date 2025/01/30
     */
    @ApiOperationSupport(order = 2)
    @Operation(summary = "获取支付记录详情")
    @SaCheckPermission("/payment/record/detail")
    @GetMapping("/payment/record/detail")
    public CommonResult<PaymentRecord> detail(@Valid PaymentRecordIdParam paymentRecordIdParam) {
        return CommonResult.data(paymentRecordService.detail(paymentRecordIdParam));
    }
} 