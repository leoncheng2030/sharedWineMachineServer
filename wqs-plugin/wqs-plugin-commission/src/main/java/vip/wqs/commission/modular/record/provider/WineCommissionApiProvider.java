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
package vip.wqs.commission.modular.record.provider;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import vip.wqs.commission.api.WineCommissionApi;
import vip.wqs.commission.api.dto.MiniCommissionPageDto;
import vip.wqs.commission.api.dto.WineCommissionRecordDto;
import vip.wqs.commission.modular.account.service.WineUserAccountService;
import vip.wqs.commission.modular.record.entity.WineCommissionRecord;
import vip.wqs.commission.modular.record.param.WineCommissionRecordPageParam;
import vip.wqs.commission.modular.record.service.WineCommissionRecordService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 佣金API实现类
 *
 * @author WQS_TEAM
 * @date 2025-01-30
 */
@Service
public class WineCommissionApiProvider implements WineCommissionApi {

    private static final Logger log = LoggerFactory.getLogger(WineCommissionApiProvider.class);

    @Resource
    private WineCommissionRecordService wineCommissionRecordService;

    @Resource
    private WineUserAccountService wineUserAccountService;

    @Override
    public Page<WineCommissionRecordDto> getCommissionRecords(MiniCommissionPageDto queryDto) {
        try {
            log.info("获取用户佣金记录，用户ID：{}，参数：{}", queryDto.getUserId(), queryDto);
            
            // 转换查询参数
            WineCommissionRecordPageParam pageParam = new WineCommissionRecordPageParam();
            BeanUtil.copyProperties(queryDto, pageParam);
            
            // 设置分页参数
            if (queryDto.getPageNum() != null) {
                pageParam.setCurrent(queryDto.getPageNum().intValue());
            }
            if (queryDto.getPageSize() != null) {
                pageParam.setSize(queryDto.getPageSize().intValue());
            }
            
            // 调用Service获取分页数据
            Page<WineCommissionRecord> recordPage = wineCommissionRecordService.page(pageParam);
            
            // 转换为DTO
            Page<WineCommissionRecordDto> result = new Page<>();
            result.setCurrent(recordPage.getCurrent());
            result.setSize(recordPage.getSize());
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

    @Override
    public Map<String, Object> getUserAccount(String userId) {
        try {
            log.info("获取用户账户信息，用户ID：{}", userId);
            
            Map<String, Object> result = new HashMap<>();
            
            // 获取用户账户信息
            try {
                var accountInfo = wineUserAccountService.getByUserId(userId);
                if (accountInfo != null) {
                    result.put("userId", accountInfo.getUserId());
                    result.put("totalBalance", accountInfo.getTotalBalance());
                    result.put("availableBalance", accountInfo.getAvailableBalance());
                    result.put("frozenBalance", accountInfo.getFrozenBalance());
                    result.put("totalCommission", accountInfo.getTotalCommission());
                    result.put("totalWithdraw", accountInfo.getTotalWithdraw());
                    result.put("accountStatus", accountInfo.getStatus());
                    result.put("createTime", accountInfo.getCreateTime());
                    result.put("updateTime", accountInfo.getUpdateTime());
                } else {
                    // 如果账户不存在，创建默认账户信息
                    result.put("userId", userId);
                    result.put("totalBalance", BigDecimal.ZERO);
                    result.put("availableBalance", BigDecimal.ZERO);
                    result.put("frozenBalance", BigDecimal.ZERO);
                    result.put("totalCommission", BigDecimal.ZERO);
                    result.put("totalWithdraw", BigDecimal.ZERO);
                    result.put("accountStatus", "INACTIVE");
                    result.put("createTime", null);
                    result.put("updateTime", null);
                }
            } catch (Exception e) {
                log.warn("获取用户账户信息失败，用户ID：{}，错误：{}", userId, e.getMessage());
                throw new RuntimeException("获取账户信息失败：" + e.getMessage());
            }
            
            log.info("获取用户账户信息成功，用户ID：{}", userId);
            return result;
            
        } catch (Exception e) {
            log.error("获取用户账户信息异常，用户ID：{}", userId, e);
            throw new RuntimeException("获取账户信息失败：" + e.getMessage());
        }
    }

    /**
     * 转换佣金记录实体为DTO
     */
    private WineCommissionRecordDto convertToCommissionRecordDto(WineCommissionRecord entity) {
        WineCommissionRecordDto dto = new WineCommissionRecordDto();
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