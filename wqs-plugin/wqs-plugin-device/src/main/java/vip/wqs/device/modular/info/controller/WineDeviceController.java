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
package vip.wqs.device.modular.info.controller;

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
import org.springframework.web.bind.annotation.*;
import vip.wqs.common.annotation.CommonLog;
import vip.wqs.common.pojo.CommonResult;
import vip.wqs.device.modular.info.entity.WineDevice;
import vip.wqs.device.modular.info.param.WineDeviceAddParam;
import vip.wqs.device.modular.info.param.WineDeviceEditParam;
import vip.wqs.device.modular.info.param.WineDeviceIdParam;
import vip.wqs.device.modular.info.param.WineDevicePageParam;
import vip.wqs.device.modular.info.service.WineDeviceService;

import java.util.List;

/**
 * 设备控制器
 *
 * @author wqs
 * @date 2025/1/27
 **/
@Tag(name = "设备控制器")
@ApiSupport(author = "WQS_TEAM", order = 1)
@RestController
@Validated
public class WineDeviceController {

    @Resource
    private WineDeviceService wineDeviceService;

    /**
     * 获取设备分页
     *
     * @author wqs
     * @date 2025/1/27
     */
    @ApiOperationSupport(order = 1)
    @Operation(summary = "获取设备分页")
    @SaCheckPermission("/device/info/page")
    @GetMapping("/device/info/page")
    public CommonResult<Page<WineDevice>> page(WineDevicePageParam wineDevicePageParam) {
        return CommonResult.data(wineDeviceService.page(wineDevicePageParam));
    }

