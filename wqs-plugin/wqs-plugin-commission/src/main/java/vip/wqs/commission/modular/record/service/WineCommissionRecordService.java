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
package vip.wqs.commission.modular.record.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import vip.wqs.commission.modular.record.entity.WineCommissionRecord;
import vip.wqs.commission.modular.record.param.WineCommissionRecordIdParam;
import vip.wqs.commission.modular.record.param.WineCommissionRecordPageParam;

import java.util.List;

/**
 * 佣金记录Service接口
 *
 * @author WQS_TEAM
 * @date 2025-01-29
 */
public interface WineCommissionRecordService extends IService<WineCommissionRecord> {

    /**
     * 获取佣金记录分页
     */
    Page<WineCommissionRecord> page(WineCommissionRecordPageParam wineCommissionRecordPageParam);

    /**
     * 获取佣金记录列表
     */
    List<WineCommissionRecord> list(WineCommissionRecordPageParam wineCommissionRecordPageParam);

    /**
     * 获取佣金记录详情
     */
    WineCommissionRecord detail(WineCommissionRecordIdParam wineCommissionRecordIdParam);

    /**
     * 获取佣金记录详情
     */
    WineCommissionRecord queryEntity(String id);

    /**
     * 佣金记录导出
     */
    void export(WineCommissionRecordPageParam wineCommissionRecordPageParam);

    /**
     * 批量发放佣金
     */
    void batchSettle(List<WineCommissionRecordIdParam> wineCommissionRecordIdParamList);

    /**
     * 批量冻结佣金
     */
    void batchFreeze(List<WineCommissionRecordIdParam> wineCommissionRecordIdParamList);

    /**
     * 批量取消佣金
     */
    void batchCancel(List<WineCommissionRecordIdParam> wineCommissionRecordIdParamList);

    /**
     * 发放佣金
     */
    void settle(WineCommissionRecordIdParam wineCommissionRecordIdParam);

    /**
     * 冻结佣金
     */
    void freeze(WineCommissionRecordIdParam wineCommissionRecordIdParam);

    /**
     * 取消佣金
     */
    void cancel(WineCommissionRecordIdParam wineCommissionRecordIdParam);

    /**
     * 重新计算佣金
     */
    void recalculate(WineCommissionRecordIdParam wineCommissionRecordIdParam);

    /**
     * 订单完成后分配佣金（核心业务方法）
     * 
     * @param orderId 订单ID
     * @param orderNo 订单号
     * @param orderAmount 订单金额
     * @param deviceId 设备ID
     * @param wineId 酒品ID
     * @param storeId 门店ID（可选）
     * @return 分配成功的佣金记录数量
     */
    int distributeCommissionForOrder(String orderId, String orderNo, java.math.BigDecimal orderAmount, 
                                   String deviceId, String wineId, String storeId);

    /**
     * 查找角色归属用户ID
     * 
     * @param commissionType 佣金类型（PLATFORM/DEVICE_OWNER/LOCATION_PROVIDER/STORE_MANAGER/SUPPLIER）
     * @param deviceId 设备ID
     * @param storeId 门店ID
     * @param wineId 酒品ID
     * @return 归属用户ID，找不到返回null
     */
    String findRoleOwnerId(String commissionType, String deviceId, String storeId, String wineId);

    /**
     * 计算指定角色的佣金金额
     * 
     * @param orderAmount 订单金额
     * @param commissionRate 佣金比例
     * @return 佣金金额
     */
    java.math.BigDecimal calculateCommissionAmount(java.math.BigDecimal orderAmount, java.math.BigDecimal commissionRate);
} 