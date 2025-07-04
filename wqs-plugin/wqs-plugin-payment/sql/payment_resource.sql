-- =====================================================
-- 支付管理插件权限资源配置
-- =====================================================

-- 支付管理模块
INSERT INTO `sys_resource` VALUES ('1880000000000000001', NULL, '支付管理', NULL, 'payment', 'MODULE', NULL, NULL, NULL, NULL, 'credit-card-outlined', '#1890ff', NULL, NULL, NULL, 60, NULL, 'NOT_DELETE', NULL, NULL, NULL, NULL);

-- 支付管理目录
INSERT INTO `sys_resource` VALUES ('1880000000000000002', '0', '支付管理', NULL, 'paymentManage', 'MENU', '1880000000000000001', 'CATALOG', '/payment', NULL, 'credit-card-outlined', NULL, NULL, NULL, NULL, 61, NULL, 'NOT_DELETE', NULL, NULL, NULL, NULL);

-- 支付记录管理
INSERT INTO `sys_resource` VALUES ('1880000000000000003', '1880000000000000002', '支付记录', 'paymentRecord', 'paymentRecordIndex', 'MENU', '1880000000000000001', 'MENU', '/payment/record', 'payment/record/index', 'unordered-list-outlined', NULL, NULL, NULL, NULL, 62, NULL, 'NOT_DELETE', NULL, NULL, NULL, NULL);

-- 支付记录按钮权限
INSERT INTO `sys_resource` VALUES ('1880000000000000004', '1880000000000000003', '支付记录分页', NULL, 'paymentRecordPage', 'BUTTON', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, 'NOT_DELETE', NULL, NULL, NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1880000000000000005', '1880000000000000003', '支付记录详情', NULL, 'paymentRecordDetail', 'BUTTON', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, NULL, 'NOT_DELETE', NULL, NULL, NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1880000000000000006', '1880000000000000003', '支付记录导出', NULL, 'paymentRecordExport', 'BUTTON', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 3, NULL, 'NOT_DELETE', NULL, NULL, NULL, NULL);

-- 支付配置管理
INSERT INTO `sys_resource` VALUES ('1880000000000000007', '1880000000000000002', '支付配置', 'paymentConfig', 'paymentConfigIndex', 'MENU', '1880000000000000001', 'MENU', '/payment/config', 'payment/config/index', 'setting-outlined', NULL, NULL, NULL, NULL, 63, NULL, 'NOT_DELETE', NULL, NULL, NULL, NULL);

