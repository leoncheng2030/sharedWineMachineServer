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
package vip.wqs.device.modular.control.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vip.wqs.device.modular.control.param.DeviceControlParam;
import vip.wqs.device.modular.control.service.EncryptApiService;

import java.util.HashMap;
import java.util.Map;

/**
 * 第三方加密接口服务实现类
 *
 * @author AI Assistant
 * @date 2025/01/30
 */
@Slf4j
@Service
public class EncryptApiServiceImpl implements EncryptApiService {

    @Value("${wqs.device.encrypt.api.url:https://gateway.biandianxia.com/encrypt/sharedevice}")
    private String encryptApiUrl;

    @Value("${wqs.device.encrypt.api.timeout:10000}")
    private int timeout;

    @Override
    public String getEncryptCommand(DeviceControlParam param) {
        try {
            log.info("调用第三方加密接口，设备UUID：{}，订单ID：{}", param.getUuid(), param.getChargeId());

            // 1. 构建请求参数
            Map<String, Object> requestData = buildRequestData(param);

            // 2. 调用第三方接口
            String response = HttpUtil.post(encryptApiUrl, requestData, timeout);

            if (StrUtil.isBlank(response)) {
                log.error("第三方接口返回空响应");
                return null;
            }

            // 3. 解析响应
            JSONObject responseJson = JSONUtil.parseObj(response);
            String cmd = responseJson.getStr("CMD");

            if (StrUtil.isBlank(cmd)) {
                log.error("第三方接口未返回CMD指令，响应：{}", response);
                return null;
            }

            log.info("第三方加密接口调用成功，指令长度：{}", cmd.length());
            return cmd;

        } catch (Exception e) {
            log.error("调用第三方加密接口异常，设备UUID：{}，订单ID：{}", param.getUuid(), param.getChargeId(), e);
            return null;
        }
    }

    @Override
    public boolean checkApiConnectivity() {
        try {
            // 使用测试参数检查接口连通性
            Map<String, Object> testData = new HashMap<>();
            testData.put("UUID", "TEST_UUID");
            testData.put("device_id", "0");
            testData.put("charge_id", "0");
            testData.put("minute", "0");
            testData.put("second", "30");

            String response = HttpUtil.post(encryptApiUrl, testData, timeout);
            return StrUtil.isNotBlank(response);

        } catch (Exception e) {
            log.error("检查第三方接口连通性异常", e);
            return false;
        }
    }

    /**
     * 构建请求参数
     */
    private Map<String, Object> buildRequestData(DeviceControlParam param) {
        Map<String, Object> data = new HashMap<>();
        data.put("UUID", param.getUuid());
        data.put("device_id", param.getDeviceId());
        data.put("charge_id", param.getChargeId());
        data.put("minute", param.getMinute() != null ? param.getMinute() : 0);
        data.put("second", param.getSecond() != null ? param.getSecond() : 30);

        log.debug("构建第三方接口请求参数：{}", JSONUtil.toJsonStr(data));
        return data;
    }
}