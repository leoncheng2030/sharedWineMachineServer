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
package vip.wqs.commission.modular.flow.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 账户流水汇总统计
 *
 * @author WQS_TEAM
 * @date 2025-01-29
 */
@Getter
@Setter
public class WineAccountFlowSummary {

    /** 总收入金额 */
    @Schema(description = "总收入金额")
    private BigDecimal totalIncomeAmount = BigDecimal.ZERO;

    /** 总支出金额 */
    @Schema(description = "总支出金额")
    private BigDecimal totalExpenseAmount = BigDecimal.ZERO;

    /** 净收入金额 */
    @Schema(description = "净收入金额")
    private BigDecimal netAmount = BigDecimal.ZERO;

    /** 流水总数 */
    @Schema(description = "流水总数")
    private Integer totalCount = 0;

    /** 收入流水数 */
    @Schema(description = "收入流水数")
    private Integer incomeCount = 0;

    /** 支出流水数 */
    @Schema(description = "支出流水数")
    private Integer expenseCount = 0;

    /** 佣金收入金额 */
    @Schema(description = "佣金收入金额")
    private BigDecimal commissionAmount = BigDecimal.ZERO;

    /** 提现支出金额 */
    @Schema(description = "提现支出金额")
    private BigDecimal withdrawAmount = BigDecimal.ZERO;

    /** 退款金额 */
    @Schema(description = "退款金额")
    private BigDecimal refundAmount = BigDecimal.ZERO;

    /** 其他收入金额 */
    @Schema(description = "其他收入金额")
    private BigDecimal otherIncomeAmount = BigDecimal.ZERO;

    /** 其他支出金额 */
    @Schema(description = "其他支出金额")
    private BigDecimal otherExpenseAmount = BigDecimal.ZERO;

    /**
     * 计算净收入
     */
    public void calculateNetAmount() {
        this.netAmount = this.totalIncomeAmount.subtract(this.totalExpenseAmount);
    }
} 