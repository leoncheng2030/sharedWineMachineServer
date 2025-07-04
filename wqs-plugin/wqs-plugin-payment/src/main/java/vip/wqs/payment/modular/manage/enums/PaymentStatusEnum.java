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
 * 支付状态枚举
 *
 * @author wqs
 * @date 2025/01/30 16:10
 **/
@Getter
public enum PaymentStatusEnum {

    /** 待支付 */
    PENDING("PENDING", "待支付"),

    /** 支付成功 */
    SUCCESS("SUCCESS", "支付成功"),

    /** 支付失败 */
    FAILED("FAILED", "支付失败"),

    /** 已取消 */
    CANCELLED("CANCELLED", "已取消"),

    /** 已退款 */
    REFUNDED("REFUNDED", "已退款"),

    /** 部分退款 */
    PARTIAL_REFUNDED("PARTIAL_REFUNDED", "部分退款"),

    /** 已过期 */
    EXPIRED("EXPIRED", "已过期");

    private final String code;
    private final String message;

    PaymentStatusEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 根据状态码获取枚举
     */
    public static PaymentStatusEnum getByCode(String code) {
        for (PaymentStatusEnum status : PaymentStatusEnum.values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }

    /**
     * 检查状态流转是否合法
     */
    public static boolean isValidTransition(PaymentStatusEnum from, PaymentStatusEnum to) {
        if (from == null || to == null) {
            return false;
        }

        switch (from) {
            case PENDING:
                return to == SUCCESS || to == FAILED || to == CANCELLED || to == EXPIRED;
            case SUCCESS:
                return to == REFUNDED || to == PARTIAL_REFUNDED;
            case FAILED:
            case CANCELLED:
            case EXPIRED:
                return false; // 终态不能再转换
            case REFUNDED:
            case PARTIAL_REFUNDED:
                return to == REFUNDED && from == PARTIAL_REFUNDED; // 部分退款可以转为全额退款
            default:
                return false;
        }
    }

    /**
     * 判断是否为终态
     */
    public boolean isFinalStatus() {
        return this == SUCCESS || this == FAILED || this == CANCELLED || 
               this == REFUNDED || this == EXPIRED;
    }

    /**
     * 判断是否为成功状态
     */
    public boolean isSuccessStatus() {
        return this == SUCCESS;
    }

    /**
     * 判断是否为失败状态
     */
    public boolean isFailedStatus() {
        return this == FAILED || this == CANCELLED || this == EXPIRED;
    }
} 