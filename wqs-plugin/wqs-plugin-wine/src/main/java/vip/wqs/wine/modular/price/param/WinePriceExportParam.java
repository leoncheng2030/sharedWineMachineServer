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
package vip.wqs.wine.modular.price.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * 酒品价格导出参数
 *
 * @author wqs
 * @date 2025/01/27
 */
@Getter
@Setter
@Schema(description = "酒品价格导出参数")
public class WinePriceExportParam {

    /** 导出的价格策略ID列表 */
    @Schema(description = "导出的价格策略ID列表")
    private List<String> priceIds;

    /** 酒品ID */
    @Schema(description = "酒品ID")
    private String productId;

    /** 价格策略状态 */
    @Schema(description = "价格策略状态")
    private String status;

    /** 开始生效日期 */
    @Schema(description = "开始生效日期")
    private Date startEffectiveDate;

    /** 结束生效日期 */
    @Schema(description = "结束生效日期")
    private Date endEffectiveDate;

    /** 导出格式：EXCEL-Excel文件，CSV-CSV文件 */
    @Schema(description = "导出格式：EXCEL-Excel文件，CSV-CSV文件")
    private String exportFormat;

    /** 导出字段列表 */
    @Schema(description = "导出字段列表")
    private List<String> exportFields;
} 