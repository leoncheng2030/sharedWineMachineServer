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
package vip.wqs.order.modular.record.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 订单分页查询参数
 *
 * @author wqs
 * @date 2025/01/30
 */
@Getter
@Setter
public class WineOrderPageParam {

    /** 页码 */
    @Schema(description = "页码", example = "1")
    private Integer pageNum = 1;

    /** 页大小 */
    @Schema(description = "页大小", example = "10")
    private Integer pageSize = 10;

    /** 订单号 */
    @Schema(description = "订单号")
    private String orderNo;

    /** 用户ID */
    @Schema(description = "用户ID")
    private String userId;

    /** 设备ID */
    @Schema(description = "设备ID")
    private String deviceId;

    /** 酒品ID */
    @Schema(description = "酒品ID")
    private String wineId;

    /** 订单状态 */
    @Schema(description = "订单状态")
    private String status;

    /** 开始时间 */
    @Schema(description = "开始时间")
    private Date startTime;

    /** 结束时间 */
    @Schema(description = "结束时间")
    private Date endTime;
} 