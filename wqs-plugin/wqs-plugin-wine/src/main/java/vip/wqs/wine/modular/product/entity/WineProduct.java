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
package vip.wqs.wine.modular.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.constant.TransType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import vip.wqs.common.pojo.CommonEntity;
import vip.wqs.wine.modular.category.entity.WineCategory;

import java.math.BigDecimal;

/**
 * 酒品信息实体类
 *
 * @author wqs
 * @date 2024/01/01
 */
@Getter
@Setter
@TableName("wine_product")
public class WineProduct extends CommonEntity {

    /** 主键 */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键")
    private String id;

    /** 酒品编码 */
    @Schema(description = "酒品编码")
    private String productCode;

    /** 酒品名称 */
    @Schema(description = "酒品名称")
    private String productName;

    /** 酒品分类ID */
    @Schema(description = "酒品分类ID")
    @Trans(type = TransType.SIMPLE, target = WineCategory.class, fields = {"categoryName"}, refs = {"categoryName"})
    private String categoryId;

    /** 分类名称（翻译字段） */
    @TableField(exist = false)
    private String categoryName;

    /** 酒品类型 */
    @Schema(description = "酒品类型")
    private String productType;

    /** 品牌 */
    @Schema(description = "品牌")
    private String brand;

    /** 酒精度数 */
    @Schema(description = "酒精度数")
    private BigDecimal alcoholContent;

    /** 净含量(ml) */
    @Schema(description = "净含量(ml)")
    private Integer volume;

    /** 产地 */
    @Schema(description = "产地")
    private String origin;

    /** 生产厂家 */
    @Schema(description = "生产厂家")
    private String manufacturer;

    /** 建议零售价 */
    @Schema(description = "建议零售价")
    private BigDecimal suggestedPrice;

    /** 成本价 */
    @Schema(description = "成本价")
    private BigDecimal costPrice;

    /** 基础单价(每ml价格) */
    @Schema(description = "基础单价(每ml价格)")
    private BigDecimal unitPrice;

    /** 酒品图片URL */
    @Schema(description = "酒品图片URL")
    private String imageUrl;

    /** 酒品描述 */
    @Schema(description = "酒品描述")
    private String description;

    /** 状态(ENABLE/DISABLE) */
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