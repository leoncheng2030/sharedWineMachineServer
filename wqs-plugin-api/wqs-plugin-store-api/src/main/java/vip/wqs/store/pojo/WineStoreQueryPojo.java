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
package vip.wqs.store.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 门店查询参数POJO
 * 
 * @author AI Assistant
 * @date 2025/01/30
 */
@Data
@Schema(description = "门店查询参数")
public class WineStoreQueryPojo implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 页码 */
    @Schema(description = "页码，从1开始")
    private Integer pageNum = 1;

    /** 每页数量 */
    @Schema(description = "每页数量")
    private Integer pageSize = 10;

    /** 关键词搜索（门店名称、编码、地址） */
    @Schema(description = "关键词搜索")
    private String keyword;

    /** 门店编码 */
    @Schema(description = "门店编码")
    private String storeCode;

    /** 门店名称 */
    @Schema(description = "门店名称")
    private String storeName;

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

    /** 门店管理员ID筛选 */
    @Schema(description = "门店管理员ID")
    private String storeManagerId;

    /** 定价权限筛选 */
    @Schema(description = "定价权限：PLATFORM-平台统一，CUSTOM-自定义")
    private String priceAuthority;

    /** 经度（用于附近门店查询） */
    @Schema(description = "经度")
    private Double longitude;

    /** 纬度（用于附近门店查询） */
    @Schema(description = "纬度")
    private Double latitude;

    /** 搜索半径（公里，用于附近门店查询） */
    @Schema(description = "搜索半径（公里）")
    private Double radius = 5.0;

    /** 排序字段 */
    @Schema(description = "排序字段：distance-距离，createTime-创建时间，sortCode-排序码，rating-评分")
    private String sortField;

    /** 排序方向 */
    @Schema(description = "排序方向：ASC-升序，DESC-降序")
    private String sortOrder;

    /** 开始时间 */
    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    /** 结束时间 */
    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    /** 是否包含设备信息 */
    @Schema(description = "是否包含设备信息")
    private Boolean includeDeviceInfo = false;

    /** 是否包含酒品信息 */
    @Schema(description = "是否包含酒品信息")
    private Boolean includeProductInfo = false;

    /** 是否只查询营业中的门店 */
    @Schema(description = "是否只查询营业中的门店")
    private Boolean onlyOpen = false;
} 