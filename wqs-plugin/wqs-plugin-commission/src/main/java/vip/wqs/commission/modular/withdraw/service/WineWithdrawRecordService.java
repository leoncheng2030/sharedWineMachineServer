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
package vip.wqs.commission.modular.withdraw.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import vip.wqs.commission.modular.withdraw.entity.WineWithdrawRecord;
import vip.wqs.commission.modular.withdraw.param.WineWithdrawRecordAuditParam;
import vip.wqs.commission.modular.withdraw.param.WineWithdrawRecordIdParam;
import vip.wqs.commission.modular.withdraw.param.WineWithdrawRecordPageParam;

import java.math.BigDecimal;
import java.util.List;

/**
 * 提现记录Service接口
 *
 * @author WQS_TEAM
 * @date 2025-01-29
 */
public interface WineWithdrawRecordService extends IService<WineWithdrawRecord> {

    /**
     * 获取提现记录分页列表
     */
    Page<WineWithdrawRecord> page(WineWithdrawRecordPageParam wineWithdrawRecordPageParam);

    /**
     * 获取提现记录详情
     */
    WineWithdrawRecord detail(WineWithdrawRecordIdParam wineWithdrawRecordIdParam);

    /**
     * 申请提现
     */
    String applyWithdraw(String userId, BigDecimal withdrawAmount, String withdrawType, String accountInfo, String accountName, String remark);

    /**
     * 审核提现申请
     */
    void auditWithdraw(WineWithdrawRecordAuditParam wineWithdrawRecordAuditParam);

    /**
     * 取消提现申请
     */
    void cancelWithdraw(WineWithdrawRecordIdParam wineWithdrawRecordIdParam);

    /**
     * 确认提现成功
     */
    void confirmWithdrawSuccess(WineWithdrawRecordIdParam wineWithdrawRecordIdParam, String thirdPartyTransNo);

    /**
     * 标记提现失败
     */
    void markWithdrawFailed(WineWithdrawRecordIdParam wineWithdrawRecordIdParam, String failReason);

    /**
     * 重新提交提现申请
     */
    void resubmitWithdraw(WineWithdrawRecordIdParam wineWithdrawRecordIdParam);

    /**
     * 获取用户提现记录列表
     */
    List<WineWithdrawRecord> getUserWithdrawList(String userId, Integer limit);

    /**
     * 根据提现单号获取记录
     */
    WineWithdrawRecord getByWithdrawNo(String withdrawNo);

    /**
     * 统计用户提现汇总信息
     */
    WineWithdrawSummary getUserWithdrawSummary(String userId);

    /**
     * 统计待审核提现数量
     */
    Long countPendingWithdraw();

    /**
     * 批量审核提现申请
     */
    void batchAuditWithdraw(List<String> ids, String auditResult, String auditRemark);

    /**
     * 导出提现记录数据
     */
    void exportWithdrawRecord(WineWithdrawRecordPageParam wineWithdrawRecordPageParam);

    /**
     * 生成提现单号
     */
    String generateWithdrawNo();

    /**
     * 计算提现手续费
     */
    BigDecimal calculateWithdrawFee(BigDecimal withdrawAmount, String withdrawType);

    /**
     * 提现汇总信息内部类
     */
    class WineWithdrawSummary {
        /** 总提现金额 */
        private BigDecimal totalWithdrawAmount;
        /** 总手续费 */
        private BigDecimal totalFee;
        /** 实际到账金额 */
        private BigDecimal totalActualAmount;
        /** 提现次数 */
        private Integer withdrawCount;
        /** 成功次数 */
        private Integer successCount;
        /** 失败次数 */
        private Integer failedCount;
        /** 待审核次数 */
        private Integer pendingCount;

        // Getter和Setter方法
        public BigDecimal getTotalWithdrawAmount() { return totalWithdrawAmount; }
        public void setTotalWithdrawAmount(BigDecimal totalWithdrawAmount) { this.totalWithdrawAmount = totalWithdrawAmount; }
        public BigDecimal getTotalFee() { return totalFee; }
        public void setTotalFee(BigDecimal totalFee) { this.totalFee = totalFee; }
        public BigDecimal getTotalActualAmount() { return totalActualAmount; }
        public void setTotalActualAmount(BigDecimal totalActualAmount) { this.totalActualAmount = totalActualAmount; }
        public Integer getWithdrawCount() { return withdrawCount; }
        public void setWithdrawCount(Integer withdrawCount) { this.withdrawCount = withdrawCount; }
        public Integer getSuccessCount() { return successCount; }
        public void setSuccessCount(Integer successCount) { this.successCount = successCount; }
        public Integer getFailedCount() { return failedCount; }
        public void setFailedCount(Integer failedCount) { this.failedCount = failedCount; }
        public Integer getPendingCount() { return pendingCount; }
        public void setPendingCount(Integer pendingCount) { this.pendingCount = pendingCount; }
    }
} 