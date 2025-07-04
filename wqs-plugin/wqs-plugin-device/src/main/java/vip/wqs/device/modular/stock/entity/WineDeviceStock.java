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
import vip.wqs.common.pojo.CommonEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 设备库存实体类
 *
 * @author AI Assistant
 * @date 2025/01/30
 */
@Getter
@Setter
@TableName("wine_device_stock")
@Schema(description = "设备库存")
public class WineDeviceStock extends CommonEntity {

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

    /** 库存数量(ml) */
    @Schema(description = "库存数量(ml)")
    private BigDecimal stockQuantity;

    /** 预警阈值(ml) */
    @Schema(description = "预警阈值(ml)")
    private BigDecimal alertThreshold;

    /** 最后补货时间 */
    @Schema(description = "最后补货时间")
    private LocalDateTime lastRefillTime;

    /** 最后补货数量 */
    @Schema(description = "最后补货数量")
    private BigDecimal lastRefillQuantity;

    /** 累计销售量 */
    @Schema(description = "累计销售量")
    private BigDecimal totalSold;

    /** 库存状态：NORMAL-正常，LOW-低库存，OUT-缺货 */
    @Schema(description = "库存状态")
    private String status;

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
     * 检查是否需要预警
     */
    public boolean needAlert() {
        if (stockQuantity == null || alertThreshold == null) {
            return false;
        }
        return stockQuantity.compareTo(alertThreshold) <= 0;
    }

    /**
     * 检查是否缺货
     */
    public boolean isOutOfStock() {
        return stockQuantity == null || stockQuantity.compareTo(BigDecimal.ZERO) <= 0;
    }

    /**
     * 更新库存状态
     */
    public void updateStatus() {
        if (isOutOfStock()) {
            this.status = "OUT";
        } else if (needAlert()) {
            this.status = "LOW";
        } else {
            this.status = "NORMAL";
        }
    }
}