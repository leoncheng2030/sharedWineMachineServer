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
package vip.wqs.commission.modular.account.provider;

import cn.hutool.core.util.ObjectUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import vip.wqs.commission.api.WineUserAccountApi;
import vip.wqs.commission.api.dto.WineUserAccountInfoDto;
import vip.wqs.commission.modular.account.entity.WineUserAccount;
import vip.wqs.commission.modular.account.service.WineUserAccountService;

/**
 * 用户账户API接口实现类
 *
 * @author WQS_TEAM
 * @date 2025-01-30
 */
@Service
public class WineUserAccountApiProvider implements WineUserAccountApi {

    @Resource
    private WineUserAccountService wineUserAccountService;

    @Override
    public void createAccount(String userId) {
        try {
            wineUserAccountService.createAccount(userId);
        } catch (Exception e) {
            // 如果账户已存在，不抛出异常
            if (e.getMessage() != null && e.getMessage().contains("用户账户已存在")) {
                // 账户已存在，直接返回
                return;
            }
            // 其他异常继续抛出
            throw e;
        }
    }

    @Override
    public WineUserAccountInfoDto getAccountInfo(String userId) {
        WineUserAccount account = wineUserAccountService.getByUserId(userId);
        if (ObjectUtil.isNull(account)) {
            return null;
        }
        
        // 手动映射字段，确保字段名称匹配
        WineUserAccountInfoDto dto = new WineUserAccountInfoDto();
        dto.setAccountId(account.getId());
        dto.setUserId(account.getUserId());
        dto.setTotalBalance(account.getTotalBalance());
        dto.setAvailableBalance(account.getAvailableBalance());
        dto.setFrozenBalance(account.getFrozenBalance());
        dto.setTotalCommission(account.getTotalCommission());
        dto.setTotalWithdraw(account.getTotalWithdraw());
        dto.setStatus(account.getStatus());
        dto.setLastCommissionTime(account.getLastCommissionTime());
        dto.setLastWithdrawTime(account.getLastWithdrawTime());
        dto.setCreateTime(account.getCreateTime());
        dto.setRemark(account.getRemark());
        
        return dto;
    }

    @Override
    public boolean accountExists(String userId) {
        WineUserAccount account = wineUserAccountService.getByUserId(userId);
        return ObjectUtil.isNotNull(account);
    }
} 