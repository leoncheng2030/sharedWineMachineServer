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
package vip.wqs.device.modular.control.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 设备控制结果
 *
 * @author AI Assistant
 * @date 2025/01/30
 */
@Data
@Schema(description = "设备控制结果")
public class DeviceControlResult {

    /** 是否成功 */
    @Schema(description = "是否成功")
    private boolean success;

    /** 结果消息 */
    @Schema(description = "结果消息")
    private String message;

    /** 加密控制指令 */
    @Schema(description = "加密控制指令")
    private String cmd;

    /** 控制参数 */
    @Schema(description = "控制参数")
    private String params;

    /** 设备响应数据 */
    @Schema(description = "设备响应数据")
    private String responseData;

    /** 执行时间戳 */
    @Schema(description = "执行时间戳")
    private Long timestamp;

    /**
     * 创建成功结果
     */
    public static DeviceControlResult success(String cmd) {
        DeviceControlResult result = new DeviceControlResult();
        result.setSuccess(true);
        result.setMessage("指令生成成功");
        result.setCmd(cmd);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    /**
     * 创建成功结果（带消息）
     */
    public static DeviceControlResult success(String cmd, String message) {
        DeviceControlResult result = new DeviceControlResult();
        result.setSuccess(true);
        result.setMessage(message);
        result.setCmd(cmd);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    /**
     * 创建失败结果
     */
    public static DeviceControlResult failure(String message) {
        DeviceControlResult result = new DeviceControlResult();
        result.setSuccess(false);
        result.setMessage(message);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }
}