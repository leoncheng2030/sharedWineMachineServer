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
package vip.wqs.wine.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 酒品分类POJO类
 * 用于API层数据传输和接口调用
 *
 * @author wqs
 * @date 2025/01/30
 */
@Getter
@Setter
@Schema(description = "酒品分类")
public class WineCategoryPojo implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键 */
    @Schema(description = "主键")
    private String id;

    /** 分类编码 */
    @Schema(description = "分类编码")
    private String categoryCode;

    /** 分类名称 */
    @Schema(description = "分类名称")
    private String categoryName;

    /** 父级分类ID */
    @Schema(description = "父级分类ID，0表示顶级分类")
    private String parentId;

    /** 分类级别 */
    @Schema(description = "分类级别，1为一级分类")
    private Integer categoryLevel;

    /** 分类路径 */
    @Schema(description = "分类路径，如：1/2/3")
    private String categoryPath;

    /** 分类图标 */
    @Schema(description = "分类图标")
    private String icon;

    /** 分类图片URL */
    @Schema(description = "分类图片URL")
    private String imageUrl;

    /** 分类描述 */
    @Schema(description = "分类描述")
    private String description;

    /** 状态 */
    @Schema(description = "状态：ENABLE-启用，DISABLE-禁用")
    private String status;

    /** 排序码 */
    @Schema(description = "排序码，数字越小越靠前")
    private Integer sortCode;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;

    /** 扩展信息 */
    @Schema(description = "扩展信息")
    private String extJson;

    /** 子分类列表（用于树形结构） */
    @Schema(description = "子分类列表")
    private List<WineCategoryPojo> children;

    /** 创建时间 */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /** 创建用户 */
    @Schema(description = "创建用户")
    private String createUser;

    /** 更新时间 */
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    /** 更新用户 */
    @Schema(description = "更新用户")
    private String updateUser;

    /** 删除标志 */
    @Schema(description = "删除标志")
    private String deleteFlag;
} 