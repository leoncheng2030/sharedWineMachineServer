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
package vip.wqs.miniprogram.modular.order.provider;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vip.wqs.common.exception.CommonException;
import vip.wqs.device.api.WineDeviceStockApi;
import vip.wqs.miniprogram.modular.order.param.MiniOrderCancelParam;
import vip.wqs.miniprogram.modular.order.param.MiniOrderCreateParam;
import vip.wqs.miniprogram.modular.order.param.MiniOrderPageParam;
import vip.wqs.order.api.WineOrderApi;
import vip.wqs.order.pojo.WineOrderApiPageParam;
import vip.wqs.order.pojo.WineOrderPojo;
import vip.wqs.order.pojo.WineOrderStatisticsPojo;

import java.math.BigDecimal;
import java.util.List;

/**
 * 小程序订单API提供者
 *
 * @author wqs
 * @date 2025/01/30
 */
@Slf4j
@Service
public class MiniOrderApiProvider {

    @Resource
    private WineOrderApi wineOrderApi;
    
    @Resource
    private WineDeviceStockApi wineDeviceStockApi;

    /**
     * 获取用户订单分页列表
     *
     * @param userId 用户ID
     * @param param  查询参数
     * @return 分页结果
     */
    public Page<WineOrderPojo> getOrderPage(String userId, MiniOrderPageParam param) {
        try {
            log.info("获取用户订单分页列表，用户ID：{}，查询参数：{}", userId, param);

            // 创建WineOrderApiPageParam
            WineOrderApiPageParam wineOrderParam = new WineOrderApiPageParam();
            wineOrderParam.setUserId(userId);
            wineOrderParam.setPageNum(param.getPageNum().intValue());
            wineOrderParam.setPageSize(param.getPageSize().intValue());
            wineOrderParam.setStatus(param.getStatus());
            wineOrderParam.setDeviceId(param.getDeviceId());
            wineOrderParam.setWineId(param.getWineId());
            wineOrderParam.setKeyword(param.getKeyword());
            wineOrderParam.setSortField(param.getSortField());
            wineOrderParam.setSortOrder(param.getSortOrder());
            
            // 调用WineOrderApi的分页方法
            Page<WineOrderPojo> orderPage = wineOrderApi.getOrderPage(wineOrderParam);
            
            // 为每个订单设置小程序专用字段
            orderPage.getRecords().forEach(this::setMiniProgramFields);
            
            log.info("获取用户订单分页列表成功，总数：{}", orderPage.getTotal());
            return orderPage;

        } catch (Exception e) {
            log.error("获取用户订单分页列表失败，用户ID：{}，错误：{}", userId, e.getMessage(), e);
            throw new CommonException("获取订单列表失败");
        }
    }

    /**
     * 获取订单详情
     *
     * @param userId  用户ID
     * @param orderId 订单ID
     * @return 订单详情
     */
    public WineOrderPojo getOrderDetail(String userId, String orderId) {
        try {
            log.info("获取订单详情，用户ID：{}，订单ID：{}", userId, orderId);

            WineOrderPojo wineOrder = wineOrderApi.getOrderById(orderId);
            if (ObjectUtil.isNull(wineOrder)) {
                throw new CommonException("订单不存在");
            }

            // 验证订单是否属于当前用户
            if (!userId.equals(wineOrder.getUserId())) {
                throw new CommonException("无权查看此订单");
            }

            // 设置小程序专用字段
            setMiniProgramFields(wineOrder);
            
            log.info("获取订单详情成功");
            return wineOrder;

        } catch (Exception e) {
            log.error("获取订单详情失败，用户ID：{}，订单ID：{}，错误：{}", userId, orderId, e.getMessage(), e);
            throw new CommonException("获取订单详情失败");
        }
    }

