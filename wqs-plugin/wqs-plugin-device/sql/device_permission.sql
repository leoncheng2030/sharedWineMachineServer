-- 设备管理模块权限配置脚本
-- 参照酒品管理模块的菜单结构

-- 1. 创建设备管理目录 (CATALOG类型，相当于酒品管理中的"酒品"目录)
INSERT INTO `sys_resource` VALUES 
('1990000000000010000', '0', '设备管理', NULL, 'deviceManageCatalog', 'MENU', '1883450100000000000', 'CATALOG', '/device_manage_catalog', NULL, 'desktop-outlined', NULL, 'TRUE', 'YES', 'YES', 200, NULL, 'NOT_DELETE', NOW(), 'SYSTEM', NULL, NULL);

-- 2. 创建设备管理具体菜单 (MENU类型)
INSERT INTO `sys_resource` VALUES 
('1990000000000010001', '1990000000000010000', '设备信息', 'deviceInfo', 'device_info_manage', 'MENU', '1883450100000000000', 'MENU', '/device/info', 'device/info/index', 'desktop-outlined', NULL, 'TRUE', 'YES', 'YES', 100, NULL, 'NOT_DELETE', NOW(), 'SYSTEM', NULL, NULL);

-- 3. 为设备信息管理添加按钮权限
INSERT INTO `sys_resource` VALUES 
('1990000000000010100', '1990000000000010001', '查看', NULL, 'deviceInfoPage', 'BUTTON', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 100, NULL, 'NOT_DELETE', NULL, NULL, NULL, NULL),
('1990000000000010101', '1990000000000010001', '新增', NULL, 'deviceInfoAdd', 'BUTTON', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 110, NULL, 'NOT_DELETE', NULL, NULL, NULL, NULL),
('1990000000000010102', '1990000000000010001', '编辑', NULL, 'deviceInfoEdit', 'BUTTON', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 120, NULL, 'NOT_DELETE', NULL, NULL, NULL, NULL),
('1990000000000010103', '1990000000000010001', '删除', NULL, 'deviceInfoDelete', 'BUTTON', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 130, NULL, 'NOT_DELETE', NULL, NULL, NULL, NULL),
('1990000000000010104', '1990000000000010001', '详情', NULL, 'deviceInfoDetail', 'BUTTON', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 140, NULL, 'NOT_DELETE', NULL, NULL, NULL, NULL),
('1990000000000010105', '1990000000000010001', '修改状态', NULL, 'deviceInfoUpdateStatus', 'BUTTON', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 150, NULL, 'NOT_DELETE', NULL, NULL, NULL, NULL);

-- 4. 为超级管理员角色分配设备管理权限
-- 添加设备管理目录权限
INSERT INTO `sys_relation` VALUES 
('1990000000000010200', '1570687866138206208', '1990000000000010000', 'SYS_ROLE_HAS_RESOURCE', '{"menuId":"1990000000000010000","buttonInfo":[]}');

-- 添加设备信息管理权限
INSERT INTO `sys_relation` VALUES 
('1990000000000010201', '1570687866138206208', '1990000000000010001', 'SYS_ROLE_HAS_RESOURCE', '{"menuId":"1990000000000010001","buttonInfo":["1990000000000010100","1990000000000010101","1990000000000010102","1990000000000010103","1990000000000010104","1990000000000010105"]}');

-- 5. 为超级管理员角色分配API权限
INSERT INTO `sys_relation` VALUES 
('1990000000000010300', '1570687866138206208', '/device/info/page', 'SYS_ROLE_HAS_PERMISSION', '{"apiUrl":"/device/info/page","scopeCategory":"SCOPE_ALL","scopeDefineOrgIdList":[]}'),
('1990000000000010301', '1570687866138206208', '/device/info/add', 'SYS_ROLE_HAS_PERMISSION', '{"apiUrl":"/device/info/add","scopeCategory":"SCOPE_ALL","scopeDefineOrgIdList":[]}'),
('1990000000000010302', '1570687866138206208', '/device/info/edit', 'SYS_ROLE_HAS_PERMISSION', '{"apiUrl":"/device/info/edit","scopeCategory":"SCOPE_ALL","scopeDefineOrgIdList":[]}'),
('1990000000000010303', '1570687866138206208', '/device/info/delete', 'SYS_ROLE_HAS_PERMISSION', '{"apiUrl":"/device/info/delete","scopeCategory":"SCOPE_ALL","scopeDefineOrgIdList":[]}'),
('1990000000000010304', '1570687866138206208', '/device/info/detail', 'SYS_ROLE_HAS_PERMISSION', '{"apiUrl":"/device/info/detail","scopeCategory":"SCOPE_ALL","scopeDefineOrgIdList":[]}'),
('1990000000000010305', '1570687866138206208', '/device/info/batchDelete', 'SYS_ROLE_HAS_PERMISSION', '{"apiUrl":"/device/info/batchDelete","scopeCategory":"SCOPE_ALL","scopeDefineOrgIdList":[]}'),
('1990000000000010306', '1570687866138206208', '/device/info/export', 'SYS_ROLE_HAS_PERMISSION', '{"apiUrl":"/device/info/export","scopeCategory":"SCOPE_ALL","scopeDefineOrgIdList":[]}'); 