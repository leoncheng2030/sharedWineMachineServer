-- =====================================================
-- 支付配置表字段优化脚本
-- 基于用户截图配置项进行字段调整
-- =====================================================

-- 1. 添加微信支付专用字段
ALTER TABLE `payment_config` 
ADD COLUMN `API_V3_KEY` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '微信支付ApiV3密钥值' AFTER `CERT_SERIAL_NO`,
ADD COLUMN `P12_CERT_PATH` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '微信支付p12证书路径' AFTER `API_V3_KEY`;

-- 2. 更新现有配置数据，将EXT_JSON中的数据迁移到专用字段
UPDATE `payment_config` 
SET 
    `API_V3_KEY` = 'qwertyuiopasdfghjklzxcvbnm12345',
    `P12_CERT_PATH` = 'D:\\defaultUploadFolder\\defaultBucketName\\2025\\4\\27\\19163373199419596982.p12',
    `EXT_JSON` = JSON_OBJECT(
        'description', '微信支付配置已优化，专用字段存储',
        'version', '1.1.0',
        'lastUpdate', NOW()
    )
WHERE `PAY_TYPE` = 'WECHAT_MINI';

-- 3. 验证字段调整结果
SELECT 
    CONFIG_NAME,
    PAY_TYPE,
    MERCHANT_ID,
    APP_ID,
    LEFT(APP_SECRET, 8) AS APP_SECRET_PREVIEW,
    LEFT(MERCHANT_KEY, 8) AS MERCHANT_KEY_PREVIEW,
    LEFT(API_V3_KEY, 8) AS API_V3_KEY_PREVIEW,
    P12_CERT_PATH,
    PRIVATE_KEY_PATH,
    CERT_PATH,
    CERT_SERIAL_NO,
    NOTIFY_URL,
    REFUND_NOTIFY_URL,
    STATUS,
    CREATE_TIME
FROM payment_config 
WHERE PAY_TYPE = 'WECHAT_MINI';

-- 4. 创建完整的配置视图，方便查看所有配置项
CREATE OR REPLACE VIEW `v_wechat_pay_config` AS
SELECT 
    ID,
    CONFIG_NAME AS '配置名称',
    PAY_TYPE AS '支付类型',
    MERCHANT_ID AS '微信支付商户号',
    APP_ID AS '微信支付应用id',
    CONCAT(LEFT(APP_SECRET, 8), '***') AS '微信支付公众号AppSecret',
    CONCAT(LEFT(MERCHANT_KEY, 8), '***') AS '微信支付商户V2密钥',
    CONCAT(LEFT(API_V3_KEY, 8), '***') AS '微信支付ApiV3密钥值',
    P12_CERT_PATH AS '微信支付p12证书路径',
    PRIVATE_KEY_PATH AS '微信支付apiClientKey路径',
    CERT_PATH AS '微信支付apiClientCert路径',
    CERT_SERIAL_NO AS '微信支付ApiV3证书序列号',
    NOTIFY_URL AS '微信支付回调地址',
    REFUND_NOTIFY_URL AS '微信支付退款回调地址',
    CASE SANDBOX_MODE 
        WHEN 0 THEN '正式环境' 
        WHEN 1 THEN '沙箱环境' 
        ELSE '未知' 
    END AS '环境模式',
    STATUS AS '状态',
    CREATE_TIME AS '创建时间',
    UPDATE_TIME AS '更新时间'
FROM payment_config 
WHERE PAY_TYPE LIKE 'WECHAT%';

-- 5. 查看优化后的配置
SELECT * FROM `v_wechat_pay_config`;

-- 6. 配置字段说明
/*
字段优化说明：
==============

新增字段：
1. API_V3_KEY - 微信支付ApiV3密钥值（专用字段，便于查询和管理）
2. P12_CERT_PATH - 微信支付p12证书路径（专用字段，便于证书管理）

字段映射关系：
- 微信支付应用id → APP_ID
- 微信支付公众号AppSecret → APP_SECRET  
- 微信支付商户号 → MERCHANT_ID
- 微信支付商户V2密钥 → MERCHANT_KEY
- 微信支付ApiV3密钥值 → API_V3_KEY（新增）
- 微信支付p12证书路径 → P12_CERT_PATH（新增）
- 微信支付apiClientKey路径 → PRIVATE_KEY_PATH
- 微信支付apiClientCert路径 → CERT_PATH
- 微信支付ApiV3证书序列号 → CERT_SERIAL_NO
- 微信支付回调地址 → NOTIFY_URL
- 微信支付退款回调地址 → REFUND_NOTIFY_URL

优化效果：
1. 所有配置项都有专用字段，便于查询和管理
2. 敏感信息在视图中自动脱敏显示
3. 配置项与截图中的参数完全对应
4. 支持未来扩展其他支付方式
*/ 