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

import com.baomidou.mybatisplus.extension.service.IService;
import vip.wqs.client.modular.user.entity.ClientUserPassword;

import java.util.List;

/**
 * C端用户密码Service接口
 *
 * @author yubaoshan
 * @date  2024/12/21 01:25
 **/
public interface ClientUserPasswordService extends IService<ClientUserPassword> {

    /**
     * 追加用户历史密码信息
     *
     * @author xuyuxiang
     * @date 2022/4/27 21:38
     */
    void insertUserPasswordHistory(String userId, String newPassword);

    /**
     * 获取用户前N个历史密码
     *
     * @author xuyuxiang
     * @date 2022/4/27 21:38
     */
    List<String> getUserPasswordHistoryLimit(String userId, int limitValue);
}
