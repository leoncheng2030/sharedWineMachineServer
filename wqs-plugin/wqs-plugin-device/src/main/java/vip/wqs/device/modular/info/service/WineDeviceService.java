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
package vip.wqs.device.modular.info.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import vip.wqs.device.modular.info.entity.WineDevice;
import vip.wqs.device.modular.info.param.WineDeviceAddParam;
import vip.wqs.device.modular.info.param.WineDeviceEditParam;
import vip.wqs.device.modular.info.param.WineDeviceIdParam;
import vip.wqs.device.modular.info.param.WineDevicePageParam;

import java.util.List;

/**
 * 设备Service接口
 *
 * @author wqs
 * @date 2025/1/27
 */
public interface WineDeviceService extends IService<WineDevice> {

    /**
     * 获取设备分页
     *
     * @param wineDevicePageParam 分页参数
     * @return 分页结果
     * @author wqs
     * @date 2025/1/27
     */
    Page<WineDevice> page(WineDevicePageParam wineDevicePageParam);

    /**
     * 获取设备列表
     *
     * @param wineDevicePageParam 查询参数
     * @return 设备列表
     * @author wqs
     * @date 2025/1/27
     */
    List<WineDevice> list(WineDevicePageParam wineDevicePageParam);

    /**
     * 添加设备
     *
     * @param wineDeviceAddParam 新增参数
     * @author wqs
     * @date 2025/1/27
     */
    void add(WineDeviceAddParam wineDeviceAddParam);

    /**
     * 编辑设备
     *
     * @param wineDeviceEditParam 编辑参数
     * @author wqs
     * @date 2025/1/27
     */
    void edit(WineDeviceEditParam wineDeviceEditParam);

    /**
     * 删除设备
     *
     * @param wineDeviceIdParam 删除参数
     * @author wqs
     * @date 2025/1/27
     */
    void delete(WineDeviceIdParam wineDeviceIdParam);

    /**
     * 批量删除设备
     *
     * @param wineDeviceIdParamList 删除参数列表
     * @author wqs
     * @date 2025/1/27
     */
    void batchDelete(List<WineDeviceIdParam> wineDeviceIdParamList);

    /**
     * 获取设备详情
     *
     * @param wineDeviceIdParam 查询参数
     * @return 设备详情
     * @author wqs
     * @date 2025/1/27
     */
    WineDevice detail(WineDeviceIdParam wineDeviceIdParam);

    /**
     * 获取设备详情
     *
     * @param id 设备ID
     * @return 设备详情
     * @author wqs
     * @date 2025/1/27
     */
    WineDevice queryEntity(String id);

    /**
     * 禁用设备
     *
     * @param wineDeviceIdParam 禁用参数
     * @author wqs
     * @date 2025/1/27
     */
    void disable(WineDeviceIdParam wineDeviceIdParam);

    /**
     * 启用设备
     *
     * @param wineDeviceIdParam 启用参数
     * @author wqs
     * @date 2025/1/27
     */
    void enable(WineDeviceIdParam wineDeviceIdParam);

    /**
     * 设备导出
     *
     * @param wineDevicePageParam 导出参数
     * @author wqs
     * @date 2025/1/27
     */
    void export(WineDevicePageParam wineDevicePageParam);

    /**
     * 设备选择器
     *
     * @param wineDevicePageParam 选择器参数
     * @return 设备列表
     * @author wqs
     * @date 2025/1/27
     */
    List<WineDevice> selector(WineDevicePageParam wineDevicePageParam);

    /**
     * 获取管理员用户选择器
     *
     * @return 用户列表
     * @author wqs
     * @date 2025/1/27
     */
    Object managerUserSelector();

    /**
     * 获取门店选择器
     *
     * @return 门店列表
     * @author wqs
     * @date 2025/1/27
     */
    Object storeSelector();

    /**
     * 获取酒品选择器
     *
     * @return 酒品列表
     * @author wqs
     * @date 2025/1/27
     */
    Object productSelector();

    /**
     * 绑定设备到酒品
     *
     * @param deviceId 设备ID
     * @param productId 酒品ID
     * @author wqs
     * @date 2025/1/27
     */
    void bindProduct(String deviceId, String productId);

    /**
     * 解绑设备酒品
     *
     * @param deviceId 设备ID
     * @author wqs
     * @date 2025/1/27
     */
    void unbindProduct(String deviceId);

    /**
     * 根据门店ID获取设备列表
     *
     * @param storeId 门店ID
     * @return 设备列表
     * @author wqs
     * @date 2025/1/27
     */
    List<WineDevice> getDevicesByStore(String storeId);

    /**
     * 绑定设备
     *
     * @param wineDeviceIdParam 绑定参数
     * @author wqs
     * @date 2025/1/27
     */
    void bind(WineDeviceIdParam wineDeviceIdParam);

    /**
     * 解绑设备
     *
     * @param wineDeviceIdParam 解绑参数
     * @author wqs
     * @date 2025/1/27
     */
    void unbind(WineDeviceIdParam wineDeviceIdParam);

    /**
     * 根据deviceCode获取设备信息
     *
     * @param deviceCode 设备编码
     * @return 设备信息
     * @author wqs
     * @date 2025/1/27
     */
    WineDevice getDeviceByDeviceCode(String deviceCode);
} 