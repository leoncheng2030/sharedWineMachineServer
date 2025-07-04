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
package vip.wqs.payment.modular.manage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import vip.wqs.common.pojo.CommonEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付记录实体
 *
 * @author WQS_TEAM
 * @date 2025/01/30 支付管理模块开发
 **/
@Getter
@Setter
@TableName("payment_record")
@Schema(description = "支付记录")
public class PaymentRecord extends CommonEntity {

    /** 主键 */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键")
    private String id;

    /** 支付单号（系统生成） */
    @Schema(description = "支付单号")
    private String paymentNo;

    /** 关联订单ID */
    @Schema(description = "关联订单ID")
    private String orderId;

    /** 订单号 */
    @Schema(description = "订单号")
    private String orderNo;

    /** 用户ID */
    @Schema(description = "用户ID")
    private String userId;

    /** 支付方式 */
    @Schema(description = "支付方式")
    private String payType;

    /** 支付金额 */
    @Schema(description = "支付金额")
    private BigDecimal payAmount;

    /** 实际支付金额 */
    @Schema(description = "实际支付金额")
    private BigDecimal actualAmount;

    /** 支付状态 */
    @Schema(description = "支付状态")
    private String payStatus;

    /** 支付时间 */
    @Schema(description = "支付时间")
    private LocalDateTime payTime;

    /** 第三方交易流水号 */
    @Schema(description = "第三方交易流水号")
    private String transactionId;

    /** 微信支付预支付交易会话标识 */
    @Schema(description = "预支付交易会话标识")
    private String prepayId;

    /** 支付回调时间 */
    @Schema(description = "支付回调时间")
    private LocalDateTime callbackTime;

    /** 支付回调内容 */
    @Schema(description = "支付回调内容")
    private String callbackContent;

    /** 退款金额 */
    @Schema(description = "退款金额")
    private BigDecimal refundAmount;

    /** 退款时间 */
    @Schema(description = "退款时间")
    private LocalDateTime refundTime;

    /** 退款单号 */
    @Schema(description = "退款单号")
    private String refundNo;

    /** 退款原因 */
    @Schema(description = "退款原因")
    private String refundReason;

    /** 失败原因 */
    @Schema(description = "失败原因")
    private String failReason;

    /** 过期时间 */
    @Schema(description = "过期时间")
    private LocalDateTime expireTime;

    /** 客户端IP */
    @Schema(description = "客户端IP")
    private String clientIp;

    /** 设备信息 */
    @Schema(description = "设备信息")
    private String deviceInfo;

    /** 商品描述 */
    @Schema(description = "商品描述")
    private String body;

    /** 附加数据 */
    @Schema(description = "附加数据")
    private String attach;

    /** 扩展信息 */
    @Schema(description = "扩展信息")
    private String extJson;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;
} 