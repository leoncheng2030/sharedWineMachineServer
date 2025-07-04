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
 * 酒品查询条件POJO类
 * 用于API层查询参数传输
 *
 * @author wqs
 * @date 2025/01/30
 */
@Getter
@Setter
@Schema(description = "酒品查询条件")
public class WineProductQueryPojo implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 酒品编码 */
    @Schema(description = "酒品编码")
    private String productCode;

    /** 酒品名称（模糊查询） */
    @Schema(description = "酒品名称（模糊查询）")
    private String productName;

    /** 酒品分类ID */
    @Schema(description = "酒品分类ID")
    private String categoryId;

    /** 酒品类型 */
    @Schema(description = "酒品类型")
    private String productType;

    /** 品牌 */
    @Schema(description = "品牌")
    private String brand;

    /** 最小酒精度数 */
    @Schema(description = "最小酒精度数")
    private BigDecimal minAlcoholContent;

    /** 最大酒精度数 */
    @Schema(description = "最大酒精度数")
    private BigDecimal maxAlcoholContent;

    /** 最小价格 */
    @Schema(description = "最小价格")
    private BigDecimal minPrice;

    /** 最大价格 */
    @Schema(description = "最大价格")
    private BigDecimal maxPrice;

    /** 状态 */
    @Schema(description = "状态：ENABLE-启用，DISABLE-禁用")
    private String status;

    /** 门店ID（查询特定门店的酒品） */
    @Schema(description = "门店ID（查询特定门店的酒品）")
    private String storeId;

    /** 是否包含价格信息 */
    @Schema(description = "是否包含价格信息")
    private Boolean includePriceInfo;

    /** 排序字段 */
    @Schema(description = "排序字段：sortCode-排序码，createTime-创建时间，price-价格")
    private String sortField;

    /** 排序方向 */
    @Schema(description = "排序方向：ASC-升序，DESC-降序")
    private String sortOrder;

    /** 页码 */
    @Schema(description = "页码，从1开始")
    private Integer pageNum;

    /** 每页数量 */
    @Schema(description = "每页数量")
    private Integer pageSize;
} 