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
package vip.wqs.device.modular.info.provider;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vip.wqs.device.api.DeviceApi;
import vip.wqs.device.modular.info.entity.WineDevice;
import vip.wqs.device.modular.info.param.WineDevicePageParam;
import vip.wqs.device.modular.info.service.WineDeviceService;
import vip.wqs.device.pojo.DevicePojo;
import vip.wqs.device.pojo.DeviceQueryPojo;
import vip.wqs.device.pojo.DeviceSimplePojo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 设备API实现类
 *
 * @author AI Assistant
 * @date 2025/01/30
 */
@Service
@Slf4j
public class DeviceApiProvider implements DeviceApi {

    @Resource
    private WineDeviceService wineDeviceService;
    


    @Override
    public Page<DeviceSimplePojo> getDevicePage(DeviceQueryPojo param) {
        try {
            log.info("分页查询设备列表，参数：{}", param);
            
            // 1. 参数转换
            WineDevicePageParam pageParam = convertToPageParam(param);
            
            // 2. 查询分页数据
            Page<WineDevice> entityPage = wineDeviceService.page(pageParam);
            
            // 3. 转换为POJO
            Page<DeviceSimplePojo> result = new Page<>();
            result.setCurrent(entityPage.getCurrent());
            result.setSize(entityPage.getSize());
            result.setTotal(entityPage.getTotal());
            result.setPages(entityPage.getPages());
            
            List<DeviceSimplePojo> records = entityPage.getRecords().stream()
                    .map(this::convertToSimplePojo)
                    .collect(Collectors.toList());
            result.setRecords(records);
            
            log.info("分页查询设备列表成功，返回{}条数据", records.size());
            return result;
            
        } catch (Exception e) {
            log.error("分页查询设备列表异常", e);
            throw new RuntimeException("查询设备列表失败：" + e.getMessage());
        }
    }

    @Override
    public DevicePojo getDeviceDetail(String id) {
        try {
            log.info("获取设备详情，设备ID：{}", id);
            
            if (StrUtil.isBlank(id)) {
                throw new IllegalArgumentException("设备ID不能为空");
            }
            
            // 1. 查询设备实体
            WineDevice entity = wineDeviceService.queryEntity(id);
            if (entity == null) {
                throw new RuntimeException("设备不存在，ID：" + id);
            }
            
            // 2. 转换为POJO
            DevicePojo result = convertToPojo(entity);
            
            log.info("获取设备详情成功，设备名称：{}", result.getDeviceName());
            return result;
            
        } catch (Exception e) {
            log.error("获取设备详情异常，设备ID：{}", id, e);
            throw new RuntimeException("获取设备详情失败：" + e.getMessage());
        }
    }

    @Override
    public Page<DeviceSimplePojo> getUserDevicePage(String userId, DeviceQueryPojo param) {
        try {
            log.info("获取用户管理的设备列表，用户ID：{}，参数：{}", userId, param);
            
            if (StrUtil.isBlank(userId)) {
                throw new IllegalArgumentException("用户ID不能为空");
            }
            
            // 1. 设置用户ID筛选条件  
            if (param == null) {
                param = new DeviceQueryPojo();
            }
            param.setManagerUserId(userId);
            
            // 2. 调用分页查询
            return getDevicePage(param);
            
        } catch (Exception e) {
            log.error("获取用户管理的设备列表异常，用户ID：{}", userId, e);
            throw new RuntimeException("获取用户设备列表失败：" + e.getMessage());
        }
    }



