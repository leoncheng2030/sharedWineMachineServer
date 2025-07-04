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
package vip.wqs.commission.modular.withdraw.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 提现记录分页查询参数
 *
 * @author WQS_TEAM
 * @date 2025-01-29
 */
@Getter
@Setter
public class WineWithdrawRecordPageParam {

    /** 当前页 */
    @Schema(description = "当前页")
    private Integer current;

    /** 每页条数 */
    @Schema(description = "每页条数")
    private Integer size;

    /** 排序字段 */
    @Schema(description = "排序字段")
    private String sortField;

    /** 排序方式 */
    @Schema(description = "排序方式")
    private String sortOrder;

    /** 搜索关键字 */
    @Schema(description = "搜索关键字")
    private String searchKey;

    /** 用户ID */
    @Schema(description = "用户ID")
    private String userId;

    /** 用户昵称 */
    @Schema(description = "用户昵称")
    private String userNickname;

    /** 提现单号 */
    @Schema(description = "提现单号")
    private String withdrawNo;

    /** 提现方式 */
    @Schema(description = "提现方式")
    private String withdrawType;

    /** 提现状态 */
    @Schema(description = "提现状态")
    private String status;

    /** 审核人ID */
    @Schema(description = "审核人ID")
    private String auditorId;

    /** 创建时间开始 */
    @Schema(description = "创建时间开始")
    private Date createTimeStart;

    /** 创建时间结束 */
    @Schema(description = "创建时间结束")
    private Date createTimeEnd;

    /** 申请时间开始 */
    @Schema(description = "申请时间开始")
    private Date applyTimeStart;

    /** 申请时间结束 */
    @Schema(description = "申请时间结束")
    private Date applyTimeEnd;

    /** 审核时间开始 */
    @Schema(description = "审核时间开始")
    private Date auditTimeStart;

    /** 审核时间结束 */
    @Schema(description = "审核时间结束")
    private Date auditTimeEnd;

    /** 完成时间开始 */
    @Schema(description = "完成时间开始")
    private Date completeTimeStart;

    /** 完成时间结束 */
    @Schema(description = "完成时间结束")
    private Date completeTimeEnd;
} 