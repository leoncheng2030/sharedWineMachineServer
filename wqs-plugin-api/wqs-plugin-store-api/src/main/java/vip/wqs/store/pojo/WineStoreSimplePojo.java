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

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 门店简化信息POJO（用于列表展示）
 * 
 * @author AI Assistant
 * @date 2025/01/30
 */
@Data
@Schema(description = "门店简化信息")
public class WineStoreSimplePojo implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @Schema(description = "门店ID")
    private String id;

    /** 门店编码 */
    @Schema(description = "门店编码")
    private String storeCode;

    /** 门店名称 */
    @Schema(description = "门店名称")
    private String storeName;

    /** 门店图片URL */
    @Schema(description = "门店图片URL")
    private String imageUrl;

    /** 完整地址（省+市+区+详细地址） */
    @Schema(description = "完整地址")
    private String fullAddress;

    /** 联系电话 */
    @Schema(description = "联系电话")
    private String contactPhone;

    /** 营业时间 */
    @Schema(description = "营业时间")
    private String businessHours;

    /** 门店状态(ENABLE-营业中, DISABLE-已打烊) */
    @Schema(description = "门店状态")
    private String status;

    /** 是否营业中 */
    @Schema(description = "是否营业中")
    private Boolean isOpen;

    /** 距离（公里，仅在附近门店查询时有值） */
    @Schema(description = "距离（公里）")
    private Double distance;

    /** 评分 */
    @Schema(description = "评分")
    private Double rating;

    /** 浏览次数 */
    @Schema(description = "浏览次数")
    private Integer viewCount;

    /** 排序码 */
    @Schema(description = "排序码")
    private Integer sortCode;

    /** 创建时间 */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
} 