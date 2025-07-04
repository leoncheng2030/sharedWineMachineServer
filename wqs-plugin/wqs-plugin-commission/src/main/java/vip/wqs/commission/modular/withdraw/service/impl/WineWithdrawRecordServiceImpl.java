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
package vip.wqs.commission.modular.withdraw.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.wqs.commission.modular.account.entity.WineUserAccount;
import vip.wqs.commission.modular.account.service.WineUserAccountService;
import vip.wqs.commission.modular.flow.service.WineAccountFlowService;
import vip.wqs.commission.modular.withdraw.entity.WineWithdrawRecord;
import vip.wqs.commission.modular.withdraw.mapper.WineWithdrawRecordMapper;
import vip.wqs.commission.modular.withdraw.param.WineWithdrawRecordAuditParam;
import vip.wqs.commission.modular.withdraw.param.WineWithdrawRecordIdParam;
import vip.wqs.commission.modular.withdraw.param.WineWithdrawRecordPageParam;
import vip.wqs.commission.modular.withdraw.service.WineWithdrawRecordService;
import vip.wqs.common.exception.CommonException;
import vip.wqs.common.page.CommonPageRequest;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 提现记录Service实现类
 *
 * @author WQS_TEAM
 * @date 2025-01-29
 */
@Service
public class WineWithdrawRecordServiceImpl extends ServiceImpl<WineWithdrawRecordMapper, WineWithdrawRecord> implements WineWithdrawRecordService {

    @Resource
    private WineWithdrawRecordMapper wineWithdrawRecordMapper;

    @Resource
    private WineUserAccountService wineUserAccountService;

    @Resource
    private WineAccountFlowService wineAccountFlowService;

    @Override
    public Page<WineWithdrawRecord> page(WineWithdrawRecordPageParam wineWithdrawRecordPageParam) {
        // 构造分页参数
        Page<WineWithdrawRecord> page = CommonPageRequest.defaultPage();
        if (ObjectUtil.isNotNull(wineWithdrawRecordPageParam.getCurrent()) && ObjectUtil.isNotNull(wineWithdrawRecordPageParam.getSize())) {
            page = new Page<>(wineWithdrawRecordPageParam.getCurrent(), wineWithdrawRecordPageParam.getSize());
        }

        // 构造查询条件
        LambdaQueryWrapper<WineWithdrawRecord> queryWrapper = new LambdaQueryWrapper<>();

        // 关键字搜索
        if (StrUtil.isNotBlank(wineWithdrawRecordPageParam.getSearchKey())) {
            queryWrapper.like(WineWithdrawRecord::getWithdrawNo, wineWithdrawRecordPageParam.getSearchKey());
        }

        // 用户ID
        if (StrUtil.isNotBlank(wineWithdrawRecordPageParam.getUserId())) {
            queryWrapper.eq(WineWithdrawRecord::getUserId, wineWithdrawRecordPageParam.getUserId());
        }

        // 提现方式
        if (StrUtil.isNotBlank(wineWithdrawRecordPageParam.getWithdrawType())) {
            queryWrapper.eq(WineWithdrawRecord::getWithdrawType, wineWithdrawRecordPageParam.getWithdrawType());
        }

        // 提现状态
        if (StrUtil.isNotBlank(wineWithdrawRecordPageParam.getStatus())) {
            queryWrapper.eq(WineWithdrawRecord::getStatus, wineWithdrawRecordPageParam.getStatus());
        }

        // 默认按创建时间倒序排列
        queryWrapper.orderByDesc(WineWithdrawRecord::getCreateTime);

        return this.page(page, queryWrapper);
    }

