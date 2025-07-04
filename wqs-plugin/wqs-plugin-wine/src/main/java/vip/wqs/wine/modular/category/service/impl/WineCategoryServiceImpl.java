/*
 * Copyright [2022] [https://www.wqs.vip]
 *
 * WQS采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改WQS源码头部的版权声明。
 * 3.本项目代码可免费商业使用，商业使用请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://www.wqs.vip
 * 5.不可二次分发开源参与同类竞品，如需要参与同类竞品请联系官方购买商业授权合同。
 * 6.若您的项目无法满足以上几点，需要更多功能代码，获取WQS商业授权许可，请在官网购买授权，地址为 https://www.wqs.vip
 */
package vip.wqs.wine.modular.category.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.wqs.common.exception.CommonException;
import vip.wqs.common.page.CommonPageRequest;
import vip.wqs.wine.modular.category.entity.WineCategory;
import vip.wqs.wine.modular.category.mapper.WineCategoryMapper;
import vip.wqs.wine.modular.category.param.*;
import vip.wqs.wine.modular.category.service.WineCategoryService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 酒品分类Service接口实现类
 *
 * @author wqs
 * @date 2025/01/27
 */
@Service
public class WineCategoryServiceImpl extends ServiceImpl<WineCategoryMapper, WineCategory> implements WineCategoryService {

    @Override
    public Page<WineCategory> page(WineCategoryPageParam wineCategoryPageParam) {
        QueryWrapper<WineCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort_code");
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Override
    public List<WineCategory> list(WineCategoryPageParam wineCategoryPageParam) {
        QueryWrapper<WineCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort_code");
        return this.list(queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(WineCategoryAddParam wineCategoryAddParam) {
        WineCategory wineCategory = BeanUtil.toBean(wineCategoryAddParam, WineCategory.class);
        this.save(wineCategory);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void edit(WineCategoryEditParam wineCategoryEditParam) {
        WineCategory wineCategory = BeanUtil.toBean(wineCategoryEditParam, WineCategory.class);
        this.updateById(wineCategory);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(WineCategoryIdParam wineCategoryIdParam) {
        this.removeById(wineCategoryIdParam.getId());
    }

    @Override
    public WineCategory detail(WineCategoryIdParam wineCategoryIdParam) {
        return this.getById(wineCategoryIdParam.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void enable(WineCategoryIdParam wineCategoryIdParam) {
        WineCategory wineCategory = this.getById(wineCategoryIdParam.getId());
        if (wineCategory != null) {
            wineCategory.setStatus("ENABLE");
            this.updateById(wineCategory);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void disable(WineCategoryIdParam wineCategoryIdParam) {
        WineCategory wineCategory = this.getById(wineCategoryIdParam.getId());
        if (wineCategory != null) {
            wineCategory.setStatus("DISABLE");
            this.updateById(wineCategory);
        }
    }

    @Override
    public void export(WineCategoryExportParam wineCategoryExportParam) {
        throw new CommonException("导出功能待实现");
    }

    @Override
    public List<WineCategory> selector(WineCategorySelectorParam wineCategorySelectorParam) {
        QueryWrapper<WineCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", "ENABLE");
        queryWrapper.orderByAsc("sort_code");
        return this.list(queryWrapper);
    }

    @Override
    public List<WineCategory> getChildrenByParentId(String parentId) {
        return this.list(new QueryWrapper<WineCategory>().eq("parent_id", parentId).orderByAsc("sort_code"));
    }

    @Override
    public WineCategory getByCategoryCode(String categoryCode) {
        return this.getOne(new QueryWrapper<WineCategory>().eq("category_code", categoryCode));
    }

    @Override
    public List<WineCategory> tree(WineCategoryPageParam wineCategoryPageParam) {
        List<WineCategory> allCategories = this.list(new QueryWrapper<WineCategory>().orderByAsc("sort_code"));
        return buildTree(allCategories, "0");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchDelete(List<WineCategoryIdParam> wineCategoryIdParamList) {
        List<String> ids = wineCategoryIdParamList.stream()
                .map(WineCategoryIdParam::getId)
                .collect(Collectors.toList());
        this.removeByIds(ids);
    }

    @Override
    public List<WineCategory> parentSelector() {
        List<WineCategory> allCategories = this.list(new QueryWrapper<WineCategory>()
                .eq("status", "ENABLE")
                .orderByAsc("sort_code"));
        return buildTree(allCategories, "0");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void move(WineCategoryMoveParam moveParam) {
        // TODO: 实现分类移动逻辑
        // 1. 验证分类是否存在
        // 2. 验证新父级分类是否存在
        // 3. 防止循环引用
        // 4. 更新分类的父级ID和层级
        throw new CommonException("分类移动功能待实现");
    }

    @Override
    public void sort(WineCategorySortParam sortParam) {
        // TODO: 实现分类排序逻辑
        // 1. 验证排序列表中的分类是否都存在
        // 2. 批量更新排序码
        throw new CommonException("分类排序功能待实现");
    }

    /**
     * 构建树形结构
     */
    private List<WineCategory> buildTree(List<WineCategory> allCategories, String parentId) {
        return allCategories.stream()
                .filter(category -> parentId.equals(category.getParentId()))
                .peek(category -> {
                    List<WineCategory> children = buildTree(allCategories, category.getId());
                    category.setChildren(children);
                })
                .collect(Collectors.toList());
    }

    public WineCategory queryEntity(String id) {
        WineCategory wineCategory = this.getById(id);
        if (ObjectUtil.isEmpty(wineCategory)) {
            throw new CommonException("酒品分类不存在，id值为：{}", id);
        }
        return wineCategory;
    }
} 