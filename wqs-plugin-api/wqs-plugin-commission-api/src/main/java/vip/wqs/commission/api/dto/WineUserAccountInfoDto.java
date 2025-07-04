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
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户账户信息DTO
 *
 * @author WQS_TEAM
 * @date 2025-01-30
 */
@Data
@Schema(description = "用户账户信息")
public class WineUserAccountInfoDto {

    /** 账户ID */
    @Schema(description = "账户ID")
    private String accountId;

    /** 用户ID */
    @Schema(description = "用户ID")
    private String userId;

    /** 总余额 */
    @Schema(description = "总余额")
    private BigDecimal totalBalance;

    /** 可用余额 */
    @Schema(description = "可用余额")
    private BigDecimal availableBalance;

    /** 冻结余额 */
    @Schema(description = "冻结余额")
    private BigDecimal frozenBalance;

    /** 累计佣金收入 */
    @Schema(description = "累计佣金收入")
    private BigDecimal totalCommission;

    /** 累计提现金额 */
    @Schema(description = "累计提现金额")
    private BigDecimal totalWithdraw;

    /** 账户状态 */
    @Schema(description = "账户状态(NORMAL:正常,FROZEN:冻结,DISABLED:禁用)")
    private String status;

    /** 最后佣金时间 */
    @Schema(description = "最后佣金时间")
    private Date lastCommissionTime;

    /** 最后提现时间 */
    @Schema(description = "最后提现时间")
    private Date lastWithdrawTime;

    /** 账户创建时间 */
    @Schema(description = "账户创建时间")
    private Date createTime;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;

    /**
     * 获取账户状态描述
     */
    public String getStatusDesc() {
        if (status == null) {
            return "";
        }
        switch (status) {
            case "NORMAL":
                return "正常";
            case "FROZEN":
                return "冻结";
            case "DISABLED":
                return "禁用";
            default:
                return status;
        }
    }

    /**
     * 检查账户是否可用
     */
    public boolean isAvailable() {
        return "NORMAL".equals(status);
    }

    /**
     * 检查账户是否被冻结
     */
    public boolean isFrozen() {
        return "FROZEN".equals(status);
    }

    /**
     * 检查账户是否被禁用
     */
    public boolean isDisabled() {
        return "DISABLED".equals(status);
    }
} 