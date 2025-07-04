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
package vip.wqs.wine.modular.product.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import vip.wqs.wine.modular.product.entity.WineProduct;
import vip.wqs.wine.modular.product.param.*;

import java.util.List;

/**
 * 酒品Service接口
 *
 * @author wqs
 * @date 2025/1/27
 */
public interface WineProductService extends IService<WineProduct> {

    /**
     * 获取酒品分页
     *
     * @param wineProductPageParam 分页参数
     * @return 分页结果
     * @author wqs
     * @date 2025/1/27
     */
    Page<WineProduct> page(WineProductPageParam wineProductPageParam);

    /**
     * 获取酒品列表
     *
     * @param wineProductPageParam 查询参数
     * @return 酒品列表
     * @author wqs
     * @date 2025/1/27
     */
    List<WineProduct> list(WineProductPageParam wineProductPageParam);

    /**
     * 添加酒品
     *
     * @param wineProductAddParam 新增参数
     * @author wqs
     * @date 2025/1/27
     */
    void add(WineProductAddParam wineProductAddParam);

    /**
     * 编辑酒品
     *
     * @param wineProductEditParam 编辑参数
     * @author wqs
     * @date 2025/1/27
     */
    void edit(WineProductEditParam wineProductEditParam);

    /**
     * 删除酒品
     *
     * @param wineProductIdParam 删除参数
     * @author wqs
     * @date 2025/1/27
     */
    void delete(WineProductIdParam wineProductIdParam);

    /**
     * 批量删除酒品
     *
     * @param wineProductIdParamList 删除参数列表
     * @author wqs
     * @date 2025/1/27
     */
    void batchDelete(List<WineProductIdParam> wineProductIdParamList);

    /**
     * 获取酒品详情
     *
     * @param wineProductIdParam 查询参数
     * @return 酒品详情
     * @author wqs
     * @date 2025/1/27
     */
    WineProduct detail(WineProductIdParam wineProductIdParam);

    /**
     * 获取酒品详情
     *
     * @param id 酒品ID
     * @return 酒品详情
     * @author wqs
     * @date 2025/1/27
     */
    WineProduct queryEntity(String id);

    /**
     * 禁用酒品
     *
     * @param wineProductIdParam 禁用参数
     * @author wqs
     * @date 2025/1/27
     */
    void disable(WineProductIdParam wineProductIdParam);

    /**
     * 启用酒品
     *
     * @param wineProductIdParam 启用参数
     * @author wqs
     * @date 2025/1/27
     */
    void enable(WineProductIdParam wineProductIdParam);

    /**
     * 酒品导出
     *
     * @param wineProductExportParam 导出参数
     * @author wqs
     * @date 2025/1/27
     */
    void export(WineProductExportParam wineProductExportParam);

    /**
     * 酒品选择器
     *
     * @param wineProductSelectorParam 选择器参数
     * @return 酒品列表
     * @author wqs
     * @date 2025/1/27
     */
    List<WineProduct> selector(WineProductSelectorParam wineProductSelectorParam);

    /**
     * 获取酒品分类选择器
     *
     * @return 分类列表
     * @author wqs
     * @date 2025/1/27
     */
    Object categorySelector();

    /**
     * 上传酒品图片
     *
     * @param uploadParam 上传参数
     * @return 图片URL
     * @author wqs
     * @date 2025/1/27
     */
    Object uploadImage(Object uploadParam);

    /**
     * 获取酒品库存
     *
     * @param wineProductIdParam 查询参数
     * @return 库存信息
     * @author wqs
     * @date 2025/1/27
     */
    Object getStock(WineProductIdParam wineProductIdParam);

    /**
     * 更新酒品库存
     *
     * @param stockUpdateParam 库存更新参数
     * @author wqs
     * @date 2025/1/27
     */
    void updateStock(WineProductStockUpdateParam stockUpdateParam);
} 