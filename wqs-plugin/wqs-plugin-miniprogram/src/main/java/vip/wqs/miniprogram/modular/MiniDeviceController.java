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
package vip.wqs.miniprogram.modular;

import cn.hutool.core.util.StrUtil;
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
import vip.wqs.common.annotation.CommonLog;
import vip.wqs.common.pojo.CommonResult;
import vip.wqs.device.api.DeviceApi;
import vip.wqs.device.api.WineDeviceStockApi;
import vip.wqs.device.dto.MiniDeviceConnectionStatusDto;
import vip.wqs.device.dto.MiniDeviceControlDto;
import vip.wqs.device.dto.MiniDeviceEncryptControlDto;
import vip.wqs.device.dto.MiniDeviceControlResultDto;
import vip.wqs.device.dto.MiniDevicePageDto;
import vip.wqs.device.dto.MiniDeviceStatusDto;
import vip.wqs.device.pojo.DevicePojo;
import vip.wqs.device.pojo.DeviceQueryPojo;
import vip.wqs.device.pojo.DeviceSimplePojo;
import vip.wqs.order.api.WineOrderApi;
import vip.wqs.order.pojo.WineOrderPojo;
import vip.wqs.store.api.WineStoreApi;
import vip.wqs.store.pojo.WineStorePojo;
import vip.wqs.wine.api.WinePriceApi;
import vip.wqs.wine.api.WineProductApi;
import vip.wqs.wine.pojo.WinePricePojo;
import vip.wqs.wine.pojo.WineProductPojo;


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * 小程序设备管理控制器
 *
 * @author AI Assistant
 * @date 2025/01/30
 */
@Tag(name = "小程序设备管理控制器")
@ApiSupport(author = "AI_ASSISTANT", order = 9)
@RestController
@Validated
@Slf4j
public class MiniDeviceController {

    @Resource
    private DeviceApi deviceApi;

    @Resource
    private WineProductApi wineProductApi;

    @Resource
    private WinePriceApi winePriceApi;

    @Resource
    private WineStoreApi wineStoreApi;

    @Resource
    private WineDeviceStockApi wineDeviceStockApi;

    @Resource
    private WineOrderApi wineOrderApi;



    /**
     * 获取设备分页列表
     * 注意：设备浏览无需登录，公开访问
     *
     * @author AI Assistant
     * @date 2025/01/30
     */
    @ApiOperationSupport(order = 1)
    @Operation(summary = "获取设备分页列表")
    @CommonLog("小程序获取设备列表")
    @GetMapping("/miniprogram/device/page")
    public CommonResult<Page<DeviceSimplePojo>> getDevicePage(@Valid MiniDevicePageDto param) {
        try {
            log.info("小程序获取设备列表，参数：{}", param);
            
            // 1. 转换查询参数
            DeviceQueryPojo queryPojo = convertToQueryPojo(param);
            
            // 2. 调用设备API获取分页数据
            Page<DeviceSimplePojo> result = deviceApi.getDevicePage(queryPojo);
            
            if (result == null) {
                return CommonResult.error("获取设备列表失败");
            }
            
            log.info("小程序获取设备列表成功，返回{}条数据", result.getRecords().size());
            return CommonResult.data(result);
            
        } catch (Exception e) {
            log.error("获取设备列表异常", e);
            return CommonResult.error("获取设备列表失败：" + e.getMessage());
        }
    }

    /**
     * 获取我管理的设备分页列表
     * 注意：需要登录才能查看我管理的设备
     *
     * @author AI Assistant
     * @date 2025/01/30
     */
    @ApiOperationSupport(order = 2)
    @Operation(summary = "获取我管理的设备分页列表")
    @CommonLog("小程序获取我管理的设备列表")
    @SaClientCheckLogin
    @GetMapping("/miniprogram/device/my-devices")
    public CommonResult<Page<DeviceSimplePojo>> getMyDevicesPage(@Valid MiniDevicePageDto param) {
        try {
            log.info("小程序获取我管理的设备列表，参数：{}", param);
            
            // 1. 获取当前用户ID
            String currentUserId = StpClientUtil.getLoginId().toString();
            
            // 2. 设置管理员用户ID过滤条件
            param.setManagerUserId(currentUserId);
            
            // 3. 转换查询参数
            DeviceQueryPojo queryPojo = convertToQueryPojo(param);
            
            // 4. 调用设备API获取用户管理的设备
            Page<DeviceSimplePojo> result = deviceApi.getUserDevicePage(currentUserId, queryPojo);
            
            if (result == null) {
                return CommonResult.error("获取我管理的设备列表失败");
            }
            
            log.info("小程序获取我管理的设备列表成功，用户ID：{}，返回{}条数据", currentUserId, result.getRecords().size());
            return CommonResult.data(result);
            
        } catch (Exception e) {
            log.error("获取我管理的设备列表异常", e);
            return CommonResult.error("获取我管理的设备列表失败：" + e.getMessage());
        }
    }

