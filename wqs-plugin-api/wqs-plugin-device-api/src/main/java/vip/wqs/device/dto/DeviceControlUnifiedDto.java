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
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 设备加密指令获取DTO
 * 仅用于获取设备控制的加密指令，不包含实际控制功能
 *
 * @author AI Assistant
 * @date 2025/01/30
 */
@Data
@Schema(description = "设备加密指令获取DTO")
public class DeviceControlUnifiedDto {

    /**
     * 设备ID
     */
    @Schema(description = "设备ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "设备ID不能为空")
    private String deviceId;

    /**
     * 设备UUID（蓝牙设备标识）
     */
    @Schema(description = "设备UUID")
    private String uuid;

    /**
     * 用户ID（由系统自动设置）
     */
    @Schema(description = "用户ID", hidden = true)
    private String userId;

    /**
     * 订单编号
     */
    @Schema(description = "订单编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "订单编号不能为空")
    private String orderId;

    /**
     * 通电分钟数
     */
    @Schema(description = "通电分钟数", example = "0")
    private Integer minute = 0;

    /**
     * 通电秒数
     */
    @Schema(description = "通电秒数", example = "30")
    private Integer second = 30;
} 