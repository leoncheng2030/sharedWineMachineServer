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
package vip.wqs.device.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 设备完整信息POJO（用于详情展示）
 * 
 * @author AI Assistant
 * @date 2025/01/30
 */
@Data
@Schema(description = "设备完整信息")
public class DevicePojo {

    /** 主键ID */
    @Schema(description = "设备ID")
    private String id;

    /** 设备编码 */
    @Schema(description = "设备编码")
    private String deviceCode;

    /** 设备名称 */
    @Schema(description = "设备名称")
    private String deviceName;

    /** 设备蓝牙UUID标识 */
    @Schema(description = "设备蓝牙UUID标识，用于蓝牙设备识别和连接")
    private String uuid;

    /** 所属门店ID */
    @Schema(description = "所属门店ID")
    private String storeId;

    /** 门店名称 */
    @Schema(description = "门店名称")
    private String storeName;

    /** 当前绑定的酒品ID */
    @Schema(description = "当前绑定的酒品ID")
    private String currentProductId;

    /** 当前酒品名称 */
    @Schema(description = "当前酒品名称")
    private String currentProductName;

    /** 设备位置 */
    @Schema(description = "设备位置")
    private String location;

    /** 设备地址 */
    @Schema(description = "设备地址")
    private String address;

    /** 设备状态(ONLINE/OFFLINE/MAINTENANCE) */
    @Schema(description = "设备业务状态")
    private String status;

    /** 设备状态文本 */
    @Schema(description = "设备业务状态文本")
    private String statusText;

    /** 蓝牙连接状态(ONLINE/OFFLINE/UNKNOWN) */
    @Schema(description = "蓝牙连接状态")
    private String connectionStatus;

    /** 蓝牙连接状态文本 */
    @Schema(description = "蓝牙连接状态文本")
    private String connectionStatusText;

    /** 绑定时间 */
    @Schema(description = "绑定时间")
    private LocalDateTime bindTime;

    /** 绑定时间文本 */
    @Schema(description = "绑定时间文本")
    private String bindTimeText;

    /** 最后在线时间 */
    @Schema(description = "最后在线时间")
    private LocalDateTime lastOnlineTime;

    /** 最后在线时间文本 */
    @Schema(description = "最后在线时间文本")
    private String lastOnlineTimeText;

    /** 最后检测时间 */
    @Schema(description = "最后蓝牙检测时间")
    private LocalDateTime lastCheckTime;

    /** 最后检测时间文本 */
    @Schema(description = "最后检测时间文本")
    private String lastCheckTimeText;

    /** 检测结果描述 */
    @Schema(description = "检测结果描述")
    private String checkResult;

    /** 管理员用户ID */
    @Schema(description = "管理员用户ID")
    private String managerUserId;

    /** 管理员用户名称 */
    @Schema(description = "管理员用户名称")
    private String managerUserName;

    /** 设备二维码 */
    @Schema(description = "设备二维码")
    private String qrCode;

    /** 创建时间 */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /** 创建时间文本 */
    @Schema(description = "创建时间文本")
    private String createTimeText;

    /** 更新时间 */
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    /** 更新时间文本 */
    @Schema(description = "更新时间文本")
    private String updateTimeText;

    /** 创建用户 */
    @Schema(description = "创建用户")
    private String createUser;

    /** 更新用户 */
    @Schema(description = "更新用户")
    private String updateUser;

    /** 排序码 */
    @Schema(description = "排序码")
    private Integer sortCode;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;

    /** 扩展信息 */
    @Schema(description = "扩展信息")
    private String extJson;

    // TODO: 后续实现关联API后，可以在这里添加关联数据
    // 如设备历史记录、设备统计信息等
} 