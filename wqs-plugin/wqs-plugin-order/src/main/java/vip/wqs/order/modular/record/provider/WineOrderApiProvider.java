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
package vip.wqs.order.modular.record.provider;

import cn.hutool.core.bean.BeanUtil;
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
 * 参考AuthServiceImpl的设计模式，使用BeanUtils简化转换过程
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
     * 参考AuthServiceImpl的设计模式，使用BeanUtils简化转换过程
     *
     * @param wineOrder 订单实体
     * @return 订单Pojo
     */
    private WineOrderPojo convertToWineOrderPojo(WineOrder wineOrder) {
        if (wineOrder == null) {
            return null;
        }
        
        WineOrderPojo pojo = new WineOrderPojo();
        // 使用BeanUtils.copyProperties进行基础字段复制
        BeanUtil.copyProperties(wineOrder, pojo);
        
        // 动态填充扩展信息（参考AuthServiceImpl.fillSaBaseClientLoginUserAndUpdateCache的设计）
        fillWineOrderExtInfo(pojo, wineOrder);
        
        return pojo;
    }
    
    /**
     * 动态填充订单扩展信息
     * 参考AuthServiceImpl.fillSaBaseClientLoginUserAndUpdateCache的设计模式
     *
     * @param pojo 订单响应对象
     * @param wineOrder 订单实体
     */
    private void fillWineOrderExtInfo(WineOrderPojo pojo, WineOrder wineOrder) {
        // 填充设备信息
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
        
        // 填充酒品信息
        if (StrUtil.isNotBlank(wineOrder.getWineId())) {
            try {
                WineProductPojo wineProduct = wineProductApi.getWineProductDetail(wineOrder.getWineId());
                if (wineProduct != null) {
                    // 设置酒品图片
                    if (StrUtil.isNotBlank(wineProduct.getImageUrl())) {
                        pojo.setWineImage(wineProduct.getImageUrl());
                    }
                    // 设置酒品规格（使用净含量信息）
                    if (wineProduct.getVolume() != null) {
                        pojo.setWineSpec(wineProduct.getVolume() + "ml");
                    }
                    // 设置酒精度数
                    if (wineProduct.getAlcoholContent() != null) {
                        pojo.setAlcoholDegree(wineProduct.getAlcoholContent());
                    }
                    // 如果wineName为空，使用酒品名称
                    if (StrUtil.isBlank(pojo.getWineName())) {
                        pojo.setWineName(wineProduct.getProductName());
                    }
                }
            } catch (Exception e) {
                // 酒品信息获取失败时，使用默认值
                if (StrUtil.isBlank(pojo.getWineName())) {
                    pojo.setWineName("共享售酒机酒品");
                }
            }
        }
        
        // 填充业务逻辑字段（小程序专用）
        fillBusinessLogicFields(pojo, wineOrder);
    }
    
    /**
     * 填充业务逻辑字段
     * 参考AuthServiceImpl中权限、角色等信息的填充逻辑
     *
     * @param pojo 订单响应对象
     * @param wineOrder 订单实体
     */
    private void fillBusinessLogicFields(WineOrderPojo pojo, WineOrder wineOrder) {
        String status = wineOrder.getStatus();
        
        // 设置订单状态显示文本
        switch (status) {
            case "PENDING":
                pojo.setStatusText("待支付");
                break;
            case "PAID":
                pojo.setStatusText("已支付");
                break;
            case "DISPENSING":
                pojo.setStatusText("出酒中");
                break;
            case "COMPLETED":
                pojo.setStatusText("已完成");
                break;
            case "CANCELLED":
                pojo.setStatusText("已取消");
                break;
            case "REFUNDED":
                pojo.setStatusText("已退款");
                break;
            default:
                pojo.setStatusText("未知状态");
        }
        
        // 设置操作权限（小程序专用字段）
        pojo.setCanCancel("PENDING".equals(status)); // 待支付状态可以取消
        pojo.setCanPay("PENDING".equals(status)); // 待支付状态可以支付
        pojo.setCanRefund("PAID".equals(status) || "DISPENSING".equals(status)); // 已支付或出酒中可以申请退款
        
        // 设置实际支付金额（总金额 + 服务费）
        BigDecimal totalAmount = wineOrder.getTotalAmount() != null ? wineOrder.getTotalAmount() : BigDecimal.ZERO;
        BigDecimal serviceFee = wineOrder.getServiceFee() != null ? wineOrder.getServiceFee() : BigDecimal.ZERO;
        pojo.setActualAmount(totalAmount.add(serviceFee));
        
        // 设置优惠金额（暂时为0）
        pojo.setDiscountAmount(BigDecimal.ZERO);
        
        // 设置支付方式和状态（暂时使用默认值）
        pojo.setPayType("WECHAT_PAY");
        pojo.setPayStatus("PAID".equals(status) ? "SUCCESS" : "PENDING");
    }
}