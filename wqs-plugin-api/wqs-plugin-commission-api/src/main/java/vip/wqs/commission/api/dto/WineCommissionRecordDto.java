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
 * 佣金记录DTO
 *
 * @author WQS_TEAM
 * @date 2025-01-30
 */
@Getter
@Setter
@Schema(description = "佣金记录DTO")
public class WineCommissionRecordDto {

    /** 主键ID */
    @Schema(description = "主键ID")
    private String id;

    /** 订单ID */
    @Schema(description = "订单ID")
    private String orderId;

    /** 订单号 */
    @Schema(description = "订单号")
    private String orderNo;

    /** 受益用户ID */
    @Schema(description = "受益用户ID")
    private String userId;

    /** 用户昵称 */
    @Schema(description = "用户昵称")
    private String userNickname;

    /** 设备ID */
    @Schema(description = "设备ID")
    private String deviceId;

    /** 设备编码 */
    @Schema(description = "设备编码")
    private String deviceCode;

    /** 酒品ID */
    @Schema(description = "酒品ID")
    private String wineId;

    /** 酒品名称 */
    @Schema(description = "酒品名称")
    private String wineName;

    /** 订单金额(元) */
    @Schema(description = "订单金额(元)")
    private BigDecimal orderAmount;

    /** 佣金类型 */
    @Schema(description = "佣金类型")
    private String commissionType;

    /** 佣金比例(%) */
    @Schema(description = "佣金比例(%)")
    private BigDecimal commissionRate;

    /** 佣金金额(元) */
    @Schema(description = "佣金金额(元)")
    private BigDecimal commissionAmount;

    /** 佣金状态 */
    @Schema(description = "佣金状态")
    private String status;

    /** 计算时间 */
    @Schema(description = "计算时间")
    private Date calculateTime;

    /** 结算时间 */
    @Schema(description = "结算时间")
    private Date settleTime;

    /** 创建时间 */
    @Schema(description = "创建时间")
    private Date createTime;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;
} 