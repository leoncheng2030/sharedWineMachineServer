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
package vip.wqs.device.modular.stock.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vip.wqs.common.annotation.CommonLog;
import vip.wqs.common.pojo.CommonResult;
import vip.wqs.device.modular.stock.entity.WineDeviceStock;
import vip.wqs.device.modular.stock.entity.WineStockLog;
import vip.wqs.device.modular.stock.param.*;
import vip.wqs.device.modular.stock.service.WineDeviceStockService;
import vip.wqs.device.modular.stock.service.WineStockLogService;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

/**
 * 设备库存控制器
 *
 * @author AI Assistant
 * @date 2025/01/30
 */
@Tag(name = "设备库存管理")
@RestController
@Validated
@Slf4j
public class WineDeviceStockController {

    @Resource
    private WineDeviceStockService wineDeviceStockService;

    @Resource
    private WineStockLogService wineStockLogService;

    /**
     * 获取设备库存分页列表
     *
     * @author AI Assistant
     * @date 2025/01/30
     */
    @Operation(summary = "获取设备库存分页列表")
    @SaCheckPermission("/device/stock/page")
    @GetMapping("/device/stock/page")
    @CommonLog("获取设备库存分页列表")
    public CommonResult<Page<WineDeviceStock>> page(@Valid WineDeviceStockPageParam param) {
        return CommonResult.data(wineDeviceStockService.page(param));
    }

    /**
     * 获取设备库存详情
     *
     * @author AI Assistant
     * @date 2025/01/30
     */
    @Operation(summary = "获取设备库存详情")
    @SaCheckPermission("/device/stock/detail")
    @GetMapping("/device/stock/detail")
    @CommonLog("获取设备库存详情")
    public CommonResult<WineDeviceStock> detail(@RequestParam String id) {
        return CommonResult.data(wineDeviceStockService.getById(id));
    }

    /**
     * 查询设备库存数量
     *
     * @author AI Assistant
     * @date 2025/01/30
     */
    @Operation(summary = "查询设备库存数量")
    @SaCheckPermission("/device/stock/quantity")
    @GetMapping("/device/stock/quantity")
    @CommonLog("查询设备库存数量")
    public CommonResult<BigDecimal> getStockQuantity(@RequestParam String deviceId, @RequestParam String productId) {
        BigDecimal quantity = wineDeviceStockService.getStockQuantity(deviceId, productId);
        return CommonResult.data(quantity);
    }

    /**
     * 初始化库存
     *
     * @author AI Assistant
     * @date 2025/01/30
     */
    @Operation(summary = "初始化库存")
    @SaCheckPermission("/device/stock/init")
    @PostMapping("/device/stock/init")
    @CommonLog("初始化库存")
    public CommonResult<String> initStock(@RequestBody @Valid WineDeviceStockInitParam param) {
        // TODO: 获取当前登录用户
        String operator = "admin"; // 临时使用admin
        wineDeviceStockService.initializeStock(param.getDeviceId(), param.getProductId(),
                param.getInitialQuantity(), param.getAlertThreshold(), operator);
        return CommonResult.ok();
    }

    /**
     * 设备补货
     *
     * @author AI Assistant
     * @date 2025/01/30
     */
    @Operation(summary = "设备补货")
    @SaCheckPermission("/device/stock/refill")
    @PostMapping("/device/stock/refill")
    @CommonLog("设备补货")
    public CommonResult<String> refill(@RequestBody @Valid WineDeviceStockRefillParam param) {
        // TODO: 获取当前登录用户
        String operator = "admin"; // 临时使用admin
        wineDeviceStockService.refill(param.getDeviceId(), param.getProductId(),
                param.getQuantity(), operator, param.getReason());
        return CommonResult.ok();
    }

    /**
     * 调整库存
     *
     * @author AI Assistant
     * @date 2025/01/30
     */
    @Operation(summary = "调整库存")
    @SaCheckPermission("/device/stock/adjust")
    @PostMapping("/device/stock/adjust")
    @CommonLog("调整库存")
    public CommonResult<String> adjustStock(@RequestBody @Valid WineDeviceStockAdjustParam param) {
        // TODO: 获取当前登录用户
        String operator = "admin"; // 临时使用admin
        // TODO: 修复参数类getter方法后启用
        wineDeviceStockService.adjustStock(param.getDeviceId(), param.getProductId(),
                param.getQuantity(), operator, param.getReason());
        return CommonResult.ok();
    }

    /**
     * 查询低库存设备列表
     *
     * @author AI Assistant
     * @date 2025/01/30
     */
    @Operation(summary = "查询低库存设备列表")
    @SaCheckPermission("/device/stock/lowStock")
    @GetMapping("/device/stock/lowStock")
    @CommonLog("查询低库存设备列表")
    public CommonResult<List<WineDeviceStock>> getLowStockList() {
        return CommonResult.data(wineDeviceStockService.getLowStockList());
    }

    /**
     * 查询缺货设备列表
     *
     * @author AI Assistant
     * @date 2025/01/30
     */
    @Operation(summary = "查询缺货设备列表")
    @SaCheckPermission("/device/stock/outOfStock")
    @GetMapping("/device/stock/outOfStock")
    @CommonLog("查询缺货设备列表")
    public CommonResult<List<WineDeviceStock>> getOutOfStockList() {
        return CommonResult.data(wineDeviceStockService.getOutOfStockList());
    }

    /**
     * 根据设备ID查询库存列表
     *
     * @author AI Assistant
     * @date 2025/01/30
     */
    @Operation(summary = "根据设备ID查询库存列表")
    @SaCheckPermission("/device/stock/byDevice")
    @GetMapping("/device/stock/byDevice")
    @CommonLog("根据设备ID查询库存列表")
    public CommonResult<List<WineDeviceStock>> getStockByDeviceId(@RequestParam String deviceId) {
        return CommonResult.data(wineDeviceStockService.getStockByDeviceId(deviceId));
    }

    /**
     * 根据酒品ID查询库存列表
     *
     * @author AI Assistant
     * @date 2025/01/30
     */
    @Operation(summary = "根据酒品ID查询库存列表")
    @SaCheckPermission("/device/stock/byProduct")
    @GetMapping("/device/stock/byProduct")
    @CommonLog("根据酒品ID查询库存列表")
    public CommonResult<List<WineDeviceStock>> getStockByProductId(@RequestParam String productId) {
        return CommonResult.data(wineDeviceStockService.getStockByProductId(productId));
    }

    /**
     * 查询库存变更日志（分页）
     *
     * @author AI Assistant
     * @date 2025/01/30
     */
    @Operation(summary = "查询库存变更日志")
    @SaCheckPermission("/device/stock/logs")
    @GetMapping("/device/stock/logs")
    @CommonLog("查询库存变更日志")
    public CommonResult<Page<WineStockLog>> getStockLogs(@Valid WineStockLogPageParam param) {
        return CommonResult.data(wineStockLogService.page(param));
    }
}