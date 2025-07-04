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
package vip.wqs.commission.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 小程序账户流水分页查询DTO
 *
 * @author WQS_TEAM
 * @date 2025-01-30
 */
@Getter
@Setter
@Schema(description = "小程序账户流水分页查询DTO")
public class MiniAccountFlowPageDto {

    /** 当前页码 */
    @Schema(description = "当前页码", example = "1")
    @NotNull(message = "页码不能为空")
    @Min(value = 1, message = "页码不能小于1")
    private Long pageNum = 1L;

    /** 每页数量 */
    @Schema(description = "每页数量", example = "10")
    @NotNull(message = "每页数量不能为空")
    @Min(value = 1, message = "每页数量不能小于1")
    @Max(value = 100, message = "每页数量不能超过100")
    private Long pageSize = 10L;

    /** 用户ID（由系统自动设置，无需前端传递） */
    @Schema(description = "用户ID", hidden = true)
    private String userId;

    /** 流水类型 */
    @Schema(description = "流水类型(COMMISSION:佣金收入,WITHDRAW:提现支出,REFUND:退款,TRANSFER:转账,ADJUST:余额调整)", example = "COMMISSION")
    private String flowType;

    /** 流水状态 */
    @Schema(description = "流水状态(SUCCESS:成功,FAILED:失败,PENDING:处理中)", example = "SUCCESS")
    private String status;

    /** 开始时间 */
    @Schema(description = "开始时间")
    private Date startTime;

    /** 结束时间 */
    @Schema(description = "结束时间")
    private Date endTime;

    /** 排序字段 */
    @Schema(description = "排序字段(createTime:创建时间,transactionTime:交易时间,amount:金额)", example = "createTime")
    private String sortField = "createTime";

    /** 排序方式 */
    @Schema(description = "排序方式(ASC:升序,DESC:降序)", example = "DESC")
    private String sortOrder = "DESC";
} 