    /**
     * 获取设备详情
     * 注意：设备详情浏览无需登录，公开访问
     *
     * @author AI Assistant
     * @date 2025/01/30
     */
    @ApiOperationSupport(order = 3)
    @Operation(summary = "获取设备详情")
    @CommonLog("小程序获取设备详情")
    @GetMapping("/miniprogram/device/detail")
    public CommonResult<Object> getDeviceDetail(@RequestParam String id) {
        try {
            log.info("小程序获取设备详情，设备ID：{}", id);
            
            // 1. 验证参数
            if (StrUtil.isBlank(id)) {
                return CommonResult.error("设备ID不能为空");
            }
            
            // 2. 调用设备API获取基础详情
            DevicePojo deviceDetail = deviceApi.getDeviceDetail(id);
            
            if (deviceDetail == null) {
                return CommonResult.error("设备不存在");
            }
            
            // 3. 构建小程序所需的完整设备详情数据
            Object result = buildDeviceDetailForMiniProgram(deviceDetail);
            
            log.info("小程序获取设备详情成功，设备ID：{}", id);
            return CommonResult.data(result);
            
        } catch (Exception e) {
            log.error("获取设备详情异常，设备ID：{}", id, e);
            return CommonResult.error("获取设备详情失败：" + e.getMessage());
        }
    }

    /**
     * 构建小程序所需的设备详情数据
     * 
     * @param deviceDetail 设备详情
     * @return 小程序设备详情数据
     */
    private Object buildDeviceDetailForMiniProgram(DevicePojo deviceDetail) {
        try {
            // 1. 基础转换
            Map<String, Object> result = new HashMap<>();
            result.put("id", deviceDetail.getId());
            result.put("deviceCode", deviceDetail.getDeviceCode());
            result.put("name", deviceDetail.getDeviceName());
            result.put("location", deviceDetail.getLocation());
            result.put("status", deviceDetail.getStatus() != null ? deviceDetail.getStatus().toLowerCase() : "offline");
            
            // 2. 动态填充扩展信息
            fillStoreInfo(result, deviceDetail.getStoreId());
            fillWineInfo(result, deviceDetail);
            
            return result;
            
        } catch (Exception e) {
            log.error("构建小程序设备详情失败，设备ID：{}，错误：{}", deviceDetail.getId(), e.getMessage(), e);
            // 返回基础信息，确保接口不会完全失败
            Map<String, Object> fallbackResult = new HashMap<>();
            fallbackResult.put("id", deviceDetail.getId());
            fallbackResult.put("deviceCode", deviceDetail.getDeviceCode());
            fallbackResult.put("name", deviceDetail.getDeviceName());
            fallbackResult.put("location", deviceDetail.getLocation());
            fallbackResult.put("status", "offline");
            fallbackResult.put("storeInfo", null);
            fallbackResult.put("wineInfo", null);
            return fallbackResult;
        }
    }

