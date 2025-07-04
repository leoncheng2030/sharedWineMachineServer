/*
 * Copyright [2022] [https://www.wqs.vip]
 *
 * WQS采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改WQS源码头部的版权声明。
 * 3.本项目代码可免费商业使用，商业使用请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://www.wqs.vip
 * 5.不可二次分发开源参与同类竞品，如有想法可联系团队wqs@wqs.vip商议合作。
 * 6.若您的项目无法满足以上几点，需要更多功能代码，获取WQS商业授权许可，请在官网购买授权，地址为 https://www.wqs.vip
 */
package vip.wqs.payment.modular.manage.enums;

import lombok.Getter;

/**
 * 支付方式枚举
 *
 * @author wqs
 * @date 2025/01/30 16:15
 **/
@Getter
public enum PaymentTypeEnum {

    /** 微信小程序支付 */
    WECHAT_MINI("WECHAT_MINI", "微信小程序支付"),

    /** 微信H5支付 */
    WECHAT_H5("WECHAT_H5", "微信H5支付"),

    /** 微信APP支付 */
    WECHAT_APP("WECHAT_APP", "微信APP支付"),

    /** 微信扫码支付 */
    WECHAT_NATIVE("WECHAT_NATIVE", "微信扫码支付"),

    /** 支付宝支付 */
    ALIPAY("ALIPAY", "支付宝支付"),

    /** 支付宝H5支付 */
    ALIPAY_H5("ALIPAY_H5", "支付宝H5支付"),

    /** 支付宝APP支付 */
    ALIPAY_APP("ALIPAY_APP", "支付宝APP支付"),

    /** 余额支付 */
    BALANCE("BALANCE", "余额支付"),

    /** 银行卡支付 */
    BANK_CARD("BANK_CARD", "银行卡支付");

    private final String code;
    private final String message;

    PaymentTypeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 根据支付方式码获取枚举
     */
    public static PaymentTypeEnum getByCode(String code) {
        for (PaymentTypeEnum type : PaymentTypeEnum.values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }

    /**
     * 判断是否为微信支付
     */
    public boolean isWechatPay() {
        return this == WECHAT_MINI || this == WECHAT_H5 || 
               this == WECHAT_APP || this == WECHAT_NATIVE;
    }

    /**
     * 判断是否为支付宝支付
     */
    public boolean isAlipay() {
        return this == ALIPAY || this == ALIPAY_H5 || this == ALIPAY_APP;
    }

    /**
     * 判断是否为第三方支付
     */
    public boolean isThirdPartyPay() {
        return isWechatPay() || isAlipay();
    }

    /**
     * 判断是否为内部支付
     */
    public boolean isInternalPay() {
        return this == BALANCE || this == BANK_CARD;
    }

    /**
     * 获取微信支付交易类型
     */
    public String getWechatTradeType() {
        switch (this) {
            case WECHAT_MINI:
                return "JSAPI";
            case WECHAT_H5:
                return "MWEB";
            case WECHAT_APP:
                return "APP";
            case WECHAT_NATIVE:
                return "NATIVE";
            default:
                return null;
        }
    }
} 