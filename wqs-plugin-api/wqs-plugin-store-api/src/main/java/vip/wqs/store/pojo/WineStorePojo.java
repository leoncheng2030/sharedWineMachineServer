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
package vip.wqs.store.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 门店完整信息POJO（用于详情展示）
 * 
 * @author AI Assistant
 * @date 2025/01/30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "门店完整信息")
public class WineStorePojo extends WineStoreSimplePojo {

    private static final long serialVersionUID = 1L;

    /** 门店管理员ID */
    @Schema(description = "门店管理员ID")
    private String storeManagerId;

    /** 门店管理员姓名 */
    @Schema(description = "门店管理员姓名")
    private String storeManagerUserName;

    /** 省份 */
    @Schema(description = "省份")
    private String province;

    /** 城市 */
    @Schema(description = "城市")
    private String city;

    /** 区县 */
    @Schema(description = "区县")
    private String district;

    /** 详细地址 */
    @Schema(description = "详细地址")
    private String detailAddress;

    /** 联系邮箱 */
    @Schema(description = "联系邮箱")
    private String contactEmail;

    /** 定价权限(PLATFORM-平台统一，CUSTOM-自定义) */
    @Schema(description = "定价权限")
    private String priceAuthority;

    /** 门店面积(平方米) */
    @Schema(description = "门店面积")
    private Double storeArea;

    /** 门店描述 */
    @Schema(description = "门店描述")
    private String description;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;

    /** 经度 */
    @Schema(description = "经度")
    private Double longitude;

    /** 纬度 */
    @Schema(description = "纬度")
    private Double latitude;

    /** 详细营业时间（周一到周日） */
    @Schema(description = "详细营业时间")
    private Map<String, String> businessHoursDetail;

    /** 门店优惠活动 */
    @Schema(description = "门店优惠活动")
    private List<StorePromotionPojo> promotions;

    /** 门店设备数量 */
    @Schema(description = "门店设备数量")
    private Integer deviceCount;

    /** 门店酒品数量 */
    @Schema(description = "门店酒品数量")
    private Integer productCount;

    /** 创建用户 */
    @Schema(description = "创建用户")
    private String createUser;

    /** 更新时间 */
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    /** 更新用户 */
    @Schema(description = "更新用户")
    private String updateUser;

    /** 扩展信息 */
    @Schema(description = "扩展信息")
    private Map<String, Object> extData;

    /**
     * 门店优惠活动内部类
     */
    @Data
    @Schema(description = "门店优惠活动")
    public static class StorePromotionPojo {
        
        /** 活动ID */
        @Schema(description = "活动ID")
        private String id;

        /** 活动标题 */
        @Schema(description = "活动标题")
        private String title;

        /** 活动描述 */
        @Schema(description = "活动描述")
        private String description;

        /** 活动类型 */
        @Schema(description = "活动类型")
        private String type;

        /** 是否有效 */
        @Schema(description = "是否有效")
        private Boolean isValid;
    }
} 