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
package vip.wqs.device.modular.info.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.constant.TransType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import vip.wqs.common.pojo.CommonEntity;

import java.time.LocalDateTime;

/**
 * 设备信息实体类
 *
 * @author wqs
 * @date 2025/01/27
 */
@Getter
@Setter
@TableName("wine_device")
public class WineDevice extends CommonEntity {

    /** 主键 */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键")
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
    @Trans(type = TransType.RPC, targetClassName = "vip.wqs.store.modular.manage.entity.WineStore", fields = "storeName", alias = "store", ref = "storeName")
    private String storeId;

    /** 门店名称（翻译字段） */
    @TableField(exist = false)
    private String storeName;

    /** 当前绑定的酒品ID */
    @Schema(description = "当前绑定的酒品ID")
    @Trans(type = TransType.RPC, targetClassName = "vip.wqs.wine.modular.product.entity.WineProduct", fields = "productName", alias = "currentProduct", ref = "currentProductName")
    private String currentProductId;

    /** 当前酒品名称（翻译字段） */
    @TableField(exist = false)
    private String currentProductName;

    /** 设备位置 */
    @Schema(description = "设备位置")
    private String location;

    /** 设备状态(ONLINE/OFFLINE/MAINTENANCE) */
    @Schema(description = "设备业务状态")
    private String status;

    /** 蓝牙连接状态(ONLINE/OFFLINE/UNKNOWN) */
    @Schema(description = "蓝牙连接状态")
    private String connectionStatus;

    /** 绑定时间 */
    @Schema(description = "绑定时间")
    private LocalDateTime bindTime;

    /** 最后在线时间 */
    @Schema(description = "最后在线时间")
    private LocalDateTime lastOnlineTime;

    /** 最后检测时间 */
    @Schema(description = "最后蓝牙检测时间")
    private LocalDateTime lastCheckTime;

    /** 检测用户ID */
    @Schema(description = "最后检测的用户ID")
    private String checkUserId;

    /** 检测结果描述 */
    @Schema(description = "检测结果描述")
    private String checkResult;

    /** 管理员用户ID */
    @Schema(description = "管理员用户ID")
    @Trans(type = TransType.RPC, targetClassName = "vip.wqs.client.modular.user.entity.ClientUser", fields = "name", alias = "managerUser", ref = "managerUserName")
    private String managerUserId;

    /** 管理员用户名称（翻译字段） */
    @TableField(exist = false)
    private String managerUserName;

    /** 设备二维码 */
    @Schema(description = "设备二维码")
    private String qrCode;

    /** 排序码 */
    @Schema(description = "排序码")
    private Integer sortCode;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;

    /** 扩展信息 */
    @Schema(description = "扩展信息")
    private String extJson;
}