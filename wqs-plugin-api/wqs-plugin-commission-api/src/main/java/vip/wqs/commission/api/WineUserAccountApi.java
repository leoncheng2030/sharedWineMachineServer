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
package vip.wqs.commission.api;

import vip.wqs.commission.api.dto.WineUserAccountInfoDto;

/**
 * 用户账户API接口
 *
 * @author WQS_TEAM
 * @date 2025-01-30
 */
public interface WineUserAccountApi {

    /**
     * 创建用户账户
     *
     * @param userId 用户ID
     * @author WQS_TEAM
     * @date 2025-01-30
     */
    void createAccount(String userId);

    /**
     * 根据用户ID获取账户完整信息
     *
     * @param userId 用户ID
     * @return 用户账户信息DTO，如果不存在则返回null
     * @author WQS_TEAM
     * @date 2025-01-30
     */
    WineUserAccountInfoDto getAccountInfo(String userId);

    /**
     * 检查用户账户是否存在
     *
     * @param userId 用户ID
     * @return 是否存在
     * @author WQS_TEAM
     * @date 2025-01-30
     */
    boolean accountExists(String userId);
} 