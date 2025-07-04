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
package vip.wqs.miniprogram.modular.flow.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vip.wqs.auth.core.annotation.SaClientCheckLogin;
import vip.wqs.auth.core.util.StpClientUtil;
import vip.wqs.commission.api.WineCommissionRecordApi;
import vip.wqs.commission.api.dto.MiniAccountFlowPageDto;
import vip.wqs.commission.api.dto.WineAccountFlowDto;
import vip.wqs.common.annotation.CommonLog;
import vip.wqs.common.pojo.CommonResult;

import java.util.List;
import java.util.Map;

/**
 * 小程序账户流水控制器
 * 
 * @author WQS_TEAM
 * @date 2025-01-30
 */
@Tag(name = "小程序账户流水控制器")
@ApiSupport(author = "WQS_TEAM", order = 9)
@RestController
@Validated
public class MiniAccountFlowController {

    private static final Logger log = LoggerFactory.getLogger(MiniAccountFlowController.class);

    @Resource
    private WineCommissionRecordApi wineCommissionRecordApi;

    /**
     * 获取用户账户流水分页列表
     * 注意：需要登录验证，获取当前用户的账户流水记录
     *
     * @author WQS_TEAM
     * @date 2025-01-30
     */
    @ApiOperationSupport(order = 1)
    @Operation(summary = "获取用户账户流水分页列表")
    @CommonLog("小程序获取用户账户流水")
    @SaClientCheckLogin
    @GetMapping("/miniprogram/account/flow/records")
    public CommonResult<Page<WineAccountFlowDto>> getAccountFlowRecords(@Valid MiniAccountFlowPageDto param) {
        try {
            // 获取当前登录用户ID
            String userId = StpClientUtil.getLoginIdAsString();
            param.setUserId(userId);
            
            log.info("小程序获取用户账户流水，用户ID：{}，参数：{}", userId, param);
            
            // 调用佣金API获取流水分页数据
            Page<WineAccountFlowDto> result = wineCommissionRecordApi.getAccountFlowRecords(param);
            
            if (result == null) {
                return CommonResult.error("获取账户流水失败");
            }
            
            log.info("小程序获取用户账户流水成功，用户ID：{}，返回{}条数据", userId, result.getRecords().size());
            return CommonResult.data(result);
            
        } catch (Exception e) {
            log.error("获取用户账户流水异常", e);
            return CommonResult.error("获取账户流水失败：" + e.getMessage());
        }
    }

    /**
     * 获取用户账户流水统计信息
     * 注意：需要登录验证，获取当前用户的流水统计
     *
     * @author WQS_TEAM
     * @date 2025-01-30
     */
    @ApiOperationSupport(order = 2)
    @Operation(summary = "获取用户账户流水统计信息")
    @CommonLog("小程序获取用户流水统计")
    @SaClientCheckLogin
    @GetMapping("/miniprogram/account/flow/statistics")
    public CommonResult<Map<String, Object>> getAccountFlowStatistics() {
        try {
            // 获取当前登录用户ID
            String userId = StpClientUtil.getLoginIdAsString();
            
            log.info("小程序获取用户流水统计，用户ID：{}", userId);
            
            // 调用佣金API获取流水统计数据
            Map<String, Object> result = wineCommissionRecordApi.getAccountFlowStatistics(userId);
            
            if (result == null) {
                return CommonResult.error("获取流水统计失败");
            }
            
            log.info("小程序获取用户流水统计成功，用户ID：{}", userId);
            return CommonResult.data(result);
            
        } catch (Exception e) {
            log.error("获取用户流水统计异常", e);
            return CommonResult.error("获取流水统计失败：" + e.getMessage());
        }
    }

    /**
     * 获取用户最近流水记录
     * 注意：需要登录验证，获取当前用户的最近流水
     *
     * @author WQS_TEAM
     * @date 2025-01-30
     */
    @ApiOperationSupport(order = 3)
    @Operation(summary = "获取用户最近流水记录")
    @CommonLog("小程序获取用户最近流水")
    @SaClientCheckLogin
    @GetMapping("/miniprogram/account/flow/recent")
    public CommonResult<List<WineAccountFlowDto>> getRecentFlowRecords(
            @RequestParam(defaultValue = "10") Integer limit) {
        try {
            // 获取当前登录用户ID
            String userId = StpClientUtil.getLoginIdAsString();
            
            log.info("小程序获取用户最近流水，用户ID：{}，数量限制：{}", userId, limit);
            
            // 调用佣金API获取最近流水记录
            List<WineAccountFlowDto> result = wineCommissionRecordApi.getRecentFlowRecords(userId, limit);
            
            if (result == null) {
                return CommonResult.error("获取最近流水失败");
            }
            
            log.info("小程序获取用户最近流水成功，用户ID：{}，返回{}条数据", userId, result.size());
            return CommonResult.data(result);
            
        } catch (Exception e) {
            log.error("获取用户最近流水异常", e);
            return CommonResult.error("获取最近流水失败：" + e.getMessage());
        }
    }

    /**
     * 根据流水类型获取流水记录
     * 注意：需要登录验证，获取当前用户指定类型的流水
     *
     * @author WQS_TEAM
     * @date 2025-01-30
     */
    @ApiOperationSupport(order = 4)
    @Operation(summary = "根据流水类型获取流水记录")
    @CommonLog("小程序根据类型获取流水")
    @SaClientCheckLogin
    @GetMapping("/miniprogram/account/flow/type/{flowType}")
    public CommonResult<Page<WineAccountFlowDto>> getFlowRecordsByType(
            @PathVariable String flowType,
            @RequestParam(defaultValue = "1") Long pageNum,
            @RequestParam(defaultValue = "10") Long pageSize) {
        try {
            // 获取当前登录用户ID
            String userId = StpClientUtil.getLoginIdAsString();
            
            log.info("小程序根据类型获取流水，用户ID：{}，流水类型：{}，页码：{}，页大小：{}", userId, flowType, pageNum, pageSize);
            
            // 构建查询参数
            MiniAccountFlowPageDto param = new MiniAccountFlowPageDto();
            param.setUserId(userId);
            param.setFlowType(flowType);
            param.setPageNum(pageNum);
            param.setPageSize(pageSize);
            
            // 调用佣金API获取流水分页数据
            Page<WineAccountFlowDto> result = wineCommissionRecordApi.getAccountFlowRecords(param);
            
            if (result == null) {
                return CommonResult.error("获取流水记录失败");
            }
            
            log.info("小程序根据类型获取流水成功，用户ID：{}，流水类型：{}，返回{}条数据", userId, flowType, result.getRecords().size());
            return CommonResult.data(result);
            
        } catch (Exception e) {
            log.error("根据类型获取流水异常", e);
            return CommonResult.error("获取流水记录失败：" + e.getMessage());
        }
    }
} 