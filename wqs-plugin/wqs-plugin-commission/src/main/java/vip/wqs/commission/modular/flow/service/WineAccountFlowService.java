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
package vip.wqs.commission.modular.flow.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import vip.wqs.commission.modular.flow.entity.WineAccountFlow;
import vip.wqs.commission.modular.flow.param.WineAccountFlowIdParam;
import vip.wqs.commission.modular.flow.param.WineAccountFlowPageParam;

import java.math.BigDecimal;
import java.util.List;

/**
 * 账户流水Service接口
 *
 * @author WQS_TEAM
 * @date 2025-01-29
 */
public interface WineAccountFlowService extends IService<WineAccountFlow> {

    /**
     * 获取账户流水分页列表
     */
    Page<WineAccountFlow> page(WineAccountFlowPageParam wineAccountFlowPageParam);

    /**
     * 获取账户流水详情
     */
    WineAccountFlow detail(WineAccountFlowIdParam wineAccountFlowIdParam);

    /**
     * 获取用户账户流水列表
     */
    List<WineAccountFlow> getUserFlowList(String userId, Integer limit);

    /**
     * 创建佣金收入流水
     */
    void createCommissionFlow(String userId, BigDecimal amount, String relatedId, String description);

    /**
     * 创建提现支出流水
     */
    void createWithdrawFlow(String userId, BigDecimal amount, String withdrawId, String description);

    /**
     * 创建退款流水
     */
    void createRefundFlow(String userId, BigDecimal amount, String relatedId, String description);

    /**
     * 创建余额调整流水
     */
    void createAdjustFlow(String userId, BigDecimal amount, String adjustType, String description, String reason);

    /**
     * 创建转账流水
     */
    void createTransferFlow(String fromUserId, String toUserId, BigDecimal amount, String description);

    /**
     * 根据关联ID获取流水记录
     */
    List<WineAccountFlow> getFlowByRelatedId(String relatedId, String relatedType);

    /**
     * 统计用户流水汇总信息
     */
    WineAccountFlowSummary getUserFlowSummary(String userId);

    /**
     * 导出账户流水数据
     */
    void exportAccountFlow(WineAccountFlowPageParam wineAccountFlowPageParam);

    /**
     * 账户流水汇总信息内部类
     */
    class WineAccountFlowSummary {
        /** 总收入 */
        private BigDecimal totalIncome;
        /** 总支出 */
        private BigDecimal totalExpense;
        /** 佣金收入 */
        private BigDecimal commissionIncome;
        /** 提现支出 */
        private BigDecimal withdrawExpense;
        /** 退款金额 */
        private BigDecimal refundAmount;
        /** 调整金额 */
        private BigDecimal adjustAmount;

        // Getter和Setter方法
        public BigDecimal getTotalIncome() { return totalIncome; }
        public void setTotalIncome(BigDecimal totalIncome) { this.totalIncome = totalIncome; }
        public BigDecimal getTotalExpense() { return totalExpense; }
        public void setTotalExpense(BigDecimal totalExpense) { this.totalExpense = totalExpense; }
        public BigDecimal getCommissionIncome() { return commissionIncome; }
        public void setCommissionIncome(BigDecimal commissionIncome) { this.commissionIncome = commissionIncome; }
        public BigDecimal getWithdrawExpense() { return withdrawExpense; }
        public void setWithdrawExpense(BigDecimal withdrawExpense) { this.withdrawExpense = withdrawExpense; }
        public BigDecimal getRefundAmount() { return refundAmount; }
        public void setRefundAmount(BigDecimal refundAmount) { this.refundAmount = refundAmount; }
        public BigDecimal getAdjustAmount() { return adjustAmount; }
        public void setAdjustAmount(BigDecimal adjustAmount) { this.adjustAmount = adjustAmount; }
    }
} 