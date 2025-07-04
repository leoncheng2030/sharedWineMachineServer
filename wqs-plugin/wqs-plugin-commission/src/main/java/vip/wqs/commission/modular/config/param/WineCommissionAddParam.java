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

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 分销配置新增参数
 *
 * @author WQS_TEAM
 * @date 2025-01-29
 */
@Getter
@Setter
public class WineCommissionAddParam {

    /** 门店ID（NULL表示平台默认配置） */
    @Schema(description = "门店ID（NULL表示平台默认配置）")
    private String storeId;

    /** 酒品ID（NULL表示通用配置） */
    @Schema(description = "酒品ID（NULL表示通用配置）")
    private String productId;

    /** 平台分成比例 */
    @Schema(description = "平台分成比例", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "平台分成比例不能为空")
    @DecimalMin(value = "0.0000", message = "平台分成比例不能小于0")
    @DecimalMax(value = "1.0000", message = "平台分成比例不能大于1")
    private BigDecimal platformRate;

    /** 设备方分成比例 */
    @Schema(description = "设备方分成比例", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "设备方分成比例不能为空")
    @DecimalMin(value = "0.0000", message = "设备方分成比例不能小于0")
    @DecimalMax(value = "1.0000", message = "设备方分成比例不能大于1")
    private BigDecimal deviceOwnerRate;

    /** 场地方分成比例 */
    @Schema(description = "场地方分成比例", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "场地方分成比例不能为空")
    @DecimalMin(value = "0.0000", message = "场地方分成比例不能小于0")
    @DecimalMax(value = "1.0000", message = "场地方分成比例不能大于1")
    private BigDecimal locationProviderRate;

    /** 门店管理员分成比例 */
    @Schema(description = "门店管理员分成比例", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "门店管理员分成比例不能为空")
    @DecimalMin(value = "0.0000", message = "门店管理员分成比例不能小于0")
    @DecimalMax(value = "1.0000", message = "门店管理员分成比例不能大于1")
    private BigDecimal storeManagerRate;

    /** 供应商分成比例 */
    @Schema(description = "供应商分成比例", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "供应商分成比例不能为空")
    @DecimalMin(value = "0.0000", message = "供应商分成比例不能小于0")
    @DecimalMax(value = "1.0000", message = "供应商分成比例不能大于1")
    private BigDecimal supplierRate;

    /** 生效日期 */
    @Schema(description = "生效日期")
    private Date effectiveDate;

    /** 失效日期 */
    @Schema(description = "失效日期")
    private Date expiryDate;

    /** 状态 */
    @Schema(description = "状态")
    private String status;

    /** 排序码 */
    @Schema(description = "排序码")
    private Integer sortCode;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;

    /** 扩展信息 */
    @Schema(description = "扩展信息")
    private String extJson;

    /**
     * 验证总分成比例是否等于1
     * @return 总分成比例
     */
    public BigDecimal getTotalRate() {
        BigDecimal total = BigDecimal.ZERO;
        if (platformRate != null) total = total.add(platformRate);
        if (deviceOwnerRate != null) total = total.add(deviceOwnerRate);
        if (locationProviderRate != null) total = total.add(locationProviderRate);
        if (storeManagerRate != null) total = total.add(storeManagerRate);
        if (supplierRate != null) total = total.add(supplierRate);
        return total;
    }


} 