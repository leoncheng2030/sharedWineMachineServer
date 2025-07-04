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
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 小程序设备蓝牙连接状态更新DTO
 *
 * @author AI Assistant
 * @date 2025/01/30
 */
@Data
@Schema(description = "小程序设备蓝牙连接状态更新DTO")
public class MiniDeviceConnectionStatusDto {

    /**
     * 用户ID（由系统自动设置，无需前端传递）
     */
    @Schema(description = "检测用户ID", hidden = true)
    private String userId;

    /**
     * 设备ID
     */
    @Schema(description = "设备ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "设备ID不能为空")
    private String deviceId;

    /**
     * 蓝牙连接状态（ONLINE-蓝牙在线，OFFLINE-蓝牙离线，UNKNOWN-未检测）
     */
    @Schema(description = "蓝牙连接状态(ONLINE:蓝牙在线,OFFLINE:蓝牙离线,UNKNOWN:未检测)", example = "ONLINE", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "蓝牙连接状态不能为空")
    @Pattern(regexp = "^(ONLINE|OFFLINE|UNKNOWN)$", message = "蓝牙连接状态必须是ONLINE、OFFLINE或UNKNOWN")
    private String connectionStatus;

    /**
     * 检测结果描述
     */
    @Schema(description = "检测结果描述", example = "蓝牙设备扫描成功，信号强度良好")
    private String checkResult;

    /**
     * 设备编码（可选，用于辅助定位设备）
     */
    @Schema(description = "设备编码")
    private String deviceCode;
} 