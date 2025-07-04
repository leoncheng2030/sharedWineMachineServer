# 支付管理插件

## 插件简介

支付管理插件为WQS系统提供完整的支付功能，包括支付记录管理和支付配置管理。支持多种支付方式：微信支付（小程序、H5、APP、扫码）、支付宝支付、余额支付等。

## 功能特性

### 1. 支付记录管理
- **查询功能**：支持按支付单号、订单号、支付方式、支付状态、用户ID、交易流水号、创建时间等条件查询
- **详情查看**：完整的支付记录信息展示，包括回调内容等详细信息
- **状态管理**：支持待支付、支付成功、支付失败、已取消、已退款、已过期等状态流转
- **数据导出**：支持支付记录数据导出（预留功能）

### 2. 支付配置管理
- **多支付方式**：支持微信支付、支付宝支付、余额支付等多种支付方式
- **配置管理**：完整的CRUD操作，包括新增、编辑、删除、批量删除
- **状态控制**：支持配置的启用/禁用状态管理
- **环境切换**：支持正式环境和沙箱环境配置
- **安全配置**：敏感信息（密钥）使用密码输入框保护
- **扩展性**：支持扩展配置JSON，便于后续功能扩展

## 安装部署

### 1. 数据库初始化

执行以下SQL文件初始化数据库：

```bash
# 1. 创建数据库表结构
mysql -u root -p your_database < wqs-plugin/wqs-plugin-payment/sql/payment_tables.sql

# 2. 初始化权限资源配置
mysql -u root -p your_database < wqs-plugin/wqs-plugin-payment/sql/payment_resource.sql
```

### 2. 编译部署

```bash
# 编译支付插件
mvn clean compile -pl wqs-plugin/wqs-plugin-payment -am

# 打包整个项目
mvn clean package -DskipTests
```

### 3. 配置说明

#### 微信支付配置
- **应用ID**：微信小程序/公众号的AppID
- **商户号**：微信支付商户号
- **应用密钥**：微信小程序/公众号的AppSecret
- **商户密钥**：微信支付商户密钥
- **私钥文件路径**：微信支付API私钥文件路径
- **证书文件路径**：微信支付API证书文件路径
- **证书序列号**：微信支付API证书序列号

#### 支付宝配置
- **应用ID**：支付宝应用的AppID
- **商户号**：支付宝商户号
- **应用私钥**：支付宝应用私钥
- **支付宝公钥**：支付宝公钥

#### 回调URL配置
- **支付回调URL**：支付成功后的异步通知地址
- **退款回调URL**：退款成功后的异步通知地址
- **支付成功跳转URL**：支付成功后的页面跳转地址

## 数据库表结构

### payment_record（支付记录表）
| 字段名 | 类型 | 说明 |
|--------|------|------|
| ID | varchar(64) | 主键 |
| PAYMENT_NO | varchar(64) | 支付单号（系统生成） |
| ORDER_ID | varchar(64) | 关联订单ID |
| ORDER_NO | varchar(64) | 订单号 |
| USER_ID | varchar(64) | 用户ID |
| PAY_TYPE | varchar(20) | 支付方式 |
| PAY_AMOUNT | decimal(10,2) | 支付金额 |
| ACTUAL_AMOUNT | decimal(10,2) | 实际支付金额 |
| PAY_STATUS | varchar(20) | 支付状态 |
| PAY_TIME | datetime | 支付时间 |
| TRANSACTION_ID | varchar(64) | 第三方交易流水号 |
| CALLBACK_CONTENT | longtext | 支付回调内容 |

### payment_config（支付配置表）
| 字段名 | 类型 | 说明 |
|--------|------|------|
| ID | varchar(64) | 主键 |
| CONFIG_NAME | varchar(100) | 配置名称 |
| PAY_TYPE | varchar(20) | 支付方式 |
| MERCHANT_ID | varchar(64) | 商户号 |
| APP_ID | varchar(64) | 应用ID |
| APP_SECRET | varchar(200) | 应用密钥 |
| MERCHANT_KEY | varchar(200) | 商户密钥 |
| NOTIFY_URL | varchar(200) | 支付回调URL |
| SANDBOX_MODE | int | 沙箱模式 |
| STATUS | varchar(20) | 状态 |