    @Override
    public Boolean updateDeviceStatus(String deviceId, String status, String userId) {
        try {
            log.info("更新设备状态，设备ID：{}，状态：{}，操作用户：{}", deviceId, status, userId);
            
            if (StrUtil.isBlank(deviceId)) {
                throw new IllegalArgumentException("设备ID不能为空");
            }
            if (StrUtil.isBlank(status)) {
                throw new IllegalArgumentException("设备状态不能为空");
            }
            
            // 1. 查询设备
            WineDevice device = wineDeviceService.queryEntity(deviceId);
            if (device == null) {
                throw new RuntimeException("设备不存在，ID：" + deviceId);
            }
            
            // 2. TODO: 检查权限（需要实现用户权限验证）
            
            // 3. 更新设备状态
            device.setStatus(status);
            
            // 如果是上线状态，更新最后在线时间
            if ("ONLINE".equals(status)) {
                device.setLastOnlineTime(LocalDateTime.now());
            }
            
            boolean success = wineDeviceService.updateById(device);
            
            log.info("更新设备状态{}，设备ID：{}", success ? "成功" : "失败", deviceId);
            return success;
            
        } catch (Exception e) {
            log.error("更新设备状态异常，设备ID：{}", deviceId, e);
            throw new RuntimeException("更新设备状态失败：" + e.getMessage());
        }
    }

    @Override
    public Boolean bindProduct(String deviceId, String productId, String userId) {
        try {
            log.info("绑定酒品到设备，设备ID：{}，酒品ID：{}，操作用户：{}", deviceId, productId, userId);
            
            if (StrUtil.isBlank(deviceId)) {
                throw new IllegalArgumentException("设备ID不能为空");
            }
            if (StrUtil.isBlank(productId)) {
                throw new IllegalArgumentException("酒品ID不能为空");
            }
            
            // 1. 查询设备
            WineDevice device = wineDeviceService.queryEntity(deviceId);
            if (device == null) {
                throw new RuntimeException("设备不存在，ID：" + deviceId);
            }
            
            // 2. TODO: 检查权限（需要实现用户权限验证）
            
            // 3. 绑定酒品
            device.setCurrentProductId(productId);
            device.setBindTime(LocalDateTime.now());
            
            boolean success = wineDeviceService.updateById(device);
            
            log.info("绑定酒品到设备{}，设备ID：{}", success ? "成功" : "失败", deviceId);
            return success;
            
        } catch (Exception e) {
            log.error("绑定酒品到设备异常，设备ID：{}", deviceId, e);
            throw new RuntimeException("绑定酒品到设备失败：" + e.getMessage());
        }
    }

    @Override
    public Boolean unbindProduct(String deviceId, String userId) {
        try {
            log.info("解绑设备酒品，设备ID：{}，操作用户：{}", deviceId, userId);
            
            if (StrUtil.isBlank(deviceId)) {
                throw new IllegalArgumentException("设备ID不能为空");
            }
            
            // 1. 查询设备
            WineDevice device = wineDeviceService.queryEntity(deviceId);
            if (device == null) {
                throw new RuntimeException("设备不存在，ID：" + deviceId);
            }
            
            // 2. TODO: 检查权限（需要实现用户权限验证）
            
            // 3. 解绑酒品
            device.setCurrentProductId(null);
            device.setBindTime(null);
            
            boolean success = wineDeviceService.updateById(device);
            
            log.info("解绑设备酒品{}，设备ID：{}", success ? "成功" : "失败", deviceId);
            return success;
            
        } catch (Exception e) {
            log.error("解绑设备酒品异常，设备ID：{}", deviceId, e);
            throw new RuntimeException("解绑设备酒品失败：" + e.getMessage());
        }
    }

