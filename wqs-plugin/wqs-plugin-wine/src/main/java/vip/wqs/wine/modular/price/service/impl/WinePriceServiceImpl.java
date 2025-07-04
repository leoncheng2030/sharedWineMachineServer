/*
 * Copyright [2022] [https://www.xiaonuo.vip]
 *
 * Snowy采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改Snowy源码头部的版权声明。
 * 3.本项目代码可免费商业使用，商业使用请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://www.xiaonuo.vip
 * 5.不可二次分发开源参与同类竞品，如有想法可联系团队xiaonuobase@qq.com商议合作。
 * 6.若您的项目无法满足以上几点，需要更多功能代码，获取Snowy商业授权许可，请在官网购买授权，地址为 https://www.xiaonuo.vip
 */

package vip.wqs.wine.modular.price.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.wqs.common.enums.CommonSortOrderEnum;
import vip.wqs.common.exception.CommonException;
import vip.wqs.common.page.CommonPageRequest;
import vip.wqs.wine.modular.price.entity.WinePrice;
import vip.wqs.wine.modular.price.mapper.WinePriceMapper;
import vip.wqs.wine.modular.price.param.*;
import vip.wqs.wine.modular.price.service.WinePriceService;
import vip.wqs.wine.modular.product.entity.WineProduct;
import vip.wqs.wine.modular.product.service.WineProductService;

import java.math.BigDecimal;
import java.util.List;

/**
 * 酒品价格Service接口实现类
 *
 * @author WQS_TEAM
 * @date 2025-01-27
 */
@Service
public class WinePriceServiceImpl extends ServiceImpl<WinePriceMapper, WinePrice> implements WinePriceService {

    @Autowired
    private WineProductService wineProductService;

