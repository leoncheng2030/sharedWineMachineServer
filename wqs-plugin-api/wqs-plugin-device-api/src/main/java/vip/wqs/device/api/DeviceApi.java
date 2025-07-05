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
package vip.wqs.device.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import vip.wqs.device.pojo.DevicePojo;
import vip.wqs.device.pojo.DeviceQueryPojo;
import vip.wqs.device.pojo.DeviceSimplePojo;

import java.util.List;

/**
 * 设备管理API接口 - 小程序专用
 *
 * @author AI Assistant
 * @date 2025/01/30
 **/
public interface DeviceApi {

    /**
     * 分页查询设备列表 - 标准分页查询方法
     *
     * @param param 查询参数
     * @return 分页结果
     * @author AI Assistant
     * @date 2025/01/30
     */
    Page<DeviceSimplePojo> getDevicePage(DeviceQueryPojo param);

    /**
     * 获取设备详情 - 标准详情查询方法
     *
     * @param id 设备ID
     * @return 设备详情
     * @author AI Assistant
     * @date 2025/01/30
     */
    DevicePojo getDeviceDetail(String id);



    /**
     * 获取用户管理的设备列表
     *
     * @param userId 用户ID
     * @param param 查询参数
     * @return 分页结果
     * @author AI Assistant
     * @date 2025/01/30
     */
    Page<DeviceSimplePojo> getUserDevicePage(String userId, DeviceQueryPojo param);



    /**
     * 更新设备状态
     *
     * @param deviceId 设备ID
     * @param status 设备状态(ONLINE/OFFLINE/MAINTENANCE)
     * @param userId 操作用户ID
     * @return 是否成功
     * @author AI Assistant
     * @date 2025/01/30
     */
    Boolean updateDeviceStatus(String deviceId, String status, String userId);

    /**
     * 绑定酒品到设备
     *
     * @param deviceId 设备ID
     * @param productId 酒品ID
     * @param userId 操作用户ID
     * @return 是否成功
     * @author AI Assistant
     * @date 2025/01/30
     */
    Boolean bindProduct(String deviceId, String productId, String userId);

    /**
     * 解绑设备酒品
     *
     * @param deviceId 设备ID
     * @param userId 操作用户ID
     * @return 是否成功
     * @author AI Assistant
     * @date 2025/01/30
     */
    Boolean unbindProduct(String deviceId, String userId);

    /**
     * 控制设备（开机/关机/重启等）
     *
     * @param deviceId 设备ID
     * @param command 控制命令
     * @param userId 操作用户ID
     * @return 是否成功
     * @author AI Assistant
     * @date 2025/01/30
     */
    Boolean controlDevice(String deviceId, String command, String userId);

    /**
     * 检查设备是否在线
     *
     * @param deviceCode 设备编码
     * @return 是否在线
     * @author AI Assistant
     * @date 2025/01/30
     */
    Boolean isDeviceOnline(String deviceCode);

    /**
     * 检查设备是否可用
     *
     * @param deviceCode 设备编码
     * @return 是否可用
     * @author AI Assistant
     * @date 2025/01/30
     */
    Boolean isDeviceAvailable(String deviceCode);

    /**
     * 根据门店ID获取设备列表
     *
     * @param storeId 门店ID
     * @return 设备列表
     * @author AI Assistant
     * @date 2025/01/30
     */
    List<DeviceSimplePojo> getDevicesByStoreId(String storeId);

    /**
     * 记录设备心跳
     *
     * @param deviceCode 设备编码
     * @param heartbeatData 心跳数据
     * @return 是否成功
     * @author AI Assistant
     * @date 2025/01/30
     */
    Boolean recordHeartbeat(String deviceCode, String heartbeatData);

    /**
     * 更新设备蓝牙连接状态
     *
     * @param deviceId 设备ID
     * @param connectionStatus 连接状态(ONLINE/OFFLINE/UNKNOWN)
     * @param userId 检测用户ID
     * @param checkResult 检测结果描述
     * @return 是否成功
     * @author AI Assistant
     * @date 2025/01/30
     */
    Boolean updateDeviceConnectionStatus(String deviceId, String connectionStatus, String userId, String checkResult);

    /**
     * 获取设备控制加密指令
     * 小程序端调用此接口获取加密控制指令，然后通过蓝牙发送给设备
     *
     * @param deviceCode 设备ID
     * @param orderNo 订单ID
     * @param minute 通电分钟数（可选）
     * @param second 通电秒数（可选）
     * @return 控制指令结果
     * @author AI Assistant
     * @date 2025/01/30
     */
    String getDeviceControlCommand(String deviceCode, Integer orderNo, Integer minute, Integer second);

    /**
     * 更新设备控制执行结果
     * 小程序端执行设备控制后，调用此方法更新执行状态
     *
     * @param orderId 订单ID
     * @param deviceId 设备ID
     * @param success 是否执行成功
     * @param message 执行结果消息
     * @param userId 操作用户ID
     * @return 是否更新成功
     * @author AI Assistant
     * @date 2025/01/30
     */
    Boolean updateDeviceControlResult(String orderId, String deviceId, Boolean success, String message, String userId);
} 