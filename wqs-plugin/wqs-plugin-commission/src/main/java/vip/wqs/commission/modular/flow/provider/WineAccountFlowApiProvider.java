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
package vip.wqs.commission.modular.flow.provider;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import vip.wqs.commission.api.WineAccountFlowApi;
import vip.wqs.commission.api.dto.MiniAccountFlowPageDto;
import vip.wqs.commission.api.dto.WineAccountFlowDto;
import vip.wqs.commission.modular.flow.entity.WineAccountFlow;
import vip.wqs.commission.modular.flow.param.WineAccountFlowPageParam;
import vip.wqs.commission.modular.flow.service.WineAccountFlowService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 账户流水API实现类
 *
 * @author WQS_TEAM
 * @date 2025-01-30
 */
@Service
public class WineAccountFlowApiProvider implements WineAccountFlowApi {

    private static final Logger log = LoggerFactory.getLogger(WineAccountFlowApiProvider.class);

    @Resource
    private WineAccountFlowService wineAccountFlowService;

    @Override
    public Page<WineAccountFlowDto> getAccountFlowRecords(MiniAccountFlowPageDto queryDto) {
        try {
            log.info("获取用户账户流水，用户ID：{}，参数：{}", queryDto.getUserId(), queryDto);
            
            // 转换查询参数
            WineAccountFlowPageParam pageParam = new WineAccountFlowPageParam();
            BeanUtil.copyProperties(queryDto, pageParam);
            
            // 设置分页参数
            if (queryDto.getPageNum() != null) {
                pageParam.setCurrent(queryDto.getPageNum().intValue());
            }
            if (queryDto.getPageSize() != null) {
                pageParam.setSize(queryDto.getPageSize().intValue());
            }
            
            // 调用Service获取分页数据
            Page<WineAccountFlow> flowPage = wineAccountFlowService.page(pageParam);
            
            // 转换为DTO
            Page<WineAccountFlowDto> result = new Page<>();
            result.setCurrent(flowPage.getCurrent());
            result.setSize(flowPage.getSize());
            result.setTotal(flowPage.getTotal());
            result.setPages(flowPage.getPages());
            
            List<WineAccountFlowDto> dtoList = flowPage.getRecords().stream()
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
            var flowSummary = wineAccountFlowService.getUserFlowSummary(userId);
            if (flowSummary != null) {
                result.put("totalIncome", flowSummary.getTotalIncome() != null ? flowSummary.getTotalIncome() : BigDecimal.ZERO);
                result.put("totalExpense", flowSummary.getTotalExpense() != null ? flowSummary.getTotalExpense() : BigDecimal.ZERO);
                // 注意：WineAccountFlowSummary暂时没有count方法，使用0作为默认值
                result.put("incomeCount", 0);
                result.put("expenseCount", 0);
            } else {
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
                    .filter(WineAccountFlow::isIncomeFlow)
                    .map(WineAccountFlow::getAmount)
                    .filter(ObjectUtil::isNotNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                    
            BigDecimal monthExpense = monthFlows.stream()
                    .filter(WineAccountFlow::isExpenseFlow)
                    .map(WineAccountFlow::getAmount)
                    .filter(ObjectUtil::isNotNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            result.put("monthIncome", monthIncome);
            result.put("monthExpense", monthExpense);
            result.put("monthIncomeCount", (int) monthFlows.stream().filter(WineAccountFlow::isIncomeFlow).count());
            result.put("monthExpenseCount", (int) monthFlows.stream().filter(WineAccountFlow::isExpenseFlow).count());
            
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
    public Page<WineAccountFlowDto> getFlowRecordsByType(MiniAccountFlowPageDto queryDto) {
        try {
            log.info("根据类型获取用户流水，用户ID：{}，流水类型：{}", queryDto.getUserId(), queryDto.getFlowType());
            
            // 转换查询参数
            WineAccountFlowPageParam pageParam = new WineAccountFlowPageParam();
            BeanUtil.copyProperties(queryDto, pageParam);
            
            // 设置分页参数
            if (queryDto.getPageNum() != null) {
                pageParam.setCurrent(queryDto.getPageNum().intValue());
            }
            if (queryDto.getPageSize() != null) {
                pageParam.setSize(queryDto.getPageSize().intValue());
            }
            
            // 调用Service获取分页数据
            Page<WineAccountFlow> flowPage = wineAccountFlowService.page(pageParam);
            
            // 转换为DTO
            Page<WineAccountFlowDto> result = new Page<>();
            result.setCurrent(flowPage.getCurrent());
            result.setSize(flowPage.getSize());
            result.setTotal(flowPage.getTotal());
            result.setPages(flowPage.getPages());
            
            List<WineAccountFlowDto> dtoList = flowPage.getRecords().stream()
                    .map(this::convertToAccountFlowDto)
                    .collect(Collectors.toList());
            result.setRecords(dtoList);
            
            log.info("根据类型获取用户流水成功，用户ID：{}，返回{}条数据", queryDto.getUserId(), dtoList.size());
            return result;
            
        } catch (Exception e) {
            log.error("根据类型获取用户流水异常，用户ID：{}", queryDto.getUserId(), e);
            throw new RuntimeException("获取流水失败：" + e.getMessage());
        }
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