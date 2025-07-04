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
package vip.wqs.device.api;

import java.math.BigDecimal;
import java.util.List;

/**
 * 设备库存API接口
 *
 * @author AI Assistant
 * @date 2025/01/30
 */
public interface WineDeviceStockApi {

    /**
     * 获取设备库存数量
     *
     * @param deviceId 设备ID
     * @param productId 酒品ID
     * @return 库存数量(ml)
     * @author AI Assistant
     * @date 2025/01/30
     */
    BigDecimal getStockQuantity(String deviceId, String productId);

    /**
     * 检查库存是否充足
     *
     * @param deviceId 设备ID
     * @param productId 酒品ID
     * @param requiredQuantity 需要的数量(ml)
     * @return 是否充足
     * @author AI Assistant
     * @date 2025/01/30
     */
    Boolean checkStockSufficient(String deviceId, String productId, BigDecimal requiredQuantity);

    /**
     * 扣减库存
     *
     * @param deviceId 设备ID
     * @param productId 酒品ID
     * @param quantity 扣减数量(ml)
     * @param operator 操作人
     * @param reason 扣减原因
     * @return 是否成功
     * @author AI Assistant
     * @date 2025/01/30
     */
    Boolean deductStock(String deviceId, String productId, BigDecimal quantity, String operator, String reason);

    /**
     * 获取设备库存信息
     *
     * @param deviceId 设备ID
     * @return 设备库存信息列表
     * @author AI Assistant
     * @date 2025/01/30
     */
    List<Object> getStockByDeviceId(String deviceId);
} 