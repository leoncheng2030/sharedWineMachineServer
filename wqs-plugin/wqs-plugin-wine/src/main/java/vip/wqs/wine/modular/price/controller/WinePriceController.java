/*
 * Copyright [2022] [https://www.xiaonuo.vip]
 *
 * Snowy采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改Snowy源码头部的版权声明。
 * 3.本项目代码可免费商业使用，商业使用请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://www.xiaonuo.vip
 * 5.不可二次分发开源参与同类竞品，如有想法可联系团队xiaonuobase@qq.com商议合作。
 * 6.若您的项目无法满足以上几点，需要更多功能代码，获取Snowy商业授权许可，请在官网购买授权，地址为 https://www.xiaonuo.vip
 */

package vip.wqs.wine.modular.price.controller;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vip.wqs.common.annotation.CommonLog;
import vip.wqs.common.pojo.CommonResult;
import vip.wqs.wine.modular.price.entity.WinePrice;
import vip.wqs.wine.modular.price.param.*;
import vip.wqs.wine.modular.price.service.WinePriceService;

import java.util.List;

/**
 * 酒品价格控制器
 *
 * @author WQS_TEAM
 * @date 2025-01-27
 */
@Tag(name = "酒品价格控制器")
@ApiSupport(author = "WQS_TEAM", order = 3)
@RestController
@Validated
public class WinePriceController {

    @Resource
    private WinePriceService winePriceService;

    /**
     * 获取酒品价格分页
     *
     * @author WQS_TEAM
     * @date 2025-01-27
     */
    @ApiOperationSupport(order = 1)
    @Operation(summary = "获取酒品价格分页")
    @SaCheckPermission("/wine/price/page")
    @GetMapping("/wine/price/page")
    public CommonResult<Page<WinePrice>> page(WinePricePageParam winePricePageParam) {
        return CommonResult.data(winePriceService.page(winePricePageParam));
    }

