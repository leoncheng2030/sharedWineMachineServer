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
package vip.wqs.commission.modular.flow.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 账户流水分页查询参数
 *
 * @author WQS_TEAM
 * @date 2025-01-29
 */
@Getter
@Setter
public class WineAccountFlowPageParam {

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

    /** 流水号 */
    @Schema(description = "流水号")
    private String flowNo;

    /** 流水类型 */
    @Schema(description = "流水类型")
    private String flowType;

    /** 关联ID */
    @Schema(description = "关联ID")
    private String relatedId;

    /** 关联类型 */
    @Schema(description = "关联类型")
    private String relatedType;

    /** 流水状态 */
    @Schema(description = "流水状态")
    private String status;

    /** 创建时间开始 */
    @Schema(description = "创建时间开始")
    private Date createTimeStart;

    /** 创建时间结束 */
    @Schema(description = "创建时间结束")
    private Date createTimeEnd;

    /** 交易时间开始 */
    @Schema(description = "交易时间开始")
    private Date transactionTimeStart;

    /** 交易时间结束 */
    @Schema(description = "交易时间结束")
    private Date transactionTimeEnd;
} 