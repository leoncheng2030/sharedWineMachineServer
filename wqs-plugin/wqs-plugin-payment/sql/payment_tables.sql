-- =====================================================
-- 支付管理插件数据库表结构
-- =====================================================

-- 支付记录表
CREATE TABLE `payment_record` (
    `ID` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键',
    
    -- 基本信息
    `PAYMENT_NO` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '支付单号（系统生成）',
    `ORDER_ID` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '关联订单ID',
    `ORDER_NO` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单号',
    `USER_ID` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户ID',
    
    -- 支付信息
    `PAY_TYPE` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '支付方式',
    `PAY_AMOUNT` decimal(10,2) NOT NULL COMMENT '支付金额',
    `ACTUAL_AMOUNT` decimal(10,2) DEFAULT NULL COMMENT '实际支付金额',
    `PAY_STATUS` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'PENDING' COMMENT '支付状态',
    `PAY_TIME` datetime DEFAULT NULL COMMENT '支付时间',
    
    -- 第三方支付信息
    `TRANSACTION_ID` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '第三方交易流水号',
    `PREPAY_ID` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '微信支付预支付交易会话标识',
    `CALLBACK_TIME` datetime DEFAULT NULL COMMENT '支付回调时间',
    `CALLBACK_CONTENT` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '支付回调内容',
    
    -- 退款信息
    `REFUND_AMOUNT` decimal(10,2) DEFAULT NULL COMMENT '退款金额',
    `REFUND_TIME` datetime DEFAULT NULL COMMENT '退款时间',
    `REFUND_NO` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '退款单号',
    `REFUND_REASON` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '退款原因',
    
    -- 其他信息
    `FAIL_REASON` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '失败原因',
    `EXPIRE_TIME` datetime DEFAULT NULL COMMENT '过期时间',
    `CLIENT_IP` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '客户端IP',
    `DEVICE_INFO` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '设备信息',
    `BODY` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '商品描述',
    `ATTACH` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '附加数据',
    `EXT_JSON` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '扩展信息',
    `REMARK` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
    
    -- 通用字段
    `DELETE_FLAG` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT 'NOT_DELETE' COMMENT '删除标志',
    `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
    `CREATE_USER` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建用户',
    `UPDATE_TIME` datetime DEFAULT NULL COMMENT '更新时间',
    `UPDATE_USER` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '更新用户',
    
    PRIMARY KEY (`ID`) USING BTREE,
    UNIQUE KEY `uk_payment_no` (`PAYMENT_NO`) USING BTREE,
    KEY `idx_order_id` (`ORDER_ID`) USING BTREE,
    KEY `idx_order_no` (`ORDER_NO`) USING BTREE,
    KEY `idx_user_id` (`USER_ID`) USING BTREE,
    KEY `idx_pay_status` (`PAY_STATUS`) USING BTREE,
    KEY `idx_transaction_id` (`TRANSACTION_ID`) USING BTREE,
    KEY `idx_create_time` (`CREATE_TIME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '支付记录表' ROW_FORMAT = Dynamic;

-- 支付配置表
CREATE TABLE `payment_config` (
    `ID` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键',
    
    -- 基本信息
    `CONFIG_NAME` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '配置名称',
    `PAY_TYPE` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '支付方式',
    
    -- 商户信息
    `MERCHANT_ID` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '商户号',
    `APP_ID` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '应用ID',
    `APP_SECRET` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '应用密钥',
    `MERCHANT_KEY` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '商户密钥',
    
    -- 证书信息
    `PRIVATE_KEY_PATH` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '私钥文件路径',
    `CERT_PATH` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '证书文件路径',
    `CERT_SERIAL_NO` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '证书序列号',
    
    -- 回调URL
    `NOTIFY_URL` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '支付回调URL',
    `REFUND_NOTIFY_URL` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '退款回调URL',
    `RETURN_URL` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '支付成功跳转URL',
    
    -- 其他配置
    `SANDBOX_MODE` int DEFAULT 0 COMMENT '沙箱模式：0-正式环境，1-沙箱环境',
    `STATUS` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT 'ENABLE' COMMENT '状态',
    `SORT_CODE` int DEFAULT NULL COMMENT '排序码',
    `EXT_JSON` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '扩展配置JSON',
    `REMARK` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
    
    -- 通用字段
    `DELETE_FLAG` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT 'NOT_DELETE' COMMENT '删除标志',
    `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
    `CREATE_USER` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建用户',
    `UPDATE_TIME` datetime DEFAULT NULL COMMENT '更新时间',
    `UPDATE_USER` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '更新用户',
    
    PRIMARY KEY (`ID`) USING BTREE,
    UNIQUE KEY `uk_pay_type` (`PAY_TYPE`) USING BTREE,
    KEY `idx_config_name` (`CONFIG_NAME`) USING BTREE,
    KEY `idx_status` (`STATUS`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '支付配置表' ROW_FORMAT = Dynamic;

-- 插入默认支付配置数据
INSERT INTO `payment_config` VALUES 
('WECHAT_MINI_CONFIG', '微信小程序支付配置', 'WECHAT_MINI', 
 '1372536002', 'wx496f8c6f04580ab0', '5d502dd141b4bd9b9be0b0050a796c6f', 'qwertyuiopasdfghjklzxcvbnm12345',
 'D:\\defaultUploadFolder\\defaultBucketName\\2025\\4\\27\\19163373473181818890.pem', 'D:\\defaultUploadFolder\\defaultBucketName\\2025\\4\\27\\19163373453813376601.pem', '50DFE2641EC288765083C05B21B262DCCA7A8D50',
 'http://test.weiqisheng.cn/pay/wx/notifyUrl', 'http://test.weiqisheng.cn/pay/wx/refundNotifyUrl', 'http://test.weiqisheng.cn/payment/success',
 1, 'ENABLE', 100, '{"apiV3Key":"qwertyuiopasdfghjklzxcvbnm12345","certSerialNo":"50DFE2641EC288765083C05B21B262DCCA7A8D50"}', '微信小程序支付正式配置',
 'NOT_DELETE', NOW(), 'SYSTEM', NOW(), 'SYSTEM');

-- 修复订单表中的状态枚举不一致问题
-- 将 PENDING_PAYMENT 改为 PENDING
UPDATE wine_order SET status = 'PENDING' WHERE status = 'PENDING_PAYMENT'; 