    @Override
    public Boolean controlDevice(String deviceId, String command, String userId) {
        try {
            log.info("控制设备，设备ID：{}，命令：{}，操作用户：{}", deviceId, command, userId);
            
            if (StrUtil.isBlank(deviceId)) {
                throw new IllegalArgumentException("设备ID不能为空");
            }
            if (StrUtil.isBlank(command)) {
                throw new IllegalArgumentException("控制命令不能为空");
            }
            
            // 1. 查询设备
            WineDevice device = wineDeviceService.queryEntity(deviceId);
            if (device == null) {
                throw new RuntimeException("设备不存在，ID：" + deviceId);
            }
            
            // 2. TODO: 检查权限（需要实现用户权限验证）
            
            // 3. 检查设备状态
            if (!"ONLINE".equals(device.getStatus())) {
                throw new RuntimeException("设备不在线，无法执行控制命令");
            }
            
            // TODO: 这里应该调用实际的设备控制接口
            // 目前只是模拟操作成功
            log.info("设备控制命令已发送，设备ID：{}，命令：{}", deviceId, command);
            
            return true;
            
        } catch (Exception e) {
            log.error("控制设备异常，设备ID：{}", deviceId, e);
            throw new RuntimeException("控制设备失败：" + e.getMessage());
        }
    }

    @Override
    public Boolean isDeviceOnline(String deviceCode) {
        try {
            if (StrUtil.isBlank(deviceCode)) {
                return false;
            }
            
            WineDevicePageParam param = new WineDevicePageParam();
            param.setDeviceCode(deviceCode);
            
            List<WineDevice> devices = wineDeviceService.list(param);
            if (devices.isEmpty()) {
                return false;
            }
            
            WineDevice device = devices.get(0);
            return "ONLINE".equals(device.getStatus());
            
        } catch (Exception e) {
            log.error("检查设备在线状态异常，设备编码：{}", deviceCode, e);
            return false;
        }
    }

    @Override
    public Boolean isDeviceAvailable(String deviceCode) {
        try {
            if (StrUtil.isBlank(deviceCode)) {
                return false;
            }
            
            WineDevicePageParam param = new WineDevicePageParam();
            param.setDeviceCode(deviceCode);
            
            List<WineDevice> devices = wineDeviceService.list(param);
            if (devices.isEmpty()) {
                return false;
            }
            
            WineDevice device = devices.get(0);
            return "ONLINE".equals(device.getStatus()) && 
                   StrUtil.isNotBlank(device.getCurrentProductId());
            
        } catch (Exception e) {
            log.error("检查设备可用状态异常，设备编码：{}", deviceCode, e);
            return false;
        }
    }

    @Override
    public List<DeviceSimplePojo> getDevicesByStoreId(String storeId) {
        try {
            log.info("根据门店ID获取设备列表，门店ID：{}", storeId);
            
            if (StrUtil.isBlank(storeId)) {
                throw new IllegalArgumentException("门店ID不能为空");
            }
            
            WineDevicePageParam param = new WineDevicePageParam();
            param.setStoreId(storeId);
            param.setSortField("sortCode");
            param.setSortOrder("ASCEND");
            
            List<WineDevice> devices = wineDeviceService.list(param);
            
            List<DeviceSimplePojo> result = devices.stream()
                    .map(this::convertToSimplePojo)
                    .collect(Collectors.toList());
            
            log.info("根据门店ID获取设备列表成功，返回{}条数据", result.size());
            return result;
            
        } catch (Exception e) {
            log.error("根据门店ID获取设备列表异常，门店ID：{}", storeId, e);
            throw new RuntimeException("获取门店设备列表失败：" + e.getMessage());
        }
    }

    @Override
    public Boolean recordHeartbeat(String deviceCode, String heartbeatData) {
        try {
            if (StrUtil.isBlank(deviceCode)) {
                return false;
            }
            
            WineDevicePageParam param = new WineDevicePageParam();
            param.setDeviceCode(deviceCode);
            
            List<WineDevice> devices = wineDeviceService.list(param);
            if (devices.isEmpty()) {
                return false;
            }
            
            WineDevice device = devices.get(0);
            device.setLastOnlineTime(LocalDateTime.now());
            device.setStatus("ONLINE");
            return wineDeviceService.updateById(device);
            
        } catch (Exception e) {
            log.error("记录设备心跳异常，设备编码：{}", deviceCode, e);
            return false;
        }
    }

