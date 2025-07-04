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
package vip.wqs.store.modular.manage.controller;

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
import vip.wqs.store.modular.manage.entity.WineStore;
import vip.wqs.store.modular.manage.param.WineStoreAddParam;
import vip.wqs.store.modular.manage.param.WineStoreEditParam;
import vip.wqs.store.modular.manage.param.WineStoreIdParam;
import vip.wqs.store.modular.manage.param.WineStorePageParam;
import vip.wqs.store.modular.manage.service.WineStoreService;

import java.util.List;

/**
 * 门店控制器
 *
 * @author wqs
 * @date 2025/1/27
 **/
@Tag(name = "门店控制器")
@ApiSupport(author = "WQS_TEAM", order = 1)
@RestController
@Validated
public class WineStoreController {

    @Resource
    private WineStoreService wineStoreService;

    /**
     * 获取门店分页
     *
     * @author wqs
     * @date 2025/1/27
     */
    @ApiOperationSupport(order = 1)
    @Operation(summary = "获取门店分页")
    @SaCheckPermission("/store/record/page")
    @GetMapping("/store/manage/page")
    public CommonResult<Page<WineStore>> page(WineStorePageParam wineStorePageParam) {
        return CommonResult.data(wineStoreService.page(wineStorePageParam));
    }

    /**
     * 添加门店
     *
     * @author wqs
     * @date 2025/1/27
     */
    @ApiOperationSupport(order = 2)
    @Operation(summary = "添加门店")
    @CommonLog("添加门店")
    @SaCheckPermission("/store/record/add")
    @PostMapping("/store/manage/add")
    public CommonResult<String> add(@RequestBody @Valid WineStoreAddParam wineStoreAddParam) {
        wineStoreService.add(wineStoreAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑门店
     *
     * @author wqs
     * @date 2025/1/27
     */
    @ApiOperationSupport(order = 3)
    @Operation(summary = "编辑门店")
    @CommonLog("编辑门店")
    @SaCheckPermission("/store/record/edit")
    @PostMapping("/store/manage/edit")
    public CommonResult<String> edit(@RequestBody @Valid WineStoreEditParam wineStoreEditParam) {
        wineStoreService.edit(wineStoreEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除门店
     *
     * @author wqs
     * @date 2025/1/27
     */
    @ApiOperationSupport(order = 4)
    @Operation(summary = "删除门店")
    @CommonLog("删除门店")
    @SaCheckPermission("/store/record/delete")
    @PostMapping("/store/manage/delete")
    public CommonResult<String> delete(@RequestBody @Valid WineStoreIdParam wineStoreIdParam) {
        wineStoreService.delete(wineStoreIdParam);
        return CommonResult.ok();
    }

    /**
     * 获取门店详情
     *
     * @author wqs
     * @date 2025/1/27
     */
    @ApiOperationSupport(order = 5)
    @Operation(summary = "获取门店详情")
    @SaCheckPermission("/store/record/detail")
    @GetMapping("/store/manage/detail")
    public CommonResult<WineStore> detail(@Valid WineStoreIdParam wineStoreIdParam) {
        return CommonResult.data(wineStoreService.detail(wineStoreIdParam));
    }

    /**
     * 禁用门店
     *
     * @author wqs
     * @date 2025/1/27
     */
    @ApiOperationSupport(order = 6)
    @Operation(summary = "禁用门店")
    @CommonLog("禁用门店")
    @SaCheckPermission("/store/record/disable")
    @PostMapping("/store/manage/disable")
    public CommonResult<String> disable(@RequestBody @Valid WineStoreIdParam wineStoreIdParam) {
        wineStoreService.disable(wineStoreIdParam);
        return CommonResult.ok();
    }

    /**
     * 启用门店
     *
     * @author wqs
     * @date 2025/1/27
     */
    @ApiOperationSupport(order = 7)
    @Operation(summary = "启用门店")
    @CommonLog("启用门店")
    @SaCheckPermission("/store/record/enable")
    @PostMapping("/store/manage/enable")
    public CommonResult<String> enable(@RequestBody @Valid WineStoreIdParam wineStoreIdParam) {
        wineStoreService.enable(wineStoreIdParam);
        return CommonResult.ok();
    }

    /**
     * 导出门店
     *
     * @author wqs
     * @date 2025/1/27
     */
    @ApiOperationSupport(order = 8)
    @Operation(summary = "导出门店")
    @CommonLog("导出门店")
    @SaCheckPermission("/store/record/export")
    @GetMapping("/store/manage/export")
    public void export(WineStorePageParam wineStorePageParam) {
        wineStoreService.export(wineStorePageParam);
    }

    /**
     * 门店选择器
     *
     * @author wqs
     * @date 2025/1/27
     */
    @ApiOperationSupport(order = 9)
    @Operation(summary = "门店选择器")
    @SaCheckPermission("/store/record/selector")
    @GetMapping("/store/manage/selector")
    public CommonResult<List<WineStore>> selector(WineStorePageParam wineStorePageParam) {
        return CommonResult.data(wineStoreService.selector(wineStorePageParam));
    }

    /**
     * 批量删除门店
     *
     * @author wqs
     * @date 2025/1/27
     */
    @ApiOperationSupport(order = 10)
    @Operation(summary = "批量删除门店")
    @CommonLog("批量删除门店")
    @SaCheckPermission("/store/record/batchDelete")
    @PostMapping("/store/manage/batchDelete")
    public CommonResult<String> batchDelete(@RequestBody @Valid @NotEmpty(message = "集合不能为空")
                                                   List<WineStoreIdParam> wineStoreIdParamList) {
        wineStoreService.batchDelete(wineStoreIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取管理员用户选择器
     *
     * @author wqs
     * @date 2025/1/27
     */
    @ApiOperationSupport(order = 11)
    @Operation(summary = "获取管理员用户选择器")
    @SaCheckPermission("/store/record/managerUserSelector")
    @GetMapping("/store/manage/managerUserSelector")
    public CommonResult<Object> managerUserSelector() {
        return CommonResult.data(wineStoreService.managerUserSelector());
    }

    /**
     * 获取门店列表
     *
     * @author wqs
     * @date 2025/1/27
     */
    @ApiOperationSupport(order = 12)
    @Operation(summary = "获取门店列表")
    @SaCheckPermission("/store/record/list")
    @GetMapping("/store/manage/list")
    public CommonResult<List<WineStore>> list(WineStorePageParam wineStorePageParam) {
        return CommonResult.data(wineStoreService.list(wineStorePageParam));
    }

    /**
     * 设置定价权限
     *
     * @author wqs
     * @date 2025/1/27
     */
    @ApiOperationSupport(order = 13)
    @Operation(summary = "设置定价权限")
    @CommonLog("设置定价权限")
    @SaCheckPermission("/store/record/setPriceAuthority")
    @PostMapping("/store/manage/setPriceAuthority")
    public CommonResult<String> setPriceAuthority(@RequestBody @Valid WineStoreIdParam wineStoreIdParam,
                                                  String priceAuthority) {
        wineStoreService.setPriceAuthority(wineStoreIdParam, priceAuthority);
        return CommonResult.ok();
    }
} 