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
package vip.wqs.commission.modular.flow.service.impl;

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
import vip.wqs.commission.modular.flow.entity.WineAccountFlow;
import vip.wqs.commission.modular.flow.mapper.WineAccountFlowMapper;
import vip.wqs.commission.modular.flow.param.WineAccountFlowIdParam;
import vip.wqs.commission.modular.flow.param.WineAccountFlowPageParam;
import vip.wqs.commission.modular.flow.service.WineAccountFlowService;
import vip.wqs.common.exception.CommonException;
import vip.wqs.common.page.CommonPageRequest;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 账户流水Service实现类
 *
 * @author WQS_TEAM
 * @date 2025-01-29
 */
@Service
public class WineAccountFlowServiceImpl extends ServiceImpl<WineAccountFlowMapper, WineAccountFlow> implements WineAccountFlowService {


    @Resource
    private WineUserAccountService wineUserAccountService;

    @Override
    public Page<WineAccountFlow> page(WineAccountFlowPageParam wineAccountFlowPageParam) {
        // 构造分页参数
        Page<WineAccountFlow> page = CommonPageRequest.defaultPage();
        if (ObjectUtil.isNotNull(wineAccountFlowPageParam.getCurrent()) && ObjectUtil.isNotNull(wineAccountFlowPageParam.getSize())) {
            page = new Page<>(wineAccountFlowPageParam.getCurrent(), wineAccountFlowPageParam.getSize());
        }

        // 构造查询条件
        LambdaQueryWrapper<WineAccountFlow> queryWrapper = new LambdaQueryWrapper<>();

        // 关键字搜索
        if (StrUtil.isNotBlank(wineAccountFlowPageParam.getSearchKey())) {
            queryWrapper.like(WineAccountFlow::getFlowNo, wineAccountFlowPageParam.getSearchKey());
        }

        // 用户ID
        if (StrUtil.isNotBlank(wineAccountFlowPageParam.getUserId())) {
            queryWrapper.eq(WineAccountFlow::getUserId, wineAccountFlowPageParam.getUserId());
        }

        // 流水类型
        if (StrUtil.isNotBlank(wineAccountFlowPageParam.getFlowType())) {
            queryWrapper.eq(WineAccountFlow::getFlowType, wineAccountFlowPageParam.getFlowType());
        }

        // 默认按创建时间倒序排列
        queryWrapper.orderByDesc(WineAccountFlow::getCreateTime);

        return this.page(page, queryWrapper);
    }

    @Override
    public WineAccountFlow detail(WineAccountFlowIdParam wineAccountFlowIdParam) {
        WineAccountFlow wineAccountFlow = this.getById(wineAccountFlowIdParam.getId());
        if (ObjectUtil.isNull(wineAccountFlow)) {
            throw new CommonException("账户流水不存在");
        }
        return wineAccountFlow;
    }

