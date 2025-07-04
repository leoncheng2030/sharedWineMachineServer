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
package vip.wqs.commission.modular.record.entity;

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

import java.math.BigDecimal;
import java.util.Date;

/**
 * 佣金记录实体类
 *
 * @author WQS_TEAM
 * @date 2025-01-29
 */
@Getter
@Setter
@TableName("wine_commission_record")
public class WineCommissionRecord extends CommonEntity {

    /** 主键ID */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键ID")
    private String id;

    /** 订单ID */
    @Schema(description = "订单ID")
    private String orderId;

    /** 订单号 */
    @Schema(description = "订单号")
    private String orderNo;

    /** 受益用户ID */
    @Schema(description = "受益用户ID")
    @Trans(type = TransType.RPC, targetClassName = "vip.wqs.client.modular.user.entity.ClientUser", fields = "name", alias = "user", ref = "userNickname")
    private String userId;

    /** 用户昵称（翻译字段） */
    @TableField(exist = false)
    private String userNickname;

    /** 设备ID */
    @Schema(description = "设备ID")
    @Trans(type = TransType.RPC, targetClassName = "vip.wqs.device.modular.info.entity.WineDevice", fields = "deviceCode", alias = "device", ref = "deviceCode")
    private String deviceId;

    /** 设备编码（翻译字段） */
    @TableField(exist = false)
    private String deviceCode;

    /** 酒品ID */
    @Schema(description = "酒品ID")
    @Trans(type = TransType.RPC, targetClassName = "vip.wqs.wine.modular.product.entity.WineProduct", fields = "productName", alias = "wine", ref = "wineName")
    private String wineId;

    /** 酒品名称（翻译字段） */
    @TableField(exist = false)
    private String wineName;

    /** 订单金额(元) */
    @Schema(description = "订单金额(元)")
    private BigDecimal orderAmount;

    /** 佣金类型 */
    @Schema(description = "佣金类型(PLATFORM:平台,DEVICE_OWNER:设备方,LOCATION_PROVIDER:场地方,STORE_MANAGER:门店管理员,SUPPLIER:供应商)")
    private String commissionType;

    /** 佣金比例(%) */
    @Schema(description = "佣金比例(%)")
    private BigDecimal commissionRate;

    /** 佣金金额(元) */
    @Schema(description = "佣金金额(元)")
    private BigDecimal commissionAmount;

    /** 佣金状态 */
    @Schema(description = "佣金状态(PENDING:待计算,CALCULATED:已计算,SETTLED:已发放,FROZEN:已冻结,CANCELLED:已取消)")
    private String status;

    /** 计算时间 */
    @Schema(description = "计算时间")
    private Date calculateTime;

    /** 发放时间 */
    @Schema(description = "发放时间")
    private Date settleTime;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;

    /** 扩展信息 */
    @Schema(description = "扩展信息")
    private String extJson;

    /**
     * 获取佣金状态描述
     */
    public String getStatusDesc() {
        if (status == null) {
            return "";
        }
        switch (status) {
            case "PENDING":
                return "待计算";
            case "CALCULATED":
                return "已计算";
            case "SETTLED":
                return "已发放";
            case "FROZEN":
                return "已冻结";
            case "CANCELLED":
                return "已取消";
            default:
                return status;
        }
    }

    /**
     * 获取佣金类型描述
     */
    public String getCommissionTypeDesc() {
        if (commissionType == null) {
            return "";
        }
        switch (commissionType) {
            case "PLATFORM":
                return "平台佣金";
            case "DEVICE_OWNER":
                return "设备方佣金";
            case "LOCATION_PROVIDER":
                return "场地方佣金";
            case "STORE_MANAGER":
                return "门店管理员佣金";
            case "SUPPLIER":
                return "供应商佣金";
            default:
                return commissionType;
        }
    }
}