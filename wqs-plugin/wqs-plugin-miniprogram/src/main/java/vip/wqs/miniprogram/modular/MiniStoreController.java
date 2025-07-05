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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vip.wqs.common.annotation.CommonLog;
import vip.wqs.common.pojo.CommonResult;
import vip.wqs.store.api.WineStoreApi;
import vip.wqs.store.pojo.WineStorePojo;
import vip.wqs.store.pojo.WineStoreQueryPojo;
import vip.wqs.store.pojo.WineStoreSimplePojo;

import java.util.List;

/**
 * 小程序门店控制器
 * 
 * @author AI Assistant
 * @date 2025/01/30
 */
@Tag(name = "小程序门店控制器")
@ApiSupport(author = "AI_ASSISTANT", order = 8)
@RestController
@Validated
@Slf4j
public class MiniStoreController {

    @Resource
    private WineStoreApi wineStoreApi;

    /**
     * 获取门店分页列表
     * 注意：门店浏览无需登录，公开访问
     *
     * @author AI Assistant
     * @date 2025/01/30
     */
    @ApiOperationSupport(order = 1)
    @Operation(summary = "获取门店分页列表")
    @CommonLog("小程序获取门店列表")
    @GetMapping("/miniprogram/store/page")
    public CommonResult<Page<WineStoreSimplePojo>> getStorePage(@Valid WineStoreQueryPojo param,
                                                               @RequestParam(required = false) Double longitude,
                                                               @RequestParam(required = false) Double latitude,
                                                               @RequestParam(required = false) Double radius) {
        try {
            log.info("小程序获取门店列表，参数：{}", param);
            
            // 小程序特有逻辑：处理位置信息和搜索半径
            if (longitude != null && latitude != null) {
                param.setLongitude(longitude);
                param.setLatitude(latitude);
                if (radius != null) {
                    param.setRadius(radius);
                } else {
                    param.setRadius(5.0); // 默认5公里
                }
            }
            
            // 小程序业务逻辑：默认只查询营业中的门店
            param.setOnlyOpen(true);
            
            // 调用门店API获取分页数据
            Page<WineStoreSimplePojo> result = wineStoreApi.getWineStorePage(param);
            
            if (result == null) {
                return CommonResult.error("获取门店列表失败");
            }
            
            log.info("小程序获取门店列表成功，返回{}条数据", result.getRecords().size());
            return CommonResult.data(result);
            
        } catch (Exception e) {
            log.error("获取门店列表异常", e);
            return CommonResult.error("获取门店列表失败：" + e.getMessage());
        }
    }

    /**
     * 获取门店详情
     * 注意：门店详情浏览无需登录，公开访问
     *
     * @author AI Assistant
     * @date 2025/01/30
     */
    @ApiOperationSupport(order = 2)
    @Operation(summary = "获取门店详情")
    @CommonLog("小程序获取门店详情")
    @GetMapping("/miniprogram/store/detail")
    public CommonResult<WineStorePojo> getStoreDetail(@RequestParam String id) {
        try {
            log.info("小程序获取门店详情，门店ID：{}", id);
            
            // 1. 验证参数
            if (StrUtil.isBlank(id)) {
                return CommonResult.error("门店ID不能为空");
            }
            
            // 2. 调用门店API获取详情
            WineStorePojo result = wineStoreApi.getWineStoreDetail(id);
            
            if (result == null) {
                return CommonResult.error("门店不存在");
            }
            
            log.info("小程序获取门店详情成功，门店：{}", result.getStoreName());
            return CommonResult.data(result);
            
        } catch (Exception e) {
            log.error("获取门店详情异常，门店ID：{}", id, e);
            return CommonResult.error("获取门店详情失败：" + e.getMessage());
        }
    }

    /**
     * 获取附近门店列表
     * 注意：附近门店查询无需登录，公开访问
     *
     * @author AI Assistant
     * @date 2025/01/30
     */
    @ApiOperationSupport(order = 3)
    @Operation(summary = "获取附近门店列表")
    @CommonLog("小程序获取附近门店")
    @GetMapping("/miniprogram/store/nearby")
    public CommonResult<List<WineStoreSimplePojo>> getNearbyStores(
            @RequestParam Double longitude,
            @RequestParam Double latitude,
            @RequestParam(defaultValue = "5.0") Double radius,
            @RequestParam(defaultValue = "20") Integer limit) {
        try {
            log.info("小程序获取附近门店，经度：{}，纬度：{}，半径：{}km，限制：{}个", 
                    longitude, latitude, radius, limit);
            
            // 1. 验证参数
            if (longitude == null || latitude == null) {
                return CommonResult.error("经纬度不能为空");
            }
            
            // 2. 调用门店API获取附近门店
            List<WineStoreSimplePojo> result = wineStoreApi.getNearbyStores(longitude, latitude, radius, limit);
            
            if (result == null) {
                return CommonResult.error("获取附近门店失败");
            }
            
            log.info("小程序获取附近门店成功，返回{}个门店", result.size());
            return CommonResult.data(result);
            
        } catch (Exception e) {
            log.error("获取附近门店异常", e);
            return CommonResult.error("获取附近门店失败：" + e.getMessage());
        }
    }

