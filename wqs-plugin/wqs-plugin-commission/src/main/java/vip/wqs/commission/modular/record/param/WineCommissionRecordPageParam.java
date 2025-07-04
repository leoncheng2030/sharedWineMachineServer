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
package vip.wqs.commission.modular.record.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 佣金记录分页查询参数
 *
 * @author WQS_TEAM
 * @date 2025-01-29
 */
@Getter
@Setter
public class WineCommissionRecordPageParam {

    /** 当前页 */
    @Schema(description = "当前页码")
    private Integer current;

    /** 每页条数 */
    @Schema(description = "每页条数")
    private Integer size;

    /** 排序字段 */
    @Schema(description = "排序字段，字段驼峰名称，如：createTime")
    private String sortField;

    /** 排序方式 */
    @Schema(description = "排序方式，升序：ASCEND；降序：DESCEND")
    private String sortOrder;

    /** 关键词搜索（订单号、用户昵称、酒品名称） */
    @Schema(description = "关键词搜索")
    private String searchKey;

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

    /** 佣金类型 */
    @Schema(description = "佣金类型(PLATFORM:平台,DEVICE_OWNER:设备方,LOCATION_PROVIDER:场地方,STORE_MANAGER:门店管理员,SUPPLIER:供应商)")
    private String commissionType;

    /** 佣金状态 */
    @Schema(description = "佣金状态(PENDING:待计算,CALCULATED:已计算,SETTLED:已发放,FROZEN:已冻结,CANCELLED:已取消)")
    private String status;

    /** 最小订单金额 */
    @Schema(description = "最小订单金额")
    private BigDecimal minOrderAmount;

    /** 最大订单金额 */
    @Schema(description = "最大订单金额")
    private BigDecimal maxOrderAmount;

    /** 最小佣金金额 */
    @Schema(description = "最小佣金金额")
    private BigDecimal minCommissionAmount;

    /** 最大佣金金额 */
    @Schema(description = "最大佣金金额")
    private BigDecimal maxCommissionAmount;

    /** 计算时间开始 */
    @Schema(description = "计算时间开始")
    private Date calculateTimeStart;

    /** 计算时间结束 */
    @Schema(description = "计算时间结束")
    private Date calculateTimeEnd;

    /** 发放时间开始 */
    @Schema(description = "发放时间开始")
    private Date settleTimeStart;

    /** 发放时间结束 */
    @Schema(description = "发放时间结束")
    private Date settleTimeEnd;

    /** 创建时间开始 */
    @Schema(description = "创建时间开始")
    private Date createTimeStart;

    /** 创建时间结束 */
    @Schema(description = "创建时间结束")
    private Date createTimeEnd;
} 