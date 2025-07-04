/*
 * Copyright [2022] [https://www.xiaonuo.vip]
 *
 * Snowy采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改Snowy源码头部的版权声明。
 * 3.本项目代码可免费商业使用，商业使用请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://www.xiaonuo.vip
 * 5.不可二次分发开源参与同类竞品，如有想法可联系团队xiaonuobase@qq.com商议合作。
 * 6.若您的项目无法满足以上几点，需要更多功能代码，获取Snowy商业授权许可，请在官网购买授权，地址为 https://www.xiaonuo.vip
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
 * 酒品价格添加参数
 *
 * @author WQS_TEAM
 * @date 2025-01-27
 */
@Getter
@Setter
public class WinePriceAddParam {

    /** 门店ID（NULL表示平台统一价格） */
    @Schema(description = "门店ID（NULL表示平台统一价格）")
    private String storeId;

    /** 关联酒品ID */
    @Schema(description = "关联酒品ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "productId不能为空")
    private String productId;

    /** 容量(ml) */
    @Schema(description = "容量(ml)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "容量不能为空")
    private BigDecimal capacity;

    /** 容量折扣率 */
    @Schema(description = "容量折扣率", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "折扣率不能为空")
    private BigDecimal discountRate;

    /** 生效日期 */
    @Schema(description = "生效日期")
    private Date effectiveDate;

    /** 失效日期 */
    @Schema(description = "失效日期")
    private Date expiryDate;

    /** 状态 */
    @Schema(description = "状态")
    private String status;

    /** 排序码 */
    @Schema(description = "排序码")
    private Integer sortCode;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;

    /** 扩展信息 */
    @Schema(description = "扩展信息")
    private String extJson;
} 