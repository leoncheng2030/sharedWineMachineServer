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
package vip.wqs.commission.modular.account.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.wqs.commission.modular.account.entity.WineUserAccount;
import vip.wqs.commission.modular.account.mapper.WineUserAccountMapper;
import vip.wqs.commission.modular.account.param.WineUserAccountAdjustParam;
import vip.wqs.commission.modular.account.param.WineUserAccountIdParam;
import vip.wqs.commission.modular.account.param.WineUserAccountPageParam;
import vip.wqs.commission.modular.account.param.WineUserAccountStatusParam;
import vip.wqs.commission.modular.account.service.WineUserAccountService;
import vip.wqs.common.enums.CommonSortOrderEnum;
import vip.wqs.common.exception.CommonException;
import vip.wqs.common.page.CommonPageRequest;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 用户账户Service实现类
 *
 * @author WQS_TEAM
 * @date 2025-01-29
 */
@Service
public class WineUserAccountServiceImpl extends ServiceImpl<WineUserAccountMapper, WineUserAccount> implements WineUserAccountService {

    @Resource
    private WineUserAccountMapper wineUserAccountMapper;

    @Override
    public Page<WineUserAccount> page(WineUserAccountPageParam wineUserAccountPageParam) {
        // 构建分页参数
        Page<WineUserAccount> page = CommonPageRequest.defaultPage();
        if (ObjectUtil.isNotNull(wineUserAccountPageParam.getCurrent()) && ObjectUtil.isNotNull(wineUserAccountPageParam.getSize())) {
            page = new Page<>(wineUserAccountPageParam.getCurrent(), wineUserAccountPageParam.getSize());
        }

        // 构建查询条件
        LambdaQueryWrapper<WineUserAccount> queryWrapper = new LambdaQueryWrapper<>();
        
        // 关键词搜索（用户昵称）
        if (StrUtil.isNotBlank(wineUserAccountPageParam.getSearchKey())) {
            queryWrapper.like(WineUserAccount::getUserNickname, wineUserAccountPageParam.getSearchKey());
        }
        
        // 用户ID
        if (StrUtil.isNotBlank(wineUserAccountPageParam.getUserId())) {
            queryWrapper.eq(WineUserAccount::getUserId, wineUserAccountPageParam.getUserId());
        }
        
        // 用户昵称
        if (StrUtil.isNotBlank(wineUserAccountPageParam.getUserNickname())) {
            queryWrapper.like(WineUserAccount::getUserNickname, wineUserAccountPageParam.getUserNickname());
        }
        
        // 账户状态
        if (StrUtil.isNotBlank(wineUserAccountPageParam.getStatus())) {
            queryWrapper.eq(WineUserAccount::getStatus, wineUserAccountPageParam.getStatus());
        }
        
        // 创建时间范围
        if (ObjectUtil.isAllNotEmpty(wineUserAccountPageParam.getCreateTimeStart(), wineUserAccountPageParam.getCreateTimeEnd())) {
            queryWrapper.between(WineUserAccount::getCreateTime, wineUserAccountPageParam.getCreateTimeStart(), wineUserAccountPageParam.getCreateTimeEnd());
        }
        
        // 排序
        if (ObjectUtil.isAllNotEmpty(wineUserAccountPageParam.getSortField(), wineUserAccountPageParam.getSortOrder())) {
            CommonSortOrderEnum.validate(wineUserAccountPageParam.getSortOrder());
            boolean isAsc = wineUserAccountPageParam.getSortOrder().equals(CommonSortOrderEnum.ASC.getValue());
            String sortField = wineUserAccountPageParam.getSortField();
            
            // 根据字段名选择对应的方法引用
            switch (sortField) {
                case "createTime":
                    queryWrapper.orderBy(true, isAsc, WineUserAccount::getCreateTime);
                    break;
                case "updateTime":
                    queryWrapper.orderBy(true, isAsc, WineUserAccount::getUpdateTime);
                    break;
                case "totalBalance":
                    queryWrapper.orderBy(true, isAsc, WineUserAccount::getTotalBalance);
                    break;
                case "availableBalance":
                    queryWrapper.orderBy(true, isAsc, WineUserAccount::getAvailableBalance);
                    break;
                case "totalCommission":
                    queryWrapper.orderBy(true, isAsc, WineUserAccount::getTotalCommission);
                    break;
                case "totalWithdraw":
                    queryWrapper.orderBy(true, isAsc, WineUserAccount::getTotalWithdraw);
                    break;
                default:
                    queryWrapper.orderByDesc(WineUserAccount::getCreateTime);
                    break;
            }
        } else {
            queryWrapper.orderByDesc(WineUserAccount::getCreateTime);
        }

        return this.page(page, queryWrapper);
    }

