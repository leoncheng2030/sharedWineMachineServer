-- =====================================================
-- 支付配置表JSON设计优化方案
-- 将所有支付相关配置存储在JSON字段中，提高扩展性
-- =====================================================

-- 1. 回滚之前的字段添加（如果已执行）
ALTER TABLE `payment_config` 
DROP COLUMN IF EXISTS `API_V3_KEY`,
DROP COLUMN IF EXISTS `P12_CERT_PATH`;

-- 2. 简化表结构，只保留核心字段
-- 保留的字段：
-- - 基本信息：ID, CONFIG_NAME, PAY_TYPE, STATUS, SORT_CODE
-- - JSON配置：CONFIG_JSON (存储所有支付相关配置)
-- - 通用字段：CREATE_TIME, UPDATE_TIME 等

-- 3. 重新设计支付配置数据结构
DELETE FROM `payment_config` WHERE PAY_TYPE = 'WECHAT_MINI';

-- 4. 插入基于JSON的微信支付配置
INSERT INTO `payment_config` (
    `ID`, `CONFIG_NAME`, `PAY_TYPE`, 
    `STATUS`, `SORT_CODE`, `EXT_JSON`, `REMARK`,
    `DELETE_FLAG`, `CREATE_TIME`, `CREATE_USER`, `UPDATE_TIME`, `UPDATE_USER`
) VALUES (
    'PAY_WX_MINI_001', 
    '微信小程序支付配置', 
    'WECHAT_MINI',
    'ENABLE', 
    100, 
    JSON_OBJECT(
        -- 基础配置
        'appId', 'wx496f8c6f04580ab0',
        'appSecret', '5d502dd141b4bd9b9be0b0050a796c6f',
        'merchantId', '1372536002',
        'merchantKey', 'qwertyuiopasdfghjklzxcvbnm12345',
        
        -- API V3配置
        'apiV3Key', 'qwertyuiopasdfghjklzxcvbnm12345',
        'certSerialNo', '50DFE2641EC288765083C05B21B262DCCA7A8D50',
        
        -- 证书文件路径
        'privateKeyPath', 'D:\\defaultUploadFolder\\defaultBucketName\\2025\\4\\27\\19163373473181818890.pem',
        'certPath', 'D:\\defaultUploadFolder\\defaultBucketName\\2025\\4\\27\\19163373453813376601.pem',
        'p12CertPath', 'D:\\defaultUploadFolder\\defaultBucketName\\2025\\4\\27\\19163373199419596982.p12',
        
        -- 回调地址
        'notifyUrl', 'http://test.weiqisheng.cn/pay/wx/notifyUrl',
        'refundNotifyUrl', 'http://test.weiqisheng.cn/pay/wx/refundNotifyUrl',
        'returnUrl', 'http://test.weiqisheng.cn/payment/success',
        
        -- 环境配置
        'sandboxMode', 0,
        'environment', 'production'
    ),
    '微信小程序支付配置 - JSON存储方案',
    'NOT_DELETE', 
    NOW(), 
    'SYSTEM', 
    NOW(), 
    'SYSTEM'
);

-- 5. 支付宝配置示例（演示扩展性）
INSERT INTO `payment_config` (
    `ID`, `CONFIG_NAME`, `PAY_TYPE`, 
    `STATUS`, `SORT_CODE`, `EXT_JSON`, `REMARK`,
    `DELETE_FLAG`, `CREATE_TIME`, `CREATE_USER`, `UPDATE_TIME`, `UPDATE_USER`
) VALUES (
    'PAY_ALIPAY_001', 
    '支付宝支付配置', 
    'ALIPAY',
    'DISABLE', 
    200, 
    JSON_OBJECT(
        -- 支付宝基础配置
        'appId', 'your_alipay_app_id',
        'merchantId', 'your_alipay_merchant_id',
        'appPrivateKey', 'your_alipay_private_key',
        'alipayPublicKey', 'alipay_public_key',
        'appCertPath', '/path/to/appCertPublicKey.crt',
        'alipayCertPath', '/path/to/alipayCertPublicKey_RSA2.crt',
        'alipayRootCertPath', '/path/to/alipayRootCert.crt',
        
        -- 回调地址
        'notifyUrl', 'http://test.weiqisheng.cn/pay/alipay/notifyUrl',
        'returnUrl', 'http://test.weiqisheng.cn/pay/alipay/returnUrl',
        
        -- 环境配置
        'sandboxMode', 1,
        'environment', 'sandbox',
        'signType', 'RSA2',
        'charset', 'UTF-8',
        'format', 'JSON'
    ),
    '支付宝支付配置 - JSON存储方案',
    'NOT_DELETE', 
    NOW(), 
    'SYSTEM', 
    NOW(), 
    'SYSTEM'
);

-- 6. 创建配置查询视图
CREATE OR REPLACE VIEW `v_payment_config_detail` AS
SELECT 
    ID,
    CONFIG_NAME AS '配置名称',
    PAY_TYPE AS '支付类型',
    CASE STATUS 
        WHEN 'ENABLE' THEN '启用' 
        WHEN 'DISABLE' THEN '禁用' 
        ELSE '未知' 
    END AS '状态',
    SORT_CODE AS '排序',
    JSON_PRETTY(EXT_JSON) AS '配置详情',
    REMARK AS '备注',
    CREATE_TIME AS '创建时间',
    UPDATE_TIME AS '更新时间'
FROM payment_config 
ORDER BY SORT_CODE ASC;

-- 7. 查看配置结果
SELECT * FROM `v_payment_config_detail`;

-- 8. JSON配置结构说明
/*
JSON配置字段设计说明：
=====================

通用字段（所有支付方式）：
- notifyUrl: 支付回调地址
- refundNotifyUrl: 退款回调地址  
- returnUrl: 支付成功跳转地址
- sandboxMode: 沙箱模式 (0-正式, 1-沙箱)
- environment: 环境标识

微信支付专用字段：
- appId: 微信应用ID
- appSecret: 微信应用密钥
- merchantId: 微信商户号
- merchantKey: 微信商户密钥
- apiV3Key: 微信API V3密钥
- certSerialNo: 证书序列号
- privateKeyPath: 私钥文件路径
- certPath: 证书文件路径
- p12CertPath: P12证书路径

支付宝专用字段：
- appId: 支付宝应用ID
- merchantId: 支付宝商户号
- appPrivateKey: 应用私钥
- alipayPublicKey: 支付宝公钥
- appCertPath: 应用证书路径
- alipayCertPath: 支付宝证书路径
- alipayRootCertPath: 根证书路径
- signType: 签名类型
- charset: 字符集
- format: 数据格式

优势：
1. 高度灵活，支持任意支付方式扩展
2. 配置项可以根据支付方式动态调整
3. 前端可以根据支付类型显示不同的配置表单
4. 数据库表结构简洁，维护成本低
5. 支持复杂的嵌套配置结构
*/ 