    @Override
    public Page<WinePrice> page(WinePricePageParam winePricePageParam) {
        QueryWrapper<WinePrice> queryWrapper = new QueryWrapper<WinePrice>().checkSqlInjection();
        if (ObjectUtil.isNotEmpty(winePricePageParam.getSearchKey())) {
            queryWrapper.lambda().and(q -> q.like(WinePrice::getProductId, winePricePageParam.getSearchKey())
                    .or().like(WinePrice::getCapacity, winePricePageParam.getSearchKey()));
        }
        if (ObjectUtil.isNotEmpty(winePricePageParam.getProductId())) {
            queryWrapper.lambda().eq(WinePrice::getProductId, winePricePageParam.getProductId());
        }
        // 由于采用平台统一定价，不再支持门店自定义价格查询
        // if (ObjectUtil.isNotEmpty(winePricePageParam.getStoreId())) {
        //     queryWrapper.lambda().eq(WinePrice::getStoreId, winePricePageParam.getStoreId());
        // }
        if (ObjectUtil.isNotEmpty(winePricePageParam.getStatus())) {
            queryWrapper.lambda().eq(WinePrice::getStatus, winePricePageParam.getStatus());
        }
        if (ObjectUtil.isNotEmpty(winePricePageParam.getStartEffectiveDate()) && ObjectUtil.isNotEmpty(winePricePageParam.getEndEffectiveDate())) {
            queryWrapper.lambda().between(WinePrice::getEffectiveDate, winePricePageParam.getStartEffectiveDate(), winePricePageParam.getEndEffectiveDate());
        }
        if (ObjectUtil.isNotEmpty(winePricePageParam.getStartCreateTime()) && ObjectUtil.isNotEmpty(winePricePageParam.getEndCreateTime())) {
            queryWrapper.lambda().between(WinePrice::getCreateTime, winePricePageParam.getStartCreateTime(), winePricePageParam.getEndCreateTime());
        }
        if (ObjectUtil.isAllNotEmpty(winePricePageParam.getSortField(), winePricePageParam.getSortOrder())) {
            CommonSortOrderEnum.validate(winePricePageParam.getSortOrder());
            queryWrapper.orderBy(true, winePricePageParam.getSortOrder().equals(CommonSortOrderEnum.ASC.getValue()),
                    StrUtil.toUnderlineCase(winePricePageParam.getSortField()));
        } else {
            queryWrapper.lambda().orderByDesc(WinePrice::getCreateTime);
        }
        Page<WinePrice> page = this.page(CommonPageRequest.defaultPage(), queryWrapper);
        
        
        return page;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(WinePriceAddParam winePriceAddParam) {
        WinePrice winePrice = BeanUtil.toBean(winePriceAddParam, WinePrice.class);
        this.save(winePrice);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void edit(WinePriceEditParam winePriceEditParam) {
        WinePrice winePrice = this.queryEntity(winePriceEditParam.getId());
        BeanUtil.copyProperties(winePriceEditParam, winePrice);
        this.updateById(winePrice);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(WinePriceIdParam winePriceIdParam) {
        this.removeById(this.queryEntity(winePriceIdParam.getId()));
    }

    @Override
    public WinePrice detail(WinePriceIdParam winePriceIdParam) {
        WinePrice winePrice = this.queryEntity(winePriceIdParam.getId());
        return winePrice;
    }

    public WinePrice queryEntity(String id) {
        WinePrice winePrice = this.getById(id);
        if (ObjectUtil.isEmpty(winePrice)) {
            throw new CommonException("酒品价格不存在，id值为：{}", id);
        }
        return winePrice;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchDelete(List<WinePriceIdParam> winePriceIdParamList) {
        List<String> ids = winePriceIdParamList.stream()
                .map(WinePriceIdParam::getId)
                .toList();
        this.removeByIds(ids);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void enable(WinePriceIdParam winePriceIdParam) {
        WinePrice winePrice = this.queryEntity(winePriceIdParam.getId());
        winePrice.setStatus("ENABLE");
        this.updateById(winePrice);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void disable(WinePriceIdParam winePriceIdParam) {
        WinePrice winePrice = this.queryEntity(winePriceIdParam.getId());
        winePrice.setStatus("DISABLE");
        this.updateById(winePrice);
    }

    @Override
    public Object productSelector() {
        // TODO: 实现酒品选择器功能
        throw new CommonException("酒品选择器功能待实现");
    }

    @Override
    public void export(WinePriceExportParam exportParam) {
        // TODO: 实现价格导出逻辑
        // 1. 根据条件查询价格数据
        // 2. 生成Excel或CSV文件
        // 3. 返回下载链接
        throw new CommonException("价格导出功能待实现");
    }

    @Override
    public List<WinePrice> list(WinePricePageParam winePricePageParam) {
        QueryWrapper<WinePrice> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        return this.list(queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchSet(WinePriceBatchSetParam batchSetParam) {
        // 1. 验证酒品是否存在
        String productId = batchSetParam.getProductId();
        WineProduct product = wineProductService.getById(productId);
        if (ObjectUtil.isEmpty(product)) {
            throw new CommonException("酒品不存在，ID：{}", productId);
        }

        // 2. 验证价格配置数据
        List<WinePriceBatchSetParam.PriceConfigParam> priceConfigs = batchSetParam.getPriceConfigs();
        if (ObjectUtil.isEmpty(priceConfigs)) {
            throw new CommonException("价格配置列表不能为空");
        }

        // 验证容量重复
        List<BigDecimal> capacities = priceConfigs.stream()
                .map(WinePriceBatchSetParam.PriceConfigParam::getCapacity)
                .toList();
        long distinctCount = capacities.stream().distinct().count();
        if (distinctCount != capacities.size()) {
            throw new CommonException("价格配置中存在重复的容量");
        }

        // 3. 处理价格配置：分为新增和更新
        List<WinePrice> toSave = new java.util.ArrayList<>();
        List<WinePrice> toUpdate = new java.util.ArrayList<>();

        for (WinePriceBatchSetParam.PriceConfigParam config : priceConfigs) {
            // 验证折扣率范围
            if (config.getDiscountRate().compareTo(BigDecimal.ZERO) < 0 || 
                config.getDiscountRate().compareTo(BigDecimal.ONE) > 0) {
                throw new CommonException("折扣率必须在0-1之间，当前值：{}", config.getDiscountRate());
            }

            if (StrUtil.isNotEmpty(config.getId())) {
                // 更新现有记录
                WinePrice existingPrice = this.getById(config.getId());
                if (ObjectUtil.isEmpty(existingPrice)) {
                    throw new CommonException("价格配置不存在，ID：{}", config.getId());
                }
                
                // 更新字段
                existingPrice.setCapacity(config.getCapacity());
                existingPrice.setDiscountRate(config.getDiscountRate());
                existingPrice.setStatus(config.getStatus());
                existingPrice.setEffectiveDate(config.getEffectiveDate());
                existingPrice.setExpiryDate(config.getExpiryDate());
                existingPrice.setSortCode(config.getSortCode());
                existingPrice.setRemark(config.getRemark());
                
                toUpdate.add(existingPrice);
            } else {
                // 新增记录
                WinePrice newPrice = new WinePrice();
                newPrice.setProductId(productId);
                newPrice.setCapacity(config.getCapacity());
                newPrice.setDiscountRate(config.getDiscountRate());
                newPrice.setStatus(config.getStatus());
                newPrice.setEffectiveDate(config.getEffectiveDate());
                newPrice.setExpiryDate(config.getExpiryDate());
                newPrice.setSortCode(config.getSortCode());
                newPrice.setRemark(config.getRemark());
                
                toSave.add(newPrice);
            }
        }

        // 4. 批量保存和更新
        if (!toSave.isEmpty()) {
            this.saveBatch(toSave);
        }
        if (!toUpdate.isEmpty()) {
            this.updateBatchById(toUpdate);
        }

        // 5. 删除不在配置中的现有价格记录（可选，根据业务需求决定）
        // 查询该酒品的所有现有价格配置
        QueryWrapper<WinePrice> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(WinePrice::getProductId, productId);
        List<WinePrice> existingPrices = this.list(queryWrapper);
        
        // 获取当前配置中的所有ID
        List<String> configIds = priceConfigs.stream()
                .map(WinePriceBatchSetParam.PriceConfigParam::getId)
                .filter(StrUtil::isNotEmpty)
                .toList();
        
        // 找出需要删除的价格记录（现有的但不在当前配置中的）
        List<String> toDeleteIds = existingPrices.stream()
                .map(WinePrice::getId)
                .filter(id -> !configIds.contains(id))
                .toList();
        
        if (!toDeleteIds.isEmpty()) {
            this.removeByIds(toDeleteIds);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void copy(WinePriceIdParam winePriceIdParam) {
        WinePrice originalPrice = this.queryEntity(winePriceIdParam.getId());
        WinePrice newPrice = BeanUtil.toBean(originalPrice, WinePrice.class);
        newPrice.setId(null);
        this.save(newPrice);
    }

    @Override
    public Object history(WinePriceIdParam winePriceIdParam) {
        // TODO: 实现价格历史记录功能
        throw new CommonException("价格历史记录功能待实现");
    }

    @Override
    public Object preview(WinePricePreviewParam previewParam) {
        // TODO: 实现价格预览逻辑
        // 1. 根据酒品ID和预览日期查询有效价格策略
        // 2. 计算折扣后的最终价格
        // 3. 返回价格详情和计算过程
        throw new CommonException("价格预览功能待实现");
    }

    @Override
    public Object current(WinePriceIdParam winePriceIdParam) {
        // TODO: 实现获取当前有效价格功能
        throw new CommonException("获取当前有效价格功能待实现");
    }

    @Override
    public Object storeSelector() {
        // TODO: 实现门店选择器功能
        // 这里应该调用门店服务获取门店列表
        throw new CommonException("门店选择器功能待实现");
    }

    @Override
    public WinePrice getPriceByStoreAndProduct(String storeId, String productId, String capacity) {
        QueryWrapper<WinePrice> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(WinePrice::getProductId, productId)
                .eq(WinePrice::getStatus, "ENABLE");
        
        if (ObjectUtil.isNotEmpty(capacity)) {
            queryWrapper.lambda().eq(WinePrice::getCapacity, capacity);
        }
        
        // 优先查找门店价格
        if (ObjectUtil.isNotEmpty(storeId)) {
            queryWrapper.lambda().eq(WinePrice::getStoreId, storeId);
            WinePrice storePrice = this.getOne(queryWrapper);
            if (ObjectUtil.isNotEmpty(storePrice)) {
                return storePrice;
            }
        }
        
        // 如果没有门店价格，查找平台价格（storeId为null）
        queryWrapper.lambda().isNull(WinePrice::getStoreId);
        return this.getOne(queryWrapper);
    }

    @Override
    public List<WinePrice> getPricesByStore(String storeId) {
        QueryWrapper<WinePrice> queryWrapper = new QueryWrapper<>();
        if (ObjectUtil.isNotEmpty(storeId)) {
            queryWrapper.lambda().eq(WinePrice::getStoreId, storeId);
        } else {
            queryWrapper.lambda().isNull(WinePrice::getStoreId);
        }
        queryWrapper.lambda()
                .eq(WinePrice::getStatus, "ENABLE")
                .orderByDesc(WinePrice::getCreateTime);
        return this.list(queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void copyPlatformPricesToStore(String storeId, List<String> productIds) {
        if (ObjectUtil.isEmpty(storeId)) {
            throw new CommonException("门店ID不能为空");
        }
        if (ObjectUtil.isEmpty(productIds)) {
            throw new CommonException("酒品ID列表不能为空");
        }
        
        // 查询平台价格（storeId为null的价格）
        QueryWrapper<WinePrice> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .isNull(WinePrice::getStoreId)
                .in(WinePrice::getProductId, productIds)
                .eq(WinePrice::getStatus, "ENABLE");
        List<WinePrice> platformPrices = this.list(queryWrapper);
        
        if (ObjectUtil.isEmpty(platformPrices)) {
            throw new CommonException("未找到对应的平台价格");
        }
        
        // 复制平台价格到门店
        List<WinePrice> storePrices = platformPrices.stream()
                .map(platformPrice -> {
                    WinePrice storePrice = BeanUtil.toBean(platformPrice, WinePrice.class);
                    storePrice.setId(null); // 清空ID，让数据库自动生成
                    storePrice.setStoreId(storeId); // 设置门店ID
                    return storePrice;
                })
                .toList();
        
        // 批量保存门店价格
        this.saveBatch(storePrices);
    }
} 