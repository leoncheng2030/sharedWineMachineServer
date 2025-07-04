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
package vip.wqs.commission.modular.record.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fhs.core.trans.anno.TransMethodResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.wqs.commission.modular.account.service.WineUserAccountService;
import vip.wqs.commission.modular.config.entity.WineCommission;
import vip.wqs.commission.modular.config.service.WineCommissionService;
import vip.wqs.commission.modular.record.entity.WineCommissionRecord;
import vip.wqs.commission.modular.record.mapper.WineCommissionRecordMapper;
import vip.wqs.commission.modular.record.param.WineCommissionRecordIdParam;
import vip.wqs.commission.modular.record.param.WineCommissionRecordPageParam;
import vip.wqs.commission.modular.record.service.WineCommissionRecordService;
import vip.wqs.common.enums.CommonSortOrderEnum;
import vip.wqs.common.exception.CommonException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 佣金记录Service接口实现类
 *
 * @author WQS_TEAM
 * @date 2025-01-29
 */
@Slf4j
@Service
public class WineCommissionRecordServiceImpl extends ServiceImpl<WineCommissionRecordMapper, WineCommissionRecord> implements WineCommissionRecordService {

    @Autowired
    private WineCommissionService wineCommissionService;
    
    @Autowired
    private WineUserAccountService wineUserAccountService;

