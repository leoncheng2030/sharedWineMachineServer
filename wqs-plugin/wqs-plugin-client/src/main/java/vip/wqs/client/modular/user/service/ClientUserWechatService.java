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
package vip.wqs.client.modular.user.service;

import vip.wqs.client.modular.user.entity.ClientUser;

/**
 * 微信用户服务接口
 *
 * @author wqs
 * @date 2025/01/29
 **/
public interface ClientUserWechatService {

    /**
     * 根据微信OpenID查找用户
     *
     * @param openid 微信OpenID
     * @return 用户信息
     * @author wqs
     * @date 2025/01/29
     */
    ClientUser findUserByWechatOpenid(String openid);

    /**
     * 根据微信UnionID查找用户
     *
     * @param unionid 微信UnionID
     * @return 用户信息
     * @author wqs
     * @date 2025/01/29
     */
    ClientUser findUserByWechatUnionid(String unionid);

    /**
     * 创建微信用户
     *
     * @param sessionResult 微信会话信息
     * @param nickName 用户昵称（可选）
     * @param avatarUrl 用户头像（可选）
     * @return 用户信息
     * @author wqs
     * @date 2025/01/29
     */
    ClientUser createWechatUser(Object sessionResult, String nickName, String avatarUrl);

    /**
     * 绑定微信用户
     *
     * @param userId 用户ID
     * @param sessionResult 微信会话信息
     * @param nickName 用户昵称（可选）
     * @param avatarUrl 用户头像（可选）
     * @return 绑定结果
     * @author wqs
     * @date 2025/01/29
     */
    boolean bindWechatUser(String userId, Object sessionResult, String nickName, String avatarUrl);

    /**
     * 解绑微信用户
     *
     * @param userId 用户ID
     * @return 解绑结果
     * @author wqs
     * @date 2025/01/29
     */
    boolean unbindWechatUser(String userId);

    /**
     * 更新微信用户信息
     *
     * @param userId 用户ID
     * @param nickName 用户昵称
     * @param avatarUrl 用户头像
     * @return 更新结果
     * @author wqs
     * @date 2025/01/29
     */
    boolean updateWechatUserInfo(String userId, String nickName, String avatarUrl);

    /**
     * 检查微信OpenID是否已绑定
     *
     * @param openid 微信OpenID
     * @return 是否已绑定
     * @author wqs
     * @date 2025/01/29
     */
    boolean isWechatOpenidBound(String openid);

    /**
     * 记录微信登录日志
     *
     * @param userId 用户ID
     * @param openid 微信OpenID
     * @param loginType 登录类型
     * @param device 登录设备
     * @param ip 登录IP
     * @param address 登录地址
     * @author wqs
     * @date 2025/01/29
     */
    void recordWechatLoginLog(String userId, String openid, String loginType, String device, String ip, String address);
} 