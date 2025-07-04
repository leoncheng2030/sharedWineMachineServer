# 设备管理模块 (wqs-plugin-device)

## 📋 模块概述

设备管理模块负责共享售酒机设备的全生命周期管理，包括设备信息、库存管理、配置管理等核心功能。

## 🎯 功能范围

### 核心功能
- **设备信息管理**: 设备基础信息、状态、位置等
- **设备库存管理**: 设备内酒品库存、补货记录  
- **设备配置管理**: 设备参数、出酒配置

### 数据表
- `wine_device`: 设备信息表
- `wine_device_stock`: 设备酒品库存表
- `wine_device_config`: 设备配置表

## 🏗 模块结构

```
wqs-plugin-device/
├── src/main/java/vip/wqs/device/
│   ├── modular/                    # 业务功能模块
│   │   ├── info/                   # 设备信息管理
│   │   ├── stock/                  # 设备库存管理
│   │   └── config/                 # 设备配置管理
│   └── core/                       # 核心配置
├── sql/                           # SQL脚本
│   ├── device_init.sql           # 初始化脚本
│   ├── device_data.sql           # 基础数据
│   └── device_permission.sql     # 权限资源
└── README.md                     # 说明文档
```

## 🔐 权限控制

- **模块权限**: `device`
- **菜单权限**: `deviceInfo`, `deviceStock`, `deviceConfig`
- **按钮权限**: `deviceInfoAdd`, `deviceInfoEdit`, `deviceInfoDelete` 等

## 🚀 开发规范

- 严格按照BizUser模块的代码结构
- 包路径: `vip.wqs.device.modular.功能名.层级`
- 权限编码: `device功能名操作`
- 实体类命名: `WineDevice`, `WineDeviceStock`, `WineDeviceConfig`

## 📊 开发状态

- [x] 模块架构设计
- [x] 目录结构创建
- [ ] 实体类开发
- [ ] CRUD接口开发
- [ ] 权限脚本生成
- [ ] 测试与优化

## 🔗 依赖关系

- 依赖酒品管理模块（库存管理需要关联酒品信息）
- 被订单管理模块依赖（订单需要关联设备信息） 