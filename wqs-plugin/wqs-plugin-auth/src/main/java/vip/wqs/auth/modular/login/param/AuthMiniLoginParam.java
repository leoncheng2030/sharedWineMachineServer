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
package vip.wqs.auth.modular.login.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 微信小程序登录参数
 *
 * @author wqs
 * @date 2025/01/29
 **/
@Getter
@Setter
public class AuthMiniLoginParam {

    /** 微信授权码 */
    @Schema(description = "微信授权码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "微信授权码不能为空")
    private String code;

    /** 设备类型 */
    @Schema(description = "设备类型")
    private String device;

    /** 用户昵称（可选，用户授权后获取） */
    @Schema(description = "用户昵称")
    private String nickName;

    /** 用户头像（可选，用户授权后获取） */
    @Schema(description = "用户头像")
    private String avatarUrl;

    /** 手机号（可选，用于绑定已有账户） */
    @Schema(description = "手机号")
    private String phone;

    /** 手机号验证码（当提供手机号时必填） */
    @Schema(description = "手机号验证码")
    private String phoneCode;

    /** 手机号授权码（新版本微信授权） */
    @Schema(description = "手机号授权码")
    private String phoneAuthCode;

    /** 手机号加密数据（传统微信授权） */
    @Schema(description = "手机号加密数据")
    private String encryptedData;

    /** 手机号解密向量（传统微信授权） */
    @Schema(description = "手机号解密向量")
    private String iv;

    /** 云开发手机号ID（云开发环境） */
    @Schema(description = "云开发手机号ID")
    private String cloudID;
}
