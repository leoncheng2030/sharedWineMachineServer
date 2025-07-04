/*
 * Copyright [2025] [https://www.wqs.vip]
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
package vip.wqs.order.modular.record.provider;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import vip.wqs.device.api.DeviceApi;
import vip.wqs.device.pojo.DevicePojo;
import vip.wqs.order.api.WineOrderApi;
import vip.wqs.order.modular.record.entity.WineOrder;
import vip.wqs.order.modular.record.param.WineOrderPageParam;
import vip.wqs.order.modular.record.service.WineOrderService;
import vip.wqs.order.pojo.WineOrderApiPageParam;
import vip.wqs.order.pojo.WineOrderPojo;
import vip.wqs.order.pojo.WineOrderStatisticsPojo;
import vip.wqs.wine.api.WineProductApi;
import vip.wqs.wine.pojo.WineProductPojo;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单管理API接口实现类
 *
 * @author wqs
 * @date 2025/01/30 16:30
 **/
@Service
public class WineOrderApiProvider implements WineOrderApi {

    @Resource
    private WineOrderService wineOrderService;
    
    @Resource
    private DeviceApi deviceApi;
    
    @Resource
    private WineProductApi wineProductApi;

    @Override
    public WineOrderPojo getOrderById(String orderId) {
        WineOrder wineOrder = wineOrderService.getById(orderId);
        return wineOrder != null ? convertToWineOrderPojo(wineOrder) : null;
    }

    @Override
    public WineOrderPojo getOrderByOrderNo(String orderNo) {
        WineOrder wineOrder = wineOrderService.getOrderByOrderNo(orderNo);
        return wineOrder != null ? convertToWineOrderPojo(wineOrder) : null;
    }

    @Override
    public String createOrder(String userId, String deviceId, String wineId, Integer amount, BigDecimal unitPrice) {
        return wineOrderService.createOrder(userId, deviceId, wineId, amount, unitPrice);
    }

    @Override
    public Boolean updateOrderStatus(String orderId, String status) {
        return wineOrderService.updateOrderStatus(orderId, status);
    }

    @Override
    public Boolean payOrder(String orderId) {
        return wineOrderService.payOrder(orderId);
    }

    @Override
    public Boolean cancelOrder(String orderId, String cancelReason) {
        return wineOrderService.cancelOrder(orderId, cancelReason);
    }

    @Override
    public Boolean startDispense(String orderId) {
        return wineOrderService.startDispense(orderId);
    }

    @Override
    public Boolean completeDispense(String orderId) {
        return wineOrderService.completeDispense(orderId);
    }

    @Override
    public List<WineOrderPojo> getOrdersByUserId(String userId) {
        List<WineOrder> orders = wineOrderService.getOrdersByUserId(userId);
        return orders.stream()
                .map(this::convertToWineOrderPojo)
                .collect(Collectors.toList());
    }

    @Override
    public List<WineOrderPojo> getOrdersByDeviceId(String deviceId) {
        List<WineOrder> orders = wineOrderService.getOrdersByDeviceId(deviceId);
        return orders.stream()
                .map(this::convertToWineOrderPojo)
                .collect(Collectors.toList());
    }

    @Override
    public Page<WineOrderPojo> getOrderPage(WineOrderApiPageParam param) {
        // 转换API参数为Service参数
        WineOrderPageParam serviceParam = new WineOrderPageParam();
        serviceParam.setPageNum(param.getPageNum());
        serviceParam.setPageSize(param.getPageSize());
        serviceParam.setUserId(param.getUserId());
        serviceParam.setStatus(param.getStatus());
        serviceParam.setDeviceId(param.getDeviceId());
        serviceParam.setWineId(param.getWineId());
        // 将keyword映射到orderNo字段
        if (param.getKeyword() != null && !param.getKeyword().trim().isEmpty()) {
            serviceParam.setOrderNo(param.getKeyword());
        }
        
        // 调用Service的分页方法
        Page<WineOrder> orderPage = wineOrderService.page(serviceParam);
        
        // 转换结果
        Page<WineOrderPojo> resultPage = new Page<>(orderPage.getCurrent(), orderPage.getSize());
        resultPage.setTotal(orderPage.getTotal());
        resultPage.setPages(orderPage.getPages());
        
        List<WineOrderPojo> records = orderPage.getRecords().stream()
                .map(this::convertToWineOrderPojo)
                .collect(Collectors.toList());
        resultPage.setRecords(records);
        
        return resultPage;
    }

