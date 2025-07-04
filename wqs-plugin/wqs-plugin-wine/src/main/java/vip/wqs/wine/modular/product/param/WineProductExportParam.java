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
package vip.wqs.wine.modular.product.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 酒品导出参数
 *
 * @author wqs
 * @date 2025/1/27
 **/
@Getter
@Setter
public class WineProductExportParam {

    /** 搜索关键词 */
    @Schema(description = "酒品编码、酒品名称、品牌关键词")
    private String searchKey;

    /** 酒品类型 */
    @Schema(description = "酒品类型")
    private String productType;

    /** 品牌 */
    @Schema(description = "品牌")
    private String brand;

    /** 状态 */
    @Schema(description = "状态")
    private String status;

    /** 酒品id集合 */
    @Schema(description = "酒品id集合")
    private String productIds;
} 