    /**
     * 创建订单
     *
     * @param userId 用户ID
     * @param param  创建参数
     * @return 订单ID
     */
    public String createOrder(String userId, MiniOrderCreateParam param) {
        try {
            log.info("创建订单，用户ID：{}，创建参数：{}", userId, param);

            // 1. 检查库存是否充足
            Boolean isStockSufficient = wineDeviceStockApi.checkStockSufficient(
                param.getDeviceId(),
                param.getWineId(),
                BigDecimal.valueOf(param.getAmount())
            );
            
            if (!isStockSufficient) {
                throw new CommonException("库存不足，无法创建订单");
            }

            // 2. 创建订单
            String orderId = wineOrderApi.createOrder(
                    userId,
                    param.getDeviceId(),
                    param.getWineId(),
                    param.getAmount(),
                    param.getUnitPrice()
            );

            // 3. 扣减库存
            try {
                Boolean deductResult = wineDeviceStockApi.deductStock(
                    param.getDeviceId(),
                    param.getWineId(),
                    BigDecimal.valueOf(param.getAmount()),
                    userId,
                    "订单销售扣减，订单号：" + orderId
                );
                
                if (!deductResult) {
                    // 库存扣减失败，需要取消订单
                    wineOrderApi.cancelOrder(orderId, "库存扣减失败");
                    throw new CommonException("库存扣减失败，订单已取消");
                }
            } catch (Exception stockException) {
                // 库存扣减异常，取消订单
                try {
                    wineOrderApi.cancelOrder(orderId, "库存扣减异常：" + stockException.getMessage());
                } catch (Exception cancelException) {
                    log.error("取消订单失败，订单ID：{}，错误：{}", orderId, cancelException.getMessage());
                }
                throw new CommonException("库存扣减失败：" + stockException.getMessage());
            }

            log.info("创建订单成功，订单ID：{}，已扣减库存：{}ml", orderId, param.getAmount());
            return orderId;

        } catch (Exception e) {
            log.error("创建订单失败，用户ID：{}，错误：{}", userId, e.getMessage(), e);
            throw new CommonException("创建订单失败：" + e.getMessage());
        }
    }

    /**
     * 取消订单
     *
     * @param userId 用户ID
     * @param param  取消参数
     * @return 是否成功
     */
    public Boolean cancelOrder(String userId, MiniOrderCancelParam param) {
        try {
            log.info("取消订单，用户ID：{}，取消参数：{}", userId, param);

            // 验证订单是否属于当前用户
            WineOrderPojo wineOrder = wineOrderApi.getOrderById(param.getOrderId());
            if (ObjectUtil.isNull(wineOrder)) {
                throw new CommonException("订单不存在");
            }
            if (!userId.equals(wineOrder.getUserId())) {
                throw new CommonException("无权操作此订单");
            }

            Boolean result = wineOrderApi.cancelOrder(param.getOrderId(), param.getCancelReason());
            log.info("取消订单成功");
            return result;

        } catch (Exception e) {
            log.error("取消订单失败，用户ID：{}，错误：{}", userId, e.getMessage(), e);
            throw new CommonException("取消订单失败");
        }
    }

    /**
     * 获取订单统计信息
     *
     * @param userId 用户ID
     * @return 统计信息
     */
    public WineOrderStatisticsPojo getOrderStatistics(String userId) {
        try {
            log.info("获取订单统计信息，用户ID：{}", userId);

            // 调用WineOrderApi获取统计信息
            WineOrderStatisticsPojo statistics = wineOrderApi.getOrderStatistics(userId);
            
            if (statistics != null) {
                // 计算进行中订单数（小程序专用字段）
                statistics.setProcessingCount(
                    (statistics.getPaidCount() != null ? statistics.getPaidCount() : 0) + 
                    (statistics.getDispensingCount() != null ? statistics.getDispensingCount() : 0)
                );
            }

            log.info("获取订单统计信息成功");
            return statistics;

        } catch (Exception e) {
            log.error("获取订单统计信息失败，用户ID：{}，错误：{}", userId, e.getMessage(), e);
            throw new CommonException("获取订单统计信息失败");
        }
    }

