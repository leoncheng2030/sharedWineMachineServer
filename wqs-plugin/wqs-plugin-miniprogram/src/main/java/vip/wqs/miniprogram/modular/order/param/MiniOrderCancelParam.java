/*
 * Copyright [2025] [https://www.wqs.vip]
 *
 * WQS采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改WQS源码头部的版权声明。
 * 3.本项目代码可免费商业使用，商业使用请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://www.wqs.vip
 * 5.不可二次分发开源参与同类竞品，如有想法可联系团队wqs@wqs.vip商议合作。
 * 6.若您的项目无法满足以上几点，需要更多功能代码，获取WQS商业授权许可，请在官网购买授权，地址为 https://www.wqs.vip
 */
package vip.wqs.miniprogram.modular.order.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 小程序订单取消参数
 *
 * @author wqs
 * @date 2025-01-30
 */
@Getter
@Setter
@Schema(description = "小程序订单取消参数")
public class MiniOrderCancelParam {

    /** 订单ID */
    @Schema(description = "订单ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "订单ID不能为空")
    private String orderId;

    /** 取消原因 */
    @Schema(description = "取消原因")
    private String cancelReason;
} 