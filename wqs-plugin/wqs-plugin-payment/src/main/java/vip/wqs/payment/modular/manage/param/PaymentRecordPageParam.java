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
package vip.wqs.payment.modular.manage.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付记录分页查询参数
 *
 * @author wqs
 * @date 2025/01/30
 */
@Getter
@Setter
@Schema(description = "支付记录分页查询参数")
public class PaymentRecordPageParam {

    /** 支付单号 */
    @Schema(description = "支付单号")
    private String paymentNo;

    /** 订单号 */
    @Schema(description = "订单号")
    private String orderNo;

    /** 用户ID */
    @Schema(description = "用户ID")
    private String userId;

    /** 支付方式 */
    @Schema(description = "支付方式")
    private String payType;

    /** 支付状态 */
    @Schema(description = "支付状态")
    private String payStatus;

    /** 最小支付金额 */
    @Schema(description = "最小支付金额")
    private BigDecimal minAmount;

    /** 最大支付金额 */
    @Schema(description = "最大支付金额")
    private BigDecimal maxAmount;

    /** 开始时间 */
    @Schema(description = "开始时间")
    private Date startTime;

    /** 结束时间 */
    @Schema(description = "结束时间")
    private Date endTime;

    /** 第三方交易流水号 */
    @Schema(description = "第三方交易流水号")
    private String transactionId;
} 