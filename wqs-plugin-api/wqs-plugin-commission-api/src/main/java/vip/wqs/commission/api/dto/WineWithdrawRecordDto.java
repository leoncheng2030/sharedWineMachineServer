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
 * 提现记录DTO
 *
 * @author WQS_TEAM
 * @date 2025-01-30
 */
@Getter
@Setter
@Schema(description = "提现记录DTO")
public class WineWithdrawRecordDto {

    /** 主键ID */
    @Schema(description = "主键ID")
    private String id;

    /** 用户ID */
    @Schema(description = "用户ID")
    private String userId;

    /** 用户昵称 */
    @Schema(description = "用户昵称")
    private String userNickname;

    /** 提现单号 */
    @Schema(description = "提现单号")
    private String withdrawNo;

    /** 提现金额(元) */
    @Schema(description = "提现金额(元)")
    private BigDecimal withdrawAmount;

    /** 手续费(元) */
    @Schema(description = "手续费(元)")
    private BigDecimal fee;

    /** 实际到账金额(元) */
    @Schema(description = "实际到账金额(元)")
    private BigDecimal actualAmount;

    /** 提现方式 */
    @Schema(description = "提现方式")
    private String withdrawType;

    /** 收款账户信息 */
    @Schema(description = "收款账户信息")
    private String accountInfo;

    /** 收款人姓名 */
    @Schema(description = "收款人姓名")
    private String accountName;

    /** 提现状态 */
    @Schema(description = "提现状态")
    private String status;

    /** 申请时间 */
    @Schema(description = "申请时间")
    private Date applyTime;

    /** 审核时间 */
    @Schema(description = "审核时间")
    private Date auditTime;

    /** 处理时间 */
    @Schema(description = "处理时间")
    private Date processTime;

    /** 创建时间 */
    @Schema(description = "创建时间")
    private Date createTime;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;
} 