    /**
     * 搜索订单
     *
     * @param userId  用户ID
     * @param keyword 关键词
     * @param limit   限制数量
     * @return 订单列表
     */
    public List<WineOrderPojo> searchOrders(String userId, String keyword, Integer limit) {
        try {
            log.info("搜索订单，用户ID：{}，关键词：{}，限制：{}", userId, keyword, limit);

            // 创建搜索参数
            WineOrderApiPageParam searchParam = new WineOrderApiPageParam();
            searchParam.setUserId(userId);
            searchParam.setPageNum(1);
            searchParam.setPageSize(limit != null ? limit : 50);
            searchParam.setKeyword(keyword);
            searchParam.setSortField("createTime");
            searchParam.setSortOrder("desc");
            
            // 调用分页查询
            Page<WineOrderPojo> orderPage = wineOrderApi.getOrderPage(searchParam);
            
            // 为每个订单设置小程序专用字段
            orderPage.getRecords().forEach(this::setMiniProgramFields);

            log.info("搜索订单成功，找到{}条记录", orderPage.getRecords().size());
            return orderPage.getRecords();

        } catch (Exception e) {
            log.error("搜索订单失败，用户ID：{}，关键词：{}，错误：{}", userId, keyword, e.getMessage(), e);
            throw new CommonException("搜索订单失败");
        }
    }

    /**
     * 为订单设置小程序专用字段
     *
     * @param order 订单对象
     */
    private void setMiniProgramFields(WineOrderPojo order) {
        if (order == null) {
            return;
        }

        // 设置状态文本
        order.setStatusText(getStatusText(order.getStatus()));

        // 设置操作权限
        setOrderOperationPermissions(order, order.getStatus());
    }

    /**
     * 获取状态文本
     *
     * @param status 状态码
     * @return 状态文本
     */
    private String getStatusText(String status) {
        if (StrUtil.isBlank(status)) {
            return "未知";
        }
        return switch (status) {
            case "PENDING" -> "待支付";
            case "DISPENSING" -> "待取酒";
            case "COMPLETED" -> "已完成";
            default -> "未知";
        };
    }

    /**
     * 设置订单操作权限
     *
     * @param order  订单对象
     * @param status 订单状态
     */
    private void setOrderOperationPermissions(WineOrderPojo order, String status) {
        if (StrUtil.isBlank(status)) {
            return;
        }

        switch (status) {
            case "PENDING":
                order.setCanPay(true);
                order.setCanCancel(true);
                order.setCanRefund(false);
                break;
            case "DISPENSING":
                order.setCanPay(false);
                order.setCanCancel(false);
                order.setCanRefund(true);
                break;
            case "COMPLETED":
                order.setCanPay(false);
                order.setCanCancel(false);
                order.setCanRefund(false);
                break;
            default:
                order.setCanPay(false);
                order.setCanCancel(false);
                order.setCanRefund(false);
                break;
        }
    }

    /**
     * 根据订单号获取订单详情
     *
     * @param userId  用户ID
     * @param orderNo 订单号
     * @return 订单详情
     */
    public WineOrderPojo getOrderByOrderNo(String userId, String orderNo) {
        try {
            log.info("根据订单号获取订单详情，用户ID：{}，订单号：{}", userId, orderNo);

            WineOrderPojo wineOrder = wineOrderApi.getOrderByOrderNo(orderNo);
            if (ObjectUtil.isNull(wineOrder)) {
                throw new CommonException("订单不存在");
            }

            // 验证订单是否属于当前用户
            if (!userId.equals(wineOrder.getUserId())) {
                throw new CommonException("无权查看此订单");
            }

            // 设置小程序专用字段
            setMiniProgramFields(wineOrder);
            
            log.info("根据订单号获取订单详情成功");
            return wineOrder;

        } catch (Exception e) {
            log.error("根据订单号获取订单详情失败，用户ID：{}，订单号：{}，错误：{}", userId, orderNo, e.getMessage(), e);
            throw new CommonException("获取订单详情失败");
        }
    }

