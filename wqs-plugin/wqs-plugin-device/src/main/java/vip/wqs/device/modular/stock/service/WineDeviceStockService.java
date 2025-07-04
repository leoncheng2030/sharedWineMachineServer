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
import vip.wqs.device.modular.stock.entity.WineDeviceStock;
import vip.wqs.device.modular.stock.param.WineDeviceStockPageParam;

import java.math.BigDecimal;
import java.util.List;

/**
 * 设备库存Service接口
 *
 * @author AI Assistant
 * @date 2025/01/30
 */
public interface WineDeviceStockService extends IService<WineDeviceStock> {

    /**
     * 获取设备库存分页列表
     *
     * @param param 查询参数
     * @return 分页结果
     */
    Page<WineDeviceStock> page(WineDeviceStockPageParam param);

    /**
     * 根据设备ID和酒品ID查询库存数量
     *
     * @param deviceId  设备ID
     * @param productId 酒品ID
     * @return 库存数量(ml)
     */
    BigDecimal getStockQuantity(String deviceId, String productId);

    /**
     * 补货
     *
     * @param deviceId  设备ID
     * @param productId 酒品ID
     * @param quantity  补货数量
     * @param operator  操作人
     * @param reason    补货原因
     */
    void refill(String deviceId, String productId, BigDecimal quantity, String operator, String reason);

    /**
     * 调整库存
     *
     * @param deviceId  设备ID
     * @param productId 酒品ID
     * @param quantity  调整后的数量
     * @param operator  操作人
     * @param reason    调整原因
     */
    void adjustStock(String deviceId, String productId, BigDecimal quantity, String operator, String reason);

    /**
     * 销售扣减库存
     *
     * @param deviceId  设备ID
     * @param productId 酒品ID
     * @param quantity  销售数量
     * @param operator  操作人
     */
    void saleDeduct(String deviceId, String productId, BigDecimal quantity, String operator);

    /**
     * 查询低库存设备列表
     *
     * @return 低库存设备列表
     */
    List<WineDeviceStock> getLowStockList();

    /**
     * 查询缺货设备列表
     *
     * @return 缺货设备列表
     */
    List<WineDeviceStock> getOutOfStockList();

    /**
     * 根据设备ID查询库存列表
     *
     * @param deviceId 设备ID
     * @return 库存列表
     */
    List<WineDeviceStock> getStockByDeviceId(String deviceId);

    /**
     * 根据酒品ID查询库存列表
     *
     * @param productId 酒品ID
     * @return 库存列表
     */
    List<WineDeviceStock> getStockByProductId(String productId);

    /**
     * 初始化设备库存
     *
     * @param deviceId        设备ID
     * @param productId       酒品ID
     * @param initialQuantity 初始库存
     * @param alertThreshold  预警阈值
     * @param operator        操作人
     */
    void initializeStock(String deviceId, String productId, BigDecimal initialQuantity, BigDecimal alertThreshold,
            String operator);

    /**
     * 检查库存是否充足
     *
     * @param deviceId         设备ID
     * @param productId        酒品ID
     * @param requiredQuantity 需要的数量
     * @return 是否充足
     */
    Boolean checkStockSufficient(String deviceId, String productId, BigDecimal requiredQuantity);

    /**
     * 扣减库存
     *
     * @param deviceId  设备ID
     * @param productId 酒品ID
     * @param quantity  扣减数量
     * @param operator  操作人
     * @param reason    扣减原因
     */
    void deductStock(String deviceId, String productId, BigDecimal quantity, String operator, String reason);

}