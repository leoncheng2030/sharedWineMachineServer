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
package vip.wqs.commission.modular.account.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.constant.TransType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import vip.wqs.common.pojo.CommonEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户账户实体类
 *
 * @author WQS_TEAM
 * @date 2025-01-29
 */
@Getter
@Setter
@TableName("wine_user_account")
public class WineUserAccount extends CommonEntity {

    /** 主键ID */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键ID")
    private String id;

    /** 用户ID */
    @Schema(description = "用户ID")
    @Trans(type = TransType.RPC, targetClassName = "vip.wqs.client.modular.user.entity.ClientUser", fields = "name", alias = "user", ref = "userNickname")
    private String userId;

    /** 用户昵称（翻译字段） */
    @TableField(exist = false)
    private String userNickname;

    /** 总余额(元) */
    @Schema(description = "总余额(元)")
    private BigDecimal totalBalance;

    /** 可用余额(元) */
    @Schema(description = "可用余额(元)")
    private BigDecimal availableBalance;

    /** 冻结余额(元) */
    @Schema(description = "冻结余额(元)")
    private BigDecimal frozenBalance;

    /** 累计佣金(元) */
    @Schema(description = "累计佣金(元)")
    private BigDecimal totalCommission;

    /** 累计提现(元) */
    @Schema(description = "累计提现(元)")
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

    /** 备注 */
    @Schema(description = "备注")
    private String remark;

    /** 扩展信息 */
    @Schema(description = "扩展信息")
    private String extJson;

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

    /**
     * 计算总余额（可用余额 + 冻结余额）
     */
    public BigDecimal calculateTotalBalance() {
        BigDecimal available = availableBalance != null ? availableBalance : BigDecimal.ZERO;
        BigDecimal frozen = frozenBalance != null ? frozenBalance : BigDecimal.ZERO;
        return available.add(frozen);
    }
} 