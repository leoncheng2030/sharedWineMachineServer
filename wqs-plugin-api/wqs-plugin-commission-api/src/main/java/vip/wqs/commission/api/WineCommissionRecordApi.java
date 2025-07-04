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
package vip.wqs.commission.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import vip.wqs.commission.api.dto.*;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 小程序佣金管理API接口
 *
 * @author WQS_TEAM
 * @date 2025-01-30
 */
public interface WineCommissionRecordApi {

    // ======================== 佣金记录相关接口 ========================

    /**
     * 获取用户佣金记录分页列表
     *
     * @param queryDto 查询参数DTO
     * @return 佣金记录分页数据
     * @author WQS_TEAM
     * @date 2025-01-30
     */
    Page<WineCommissionRecordDto> getCommissionRecords(MiniCommissionPageDto queryDto);

    /**
     * 获取用户佣金统计信息
     *
     * @param userId 用户ID
     * @return 佣金统计信息
     * @author WQS_TEAM
     * @date 2025-01-30
     */
    Map<String, Object> getCommissionStatistics(String userId);

    /**
     * 获取用户佣金汇总信息
     *
     * @param userId 用户ID
     * @return 佣金汇总信息
     * @author WQS_TEAM
     * @date 2025-01-30
     */
    Map<String, BigDecimal> getCommissionSummary(String userId);

    // ======================== 提现管理相关接口 ========================

    /**
     * 申请提现
     *
     * @param applyDto 提现申请DTO
     * @return 提现记录ID
     * @author WQS_TEAM
     * @date 2025-01-30
     */
    String applyWithdraw(MiniWithdrawApplyDto applyDto);

    /**
     * 获取用户提现记录分页列表
     *
     * @param userId 用户ID
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 提现记录分页数据
     * @author WQS_TEAM
     * @date 2025-01-30
     */
    Page<WineWithdrawRecordDto> getWithdrawRecords(String userId, Long pageNum, Long pageSize);

    /**
     * 获取提现配置信息
     *
     * @return 提现配置信息
     * @author WQS_TEAM
     * @date 2025-01-30
     */
    Map<String, Object> getWithdrawConfig();

    // ======================== 账户流水相关接口 ========================

    /**
     * 获取用户账户流水分页列表
     *
     * @param queryDto 查询参数DTO
     * @return 账户流水分页数据
     * @author WQS_TEAM
     * @date 2025-01-30
     */
    Page<WineAccountFlowDto> getAccountFlowRecords(MiniAccountFlowPageDto queryDto);

    /**
     * 获取用户账户流水统计信息
     *
     * @param userId 用户ID
     * @return 流水统计信息
     * @author WQS_TEAM
     * @date 2025-01-30
     */
    Map<String, Object> getAccountFlowStatistics(String userId);

    /**
     * 获取用户最近流水记录
     *
     * @param userId 用户ID
     * @param limit 记录数量限制
     * @return 最近流水记录列表
     * @author WQS_TEAM
     * @date 2025-01-30
     */
    java.util.List<WineAccountFlowDto> getRecentFlowRecords(String userId, Integer limit);

    /**
     * 订单完成后分配佣金（核心业务API）
     * 
     * @param orderId 订单ID
     * @param orderNo 订单号
     * @param orderAmount 订单金额
     * @param deviceId 设备ID
     * @param wineId 酒品ID
     * @param storeId 门店ID（可选）
     * @return 分配成功的佣金记录数量
     */
    Integer distributeCommissionForOrder(String orderId, String orderNo, java.math.BigDecimal orderAmount, 
                                       String deviceId, String wineId, String storeId);

    /**
     * 查找角色归属用户ID
     * 
     * @param commissionType 佣金类型（PLATFORM/DEVICE_OWNER/LOCATION_PROVIDER/STORE_MANAGER/SUPPLIER）
     * @param deviceId 设备ID
     * @param storeId 门店ID
     * @param wineId 酒品ID
     * @return 归属用户ID，找不到返回null
     */
    String findRoleOwnerId(String commissionType, String deviceId, String storeId, String wineId);
} 