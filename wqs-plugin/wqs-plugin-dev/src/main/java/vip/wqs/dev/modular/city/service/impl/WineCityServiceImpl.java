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
package vip.wqs.dev.modular.city.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.wqs.common.exception.CommonException;
import vip.wqs.dev.modular.city.entity.WineCity;
import vip.wqs.dev.modular.city.mapper.WineCityMapper;
import vip.wqs.dev.modular.city.param.WineCityAddParam;
import vip.wqs.dev.modular.city.param.WineCityEditParam;
import vip.wqs.dev.modular.city.param.WineCityIdParam;
import vip.wqs.dev.modular.city.param.WineCityPageParam;
import vip.wqs.dev.modular.city.service.WineCityService;

import java.util.ArrayList;
import java.util.List;

/**
 * 城市管理Service接口实现类
 *
 * @author wqs
 * @date 2025/01/30
 */
@Service
public class WineCityServiceImpl extends ServiceImpl<WineCityMapper, WineCity> implements WineCityService {

    private static final Log log = Log.get();

    @Override
    public Page<WineCity> page(WineCityPageParam wineCityPageParam) {
        LambdaQueryWrapper<WineCity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        
        // 添加查询条件
        if (StrUtil.isNotBlank(wineCityPageParam.getCityCode())) {
            lambdaQueryWrapper.like(WineCity::getCityCode, wineCityPageParam.getCityCode());
        }
        if (StrUtil.isNotBlank(wineCityPageParam.getCityName())) {
            lambdaQueryWrapper.like(WineCity::getCityName, wineCityPageParam.getCityName());
        }
        if (StrUtil.isNotBlank(wineCityPageParam.getProvinceCode())) {
            lambdaQueryWrapper.eq(WineCity::getProvinceCode, wineCityPageParam.getProvinceCode());
        }
        if (StrUtil.isNotBlank(wineCityPageParam.getProvinceName())) {
            lambdaQueryWrapper.like(WineCity::getProvinceName, wineCityPageParam.getProvinceName());
        }
        if (StrUtil.isNotBlank(wineCityPageParam.getParentCode())) {
            lambdaQueryWrapper.eq(WineCity::getParentCode, wineCityPageParam.getParentCode());
        }
        if (ObjectUtil.isNotNull(wineCityPageParam.getLevel())) {
            lambdaQueryWrapper.eq(WineCity::getLevel, wineCityPageParam.getLevel());
        }
        if (StrUtil.isNotBlank(wineCityPageParam.getStatus())) {
            lambdaQueryWrapper.eq(WineCity::getStatus, wineCityPageParam.getStatus());
        }
        if (StrUtil.isNotBlank(wineCityPageParam.getIsHot())) {
            lambdaQueryWrapper.eq(WineCity::getIsHot, wineCityPageParam.getIsHot());
        }
        if (StrUtil.isNotBlank(wineCityPageParam.getSupportDelivery())) {
            lambdaQueryWrapper.eq(WineCity::getSupportDelivery, wineCityPageParam.getSupportDelivery());
        }
        
        // 排序
        lambdaQueryWrapper.orderByAsc(WineCity::getSortCode);
        lambdaQueryWrapper.orderByAsc(WineCity::getCityCode);
        
        // 分页查询
        Page<WineCity> page = new Page<>(wineCityPageParam.getCurrent(), wineCityPageParam.getSize());
        return this.page(page, lambdaQueryWrapper);
    }

