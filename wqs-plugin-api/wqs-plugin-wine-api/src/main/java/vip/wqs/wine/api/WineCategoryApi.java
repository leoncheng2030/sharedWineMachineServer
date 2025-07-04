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
package vip.wqs.wine.api;

import vip.wqs.wine.pojo.WineCategoryPojo;

import java.util.List;

/**
 * 酒品分类Api
 *
 * @author WQS_TEAM
 * @date 2025/1/27 酒品管理模块开发
 **/
public interface WineCategoryApi {

    /**
     * 获取酒品分类树
     *
     * @param parentId 父分类ID，为空则获取根分类
     * @return 分类树列表
     * @author WQS_TEAM
     * @date 2025/1/27
     */
    List<WineCategoryPojo> getCategoryTree(String parentId);

    /**
     * 获取酒品分类选择器
     *
     * @param searchKey 搜索关键字
     * @return 分类列表
     * @author WQS_TEAM
     * @date 2025/1/27
     */
    List<WineCategoryPojo> wineCategorySelector(String searchKey);

    /**
     * 根据分类ID获取分类信息
     *
     * @param categoryId 分类ID
     * @return 分类信息
     * @author WQS_TEAM
     * @date 2025/1/27
     */
    WineCategoryPojo getWineCategoryById(String categoryId);

    /**
     * 获取完整的分类树（包含所有层级）
     *
     * @return 完整分类树
     * @author WQS_TEAM
     * @date 2025/1/30
     */
    List<WineCategoryPojo> getFullCategoryTree();

    /**
     * 根据分类编码获取分类信息
     *
     * @param categoryCode 分类编码
     * @return 分类信息
     * @author WQS_TEAM
     * @date 2025/1/30
     */
    WineCategoryPojo getWineCategoryByCode(String categoryCode);

    /**
     * 获取分类路径
     *
     * @param categoryId 分类ID
     * @return 分类路径列表（从根分类到当前分类）
     * @author WQS_TEAM
     * @date 2025/1/30
     */
    List<WineCategoryPojo> getCategoryPath(String categoryId);

    /**
     * 检查分类是否存在子分类
     *
     * @param categoryId 分类ID
     * @return 是否存在子分类
     * @author WQS_TEAM
     * @date 2025/1/27
     */
    Boolean hasChildCategory(String categoryId);

    /**
     * 获取热门分类列表
     *
     * @param limit 返回数量限制
     * @return 热门分类列表
     * @author WQS_TEAM
     * @date 2025/1/30
     */
    List<WineCategoryPojo> getPopularCategories(Integer limit);
} 