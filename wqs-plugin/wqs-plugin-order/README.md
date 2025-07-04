# 订单管理模块 (wqs-plugin-order)

## 📋 模块概述

订单管理模块负责共享售酒机订单的全生命周期管理，是整个业务流程的核心枢纽。该模块集成了酒品、设备、支付、佣金等多个子系统，提供完整的订单处理能力。

## 🎯 核心功能

### 1. 订单创建与管理
- **订单生成**：扫码购买时自动创建订单
- **订单验证**：库存检查、设备状态验证、价格计算
- **订单修改**：支持订单信息修改和取消

### 2. 订单状态流转
- **PENDING** → **PAID** → **DISPENSING** → **COMPLETED**
- **异常流转**：PENDING → CANCELLED、PAID → REFUNDED
- **状态监控**：实时跟踪订单状态变化

### 3. 支付集成
- **支付创建**：集成微信支付创建支付订单
- **支付回调**：处理支付成功/失败回调
- **退款处理**：支持订单退款和退款状态管理

### 4. 设备控制集成
- **出酒控制**：支付成功后自动控制设备出酒
- **状态同步**：实时同步设备出酒状态
- **异常处理**：设备故障时的订单处理

### 5. 佣金计算与分配
- **佣金计算**：基于订单金额自动计算各方佣金
- **佣金分配**：订单完成后自动分配佣金到各账户
- **分配记录**：详细记录佣金分配明细

## 🏗️ 技术架构

### 模块结构
```
wqs-plugin-order/
├── src/main/java/vip/wqs/order/
│   └── modular/manage/
│       ├── entity/           # 实体类
│       │   └── WineOrder.java
│       ├── param/            # 参数类
│       │   ├── WineOrderAddParam.java
│       │   ├── WineOrderEditParam.java
│       │   ├── WineOrderPageParam.java
│       │   ├── WineOrderIdParam.java
│       │   ├── WineOrderCreateParam.java
│       │   ├── WineOrderPayParam.java
│       │   └── WineOrderCancelParam.java
│       ├── result/           # 结果类
│       │   ├── WineOrderResult.java
│       │   └── WineOrderDetailResult.java
│       ├── mapper/           # 数据访问层
│       │   ├── WineOrderMapper.java
│       │   └── mapping/WineOrderMapper.xml
│       ├── service/          # 业务逻辑层
│       │   ├── WineOrderService.java
│       │   └── impl/WineOrderServiceImpl.java
│       ├── controller/       # 控制层
│       │   └── WineOrderController.java
│       └── enums/           # 枚举类
│           └── OrderStatusEnum.java
└── sql/                     # SQL脚本
    ├── order_permission.sql # 权限资源
    └── README.md           # 说明文档
```

### 数据表结构
- **wine_order**: 订单主表
  - 基础信息：订单号、用户、设备、酒品
  - 金额信息：数量、单价、总金额、服务费
  - 状态信息：订单状态、各时间节点
  - 扩展信息：取消原因、退款信息、备注

## 🔐 权限控制

### 模块权限
- **模块权限**: `order`
- **菜单权限**: `orderManage`

### 按钮权限
- `orderManagePage` - 查看订单列表
- `orderManageDetail` - 查看订单详情
- `orderManageCancel` - 取消订单
- `orderManageRefund` - 退款处理
- `orderManageDispense` - 手动出酒
- `orderManageExport` - 导出订单

## 📊 业务流程

### 1. 订单创建流程
```
扫码 → 选择酒品 → 验证库存 → 计算价格 → 创建订单 → 发起支付
```

### 2. 订单支付流程
```
支付创建 → 用户支付 → 支付回调 → 订单状态更新 → 触发出酒
```

### 3. 出酒流程
```
支付成功 → 设备控制 → 开始出酒 → 出酒完成 → 佣金分配 → 订单完成
```

### 4. 异常处理流程
```
支付超时 → 自动取消
设备故障 → 退款处理
用户取消 → 状态更新
```

## 🔗 模块依赖

### 依赖模块
- `wqs-plugin-wine-api` - 酒品信息和价格
- `wqs-plugin-device-api` - 设备控制和状态
- `wqs-plugin-store-api` - 门店信息
- `wqs-plugin-commission-api` - 佣金计算和分配
- `wqs-plugin-client-api` - 用户信息
- `wqs-plugin-auth-api` - 登录鉴权

### 被依赖模块
- `wqs-plugin-miniprogram` - 小程序订单接口
- 统计分析模块 - 订单数据统计

## 🚀 开发规范

### 1. 代码规范
- **实体类命名**: WineOrder
- **参数类命名**: WineOrder + 功能 + Param
- **接口路径**: /order/manage/{action}
- **权限编码**: orderManage + 功能

### 2. 状态管理
- **状态枚举**: OrderStatusEnum
- **状态流转**: 严格按照业务流程进行状态转换
- **状态验证**: 每次状态变更前进行合法性检查

### 3. 异常处理
- **业务异常**: 使用统一的业务异常类
- **事务控制**: 关键操作使用@Transactional注解
- **日志记录**: 详细记录订单状态变更日志

## 📈 开发进度

### ✅ 已完成功能
- [x] 插件架构搭建
- [x] POM配置和依赖管理
- [x] README文档创建

### 🔄 进行中功能
- [ ] 实体类和参数类开发
- [ ] 数据访问层开发
- [ ] 业务逻辑层开发
- [ ] 控制层开发
- [ ] 权限脚本生成

### 📅 待开发功能
- [ ] 支付集成
- [ ] 设备控制集成
- [ ] 佣金分配集成
- [ ] 前端管理页面
- [ ] 测试和优化

## 🎯 开发重点

1. **状态管理**: 订单状态的准确流转是核心
2. **集成能力**: 与多个子系统的无缝集成
3. **异常处理**: 完善的异常处理和恢复机制
4. **性能优化**: 高并发场景下的性能保证
5. **数据一致性**: 确保订单数据的完整性和一致性 