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
package vip.wqs.miniprogram.modular;

import cn.hutool.core.util.ObjectUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vip.wqs.common.annotation.CommonLog;
import vip.wqs.common.pojo.CommonResult;
import vip.wqs.dev.api.DevSlideshowApi;
import vip.wqs.dev.pojo.DevSlideshowPojo;
import vip.wqs.dev.pojo.DevSlideshowQueryPojo;

import java.util.List;

/**
 * 小程序轮播图控制器
 *
 * @author yubaoshan
 * @date 2024/07/13 00:31
 */
@Tag(name = "小程序轮播图控制器")
@ApiSupport(author = "yubaoshan", order = 1)
@RestController
@Validated
@Slf4j
public class MiniSlideshowController {

    @Resource
    private DevSlideshowApi devSlideshowApi;

    /**
     * 获取轮播图列表
     *
     * @param place 展示位置
     * @return 轮播图列表
     * @author yubaoshan
     * @date 2024/07/13 00:31
     */
    @Operation(summary = "获取轮播图列表", description = "根据展示位置获取轮播图列表，无需登录，公开访问")
    @ApiOperationSupport(order = 1)
    @CommonLog("获取轮播图列表")
    @GetMapping("/mini/slideshow/list")
    public CommonResult<List<DevSlideshowPojo>> getList(@RequestParam(required = false) String place) {
        try {
            List<DevSlideshowPojo> resultList;
            
            if (ObjectUtil.isNotEmpty(place)) {
                // 根据位置获取轮播图
                resultList = devSlideshowApi.getListByPlace(place);
            } else {
                // 获取所有启用的轮播图
                DevSlideshowQueryPojo queryPojo = new DevSlideshowQueryPojo();
                // 注意：直接设置status字段
                queryPojo.setStatus("ENABLE");
                resultList = devSlideshowApi.getList(queryPojo);
            }
            
            return CommonResult.data(resultList);
        } catch (Exception e) {
            return CommonResult.error("获取轮播图列表失败：" + e.getMessage());
        }
    }

    /**
     * 根据位置获取轮播图列表
     *
     * @param place 展示位置
     * @return 轮播图列表
     * @author yubaoshan
     * @date 2024/07/13 00:31
     */
    @Operation(summary = "根据位置获取轮播图列表", description = "根据指定位置获取轮播图列表，无需登录，公开访问")
    @ApiOperationSupport(order = 2)
    @CommonLog("根据位置获取轮播图列表")
    @GetMapping("/mini/slideshow/listByPlace")
    public CommonResult<List<DevSlideshowPojo>> getListByPlace(@RequestParam String place) {
        try {
            List<DevSlideshowPojo> resultList = devSlideshowApi.getListByPlace(place);
            return CommonResult.data(resultList);
        } catch (Exception e) {
            return CommonResult.error("根据位置获取轮播图列表失败：" + e.getMessage());
        }
    }
} 