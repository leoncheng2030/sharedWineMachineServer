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
package vip.wqs.device.modular.stock.provider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vip.wqs.device.api.WineDeviceStockApi;
import vip.wqs.device.modular.stock.service.WineDeviceStockService;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 设备库存API实现类
 *
 * @author AI Assistant
 * @date 2025/01/30
 */
@Slf4j
@Service
public class WineDeviceStockApiProvider implements WineDeviceStockApi {

    @Autowired
    private WineDeviceStockService wineDeviceStockService;

    @Override
    public BigDecimal getStockQuantity(String deviceId, String productId) {
        try {
            return wineDeviceStockService.getStockQuantity(deviceId, productId);
        } catch (Exception e) {
            System.err.println(
                    "获取设备库存数量失败，deviceId: " + deviceId + ", productId: " + productId + ", error: " + e.getMessage());
            return BigDecimal.ZERO;
        }
    }

    @Override
    public Boolean checkStockSufficient(String deviceId, String productId, BigDecimal requiredQuantity) {
        try {
            return wineDeviceStockService.checkStockSufficient(deviceId, productId, requiredQuantity);
        } catch (Exception e) {
            System.err.println("检查库存是否充足失败，deviceId: " + deviceId + ", productId: " + productId + ", requiredQuantity: "
                    + requiredQuantity + ", error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Boolean deductStock(String deviceId, String productId, BigDecimal quantity, String operator, String reason) {
        try {
            wineDeviceStockService.deductStock(deviceId, productId, quantity, operator, reason);
            return true;
        } catch (Exception e) {
            System.err.println("扣减库存失败，deviceId: " + deviceId + ", productId: " + productId + ", quantity: " + quantity
                    + ", operator: " + operator + ", reason: " + reason + ", error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Object> getStockByDeviceId(String deviceId) {
        try {
            return wineDeviceStockService.getStockByDeviceId(deviceId)
                    .stream()
                    .map(stock -> (Object) stock)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("获取设备库存信息失败，deviceId: " + deviceId + ", error: " + e.getMessage());
            return java.util.Collections.emptyList();
        }
    }
}