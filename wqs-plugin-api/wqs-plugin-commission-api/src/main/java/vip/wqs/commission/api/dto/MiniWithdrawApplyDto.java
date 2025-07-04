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
package vip.wqs.commission.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 小程序提现申请DTO
 *
 * @author WQS_TEAM
 * @date 2025-01-30
 */
@Getter
@Setter
@Schema(description = "小程序提现申请DTO")
public class MiniWithdrawApplyDto {

    /** 用户ID（由系统自动设置，无需前端传递） */
    @Schema(description = "用户ID", hidden = true)
    private String userId;

    /** 提现金额 */
    @Schema(description = "提现金额", example = "100.00")
    @NotNull(message = "提现金额不能为空")
    @DecimalMin(value = "0.01", message = "提现金额必须大于0.01")
    private BigDecimal amount;

    /** 提现方式 */
    @Schema(description = "提现方式(WECHAT:微信,ALIPAY:支付宝,BANK:银行卡)", example = "WECHAT")
    @NotBlank(message = "提现方式不能为空")
    private String withdrawType;

    /** 收款账户 */
    @Schema(description = "收款账户", example = "张三")
    @NotBlank(message = "收款账户不能为空")
    private String accountName;

    /** 收款账号 */
    @Schema(description = "收款账号", example = "13800138000")
    @NotBlank(message = "收款账号不能为空")
    private String accountNumber;

    /** 银行名称（银行卡提现时必填） */
    @Schema(description = "银行名称", example = "中国工商银行")
    private String bankName;

    /** 银行支行（银行卡提现时选填） */
    @Schema(description = "银行支行", example = "北京分行")
    private String bankBranch;

    /** 备注 */
    @Schema(description = "备注", example = "提现申请")
    private String remark;
} 