    /**
     * 填充门店信息
     * 
     * @param result 结果Map
     * @param storeId 门店ID
     */
    private void fillStoreInfo(Map<String, Object> result, String storeId) {
        if (StrUtil.isBlank(storeId)) {
            result.put("storeInfo", null);
            return;
        }
        
        try {
            WineStorePojo storeDetail = wineStoreApi.getWineStoreDetail(storeId);
            if (storeDetail == null) {
                result.put("storeInfo", null);
                return;
            }
            
            Map<String, Object> storeInfo = new HashMap<>();
            storeInfo.put("id", storeDetail.getId());
            storeInfo.put("name", storeDetail.getStoreName());
            storeInfo.put("code", storeDetail.getStoreCode());
            storeInfo.put("managerId", storeDetail.getStoreManagerId());
            storeInfo.put("managerName", StrUtil.isNotBlank(storeDetail.getStoreManagerUserName()) ? 
                    storeDetail.getStoreManagerUserName() : "未设置");
            storeInfo.put("image", StrUtil.isNotBlank(storeDetail.getImageUrl()) ? 
                    storeDetail.getImageUrl() : "/static/images/store_default.jpg");
            storeInfo.put("province", StrUtil.isNotBlank(storeDetail.getProvince()) ? 
                    storeDetail.getProvince() : "");
            storeInfo.put("city", StrUtil.isNotBlank(storeDetail.getCity()) ? 
                    storeDetail.getCity() : "");
            storeInfo.put("district", StrUtil.isNotBlank(storeDetail.getDistrict()) ? 
                    storeDetail.getDistrict() : "");
            storeInfo.put("fullAddress", StrUtil.isNotBlank(storeDetail.getFullAddress()) ? 
                    storeDetail.getFullAddress() : "");
            
            result.put("storeInfo", storeInfo);
            
        } catch (Exception e) {
            log.warn("填充门店信息失败，门店ID：{}，错误：{}", storeId, e.getMessage());
            result.put("storeInfo", null);
        }
    }

    /**
     * 填充酒品信息
     * 
     * @param result 结果Map
     * @param deviceDetail 设备详情
     */
    private void fillWineInfo(Map<String, Object> result, DevicePojo deviceDetail) {
        String productId = deviceDetail.getCurrentProductId();
        if (StrUtil.isBlank(productId)) {
            result.put("wineInfo", null);
            return;
        }
        
        try {
            WineProductPojo productDetail = wineProductApi.getWineProductDetail(productId);
            if (productDetail == null) {
                result.put("wineInfo", null);
                return;
            }
            
            Map<String, Object> wineInfo = new HashMap<>();
            wineInfo.put("id", productDetail.getId());
            wineInfo.put("name", StrUtil.isNotBlank(productDetail.getProductName()) ? 
                    productDetail.getProductName() : "未知酒品");
            wineInfo.put("brand", StrUtil.isNotBlank(productDetail.getBrand()) ? 
                    productDetail.getBrand() : "");
            wineInfo.put("type", StrUtil.isNotBlank(productDetail.getProductType()) ? 
                    productDetail.getProductType() : "");
            wineInfo.put("alcoholContent", productDetail.getAlcoholContent() != null ? 
                    productDetail.getAlcoholContent() : 0);
            wineInfo.put("image", StrUtil.isNotBlank(productDetail.getImageUrl()) ? 
                    productDetail.getImageUrl() : "/static/images/wine_default.png");
            wineInfo.put("unitPrice", productDetail.getUnitPrice() != null ? 
                    productDetail.getUnitPrice() : BigDecimal.ZERO);
            
            // 填充库存信息
            fillStockInfo(wineInfo, deviceDetail.getId(), productId);
            
            // 填充容量规格
            fillCapacityInfo(wineInfo, productId);
            
            result.put("wineInfo", wineInfo);
            
        } catch (Exception e) {
            log.warn("填充酒品信息失败，设备ID：{}，酒品ID：{}，错误：{}", 
                    deviceDetail.getId(), productId, e.getMessage());
            result.put("wineInfo", null);
        }
    }

    /**
     * 填充库存信息
     * 
     * @param wineInfo 酒品信息Map
     * @param deviceId 设备ID
     * @param productId 酒品ID
     */
    private void fillStockInfo(Map<String, Object> wineInfo, String deviceId, String productId) {
        try {
            BigDecimal stockQuantity = wineDeviceStockApi.getStockQuantity(deviceId, productId);
            wineInfo.put("stock", stockQuantity != null ? stockQuantity.intValue() : 0);
        } catch (Exception e) {
            log.warn("查询设备库存失败，设备ID：{}，酒品ID：{}，错误：{}", 
                    deviceId, productId, e.getMessage());
            wineInfo.put("stock", 0);
        }
    }

    /**
     * 填充容量规格信息
     * 
     * @param wineInfo 酒品信息Map
     * @param productId 酒品ID
     */
    private void fillCapacityInfo(Map<String, Object> wineInfo, String productId) {
        try {
            List<WinePricePojo> priceList = winePriceApi.getWinePriceList(productId);
            List<Map<String, Object>> capacities = new ArrayList<>();
            
            if (priceList != null && !priceList.isEmpty()) {
                for (WinePricePojo price : priceList) {
                    Map<String, Object> capacity = new HashMap<>();
                    capacity.put("id", price.getId());
                    capacity.put("size", (price.getCapacity() != null ? price.getCapacity().intValue() : 0) + "ml");
                    capacity.put("sortCode", price.getSortCode());
                    capacities.add(capacity);
                }
            }
            
            wineInfo.put("capacities", capacities);
            
        } catch (Exception e) {
            log.warn("查询酒品容量规格失败，酒品ID：{}，错误：{}", productId, e.getMessage());
            wineInfo.put("capacities", new ArrayList<>());
        }
    }

