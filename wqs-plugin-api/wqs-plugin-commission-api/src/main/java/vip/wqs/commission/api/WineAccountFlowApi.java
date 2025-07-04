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
import vip.wqs.commission.api.dto.MiniAccountFlowPageDto;
import vip.wqs.commission.api.dto.WineAccountFlowDto;

import java.util.List;
import java.util.Map;

/**
 * 账户流水API接口
 *
 * @author WQS_TEAM
 * @date 2025-01-30
 */
public interface WineAccountFlowApi {

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
    List<WineAccountFlowDto> getRecentFlowRecords(String userId, Integer limit);

    /**
     * 根据流水类型获取流水记录
     *
     * @param queryDto 查询参数DTO（包含流水类型）
     * @return 流水记录分页数据
     * @author WQS_TEAM
     * @date 2025-01-30
     */
    Page<WineAccountFlowDto> getFlowRecordsByType(MiniAccountFlowPageDto queryDto);
} 