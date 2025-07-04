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
package vip.wqs.payment.api.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 微信退款参数
 *
 * @author AI Assistant
 * @date 2025/01/30
 */
@Getter
@Setter
@Schema(description = "微信退款参数")
public class WechatRefundParam {

    /** 商户订单号 */
    @Schema(description = "商户订单号")
    @NotBlank(message = "商户订单号不能为空")
    private String outTradeNo;

    /** 商户退款单号 */
    @Schema(description = "商户退款单号")
    @NotBlank(message = "商户退款单号不能为空")
    private String outRefundNo;

    /** 退款金额 */
    @Schema(description = "退款金额")
    @NotNull(message = "退款金额不能为空")
    private BigDecimal refundAmount;

    /** 原订单金额 */
    @Schema(description = "原订单金额")
    @NotNull(message = "原订单金额不能为空")
    private BigDecimal totalAmount;

    /** 退款原因 */
    @Schema(description = "退款原因")
    private String reason;

    /** 支付类型 */
    @Schema(description = "支付类型")
    private String payType;
}