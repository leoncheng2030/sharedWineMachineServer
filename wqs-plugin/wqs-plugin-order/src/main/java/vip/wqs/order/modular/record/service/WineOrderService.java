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
package vip.wqs.order.modular.record.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import vip.wqs.order.modular.record.entity.WineOrder;
import vip.wqs.order.modular.record.param.WineOrderCreateParam;
import vip.wqs.order.modular.record.param.WineOrderIdParam;
import vip.wqs.order.modular.record.param.WineOrderPageParam;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单管理Service接口
 *
 * @author wqs
 * @date 2025/01/30 16:35
 **/
public interface WineOrderService extends IService<WineOrder> {

    /**
     * 获取订单分页
     *
     * @param wineOrderPageParam 分页参数
     * @return 分页结果
     * @author wqs
     * @date 2025/01/30 16:35
     */
    Page<WineOrder> page(WineOrderPageParam wineOrderPageParam);

    /**
     * 创建订单
     *
     * @param wineOrderCreateParam 创建参数
     * @return 订单ID
     * @author wqs
     * @date 2025/01/30 16:35
     */
    String add(WineOrderCreateParam wineOrderCreateParam);

    /**
     * 获取订单详情
     *
     * @param wineOrderIdParam ID参数
     * @return 订单详情
     * @author wqs
     * @date 2025/01/30 16:35
     */
    WineOrder detail(WineOrderIdParam wineOrderIdParam);

    /**
     * 取消订单
     *
     * @param wineOrderIdParam ID参数
     * @param cancelReason 取消原因
     * @return 是否成功
     * @author wqs
     * @date 2025/01/30 16:35
     */
    Boolean cancel(WineOrderIdParam wineOrderIdParam, String cancelReason);

    /**
     * 根据订单号获取订单
     *
     * @param orderNo 订单号
     * @return 订单信息
     * @author wqs
     * @date 2025/01/30 16:35
     */
    WineOrder getOrderByOrderNo(String orderNo);

    /**
     * 创建订单（API方法）
     *
     * @param userId 用户ID
     * @param deviceId 设备ID
     * @param wineId 酒品ID
     * @param amount 出酒量
     * @param unitPrice 单价
     * @return 订单ID
     * @author wqs
     * @date 2025/01/30 16:35
     */
    String createOrder(String userId, String deviceId, String wineId, Integer amount, BigDecimal unitPrice);

    /**
     * 更新订单状态
     *
     * @param orderId 订单ID
     * @param status 新状态
     * @return 是否成功
     * @author wqs
     * @date 2025/01/30 16:35
     */
    Boolean updateOrderStatus(String orderId, String status);

    /**
     * 支付订单
     *
     * @param orderId 订单ID
     * @return 是否成功
     * @author wqs
     * @date 2025/01/30 16:35
     */
    Boolean payOrder(String orderId);

    /**
     * 取消订单（API方法）
     *
     * @param orderId 订单ID
     * @param cancelReason 取消原因
     * @return 是否成功
     * @author wqs
     * @date 2025/01/30 16:35
     */
    Boolean cancelOrder(String orderId, String cancelReason);

    /**
     * 开始出酒
     *
     * @param orderId 订单ID
     * @return 是否成功
     * @author wqs
     * @date 2025/01/30 16:35
     */
    Boolean startDispense(String orderId);

    /**
     * 完成出酒
     *
     * @param orderId 订单ID
     * @return 是否成功
     * @author wqs
     * @date 2025/01/30 16:35
     */
    Boolean completeDispense(String orderId);

    /**
     * 根据用户ID获取订单列表
     *
     * @param userId 用户ID
     * @return 订单列表
     * @author wqs
     * @date 2025/01/30 16:35
     */
    List<WineOrder> getOrdersByUserId(String userId);

    /**
     * 根据设备ID获取订单列表
     *
     * @param deviceId 设备ID
     * @return 订单列表
     * @author wqs
     * @date 2025/01/30 16:35
     */
    List<WineOrder> getOrdersByDeviceId(String deviceId);

    /**
     * 获取订单统计信息
     *
     * @param userId 用户ID（可选）
     * @return 统计信息
     * @author wqs
     * @date 2025/01/30 16:35
     */
    Object getOrderStatistics(String userId);
} 