    /**
     * 更新设备状态
     * 注意：需要登录才能操作
     *
     * @author AI Assistant
     * @date 2025/01/30
     */
    @ApiOperationSupport(order = 4)
    @Operation(summary = "更新设备状态")
    @CommonLog("小程序更新设备状态")
    @SaClientCheckLogin
    @PostMapping("/miniprogram/device/update-status")
    public CommonResult<Boolean> updateDeviceStatus(@RequestBody @Valid MiniDeviceStatusDto param) {
        try {
            log.info("小程序更新设备状态，参数：{}", param);
            
            // 1. 获取当前用户ID
            String currentUserId = StpClientUtil.getLoginId().toString();
            param.setUserId(currentUserId);
            
            // 2. 调用设备API更新状态
            Boolean result = deviceApi.updateDeviceStatus(param.getDeviceId(), param.getStatus(), currentUserId);
            
            if (result == null || !result) {
                return CommonResult.error("更新设备状态失败");
            }
            
            log.info("小程序更新设备状态成功，设备ID：{}，状态：{}", param.getDeviceId(), param.getStatus());
            return CommonResult.data(result);
            
        } catch (Exception e) {
            log.error("更新设备状态异常，参数：{}", param, e);
            return CommonResult.error("更新设备状态失败：" + e.getMessage());
        }
    }

    /**
     * 控制设备
     * 注意：需要登录才能操作
     *
     * @author AI Assistant
     * @date 2025/01/30
     */
    @ApiOperationSupport(order = 5)
    @Operation(summary = "控制设备")
    @CommonLog("小程序控制设备")
    @SaClientCheckLogin
    @PostMapping("/miniprogram/device/control")
    public CommonResult<Boolean> controlDevice(@RequestBody @Valid MiniDeviceControlDto param) {
        try {
            log.info("小程序控制设备，参数：{}", param);
            
            // 1. 获取当前用户ID
            String currentUserId = StpClientUtil.getLoginId().toString();
            param.setUserId(currentUserId);
            
            // 2. 调用设备API控制设备
            Boolean result = deviceApi.controlDevice(param.getDeviceId(), param.getCommand(), currentUserId);
            
            if (result == null || !result) {
                return CommonResult.error("控制设备失败");
            }
            
            log.info("小程序控制设备成功，设备ID：{}，命令：{}", param.getDeviceId(), param.getCommand());
            return CommonResult.data(result);
            
        } catch (Exception e) {
            log.error("控制设备异常，参数：{}", param, e);
            return CommonResult.error("控制设备失败：" + e.getMessage());
        }
    }

    /**
     * 检查设备在线状态
     * 注意：公开访问
     *
     * @author AI Assistant
     * @date 2025/01/30
     */
    @ApiOperationSupport(order = 6)
    @Operation(summary = "检查设备在线状态")
    @CommonLog("小程序检查设备在线状态")
    @GetMapping("/miniprogram/device/online-status/{deviceCode}")
    public CommonResult<Boolean> isDeviceOnline(@PathVariable String deviceCode) {
        try {
            log.info("小程序检查设备在线状态，设备编码：{}", deviceCode);
            
            // 1. 验证参数
            if (StrUtil.isBlank(deviceCode)) {
                return CommonResult.error("设备编码不能为空");
            }
            
            // 2. 调用设备API检查在线状态
            Boolean result = deviceApi.isDeviceOnline(deviceCode);
            
            log.info("小程序检查设备在线状态完成，设备编码：{}，在线状态：{}", deviceCode, result);
            return CommonResult.data(result);
            
        } catch (Exception e) {
            log.error("检查设备在线状态异常，设备编码：{}", deviceCode, e);
            return CommonResult.error("检查设备状态失败：" + e.getMessage());
        }
    }

