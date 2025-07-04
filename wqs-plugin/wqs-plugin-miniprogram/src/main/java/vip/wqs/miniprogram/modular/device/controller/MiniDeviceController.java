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
package vip.wqs.miniprogram.modular.device.controller;

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
import vip.wqs.device.dto.MiniDevicePageDto;
import vip.wqs.device.dto.MiniDeviceStatusDto;
import vip.wqs.device.pojo.DevicePojo;
import vip.wqs.device.pojo.DeviceQueryPojo;
import vip.wqs.device.pojo.DeviceSimplePojo;
import vip.wqs.store.api.WineStoreApi;
import vip.wqs.store.pojo.WineStorePojo;
import vip.wqs.wine.api.WinePriceApi;
import vip.wqs.wine.api.WineProductApi;
import vip.wqs.wine.pojo.WinePricePojo;
import vip.wqs.wine.pojo.WineProductPojo;

import java.math.BigDecimal;
import java.util.List;

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
     */
    private Object buildDeviceDetailForMiniProgram(DevicePojo deviceDetail) {
        // 构建符合小程序设备详情页面需求的数据结构
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        
        // 设备基本信息
        result.put("id", deviceDetail.getId());
        result.put("deviceCode", deviceDetail.getDeviceCode());
        result.put("name", deviceDetail.getDeviceName());
        result.put("location", deviceDetail.getLocation());
        result.put("status", deviceDetail.getStatus().toLowerCase());
        
        // 门店信息

        if (StrUtil.isNotBlank(deviceDetail.getStoreId())){
            WineStorePojo wineStoreDetail = wineStoreApi.getWineStoreDetail(deviceDetail.getStoreId());
            java.util.Map<String, Object> storeInfo = new java.util.HashMap<>();
            storeInfo.put("id", wineStoreDetail.getId());
            storeInfo.put("name", wineStoreDetail.getStoreName());
            storeInfo.put("code", wineStoreDetail.getStoreCode());
            storeInfo.put("managerId", wineStoreDetail.getStoreManagerId());
            storeInfo.put("managerName", wineStoreDetail.getStoreManagerUserName() != null ? wineStoreDetail.getStoreManagerUserName() : "王小虎");
            storeInfo.put("image", wineStoreDetail.getImageUrl() != null ? wineStoreDetail.getImageUrl() : "/static/images/store_image.jpg");
            storeInfo.put("province", wineStoreDetail.getProvince() != null ? wineStoreDetail.getProvince() : "北京");
            storeInfo.put("city", wineStoreDetail.getCity() != null ? wineStoreDetail.getCity() : "北京");
            storeInfo.put("district", wineStoreDetail.getDistrict() != null ? wineStoreDetail.getDistrict() : "朝阳区");
            storeInfo.put("fullAddress", wineStoreDetail.getFullAddress() != null ? wineStoreDetail.getFullAddress() : "北京市朝阳区建国路93号万达广场1楼");
            result.put("storeInfo", storeInfo);
        }

        // 酒品信息（如果设备绑定了酒品）
        if (StrUtil.isNotBlank(deviceDetail.getCurrentProductId())) {
            WineProductPojo wineProductDetail = wineProductApi.getWineProductDetail(deviceDetail.getCurrentProductId());
            java.util.Map<String, Object> wineInfo = new java.util.HashMap<>();
            wineInfo.put("id", wineProductDetail.getId());
            wineInfo.put("name", wineProductDetail.getProductName() != null ? wineProductDetail.getProductName() : "五粮液散酒");
            wineInfo.put("brand", wineProductDetail.getBrand()!= null ? wineProductDetail.getBrand() : "五粮液");
            wineInfo.put("type", wineProductDetail.getProductType()!= null ? wineProductDetail.getProductType() : "白酒");
            wineInfo.put("alcoholContent", wineProductDetail.getAlcoholContent()!= null ? wineProductDetail.getAlcoholContent() : 12);
            wineInfo.put("image", wineProductDetail.getImageUrl()!= null ? wineProductDetail.getImageUrl() : "/static/images/wine.png");
            wineInfo.put("price", wineProductDetail.getSuggestedPrice()!=null ? wineProductDetail.getSuggestedPrice() : 0); // 每ml价格
            // 集成真实库存查询
            try {
                BigDecimal stockQuantity = wineDeviceStockApi.getStockQuantity(deviceDetail.getId(), deviceDetail.getCurrentProductId());
                wineInfo.put("stock", stockQuantity != null ? stockQuantity.intValue() : 0);
            } catch (Exception e) {
                log.warn("查询设备库存失败，设备ID：{}，酒品ID：{}，错误：{}", 
                        deviceDetail.getId(), deviceDetail.getCurrentProductId(), e.getMessage());
                wineInfo.put("stock", 0); // 查询失败时返回0
            }
            java.util.List<java.util.Map<String, Object>> capacities = new java.util.ArrayList<>();
            // 容量规格
            if (StrUtil.isNotBlank(deviceDetail.getCurrentProductId())){
                List<WinePricePojo> winePriceList = winePriceApi.getWinePriceList(deviceDetail.getCurrentProductId());
                if (!winePriceList.isEmpty()){
                    winePriceList.forEach(price -> {
                        java.util.Map<String, Object> capacity = new java.util.HashMap<>();
                        capacity.put("id", price.getId());
                        capacity.put("size", price.getCapacity().intValue()+"ml");
                        capacity.put("sortCode", price.getSortCode());
                        capacities.add(capacity);
                    });

                }
            }
            wineInfo.put("capacities", capacities);
            result.put("wineInfo", wineInfo);
        } else {
            // 如果没有绑定酒品，返回空的酒品信息
            result.put("wineInfo", null);
        }
        
        return result;
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
} 