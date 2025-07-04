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
package vip.wqs.miniprogram.modular.wine.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

/**
 * 小程序酒品分页查询参数
 *
 * @author wqs
 * @date 2025/01/30
 */
@Getter
@Setter
@Schema(description = "小程序酒品分页查询参数")
public class MiniWinePageParam {

    /** 页码 */
    @Schema(description = "页码", example = "1")
    @Min(value = 1, message = "页码不能小于1")
    private Long pageNum = 1L;

    /** 页大小 */
    @Schema(description = "页大小", example = "10")
    @Min(value = 1, message = "页大小不能小于1")
    @Max(value = 100, message = "页大小不能大于100")
    private Long pageSize = 10L;

    /** 酒品分类ID */
    @Schema(description = "酒品分类ID")
    private String categoryId;

    /** 搜索关键词（商品名称、品牌等） */
    @Schema(description = "搜索关键词")
    private String keyword;

    /** 价格范围-最低价 */
    @Schema(description = "价格范围-最低价")
    @Min(value = 0, message = "最低价不能小于0")
    private Double minPrice;

    /** 价格范围-最高价 */
    @Schema(description = "价格范围-最高价")
    @Min(value = 0, message = "最高价不能小于0")
    private Double maxPrice;

    /** 排序字段 */
    @Schema(description = "排序字段", allowableValues = {"createTime", "price", "salesCount", "productName"})
    private String sortField = "createTime";

    /** 排序方向 */
    @Schema(description = "排序方向", allowableValues = {"ASC", "DESC"})
    private String sortOrder = "DESC";

    @Override
    public String toString() {
        return "MiniWinePageParam{" +
                "pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", categoryId='" + categoryId + '\'' +
                ", keyword='" + keyword + '\'' +
                ", minPrice=" + minPrice +
                ", maxPrice=" + maxPrice +
                ", sortField='" + sortField + '\'' +
                ", sortOrder='" + sortOrder + '\'' +
                '}';
    }
} 