    /**
     * 更新设备蓝牙连接状态
     * 注意：需要登录才能操作
     *
     * @author AI Assistant
     * @date 2025/01/30
     */
    @ApiOperationSupport(order = 7)
    @Operation(summary = "更新设备蓝牙连接状态")
    @CommonLog("小程序更新设备蓝牙连接状态")
    @SaClientCheckLogin
    @PostMapping("/miniprogram/device/update-connection-status")
    public CommonResult<Boolean> updateDeviceConnectionStatus(@RequestBody @Valid MiniDeviceConnectionStatusDto param) {
        try {
            log.info("小程序更新设备蓝牙连接状态，参数：{}", param);
            
            // 1. 获取当前用户ID
            String currentUserId = StpClientUtil.getLoginId().toString();
            param.setUserId(currentUserId);


            // 2. 调用设备API更新蓝牙连接状态
            Boolean result = deviceApi.updateDeviceConnectionStatus(
                param.getDeviceId(), 
                param.getConnectionStatus(), 
                currentUserId, 
                param.getCheckResult()
            );
            
            if (result == null || !result) {
                return CommonResult.error("更新设备蓝牙连接状态失败");
            }
            
            log.info("小程序更新设备蓝牙连接状态成功，设备ID：{}，连接状态：{}", param.getDeviceId(), param.getConnectionStatus());
            return CommonResult.data(result);
            
        } catch (Exception e) {
            log.error("更新设备蓝牙连接状态异常，参数：{}", param, e);
            return CommonResult.error("更新设备蓝牙连接状态失败：" + e.getMessage());
        }
    }

    /**
     * 根据门店ID获取设备列表
     * 注意：公开访问
     *
     * @author AI Assistant
     * @date 2025/01/30
     */
    @ApiOperationSupport(order = 6)
    @Operation(summary = "根据门店ID获取设备列表")
    @CommonLog("小程序根据门店ID获取设备列表")
    @GetMapping("/miniprogram/device/store/{storeId}")
    public CommonResult<List<DeviceSimplePojo>> getDevicesByStoreId(@PathVariable String storeId) {
        try {
            log.info("小程序根据门店ID获取设备列表，门店ID：{}", storeId);
            
            // 1. 验证参数
            if (StrUtil.isBlank(storeId)) {
                return CommonResult.error("门店ID不能为空");
            }
            
            // 2. 调用设备API获取门店设备列表
            List<DeviceSimplePojo> result = deviceApi.getDevicesByStoreId(storeId);
            
            if (result == null) {
                return CommonResult.error("获取门店设备列表失败");
            }
            
            log.info("小程序根据门店ID获取设备列表成功，返回{}条数据", result.size());
            return CommonResult.data(result);
            
        } catch (Exception e) {
            log.error("根据门店ID获取设备列表异常，门店ID：{}", storeId, e);
            return CommonResult.error("获取门店设备列表失败：" + e.getMessage());
        }
    }



    /**
     * 查询设备库存信息
     * 注意：库存查询无需登录，公开访问
     *
     * @author AI Assistant
     * @date 2025/01/30
     */
    @ApiOperationSupport(order = 8)
    @Operation(summary = "查询设备库存信息")
    @CommonLog("小程序查询设备库存信息")
    @GetMapping("/miniprogram/device/stock")
    public CommonResult<List<Object>> getDeviceStock(@RequestParam String deviceId) {
        try {
            log.info("小程序查询设备库存信息，设备ID：{}", deviceId);
            
            // 1. 验证参数
            if (StrUtil.isBlank(deviceId)) {
                return CommonResult.error("设备ID不能为空");
            }
            
            // 2. 调用库存API获取设备库存信息
            List<Object> result = wineDeviceStockApi.getStockByDeviceId(deviceId);
            
            if (result == null) {
                return CommonResult.error("获取设备库存信息失败");
            }
            
            log.info("小程序查询设备库存信息成功，设备ID：{}，返回{}条数据", deviceId, result.size());
            return CommonResult.data(result);
            
        } catch (Exception e) {
            log.error("查询设备库存信息异常，设备ID：{}", deviceId, e);
            return CommonResult.error("查询设备库存信息失败：" + e.getMessage());
        }
    }