-- 支付配置按钮权限
INSERT INTO `sys_resource` VALUES ('1880000000000000008', '1880000000000000007', '支付配置分页', NULL, 'paymentConfigPage', 'BUTTON', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, 'NOT_DELETE', NULL, NULL, NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1880000000000000009', '1880000000000000007', '添加支付配置', NULL, 'paymentConfigAdd', 'BUTTON', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, NULL, 'NOT_DELETE', NULL, NULL, NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1880000000000000010', '1880000000000000007', '编辑支付配置', NULL, 'paymentConfigEdit', 'BUTTON', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 3, NULL, 'NOT_DELETE', NULL, NULL, NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1880000000000000011', '1880000000000000007', '删除支付配置', NULL, 'paymentConfigDelete', 'BUTTON', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 4, NULL, 'NOT_DELETE', NULL, NULL, NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1880000000000000012', '1880000000000000007', '支付配置详情', NULL, 'paymentConfigDetail', 'BUTTON', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 5, NULL, 'NOT_DELETE', NULL, NULL, NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1880000000000000013', '1880000000000000007', '启用支付配置', NULL, 'paymentConfigEnable', 'BUTTON', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 6, NULL, 'NOT_DELETE', NULL, NULL, NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1880000000000000014', '1880000000000000007', '禁用支付配置', NULL, 'paymentConfigDisable', 'BUTTON', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 7, NULL, 'NOT_DELETE', NULL, NULL, NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1880000000000000015', '1880000000000000007', '批量删除支付配置', NULL, 'paymentConfigBatchDelete', 'BUTTON', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 8, NULL, 'NOT_DELETE', NULL, NULL, NULL, NULL);
INSERT INTO `sys_resource` VALUES ('1880000000000000016', '1880000000000000007', '支付配置选择器', NULL, 'paymentConfigSelector', 'BUTTON', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 9, NULL, 'NOT_DELETE', NULL, NULL, NULL, NULL);

-- 默认给超级管理员角色授权支付管理权限
-- 注意：这里的角色ID需要根据实际系统中的超级管理员角色ID进行调整
-- 支付记录管理权限
INSERT INTO `sys_relation` VALUES ('1880000000000000201', '1570687866138206208', '1880000000000000003', 'SYS_ROLE_HAS_RESOURCE', '{"menuId":"1880000000000000003","buttonInfo":["1880000000000000004","1880000000000000005","1880000000000000006"]}');

-- 支付配置管理权限
INSERT INTO `sys_relation` VALUES ('1880000000000000202', '1570687866138206208', '1880000000000000007', 'SYS_ROLE_HAS_RESOURCE', '{"menuId":"1880000000000000007","buttonInfo":["1880000000000000008","1880000000000000009","1880000000000000010","1880000000000000011","1880000000000000012","1880000000000000013","1880000000000000014","1880000000000000015","1880000000000000016"]}');

-- =====================================================
-- API权限配置 - 支付记录管理
-- =====================================================
INSERT INTO `sys_relation` VALUES ('1880000000000000300', '1570687866138206208', '/payment/record/page', 'SYS_ROLE_HAS_PERMISSION', '{"apiUrl":"/payment/record/page","scopeCategory":"SCOPE_ALL","scopeDefineOrgIdList":[]}');
INSERT INTO `sys_relation` VALUES ('1880000000000000301', '1570687866138206208', '/payment/record/detail', 'SYS_ROLE_HAS_PERMISSION', '{"apiUrl":"/payment/record/detail","scopeCategory":"SCOPE_ALL","scopeDefineOrgIdList":[]}');

-- =====================================================
-- API权限配置 - 支付配置管理
-- =====================================================
INSERT INTO `sys_relation` VALUES ('1880000000000000310', '1570687866138206208', '/payment/record/page', 'SYS_ROLE_HAS_PERMISSION', '{"apiUrl":"/payment/record/page","scopeCategory":"SCOPE_ALL","scopeDefineOrgIdList":[]}');
INSERT INTO `sys_relation` VALUES ('1880000000000000311', '1570687866138206208', '/payment/record/add', 'SYS_ROLE_HAS_PERMISSION', '{"apiUrl":"/payment/record/add","scopeCategory":"SCOPE_ALL","scopeDefineOrgIdList":[]}');
INSERT INTO `sys_relation` VALUES ('1880000000000000312', '1570687866138206208', '/payment/record/edit', 'SYS_ROLE_HAS_PERMISSION', '{"apiUrl":"/payment/record/edit","scopeCategory":"SCOPE_ALL","scopeDefineOrgIdList":[]}');
INSERT INTO `sys_relation` VALUES ('1880000000000000313', '1570687866138206208', '/payment/record/delete', 'SYS_ROLE_HAS_PERMISSION', '{"apiUrl":"/payment/record/delete","scopeCategory":"SCOPE_ALL","scopeDefineOrgIdList":[]}');
INSERT INTO `sys_relation` VALUES ('1880000000000000314', '1570687866138206208', '/payment/record/detail', 'SYS_ROLE_HAS_PERMISSION', '{"apiUrl":"/payment/record/detail","scopeCategory":"SCOPE_ALL","scopeDefineOrgIdList":[]}');
INSERT INTO `sys_relation` VALUES ('1880000000000000315', '1570687866138206208', '/payment/record/disable', 'SYS_ROLE_HAS_PERMISSION', '{"apiUrl":"/payment/record/disable","scopeCategory":"SCOPE_ALL","scopeDefineOrgIdList":[]}');
INSERT INTO `sys_relation` VALUES ('1880000000000000316', '1570687866138206208', '/payment/record/enable', 'SYS_ROLE_HAS_PERMISSION', '{"apiUrl":"/payment/record/enable","scopeCategory":"SCOPE_ALL","scopeDefineOrgIdList":[]}');
INSERT INTO `sys_relation` VALUES ('1880000000000000317', '1570687866138206208', '/payment/record/batchDelete', 'SYS_ROLE_HAS_PERMISSION', '{"apiUrl":"/payment/record/batchDelete","scopeCategory":"SCOPE_ALL","scopeDefineOrgIdList":[]}');
INSERT INTO `sys_relation` VALUES ('1880000000000000318', '1570687866138206208', '/payment/record/selector', 'SYS_ROLE_HAS_PERMISSION', '{"apiUrl":"/payment/record/selector","scopeCategory":"SCOPE_ALL","scopeDefineOrgIdList":[]}');
INSERT INTO `sys_relation` VALUES ('1880000000000000319', '1570687866138206208', '/payment/record/enabled', 'SYS_ROLE_HAS_PERMISSION', '{"apiUrl":"/payment/record/enabled","scopeCategory":"SCOPE_ALL","scopeDefineOrgIdList":[]}');

-- =====================================================
-- API权限配置 - 微信支付管理
-- =====================================================
INSERT INTO `sys_relation` VALUES ('1880000000000000320', '1570687866138206208', '/payment/wechat/create', 'SYS_ROLE_HAS_PERMISSION', '{"apiUrl":"/payment/wechat/create","scopeCategory":"SCOPE_ALL","scopeDefineOrgIdList":[]}');
INSERT INTO `sys_relation` VALUES ('1880000000000000321', '1570687866138206208', '/payment/wechat/query', 'SYS_ROLE_HAS_PERMISSION', '{"apiUrl":"/payment/wechat/query","scopeCategory":"SCOPE_ALL","scopeDefineOrgIdList":[]}');
INSERT INTO `sys_relation` VALUES ('1880000000000000322', '1570687866138206208', '/payment/wechat/refund', 'SYS_ROLE_HAS_PERMISSION', '{"apiUrl":"/payment/wechat/refund","scopeCategory":"SCOPE_ALL","scopeDefineOrgIdList":[]}');
INSERT INTO `sys_relation` VALUES ('1880000000000000323', '1570687866138206208', '/payment/wechat/refund/query', 'SYS_ROLE_HAS_PERMISSION', '{"apiUrl":"/payment/wechat/refund/query","scopeCategory":"SCOPE_ALL","scopeDefineOrgIdList":[]}');
INSERT INTO `sys_relation` VALUES ('1880000000000000324', '1570687866138206208', '/payment/wechat/close', 'SYS_ROLE_HAS_PERMISSION', '{"apiUrl":"/payment/wechat/close","scopeCategory":"SCOPE_ALL","scopeDefineOrgIdList":[]}');

-- 支付方式字典数据
INSERT INTO `dev_dict` VALUES ('1880000000000000501', '0', '支付方式', 'PAY_TYPE', 'payment_type_dict', 'BIZ', 100, NULL, 'NOT_DELETE', NULL, NULL, NULL, NULL);

INSERT INTO `dev_dict` VALUES ('1880000000000000301', '1880000000000000501', '微信小程序支付', 'WECHAT_MINI', 'wechat_mini_pay', 'BIZ', 10, NULL, 'NOT_DELETE', NULL, NULL, NULL, NULL);
INSERT INTO `dev_dict` VALUES ('1880000000000000302', '1880000000000000501', '微信H5支付', 'WECHAT_H5', 'wechat_h5_pay', 'BIZ', 20, NULL, 'NOT_DELETE', NULL, NULL, NULL, NULL);
INSERT INTO `dev_dict` VALUES ('1880000000000000303', '1880000000000000501', '微信APP支付', 'WECHAT_APP', 'wechat_app_pay', 'BIZ', 30, NULL, 'NOT_DELETE', NULL, NULL, NULL, NULL);
INSERT INTO `dev_dict` VALUES ('1880000000000000304', '1880000000000000501', '微信扫码支付', 'WECHAT_SCAN', 'wechat_scan_pay', 'BIZ', 40, NULL, 'NOT_DELETE', NULL, NULL, NULL, NULL);
INSERT INTO `dev_dict` VALUES ('1880000000000000305', '1880000000000000501', '支付宝APP支付', 'ALIPAY_APP', 'alipay_app_pay', 'BIZ', 50, NULL, 'NOT_DELETE', NULL, NULL, NULL, NULL);
INSERT INTO `dev_dict` VALUES ('1880000000000000306', '1880000000000000501', '支付宝H5支付', 'ALIPAY_H5', 'alipay_h5_pay', 'BIZ', 60, NULL, 'NOT_DELETE', NULL, NULL, NULL, NULL);
INSERT INTO `dev_dict` VALUES ('1880000000000000307', '1880000000000000501', '支付宝扫码支付', 'ALIPAY_SCAN', 'alipay_scan_pay', 'BIZ', 70, NULL, 'NOT_DELETE', NULL, NULL, NULL, NULL);
INSERT INTO `dev_dict` VALUES ('1880000000000000308', '1880000000000000501', '余额支付', 'BALANCE', 'balance_pay', 'BIZ', 80, NULL, 'NOT_DELETE', NULL, NULL, NULL, NULL);

-- 支付状态字典数据
INSERT INTO `dev_dict` VALUES ('1880000000000000502', '0', '支付状态', 'PAY_STATUS', 'payment_status_dict', 'BIZ', 101, NULL, 'NOT_DELETE', NULL, NULL, NULL, NULL);

INSERT INTO `dev_dict` VALUES ('1880000000000000401', '1880000000000000502', '待支付', 'PENDING', 'pay_pending', 'BIZ', 10, NULL, 'NOT_DELETE', NULL, NULL, NULL, NULL);
INSERT INTO `dev_dict` VALUES ('1880000000000000402', '1880000000000000502', '支付成功', 'SUCCESS', 'pay_success', 'BIZ', 20, NULL, 'NOT_DELETE', NULL, NULL, NULL, NULL);
INSERT INTO `dev_dict` VALUES ('1880000000000000403', '1880000000000000502', '支付失败', 'FAILED', 'pay_failed', 'BIZ', 30, NULL, 'NOT_DELETE', NULL, NULL, NULL, NULL);
INSERT INTO `dev_dict` VALUES ('1880000000000000404', '1880000000000000502', '已取消', 'CANCELED', 'pay_canceled', 'BIZ', 40, NULL, 'NOT_DELETE', NULL, NULL, NULL, NULL);
INSERT INTO `dev_dict` VALUES ('1880000000000000405', '1880000000000000502', '已退款', 'REFUNDED', 'pay_refunded', 'BIZ', 50, NULL, 'NOT_DELETE', NULL, NULL, NULL, NULL);
INSERT INTO `dev_dict` VALUES ('1880000000000000406', '1880000000000000502', '已过期', 'EXPIRED', 'pay_expired', 'BIZ', 60, NULL, 'NOT_DELETE', NULL, NULL, NULL, NULL); 