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
package vip.wqs.commission.modular.config.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fhs.core.trans.anno.TransMethodResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.wqs.commission.modular.config.entity.WineCommission;
import vip.wqs.commission.modular.config.mapper.WineCommissionMapper;
import vip.wqs.commission.modular.config.param.WineCommissionAddParam;
import vip.wqs.commission.modular.config.param.WineCommissionEditParam;
import vip.wqs.commission.modular.config.param.WineCommissionIdParam;
import vip.wqs.commission.modular.config.param.WineCommissionPageParam;
import vip.wqs.commission.modular.config.service.WineCommissionService;
import vip.wqs.common.exception.CommonException;
import vip.wqs.common.page.CommonPageRequest;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 分销配置Service实现类
 *
 * @author WQS_TEAM
 * @date 2025-01-29
 */
@Service
public class WineCommissionServiceImpl extends ServiceImpl<WineCommissionMapper, WineCommission> implements WineCommissionService {

    @Override
    @TransMethodResult
    public Page<WineCommission> page(WineCommissionPageParam wineCommissionPageParam) {
        QueryWrapper<WineCommission> queryWrapper = new QueryWrapper<>();
        
        // 门店ID查询
        if (StrUtil.isNotBlank(wineCommissionPageParam.getStoreId())) {
            queryWrapper.eq("store_id", wineCommissionPageParam.getStoreId());
        }
        
        // 酒品ID查询
        if (StrUtil.isNotBlank(wineCommissionPageParam.getProductId())) {
            queryWrapper.eq("product_id", wineCommissionPageParam.getProductId());
        }
        
        // 状态查询
        if (StrUtil.isNotBlank(wineCommissionPageParam.getStatus())) {
            queryWrapper.eq("status", wineCommissionPageParam.getStatus());
        }
        
        // 配置类型查询 - 根据configType参数构建对应的查询条件
        if (StrUtil.isNotBlank(wineCommissionPageParam.getConfigType())) {
            String configType = wineCommissionPageParam.getConfigType();
            switch (configType) {
                case "PLATFORM_DEFAULT":
                    // 平台默认：store_id和product_id都为空
                    queryWrapper.isNull("store_id").isNull("product_id");
                    break;
                case "STORE_GENERAL":
                    // 门店通用：store_id不为空，product_id为空
                    queryWrapper.isNotNull("store_id").isNull("product_id");
                    break;
                case "PRODUCT_GENERAL":
                    // 酒品通用：store_id为空，product_id不为空
                    queryWrapper.isNull("store_id").isNotNull("product_id");
                    break;
                case "STORE_PRODUCT":
                    // 门店酒品专属：store_id和product_id都不为空
                    queryWrapper.isNotNull("store_id").isNotNull("product_id");
                    break;
                default:
                    // 未知类型，不添加查询条件
                    break;
            }
        }
        
        // 排序
        queryWrapper.orderByAsc("sort_code").orderByDesc("create_time");
        
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(WineCommissionAddParam wineCommissionAddParam) {
        // 验证总分成比例不能超过100%（允许小于100%，为后期利润分成等灵活模式预留空间）
        BigDecimal totalRate = wineCommissionAddParam.getTotalRate();
        if (totalRate.compareTo(BigDecimal.ONE) > 0) {
            throw new CommonException("总分成比例不能超过100%，当前为：" + 
                totalRate.multiply(new BigDecimal("100")) + "%");
        }

        // 检查是否存在重复配置
        QueryWrapper<WineCommission> checkWrapper = new QueryWrapper<>();
        checkWrapper.eq("store_id", wineCommissionAddParam.getStoreId())
                   .eq("product_id", wineCommissionAddParam.getProductId());
        long duplicateCount = this.count(checkWrapper);
        if (duplicateCount > 0) {
            throw new CommonException("该门店和酒品的分销配置已存在，请勿重复添加");
        }

        // 转换并保存
        WineCommission wineCommission = BeanUtil.toBean(wineCommissionAddParam, WineCommission.class);
        wineCommission.setStatus("ENABLE");
        
        // 设置默认生效时间
        if (wineCommission.getEffectiveDate() == null) {
            wineCommission.setEffectiveDate(new Date());
        }
        
        // 设置默认失效时间（100年后）
        if (wineCommission.getExpiryDate() == null) {
            wineCommission.setExpiryDate(new Date(System.currentTimeMillis() + 100L * 365 * 24 * 60 * 60 * 1000));
        }

        this.save(wineCommission);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(WineCommissionEditParam wineCommissionEditParam) {
        // 验证总分成比例不能超过100%（允许小于100%，为后期利润分成等灵活模式预留空间）
        BigDecimal totalRate = wineCommissionEditParam.getTotalRate();
        if (totalRate.compareTo(BigDecimal.ONE) > 0) {
            throw new CommonException("总分成比例不能超过100%，当前为：" + 
                totalRate.multiply(new BigDecimal("100")) + "%");
        }

        // 获取原配置
        WineCommission wineCommission = this.getById(wineCommissionEditParam.getId());
        if (ObjectUtil.isEmpty(wineCommission)) {
            throw new CommonException("分销配置不存在");
        }

        // 检查是否存在重复配置（排除当前配置）
        QueryWrapper<WineCommission> checkWrapper = new QueryWrapper<>();
        checkWrapper.eq("store_id", wineCommissionEditParam.getStoreId())
                   .eq("product_id", wineCommissionEditParam.getProductId())
                   .ne("id", wineCommissionEditParam.getId());
        long duplicateCount = this.count(checkWrapper);
        if (duplicateCount > 0) {
            throw new CommonException("该门店和酒品的分销配置已存在，请勿重复配置");
        }

        // 更新配置
        BeanUtil.copyProperties(wineCommissionEditParam, wineCommission);
        this.updateById(wineCommission);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(WineCommissionIdParam wineCommissionIdParam) {
        // 检查是否为平台默认配置
        WineCommission wineCommission = this.getById(wineCommissionIdParam.getId());
        if (wineCommission != null && StrUtil.isBlank(wineCommission.getStoreId()) && StrUtil.isBlank(wineCommission.getProductId())) {
            throw new CommonException("平台默认配置不能删除，请联系管理员");
        }

        this.removeById(wineCommissionIdParam.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<WineCommissionIdParam> wineCommissionIdParamList) {
        if (CollUtil.isEmpty(wineCommissionIdParamList)) {
            throw new CommonException("请选择要删除的分销配置");
        }

        List<String> idList = wineCommissionIdParamList.stream()
                .map(WineCommissionIdParam::getId)
                .collect(java.util.stream.Collectors.toList());

        // 检查是否包含平台默认配置
        QueryWrapper<WineCommission> checkWrapper = new QueryWrapper<>();
        checkWrapper.in("id", idList)
                   .isNull("store_id")
                   .isNull("product_id");
        long defaultConfigCount = this.count(checkWrapper);
        
        if (defaultConfigCount > 0) {
            throw new CommonException("平台默认配置不能删除，请联系管理员");
        }

        this.removeByIds(idList);
    }

    @Override
    @TransMethodResult
    public WineCommission detail(WineCommissionIdParam wineCommissionIdParam) {
        WineCommission wineCommission = this.getById(wineCommissionIdParam.getId());
        if (ObjectUtil.isEmpty(wineCommission)) {
            throw new CommonException("分销配置不存在");
        }
        return wineCommission;
    }

    @Override
    @TransMethodResult
    public WineCommission getCommissionByStoreAndProduct(String storeId, String productId) {
        // 优先级查询：门店+酒品专属 > 门店通用 > 酒品通用 > 平台默认
        
        // 1. 门店+酒品专属配置
        QueryWrapper<WineCommission> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("store_id", storeId)
                .eq("product_id", productId)
                .eq("status", "ENABLE");
        WineCommission commission = this.getOne(wrapper1);
        if (commission != null) {
            return commission;
        }
        
        // 2. 门店通用配置
        QueryWrapper<WineCommission> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("store_id", storeId)
                .isNull("product_id")
                .eq("status", "ENABLE");
        commission = this.getOne(wrapper2);
        if (commission != null) {
            return commission;
        }
        
        // 3. 酒品通用配置
        QueryWrapper<WineCommission> wrapper3 = new QueryWrapper<>();
        wrapper3.isNull("store_id")
                .eq("product_id", productId)
                .eq("status", "ENABLE");
        commission = this.getOne(wrapper3);
        if (commission != null) {
            return commission;
        }
        
        // 4. 平台默认配置
        QueryWrapper<WineCommission> wrapper4 = new QueryWrapper<>();
        wrapper4.isNull("store_id")
                .isNull("product_id")
                .eq("status", "ENABLE");
        return this.getOne(wrapper4);
    }

    @Override
    @TransMethodResult
    public List<WineCommission> getByStoreId(String storeId) {
        QueryWrapper<WineCommission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("store_id", storeId)
                   .eq("status", "ENABLE")
                   .orderByAsc("sort_code");
        return this.list(queryWrapper);
    }

    @Override
    @TransMethodResult
    public List<WineCommission> getByProductId(String productId) {
        QueryWrapper<WineCommission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_id", productId)
                   .eq("status", "ENABLE")
                   .orderByAsc("sort_code");
        return this.list(queryWrapper);
    }

    @Override
    @TransMethodResult
    public WineCommission getDefaultConfig() {
        QueryWrapper<WineCommission> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull("store_id")
                   .isNull("product_id")
                   .eq("status", "ENABLE");
        return this.getOne(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void enable(WineCommissionIdParam wineCommissionIdParam) {
        WineCommission commission = this.getById(wineCommissionIdParam.getId());
        if (commission != null) {
            commission.setStatus("ENABLE");
            this.updateById(commission);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disable(WineCommissionIdParam wineCommissionIdParam) {
        // 检查是否为平台默认配置
        WineCommission commission = this.getById(wineCommissionIdParam.getId());
        if (commission != null && StrUtil.isBlank(commission.getStoreId()) && StrUtil.isBlank(commission.getProductId())) {
            throw new CommonException("平台默认配置不能禁用，请联系管理员");
        }

        if (commission != null) {
            commission.setStatus("DISABLE");
            this.updateById(commission);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchEnable(List<WineCommissionIdParam> wineCommissionIdParamList) {
        if (CollUtil.isEmpty(wineCommissionIdParamList)) {
            throw new CommonException("请选择要启用的分销配置");
        }
        
        List<String> idList = wineCommissionIdParamList.stream()
                .map(WineCommissionIdParam::getId)
                .collect(java.util.stream.Collectors.toList());
        
        // 批量更新状态
        List<WineCommission> commissions = this.listByIds(idList);
        commissions.forEach(commission -> commission.setStatus("ENABLE"));
        this.updateBatchById(commissions);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDisable(List<WineCommissionIdParam> wineCommissionIdParamList) {
        if (CollUtil.isEmpty(wineCommissionIdParamList)) {
            throw new CommonException("请选择要禁用的分销配置");
        }

        List<String> idList = wineCommissionIdParamList.stream()
                .map(WineCommissionIdParam::getId)
                .collect(java.util.stream.Collectors.toList());

        // 检查是否包含平台默认配置
        QueryWrapper<WineCommission> checkWrapper = new QueryWrapper<>();
        checkWrapper.in("id", idList)
                   .isNull("store_id")
                   .isNull("product_id");
        long defaultConfigCount = this.count(checkWrapper);
        
        if (defaultConfigCount > 0) {
            throw new CommonException("平台默认配置不能禁用，请联系管理员");
        }

        // 批量更新状态
        List<WineCommission> commissions = this.listByIds(idList);
        commissions.forEach(commission -> commission.setStatus("DISABLE"));
        this.updateBatchById(commissions);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchSet(List<String> storeIds, List<String> productIds, WineCommissionAddParam wineCommissionAddParam) {
        // 验证总分成比例不能超过100%（允许小于100%，为后期利润分成等灵活模式预留空间）
        BigDecimal totalRate = wineCommissionAddParam.getTotalRate();
        if (totalRate.compareTo(BigDecimal.ONE) > 0) {
            throw new CommonException("总分成比例不能超过100%，当前为：" + 
                totalRate.multiply(new BigDecimal("100")) + "%");
        }

        if (CollUtil.isEmpty(storeIds) && CollUtil.isEmpty(productIds)) {
            throw new CommonException("请选择门店或酒品");
        }

        // 生成配置组合
        if (CollUtil.isNotEmpty(storeIds) && CollUtil.isNotEmpty(productIds)) {
            // 门店+酒品组合
            for (String storeId : storeIds) {
                for (String productId : productIds) {
                    createCommissionIfNotExists(storeId, productId, wineCommissionAddParam);
                }
            }
        } else if (CollUtil.isNotEmpty(storeIds)) {
            // 仅门店
            for (String storeId : storeIds) {
                createCommissionIfNotExists(storeId, null, wineCommissionAddParam);
            }
        } else {
            // 仅酒品
            for (String productId : productIds) {
                createCommissionIfNotExists(null, productId, wineCommissionAddParam);
            }
        }
    }

    /**
     * 如果配置不存在则创建
     */
    private void createCommissionIfNotExists(String storeId, String productId, WineCommissionAddParam param) {
        QueryWrapper<WineCommission> checkWrapper = new QueryWrapper<>();
        checkWrapper.eq("store_id", storeId)
                   .eq("product_id", productId);
        long duplicateCount = this.count(checkWrapper);
        
        if (duplicateCount == 0) {
            WineCommission commission = BeanUtil.toBean(param, WineCommission.class);
            commission.setStoreId(storeId);
            commission.setProductId(productId);
            commission.setStatus("ENABLE");
            
            if (commission.getEffectiveDate() == null) {
                commission.setEffectiveDate(new Date());
            }
            if (commission.getExpiryDate() == null) {
                commission.setExpiryDate(new Date(System.currentTimeMillis() + 100L * 365 * 24 * 60 * 60 * 1000));
            }
            
            this.save(commission);
        }
    }

    @Override
    @TransMethodResult
    public WineCommission getEffectiveCommission(String storeId, String productId) {
        // 按优先级查找：门店+酒品专属 > 门店通用 > 酒品通用 > 平台默认
        
        // 1. 门店+酒品专属配置（最高优先级）
        if (StrUtil.isNotBlank(storeId) && StrUtil.isNotBlank(productId)) {
            QueryWrapper<WineCommission> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("store_id", storeId)
                    .eq("product_id", productId)
                    .eq("status", "ENABLE")
                    .orderByAsc("sort_code")
                    .orderByDesc("create_time")
                    .last("LIMIT 1");
            WineCommission commission = this.getOne(wrapper1);
            if (commission != null) {
                return commission;
            }
        }
        
        // 2. 门店通用配置
        if (StrUtil.isNotBlank(storeId)) {
            QueryWrapper<WineCommission> wrapper2 = new QueryWrapper<>();
            wrapper2.eq("store_id", storeId)
                    .isNull("product_id")
                    .eq("status", "ENABLE")
                    .orderByAsc("sort_code")
                    .orderByDesc("create_time")
                    .last("LIMIT 1");
            WineCommission commission = this.getOne(wrapper2);
            if (commission != null) {
                return commission;
            }
        }
        
        // 3. 酒品通用配置
        if (StrUtil.isNotBlank(productId)) {
            QueryWrapper<WineCommission> wrapper3 = new QueryWrapper<>();
            wrapper3.isNull("store_id")
                    .eq("product_id", productId)
                    .eq("status", "ENABLE")
                    .orderByAsc("sort_code")
                    .orderByDesc("create_time")
                    .last("LIMIT 1");
            WineCommission commission = this.getOne(wrapper3);
            if (commission != null) {
                return commission;
            }
        }
        
        // 4. 平台默认配置（最低优先级）
        QueryWrapper<WineCommission> wrapper4 = new QueryWrapper<>();
        wrapper4.isNull("store_id")
                .isNull("product_id")
                .eq("status", "ENABLE")
                .orderByAsc("sort_code")
                .orderByDesc("create_time")
                .last("LIMIT 1");
        return this.getOne(wrapper4);
    }
}