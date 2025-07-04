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
package vip.wqs.payment.modular.manage.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.wqs.common.exception.CommonException;
import vip.wqs.common.page.CommonPageRequest;
import vip.wqs.payment.modular.manage.entity.PaymentRecord;
import vip.wqs.payment.modular.manage.enums.PaymentStatusEnum;
import vip.wqs.payment.modular.manage.mapper.PaymentRecordMapper;
import vip.wqs.payment.modular.manage.param.PaymentRecordIdParam;
import vip.wqs.payment.modular.manage.param.PaymentRecordPageParam;
import vip.wqs.payment.modular.manage.service.PaymentRecordService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付记录Service接口实现类
 *
 * @author wqs
 * @date 2025/01/30
 */
@Service
public class PaymentRecordServiceImpl extends ServiceImpl<PaymentRecordMapper, PaymentRecord> implements PaymentRecordService {

    @Override
    public Page<PaymentRecord> page(PaymentRecordPageParam paymentRecordPageParam) {
        QueryWrapper<PaymentRecord> queryWrapper = new QueryWrapper<>();
        
        // 条件查询
        if (StrUtil.isNotBlank(paymentRecordPageParam.getPaymentNo())) {
            queryWrapper.like("payment_no", paymentRecordPageParam.getPaymentNo());
        }
        if (StrUtil.isNotBlank(paymentRecordPageParam.getOrderNo())) {
            queryWrapper.like("order_no", paymentRecordPageParam.getOrderNo());
        }
        if (StrUtil.isNotBlank(paymentRecordPageParam.getUserId())) {
            queryWrapper.eq("user_id", paymentRecordPageParam.getUserId());
        }
        if (StrUtil.isNotBlank(paymentRecordPageParam.getPayType())) {
            queryWrapper.eq("pay_type", paymentRecordPageParam.getPayType());
        }
        if (StrUtil.isNotBlank(paymentRecordPageParam.getPayStatus())) {
            queryWrapper.eq("pay_status", paymentRecordPageParam.getPayStatus());
        }
        if (ObjectUtil.isNotNull(paymentRecordPageParam.getMinAmount())) {
            queryWrapper.ge("pay_amount", paymentRecordPageParam.getMinAmount());
        }
        if (ObjectUtil.isNotNull(paymentRecordPageParam.getMaxAmount())) {
            queryWrapper.le("pay_amount", paymentRecordPageParam.getMaxAmount());
        }
        if (ObjectUtil.isNotNull(paymentRecordPageParam.getStartTime())) {
            queryWrapper.ge("create_time", paymentRecordPageParam.getStartTime());
        }
        if (ObjectUtil.isNotNull(paymentRecordPageParam.getEndTime())) {
            queryWrapper.le("create_time", paymentRecordPageParam.getEndTime());
        }
        if (StrUtil.isNotBlank(paymentRecordPageParam.getTransactionId())) {
            queryWrapper.eq("transaction_id", paymentRecordPageParam.getTransactionId());
        }
        
        // 按创建时间倒序排列
        queryWrapper.orderByDesc("create_time");
        
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Override
    public PaymentRecord detail(PaymentRecordIdParam paymentRecordIdParam) {
        return this.queryEntity(paymentRecordIdParam.getId());
    }

    @Override
    public PaymentRecord queryEntity(String id) {
        PaymentRecord paymentRecord = this.getById(id);
        if (ObjectUtil.isNull(paymentRecord)) {
            throw new CommonException("支付记录不存在，id值为：{}", id);
        }
        return paymentRecord;
    }

    @Override
    public String createPaymentRecord(String orderId, String orderNo, String userId, 
                                     String payType, BigDecimal payAmount, String clientIp, String body) {
        PaymentRecord paymentRecord = new PaymentRecord();
        paymentRecord.setId(IdUtil.fastSimpleUUID());
        paymentRecord.setPaymentNo(generatePaymentNo());
        paymentRecord.setOrderId(orderId);
        paymentRecord.setOrderNo(orderNo);
        paymentRecord.setUserId(userId);
        paymentRecord.setPayType(payType);
        paymentRecord.setPayAmount(payAmount);
        paymentRecord.setPayStatus(PaymentStatusEnum.PENDING.getCode());
        
        this.save(paymentRecord);
        return paymentRecord.getId();
    }

    @Override
    public PaymentRecord getByOrderId(String orderId) {
        QueryWrapper<PaymentRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderId);
        return this.getOne(queryWrapper);
    }

