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
package vip.wqs.order.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单统计响应数据
 *
 * @author wqs
 * @date 2025/01/30
 */
@Getter
@Setter
@Schema(description = "订单统计响应数据")
public class WineOrderStatisticsPojo implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 待支付订单数 */
    @Schema(description = "待支付订单数")
    private Integer pendingPaymentCount = 0;

    /** 已支付订单数 */
    @Schema(description = "已支付订单数")
    private Integer paidCount = 0;

    /** 出酒中订单数 */
    @Schema(description = "出酒中订单数")
    private Integer dispensingCount = 0;

    /** 进行中订单数（已支付+出酒中） */
    @Schema(description = "进行中订单数（已支付+出酒中）")
    private Integer processingCount = 0;

    /** 已完成订单数 */
    @Schema(description = "已完成订单数")
    private Integer completedCount = 0;

    /** 已取消订单数 */
    @Schema(description = "已取消订单数")
    private Integer cancelledCount = 0;

    /** 已退款订单数 */
    @Schema(description = "已退款订单数")
    private Integer refundedCount = 0;

    /** 总订单数 */
    @Schema(description = "总订单数")
    private Integer totalCount = 0;

    /** 累计消费金额(元) */
    @Schema(description = "累计消费金额(元)")
    private BigDecimal totalAmount = BigDecimal.ZERO;

    /** 本月订单数 */
    @Schema(description = "本月订单数")
    private Integer monthlyCount = 0;

    /** 本月消费金额(元) */
    @Schema(description = "本月消费金额(元)")
    private BigDecimal monthlyAmount = BigDecimal.ZERO;

    /** 今日订单数 */
    @Schema(description = "今日订单数")
    private Integer todayCount = 0;

    /** 今日消费金额(元) */
    @Schema(description = "今日消费金额(元)")
    private BigDecimal todayAmount = BigDecimal.ZERO;

    /** 累计出酒量(ml) */
    @Schema(description = "累计出酒量(ml)")
    private Long totalWineAmount = 0L;

    /** 平均订单金额(元) */
    @Schema(description = "平均订单金额(元)")
    private BigDecimal averageOrderAmount = BigDecimal.ZERO;

    // ========== 小程序专用字段 ==========

    /** 最常购买的酒品名称 */
    @Schema(description = "最常购买的酒品名称")
    private String favoriteWineName;

    /** 最常使用的设备名称 */
    @Schema(description = "最常使用的设备名称")
    private String favoriteDeviceName;
} 