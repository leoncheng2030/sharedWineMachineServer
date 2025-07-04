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
package vip.wqs.store.modular.manage.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * 门店新增参数
 *
 * @author wqs
 * @date 2025/1/27
 **/
@Getter
@Setter
public class WineStoreAddParam {

    /** 门店编码 */
    @Schema(description = "门店编码")
    @NotBlank(message = "门店编码不能为空")
    private String storeCode;

    /** 门店名称 */
    @Schema(description = "门店名称")
    @NotBlank(message = "门店名称不能为空")
    private String storeName;

    /** 门店管理员ID(关联client_user) */
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

    /** 联系电话 */
    @Schema(description = "联系电话")
    private String contactPhone;

    /** 联系邮箱 */
    @Schema(description = "联系邮箱")
    private String contactEmail;

    /** 营业时间 */
    @Schema(description = "营业时间")
    private String businessHours;

    /** 门店状态(ENABLE/DISABLE) */
    @Schema(description = "门店状态")
    private String status;

    /** 定价权限(PLATFORM-平台统一，CUSTOM-自定义) */
    @Schema(description = "定价权限")
    private String priceAuthority;

    /** 门店面积(平方米) */
    @Schema(description = "门店面积")
    private Double storeArea;

    /** 经度 */
    @Schema(description = "经度")
    private Double longitude;

    /** 纬度 */
    @Schema(description = "纬度")
    private Double latitude;

    /** 排序码 */
    @Schema(description = "排序码")
    @NotNull(message = "排序码不能为空")
    private Integer sortCode;

    /** 门店描述 */
    @Schema(description = "门店描述")
    private String description;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;

    /** 扩展信息 */
    @Schema(description = "扩展信息")
    private String extJson;
} 