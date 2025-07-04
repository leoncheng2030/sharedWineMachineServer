/*
 * Copyright [2025] [https://www.wqs.vip]
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
package vip.wqs.miniprogram.modular.order.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 小程序订单创建参数
 *
 * @author wqs
 * @date 2025-01-30
 */
@Getter
@Setter
@Schema(description = "小程序订单创建参数")
public class MiniOrderCreateParam {

    /** 设备ID */
    @Schema(description = "设备ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "设备ID不能为空")
    private String deviceId;

    /** 酒品ID */
    @Schema(description = "酒品ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "酒品ID不能为空")
    private String wineId;

    /** 出酒量(ml) */
    @Schema(description = "出酒量(ml)", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    @NotNull(message = "出酒量不能为空")
    @Positive(message = "出酒量必须大于0")
    private Integer amount;

    /** 单价(元/ml) */
    @Schema(description = "单价(元/ml)", requiredMode = Schema.RequiredMode.REQUIRED, example = "0.05")
    @NotNull(message = "单价不能为空")
    private BigDecimal unitPrice;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;

    /** 下单位置纬度 */
    @Schema(description = "下单位置纬度", example = "39.908860")
    private Double latitude;

    /** 下单位置经度 */
    @Schema(description = "下单位置经度", example = "116.397390")
    private Double longitude;

    /** 优惠券ID */
    @Schema(description = "优惠券ID")
    private String couponId;

    /** 促销活动ID */
    @Schema(description = "促销活动ID")
    private String promotionId;
} 