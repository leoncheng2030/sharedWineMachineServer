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
package vip.wqs.payment.modular.wechat.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult;
import com.github.binarywang.wxpay.bean.order.WxPayAppOrderResult;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.*;
import com.github.binarywang.wxpay.bean.result.*;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vip.wqs.common.exception.CommonException;
import vip.wqs.payment.modular.manage.entity.PaymentConfig;
import vip.wqs.payment.modular.manage.enums.PaymentTypeEnum;
import vip.wqs.payment.modular.manage.service.PaymentConfigService;
import vip.wqs.payment.modular.manage.util.PaymentConfigUtil;
import vip.wqs.payment.modular.wechat.param.WechatPayCreateParam;
import vip.wqs.payment.modular.wechat.param.WechatPayQueryParam;
import vip.wqs.payment.modular.wechat.param.WechatRefundParam;
import vip.wqs.payment.modular.wechat.result.WechatPayCreateResult;
import vip.wqs.payment.modular.wechat.result.WechatPayQueryResult;
import vip.wqs.payment.modular.wechat.result.WechatRefundResult;
import vip.wqs.payment.modular.wechat.service.WechatPayService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 微信支付服务实现类
 *
 * @author wqs
 * @date 2025/01/30 16:30
 **/
@Slf4j
@Service
public class WechatPayServiceImpl implements WechatPayService {

    @Resource
    private PaymentConfigService paymentConfigService;

    /** 微信支付服务缓存 */
    private final ConcurrentHashMap<String, WxPayService> wxPayServiceCache = new ConcurrentHashMap<>();

    /**
     * 获取微信支付服务
     */
    private WxPayService getWxPayService(String payType) {
        return wxPayServiceCache.computeIfAbsent(payType, key -> {
            try {
                PaymentConfig config = paymentConfigService.getByPayType(payType);
                if (ObjectUtil.isNull(config)) {
                    throw new CommonException("未找到支付配置：" + payType);
                }
                
                // 构建微信支付配置
                WxPayConfig wxPayConfig = new WxPayConfig();
                wxPayConfig.setAppId(PaymentConfigUtil.getAppId(config));
                wxPayConfig.setMchId(PaymentConfigUtil.getMerchantId(config));
                wxPayConfig.setMchKey(PaymentConfigUtil.getMerchantKey(config));
                wxPayConfig.setKeyPath(PaymentConfigUtil.getPrivateKeyPath(config));
                wxPayConfig.setNotifyUrl(PaymentConfigUtil.getNotifyUrl(config));
                wxPayConfig.setSignType(WxPayConstants.SignType.MD5);
                wxPayConfig.setUseSandboxEnv(PaymentConfigUtil.isSandboxMode(config));
                
                // 创建微信支付服务
                WxPayService wxPayService = new WxPayServiceImpl();
                wxPayService.setConfig(wxPayConfig);
                
                return wxPayService;
            } catch (Exception e) {
                log.error("获取微信支付配置失败：{}", e.getMessage(), e);
                throw new CommonException("获取微信支付配置失败：" + e.getMessage());
            }
        });
    }

    /**
     * 获取微信支付配置信息
     */
    private PaymentConfig getPaymentConfig(String payType) {
        PaymentConfig config = paymentConfigService.getByPayType(payType);
        if (ObjectUtil.isNull(config)) {
            throw new CommonException("未找到支付配置：" + payType);
        }
        return config;
    }



    @Override
    public WechatPayCreateResult createPayOrder(WechatPayCreateParam wechatPayCreateParam) {
        try {
            log.info("创建微信支付订单，参数：{}", JSONUtil.toJsonStr(wechatPayCreateParam));
            
            PaymentTypeEnum paymentType = PaymentTypeEnum.getByCode(wechatPayCreateParam.getPayType());
            if (ObjectUtil.isNull(paymentType) || !paymentType.isWechatPay()) {
                throw new CommonException("不支持的支付方式：" + wechatPayCreateParam.getPayType());
            }

            switch (paymentType) {
                case WECHAT_MINI:
                    return createJsapiPayOrder(wechatPayCreateParam);
                case WECHAT_H5:
                    return createH5PayOrder(wechatPayCreateParam);
                case WECHAT_APP:
                    return createAppPayOrder(wechatPayCreateParam);
                case WECHAT_NATIVE:
                    return createNativePayOrder(wechatPayCreateParam);
                default:
                    throw new CommonException("不支持的微信支付方式：" + paymentType.getMessage());
            }
        } catch (Exception e) {
            log.error("创建微信支付订单失败：{}", e.getMessage(), e);
            throw new CommonException("创建微信支付订单失败：" + e.getMessage());
        }
    }