    /**
     * 搜索门店
     * 注意：门店搜索无需登录，公开访问
     *
     * @author AI Assistant
     * @date 2025/01/30
     */
    @ApiOperationSupport(order = 4)
    @Operation(summary = "搜索门店")
    @CommonLog("小程序搜索门店")
    @GetMapping("/miniprogram/store/search")
    public CommonResult<List<WineStoreSimplePojo>> searchStores(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "10") Integer limit) {
        try {
            log.info("小程序搜索门店，关键词：{}，限制：{}个", keyword, limit);
            
            // 1. 验证参数
            if (StrUtil.isBlank(keyword)) {
                return CommonResult.error("搜索关键词不能为空");
            }
            
            // 2. 调用门店API搜索门店
            List<WineStoreSimplePojo> result = wineStoreApi.searchStores(keyword, limit);
            
            if (result == null) {
                return CommonResult.error("搜索门店失败");
            }
            
            log.info("小程序搜索门店成功，返回{}个门店", result.size());
            return CommonResult.data(result);
            
        } catch (Exception e) {
            log.error("搜索门店异常，关键词：{}", keyword, e);
            return CommonResult.error("搜索门店失败：" + e.getMessage());
        }
    }

    /**
     * 获取推荐门店列表
     * 注意：推荐门店无需登录，公开访问
     *
     * @author AI Assistant
     * @date 2025/01/30
     */
    @ApiOperationSupport(order = 5)
    @Operation(summary = "获取推荐门店列表")
    @CommonLog("小程序获取推荐门店")
    @GetMapping("/miniprogram/store/recommended")
    public CommonResult<List<WineStoreSimplePojo>> getRecommendedStores(
            @RequestParam(defaultValue = "10") Integer limit) {
        try {
            log.info("小程序获取推荐门店，限制：{}个", limit);
            
            // 1. 调用门店API获取推荐门店
            List<WineStoreSimplePojo> result = wineStoreApi.getRecommendedStores(limit);
            
            if (result == null) {
                return CommonResult.error("获取推荐门店失败");
            }
            
            log.info("小程序获取推荐门店成功，返回{}个门店", result.size());
            return CommonResult.data(result);
            
        } catch (Exception e) {
            log.error("获取推荐门店异常", e);
            return CommonResult.error("获取推荐门店失败：" + e.getMessage());
        }
    }

    /**
     * 检查门店是否营业
     * 注意：营业状态查询无需登录，公开访问
     *
     * @author AI Assistant
     * @date 2025/01/30
     */
    @ApiOperationSupport(order = 6)
    @Operation(summary = "检查门店是否营业")
    @CommonLog("小程序检查门店营业状态")
    @GetMapping("/miniprogram/store/check-open")
    public CommonResult<Boolean> checkStoreOpen(@RequestParam String storeId) {
        try {
            log.info("小程序检查门店营业状态，门店ID：{}", storeId);
            
            // 1. 验证参数
            if (StrUtil.isBlank(storeId)) {
                return CommonResult.error("门店ID不能为空");
            }
            
            // 2. 调用门店API检查营业状态
            Boolean result = wineStoreApi.checkStoreOpen(storeId);
            
            if (result == null) {
                return CommonResult.error("检查门店营业状态失败");
            }
            
            log.info("小程序检查门店营业状态成功，门店ID：{}，营业状态：{}", storeId, result);
            return CommonResult.data(result);
            
        } catch (Exception e) {
            log.error("检查门店营业状态异常，门店ID：{}", storeId, e);
            return CommonResult.error("检查门店营业状态失败：" + e.getMessage());
        }
    }

    /**
     * 获取门店设备列表
     * 注意：设备列表查询无需登录，公开访问
     *
     * @author AI Assistant
     * @date 2025/01/30
     */
    @ApiOperationSupport(order = 7)
    @Operation(summary = "获取门店设备列表")
    @CommonLog("小程序获取门店设备列表")
    @GetMapping("/miniprogram/store/{storeId}/devices")
    public CommonResult<List<Object>> getStoreDevices(@PathVariable String storeId) {
        try {
            log.info("小程序获取门店设备列表，门店ID：{}", storeId);
            
            // 1. 验证参数
            if (StrUtil.isBlank(storeId)) {
                return CommonResult.error("门店ID不能为空");
            }
            
            // 2. 调用门店API获取设备列表
            List<Object> result = wineStoreApi.getStoreDevices(storeId);
            
            if (result == null) {
                return CommonResult.error("获取门店设备列表失败");
            }
            
            log.info("小程序获取门店设备列表成功，门店ID：{}，设备数量：{}", storeId, result.size());
            return CommonResult.data(result);
            
        } catch (Exception e) {
            log.error("获取门店设备列表异常，门店ID：{}", storeId, e);
            return CommonResult.error("获取门店设备列表失败：" + e.getMessage());
        }
    }

    /**
     * 获取门店统计信息
     * 注意：统计信息查询无需登录，公开访问
     *
     * @author AI Assistant
     * @date 2025/01/30
     */
    @ApiOperationSupport(order = 8)
    @Operation(summary = "获取门店统计信息")
    @CommonLog("小程序获取门店统计信息")
    @GetMapping("/miniprogram/store/{storeId}/statistics")
    public CommonResult<Object> getStoreStatistics(@PathVariable String storeId) {
        try {
            log.info("小程序获取门店统计信息，门店ID：{}", storeId);
            
            // 1. 验证参数
            if (StrUtil.isBlank(storeId)) {
                return CommonResult.error("门店ID不能为空");
            }
            
            // 2. 调用门店API获取统计信息
            Object result = wineStoreApi.getStoreStatistics(storeId);
            
            if (result == null) {
                return CommonResult.error("获取门店统计信息失败");
            }
            
            log.info("小程序获取门店统计信息成功，门店ID：{}", storeId);
            return CommonResult.data(result);
            
        } catch (Exception e) {
            log.error("获取门店统计信息异常，门店ID：{}", storeId, e);
            return CommonResult.error("获取门店统计信息失败：" + e.getMessage());
        }
    }


} 