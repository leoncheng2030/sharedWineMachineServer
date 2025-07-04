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
package vip.wqs.commission.modular.withdraw.provider;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import vip.wqs.commission.api.WineWithdrawApi;
import vip.wqs.commission.api.dto.MiniWithdrawApplyDto;
import vip.wqs.commission.api.dto.WineWithdrawRecordDto;
import vip.wqs.commission.modular.withdraw.entity.WineWithdrawRecord;
import vip.wqs.commission.modular.withdraw.param.WineWithdrawRecordPageParam;
import vip.wqs.commission.modular.withdraw.service.WineWithdrawRecordService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 提现API实现类
 *
 * @author WQS_TEAM
 * @date 2025-01-30
 */
@Service
public class WineWithdrawApiProvider implements WineWithdrawApi {

    private static final Logger log = LoggerFactory.getLogger(WineWithdrawApiProvider.class);

    @Resource
    private WineWithdrawRecordService wineWithdrawRecordService;

    @Override
    public String applyWithdraw(MiniWithdrawApplyDto applyDto) {
        try {
            log.info("用户申请提现，用户ID：{}，申请金额：{}", applyDto.getUserId(), applyDto.getAmount());
            
            // 构建账户信息字符串
            String accountInfo = buildAccountInfo(applyDto);
            
            // 调用Service处理提现申请
            String withdrawId = wineWithdrawRecordService.applyWithdraw(
                applyDto.getUserId(),
                applyDto.getAmount(),
                applyDto.getWithdrawType(),
                accountInfo,
                applyDto.getAccountName(),
                applyDto.getRemark()
            );
            
            log.info("用户提现申请成功，用户ID：{}，提现ID：{}", applyDto.getUserId(), withdrawId);
            return withdrawId;
            
        } catch (Exception e) {
            log.error("用户提现申请异常，用户ID：{}", applyDto.getUserId(), e);
            throw new RuntimeException("提现申请失败：" + e.getMessage());
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
            Page<WineWithdrawRecordDto> result = new Page<>();
            result.setCurrent(recordPage.getCurrent());
            result.setSize(recordPage.getSize());
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
            
            Map<String, Object> result = new HashMap<>();
            
            // 获取提现配置（这里可以从配置表或配置文件中获取）
            result.put("minWithdrawAmount", new BigDecimal("10.00")); // 最小提现金额
            result.put("maxWithdrawAmount", new BigDecimal("10000.00")); // 最大提现金额
            result.put("withdrawFeeRate", new BigDecimal("0.01")); // 提现手续费率
            result.put("minWithdrawFee", new BigDecimal("1.00")); // 最小手续费
            result.put("maxWithdrawFee", new BigDecimal("50.00")); // 最大手续费
            result.put("withdrawTypes", List.of("ALIPAY", "WECHAT", "BANK_CARD")); // 支持的提现方式
            result.put("workingHours", "09:00-18:00"); // 工作时间
            result.put("processingTime", "1-3个工作日"); // 处理时间
            result.put("dailyWithdrawLimit", new BigDecimal("5000.00")); // 每日提现限额
            result.put("monthlyWithdrawLimit", new BigDecimal("50000.00")); // 每月提现限额
            
            log.info("获取提现配置信息成功");
            return result;
            
        } catch (Exception e) {
            log.error("获取提现配置信息异常", e);
            throw new RuntimeException("获取提现配置失败：" + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getWithdrawStatistics(String userId) {
        try {
            log.info("获取用户提现统计信息，用户ID：{}", userId);
            
            Map<String, Object> result = new HashMap<>();
            
            // 使用Service中的现有方法获取用户提现汇总信息
            try {
                var withdrawSummary = wineWithdrawRecordService.getUserWithdrawSummary(userId);
                if (ObjectUtil.isNotNull(withdrawSummary)) {
                    result.put("totalWithdrawAmount", withdrawSummary.getTotalWithdrawAmount()); // 总提现金额
                    result.put("totalWithdrawCount", withdrawSummary.getWithdrawCount()); // 总提现次数
                    result.put("successWithdrawAmount", withdrawSummary.getTotalActualAmount()); // 成功提现金额
                    result.put("successWithdrawCount", withdrawSummary.getSuccessCount()); // 成功提现次数
                    result.put("pendingWithdrawAmount", BigDecimal.ZERO); // 待审核提现金额（需要单独计算）
                    result.put("pendingWithdrawCount", withdrawSummary.getPendingCount()); // 待审核提现次数
                    result.put("failedWithdrawAmount", BigDecimal.ZERO); // 失败提现金额（需要单独计算）
                    result.put("failedWithdrawCount", withdrawSummary.getFailedCount()); // 失败提现次数
                } else {
                    // 如果没有统计数据，返回默认值
                    result.put("totalWithdrawAmount", BigDecimal.ZERO); // 总提现金额
                    result.put("totalWithdrawCount", 0L); // 总提现次数
                    result.put("successWithdrawAmount", BigDecimal.ZERO); // 成功提现金额
                    result.put("successWithdrawCount", 0L); // 成功提现次数
                    result.put("pendingWithdrawAmount", BigDecimal.ZERO); // 待审核提现金额
                    result.put("pendingWithdrawCount", 0L); // 待审核提现次数
                    result.put("failedWithdrawAmount", BigDecimal.ZERO); // 失败提现金额
                    result.put("failedWithdrawCount", 0L); // 失败提现次数
                }
            } catch (Exception e) {
                log.warn("获取用户提现汇总信息失败，返回默认值，用户ID：{}", userId, e);
                // 返回默认值
                result.put("totalWithdrawAmount", BigDecimal.ZERO);
                result.put("totalWithdrawCount", 0L);
                result.put("successWithdrawAmount", BigDecimal.ZERO);
                result.put("successWithdrawCount", 0L);
                result.put("pendingWithdrawAmount", BigDecimal.ZERO);
                result.put("pendingWithdrawCount", 0L);
                result.put("failedWithdrawAmount", BigDecimal.ZERO);
                result.put("failedWithdrawCount", 0L);
            }
            
            // 添加今日和本月统计（简化实现）
            result.put("todayWithdrawAmount", BigDecimal.ZERO); // 今日提现金额
            result.put("todayWithdrawCount", 0L); // 今日提现次数
            result.put("thisMonthWithdrawAmount", BigDecimal.ZERO); // 本月提现金额
            result.put("thisMonthWithdrawCount", 0L); // 本月提现次数
            
            log.info("获取用户提现统计信息成功，用户ID：{}", userId);
            return result;
            
        } catch (Exception e) {
            log.error("获取用户提现统计信息异常，用户ID：{}", userId, e);
            throw new RuntimeException("获取提现统计失败：" + e.getMessage());
        }
    }

    /**
     * 构建账户信息字符串
     */
    private String buildAccountInfo(MiniWithdrawApplyDto applyDto) {
        StringBuilder accountInfo = new StringBuilder();
        
        // 根据提现方式构建不同的账户信息格式
        switch (applyDto.getWithdrawType()) {
            case "WECHAT":
                accountInfo.append("微信号：").append(applyDto.getAccountNumber());
                if (ObjectUtil.isNotEmpty(applyDto.getAccountName())) {
                    accountInfo.append("，姓名：").append(applyDto.getAccountName());
                }
                break;
            case "ALIPAY":
                accountInfo.append("支付宝账号：").append(applyDto.getAccountNumber());
                if (ObjectUtil.isNotEmpty(applyDto.getAccountName())) {
                    accountInfo.append("，姓名：").append(applyDto.getAccountName());
                }
                break;
            case "BANK":
                accountInfo.append("银行卡号：").append(applyDto.getAccountNumber());
                if (ObjectUtil.isNotEmpty(applyDto.getAccountName())) {
                    accountInfo.append("，户名：").append(applyDto.getAccountName());
                }
                if (ObjectUtil.isNotEmpty(applyDto.getBankName())) {
                    accountInfo.append("，开户行：").append(applyDto.getBankName());
                }
                if (ObjectUtil.isNotEmpty(applyDto.getBankBranch())) {
                    accountInfo.append("，支行：").append(applyDto.getBankBranch());
                }
                break;
            default:
                accountInfo.append("账号：").append(applyDto.getAccountNumber());
                if (ObjectUtil.isNotEmpty(applyDto.getAccountName())) {
                    accountInfo.append("，姓名：").append(applyDto.getAccountName());
                }
                break;
        }
        
        return accountInfo.toString();
    }

    /**
     * 转换提现记录实体为DTO
     */
    private WineWithdrawRecordDto convertToWithdrawRecordDto(WineWithdrawRecord entity) {
        WineWithdrawRecordDto dto = new WineWithdrawRecordDto();
        BeanUtil.copyProperties(entity, dto);
        return dto;
    }
} 