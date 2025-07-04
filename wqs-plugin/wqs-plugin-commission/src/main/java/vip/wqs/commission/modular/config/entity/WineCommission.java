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
package vip.wqs.commission.modular.config.entity;

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
 * 分销配置实体类
 *
 * @author WQS_TEAM
 * @date 2025-01-29
 */
@Getter
@Setter
@TableName("wine_commission")
public class WineCommission extends CommonEntity {

    /** 主键 */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键")
    private String id;

    /** 门店ID（NULL表示平台默认配置） */
    @Schema(description = "门店ID（NULL表示平台默认配置）")
    @Trans(type = TransType.RPC, targetClassName = "vip.wqs.store.modular.manage.entity.WineStore", fields = "storeName", alias = "store", ref = "storeName")
    private String storeId;

    /** 门店名称（翻译字段） */
    @TableField(exist = false)
    private String storeName;

    /** 酒品ID（NULL表示通用配置） */
    @Schema(description = "酒品ID（NULL表示通用配置）")
    @Trans(type = TransType.RPC, targetClassName = "vip.wqs.wine.modular.product.entity.WineProduct", fields = "productName", alias = "product", ref = "productName")
    private String productId;

    /** 酒品名称（翻译字段） */
    @TableField(exist = false)
    private String productName;

    /** 平台分成比例（默认15%） */
    @Schema(description = "平台分成比例")
    private BigDecimal platformRate;

    /** 设备方分成比例（默认20%） */
    @Schema(description = "设备方分成比例")
    private BigDecimal deviceOwnerRate;

    /** 场地方分成比例（默认10%） */
    @Schema(description = "场地方分成比例")
    private BigDecimal locationProviderRate;

    /** 门店管理员分成比例（默认10%） */
    @Schema(description = "门店管理员分成比例")
    private BigDecimal storeManagerRate;

    /** 供应商分成比例（默认45%） */
    @Schema(description = "供应商分成比例")
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

    /** 配置类型（翻译字段） */
    @TableField(exist = false)
    private String configType;

    /**
     * 获取配置类型枚举值
     * 
     * @return 配置类型枚举值
     */
    public String getConfigType() {
        if (storeId != null && productId != null) {
            return "STORE_PRODUCT";
        } else if (storeId != null) {
            return "STORE_GENERAL";
        } else if (productId != null) {
            return "PRODUCT_GENERAL";
        } else {
            return "PLATFORM_DEFAULT";
        }
    }

    /** 总分成比例（翻译字段） */
    @TableField(exist = false)
    private BigDecimal totalRate;

    /**
     * 获取总分成比例
     * 
     * @return 总分成比例
     */
    public BigDecimal getTotalRate() {
        BigDecimal total = BigDecimal.ZERO;
        if (platformRate != null)
            total = total.add(platformRate);
        if (deviceOwnerRate != null)
            total = total.add(deviceOwnerRate);
        if (locationProviderRate != null)
            total = total.add(locationProviderRate);
        if (storeManagerRate != null)
            total = total.add(storeManagerRate);
        if (supplierRate != null)
            total = total.add(supplierRate);
        return total;
    }
}