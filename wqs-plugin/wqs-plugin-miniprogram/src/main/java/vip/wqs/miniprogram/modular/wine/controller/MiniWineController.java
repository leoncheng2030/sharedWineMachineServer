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
package vip.wqs.miniprogram.modular.wine.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vip.wqs.common.annotation.CommonLog;
import vip.wqs.common.pojo.CommonResult;
import vip.wqs.miniprogram.modular.wine.param.MiniWinePageParam;
import vip.wqs.wine.api.WineProductApi;
import vip.wqs.wine.pojo.WineProductPojo;
import vip.wqs.wine.pojo.WineProductQueryPojo;
import vip.wqs.wine.pojo.WineProductSimplePojo;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 小程序酒品控制器
 * 
 * @author wqs
 * @date 2025/01/30
 */
@Tag(name = "小程序酒品控制器")
@ApiSupport(author = "WQS_TEAM", order = 7)
@RestController
@Validated
@Slf4j
public class MiniWineController {

    @Resource
    private WineProductApi wineProductApi;

    /**
     * 获取酒品分页列表
     * 注意：酒品浏览无需登录，公开访问
     *
     * @author wqs
     * @date 2025/01/30
     */
    @ApiOperationSupport(order = 1)
    @Operation(summary = "获取酒品分页列表")
    @CommonLog("小程序获取酒品列表")
    @GetMapping("/miniprogram/wine/page")
    public CommonResult<Page<WineProductSimplePojo>> getWinePage(@Valid MiniWinePageParam param) {
        try {
            log.info("小程序获取酒品列表，参数：{}", param);
            
            // 1. 转换查询参数
            WineProductQueryPojo queryPojo = convertToQueryPojo(param);
            
            // 2. 调用酒品API获取分页数据
            Page<WineProductSimplePojo> result = wineProductApi.getWineProductPage(queryPojo);
            
            if (result == null) {
                return CommonResult.error("获取酒品列表失败");
            }
            
            log.info("小程序获取酒品列表成功，返回{}条数据", result.getRecords().size());
            return CommonResult.data(result);
            
        } catch (Exception e) {
            log.error("获取酒品列表异常", e);
            return CommonResult.error("获取酒品列表失败：" + e.getMessage());
        }
    }

    /**
     * 获取酒品详情
     * 注意：酒品详情浏览无需登录，公开访问
     *
     * @author wqs
     * @date 2025/01/30
     */
    @ApiOperationSupport(order = 2)
    @Operation(summary = "获取酒品详情")
    @CommonLog("小程序获取酒品详情")
    @GetMapping("/miniprogram/wine/detail")
    public CommonResult<WineProductPojo> getWineDetail(@RequestParam String id) {
        try {
            log.info("小程序获取酒品详情，酒品ID：{}", id);
            
            // 1. 验证参数
            if (StrUtil.isBlank(id)) {
                return CommonResult.error("酒品ID不能为空");
            }
            
            // 2. 调用酒品API获取详情
            WineProductPojo result = wineProductApi.getWineProductDetail(id);
            
            if (result == null) {
                return CommonResult.error("酒品不存在");
            }
            
            log.info("小程序获取酒品详情成功，酒品：{}", result.getProductName());
            return CommonResult.data(result);
            
        } catch (Exception e) {
            log.error("获取酒品详情异常，酒品ID：{}", id, e);
            return CommonResult.error("获取酒品详情失败：" + e.getMessage());
        }
    }

    /**
     * 获取推荐酒品
     * 注意：推荐酒品无需登录，公开访问
     *
     * @author wqs
     * @date 2025/01/30
     */
    @ApiOperationSupport(order = 3)
    @Operation(summary = "获取推荐酒品")
    @CommonLog("小程序获取推荐酒品")
    @GetMapping("/miniprogram/wine/recommended")
    public CommonResult<List<WineProductSimplePojo>> getRecommendedWines(@RequestParam(defaultValue = "10") Integer limit) {
        try {
            log.info("小程序获取推荐酒品，数量限制：{}", limit);
            
            // 1. 构建查询参数（获取热门酒品）
            WineProductQueryPojo queryPojo = new WineProductQueryPojo();
            queryPojo.setPageNum(1);
            queryPojo.setPageSize(limit);
            queryPojo.setSortField("salesCount"); // 按销量排序
            queryPojo.setSortOrder("DESC");
            queryPojo.setStatus("ENABLE"); // 只获取启用的酒品
            
            // 2. 调用酒品API获取推荐酒品
            Page<WineProductSimplePojo> apiResult = wineProductApi.getWineProductPage(queryPojo);
            
            if (apiResult == null || apiResult.getRecords().isEmpty()) {
                return CommonResult.data(List.of());
            }
            
            // 3. 返回记录列表
            List<WineProductSimplePojo> result = apiResult.getRecords();
            
            log.info("小程序获取推荐酒品成功，返回{}个商品", result.size());
            return CommonResult.data(result);
            
        } catch (Exception e) {
            log.error("获取推荐酒品异常", e);
            return CommonResult.error("获取推荐酒品失败：" + e.getMessage());
        }
    }

