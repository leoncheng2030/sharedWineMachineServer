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
package vip.wqs.dev.modular.city.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vip.wqs.common.annotation.CommonLog;
import vip.wqs.common.pojo.CommonResult;
import vip.wqs.dev.modular.city.entity.WineCity;
import vip.wqs.dev.modular.city.param.WineCityAddParam;
import vip.wqs.dev.modular.city.param.WineCityEditParam;
import vip.wqs.dev.modular.city.param.WineCityIdParam;
import vip.wqs.dev.modular.city.param.WineCityPageParam;
import vip.wqs.dev.modular.city.service.WineCityService;

import java.util.List;

/**
 * 城市管理控制器
 *
 * @author wqs
 * @date 2025/01/30
 */
@Tag(name = "城市管理控制器")
@ApiSupport(author = "WQS_TEAM", order = 1)
@RestController
@Validated
public class WineCityController {

    @Resource
    private WineCityService wineCityService;

    /**
     * 获取城市分页
     */
    @ApiOperationSupport(order = 1)
    @Operation(summary = "获取城市分页")
    @SaCheckPermission("/dev/city/page")
    @GetMapping("/dev/city/page")
    public CommonResult<Page<WineCity>> page(WineCityPageParam wineCityPageParam) {
        return CommonResult.data(wineCityService.page(wineCityPageParam));
    }

    /**
     * 获取城市列表
     */
    @ApiOperationSupport(order = 2)
    @Operation(summary = "获取城市列表")
    @SaCheckPermission("/dev/city/list")
    @GetMapping("/dev/city/list")
    public CommonResult<List<WineCity>> list(WineCityPageParam wineCityPageParam) {
        return CommonResult.data(wineCityService.list(wineCityPageParam));
    }

