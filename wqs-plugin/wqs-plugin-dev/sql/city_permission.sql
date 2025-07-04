-- 城市管理权限脚本
-- 创建日期：2025/01/30
-- 说明：为城市管理模块创建菜单资源和按钮权限
-- 注意：此脚本可重复执行，会先删除已存在的记录
-- 参照weiqisheng.sql中的sys_resource表结构和MDC开发规范手册

-- ----------------------------
-- 删除已存在的城市管理权限记录（开发阶段使用）
-- ----------------------------
DELETE FROM `sys_resource` WHERE `ID` IN (
  '1990000000000020000', -- 城市管理菜单
  '1990000000000020100', -- 分页查询
  '1990000000000020101', -- 列表查询
  '1990000000000020102', -- 新增城市
  '1990000000000020103', -- 编辑城市
  '1990000000000020104', -- 删除城市
  '1990000000000020105', -- 批量删除
  '1990000000000020106', -- 城市详情
  '1990000000000020107', -- 启用城市
  '1990000000000020108', -- 禁用城市
  '1990000000000020109', -- 城市导出
  '1990000000000020110', -- 城市选择器
  '1990000000000020111', -- 按层级查询
  '1990000000000020112', -- 按父级查询
  '1990000000000020113', -- 热门城市
  '1990000000000020114', -- 设置热门
  '1990000000000020115', -- 设置配送
  '1990000000000020116'  -- 更新统计
);

-- ----------------------------
-- 插入城市管理权限记录
-- ----------------------------

-- 1. 城市管理菜单（MENU类型）
INSERT INTO `sys_resource` VALUES (
  '1990000000000020000',          -- ID
  '1548901111999771626',           -- PARENT_ID（基础工具模块）
  '城市管理',                      -- TITLE
  NULL,                            -- NAME
  'devCity',                       -- CODE
  'MENU',                          -- CATEGORY
  NULL,                            -- MODULE
  '/dev/city',                     -- PATH
  NULL,                            -- ICON
  NULL,                            -- COLOR
  NULL,                            -- COMPONENT
  NULL,                            -- PERMISSION
  NULL,                            -- EFFECT
  NULL,                            -- AFFIX
  NULL,                            -- VISIBLE
  100,                             -- SORT_CODE
  NULL,                            -- REMARK
  'NOT_DELETE',                    -- DELETE_FLAG
  NOW(),                           -- CREATE_TIME
  '1543837863788879871',           -- CREATE_USER
  NULL,                            -- UPDATE_TIME
  NULL                             -- UPDATE_USER
);

-- 2. 分页查询按钮
INSERT INTO `sys_resource` VALUES (
  '1990000000000020100',          -- ID
  '1990000000000020000',           -- PARENT_ID
  '分页查询',                      -- TITLE
  NULL,                            -- NAME
  '/dev/city/page',                -- CODE
  'BUTTON',                        -- CATEGORY
  NULL,                            -- MODULE
  NULL,                            -- PATH
  NULL,                            -- ICON
  NULL,                            -- COLOR
  NULL,                            -- COMPONENT
  NULL,                            -- PERMISSION
  NULL,                            -- EFFECT
  NULL,                            -- AFFIX
  NULL,                            -- VISIBLE
  100,                             -- SORT_CODE
  NULL,                            -- REMARK
  'NOT_DELETE',                    -- DELETE_FLAG
  NOW(),                           -- CREATE_TIME
  '1543837863788879871',           -- CREATE_USER
  NULL,                            -- UPDATE_TIME
  NULL                             -- UPDATE_USER
);

