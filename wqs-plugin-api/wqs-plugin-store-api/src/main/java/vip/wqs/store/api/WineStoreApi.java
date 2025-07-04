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
package vip.wqs.store.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import vip.wqs.store.pojo.WineStorePojo;
import vip.wqs.store.pojo.WineStoreQueryPojo;
import vip.wqs.store.pojo.WineStoreSimplePojo;

import java.util.List;

/**
 * 门店API接口 - 遵循小程序接口开发规范
 *
 * @author AI Assistant
 * @date 2025/01/30
 **/
public interface WineStoreApi {

    /**
     * 分页查询门店列表 - 标准分页查询方法
     *
     * @param param 查询参数
     * @return 分页结果
     */
    Page<WineStoreSimplePojo> getWineStorePage(WineStoreQueryPojo param);

    /**
     * 获取门店详情 - 标准详情查询方法
     *
     * @param id 门店ID
     * @return 门店详情
     */
    WineStorePojo getWineStoreDetail(String id);

    /**
     * 根据门店编码获取门店信息
     *
     * @param storeCode 门店编码
     * @return 门店信息
     */
    WineStorePojo getStoreByCode(String storeCode);

    /**
     * 获取附近门店列表 - 基于位置的查询
     *
     * @param longitude 经度
     * @param latitude 纬度
     * @param radius 搜索半径（公里）
     * @param limit 返回数量限制
     * @return 附近门店列表（按距离排序）
     */
    List<WineStoreSimplePojo> getNearbyStores(Double longitude, Double latitude, Double radius, Integer limit);

    /**
     * 搜索门店 - 关键词搜索
     *
     * @param keyword 搜索关键词
     * @param limit 返回数量限制
     * @return 搜索结果列表
     */
    List<WineStoreSimplePojo> searchStores(String keyword, Integer limit);

    /**
     * 获取推荐门店列表 - 根据评分和热度推荐
     *
     * @param limit 返回数量限制
     * @return 推荐门店列表
     */
    List<WineStoreSimplePojo> getRecommendedStores(Integer limit);

    /**
     * 检查门店是否营业
     *
     * @param storeId 门店ID
     * @return 是否营业
     */
    Boolean checkStoreOpen(String storeId);

    /**
     * 获取门店下的设备列表
     *
     * @param storeId 门店ID
     * @return 设备列表
     */
    List<Object> getStoreDevices(String storeId);

    /**
     * 获取门店统计信息
     *
     * @param storeId 门店ID
     * @return 统计信息（设备数量、酒品数量等）
     */
    Object getStoreStatistics(String storeId);

    /**
     * 更新门店浏览次数
     *
     * @param storeId 门店ID
     * @return 更新结果
     */
    Boolean incrementViewCount(String storeId);
} 