    /**
     * 创建JSAPI支付订单（小程序支付）
     */
    private WechatPayCreateResult createJsapiPayOrder(WechatPayCreateParam param) {
        try {
            WxPayService wxPayService = getWxPayService(param.getPayType());
            
            // 构建统一下单请求
            WxPayUnifiedOrderRequest request = new WxPayUnifiedOrderRequest();
            request.setOutTradeNo(param.getOutTradeNo());
            request.setBody(param.getDescription());
            request.setTotalFee(param.getTotalAmount().multiply(new BigDecimal("100")).intValue()); // 转换为分
            request.setTradeType(WxPayConstants.TradeType.JSAPI);
            request.setOpenid(param.getOpenid());
            request.setSpbillCreateIp(StrUtil.isNotBlank(param.getSpbillCreateIp()) ? param.getSpbillCreateIp() : "127.0.0.1");
            
            // 设置附加数据
            if (StrUtil.isNotBlank(param.getAttach())) {
                request.setAttach(param.getAttach());
            }
            
            // 设置订单过期时间
            if (ObjectUtil.isNotNull(param.getTimeExpire())) {
                LocalDateTime expireTime = LocalDateTime.now().plusMinutes(param.getTimeExpire());
                request.setTimeExpire(expireTime.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
            }
            
            // 调用统一下单API
            WxPayUnifiedOrderResult unifiedOrderResult = wxPayService.unifiedOrder(request);
            
            // 构建小程序支付参数 - 修复API调用
            WxPayMpOrderResult mpOrderResult = wxPayService.createOrder(request);
            
            // 构建返回结果
            WechatPayCreateResult result = new WechatPayCreateResult();
            result.setPrepayId(unifiedOrderResult.getPrepayId());
            result.setTimeStamp(mpOrderResult.getTimeStamp());
            result.setNonceStr(mpOrderResult.getNonceStr());
            result.setPackageValue(mpOrderResult.getPackageValue());
            result.setSignType(mpOrderResult.getSignType());
            result.setPaySign(mpOrderResult.getPaySign());
            
            log.info("小程序支付订单创建成功，prepayId：{}", unifiedOrderResult.getPrepayId());
            return result;
        } catch (Exception e) {
            log.error("创建小程序支付订单失败：{}", e.getMessage(), e);
            throw new CommonException("创建小程序支付订单失败：" + e.getMessage());
        }
    }

    /**
     * 创建H5支付订单
     */
    private WechatPayCreateResult createH5PayOrder(WechatPayCreateParam param) {
        try {
            WxPayService wxPayService = getWxPayService(param.getPayType());
            
            // 构建统一下单请求
            WxPayUnifiedOrderRequest request = new WxPayUnifiedOrderRequest();
            request.setOutTradeNo(param.getOutTradeNo());
            request.setBody(param.getDescription());
            request.setTotalFee(param.getTotalAmount().multiply(new BigDecimal("100")).intValue());
            request.setTradeType(WxPayConstants.TradeType.MWEB);
            request.setSpbillCreateIp(StrUtil.isNotBlank(param.getSpbillCreateIp()) ? param.getSpbillCreateIp() : "127.0.0.1");
            
            // 设置附加数据
            if (StrUtil.isNotBlank(param.getAttach())) {
                request.setAttach(param.getAttach());
            }
            
            // 设置订单过期时间
            if (ObjectUtil.isNotNull(param.getTimeExpire())) {
                LocalDateTime expireTime = LocalDateTime.now().plusMinutes(param.getTimeExpire());
                request.setTimeExpire(expireTime.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
            }
            
            // 调用统一下单API
            WxPayUnifiedOrderResult unifiedOrderResult = wxPayService.unifiedOrder(request);
            
            // 构建返回结果
            WechatPayCreateResult result = new WechatPayCreateResult();
            result.setPrepayId(unifiedOrderResult.getPrepayId());
            result.setH5Url(unifiedOrderResult.getMwebUrl());
            
            log.info("H5支付订单创建成功，prepayId：{}", unifiedOrderResult.getPrepayId());
            return result;
        } catch (Exception e) {
            log.error("创建H5支付订单失败：{}", e.getMessage(), e);
            throw new CommonException("创建H5支付订单失败：" + e.getMessage());
        }
    }

    /**
     * 创建APP支付订单
     */
    private WechatPayCreateResult createAppPayOrder(WechatPayCreateParam param) {
        try {
            WxPayService wxPayService = getWxPayService(param.getPayType());
            PaymentConfig paymentConfig = getPaymentConfig(param.getPayType());
            
            // 构建统一下单请求
            WxPayUnifiedOrderRequest request = new WxPayUnifiedOrderRequest();
            request.setOutTradeNo(param.getOutTradeNo());
            request.setBody(param.getDescription());
            request.setTotalFee(param.getTotalAmount().multiply(new BigDecimal("100")).intValue());
            request.setTradeType(WxPayConstants.TradeType.APP);
            request.setSpbillCreateIp(StrUtil.isNotBlank(param.getSpbillCreateIp()) ? param.getSpbillCreateIp() : "127.0.0.1");
            
            // 设置附加数据
            if (StrUtil.isNotBlank(param.getAttach())) {
                request.setAttach(param.getAttach());
            }
            
            // 设置订单过期时间
            if (ObjectUtil.isNotNull(param.getTimeExpire())) {
                LocalDateTime expireTime = LocalDateTime.now().plusMinutes(param.getTimeExpire());
                request.setTimeExpire(expireTime.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
            }
            
            // 调用统一下单API
            WxPayUnifiedOrderResult unifiedOrderResult = wxPayService.unifiedOrder(request);
            
            // 构建APP支付参数 - 修复API调用
            WxPayAppOrderResult appOrderResult = wxPayService.createOrder(request);
            
            // 构建返回结果
            WechatPayCreateResult result = new WechatPayCreateResult();
            result.setPrepayId(unifiedOrderResult.getPrepayId());
            result.setAppid(PaymentConfigUtil.getAppId(paymentConfig));
            result.setPartnerid(PaymentConfigUtil.getMerchantId(paymentConfig));
            result.setSign(appOrderResult.getSign());
            
            log.info("APP支付订单创建成功，prepayId：{}", unifiedOrderResult.getPrepayId());
            return result;
        } catch (Exception e) {
            log.error("创建APP支付订单失败：{}", e.getMessage(), e);
            throw new CommonException("创建APP支付订单失败：" + e.getMessage());
        }
    }

    /**
     * 创建Native支付订单（扫码支付）
     */
    private WechatPayCreateResult createNativePayOrder(WechatPayCreateParam param) {
        try {
            WxPayService wxPayService = getWxPayService(param.getPayType());
            
            // 构建统一下单请求
            WxPayUnifiedOrderRequest request = new WxPayUnifiedOrderRequest();
            request.setOutTradeNo(param.getOutTradeNo());
            request.setBody(param.getDescription());
            request.setTotalFee(param.getTotalAmount().multiply(new BigDecimal("100")).intValue());
            request.setTradeType(WxPayConstants.TradeType.NATIVE);
            request.setSpbillCreateIp(StrUtil.isNotBlank(param.getSpbillCreateIp()) ? param.getSpbillCreateIp() : "127.0.0.1");
            
            // 设置附加数据
            if (StrUtil.isNotBlank(param.getAttach())) {
                request.setAttach(param.getAttach());
            }
            
            // 设置订单过期时间
            if (ObjectUtil.isNotNull(param.getTimeExpire())) {
                LocalDateTime expireTime = LocalDateTime.now().plusMinutes(param.getTimeExpire());
                request.setTimeExpire(expireTime.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
            }
            
            // 调用统一下单API
            WxPayUnifiedOrderResult unifiedOrderResult = wxPayService.unifiedOrder(request);
            
            // 构建返回结果
            WechatPayCreateResult result = new WechatPayCreateResult();
            result.setPrepayId(unifiedOrderResult.getPrepayId());
            result.setCodeUrl(unifiedOrderResult.getCodeURL());
            
            log.info("Native支付订单创建成功，prepayId：{}，codeUrl：{}", unifiedOrderResult.getPrepayId(), unifiedOrderResult.getCodeURL());
            return result;
        } catch (Exception e) {
            log.error("创建Native支付订单失败：{}", e.getMessage(), e);
            throw new CommonException("创建Native支付订单失败：" + e.getMessage());
        }
    }

    @Override
    public WechatPayQueryResult queryPayOrder(WechatPayQueryParam wechatPayQueryParam) {
        try {
            log.info("查询微信支付订单，参数：{}", JSONUtil.toJsonStr(wechatPayQueryParam));
            
            WxPayService wxPayService = getWxPayService(wechatPayQueryParam.getPayType());
            
            // 构建查询请求
            WxPayOrderQueryRequest request = new WxPayOrderQueryRequest();
            request.setOutTradeNo(wechatPayQueryParam.getOutTradeNo());
            
            // 调用查询订单API
            WxPayOrderQueryResult queryResult = wxPayService.queryOrder(request);
            
            // 构建返回结果
            WechatPayQueryResult result = new WechatPayQueryResult();
            result.setOutTradeNo(queryResult.getOutTradeNo());
            result.setTransactionId(queryResult.getTransactionId());
            result.setTradeState(queryResult.getTradeState());
            result.setTradeStateDesc(queryResult.getTradeStateDesc());
            result.setBankType(queryResult.getBankType());
            
            if (queryResult.getTotalFee() != null) {
                result.setTotalAmount(new BigDecimal(queryResult.getTotalFee()).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP));
            }
            
            if (queryResult.getCashFee() != null) {
                result.setPayerTotal(new BigDecimal(queryResult.getCashFee()).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP));
            } else if (queryResult.getTotalFee() != null) {
                result.setPayerTotal(new BigDecimal(queryResult.getTotalFee()).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP));
            }
            
            result.setCurrency(queryResult.getFeeType() != null ? queryResult.getFeeType() : "CNY");
            result.setPayerCurrency(queryResult.getCashFeeType() != null ? queryResult.getCashFeeType() : "CNY");
            result.setOpenid(queryResult.getOpenid());
            result.setAttach(queryResult.getAttach());
            
            // 转换时间格式
            if (StrUtil.isNotBlank(queryResult.getTimeEnd())) {
                try {
                    LocalDateTime dateTime = LocalDateTime.parse(queryResult.getTimeEnd(), 
                        java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
                    result.setSuccessTime(Date.from(dateTime.atZone(ZoneOffset.of("+8")).toInstant()));
                } catch (Exception e) {
                    log.warn("解析支付完成时间失败：{}", queryResult.getTimeEnd());
                }
            }
            
            log.info("查询微信支付订单成功，订单号：{}，状态：{}", result.getOutTradeNo(), result.getTradeState());
            return result;
        } catch (Exception e) {
            log.error("查询微信支付订单失败：{}", e.getMessage(), e);
            throw new CommonException("查询微信支付订单失败：" + e.getMessage());
        }
    }

    @Override
    public WechatRefundResult refundOrder(WechatRefundParam wechatRefundParam) {
        try {
            log.info("申请微信退款，参数：{}", JSONUtil.toJsonStr(wechatRefundParam));
            
            WxPayService wxPayService = getWxPayService(wechatRefundParam.getPayType());
            
            // 构建退款请求
            WxPayRefundRequest request = new WxPayRefundRequest();
            request.setOutTradeNo(wechatRefundParam.getOutTradeNo());
            request.setOutRefundNo(wechatRefundParam.getOutRefundNo());
            request.setRefundDesc(wechatRefundParam.getReason());
            request.setTotalFee(wechatRefundParam.getTotalAmount().multiply(new BigDecimal("100")).intValue());
            request.setRefundFee(wechatRefundParam.getRefundAmount().multiply(new BigDecimal("100")).intValue());
            
            // 调用退款API
            WxPayRefundResult refundResult = wxPayService.refund(request);
            
            // 构建返回结果
            WechatRefundResult result = new WechatRefundResult();
            result.setRefundId(refundResult.getRefundId());
            result.setOutRefundNo(refundResult.getOutRefundNo());
            result.setOutTradeNo(refundResult.getOutTradeNo());
            result.setTransactionId(refundResult.getTransactionId());
            
            // 设置金额信息（转换为元）
            if (refundResult.getTotalFee() != null) {
                result.setTotalAmount(new BigDecimal(refundResult.getTotalFee()).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP));
            }
            if (refundResult.getRefundFee() != null) {
                result.setRefundAmount(new BigDecimal(refundResult.getRefundFee()).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP));
            }
            result.setCurrency(refundResult.getFeeType() != null ? refundResult.getFeeType() : "CNY");
            
            log.info("申请微信退款成功，退款单号：{}，状态：{}", result.getOutRefundNo(), result.getStatus());
            return result;
        } catch (Exception e) {
            log.error("申请微信退款失败：{}", e.getMessage(), e);
            throw new CommonException("申请微信退款失败：" + e.getMessage());
        }
    }

