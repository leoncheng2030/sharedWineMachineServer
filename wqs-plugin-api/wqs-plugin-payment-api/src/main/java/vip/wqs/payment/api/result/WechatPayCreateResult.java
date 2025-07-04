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
package vip.wqs.payment.api.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 微信支付创建结果
 *
 * @author AI Assistant
 * @date 2025/01/30
 */
@Getter
@Setter
@Schema(description = "微信支付创建结果")
public class WechatPayCreateResult {

    /** 预支付交易会话标识 */
    @Schema(description = "预支付交易会话标识")
    private String prepayId;

    /** 时间戳 */
    @Schema(description = "时间戳")
    private String timeStamp;

    /** 随机字符串 */
    @Schema(description = "随机字符串")
    private String nonceStr;

    /** 订单详情扩展字符串 */
    @Schema(description = "订单详情扩展字符串")
    private String packageValue;

    /** 签名方式 */
    @Schema(description = "签名方式")
    private String signType;

    /** 签名 */
    @Schema(description = "签名")
    private String paySign;

    /** 应用ID */
    @Schema(description = "应用ID")
    private String appid;

    /** 商户号 */
    @Schema(description = "商户号")
    private String partnerid;

    /** 签名 */
    @Schema(description = "签名")
    private String sign;

    /** H5支付链接 */
    @Schema(description = "H5支付链接")
    private String h5Url;

    /** 二维码链接 */
    @Schema(description = "二维码链接")
    private String codeUrl;
}