    @Override
    public WineWithdrawRecord detail(WineWithdrawRecordIdParam wineWithdrawRecordIdParam) {
        WineWithdrawRecord wineWithdrawRecord = this.getById(wineWithdrawRecordIdParam.getId());
        if (ObjectUtil.isNull(wineWithdrawRecord)) {
            throw new CommonException("提现记录不存在");
        }
        return wineWithdrawRecord;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String applyWithdraw(String userId, BigDecimal withdrawAmount, String withdrawType, String accountInfo, String accountName, String remark) {
        // 检查用户账户
        WineUserAccount userAccount = wineUserAccountService.getByUserId(userId);
        if (ObjectUtil.isNull(userAccount)) {
            throw new CommonException("用户账户不存在");
        }

        // 检查账户状态
        if (!"NORMAL".equals(userAccount.getStatus())) {
            throw new CommonException("账户状态异常，无法提现");
        }

        // 检查余额是否充足
        if (userAccount.getAvailableBalance().compareTo(withdrawAmount) < 0) {
            throw new CommonException("可用余额不足");
        }

        // 计算手续费
        BigDecimal fee = calculateWithdrawFee(withdrawAmount, withdrawType);
        BigDecimal actualAmount = withdrawAmount.subtract(fee);

        // 创建提现记录
        WineWithdrawRecord withdrawRecord = new WineWithdrawRecord();
        withdrawRecord.setUserId(userId);
        withdrawRecord.setWithdrawNo(generateWithdrawNo());
        withdrawRecord.setWithdrawAmount(withdrawAmount);
        withdrawRecord.setFee(fee);
        withdrawRecord.setActualAmount(actualAmount);
        withdrawRecord.setWithdrawType(withdrawType);
        withdrawRecord.setAccountInfo(accountInfo);
        withdrawRecord.setAccountName(accountName);
        withdrawRecord.setStatus("PENDING");
        withdrawRecord.setApplyTime(new Date());
        withdrawRecord.setRemark(remark);

        this.save(withdrawRecord);

        // 冻结用户余额
        wineUserAccountService.freezeBalance(userId, withdrawAmount, "提现申请冻结");

        return withdrawRecord.getWithdrawNo();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditWithdraw(WineWithdrawRecordAuditParam wineWithdrawRecordAuditParam) {
        WineWithdrawRecord withdrawRecord = this.getById(wineWithdrawRecordAuditParam.getId());
        if (ObjectUtil.isNull(withdrawRecord)) {
            throw new CommonException("提现记录不存在");
        }

        if (!"PENDING".equals(withdrawRecord.getStatus())) {
            throw new CommonException("只能审核待审核状态的提现申请");
        }

        Date now = new Date();
        withdrawRecord.setAuditTime(now);
        withdrawRecord.setAuditRemark(wineWithdrawRecordAuditParam.getAuditRemark());

        if ("APPROVED".equals(wineWithdrawRecordAuditParam.getAuditResult())) {
            // 审核通过
            withdrawRecord.setStatus("APPROVED");
        } else if ("REJECTED".equals(wineWithdrawRecordAuditParam.getAuditResult())) {
            // 审核拒绝
            withdrawRecord.setStatus("FAILED");
            withdrawRecord.setFailReason(wineWithdrawRecordAuditParam.getFailReason());
            
            // 解冻用户余额
            wineUserAccountService.unfreezeBalance(withdrawRecord.getUserId(), withdrawRecord.getWithdrawAmount(), "提现审核拒绝解冻");
        }

        this.updateById(withdrawRecord);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelWithdraw(WineWithdrawRecordIdParam wineWithdrawRecordIdParam) {
        WineWithdrawRecord withdrawRecord = this.getById(wineWithdrawRecordIdParam.getId());
        if (ObjectUtil.isNull(withdrawRecord)) {
            throw new CommonException("提现记录不存在");
        }

        if (!withdrawRecord.canCancel()) {
            throw new CommonException("当前状态不允许取消");
        }

        withdrawRecord.setStatus("CANCELLED");
        this.updateById(withdrawRecord);

        // 解冻用户余额
        wineUserAccountService.unfreezeBalance(withdrawRecord.getUserId(), withdrawRecord.getWithdrawAmount(), "提现取消解冻");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmWithdrawSuccess(WineWithdrawRecordIdParam wineWithdrawRecordIdParam, String thirdPartyTransNo) {
        WineWithdrawRecord withdrawRecord = this.getById(wineWithdrawRecordIdParam.getId());
        if (ObjectUtil.isNull(withdrawRecord)) {
            throw new CommonException("提现记录不存在");
        }

        if (!"APPROVED".equals(withdrawRecord.getStatus()) && !"PROCESSING".equals(withdrawRecord.getStatus())) {
            throw new CommonException("只能确认已审核或处理中状态的提现");
        }

        withdrawRecord.setStatus("SUCCESS");
        withdrawRecord.setCompleteTime(new Date());
        withdrawRecord.setThirdPartyTransNo(thirdPartyTransNo);
        this.updateById(withdrawRecord);

        // 扣减用户余额并创建流水
        wineUserAccountService.subtractAvailableBalance(withdrawRecord.getUserId(), withdrawRecord.getWithdrawAmount(), "提现成功");
        wineAccountFlowService.createWithdrawFlow(withdrawRecord.getUserId(), withdrawRecord.getWithdrawAmount(), withdrawRecord.getId(), "提现成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markWithdrawFailed(WineWithdrawRecordIdParam wineWithdrawRecordIdParam, String failReason) {
        WineWithdrawRecord withdrawRecord = this.getById(wineWithdrawRecordIdParam.getId());
        if (ObjectUtil.isNull(withdrawRecord)) {
            throw new CommonException("提现记录不存在");
        }

        withdrawRecord.setStatus("FAILED");
        withdrawRecord.setFailReason(failReason);
        withdrawRecord.setCompleteTime(new Date());
        this.updateById(withdrawRecord);

        // 解冻用户余额
        wineUserAccountService.unfreezeBalance(withdrawRecord.getUserId(), withdrawRecord.getWithdrawAmount(), "提现失败解冻");
    }

    @Override
    public void resubmitWithdraw(WineWithdrawRecordIdParam wineWithdrawRecordIdParam) {
        // TODO: 实现重新提交逻辑
    }

    @Override
    public List<WineWithdrawRecord> getUserWithdrawList(String userId, Integer limit) {
        LambdaQueryWrapper<WineWithdrawRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WineWithdrawRecord::getUserId, userId);
        queryWrapper.orderByDesc(WineWithdrawRecord::getCreateTime);
        if (limit != null && limit > 0) {
            queryWrapper.last("LIMIT " + limit);
        }
        return this.list(queryWrapper);
    }

    @Override
    public WineWithdrawRecord getByWithdrawNo(String withdrawNo) {
        LambdaQueryWrapper<WineWithdrawRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WineWithdrawRecord::getWithdrawNo, withdrawNo);
        return this.getOne(queryWrapper);
    }

    @Override
    public WineWithdrawSummary getUserWithdrawSummary(String userId) {
        WineWithdrawSummary summary = new WineWithdrawSummary();
        
        LambdaQueryWrapper<WineWithdrawRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WineWithdrawRecord::getUserId, userId);
        
        List<WineWithdrawRecord> withdrawList = this.list(queryWrapper);
        
        BigDecimal totalWithdrawAmount = BigDecimal.ZERO;
        BigDecimal totalFee = BigDecimal.ZERO;
        BigDecimal totalActualAmount = BigDecimal.ZERO;
        int withdrawCount = withdrawList.size();
        int successCount = 0;
        int failedCount = 0;
        int pendingCount = 0;
        
        for (WineWithdrawRecord record : withdrawList) {
            totalWithdrawAmount = totalWithdrawAmount.add(record.getWithdrawAmount());
            totalFee = totalFee.add(record.getFee() != null ? record.getFee() : BigDecimal.ZERO);
            
            switch (record.getStatus()) {
                case "SUCCESS":
                    successCount++;
                    totalActualAmount = totalActualAmount.add(record.getActualAmount() != null ? record.getActualAmount() : BigDecimal.ZERO);
                    break;
                case "FAILED":
                case "CANCELLED":
                    failedCount++;
                    break;
                case "PENDING":
                case "APPROVED":
                case "PROCESSING":
                    pendingCount++;
                    break;
            }
        }
        
        summary.setTotalWithdrawAmount(totalWithdrawAmount);
        summary.setTotalFee(totalFee);
        summary.setTotalActualAmount(totalActualAmount);
        summary.setWithdrawCount(withdrawCount);
        summary.setSuccessCount(successCount);
        summary.setFailedCount(failedCount);
        summary.setPendingCount(pendingCount);
        
        return summary;
    }

    @Override
    public Long countPendingWithdraw() {
        LambdaQueryWrapper<WineWithdrawRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WineWithdrawRecord::getStatus, "PENDING");
        return this.count(queryWrapper);
    }

    @Override
    public void batchAuditWithdraw(List<String> ids, String auditResult, String auditRemark) {
        // TODO: 实现批量审核逻辑
    }

    @Override
    public void exportWithdrawRecord(WineWithdrawRecordPageParam wineWithdrawRecordPageParam) {
        // TODO: 实现导出功能
    }

    @Override
    public String generateWithdrawNo() {
        return "WD" + System.currentTimeMillis() + String.format("%04d", (int)(Math.random() * 10000));
    }

    @Override
    public BigDecimal calculateWithdrawFee(BigDecimal withdrawAmount, String withdrawType) {
        // 简单的手续费计算逻辑，可以根据实际需求调整
        BigDecimal feeRate = BigDecimal.ZERO;
        
        switch (withdrawType) {
            case "WECHAT":
                feeRate = new BigDecimal("0.001"); // 0.1%
                break;
            case "ALIPAY":
                feeRate = new BigDecimal("0.001"); // 0.1%
                break;
            case "BANK":
                feeRate = new BigDecimal("0.002"); // 0.2%
                break;
            default:
                feeRate = new BigDecimal("0.001");
                break;
        }
        
        BigDecimal fee = withdrawAmount.multiply(feeRate);
        // 设置最低手续费
        BigDecimal minFee = new BigDecimal("0.01");
        return fee.compareTo(minFee) < 0 ? minFee : fee;
    }
} 