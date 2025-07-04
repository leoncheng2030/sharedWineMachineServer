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
package vip.wqs.dev.modular.city.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 城市管理新增参数
 *
 * @author wqs
 * @date 2025/01/30
 */
@Getter
@Setter
public class WineCityAddParam {

    /** 城市编码（国标行政区划代码） */
    @Schema(description = "城市编码（国标行政区划代码）")
    @NotBlank(message = "城市编码不能为空")
    private String cityCode;

    /** 城市名称 */
    @Schema(description = "城市名称")
    @NotBlank(message = "城市名称不能为空")
    private String cityName;

    /** 省份编码 */
    @Schema(description = "省份编码")
    private String provinceCode;

    /** 省份名称 */
    @Schema(description = "省份名称")
    private String provinceName;

    /** 父级编码 */
    @Schema(description = "父级编码")
    private String parentCode;

    /** 层级：1-省，2-市，3-区县 */
    @Schema(description = "层级：1-省，2-市，3-区县")
    @NotNull(message = "层级不能为空")
    private Integer level;

    /** 状态：ENABLE-启用，DISABLE-禁用 */
    @Schema(description = "状态：ENABLE-启用，DISABLE-禁用")
    private String status;

    /** 是否热门城市：YES-是，NO-否 */
    @Schema(description = "是否热门城市：YES-是，NO-否")
    private String isHot;

    /** 是否支持配送：YES-支持，NO-不支持 */
    @Schema(description = "是否支持配送：YES-支持，NO-不支持")
    private String supportDelivery;

    /** 经度 */
    @Schema(description = "经度")
    private BigDecimal longitude;

    /** 纬度 */
    @Schema(description = "纬度")
    private BigDecimal latitude;

    /** 区号 */
    @Schema(description = "区号")
    private String areaCode;

    /** 邮政编码 */
    @Schema(description = "邮政编码")
    private String postalCode;

    /** 门店数量 */
    @Schema(description = "门店数量")
    private Integer storeCount;

    /** 设备数量 */
    @Schema(description = "设备数量")
    private Integer deviceCount;

    /** 订单数量 */
    @Schema(description = "订单数量")
    private Integer orderCount;

    /** 排序码 */
    @Schema(description = "排序码")
    @NotNull(message = "排序码不能为空")
    private Integer sortCode;

    /** 拼音 */
    @Schema(description = "拼音")
    private String pinyin;

    /** 简称 */
    @Schema(description = "简称")
    private String shortName;

    /** 城市描述 */
    @Schema(description = "城市描述")
    private String description;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;

    /** 扩展信息JSON */
    @Schema(description = "扩展信息JSON")
    private String extJson;
} 