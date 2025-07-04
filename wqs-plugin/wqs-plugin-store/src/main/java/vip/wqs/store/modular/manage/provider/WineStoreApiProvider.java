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
package vip.wqs.store.modular.manage.provider;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vip.wqs.common.exception.CommonException;
import vip.wqs.store.api.WineStoreApi;
import vip.wqs.store.modular.manage.entity.WineStore;
import vip.wqs.store.modular.manage.param.WineStorePageParam;
import vip.wqs.store.modular.manage.service.WineStoreService;
import vip.wqs.store.pojo.WineStorePojo;
import vip.wqs.store.pojo.WineStoreQueryPojo;
import vip.wqs.store.pojo.WineStoreSimplePojo;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 门店API实现类 - 遵循小程序接口开发规范
 * 
 * @author AI Assistant
 * @date 2025/01/30
 */
@Service
@Slf4j
public class WineStoreApiProvider implements WineStoreApi {

    @Resource
    private WineStoreService wineStoreService;

    @Override
    public Page<WineStoreSimplePojo> getWineStorePage(WineStoreQueryPojo param) {
        log.info("分页查询门店列表，参数：{}", param);
        
        try {
            // 1. 参数转换
            WineStorePageParam pageParam = convertToPageParam(param);
            
            // 2. 业务查询
            Page<WineStore> pageResult = wineStoreService.page(pageParam);
            
            // 3. 结果转换
            List<WineStoreSimplePojo> records = pageResult.getRecords().stream()
                    .map(this::convertToSimplePojo)
                    .collect(Collectors.toList());
            
            Page<WineStoreSimplePojo> result = new Page<>(pageParam.getCurrent() != null ? pageParam.getCurrent() : 1, 
                    pageParam.getSize() != null ? pageParam.getSize() : 10);
            result.setRecords(records);
            result.setTotal(pageResult.getTotal());
            
            return result;
            
        } catch (Exception e) {
            log.error("分页查询门店列表失败", e);
            throw new CommonException("查询门店列表失败：" + e.getMessage());
        }
    }

    @Override
    public WineStorePojo getWineStoreDetail(String id) {
        log.info("获取门店详情，门店ID：{}", id);
        
        try {
            // 1. 参数验证
            if (StrUtil.isBlank(id)) {
                throw new CommonException("门店ID不能为空");
            }
            
            // 2. 查询实体
            WineStore entity = wineStoreService.getById(id);
            if (ObjectUtil.isEmpty(entity)) {
                throw new CommonException("门店不存在");
            }
            
            // 3. 转换为POJO
            WineStorePojo pojo = convertToPojo(entity);
            
            // 4. 获取关联数据
            enrichStoreDetail(pojo);
            
            // 5. 更新浏览次数（异步处理）
            incrementViewCount(id);
            
            return pojo;
            
        } catch (Exception e) {
            log.error("获取门店详情失败，门店ID：{}", id, e);
            throw new CommonException("获取门店详情失败：" + e.getMessage());
        }
    }

    @Override
    public WineStorePojo getStoreByCode(String storeCode) {
        log.info("根据编码获取门店信息，门店编码：{}", storeCode);
        
        try {
            // 1. 参数验证
            if (StrUtil.isBlank(storeCode)) {
                throw new CommonException("门店编码不能为空");
            }
            
            // 2. 查询实体
            LambdaQueryWrapper<WineStore> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(WineStore::getStoreCode, storeCode);
            WineStore entity = wineStoreService.getOne(wrapper);
            
            if (ObjectUtil.isEmpty(entity)) {
                throw new CommonException("门店不存在");
            }
            
            // 3. 转换为POJO
            WineStorePojo pojo = convertToPojo(entity);
            
            // 4. 获取关联数据
            enrichStoreDetail(pojo);
            
            return pojo;
            
        } catch (Exception e) {
            log.error("根据编码获取门店信息失败，门店编码：{}", storeCode, e);
            throw new CommonException("获取门店信息失败：" + e.getMessage());
        }
    }