-- 3. 列表查询按钮
INSERT INTO `sys_resource` VALUES (
  '1990000000000020101',          -- ID
  '1990000000000020000',           -- PARENT_ID
  '列表查询',                      -- TITLE
  NULL,                            -- NAME
  '/dev/city/list',                -- CODE
  'BUTTON',                        -- CATEGORY
  NULL,                            -- MODULE
  NULL,                            -- PATH
  NULL,                            -- ICON
  NULL,                            -- COLOR
  NULL,                            -- COMPONENT
  NULL,                            -- PERMISSION
  NULL,                            -- EFFECT
  NULL,                            -- AFFIX
  NULL,                            -- VISIBLE
  101,                             -- SORT_CODE
  NULL,                            -- REMARK
  'NOT_DELETE',                    -- DELETE_FLAG
  NOW(),                           -- CREATE_TIME
  '1543837863788879871',           -- CREATE_USER
  NULL,                            -- UPDATE_TIME
  NULL                             -- UPDATE_USER
);

-- 4. 新增城市按钮
INSERT INTO `sys_resource` VALUES (
  '1990000000000020102',          -- ID
  '1990000000000020000',           -- PARENT_ID
  '新增城市',                      -- TITLE
  NULL,                            -- NAME
  '/dev/city/add',                 -- CODE
  'BUTTON',                        -- CATEGORY
  NULL,                            -- MODULE
  NULL,                            -- PATH
  NULL,                            -- ICON
  NULL,                            -- COLOR
  NULL,                            -- COMPONENT
  NULL,                            -- PERMISSION
  NULL,                            -- EFFECT
  NULL,                            -- AFFIX
  NULL,                            -- VISIBLE
  102,                             -- SORT_CODE
  NULL,                            -- REMARK
  'NOT_DELETE',                    -- DELETE_FLAG
  NOW(),                           -- CREATE_TIME
  '1543837863788879871',           -- CREATE_USER
  NULL,                            -- UPDATE_TIME
  NULL                             -- UPDATE_USER
);

-- 5. 编辑城市按钮
INSERT INTO `sys_resource` VALUES (
  '1990000000000020103',          -- ID
  '1990000000000020000',           -- PARENT_ID
  '编辑城市',                      -- TITLE
  NULL,                            -- NAME
  '/dev/city/edit',                -- CODE
  'BUTTON',                        -- CATEGORY
  NULL,                            -- MODULE
  NULL,                            -- PATH
  NULL,                            -- ICON
  NULL,                            -- COLOR
  NULL,                            -- COMPONENT
  NULL,                            -- PERMISSION
  NULL,                            -- EFFECT
  NULL,                            -- AFFIX
  NULL,                            -- VISIBLE
  103,                             -- SORT_CODE
  NULL,                            -- REMARK
  'NOT_DELETE',                    -- DELETE_FLAG
  NOW(),                           -- CREATE_TIME
  '1543837863788879871',           -- CREATE_USER
  NULL,                            -- UPDATE_TIME
  NULL                             -- UPDATE_USER
);

-- 6. 删除城市按钮
INSERT INTO `sys_resource` VALUES (
  '1990000000000020104',          -- ID
  '1990000000000020000',           -- PARENT_ID
  '删除城市',                      -- TITLE
  NULL,                            -- NAME
  '/dev/city/delete',              -- CODE
  'BUTTON',                        -- CATEGORY
  NULL,                            -- MODULE
  NULL,                            -- PATH
  NULL,                            -- ICON
  NULL,                            -- COLOR
  NULL,                            -- COMPONENT
  NULL,                            -- PERMISSION
  NULL,                            -- EFFECT
  NULL,                            -- AFFIX
  NULL,                            -- VISIBLE
  104,                             -- SORT_CODE
  NULL,                            -- REMARK
  'NOT_DELETE',                    -- DELETE_FLAG
  NOW(),                           -- CREATE_TIME
  '1543837863788879871',           -- CREATE_USER
  NULL,                            -- UPDATE_TIME
  NULL                             -- UPDATE_USER
);

