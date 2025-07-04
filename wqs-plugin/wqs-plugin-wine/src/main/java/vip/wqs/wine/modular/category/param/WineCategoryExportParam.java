/*
 * Copyright [2022] [https://www.wqs.vip]
 *
 * WQS采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改WQS源码头部的版权声明。
 * 3.本项目代码可免费商业使用，商业使用请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://www.wqs.vip
 * 5.不可二次分发开源参与同类竞品，如需要参与同类竞品请联系官方购买商业授权合同。
 * 6.若您的项目无法满足以上几点，需要更多功能代码，获取WQS商业授权许可，请在官网购买授权，地址为 https://www.wqs.vip
 */
package vip.wqs.wine.modular.category.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 酒品分类导出参数
 *
 * @author wqs
 * @date 2025/01/27
 */
@Getter
@Setter
@Schema(description = "酒品分类导出参数")
public class WineCategoryExportParam {

    /** 分类编码 */
    @Schema(description = "分类编码")
    private String categoryCode;

    /** 分类名称 */
    @Schema(description = "分类名称")
    private String categoryName;

    /** 父级分类ID */
    @Schema(description = "父级分类ID")
    private String parentId;

    /** 分类级别 */
    @Schema(description = "分类级别")
    private Integer categoryLevel;

    /** 状态 */
    @Schema(description = "状态：ENABLE-启用，DISABLE-禁用")
    private String status;
} 