# 酒品管理模块 (wqs-plugin-wine)

## 模块概述

酒品管理模块是共享售酒机系统的核心业务模块，负责酒品相关的所有基础数据管理，包括酒品信息、分类管理、价格管理等功能。

## 功能范围

### ✅ 应该负责的功能
- 酒品基础信息管理（增删改查）
- 酒品分类体系管理
- 酒品价格管理和调整
- 酒品库存基础信息维护
- 酒品状态管理（上架/下架）
- 酒品图片和描述管理

### ❌ 不应涉及的功能
- 具体库存数量管理（由设备模块负责）
- 订单相关业务逻辑（由订单模块负责）
- 支付相关功能（由订单模块负责）
- 设备配置和控制（由设备模块负责）

## 开发规范

### 1. 数据库表检查流程 ⭐

**在创建实体类之前，必须先检查数据库表结构：**

1. **读取数据库SQL文件**
   ```bash
   # 查看根目录下的weiqisheng.sql文件
   # 搜索相关表结构：wine_product、wine_category、wine_price等
   ```

2. **检查表是否存在**
   - 如果表已存在：根据实际表结构创建实体类
   - 如果表不存在：先设计表结构，再创建实体类

3. **字段映射确认**
   - 确保实体类字段与数据库表字段完全匹配
   - 使用正确的数据类型和注解
   - 包含所有必要的MyBatis Plus注解

### 2. 目录结构

```
wqs-plugin-wine/
├── src/main/java/vip/wqs/wine/
│   ├── core/                           # 核心配置
│   │   └── config/                     # 配置类
│   │       └── WineConfigure.java      # Wine插件配置
│   │   ├── modular/
│   │   │   ├── product/                    # 酒品管理
│   │   │   │   ├── controller/             # 控制器层
│   │   │   │   ├── service/                # 服务层
│   │   │   │   │   └── impl/               # 服务实现
│   │   │   │   ├── mapper/                 # 数据访问层
│   │   │   │   │   ├── WineProductMapper.java
│   │   │   │   │   └── mapping/            # MyBatis映射文件
│   │   │   │   │       └── WineProductMapper.xml
│   │   │   │   ├── entity/                 # 实体类（继承CommonEntity）
│   │   │   │   ├── param/                  # 参数类
│   │   │   │   │   ├── WineProductIdParam.java
│   │   │   │   │   ├── WineProductAddParam.java
│   │   │   │   │   ├── WineProductEditParam.java
│   │   │   │   │   ├── WineProductPageParam.java
│   │   │   │   │   ├── WineProductExportParam.java
│   │   │   │   │   └── WineProductSelectorParam.java
│   │   │   │   ├── provider/               # 模块间调用接口
│   │   │   │   └── enums/                  # 枚举类
│   │   │   ├── category/                   # 酒品分类管理
│   │   │   │   ├── controller/             # 控制器层
│   │   │   │   ├── service/                # 服务层
│   │   │   │   │   └── impl/               # 服务实现
│   │   │   │   ├── mapper/                 # 数据访问层
│   │   │   │   │   ├── WineCategoryMapper.java
│   │   │   │   │   └── mapping/            # MyBatis映射文件
│   │   │   │   │       └── WineCategoryMapper.xml
│   │   │   │   ├── entity/                 # 实体类（继承CommonEntity）
│   │   │   │   ├── param/                  # 参数类
│   │   │   │   ├── provider/               # 模块间调用接口
│   │   │   │   └── enums/                  # 枚举类
│   │   │   └── price/                      # 酒品价格管理
│   │   │       ├── controller/             # 控制器层
│   │   │       ├── service/                # 服务层
│   │   │       │   └── impl/               # 服务实现
│   │   │       ├── mapper/                 # 数据访问层
│   │   │       │   └── mapping/            # MyBatis映射文件
│   │   │       ├── entity/                 # 实体类（继承CommonEntity）
│   │   │       ├── param/                  # 参数类
│   │   │       ├── provider/               # 模块间调用接口
│   │   │       └── enums/                  # 枚举类
│   └── enums/                          # 模块级枚举
└── pom.xml
```

### 3. 开发步骤

1. **数据库表检查** ⭐
   - 读取weiqisheng.sql文件
   - 确认相关表结构
   - 分析字段定义和约束

2. **实体类开发**
   - 根据数据库表结构创建实体类
   - 添加MyBatis Plus注解
   - 添加Swagger文档注解

3. **参数类开发**
   - 创建完整的参数类体系
   - 包含Id、Add、Edit、Page、Export、Selector参数

4. **Mapper接口开发**
   - 继承BaseMapper
   - 添加自定义查询方法

5. **Service接口和实现**
   - 定义标准CRUD方法
   - 实现业务逻辑

6. **Controller开发**
   - 实现标准REST接口
   - 添加权限控制注解
   - 添加操作日志注解

7. **权限脚本生成** ⭐
   - 生成wine_permission.sql权限脚本
   - 包含模块、菜单、按钮权限

