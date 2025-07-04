-- =====================================================
-- 支付管理插件权限修复脚本
-- 解决 "无此权限：/payment/record/edit" 等权限问题
-- =====================================================

-- 清理可能重复的权限记录（如果存在）
DELETE FROM `sys_relation` WHERE `id` IN (
    '1880000000000000300', '1880000000000000301', '1880000000000000310', '1880000000000000311', 
    '1880000000000000312', '1880000000000000313', '1880000000000000314', '1880000000000000315', 
    '1880000000000000316', '1880000000000000317', '1880000000000000318', '1880000000000000319',
    '1880000000000000320', '1880000000000000321', '1880000000000000322', '1880000000000000323', '1880000000000000324'
);

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

-- =====================================================
-- 验证权限配置是否成功
-- =====================================================
-- 查询添加的权限记录
SELECT 
    r.id,
    r.object_id as role_id,
    r.target_id as api_url,
    r.category,
    r.ext_json
FROM sys_relation r 
WHERE r.category = 'SYS_ROLE_HAS_PERMISSION' 
AND r.target_id LIKE '/payment/%'
ORDER BY r.target_id; 