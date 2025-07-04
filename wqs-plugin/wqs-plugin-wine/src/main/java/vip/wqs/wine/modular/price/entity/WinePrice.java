/*
 * Copyright [2022] [https://www.xiaonuo.vip]
 *
 * Snowy采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改Snowy源码头部的版权声明。
 * 3.本项目代码可免费商业使用，商业使用请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://www.xiaonuo.vip
 * 5.不可二次分发开源参与同类竞品，如有想法可联系团队xiaonuobase@qq.com商议合作。
 * 6.若您的项目无法满足以上几点，需要更多功能代码，获取Snowy商业授权许可，请在官网购买授权，地址为 https://www.xiaonuo.vip
 */

package vip.wqs.wine.modular.price.entity;

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
import vip.wqs.wine.modular.product.entity.WineProduct;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

/**
 * 酒品价格实体类
 *
 * @author WQS_TEAM
 * @date 2025-01-27
 */
@Getter
@Setter
@TableName("wine_price")
public class WinePrice extends CommonEntity {

    /** 主键 */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键")
    private String id;

    /** 门店ID（NULL表示平台统一价格） */
    @Schema(description = "门店ID（NULL表示平台统一价格）")
    @Trans(type = TransType.RPC, targetClassName = "vip.wqs.store.modular.record.entity.WineStore", fields = "storeName", alias = "store", ref = "storeName")
    private String storeId;

    /** 门店名称（翻译字段） */
    @TableField(exist = false)
    private String storeName;

    /** 关联酒品ID */
    @Schema(description = "关联酒品ID")
    @Trans(type = TransType.SIMPLE, target = WineProduct.class, fields = {"productName", "productCode"}, refs = {"productName", "productCode"})
    private String productId;

    /** 容量折扣率 */
    @Schema(description = "容量折扣率")
    private BigDecimal discountRate;

    /** 生效日期 */
    @Schema(description = "生效日期")
    private Date effectiveDate;

    /** 失效日期 */
    @Schema(description = "失效日期")
    private Date expiryDate;

    /** 容量(ml) */
    @Schema(description = "容量(ml)")
    private BigDecimal capacity;

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

    /** 酒品名称 */
    @Schema(description = "酒品名称")
    @TableField(exist = false)
    private String productName;

    /** 酒品编码 */
    @Schema(description = "酒品编码")
    @TableField(exist = false)
    private String productCode;

    /**
     * 计算最终价格
     * 最终价格 = 基础单价 × 容量 × (1 - 折扣率)
     * 
     * @param unitPrice 基础单价(元/ml)
     * @return 最终价格
     */
    public BigDecimal calculateFinalPrice(BigDecimal unitPrice) {
        if (unitPrice == null || capacity == null || discountRate == null) {
            return BigDecimal.ZERO;
        }
        
        // 基础金额 = 基础单价 × 容量
        BigDecimal baseAmount = unitPrice.multiply(capacity);
        
        // 折扣金额 = 基础金额 × 折扣率
        BigDecimal discountAmount = baseAmount.multiply(discountRate);
        
        // 最终价格 = 基础金额 - 折扣金额
        BigDecimal finalPrice = baseAmount.subtract(discountAmount);
        
        // 保留2位小数，四舍五入
        return finalPrice.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * 获取实际单价(考虑折扣后)
     * 
     * @param unitPrice 基础单价(元/ml)
     * @return 实际单价(元/ml)
     */
    public BigDecimal getActualUnitPrice(BigDecimal unitPrice) {
        if (capacity == null || capacity.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return calculateFinalPrice(unitPrice).divide(capacity, 4, RoundingMode.HALF_UP);
    }
} 