    /**
     * 查询设备特定酒品的库存数量
     * 注意：库存查询无需登录，公开访问
     *
     * @author AI Assistant
     * @date 2025/01/30
     */
    @ApiOperationSupport(order = 9)
    @Operation(summary = "查询设备特定酒品的库存数量")
    @CommonLog("小程序查询设备库存数量")
    @GetMapping("/miniprogram/device/stock/quantity")
    public CommonResult<Object> getStockQuantity(@RequestParam String deviceId, @RequestParam String productId) {
        try {
            log.info("小程序查询设备库存数量，设备ID：{}，酒品ID：{}", deviceId, productId);
            
            // 1. 验证参数
            if (StrUtil.isBlank(deviceId) || StrUtil.isBlank(productId)) {
                return CommonResult.error("设备ID和酒品ID不能为空");
            }
            
            // 2. 调用库存API获取库存数量
            BigDecimal quantity = wineDeviceStockApi.getStockQuantity(deviceId, productId);
            
            // 3. 构建返回结果
            java.util.Map<String, Object> result = new java.util.HashMap<>();
            result.put("quantity", quantity != null ? quantity.doubleValue() : 0.0);
            
            log.info("小程序查询设备库存数量成功，设备ID：{}，酒品ID：{}，库存：{}ml", deviceId, productId, quantity);
            return CommonResult.data(result);
            
        } catch (Exception e) {
            log.error("查询设备库存数量异常，设备ID：{}，酒品ID：{}", deviceId, productId, e);
            return CommonResult.error("查询设备库存数量失败：" + e.getMessage());
        }
    }

    /**
     * 检查库存是否充足
     * 注意：库存检查无需登录，公开访问
     *
     * @author AI Assistant
     * @date 2025/01/30
     */
    @ApiOperationSupport(order = 10)
    @Operation(summary = "检查库存是否充足")
    @CommonLog("小程序检查库存是否充足")
    @GetMapping("/miniprogram/device/stock/check")
    public CommonResult<Object> checkStockSufficient(@RequestParam String deviceId, 
                                                     @RequestParam String productId, 
                                                     @RequestParam BigDecimal requiredQuantity) {
        try {
            log.info("小程序检查库存是否充足，设备ID：{}，酒品ID：{}，需要数量：{}ml", deviceId, productId, requiredQuantity);
            
            // 1. 验证参数
            if (StrUtil.isBlank(deviceId) || StrUtil.isBlank(productId) || requiredQuantity == null) {
                return CommonResult.error("参数不能为空");
            }
            
            if (requiredQuantity.compareTo(BigDecimal.ZERO) <= 0) {
                return CommonResult.error("需要数量必须大于0");
            }
            
            // 2. 调用库存API检查库存
            Boolean sufficient = wineDeviceStockApi.checkStockSufficient(deviceId, productId, requiredQuantity);
            
            // 3. 构建返回结果
            java.util.Map<String, Object> result = new java.util.HashMap<>();
            result.put("sufficient", sufficient != null ? sufficient : false);
            
            log.info("小程序检查库存是否充足完成，设备ID：{}，酒品ID：{}，需要数量：{}ml，结果：{}", 
                    deviceId, productId, requiredQuantity, sufficient);
            return CommonResult.data(result);
            
        } catch (Exception e) {
            log.error("检查库存是否充足异常，设备ID：{}，酒品ID：{}，需要数量：{}ml", deviceId, productId, requiredQuantity, e);
            return CommonResult.error("检查库存是否充足失败：" + e.getMessage());
        }
    }

    /**
     * 获取设备库存状态概览
     * 注意：库存概览无需登录，公开访问
     *
     * @author AI Assistant
     * @date 2025/01/30
     */
    @ApiOperationSupport(order = 11)
    @Operation(summary = "获取设备库存状态概览")
    @CommonLog("小程序获取设备库存状态概览")
    @GetMapping("/miniprogram/device/stock/overview")
    public CommonResult<Object> getDeviceStockOverview(@RequestParam String deviceId) {
        try {
            log.info("小程序获取设备库存状态概览，设备ID：{}", deviceId);
            
            // 1. 验证参数
            if (StrUtil.isBlank(deviceId)) {
                return CommonResult.error("设备ID不能为空");
            }
            
            // 2. 获取设备库存列表
            List<Object> stockList = wineDeviceStockApi.getStockByDeviceId(deviceId);
            
            // 3. 统计库存状态
            int totalProducts = 0;
            int normalStock = 0;
            int lowStock = 0;
            int outOfStock = 0;
            
            if (stockList != null) {
                totalProducts = stockList.size();
                // 这里需要根据实际的库存对象结构来统计
                // 暂时使用简单的统计逻辑
                normalStock = totalProducts;
            }
            
            // 4. 构建返回结果
            java.util.Map<String, Object> result = new java.util.HashMap<>();
            result.put("totalProducts", totalProducts);
            result.put("normalStock", normalStock);
            result.put("lowStock", lowStock);
            result.put("outOfStock", outOfStock);
            
            log.info("小程序获取设备库存状态概览成功，设备ID：{}，总产品：{}个", deviceId, totalProducts);
            return CommonResult.data(result);
            
        } catch (Exception e) {
            log.error("获取设备库存状态概览异常，设备ID：{}", deviceId, e);
            return CommonResult.error("获取设备库存状态概览失败：" + e.getMessage());
        }
    }

