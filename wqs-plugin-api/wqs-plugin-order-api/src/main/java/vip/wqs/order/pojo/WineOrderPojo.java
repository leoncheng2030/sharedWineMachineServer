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
package vip.wqs.order.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单响应数据
 *
 * @author wqs
 * @date 2025/01/30
 */
@Getter
@Setter
@Schema(description = "订单响应数据")
public class WineOrderPojo implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 订单ID */
    @Schema(description = "订单ID")
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

    /** 设备名称 */
    @Schema(description = "设备名称")
    private String deviceName;

    /** 设备编码 */
    @Schema(description = "设备编码")
    private String deviceCode;

    /** 设备位置描述 */
    @Schema(description = "设备位置描述")
    private String deviceLocation;

    /** 酒品ID */
    @Schema(description = "酒品ID")
    private String wineId;

    /** 酒品名称 */
    @Schema(description = "酒品名称")
    private String wineName;

    /** 酒品图片 */
    @Schema(description = "酒品图片")
    private String wineImage;

    /** 酒品规格 */
    @Schema(description = "酒品规格")
    private String wineSpec;

    /** 酒精度数 */
    @Schema(description = "酒精度数")
    private BigDecimal alcoholDegree;

    /** 出酒量(ml) */
    @Schema(description = "出酒量(ml)")
    private Integer amount;

    /** 单价(元/ml) */
    @Schema(description = "单价(元/ml)")
    private BigDecimal unitPrice;

    /** 总金额(元) */
    @Schema(description = "总金额(元)")
    private BigDecimal totalAmount;

    /** 服务费(元) */
    @Schema(description = "服务费(元)")
    private BigDecimal serviceFee;

    /** 实际支付金额(元) */
    @Schema(description = "实际支付金额(元)")
    private BigDecimal actualAmount;

    /** 优惠金额(元) */
    @Schema(description = "优惠金额(元)")
    private BigDecimal discountAmount;

    /** 订单状态 */
    @Schema(description = "订单状态")
    private String status;

    /** 订单状态显示文本 */
    @Schema(description = "订单状态显示文本")
    private String statusText;

    /** 支付时间 */
    @Schema(description = "支付时间")
    private Date payTime;

    /** 开始出酒时间 */
    @Schema(description = "开始出酒时间")
    private Date dispenseStartTime;

    /** 出酒完成时间 */
    @Schema(description = "出酒完成时间")
    private Date dispenseEndTime;

    /** 取消时间 */
    @Schema(description = "取消时间")
    private Date cancelTime;

    /** 取消原因 */
    @Schema(description = "取消原因")
    private String cancelReason;

    /** 退款时间 */
    @Schema(description = "退款时间")
    private Date refundTime;

    /** 退款金额(元) */
    @Schema(description = "退款金额(元)")
    private BigDecimal refundAmount;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;

    /** 扩展信息 */
    @Schema(description = "扩展信息")
    private String extJson;

    /** 创建时间 */
    @Schema(description = "创建时间")
    private Date createTime;

    /** 更新时间 */
    @Schema(description = "更新时间")
    private Date updateTime;

    /** 创建用户 */
    @Schema(description = "创建用户")
    private String createUser;

    /** 更新用户 */
    @Schema(description = "更新用户")
    private String updateUser;

    // ========== 小程序专用字段 ==========

    /** 是否可以取消 */
    @Schema(description = "是否可以取消")
    private Boolean canCancel;

    /** 是否可以支付 */
    @Schema(description = "是否可以支付")
    private Boolean canPay;

    /** 是否可以申请退款 */
    @Schema(description = "是否可以申请退款")
    private Boolean canRefund;

    /** 支付方式 */
    @Schema(description = "支付方式")
    private String payType;

    /** 支付状态 */
    @Schema(description = "支付状态")
    private String payStatus;

    /** 控制指令 */
    @Schema(description = "控制指令")
    private String controlCmd;

    // ========== 小程序UI显示字段 ==========

    /** 是否显示支付按钮 */
    @Schema(description = "是否显示支付按钮")
    private Boolean showPayButton;

    /** 是否显示取消按钮 */
    @Schema(description = "是否显示取消按钮")
    private Boolean showCancelButton;

    /** 是否显示退款按钮 */
    @Schema(description = "是否显示退款按钮")
    private Boolean showRefundButton;

    /** 订单进度百分比 */
    @Schema(description = "订单进度百分比")
    private Integer progressPercent;
} 