    @Override
    @TransMethodResult
    public Page<WineCommissionRecord> page(WineCommissionRecordPageParam wineCommissionRecordPageParam) {
        QueryWrapper<WineCommissionRecord> queryWrapper = new QueryWrapper<WineCommissionRecord>().checkSqlInjection();
        
        // 关键词搜索
        if (StrUtil.isNotBlank(wineCommissionRecordPageParam.getSearchKey())) {
            queryWrapper.and(wrapper -> wrapper.lambda()
                .like(WineCommissionRecord::getOrderNo, wineCommissionRecordPageParam.getSearchKey())
                .or().like(WineCommissionRecord::getUserNickname, wineCommissionRecordPageParam.getSearchKey())
                .or().like(WineCommissionRecord::getWineName, wineCommissionRecordPageParam.getSearchKey())
                .or().like(WineCommissionRecord::getDeviceCode, wineCommissionRecordPageParam.getSearchKey())
            );
        }
        
        // 订单ID
        if (StrUtil.isNotBlank(wineCommissionRecordPageParam.getOrderId())) {
            queryWrapper.lambda().eq(WineCommissionRecord::getOrderId, wineCommissionRecordPageParam.getOrderId());
        }
        
        // 订单号
        if (StrUtil.isNotBlank(wineCommissionRecordPageParam.getOrderNo())) {
            queryWrapper.lambda().like(WineCommissionRecord::getOrderNo, wineCommissionRecordPageParam.getOrderNo());
        }
        
        // 受益用户ID
        if (StrUtil.isNotBlank(wineCommissionRecordPageParam.getUserId())) {
            queryWrapper.lambda().eq(WineCommissionRecord::getUserId, wineCommissionRecordPageParam.getUserId());
        }
        
        // 用户昵称
        if (StrUtil.isNotBlank(wineCommissionRecordPageParam.getUserNickname())) {
            queryWrapper.lambda().like(WineCommissionRecord::getUserNickname, wineCommissionRecordPageParam.getUserNickname());
        }
        
        // 设备ID
        if (StrUtil.isNotBlank(wineCommissionRecordPageParam.getDeviceId())) {
            queryWrapper.lambda().eq(WineCommissionRecord::getDeviceId, wineCommissionRecordPageParam.getDeviceId());
        }
        
        // 设备编码
        if (StrUtil.isNotBlank(wineCommissionRecordPageParam.getDeviceCode())) {
            queryWrapper.lambda().like(WineCommissionRecord::getDeviceCode, wineCommissionRecordPageParam.getDeviceCode());
        }
        
        // 酒品ID
        if (StrUtil.isNotBlank(wineCommissionRecordPageParam.getWineId())) {
            queryWrapper.lambda().eq(WineCommissionRecord::getWineId, wineCommissionRecordPageParam.getWineId());
        }
        
        // 酒品名称
        if (StrUtil.isNotBlank(wineCommissionRecordPageParam.getWineName())) {
            queryWrapper.lambda().like(WineCommissionRecord::getWineName, wineCommissionRecordPageParam.getWineName());
        }
        
        // 佣金类型
        if (StrUtil.isNotBlank(wineCommissionRecordPageParam.getCommissionType())) {
            queryWrapper.lambda().eq(WineCommissionRecord::getCommissionType, wineCommissionRecordPageParam.getCommissionType());
        }
        
        // 佣金状态
        if (StrUtil.isNotBlank(wineCommissionRecordPageParam.getStatus())) {
            queryWrapper.lambda().eq(WineCommissionRecord::getStatus, wineCommissionRecordPageParam.getStatus());
        }
        
        // 订单金额范围
        if (ObjectUtil.isNotNull(wineCommissionRecordPageParam.getMinOrderAmount())) {
            queryWrapper.lambda().ge(WineCommissionRecord::getOrderAmount, wineCommissionRecordPageParam.getMinOrderAmount());
        }
        if (ObjectUtil.isNotNull(wineCommissionRecordPageParam.getMaxOrderAmount())) {
            queryWrapper.lambda().le(WineCommissionRecord::getOrderAmount, wineCommissionRecordPageParam.getMaxOrderAmount());
        }
        
        // 佣金金额范围
        if (ObjectUtil.isNotNull(wineCommissionRecordPageParam.getMinCommissionAmount())) {
            queryWrapper.lambda().ge(WineCommissionRecord::getCommissionAmount, wineCommissionRecordPageParam.getMinCommissionAmount());
        }
        if (ObjectUtil.isNotNull(wineCommissionRecordPageParam.getMaxCommissionAmount())) {
            queryWrapper.lambda().le(WineCommissionRecord::getCommissionAmount, wineCommissionRecordPageParam.getMaxCommissionAmount());
        }
        
        // 计算时间范围
        if (ObjectUtil.isNotNull(wineCommissionRecordPageParam.getCalculateTimeStart())) {
            queryWrapper.lambda().ge(WineCommissionRecord::getCalculateTime, wineCommissionRecordPageParam.getCalculateTimeStart());
        }
        if (ObjectUtil.isNotNull(wineCommissionRecordPageParam.getCalculateTimeEnd())) {
            queryWrapper.lambda().le(WineCommissionRecord::getCalculateTime, wineCommissionRecordPageParam.getCalculateTimeEnd());
        }
        
        // 发放时间范围
        if (ObjectUtil.isNotNull(wineCommissionRecordPageParam.getSettleTimeStart())) {
            queryWrapper.lambda().ge(WineCommissionRecord::getSettleTime, wineCommissionRecordPageParam.getSettleTimeStart());
        }
        if (ObjectUtil.isNotNull(wineCommissionRecordPageParam.getSettleTimeEnd())) {
            queryWrapper.lambda().le(WineCommissionRecord::getSettleTime, wineCommissionRecordPageParam.getSettleTimeEnd());
        }
        
        // 创建时间范围
        if (ObjectUtil.isNotNull(wineCommissionRecordPageParam.getCreateTimeStart())) {
            queryWrapper.lambda().ge(WineCommissionRecord::getCreateTime, wineCommissionRecordPageParam.getCreateTimeStart());
        }
        if (ObjectUtil.isNotNull(wineCommissionRecordPageParam.getCreateTimeEnd())) {
            queryWrapper.lambda().le(WineCommissionRecord::getCreateTime, wineCommissionRecordPageParam.getCreateTimeEnd());
        }
        
        // 排序
        if (ObjectUtil.isAllNotEmpty(wineCommissionRecordPageParam.getSortField(), wineCommissionRecordPageParam.getSortOrder())) {
            CommonSortOrderEnum.validate(wineCommissionRecordPageParam.getSortOrder());
            if (wineCommissionRecordPageParam.getSortOrder().equals(CommonSortOrderEnum.ASC.getValue())) {
                queryWrapper.orderByAsc(StrUtil.toUnderlineCase(wineCommissionRecordPageParam.getSortField()));
            } else {
                queryWrapper.orderByDesc(StrUtil.toUnderlineCase(wineCommissionRecordPageParam.getSortField()));
            }
        } else {
            queryWrapper.lambda().orderByDesc(WineCommissionRecord::getCreateTime);
        }
        
        // 创建分页对象
        Page<WineCommissionRecord> page = new Page<>();
        if (ObjectUtil.isNotNull(wineCommissionRecordPageParam.getCurrent())) {
            page.setCurrent(wineCommissionRecordPageParam.getCurrent());
        } else {
            page.setCurrent(1);
        }
        if (ObjectUtil.isNotNull(wineCommissionRecordPageParam.getSize())) {
            page.setSize(wineCommissionRecordPageParam.getSize());
        } else {
            page.setSize(20);
        }
        
        return this.page(page, queryWrapper);
    }

