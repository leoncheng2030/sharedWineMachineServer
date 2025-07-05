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
package vip.wqs.order.modular.record.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.wqs.commission.api.WineCommissionRecordApi;
import vip.wqs.wine.api.WineProductApi;
import vip.wqs.wine.pojo.WineProductPojo;
import vip.wqs.order.modular.record.entity.WineOrder;
import vip.wqs.order.modular.record.enums.OrderStatusEnum;
import vip.wqs.order.modular.record.mapper.WineOrderMapper;
import vip.wqs.order.modular.record.param.WineOrderCreateParam;
import vip.wqs.order.modular.record.param.WineOrderIdParam;
import vip.wqs.order.modular.record.param.WineOrderPageParam;
import vip.wqs.order.modular.record.service.WineOrderService;
import vip.wqs.common.util.ControllerTimestampUtil;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单Service接口实现类
 *
 * @author wqs
 * @date 2025/01/30
 */
@Slf4j
@Service
public class WineOrderServiceImpl extends ServiceImpl<WineOrderMapper, WineOrder> implements WineOrderService {

    @Autowired
    private WineCommissionRecordApi wineCommissionRecordApi;

    @Autowired
    private WineProductApi wineProductApi;
    


    @Override
    public Page<WineOrder> page(WineOrderPageParam wineOrderPageParam) {
        QueryWrapper<WineOrder> queryWrapper = new QueryWrapper<>();
        
        // 条件查询
        if (StrUtil.isNotBlank(wineOrderPageParam.getOrderNo())) {
            queryWrapper.like("order_no", wineOrderPageParam.getOrderNo());
        }
        if (StrUtil.isNotBlank(wineOrderPageParam.getUserId())) {
            queryWrapper.eq("user_id", wineOrderPageParam.getUserId());
        }
        if (StrUtil.isNotBlank(wineOrderPageParam.getStatus())) {
            queryWrapper.eq("status", wineOrderPageParam.getStatus());
        }
        if (ObjectUtil.isNotNull(wineOrderPageParam.getStartTime())) {
            queryWrapper.ge("create_time", wineOrderPageParam.getStartTime());
        }
        if (ObjectUtil.isNotNull(wineOrderPageParam.getEndTime())) {
            queryWrapper.le("create_time", wineOrderPageParam.getEndTime());
        }
        
        // 按创建时间倒序排列
        queryWrapper.orderByDesc("create_time");
        
        // 创建分页对象，使用传入的分页参数
        int pageNum = wineOrderPageParam.getPageNum() != null ? wineOrderPageParam.getPageNum() : 1;
        int pageSize = wineOrderPageParam.getPageSize() != null ? wineOrderPageParam.getPageSize() : 10;
        Page<WineOrder> page = new Page<>(pageNum, pageSize);
        
        return this.page(page, queryWrapper);
    }



    @Transactional(rollbackFor = Exception.class)
    @Override
    public String add(WineOrderCreateParam wineOrderCreateParam) {
        WineOrder wineOrder = BeanUtil.toBean(wineOrderCreateParam, WineOrder.class);
        
        // 生成订单号
        wineOrder.setOrderNo(generateOrderNo());
        
        // 计算总金额
        BigDecimal totalAmount = wineOrderCreateParam.getUnitPrice()
                .multiply(BigDecimal.valueOf(wineOrderCreateParam.getAmount()));
        wineOrder.setTotalAmount(totalAmount);
        
        // 设置初始状态
        wineOrder.setStatus(OrderStatusEnum.PENDING.getCode());
        
        this.save(wineOrder);
        return wineOrder.getId();
    }

    @Override
    public WineOrder detail(WineOrderIdParam wineOrderIdParam) {
        return this.getById(wineOrderIdParam.getId());
    }

    @Override
    public WineOrder getOrderByOrderNo(String orderNo) {
        QueryWrapper<WineOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no", orderNo);
        return this.getOne(queryWrapper);
    }