    /**
     * 根据分类获取酒品
     * 注意：分类酒品浏览无需登录，公开访问
     *
     * @author wqs
     * @date 2025/01/30
     */
    @ApiOperationSupport(order = 4)
    @Operation(summary = "根据分类获取酒品")
    @CommonLog("小程序根据分类获取酒品")
    @GetMapping("/miniprogram/wine/category/{categoryId}")
    public CommonResult<Page<WineProductSimplePojo>> getWinesByCategory(
            @PathVariable String categoryId,
            @RequestParam(defaultValue = "1") Long pageNum,
            @RequestParam(defaultValue = "10") Long pageSize) {
        try {
            log.info("小程序根据分类获取酒品，分类ID：{}，页码：{}，页大小：{}", categoryId, pageNum, pageSize);
            
            // 1. 验证参数
            if (StrUtil.isBlank(categoryId)) {
                return CommonResult.error("分类ID不能为空");
            }
            
            // 2. 构建查询参数
            WineProductQueryPojo queryPojo = new WineProductQueryPojo();
            queryPojo.setCategoryId(categoryId);
            queryPojo.setPageNum(pageNum.intValue());
            queryPojo.setPageSize(pageSize.intValue());
            queryPojo.setSortField("createTime");
            queryPojo.setSortOrder("DESC");
            queryPojo.setStatus("ENABLE"); // 只获取启用的酒品
            
            // 3. 调用酒品API获取分页数据
            Page<WineProductSimplePojo> result = wineProductApi.getWineProductPage(queryPojo);
            
            if (result == null) {
                return CommonResult.error("获取分类酒品失败");
            }
            
            log.info("小程序根据分类获取酒品成功，返回{}条数据", result.getRecords().size());
            return CommonResult.data(result);
            
        } catch (Exception e) {
            log.error("根据分类获取酒品异常，分类ID：{}", categoryId, e);
            return CommonResult.error("获取分类酒品失败：" + e.getMessage());
        }
    }

    /**
     * 搜索酒品
     * 注意：酒品搜索无需登录，公开访问
     *
     * @author wqs
     * @date 2025/01/30
     */
    @ApiOperationSupport(order = 5)
    @Operation(summary = "搜索酒品")
    @CommonLog("小程序搜索酒品")
    @GetMapping("/miniprogram/wine/search")
    public CommonResult<Page<WineProductSimplePojo>> searchWines(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") Long pageNum,
            @RequestParam(defaultValue = "10") Long pageSize) {
        try {
            log.info("小程序搜索酒品，关键词：{}，页码：{}，页大小：{}", keyword, pageNum, pageSize);
            
            // 1. 验证参数
            if (StrUtil.isBlank(keyword)) {
                return CommonResult.error("搜索关键词不能为空");
            }
            
            // 2. 构建查询参数
            WineProductQueryPojo queryPojo = new WineProductQueryPojo();
            queryPojo.setProductName(keyword); // 按商品名称搜索
            queryPojo.setPageNum(pageNum.intValue());
            queryPojo.setPageSize(pageSize.intValue());
            queryPojo.setSortField("createTime");
            queryPojo.setSortOrder("DESC");
            queryPojo.setStatus("ENABLE"); // 只获取启用的酒品
            
            // 3. 调用酒品API搜索
            Page<WineProductSimplePojo> result = wineProductApi.getWineProductPage(queryPojo);
            
            if (result == null) {
                return CommonResult.error("搜索酒品失败");
            }
            
            log.info("小程序搜索酒品成功，关键词：{}，返回{}条数据", keyword, result.getRecords().size());
            return CommonResult.data(result);
            
        } catch (Exception e) {
            log.error("搜索酒品异常，关键词：{}", keyword, e);
            return CommonResult.error("搜索酒品失败：" + e.getMessage());
        }
    }

    /**
     * 获取热门酒品（首页使用）
     * 注意：热门酒品无需登录，公开访问
     *
     * @author wqs
     * @date 2025/01/30
     */
    @ApiOperationSupport(order = 6)
    @Operation(summary = "获取热门酒品")
    @CommonLog("小程序获取热门酒品")
    @GetMapping("/miniprogram/wine/hot")
    public CommonResult<List<WineProductSimplePojo>> getHotWines(@RequestParam(defaultValue = "20") Integer limit) {
        try {
            log.info("小程序获取热门酒品，数量限制：{}", limit);
            
            // 1. 构建查询参数（按销量排序获取热门酒品）
            WineProductQueryPojo queryPojo = new WineProductQueryPojo();
            queryPojo.setPageNum(1);
            queryPojo.setPageSize(limit);
            queryPojo.setSortField("salesCount"); // 按销量排序
            queryPojo.setSortOrder("DESC");
            queryPojo.setStatus("ENABLE"); // 只获取启用的酒品
            
            // 2. 调用酒品API获取热门酒品
            Page<WineProductSimplePojo> apiResult = wineProductApi.getWineProductPage(queryPojo);
            
            if (apiResult == null || apiResult.getRecords().isEmpty()) {
                return CommonResult.data(List.of());
            }
            
            // 3. 返回记录列表
            List<WineProductSimplePojo> result = apiResult.getRecords();
            
            log.info("小程序获取热门酒品成功，返回{}个商品", result.size());
            return CommonResult.data(result);
            
        } catch (Exception e) {
            log.error("获取热门酒品异常", e);
            return CommonResult.error("获取热门酒品失败：" + e.getMessage());
        }
    }

