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
import lombok.Getter;
import lombok.Setter;

/**
 * 城市管理分页参数
 *
 * @author wqs
 * @date 2025/01/30
 */
@Getter
@Setter
public class WineCityPageParam {

    /** 当前页 */
    @Schema(description = "当前页")
    private Integer current;

    /** 每页条数 */
    @Schema(description = "每页条数")
    private Integer size;

    /** 城市编码 */
    @Schema(description = "城市编码")
    private String cityCode;

    /** 城市名称 */
    @Schema(description = "城市名称")
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

    /** 拼音 */
    @Schema(description = "拼音")
    private String pinyin;

    /** 简称 */
    @Schema(description = "简称")
    private String shortName;
} 