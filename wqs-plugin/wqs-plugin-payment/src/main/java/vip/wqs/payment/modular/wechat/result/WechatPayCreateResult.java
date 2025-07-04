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
package vip.wqs.payment.modular.wechat.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 微信支付创建结果
 *
 * @author wqs
 * @date 2025/01/30 16:45
 **/
@Getter
@Setter
@Schema(description = "微信支付创建结果")
public class WechatPayCreateResult {

    /** 预支付交易会话标识 */
    @Schema(description = "预支付交易会话标识")
    private String prepayId;

    /** 二维码链接（NATIVE支付） */
    @Schema(description = "二维码链接")
    private String codeUrl;

    /** H5支付跳转链接 */
    @Schema(description = "H5支付跳转链接")
    private String h5Url;

    /** 小程序支付参数 */
    @Schema(description = "小程序时间戳")
    private String timeStamp;

    @Schema(description = "小程序随机字符串")
    private String nonceStr;

    @Schema(description = "小程序数据包")
    private String packageValue;

    @Schema(description = "小程序签名方式")
    private String signType;

    @Schema(description = "小程序签名")
    private String paySign;

    /** APP支付参数 */
    @Schema(description = "APP应用ID")
    private String appid;

    @Schema(description = "APP商户号")
    private String partnerid;

    @Schema(description = "APP签名")
    private String sign;
} 