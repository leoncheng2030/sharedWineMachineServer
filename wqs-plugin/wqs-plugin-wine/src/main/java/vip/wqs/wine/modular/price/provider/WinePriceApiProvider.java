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
package vip.wqs.wine.modular.price.provider;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import vip.wqs.wine.api.WinePriceApi;
import vip.wqs.wine.modular.price.entity.WinePrice;
import vip.wqs.wine.modular.price.service.WinePriceService;
import vip.wqs.wine.modular.product.entity.WineProduct;
import vip.wqs.wine.modular.product.service.WineProductService;
import vip.wqs.wine.pojo.WinePricePojo;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 酒品价格API实现类
 *
 * @author WQS_TEAM
 * @date 2025/1/30
 */
@Service
public class WinePriceApiProvider implements WinePriceApi {

    private static final Logger log = LoggerFactory.getLogger(WinePriceApiProvider.class);

    @Resource
    private WinePriceService winePriceService;
    @Resource
    private WineProductService wineProductService;

    @Override
    public List<WinePricePojo> getWinePriceList(String productId) {
        log.info("获取酒品价格列表，酒品ID：{}", productId);
        
        if (StrUtil.isEmpty(productId)) {
            return Collections.emptyList();
        }
        
        QueryWrapper<WinePrice> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(WinePrice::getProductId, productId)
                .eq(WinePrice::getStatus, "ENABLE")
                .orderByDesc(WinePrice::getCreateTime);
        
        List<WinePrice> priceList = winePriceService.list(queryWrapper);
        return priceList.stream()
                .map(this::convertToPojo)
                .collect(Collectors.toList());
    }

    @Override
    public BigDecimal getWinePrice(String productId, String storeId) {
        log.info("获取酒品当前价格，酒品ID：{}，门店ID：{}", productId, storeId);
        
        if (StrUtil.isEmpty(productId)) {
            return null;
        }
        WinePrice winePrice = winePriceService.getPriceByStoreAndProduct(storeId, productId, null);
        if (ObjectUtil.isNotEmpty(winePrice)) {
            WineProduct wineProduct = wineProductService.queryEntity(productId);
            BigDecimal unitPrice = wineProduct != null ? wineProduct.getUnitPrice() : null;
            return winePrice.calculateFinalPrice(unitPrice);
        }
        return null;
    }

    @Override
    public WinePricePojo getWinePriceDetail(String productId, String storeId) {
        log.info("获取酒品价格详情，酒品ID：{}，门店ID：{}", productId, storeId);
        
        if (StrUtil.isEmpty(productId)) {
            return null;
        }
        
        WinePrice winePrice = winePriceService.getPriceByStoreAndProduct(storeId, productId, null);
        return ObjectUtil.isNotEmpty(winePrice) ? convertToPojo(winePrice) : null;
    }

    @Override
    public List<WinePricePojo> getWinePriceHistory(String productId, String storeId) {
        log.info("获取酒品价格历史，酒品ID：{}，门店ID：{}", productId, storeId);
        
        if (StrUtil.isEmpty(productId)) {
            return Collections.emptyList();
        }
        
        QueryWrapper<WinePrice> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(WinePrice::getProductId, productId)
                .orderByDesc(WinePrice::getCreateTime);
        
        if (StrUtil.isNotEmpty(storeId)) {
            queryWrapper.lambda().eq(WinePrice::getStoreId, storeId);
        } else {
            queryWrapper.lambda().isNull(WinePrice::getStoreId);
        }
        
        List<WinePrice> priceList = winePriceService.list(queryWrapper);
        return priceList.stream()
                .map(this::convertToPojo)
                .collect(Collectors.toList());
    }

    @Override
    public List<WinePricePojo> getWinePriceByAllStores(String productId) {
        log.info("获取酒品在所有门店的价格，酒品ID：{}", productId);
        
        if (StrUtil.isEmpty(productId)) {
            return Collections.emptyList();
        }
        
        QueryWrapper<WinePrice> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(WinePrice::getProductId, productId)
                .eq(WinePrice::getStatus, "ENABLE")
                .orderByDesc(WinePrice::getCreateTime);
        
        List<WinePrice> priceList = winePriceService.list(queryWrapper);
        return priceList.stream()
                .map(this::convertToPojo)
                .collect(Collectors.toList());
    }

    @Override
    public Boolean hasValidPrice(String productId, String storeId) {
        log.info("检查酒品是否有有效价格，酒品ID：{}，门店ID：{}", productId, storeId);
        
        if (StrUtil.isEmpty(productId)) {
            return false;
        }
        
        WinePrice winePrice = winePriceService.getPriceByStoreAndProduct(storeId, productId, null);
        return ObjectUtil.isNotEmpty(winePrice) && "ENABLE".equals(winePrice.getStatus());
    }

    @Override
    public Map<String, BigDecimal> batchGetWinePrice(List<String> productIds, String storeId) {
        log.info("批量获取酒品价格，酒品数量：{}，门店ID：{}", 
                CollUtil.isNotEmpty(productIds) ? productIds.size() : 0, storeId);
        
        if (CollUtil.isEmpty(productIds)) {
            return Collections.emptyMap();
        }
        
        Map<String, BigDecimal> priceMap = new HashMap<>();
        
        for (String productId : productIds) {
            BigDecimal price = getWinePrice(productId, storeId);
            if (ObjectUtil.isNotEmpty(price)) {
                priceMap.put(productId, price);
            }
        }
        
        return priceMap;
    }

    @Override
    public List<WinePricePojo> batchGetWinePriceDetail(List<String> productIds, String storeId) {
        log.info("批量获取酒品价格详情，酒品数量：{}，门店ID：{}", 
                CollUtil.isNotEmpty(productIds) ? productIds.size() : 0, storeId);
        
        if (CollUtil.isEmpty(productIds)) {
            return Collections.emptyList();
        }
        
        List<WinePricePojo> priceList = new ArrayList<>();
        
        for (String productId : productIds) {
            WinePricePojo priceDetail = getWinePriceDetail(productId, storeId);
            if (ObjectUtil.isNotEmpty(priceDetail)) {
                priceList.add(priceDetail);
            }
        }
        
        return priceList;
    }

    @Override
    public List<WinePricePojo> getStorePriceList(String storeId) {
        log.info("获取门店所有酒品的价格，门店ID：{}", storeId);
        
        List<WinePrice> priceList = winePriceService.getPricesByStore(storeId);
        return priceList.stream()
                .map(this::convertToPojo)
                .collect(Collectors.toList());
    }

    /**
     * 转换实体为POJO
     *
     * @param winePrice 价格实体
     * @return 价格POJO
     */
    private WinePricePojo convertToPojo(WinePrice winePrice) {
        if (ObjectUtil.isEmpty(winePrice)) {
            return null;
        }
        
        WinePricePojo pojo = new WinePricePojo();
        BeanUtil.copyProperties(winePrice, pojo);
        
        // TODO: 如果需要获取门店名称和酒品名称等关联信息，可以在这里实现
        // 目前先返回基础信息
        
        return pojo;
    }
} 