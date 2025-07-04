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
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 酒品分类移动参数
 *
 * @author wqs
 * @date 2025/01/27
 */
@Getter
@Setter
@Schema(description = "酒品分类移动参数")
public class WineCategoryMoveParam {

    /** 分类ID */
    @Schema(description = "分类ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "分类ID不能为空")
    private String id;

    /** 新的父级分类ID */
    @Schema(description = "新的父级分类ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "新的父级分类ID不能为空")
    private String newParentId;

    /** 移动位置：BEFORE-之前，AFTER-之后，INNER-内部 */
    @Schema(description = "移动位置：BEFORE-之前，AFTER-之后，INNER-内部")
    private String position;

    /** 目标分类ID（当position为BEFORE或AFTER时需要） */
    @Schema(description = "目标分类ID")
    private String targetId;
}