    @Override
    public List<WineStoreSimplePojo> getNearbyStores(Double longitude, Double latitude, Double radius, Integer limit) {
        log.info("获取附近门店列表，经度：{}，纬度：{}，半径：{}km，限制：{}个", longitude, latitude, radius, limit);
        
        try {
            // 1. 参数验证
            if (ObjectUtil.isEmpty(longitude) || ObjectUtil.isEmpty(latitude)) {
                throw new CommonException("经纬度不能为空");
            }
            
            if (ObjectUtil.isEmpty(radius)) {
                radius = 5.0; // 默认5公里
            }
            
            if (ObjectUtil.isEmpty(limit)) {
                limit = 20; // 默认20个
            }
            
            // 2. 查询所有启用的门店
            LambdaQueryWrapper<WineStore> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(WineStore::getStatus, "ENABLE");
            wrapper.orderByAsc(WineStore::getSortCode);
            List<WineStore> allStores = wineStoreService.list(wrapper);
            
            if (CollUtil.isEmpty(allStores)) {
                return new ArrayList<>();
            }
            
            // 3. 计算距离并筛选
            Double finalRadius = radius;
            List<WineStoreSimplePojo> nearbyStores = allStores.stream()
                    .map(this::convertToSimplePojo)
                    .map(store -> {
                        // TODO: 这里需要根据实际情况获取门店的经纬度
                        // 暂时模拟距离计算
                        double distance = calculateDistance(longitude, latitude, 
                                116.4074 + Math.random() * 0.1, // 模拟经度
                                39.9042 + Math.random() * 0.1); // 模拟纬度
                        store.setDistance(Math.round(distance * 100.0) / 100.0); // 保留两位小数
                        return store;
                    })
                    .filter(store -> store.getDistance() <= finalRadius)
                    .sorted(Comparator.comparing(WineStoreSimplePojo::getDistance))
                    .limit(limit)
                    .collect(Collectors.toList());
            
            log.info("获取附近门店列表成功，返回{}个门店", nearbyStores.size());
            return nearbyStores;
            
        } catch (Exception e) {
            log.error("获取附近门店列表失败", e);
            throw new CommonException("获取附近门店列表失败：" + e.getMessage());
        }
    }

    @Override
    public List<WineStoreSimplePojo> searchStores(String keyword, Integer limit) {
        log.info("搜索门店，关键词：{}，限制：{}个", keyword, limit);
        
        try {
            // 1. 参数验证
            if (StrUtil.isBlank(keyword)) {
                return new ArrayList<>();
            }
            
            if (ObjectUtil.isEmpty(limit)) {
                limit = 10; // 默认10个
            }
            
            // 2. 构建查询条件
            LambdaQueryWrapper<WineStore> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(WineStore::getStatus, "ENABLE");
            wrapper.and(w -> w.like(WineStore::getStoreName, keyword)
                    .or().like(WineStore::getStoreCode, keyword)
                    .or().like(WineStore::getDetailAddress, keyword)
                    .or().like(WineStore::getCity, keyword)
                    .or().like(WineStore::getDistrict, keyword));
            wrapper.orderByDesc(WineStore::getCreateTime);
            wrapper.last("LIMIT " + limit);
            
            // 3. 查询并转换
            List<WineStore> stores = wineStoreService.list(wrapper);
            List<WineStoreSimplePojo> result = stores.stream()
                    .map(this::convertToSimplePojo)
                    .collect(Collectors.toList());
            
            log.info("搜索门店成功，返回{}个门店", result.size());
            return result;
            
        } catch (Exception e) {
            log.error("搜索门店失败，关键词：{}", keyword, e);
            throw new CommonException("搜索门店失败：" + e.getMessage());
        }
    }

    @Override
    public List<WineStoreSimplePojo> getRecommendedStores(Integer limit) {
        log.info("获取推荐门店列表，限制：{}个", limit);
        
        try {
            if (ObjectUtil.isEmpty(limit)) {
                limit = 10; // 默认10个
            }
            
            // 查询启用的门店，按排序码和创建时间排序
            LambdaQueryWrapper<WineStore> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(WineStore::getStatus, "ENABLE");
            wrapper.orderByAsc(WineStore::getSortCode);
            wrapper.orderByDesc(WineStore::getCreateTime);
            wrapper.last("LIMIT " + limit);
            
            List<WineStore> stores = wineStoreService.list(wrapper);
            List<WineStoreSimplePojo> result = stores.stream()
                    .map(this::convertToSimplePojo)
                    .collect(Collectors.toList());
            
            log.info("获取推荐门店列表成功，返回{}个门店", result.size());
            return result;
            
        } catch (Exception e) {
            log.error("获取推荐门店列表失败", e);
            throw new CommonException("获取推荐门店列表失败：" + e.getMessage());
        }
    }

