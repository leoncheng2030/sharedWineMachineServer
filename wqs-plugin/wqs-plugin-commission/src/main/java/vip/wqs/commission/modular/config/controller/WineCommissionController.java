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
package vip.wqs.commission.modular.config.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vip.wqs.commission.modular.config.entity.WineCommission;
import vip.wqs.commission.modular.config.param.WineCommissionAddParam;
import vip.wqs.commission.modular.config.param.WineCommissionEditParam;
import vip.wqs.commission.modular.config.param.WineCommissionIdParam;
import vip.wqs.commission.modular.config.param.WineCommissionPageParam;
import vip.wqs.commission.modular.config.service.WineCommissionService;
import vip.wqs.common.pojo.CommonResult;

import java.util.List;

/**
 * 分销配置控制器
 *
 * @author WQS_TEAM
 * @date 2025-01-29
 */
@Tag(name = "分销配置控制器")
@RestController
@RequestMapping("/commission/config")
@Validated
public class WineCommissionController {

    @Resource
    private WineCommissionService wineCommissionService;

    /**
     * 获取分销配置分页
     */
    @Operation(summary = "获取分销配置分页")
    @SaCheckPermission("/commission/record/page")
    @GetMapping("/page")
    public CommonResult<Page<WineCommission>> page(WineCommissionPageParam wineCommissionPageParam) {
        return CommonResult.data(wineCommissionService.page(wineCommissionPageParam));
    }

    /**
     * 添加分销配置
     */
    @Operation(summary = "添加分销配置")
    @SaCheckPermission("/commission/record/add")
    @PostMapping("/add")
    public CommonResult<String> add(@RequestBody @Valid WineCommissionAddParam wineCommissionAddParam) {
        wineCommissionService.add(wineCommissionAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑分销配置
     */
    @Operation(summary = "编辑分销配置")
    @SaCheckPermission("/commission/record/edit")
    @PostMapping("/edit")
    public CommonResult<String> edit(@RequestBody @Valid WineCommissionEditParam wineCommissionEditParam) {
        wineCommissionService.edit(wineCommissionEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除分销配置
     */
    @Operation(summary = "删除分销配置")
    @SaCheckPermission("/commission/record/delete")
    @PostMapping("/delete")
    public CommonResult<String> delete(@RequestBody @Valid WineCommissionIdParam wineCommissionIdParam) {
        wineCommissionService.delete(wineCommissionIdParam);
        return CommonResult.ok();
    }

    /**
     * 批量删除分销配置
     */
    @Operation(summary = "批量删除分销配置")
    @SaCheckPermission("/commission/record/batchDelete")
    @PostMapping("/batchDelete")
    public CommonResult<String> batchDelete(@RequestBody @Valid @NotEmpty(message = "集合不能为空") List<WineCommissionIdParam> wineCommissionIdParamList) {
        wineCommissionService.batchDelete(wineCommissionIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取分销配置详情
     */
    @Operation(summary = "获取分销配置详情")
    @SaCheckPermission("/commission/record/detail")
    @GetMapping("/detail")
    public CommonResult<WineCommission> detail(@Valid WineCommissionIdParam wineCommissionIdParam) {
        return CommonResult.data(wineCommissionService.detail(wineCommissionIdParam));
    }

    /**
     * 根据门店和酒品获取分销配置
     */
    @Operation(summary = "根据门店和酒品获取分销配置")
    @SaCheckPermission("/commission/record/getByStoreAndProduct")
    @GetMapping("/getByStoreAndProduct")
    public CommonResult<WineCommission> getByStoreAndProduct(@RequestParam String storeId, @RequestParam String productId) {
        return CommonResult.data(wineCommissionService.getCommissionByStoreAndProduct(storeId, productId));
    }

    /**
     * 获取门店的所有分销配置
     */
    @Operation(summary = "获取门店的所有分销配置")
    @SaCheckPermission("/commission/record/getByStoreId")
    @GetMapping("/getByStoreId")
    public CommonResult<List<WineCommission>> getByStoreId(@RequestParam String storeId) {
        return CommonResult.data(wineCommissionService.getByStoreId(storeId));
    }

    /**
     * 获取酒品的所有分销配置
     */
    @Operation(summary = "获取酒品的所有分销配置")
    @SaCheckPermission("/commission/record/getByProductId")
    @GetMapping("/getByProductId")
    public CommonResult<List<WineCommission>> getByProductId(@RequestParam String productId) {
        return CommonResult.data(wineCommissionService.getByProductId(productId));
    }

    /**
     * 获取平台默认配置
     */
    @Operation(summary = "获取平台默认配置")
    @SaCheckPermission("/commission/record/getDefaultConfig")
    @GetMapping("/getDefaultConfig")
    public CommonResult<WineCommission> getDefaultConfig() {
        return CommonResult.data(wineCommissionService.getDefaultConfig());
    }

    /**
     * 启用分销配置
     */
    @Operation(summary = "启用分销配置")
    @SaCheckPermission("/commission/record/enable")
    @PostMapping("/enable")
    public CommonResult<String> enable(@RequestBody @Valid WineCommissionIdParam wineCommissionIdParam) {
        wineCommissionService.enable(wineCommissionIdParam);
        return CommonResult.ok();
    }

    /**
     * 禁用分销配置
     */
    @Operation(summary = "禁用分销配置")
    @SaCheckPermission("/commission/record/disable")
    @PostMapping("/disable")
    public CommonResult<String> disable(@RequestBody @Valid WineCommissionIdParam wineCommissionIdParam) {
        wineCommissionService.disable(wineCommissionIdParam);
        return CommonResult.ok();
    }

    /**
     * 批量启用分销配置
     */
    @Operation(summary = "批量启用分销配置")
    @SaCheckPermission("/commission/record/batchEnable")
    @PostMapping("/batchEnable")
    public CommonResult<String> batchEnable(@RequestBody @Valid @NotEmpty(message = "集合不能为空") List<WineCommissionIdParam> wineCommissionIdParamList) {
        wineCommissionService.batchEnable(wineCommissionIdParamList);
        return CommonResult.ok();
    }

    /**
     * 批量禁用分销配置
     */
    @Operation(summary = "批量禁用分销配置")
    @SaCheckPermission("/commission/record/batchDisable")
    @PostMapping("/batchDisable")
    public CommonResult<String> batchDisable(@RequestBody @Valid @NotEmpty(message = "集合不能为空") List<WineCommissionIdParam> wineCommissionIdParamList) {
        wineCommissionService.batchDisable(wineCommissionIdParamList);
        return CommonResult.ok();
    }



    /**
     * 批量设置分销配置
     */
    @Operation(summary = "批量设置分销配置")
    @SaCheckPermission("/commission/record/batchSet")
    @PostMapping("/batchSet")
    public CommonResult<String> batchSet(@RequestParam List<String> storeIds, 
                                        @RequestParam List<String> productIds,
                                        @RequestBody @Valid WineCommissionAddParam wineCommissionAddParam) {
        wineCommissionService.batchSet(storeIds, productIds, wineCommissionAddParam);
        return CommonResult.ok();
    }
} 