    /**
     * 查询参数转换为分页参数
     */
    private WineDevicePageParam convertToPageParam(DeviceQueryPojo param) {
        WineDevicePageParam pageParam = new WineDevicePageParam();
        BeanUtil.copyProperties(param, pageParam);
        
        // 处理分页参数
        if (param.getPageNum() != null) {
            pageParam.setCurrent(param.getPageNum());
        }
        if (param.getPageSize() != null) {
            pageParam.setSize(param.getPageSize());
        }
        
        // 处理搜索关键词
        if (StrUtil.isNotBlank(param.getKeyword())) {
            pageParam.setSearchKey(param.getKeyword());
        }
        if (StrUtil.isNotBlank(param.getSearchKey())) {
            pageParam.setSearchKey(param.getSearchKey());
        }
        
        // 处理排序
        if (StrUtil.isNotBlank(param.getSortField())) {
            String sortOrder = StrUtil.isNotBlank(param.getSortOrder()) ? 
                param.getSortOrder() : "asc";
            pageParam.setSortField(param.getSortField());
            pageParam.setSortOrder("desc".equalsIgnoreCase(sortOrder) ? "DESCEND" : "ASCEND");
        }
        
        return pageParam;
    }
    
    /**
     * 实体转换为简化POJO
     */
    private DeviceSimplePojo convertToSimplePojo(WineDevice entity) {
        DeviceSimplePojo simplePojo = new DeviceSimplePojo();
        BeanUtil.copyProperties(entity, simplePojo);
        
        // 处理业务状态文本
        simplePojo.setStatusText(getStatusText(entity.getStatus()));
        
        // 处理连接状态文本
        simplePojo.setConnectionStatusText(getConnectionStatusText(entity.getConnectionStatus()));
        
        // 处理时间格式化
        if (entity.getLastOnlineTime() != null) {
            simplePojo.setLastOnlineTimeText(formatDateTime(entity.getLastOnlineTime()));
        }
        if (entity.getLastCheckTime() != null) {
            simplePojo.setLastCheckTimeText(formatDateTime(entity.getLastCheckTime()));
        }
        
        // TODO: 后续实现关联API后，可以在这里获取关联数据
        // 如门店名称、酒品名称、管理员名称等
        
        return simplePojo;
    }
    
    /**
     * 实体转换为完整POJO
     */
    private DevicePojo convertToPojo(WineDevice entity) {
        DevicePojo pojo = new DevicePojo();
        BeanUtil.copyProperties(entity, pojo);
        
        // 处理业务状态文本
        pojo.setStatusText(getStatusText(entity.getStatus()));
        
        // 处理连接状态文本
        pojo.setConnectionStatusText(getConnectionStatusText(entity.getConnectionStatus()));
        
        // 处理时间格式化
        if (entity.getBindTime() != null) {
            pojo.setBindTimeText(formatDateTime(entity.getBindTime()));
        }
        if (entity.getLastOnlineTime() != null) {
            pojo.setLastOnlineTimeText(formatDateTime(entity.getLastOnlineTime()));
        }
        if (entity.getLastCheckTime() != null) {
            pojo.setLastCheckTimeText(formatDateTime(entity.getLastCheckTime()));
        }
        if (entity.getCreateTime() != null) {
            pojo.setCreateTimeText(formatDateTime(entity.getCreateTime()));
        }
        if (entity.getUpdateTime() != null) {
            pojo.setUpdateTimeText(formatDateTime(entity.getUpdateTime()));
        }
        
        // TODO: 后续实现关联API后，可以在这里获取关联数据
        // 如门店详情、酒品详情、管理员详情等
        
        return pojo;
    }
    
    /**
     * 格式化日期时间
     */
    private String formatDateTime(Object dateTime) {
        if (dateTime == null) {
            return null;
        }
        
        if (dateTime instanceof LocalDateTime) {
            return ((LocalDateTime) dateTime).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } else if (dateTime instanceof java.util.Date) {
            return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dateTime);
        }
        
