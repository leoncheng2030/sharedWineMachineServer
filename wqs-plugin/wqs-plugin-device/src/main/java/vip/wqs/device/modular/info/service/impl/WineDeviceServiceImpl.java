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
package vip.wqs.device.modular.info.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.wqs.common.exception.CommonException;
import vip.wqs.common.page.CommonPageRequest;
import vip.wqs.device.modular.info.entity.WineDevice;
import vip.wqs.device.modular.info.mapper.WineDeviceMapper;
import vip.wqs.device.modular.info.param.WineDeviceAddParam;
import vip.wqs.device.modular.info.param.WineDeviceEditParam;
import vip.wqs.device.modular.info.param.WineDeviceIdParam;
import vip.wqs.device.modular.info.param.WineDevicePageParam;
import vip.wqs.device.modular.info.service.WineDeviceService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 设备Service接口实现类
 *
 * @author wqs
 * @date 2025/01/27
 */
@Service
public class WineDeviceServiceImpl extends ServiceImpl<WineDeviceMapper, WineDevice> implements WineDeviceService {

    @Override
    public Page<WineDevice> page(WineDevicePageParam wineDevicePageParam) {
        QueryWrapper<WineDevice> queryWrapper = new QueryWrapper<>();

        // 关键词搜索
        if (StrUtil.isNotBlank(wineDevicePageParam.getSearchKey())) {
            queryWrapper.and(wrapper -> wrapper
                    .like("device_code", wineDevicePageParam.getSearchKey())
                    .or().like("device_name", wineDevicePageParam.getSearchKey())
                    .or().like("location", wineDevicePageParam.getSearchKey()));
        }

        // 设备编码
        if (StrUtil.isNotBlank(wineDevicePageParam.getDeviceCode())) {
            queryWrapper.like("device_code", wineDevicePageParam.getDeviceCode());
        }

        // 设备名称
        if (StrUtil.isNotBlank(wineDevicePageParam.getDeviceName())) {
            queryWrapper.like("device_name", wineDevicePageParam.getDeviceName());
        }

        // 设备位置
        if (StrUtil.isNotBlank(wineDevicePageParam.getLocation())) {
            queryWrapper.like("location", wineDevicePageParam.getLocation());
        }

        // 设备状态
        if (StrUtil.isNotBlank(wineDevicePageParam.getStatus())) {
            queryWrapper.eq("status", wineDevicePageParam.getStatus());
        }

        // 管理员用户ID
        if (StrUtil.isNotBlank(wineDevicePageParam.getManagerUserId())) {
            queryWrapper.eq("manager_user_id", wineDevicePageParam.getManagerUserId());
        }

        // 所属门店ID
        if (StrUtil.isNotBlank(wineDevicePageParam.getStoreId())) {
            queryWrapper.eq("store_id", wineDevicePageParam.getStoreId());
        }

        // 当前绑定的酒品ID
        if (StrUtil.isNotBlank(wineDevicePageParam.getCurrentProductId())) {
            queryWrapper.eq("current_product_id", wineDevicePageParam.getCurrentProductId());
        }

        // 创建时间范围
        if (StrUtil.isNotBlank(wineDevicePageParam.getStartCreateTime())) {
            queryWrapper.ge("create_time", wineDevicePageParam.getStartCreateTime());
        }
        if (StrUtil.isNotBlank(wineDevicePageParam.getEndCreateTime())) {
            queryWrapper.le("create_time", wineDevicePageParam.getEndCreateTime());
        }

        // 排序
        if (StrUtil.isNotBlank(wineDevicePageParam.getSortField())) {
            if ("ASCEND".equals(wineDevicePageParam.getSortOrder())) {
                queryWrapper.orderByAsc(StrUtil.toUnderlineCase(wineDevicePageParam.getSortField()));
            } else {
                queryWrapper.orderByDesc(StrUtil.toUnderlineCase(wineDevicePageParam.getSortField()));
            }
        } else {
            queryWrapper.orderByAsc("sort_code").orderByDesc("create_time");
        }

        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Override
    public List<WineDevice> list(WineDevicePageParam wineDevicePageParam) {
        QueryWrapper<WineDevice> queryWrapper = new QueryWrapper<>();

        // 设备状态
        if (StrUtil.isNotBlank(wineDevicePageParam.getStatus())) {
            queryWrapper.eq("status", wineDevicePageParam.getStatus());
        }

        // 管理员用户ID
        if (StrUtil.isNotBlank(wineDevicePageParam.getManagerUserId())) {
            queryWrapper.eq("manager_user_id", wineDevicePageParam.getManagerUserId());
        }

        queryWrapper.orderByAsc("sort_code").orderByDesc("create_time");
        return this.list(queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(WineDeviceAddParam wineDeviceAddParam) {
        // 检查设备编码是否重复
        QueryWrapper<WineDevice> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_code", wineDeviceAddParam.getDeviceCode());
        if (this.count(queryWrapper) > 0) {
            throw new CommonException("设备编码已存在");
        }

        WineDevice wineDevice = BeanUtil.toBean(wineDeviceAddParam, WineDevice.class);
        // 设置默认状态
        if (StrUtil.isBlank(wineDevice.getStatus())) {
            wineDevice.setStatus("OFFLINE");
        }
        // 设置默认排序码
        if (ObjectUtil.isNull(wineDevice.getSortCode())) {
            wineDevice.setSortCode(99);
        }
        this.save(wineDevice);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void edit(WineDeviceEditParam wineDeviceEditParam) {
        // 检查设备编码是否重复（排除自己）
        QueryWrapper<WineDevice> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_code", wineDeviceEditParam.getDeviceCode());
        queryWrapper.ne("id", wineDeviceEditParam.getId());
        if (this.count(queryWrapper) > 0) {
            throw new CommonException("设备编码已存在");
        }

        WineDevice wineDevice = BeanUtil.toBean(wineDeviceEditParam, WineDevice.class);
        this.updateById(wineDevice);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(WineDeviceIdParam wineDeviceIdParam) {
        this.removeById(wineDeviceIdParam.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchDelete(List<WineDeviceIdParam> wineDeviceIdParamList) {
        List<String> ids = wineDeviceIdParamList.stream()
                .map(WineDeviceIdParam::getId)
                .collect(Collectors.toList());
        this.removeByIds(ids);
    }

    @Override
    public WineDevice detail(WineDeviceIdParam wineDeviceIdParam) {
        return this.getById(wineDeviceIdParam.getId());
    }

    @Override
    public WineDevice queryEntity(String id) {
        return this.getById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void disable(WineDeviceIdParam wineDeviceIdParam) {
        WineDevice wineDevice = this.getById(wineDeviceIdParam.getId());
        if (wineDevice != null) {
            wineDevice.setStatus("OFFLINE");
            this.updateById(wineDevice);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void enable(WineDeviceIdParam wineDeviceIdParam) {
        WineDevice wineDevice = this.getById(wineDeviceIdParam.getId());
        if (wineDevice != null) {
            wineDevice.setStatus("ONLINE");
            this.updateById(wineDevice);
        }
    }

    @Override
    public void export(WineDevicePageParam wineDevicePageParam) {
        throw new CommonException("导出功能待实现");
    }

    @Override
    public List<WineDevice> selector(WineDevicePageParam wineDevicePageParam) {
        QueryWrapper<WineDevice> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("status", "ONLINE", "OFFLINE"); // 排除维护状态的设备
        queryWrapper.orderByAsc("sort_code").orderByAsc("device_name");
        return this.list(queryWrapper);
    }

    @Override
    public Object managerUserSelector() {
        // TODO: 调用用户模块API获取C端用户列表
        throw new CommonException("管理员用户选择器功能待实现");
    }

    @Override
    public Object storeSelector() {
        // TODO: 调用门店模块API获取门店列表
        throw new CommonException("门店选择器功能待实现");
    }

    @Override
    public Object productSelector() {
        // TODO: 调用酒品模块API获取酒品列表
        throw new CommonException("酒品选择器功能待实现");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void bindProduct(String deviceId, String productId) {
        WineDevice device = this.getById(deviceId);
        if (device == null) {
            throw new CommonException("设备不存在");
        }

        // 更新设备绑定的酒品
        device.setCurrentProductId(productId);
        this.updateById(device);

        // TODO: 记录绑定历史
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void unbindProduct(String deviceId) {
        WineDevice device = this.getById(deviceId);
        if (device == null) {
            throw new CommonException("设备不存在");
        }

        // 清空设备绑定的酒品
        device.setCurrentProductId(null);
        this.updateById(device);

        // TODO: 记录解绑历史
    }

    @Override
    public List<WineDevice> getDevicesByStore(String storeId) {
        QueryWrapper<WineDevice> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("store_id", storeId);
        queryWrapper.orderByAsc("sort_code").orderByAsc("device_name");
        return this.list(queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void bind(WineDeviceIdParam wineDeviceIdParam) {
        WineDevice wineDevice = this.getById(wineDeviceIdParam.getId());
        if (wineDevice != null) {
            wineDevice.setBindTime(LocalDateTime.now());
            wineDevice.setStatus("ONLINE");
            this.updateById(wineDevice);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void unbind(WineDeviceIdParam wineDeviceIdParam) {
        WineDevice wineDevice = this.getById(wineDeviceIdParam.getId());
        if (wineDevice != null) {
            wineDevice.setBindTime(null);
            wineDevice.setStatus("OFFLINE");
            wineDevice.setLastOnlineTime(LocalDateTime.now());
            this.updateById(wineDevice);
        }
    }
}