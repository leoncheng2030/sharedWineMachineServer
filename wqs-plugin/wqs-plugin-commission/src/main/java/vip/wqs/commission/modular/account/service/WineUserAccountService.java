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
package vip.wqs.commission.modular.account.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import vip.wqs.commission.modular.account.entity.WineUserAccount;
import vip.wqs.commission.modular.account.param.WineUserAccountAdjustParam;
import vip.wqs.commission.modular.account.param.WineUserAccountIdParam;
import vip.wqs.commission.modular.account.param.WineUserAccountPageParam;
import vip.wqs.commission.modular.account.param.WineUserAccountStatusParam;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用户账户Service接口
 *
 * @author WQS_TEAM
 * @date 2025-01-29
 */
public interface WineUserAccountService extends IService<WineUserAccount> {

    /**
     * 获取用户账户分页列表
     */
    Page<WineUserAccount> page(WineUserAccountPageParam wineUserAccountPageParam);

    /**
     * 获取用户账户详情
     */
    WineUserAccount detail(WineUserAccountIdParam wineUserAccountIdParam);

    /**
     * 根据用户ID获取账户信息
     */
    WineUserAccount getByUserId(String userId);

    /**
     * 创建用户账户
     */
    void createAccount(String userId);

    /**
     * 调整账户余额
     */
    void adjustBalance(WineUserAccountAdjustParam wineUserAccountAdjustParam);

    /**
     * 修改账户状态
     */
    void updateStatus(WineUserAccountStatusParam wineUserAccountStatusParam);

    /**
     * 冻结账户
     */
    void freezeAccount(WineUserAccountIdParam wineUserAccountIdParam);

    /**
     * 解冻账户
     */
    void unfreezeAccount(WineUserAccountIdParam wineUserAccountIdParam);

    /**
     * 禁用账户
     */
    void disableAccount(WineUserAccountIdParam wineUserAccountIdParam);

    /**
     * 启用账户
     */
    void enableAccount(WineUserAccountIdParam wineUserAccountIdParam);

    /**
     * 获取用户账户选择器列表
     */
    List<WineUserAccount> accountSelector(String searchKey);

    /**
     * 增加可用余额
     */
    void addAvailableBalance(String userId, BigDecimal amount, String reason);

    /**
     * 减少可用余额
     */
    void subtractAvailableBalance(String userId, BigDecimal amount, String reason);

    /**
     * 冻结余额
     */
    void freezeBalance(String userId, BigDecimal amount, String reason);

    /**
     * 解冻余额
     */
    void unfreezeBalance(String userId, BigDecimal amount, String reason);

    /**
     * 增加累计佣金
     */
    void addTotalCommission(String userId, BigDecimal amount);

    /**
     * 增加累计提现
     */
    void addTotalWithdraw(String userId, BigDecimal amount);

    /**
     * 导出用户账户数据
     */
    void exportUserAccount(WineUserAccountPageParam wineUserAccountPageParam);

    /**
     * 增加佣金收入（佣金分配时调用）
     * 
     * @param userId 用户ID
     * @param commissionAmount 佣金金额
     * @param flowType 流水类型
     * @param remark 备注
     */
    void addCommission(String userId, BigDecimal commissionAmount, String flowType, String remark);
} 