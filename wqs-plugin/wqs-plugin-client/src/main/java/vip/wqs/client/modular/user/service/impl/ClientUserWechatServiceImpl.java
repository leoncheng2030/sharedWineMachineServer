/*
 * Copyright [2022] [https://www.xiaonuo.vip]
 *
 * Snowy采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改Snowy源码头部的版权声明。
 * 3.本项目代码可免费商业使用，商业使用请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://www.xiaonuo.vip
 * 5.不可二次分发开源参与同类竞品，如有想法可联系团队xiaonuobase@qq.com商议合作。
 * 6.若您的项目无法满足以上几点，需要更多功能代码，获取Snowy商业授权许可，请在官网购买授权，地址为 https://www.xiaonuo.vip
 */
package vip.wqs.client.modular.user.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.wqs.client.modular.user.entity.ClientUser;
import vip.wqs.client.modular.user.mapper.ClientUserMapper;
import vip.wqs.client.modular.user.service.ClientUserWechatService;

import java.util.Date;

/**
 * 微信用户服务实现类
 *
 * @author wqs
 * @date 2025/01/29
 **/
@Slf4j
@Service
public class ClientUserWechatServiceImpl implements ClientUserWechatService {

    @Resource
    private ClientUserMapper clientUserMapper;

    @Override
    public ClientUser findUserByWechatOpenid(String openid) {
        if (StrUtil.isBlank(openid)) {
            return null;
        }
        
        LambdaQueryWrapper<ClientUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ClientUser::getWechatOpenid, openid)
                   .eq(ClientUser::getDeleteFlag, "N");
        
        return clientUserMapper.selectOne(queryWrapper);
    }

    @Override
    public ClientUser findUserByWechatUnionid(String unionid) {
        if (StrUtil.isBlank(unionid)) {
            return null;
        }
        
        LambdaQueryWrapper<ClientUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ClientUser::getWechatUnionid, unionid)
                   .eq(ClientUser::getDeleteFlag, "N");
        
        return clientUserMapper.selectOne(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ClientUser createWechatUser(Object sessionResult, String nickName, String avatarUrl) {
        // 临时实现，需要根据实际的WxMaJscode2SessionResult类型调整
        String openid = "temp_openid_" + System.currentTimeMillis();
        String unionid = null;
        
        ClientUser clientUser = new ClientUser();
        clientUser.setId(IdUtil.fastSimpleUUID());
        clientUser.setAccount("wx_" + openid);
        clientUser.setName(StrUtil.isNotBlank(nickName) ? nickName : "微信用户");
        clientUser.setNickname(nickName);
        clientUser.setWechatOpenid(openid);
        clientUser.setWechatUnionid(unionid);
        clientUser.setWechatNickname(nickName);
        clientUser.setWechatAvatarUrl(avatarUrl);
        clientUser.setWechatBindTime(new Date());
        clientUser.setIsWechatUser("Y");
        clientUser.setUserStatus("ENABLE");
        clientUser.setDeleteFlag("NOT_DELETE");
        clientUser.setCreateTime(new Date());
        
        int result = clientUserMapper.insert(clientUser);
        if (result > 0) {
            log.info("创建微信用户成功，openid: {}", openid);
            return clientUser;
        } else {
            log.error("创建微信用户失败，openid: {}", openid);
            return null;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean bindWechatUser(String userId, Object sessionResult, String nickName, String avatarUrl) {
        if (StrUtil.isBlank(userId)) {
            return false;
        }
        
        // 临时实现，需要根据实际的WxMaJscode2SessionResult类型调整
        String openid = "temp_openid_" + System.currentTimeMillis();
        String unionid = null;
        
        ClientUser clientUser = clientUserMapper.selectById(userId);
        if (clientUser == null) {
            log.error("用户不存在，无法绑定微信，userId: {}", userId);
            return false;
        }
        
        // 检查OpenID是否已被其他用户绑定
        if (isWechatOpenidBound(openid)) {
            log.error("微信OpenID已被其他用户绑定，openid: {}", openid);
            return false;
        }
        
        clientUser.setWechatOpenid(openid);
        clientUser.setWechatUnionid(unionid);
        clientUser.setWechatNickname(nickName);
        clientUser.setWechatAvatarUrl(avatarUrl);
        clientUser.setWechatBindTime(new Date());
        clientUser.setIsWechatUser("Y");
        clientUser.setUpdateTime(new Date());
        
        int result = clientUserMapper.updateById(clientUser);
        if (result > 0) {
            log.info("绑定微信用户成功，userId: {}, openid: {}", userId, openid);
            return true;
        } else {
            log.error("绑定微信用户失败，userId: {}, openid: {}", userId, openid);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unbindWechatUser(String userId) {
        if (StrUtil.isBlank(userId)) {
            return false;
        }
        
        ClientUser clientUser = clientUserMapper.selectById(userId);
        if (clientUser == null) {
            log.error("用户不存在，无法解绑微信，userId: {}", userId);
            return false;
        }
        
        clientUser.setWechatOpenid(null);
        clientUser.setWechatUnionid(null);
        clientUser.setWechatNickname(null);
        clientUser.setWechatAvatarUrl(null);
        clientUser.setWechatBindTime(null);
        clientUser.setIsWechatUser("N");
        clientUser.setUpdateTime(new Date());
        
        int result = clientUserMapper.updateById(clientUser);
        if (result > 0) {
            log.info("解绑微信用户成功，userId: {}", userId);
            return true;
        } else {
            log.error("解绑微信用户失败，userId: {}", userId);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateWechatUserInfo(String userId, String nickName, String avatarUrl) {
        if (StrUtil.isBlank(userId)) {
            return false;
        }
        
        ClientUser clientUser = clientUserMapper.selectById(userId);
        if (clientUser == null) {
            log.error("用户不存在，无法更新微信信息，userId: {}", userId);
            return false;
        }
        
        if (StrUtil.isNotBlank(nickName)) {
            clientUser.setWechatNickname(nickName);
            if (StrUtil.isBlank(clientUser.getNickname())) {
                clientUser.setNickname(nickName);
            }
        }
        
        if (StrUtil.isNotBlank(avatarUrl)) {
            clientUser.setWechatAvatarUrl(avatarUrl);
        }
        
        clientUser.setUpdateTime(new Date());
        
        int result = clientUserMapper.updateById(clientUser);
        if (result > 0) {
            log.info("更新微信用户信息成功，userId: {}", userId);
            return true;
        } else {
            log.error("更新微信用户信息失败，userId: {}", userId);
            return false;
        }
    }

    @Override
    public boolean isWechatOpenidBound(String openid) {
        if (StrUtil.isBlank(openid)) {
            return false;
        }
        
        ClientUser user = findUserByWechatOpenid(openid);
        return ObjectUtil.isNotNull(user);
    }

    @Override
    public void recordWechatLoginLog(String userId, String openid, String loginType, String device, String ip, String address) {
        // TODO: 实现微信登录日志记录
        log.info("记录微信登录日志 - userId: {}, openid: {}, loginType: {}, device: {}, ip: {}, address: {}", 
                userId, openid, loginType, device, ip, address);
    }
} 