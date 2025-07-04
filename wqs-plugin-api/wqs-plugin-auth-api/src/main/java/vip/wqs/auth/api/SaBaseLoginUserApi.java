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
package vip.wqs.auth.api;

import cn.hutool.json.JSONObject;
import vip.wqs.auth.core.pojo.SaBaseClientLoginUser;
import vip.wqs.auth.core.pojo.SaBaseLoginUser;

import java.util.List;

/**
 * 登录用户API，由其他模块实现
 *
 * @author xuyuxiang
 * @date 2021/12/23 21:48
 */
public interface SaBaseLoginUserApi {

    /**
     * 根据id获取B端用户信息，查不到则返回null
     *
     * @author xuyuxiang
     * @date 2022/3/10 16:14
     **/
    SaBaseLoginUser getUserById(String id);

    /**
     * 根据id获取C端用户信息，查不到则返回null
     *
     * @author xuyuxiang
     * @date 2022/3/10 16:14
     **/
    SaBaseClientLoginUser getClientUserById(String id);

    /**
     * 根据账号获取B端用户信息，查不到则返回null
     *
     * @author xuyuxiang
     * @date 2022/3/10 16:14
     **/
    SaBaseLoginUser getUserByAccount(String account);

    /**
     * 根据账号获取C端用户信息，查不到则返回null
     *
     * @author xuyuxiang
     * @date 2022/3/10 16:14
     **/
    SaBaseClientLoginUser getClientUserByAccount(String account);

    /**
     * 根据手机号获取B端用户信息，查不到则返回null
     *
     * @author xuyuxiang
     * @date 2022/3/10 16:14
     **/
    SaBaseLoginUser getUserByPhone(String phone);

    /**
     * 根据邮箱获取B端用户信息，查不到则返回null
     *
     * @author xuyuxiang
     * @date 2022/3/10 16:14
     **/
    SaBaseLoginUser getUserByEmail(String email);

    /**
     * 根据手机号获取C端用户信息，查不到则返回null
     *
     * @author xuyuxiang
     * @date 2022/3/10 16:14
     **/
    SaBaseClientLoginUser getClientUserByPhone(String phone);

    /**
     * 根据邮箱获取C端用户信息，查不到则返回null
     *
     * @author xuyuxiang
     * @date 2022/3/10 16:14
     **/
    SaBaseClientLoginUser getClientUserByEmail(String email);

    /**
     * 根据用户id获取用户集合
     *
     * @author xuyuxiang
     * @date 2022/4/27 22:53
     */
    List<JSONObject> listUserByUserIdList(List<String> userIdList);

    /**
     * 根据用户id获取角色集合
     *
     * @author xuyuxiang
     * @date 2022/4/27 22:53
     */
    List<JSONObject> getRoleListByUserId(String userId);

    /**
     * 根据角色id和用户id集合获取按钮码集合
     *
     * @author xuyuxiang
     * @date 2022/4/27 22:54
     */
    List<String> getButtonCodeListListByUserAndRoleIdList(List<String> userAndRoleIdList);

    /**
     * 根据角色id和用户id集合获取移动端按钮码集合
     *
     * @author xuyuxiang
     * @date 2022/4/27 22:54
     */
    List<String> getMobileButtonCodeListListByUserIdAndRoleIdList(List<String> userAndRoleIdList);

    /**
     * 根据角色id和用户id集合获取权限集合
     *
     * @author xuyuxiang
     * @date 2022/4/27 22:54
     */
    List<JSONObject> getPermissionListByUserIdAndRoleIdList(List<String> userAndRoleIdList, String orgId);

    /**
     * 更新用户的登录时间和登录ip等信息
     *
     * @author xuyuxiang
     * @date 2022/4/27 22:57
     */
    void updateUserLoginInfo(String userId, String device);

    /**
     * 使用手机号创建B端用户
     *
     * @author xuyuxiang
     * @date 2022/3/10 16:14
     **/
    SaBaseLoginUser createUserWithPhone(String phone);

    /**
     * 使用手机号创建C端用户
     *
     * @author xuyuxiang
     * @date 2022/3/10 16:14
     **/
    SaBaseClientLoginUser createClientUserWithPhone(String phone);

    /**
     * 使用邮箱创建B端用户
     *
     * @author xuyuxiang
     * @date 2022/3/10 16:14
     **/
    SaBaseLoginUser createUserWithEmail(String email);

    /**
     * 使用邮箱创建C端用户
     *
     * @author xuyuxiang
     * @date 2022/3/10 16:14
     **/
    SaBaseClientLoginUser createClientUserWithEmail(String email);

    /**
     * 执行注册
     *
     * @author xuyuxiang
     * @date 2022/3/10 16:14
     **/
    void doRegister(String account, String password);

    /**
     * 根据微信openid获取C端用户信息，查不到则返回null
     *
     * @param openid 微信openid
     * @return C端用户信息
     * @author wqs
     * @date 2025/01/29
     */
    SaBaseClientLoginUser getClientUserByWechatOpenid(String openid);

    /**
     * 绑定微信信息到已有C端用户
     *
     * @param userId 用户ID
     * @param openid 微信openid
     * @param unionid 微信unionid
     * @param nickname 微信昵称
     * @param avatarUrl 微信头像URL
     * @author wqs
     * @date 2025/01/29
     */
    void bindWechatInfo(String userId, String openid, String unionid, String nickname, String avatarUrl);

    /**
     * 创建微信C端用户
     *
     * @param account 用户账号
     * @param nickname 用户昵称
     * @param avatarUrl 头像URL
     * @param openid 微信openid
     * @param unionid 微信unionid
     * @param phone 手机号（可为空）
     * @return 创建的用户ID
     * @author wqs
     * @date 2025/01/29
     */
    String createWechatUser(String account, String nickname, String avatarUrl, String openid, String unionid, String phone);
}
