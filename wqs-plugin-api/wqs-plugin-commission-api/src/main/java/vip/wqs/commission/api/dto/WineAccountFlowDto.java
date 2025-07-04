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
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 账户流水DTO
 *
 * @author WQS_TEAM
 * @date 2025-01-30
 */
@Getter
@Setter
@Schema(description = "账户流水DTO")
public class WineAccountFlowDto {

    /** 主键ID */
    @Schema(description = "主键ID")
    private String id;

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

    /** 交易金额(元) */
    @Schema(description = "交易金额(元)")
    private BigDecimal amount;

    /** 余额变动(元) */
    @Schema(description = "余额变动(元)")
    private BigDecimal balanceChange;

    /** 关联ID */
    @Schema(description = "关联ID")
    private String relatedId;

    /** 关联类型 */
    @Schema(description = "关联类型")
    private String relatedType;

    /** 流水描述 */
    @Schema(description = "流水描述")
    private String description;

    /** 交易前余额(元) */
    @Schema(description = "交易前余额(元)")
    private BigDecimal beforeBalance;

    /** 交易后余额(元) */
    @Schema(description = "交易后余额(元)")
    private BigDecimal afterBalance;

    /** 流水状态 */
    @Schema(description = "流水状态")
    private String status;

    /** 交易时间 */
    @Schema(description = "交易时间")
    private Date transactionTime;

    /** 创建时间 */
    @Schema(description = "创建时间")
    private Date createTime;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;
} 