    /**
     * 添加酒品价格
     *
     * @author WQS_TEAM
     * @date 2025-01-27
     */
    @ApiOperationSupport(order = 2)
    @Operation(summary = "添加酒品价格")
    @CommonLog("添加酒品价格")
    @SaCheckPermission("/wine/price/add")
    @PostMapping("/wine/price/add")
    public CommonResult<String> add(@RequestBody @Valid WinePriceAddParam winePriceAddParam) {
        winePriceService.add(winePriceAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑酒品价格
     *
     * @author WQS_TEAM
     * @date 2025-01-27
     */
    @ApiOperationSupport(order = 3)
    @Operation(summary = "编辑酒品价格")
    @CommonLog("编辑酒品价格")
    @SaCheckPermission("/wine/price/edit")
    @PostMapping("/wine/price/edit")
    public CommonResult<String> edit(@RequestBody @Valid WinePriceEditParam winePriceEditParam) {
        winePriceService.edit(winePriceEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除酒品价格
     *
     * @author WQS_TEAM
     * @date 2025-01-27
     */
    @ApiOperationSupport(order = 4)
    @Operation(summary = "删除酒品价格")
    @CommonLog("删除酒品价格")
    @SaCheckPermission("/wine/price/delete")
    @PostMapping("/wine/price/delete")
    public CommonResult<String> delete(@RequestBody @Valid WinePriceIdParam winePriceIdParam) {
        winePriceService.delete(winePriceIdParam);
        return CommonResult.ok();
    }

    /**
     * 批量删除酒品价格
     *
     * @author WQS_TEAM
     * @date 2025-01-27
     */
    @ApiOperationSupport(order = 5)
    @Operation(summary = "批量删除酒品价格")
    @CommonLog("批量删除酒品价格")
    @SaCheckPermission("/wine/price/batchDelete")
    @PostMapping("/wine/price/batchDelete")
    public CommonResult<String> batchDelete(@RequestBody @Valid List<WinePriceIdParam> winePriceIdParamList) {
        winePriceService.batchDelete(winePriceIdParamList);
        return CommonResult.ok();
    }

    /**
     * 启用价格策略
     *
     * @author WQS_TEAM
     * @date 2025-01-27
     */
    @ApiOperationSupport(order = 6)
    @Operation(summary = "启用价格策略")
    @CommonLog("启用价格策略")
    @SaCheckPermission("/wine/price/enable")
    @PostMapping("/wine/price/enable")
    public CommonResult<String> enable(@RequestBody @Valid WinePriceIdParam winePriceIdParam) {
        winePriceService.enable(winePriceIdParam);
        return CommonResult.ok();
    }

    /**
     * 禁用价格策略
     *
     * @author WQS_TEAM
     * @date 2025-01-27
     */
    @ApiOperationSupport(order = 7)
    @Operation(summary = "禁用价格策略")
    @CommonLog("禁用价格策略")
    @SaCheckPermission("/wine/price/disable")
    @PostMapping("/wine/price/disable")
    public CommonResult<String> disable(@RequestBody @Valid WinePriceIdParam winePriceIdParam) {
        winePriceService.disable(winePriceIdParam);
        return CommonResult.ok();
    }

    /**
     * 获取酒品选择器
     *
     * @author WQS_TEAM
     * @date 2025-01-27
     */
    @ApiOperationSupport(order = 8)
    @Operation(summary = "获取酒品选择器")
    @SaCheckPermission("/wine/price/productSelector")
    @GetMapping("/wine/price/productSelector")
    public CommonResult<Object> productSelector() {
        return CommonResult.data(winePriceService.productSelector());
    }

    /**
     * 导出酒品价格
     *
     * @author WQS_TEAM
     * @date 2025-01-27
     */
    @ApiOperationSupport(order = 9)
    @Operation(summary = "导出酒品价格")
    @CommonLog("导出酒品价格")
    @SaCheckPermission("/wine/price/export")
    @PostMapping("/wine/price/export")
    public void export(@RequestBody @Valid WinePriceExportParam exportParam) {
        winePriceService.export(exportParam);
    }

    /**
     * 获取价格策略列表（不分页）
     *
     * @author WQS_TEAM
     * @date 2025-01-27
     */
    @ApiOperationSupport(order = 10)
    @Operation(summary = "获取价格策略列表")
    @SaCheckPermission("/wine/price/list")
    @GetMapping("/wine/price/list")
    public CommonResult<List<WinePrice>> list(WinePricePageParam winePricePageParam) {
        return CommonResult.data(winePriceService.list(winePricePageParam));
    }

    /**
     * 批量设置酒品价格
     *
     * @author WQS_TEAM
     * @date 2025-01-27
     */
    @ApiOperationSupport(order = 11)
    @Operation(summary = "批量设置酒品价格")
    @CommonLog("批量设置酒品价格")
    @SaCheckPermission("/wine/price/batchSet")
    @PostMapping("/wine/price/batchSet")
    public CommonResult<String> batchSet(@RequestBody @Valid WinePriceBatchSetParam batchSetParam) {
        winePriceService.batchSet(batchSetParam);
        return CommonResult.ok();
    }

    /**
     * 复制价格策略
     *
     * @author WQS_TEAM
     * @date 2025-01-27
     */
    @ApiOperationSupport(order = 12)
    @Operation(summary = "复制价格策略")
    @CommonLog("复制价格策略")
    @SaCheckPermission("/wine/price/copy")
    @PostMapping("/wine/price/copy")
    public CommonResult<String> copy(@RequestBody @Valid WinePriceIdParam winePriceIdParam) {
        winePriceService.copy(winePriceIdParam);
        return CommonResult.ok();
    }

    /**
     * 获取价格历史记录
     *
     * @author WQS_TEAM
     * @date 2025-01-27
     */
    @ApiOperationSupport(order = 13)
    @Operation(summary = "获取价格历史记录")
    @SaCheckPermission("/wine/price/history")
    @GetMapping("/wine/price/history")
    public CommonResult<Object> history(@Valid WinePriceIdParam winePriceIdParam) {
        return CommonResult.data(winePriceService.history(winePriceIdParam));
    }

    /**
     * 预览酒品价格
     *
     * @author WQS_TEAM
     * @date 2025-01-27
     */
    @ApiOperationSupport(order = 14)
    @Operation(summary = "预览酒品价格")
    @CommonLog("预览酒品价格")
    @SaCheckPermission("/wine/price/preview")
    @PostMapping("/wine/price/preview")
    public CommonResult<Object> preview(@RequestBody @Valid WinePricePreviewParam previewParam) {
        return CommonResult.data(winePriceService.preview(previewParam));
    }

    /**
     * 获取当前有效价格
     *
     * @author WQS_TEAM
     * @date 2025-01-27
     */
    @ApiOperationSupport(order = 15)
    @Operation(summary = "获取当前有效价格")
    @SaCheckPermission("/wine/price/current")
    @GetMapping("/wine/price/current")
    public CommonResult<Object> current(@Valid WinePriceIdParam winePriceIdParam) {
        return CommonResult.data(winePriceService.current(winePriceIdParam));
    }

    /**
     * 获取酒品价格详情
     *
     * @author WQS_TEAM
     * @date 2025-01-27
     */
    @ApiOperationSupport(order = 16)
    @Operation(summary = "获取酒品价格详情")
    @SaCheckPermission("/wine/price/detail")
    @GetMapping("/wine/price/detail")
    public CommonResult<WinePrice> detail(@Valid WinePriceIdParam winePriceIdParam) {
        return CommonResult.data(winePriceService.detail(winePriceIdParam));
    }
} 