-- 7. 批量删除按钮
INSERT INTO `sys_resource` VALUES (
  '1990000000000020105',          -- ID
  '1990000000000020000',           -- PARENT_ID
  '批量删除',                      -- TITLE
  NULL,                            -- NAME
  '/dev/city/batchDelete',         -- CODE
  'BUTTON',                        -- CATEGORY
  NULL,                            -- MODULE
  NULL,                            -- PATH
  NULL,                            -- ICON
  NULL,                            -- COLOR
  NULL,                            -- COMPONENT
  NULL,                            -- PERMISSION
  NULL,                            -- EFFECT
  NULL,                            -- AFFIX
  NULL,                            -- VISIBLE
  105,                             -- SORT_CODE
  NULL,                            -- REMARK
  'NOT_DELETE',                    -- DELETE_FLAG
  NOW(),                           -- CREATE_TIME
  '1543837863788879871',           -- CREATE_USER
  NULL,                            -- UPDATE_TIME
  NULL                             -- UPDATE_USER
);

-- 8. 城市详情按钮
INSERT INTO `sys_resource` VALUES (
  '1990000000000020106',          -- ID
  '1990000000000020000',           -- PARENT_ID
  '城市详情',                      -- TITLE
  NULL,                            -- NAME
  '/dev/city/detail',              -- CODE
  'BUTTON',                        -- CATEGORY
  NULL,                            -- MODULE
  NULL,                            -- PATH
  NULL,                            -- ICON
  NULL,                            -- COLOR
  NULL,                            -- COMPONENT
  NULL,                            -- PERMISSION
  NULL,                            -- EFFECT
  NULL,                            -- AFFIX
  NULL,                            -- VISIBLE
  106,                             -- SORT_CODE
  NULL,                            -- REMARK
  'NOT_DELETE',                    -- DELETE_FLAG
  NOW(),                           -- CREATE_TIME
  '1543837863788879871',           -- CREATE_USER
  NULL,                            -- UPDATE_TIME
  NULL                             -- UPDATE_USER
);

-- 9. 启用城市按钮
INSERT INTO `sys_resource` VALUES (
  '1990000000000020107',          -- ID
  '1990000000000020000',           -- PARENT_ID
  '启用城市',                      -- TITLE
  NULL,                            -- NAME
  '/dev/city/enable',              -- CODE
  'BUTTON',                        -- CATEGORY
  NULL,                            -- MODULE
  NULL,                            -- PATH
  NULL,                            -- ICON
  NULL,                            -- COLOR
  NULL,                            -- COMPONENT
  NULL,                            -- PERMISSION
  NULL,                            -- EFFECT
  NULL,                            -- AFFIX
  NULL,                            -- VISIBLE
  107,                             -- SORT_CODE
  NULL,                            -- REMARK
  'NOT_DELETE',                    -- DELETE_FLAG
  NOW(),                           -- CREATE_TIME
  '1543837863788879871',           -- CREATE_USER
  NULL,                            -- UPDATE_TIME
  NULL                             -- UPDATE_USER
);

-- 10. 禁用城市按钮
INSERT INTO `sys_resource` VALUES (
  '1990000000000020108',          -- ID
  '1990000000000020000',           -- PARENT_ID
  '禁用城市',                      -- TITLE
  NULL,                            -- NAME
  '/dev/city/disable',             -- CODE
  'BUTTON',                        -- CATEGORY
  NULL,                            -- MODULE
  NULL,                            -- PATH
  NULL,                            -- ICON
  NULL,                            -- COLOR
  NULL,                            -- COMPONENT
  NULL,                            -- PERMISSION
  NULL,                            -- EFFECT
  NULL,                            -- AFFIX
  NULL,                            -- VISIBLE
  108,                             -- SORT_CODE
  NULL,                            -- REMARK
  'NOT_DELETE',                    -- DELETE_FLAG
  NOW(),                           -- CREATE_TIME
  '1543837863788879871',           -- CREATE_USER
  NULL,                            -- UPDATE_TIME
  NULL                             -- UPDATE_USER
);

