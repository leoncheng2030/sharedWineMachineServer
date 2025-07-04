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
package vip.wqs.device.modular.control.service;

import vip.wqs.device.modular.control.param.DeviceControlParam;

/**
 * 第三方加密接口服务
 *
 * @author AI Assistant
 * @date 2025/01/30
 */
public interface EncryptApiService {

    /**
     * 调用第三方接口获取加密控制指令
     *
     * @param param 控制参数
     * @return 加密指令
     */
    String getEncryptCommand(DeviceControlParam param);

    /**
     * 验证第三方接口连通性
     *
     * @return 是否连通
     */
    boolean checkApiConnectivity();
}