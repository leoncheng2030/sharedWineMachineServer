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
package vip.wqs.wine.modular.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.wqs.common.exception.CommonException;
import vip.wqs.common.page.CommonPageRequest;
import vip.wqs.wine.modular.product.entity.WineProduct;
import vip.wqs.wine.modular.product.mapper.WineProductMapper;
import vip.wqs.wine.modular.product.param.*;
import vip.wqs.wine.modular.product.service.WineProductService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 酒品产品Service接口实现类
 *
 * @author wqs
 * @date 2025/01/27
 */
@Service
public class WineProductServiceImpl extends ServiceImpl<WineProductMapper, WineProduct> implements WineProductService {

    @Override
    public Page<WineProduct> page(WineProductPageParam wineProductPageParam) {
        QueryWrapper<WineProduct> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Override
    public List<WineProduct> list(WineProductPageParam wineProductPageParam) {
        QueryWrapper<WineProduct> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        return this.list(queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(WineProductAddParam wineProductAddParam) {
        WineProduct wineProduct = BeanUtil.toBean(wineProductAddParam, WineProduct.class);
        this.save(wineProduct);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void edit(WineProductEditParam wineProductEditParam) {
        WineProduct wineProduct = BeanUtil.toBean(wineProductEditParam, WineProduct.class);
        this.updateById(wineProduct);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(WineProductIdParam wineProductIdParam) {
        this.removeById(wineProductIdParam.getId());
    }

    @Override
    public WineProduct detail(WineProductIdParam wineProductIdParam) {
        return this.getById(wineProductIdParam.getId());
    }

    @Override
    public WineProduct queryEntity(String id) {
        return this.getById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void enable(WineProductIdParam wineProductIdParam) {
        WineProduct wineProduct = this.getById(wineProductIdParam.getId());
        if (wineProduct != null) {
            wineProduct.setStatus("ENABLE");
            this.updateById(wineProduct);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void disable(WineProductIdParam wineProductIdParam) {
        WineProduct wineProduct = this.getById(wineProductIdParam.getId());
        if (wineProduct != null) {
            wineProduct.setStatus("DISABLE");
            this.updateById(wineProduct);
        }
    }

    @Override
    public void export(WineProductExportParam wineProductExportParam) {
        throw new CommonException("导出功能待实现");
    }

    @Override
    public List<WineProduct> selector(WineProductSelectorParam wineProductSelectorParam) {
        QueryWrapper<WineProduct> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", "ENABLE");
        queryWrapper.orderByAsc("sort_code");
        return this.list(queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchDelete(List<WineProductIdParam> wineProductIdParamList) {
        List<String> ids = wineProductIdParamList.stream()
                .map(WineProductIdParam::getId)
                .collect(Collectors.toList());
        this.removeByIds(ids);
    }

    @Override
    public Object categorySelector() {
        throw new CommonException("分类选择器功能待实现");
    }

    @Override
    public Object uploadImage(Object uploadParam) {
        throw new CommonException("图片上传功能待实现");
    }

    @Override
    public Object getStock(WineProductIdParam wineProductIdParam) {
        throw new CommonException("库存查询功能待实现");
    }

    @Override
    public void updateStock(WineProductStockUpdateParam stockUpdateParam) {
        // TODO: 实现库存更新逻辑
        // 1. 验证酒品是否存在
        // 2. 根据变更类型处理库存
        // 3. 记录库存变更日志
        throw new CommonException("库存更新功能待实现");
    }
} 