    @Override
    public String createOrder(String userId, String deviceId, String wineId, Integer amount, BigDecimal unitPrice) {
        WineOrder wineOrder = new WineOrder();
        wineOrder.setOrderNo(generateOrderNo());
        wineOrder.setUserId(userId);
        wineOrder.setDeviceId(deviceId);
        wineOrder.setWineId(wineId);
        wineOrder.setAmount(amount);
        wineOrder.setUnitPrice(unitPrice);
        
        // 🔧 设置酒品名称 - 从酒品ID获取酒品名称
        if (StrUtil.isNotBlank(wineId)) {
            try {
                WineProductPojo wineProduct = wineProductApi.getWineProductDetail(wineId);
                if (wineProduct != null && StrUtil.isNotBlank(wineProduct.getProductName())) {
                    wineOrder.setWineName(wineProduct.getProductName());
                    log.info("创建订单时获取酒品名称成功，wineId: {}, wineName: {}", wineId, wineProduct.getProductName());
                } else {
                    wineOrder.setWineName("共享售酒机酒品");
                    log.warn("酒品信息为空或名称为空，使用默认名称，wineId: {}", wineId);
                }
            } catch (Exception e) {
                log.warn("获取酒品名称失败，使用默认名称，wineId: {}, 错误: {}", wineId, e.getMessage());
                wineOrder.setWineName("共享售酒机酒品");
            }
        }
        
        // 计算总金额
        BigDecimal totalAmount = unitPrice.multiply(BigDecimal.valueOf(amount));
        wineOrder.setTotalAmount(totalAmount);
        
        // 设置初始状态
        wineOrder.setStatus(OrderStatusEnum.PENDING.getCode());
        
        this.save(wineOrder);
        return wineOrder.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateOrderStatus(String orderId, String status) {
        WineOrder wineOrder = this.getById(orderId);
        if (ObjectUtil.isNull(wineOrder)) {
            return false;
        }
        
        wineOrder.setStatus(status);
        return this.updateById(wineOrder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean payOrder(String orderNo) {
        QueryWrapper<WineOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(WineOrder::getOrderNo, orderNo);
        WineOrder wineOrder = this.getOne(queryWrapper);
        if (ObjectUtil.isNull(wineOrder)) {
            return false;
        }
        
        // 检查订单状态
        if (!OrderStatusEnum.PENDING.getCode().equals(wineOrder.getStatus())) {
            return false;
        }
        
        // 支付后直接进入待取酒状态
        wineOrder.setStatus(OrderStatusEnum.DISPENSING.getCode());
        wineOrder.setPayTime(new Date());
        
        return this.updateById(wineOrder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean cancelOrder(String orderId, String cancelReason) {
        WineOrder wineOrder = this.getById(orderId);
        if (ObjectUtil.isNull(wineOrder)) {
            return false;
        }
        
        // 只有待支付状态的订单才能取消，取消后保持PENDING状态但记录取消信息
        if (!OrderStatusEnum.PENDING.getCode().equals(wineOrder.getStatus())) {
            return false;
        }
        
        // 保持PENDING状态，但记录取消信息
        wineOrder.setCancelReason(cancelReason);
        wineOrder.setCancelTime(new Date());
        
        return this.updateById(wineOrder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean startDispense(String orderId) {
        WineOrder wineOrder = this.getById(orderId);
        if (ObjectUtil.isNull(wineOrder)) {
            return false;
        }
        
        // 检查订单状态，只有待取酒状态才能开始出酒
        if (!OrderStatusEnum.DISPENSING.getCode().equals(wineOrder.getStatus())) {
            return false;
        }
        
        wineOrder.setStatus(OrderStatusEnum.DISPENSING.getCode());
        wineOrder.setDispenseStartTime(new Date());
        
        return this.updateById(wineOrder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean completeDispense(String orderId) {
        WineOrder wineOrder = this.getById(orderId);
        if (ObjectUtil.isNull(wineOrder)) {
            return false;
        }
        
        // 检查订单状态
        if (!OrderStatusEnum.DISPENSING.getCode().equals(wineOrder.getStatus())) {
            return false;
        }
        
        // 更新订单状态为已完成
        wineOrder.setStatus(OrderStatusEnum.COMPLETED.getCode());
        wineOrder.setDispenseEndTime(new Date());
        
        Boolean updateResult = this.updateById(wineOrder);
        
        // 订单状态更新成功后，触发佣金分配
        if (updateResult) {
            try {
                log.info("订单完成，开始分配佣金，订单ID：{}，订单号：{}", orderId, wineOrder.getOrderNo());
                
                // 调用佣金分配API
                Integer commissionCount = wineCommissionRecordApi.distributeCommissionForOrder(
                        orderId,                    // 订单ID
                        wineOrder.getOrderNo(),     // 订单号
                        wineOrder.getTotalAmount(), // 订单金额
                        wineOrder.getDeviceId(),    // 设备ID
                        wineOrder.getWineId(),      // 酒品ID
                        null                        // 门店ID（如果订单中有门店信息可以传入）
                );
                
                log.info("佣金分配完成，订单ID：{}，成功分配{}条佣金记录", orderId, commissionCount);
                
            } catch (Exception e) {
                // 佣金分配失败不影响订单完成，但需要记录错误日志
                log.error("订单完成后佣金分配失败，订单ID：{}，错误：{}", orderId, e.getMessage(), e);
                // 这里可以考虑发送通知或者记录到错误表，供后续人工处理
            }
        }
        
        return updateResult;
    }

    @Override
    public List<WineOrder> getOrdersByUserId(String userId) {
        QueryWrapper<WineOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderByDesc("create_time");
        return this.list(queryWrapper);
    }

    @Override
    public List<WineOrder> getOrdersByDeviceId(String deviceId) {
        QueryWrapper<WineOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_id", deviceId);
        queryWrapper.orderByDesc("create_time");
        return this.list(queryWrapper);
    }

    @Override
    public Object getOrderStatistics(String userId) {
        // TODO: 实现订单统计逻辑
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean cancel(WineOrderIdParam wineOrderIdParam, String cancelReason) {
        WineOrder wineOrder = this.getById(wineOrderIdParam.getId());
        if (ObjectUtil.isNull(wineOrder)) {
            return false;
        }
        
        // 只有待支付状态的订单才能取消
        if (!OrderStatusEnum.PENDING.getCode().equals(wineOrder.getStatus())) {
            return false;
        }
        
        wineOrder.setStatus(OrderStatusEnum.CANCELLED.getCode());
        wineOrder.setCancelReason(cancelReason);
        wineOrder.setCancelTime(new Date());
        
        return this.updateById(wineOrder);
    }

    /**
     * 生成订单号
     * 格式：WO + 控制器兼容时间戳 + 4位随机数
     * 
     * 控制器要求：
     * - 时间戳不能重复使用
     * - 后续指令的时间戳必须大于上一次启动成功的数值
     * - 最小值：102401，最大值：2147483647
     *
     * @return 订单号
     */
    private String generateOrderNo() {
        // 使用工具类生成唯一的控制器时间戳字符串

        // 生成最终的订单号：WO + 控制器时间戳字符串
        String orderNo = ControllerTimestampUtil.generateUniqueTimestampString();
        
        log.debug("生成订单号：{}", orderNo);
        log.debug("订单号长度：{}", orderNo.length());
        
        return orderNo;
    }
} 