    /**
     * 添加设备
     *
     * @author wqs
     * @date 2025/1/27
     */
    @ApiOperationSupport(order = 2)
    @Operation(summary = "添加设备")
    @CommonLog("添加设备")
    @SaCheckPermission("/device/info/add")
    @PostMapping("/device/info/add")
    public CommonResult<String> add(@RequestBody @Valid WineDeviceAddParam wineDeviceAddParam) {
        wineDeviceService.add(wineDeviceAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑设备
     *
     * @author wqs
     * @date 2025/1/27
     */
    @ApiOperationSupport(order = 3)
    @Operation(summary = "编辑设备")
    @CommonLog("编辑设备")
    @SaCheckPermission("/device/info/edit")
    @PostMapping("/device/info/edit")
    public CommonResult<String> edit(@RequestBody @Valid WineDeviceEditParam wineDeviceEditParam) {
        wineDeviceService.edit(wineDeviceEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除设备
     *
     * @author wqs
     * @date 2025/1/27
     */
    @ApiOperationSupport(order = 4)
    @Operation(summary = "删除设备")
    @CommonLog("删除设备")
    @SaCheckPermission("/device/info/delete")
    @PostMapping("/device/info/delete")
    public CommonResult<String> delete(@RequestBody @Valid WineDeviceIdParam wineDeviceIdParam) {
        wineDeviceService.delete(wineDeviceIdParam);
        return CommonResult.ok();
    }

    /**
     * 获取设备详情
     *
     * @author wqs
     * @date 2025/1/27
     */
    @ApiOperationSupport(order = 5)
    @Operation(summary = "获取设备详情")
    @SaCheckPermission("/device/info/detail")
    @GetMapping("/device/info/detail")
    public CommonResult<WineDevice> detail(@Valid WineDeviceIdParam wineDeviceIdParam) {
        return CommonResult.data(wineDeviceService.detail(wineDeviceIdParam));
    }

    /**
     * 禁用设备
     *
     * @author wqs
     * @date 2025/1/27
     */
    @ApiOperationSupport(order = 6)
    @Operation(summary = "禁用设备")
    @CommonLog("禁用设备")
    @SaCheckPermission("/device/info/disable")
    @PostMapping("/device/info/disable")
    public CommonResult<String> disable(@RequestBody @Valid WineDeviceIdParam wineDeviceIdParam) {
        wineDeviceService.disable(wineDeviceIdParam);
        return CommonResult.ok();
    }

    /**
     * 启用设备
     *
     * @author wqs
     * @date 2025/1/27
     */
    @ApiOperationSupport(order = 7)
    @Operation(summary = "启用设备")
    @CommonLog("启用设备")
    @SaCheckPermission("/device/info/enable")
    @PostMapping("/device/info/enable")
    public CommonResult<String> enable(@RequestBody @Valid WineDeviceIdParam wineDeviceIdParam) {
        wineDeviceService.enable(wineDeviceIdParam);
        return CommonResult.ok();
    }

    /**
     * 导出设备
     *
     * @author wqs
     * @date 2025/1/27
     */
    @ApiOperationSupport(order = 8)
    @Operation(summary = "导出设备")
    @CommonLog("导出设备")
    @SaCheckPermission("/device/info/export")
    @GetMapping("/device/info/export")
    public void export(WineDevicePageParam wineDevicePageParam) {
        wineDeviceService.export(wineDevicePageParam);
    }

    /**
     * 设备选择器
     *
     * @author wqs
     * @date 2025/1/27
     */
    @ApiOperationSupport(order = 9)
    @Operation(summary = "设备选择器")
    @SaCheckPermission("/device/info/selector")
    @GetMapping("/device/info/selector")
    public CommonResult<List<WineDevice>> selector(WineDevicePageParam wineDevicePageParam) {
        return CommonResult.data(wineDeviceService.selector(wineDevicePageParam));
    }

    /**
     * 批量删除设备
     *
     * @author wqs
     * @date 2025/1/27
     */
    @ApiOperationSupport(order = 10)
    @Operation(summary = "批量删除设备")
    @CommonLog("批量删除设备")
    @SaCheckPermission("/device/info/batchDelete")
    @PostMapping("/device/info/batchDelete")
    public CommonResult<String> batchDelete(@RequestBody @Valid @NotEmpty(message = "集合不能为空")
                                                   List<WineDeviceIdParam> wineDeviceIdParamList) {
        wineDeviceService.batchDelete(wineDeviceIdParamList);
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
    @SaCheckPermission("/device/info/managerUserSelector")
    @GetMapping("/device/info/managerUserSelector")
    public CommonResult<Object> managerUserSelector() {
        return CommonResult.data(wineDeviceService.managerUserSelector());
    }

    /**
     * 获取门店选择器
     *
     * @author wqs
     * @date 2025/1/27
     */
    @ApiOperationSupport(order = 12)
    @Operation(summary = "获取门店选择器")
    @SaCheckPermission("/device/info/storeSelector")
    @GetMapping("/device/info/storeSelector")
    public CommonResult<Object> storeSelector() {
        return CommonResult.data(wineDeviceService.storeSelector());
    }

    /**
     * 获取酒品选择器
     *
     * @author wqs
     * @date 2025/1/27
     */
    @ApiOperationSupport(order = 13)
    @Operation(summary = "获取酒品选择器")
    @SaCheckPermission("/device/info/productSelector")
    @GetMapping("/device/info/productSelector")
    public CommonResult<Object> productSelector() {
        return CommonResult.data(wineDeviceService.productSelector());
    }

    /**
     * 绑定设备到酒品
     *
     * @author wqs
     * @date 2025/1/27
     */
    @ApiOperationSupport(order = 14)
    @Operation(summary = "绑定设备到酒品")
    @CommonLog("绑定设备到酒品")
    @SaCheckPermission("/device/info/bindProduct")
    @PostMapping("/device/info/bindProduct")
    public CommonResult<String> bindProduct(@RequestParam String deviceId, @RequestParam String productId) {
        wineDeviceService.bindProduct(deviceId, productId);
        return CommonResult.ok();
    }

    /**
     * 解绑设备酒品
     *
     * @author wqs
     * @date 2025/1/27
     */
    @ApiOperationSupport(order = 15)
    @Operation(summary = "解绑设备酒品")
    @CommonLog("解绑设备酒品")
    @SaCheckPermission("/device/info/unbindProduct")
    @PostMapping("/device/info/unbindProduct")
    public CommonResult<String> unbindProduct(@RequestParam String deviceId) {
        wineDeviceService.unbindProduct(deviceId);
        return CommonResult.ok();
    }

    /**
     * 根据门店ID获取设备列表
     *
     * @author wqs
     * @date 2025/1/27
     */
    @ApiOperationSupport(order = 16)
    @Operation(summary = "根据门店ID获取设备列表")
    @SaCheckPermission("/device/info/getDevicesByStore")
    @GetMapping("/device/info/getDevicesByStore")
    public CommonResult<List<WineDevice>> getDevicesByStore(@RequestParam String storeId) {
        return CommonResult.data(wineDeviceService.getDevicesByStore(storeId));
    }

    /**
     * 获取设备列表
     *
     * @author wqs
     * @date 2025/1/27
     */
    @ApiOperationSupport(order = 17)
    @Operation(summary = "获取设备列表")
    @SaCheckPermission("/device/info/list")
    @GetMapping("/device/info/list")
    public CommonResult<List<WineDevice>> list(WineDevicePageParam wineDevicePageParam) {
        return CommonResult.data(wineDeviceService.list(wineDevicePageParam));
    }

    /**
     * 绑定设备
     *
     * @author wqs
     * @date 2025/1/27
     */
    @ApiOperationSupport(order = 18)
    @Operation(summary = "绑定设备")
    @CommonLog("绑定设备")
    @SaCheckPermission("/device/info/bind")
    @PostMapping("/device/info/bind")
    public CommonResult<String> bind(@RequestBody @Valid WineDeviceIdParam wineDeviceIdParam) {
        wineDeviceService.bind(wineDeviceIdParam);
        return CommonResult.ok();
    }

    /**
     * 解绑设备
     *
     * @author wqs
     * @date 2025/1/27
     */
    @ApiOperationSupport(order = 19)
    @Operation(summary = "解绑设备")
    @CommonLog("解绑设备")
    @SaCheckPermission("/device/info/unbind")
    @PostMapping("/device/info/unbind")
    public CommonResult<String> unbind(@RequestBody @Valid WineDeviceIdParam wineDeviceIdParam) {
        wineDeviceService.unbind(wineDeviceIdParam);
        return CommonResult.ok();
    }
} 