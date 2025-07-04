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
package vip.wqs.order.modular.record.enums;

import lombok.Getter;

/**
 * 订单状态枚举
 *
 * @author wqs
 * @date 2025/01/30 15:30
 **/
@Getter
public enum OrderStatusEnum {

    /** 待支付 */
    PENDING("PENDING", "待支付"),

    /** 待取酒 */
    DISPENSING("DISPENSING", "待取酒"),

    /** 已完成 */
    COMPLETED("COMPLETED", "已完成"),

    /** 已取消 */
    CANCELLED("CANCELLED", "已取消");

    private final String code;
    private final String message;

    OrderStatusEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 根据状态码获取枚举
     */
    public static OrderStatusEnum getByCode(String code) {
        for (OrderStatusEnum status : OrderStatusEnum.values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }

    /**
     * 检查状态流转是否合法
     */
    public static boolean isValidTransition(OrderStatusEnum from, OrderStatusEnum to) {
        if (from == null || to == null) {
            return false;
        }

        switch (from) {
            case PENDING:
                return to == DISPENSING || to == CANCELLED; // 待支付 -> 待取酒 或 已取消
            case DISPENSING:
                return to == COMPLETED; // 待取酒 -> 已完成
            case COMPLETED:
            case CANCELLED:
                return false; // 终态不能再转换
            default:
                return false;
        }
    }
} 