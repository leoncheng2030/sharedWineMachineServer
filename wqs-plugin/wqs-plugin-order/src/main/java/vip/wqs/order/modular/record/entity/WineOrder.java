/*
 * Copyright [2022] [https://www.wqs.vip]
 *
 * WQS采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改WQS源码头部的版权声明。
 * 3.本项目代码可免费商业使用，商业使用请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://www.wqs.vip
 * 5.不可二次分发开源参与同类竞品，如有想法可联系团队wqs@wqs.vip商议合作。
 * 6.若您的项目无法满足以上几点，需要更多功能代码，获取WQS商业授权许可，请在官网购买授权，地址为 https://www.wqs.vip
 */
package vip.wqs.order.modular.record.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.constant.TransType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import vip.wqs.common.pojo.CommonEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单实体
 *
 * @author wqs
 * @date 2025/01/30 15:45
 **/
@Getter
@Setter
@TableName(value = "WINE_ORDER", autoResultMap = true)
public class WineOrder extends CommonEntity {

    /** 主键 */
    @TableId
    @Schema(description = "主键")
    private String id;

    /** 订单号 */
    @Schema(description = "订单号")
    private String orderNo;

    /** 用户ID */
    @Schema(description = "用户ID")
    private String userId;

    /** 设备ID */
    @Schema(description = "设备ID")
    private String deviceId;

    /** 酒品ID */
    @Schema(description = "酒品ID")
    private String wineId;

    /** 酒品名称 */
    @Schema(description = "酒品名称")
    private String wineName;

    /** 出酒量(ml) */
    @Schema(description = "出酒量(ml)")
    private Integer amount;

    /** 单价(元/ml) */
    @Schema(description = "单价(元/ml)")
    private BigDecimal unitPrice;

    /** 总金额 */
    @Schema(description = "总金额")
    private BigDecimal totalAmount;

    /** 服务费 */
    @Schema(description = "服务费")
    @TableField(insertStrategy = FieldStrategy.ALWAYS, updateStrategy = FieldStrategy.ALWAYS)
    private BigDecimal serviceFee;

    /** 订单状态 */
    @Schema(description = "订单状态")
    @TableField(insertStrategy = FieldStrategy.ALWAYS, updateStrategy = FieldStrategy.ALWAYS)
    @Trans(type = TransType.DICTIONARY, key = "ORDER_STATUS")
    private String status;

    /** 支付时间 */
    @Schema(description = "支付时间")
    @TableField(insertStrategy = FieldStrategy.ALWAYS, updateStrategy = FieldStrategy.ALWAYS)
    private Date payTime;

    /** 开始出酒时间 */
    @Schema(description = "开始出酒时间")
    @TableField(insertStrategy = FieldStrategy.ALWAYS, updateStrategy = FieldStrategy.ALWAYS)
    private Date dispenseStartTime;

    /** 出酒完成时间 */
    @Schema(description = "出酒完成时间")
    @TableField(insertStrategy = FieldStrategy.ALWAYS, updateStrategy = FieldStrategy.ALWAYS)
    private Date dispenseEndTime;

    /** 取消时间 */
    @Schema(description = "取消时间")
    @TableField(insertStrategy = FieldStrategy.ALWAYS, updateStrategy = FieldStrategy.ALWAYS)
    private Date cancelTime;

    /** 取消原因 */
    @Schema(description = "取消原因")
    @TableField(insertStrategy = FieldStrategy.ALWAYS, updateStrategy = FieldStrategy.ALWAYS)
    private String cancelReason;

    /** 退款时间 */
    @Schema(description = "退款时间")
    @TableField(insertStrategy = FieldStrategy.ALWAYS, updateStrategy = FieldStrategy.ALWAYS)
    private Date refundTime;

    /** 退款金额 */
    @Schema(description = "退款金额")
    @TableField(insertStrategy = FieldStrategy.ALWAYS, updateStrategy = FieldStrategy.ALWAYS)
    private BigDecimal refundAmount;

    /** 备注 */
    @Schema(description = "备注")
    @TableField(insertStrategy = FieldStrategy.ALWAYS, updateStrategy = FieldStrategy.ALWAYS)
    private String remark;

    /** 扩展信息 */
    @Schema(description = "扩展信息")
    @TableField(insertStrategy = FieldStrategy.ALWAYS, updateStrategy = FieldStrategy.ALWAYS)
    private String extJson;

    /** 用户名称（关联查询字段） */
    @Schema(description = "用户名称")
    @TableField(exist = false)
    private String userName;

    /** 设备名称（关联查询字段） */
    @Schema(description = "设备名称")
    @TableField(exist = false)
    private String deviceName;

    /** 酒品名称（翻译字段） */
    @Schema(description = "酒品名称翻译")
    @TableField(exist = false)
    private String wineNameTrans;
} 