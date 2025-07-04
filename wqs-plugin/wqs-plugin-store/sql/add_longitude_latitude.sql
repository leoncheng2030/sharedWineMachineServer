-- 为门店表添加经纬度字段
-- 作者: AI助手
-- 日期: 2025-01-29

-- 添加经度字段
ALTER TABLE wine_store ADD COLUMN longitude DOUBLE COMMENT '经度';

-- 添加纬度字段  
ALTER TABLE wine_store ADD COLUMN latitude DOUBLE COMMENT '纬度';

-- 创建经纬度索引，用于地理位置查询优化
CREATE INDEX idx_wine_store_location ON wine_store(longitude, latitude);

-- 注释：
-- 1. longitude: 经度，范围 -180 到 180
-- 2. latitude: 纬度，范围 -90 到 90
-- 3. 索引将提高基于地理位置的查询性能 