    /**
     * 获取新品酒品
     * 注意：新品酒品无需登录，公开访问
     *
     * @author wqs
     * @date 2025/01/30
     */
    @ApiOperationSupport(order = 7)
    @Operation(summary = "获取新品酒品")
    @CommonLog("小程序获取新品酒品")
    @GetMapping("/miniprogram/wine/new")
    public CommonResult<List<WineProductSimplePojo>> getNewWines(@RequestParam(defaultValue = "10") Integer limit) {
        try {
            log.info("小程序获取新品酒品，数量限制：{}", limit);
            
            // 1. 构建查询参数（按创建时间排序获取新品）
            WineProductQueryPojo queryPojo = new WineProductQueryPojo();
            queryPojo.setPageNum(1);
            queryPojo.setPageSize(limit);
            queryPojo.setSortField("createTime"); // 按创建时间排序
            queryPojo.setSortOrder("DESC");
            queryPojo.setStatus("ENABLE"); // 只获取启用的酒品
            
            // 2. 调用酒品API获取新品酒品
            Page<WineProductSimplePojo> apiResult = wineProductApi.getWineProductPage(queryPojo);
            
            if (apiResult == null || apiResult.getRecords().isEmpty()) {
                return CommonResult.data(List.of());
            }
            
            // 3. 返回记录列表
            List<WineProductSimplePojo> result = apiResult.getRecords();
            
            log.info("小程序获取新品酒品成功，返回{}个商品", result.size());
            return CommonResult.data(result);
            
        } catch (Exception e) {
            log.error("获取新品酒品异常", e);
            return CommonResult.error("获取新品酒品失败：" + e.getMessage());
        }
    }

    /**
     * 获取特价酒品
     * 注意：特价酒品无需登录，公开访问
     *
     * @author wqs
     * @date 2025/01/30
     */
    @ApiOperationSupport(order = 8)
    @Operation(summary = "获取特价酒品")
    @CommonLog("小程序获取特价酒品")
    @GetMapping("/miniprogram/wine/discount")
    public CommonResult<List<WineProductSimplePojo>> getDiscountWines(@RequestParam(defaultValue = "10") Integer limit) {
        try {
            log.info("小程序获取特价酒品，数量限制：{}", limit);
            
            // 1. 构建查询参数（获取有折扣的酒品）
            WineProductQueryPojo queryPojo = new WineProductQueryPojo();
            queryPojo.setPageNum(1);
            queryPojo.setPageSize(limit);
            queryPojo.setSortField("createTime"); // 按创建时间排序
            queryPojo.setSortOrder("DESC");
            queryPojo.setStatus("ENABLE"); // 只获取启用的酒品
            // TODO: 后续可以添加价格折扣条件过滤
            
            // 2. 调用酒品API获取特价酒品
            Page<WineProductSimplePojo> apiResult = wineProductApi.getWineProductPage(queryPojo);
            
            if (apiResult == null || apiResult.getRecords().isEmpty()) {
                return CommonResult.data(List.of());
            }
            
            // 3. 过滤有折扣的商品（当前价格 < 建议零售价）
            List<WineProductSimplePojo> result = apiResult.getRecords().stream()
                    .filter(item -> item.getCurrentPrice() != null && item.getSuggestedPrice() != null 
                            && item.getCurrentPrice().compareTo(item.getSuggestedPrice()) < 0)
                    .collect(Collectors.toList());
            
            // 4. 如果过滤后数量不足，返回所有商品
            if (result.isEmpty()) {
                result = apiResult.getRecords();
            }
            
            log.info("小程序获取特价酒品成功，返回{}个商品", result.size());
            return CommonResult.data(result);
            
        } catch (Exception e) {
            log.error("获取特价酒品异常", e);
            return CommonResult.error("获取特价酒品失败：" + e.getMessage());
        }
    }

    /**
     * 转换查询参数
     */
    private WineProductQueryPojo convertToQueryPojo(MiniWinePageParam param) {
        WineProductQueryPojo queryPojo = new WineProductQueryPojo();
        
        // 基础分页参数 - 类型转换 Long -> Integer
        queryPojo.setPageNum(param.getPageNum().intValue());
        queryPojo.setPageSize(param.getPageSize().intValue());
        
        // 查询条件
        queryPojo.setCategoryId(param.getCategoryId());
        queryPojo.setProductName(param.getKeyword()); // 关键词搜索商品名称
        
        // 排序参数
        queryPojo.setSortField(param.getSortField());
        queryPojo.setSortOrder(param.getSortOrder());
        
        // 默认只查询启用的酒品
        queryPojo.setStatus("ENABLE");
        
        return queryPojo;
    }
} 