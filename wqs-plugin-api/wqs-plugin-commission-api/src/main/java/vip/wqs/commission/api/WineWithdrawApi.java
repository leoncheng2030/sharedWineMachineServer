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
import vip.wqs.commission.api.dto.MiniWithdrawApplyDto;
import vip.wqs.commission.api.dto.WineWithdrawRecordDto;

import java.util.Map;

/**
 * 提现管理API接口
 *
 * @author WQS_TEAM
 * @date 2025-01-30
 */
public interface WineWithdrawApi {

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

    /**
     * 获取用户提现统计信息
     *
     * @param userId 用户ID
     * @return 提现统计信息
     * @author WQS_TEAM
     * @date 2025-01-30
     */
    Map<String, Object> getWithdrawStatistics(String userId);
} 