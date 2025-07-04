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
package vip.wqs.store.modular.manage.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fhs.core.trans.anno.TransMethodResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.wqs.common.enums.CommonSortOrderEnum;
import vip.wqs.common.exception.CommonException;
import vip.wqs.common.page.CommonPageRequest;
import vip.wqs.store.modular.manage.entity.WineStore;
import vip.wqs.store.modular.manage.mapper.WineStoreMapper;
import vip.wqs.store.modular.manage.param.WineStoreAddParam;
import vip.wqs.store.modular.manage.param.WineStoreEditParam;
import vip.wqs.store.modular.manage.param.WineStoreIdParam;
import vip.wqs.store.modular.manage.param.WineStorePageParam;
import vip.wqs.store.modular.manage.service.WineStoreService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 门店Service接口实现类
 *
 * @author wqs
 * @date 2025/01/27
 */
@Service
public class WineStoreServiceImpl extends ServiceImpl<WineStoreMapper, WineStore> implements WineStoreService {

    @Override
    @TransMethodResult
    public Page<WineStore> page(WineStorePageParam wineStorePageParam) {
        QueryWrapper<WineStore> queryWrapper = new QueryWrapper<WineStore>().checkSqlInjection();
        
        // 关键词搜索
        if (StrUtil.isNotBlank(wineStorePageParam.getSearchKey())) {
            queryWrapper.and(wrapper -> wrapper.lambda()
                .like(WineStore::getStoreCode, wineStorePageParam.getSearchKey())
                .or().like(WineStore::getStoreName, wineStorePageParam.getSearchKey())
                .or().like(WineStore::getDetailAddress, wineStorePageParam.getSearchKey())
            );
        }
        
        // 门店编码
        if (StrUtil.isNotBlank(wineStorePageParam.getStoreCode())) {
            queryWrapper.lambda().like(WineStore::getStoreCode, wineStorePageParam.getStoreCode());
        }
        
        // 门店名称
        if (StrUtil.isNotBlank(wineStorePageParam.getStoreName())) {
            queryWrapper.lambda().like(WineStore::getStoreName, wineStorePageParam.getStoreName());
        }

        // 省份
        if (StrUtil.isNotBlank(wineStorePageParam.getProvince())) {
            queryWrapper.lambda().eq(WineStore::getProvince, wineStorePageParam.getProvince());
        }
        
        // 城市
        if (StrUtil.isNotBlank(wineStorePageParam.getCity())) {
            queryWrapper.lambda().eq(WineStore::getCity, wineStorePageParam.getCity());
        }
        
        // 区县
        if (StrUtil.isNotBlank(wineStorePageParam.getDistrict())) {
            queryWrapper.lambda().eq(WineStore::getDistrict, wineStorePageParam.getDistrict());
        }
        
        // 门店状态
        if (StrUtil.isNotBlank(wineStorePageParam.getStatus())) {
            queryWrapper.lambda().eq(WineStore::getStatus, wineStorePageParam.getStatus());
        }
        
        // 定价权限
        if (StrUtil.isNotBlank(wineStorePageParam.getPriceAuthority())) {
            queryWrapper.lambda().eq(WineStore::getPriceAuthority, wineStorePageParam.getPriceAuthority());
        }
        
        // 门店管理员ID
        if (StrUtil.isNotBlank(wineStorePageParam.getStoreManagerId())) {
            queryWrapper.lambda().eq(WineStore::getStoreManagerId, wineStorePageParam.getStoreManagerId());
        }
        
        // 创建时间范围  
        if (ObjectUtil.isNotEmpty(wineStorePageParam.getStartCreateTime()) && ObjectUtil.isNotEmpty(wineStorePageParam.getEndCreateTime())) {
            queryWrapper.ge("create_time", wineStorePageParam.getStartCreateTime());
            queryWrapper.le("create_time", wineStorePageParam.getEndCreateTime());
        }
        
        // 排序
        if (ObjectUtil.isAllNotEmpty(wineStorePageParam.getSortField(), wineStorePageParam.getSortOrder())) {
            CommonSortOrderEnum.validate(wineStorePageParam.getSortOrder());
            queryWrapper.orderBy(true, wineStorePageParam.getSortOrder().equals(CommonSortOrderEnum.ASC.getValue()),
                    StrUtil.toUnderlineCase(wineStorePageParam.getSortField()));
        } else {
            queryWrapper.lambda().orderByAsc(WineStore::getSortCode);
        }
        
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Override
    @TransMethodResult
    public List<WineStore> list(WineStorePageParam wineStorePageParam) {
        QueryWrapper<WineStore> queryWrapper = new QueryWrapper<>();
        
        // 门店状态
        if (StrUtil.isNotBlank(wineStorePageParam.getStatus())) {
            queryWrapper.eq("status", wineStorePageParam.getStatus());
        }
        
        // 定价权限
        if (StrUtil.isNotBlank(wineStorePageParam.getPriceAuthority())) {
            queryWrapper.eq("price_authority", wineStorePageParam.getPriceAuthority());
        }
        
        // 门店管理员ID
        if (StrUtil.isNotBlank(wineStorePageParam.getStoreManagerId())) {
            queryWrapper.eq("store_manager_id", wineStorePageParam.getStoreManagerId());
        }
        
        queryWrapper.orderByAsc("sort_code").orderByDesc("create_time");
        return this.list(queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(WineStoreAddParam wineStoreAddParam) {
        // 检查门店编码是否重复
        QueryWrapper<WineStore> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("store_code", wineStoreAddParam.getStoreCode());
        if (this.count(queryWrapper) > 0) {
            throw new CommonException("门店编码已存在");
        }
        
        WineStore wineStore = BeanUtil.toBean(wineStoreAddParam, WineStore.class);
        // 设置默认状态
        if (StrUtil.isBlank(wineStore.getStatus())) {
            wineStore.setStatus("ENABLE");
        }
        // 设置默认定价权限
        if (StrUtil.isBlank(wineStore.getPriceAuthority())) {
            wineStore.setPriceAuthority("PLATFORM");
        }
        // 设置默认排序码
        if (ObjectUtil.isNull(wineStore.getSortCode())) {
            wineStore.setSortCode(99);
        }
        this.save(wineStore);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void edit(WineStoreEditParam wineStoreEditParam) {
        // 检查门店编码是否重复（排除自己）
        QueryWrapper<WineStore> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("store_code", wineStoreEditParam.getStoreCode());
        queryWrapper.ne("id", wineStoreEditParam.getId());
        if (this.count(queryWrapper) > 0) {
            throw new CommonException("门店编码已存在");
        }
        
        WineStore wineStore = BeanUtil.toBean(wineStoreEditParam, WineStore.class);
        this.updateById(wineStore);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(WineStoreIdParam wineStoreIdParam) {
        this.removeById(wineStoreIdParam.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchDelete(List<WineStoreIdParam> wineStoreIdParamList) {
        List<String> ids = wineStoreIdParamList.stream()
                .map(WineStoreIdParam::getId)
                .collect(Collectors.toList());
        this.removeByIds(ids);
    }

    @Override
    public WineStore detail(WineStoreIdParam wineStoreIdParam) {
        return this.getById(wineStoreIdParam.getId());
    }

    @Override
    public WineStore queryEntity(String id) {
        return this.getById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void disable(WineStoreIdParam wineStoreIdParam) {
        WineStore wineStore = this.getById(wineStoreIdParam.getId());
        if (wineStore != null) {
            wineStore.setStatus("DISABLE");
            this.updateById(wineStore);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void enable(WineStoreIdParam wineStoreIdParam) {
        WineStore wineStore = this.getById(wineStoreIdParam.getId());
        if (wineStore != null) {
            wineStore.setStatus("ENABLE");
            this.updateById(wineStore);
        }
    }

    @Override
    public void export(WineStorePageParam wineStorePageParam) {
        // TODO: 实现门店导出功能
    }

    @Override
    public List<WineStore> selector(WineStorePageParam wineStorePageParam) {
        QueryWrapper<WineStore> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", "ENABLE");
        queryWrapper.orderByAsc("sort_code").orderByDesc("create_time");
        return this.list(queryWrapper);
    }

    @Override
    public Object managerUserSelector() {
        // TODO: 实现管理员用户选择器
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void setPriceAuthority(WineStoreIdParam wineStoreIdParam, String priceAuthority) {
        WineStore wineStore = this.getById(wineStoreIdParam.getId());
        if (wineStore != null) {
            wineStore.setPriceAuthority(priceAuthority);
            this.updateById(wineStore);
        }
    }
} 