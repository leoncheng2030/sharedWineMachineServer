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
package vip.wqs.payment.api;

import cn.hutool.json.JSONObject;

/**
 * 支付配置Api
 *
 * @author WQS_TEAM
 * @date 2025/01/30 支付管理模块开发
 **/
public interface PaymentConfigApi {

    /**
     * 根据支付方式获取支付配置
     *
     * @param payType 支付方式
     * @return 支付配置信息
     * @author WQS_TEAM
     * @date 2025/01/30
     */
    JSONObject getPaymentConfigByType(String payType);

    /**
     * 获取所有可用的支付配置
     *
     * @return 支付配置列表
     * @author WQS_TEAM
     * @date 2025/01/30
     */
    JSONObject getAllAvailablePaymentConfigs();

    /**
     * 检查支付方式是否可用
     *
     * @param payType 支付方式
     * @return 是否可用
     * @author WQS_TEAM
     * @date 2025/01/30
     */
    Boolean isPaymentTypeAvailable(String payType);

    /**
     * 获取支付方式的回调URL
     *
     * @param payType 支付方式
     * @return 回调URL
     * @author WQS_TEAM
     * @date 2025/01/30
     */
    String getNotifyUrl(String payType);

    /**
     * 获取支付方式的退款回调URL
     *
     * @param payType 支付方式
     * @return 退款回调URL
     * @author WQS_TEAM
     * @date 2025/01/30
     */
    String getRefundNotifyUrl(String payType);
} 