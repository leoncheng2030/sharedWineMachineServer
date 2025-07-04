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
package vip.wqs.wine.modular.product.controller;

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
import vip.wqs.wine.modular.product.entity.WineProduct;
import vip.wqs.wine.modular.product.param.*;
import vip.wqs.wine.modular.product.service.WineProductService;

import java.util.List;

/**
 * 酒品控制器
 *
 * @author wqs
 * @date 2025/1/27
 **/
@Tag(name = "酒品控制器")
@ApiSupport(author = "WQS_TEAM", order = 1)
@RestController
@Validated
public class WineProductController {

    @Resource
    private WineProductService wineProductService;

    /**
     * 获取酒品分页
     *
     * @author wqs
     * @date 2025/1/27
     */
    @ApiOperationSupport(order = 1)
    @Operation(summary = "获取酒品分页")
    @SaCheckPermission("/wine/product/page")
    @GetMapping("/wine/product/page")
    public CommonResult<Page<WineProduct>> page(WineProductPageParam wineProductPageParam) {
        return CommonResult.data(wineProductService.page(wineProductPageParam));
    }

    /**
     * 添加酒品
     *
     * @author wqs
     * @date 2025/1/27
     */
    @ApiOperationSupport(order = 2)
    @Operation(summary = "添加酒品")
    @CommonLog("添加酒品")
    @SaCheckPermission("/wine/product/add")
    @PostMapping("/wine/product/add")
    public CommonResult<String> add(@RequestBody @Valid WineProductAddParam wineProductAddParam) {
        wineProductService.add(wineProductAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑酒品
     *
     * @author wqs
     * @date 2025/1/27
     */
    @ApiOperationSupport(order = 3)
    @Operation(summary = "编辑酒品")
    @CommonLog("编辑酒品")
    @SaCheckPermission("/wine/product/edit")
    @PostMapping("/wine/product/edit")
    public CommonResult<String> edit(@RequestBody @Valid WineProductEditParam wineProductEditParam) {
        wineProductService.edit(wineProductEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除酒品
     *
     * @author wqs
     * @date 2025/1/27
     */
    @ApiOperationSupport(order = 4)
    @Operation(summary = "删除酒品")
    @CommonLog("删除酒品")
    @SaCheckPermission("/wine/product/delete")
    @PostMapping("/wine/product/delete")
    public CommonResult<String> delete(@RequestBody @Valid WineProductIdParam wineProductIdParam) {
        wineProductService.delete(wineProductIdParam);
        return CommonResult.ok();
    }

    /**
     * 获取酒品详情
     *
     * @author wqs
     * @date 2025/1/27
     */
    @ApiOperationSupport(order = 5)
    @Operation(summary = "获取酒品详情")
    @SaCheckPermission("/wine/product/detail")
    @PostMapping("/wine/product/detail")
    public CommonResult<WineProduct> detail(@RequestBody @Valid WineProductIdParam wineProductIdParam) {
        return CommonResult.data(wineProductService.detail(wineProductIdParam));
    }

    /**
     * 禁用酒品
     *
     * @author wqs
     * @date 2025/1/27
     */
    @ApiOperationSupport(order = 6)
    @Operation(summary = "禁用酒品")
    @CommonLog("禁用酒品")
    @SaCheckPermission("/wine/product/disable")
    @PostMapping("/wine/product/disable")
    public CommonResult<String> disable(@RequestBody @Valid WineProductIdParam wineProductIdParam) {
        wineProductService.disable(wineProductIdParam);
        return CommonResult.ok();
    }

    /**
     * 启用酒品
     *
     * @author wqs
     * @date 2025/1/27
     */
    @ApiOperationSupport(order = 7)
    @Operation(summary = "启用酒品")
    @CommonLog("启用酒品")
    @SaCheckPermission("/wine/product/enable")
    @PostMapping("/wine/product/enable")
    public CommonResult<String> enable(@RequestBody @Valid WineProductIdParam wineProductIdParam) {
        wineProductService.enable(wineProductIdParam);
        return CommonResult.ok();
    }

    /**
     * 导出酒品
     *
     * @author wqs
     * @date 2025/1/27
     */
    @ApiOperationSupport(order = 8)
    @Operation(summary = "导出酒品")
    @CommonLog("导出酒品")
    @SaCheckPermission("/wine/product/export")
    @GetMapping("/wine/product/export")
    public void export(WineProductExportParam wineProductExportParam) {
        wineProductService.export(wineProductExportParam);
    }

    /**
     * 酒品选择器
     *
     * @author wqs
     * @date 2025/1/27
     */
    @ApiOperationSupport(order = 9)
    @Operation(summary = "酒品选择器")
    @SaCheckPermission("/wine/product/selector")
    @GetMapping("/wine/product/selector")
    public CommonResult<List<WineProduct>> selector(WineProductSelectorParam wineProductSelectorParam) {
        return CommonResult.data(wineProductService.selector(wineProductSelectorParam));
    }

    /**
     * 批量删除酒品
     *
     * @author wqs
     * @date 2025/1/27
     */
    @ApiOperationSupport(order = 10)
    @Operation(summary = "批量删除酒品")
    @CommonLog("批量删除酒品")
    @SaCheckPermission("/wine/product/batchDelete")
    @PostMapping("/wine/product/batchDelete")
    public CommonResult<String> batchDelete(@RequestBody @Valid @NotEmpty(message = "集合不能为空")
                                                   List<WineProductIdParam> wineProductIdParamList) {
        wineProductService.batchDelete(wineProductIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取酒品分类选择器
     *
     * @author wqs
     * @date 2025/1/27
     */
    @ApiOperationSupport(order = 11)
    @Operation(summary = "获取酒品分类选择器")
    @SaCheckPermission("/wine/product/categorySelector")
    @GetMapping("/wine/product/categorySelector")
    public CommonResult<Object> categorySelector() {
        return CommonResult.data(wineProductService.categorySelector());
    }

    /**
     * 获取酒品列表（不分页）
     *
     * @author wqs
     * @date 2025/1/27
     */
    @ApiOperationSupport(order = 12)
    @Operation(summary = "获取酒品列表")
    @SaCheckPermission("/wine/product/list")
    @GetMapping("/wine/product/list")
    public CommonResult<List<WineProduct>> list(WineProductPageParam wineProductPageParam) {
        return CommonResult.data(wineProductService.list(wineProductPageParam));
    }

    /**
     * 上传酒品图片
     *
     * @author wqs
     * @date 2025/1/27
     */
    @ApiOperationSupport(order = 13)
    @Operation(summary = "上传酒品图片")
    @CommonLog("上传酒品图片")
    @SaCheckPermission("/wine/product/uploadImage")
    @PostMapping("/wine/product/uploadImage")
    public CommonResult<Object> uploadImage(@RequestBody String imageData) {
        return CommonResult.data(wineProductService.uploadImage(imageData));
    }

    /**
     * 获取酒品库存信息
     *
     * @author wqs
     * @date 2025/1/27
     */
    @ApiOperationSupport(order = 14)
    @Operation(summary = "获取酒品库存信息")
    @SaCheckPermission("/wine/product/stock")
    @GetMapping("/wine/product/stock")
    public CommonResult<Object> stock(@Valid WineProductIdParam wineProductIdParam) {
        return CommonResult.data(wineProductService.getStock(wineProductIdParam));
    }

    /**
     * 更新酒品库存
     *
     * @author WQS_TEAM
     * @date 2025-01-27
     */
    @ApiOperationSupport(order = 15)
    @Operation(summary = "更新酒品库存")
    @CommonLog("更新酒品库存")
    @SaCheckPermission("/wine/product/updateStock")
    @PostMapping("/wine/product/updateStock")
    public CommonResult<String> updateStock(@RequestBody @Valid WineProductStockUpdateParam stockUpdateParam) {
        wineProductService.updateStock(stockUpdateParam);
        return CommonResult.ok();
    }
} 