### 4. 代码规范

- **包路径**: `vip.wqs.wine.modular.功能名.层级`
- **实体类规范**: 必须继承`vip.wqs.common.pojo.CommonEntity`
- **映射文件位置**: 放在每个模块的`mapper/mapping/`目录下，如`src/main/java/vip/wqs/wine/modular/product/mapper/mapping/`
- **权限控制**: 每个接口加`@SaCheckPermission`注解
- **日志记录**: 增删改操作加`@CommonLog`注解
- **参数校验**: 使用`@Valid`和`@Validated`注解
- **文档注解**: 使用Swagger 3.x注解
- **POM配置**: 主POM和启动类POM都需要添加wine插件依赖

### 5. 当前进度

#### API接口模块 (wqs-plugin-wine-api)
- ✅ WineProductApi - 酒品信息接口
- ✅ WineCategoryApi - 酒品分类接口  
- ✅ WinePriceApi - 酒品价格接口
- **完成度**: 100%

#### 业务模块 (wqs-plugin-wine)
- ✅ 目录结构创建
- ✅ WineProduct实体类（已根据数据库表结构更新，继承CommonEntity）
- ✅ 参数类开发（IdParam、AddParam、EditParam、PageParam、ExportParam、SelectorParam）
- ✅ Mapper接口开发（WineProductMapper + XML映射文件）
- ✅ Service开发（WineProductService接口 + 实现类）
- ✅ Controller开发（WineProductController REST接口）
- ✅ 权限脚本生成（wine_permission.sql）
- ✅ 版权声明规范化（已修复所有xiaonuo版权声明为WQS）
- ✅ POM配置完善（主POM + 启动类POM + plugin模块POM）
- ✅ 映射文件位置规范化（移动到正确位置）
- ✅ 编译验证通过
- ✅ Category模块开发（酒品分类管理）
  - ✅ WineCategory实体类（继承CommonEntity）
  - ✅ 参数类开发（IdParam、AddParam、EditParam、PageParam、ExportParam、SelectorParam）
  - ✅ Mapper接口开发（WineCategoryMapper + XML映射文件）
  - ✅ Service开发（WineCategoryService接口 + 实现类）
  - ✅ Controller开发（WineCategoryController REST接口）
  - ✅ 权限脚本生成（wine_category_permission.sql）
  - ✅ 编译验证通过
- ✅ Price模块开发（酒品价格管理）
  - ✅ WinePrice实体类（继承CommonEntity）
  - ✅ 参数类开发（IdParam、AddParam、EditParam、PageParam）
  - ✅ Mapper接口开发（WinePriceMapper + XML映射文件）
  - ✅ Service开发（WinePriceService接口 + 实现类）
  - ✅ Controller开发（WinePriceController REST接口）
  - ✅ 权限脚本生成（wine_price_permission.sql）
  - ✅ 编译验证通过
- **完成度**: 100%

## 数据库表结构

### wine_product 表
```sql
CREATE TABLE `wine_product` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `product_code` varchar(50) DEFAULT NULL COMMENT '酒品编码',
  `product_name` varchar(100) DEFAULT NULL COMMENT '酒品名称',
  `product_type` varchar(50) DEFAULT NULL COMMENT '酒品类型',
  `brand` varchar(100) DEFAULT NULL COMMENT '品牌',
  `alcohol_content` decimal(5,2) DEFAULT NULL COMMENT '酒精度数',
  `volume` int DEFAULT NULL COMMENT '净含量(ml)',
  `origin` varchar(100) DEFAULT NULL COMMENT '产地',
  `manufacturer` varchar(200) DEFAULT NULL COMMENT '生产厂家',
  `suggested_price` decimal(10,2) DEFAULT NULL COMMENT '建议零售价',
  `cost_price` decimal(10,2) DEFAULT NULL COMMENT '成本价',
  `image_url` varchar(500) DEFAULT NULL COMMENT '酒品图片URL',
  `description` text COMMENT '酒品描述',
  `status` varchar(20) DEFAULT 'ENABLE' COMMENT '状态',
  `sort_code` int DEFAULT NULL COMMENT '排序码',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `ext_json` longtext COMMENT '扩展信息',
  `delete_flag` varchar(1) DEFAULT 'N' COMMENT '删除标志',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(64) DEFAULT NULL COMMENT '创建用户',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_user` varchar(64) DEFAULT NULL COMMENT '更新用户',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='酒品信息表';
```

## 下一步计划

1. 开发WineProduct相关的参数类
2. 创建WineProductMapper接口和映射文件
3. 开发WineProductService接口和实现
4. 创建WineProductController
5. 生成wine_permission.sql权限脚本
6. 重复以上步骤完成WineCategory和WinePrice模块

## 注意事项

- 所有代码必须采用UTF-8编码
- 严格按照现有项目的代码规范和注解风格
- 每个模块完成后必须生成对应的权限脚本
- 实体类字段必须与数据库表结构完全匹配 