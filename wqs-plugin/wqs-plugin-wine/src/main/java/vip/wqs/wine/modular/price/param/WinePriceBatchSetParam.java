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
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 酒品价格批量设置参数
 *
 * @author wqs
 * @date 2025/01/27
 */
@Getter
@Setter
@Schema(description = "酒品价格批量设置参数")
public class WinePriceBatchSetParam {

    /** 酒品ID */
    @Schema(description = "酒品ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "酒品ID不能为空")
    private String productId;

    /** 价格配置列表 */
    @Schema(description = "价格配置列表", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "价格配置列表不能为空")
    @Valid
    private List<PriceConfigParam> priceConfigs;

    /**
     * 价格配置参数
     */
    @Getter
    @Setter
    @Schema(description = "价格配置参数")
    public static class PriceConfigParam {

        /** ID，有值表示更新，无值表示新增 */
        @Schema(description = "价格配置ID")
        private String id;

        /** 酒品ID */
        @Schema(description = "酒品ID", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotEmpty(message = "酒品ID不能为空")
        private String productId;

        /** 容量(ml) */
        @Schema(description = "容量(ml)", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "容量不能为空")
        private BigDecimal capacity;

        /** 容量折扣率 (0-1之间，0.1表示10%折扣) */
        @Schema(description = "容量折扣率", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "折扣率不能为空")
        private BigDecimal discountRate;

        /** 状态：ENABLE-启用，DISABLE-禁用 */
        @Schema(description = "状态")
        private String status = "ENABLE";

        /** 生效日期 */
        @Schema(description = "生效日期")
        private Date effectiveDate;

        /** 失效日期 */
        @Schema(description = "失效日期")
        private Date expiryDate;

        /** 排序码 */
        @Schema(description = "排序码")
        private Integer sortCode = 1;

        /** 备注 */
        @Schema(description = "备注")
        private String remark;
    }
} 