    @Override
    public List<WineCity> list(WineCityPageParam wineCityPageParam) {
        LambdaQueryWrapper<WineCity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        
        // 添加查询条件（与page方法相同的条件）
        if (StrUtil.isNotBlank(wineCityPageParam.getCityCode())) {
            lambdaQueryWrapper.like(WineCity::getCityCode, wineCityPageParam.getCityCode());
        }
        if (StrUtil.isNotBlank(wineCityPageParam.getCityName())) {
            lambdaQueryWrapper.like(WineCity::getCityName, wineCityPageParam.getCityName());
        }
        if (StrUtil.isNotBlank(wineCityPageParam.getProvinceCode())) {
            lambdaQueryWrapper.eq(WineCity::getProvinceCode, wineCityPageParam.getProvinceCode());
        }
        if (StrUtil.isNotBlank(wineCityPageParam.getProvinceName())) {
            lambdaQueryWrapper.like(WineCity::getProvinceName, wineCityPageParam.getProvinceName());
        }
        if (StrUtil.isNotBlank(wineCityPageParam.getParentCode())) {
            lambdaQueryWrapper.eq(WineCity::getParentCode, wineCityPageParam.getParentCode());
        }
        if (ObjectUtil.isNotNull(wineCityPageParam.getLevel())) {
            lambdaQueryWrapper.eq(WineCity::getLevel, wineCityPageParam.getLevel());
        }
        if (StrUtil.isNotBlank(wineCityPageParam.getStatus())) {
            lambdaQueryWrapper.eq(WineCity::getStatus, wineCityPageParam.getStatus());
        }
        if (StrUtil.isNotBlank(wineCityPageParam.getIsHot())) {
            lambdaQueryWrapper.eq(WineCity::getIsHot, wineCityPageParam.getIsHot());
        }
        if (StrUtil.isNotBlank(wineCityPageParam.getSupportDelivery())) {
            lambdaQueryWrapper.eq(WineCity::getSupportDelivery, wineCityPageParam.getSupportDelivery());
        }
        
        // 排序
        lambdaQueryWrapper.orderByAsc(WineCity::getSortCode);
        lambdaQueryWrapper.orderByAsc(WineCity::getCityCode);
        
        return this.list(lambdaQueryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(WineCityAddParam wineCityAddParam) {
        // 检查城市编码是否已存在
        LambdaQueryWrapper<WineCity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(WineCity::getCityCode, wineCityAddParam.getCityCode());
        boolean exists = this.count(lambdaQueryWrapper) > 0;
        if (exists) {
            throw new CommonException("城市编码已存在：{}", wineCityAddParam.getCityCode());
        }

        WineCity wineCity = BeanUtil.copyProperties(wineCityAddParam, WineCity.class);
        
        // 设置默认值
        if (StrUtil.isBlank(wineCity.getStatus())) {
            wineCity.setStatus("ENABLE");
        }
        if (StrUtil.isBlank(wineCity.getIsHot())) {
            wineCity.setIsHot("NO");
        }
        if (StrUtil.isBlank(wineCity.getSupportDelivery())) {
            wineCity.setSupportDelivery("YES");
        }
        if (ObjectUtil.isNull(wineCity.getStoreCount())) {
            wineCity.setStoreCount(0);
        }
        if (ObjectUtil.isNull(wineCity.getDeviceCount())) {
            wineCity.setDeviceCount(0);
        }
        if (ObjectUtil.isNull(wineCity.getOrderCount())) {
            wineCity.setOrderCount(0);
        }

        this.save(wineCity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void edit(WineCityEditParam wineCityEditParam) {
        WineCity wineCity = this.queryEntity(wineCityEditParam.getId());
        
        // 检查城市编码是否已存在（排除自己）
        if (!wineCity.getCityCode().equals(wineCityEditParam.getCityCode())) {
            LambdaQueryWrapper<WineCity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(WineCity::getCityCode, wineCityEditParam.getCityCode());
            lambdaQueryWrapper.ne(WineCity::getId, wineCityEditParam.getId());
            boolean exists = this.count(lambdaQueryWrapper) > 0;
            if (exists) {
                throw new CommonException("城市编码已存在：{}", wineCityEditParam.getCityCode());
            }
        }

        BeanUtil.copyProperties(wineCityEditParam, wineCity);
        this.updateById(wineCity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(WineCityIdParam wineCityIdParam) {
        WineCity wineCity = this.queryEntity(wineCityIdParam.getId());
        
        // 检查是否有子级城市
        LambdaQueryWrapper<WineCity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(WineCity::getParentCode, wineCity.getCityCode());
        long childCount = this.count(lambdaQueryWrapper);
        if (childCount > 0) {
            throw new CommonException("该城市下存在子级城市，无法删除");
        }

        this.removeById(wineCityIdParam.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchDelete(List<WineCityIdParam> wineCityIdParamList) {
        List<String> wineCityIdList = wineCityIdParamList.stream().map(WineCityIdParam::getId).toList();
        if (ObjectUtil.isNotEmpty(wineCityIdList)) {
            // 检查每个城市是否有子级城市
            for (String cityId : wineCityIdList) {
                WineCity wineCity = this.queryEntity(cityId);
                LambdaQueryWrapper<WineCity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(WineCity::getParentCode, wineCity.getCityCode());
                long childCount = this.count(lambdaQueryWrapper);
                if (childCount > 0) {
                    throw new CommonException("城市【{}】下存在子级城市，无法删除", wineCity.getCityName());
                }
            }
            this.removeByIds(wineCityIdList);
        }
    }

    @Override
    public WineCity detail(WineCityIdParam wineCityIdParam) {
        return this.queryEntity(wineCityIdParam.getId());
    }

    @Override
    public WineCity queryEntity(String id) {
        WineCity wineCity = this.getById(id);
        if (ObjectUtil.isEmpty(wineCity)) {
            throw new CommonException("城市不存在，id值为：{}", id);
        }
        return wineCity;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void disable(WineCityIdParam wineCityIdParam) {
        WineCity wineCity = this.queryEntity(wineCityIdParam.getId());
        wineCity.setStatus("DISABLE");
        this.updateById(wineCity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void enable(WineCityIdParam wineCityIdParam) {
        WineCity wineCity = this.queryEntity(wineCityIdParam.getId());
        wineCity.setStatus("ENABLE");
        this.updateById(wineCity);
    }

    @Override
    public void export(WineCityPageParam wineCityPageParam) {
        // TODO: 实现导出功能
        log.info("城市导出功能待实现");
    }

    @Override
    public List<WineCity> selector(WineCityPageParam wineCityPageParam) {
        wineCityPageParam.setStatus("ENABLE");
        return this.list(wineCityPageParam);
    }

    @Override
    public List<WineCity> listByLevel(Integer level) {
        LambdaQueryWrapper<WineCity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(WineCity::getLevel, level);
        lambdaQueryWrapper.eq(WineCity::getStatus, "ENABLE");
        lambdaQueryWrapper.orderByAsc(WineCity::getSortCode);
        return this.list(lambdaQueryWrapper);
    }

    @Override
    public List<WineCity> listByParentCode(String parentCode) {
        LambdaQueryWrapper<WineCity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(WineCity::getParentCode, parentCode);
        lambdaQueryWrapper.eq(WineCity::getStatus, "ENABLE");
        lambdaQueryWrapper.orderByAsc(WineCity::getSortCode);
        return this.list(lambdaQueryWrapper);
    }

    @Override
    public List<WineCity> listHotCities() {
        LambdaQueryWrapper<WineCity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(WineCity::getIsHot, "YES");
        lambdaQueryWrapper.eq(WineCity::getStatus, "ENABLE");
        lambdaQueryWrapper.orderByAsc(WineCity::getSortCode);
        return this.list(lambdaQueryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void setHotCity(WineCityIdParam wineCityIdParam, String isHot) {
        WineCity wineCity = this.queryEntity(wineCityIdParam.getId());
        wineCity.setIsHot(isHot);
        this.updateById(wineCity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void setSupportDelivery(WineCityIdParam wineCityIdParam, String supportDelivery) {
        WineCity wineCity = this.queryEntity(wineCityIdParam.getId());
        wineCity.setSupportDelivery(supportDelivery);
        this.updateById(wineCity);
    }

    @Override
    public void updateCityStatistics(String cityCode) {
        // TODO: 实现统计更新逻辑
        log.info("更新城市统计数据: {}", cityCode);
    }

    @Override
    public List<WineCity> findByNames(String provinceName, String cityName, String districtName) {
        List<WineCity> result = new ArrayList<>();
        
        try {
            // 1. 查找省份信息
            if (StrUtil.isNotBlank(provinceName)) {
                LambdaQueryWrapper<WineCity> provinceWrapper = new LambdaQueryWrapper<>();
                provinceWrapper.eq(WineCity::getCityName, provinceName);
                provinceWrapper.eq(WineCity::getLevel, 1);
                provinceWrapper.eq(WineCity::getStatus, "ENABLE");
                WineCity province = this.getOne(provinceWrapper);
                if (province != null) {
                    result.add(province);
                    
                    // 2. 查找城市信息
                    if (StrUtil.isNotBlank(cityName)) {
                        LambdaQueryWrapper<WineCity> cityWrapper = new LambdaQueryWrapper<>();
                        cityWrapper.eq(WineCity::getCityName, cityName);
                        cityWrapper.eq(WineCity::getLevel, 2);
                        cityWrapper.eq(WineCity::getParentCode, province.getCityCode());
                        cityWrapper.eq(WineCity::getStatus, "ENABLE");
                        WineCity city = this.getOne(cityWrapper);
                        if (city != null) {
                            result.add(city);
                            
                            // 3. 查找区县信息
                            if (StrUtil.isNotBlank(districtName)) {
                                LambdaQueryWrapper<WineCity> districtWrapper = new LambdaQueryWrapper<>();
                                districtWrapper.eq(WineCity::getCityName, districtName);
                                districtWrapper.eq(WineCity::getLevel, 3);
                                districtWrapper.eq(WineCity::getParentCode, city.getCityCode());
                                districtWrapper.eq(WineCity::getStatus, "ENABLE");
                                WineCity district = this.getOne(districtWrapper);
                                if (district != null) {
                                    result.add(district);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("根据城市名称查找城市信息失败", e);
        }
        
        return result;
    }
} 