    /**
     * 添加城市
     */
    @ApiOperationSupport(order = 3)
    @Operation(summary = "添加城市")
    @CommonLog("添加城市")
    @SaCheckPermission("/dev/city/add")
    @PostMapping("/dev/city/add")
    public CommonResult<String> add(@RequestBody @Valid WineCityAddParam wineCityAddParam) {
        wineCityService.add(wineCityAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑城市
     */
    @ApiOperationSupport(order = 4)
    @Operation(summary = "编辑城市")
    @CommonLog("编辑城市")
    @SaCheckPermission("/dev/city/edit")
    @PostMapping("/dev/city/edit")
    public CommonResult<String> edit(@RequestBody @Valid WineCityEditParam wineCityEditParam) {
        wineCityService.edit(wineCityEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除城市
     */
    @ApiOperationSupport(order = 5)
    @Operation(summary = "删除城市")
    @CommonLog("删除城市")
    @SaCheckPermission("/dev/city/delete")
    @PostMapping("/dev/city/delete")
    public CommonResult<String> delete(@RequestBody @Valid WineCityIdParam wineCityIdParam) {
        wineCityService.delete(wineCityIdParam);
        return CommonResult.ok();
    }

    /**
     * 批量删除城市
     */
    @ApiOperationSupport(order = 6)
    @Operation(summary = "批量删除城市")
    @CommonLog("批量删除城市")
    @SaCheckPermission("/dev/city/batchDelete")
    @PostMapping("/dev/city/batchDelete")
    public CommonResult<String> batchDelete(@RequestBody @Valid @NotEmpty(message = "集合不能为空") List<WineCityIdParam> wineCityIdParamList) {
        wineCityService.batchDelete(wineCityIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取城市详情
     */
    @ApiOperationSupport(order = 7)
    @Operation(summary = "获取城市详情")
    @SaCheckPermission("/dev/city/detail")
    @GetMapping("/dev/city/detail")
    public CommonResult<WineCity> detail(@Valid WineCityIdParam wineCityIdParam) {
        return CommonResult.data(wineCityService.detail(wineCityIdParam));
    }

    /**
     * 禁用城市
     */
    @ApiOperationSupport(order = 8)
    @Operation(summary = "禁用城市")
    @CommonLog("禁用城市")
    @SaCheckPermission("/dev/city/disable")
    @PostMapping("/dev/city/disable")
    public CommonResult<String> disable(@RequestBody @Valid WineCityIdParam wineCityIdParam) {
        wineCityService.disable(wineCityIdParam);
        return CommonResult.ok();
    }

    /**
     * 启用城市
     */
    @ApiOperationSupport(order = 9)
    @Operation(summary = "启用城市")
    @CommonLog("启用城市")
    @SaCheckPermission("/dev/city/enable")
    @PostMapping("/dev/city/enable")
    public CommonResult<String> enable(@RequestBody @Valid WineCityIdParam wineCityIdParam) {
        wineCityService.enable(wineCityIdParam);
        return CommonResult.ok();
    }

    /**
     * 城市导出
     */
    @ApiOperationSupport(order = 10)
    @Operation(summary = "城市导出")
    @CommonLog("城市导出")
    @SaCheckPermission("/dev/city/export")
    @GetMapping("/dev/city/export")
    public void export(WineCityPageParam wineCityPageParam) {
        wineCityService.export(wineCityPageParam);
    }

    /**
     * 城市选择器
     */
    @ApiOperationSupport(order = 11)
    @Operation(summary = "城市选择器")
    @SaCheckPermission("/dev/city/selector")
    @GetMapping("/dev/city/selector")
    public CommonResult<List<WineCity>> selector(WineCityPageParam wineCityPageParam) {
        return CommonResult.data(wineCityService.selector(wineCityPageParam));
    }

    /**
     * 根据层级获取城市列表
     */
    @ApiOperationSupport(order = 12)
    @Operation(summary = "根据层级获取城市列表")
    @SaCheckPermission("/dev/city/listByLevel")
    @GetMapping("/dev/city/listByLevel")
    public CommonResult<List<WineCity>> listByLevel(@RequestParam Integer level) {
        return CommonResult.data(wineCityService.listByLevel(level));
    }

    /**
     * 根据父级编码获取子级城市列表
     */
    @ApiOperationSupport(order = 13)
    @Operation(summary = "根据父级编码获取子级城市列表")
    @SaCheckPermission("/dev/city/listByParentCode")
    @GetMapping("/dev/city/listByParentCode")
    public CommonResult<List<WineCity>> listByParentCode(@RequestParam String parentCode) {
        return CommonResult.data(wineCityService.listByParentCode(parentCode));
    }

    /**
     * 获取热门城市列表
     */
    @ApiOperationSupport(order = 14)
    @Operation(summary = "获取热门城市列表")
    @SaCheckPermission("/dev/city/listHotCities")
    @GetMapping("/dev/city/listHotCities")
    public CommonResult<List<WineCity>> listHotCities() {
        return CommonResult.data(wineCityService.listHotCities());
    }

    /**
     * 设置热门城市
     */
    @ApiOperationSupport(order = 15)
    @Operation(summary = "设置热门城市")
    @CommonLog("设置热门城市")
    @SaCheckPermission("/dev/city/setHotCity")
    @PostMapping("/dev/city/setHotCity")
    public CommonResult<String> setHotCity(@RequestBody @Valid WineCityIdParam wineCityIdParam, 
                                           @RequestParam String isHot) {
        wineCityService.setHotCity(wineCityIdParam, isHot);
        return CommonResult.ok();
    }

    /**
     * 设置配送支持
     */
    @ApiOperationSupport(order = 16)
    @Operation(summary = "设置配送支持")
    @CommonLog("设置配送支持")
    @SaCheckPermission("/dev/city/setSupportDelivery")
    @PostMapping("/dev/city/setSupportDelivery")
    public CommonResult<String> setSupportDelivery(@RequestBody @Valid WineCityIdParam wineCityIdParam, 
                                                    @RequestParam String supportDelivery) {
        wineCityService.setSupportDelivery(wineCityIdParam, supportDelivery);
        return CommonResult.ok();
    }

    /**
     * 更新城市统计数据
     */
    @ApiOperationSupport(order = 17)
    @Operation(summary = "更新城市统计数据")
    @CommonLog("更新城市统计数据")
    @SaCheckPermission("/dev/city/updateStatistics")
    @PostMapping("/dev/city/updateStatistics")
    public CommonResult<String> updateStatistics(@RequestParam String cityCode) {
        wineCityService.updateCityStatistics(cityCode);
        return CommonResult.ok();
    }

    /**
     * 根据城市名称查找城市信息（用于反显）
     */
    @ApiOperationSupport(order = 18)
    @Operation(summary = "根据城市名称查找城市信息")
    @SaCheckPermission("/dev/city/findByNames")
    @GetMapping("/dev/city/findByNames")
    public CommonResult<List<WineCity>> findByNames(@RequestParam(required = false) String provinceName,
                                                    @RequestParam(required = false) String cityName,
                                                    @RequestParam(required = false) String districtName) {
        return CommonResult.data(wineCityService.findByNames(provinceName, cityName, districtName));
    }
} 