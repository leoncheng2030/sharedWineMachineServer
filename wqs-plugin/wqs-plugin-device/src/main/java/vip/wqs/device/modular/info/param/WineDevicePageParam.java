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
package vip.wqs.device.modular.info.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 设备分页查询参数
 *
 * @author wqs
 * @date 2025/1/27
 **/
@Getter
@Setter
public class WineDevicePageParam {

    /** 当前页 */
    @Schema(description = "当前页码")
    private Integer current;

    /** 每页条数 */
    @Schema(description = "每页条数")
    private Integer size;

    /** 排序字段 */
    @Schema(description = "排序字段，字段驼峰名称，如：deviceName")
    private String sortField;

    /** 排序方式 */
    @Schema(description = "排序方式，升序：ASCEND；降序：DESCEND")
    private String sortOrder;

    /** 关键词 */
    @Schema(description = "设备名称、编码关键词")
    private String searchKey;

    /** 设备编码 */
    @Schema(description = "设备编码")
    private String deviceCode;

    /** 设备名称 */
    @Schema(description = "设备名称")
    private String deviceName;

    /** 设备位置 */
    @Schema(description = "设备位置")
    private String location;

    /** 设备状态(ONLINE/OFFLINE/MAINTENANCE) */
    @Schema(description = "设备状态")
    private String status;

    /** 管理员用户ID */
    @Schema(description = "管理员用户ID")
    private String managerUserId;

    /** 所属门店ID */
    @Schema(description = "所属门店ID")
    private String storeId;

    /** 当前绑定的酒品ID */
    @Schema(description = "当前绑定的酒品ID")
    private String currentProductId;

    /** 创建时间开始 */
    @Schema(description = "创建时间开始")
    private String startCreateTime;

    /** 创建时间结束 */
    @Schema(description = "创建时间结束")
    private String endCreateTime;
} 