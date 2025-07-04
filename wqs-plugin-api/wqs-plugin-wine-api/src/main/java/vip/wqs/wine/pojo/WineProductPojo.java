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
import java.time.LocalDateTime;
import java.util.List;

/**
 * 酒品信息POJO类
 * 用于API层数据传输和接口调用，支持列表和详情展示
 *
 * @author wqs
 * @date 2025/01/30
 */
@Getter
@Setter
@Schema(description = "酒品信息")
public class WineProductPojo implements Serializable {

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

    /** 酒品分类ID */
    @Schema(description = "酒品分类ID")
    private String categoryId;

    /** 分类名称 */
    @Schema(description = "分类名称")
    private String categoryName;

    /** 分类信息（详情时使用） */
    @Schema(description = "分类信息")
    private WineCategoryPojo category;

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

    /** 当前价格（平台统一价格） */
    @Schema(description = "当前价格（平台统一价格）")
    private BigDecimal currentPrice;

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

    /** 价格列表（详情时使用，可选） */
    @Schema(description = "价格列表（详情时使用）")
    private List<WinePricePojo> priceList;

    /** 创建时间 */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /** 创建用户 */
    @Schema(description = "创建用户")
    private String createUser;

    /** 更新时间 */
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    /** 更新用户 */
    @Schema(description = "更新用户")
    private String updateUser;

    /** 删除标志 */
    @Schema(description = "删除标志")
    private String deleteFlag;
} 