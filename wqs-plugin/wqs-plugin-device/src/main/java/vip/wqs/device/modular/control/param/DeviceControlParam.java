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
package vip.wqs.device.modular.control.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 设备控制参数
 *
 * @author AI Assistant
 * @date 2025/01/30
 */
@Data
@Schema(description = "设备控制参数")
public class DeviceControlParam {

    /** 设备UUID */
    @Schema(description = "设备蓝牙UUID标识")
    private String uuid;

    /** 设备Code */
    @Schema(description = "设备Code")
    private Integer deviceCode;

    /** 订单ID（充电ID） */
    @Schema(description = "订单ID")
    private Integer chargeId;

    /** 控制时长-分钟 */
    @Schema(description = "控制时长-分钟")
    private Integer minute;

    /** 控制时长-秒 */
    @Schema(description = "控制时长-秒")
    private Integer second;

    /** 操作用户ID */
    @Schema(description = "操作用户ID")
    private String userId;
}