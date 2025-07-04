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
package vip.wqs.dev.modular.slideshow.provider;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vip.wqs.dev.api.DevSlideshowApi;
import vip.wqs.dev.core.conts.DevConstants;
import vip.wqs.dev.modular.slideshow.entity.DevSlideshow;
import vip.wqs.dev.modular.slideshow.enums.DevSlideshowStatusEnum;
import vip.wqs.dev.modular.slideshow.service.DevSlideshowService;
import vip.wqs.dev.pojo.DevSlideshowPojo;
import vip.wqs.dev.pojo.DevSlideshowQueryPojo;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 轮播图API接口实现类
 *
 * @author yubaoshan
 * @date  2024/07/13 00:31
 */
@Slf4j
@Service
public class DevSlideshowApiProvider implements DevSlideshowApi {

    @Resource
    private DevSlideshowService devSlideshowService;

    @Override
    public List<DevSlideshowPojo> getListByPlace(String place) {
        try {
            // 构建查询条件
            LambdaQueryWrapper<DevSlideshow> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(DevSlideshow::getStatus, DevSlideshowStatusEnum.ENABLE.getValue());
            
            // 如果指定了位置，按位置过滤
            if (StrUtil.isNotBlank(place)) {
                queryWrapper.like(DevSlideshow::getPlace, place);
            }
            
            queryWrapper.orderByAsc(DevSlideshow::getSortCode);
            
            List<DevSlideshow> slideshowList = devSlideshowService.list(queryWrapper);
            
            // 转换为POJO并处理link字段
            List<DevSlideshowPojo> resultList = slideshowList.stream()
                    .map(this::convertToPojoWithLink)
                    .collect(Collectors.toList());
            
            // 如果没有数据，返回默认轮播图
            if (resultList.isEmpty()) {
                DevSlideshowPojo defaultPojo = createDefaultSlideshow(place);
                resultList.add(defaultPojo);
            }
            
            return resultList;
        } catch (Exception e) {
            log.error("获取轮播图列表失败，place: {}", place, e);
            throw new RuntimeException("获取轮播图列表失败", e);
        }
    }

    @Override
    public List<DevSlideshowPojo> getList(DevSlideshowQueryPojo queryPojo) {
        try {
            LambdaQueryWrapper<DevSlideshow> queryWrapper = new LambdaQueryWrapper<>();
            
            // 默认只查询启用状态的轮播图
            String status = ObjectUtil.isNotEmpty(queryPojo.getStatus()) ? 
                    queryPojo.getStatus() : DevSlideshowStatusEnum.ENABLE.getValue();
            queryWrapper.eq(DevSlideshow::getStatus, status);
            
            // 如果指定了位置，则按位置过滤
            if (ObjectUtil.isNotEmpty(queryPojo.getPlace())) {
                queryWrapper.like(DevSlideshow::getPlace, queryPojo.getPlace());
            }
            
            queryWrapper.orderByAsc(DevSlideshow::getSortCode);
            
            List<DevSlideshow> slideshowList = devSlideshowService.list(queryWrapper);
            
            return slideshowList.stream()
                    .map(this::convertToPojoWithLink)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("获取轮播图列表失败，查询条件: {}", queryPojo, e);
            throw new RuntimeException("获取轮播图列表失败", e);
        }
    }

    /**
     * 转换实体为POJO并处理link字段
     */
    private DevSlideshowPojo convertToPojoWithLink(DevSlideshow slideshow) {
        DevSlideshowPojo pojo = BeanUtil.toBean(slideshow, DevSlideshowPojo.class);
        
        // 从pathDetails中提取link
        String link = extractLinkFromPathDetails(slideshow.getPathDetails());
        pojo.setLink(link);
        
        return pojo;
    }

    /**
     * 创建默认轮播图
     */
    private DevSlideshowPojo createDefaultSlideshow(String place) {
        DevSlideshowPojo pojo = new DevSlideshowPojo();
        pojo.setId(IdWorker.getIdStr());
        pojo.setTitle("默认轮播图");
        pojo.setImage(DevConstants.STATIC_SLIDESHOW_IMAGE);
        pojo.setPlace(place);
        pojo.setStatus(DevSlideshowStatusEnum.ENABLE.getValue());
        pojo.setSortCode(1);
        pojo.setLink(null);
        return pojo;
    }

    /**
     * 从路径详情中提取链接
     * @param pathDetails 路径详情JSON字符串
     * @return 提取的链接
     */
    private String extractLinkFromPathDetails(String pathDetails) {
        if (StrUtil.isBlank(pathDetails)) {
            return null;
        }
        
        try {
            JSONObject pathObj = JSONUtil.parseObj(pathDetails);
            return pathObj.getStr("url");
        } catch (Exception e) {
            log.warn("解析路径详情失败: {}", pathDetails, e);
            return null;
        }
    }
}
