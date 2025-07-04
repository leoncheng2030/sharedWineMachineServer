-- =====================================================
-- 酒品表添加供应商字段
-- =====================================================
-- 为wine_product表添加供应商相关字段，支持供应商角色参与佣金分配
-- 创建日期：2025-01-30

-- 添加供应商客户端ID字段
ALTER TABLE wine_product ADD COLUMN supplier_id VARCHAR(64) COMMENT '供应商客户端ID';

-- 创建供应商索引，用于查询优化
CREATE INDEX idx_wine_product_supplier ON wine_product(supplier_id);

-- 注释：
-- 1. supplier_id: 供应商客户端ID，关联client_user表
-- 2. 索引将提高基于供应商的查询性能
-- 3. 支持供应商角色参与佣金分配功能 