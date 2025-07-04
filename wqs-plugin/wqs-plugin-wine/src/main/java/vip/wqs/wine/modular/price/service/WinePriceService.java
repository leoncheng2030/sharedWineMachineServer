/*
 * Copyright [2024] [WQS]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package vip.wqs.wine.modular.price.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import vip.wqs.wine.modular.price.entity.WinePrice;
import vip.wqs.wine.modular.price.param.*;

import java.util.List;

/**
 * 酒品价格表 Service接口
 *
 * @author WQS
 * @date 2025/01/27
 */
public interface WinePriceService extends IService<WinePrice> {

    /**
     * 分页查询
     *
     * @param pageParam
     * @return
     */
    Page<WinePrice> page(WinePricePageParam pageParam);

    /**
     * 添加
     *
     * @param addParam
     */
    void add(WinePriceAddParam addParam);

    /**
     * 编辑
     *
     * @param editParam
     */
    void edit(WinePriceEditParam editParam);

    /**
     * 删除
     *
     * @param idParam
     */
    void delete(WinePriceIdParam idParam);

    /**
     * 详情
     *
     * @param idParam
     * @return
     */
    WinePrice detail(WinePriceIdParam idParam);

    /**
     * 批量删除
     *
     * @param winePriceIdParamList
     */
    void batchDelete(List<WinePriceIdParam> winePriceIdParamList);

    /**
     * 启用价格策略
     *
     * @param winePriceIdParam
     */
    void enable(WinePriceIdParam winePriceIdParam);

    /**
     * 禁用价格策略
     *
     * @param winePriceIdParam
     */
    void disable(WinePriceIdParam winePriceIdParam);

    /**
     * 获取酒品选择器
     *
     * @return
     */
    Object productSelector();

    /**
     * 导出酒品价格
     *
     * @param exportParam 导出参数
     * @author wqs
     * @date 2025/1/27
     */
    void export(WinePriceExportParam exportParam);

    /**
     * 获取价格策略列表（不分页）
     *
     * @param winePricePageParam
     * @return
     */
    List<WinePrice> list(WinePricePageParam winePricePageParam);

    /**
     * 批量设置价格
     *
     * @param batchSetParam 批量设置参数
     * @author wqs
     * @date 2025/1/27
     */
    void batchSet(WinePriceBatchSetParam batchSetParam);

    /**
     * 复制价格策略
     *
     * @param winePriceIdParam
     */
    void copy(WinePriceIdParam winePriceIdParam);

    /**
     * 获取价格历史记录
     *
     * @param winePriceIdParam
     * @return
     */
    Object history(WinePriceIdParam winePriceIdParam);

    /**
     * 价格预览
     *
     * @param previewParam 预览参数
     * @return 预览结果
     * @author wqs
     * @date 2025/1/27
     */
    Object preview(WinePricePreviewParam previewParam);

    /**
     * 获取当前有效价格
     *
     * @param winePriceIdParam
     * @return
     */
    Object current(WinePriceIdParam winePriceIdParam);

    /**
     * 获取门店选择器
     *
     * @return 门店列表
     * @author wqs
     * @date 2025/1/27
     */
    Object storeSelector();

    /**
     * 根据门店和酒品查询价格
     *
     * @param storeId 门店ID（NULL查询平台价格）
     * @param productId 酒品ID
     * @param capacity 容量
     * @return 价格信息
     * @author wqs
     * @date 2025/1/27
     */
    WinePrice getPriceByStoreAndProduct(String storeId, String productId, String capacity);

    /**
     * 根据门店ID获取价格列表
     *
     * @param storeId 门店ID
     * @return 价格列表
     * @author wqs
     * @date 2025/1/27
     */
    List<WinePrice> getPricesByStore(String storeId);

    /**
     * 复制平台价格到门店
     *
     * @param storeId 门店ID
     * @param productIds 酒品ID列表
     * @author wqs
     * @date 2025/1/27
     */
    void copyPlatformPricesToStore(String storeId, List<String> productIds);
} 