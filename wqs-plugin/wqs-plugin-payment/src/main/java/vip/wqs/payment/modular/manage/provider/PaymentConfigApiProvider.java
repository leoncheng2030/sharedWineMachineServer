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
package vip.wqs.payment.modular.manage.provider;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vip.wqs.payment.api.PaymentConfigApi;
import vip.wqs.payment.modular.manage.entity.PaymentConfig;
import vip.wqs.payment.modular.manage.service.PaymentConfigService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 支付配置Api实现类
 *
 * @author WQS_TEAM
 * @date 2025/01/30 支付管理模块开发
 **/
@Slf4j
@Service
public class PaymentConfigApiProvider implements PaymentConfigApi {

    @Resource
    private PaymentConfigService paymentConfigService;

    @Override
    public JSONObject getPaymentConfigByType(String payType) {
        try {
            PaymentConfig config = paymentConfigService.getByPayType(payType);
            if (ObjectUtil.isNull(config)) {
                log.warn("未找到支付配置，支付方式：{}", payType);
                return new JSONObject();
            }

            JSONObject result = new JSONObject();
            result.set("id", config.getId());
            result.set("configName", config.getConfigName());
            result.set("payType", config.getPayType());
            result.set("status", config.getStatus());
            result.set("sortCode", config.getSortCode());
            result.set("remark", config.getRemark());

            // 解析扩展配置JSON
            if (StrUtil.isNotBlank(config.getExtJson())) {
                JSONObject extConfig = JSONUtil.parseObj(config.getExtJson());
                result.set("record", extConfig);
            } else {
                result.set("record", new JSONObject());
            }

            return result;
        } catch (Exception e) {
            log.error("获取支付配置失败，支付方式：{}，错误信息：{}", payType, e.getMessage(), e);
            return new JSONObject();
        }
    }

    @Override
    public JSONObject getAllAvailablePaymentConfigs() {
        try {
            List<PaymentConfig> configs = paymentConfigService.getEnabledConfigs();
            
            JSONObject result = new JSONObject();
            result.set("total", configs.size());
            
            List<JSONObject> configList = configs.stream().map(config -> {
                JSONObject item = new JSONObject();
                item.set("id", config.getId());
                item.set("configName", config.getConfigName());
                item.set("payType", config.getPayType());
                item.set("status", config.getStatus());
                item.set("sortCode", config.getSortCode());
                item.set("remark", config.getRemark());
                
                // 解析扩展配置JSON
                if (StrUtil.isNotBlank(config.getExtJson())) {
                    JSONObject extConfig = JSONUtil.parseObj(config.getExtJson());
                    item.set("record", extConfig);
                } else {
                    item.set("record", new JSONObject());
                }
                
                return item;
            }).collect(Collectors.toList());
            
            result.set("configs", configList);
            return result;
        } catch (Exception e) {
            log.error("获取所有可用支付配置失败，错误信息：{}", e.getMessage(), e);
            return new JSONObject();
        }
    }

    @Override
    public Boolean isPaymentTypeAvailable(String payType) {
        try {
            PaymentConfig config = paymentConfigService.getByPayType(payType);
            return ObjectUtil.isNotNull(config) && "ENABLE".equals(config.getStatus());
        } catch (Exception e) {
            log.error("检查支付方式可用性失败，支付方式：{}，错误信息：{}", payType, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public String getNotifyUrl(String payType) {
        try {
            JSONObject config = getPaymentConfigByType(payType);
            if (config.isEmpty()) {
                return null;
            }
            
            JSONObject extConfig = config.getJSONObject("record");
            if (ObjectUtil.isNull(extConfig)) {
                return null;
            }
            
            return extConfig.getStr("notifyUrl");
        } catch (Exception e) {
            log.error("获取支付回调URL失败，支付方式：{}，错误信息：{}", payType, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public String getRefundNotifyUrl(String payType) {
        try {
            JSONObject config = getPaymentConfigByType(payType);
            if (config.isEmpty()) {
                return null;
            }
            
            JSONObject extConfig = config.getJSONObject("record");
            if (ObjectUtil.isNull(extConfig)) {
                return null;
            }
            
            return extConfig.getStr("refundNotifyUrl");
        } catch (Exception e) {
            log.error("获取退款回调URL失败，支付方式：{}，错误信息：{}", payType, e.getMessage(), e);
            return null;
        }
    }
} 