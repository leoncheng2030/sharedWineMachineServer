<div align="center">
    <p align="center">
        <img src="wqs-admin-web/public/img/logo.png" height="150" alt="logo"/>
    </p>
</div>

## 框架介绍

Snowy（SnowyAdmin）是国内首个国密前后端分离快速开发平台，集成国密加解密插件，
软件层面完全符合等保测评要求，同时实现国产化机型、中间件、数据库适配，是您的不二之选！
技术框架与密码结合，让更多的人认识密码，使用密码；更是让前后分离“密”不可分。

采用SpringBoot+MybatisPlus+AntDesignVue+Vite 等更多组件及前沿技术开发，注释丰富，代码简洁，开箱即用！

Snowy谐音“小诺”，恰应小诺团队名称；意思为”下雪的、纯洁的“，寓意框架追求简洁至上，大道至简。

<p align="center">     
    <p align="center">
        <a href="https://gitcode.com/xiaonuobase/Snowy">
            <img src="https://gitcode.com/xiaonuobase/Snowy/star/badge.svg" alt="bootstrap">
        </a>
        <a href="https://gitee.com/xiaonuobase/snowy">
            <img src="https://gitee.com/xiaonuobase/snowy/badge/star.svg?theme=dark" alt="Gitee star">
        </a>
        <a href="https://gitee.com/xiaonuobase/snowy">
            <img src="https://gitee.com/xiaonuobase/snowy/badge/fork.svg?theme=dark" alt="Gitee fork">
        </a>
        <a href="https://www.antdv.com/docs/vue/introduce-cn/">
            <img src="https://img.shields.io/badge/vue-3-blue.svg" alt="bootstrap">
        </a> 
        <a href="http://spring.io/projects/spring-boot">
            <img src="https://img.shields.io/badge/vite-5-green.svg" alt="spring-boot">
        </a>
        <a href="https://www.antdv.com/docs/vue/introduce-cn/">
            <img src="https://img.shields.io/badge/vue--ant--design-4-blue.svg" alt="bootstrap">
        </a> 
        <a href="http://spring.io/projects/spring-boot">
            <img src="https://img.shields.io/badge/spring--boot-3-green.svg" alt="spring-boot">
        </a>
        <a href="http://mp.baomidou.com">
            <img src="https://img.shields.io/badge/mybatis--plus-3-blue.svg" alt="mybatis-plus">
        </a>  
        <a href="./LICENSE">
            <img src="https://img.shields.io/badge/license-Apache%202-red" alt="license Apache 2.0">
        </a>
        <a href="https://old.murphysec.com/dr/mQ1xAybeOLMLOxH8pU" alt="OSCS Status">
            <img src="https://www.oscs1024.com/platform/badge//xiaonuobase/snowy.git.svg?size=small"/>
        </a>
    </p>
</p>

## 快速链接