## API接口

### 支付记录接口

#### 1. 获取支付记录分页
- **接口地址**：`GET /payment/record/page`
- **权限标识**：`paymentRecordPage`
- **参数说明**：
  - `paymentNo`：支付单号
  - `orderNo`：订单号
  - `payType`：支付方式
  - `payStatus`：支付状态
  - `userId`：用户ID
  - `transactionId`：交易流水号
  - `startTime`、`endTime`：创建时间范围

#### 2. 获取支付记录详情
- **接口地址**：`GET /payment/record/detail`
- **权限标识**：`paymentRecordDetail`
- **参数说明**：
  - `id`：支付记录ID

### 支付配置接口

#### 1. 获取支付配置分页
- **接口地址**：`GET /payment/config/page`
- **权限标识**：`paymentConfigPage`

#### 2. 添加支付配置
- **接口地址**：`POST /payment/config/add`
- **权限标识**：`paymentConfigAdd`

#### 3. 编辑支付配置
- **接口地址**：`POST /payment/config/edit`
- **权限标识**：`paymentConfigEdit`

#### 4. 删除支付配置
- **接口地址**：`POST /payment/config/delete`
- **权限标识**：`paymentConfigDelete`

#### 5. 启用/禁用支付配置
- **接口地址**：`POST /payment/config/enable` 或 `/payment/config/disable`
- **权限标识**：`paymentConfigEnable` 或 `paymentConfigDisable`

## 前端页面

### 1. 支付记录管理页面
- **路径**：`/payment/record`
- **文件位置**：`wqs-admin-web/src/views/payment/record/index.vue`
- **功能**：支付记录查询、详情查看、数据导出

### 2. 支付配置管理页面
- **路径**：`/payment/config`
- **文件位置**：`wqs-admin-web/src/views/payment/config/index.vue`
- **功能**：支付配置的增删改查、状态管理

### 3. 支付配置表单页面
- **路径**：`/payment/config/form`
- **文件位置**：`wqs-admin-web/src/views/payment/config/form.vue`
- **功能**：支付配置的新增和编辑表单

## 支付方式枚举

| 枚举值 | 说明 |
|--------|------|
| WECHAT_MINI | 微信小程序支付 |
| WECHAT_H5 | 微信H5支付 |
| WECHAT_APP | 微信APP支付 |
| WECHAT_SCAN | 微信扫码支付 |
| ALIPAY_APP | 支付宝APP支付 |
| ALIPAY_H5 | 支付宝H5支付 |
| ALIPAY_SCAN | 支付宝扫码支付 |
| BALANCE | 余额支付 |

## 支付状态枚举

| 枚举值 | 说明 |
|--------|------|
| PENDING | 待支付 |
| SUCCESS | 支付成功 |
| FAILED | 支付失败 |
| CANCELED | 已取消 |
| REFUNDED | 已退款 |
| EXPIRED | 已过期 |

## 权限配置

插件自动创建以下权限资源：

### 模块权限
- 支付管理模块：`payment`

### 菜单权限
- 支付管理目录：`paymentManage`
- 支付记录管理：`paymentRecord`
- 支付配置管理：`paymentConfig`

### 按钮权限
- 支付记录相关：`paymentRecordPage`、`paymentRecordDetail`、`paymentRecordExport`
- 支付配置相关：`paymentConfigPage`、`paymentConfigAdd`、`paymentConfigEdit`、`paymentConfigDelete`、`paymentConfigEnable`、`paymentConfigDisable`、`paymentConfigBatchDelete`、`paymentConfigSelector`

## 注意事项

1. **安全性**：
   - 支付配置中的敏感信息（密钥）已加密处理
   - 支付回调需要验签确保安全性
   - 建议在生产环境中使用HTTPS

2. **性能优化**：
   - 支付记录表建议定期归档历史数据
   - 支付配置建议启用缓存机制

3. **扩展性**：
   - 支持通过扩展配置JSON添加自定义配置
   - 支持新增支付方式的扩展开发

## 更新日志

### v1.0.0 (2025-01-30)
- 初始版本发布
- 支持支付记录管理
- 支持支付配置管理
- 支持多种支付方式
- 完整的权限控制体系 