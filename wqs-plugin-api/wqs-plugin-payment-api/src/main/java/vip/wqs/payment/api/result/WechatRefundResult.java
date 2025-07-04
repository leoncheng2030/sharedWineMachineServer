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
package vip.wqs.payment.api.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 微信退款结果
 *
 * @author AI Assistant
 * @date 2025/01/30
 */
@Getter
@Setter
@Schema(description = "微信退款结果")
public class WechatRefundResult {

    /** 商户订单号 */
    @Schema(description = "商户订单号")
    private String outTradeNo;

    /** 商户退款单号 */
    @Schema(description = "商户退款单号")
    private String outRefundNo;

    /** 微信退款单号 */
    @Schema(description = "微信退款单号")
    private String refundId;

    /** 微信支付订单号 */
    @Schema(description = "微信支付订单号")
    private String transactionId;

    /** 订单总金额 */
    @Schema(description = "订单总金额")
    private BigDecimal totalAmount;

    /** 退款金额 */
    @Schema(description = "退款金额")
    private BigDecimal refundAmount;

    /** 货币类型 */
    @Schema(description = "货币类型")
    private String currency;

    /** 退款状态 */
    @Schema(description = "退款状态")
    private String status;

    /** 退款入账账户 */
    @Schema(description = "退款入账账户")
    private String userReceivedAccount;

    /** 退款成功时间 */
    @Schema(description = "退款成功时间")
    private Date successTime;

    /** 退款原因 */
    @Schema(description = "退款原因")
    private String reason;
}