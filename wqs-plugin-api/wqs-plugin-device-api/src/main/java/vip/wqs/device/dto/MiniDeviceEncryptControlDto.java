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
 * 小程序蓝牙设备控制DTO
 * 用于获取设备控制加密指令
 *
 * @author AI Assistant
 * @date 2025/01/30
 */
@Data
@Schema(description = "小程序蓝牙设备控制DTO")
public class MiniDeviceEncryptControlDto {

    /**
     * 设备UUID
     */
    @Schema(description = "设备UUID")
    private String uuid;

    /**
     * 订单编号
     */
    @Schema(description = "订单编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "订单编号不能为空")
    private String chargeId;

    /**
     * 设备ID
     */
    @Schema(description = "设备ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "设备ID不能为空")
    private String deviceId;

    /**
     * 用户ID（由系统自动设置，无需前端传递）
     */
    @Schema(description = "用户ID", hidden = true)
    private String userId;

    /**
     * 通电分钟数
     */
    @Schema(description = "通电分钟数")
    private Integer minute;

    /**
     * 通电秒数
     */
    @Schema(description = "通电秒数")
    private Integer second;

    /**
     * 最大功率限制
     */
    @Schema(description = "最大功率限制")
    private Integer maxPower;

    /**
     * 最小功率限制
     */
    @Schema(description = "最小功率限制")
    private Integer minPower;

    /**
     * 拔插断电检测时间
     */
    @Schema(description = "拔插断电检测时间")
    private Integer blackoutTime;

    /**
     * 延迟断电检测时间
     */
    @Schema(description = "延迟断电检测时间")
    private Integer delayTime;

    /**
     * 授权电量/投币数/脉冲数
     */
    @Schema(description = "授权电量/投币数/脉冲数")
    private Integer quantity;

    /**
     * 扩展指令标识
     */
    @Schema(description = "扩展指令标识")
    private Integer extMode;

    /**
     * 浮充终止判断时间
     */
    @Schema(description = "浮充终止判断时间")
    private Integer extTime;

    /**
     * 当前时间戳
     */
    @Schema(description = "当前时间戳")
    private Long nowTime;

    /**
     * 订单起始时间戳
     */
    @Schema(description = "订单起始时间戳")
    private Long startTime;

    /**
     * 订单启动窗口
     */
    @Schema(description = "订单启动窗口")
    private Integer validSecond;

    /**
     * 覆盖标识
     */
    @Schema(description = "覆盖标识")
    private Integer overlap;
} 