    @Override
    public PaymentRecord getByPaymentNo(String paymentNo) {
        QueryWrapper<PaymentRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("payment_no", paymentNo);
        return this.getOne(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updatePaymentSuccess(String paymentId, String transactionId, 
                                       BigDecimal actualAmount, String callbackContent) {
        PaymentRecord paymentRecord = this.getById(paymentId);
        if (ObjectUtil.isNull(paymentRecord)) {
            return false;
        }
        
        paymentRecord.setPayStatus(PaymentStatusEnum.SUCCESS.getCode());
        paymentRecord.setTransactionId(transactionId);
        paymentRecord.setActualAmount(actualAmount);
        paymentRecord.setPayTime(LocalDateTime.now());
        paymentRecord.setCallbackTime(LocalDateTime.now());
        paymentRecord.setCallbackContent(callbackContent);
        
        return this.updateById(paymentRecord);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updatePaymentFailed(String paymentId, String failReason) {
        PaymentRecord paymentRecord = this.getById(paymentId);
        if (ObjectUtil.isNull(paymentRecord)) {
            return false;
        }
        
        paymentRecord.setPayStatus(PaymentStatusEnum.FAILED.getCode());
        paymentRecord.setCallbackTime(LocalDateTime.now());
        paymentRecord.setCallbackContent(failReason);
        
        return this.updateById(paymentRecord);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updatePrepayId(String paymentId, String prepayId) {
        PaymentRecord paymentRecord = this.getById(paymentId);
        if (ObjectUtil.isNull(paymentRecord)) {
            return false;
        }
        
        paymentRecord.setPrepayId(prepayId);
        return this.updateById(paymentRecord);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean applyRefund(String paymentId, BigDecimal refundAmount, String refundReason) {
        PaymentRecord paymentRecord = this.getById(paymentId);
        if (ObjectUtil.isNull(paymentRecord)) {
            return false;
        }
        
        // 检查是否可以退款
        if (!PaymentStatusEnum.SUCCESS.getCode().equals(paymentRecord.getPayStatus())) {
            return false;
        }
        
        paymentRecord.setRefundAmount(refundAmount);
        paymentRecord.setRefundTime(LocalDateTime.now());
        
        // 判断是全额退款还是部分退款
        if (refundAmount.compareTo(paymentRecord.getPayAmount()) >= 0) {
            paymentRecord.setPayStatus(PaymentStatusEnum.REFUNDED.getCode());
        } else {
            paymentRecord.setPayStatus(PaymentStatusEnum.PARTIAL_REFUNDED.getCode());
        }
        
        return this.updateById(paymentRecord);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateRefundStatus(String paymentId, String refundNo, BigDecimal refundAmount) {
        PaymentRecord paymentRecord = this.getById(paymentId);
        if (ObjectUtil.isNull(paymentRecord)) {
            return false;
        }
        
        paymentRecord.setRefundNo(refundNo);
        paymentRecord.setRefundAmount(refundAmount);
        paymentRecord.setRefundTime(LocalDateTime.now());
        
        // 判断是全额退款还是部分退款
        if (refundAmount.compareTo(paymentRecord.getPayAmount()) >= 0) {
            paymentRecord.setPayStatus(PaymentStatusEnum.REFUNDED.getCode());
        } else {
            paymentRecord.setPayStatus(PaymentStatusEnum.PARTIAL_REFUNDED.getCode());
        }
        
        return this.updateById(paymentRecord);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean cancelPayment(String paymentId) {
        PaymentRecord paymentRecord = this.getById(paymentId);
        if (ObjectUtil.isNull(paymentRecord)) {
            return false;
        }
        
        // 只有待支付状态才能取消
        if (!PaymentStatusEnum.PENDING.getCode().equals(paymentRecord.getPayStatus())) {
            return false;
        }
        
        paymentRecord.setPayStatus(PaymentStatusEnum.CANCELLED.getCode());
        return this.updateById(paymentRecord);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean expirePayment(String paymentId) {
        PaymentRecord paymentRecord = this.getById(paymentId);
        if (ObjectUtil.isNull(paymentRecord)) {
            return false;
        }
        
        // 只有待支付状态才能过期
        if (!PaymentStatusEnum.PENDING.getCode().equals(paymentRecord.getPayStatus())) {
            return false;
        }
        
        paymentRecord.setPayStatus(PaymentStatusEnum.EXPIRED.getCode());
        return this.updateById(paymentRecord);
    }

    /**
     * 生成支付单号
     * 格式：PAY + yyyyMMddHHmmss + 4位随机数
     *
     * @return 支付单号
     */
    private String generatePaymentNo() {
        return "PAY" + System.currentTimeMillis() + String.format("%04d", (int)(Math.random() * 10000));
    }
} 