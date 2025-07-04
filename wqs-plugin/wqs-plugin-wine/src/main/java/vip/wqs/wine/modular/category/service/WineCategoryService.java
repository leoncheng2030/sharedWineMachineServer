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
package vip.wqs.wine.modular.category.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import vip.wqs.wine.modular.category.entity.WineCategory;
import vip.wqs.wine.modular.category.param.*;

import java.util.List;

/**
 * 酒品分类Service接口
 *
 * @author wqs
 * @date 2025/01/27
 */
public interface WineCategoryService {

    /**
     * 获取分类分页
     *
     * @param wineCategoryPageParam 查询参数
     * @return 分页结果
     */
    Page<WineCategory> page(WineCategoryPageParam wineCategoryPageParam);

    /**
     * 获取分类列表
     *
     * @param wineCategoryPageParam 查询参数
     * @return 分类列表
     */
    List<WineCategory> list(WineCategoryPageParam wineCategoryPageParam);

    /**
     * 添加分类
     *
     * @param wineCategoryAddParam 添加参数
     */
    void add(WineCategoryAddParam wineCategoryAddParam);

    /**
     * 编辑分类
     *
     * @param wineCategoryEditParam 编辑参数
     */
    void edit(WineCategoryEditParam wineCategoryEditParam);

    /**
     * 删除分类
     *
     * @param wineCategoryIdParam 删除参数
     */
    void delete(WineCategoryIdParam wineCategoryIdParam);

    /**
     * 获取分类详情
     *
     * @param wineCategoryIdParam 查询参数
     * @return 分类详情
     */
    WineCategory detail(WineCategoryIdParam wineCategoryIdParam);

    /**
     * 启用分类
     *
     * @param wineCategoryIdParam 启用参数
     */
    void enable(WineCategoryIdParam wineCategoryIdParam);

    /**
     * 禁用分类
     *
     * @param wineCategoryIdParam 禁用参数
     */
    void disable(WineCategoryIdParam wineCategoryIdParam);

    /**
     * 导出分类
     *
     * @param wineCategoryExportParam 导出参数
     */
    void export(WineCategoryExportParam wineCategoryExportParam);

    /**
     * 分类选择器
     *
     * @param wineCategorySelectorParam 选择器参数
     * @return 分类列表
     */
    List<WineCategory> selector(WineCategorySelectorParam wineCategorySelectorParam);

    /**
     * 根据父级ID获取子分类列表
     *
     * @param parentId 父级ID
     * @return 子分类列表
     */
    List<WineCategory> getChildrenByParentId(String parentId);

    /**
     * 根据分类编码获取分类信息
     *
     * @param categoryCode 分类编码
     * @return 分类信息
     */
    WineCategory getByCategoryCode(String categoryCode);

    /**
     * 获取分类树形结构
     *
     * @param wineCategoryPageParam 查询参数
     * @return 树形分类列表
     */
    List<WineCategory> tree(WineCategoryPageParam wineCategoryPageParam);

    /**
     * 批量删除分类
     *
     * @param wineCategoryIdParamList 删除参数列表
     */
    void batchDelete(List<WineCategoryIdParam> wineCategoryIdParamList);

    /**
     * 获取父级分类选择器
     *
     * @return 父级分类列表
     */
    List<WineCategory> parentSelector();

    /**
     * 移动分类（调整父级）
     *
     * @param moveParam 移动参数
     */
    void move(WineCategoryMoveParam moveParam);

    /**
     * 调整分类排序
     *
     * @param sortParam 排序参数
     */
    void sort(WineCategorySortParam sortParam);
}