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
package vip.wqs.miniprogram.modular.store.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

/**
 * 小程序门店分页查询参数
 *
 * @author AI Assistant
 * @date 2025/01/30
 */
@Getter
@Setter
@Schema(description = "小程序门店分页查询参数")
public class MiniStorePageParam {

    /** 页码 */
    @Schema(description = "页码", example = "1")
    @Min(value = 1, message = "页码不能小于1")
    private Long pageNum = 1L;

    /** 页大小 */
    @Schema(description = "页大小", example = "10")
    @Min(value = 1, message = "页大小不能小于1")
    @Max(value = 100, message = "页大小不能大于100")
    private Long pageSize = 10L;

    /** 搜索关键词（门店名称、地址等） */
    @Schema(description = "搜索关键词")
    private String keyword;

    /** 门店状态筛选 */
    @Schema(description = "门店状态：ENABLE-营业中，DISABLE-已打烊")
    private String status;

    /** 省份筛选 */
    @Schema(description = "省份")
    private String province;

    /** 城市筛选 */
    @Schema(description = "城市")
    private String city;

    /** 区县筛选 */
    @Schema(description = "区县")
    private String district;

    /** 经度（用于附近门店查询） */
    @Schema(description = "经度")
    private Double longitude;

    /** 纬度（用于附近门店查询） */
    @Schema(description = "纬度")
    private Double latitude;

    /** 搜索半径（公里，用于附近门店查询） */
    @Schema(description = "搜索半径（公里）", example = "5.0")
    @Min(value = 0, message = "搜索半径不能小于0")
    @Max(value = 100, message = "搜索半径不能大于100")
    private Double radius = 5.0;

    /** 排序字段 */
    @Schema(description = "排序字段", allowableValues = {"distance", "createTime", "sortCode", "rating"})
    private String sortField = "createTime";

    /** 排序方向 */
    @Schema(description = "排序方向", allowableValues = {"ASC", "DESC"})
    private String sortOrder = "DESC";

    @Override
    public String toString() {
        return "MiniStorePageParam{" +
                "pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", keyword='" + keyword + '\'' +
                ", status='" + status + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", radius=" + radius +
                ", sortField='" + sortField + '\'' +
                ", sortOrder='" + sortOrder + '\'' +
                '}';
    }
} 