    @Override
    public WechatRefundResult queryRefund(String refundId) {
        try {
            log.info("查询微信退款，退款单号：{}", refundId);
            
            WxPayService wxPayService = getWxPayService("WECHAT_MINI"); // 默认使用小程序支付类型
            
            // 查询退款
            WxPayRefundQueryRequest request = new WxPayRefundQueryRequest();
            request.setOutRefundNo(refundId);
            
            WxPayRefundQueryResult queryResult = wxPayService.refundQuery(request);
            
            // 构建返回结果
            WechatRefundResult result = new WechatRefundResult();
            result.setOutTradeNo(queryResult.getOutTradeNo());
            result.setTransactionId(queryResult.getTransactionId());
            
            // 设置金额信息（转换为元）
            if (queryResult.getTotalFee() != null) {
                result.setTotalAmount(new BigDecimal(queryResult.getTotalFee()).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP));
            }
            if (queryResult.getRefundFee() != null) {
                result.setRefundAmount(new BigDecimal(queryResult.getRefundFee()).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP));
            }
            result.setCurrency(queryResult.getFeeType() != null ? queryResult.getFeeType() : "CNY");
            
            log.info("查询微信退款成功，退款单号：{}，状态：{}", result.getOutRefundNo(), result.getStatus());
            return result;
        } catch (Exception e) {
            log.error("查询微信退款失败：{}", e.getMessage(), e);
            throw new CommonException("查询微信退款失败：" + e.getMessage());
        }
    }

    @Override
    public Boolean closeOrder(String outTradeNo) {
        try {
            log.info("关闭微信支付订单，订单号：{}", outTradeNo);
            
            WxPayService wxPayService = getWxPayService("WECHAT_MINI"); // 默认使用小程序支付类型
            
            // 构建关闭订单请求
            WxPayOrderCloseRequest request = new WxPayOrderCloseRequest();
            request.setOutTradeNo(outTradeNo);
            
            // 调用关闭订单API
            WxPayOrderCloseResult closeResult = wxPayService.closeOrder(request);
            
            log.info("关闭微信支付订单成功，订单号：{}", outTradeNo);
            return true;
        } catch (Exception e) {
            log.error("关闭微信支付订单失败：{}", e.getMessage(), e);
            throw new CommonException("关闭微信支付订单失败：" + e.getMessage());
        }
    }

    @Override
    public String verifyNotify(String requestBody, String signature, String timestamp, String nonce) {
        try {
            log.info("验证微信支付回调，timestamp：{}，nonce：{}", timestamp, nonce);
            
            WxPayService wxPayService = getWxPayService("WECHAT_MINI"); // 默认使用小程序支付类型
            
            // 解析回调数据
            WxPayOrderNotifyResult notifyResult = wxPayService.parseOrderNotifyResult(requestBody);
            
            log.info("验证微信支付回调成功，订单号：{}，交易状态：{}", notifyResult.getOutTradeNo(), notifyResult.getResultCode());
            return JSONUtil.toJsonStr(notifyResult);
        } catch (Exception e) {
            log.error("验证微信支付回调失败：{}", e.getMessage(), e);
            throw new CommonException("验证微信支付回调失败：" + e.getMessage());
        }
    }

    @Override
    public String verifyRefundNotify(String requestBody, String signature, String timestamp, String nonce) {
        try {
            log.info("验证微信退款回调，timestamp：{}，nonce：{}", timestamp, nonce);
            
            WxPayService wxPayService = getWxPayService("WECHAT_MINI"); // 默认使用小程序支付类型
            
            // 解析退款回调数据
            WxPayRefundNotifyResult notifyResult = wxPayService.parseRefundNotifyResult(requestBody);
            
            log.info("验证微信退款回调成功，退款单号：{}，退款状态：{}", notifyResult.getReqInfo().getOutRefundNo(), notifyResult.getReqInfo().getRefundStatus());
            return JSONUtil.toJsonStr(notifyResult);
        } catch (Exception e) {
            log.error("验证微信退款回调失败：{}", e.getMessage(), e);
            throw new CommonException("验证微信退款回调失败：" + e.getMessage());
        }
    }
}