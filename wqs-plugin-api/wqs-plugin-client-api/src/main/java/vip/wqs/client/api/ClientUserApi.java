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
package vip.wqs.client.api;

import cn.hutool.json.JSONObject;
import vip.wqs.common.pojo.CommonResult;

/**
 * 客户端用户管理Api
 *
 * @author WQS_TEAM
 * @date 2025/01/30 客户端用户管理模块开发
 **/
public interface ClientUserApi {

    /**
     * 根据用户ID获取用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     * @author WQS_TEAM
     * @date 2025/01/30
     */
    CommonResult<Object> getUserById(String userId);

    /**
     * 更新用户信息
     *
     * @param updateParam 更新参数
     * @return 更新结果
     * @author WQS_TEAM
     * @date 2025/01/30
     */
    CommonResult<Object> updateUser(Object updateParam);

    /**
     * 绑定手机号
     *
     * @param bindParam 绑定参数
     * @return 绑定结果
     * @author WQS_TEAM
     * @date 2025/01/30
     */
    CommonResult<Object> bindPhone(Object bindParam);

    /**
     * 实名认证
     *
     * @param verifyParam 认证参数
     * @return 认证结果
     * @author WQS_TEAM
     * @date 2025/01/30
     */
    CommonResult<Object> idVerify(Object verifyParam);

    /**
     * 获取用户钱包信息
     *
     * @param userId 用户ID
     * @return 钱包信息
     * @author WQS_TEAM
     * @date 2025/01/30
     */
    CommonResult<JSONObject> getUserWallet(String userId);

    /**
     * 用户充值
     *
     * @param rechargeParam 充值参数
     * @return 充值结果
     * @author WQS_TEAM
     * @date 2025/01/30
     */
    CommonResult<Object> recharge(Object rechargeParam);

    /**
     * 用户提现
     *
     * @param withdrawParam 提现参数
     * @return 提现结果
     * @author WQS_TEAM
     * @date 2025/01/30
     */
    CommonResult<Object> withdraw(Object withdrawParam);

    /**
     * 获取用户统计信息
     *
     * @param userId 用户ID
     * @return 统计信息
     * @author WQS_TEAM
     * @date 2025/01/30
     */
    CommonResult<JSONObject> getUserStats(String userId);
} 