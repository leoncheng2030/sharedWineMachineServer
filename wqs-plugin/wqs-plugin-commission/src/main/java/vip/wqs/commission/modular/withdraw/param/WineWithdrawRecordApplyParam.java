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
package vip.wqs.commission.modular.withdraw.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 提现申请参数
 *
 * @author WQS_TEAM
 * @date 2025-01-29
 */
@Getter
@Setter
public class WineWithdrawRecordApplyParam {

    /** 用户ID */
    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "用户ID不能为空")
    private String userId;

    /** 提现金额(元) */
    @Schema(description = "提现金额(元)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "提现金额不能为空")
    @DecimalMin(value = "0.01", message = "提现金额必须大于0")
    private BigDecimal withdrawAmount;

    /** 提现方式 */
    @Schema(description = "提现方式(WECHAT:微信,ALIPAY:支付宝,BANK:银行卡)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "提现方式不能为空")
    private String withdrawType;

    /** 收款账户信息 */
    @Schema(description = "收款账户信息", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "收款账户信息不能为空")
    private String accountInfo;

    /** 收款人姓名 */
    @Schema(description = "收款人姓名", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "收款人姓名不能为空")
    private String accountName;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;
} 