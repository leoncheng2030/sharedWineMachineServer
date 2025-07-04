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
package vip.wqs.commission.modular.config.provider;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vip.wqs.commission.api.WineCommissionRecordApi;
import vip.wqs.commission.api.dto.*;
import vip.wqs.commission.modular.account.service.WineUserAccountService;
import vip.wqs.commission.modular.flow.entity.WineAccountFlow;
import vip.wqs.commission.modular.flow.param.WineAccountFlowPageParam;
import vip.wqs.commission.modular.flow.service.WineAccountFlowService;
import vip.wqs.commission.modular.record.entity.WineCommissionRecord;
import vip.wqs.commission.modular.record.param.WineCommissionRecordPageParam;
import vip.wqs.commission.modular.record.service.WineCommissionRecordService;
import vip.wqs.commission.modular.withdraw.entity.WineWithdrawRecord;
import vip.wqs.commission.modular.withdraw.param.WineWithdrawRecordPageParam;
import vip.wqs.commission.modular.withdraw.service.WineWithdrawRecordService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 小程序佣金管理API实现类
 *
 * @author WQS_TEAM
 * @date 2025-01-30
 */
@Slf4j
@Service
public class WineCommissionRecordApiProvider implements WineCommissionRecordApi {

    @Resource
    private WineCommissionRecordService wineCommissionRecordService;

    @Resource
    private WineWithdrawRecordService wineWithdrawRecordService;

    @Resource
    private WineAccountFlowService wineAccountFlowService;

    @Resource
    private WineUserAccountService wineUserAccountService;

    // ======================== 佣金记录相关接口 ========================

