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
package vip.wqs.store.modular.manage.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import vip.wqs.store.modular.manage.entity.WineStore;
import vip.wqs.store.modular.manage.param.WineStoreAddParam;
import vip.wqs.store.modular.manage.param.WineStoreEditParam;
import vip.wqs.store.modular.manage.param.WineStoreIdParam;
import vip.wqs.store.modular.manage.param.WineStorePageParam;

import java.util.List;

/**
 * 门店Service接口
 *
 * @author wqs
 * @date 2025/1/27
 */
public interface WineStoreService extends IService<WineStore> {

    /**
     * 获取门店分页
     */
    Page<WineStore> page(WineStorePageParam wineStorePageParam);

    /**
     * 获取门店列表
     */
    List<WineStore> list(WineStorePageParam wineStorePageParam);

    /**
     * 添加门店
     */
    void add(WineStoreAddParam wineStoreAddParam);

    /**
     * 编辑门店
     */
    void edit(WineStoreEditParam wineStoreEditParam);

    /**
     * 删除门店
     */
    void delete(WineStoreIdParam wineStoreIdParam);

    /**
     * 批量删除门店
     */
    void batchDelete(List<WineStoreIdParam> wineStoreIdParamList);

    /**
     * 获取门店详情
     */
    WineStore detail(WineStoreIdParam wineStoreIdParam);

    /**
     * 获取门店详情
     */
    WineStore queryEntity(String id);

    /**
     * 禁用门店
     */
    void disable(WineStoreIdParam wineStoreIdParam);

    /**
     * 启用门店
     */
    void enable(WineStoreIdParam wineStoreIdParam);

    /**
     * 门店导出
     */
    void export(WineStorePageParam wineStorePageParam);

    /**
     * 门店选择器
     */
    List<WineStore> selector(WineStorePageParam wineStorePageParam);

    /**
     * 获取管理员用户选择器
     */
    Object managerUserSelector();

    /**
     * 设置定价权限
     */
    void setPriceAuthority(WineStoreIdParam wineStoreIdParam, String priceAuthority);
} 