    /**
     * 分页参数转换为查询参数
     */
    private DeviceQueryPojo convertToQueryPojo(MiniDevicePageDto param) {
        DeviceQueryPojo queryPojo = new DeviceQueryPojo();
        
        // 分页参数
        queryPojo.setPageNum(param.getPageNum().intValue());
        queryPojo.setPageSize(param.getPageSize().intValue());
        
        // 查询条件
        queryPojo.setKeyword(param.getKeyword());
        queryPojo.setSearchKey(param.getKeyword()); // 兼容现有接口
        queryPojo.setStatus(param.getStatus());
        queryPojo.setStoreId(param.getStoreId());
        queryPojo.setManagerUserId(param.getManagerUserId());
        
        // 排序参数
        queryPojo.setSortField(param.getSortField());
        queryPojo.setSortOrder(param.getSortOrder());
        
        return queryPojo;
    }

    // ==================== 蓝牙设备控制相关接口 ====================

    /**
     * 获取设备控制加密指令
     * 小程序端调用此接口获取加密控制指令，然后通过蓝牙发送给设备
     *
     * @author AI Assistant
     * @date 2025/01/30
     */
    @ApiOperationSupport(order = 12)
    @Operation(summary = "获取设备控制加密指令")
    @CommonLog("小程序获取设备控制指令")
    @SaClientCheckLogin
    @PostMapping("/miniprogram/device/control/getCommand")
    public CommonResult<Object> getDeviceControlCommand(@RequestBody @Valid MiniDeviceEncryptControlDto param) {
        try {
            log.info("小程序获取设备控制指令，参数：{}", param);
            
            // 1. 设置当前用户ID
            String currentUserId = StpClientUtil.getLoginId().toString();
            param.setUserId(currentUserId);
            
            // 2. 调用设备API获取控制指令
            String command = deviceApi.getDeviceControlCommand(
                param.getDeviceCode(),
                Integer.valueOf(param.getChargeId()),
                param.getMinute(), 
                param.getSecond()
            );
            
            if (StrUtil.isBlank(command)) {
                return CommonResult.error("获取控制指令失败");
            }
            
            // 3. 构建返回结果
            java.util.Map<String, Object> result = new java.util.HashMap<>();
            result.put("success", true);
            result.put("cmd", command);
            result.put("message", "获取控制指令成功");
            result.put("deviceId", param.getDeviceCode());
            result.put("orderId", param.getChargeId());
            
            log.info("小程序获取设备控制指令成功，用户ID：{}，设备ID：{}", currentUserId, param.getDeviceCode());
            return CommonResult.data(result);
            
        } catch (Exception e) {
            log.error("获取设备控制指令异常", e);
            return CommonResult.error("获取设备控制指令失败：" + e.getMessage());
        }
    }

    /**
     * 更新设备控制结果
     * 小程序端调用此接口更新设备控制结果
     *
     * @author AI Assistant
     * @date 2025/01/30
     */
    @ApiOperationSupport(order = 13)
    @Operation(summary = "更新设备控制结果")
    @CommonLog("小程序更新设备控制结果")
    @SaClientCheckLogin
    @PostMapping("/miniprogram/device/control/updateResult")
    public CommonResult<Boolean> updateDeviceControlResult(@RequestBody @Valid MiniDeviceControlResultDto param) {
        try {
            log.info("小程序更新设备控制结果，参数：{}", param);
            return CommonResult.data(deviceApi.updateDeviceControlResult(param.getOrderId(), param.getDeviceId(), param.getSuccess(), param.getMessage(), param.getUserId()));
        } catch (Exception e) {
            log.error("更新设备控制结果异常", e);
            return CommonResult.error("更新设备控制结果失败：" + e.getMessage());
        }
    }


} 