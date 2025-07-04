# 门店管理模块 (wqs-plugin-store)

## 📋 模块概述

门店管理模块负责共享售酒机门店的全生命周期管理，是支持个性化定价和门店运营的核心模块。该模块为平台提供门店维度的管理能力，支持门店独立定价和精细化运营。

## 🎯 核心功能

### 1. 门店信息管理
- **门店基础信息**：门店编码、名称、地址、联系方式
- **门店状态管理**：启用/禁用状态控制
- **门店分类标签**：支持门店分类和标签管理

### 2. 门店管理员分配
- **C端用户绑定**：将C端用户指定为门店管理员
- **权限管理**：门店管理员权限控制
- **多管理员支持**：支持一个门店多个管理员

### 3. 定价权限控制
- **平台统一定价**：使用平台设定的统一价格
- **门店自定义定价**：门店可自主设定酒品价格
- **权限动态切换**：支持动态调整门店定价权限

### 4. 门店运营支持
- **门店选择器**：为其他模块提供门店选择功能
- **数据统计接口**：为统计分析提供门店维度数据
- **批量操作**：支持门店批量管理操作

## 🏗️ 技术架构

### 模块结构
```
wqs-plugin-store/
├── src/main/java/vip/wqs/store/
│   └── modular/manage/
│       ├── entity/           # 实体类
│       │   └── WineStore.java
│       ├── param/            # 参数类
│       │   ├── WineStoreAddParam.java
│       │   ├── WineStoreEditParam.java
│       │   ├── WineStorePageParam.java
│       │   └── WineStoreIdParam.java
│       ├── mapper/           # 数据访问层
│       │   ├── WineStoreMapper.java
│       │   └── mapping/WineStoreMapper.xml
│       ├── service/          # 业务逻辑层
│       │   ├── WineStoreService.java
│       │   └── impl/WineStoreServiceImpl.java
│       └── controller/       # 控制器层
│           └── WineStoreController.java
├── sql/                      # 数据库脚本
│   └── store_permission.sql
└── pom.xml
```

### 数据模型

#### WineStore 实体类
```java
@TableName("wine_store")
public class WineStore extends CommonEntity {
    @TableId(type = IdType.ASSIGN_ID)
    private String id;                    // 主键ID
    
    private String storeCode;             // 门店编码（唯一）
    private String storeName;             // 门店名称
    private String address;               // 门店地址
    private String contactPhone;         // 联系电话
    private String contactPerson;        // 联系人
    private String status;                // 状态(ENABLE/DISABLE)
    private String priceAuthority;        // 定价权限(PLATFORM/CUSTOM)
    private String managerId;             // 门店管理员ID
    private String description;           // 门店描述
    private Integer sortCode;             // 排序码
}
```

## 🔌 API 接口

### 基础 CRUD 接口
- `GET /store/manage/page` - 获取门店分页列表
- `POST /store/manage/add` - 新增门店
- `POST /store/manage/edit` - 编辑门店
- `POST /store/manage/delete` - 删除门店
- `GET /store/manage/detail` - 获取门店详情

### 状态管理接口
- `POST /store/manage/enable` - 启用门店
- `POST /store/manage/disable` - 禁用门店

### 特殊功能接口
- `GET /store/manage/list` - 获取门店列表
- `GET /store/manage/selector` - 门店选择器
- `POST /store/manage/batchDelete` - 批量删除门店
- `POST /store/manage/setPriceAuthority` - 设置定价权限
- `GET /store/manage/managerUserSelector` - 管理员用户选择器
- `GET /store/manage/export` - 导出门店数据

## 🔐 权限配置

### 菜单权限
- **门店管理目录** (`storeManageCatalog`) - CATALOG类型
- **门店信息管理** (`store_manage`) - MENU类型

### 按钮权限
- `storeManagePage` - 查看
- `storeManageAdd` - 新增
- `storeManageEdit` - 编辑
- `storeManageDelete` - 删除
- `storeManageDetail` - 详情
- `storeManageUpdateStatus` - 修改状态
- `storeManageSetPriceAuthority` - 设置定价权限

## 📊 业务流程

### 1. 门店注册流程
```
创建门店 → 设置基础信息 → 分配管理员 → 设置定价权限 → 启用门店
```

### 2. 定价权限管理流程
```
平台统一定价 ↔ 门店自定义定价
     ↓              ↓
使用平台价格    门店设置个性化价格
```

### 3. 门店运营流程
```
门店启用 → 设备关联 → 酒品上架 → 价格设置 → 正常运营
```

## 🔗 模块依赖

### 依赖模块
- `wqs-common` - 公共基础模块
- `wqs-plugin-auth-api` - 登录鉴权接口
- `wqs-plugin-client-api` - C端用户接口（门店管理员）

### 被依赖模块
- `wqs-plugin-device` - 设备管理模块（设备关联门店）
- `wqs-plugin-wine` - 价格管理模块（门店定价）
- `wqs-plugin-order` - 订单管理模块（门店订单）

## 🚀 开发规范

### 1. 代码规范
- **Mapper规范**：只继承BaseMapper，不定义自定义查询方法
- **查询规范**：所有查询使用QueryWrapper方式
- **事务规范**：涉及数据修改的方法使用@Transactional注解

### 2. 命名规范
- **实体类**：WineStore
- **参数类**：WineStore + 功能 + Param
- **接口路径**：/store/manage/{action}
- **权限码**：storeManage + 功能

### 3. 文件编码
- 所有文件必须采用UTF-8编码
- XML映射文件必须创建对应的标准映射文件

## 📈 开发进度

### ✅ 已完成功能
- [x] POM配置和模块结构
- [x] WineStore实体类和参数类
- [x] WineStoreMapper和XML映射文件
- [x] WineStoreService接口和实现类
- [x] WineStoreController REST API
- [x] 权限脚本和菜单配置
- [x] 编译测试通过

### ⏳ 待开发功能
- [ ] 前端页面开发
- [ ] API接口文档
- [ ] 单元测试
- [ ] 集成测试

## 🎯 业务价值

### 1. 支持个性化定价
- 门店可根据当地市场情况自主定价
- 提高门店运营灵活性和竞争力

### 2. 精细化运营管理
- 门店维度的数据统计和分析
- 支持差异化运营策略

### 3. 扩展性设计
- 为未来的门店分成、区域管理等功能预留接口
- 支持多级门店管理体系

## 📝 使用说明

### 1. 创建门店
```java
WineStoreAddParam param = new WineStoreAddParam();
param.setStoreCode("STORE001");
param.setStoreName("测试门店");
param.setAddress("北京市朝阳区");
param.setPriceAuthority("PLATFORM"); // 默认平台定价
wineStoreService.add(param);
```

### 2. 设置定价权限
```java
WineStoreIdParam idParam = new WineStoreIdParam();
idParam.setId("门店ID");
wineStoreService.setPriceAuthority(idParam, "CUSTOM"); // 改为自定义定价
```

### 3. 门店选择器
```java
WineStorePageParam param = new WineStorePageParam();
param.setStatus("ENABLE"); // 只查询启用的门店
List<WineStore> stores = wineStoreService.selector(param);
```

---

> 📅 **创建时间**: 2025年1月27日  
> 👨‍💻 **开发者**: WQS团队  
> 📋 **状态**: 后端开发完成，前端开发中  
> 🔄 **最后更新**: 2025年1月27日 