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
package vip.wqs.commission.modular.account.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 用户账户余额调整参数
 *
 * @author WQS_TEAM
 * @date 2025-01-29
 */
@Getter
@Setter
public class WineUserAccountAdjustParam {

    /** 主键ID */
    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "主键ID不能为空")
    private String id;

    /** 调整类型 */
    @Schema(description = "调整类型(ADD:增加,SUBTRACT:减少,FREEZE:冻结,UNFREEZE:解冻)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "调整类型不能为空")
    private String adjustType;

    /** 调整金额 */
    @Schema(description = "调整金额", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "调整金额不能为空")
    @DecimalMin(value = "0.01", message = "调整金额必须大于0")
    private BigDecimal adjustAmount;

    /** 调整原因 */
    @Schema(description = "调整原因", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "调整原因不能为空")
    private String adjustReason;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;
} 