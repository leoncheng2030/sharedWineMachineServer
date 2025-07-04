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

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vip.wqs.common.exception.CommonException;
import vip.wqs.device.modular.stock.entity.WineStockLog;
import vip.wqs.device.modular.stock.mapper.WineStockLogMapper;
import vip.wqs.device.modular.stock.param.WineStockLogPageParam;
import vip.wqs.device.modular.stock.service.WineStockLogService;

import java.util.List;

/**
 * 库存变更日志Service实现类
 *
 * @author AI Assistant
 * @date 2025/01/30
 */
@Slf4j
@Service
public class WineStockLogServiceImpl extends ServiceImpl<WineStockLogMapper, WineStockLog>
        implements WineStockLogService {

    @Override
    public List<WineStockLog> getLogsByDeviceId(String deviceId) {
        if (StrUtil.isBlank(deviceId)) {
            throw new CommonException("设备ID不能为空");
        }
        QueryWrapper<WineStockLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_id", deviceId)
                .orderByDesc("create_time");
        return this.list(queryWrapper);
    }

    @Override
    public List<WineStockLog> getLogsByProductId(String productId) {
        if (StrUtil.isBlank(productId)) {
            throw new CommonException("酒品ID不能为空");
        }
        QueryWrapper<WineStockLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_id", productId)
                .orderByDesc("create_time");
        return this.list(queryWrapper);
    }

    @Override
    public List<WineStockLog> getLogsByDeviceAndProduct(String deviceId, String productId) {
        if (StrUtil.isBlank(deviceId) || StrUtil.isBlank(productId)) {
            throw new CommonException("设备ID和酒品ID不能为空");
        }
        QueryWrapper<WineStockLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_id", deviceId)
                .eq("product_id", productId)
                .orderByDesc("create_time");
        return this.list(queryWrapper);
    }

    @Override
    public Page<WineStockLog> page(WineStockLogPageParam param) {
        QueryWrapper<WineStockLog> queryWrapper = new QueryWrapper<>();

        // 设备ID过滤
        if (StrUtil.isNotBlank(param.getDeviceId())) {
            queryWrapper.eq("device_id", param.getDeviceId());
        }

        // 酒品ID过滤
        if (StrUtil.isNotBlank(param.getProductId())) {
            queryWrapper.eq("product_id", param.getProductId());
        }

        // 变更类型过滤
        if (StrUtil.isNotBlank(param.getChangeType())) {
            queryWrapper.eq("change_type", param.getChangeType());
        }

        // 操作人过滤
        if (StrUtil.isNotBlank(param.getOperator())) {
            queryWrapper.eq("operator", param.getOperator());
        }

        // 关键词搜索
        if (StrUtil.isNotBlank(param.getSearchKey())) {
            queryWrapper.and(wrapper -> wrapper
                    .like("device_code", param.getSearchKey())
                    .or().like("device_name", param.getSearchKey())
                    .or().like("product_name", param.getSearchKey())
                    .or().like("operator", param.getSearchKey()));
        }

        // 创建时间范围
        if (param.getCreateTimeStart() != null) {
            queryWrapper.ge("create_time", param.getCreateTimeStart());
        }
        if (param.getCreateTimeEnd() != null) {
            queryWrapper.le("create_time", param.getCreateTimeEnd());
        }

        // 变更数量范围
        if (param.getMinChangeQuantity() != null) {
            queryWrapper.ge("change_quantity", param.getMinChangeQuantity());
        }
        if (param.getMaxChangeQuantity() != null) {
            queryWrapper.le("change_quantity", param.getMaxChangeQuantity());
        }

        // 排序
        if (StrUtil.isNotBlank(param.getSortField()) && StrUtil.isNotBlank(param.getSortOrder())) {
            if ("ASCEND".equals(param.getSortOrder())) {
                queryWrapper.orderByAsc(param.getSortField());
            } else {
                queryWrapper.orderByDesc(param.getSortField());
            }
        } else {
            // 默认按创建时间倒序
            queryWrapper.orderByDesc("create_time");
        }

        // 分页查询
        Page<WineStockLog> page = new Page<>(param.getCurrent(), param.getSize());
        return this.page(page, queryWrapper);
    }
}