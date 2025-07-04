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
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 小程序设备分页查询DTO
 *
 * @author AI Assistant
 * @date 2025/01/30
 */
@Data
@Schema(description = "小程序设备分页查询DTO")
public class MiniDevicePageDto {

    /**
     * 当前页码
     */
    @Schema(description = "当前页码", example = "1")
    @NotNull(message = "页码不能为空")
    @Min(value = 1, message = "页码不能小于1")
    private Long pageNum = 1L;

    /**
     * 每页数量
     */
    @Schema(description = "每页数量", example = "10")
    @NotNull(message = "每页数量不能为空")
    @Min(value = 1, message = "每页数量不能小于1")
    @Max(value = 100, message = "每页数量不能超过100")
    private Long pageSize = 10L;

    /**
     * 用户ID（由系统自动设置，无需前端传递）
     */
    @Schema(description = "用户ID", hidden = true)
    private String userId;

    /**
     * 搜索关键词（设备名称、设备编码、位置）
     */
    @Schema(description = "搜索关键词")
    private String keyword;

    /**
     * 设备编码
     */
    @Schema(description = "设备编码")
    private String deviceCode;

    /**
     * 设备名称
     */
    @Schema(description = "设备名称")
    private String deviceName;

    /**
     * 设备位置
     */
    @Schema(description = "设备位置")
    private String location;

    /**
     * 设备状态（ONLINE-在线，OFFLINE-离线，MAINTENANCE-维护中）
     */
    @Schema(description = "设备状态(ONLINE:在线,OFFLINE:离线,MAINTENANCE:维护中)", example = "ONLINE")
    private String status;

    /**
     * 门店ID
     */
    @Schema(description = "门店ID")
    private String storeId;

    /**
     * 管理员用户ID
     */
    @Schema(description = "管理员用户ID")
    private String managerUserId;

    /**
     * 开始时间
     */
    @Schema(description = "开始时间")
    private String startTime;

    /**
     * 结束时间
     */
    @Schema(description = "结束时间")
    private String endTime;

    /**
     * 排序字段
     */
    @Schema(description = "排序字段(createTime:创建时间,updateTime:更新时间,deviceName:设备名称)", example = "createTime")
    private String sortField = "createTime";

    /**
     * 排序方式（ASC-升序，DESC-降序）
     */
    @Schema(description = "排序方式(ASC:升序,DESC:降序)", example = "DESC")
    private String sortOrder = "DESC";
} 