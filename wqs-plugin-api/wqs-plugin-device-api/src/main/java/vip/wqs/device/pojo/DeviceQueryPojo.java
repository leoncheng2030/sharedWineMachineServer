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
package vip.wqs.device.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 设备查询参数POJO
 * 
 * @author AI Assistant
 * @date 2025/01/30
 */
@Data
@Schema(description = "设备查询参数")
public class DeviceQueryPojo {

    /** 页码 */
    @Schema(description = "页码")
    private Integer pageNum = 1;

    /** 页大小 */
    @Schema(description = "页大小")
    private Integer pageSize = 10;

    /** 关键词搜索（设备名称、设备编码、位置） */
    @Schema(description = "关键词搜索")
    private String keyword;

    /** 搜索关键字（兼容现有接口） */
    @Schema(description = "搜索关键字")
    private String searchKey;

    /** 门店ID筛选 */
    @Schema(description = "门店ID筛选")
    private String storeId;

    /** 设备状态筛选 */
    @Schema(description = "设备状态筛选")
    private String status;

    /** 管理员用户ID筛选 */
    @Schema(description = "管理员用户ID筛选")
    private String managerUserId;

    /** 当前绑定酒品ID筛选 */
    @Schema(description = "当前绑定酒品ID筛选")
    private String currentProductId;

    /** 开始时间 */
    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    /** 结束时间 */
    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    /** 排序字段 */
    @Schema(description = "排序字段")
    private String sortField;

    /** 排序方向(asc/desc) */
    @Schema(description = "排序方向")
    private String sortOrder;

    /** 是否只查询在线设备 */
    @Schema(description = "是否只查询在线设备")
    private Boolean onlineOnly;

    /** 是否只查询有绑定酒品的设备 */
    @Schema(description = "是否只查询有绑定酒品的设备")
    private Boolean bindProductOnly;
} 