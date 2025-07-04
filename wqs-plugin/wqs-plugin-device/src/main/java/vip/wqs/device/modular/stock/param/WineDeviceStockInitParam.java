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
package vip.wqs.device.modular.stock.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 设备库存初始化参数
 *
 * @author AI Assistant
 * @date 2025/01/30
 */
@Getter
@Setter
@Schema(description = "设备库存初始化参数")
public class WineDeviceStockInitParam {

    // 手动添加getter方法以解决Lombok问题
    /** 设备ID */
    @Schema(description = "设备ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "设备ID不能为空")
    private String deviceId;

    /** 酒品ID */
    @Schema(description = "酒品ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "酒品ID不能为空")
    private String productId;

    /** 初始库存数量 */
    @Schema(description = "初始库存数量(ml)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "初始库存数量不能为空")
    @DecimalMin(value = "0", message = "初始库存数量不能为负数")
    private BigDecimal initialQuantity;

    /** 预警阈值 */
    @Schema(description = "预警阈值(ml)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "预警阈值不能为空")
    @DecimalMin(value = "0", message = "预警阈值不能为负数")
    private BigDecimal alertThreshold;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;

}