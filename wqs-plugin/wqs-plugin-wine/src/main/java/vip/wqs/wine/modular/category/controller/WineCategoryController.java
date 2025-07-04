/*
 * Copyright [2022] [https://www.wqs.vip]
 *
 * WQS采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改WQS源码头部的版权声明。
 * 3.本项目代码可免费商业使用，商业使用请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://www.wqs.vip
 * 5.不可二次分发开源参与同类竞品，如需要参与同类竞品请联系官方购买商业授权合同。
 * 6.若您的项目无法满足以上几点，需要更多功能代码，获取WQS商业授权许可，请在官网购买授权，地址为 https://www.wqs.vip
 */
package vip.wqs.wine.modular.category.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vip.wqs.common.annotation.CommonLog;
import vip.wqs.common.pojo.CommonResult;
import vip.wqs.wine.modular.category.entity.WineCategory;
import vip.wqs.wine.modular.category.param.*;
import vip.wqs.wine.modular.category.service.WineCategoryService;

import javax.validation.Valid;
import java.util.List;

/**
 * 酒品分类控制器
 *
 * @author wqs
 * @date 2025/01/27
 */
@Tag(name = "酒品分类控制器")
@ApiSupport(author = "WQS", order = 2)
@RestController
@Validated
public class WineCategoryController {

    @Resource
    private WineCategoryService wineCategoryService;

    /**
     * 获取酒品分类分页
     */
    @Operation(summary = "获取酒品分类分页")
    @ApiOperationSupport(order = 1)
    @SaCheckPermission("/wine/category/page")
    @GetMapping("/wine/category/page")
    public CommonResult<Page<WineCategory>> page(@Valid WineCategoryPageParam wineCategoryPageParam) {
        return CommonResult.data(wineCategoryService.page(wineCategoryPageParam));
    }

    /**
     * 获取酒品分类列表
     */
    @Operation(summary = "获取酒品分类列表")
    @ApiOperationSupport(order = 2)
    @SaCheckPermission("/wine/category/list")
    @GetMapping("/wine/category/list")
    public CommonResult<List<WineCategory>> list(@Valid WineCategoryPageParam wineCategoryPageParam) {
        return CommonResult.data(wineCategoryService.list(wineCategoryPageParam));
    }

