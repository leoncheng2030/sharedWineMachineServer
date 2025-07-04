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
package vip.wqs.device.modular.control.service;

import vip.wqs.device.modular.control.param.DeviceControlParam;
import vip.wqs.device.modular.control.result.DeviceControlResult;

/**
 * 设备控制业务服务接口
 *
 * @author AI Assistant
 * @date 2025/01/30
 */
public interface DeviceControlService {

    /**
     * 获取设备控制指令
     *
     * @param param 控制参数
     * @return 控制结果，包含加密指令
     */
    DeviceControlResult getControlCommand(DeviceControlParam param);

    /**
     * 验证设备控制权限
     *
     * @param orderId  订单ID
     * @param deviceId 设备ID
     * @return 是否有权限
     */
    boolean validateControlPermission(String orderId, String deviceId);

    /**
     * 更新设备控制执行结果
     *
     * @param orderId  订单ID
     * @param deviceId 设备ID
     * @param success  执行是否成功
     * @param message  执行结果消息
     */
    void updateControlResult(String orderId, String deviceId, boolean success, String message);

    /**
     * 生成设备控制指令
     *
     * @param deviceUuid 设备UUID
     * @param orderId    订单ID
     * @param minute     分钟数
     * @param second     秒数
     * @return 加密控制指令
     */
    String generateControlCommand(String deviceUuid, String orderId, Integer minute, Integer second);

    /**
     * 验证设备是否在线
     *
     * @param deviceId 设备ID
     * @return 是否在线
     */
    boolean isDeviceOnline(String deviceId);

    /**
     * 检查设备是否可用（在线且非维护状态）
     *
     * @param deviceId 设备ID
     * @return 是否可用
     */
    boolean isDeviceAvailable(String deviceId);
}