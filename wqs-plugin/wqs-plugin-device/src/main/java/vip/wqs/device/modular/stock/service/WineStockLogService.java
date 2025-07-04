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
package vip.wqs.device.modular.stock.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import vip.wqs.device.modular.stock.entity.WineStockLog;
import vip.wqs.device.modular.stock.param.WineStockLogPageParam;

import java.util.List;

/**
 * 库存变更日志Service接口
 *
 * @author AI Assistant
 * @date 2025/01/30
 */
public interface WineStockLogService extends IService<WineStockLog> {

    /**
     * 根据设备ID查询库存日志
     *
     * @param deviceId 设备ID
     * @return 库存日志列表
     */
    List<WineStockLog> getLogsByDeviceId(String deviceId);

    /**
     * 根据酒品ID查询库存日志
     *
     * @param productId 酒品ID
     * @return 库存日志列表
     */
    List<WineStockLog> getLogsByProductId(String productId);

    /**
     * 根据设备ID和酒品ID查询库存日志
     *
     * @param deviceId  设备ID
     * @param productId 酒品ID
     * @return 库存日志列表
     */
    List<WineStockLog> getLogsByDeviceAndProduct(String deviceId, String productId);

    /**
     * 分页查询库存日志
     *
     * @param param 分页查询参数
     * @return 分页结果
     */
    Page<WineStockLog> page(WineStockLogPageParam param);
}