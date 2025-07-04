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
package vip.wqs.payment.modular.manage.util;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import vip.wqs.payment.modular.manage.entity.PaymentConfig;

/**
 * 支付配置工具类
 *
 * @author WQS_TEAM
 * @date 2025/01/30 支付管理模块开发
 **/
public class PaymentConfigUtil {

    /**
     * 从支付配置中获取JSON配置对象
     *
     * @param config 支付配置
     * @return JSON配置对象
     */
    public static JSONObject getExtConfig(PaymentConfig config) {
        if (ObjectUtil.isNull(config) || StrUtil.isBlank(config.getExtJson())) {
            return new JSONObject();
        }
        
        try {
            return JSONUtil.parseObj(config.getExtJson());
        } catch (Exception e) {
            return new JSONObject();
        }
    }

    /**
     * 从支付配置中获取字符串配置值
     *
     * @param config 支付配置
     * @param key 配置键
     * @return 配置值
     */
    public static String getConfigStr(PaymentConfig config, String key) {
        JSONObject extConfig = getExtConfig(config);
        return extConfig.getStr(key);
    }

    /**
     * 从支付配置中获取字符串配置值（带默认值）
     *
     * @param config 支付配置
     * @param key 配置键
     * @param defaultValue 默认值
     * @return 配置值
     */
    public static String getConfigStr(PaymentConfig config, String key, String defaultValue) {
        String value = getConfigStr(config, key);
        return StrUtil.isNotBlank(value) ? value : defaultValue;
    }

    /**
     * 从支付配置中获取整数配置值
     *
     * @param config 支付配置
     * @param key 配置键
     * @return 配置值
     */
    public static Integer getConfigInt(PaymentConfig config, String key) {
        JSONObject extConfig = getExtConfig(config);
        return extConfig.getInt(key);
    }

    /**
     * 从支付配置中获取整数配置值（带默认值）
     *
     * @param config 支付配置
     * @param key 配置键
     * @param defaultValue 默认值
     * @return 配置值
     */
    public static Integer getConfigInt(PaymentConfig config, String key, Integer defaultValue) {
        Integer value = getConfigInt(config, key);
        return ObjectUtil.isNotNull(value) ? value : defaultValue;
    }

    /**
     * 从支付配置中获取布尔配置值
     *
     * @param config 支付配置
     * @param key 配置键
     * @return 配置值
     */
    public static Boolean getConfigBool(PaymentConfig config, String key) {
        JSONObject extConfig = getExtConfig(config);
        return extConfig.getBool(key);
    }

    /**
     * 从支付配置中获取布尔配置值（带默认值）
     *
     * @param config 支付配置
     * @param key 配置键
     * @param defaultValue 默认值
     * @return 配置值
     */
    public static Boolean getConfigBool(PaymentConfig config, String key, Boolean defaultValue) {
        Boolean value = getConfigBool(config, key);
        return ObjectUtil.isNotNull(value) ? value : defaultValue;
    }

    /**
     * 检查是否为沙箱模式
     *
     * @param config 支付配置
     * @return 是否为沙箱模式
     */
    public static boolean isSandboxMode(PaymentConfig config) {
        return getConfigInt(config, "sandboxMode", 0) == 1;
    }

    /**
     * 获取应用ID
     *
     * @param config 支付配置
     * @return 应用ID
     */
    public static String getAppId(PaymentConfig config) {
        return getConfigStr(config, "appId");
    }

    /**
     * 获取商户号
     *
     * @param config 支付配置
     * @return 商户号
     */
    public static String getMerchantId(PaymentConfig config) {
        return getConfigStr(config, "merchantId");
    }

    /**
     * 获取商户密钥
     *
     * @param config 支付配置
     * @return 商户密钥
     */
    public static String getMerchantKey(PaymentConfig config) {
        return getConfigStr(config, "merchantKey");
    }

    /**
     * 获取应用密钥
     *
     * @param config 支付配置
     * @return 应用密钥
     */
    public static String getAppSecret(PaymentConfig config) {
        return getConfigStr(config, "appSecret");
    }

    /**
     * 获取私钥文件路径
     *
     * @param config 支付配置
     * @return 私钥文件路径
     */
    public static String getPrivateKeyPath(PaymentConfig config) {
        return getConfigStr(config, "privateKeyPath");
    }

    /**
     * 获取证书文件路径
     *
     * @param config 支付配置
     * @return 证书文件路径
     */
    public static String getCertPath(PaymentConfig config) {
        return getConfigStr(config, "certPath");
    }

    /**
     * 获取证书序列号
     *
     * @param config 支付配置
     * @return 证书序列号
     */
    public static String getCertSerialNo(PaymentConfig config) {
        return getConfigStr(config, "certSerialNo");
    }

    /**
     * 获取支付回调URL
     *
     * @param config 支付配置
     * @return 支付回调URL
     */
    public static String getNotifyUrl(PaymentConfig config) {
        return getConfigStr(config, "notifyUrl");
    }

    /**
     * 获取退款回调URL
     *
     * @param config 支付配置
     * @return 退款回调URL
     */
    public static String getRefundNotifyUrl(PaymentConfig config) {
        return getConfigStr(config, "refundNotifyUrl");
    }

    /**
     * 获取支付成功跳转URL
     *
     * @param config 支付配置
     * @return 支付成功跳转URL
     */
    public static String getReturnUrl(PaymentConfig config) {
        return getConfigStr(config, "returnUrl");
    }
} 