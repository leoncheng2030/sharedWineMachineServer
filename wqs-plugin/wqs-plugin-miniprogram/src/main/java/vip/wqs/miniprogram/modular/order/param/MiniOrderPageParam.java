/*
 * Copyright [2025] [https://www.wqs.vip]
 *
 * WQS采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改WQS源码头部的版权声明。
 * 3.本项目代码可免费商业使用，商业使用请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://www.wqs.vip
 * 5.不可二次分发开源参与同类竞品，如有想法可联系团队wqs@wqs.vip商议合作。
 * 6.若您的项目无法满足以上几点，需要更多功能代码，获取WQS商业授权许可，请在官网购买授权，地址为 https://www.wqs.vip
 */
package vip.wqs.miniprogram.modular.order.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

/**
 * 小程序订单分页查询参数
 *
 * @author wqs
 * @date 2025-01-30
 */
@Getter
@Setter
@Schema(description = "小程序订单分页查询参数")
public class MiniOrderPageParam {

    /** 页码 */
    @Schema(description = "页码", example = "1")
    @Min(value = 1, message = "页码不能小于1")
    private Long pageNum = 1L;

    /** 页大小 */
    @Schema(description = "页大小", example = "10")
    @Min(value = 1, message = "页大小不能小于1")
    @Max(value = 100, message = "页大小不能大于100")
    private Long pageSize = 10L;

    /** 订单状态筛选 */
    @Schema(description = "订单状态：PENDING_PAYMENT-待支付，PAID-已支付，DISPENSING-出酒中，COMPLETED-已完成，CANCELLED-已取消，REFUNDED-已退款")
    private String status;

    /** 开始时间 */
    @Schema(description = "开始时间，格式：yyyy-MM-dd HH:mm:ss")
    private String startTime;

    /** 结束时间 */
    @Schema(description = "结束时间，格式：yyyy-MM-dd HH:mm:ss")
    private String endTime;

    /** 关键词搜索 */
    @Schema(description = "关键词搜索（订单号、酒品名称）")
    private String keyword;

    /** 设备ID筛选 */
    @Schema(description = "设备ID筛选")
    private String deviceId;

    /** 酒品ID筛选 */
    @Schema(description = "酒品ID筛选")
    private String wineId;

    /** 排序字段 */
    @Schema(description = "排序字段", allowableValues = {"createTime", "payTime", "totalAmount"})
    private String sortField = "createTime";

    /** 排序方向 */
    @Schema(description = "排序方向", allowableValues = {"ASC", "DESC"})
    private String sortOrder = "DESC";
} 