    @Override
    public List<WineAccountFlow> getUserFlowList(String userId, Integer limit) {
        LambdaQueryWrapper<WineAccountFlow> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WineAccountFlow::getUserId, userId);
        queryWrapper.orderByDesc(WineAccountFlow::getCreateTime);
        if (limit != null && limit > 0) {
            queryWrapper.last("LIMIT " + limit);
        }
        return this.list(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createCommissionFlow(String userId, BigDecimal amount, String relatedId, String description) {
        createFlow(userId, amount, "COMMISSION", relatedId, "ORDER", description);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createWithdrawFlow(String userId, BigDecimal amount, String withdrawId, String description) {
        createFlow(userId, amount.negate(), "WITHDRAW", withdrawId, "WITHDRAW", description);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createRefundFlow(String userId, BigDecimal amount, String relatedId, String description) {
        createFlow(userId, amount, "REFUND", relatedId, "ORDER", description);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createAdjustFlow(String userId, BigDecimal amount, String adjustType, String description, String reason) {
        createFlow(userId, amount, "ADJUST", null, "MANUAL", description);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createTransferFlow(String fromUserId, String toUserId, BigDecimal amount, String description) {
        // 创建转出流水
        createFlow(fromUserId, amount.negate(), "TRANSFER", toUserId, "TRANSFER", description);
        // 创建转入流水
        createFlow(toUserId, amount, "TRANSFER", fromUserId, "TRANSFER", description);
    }

    @Override
    public List<WineAccountFlow> getFlowByRelatedId(String relatedId, String relatedType) {
        LambdaQueryWrapper<WineAccountFlow> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WineAccountFlow::getRelatedId, relatedId);
        if (StrUtil.isNotBlank(relatedType)) {
            queryWrapper.eq(WineAccountFlow::getRelatedType, relatedType);
        }
        queryWrapper.orderByDesc(WineAccountFlow::getCreateTime);
        return this.list(queryWrapper);
    }

    @Override
    public WineAccountFlowSummary getUserFlowSummary(String userId) {
        WineAccountFlowSummary summary = new WineAccountFlowSummary();
        
        LambdaQueryWrapper<WineAccountFlow> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WineAccountFlow::getUserId, userId);
        queryWrapper.eq(WineAccountFlow::getStatus, "SUCCESS");
        
        List<WineAccountFlow> flowList = this.list(queryWrapper);
        
        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpense = BigDecimal.ZERO;
        BigDecimal commissionIncome = BigDecimal.ZERO;
        BigDecimal withdrawExpense = BigDecimal.ZERO;
        
        for (WineAccountFlow flow : flowList) {
            BigDecimal balanceChange = flow.getBalanceChange() != null ? flow.getBalanceChange() : BigDecimal.ZERO;
            
            if (balanceChange.compareTo(BigDecimal.ZERO) > 0) {
                totalIncome = totalIncome.add(balanceChange);
                if ("COMMISSION".equals(flow.getFlowType())) {
                    commissionIncome = commissionIncome.add(balanceChange);
                }
            } else {
                totalExpense = totalExpense.add(balanceChange.abs());
                if ("WITHDRAW".equals(flow.getFlowType())) {
                    withdrawExpense = withdrawExpense.add(balanceChange.abs());
                }
            }
        }
        
        summary.setTotalIncome(totalIncome);
        summary.setTotalExpense(totalExpense);
        summary.setCommissionIncome(commissionIncome);
        summary.setWithdrawExpense(withdrawExpense);
        
        return summary;
    }

    @Override
    public void exportAccountFlow(WineAccountFlowPageParam wineAccountFlowPageParam) {
        // TODO: 实现导出功能
    }

    /**
     * 创建流水记录的通用方法
     */
    private void createFlow(String userId, BigDecimal balanceChange, String flowType, String relatedId, String relatedType, String description) {
        // 获取用户账户信息
        WineUserAccount userAccount = wineUserAccountService.getByUserId(userId);
        if (ObjectUtil.isNull(userAccount)) {
            throw new CommonException("用户账户不存在，无法创建流水记录");
        }

        BigDecimal beforeBalance = userAccount.getAvailableBalance();
        BigDecimal afterBalance = beforeBalance.add(balanceChange);
        BigDecimal amount = balanceChange.abs();

        // 创建流水记录
        WineAccountFlow flow = new WineAccountFlow();
        flow.setUserId(userId);
        flow.setFlowNo(generateFlowNo());
        flow.setFlowType(flowType);
        flow.setAmount(amount);
        flow.setBalanceChange(balanceChange);
        flow.setRelatedId(relatedId);
        flow.setRelatedType(relatedType);
        flow.setDescription(description);
        flow.setBeforeBalance(beforeBalance);
        flow.setAfterBalance(afterBalance);
        flow.setStatus("SUCCESS");
        flow.setTransactionTime(new Date());

        this.save(flow);
    }

    /**
     * 生成流水号
     */
    private String generateFlowNo() {
        return "FL" + System.currentTimeMillis() + String.format("%04d", (int)(Math.random() * 10000));
    }
} 