-- 11. 城市导出按钮
INSERT INTO `sys_resource` VALUES (
  '1990000000000020109',          -- ID
  '1990000000000020000',           -- PARENT_ID
  '城市导出',                      -- TITLE
  NULL,                            -- NAME
  '/dev/city/export',              -- CODE
  'BUTTON',                        -- CATEGORY
  NULL,                            -- MODULE
  NULL,                            -- PATH
  NULL,                            -- ICON
  NULL,                            -- COLOR
  NULL,                            -- COMPONENT
  NULL,                            -- PERMISSION
  NULL,                            -- EFFECT
  NULL,                            -- AFFIX
  NULL,                            -- VISIBLE
  109,                             -- SORT_CODE
  NULL,                            -- REMARK
  'NOT_DELETE',                    -- DELETE_FLAG
  NOW(),                           -- CREATE_TIME
  '1543837863788879871',           -- CREATE_USER
  NULL,                            -- UPDATE_TIME
  NULL                             -- UPDATE_USER
);

-- 12. 城市选择器按钮
INSERT INTO `sys_resource` VALUES (
  '1990000000000020110',          -- ID
  '1990000000000020000',           -- PARENT_ID
  '城市选择器',                    -- TITLE
  NULL,                            -- NAME
  '/dev/city/selector',            -- CODE
  'BUTTON',                        -- CATEGORY
  NULL,                            -- MODULE
  NULL,                            -- PATH
  NULL,                            -- ICON
  NULL,                            -- COLOR
  NULL,                            -- COMPONENT
  NULL,                            -- PERMISSION
  NULL,                            -- EFFECT
  NULL,                            -- AFFIX
  NULL,                            -- VISIBLE
  110,                             -- SORT_CODE
  NULL,                            -- REMARK
  'NOT_DELETE',                    -- DELETE_FLAG
  NOW(),                           -- CREATE_TIME
  '1543837863788879871',           -- CREATE_USER
  NULL,                            -- UPDATE_TIME
  NULL                             -- UPDATE_USER
);

-- 13. 按层级查询按钮
INSERT INTO `sys_resource` VALUES (
  '1990000000000020111',          -- ID
  '1990000000000020000',           -- PARENT_ID
  '按层级查询',                    -- TITLE
  NULL,                            -- NAME
  '/dev/city/listByLevel',         -- CODE
  'BUTTON',                        -- CATEGORY
  NULL,                            -- MODULE
  NULL,                            -- PATH
  NULL,                            -- ICON
  NULL,                            -- COLOR
  NULL,                            -- COMPONENT
  NULL,                            -- PERMISSION
  NULL,                            -- EFFECT
  NULL,                            -- AFFIX
  NULL,                            -- VISIBLE
  111,                             -- SORT_CODE
  NULL,                            -- REMARK
  'NOT_DELETE',                    -- DELETE_FLAG
  NOW(),                           -- CREATE_TIME
  '1543837863788879871',           -- CREATE_USER
  NULL,                            -- UPDATE_TIME
  NULL                             -- UPDATE_USER
);

-- 14. 按父级查询按钮
INSERT INTO `sys_resource` VALUES (
  '1990000000000020112',          -- ID
  '1990000000000020000',           -- PARENT_ID
  '按父级查询',                    -- TITLE
  NULL,                            -- NAME
  '/dev/city/listByParentCode',    -- CODE
  'BUTTON',                        -- CATEGORY
  NULL,                            -- MODULE
  NULL,                            -- PATH
  NULL,                            -- ICON
  NULL,                            -- COLOR
  NULL,                            -- COMPONENT
  NULL,                            -- PERMISSION
  NULL,                            -- EFFECT
  NULL,                            -- AFFIX
  NULL,                            -- VISIBLE
  112,                             -- SORT_CODE
  NULL,                            -- REMARK
  'NOT_DELETE',                    -- DELETE_FLAG
  NOW(),                           -- CREATE_TIME
  '1543837863788879871',           -- CREATE_USER
  NULL,                            -- UPDATE_TIME
  NULL                             -- UPDATE_USER
);

