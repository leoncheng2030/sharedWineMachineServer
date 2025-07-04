-- 门店管理模块权限配置脚本
-- 参照酒品管理和设备管理模块的菜单结构

-- 1. 创建门店管理目录 (CATALOG类型)
INSERT INTO `sys_resource` VALUES 
('1991000000000010000', '0', '门店管理', NULL, 'storeManageCatalog', 'MENU', '1883450100000000000', 'CATALOG', '/store_manage_catalog', NULL, 'shop-outlined', NULL, 'TRUE', 'YES', 'YES', 300, NULL, 'NOT_DELETE', NOW(), 'SYSTEM', NULL, NULL);

-- 2. 创建门店管理具体菜单 (MENU类型)
INSERT INTO `sys_resource` VALUES 
('1991000000000010001', '1991000000000010000', '门店信息', 'storeManage', 'store_manage', 'MENU', '1883450100000000000', 'MENU', '/store/record', 'store/record/index', 'shop-outlined', NULL, 'TRUE', 'YES', 'YES', 100, NULL, 'NOT_DELETE', NOW(), 'SYSTEM', NULL, NULL);

-- 3. 为门店信息管理添加按钮权限
INSERT INTO `sys_resource` VALUES 
('1991000000000010100', '1991000000000010001', '查看', NULL, 'storeManagePage', 'BUTTON', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 100, NULL, 'NOT_DELETE', NULL, NULL, NULL, NULL),
('1991000000000010101', '1991000000000010001', '新增', NULL, 'storeManageAdd', 'BUTTON', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 110, NULL, 'NOT_DELETE', NULL, NULL, NULL, NULL),
('1991000000000010102', '1991000000000010001', '编辑', NULL, 'storeManageEdit', 'BUTTON', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 120, NULL, 'NOT_DELETE', NULL, NULL, NULL, NULL),
('1991000000000010103', '1991000000000010001', '删除', NULL, 'storeManageDelete', 'BUTTON', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 130, NULL, 'NOT_DELETE', NULL, NULL, NULL, NULL),
('1991000000000010104', '1991000000000010001', '详情', NULL, 'storeManageDetail', 'BUTTON', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 140, NULL, 'NOT_DELETE', NULL, NULL, NULL, NULL),
('1991000000000010105', '1991000000000010001', '修改状态', NULL, 'storeManageUpdateStatus', 'BUTTON', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 150, NULL, 'NOT_DELETE', NULL, NULL, NULL, NULL),
('1991000000000010106', '1991000000000010001', '设置定价权限', NULL, 'storeManageSetPriceAuthority', 'BUTTON', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 160, NULL, 'NOT_DELETE', NULL, NULL, NULL, NULL);

-- 4. 为超级管理员角色分配门店管理权限
-- 添加门店管理目录权限
INSERT INTO `sys_relation` VALUES 
('1991000000000010200', '1570687866138206208', '1991000000000010000', 'SYS_ROLE_HAS_RESOURCE', '{"menuId":"1991000000000010000","buttonInfo":[]}');

-- 添加门店信息管理权限
INSERT INTO `sys_relation` VALUES 
('1991000000000010201', '1570687866138206208', '1991000000000010001', 'SYS_ROLE_HAS_RESOURCE', '{"menuId":"1991000000000010001","buttonInfo":["1991000000000010100","1991000000000010101","1991000000000010102","1991000000000010103","1991000000000010104","1991000000000010105","1991000000000010106"]}');

-- 5. 为超级管理员角色分配API权限
INSERT INTO `sys_relation` VALUES 
('1991000000000010300', '1570687866138206208', '/store/record/page', 'SYS_ROLE_HAS_PERMISSION', '{"apiUrl":"/store/record/page","scopeCategory":"SCOPE_ALL","scopeDefineOrgIdList":[]}'),
('1991000000000010301', '1570687866138206208', '/store/record/add', 'SYS_ROLE_HAS_PERMISSION', '{"apiUrl":"/store/record/add","scopeCategory":"SCOPE_ALL","scopeDefineOrgIdList":[]}'),
('1991000000000010302', '1570687866138206208', '/store/record/edit', 'SYS_ROLE_HAS_PERMISSION', '{"apiUrl":"/store/record/edit","scopeCategory":"SCOPE_ALL","scopeDefineOrgIdList":[]}'),
('1991000000000010303', '1570687866138206208', '/store/record/delete', 'SYS_ROLE_HAS_PERMISSION', '{"apiUrl":"/store/record/delete","scopeCategory":"SCOPE_ALL","scopeDefineOrgIdList":[]}'),
('1991000000000010304', '1570687866138206208', '/store/record/detail', 'SYS_ROLE_HAS_PERMISSION', '{"apiUrl":"/store/record/detail","scopeCategory":"SCOPE_ALL","scopeDefineOrgIdList":[]}'),
('1991000000000010305', '1570687866138206208', '/store/record/batchDelete', 'SYS_ROLE_HAS_PERMISSION', '{"apiUrl":"/store/record/batchDelete","scopeCategory":"SCOPE_ALL","scopeDefineOrgIdList":[]}'),
('1991000000000010306', '1570687866138206208', '/store/record/export', 'SYS_ROLE_HAS_PERMISSION', '{"apiUrl":"/store/record/export","scopeCategory":"SCOPE_ALL","scopeDefineOrgIdList":[]}'),
('1991000000000010307', '1570687866138206208', '/store/record/selector', 'SYS_ROLE_HAS_PERMISSION', '{"apiUrl":"/store/record/selector","scopeCategory":"SCOPE_ALL","scopeDefineOrgIdList":[]}'),
('1991000000000010308', '1570687866138206208', '/store/record/list', 'SYS_ROLE_HAS_PERMISSION', '{"apiUrl":"/store/record/list","scopeCategory":"SCOPE_ALL","scopeDefineOrgIdList":[]}'),
('1991000000000010309', '1570687866138206208', '/store/record/setPriceAuthority', 'SYS_ROLE_HAS_PERMISSION', '{"apiUrl":"/store/record/setPriceAuthority","scopeCategory":"SCOPE_ALL","scopeDefineOrgIdList":[]}'),
('1991000000000010310', '1570687866138206208', '/store/record/enable', 'SYS_ROLE_HAS_PERMISSION', '{"apiUrl":"/store/record/enable","scopeCategory":"SCOPE_ALL","scopeDefineOrgIdList":[]}'),
('1991000000000010311', '1570687866138206208', '/store/record/disable', 'SYS_ROLE_HAS_PERMISSION', '{"apiUrl":"/store/record/disable","scopeCategory":"SCOPE_ALL","scopeDefineOrgIdList":[]}'),
('1991000000000010312', '1570687866138206208', '/store/record/managerUserSelector', 'SYS_ROLE_HAS_PERMISSION', '{"apiUrl":"/store/record/managerUserSelector","scopeCategory":"SCOPE_ALL","scopeDefineOrgIdList":[]}');