    @Override
    @TransMethodResult
    public List<WineCommissionRecord> list(WineCommissionRecordPageParam wineCommissionRecordPageParam) {
        QueryWrapper<WineCommissionRecord> queryWrapper = new QueryWrapper<>();
        
        // 佣金状态
        if (StrUtil.isNotBlank(wineCommissionRecordPageParam.getStatus())) {
            queryWrapper.eq("status", wineCommissionRecordPageParam.getStatus());
        }
        
        // 佣金类型
        if (StrUtil.isNotBlank(wineCommissionRecordPageParam.getCommissionType())) {
            queryWrapper.eq("commission_type", wineCommissionRecordPageParam.getCommissionType());
        }
        
        // 受益用户ID
        if (StrUtil.isNotBlank(wineCommissionRecordPageParam.getUserId())) {
            queryWrapper.eq("user_id", wineCommissionRecordPageParam.getUserId());
        }
        
        queryWrapper.orderByDesc("create_time");
        return this.list(queryWrapper);
    }

    @Override
    public WineCommissionRecord detail(WineCommissionRecordIdParam wineCommissionRecordIdParam) {
        return this.getById(wineCommissionRecordIdParam.getId());
    }

    @Override
    public WineCommissionRecord queryEntity(String id) {
        return this.getById(id);
    }