    @Override
    public Boolean checkStoreOpen(String storeId) {
        log.info("检查门店是否营业，门店ID：{}", storeId);
        
        try {
            // 1. 参数验证
            if (StrUtil.isBlank(storeId)) {
                throw new CommonException("门店ID不能为空");
            }
            
            // 2. 查询门店
            WineStore store = wineStoreService.getById(storeId);
            if (ObjectUtil.isEmpty(store)) {
                throw new CommonException("门店不存在");
            }
            
            // 3. 检查门店状态
            if (!"ENABLE".equals(store.getStatus())) {
                return false;
            }
            
            // 4. 检查营业时间
            String businessHours = store.getBusinessHours();
            if (StrUtil.isBlank(businessHours)) {
                return true; // 如果没有设置营业时间，默认营业
            }
            
            return isWithinBusinessHours(businessHours);
            
        } catch (Exception e) {
            log.error("检查门店是否营业失败，门店ID：{}", storeId, e);
            return false;
        }
    }

    @Override
    public List<Object> getStoreDevices(String storeId) {
        log.info("获取门店设备列表，门店ID：{}", storeId);
        
        try {
            // TODO: 这里需要调用设备API获取门店下的设备列表
            // 暂时返回空列表
            return new ArrayList<>();
            
        } catch (Exception e) {
            log.error("获取门店设备列表失败，门店ID：{}", storeId, e);
            throw new CommonException("获取门店设备列表失败：" + e.getMessage());
        }
    }

    @Override
    public Object getStoreStatistics(String storeId) {
        log.info("获取门店统计信息，门店ID：{}", storeId);
        
        try {
            // TODO: 这里需要统计门店的设备数量、酒品数量等信息
            Map<String, Object> statistics = new HashMap<>();
            statistics.put("deviceCount", 0);
            statistics.put("productCount", 0);
            statistics.put("orderCount", 0);
            statistics.put("salesAmount", 0.0);
            
            return statistics;
            
        } catch (Exception e) {
            log.error("获取门店统计信息失败，门店ID：{}", storeId, e);
            throw new CommonException("获取门店统计信息失败：" + e.getMessage());
        }
    }

    @Override
    public Boolean incrementViewCount(String storeId) {
        try {
            // TODO: 这里可以异步更新门店的浏览次数
            // 暂时返回true
            return true;
            
        } catch (Exception e) {
            log.error("更新门店浏览次数失败，门店ID：{}", storeId, e);
            return false;
        }
    }

    /**
     * 查询参数转换为分页参数
     */
    private WineStorePageParam convertToPageParam(WineStoreQueryPojo param) {
        WineStorePageParam pageParam = new WineStorePageParam();
        BeanUtil.copyProperties(param, pageParam);
        
        // 设置分页参数
        pageParam.setCurrent(param.getPageNum());
        pageParam.setSize(param.getPageSize());
        
        // 设置搜索关键词
        if (StrUtil.isNotBlank(param.getKeyword())) {
            pageParam.setSearchKey(param.getKeyword());
        }
        
        // 处理排序
        if (StrUtil.isNotBlank(param.getSortField())) {
            pageParam.setSortField(param.getSortField());
            // 转换排序方式：ASC -> ascend, DESC -> descend
            String sortOrder = param.getSortOrder();
            if (StrUtil.isNotBlank(sortOrder)) {
                if ("ASC".equalsIgnoreCase(sortOrder)) {
                    pageParam.setSortOrder("ascend");
                } else if ("DESC".equalsIgnoreCase(sortOrder)) {
                    pageParam.setSortOrder("descend");
                } else {
                    pageParam.setSortOrder("descend"); // 默认降序
                }
            } else {
                pageParam.setSortOrder("descend"); // 默认降序
            }
        }
        
        return pageParam;
    }

