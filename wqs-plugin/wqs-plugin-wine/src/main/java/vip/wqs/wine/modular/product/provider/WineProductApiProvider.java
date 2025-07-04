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
package vip.wqs.wine.modular.product.provider;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import vip.wqs.wine.api.WineProductApi;
import vip.wqs.wine.modular.product.entity.WineProduct;
import vip.wqs.wine.modular.product.param.WineProductPageParam;
import vip.wqs.wine.modular.product.service.WineProductService;
import vip.wqs.wine.pojo.WineProductPojo;
import vip.wqs.wine.pojo.WineProductQueryPojo;
import vip.wqs.wine.pojo.WineProductSimplePojo;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 酒品API实现类
 *
 * @author wqs
 * @date 2025/01/30
 */
@Service
public class WineProductApiProvider implements WineProductApi {

    @Resource
    private WineProductService wineProductService;

    /**
     * 分页查询酒品列表
     *
     * @param queryPojo 查询条件
     * @return 酒品分页列表
     */
    @Override
    public Page<WineProductSimplePojo> getWineProductPage(WineProductQueryPojo queryPojo) {
        // 转换查询参数
        WineProductPageParam pageParam = convertToPageParam(queryPojo);
        
        // 调用Service层查询
        Page<WineProduct> entityPage = wineProductService.page(pageParam);
        
        // 转换为SimplePojo
        Page<WineProductSimplePojo> resultPage = new Page<>();
        BeanUtil.copyProperties(entityPage, resultPage, "records");
        
        List<WineProductSimplePojo> simplePojoList = entityPage.getRecords().stream()
                .map(this::convertToSimplePojo)
                .collect(Collectors.toList());
        
        resultPage.setRecords(simplePojoList);
        return resultPage;
    }

    /**
     * 获取酒品详细信息
     *
     * @param productId 酒品ID
     * @return 酒品详细信息
     */
    @Override
    public WineProductPojo getWineProductDetail(String productId) {
        // 获取酒品基础信息
        WineProduct wineProduct = wineProductService.queryEntity(productId);
        if (ObjectUtil.isEmpty(wineProduct)) {
            return null;
        }
        
        // 转换为POJO
        WineProductPojo productPojo = convertToPojo(wineProduct);
        
        // TODO: 后续实现分类和价格API后，可以在这里获取相关信息
        // 目前使用实体中的基础价格信息
        productPojo.setCurrentPrice(wineProduct.getSuggestedPrice());
        
        return productPojo;
    }

    /**
     * 转换查询参数
     */
    private WineProductPageParam convertToPageParam(WineProductQueryPojo queryPojo) {
        WineProductPageParam pageParam = new WineProductPageParam();
        
        if (ObjectUtil.isNotEmpty(queryPojo)) {
            // 基础查询条件
            pageParam.setProductName(queryPojo.getProductName());
            pageParam.setCategoryId(queryPojo.getCategoryId());
            pageParam.setStatus(queryPojo.getStatus());
            
            // 构建搜索关键词（包含编码、名称、品牌等）
            if (StrUtil.isNotBlank(queryPojo.getProductCode()) || 
                StrUtil.isNotBlank(queryPojo.getProductName()) || 
                StrUtil.isNotBlank(queryPojo.getBrand())) {
                String searchKey = StrUtil.join(" ", queryPojo.getProductCode(), 
                                              queryPojo.getProductName(), 
                                              queryPojo.getBrand());
                pageParam.setSearchKey(searchKey.trim());
            }
            
            // 分页参数
            if (ObjectUtil.isNotEmpty(queryPojo.getPageNum())) {
                pageParam.setCurrent(queryPojo.getPageNum());
            }
            if (ObjectUtil.isNotEmpty(queryPojo.getPageSize())) {
                pageParam.setSize(queryPojo.getPageSize());
            }
            
            // 排序参数
            if (ObjectUtil.isNotEmpty(queryPojo.getSortField())) {
                pageParam.setSortField(queryPojo.getSortField());
                // 转换排序方向
                String sortOrder = "ASC".equalsIgnoreCase(queryPojo.getSortOrder()) ? "ASCEND" : "DESCEND";
                pageParam.setSortOrder(sortOrder);
            }
        }
        
        return pageParam;
    }

    /**
     * 转换为简化POJO
     */
    private WineProductSimplePojo convertToSimplePojo(WineProduct entity) {
        WineProductSimplePojo simplePojo = new WineProductSimplePojo();
        BeanUtil.copyProperties(entity, simplePojo);
        
        // TODO: 后续实现价格API后，可以在这里获取实时价格
        // 目前使用建议零售价
        simplePojo.setCurrentPrice(entity.getSuggestedPrice());
        
        return simplePojo;
    }

    /**
     * 转换为完整POJO
     */
    private WineProductPojo convertToPojo(WineProduct entity) {
        WineProductPojo pojo = new WineProductPojo();
        BeanUtil.copyProperties(entity, pojo);
        return pojo;
    }
} 