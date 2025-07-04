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
import lombok.EqualsAndHashCode;

/**
 * 设备详情信息POJO（用于小程序设备详情页面）
 * 包含设备基本信息、门店信息、酒品信息等完整数据
 * 
 * @author AI Assistant
 * @date 2025/01/30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "设备详情信息")
public class DeviceDetailPojo extends DevicePojo {

    private static final long serialVersionUID = 1L;

    /** 门店信息 */
    @Schema(description = "门店信息")
    private StoreInfo storeInfo;

    /** 当前绑定的酒品信息 */
    @Schema(description = "当前绑定的酒品信息")
    private WineInfo wineInfo;

    /**
     * 门店信息内部类
     */
    @Data
    @Schema(description = "门店信息")
    public static class StoreInfo {
        /** 门店ID */
        @Schema(description = "门店ID")
        private String id;
        
        /** 门店名称 */
        @Schema(description = "门店名称")
        private String name;
        
        /** 营业时间 */
        @Schema(description = "营业时间")
        private String hours;
        
        /** 联系电话 */
        @Schema(description = "联系电话")
        private String phone;
        
        /** 完整地址 */
        @Schema(description = "完整地址")
        private String address;
        
        /** 门店状态 */
        @Schema(description = "门店状态")
        private String status;
        
        /** 是否营业中 */
        @Schema(description = "是否营业中")
        private Boolean isOpen;
    }

    /**
     * 酒品信息内部类
     */
    @Data
    @Schema(description = "酒品信息")
    public static class WineInfo {
        /** 酒品ID */
        @Schema(description = "酒品ID")
        private String id;
        
        /** 酒品名称 */
        @Schema(description = "酒品名称")
        private String name;
        
        /** 品牌 */
        @Schema(description = "品牌")
        private String brand;
        
        /** 酒品类型 */
        @Schema(description = "酒品类型")
        private String type;
        
        /** 酒精度数 */
        @Schema(description = "酒精度数")
        private Double alcoholContent;
        
        /** 酒品图片 */
        @Schema(description = "酒品图片")
        private String image;
        
        /** 价格（每100ml） */
        @Schema(description = "价格（每100ml）")
        private Double price;
        
        /** 库存（ml） */
        @Schema(description = "库存（ml）")
        private Integer stock;
        
        /** 容量规格 */
        @Schema(description = "容量规格")
        private java.util.List<Capacity> capacities;
    }

    /**
     * 容量规格内部类
     */
    @Data
    @Schema(description = "容量规格")
    public static class Capacity {
        /** 规格ID */
        @Schema(description = "规格ID")
        private String id;
        
        /** 容量大小 */
        @Schema(description = "容量大小")
        private String size;
        
        /** 规格描述 */
        @Schema(description = "规格描述")
        private String description;
        
        /** 价格 */
        @Schema(description = "价格")
        private Double price;
        
        /** 库存 */
        @Schema(description = "库存")
        private Integer stock;
    }
} 