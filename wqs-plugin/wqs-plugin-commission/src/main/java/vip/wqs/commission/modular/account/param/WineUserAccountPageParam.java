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
package vip.wqs.commission.modular.account.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户账户分页查询参数
 *
 * @author WQS_TEAM
 * @date 2025-01-29
 */
@Getter
@Setter
public class WineUserAccountPageParam {

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

    /** 关键词搜索（用户昵称） */
    @Schema(description = "关键词搜索")
    private String searchKey;

    /** 用户ID */
    @Schema(description = "用户ID")
    private String userId;

    /** 用户昵称 */
    @Schema(description = "用户昵称")
    private String userNickname;

    /** 账户状态 */
    @Schema(description = "账户状态(NORMAL:正常,FROZEN:冻结,DISABLED:禁用)")
    private String status;

    /** 最小总余额 */
    @Schema(description = "最小总余额")
    private BigDecimal minTotalBalance;

    /** 最大总余额 */
    @Schema(description = "最大总余额")
    private BigDecimal maxTotalBalance;

    /** 最小可用余额 */
    @Schema(description = "最小可用余额")
    private BigDecimal minAvailableBalance;

    /** 最大可用余额 */
    @Schema(description = "最大可用余额")
    private BigDecimal maxAvailableBalance;

    /** 最小累计佣金 */
    @Schema(description = "最小累计佣金")
    private BigDecimal minTotalCommission;

    /** 最大累计佣金 */
    @Schema(description = "最大累计佣金")
    private BigDecimal maxTotalCommission;

    /** 最小累计提现 */
    @Schema(description = "最小累计提现")
    private BigDecimal minTotalWithdraw;

    /** 最大累计提现 */
    @Schema(description = "最大累计提现")
    private BigDecimal maxTotalWithdraw;

    /** 最后佣金时间开始 */
    @Schema(description = "最后佣金时间开始")
    private Date lastCommissionTimeStart;

    /** 最后佣金时间结束 */
    @Schema(description = "最后佣金时间结束")
    private Date lastCommissionTimeEnd;

    /** 最后提现时间开始 */
    @Schema(description = "最后提现时间开始")
    private Date lastWithdrawTimeStart;

    /** 最后提现时间结束 */
    @Schema(description = "最后提现时间结束")
    private Date lastWithdrawTimeEnd;

    /** 创建时间开始 */
    @Schema(description = "创建时间开始")
    private Date createTimeStart;

    /** 创建时间结束 */
    @Schema(description = "创建时间结束")
    private Date createTimeEnd;
} 