    /**
     * 实体转换为简化POJO
     */
    private WineStoreSimplePojo convertToSimplePojo(WineStore entity) {
        WineStoreSimplePojo pojo = new WineStoreSimplePojo();
        BeanUtil.copyProperties(entity, pojo);
        
        // 构建完整地址
        String fullAddress = buildFullAddress(entity.getProvince(), entity.getCity(), 
                entity.getDistrict(), entity.getDetailAddress());
        pojo.setFullAddress(fullAddress);
        
        // 设置营业状态
        pojo.setIsOpen("ENABLE".equals(entity.getStatus()) && 
                isWithinBusinessHours(entity.getBusinessHours()));
        
        // 设置默认评分和浏览次数
        pojo.setRating(4.8); // 默认评分
        pojo.setViewCount(0); // TODO: 从统计表获取真实浏览次数
        
        return pojo;
    }

    /**
     * 实体转换为完整POJO
     */
    private WineStorePojo convertToPojo(WineStore entity) {
        WineStorePojo pojo = new WineStorePojo();
        BeanUtil.copyProperties(entity, pojo);
        
        // 构建完整地址
        String fullAddress = buildFullAddress(entity.getProvince(), entity.getCity(), 
                entity.getDistrict(), entity.getDetailAddress());
        pojo.setFullAddress(fullAddress);
        
        // 设置营业状态
        pojo.setIsOpen("ENABLE".equals(entity.getStatus()) && 
                isWithinBusinessHours(entity.getBusinessHours()));
        
        // 设置默认评分和浏览次数
        pojo.setRating(4.8); // 默认评分
        pojo.setViewCount(0); // TODO: 从统计表获取真实浏览次数
        
        return pojo;
    }

    /**
     * 丰富门店详情信息
     */
    private void enrichStoreDetail(WineStorePojo pojo) {
        // TODO: 获取详细营业时间
        Map<String, String> businessHoursDetail = new HashMap<>();
        businessHoursDetail.put("1", pojo.getBusinessHours()); // 周一
        businessHoursDetail.put("2", pojo.getBusinessHours()); // 周二
        businessHoursDetail.put("3", pojo.getBusinessHours()); // 周三
        businessHoursDetail.put("4", pojo.getBusinessHours()); // 周四
        businessHoursDetail.put("5", pojo.getBusinessHours()); // 周五
        businessHoursDetail.put("6", pojo.getBusinessHours()); // 周六
        businessHoursDetail.put("0", pojo.getBusinessHours()); // 周日
        pojo.setBusinessHoursDetail(businessHoursDetail);
        
        // TODO: 获取门店优惠活动
        List<WineStorePojo.StorePromotionPojo> promotions = new ArrayList<>();
        pojo.setPromotions(promotions);
        
        // TODO: 获取门店统计信息
        pojo.setDeviceCount(0);
        pojo.setProductCount(0);
    }

    /**
     * 构建完整地址
     */
    private String buildFullAddress(String province, String city, String district, String detailAddress) {
        StringBuilder address = new StringBuilder();
        if (StrUtil.isNotBlank(province)) {
            address.append(province);
        }
        if (StrUtil.isNotBlank(city)) {
            address.append(city);
        }
        if (StrUtil.isNotBlank(district)) {
            address.append(district);
        }
        if (StrUtil.isNotBlank(detailAddress)) {
            address.append(detailAddress);
        }
        return address.toString();
    }

    /**
     * 检查是否在营业时间内
     */
    private boolean isWithinBusinessHours(String businessHours) {
        if (StrUtil.isBlank(businessHours)) {
            return true; // 如果没有设置营业时间，默认营业
        }
        
        try {
            // 简单的营业时间检查，格式：08:00-22:00
            if (businessHours.contains("-")) {
                String[] times = businessHours.split("-");
                if (times.length == 2) {
                    LocalTime openTime = LocalTime.parse(times[0].trim());
                    LocalTime closeTime = LocalTime.parse(times[1].trim());
                    LocalTime currentTime = LocalTime.now();
                    
                    return currentTime.isAfter(openTime) && currentTime.isBefore(closeTime);
                }
            }
            
            return true; // 如果格式不正确，默认营业
            
        } catch (Exception e) {
            log.warn("解析营业时间失败，营业时间：{}", businessHours, e);
            return true; // 解析失败，默认营业
        }
    }

    /**
     * 计算两点间距离（公里）
     * 使用Haversine公式
     */
    private double calculateDistance(double lon1, double lat1, double lon2, double lat2) {
        final double R = 6371; // 地球半径（公里）
        
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return R * c;
    }
}