-- 15. 热门城市按钮
INSERT INTO `sys_resource` VALUES (
  '1990000000000020113',          -- ID
  '1990000000000020000',           -- PARENT_ID
  '热门城市',                      -- TITLE
  NULL,                            -- NAME
  '/dev/city/listHotCities',       -- CODE
  'BUTTON',                        -- CATEGORY
  NULL,                            -- MODULE
  NULL,                            -- PATH
  NULL,                            -- ICON
  NULL,                            -- COLOR
  NULL,                            -- COMPONENT
  NULL,                            -- PERMISSION
  NULL,                            -- EFFECT
  NULL,                            -- AFFIX
  NULL,                            -- VISIBLE
  113,                             -- SORT_CODE
  NULL,                            -- REMARK
  'NOT_DELETE',                    -- DELETE_FLAG
  NOW(),                           -- CREATE_TIME
  '1543837863788879871',           -- CREATE_USER
  NULL,                            -- UPDATE_TIME
  NULL                             -- UPDATE_USER
);

-- 16. 设置热门按钮
INSERT INTO `sys_resource` VALUES (
  '1990000000000020114',          -- ID
  '1990000000000020000',           -- PARENT_ID
  '设置热门',                      -- TITLE
  NULL,                            -- NAME
  '/dev/city/setHotCity',          -- CODE
  'BUTTON',                        -- CATEGORY
  NULL,                            -- MODULE
  NULL,                            -- PATH
  NULL,                            -- ICON
  NULL,                            -- COLOR
  NULL,                            -- COMPONENT
  NULL,                            -- PERMISSION
  NULL,                            -- EFFECT
  NULL,                            -- AFFIX
  NULL,                            -- VISIBLE
  114,                             -- SORT_CODE
  NULL,                            -- REMARK
  'NOT_DELETE',                    -- DELETE_FLAG
  NOW(),                           -- CREATE_TIME
  '1543837863788879871',           -- CREATE_USER
  NULL,                            -- UPDATE_TIME
  NULL                             -- UPDATE_USER
);

-- 17. 设置配送按钮
INSERT INTO `sys_resource` VALUES (
  '1990000000000020115',          -- ID
  '1990000000000020000',           -- PARENT_ID
  '设置配送',                      -- TITLE
  NULL,                            -- NAME
  '/dev/city/setDelivery',         -- CODE
  'BUTTON',                        -- CATEGORY
  NULL,                            -- MODULE
  NULL,                            -- PATH
  NULL,                            -- ICON
  NULL,                            -- COLOR
  NULL,                            -- COMPONENT
  NULL,                            -- PERMISSION
  NULL,                            -- EFFECT
  NULL,                            -- AFFIX
  NULL,                            -- VISIBLE
  115,                             -- SORT_CODE
  NULL,                            -- REMARK
  'NOT_DELETE',                    -- DELETE_FLAG
  NOW(),                           -- CREATE_TIME
  '1543837863788879871',           -- CREATE_USER
  NULL,                            -- UPDATE_TIME
  NULL                             -- UPDATE_USER
);

-- 18. 更新统计按钮
INSERT INTO `sys_resource` VALUES (
  '1990000000000020116',          -- ID
  '1990000000000020000',           -- PARENT_ID
  '更新统计',                      -- TITLE
  NULL,                            -- NAME
  '/dev/city/updateStatistics',    -- CODE
  'BUTTON',                        -- CATEGORY
  NULL,                            -- MODULE
  NULL,                            -- PATH
  NULL,                            -- ICON
  NULL,                            -- COLOR
  NULL,                            -- COMPONENT
  NULL,                            -- PERMISSION
  NULL,                            -- EFFECT
  NULL,                            -- AFFIX
  NULL,                            -- VISIBLE
  116,                             -- SORT_CODE
  NULL,                            -- REMARK
  'NOT_DELETE',                    -- DELETE_FLAG
  NOW(),                           -- CREATE_TIME
  '1543837863788879871',           -- CREATE_USER
  NULL,                            -- UPDATE_TIME
  NULL                             -- UPDATE_USER
);

-- ----------------------------
-- 权限脚本执行完成
-- ----------------------------