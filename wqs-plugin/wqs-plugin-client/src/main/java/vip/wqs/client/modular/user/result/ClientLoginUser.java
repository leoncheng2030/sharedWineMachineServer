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
package vip.wqs.client.modular.user.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import vip.wqs.auth.core.pojo.SaBaseClientLoginUser;
import vip.wqs.client.modular.user.enums.ClientUserStatusEnum;

import java.math.BigDecimal;

/**
 * C端登录用户对象
 *
 * @author xuyuxiang
 * @date 2022/4/21 19:33
 **/
@Getter
@Setter
public class ClientLoginUser extends SaBaseClientLoginUser {

    /** 微信OpenID */
    @Schema(description = "微信OpenID")
    private String wechatOpenid;

    /** 微信UnionID */
    @Schema(description = "微信UnionID")
    private String wechatUnionid;

    /** 微信昵称 */
    @Schema(description = "微信昵称")
    private String wechatNickname;

    /** 微信头像URL */
    @Schema(description = "微信头像URL")
    private String wechatAvatarUrl;

    /** 佣金账户总余额 */
    @Schema(description = "佣金账户总余额")
    private BigDecimal commissionBalance;

    /** 可用余额 */
    @Schema(description = "可用余额")
    private BigDecimal availableBalance;

    /** 冻结余额 */
    @Schema(description = "冻结余额")
    private BigDecimal frozenBalance;

    /** 佣金账户状态 */
    @Schema(description = "佣金账户状态")
    private String commissionAccountStatus;

    /** 累计佣金收入 */
    @Schema(description = "累计佣金收入")
    private BigDecimal totalCommissionIncome;

    /** 累计提现金额 */
    @Schema(description = "累计提现金额")
    private BigDecimal totalWithdrawAmount;

    /** 佣金账户创建时间 */
    @Schema(description = "佣金账户创建时间")
    private String commissionAccountCreateTime;

    /**
     * 实现是否可以登录
     *
     * @author xuyuxiang
     * @date 2022/8/15 15:27
     **/
    @Override
    public Boolean getEnabled() {
        // 仅判断状态是否正常，可自行扩展
        return this.getUserStatus().equals(ClientUserStatusEnum.ENABLE.getValue());
    }
}