    @Override
    public WineUserAccount detail(WineUserAccountIdParam wineUserAccountIdParam) {
        WineUserAccount wineUserAccount = this.getById(wineUserAccountIdParam.getId());
        if (ObjectUtil.isNull(wineUserAccount)) {
            throw new CommonException("用户账户不存在");
        }
        return wineUserAccount;
    }

    @Override
    public WineUserAccount getByUserId(String userId) {
        LambdaQueryWrapper<WineUserAccount> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WineUserAccount::getUserId, userId);
        return this.getOne(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createAccount(String userId) {
        // 检查用户是否已有账户
        WineUserAccount existAccount = this.getByUserId(userId);
        if (ObjectUtil.isNotNull(existAccount)) {
            throw new CommonException("用户账户已存在");
        }

        // 创建新账户
        WineUserAccount wineUserAccount = new WineUserAccount();
        wineUserAccount.setUserId(userId);
        wineUserAccount.setTotalBalance(BigDecimal.ZERO);
        wineUserAccount.setAvailableBalance(BigDecimal.ZERO);
        wineUserAccount.setFrozenBalance(BigDecimal.ZERO);
        wineUserAccount.setTotalCommission(BigDecimal.ZERO);
        wineUserAccount.setTotalWithdraw(BigDecimal.ZERO);
        wineUserAccount.setStatus("NORMAL");
        wineUserAccount.setRemark("系统自动创建");

        this.save(wineUserAccount);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void adjustBalance(WineUserAccountAdjustParam wineUserAccountAdjustParam) {
        WineUserAccount wineUserAccount = this.getById(wineUserAccountAdjustParam.getId());
        if (ObjectUtil.isNull(wineUserAccount)) {
            throw new CommonException("用户账户不存在");
        }

        String adjustType = wineUserAccountAdjustParam.getAdjustType();
        BigDecimal adjustAmount = wineUserAccountAdjustParam.getAdjustAmount();
        BigDecimal currentAvailable = wineUserAccount.getAvailableBalance();
        BigDecimal currentFrozen = wineUserAccount.getFrozenBalance();

        switch (adjustType) {
            case "ADD":
                // 增加可用余额
                wineUserAccount.setAvailableBalance(currentAvailable.add(adjustAmount));
                break;
            case "SUBTRACT":
                // 减少可用余额
                if (currentAvailable.compareTo(adjustAmount) < 0) {
                    throw new CommonException("可用余额不足");
                }
                wineUserAccount.setAvailableBalance(currentAvailable.subtract(adjustAmount));
                break;
            case "FREEZE":
                // 冻结余额
                if (currentAvailable.compareTo(adjustAmount) < 0) {
                    throw new CommonException("可用余额不足");
                }
                wineUserAccount.setAvailableBalance(currentAvailable.subtract(adjustAmount));
                wineUserAccount.setFrozenBalance(currentFrozen.add(adjustAmount));
                break;
            case "UNFREEZE":
                // 解冻余额
                if (currentFrozen.compareTo(adjustAmount) < 0) {
                    throw new CommonException("冻结余额不足");
                }
                wineUserAccount.setFrozenBalance(currentFrozen.subtract(adjustAmount));
                wineUserAccount.setAvailableBalance(currentAvailable.add(adjustAmount));
                break;
            default:
                throw new CommonException("不支持的调整类型");
        }

        // 重新计算总余额
        wineUserAccount.setTotalBalance(wineUserAccount.getAvailableBalance().add(wineUserAccount.getFrozenBalance()));
        wineUserAccount.setRemark(wineUserAccountAdjustParam.getAdjustReason());

        this.updateById(wineUserAccount);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(WineUserAccountStatusParam wineUserAccountStatusParam) {
        WineUserAccount wineUserAccount = this.getById(wineUserAccountStatusParam.getId());
        if (ObjectUtil.isNull(wineUserAccount)) {
            throw new CommonException("用户账户不存在");
        }

        wineUserAccount.setStatus(wineUserAccountStatusParam.getStatus());
        if (StrUtil.isNotBlank(wineUserAccountStatusParam.getRemark())) {
            wineUserAccount.setRemark(wineUserAccountStatusParam.getRemark());
        }

        this.updateById(wineUserAccount);
    }

    @Override
    public void freezeAccount(WineUserAccountIdParam wineUserAccountIdParam) {
        WineUserAccountStatusParam statusParam = new WineUserAccountStatusParam();
        statusParam.setId(wineUserAccountIdParam.getId());
        statusParam.setStatus("FROZEN");
        statusParam.setReason("账户冻结");
        this.updateStatus(statusParam);
    }

    @Override
    public void unfreezeAccount(WineUserAccountIdParam wineUserAccountIdParam) {
        WineUserAccountStatusParam statusParam = new WineUserAccountStatusParam();
        statusParam.setId(wineUserAccountIdParam.getId());
        statusParam.setStatus("NORMAL");
        statusParam.setReason("账户解冻");
        this.updateStatus(statusParam);
    }

    @Override
    public void disableAccount(WineUserAccountIdParam wineUserAccountIdParam) {
        WineUserAccountStatusParam statusParam = new WineUserAccountStatusParam();
        statusParam.setId(wineUserAccountIdParam.getId());
        statusParam.setStatus("DISABLED");
        statusParam.setReason("账户禁用");
        this.updateStatus(statusParam);
    }

    @Override
    public void enableAccount(WineUserAccountIdParam wineUserAccountIdParam) {
        WineUserAccountStatusParam statusParam = new WineUserAccountStatusParam();
        statusParam.setId(wineUserAccountIdParam.getId());
        statusParam.setStatus("NORMAL");
        statusParam.setReason("账户启用");
        this.updateStatus(statusParam);
    }

    @Override
    public List<WineUserAccount> accountSelector(String searchKey) {
        LambdaQueryWrapper<WineUserAccount> queryWrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(searchKey)) {
            queryWrapper.like(WineUserAccount::getUserNickname, searchKey);
        }
        queryWrapper.eq(WineUserAccount::getStatus, "NORMAL");
        queryWrapper.orderByDesc(WineUserAccount::getCreateTime);
        queryWrapper.last("LIMIT 100");
        
        return this.list(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addAvailableBalance(String userId, BigDecimal amount, String reason) {
        WineUserAccount account = this.getByUserId(userId);
        if (ObjectUtil.isNull(account)) {
            // 如果账户不存在，自动创建
            this.createAccount(userId);
            account = this.getByUserId(userId);
        }

        BigDecimal newAvailable = account.getAvailableBalance().add(amount);
        BigDecimal newTotal = newAvailable.add(account.getFrozenBalance());

        LambdaUpdateWrapper<WineUserAccount> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(WineUserAccount::getId, account.getId());
        updateWrapper.set(WineUserAccount::getAvailableBalance, newAvailable);
        updateWrapper.set(WineUserAccount::getTotalBalance, newTotal);
        updateWrapper.set(WineUserAccount::getRemark, reason);

        this.update(updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void subtractAvailableBalance(String userId, BigDecimal amount, String reason) {
        WineUserAccount account = this.getByUserId(userId);
        if (ObjectUtil.isNull(account)) {
            throw new CommonException("用户账户不存在");
        }

        if (account.getAvailableBalance().compareTo(amount) < 0) {
            throw new CommonException("可用余额不足");
        }

        BigDecimal newAvailable = account.getAvailableBalance().subtract(amount);
        BigDecimal newTotal = newAvailable.add(account.getFrozenBalance());

        LambdaUpdateWrapper<WineUserAccount> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(WineUserAccount::getId, account.getId());
        updateWrapper.set(WineUserAccount::getAvailableBalance, newAvailable);
        updateWrapper.set(WineUserAccount::getTotalBalance, newTotal);
        updateWrapper.set(WineUserAccount::getRemark, reason);

        this.update(updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void freezeBalance(String userId, BigDecimal amount, String reason) {
        WineUserAccount account = this.getByUserId(userId);
        if (ObjectUtil.isNull(account)) {
            throw new CommonException("用户账户不存在");
        }

        if (account.getAvailableBalance().compareTo(amount) < 0) {
            throw new CommonException("可用余额不足");
        }

        BigDecimal newAvailable = account.getAvailableBalance().subtract(amount);
        BigDecimal newFrozen = account.getFrozenBalance().add(amount);

        LambdaUpdateWrapper<WineUserAccount> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(WineUserAccount::getId, account.getId());
        updateWrapper.set(WineUserAccount::getAvailableBalance, newAvailable);
        updateWrapper.set(WineUserAccount::getFrozenBalance, newFrozen);
        updateWrapper.set(WineUserAccount::getRemark, reason);

        this.update(updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unfreezeBalance(String userId, BigDecimal amount, String reason) {
        WineUserAccount account = this.getByUserId(userId);
        if (ObjectUtil.isNull(account)) {
            throw new CommonException("用户账户不存在");
        }

        if (account.getFrozenBalance().compareTo(amount) < 0) {
            throw new CommonException("冻结余额不足");
        }

        BigDecimal newFrozen = account.getFrozenBalance().subtract(amount);
        BigDecimal newAvailable = account.getAvailableBalance().add(amount);

        LambdaUpdateWrapper<WineUserAccount> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(WineUserAccount::getId, account.getId());
        updateWrapper.set(WineUserAccount::getAvailableBalance, newAvailable);
        updateWrapper.set(WineUserAccount::getFrozenBalance, newFrozen);
        updateWrapper.set(WineUserAccount::getRemark, reason);

        this.update(updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addTotalCommission(String userId, BigDecimal amount) {
        WineUserAccount account = this.getByUserId(userId);
        if (ObjectUtil.isNull(account)) {
            // 如果账户不存在，自动创建
            this.createAccount(userId);
            account = this.getByUserId(userId);
        }

        BigDecimal newTotalCommission = account.getTotalCommission().add(amount);

        LambdaUpdateWrapper<WineUserAccount> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(WineUserAccount::getId, account.getId());
        updateWrapper.set(WineUserAccount::getTotalCommission, newTotalCommission);
        updateWrapper.set(WineUserAccount::getLastCommissionTime, new Date());

        this.update(updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addTotalWithdraw(String userId, BigDecimal amount) {
        WineUserAccount account = this.getByUserId(userId);
        if (ObjectUtil.isNull(account)) {
            throw new CommonException("用户账户不存在");
        }

        BigDecimal newTotalWithdraw = account.getTotalWithdraw().add(amount);

        LambdaUpdateWrapper<WineUserAccount> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(WineUserAccount::getId, account.getId());
        updateWrapper.set(WineUserAccount::getTotalWithdraw, newTotalWithdraw);
        updateWrapper.set(WineUserAccount::getLastWithdrawTime, new Date());

        this.update(updateWrapper);
    }

    @Override
    public void exportUserAccount(WineUserAccountPageParam wineUserAccountPageParam) {
        // TODO: 实现导出功能
        throw new CommonException("导出功能待实现");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addCommission(String userId, BigDecimal commissionAmount, String flowType, String remark) {
        if (ObjectUtil.isNull(commissionAmount) || commissionAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new CommonException("佣金金额必须大于0");
        }

        // 获取用户账户，如果不存在则自动创建
        WineUserAccount account = this.getByUserId(userId);
        if (ObjectUtil.isNull(account)) {
            this.createAccount(userId);
            account = this.getByUserId(userId);
        }

        // 增加可用余额和累计佣金
        BigDecimal newAvailable = account.getAvailableBalance().add(commissionAmount);
        BigDecimal newTotal = newAvailable.add(account.getFrozenBalance());
        BigDecimal newTotalCommission = account.getTotalCommission().add(commissionAmount);

        // 更新账户信息
        LambdaUpdateWrapper<WineUserAccount> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(WineUserAccount::getId, account.getId());
        updateWrapper.set(WineUserAccount::getAvailableBalance, newAvailable);
        updateWrapper.set(WineUserAccount::getTotalBalance, newTotal);
        updateWrapper.set(WineUserAccount::getTotalCommission, newTotalCommission);
        updateWrapper.set(WineUserAccount::getLastCommissionTime, new Date());
        updateWrapper.set(WineUserAccount::getRemark, remark);

        this.update(updateWrapper);
    }
} 