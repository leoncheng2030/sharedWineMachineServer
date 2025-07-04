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
package vip.wqs.device.modular.stock.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.constant.TransType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 库存变更日志实体类
 *
 * @author AI Assistant
 * @date 2025/01/30
 */
@Getter
@Setter
@TableName("wine_stock_log")
@Schema(description = "库存变更日志")
public class WineStockLog {

    /** 主键 */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键")
    private String id;

    /** 设备ID */
    @Schema(description = "设备ID")
    @Trans(type = TransType.RPC, targetClassName = "vip.wqs.device.modular.info.entity.WineDevice", fields = "deviceName,deviceCode", alias = "device", ref = "deviceName,deviceCode")
    private String deviceId;

    /** 酒品ID */
    @Schema(description = "酒品ID")
    @Trans(type = TransType.RPC, targetClassName = "vip.wqs.wine.modular.product.entity.WineProduct", fields = "productName,productCode", alias = "product", ref = "productName,productCode")
    private String productId;

    /** 变更类型：REFILL-补货，SALE-销售，ADJUST-调整 */
    @Schema(description = "变更类型")
    private String changeType;

    /** 变更数量 */
    @Schema(description = "变更数量")
    private BigDecimal changeQuantity;

    /** 变更前数量 */
    @Schema(description = "变更前数量")
    private BigDecimal beforeQuantity;

    /** 变更后数量 */
    @Schema(description = "变更后数量")
    private BigDecimal afterQuantity;

    /** 变更原因 */
    @Schema(description = "变更原因")
    private String changeReason;

    /** 操作人 */
    @Schema(description = "操作人")
    private String operator;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;

    /** 创建时间 */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /** 创建用户 */
    @Schema(description = "创建用户")
    private String createUser;

    /** 设备名称（翻译字段） */
    @TableField(exist = false)
    @Schema(description = "设备名称")
    private String deviceName;

    /** 酒品名称（翻译字段） */
    @TableField(exist = false)
    @Schema(description = "酒品名称")
    private String productName;

    /** 设备编码（翻译字段） */
    @TableField(exist = false)
    @Schema(description = "设备编码")
    private String deviceCode;

    /** 酒品编码（翻译字段） */
    @TableField(exist = false)
    @Schema(description = "酒品编码")
    private String productCode;

    /**
     * 变更类型枚举
     */
    public static class ChangeType {
        /** 补货 */
        public static final String REFILL = "REFILL";
        /** 销售 */
        public static final String SALE = "SALE";
        /** 调整 */
        public static final String ADJUST = "ADJUST";
    }
}