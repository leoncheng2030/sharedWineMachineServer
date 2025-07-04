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
package vip.wqs.device.modular.stock.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 库存日志分页查询参数
 *
 * @author AI Assistant
 * @date 2025/01/30
 */
@Getter
@Setter
@Schema(description = "库存日志分页查询参数")
public class WineStockLogPageParam {

    /** 当前页 */
    @Schema(description = "当前页码")
    private Integer current;

    /** 每页条数 */
    @Schema(description = "每页条数")
    private Integer size;

    /** 排序字段 */
    @Schema(description = "排序字段，字段驼峰名称，如：createTime")
    private String sortField;

    /** 排序方式 */
    @Schema(description = "排序方式，升序：ASCEND；降序：DESCEND")
    private String sortOrder;

    /** 关键词搜索（设备编码、设备名称、酒品名称、操作人） */
    @Schema(description = "关键词搜索")
    private String searchKey;

    /** 设备ID */
    @Schema(description = "设备ID")
    private String deviceId;

    /** 酒品ID */
    @Schema(description = "酒品ID")
    private String productId;

    /** 变更类型：REFILL-补货，SALE-销售，ADJUST-调整，DEDUCT-扣减 */
    @Schema(description = "变更类型(REFILL:补货,SALE:销售,ADJUST:调整,DEDUCT:扣减)")
    private String changeType;

    /** 操作人 */
    @Schema(description = "操作人")
    private String operator;

    /** 设备编码 */
    @Schema(description = "设备编码")
    private String deviceCode;

    /** 设备名称 */
    @Schema(description = "设备名称")
    private String deviceName;

    /** 酒品名称 */
    @Schema(description = "酒品名称")
    private String productName;

    /** 最小变更数量 */
    @Schema(description = "最小变更数量")
    private BigDecimal minChangeQuantity;

    /** 最大变更数量 */
    @Schema(description = "最大变更数量")
    private BigDecimal maxChangeQuantity;

    /** 创建时间开始 */
    @Schema(description = "创建时间开始")
    private Date createTimeStart;

    /** 创建时间结束 */
    @Schema(description = "创建时间结束")
    private Date createTimeEnd;
}