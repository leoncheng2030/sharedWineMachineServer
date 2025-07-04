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
package vip.wqs.wine.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 酒品简化信息POJO类
 * 用于列表展示和轻量级数据传输
 *
 * @author wqs
 * @date 2025/01/30
 */
@Getter
@Setter
@Schema(description = "酒品简化信息")
public class WineProductSimplePojo implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键 */
    @Schema(description = "主键")
    private String id;

    /** 酒品编码 */
    @Schema(description = "酒品编码")
    private String productCode;

    /** 酒品名称 */
    @Schema(description = "酒品名称")
    private String productName;

    /** 分类名称 */
    @Schema(description = "分类名称")
    private String categoryName;

    /** 品牌 */
    @Schema(description = "品牌")
    private String brand;

    /** 供应商名称 */
    @Schema(description = "供应商名称")
    private String supplierName;

    /** 酒精度数 */
    @Schema(description = "酒精度数")
    private BigDecimal alcoholContent;

    /** 净含量(ml) */
    @Schema(description = "净含量(ml)")
    private Integer volume;

    /** 建议零售价 */
    @Schema(description = "建议零售价")
    private BigDecimal suggestedPrice;

    /** 当前门店价格 */
    @Schema(description = "当前门店价格")
    private BigDecimal currentPrice;

    /** 酒品图片URL */
    @Schema(description = "酒品图片URL")
    private String imageUrl;

    /** 状态(ENABLE/DISABLE) */
    @Schema(description = "状态")
    private String status;

    /** 排序码 */
    @Schema(description = "排序码")
    private Integer sortCode;
} 