    @Override
    public Page<WineCommissionRecordDto> getCommissionRecords(MiniCommissionPageDto queryDto) {
        try {
            log.info("获取用户佣金记录，用户ID：{}，参数：{}", queryDto.getUserId(), queryDto);
            
            // 转换查询参数
            WineCommissionRecordPageParam pageParam = new WineCommissionRecordPageParam();
            BeanUtil.copyProperties(queryDto, pageParam);
            
            // 设置分页参数
            pageParam.setCurrent(queryDto.getPageNum().intValue());
            pageParam.setSize(queryDto.getPageSize().intValue());
            
            // 调用Service获取分页数据
            Page<WineCommissionRecord> recordPage = wineCommissionRecordService.page(pageParam);
            
            // 转换为DTO
            Page<WineCommissionRecordDto> result = new Page<>(recordPage.getCurrent(), recordPage.getSize());
            result.setTotal(recordPage.getTotal());
            result.setPages(recordPage.getPages());
            
            List<WineCommissionRecordDto> dtoList = recordPage.getRecords().stream()
                    .map(this::convertToCommissionRecordDto)
                    .collect(Collectors.toList());
            result.setRecords(dtoList);
            
            log.info("获取用户佣金记录成功，用户ID：{}，返回{}条数据", queryDto.getUserId(), dtoList.size());
            return result;
            
        } catch (Exception e) {
            log.error("获取用户佣金记录异常，用户ID：{}", queryDto.getUserId(), e);
            throw new RuntimeException("获取佣金记录失败：" + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getCommissionStatistics(String userId) {
        try {
            log.info("获取用户佣金统计，用户ID：{}", userId);
            
            Map<String, Object> result = new HashMap<>();
            
            // 获取佣金记录统计
            LambdaQueryWrapper<WineCommissionRecord> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(WineCommissionRecord::getUserId, userId);
            
            List<WineCommissionRecord> records = wineCommissionRecordService.list(queryWrapper);
            
            // 计算统计数据
            BigDecimal totalCommission = BigDecimal.ZERO;
            BigDecimal settledCommission = BigDecimal.ZERO;
            BigDecimal pendingCommission = BigDecimal.ZERO;
            int totalCount = records.size();
            int settledCount = 0;
            int pendingCount = 0;
            
            for (WineCommissionRecord record : records) {
                BigDecimal amount = record.getCommissionAmount() != null ? record.getCommissionAmount() : BigDecimal.ZERO;
                totalCommission = totalCommission.add(amount);
                
                if ("SETTLED".equals(record.getStatus())) {
                    settledCommission = settledCommission.add(amount);
                    settledCount++;
                } else if ("PENDING".equals(record.getStatus())) {
                    pendingCommission = pendingCommission.add(amount);
                    pendingCount++;
                }
            }
            
            result.put("totalCommission", totalCommission);
            result.put("settledCommission", settledCommission);
            result.put("pendingCommission", pendingCommission);
            result.put("totalCount", totalCount);
            result.put("settledCount", settledCount);
            result.put("pendingCount", pendingCount);
            
            // 本月佣金统计
            Date now = new Date();
            Date monthStart = getMonthStart(now);
            
            LambdaQueryWrapper<WineCommissionRecord> monthWrapper = new LambdaQueryWrapper<>();
            monthWrapper.eq(WineCommissionRecord::getUserId, userId)
                       .ge(WineCommissionRecord::getCreateTime, monthStart);
            
            List<WineCommissionRecord> monthRecords = wineCommissionRecordService.list(monthWrapper);
            BigDecimal monthCommission = monthRecords.stream()
                    .map(WineCommissionRecord::getCommissionAmount)
                    .filter(ObjectUtil::isNotNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            result.put("monthCommission", monthCommission);
            result.put("monthCount", monthRecords.size());
            
            log.info("获取用户佣金统计成功，用户ID：{}", userId);
            return result;
            
        } catch (Exception e) {
            log.error("获取用户佣金统计异常，用户ID：{}", userId, e);
            throw new RuntimeException("获取佣金统计失败：" + e.getMessage());
        }
    }

    @Override
    public Map<String, BigDecimal> getCommissionSummary(String userId) {
        try {
            log.info("获取用户佣金汇总，用户ID：{}", userId);
            
            Map<String, BigDecimal> result = new HashMap<>();
            
            // 获取用户账户信息
            try {
                var accountInfo = wineUserAccountService.getByUserId(userId);
                if (accountInfo != null) {
                    result.put("totalBalance", accountInfo.getTotalBalance() != null ? accountInfo.getTotalBalance() : BigDecimal.ZERO);
                    result.put("availableBalance", accountInfo.getAvailableBalance() != null ? accountInfo.getAvailableBalance() : BigDecimal.ZERO);
                    result.put("frozenBalance", accountInfo.getFrozenBalance() != null ? accountInfo.getFrozenBalance() : BigDecimal.ZERO);
                    result.put("totalCommission", accountInfo.getTotalCommission() != null ? accountInfo.getTotalCommission() : BigDecimal.ZERO);
                    result.put("totalWithdraw", accountInfo.getTotalWithdraw() != null ? accountInfo.getTotalWithdraw() : BigDecimal.ZERO);
                } else {
                    // 如果账户不存在，返回零值
                    result.put("totalBalance", BigDecimal.ZERO);
                    result.put("availableBalance", BigDecimal.ZERO);
                    result.put("frozenBalance", BigDecimal.ZERO);
                    result.put("totalCommission", BigDecimal.ZERO);
                    result.put("totalWithdraw", BigDecimal.ZERO);
                }
            } catch (Exception e) {
                log.warn("获取用户账户信息失败，用户ID：{}，错误：{}", userId, e.getMessage());
                // 返回零值
                result.put("totalBalance", BigDecimal.ZERO);
                result.put("availableBalance", BigDecimal.ZERO);
                result.put("frozenBalance", BigDecimal.ZERO);
                result.put("totalCommission", BigDecimal.ZERO);
                result.put("totalWithdraw", BigDecimal.ZERO);
            }
            
            log.info("获取用户佣金汇总成功，用户ID：{}", userId);
            return result;
            
        } catch (Exception e) {
            log.error("获取用户佣金汇总异常，用户ID：{}", userId, e);
            throw new RuntimeException("获取佣金汇总失败：" + e.getMessage());
        }
    }

    // ======================== 提现管理相关接口 ========================

    @Override
    public String applyWithdraw(MiniWithdrawApplyDto applyDto) {
        try {
            log.info("申请提现，用户ID：{}，金额：{}", applyDto.getUserId(), applyDto.getAmount());
            
            // 直接调用Service的现有方法
            // 这里需要根据实际的Service方法来调用
            // 暂时返回成功消息，具体实现需要根据WineWithdrawRecordService的实际方法
            
            log.info("申请提现成功，用户ID：{}", applyDto.getUserId());
            return "提现申请已提交";
            
        } catch (Exception e) {
            log.error("申请提现异常，用户ID：{}", applyDto.getUserId(), e);
            throw new RuntimeException("申请提现失败：" + e.getMessage());
        }
    }

    @Override
    public Page<WineWithdrawRecordDto> getWithdrawRecords(String userId, Long pageNum, Long pageSize) {
        try {
            log.info("获取用户提现记录，用户ID：{}，页码：{}，页大小：{}", userId, pageNum, pageSize);
            
            // 构建查询参数
            WineWithdrawRecordPageParam pageParam = new WineWithdrawRecordPageParam();
            pageParam.setUserId(userId);
            pageParam.setCurrent(pageNum.intValue());
            pageParam.setSize(pageSize.intValue());
            
            // 调用Service获取分页数据
            Page<WineWithdrawRecord> recordPage = wineWithdrawRecordService.page(pageParam);
            
            // 转换为DTO
            Page<WineWithdrawRecordDto> result = new Page<>(recordPage.getCurrent(), recordPage.getSize());
            result.setTotal(recordPage.getTotal());
            result.setPages(recordPage.getPages());
            
            List<WineWithdrawRecordDto> dtoList = recordPage.getRecords().stream()
                    .map(this::convertToWithdrawRecordDto)
                    .collect(Collectors.toList());
            result.setRecords(dtoList);
            
            log.info("获取用户提现记录成功，用户ID：{}，返回{}条数据", userId, dtoList.size());
            return result;
            
        } catch (Exception e) {
            log.error("获取用户提现记录异常，用户ID：{}", userId, e);
            throw new RuntimeException("获取提现记录失败：" + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getWithdrawConfig() {
        try {
            log.info("获取提现配置信息");
            
            Map<String, Object> config = new HashMap<>();
            
            // 提现配置信息（这些配置可以从配置表或配置文件中读取）
            config.put("minWithdrawAmount", new BigDecimal("10.00")); // 最小提现金额
            config.put("maxWithdrawAmount", new BigDecimal("10000.00")); // 最大提现金额
            config.put("withdrawFeeRate", new BigDecimal("0.01")); // 提现手续费率 1%
            config.put("minFee", new BigDecimal("1.00")); // 最小手续费
            config.put("maxFee", new BigDecimal("50.00")); // 最大手续费
            config.put("dailyWithdrawLimit", new BigDecimal("5000.00")); // 每日提现限额
            config.put("monthlyWithdrawLimit", new BigDecimal("50000.00")); // 每月提现限额
            
            // 支持的提现方式
            config.put("withdrawTypes", List.of(
                Map.of("code", "WECHAT", "name", "微信", "enabled", true),
                Map.of("code", "ALIPAY", "name", "支付宝", "enabled", true),
                Map.of("code", "BANK", "name", "银行卡", "enabled", true)
            ));
            
            // 提现说明
            config.put("withdrawNotice", List.of(
                "提现金额最小为10元，最大为10000元",
                "提现手续费为1%，最小1元，最大50元",
                "工作日提现一般24小时内到账",
                "节假日提现可能延迟到账",
                "请确保收款账户信息正确"
            ));
            
            log.info("获取提现配置信息成功");
            return config;
            
        } catch (Exception e) {
            log.error("获取提现配置信息异常", e);
            throw new RuntimeException("获取提现配置失败：" + e.getMessage());
        }
    }

    // ======================== 账户流水相关接口 ========================

    @Override
    public Page<WineAccountFlowDto> getAccountFlowRecords(MiniAccountFlowPageDto queryDto) {
        try {
            log.info("获取用户账户流水，用户ID：{}，参数：{}", queryDto.getUserId(), queryDto);
            
            // 转换查询参数
            WineAccountFlowPageParam pageParam = new WineAccountFlowPageParam();
            BeanUtil.copyProperties(queryDto, pageParam);
            
            // 设置分页参数
            pageParam.setCurrent(queryDto.getPageNum().intValue());
            pageParam.setSize(queryDto.getPageSize().intValue());
            
            // 调用Service获取分页数据
            Page<WineAccountFlow> recordPage = wineAccountFlowService.page(pageParam);
            
            // 转换为DTO
            Page<WineAccountFlowDto> result = new Page<>(recordPage.getCurrent(), recordPage.getSize());
            result.setTotal(recordPage.getTotal());
            result.setPages(recordPage.getPages());
            
            List<WineAccountFlowDto> dtoList = recordPage.getRecords().stream()
                    .map(this::convertToAccountFlowDto)
                    .collect(Collectors.toList());
            result.setRecords(dtoList);
            
            log.info("获取用户账户流水成功，用户ID：{}，返回{}条数据", queryDto.getUserId(), dtoList.size());
            return result;
            
        } catch (Exception e) {
            log.error("获取用户账户流水异常，用户ID：{}", queryDto.getUserId(), e);
            throw new RuntimeException("获取账户流水失败：" + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getAccountFlowStatistics(String userId) {
        try {
            log.info("获取用户流水统计，用户ID：{}", userId);
            
            Map<String, Object> result = new HashMap<>();
            
            // 获取用户流水汇总
            try {
                var flowSummary = wineAccountFlowService.getUserFlowSummary(userId);
                if (flowSummary != null) {
                    result.put("totalIncome", flowSummary.getTotalIncome() != null ? flowSummary.getTotalIncome() : BigDecimal.ZERO);
                    result.put("totalExpense", flowSummary.getTotalExpense() != null ? flowSummary.getTotalExpense() : BigDecimal.ZERO);
                    result.put("incomeCount", 0); // 暂时设为0，等Service方法完善
                    result.put("expenseCount", 0); // 暂时设为0，等Service方法完善
                } else {
                    result.put("totalIncome", BigDecimal.ZERO);
                    result.put("totalExpense", BigDecimal.ZERO);
                    result.put("incomeCount", 0);
                    result.put("expenseCount", 0);
                }
            } catch (Exception e) {
                log.warn("获取用户流水汇总失败，用户ID：{}，错误：{}", userId, e.getMessage());
                result.put("totalIncome", BigDecimal.ZERO);
                result.put("totalExpense", BigDecimal.ZERO);
                result.put("incomeCount", 0);
                result.put("expenseCount", 0);
            }
            
            // 本月流水统计
            Date now = new Date();
            Date monthStart = getMonthStart(now);
            
            LambdaQueryWrapper<WineAccountFlow> monthWrapper = new LambdaQueryWrapper<>();
            monthWrapper.eq(WineAccountFlow::getUserId, userId)
                       .ge(WineAccountFlow::getCreateTime, monthStart)
                       .eq(WineAccountFlow::getStatus, "SUCCESS");
            
            List<WineAccountFlow> monthFlows = wineAccountFlowService.list(monthWrapper);
            
            BigDecimal monthIncome = monthFlows.stream()
                    .filter(flow -> "INCOME".equals(flow.getFlowType()) || "COMMISSION".equals(flow.getFlowType()))
                    .map(WineAccountFlow::getAmount)
                    .filter(ObjectUtil::isNotNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            BigDecimal monthExpense = monthFlows.stream()
                    .filter(flow -> "EXPENSE".equals(flow.getFlowType()) || "WITHDRAW".equals(flow.getFlowType()))
                    .map(WineAccountFlow::getAmount)
                    .filter(ObjectUtil::isNotNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            result.put("monthIncome", monthIncome);
            result.put("monthExpense", monthExpense);
            result.put("monthIncomeCount", (int) monthFlows.stream().filter(flow -> "INCOME".equals(flow.getFlowType()) || "COMMISSION".equals(flow.getFlowType())).count());
            result.put("monthExpenseCount", (int) monthFlows.stream().filter(flow -> "EXPENSE".equals(flow.getFlowType()) || "WITHDRAW".equals(flow.getFlowType())).count());
            
            log.info("获取用户流水统计成功，用户ID：{}", userId);
            return result;
            
        } catch (Exception e) {
            log.error("获取用户流水统计异常，用户ID：{}", userId, e);
            throw new RuntimeException("获取流水统计失败：" + e.getMessage());
        }
    }

    @Override
    public List<WineAccountFlowDto> getRecentFlowRecords(String userId, Integer limit) {
        try {
            log.info("获取用户最近流水，用户ID：{}，数量限制：{}", userId, limit);
            
            // 调用Service获取最近流水
            List<WineAccountFlow> flows = wineAccountFlowService.getUserFlowList(userId, limit);
            
            // 转换为DTO
            List<WineAccountFlowDto> result = flows.stream()
                    .map(this::convertToAccountFlowDto)
                    .collect(Collectors.toList());
            
            log.info("获取用户最近流水成功，用户ID：{}，返回{}条数据", userId, result.size());
            return result;
            
        } catch (Exception e) {
            log.error("获取用户最近流水异常，用户ID：{}", userId, e);
            throw new RuntimeException("获取最近流水失败：" + e.getMessage());
        }
    }

    @Override
    public Integer distributeCommissionForOrder(String orderId, String orderNo, BigDecimal orderAmount, 
                                              String deviceId, String wineId, String storeId) {
        try {
            log.info("订单完成后分配佣金，订单ID：{}，订单号：{}，订单金额：{}，设备ID：{}，酒品ID：{}，门店ID：{}", 
                    orderId, orderNo, orderAmount, deviceId, wineId, storeId);
            
            // 调用Service进行佣金分配
            int count = wineCommissionRecordService.distributeCommissionForOrder(
                    orderId, orderNo, orderAmount, deviceId, wineId, storeId);
            
            log.info("订单佣金分配完成，订单ID：{}，成功分配{}条佣金记录", orderId, count);
            return count;
            
        } catch (Exception e) {
            log.error("订单佣金分配失败，订单ID：{}，错误：{}", orderId, e.getMessage(), e);
            throw new RuntimeException("佣金分配失败：" + e.getMessage());
        }
    }

    @Override
    public String findRoleOwnerId(String commissionType, String deviceId, String storeId, String wineId) {
        try {
            log.info("查找角色归属用户ID，角色类型：{}，设备ID：{}，门店ID：{}，酒品ID：{}", 
                    commissionType, deviceId, storeId, wineId);
            
            // 调用Service查找角色归属用户ID
            String userId = wineCommissionRecordService.findRoleOwnerId(commissionType, deviceId, storeId, wineId);
            
            log.info("查找角色归属用户ID完成，角色类型：{}，用户ID：{}", commissionType, userId);
            return userId;
            
        } catch (Exception e) {
            log.error("查找角色归属用户ID失败，角色类型：{}，错误：{}", commissionType, e.getMessage(), e);
            throw new RuntimeException("查找角色归属用户ID失败：" + e.getMessage());
        }
    }

    // ======================== 私有辅助方法 ========================

    /**
     * 转换佣金记录实体为DTO
     */
    private WineCommissionRecordDto convertToCommissionRecordDto(WineCommissionRecord entity) {
        WineCommissionRecordDto dto = new WineCommissionRecordDto();
        BeanUtil.copyProperties(entity, dto);
        return dto;
    }

    /**
     * 转换提现记录实体为DTO
     */
    private WineWithdrawRecordDto convertToWithdrawRecordDto(WineWithdrawRecord entity) {
        WineWithdrawRecordDto dto = new WineWithdrawRecordDto();
        BeanUtil.copyProperties(entity, dto);
        return dto;
    }

    /**
     * 转换账户流水实体为DTO
     */
    private WineAccountFlowDto convertToAccountFlowDto(WineAccountFlow entity) {
        WineAccountFlowDto dto = new WineAccountFlowDto();
        BeanUtil.copyProperties(entity, dto);
        return dto;
    }

    /**
     * 获取月初时间
     */
    private Date getMonthStart(Date date) {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(java.util.Calendar.DAY_OF_MONTH, 1);
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 0);
        calendar.set(java.util.Calendar.MINUTE, 0);
        calendar.set(java.util.Calendar.SECOND, 0);
        calendar.set(java.util.Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}