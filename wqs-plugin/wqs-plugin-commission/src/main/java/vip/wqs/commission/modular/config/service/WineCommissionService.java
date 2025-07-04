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
package vip.wqs.commission.modular.config.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import vip.wqs.commission.modular.config.entity.WineCommission;
import vip.wqs.commission.modular.config.param.WineCommissionAddParam;
import vip.wqs.commission.modular.config.param.WineCommissionEditParam;
import vip.wqs.commission.modular.config.param.WineCommissionIdParam;
import vip.wqs.commission.modular.config.param.WineCommissionPageParam;

import java.util.List;

/**
 * 分销配置Service
 *
 * @author WQS_TEAM
 * @date 2025-01-29
 */
public interface WineCommissionService extends IService<WineCommission> {

    /**
     * 获取分销配置分页
     * @param wineCommissionPageParam 查询参数
     * @return 分页结果
     */
    Page<WineCommission> page(WineCommissionPageParam wineCommissionPageParam);

    /**
     * 添加分销配置
     * @param wineCommissionAddParam 添加参数
     */
    void add(WineCommissionAddParam wineCommissionAddParam);

    /**
     * 编辑分销配置
     * @param wineCommissionEditParam 编辑参数
     */
    void edit(WineCommissionEditParam wineCommissionEditParam);

    /**
     * 删除分销配置
     * @param wineCommissionIdParam ID参数
     */
    void delete(WineCommissionIdParam wineCommissionIdParam);

    /**
     * 批量删除分销配置
     * @param wineCommissionIdParamList ID参数列表
     */
    void batchDelete(List<WineCommissionIdParam> wineCommissionIdParamList);

    /**
     * 获取分销配置详情
     * @param wineCommissionIdParam ID参数
     * @return 分销配置详情
     */
    WineCommission detail(WineCommissionIdParam wineCommissionIdParam);

    /**
     * 根据门店和酒品获取分销配置（含优先级查询）
     * @param storeId 门店ID
     * @param productId 酒品ID
     * @return 分销配置
     */
    WineCommission getCommissionByStoreAndProduct(String storeId, String productId);

    /**
     * 获取门店的所有分销配置
     * @param storeId 门店ID
     * @return 分销配置列表
     */
    List<WineCommission> getByStoreId(String storeId);

    /**
     * 获取酒品的所有分销配置
     * @param productId 酒品ID
     * @return 分销配置列表
     */
    List<WineCommission> getByProductId(String productId);

    /**
     * 获取平台默认配置
     * @return 平台默认配置
     */
    WineCommission getDefaultConfig();

    /**
     * 启用分销配置
     * @param wineCommissionIdParam ID参数
     */
    void enable(WineCommissionIdParam wineCommissionIdParam);

    /**
     * 禁用分销配置
     * @param wineCommissionIdParam ID参数
     */
    void disable(WineCommissionIdParam wineCommissionIdParam);

    /**
     * 批量启用分销配置
     * @param wineCommissionIdParamList ID参数列表
     */
    void batchEnable(List<WineCommissionIdParam> wineCommissionIdParamList);

    /**
     * 批量禁用分销配置
     * @param wineCommissionIdParamList ID参数列表
     */
    void batchDisable(List<WineCommissionIdParam> wineCommissionIdParamList);

    /**
     * 批量设置分销配置
     * @param storeIds 门店ID列表
     * @param productIds 酒品ID列表
     * @param wineCommissionAddParam 分销配置参数
     */
    void batchSet(List<String> storeIds, List<String> productIds, WineCommissionAddParam wineCommissionAddParam);

    /**
     * 获取有效的佣金配置（按优先级查找）
     * 优先级：门店+酒品专属 > 门店通用 > 酒品通用 > 平台默认
     * 
     * @param storeId 门店ID
     * @param productId 酒品ID
     * @return 有效的佣金配置，找不到返回null
     */
    WineCommission getEffectiveCommission(String storeId, String productId);
} 