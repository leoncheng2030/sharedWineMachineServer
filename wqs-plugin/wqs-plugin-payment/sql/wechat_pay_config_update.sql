-- =====================================================
-- 微信支付配置更新脚本 - 基于正确的参数配置
-- =====================================================

-- 1. 删除旧的配置（如果存在）
DELETE FROM payment_config WHERE PAY_TYPE = 'WECHAT_MINI';

-- 2. 插入微信小程序支付配置
INSERT INTO `payment_config` (
    `ID`, `CONFIG_NAME`, `PAY_TYPE`, 
    `MERCHANT_ID`, `APP_ID`, `APP_SECRET`, `MERCHANT_KEY`,
    `PRIVATE_KEY_PATH`, `CERT_PATH`, `CERT_SERIAL_NO`,
    `NOTIFY_URL`, `REFUND_NOTIFY_URL`, `RETURN_URL`,
    `SANDBOX_MODE`, `STATUS`, `SORT_CODE`, `EXT_JSON`, `REMARK`,
    `DELETE_FLAG`, `CREATE_TIME`, `CREATE_USER`, `UPDATE_TIME`, `UPDATE_USER`
) VALUES (
    'PAY_WX_MINI_001', 
    '微信小程序支付配置', 
    'WECHAT_MINI',
    
    -- 商户信息
    '1372536002',                                           -- 微信支付商户号
    'wx496f8c6f04580ab0',                                  -- 微信支付应用id
    '5d502dd141b4bd9b9be0b0050a796c6f',                    -- 微信支付公众号AppSecret
    'qwertyuiopasdfghjklzxcvbnm12345',                     -- 微信支付商户V2密钥
    
    -- 证书信息
    'D:\\defaultUploadFolder\\defaultBucketName\\2025\\4\\27\\19163373473181818890.pem',  -- 微信支付apiClientKey路径
    'D:\\defaultUploadFolder\\defaultBucketName\\2025\\4\\27\\19163373453813376601.pem',  -- 微信支付apiClientCert路径
    '50DFE2641EC288765083C05B21B262DCCA7A8D50',           -- 微信支付ApiV3证书序列号
    
    -- 回调URL
    'http://test.weiqisheng.cn/pay/wx/notifyUrl',          -- 微信支付回调地址
    'http://test.weiqisheng.cn/pay/wx/refundNotifyUrl',    -- 微信支付退款回调地址
    'http://test.weiqisheng.cn/payment/success',           -- 支付成功跳转地址
    
    -- 其他配置
    0,                                                      -- 沙箱模式：0-正式环境
    'ENABLE',                                              -- 状态：启用
    100,                                                   -- 排序码
    JSON_OBJECT(
        'apiV3Key', 'qwertyuiopasdfghjklzxcvbnm12345',     -- 微信支付ApiV3密钥值
        'certSerialNo', '50DFE2641EC288765083C05B21B262DCCA7A8D50',  -- 证书序列号
        'p12CertPath', 'D:\\defaultUploadFolder\\defaultBucketName\\2025\\4\\27\\19163373199419596982.p12'  -- 微信支付p12证书路径
    ),
    '微信小程序支付正式环境配置 - 已配置正确参数',
    
    -- 通用字段
    'NOT_DELETE', 
    NOW(), 
    'SYSTEM', 
    NOW(), 
    'SYSTEM'
);

-- 3. 验证配置是否插入成功
SELECT 
    CONFIG_NAME,
    PAY_TYPE,
    MERCHANT_ID,
    APP_ID,
    LEFT(APP_SECRET, 8) AS APP_SECRET_PREVIEW,
    LEFT(MERCHANT_KEY, 8) AS MERCHANT_KEY_PREVIEW,
    NOTIFY_URL,
    STATUS,
    SANDBOX_MODE,
    CREATE_TIME
FROM payment_config 
WHERE PAY_TYPE = 'WECHAT_MINI';

-- 4. 配置说明注释
/*
微信支付配置说明：
=================

1. 商户信息：
   - 商户号：1372536002
   - 应用ID：wx496f8c6f04580ab0
   - 应用密钥：5d502dd141b4bd9b9be0b0050a796c6f
   - 商户密钥：qwertyuiopasdfghjklzxcvbnm12345

2. 证书信息：
   - API私钥文件：19163373473181818890.pem
   - API证书文件：19163373453813376601.pem
   - P12证书文件：19163373199419596982.p12
   - 证书序列号：50DFE2641EC288765083C05B21B262DCCA7A8D50

3. 回调地址：
   - 支付回调：http://test.weiqisheng.cn/pay/wx/notifyUrl
   - 退款回调：http://test.weiqisheng.cn/pay/wx/refundNotifyUrl

4. 环境配置：
   - 当前为正式环境配置（SANDBOX_MODE = 0）
   - 如需测试环境，请修改为 SANDBOX_MODE = 1

使用说明：
=========
1. 执行此脚本前，请确保证书文件已正确上传到指定路径
2. 回调地址需要能够被微信服务器访问
3. 生产环境部署时，请检查所有路径和域名是否正确
*/ 