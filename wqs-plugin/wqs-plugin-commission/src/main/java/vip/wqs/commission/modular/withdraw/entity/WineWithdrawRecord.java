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
package vip.wqs.commission.modular.withdraw.entity;

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
 * 提现记录实体类
 *
 * @author WQS_TEAM
 * @date 2025-01-29
 */
@Getter
@Setter
@TableName("wine_withdraw_record")
public class WineWithdrawRecord extends CommonEntity {

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

    /** 提现单号 */
    @Schema(description = "提现单号")
    private String withdrawNo;

    /** 提现金额(元) */
    @Schema(description = "提现金额(元)")
    private BigDecimal withdrawAmount;

    /** 手续费(元) */
    @Schema(description = "手续费(元)")
    private BigDecimal fee;

    /** 实际到账金额(元) */
    @Schema(description = "实际到账金额(元)")
    private BigDecimal actualAmount;

    /** 提现方式 */
    @Schema(description = "提现方式(WECHAT:微信,ALIPAY:支付宝,BANK:银行卡)")
    private String withdrawType;

    /** 收款账户信息 */
    @Schema(description = "收款账户信息")
    private String accountInfo;

    /** 收款人姓名 */
    @Schema(description = "收款人姓名")
    private String accountName;

    /** 提现状态 */
    @Schema(description = "提现状态(PENDING:待审核,APPROVED:已审核,PROCESSING:处理中,SUCCESS:成功,FAILED:失败,CANCELLED:已取消)")
    private String status;

    /** 申请时间 */
    @Schema(description = "申请时间")
    private Date applyTime;

    /** 审核时间 */
    @Schema(description = "审核时间")
    private Date auditTime;

    /** 审核人ID */
    @Schema(description = "审核人ID")
    @Trans(type = TransType.RPC, targetClassName = "vip.wqs.sys.modular.user.entity.SysUser", fields = "name", alias = "auditor", ref = "auditorName")
    private String auditorId;

    /** 审核人姓名（翻译字段） */
    @TableField(exist = false)
    private String auditorName;

    /** 完成时间 */
    @Schema(description = "完成时间")
    private Date completeTime;

    /** 失败原因 */
    @Schema(description = "失败原因")
    private String failReason;

    /** 第三方交易号 */
    @Schema(description = "第三方交易号")
    private String thirdPartyTransNo;

    /** 审核备注 */
    @Schema(description = "审核备注")
    private String auditRemark;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;

    /** 扩展信息 */
    @Schema(description = "扩展信息")
    private String extJson;

    /**
     * 获取提现方式描述
     */
    public String getWithdrawTypeDesc() {
        if (withdrawType == null) {
            return "";
        }
        switch (withdrawType) {
            case "WECHAT":
                return "微信";
            case "ALIPAY":
                return "支付宝";
            case "BANK":
                return "银行卡";
            default:
                return withdrawType;
        }
    }

    /**
     * 获取提现状态描述
     */
    public String getStatusDesc() {
        if (status == null) {
            return "";
        }
        switch (status) {
            case "PENDING":
                return "待审核";
            case "APPROVED":
                return "已审核";
            case "PROCESSING":
                return "处理中";
            case "SUCCESS":
                return "成功";
            case "FAILED":
                return "失败";
            case "CANCELLED":
                return "已取消";
            default:
                return status;
        }
    }

    /**
     * 检查是否可以取消
     */
    public boolean canCancel() {
        return "PENDING".equals(status) || "APPROVED".equals(status);
    }

    /**
     * 检查是否可以审核
     */
    public boolean canAudit() {
        return "PENDING".equals(status);
    }

    /**
     * 检查是否可以重新提交
     */
    public boolean canResubmit() {
        return "FAILED".equals(status) || "CANCELLED".equals(status);
    }

    /**
     * 检查是否已完成
     */
    public boolean isCompleted() {
        return "SUCCESS".equals(status) || "FAILED".equals(status) || "CANCELLED".equals(status);
    }

    /**
     * 计算手续费率
     */
    public BigDecimal getFeeRate() {
        if (withdrawAmount == null || withdrawAmount.compareTo(BigDecimal.ZERO) == 0 || fee == null) {
            return BigDecimal.ZERO;
        }
        return fee.divide(withdrawAmount, 4, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 获取状态颜色（用于前端显示）
     */
    public String getStatusColor() {
        if (status == null) {
            return "default";
        }
        switch (status) {
            case "PENDING":
                return "warning";
            case "APPROVED":
                return "processing";
            case "PROCESSING":
                return "processing";
            case "SUCCESS":
                return "success";
            case "FAILED":
                return "error";
            case "CANCELLED":
                return "default";
            default:
                return "default";
        }
    }
} 