    /**
     * 添加酒品分类
     */
    @Operation(summary = "添加酒品分类")
    @ApiOperationSupport(order = 3)
    @CommonLog("添加酒品分类")
    @SaCheckPermission("/wine/category/add")
    @PostMapping("/wine/category/add")
    public CommonResult<String> add(@RequestBody @Valid WineCategoryAddParam wineCategoryAddParam) {
        wineCategoryService.add(wineCategoryAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑酒品分类
     */
    @Operation(summary = "编辑酒品分类")
    @ApiOperationSupport(order = 4)
    @CommonLog("编辑酒品分类")
    @SaCheckPermission("/wine/category/edit")
    @PostMapping("/wine/category/edit")
    public CommonResult<String> edit(@RequestBody @Valid WineCategoryEditParam wineCategoryEditParam) {
        wineCategoryService.edit(wineCategoryEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除酒品分类
     */
    @Operation(summary = "删除酒品分类")
    @ApiOperationSupport(order = 5)
    @CommonLog("删除酒品分类")
    @SaCheckPermission("/wine/category/delete")
    @PostMapping("/wine/category/delete")
    public CommonResult<String> delete(@RequestBody @Valid WineCategoryIdParam wineCategoryIdParam) {
        wineCategoryService.delete(wineCategoryIdParam);
        return CommonResult.ok();
    }

    /**
     * 获取酒品分类详情
     */
    @Operation(summary = "获取酒品分类详情")
    @ApiOperationSupport(order = 6)
    @SaCheckPermission("/wine/category/detail")
    @GetMapping("/wine/category/detail")
    public CommonResult<WineCategory> detail(@Valid WineCategoryIdParam wineCategoryIdParam) {
        return CommonResult.data(wineCategoryService.detail(wineCategoryIdParam));
    }

    /**
     * 启用酒品分类
     */
    @Operation(summary = "启用酒品分类")
    @ApiOperationSupport(order = 7)
    @CommonLog("启用酒品分类")
    @SaCheckPermission("/wine/category/enable")
    @PostMapping("/wine/category/enable")
    public CommonResult<String> enable(@RequestBody @Valid WineCategoryIdParam wineCategoryIdParam) {
        wineCategoryService.enable(wineCategoryIdParam);
        return CommonResult.ok();
    }

    /**
     * 禁用酒品分类
     */
    @Operation(summary = "禁用酒品分类")
    @ApiOperationSupport(order = 8)
    @CommonLog("禁用酒品分类")
    @SaCheckPermission("/wine/category/disable")
    @PostMapping("/wine/category/disable")
    public CommonResult<String> disable(@RequestBody @Valid WineCategoryIdParam wineCategoryIdParam) {
        wineCategoryService.disable(wineCategoryIdParam);
        return CommonResult.ok();
    }

    /**
     * 导出酒品分类
     */
    @Operation(summary = "导出酒品分类")
    @ApiOperationSupport(order = 9)
    @CommonLog("导出酒品分类")
    @SaCheckPermission("/wine/category/export")
    @PostMapping("/wine/category/export")
    public void export(@RequestBody @Valid WineCategoryExportParam wineCategoryExportParam) {
        wineCategoryService.export(wineCategoryExportParam);
    }

    /**
     * 酒品分类选择器
     */
    @Operation(summary = "酒品分类选择器")
    @ApiOperationSupport(order = 10)
    @SaCheckPermission("/wine/category/wineCategorySelector")
    @GetMapping("/wine/category/wineCategorySelector")
    public CommonResult<List<WineCategory>> selector(@Valid WineCategorySelectorParam wineCategorySelectorParam) {
        return CommonResult.data(wineCategoryService.selector(wineCategorySelectorParam));
    }

    /**
     * 根据父级ID获取子分类列表
     */
    @Operation(summary = "根据父级ID获取子分类列表")
    @ApiOperationSupport(order = 11)
    @SaCheckPermission("/wine/category/children")
    @GetMapping("/wine/category/children")
    public CommonResult<List<WineCategory>> getChildrenByParentId(@RequestParam String parentId) {
        return CommonResult.data(wineCategoryService.getChildrenByParentId(parentId));
    }

    /**
     * 根据分类编码获取分类信息
     */
    @Operation(summary = "根据分类编码获取分类信息")
    @ApiOperationSupport(order = 12)
    @SaCheckPermission("/wine/category/getByCategoryCode")
    @GetMapping("/wine/category/getByCategoryCode")
    public CommonResult<WineCategory> getByCategoryCode(@RequestParam String categoryCode) {
        return CommonResult.data(wineCategoryService.getByCategoryCode(categoryCode));
    }

    /**
     * 获取酒品分类树形结构
     */
    @Operation(summary = "获取酒品分类树形结构")
    @ApiOperationSupport(order = 13)
    @SaCheckPermission("/wine/category/tree")
    @GetMapping("/wine/category/tree")
    public CommonResult<List<WineCategory>> tree(@Valid WineCategoryPageParam wineCategoryPageParam) {
        return CommonResult.data(wineCategoryService.tree(wineCategoryPageParam));
    }

    /**
     * 批量删除酒品分类
     */
    @Operation(summary = "批量删除酒品分类")
    @ApiOperationSupport(order = 14)
    @CommonLog("批量删除酒品分类")
    @SaCheckPermission("/wine/category/batchDelete")
    @PostMapping("/wine/category/batchDelete")
    public CommonResult<String> batchDelete(@RequestBody @Valid List<WineCategoryIdParam> wineCategoryIdParamList) {
        wineCategoryService.batchDelete(wineCategoryIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取父级分类选择器
     */
    @Operation(summary = "获取父级分类选择器")
    @ApiOperationSupport(order = 15)
    @SaCheckPermission("/wine/category/parentSelector")
    @GetMapping("/wine/category/parentSelector")
    public CommonResult<List<WineCategory>> parentSelector() {
        return CommonResult.data(wineCategoryService.parentSelector());
    }

    /**
     * 移动酒品分类
     *
     * @author WQS_TEAM
     * @date 2025-01-27
     */
    @ApiOperationSupport(order = 16)
    @Operation(summary = "移动酒品分类")
    @CommonLog("移动酒品分类")
    @SaCheckPermission("/wine/category/move")
    @PostMapping("/wine/category/move")
    public CommonResult<String> move(@RequestBody @Valid WineCategoryMoveParam moveParam) {
        wineCategoryService.move(moveParam);
        return CommonResult.ok();
    }

    /**
     * 酒品分类排序
     *
     * @author WQS_TEAM
     * @date 2025-01-27
     */
    @ApiOperationSupport(order = 17)
    @Operation(summary = "酒品分类排序")
    @CommonLog("酒品分类排序")
    @SaCheckPermission("/wine/category/sort")
    @PostMapping("/wine/category/sort")
    public CommonResult<String> sort(@RequestBody @Valid WineCategorySortParam sortParam) {
        wineCategoryService.sort(sortParam);
        return CommonResult.ok();
    }
} 