    /**
     * 确认订单
     *
     * @param userId  用户ID
     * @param orderId 订单ID
     * @return 是否成功
     */
    public Boolean confirmOrder(String userId, String orderId) {
        try {
            log.info("确认订单，用户ID：{}，订单ID：{}", userId, orderId);

            // 验证订单是否属于当前用户
            WineOrderPojo wineOrder = wineOrderApi.getOrderById(orderId);
            if (ObjectUtil.isNull(wineOrder)) {
                throw new CommonException("订单不存在");
            }
            if (!userId.equals(wineOrder.getUserId())) {
                throw new CommonException("无权操作此订单");
            }

            // 检查订单状态是否可以确认
            if (!"DISPENSING".equals(wineOrder.getStatus())) {
                throw new CommonException("订单状态不允许确认操作");
            }

            // 使用completeDispense方法来完成订单
            Boolean result = wineOrderApi.completeDispense(orderId);
            log.info("确认订单成功");
            return result;

        } catch (Exception e) {
            log.error("确认订单失败，用户ID：{}，订单ID：{}，错误：{}", userId, orderId, e.getMessage(), e);
            throw new CommonException("确认订单失败");
        }
    }

    /**
     * 申请退款
     *
     * @param userId  用户ID
     * @param orderId 订单ID
     * @param reason  退款原因
     * @return 是否成功
     */
    public Boolean refundOrder(String userId, String orderId, String reason) {
        try {
            log.info("申请退款，用户ID：{}，订单ID：{}，原因：{}", userId, orderId, reason);

            // 验证订单是否属于当前用户
            WineOrderPojo wineOrder = wineOrderApi.getOrderById(orderId);
            if (ObjectUtil.isNull(wineOrder)) {
                throw new CommonException("订单不存在");
            }
            if (!userId.equals(wineOrder.getUserId())) {
                throw new CommonException("无权操作此订单");
            }

            // 检查订单状态是否可以退款
            // 允许已支付、出酒中、已完成状态的订单申请退款
            String status = wineOrder.getStatus();
            if (!"PAID".equals(status) && !"DISPENSING".equals(status) && !"COMPLETED".equals(status)) {
                throw new CommonException("订单状态不允许退款操作，当前状态：" + status);
            }

            // 使用updateOrderStatus方法将状态改为REFUNDED
            Boolean result = wineOrderApi.updateOrderStatus(orderId, "REFUNDED");
            log.info("申请退款成功");
            return result;

        } catch (Exception e) {
            log.error("申请退款失败，用户ID：{}，订单ID：{}，错误：{}", userId, orderId, e.getMessage(), e);
            throw new CommonException("申请退款失败");
        }
    }

    /**
     * 重新下单
     *
     * @param userId  用户ID
     * @param orderId 原订单ID
     * @return 新订单ID
     */
    public String reorder(String userId, String orderId) {
        try {
            log.info("重新下单，用户ID：{}，原订单ID：{}", userId, orderId);

            // 验证原订单是否属于当前用户
            WineOrderPojo originalOrder = wineOrderApi.getOrderById(orderId);
            if (ObjectUtil.isNull(originalOrder)) {
                throw new CommonException("原订单不存在");
            }
            if (!userId.equals(originalOrder.getUserId())) {
                throw new CommonException("无权操作此订单");
            }

            // 基于原订单信息创建新订单
            String newOrderId = wineOrderApi.createOrder(
                    userId,
                    originalOrder.getDeviceId(),
                    originalOrder.getWineId(),
                    originalOrder.getAmount(),
                    originalOrder.getUnitPrice()
            );

            log.info("重新下单成功，新订单ID：{}", newOrderId);
            return newOrderId;

        } catch (Exception e) {
            log.error("重新下单失败，用户ID：{}，原订单ID：{}，错误：{}", userId, orderId, e.getMessage(), e);
            throw new CommonException("重新下单失败");
        }
    }
} 