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
package vip.wqs.commission.modular.flow.entity;

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
 * 账户流水实体类
 *
 * @author WQS_TEAM
 * @date 2025-01-29
 */
@Getter
@Setter
@TableName("wine_account_flow")
public class WineAccountFlow extends CommonEntity {

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

    /** 流水号 */
    @Schema(description = "流水号")
    private String flowNo;

    /** 流水类型 */
    @Schema(description = "流水类型(COMMISSION:佣金收入,WITHDRAW:提现支出,REFUND:退款,TRANSFER:转账,ADJUST:余额调整)")
    private String flowType;

    /** 交易金额(元) */
    @Schema(description = "交易金额(元)")
    private BigDecimal amount;

    /** 余额变动(元) - 正数表示增加，负数表示减少 */
    @Schema(description = "余额变动(元)")
    private BigDecimal balanceChange;

    /** 关联ID */
    @Schema(description = "关联ID(订单ID、提现ID等)")
    private String relatedId;

    /** 关联类型 */
    @Schema(description = "关联类型(ORDER:订单,WITHDRAW:提现,MANUAL:手动调整)")
    private String relatedType;

    /** 流水描述 */
    @Schema(description = "流水描述")
    private String description;

    /** 交易前余额(元) */
    @Schema(description = "交易前余额(元)")
    private BigDecimal beforeBalance;

    /** 交易后余额(元) */
    @Schema(description = "交易后余额(元)")
    private BigDecimal afterBalance;

    /** 流水状态 */
    @Schema(description = "流水状态(SUCCESS:成功,FAILED:失败,PENDING:处理中)")
    private String status;

    /** 交易时间 */
    @Schema(description = "交易时间")
    private Date transactionTime;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;

    /** 扩展信息 */
    @Schema(description = "扩展信息")
    private String extJson;

    /**
     * 获取流水类型描述
     */
    public String getFlowTypeDesc() {
        if (flowType == null) {
            return "";
        }
        switch (flowType) {
            case "COMMISSION":
                return "佣金收入";
            case "WITHDRAW":
                return "提现支出";
            case "REFUND":
                return "退款";
            case "TRANSFER":
                return "转账";
            case "ADJUST":
                return "余额调整";
            default:
                return flowType;
        }
    }

    /**
     * 获取流水状态描述
     */
    public String getStatusDesc() {
        if (status == null) {
            return "";
        }
        switch (status) {
            case "SUCCESS":
                return "成功";
            case "FAILED":
                return "失败";
            case "PENDING":
                return "处理中";
            default:
                return status;
        }
    }

    /**
     * 检查是否为收入流水
     */
    public boolean isIncomeFlow() {
        return "COMMISSION".equals(flowType) || "REFUND".equals(flowType) || 
               ("ADJUST".equals(flowType) && balanceChange != null && balanceChange.compareTo(BigDecimal.ZERO) > 0);
    }

    /**
     * 检查是否为支出流水
     */
    public boolean isExpenseFlow() {
        return "WITHDRAW".equals(flowType) || "TRANSFER".equals(flowType) || 
               ("ADJUST".equals(flowType) && balanceChange != null && balanceChange.compareTo(BigDecimal.ZERO) < 0);
    }

    /**
     * 获取金额显示（带符号）
     */
    public String getAmountDisplay() {
        if (amount == null) {
            return "¥0.00";
        }
        String prefix = isIncomeFlow() ? "+" : "-";
        return prefix + "¥" + amount.toString();
    }
} 