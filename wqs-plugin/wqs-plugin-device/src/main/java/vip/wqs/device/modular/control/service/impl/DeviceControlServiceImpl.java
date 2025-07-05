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
import jakarta.annotation.Resource;
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
 * 仅负责获取设备控制的加密指令，不包含实际控制功能
 *
 * @author AI Assistant
 * @date 2025/01/30
 */
@Slf4j
@Service
public class DeviceControlServiceImpl implements DeviceControlService {

    @Resource
    private EncryptApiService encryptApiService;

    @Resource
    private WineDeviceService wineDeviceService;

    @Override
    public DeviceControlResult getControlCommand(DeviceControlParam param) {
        try {
            log.info("获取设备控制加密指令，设备ID：{}，订单ID：{}", param.getDeviceCode(), param.getChargeId());
            // 1. 获取设备信息
            WineDevice device = wineDeviceService.getDeviceByDeviceCode(String.valueOf(param.getDeviceCode()));
            if (device == null) {
                return DeviceControlResult.failure("设备不存在");
            }
            param.setUuid(device.getUuid());
            // 4. 调用第三方接口获取加密指令
            String encryptCommand = encryptApiService.getEncryptCommand(param);
            if (StrUtil.isBlank(encryptCommand)) {
                return DeviceControlResult.failure("获取加密指令失败");
            }

            log.info("设备控制加密指令获取成功，设备ID：{}，订单ID：{}", param.getDeviceCode(), param.getChargeId());
            return DeviceControlResult.success(encryptCommand);

        } catch (Exception e) {
            log.error("获取设备控制加密指令异常，设备ID：{}，订单ID：{}", param.getDeviceCode(), param.getChargeId(), e);
            return DeviceControlResult.failure("系统异常：" + e.getMessage());
        }
    }
}