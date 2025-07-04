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
package vip.wqs.store.modular.manage.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 门店分页查询参数
 *
 * @author wqs
 * @date 2025/1/27
 **/
@Getter
@Setter
public class WineStorePageParam {

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

    /** 关键词 */
    @Schema(description = "门店名称、编码关键词")
    private String searchKey;

    /** 门店编码 */
    @Schema(description = "门店编码")
    private String storeCode;

    /** 门店名称 */
    @Schema(description = "门店名称")
    private String storeName;

    /** 门店地址 */
    @Schema(description = "门店地址")
    private String address;

    /** 门店状态(ENABLE/DISABLE) */
    @Schema(description = "门店状态")
    private String status;

    /** 定价权限(PLATFORM-平台统一，CUSTOM-自定义) */
    @Schema(description = "定价权限")
    private String priceAuthority;

    /** 门店管理员ID */
    @Schema(description = "门店管理员ID")
    private String storeManagerId;

    /** 省份 */
    @Schema(description = "省份")
    private String province;

    /** 城市 */
    @Schema(description = "城市")
    private String city;

    /** 区县 */
    @Schema(description = "区县")
    private String district;

    /** 创建时间开始 */
    @Schema(description = "创建时间开始")
    private String startCreateTime;

    /** 创建时间结束 */
    @Schema(description = "创建时间结束")
    private String endCreateTime;

} 