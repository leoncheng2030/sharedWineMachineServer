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
import vip.wqs.commission.api.dto.MiniCommissionPageDto;
import vip.wqs.commission.api.dto.WineCommissionRecordDto;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 佣金管理API接口
 *
 * @author WQS_TEAM
 * @date 2025-01-30
 */
public interface WineCommissionApi {

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

    /**
     * 获取用户账户信息
     *
     * @param userId 用户ID
     * @return 账户信息
     * @author WQS_TEAM
     * @date 2025-01-30
     */
    Map<String, Object> getUserAccount(String userId);
} 