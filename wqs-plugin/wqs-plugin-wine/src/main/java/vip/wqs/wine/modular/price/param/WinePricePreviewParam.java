/*
 * Copyright [2022] [https://www.wqs.vip]
 *
 * WQS采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改WQS源码头部的版权声明。
 * 3.本项目代码可免费商业使用，商业使用请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://www.wqs.vip
 * 5.不可二次分发开源参与同类竞品，如需要参与同类竞品请联系官方购买商业授权合同。
 * 6.若您的项目无法满足以上几点，需要更多功能代码，获取WQS商业授权许可，请在官网购买授权，地址为 https://www.wqs.vip
 */
package vip.wqs.wine.modular.price.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 酒品价格预览参数
 *
 * @author wqs
 * @date 2025/01/27
 */
@Getter
@Setter
@Schema(description = "酒品价格预览参数")
public class WinePricePreviewParam {

    /** 酒品ID */
    @Schema(description = "酒品ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "酒品ID不能为空")
    private String productId;

    /** 原价 */
    @Schema(description = "原价", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "原价不能为空")
    private BigDecimal originalPrice;

    /** 折扣类型：PERCENTAGE-百分比，AMOUNT-固定金额 */
    @Schema(description = "折扣类型")
    private String discountType;

    /** 折扣值 */
    @Schema(description = "折扣值")
    private BigDecimal discountValue;

    /** 预览日期（计算该日期的有效价格） */
    @Schema(description = "预览日期")
    private Date previewDate;

    /** 购买数量 */
    @Schema(description = "购买数量")
    private Integer quantity;

    /** 用户类型：VIP-会员，NORMAL-普通用户 */
    @Schema(description = "用户类型")
    private String userType;
} 