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
package vip.wqs.wine.modular.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import vip.wqs.wine.modular.product.entity.WineProduct;

/**
 * 酒品Mapper接口
 * 
 * 说明：按照项目规范，所有查询都使用QueryWrapper方式，
 * 不在Mapper中定义自定义查询方法，保持简洁性
 *
 * @author wqs
 * @date 2025/1/27
 */
public interface WineProductMapper extends BaseMapper<WineProduct> {
    
    // 继承BaseMapper，提供基础的CRUD操作
    // 所有复杂查询都在Service层使用QueryWrapper实现
    
} 