        return dateTime.toString();
    }
    
    /**
     * 获取业务状态文本
     */
    private String getStatusText(String status) {
        if (StrUtil.isBlank(status)) {
            return "未知";
        }
        
        switch (status) {
            case "ONLINE":
                return "正常营业";
            case "OFFLINE":
                return "暂停营业";
            case "MAINTENANCE":
                return "维护中";
            default:
                return status;
        }
    }
    
    /**
     * 获取连接状态文本
     */
    private String getConnectionStatusText(String connectionStatus) {
        if (StrUtil.isBlank(connectionStatus)) {
            return "未知";
        }
        
        switch (connectionStatus) {
            case "ONLINE":
                return "蓝牙在线";
            case "OFFLINE":
                return "蓝牙离线";
            case "UNKNOWN":
                return "未检测";
            default:
                return connectionStatus;
        }
    }
    
    /**
     * 更新设备蓝牙检测状态
     * @param deviceId 设备ID
     * @param connectionStatus 连接状态
     * @param userId 检测用户ID
     * @param checkResult 检测结果描述
     * @return 更新结果
     */
    public Boolean updateDeviceConnectionStatus(String deviceId, String connectionStatus, String userId, String checkResult) {
        try {
            log.info("更新设备蓝牙连接状态，设备ID：{}，连接状态：{}，检测用户：{}", deviceId, connectionStatus, userId);
            
            if (StrUtil.isBlank(deviceId)) {
                throw new IllegalArgumentException("设备ID不能为空");
            }
            if (StrUtil.isBlank(connectionStatus)) {
                throw new IllegalArgumentException("连接状态不能为空");
            }
            
            // 1. 查询设备
            WineDevice device = wineDeviceService.queryEntity(deviceId);
            if (device == null) {
                throw new RuntimeException("设备不存在，ID：" + deviceId);
            }
            
            // 2. 更新连接状态
            device.setConnectionStatus(connectionStatus);
            device.setLastCheckTime(LocalDateTime.now());
            device.setCheckUserId(userId);
            device.setCheckResult(checkResult);
            
            // 3. 如果检测为在线，更新最后在线时间
            if ("ONLINE".equals(connectionStatus)) {
                device.setLastOnlineTime(LocalDateTime.now());
            }
            
            boolean success = wineDeviceService.updateById(device);
            
            log.info("更新设备蓝牙连接状态{}，设备ID：{}", success ? "成功" : "失败", deviceId);
            return success;
            
        } catch (Exception e) {
            log.error("更新设备蓝牙连接状态异常，设备ID：{}", deviceId, e);
            throw new RuntimeException("更新设备连接状态失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取设备连接状态检测历史
     * @param deviceId 设备ID
     * @param limit 限制条数
     * @return 检测历史
     */
    public String getDeviceCheckHistory(String deviceId, Integer limit) {
        try {
            WineDevice device = wineDeviceService.queryEntity(deviceId);
            if (device == null) {
                return "设备不存在";
            }
            
            if (device.getLastCheckTime() == null) {
                return "未检测";
            }
            
            // 计算时间差
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime checkTime = device.getLastCheckTime();
            long minutes = java.time.Duration.between(checkTime, now).toMinutes();
            
            String timeText;
            if (minutes < 1) {
                timeText = "刚刚";
            } else if (minutes < 60) {
                timeText = minutes + "分钟前";
            } else if (minutes < 1440) { // 24小时
                timeText = (minutes / 60) + "小时前";
            } else {
                timeText = (minutes / 1440) + "天前";
            }
            
            String statusText = getConnectionStatusText(device.getConnectionStatus());
            return timeText + "检测为" + statusText;
            
        } catch (Exception e) {
            log.error("获取设备检测历史异常，设备ID：{}", deviceId, e);
            return "获取检测历史失败";
        }
    }
}