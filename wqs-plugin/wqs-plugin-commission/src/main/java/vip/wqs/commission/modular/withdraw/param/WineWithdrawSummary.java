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
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 提现汇总统计
 *
 * @author WQS_TEAM
 * @date 2025-01-29
 */
@Getter
@Setter
public class WineWithdrawSummary {

    /** 总提现金额 */
    @Schema(description = "总提现金额")
    private BigDecimal totalWithdrawAmount = BigDecimal.ZERO;

    /** 总手续费 */
    @Schema(description = "总手续费")
    private BigDecimal totalFee = BigDecimal.ZERO;

    /** 实际到账总金额 */
    @Schema(description = "实际到账总金额")
    private BigDecimal totalActualAmount = BigDecimal.ZERO;

    /** 提现次数 */
    @Schema(description = "提现次数")
    private Integer withdrawCount = 0;

    /** 成功次数 */
    @Schema(description = "成功次数")
    private Integer successCount = 0;

    /** 失败次数 */
    @Schema(description = "失败次数")
    private Integer failedCount = 0;

    /** 待处理次数 */
    @Schema(description = "待处理次数")
    private Integer pendingCount = 0;

    /** 成功率 */
    @Schema(description = "成功率")
    public BigDecimal getSuccessRate() {
        if (withdrawCount == 0) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(successCount).divide(new BigDecimal(withdrawCount), 4, BigDecimal.ROUND_HALF_UP);
    }
} 