gitee下载地址：[https://gitee.com/xiaonuobase/snowy](https://gitee.com/xiaonuobase/snowy)

github下载地址（镜像）：[https://github.com/xiaonuobase/Snowy](https://github.com/xiaonuobase/Snowy)

gitcode下载地址：[https://gitcode.com/xiaonuobase/Snowy](https://gitcode.com/xiaonuobase/Snowy)

演示地址：[https://snowy.xiaonuo.vip](https://snowy.xiaonuo.vip)

文档地址：[https://xiaonuo.vip/doc](https://xiaonuo.vip/doc)

## 🔴 重要：MDC开发规范

**在开始开发前，请务必阅读并严格遵守 [MDC开发规范手册](../MDC-开发规范手册.md)**

MDC (Master Development Code) 是本项目的强制开发标准，包含：
- 🎯 核心开发原则（设计先行、一致性至上等）
- 📝 统一命名规范（数据库、Java、前端字段命名一致）
- 🔄 标准开发流程（设计→开发→测试的完整流程）
- ✅ 质量检查清单（每个阶段的强制检查项）
- ⚠️ 违规处理机制（严重违规零容忍）

**违反MDC规范的代码将不允许合并到主分支！**

## 快速启动

全栈工程师推荐idea

### 前端支撑

| 插件      | 版本  | 用途             |
|---------|-----|----------------|
| node.js | ≥18 | JavaScript运行环境 |

### 启动前端

```
npm install
```

```
npm run dev
```

### 后端支撑

| 插件     | 版本        | 用途     |
|--------|-----------|--------|
| jdk    | 17        | java环境 |
| lombok | idea内     | 代码简化插件 |
| maven  | 最新版       | 包管理工具  |
| redis  | 最新版       | 缓存库    |
| mysql  | 8.0 / 5.7 | 数据库    |

### 启动后端

开发工具内配置好maven并在代码中配置数据库即可启动

## 代码结构

Snowy3.0框架对代码以插件化的模式进行分包，使得包层级结构更加清晰合理，同时降低了耦合度，关于插件模块化开发的规范请查阅文档【SNOWY开源文档——前端手册or后端手册——开发规范】板块。

```
wqs-develop (共享售酒机后端)
  |-wqs-admin-web == 管理端前端
    |-public == 基础静态文件
    |-src == 前端源代码
      |-api == API接口转发
      |-assets == 静态文件
      |-components == VUE组件
      |-config == 基础配置
      |-layout == 基础布局
      |-locales == 多语言配置
      |-router == 基础路由配置
      |-store == Pinia缓存配置
      |-style == 样式风格配置
      |-utils == 工具类
      |-views == 所有视图界面
  |-wqs-common == 基础通用模块
  |-wqs-plugin == 业务插件包
    |-wqs-plugin-auth == 登录鉴权插件
    |-wqs-plugin-biz == 业务功能插件
    |-wqs-plugin-client == C端功能插件
    |-wqs-plugin-dev == 开发工具插件
    |-wqs-plugin-gen == 代码生成插件
    |-wqs-plugin-mobile == 移动端管理插件
    |-wqs-plugin-sys == 系统功能插件
    |-wqs-plugin-wine == 酒品管理插件
    |-wqs-plugin-device == 设备管理插件
    |-wqs-plugin-order == 订单管理插件
    |-wqs-plugin-commission == 佣金管理插件
    |-wqs-plugin-statistics == 统计分析插件
  |-wqs-plugin-api == 插件API包
    |-wqs-plugin-auth-api == 登录鉴权插件api接口
    |-wqs-plugin-biz-api == 业务功能插件api接口
    |-wqs-plugin-client-api == C端功能插件api接口
    |-wqs-plugin-dev-api == 开发工具插件api接口
    |-wqs-plugin-gen-api == 代码生成插件api接口
    |-wqs-plugin-mobile-api == 移动端管理插件api接口
    |-wqs-plugin-sys-api == 系统功能插件api接口
    |-wqs-plugin-wine-api == 酒品管理插件api接口
    |-wqs-plugin-device-api == 设备管理插件api接口
    |-wqs-plugin-order-api == 订单管理插件api接口
    |-wqs-plugin-commission-api == 佣金管理插件api接口
    |-wqs-plugin-statistics-api == 统计分析插件api接口
  |-wqs-web-app == 主启动模块
```

## 共享售酒机业务模块说明

### 业务插件模块

**wqs-plugin-wine (酒品管理模块)**
- 酒品信息管理：酒品基础信息、分类管理
- 价格管理：酒品定价、价格策略
- 库存基础信息：酒品库存配置

**wqs-plugin-device (设备管理模块)**
- 设备注册与绑定：设备信息管理、用户绑定
- 设备状态监控：在线状态、运行状态
- 设备配置管理：设备参数配置
- 设备库存管理：实时库存、库存同步
- 设备通信接口：WebSocket通信、硬件交互

**wqs-plugin-store (门店管理模块)** ✅ **新增完成**
- 门店信息管理：门店基础信息、地址管理
- 门店管理员分配：C端用户绑定、权限管理
- 定价权限控制：平台统一定价 vs 门店自定义定价
- 门店状态管理：启用/禁用状态控制
- 门店选择器：为其他模块提供门店选择功能

**wqs-plugin-order (订单管理模块)**
- 订单创建与管理：订单生命周期管理
- 支付集成：微信支付、支付状态管理
- 订单状态流转：下单→支付→出酒→完成
- 出酒控制逻辑：硬件控制、出酒监控

**wqs-plugin-commission (佣金管理模块)**
- 佣金配置管理：佣金比例、分配规则
- 佣金计算与分配：自动计算、实时分配
- 提现管理：提现申请、审核、到账
- 账户余额管理：用户账户、资金流水

**wqs-plugin-statistics (统计分析模块)**
- 销售统计：销售额、销量统计
- 设备运营数据：设备使用率、故障率
- 用户行为分析：用户画像、消费习惯
- 财务报表：收入报表、佣金报表

### 模块依赖关系

```
订单模块 ← 依赖 ← 酒品模块 + 设备模块
佣金模块 ← 依赖 ← 订单模块
统计模块 ← 依赖 ← 所有业务模块
```

### 核心业务流程

1. **用户购酒流程**：扫码 → 连接设备 → 选择酒品 → 支付 → 出酒 → 完成
2. **设备管理流程**：设备注册 → 绑定管理员 → 配置参数 → 上架酒品 → 运营监控
3. **佣金分配流程**：订单完成 → 佣金计算 → 自动分配 → 提现申请 → 资金到账