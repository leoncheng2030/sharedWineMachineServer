package vip.wqs.auth.core.config;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import cn.hutool.core.util.StrUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vip.wqs.common.exception.CommonException;
import vip.wqs.dev.api.DevConfigApi;

/**
 * 微信小程序服务配置
 *
 * @author wqs
 * @date 2025/01/29
 */
@Slf4j
@Configuration
public class WxMaServiceConfigure {
    
    /** 微信小程序AppID配置键 */
    private static final String WQS_WECHAT_MINI_APP_ID = "WQS_WECHAT_MINI_APP_ID";
    
    /** 微信小程序AppSecret配置键 */
    private static final String WQS_WECHAT_MINI_APP_SECRET = "WQS_WECHAT_MINI_APP_SECRET";
    
    @Resource
    private DevConfigApi devConfigApi;
    
    /**
     * 微信小程序服务Bean
     *
     * @return WxMaService
     * @author wqs
     * @date 2025/01/29
     */
    @Bean
    public WxMaService wxMaService() {
        try {
            // 获取配置信息
            String appId = devConfigApi.getValueByKey(WQS_WECHAT_MINI_APP_ID);
            String appSecret = devConfigApi.getValueByKey(WQS_WECHAT_MINI_APP_SECRET);
            
            // 验证配置信息
            if (StrUtil.isBlank(appId)) {
                log.warn("微信小程序AppID未配置，微信登录功能将不可用");
                return null;
            }
            
            if (StrUtil.isBlank(appSecret)) {
                log.warn("微信小程序AppSecret未配置，微信登录功能将不可用");
                return null;
            }
            
            // 创建配置对象
            WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
            config.setAppid(appId);
            config.setSecret(appSecret);
            
            // 创建服务实例
            WxMaService maService = new WxMaServiceImpl();
            maService.setWxMaConfig(config);
            
            log.info("微信小程序服务初始化成功，AppID: {}", appId);
            return maService;
            
        } catch (Exception e) {
            log.error("微信小程序服务初始化失败", e);
            throw new CommonException("微信小程序服务初始化失败: {}", e.getMessage());
        }
    }
}
