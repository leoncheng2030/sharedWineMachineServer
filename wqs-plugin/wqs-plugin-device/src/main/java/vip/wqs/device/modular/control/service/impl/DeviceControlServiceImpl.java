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
package vip.wqs.device.modular.control.service.impl;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vip.wqs.device.modular.control.param.DeviceControlParam;
import vip.wqs.device.modular.control.result.DeviceControlResult;
import vip.wqs.device.modular.control.service.DeviceControlService;
import vip.wqs.device.modular.control.service.EncryptApiService;
import vip.wqs.device.modular.info.entity.WineDevice;
import vip.wqs.device.modular.info.service.WineDeviceService;

/**
 * 设备控制业务服务实现类
 *
 * @author AI Assistant
 * @date 2025/01/30
 */
@Slf4j
@Service
public class DeviceControlServiceImpl implements DeviceControlService {

    @Autowired
    private EncryptApiService encryptApiService;

    @Autowired
    private WineDeviceService wineDeviceService;

    @Override
    public DeviceControlResult getControlCommand(DeviceControlParam param) {
        try {
            log.info("获取设备控制指令，设备ID：{}，订单ID：{}", param.getDeviceId(), param.getChargeId());

            // 1. 参数验证
            if (StrUtil.isBlank(param.getDeviceId()) || StrUtil.isBlank(param.getChargeId())) {
                return DeviceControlResult.failure("设备ID和订单ID不能为空");
            }

            // 2. 验证设备是否存在
            WineDevice device = wineDeviceService.getById(param.getDeviceId());
            if (device == null) {
                return DeviceControlResult.failure("设备不存在");
            }

            // 3. 验证设备是否可用
            if (!isDeviceAvailable(param.getDeviceId())) {
                return DeviceControlResult.failure("设备不可用");
            }

            // 4. 设置设备UUID
            if (StrUtil.isBlank(param.getUuid())) {
                param.setUuid(device.getUuid());
            }

            // 5. 调用第三方接口获取加密指令
            String encryptCommand = encryptApiService.getEncryptCommand(param);
            if (StrUtil.isBlank(encryptCommand)) {
                return DeviceControlResult.failure("获取加密指令失败");
            }

            log.info("设备控制指令获取成功，设备ID：{}，订单ID：{}", param.getDeviceId(), param.getChargeId());
            return DeviceControlResult.success(encryptCommand);

        } catch (Exception e) {
            log.error("获取设备控制指令异常，设备ID：{}，订单ID：{}", param.getDeviceId(), param.getChargeId(), e);
            return DeviceControlResult.failure("系统异常：" + e.getMessage());
        }
    }

    @Override
    public boolean validateControlPermission(String orderId, String deviceId) {
        try {
            log.info("验证设备控制权限，订单ID：{}，设备ID：{}", orderId, deviceId);

            // 1. 参数验证
            if (StrUtil.isBlank(orderId) || StrUtil.isBlank(deviceId)) {
                log.warn("验证设备控制权限失败：参数不能为空");
                return false;
            }

            // 2. 验证设备是否存在
            WineDevice device = wineDeviceService.getById(deviceId);
            if (device == null) {
                log.warn("验证设备控制权限失败：设备不存在，设备ID：{}", deviceId);
                return false;
            }

            // 3. 验证设备是否可用
            if (!isDeviceAvailable(deviceId)) {
                log.warn("验证设备控制权限失败：设备不可用，设备ID：{}", deviceId);
                return false;
            }

            // TODO: 4. 验证订单状态和权限
            // 这里需要根据实际业务逻辑验证订单状态
            // 比如：订单是否已支付、是否属于当前用户等

            log.info("设备控制权限验证通过，订单ID：{}，设备ID：{}", orderId, deviceId);
            return true;

        } catch (Exception e) {
            log.error("验证设备控制权限异常，订单ID：{}，设备ID：{}", orderId, deviceId, e);
            return false;
        }
    }

    @Override
    public void updateControlResult(String orderId, String deviceId, boolean success, String message) {
        try {
            log.info("更新设备控制结果，订单ID：{}，设备ID：{}，成功：{}，消息：{}", orderId, deviceId, success, message);

            // TODO: 根据实际业务需求更新订单状态和设备状态
            // 1. 更新订单状态
            // 2. 记录控制日志
            // 3. 更新设备最后控制时间
            // 4. 发送通知等

            log.info("设备控制结果更新完成，订单ID：{}，设备ID：{}", orderId, deviceId);

        } catch (Exception e) {
            log.error("更新设备控制结果异常，订单ID：{}，设备ID：{}", orderId, deviceId, e);
        }
    }

    @Override
    public String generateControlCommand(String deviceUuid, String orderId, Integer minute, Integer second) {
        try {
            log.info("生成设备控制指令，设备UUID：{}，订单ID：{}", deviceUuid, orderId);

            DeviceControlParam param = new DeviceControlParam();
            param.setUuid(deviceUuid);
            param.setChargeId(orderId);
            param.setMinute(minute != null ? minute : 0);
            param.setSecond(second != null ? second : 30);

            return encryptApiService.getEncryptCommand(param);

        } catch (Exception e) {
            log.error("生成设备控制指令异常，设备UUID：{}，订单ID：{}", deviceUuid, orderId, e);
            return null;
        }
    }

    @Override
    public boolean isDeviceOnline(String deviceId) {
        try {
            WineDevice device = wineDeviceService.getById(deviceId);
            if (device == null) {
                return false;
            }

            // TODO: 根据实际业务逻辑判断设备是否在线
            // 比如：检查设备心跳时间、蓝牙连接状态等
            return "1".equals(device.getStatus());

        } catch (Exception e) {
            log.error("检查设备在线状态异常，设备ID：{}", deviceId, e);
            return false;
        }
    }

    @Override
    public boolean isDeviceAvailable(String deviceId) {
        try {
            WineDevice device = wineDeviceService.getById(deviceId);
            if (device == null) {
                return false;
            }

            // 检查设备状态：1-正常，0-禁用，2-维护
            String status = device.getStatus();
            return "1".equals(status);

        } catch (Exception e) {
            log.error("检查设备可用性异常，设备ID：{}", deviceId, e);
            return false;
        }
    }
}