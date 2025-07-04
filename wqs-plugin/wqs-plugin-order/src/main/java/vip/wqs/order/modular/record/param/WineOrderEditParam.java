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
package vip.wqs.order.modular.record.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 订单编辑参数
 *
 * @author wqs
 * @date 2025/01/30
 */
@Getter
@Setter
public class WineOrderEditParam {

    /** 主键 */
    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "id不能为空")
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
    private BigDecimal serviceFee;

    /** 订单状态 */
    @Schema(description = "订单状态")
    private String status;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;

    /** 扩展信息 */
    @Schema(description = "扩展信息")
    private String extJson;
} 