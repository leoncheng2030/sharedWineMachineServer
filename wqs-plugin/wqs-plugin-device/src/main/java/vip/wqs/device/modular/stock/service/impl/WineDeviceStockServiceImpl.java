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
package vip.wqs.device.modular.stock.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fhs.core.trans.anno.TransMethodResult;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.wqs.common.exception.CommonException;
import vip.wqs.common.page.CommonPageRequest;
import vip.wqs.device.modular.stock.entity.WineDeviceStock;
import vip.wqs.device.modular.stock.entity.WineStockLog;
import vip.wqs.device.modular.stock.mapper.WineDeviceStockMapper;
import vip.wqs.device.modular.stock.param.WineDeviceStockPageParam;
import vip.wqs.device.modular.stock.service.WineDeviceStockService;
import vip.wqs.device.modular.stock.service.WineStockLogService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 设备库存Service实现类
 *
 * @author AI Assistant
 * @date 2025/01/30
 */
@Slf4j
@Service
public class WineDeviceStockServiceImpl extends ServiceImpl<WineDeviceStockMapper, WineDeviceStock>
        implements WineDeviceStockService {

    @Resource
    private WineStockLogService wineStockLogService;

    @Override
    @TransMethodResult
    public Page<WineDeviceStock> page(WineDeviceStockPageParam param) {
        QueryWrapper<WineDeviceStock> queryWrapper = new QueryWrapper<>();

        // 设备ID条件 - 这是关键的过滤条件
        if (StrUtil.isNotBlank(param.getDeviceId())) {
            queryWrapper.eq("device_id", param.getDeviceId());
        }

        // 酒品ID条件
        if (StrUtil.isNotBlank(param.getProductId())) {
            queryWrapper.eq("product_id", param.getProductId());
        }

        // 库存状态条件
        if (StrUtil.isNotBlank(param.getStatus())) {
            queryWrapper.eq("status", param.getStatus());
        }

        // 库存数量范围
        if (param.getMinStockQuantity() != null) {
            queryWrapper.ge("stock_quantity", param.getMinStockQuantity());
        }
        if (param.getMaxStockQuantity() != null) {
            queryWrapper.le("stock_quantity", param.getMaxStockQuantity());
        }

        // 预警阈值范围
        if (param.getMinAlertThreshold() != null) {
            queryWrapper.ge("alert_threshold", param.getMinAlertThreshold());
        }
        if (param.getMaxAlertThreshold() != null) {
            queryWrapper.le("alert_threshold", param.getMaxAlertThreshold());
        }

        // 默认排序
        queryWrapper.orderByDesc("update_time");

        // 分页
        Page<WineDeviceStock> page = CommonPageRequest.defaultPage();
        if (param.getCurrent() != null && param.getSize() != null) {
            page = new Page<>(param.getCurrent(), param.getSize());
        }

        return this.page(page, queryWrapper);
    }

    @Override
    public BigDecimal getStockQuantity(String deviceId, String productId) {
        if (StrUtil.isBlank(deviceId) || StrUtil.isBlank(productId)) {
            return BigDecimal.ZERO;
        }

        QueryWrapper<WineDeviceStock> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_id", deviceId)
                .eq("product_id", productId);
        WineDeviceStock stock = this.getOne(queryWrapper);

        return stock != null && stock.getStockQuantity() != null ? stock.getStockQuantity() : BigDecimal.ZERO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refill(String deviceId, String productId, BigDecimal quantity, String operator, String reason) {
        if (StrUtil.isBlank(deviceId) || StrUtil.isBlank(productId)) {
            throw new CommonException("设备ID和酒品ID不能为空");
        }
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new CommonException("补货数量必须大于0");
        }

        // 查询现有库存
        WineDeviceStock stock = getExistingStock(deviceId, productId);
        BigDecimal beforeQuantity = stock.getStockQuantity();
        BigDecimal afterQuantity = beforeQuantity.add(quantity);

        // 更新库存
        stock.setStockQuantity(afterQuantity);
        stock.setLastRefillTime(LocalDateTime.now());
        stock.setLastRefillQuantity(quantity);
        stock.updateStatus();
        this.updateById(stock);

        // 记录日志
        recordStockLog(deviceId, productId, WineStockLog.ChangeType.REFILL,
                quantity, beforeQuantity, afterQuantity, reason, operator);

        log.info("设备补货成功，设备ID：{}，酒品ID：{}，补货数量：{}，操作人：{}",
                deviceId, productId, quantity, operator);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void adjustStock(String deviceId, String productId, BigDecimal quantity, String operator, String reason) {
        if (StrUtil.isBlank(deviceId) || StrUtil.isBlank(productId)) {
            throw new CommonException("设备ID和酒品ID不能为空");
        }
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) < 0) {
            throw new CommonException("调整后的库存数量不能为负数");
        }

        // 查询现有库存
        WineDeviceStock stock = getExistingStock(deviceId, productId);
        BigDecimal beforeQuantity = stock.getStockQuantity();
        BigDecimal changeQuantity = quantity.subtract(beforeQuantity);

        // 更新库存
        stock.setStockQuantity(quantity);
        stock.updateStatus();
        this.updateById(stock);

        // 记录日志
        recordStockLog(deviceId, productId, WineStockLog.ChangeType.ADJUST,
                changeQuantity, beforeQuantity, quantity, reason, operator);

        log.info("设备库存调整成功，设备ID：{}，酒品ID：{}，调整后数量：{}，操作人：{}",
                deviceId, productId, quantity, operator);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saleDeduct(String deviceId, String productId, BigDecimal quantity, String operator) {
        if (StrUtil.isBlank(deviceId) || StrUtil.isBlank(productId)) {
            throw new CommonException("设备ID和酒品ID不能为空");
        }
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new CommonException("销售数量必须大于0");
        }

        // 查询现有库存
        WineDeviceStock stock = getExistingStock(deviceId, productId);
        BigDecimal beforeQuantity = stock.getStockQuantity();

        if (beforeQuantity.compareTo(quantity) < 0) {
            throw new CommonException("库存不足，当前库存：" + beforeQuantity + "ml，销售数量：" + quantity + "ml");
        }

        BigDecimal afterQuantity = beforeQuantity.subtract(quantity);

        // 更新库存
        stock.setStockQuantity(afterQuantity);
        // 初始化累计销售量（如果为null）
        if (stock.getTotalSold() == null) {
            stock.setTotalSold(BigDecimal.ZERO);
        }
        stock.setTotalSold(stock.getTotalSold().add(quantity));
        stock.updateStatus();
        this.updateById(stock);

        // 记录日志
        recordStockLog(deviceId, productId, WineStockLog.ChangeType.SALE,
                quantity.negate(), beforeQuantity, afterQuantity, "销售扣减", operator);

        log.info("设备销售扣减成功，设备ID：{}，酒品ID：{}，销售数量：{}，操作人：{}",
                deviceId, productId, quantity, operator);
    }

    @Override
    @TransMethodResult
    public List<WineDeviceStock> getLowStockList() {
        QueryWrapper<WineDeviceStock> queryWrapper = new QueryWrapper<>();
        queryWrapper.le("stock_quantity", "alert_threshold");
        return this.list(queryWrapper);
    }

    @Override
    @TransMethodResult
    public List<WineDeviceStock> getOutOfStockList() {
        QueryWrapper<WineDeviceStock> queryWrapper = new QueryWrapper<>();
        queryWrapper.le("stock_quantity", 0);
        return this.list(queryWrapper);
    }

    @Override
    @TransMethodResult
    public List<WineDeviceStock> getStockByDeviceId(String deviceId) {
        if (StrUtil.isBlank(deviceId)) {
            throw new CommonException("设备ID不能为空");
        }
        QueryWrapper<WineDeviceStock> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_id", deviceId);
        return this.list(queryWrapper);
    }

    @Override
    @TransMethodResult
    public List<WineDeviceStock> getStockByProductId(String productId) {
        if (StrUtil.isBlank(productId)) {
            throw new CommonException("酒品ID不能为空");
        }
        QueryWrapper<WineDeviceStock> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_id", productId);
        return this.list(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void initializeStock(String deviceId, String productId, BigDecimal initialQuantity,
            BigDecimal alertThreshold, String operator) {
        if (StrUtil.isBlank(deviceId) || StrUtil.isBlank(productId)) {
            throw new CommonException("设备ID和酒品ID不能为空");
        }
        if (initialQuantity == null || initialQuantity.compareTo(BigDecimal.ZERO) < 0) {
            throw new CommonException("初始库存不能为负数");
        }

        // 检查是否已存在库存记录
        QueryWrapper<WineDeviceStock> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_id", deviceId)
                .eq("product_id", productId);
        WineDeviceStock existingStock = this.getOne(queryWrapper);

        if (existingStock != null) {
            throw new CommonException("该设备的该酒品库存已存在，请使用补货或调整功能");
        }

        // 创建新的库存记录
        WineDeviceStock stock = new WineDeviceStock();
        stock.setId(IdUtil.getSnowflakeNextIdStr());
        stock.setDeviceId(deviceId);
        stock.setProductId(productId);
        stock.setStockQuantity(initialQuantity);
        stock.setAlertThreshold(alertThreshold != null ? alertThreshold : new BigDecimal("1000"));
        stock.setTotalSold(BigDecimal.ZERO);
        stock.updateStatus();
        this.save(stock);

        // 记录日志
        recordStockLog(deviceId, productId, WineStockLog.ChangeType.REFILL,
                initialQuantity, BigDecimal.ZERO, initialQuantity, "初始化库存", operator);

        log.info("设备库存初始化成功，设备ID：{}，酒品ID：{}，初始数量：{}，操作人：{}",
                deviceId, productId, initialQuantity, operator);
    }

    /**
     * 获取现有库存记录，如果不存在则抛出异常
     */
    private WineDeviceStock getExistingStock(String deviceId, String productId) {
        QueryWrapper<WineDeviceStock> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_id", deviceId)
                .eq("product_id", productId);
        WineDeviceStock stock = this.getOne(queryWrapper);

        if (stock == null) {
            throw new CommonException("未找到该设备的该酒品库存记录，请先初始化库存");
        }

        return stock;
    }

    @Override
    public Boolean checkStockSufficient(String deviceId, String productId, BigDecimal requiredQuantity) {
        if (StrUtil.isBlank(deviceId) || StrUtil.isBlank(productId)) {
            throw new CommonException("设备ID和酒品ID不能为空");
        }
        if (requiredQuantity == null || requiredQuantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new CommonException("需要的数量必须大于0");
        }

        BigDecimal currentQuantity = getStockQuantity(deviceId, productId);
        return currentQuantity.compareTo(requiredQuantity) >= 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deductStock(String deviceId, String productId, BigDecimal quantity, String operator, String reason) {
        if (StrUtil.isBlank(deviceId) || StrUtil.isBlank(productId)) {
            throw new CommonException("设备ID和酒品ID不能为空");
        }
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new CommonException("扣减数量必须大于0");
        }
        if (StrUtil.isBlank(operator)) {
            throw new CommonException("操作人不能为空");
        }

        // 检查库存是否充足
        if (!checkStockSufficient(deviceId, productId, quantity)) {
            throw new CommonException("库存不足，无法扣减");
        }

        // 查询现有库存
        WineDeviceStock stock = getExistingStock(deviceId, productId);
        BigDecimal beforeQuantity = stock.getStockQuantity();
        BigDecimal afterQuantity = beforeQuantity.subtract(quantity);

        // 更新库存
        stock.setStockQuantity(afterQuantity);
        stock.updateStatus();
        this.updateById(stock);

        // 记录日志
        recordStockLog(deviceId, productId, "DEDUCT",
                quantity.negate(), beforeQuantity, afterQuantity, reason, operator);

        log.info("设备库存扣减成功，设备ID：{}，酒品ID：{}，扣减数量：{}，剩余数量：{}",
                deviceId, productId, quantity, afterQuantity);
    }

    /**
     * 记录库存变更日志
     */
    private void recordStockLog(String deviceId, String productId, String changeType,
            BigDecimal changeQuantity, BigDecimal beforeQuantity,
            BigDecimal afterQuantity, String reason, String operator) {
        WineStockLog log = new WineStockLog();
        log.setId(IdUtil.getSnowflakeNextIdStr());
        log.setDeviceId(deviceId);
        log.setProductId(productId);
        log.setChangeType(changeType);
        log.setChangeQuantity(changeQuantity);
        log.setBeforeQuantity(beforeQuantity);
        log.setAfterQuantity(afterQuantity);
        log.setChangeReason(reason);
        log.setOperator(operator);
        log.setCreateTime(LocalDateTime.now());

        wineStockLogService.save(log);
    }

}