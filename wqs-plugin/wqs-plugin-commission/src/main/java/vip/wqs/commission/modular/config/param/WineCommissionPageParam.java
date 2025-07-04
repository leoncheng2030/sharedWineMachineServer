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
package vip.wqs.commission.modular.config.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 分销配置分页查询参数
 *
 * @author WQS_TEAM
 * @date 2025-01-29
 */
@Getter
@Setter
public class WineCommissionPageParam {

    /** 当前页 */
    @Schema(description = "当前页码")
    private Integer current;

    /** 每页条数 */
    @Schema(description = "每页条数")
    private Integer size;

    /** 排序字段 */
    @Schema(description = "排序字段，字段驼峰名称，如：storeName")
    private String sortField;

    /** 排序方式 */
    @Schema(description = "排序方式，升序：ASCEND；降序：DESCEND")
    private String sortOrder;

    /** 门店ID */
    @Schema(description = "门店ID")
    private String storeId;

    /** 门店名称 */
    @Schema(description = "门店名称")
    private String storeName;

    /** 酒品ID */
    @Schema(description = "酒品ID")
    private String productId;

    /** 酒品名称 */
    @Schema(description = "酒品名称")
    private String productName;

    /** 状态 */
    @Schema(description = "状态")
    private String status;

    /** 配置类型（平台默认、门店通用、酒品通用、门店+酒品专属） */
    @Schema(description = "配置类型")
    private String configType;

    /** 搜索关键字（支持门店名称、酒品名称、备注） */
    @Schema(description = "搜索关键字")
    private String searchKey;
} 