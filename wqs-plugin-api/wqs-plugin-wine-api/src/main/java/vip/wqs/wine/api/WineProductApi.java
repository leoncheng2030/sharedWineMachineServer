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
package vip.wqs.wine.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import vip.wqs.wine.pojo.WineProductPojo;
import vip.wqs.wine.pojo.WineProductQueryPojo;
import vip.wqs.wine.pojo.WineProductSimplePojo;

/**
 * 酒品信息Api
 *
 * @author WQS_TEAM
 * @date 2025/1/27 酒品管理模块开发
 **/
public interface WineProductApi {

    /**
     * 分页查询酒品列表
     *
     * @param queryPojo 查询条件
     * @return 酒品分页列表
     * @author WQS_TEAM
     * @date 2025/1/30
     */
    Page<WineProductSimplePojo> getWineProductPage(WineProductQueryPojo queryPojo);

    /**
     * 获取酒品详细信息（显示平台统一价格和分类信息）
     * 用于首页酒品详情查看，显示平台设定的统一价格
     *
     * @param productId 酒品ID
     * @return 酒品详细信息（包含平台统一价格）
     * @author WQS_TEAM
     * @date 2025/1/30
     */
    WineProductPojo getWineProductDetail(String productId);
} 