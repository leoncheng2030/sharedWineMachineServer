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
package vip.wqs.device.modular.info.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 设备编辑参数
 *
 * @author wqs
 * @date 2025/1/27
 **/
@Getter
@Setter
public class WineDeviceEditParam {

    /** 主键 */
    @Schema(description = "主键")
    @NotBlank(message = "主键不能为空")
    private String id;

    /** 设备编码 */
    @Schema(description = "设备编码")
    @NotBlank(message = "设备编码不能为空")
    private String deviceCode;

    /** 设备名称 */
    @Schema(description = "设备名称")
    @NotBlank(message = "设备名称不能为空")
    private String deviceName;

    /** 所属门店ID */
    @Schema(description = "所属门店ID")
    @NotBlank(message = "所属门店ID不能为空")
    private String storeId;

    /** 当前绑定的酒品ID */
    @Schema(description = "当前绑定的酒品ID")
    private String currentProductId;

    /** 设备位置 */
    @Schema(description = "设备位置")
    private String location;

    /** 设备状态(ONLINE/OFFLINE/MAINTENANCE) */
    @Schema(description = "设备状态")
    private String status;

    /** 绑定时间 */
    @Schema(description = "绑定时间")
    private LocalDateTime bindTime;

    /** 最后在线时间 */
    @Schema(description = "最后在线时间")
    private LocalDateTime lastOnlineTime;

    /** 管理员用户ID */
    @Schema(description = "管理员用户ID")
    private String managerUserId;

    /** 设备二维码 */
    @Schema(description = "设备二维码")
    private String qrCode;

    /** 排序码 */
    @Schema(description = "排序码")
    @NotNull(message = "排序码不能为空")
    private Integer sortCode;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;

    /** 扩展信息 */
    @Schema(description = "扩展信息")
    private String extJson;
} 