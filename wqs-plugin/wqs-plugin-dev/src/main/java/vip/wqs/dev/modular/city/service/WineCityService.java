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
package vip.wqs.dev.modular.city.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import vip.wqs.dev.modular.city.entity.WineCity;
import vip.wqs.dev.modular.city.param.WineCityAddParam;
import vip.wqs.dev.modular.city.param.WineCityEditParam;
import vip.wqs.dev.modular.city.param.WineCityIdParam;
import vip.wqs.dev.modular.city.param.WineCityPageParam;

import java.util.List;

/**
 * 城市管理Service接口
 *
 * @author wqs
 * @date 2025/01/30
 */
public interface WineCityService extends IService<WineCity> {

    /**
     * 获取城市分页
     */
    Page<WineCity> page(WineCityPageParam wineCityPageParam);

    /**
     * 获取城市列表
     */
    List<WineCity> list(WineCityPageParam wineCityPageParam);

    /**
     * 添加城市
     */
    void add(WineCityAddParam wineCityAddParam);

    /**
     * 编辑城市
     */
    void edit(WineCityEditParam wineCityEditParam);

    /**
     * 删除城市
     */
    void delete(WineCityIdParam wineCityIdParam);

    /**
     * 批量删除城市
     */
    void batchDelete(List<WineCityIdParam> wineCityIdParamList);

    /**
     * 获取城市详情
     */
    WineCity detail(WineCityIdParam wineCityIdParam);

    /**
     * 获取城市详情
     */
    WineCity queryEntity(String id);

    /**
     * 禁用城市
     */
    void disable(WineCityIdParam wineCityIdParam);

    /**
     * 启用城市
     */
    void enable(WineCityIdParam wineCityIdParam);

    /**
     * 城市导出
     */
    void export(WineCityPageParam wineCityPageParam);

    /**
     * 城市选择器
     */
    List<WineCity> selector(WineCityPageParam wineCityPageParam);

    /**
     * 根据层级获取城市列表
     */
    List<WineCity> listByLevel(Integer level);

    /**
     * 根据父级编码获取子级城市列表
     */
    List<WineCity> listByParentCode(String parentCode);

    /**
     * 获取热门城市列表
     */
    List<WineCity> listHotCities();

    /**
     * 设置热门城市
     */
    void setHotCity(WineCityIdParam wineCityIdParam, String isHot);

    /**
     * 设置配送支持
     */
    void setSupportDelivery(WineCityIdParam wineCityIdParam, String supportDelivery);

    /**
     * 更新城市统计数据
     */
    void updateCityStatistics(String cityCode);

    /**
     * 根据城市名称查找城市信息（用于反显）
     * @param provinceName 省份名称
     * @param cityName 城市名称
     * @param districtName 区县名称
     * @return 城市信息列表
     */
    List<WineCity> findByNames(String provinceName, String cityName, String districtName);
} 