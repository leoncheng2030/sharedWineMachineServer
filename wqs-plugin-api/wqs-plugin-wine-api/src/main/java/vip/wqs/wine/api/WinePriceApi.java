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
package vip.wqs.wine.api;

import vip.wqs.wine.pojo.WinePricePojo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 酒品价格Api
 *
 * @author WQS_TEAM
 * @date 2025/1/27 酒品管理模块开发
 **/
public interface WinePriceApi {

    /**
     * 获取酒品价格列表
     *
     * @param productId 查询参数
     * @return 价格列表
     * @author WQS_TEAM
     * @date 2025/1/27
     */
    List<WinePricePojo> getWinePriceList(String productId);

    /**
     * 获取酒品当前价格
     *
     * @param productId 酒品ID
     * @param storeId 门店ID（可选，为空则获取平台统一价格）
     * @return 价格信息
     * @author WQS_TEAM
     * @date 2025/1/27
     */
    BigDecimal getWinePrice(String productId, String storeId);

    /**
     * 获取酒品价格详情
     *
     * @param productId 酒品ID
     * @param storeId 门店ID（可选）
     * @return 价格详情信息
     * @author WQS_TEAM
     * @date 2025/1/27
     */
    WinePricePojo getWinePriceDetail(String productId, String storeId);

    /**
     * 获取酒品价格历史
     *
     * @param productId 酒品ID
     * @param storeId 门店ID（可选）
     * @return 价格历史列表
     * @author WQS_TEAM
     * @date 2025/1/27
     */
    List<WinePricePojo> getWinePriceHistory(String productId, String storeId);

    /**
     * 获取酒品在所有门店的价格
     *
     * @param productId 酒品ID
     * @return 所有门店价格列表
     * @author WQS_TEAM
     * @date 2025/1/30
     */
    List<WinePricePojo> getWinePriceByAllStores(String productId);

    /**
     * 检查酒品是否有有效价格
     *
     * @param productId 酒品ID
     * @param storeId 门店ID（可选）
     * @return 是否有有效价格
     * @author WQS_TEAM
     * @date 2025/1/27
     */
    Boolean hasValidPrice(String productId, String storeId);

    /**
     * 批量获取酒品价格
     *
     * @param productIds 酒品ID列表
     * @param storeId 门店ID（可选）
     * @return 价格信息映射 (productId -> price)
     * @author WQS_TEAM
     * @date 2025/1/27
     */
    Map<String, BigDecimal> batchGetWinePrice(List<String> productIds, String storeId);

    /**
     * 批量获取酒品价格详情
     *
     * @param productIds 酒品ID列表
     * @param storeId 门店ID（可选）
     * @return 价格详情列表
     * @author WQS_TEAM
     * @date 2025/1/30
     */
    List<WinePricePojo> batchGetWinePriceDetail(List<String> productIds, String storeId);

    /**
     * 获取门店所有酒品的价格
     *
     * @param storeId 门店ID
     * @return 门店酒品价格列表
     * @author WQS_TEAM
     * @date 2025/1/30
     */
    List<WinePricePojo> getStorePriceList(String storeId);
} 