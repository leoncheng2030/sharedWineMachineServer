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
package vip.wqs.device.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 设备加密指令结果DTO
 * 用于返回设备控制的加密指令
 *
 * @author AI Assistant
 * @date 2025/01/30
 */
@Data
@Schema(description = "设备加密指令结果DTO")
public class DeviceControlResultDto {

    /**
     * 是否成功
     */
    @Schema(description = "是否成功", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean success;

    /**
     * 结果消息
     */
    @Schema(description = "结果消息")
    private String message;

    /**
     * 加密指令
     */
    @Schema(description = "加密指令")
    private String cmd;

    /**
     * 设备ID
     */
    @Schema(description = "设备ID")
    private String deviceId;

    /**
     * 订单ID
     */
    @Schema(description = "订单ID")
    private String orderId;

    // ==================== 静态构造方法 ====================
    
    /**
     * 创建成功结果
     */
    public static DeviceControlResultDto success(String cmd, String message) {
        DeviceControlResultDto result = new DeviceControlResultDto();
        result.setSuccess(true);
        result.setCmd(cmd);
        result.setMessage(message);
        return result;
    }

    /**
     * 创建失败结果
     */
    public static DeviceControlResultDto failure(String message) {
        DeviceControlResultDto result = new DeviceControlResultDto();
        result.setSuccess(false);
        result.setMessage(message);
        return result;
    }
} 