    @Override
    public WineOrderStatisticsPojo getOrderStatistics(String userId) {
        // 临时简化实现，先返回空统计数据
        return new WineOrderStatisticsPojo();
    }
    
    /**
     * 将WineOrder实体转换为WineOrderPojo
     *
     * @param wineOrder 订单实体
     * @return 订单Pojo
     */
    private WineOrderPojo convertToWineOrderPojo(WineOrder wineOrder) {
        if (wineOrder == null) {
            return null;
        }
        
        WineOrderPojo pojo = new WineOrderPojo();
        // 设置基本字段
        pojo.setId(wineOrder.getId());
        pojo.setOrderNo(wineOrder.getOrderNo());
        pojo.setUserId(wineOrder.getUserId());
        pojo.setDeviceId(wineOrder.getDeviceId());
        pojo.setWineId(wineOrder.getWineId());
        pojo.setWineName(wineOrder.getWineName());
        pojo.setAmount(wineOrder.getAmount());
        pojo.setUnitPrice(wineOrder.getUnitPrice());
        pojo.setTotalAmount(wineOrder.getTotalAmount());
        pojo.setStatus(wineOrder.getStatus());
        pojo.setCreateTime(wineOrder.getCreateTime());
        pojo.setUpdateTime(wineOrder.getUpdateTime());
        
        // 获取设备信息并填充
        if (StrUtil.isNotBlank(wineOrder.getDeviceId())) {
            try {
                DevicePojo deviceInfo = deviceApi.getDeviceDetail(wineOrder.getDeviceId());
                if (deviceInfo != null) {
                    pojo.setDeviceName(deviceInfo.getDeviceName());
                    pojo.setDeviceCode(deviceInfo.getDeviceCode());
                    pojo.setDeviceLocation(deviceInfo.getLocation());
                }
            } catch (Exception e) {
                // 设备信息获取失败时，设置默认值
                pojo.setDeviceName("共享售酒机设备");
                pojo.setDeviceCode("未知");
                pojo.setDeviceLocation("未知位置");
            }
        }
        
        // 获取酒品信息并填充图片
        if (StrUtil.isNotBlank(wineOrder.getWineId())) {
            try {
                System.out.println("🍷 开始获取酒品信息，wineId: " + wineOrder.getWineId());
                WineProductPojo wineProduct = wineProductApi.getWineProductDetail(wineOrder.getWineId());
                if (wineProduct != null) {
                    System.out.println("🍷 酒品信息获取成功: " + wineProduct.getProductName());
                    System.out.println("🍷 酒品图片URL: " + wineProduct.getImageUrl());
                    if (StrUtil.isNotBlank(wineProduct.getImageUrl())) {
                        pojo.setWineImage(wineProduct.getImageUrl());
                        System.out.println("🍷 酒品图片设置成功: " + wineProduct.getImageUrl());
                    } else {
                        System.out.println("🍷 酒品图片URL为空");
                    }
                } else {
                    System.out.println("🍷 酒品信息为空，wineId: " + wineOrder.getWineId());
                }
            } catch (Exception e) {
                System.err.println("🍷 获取酒品信息失败，wineId: " + wineOrder.getWineId() + "，错误: " + e.getMessage());
                e.printStackTrace();
                // 酒品信息获取失败时，忽略错误
                // 不设置图片，保持为null
            }
        }
        
        // 其他字段暂时注释，避免编译错误
        // pojo.setServiceFee(wineOrder.getServiceFee());
        // pojo.setPayTime(wineOrder.getPayTime());
        // pojo.setDispenseStartTime(wineOrder.getDispenseStartTime());
        // pojo.setDispenseEndTime(wineOrder.getDispenseEndTime());
        // pojo.setCancelTime(wineOrder.getCancelTime());
        // pojo.setCancelReason(wineOrder.getCancelReason());
        // pojo.setRefundTime(wineOrder.getRefundTime());
        // pojo.setRefundAmount(wineOrder.getRefundAmount());
        // pojo.setRemark(wineOrder.getRemark());
        // pojo.setExtJson(wineOrder.getExtJson());
        // pojo.setCreateUser(wineOrder.getCreateUser());
        // pojo.setUpdateUser(wineOrder.getUpdateUser());
        
        return pojo;
    }
}