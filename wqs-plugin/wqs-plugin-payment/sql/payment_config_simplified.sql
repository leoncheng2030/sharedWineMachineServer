-- =====================================================
-- 支付配置表简化设计 - 只保留核心公共字段
-- 所有支付相关配置存储在JSON字段中
-- =====================================================

-- 1. 删除不必要的字段（如果存在）
ALTER TABLE `payment_config` 
DROP COLUMN IF EXISTS `MERCHANT_ID`,
DROP COLUMN IF EXISTS `APP_ID`,
DROP COLUMN IF EXISTS `APP_SECRET`,
DROP COLUMN IF EXISTS `MERCHANT_KEY`,
DROP COLUMN IF EXISTS `PRIVATE_KEY_PATH`,
DROP COLUMN IF EXISTS `CERT_PATH`,
DROP COLUMN IF EXISTS `CERT_SERIAL_NO`,
DROP COLUMN IF EXISTS `NOTIFY_URL`,
DROP COLUMN IF EXISTS `REFUND_NOTIFY_URL`,
DROP COLUMN IF EXISTS `RETURN_URL`,
DROP COLUMN IF EXISTS `SANDBOX_MODE`,
DROP COLUMN IF EXISTS `API_V3_KEY`,
DROP COLUMN IF EXISTS `P12_CERT_PATH`;

-- 2. 查看简化后的表结构
DESCRIBE `payment_config`;

-- 3. 最终表结构应该只包含以下字段：
/*
核心字段（保留）：
- ID: 主键
- CONFIG_NAME: 配置名称
- PAY_TYPE: 支付类型（WECHAT_MINI, WECHAT_H5, ALIPAY, etc）
- STATUS: 状态（ENABLE/DISABLE）
- SORT_CODE: 排序码
- EXT_JSON: 扩展配置JSON（存储所有支付相关配置）
- REMARK: 备注
- DELETE_FLAG: 删除标记
- CREATE_TIME: 创建时间
- CREATE_USER: 创建用户
- UPDATE_TIME: 更新时间
- UPDATE_USER: 更新用户

移入JSON的字段：
- merchantId: 商户号
- appId: 应用ID
- appSecret: 应用密钥
- merchantKey: 商户密钥
- privateKeyPath: 私钥文件路径
- certPath: 证书文件路径
- certSerialNo: 证书序列号
- apiV3Key: API V3密钥
- p12CertPath: P12证书路径
- notifyUrl: 支付回调URL
- refundNotifyUrl: 退款回调URL
- returnUrl: 支付成功跳转URL
- sandboxMode: 沙箱模式
- environment: 环境标识
- 以及其他支付方式特有的配置项
*/

-- 4. 重新插入基于JSON的配置数据
DELETE FROM `payment_config` WHERE PAY_TYPE IN ('WECHAT_MINI', 'ALIPAY');

-- 5. 插入微信小程序支付配置
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
    '微信小程序支付配置 - 简化JSON存储方案',
    'NOT_DELETE', 
    NOW(), 
    'SYSTEM', 
    NOW(), 
    'SYSTEM'
);

-- 6. 插入支付宝配置示例
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
    '支付宝支付配置 - 简化JSON存储方案',
    'NOT_DELETE', 
    NOW(), 
    'SYSTEM', 
    NOW(), 
    'SYSTEM'
);

-- 7. 查看最终配置结果
SELECT 
    ID,
    CONFIG_NAME,
    PAY_TYPE,
    STATUS,
    SORT_CODE,
    JSON_PRETTY(EXT_JSON) AS '配置详情',
    REMARK,
    CREATE_TIME
FROM payment_config 
WHERE DELETE_FLAG = 'NOT_DELETE'
ORDER BY SORT_CODE ASC;

-- 8. 验证JSON配置查询
-- 查询微信支付的appId
SELECT 
    CONFIG_NAME,
    JSON_EXTRACT(EXT_JSON, '$.appId') AS 'AppID',
    JSON_EXTRACT(EXT_JSON, '$.merchantId') AS '商户号',
    JSON_EXTRACT(EXT_JSON, '$.environment') AS '环境'
FROM payment_config 
WHERE PAY_TYPE = 'WECHAT_MINI' AND DELETE_FLAG = 'NOT_DELETE';

-- 9. 配置优势总结
/*
简化后的设计优势：
=====================

1. 表结构极简：
   - 只保留7个核心业务字段
   - 所有支付相关配置存储在EXT_JSON中
   - 表结构稳定，无需频繁修改

2. 高度灵活：
   - 支持任意支付方式扩展
   - 配置项可以根据需要动态调整
   - JSON查询和更新功能强大

3. 维护简单：
   - 减少了20+个字段的维护成本
   - 统一的配置存储和查询方式
   - 前端表单可以完全基于JSON动态生成

4. 扩展性强：
   - 新增支付方式只需要插入新记录
   - 配置项可以无限扩展
   - 支持复杂的嵌套配置结构

5. 性能优化：
   - 减少JOIN查询的复杂度
   - JSON索引支持高效查询
   - 配置缓存更加简单
*/ 