    @Override
    public void export(WineCommissionRecordPageParam wineCommissionRecordPageParam) {
        // TODO: 实现佣金记录导出功能
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchSettle(List<WineCommissionRecordIdParam> wineCommissionRecordIdParamList) {
        List<String> ids = wineCommissionRecordIdParamList.stream()
                .map(WineCommissionRecordIdParam::getId)
                .collect(Collectors.toList());
        
        for (String id : ids) {
            WineCommissionRecord record = this.getById(id);
            if (record != null && "CALCULATED".equals(record.getStatus())) {
                record.setStatus("SETTLED");
                record.setSettleTime(new Date());
                this.updateById(record);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchFreeze(List<WineCommissionRecordIdParam> wineCommissionRecordIdParamList) {
        List<String> ids = wineCommissionRecordIdParamList.stream()
                .map(WineCommissionRecordIdParam::getId)
                .collect(Collectors.toList());
        
        for (String id : ids) {
            WineCommissionRecord record = this.getById(id);
            if (record != null && !"CANCELLED".equals(record.getStatus())) {
                record.setStatus("FROZEN");
                this.updateById(record);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchCancel(List<WineCommissionRecordIdParam> wineCommissionRecordIdParamList) {
        List<String> ids = wineCommissionRecordIdParamList.stream()
                .map(WineCommissionRecordIdParam::getId)
                .collect(Collectors.toList());
        
        for (String id : ids) {
            WineCommissionRecord record = this.getById(id);
            if (record != null && !"SETTLED".equals(record.getStatus())) {
                record.setStatus("CANCELLED");
                this.updateById(record);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void settle(WineCommissionRecordIdParam wineCommissionRecordIdParam) {
        WineCommissionRecord record = this.getById(wineCommissionRecordIdParam.getId());
        if (record == null) {
            throw new CommonException("佣金记录不存在");
        }
        
        if (!"CALCULATED".equals(record.getStatus())) {
            throw new CommonException("只有已计算状态的佣金记录才能发放");
        }
        
        record.setStatus("SETTLED");
        record.setSettleTime(new Date());
        this.updateById(record);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void freeze(WineCommissionRecordIdParam wineCommissionRecordIdParam) {
        WineCommissionRecord record = this.getById(wineCommissionRecordIdParam.getId());
        if (record == null) {
            throw new CommonException("佣金记录不存在");
        }
        
        if ("CANCELLED".equals(record.getStatus())) {
            throw new CommonException("已取消的佣金记录不能冻结");
        }
        
        record.setStatus("FROZEN");
        this.updateById(record);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void cancel(WineCommissionRecordIdParam wineCommissionRecordIdParam) {
        WineCommissionRecord record = this.getById(wineCommissionRecordIdParam.getId());
        if (record == null) {
            throw new CommonException("佣金记录不存在");
        }
        
        if ("SETTLED".equals(record.getStatus())) {
            throw new CommonException("已发放的佣金记录不能取消");
        }
        
        record.setStatus("CANCELLED");
        this.updateById(record);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void recalculate(WineCommissionRecordIdParam wineCommissionRecordIdParam) {
        WineCommissionRecord record = this.getById(wineCommissionRecordIdParam.getId());
        if (record == null) {
            throw new CommonException("佣金记录不存在");
        }
        
        if ("SETTLED".equals(record.getStatus())) {
            throw new CommonException("已发放的佣金记录不能重新计算");
        }
        
        // TODO: 实现重新计算佣金逻辑
        record.setStatus("CALCULATED");
        record.setCalculateTime(new Date());
        this.updateById(record);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int distributeCommissionForOrder(String orderId, String orderNo, BigDecimal orderAmount, 
                                          String deviceId, String wineId, String storeId) {
        try {
            log.info("开始为订单分配佣金，订单ID：{}，订单号：{}，订单金额：{}，设备ID：{}，酒品ID：{}，门店ID：{}", 
                    orderId, orderNo, orderAmount, deviceId, wineId, storeId);
            
            // 1. 获取分成配置（按优先级查找）
            WineCommission commissionConfig = wineCommissionService.getEffectiveCommission(storeId, wineId);
            if (commissionConfig == null) {
                log.warn("未找到有效的佣金配置，订单ID：{}", orderId);
                return 0;
            }
            
            log.info("找到佣金配置，配置ID：{}，平台比例：{}%，设备方比例：{}%，场地方比例：{}%，门店管理员比例：{}%，供应商比例：{}%", 
                    "配置ID", commissionConfig.getPlatformRate(), commissionConfig.getDeviceOwnerRate(),
                    commissionConfig.getLocationProviderRate(), commissionConfig.getStoreManagerRate(), commissionConfig.getSupplierRate());
            
            // 2. 分配各角色佣金
            List<WineCommissionRecord> commissionRecords = new ArrayList<>();
            
            // 平台方佣金
            if (commissionConfig.getPlatformRate() != null && commissionConfig.getPlatformRate().compareTo(BigDecimal.ZERO) > 0) {
                String platformUserId = findRoleOwnerId("PLATFORM", deviceId, storeId, wineId);
                if (StrUtil.isNotBlank(platformUserId)) {
                    BigDecimal commissionAmount = calculateCommissionAmount(orderAmount, commissionConfig.getPlatformRate());
                    WineCommissionRecord record = createCommissionRecord(orderId, orderNo, orderAmount, deviceId, wineId, 
                            platformUserId, "PLATFORM", commissionConfig.getPlatformRate(), commissionAmount);
                    commissionRecords.add(record);
                    log.info("创建平台方佣金记录，用户ID：{}，佣金金额：{}", platformUserId, commissionAmount);
                }
            }
            
            // 设备方佣金
            if (commissionConfig.getDeviceOwnerRate() != null && commissionConfig.getDeviceOwnerRate().compareTo(BigDecimal.ZERO) > 0) {
                String deviceOwnerUserId = findRoleOwnerId("DEVICE_OWNER", deviceId, storeId, wineId);
                if (StrUtil.isNotBlank(deviceOwnerUserId)) {
                    BigDecimal commissionAmount = calculateCommissionAmount(orderAmount, commissionConfig.getDeviceOwnerRate());
                    WineCommissionRecord record = createCommissionRecord(orderId, orderNo, orderAmount, deviceId, wineId, 
                            deviceOwnerUserId, "DEVICE_OWNER", commissionConfig.getDeviceOwnerRate(), commissionAmount);
                    commissionRecords.add(record);
                    log.info("创建设备方佣金记录，用户ID：{}，佣金金额：{}", deviceOwnerUserId, commissionAmount);
                }
            }
            
            // 场地方佣金
            if (commissionConfig.getLocationProviderRate() != null && commissionConfig.getLocationProviderRate().compareTo(BigDecimal.ZERO) > 0) {
                String locationProviderUserId = findRoleOwnerId("LOCATION_PROVIDER", deviceId, storeId, wineId);
                if (StrUtil.isNotBlank(locationProviderUserId)) {
                    BigDecimal commissionAmount = calculateCommissionAmount(orderAmount, commissionConfig.getLocationProviderRate());
                    WineCommissionRecord record = createCommissionRecord(orderId, orderNo, orderAmount, deviceId, wineId, 
                            locationProviderUserId, "LOCATION_PROVIDER", commissionConfig.getLocationProviderRate(), commissionAmount);
                    commissionRecords.add(record);
                    log.info("创建场地方佣金记录，用户ID：{}，佣金金额：{}", locationProviderUserId, commissionAmount);
                }
            }
            
            // 门店管理员佣金
            if (commissionConfig.getStoreManagerRate() != null && commissionConfig.getStoreManagerRate().compareTo(BigDecimal.ZERO) > 0) {
                String storeManagerUserId = findRoleOwnerId("STORE_MANAGER", deviceId, storeId, wineId);
                if (StrUtil.isNotBlank(storeManagerUserId)) {
                    BigDecimal commissionAmount = calculateCommissionAmount(orderAmount, commissionConfig.getStoreManagerRate());
                    WineCommissionRecord record = createCommissionRecord(orderId, orderNo, orderAmount, deviceId, wineId, 
                            storeManagerUserId, "STORE_MANAGER", commissionConfig.getStoreManagerRate(), commissionAmount);
                    commissionRecords.add(record);
                    log.info("创建门店管理员佣金记录，用户ID：{}，佣金金额：{}", storeManagerUserId, commissionAmount);
                }
            }
            
            // 供应商佣金
            if (commissionConfig.getSupplierRate() != null && commissionConfig.getSupplierRate().compareTo(BigDecimal.ZERO) > 0) {
                String supplierUserId = findRoleOwnerId("SUPPLIER", deviceId, storeId, wineId);
                if (StrUtil.isNotBlank(supplierUserId)) {
                    BigDecimal commissionAmount = calculateCommissionAmount(orderAmount, commissionConfig.getSupplierRate());
                    WineCommissionRecord record = createCommissionRecord(orderId, orderNo, orderAmount, deviceId, wineId, 
                            supplierUserId, "SUPPLIER", commissionConfig.getSupplierRate(), commissionAmount);
                    commissionRecords.add(record);
                    log.info("创建供应商佣金记录，用户ID：{}，佣金金额：{}", supplierUserId, commissionAmount);
                }
            }
            
            // 3. 批量保存佣金记录
            if (!commissionRecords.isEmpty()) {
                this.saveBatch(commissionRecords);
                
                // 4. 更新用户账户余额（暂时注释，待账户服务实现完成后启用）
                for (WineCommissionRecord record : commissionRecords) {
                    try {
                        // TODO: 待WineUserAccountService.addCommission方法实现后启用
                        // wineUserAccountService.addCommission(record.getUserId(), record.getCommissionAmount(), 
                        //         "ORDER_COMMISSION", "订单佣金：" + orderNo);
                        log.info("更新用户账户余额（暂未实现），用户ID：{}，佣金金额：{}", record.getUserId(), record.getCommissionAmount());
                    } catch (Exception e) {
                        log.error("更新用户账户余额失败，用户ID：{}，佣金金额：{}，错误：{}", 
                                record.getUserId(), record.getCommissionAmount(), e.getMessage(), e);
                        // 不抛出异常，避免影响整体流程，但需要记录日志
                    }
                }
            }
            
            log.info("订单佣金分配完成，订单ID：{}，成功分配{}条佣金记录", orderId, commissionRecords.size());
            return commissionRecords.size();
            
        } catch (Exception e) {
            log.error("订单佣金分配失败，订单ID：{}，错误：{}", orderId, e.getMessage(), e);
            throw new CommonException("佣金分配失败：" + e.getMessage());
        }
    }

    @Override
    public String findRoleOwnerId(String commissionType, String deviceId, String storeId, String wineId) {
        try {
            log.debug("查找角色归属用户ID，角色类型：{}，设备ID：{}，门店ID：{}，酒品ID：{}", 
                     commissionType, deviceId, storeId, wineId);
            
            // 参数校验
            if (StrUtil.isBlank(commissionType)) {
                log.warn("佣金类型不能为空");
                return null;
            }
            
            switch (commissionType) {
                case "PLATFORM":
                    // 平台方用户ID从系统参数获取，这里先用固定值，后续可配置化
                    String platformUserId = "PLATFORM_USER_001"; // TODO: 从系统参数表获取
                    log.debug("找到平台方用户ID：{}", platformUserId);
                    return platformUserId;
                    
                case "DEVICE_OWNER":
                    // 从设备表获取设备拥有者ID
                    if (StrUtil.isNotBlank(deviceId)) {
                        // TODO: 调用设备API获取ownerId
                        // String deviceOwnerUserId = wineDeviceApi.getDeviceOwner(deviceId);
                        // if (StrUtil.isNotBlank(deviceOwnerUserId)) {
                        //     log.debug("找到设备方用户ID：{}，设备ID：{}", deviceOwnerUserId, deviceId);
                        //     return deviceOwnerUserId;
                        // }
                        log.warn("设备方用户ID查找未实现，设备ID：{}", deviceId);
                        return null;
                    } else {
                        log.warn("查找设备方用户ID时，设备ID为空");
                        return null;
                    }
                    
                case "LOCATION_PROVIDER":
                    // 从设备表或门店表获取场地提供者ID
                    if (StrUtil.isNotBlank(deviceId)) {
                        // TODO: 调用设备API获取locationProviderId
                        // String locationProviderUserId = wineDeviceApi.getLocationProvider(deviceId);
                        // if (StrUtil.isNotBlank(locationProviderUserId)) {
                        //     log.debug("找到场地方用户ID：{}，设备ID：{}", locationProviderUserId, deviceId);
                        //     return locationProviderUserId;
                        // }
                        log.warn("场地方用户ID查找未实现，设备ID：{}", deviceId);
                        return null;
                    } else {
                        log.warn("查找场地方用户ID时，设备ID为空");
                        return null;
                    }
                    
                case "STORE_MANAGER":
                    // 从门店表获取门店管理员ID
                    if (StrUtil.isNotBlank(storeId)) {
                        // TODO: 调用门店API获取managerId
                        // String storeManagerUserId = wineStoreApi.getStoreManager(storeId);
                        // if (StrUtil.isNotBlank(storeManagerUserId)) {
                        //     log.debug("找到门店管理员用户ID：{}，门店ID：{}", storeManagerUserId, storeId);
                        //     return storeManagerUserId;
                        // }
                        log.warn("门店管理员用户ID查找未实现，门店ID：{}", storeId);
                        return null;
                    } else {
                        log.warn("查找门店管理员用户ID时，门店ID为空");
                        return null;
                    }
                    
                case "SUPPLIER":
                    // 从酒品表获取供应商ID
                    if (StrUtil.isNotBlank(wineId)) {
                        // TODO: 调用酒品API获取supplierId
                        // String supplierUserId = wineProductApi.getSupplier(wineId);
                        // if (StrUtil.isNotBlank(supplierUserId)) {
                        //     log.debug("找到供应商用户ID：{}，酒品ID：{}", supplierUserId, wineId);
                        //     return supplierUserId;
                        // }
                        log.warn("供应商用户ID查找未实现，酒品ID：{}", wineId);
                        return null;
                    } else {
                        log.warn("查找供应商用户ID时，酒品ID为空");
                        return null;
                    }
                    
                default:
                    log.warn("未知的佣金类型：{}", commissionType);
                    return null;
            }
            
        } catch (Exception e) {
            log.error("查找角色归属用户ID失败，角色类型：{}，设备ID：{}，门店ID：{}，酒品ID：{}，错误：{}", 
                     commissionType, deviceId, storeId, wineId, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public BigDecimal calculateCommissionAmount(BigDecimal orderAmount, BigDecimal commissionRate) {
        try {
            // 参数校验
            if (orderAmount == null) {
                log.warn("计算佣金时订单金额为空");
                return BigDecimal.ZERO;
            }
            if (commissionRate == null) {
                log.warn("计算佣金时佣金比例为空");
                return BigDecimal.ZERO;
            }
            if (orderAmount.compareTo(BigDecimal.ZERO) < 0) {
                log.warn("计算佣金时订单金额为负数：{}", orderAmount);
                return BigDecimal.ZERO;
            }
            if (commissionRate.compareTo(BigDecimal.ZERO) < 0 || commissionRate.compareTo(BigDecimal.ONE) > 0) {
                log.warn("计算佣金时佣金比例超出范围[0,1]：{}", commissionRate);
                return BigDecimal.ZERO;
            }
            
            // 佣金金额 = 订单金额 × 佣金比例，保留2位小数，四舍五入
            BigDecimal commissionAmount = orderAmount.multiply(commissionRate).setScale(2, RoundingMode.HALF_UP);
            log.debug("计算佣金成功，订单金额：{}，佣金比例：{}，佣金金额：{}", orderAmount, commissionRate, commissionAmount);
            return commissionAmount;
            
        } catch (Exception e) {
            log.error("计算佣金金额失败，订单金额：{}，佣金比例：{}，错误：{}", orderAmount, commissionRate, e.getMessage(), e);
            return BigDecimal.ZERO;
        }
    }

    /**
     * 创建佣金记录
     */
    private WineCommissionRecord createCommissionRecord(String orderId, String orderNo, BigDecimal orderAmount,
                                                       String deviceId, String wineId, String userId, 
                                                       String commissionType, BigDecimal commissionRate, 
                                                       BigDecimal commissionAmount) {
        try {
            WineCommissionRecord record = new WineCommissionRecord();
            // 设置基本信息
            record.setOrderId(orderId);
            record.setOrderNo(orderNo);
            record.setUserId(userId);
            record.setDeviceId(deviceId);
            record.setWineId(wineId);
            // 设置金额信息
            // record.setOrderAmount(orderAmount);  // 暂时注释，待实体类完善后启用
            record.setCommissionType(commissionType);
            // record.setCommissionRate(commissionRate);  // 暂时注释，待实体类完善后启用
            // record.setCommissionAmount(commissionAmount);  // 暂时注释，待实体类完善后启用
            // 设置状态和时间
            record.setStatus("CALCULATED"); // 初始状态为已计算
            record.setCalculateTime(new Date());
            
            log.debug("创建佣金记录成功，订单ID：{}，用户ID：{}，佣金类型：{}，佣金金额：{}", 
                     orderId, userId, commissionType, commissionAmount);
            return record;
            
        } catch (Exception e) {
            log.error("创建佣金记录失败，订单ID：{}，用户ID：{}，佣金类型：{}，错误：{}", 
                     orderId, userId, commissionType, e.getMessage(), e);
            throw new CommonException("创建佣金记录失败：" + e.getMessage());
        }
    }
} 