# API数据转换最佳实践

## 📋 概述

本文档基于 `WineOrderApiProvider` 的优化经验，总结了API数据转换的最佳实践。参考 `AuthServiceImpl.getClientLoginUser()` 的设计模式，形成标准化的开发规范。

## 🎯 设计原则

### 1. 简化转换过程
- 使用 `BeanUtil.copyProperties()` 自动复制基础字段
- 避免手动逐个字段赋值的重复代码
- 减少维护成本和出错概率

### 2. 职责分离
- 转换逻辑分解为多个专门的方法
- 每个方法负责特定类型的信息填充
- 提高代码可读性和可维护性

### 3. 动态填充
- 通过专门的方法填充扩展信息
- 支持关联数据的动态获取
- 灵活处理业务逻辑字段

## 🚀 标准实现模式

### 基础结构模板

```java
@Service
public class XxxApiProvider implements XxxApi {
    
    @Resource
    private XxxService xxxService;
    
    @Resource 
    private RelatedApi relatedApi; // 关联API
    
    @Override
    public XxxPojo getXxxById(String id) {
        XxxEntity entity = xxxService.getById(id);
        return entity != null ? convertToXxxPojo(entity) : null;
    }
    
    @Override
    public List<XxxPojo> getXxxsByUserId(String userId) {
        List<XxxEntity> entities = xxxService.getByUserId(userId);
        return entities.stream()
                .map(this::convertToXxxPojo)
                .collect(Collectors.toList());
    }
    
    @Override
    public Page<XxxPojo> getXxxPage(XxxApiPageParam param) {
        // 转换API参数为Service参数
        XxxPageParam serviceParam = new XxxPageParam();
        BeanUtil.copyProperties(param, serviceParam);
        
        // 调用Service的分页方法
        Page<XxxEntity> entityPage = xxxService.page(serviceParam);
        
        // 转换结果
        Page<XxxPojo> resultPage = new Page<>(entityPage.getCurrent(), entityPage.getSize());
        resultPage.setTotal(entityPage.getTotal());
        resultPage.setPages(entityPage.getPages());
        
        List<XxxPojo> records = entityPage.getRecords().stream()
                .map(this::convertToXxxPojo)
                .collect(Collectors.toList());
        resultPage.setRecords(records);
        
        return resultPage;
    }
    
    // === 私有转换方法 ===
    
    /**
     * 将XxxEntity实体转换为XxxPojo
     * 参考AuthServiceImpl的设计模式，使用BeanUtils简化转换过程
     *
     * @param entity Xxx实体
     * @return Xxx响应对象
     */
    private XxxPojo convertToXxxPojo(XxxEntity entity) {
        if (entity == null) {
            return null;
        }
        
        XxxPojo pojo = new XxxPojo();
        // 使用BeanUtils.copyProperties进行基础字段复制
        BeanUtil.copyProperties(entity, pojo);
        
        // 动态填充扩展信息（参考AuthServiceImpl.fillSaBaseClientLoginUserAndUpdateCache的设计）
        fillXxxExtInfo(pojo, entity);
        
        return pojo;
    }
    
    /**
     * 动态填充Xxx扩展信息
     * 参考AuthServiceImpl.fillSaBaseClientLoginUserAndUpdateCache的设计模式
     *
     * @param pojo Xxx响应对象
     * @param entity Xxx实体
     */
    private void fillXxxExtInfo(XxxPojo pojo, XxxEntity entity) {
        // 填充关联信息
        fillRelatedInfo(pojo, entity);
        
        // 填充业务逻辑字段
        fillBusinessLogicFields(pojo, entity);
    }
    
    /**
     * 填充关联信息
     *
     * @param pojo Xxx响应对象
     * @param entity Xxx实体
     */
    private void fillRelatedInfo(XxxPojo pojo, XxxEntity entity) {
        // 填充设备信息
        if (StrUtil.isNotBlank(entity.getDeviceId())) {
            try {
                DevicePojo deviceInfo = deviceApi.getDeviceDetail(entity.getDeviceId());
                if (deviceInfo != null) {
                    pojo.setDeviceName(deviceInfo.getDeviceName());
                    pojo.setDeviceCode(deviceInfo.getDeviceCode());
                    pojo.setDeviceLocation(deviceInfo.getLocation());
                }
            } catch (Exception e) {
                // 关联信息获取失败时，设置默认值
                pojo.setDeviceName("默认设备");
                pojo.setDeviceCode("未知");
                pojo.setDeviceLocation("未知位置");
            }
        }
        
        // 填充用户信息
        if (StrUtil.isNotBlank(entity.getUserId())) {
            try {
                UserPojo userInfo = userApi.getUserDetail(entity.getUserId());
                if (userInfo != null) {
                    pojo.setUserName(userInfo.getName());
                    pojo.setUserPhone(userInfo.getPhone());
                }
            } catch (Exception e) {
                // 用户信息获取失败时，设置默认值
                pojo.setUserName("未知用户");
            }
        }
    }
    
    /**
     * 填充业务逻辑字段
     * 参考AuthServiceImpl中权限、角色等信息的填充逻辑
     *
     * @param pojo Xxx响应对象
     * @param entity Xxx实体
     */
    private void fillBusinessLogicFields(XxxPojo pojo, XxxEntity entity) {
        String status = entity.getStatus();
        
        // 设置状态显示文本
        switch (status) {
            case "PENDING":
                pojo.setStatusText("待处理");
                break;
            case "PROCESSING":
                pojo.setStatusText("处理中");
                break;
            case "COMPLETED":
                pojo.setStatusText("已完成");
                break;
            case "CANCELLED":
                pojo.setStatusText("已取消");
                break;
            default:
                pojo.setStatusText("未知状态");
        }
        
        // 设置操作权限（业务专用字段）
        pojo.setCanEdit("PENDING".equals(status) || "PROCESSING".equals(status));
        pojo.setCanCancel("PENDING".equals(status));
        pojo.setCanDelete("CANCELLED".equals(status));
        
        // 设置计算字段
        BigDecimal amount = entity.getAmount() != null ? entity.getAmount() : BigDecimal.ZERO;
        BigDecimal fee = entity.getFee() != null ? entity.getFee() : BigDecimal.ZERO;
        pojo.setTotalAmount(amount.add(fee));
        
        // 设置默认值
        pojo.setDiscountAmount(BigDecimal.ZERO);
        pojo.setPayType("WECHAT_PAY");
    }
}
```

## 📝 核心要点

### 1. 使用 BeanUtil.copyProperties

```java
// ✅ 推荐：使用BeanUtils自动复制基础字段
BeanUtil.copyProperties(entity, pojo);

// ❌ 避免：手动逐个字段赋值
pojo.setId(entity.getId());
pojo.setName(entity.getName());
pojo.setCreateTime(entity.getCreateTime());
pojo.setUpdateTime(entity.getUpdateTime());
// ... 50+ 行重复代码
```

**优势**：
- 减少代码量：从 50+ 行减少到 1 行
- 自动映射：字段名相同时自动复制
- 维护友好：新增字段时无需修改转换代码

### 2. 职责分离

```java
// ✅ 推荐：分离不同类型的填充逻辑
private void fillXxxExtInfo(XxxPojo pojo, XxxEntity entity) {
    fillRelatedInfo(pojo, entity);        // 关联信息
    fillBusinessLogicFields(pojo, entity); // 业务逻辑
}

// ❌ 避免：所有逻辑混在一个方法中
private void fillAllInfo(XxxPojo pojo, XxxEntity entity) {
    // 100+ 行混合逻辑...
}
```

**优势**：
- 代码清晰：每个方法职责单一
- 易于维护：修改某类信息时只需关注对应方法
- 便于测试：可以单独测试每个填充方法

### 3. 优雅的错误处理

```java
// ✅ 推荐：优雅的错误处理和默认值
try {
    DevicePojo deviceInfo = deviceApi.getDeviceDetail(entity.getDeviceId());
    if (deviceInfo != null) {
        pojo.setDeviceName(deviceInfo.getDeviceName());
        pojo.setDeviceCode(deviceInfo.getDeviceCode());
    }
} catch (Exception e) {
    // 外部API调用失败时，设置默认值，不影响主流程
    pojo.setDeviceName("默认设备");
    pojo.setDeviceCode("未知");
    log.warn("获取设备信息失败，deviceId: {}, error: {}", entity.getDeviceId(), e.getMessage());
}

// ❌ 避免：异常向上抛出，影响主流程
DevicePojo deviceInfo = deviceApi.getDeviceDetail(entity.getDeviceId()); // 可能抛异常
```

**优势**：
- 健壮性：外部API异常不影响主流程
- 用户体验：显示默认值而非错误信息
- 可观测性：记录警告日志便于排查问题

### 4. 性能考虑

```java
// ✅ 推荐：批量处理时的性能优化
@Override
public List<XxxPojo> getXxxList(String userId) {
    List<XxxEntity> entities = xxxService.getByUserId(userId);
    
    // 可以考虑批量获取关联信息以提升性能
    // Map<String, DevicePojo> deviceMap = deviceApi.getDevicesByIds(deviceIds);
    
    return entities.stream()
            .map(this::convertToXxxPojo)
            .collect(Collectors.toList());
}

// 🔄 优化：对于大量数据，考虑分页或批量查询
@Override
public Page<XxxPojo> getXxxPage(XxxApiPageParam param) {
    Page<XxxEntity> entityPage = xxxService.page(serviceParam);
    
    // 批量获取关联信息（如果需要）
    Set<String> deviceIds = entityPage.getRecords().stream()
            .map(XxxEntity::getDeviceId)
            .filter(StrUtil::isNotBlank)
            .collect(Collectors.toSet());
    Map<String, DevicePojo> deviceMap = deviceApi.getDevicesByIds(deviceIds);
    
    // 转换时使用缓存的关联信息
    List<XxxPojo> records = entityPage.getRecords().stream()
            .map(entity -> convertToXxxPojo(entity, deviceMap))
            .collect(Collectors.toList());
    
    // ...
}
```

## 🎨 命名约定

### 方法命名
- 转换方法：`convertTo{目标类型}()`
- 填充方法：`fill{信息类型}Info()`
- 业务逻辑：`fillBusinessLogicFields()`
- 关联信息：`fillRelatedInfo()`

### 示例
```java
// 转换方法
private WineOrderPojo convertToWineOrderPojo(WineOrder entity)
private UserPojo convertToUserPojo(User entity)

// 填充方法
private void fillWineOrderExtInfo(WineOrderPojo pojo, WineOrder entity)
private void fillUserExtInfo(UserPojo pojo, User entity)

// 具体填充
private void fillRelatedInfo(WineOrderPojo pojo, WineOrder entity)
private void fillBusinessLogicFields(WineOrderPojo pojo, WineOrder entity)
```

## 📚 注释规范

### 转换方法注释
```java
/**
 * 将WineOrder实体转换为WineOrderPojo
 * 参考AuthServiceImpl的设计模式，使用BeanUtils简化转换过程
 *
 * @param entity 订单实体
 * @return 订单响应对象
 */
private WineOrderPojo convertToWineOrderPojo(WineOrder entity) {
    // 实现代码
}
```

### 填充方法注释
```java
/**
 * 动态填充订单扩展信息
 * 参考AuthServiceImpl.fillSaBaseClientLoginUserAndUpdateCache的设计模式
 *
 * @param pojo 订单响应对象
 * @param entity 订单实体
 */
private void fillWineOrderExtInfo(WineOrderPojo pojo, WineOrder entity) {
    // 实现代码
}
```

## 🔧 实际案例：WineOrderApiProvider

基于此规范优化的 `WineOrderApiProvider` 实现：

### 优化前问题
- 手动映射 50+ 个字段
- 转换逻辑混乱，难以维护
- 缺乏统一的错误处理
- 性能考虑不足

### 优化后效果
- 使用 `BeanUtil.copyProperties` 自动复制基础字段
- 职责分离：`fillRelatedInfo` + `fillBusinessLogicFields`
- 统一的错误处理和默认值设置
- 清晰的代码结构和注释

### 核心代码结构
```java
private WineOrderPojo convertToWineOrderPojo(WineOrder wineOrder) {
    WineOrderPojo pojo = new WineOrderPojo();
    BeanUtil.copyProperties(wineOrder, pojo);           // 基础字段复制
    fillWineOrderExtInfo(pojo, wineOrder);              // 扩展信息填充
    return pojo;
}

private void fillWineOrderExtInfo(WineOrderPojo pojo, WineOrder wineOrder) {
    fillRelatedInfo(pojo, wineOrder);                   // 设备、酒品信息
    fillBusinessLogicFields(pojo, wineOrder);           // 状态、权限、计算字段
}
```

## ⚠️ 注意事项

### 1. 字段映射
- 确保实体类和Pojo类中同名字段类型兼容
- 对于类型不同的字段，需要手动处理
- 注意日期类型的转换（Date vs LocalDateTime）

### 2. 性能考虑
- 对于大量数据，考虑批量获取关联信息
- 避免在循环中调用外部API
- 必要时使用缓存机制

### 3. 异常处理
- 外部API调用必须有异常捕获
- 设置合理的默认值
- 记录适当的日志信息

### 4. 测试验证
- 验证基础字段复制的正确性
- 测试关联信息获取失败的场景
- 确保业务逻辑字段的计算正确

## 📈 效果评估

采用此规范后的预期效果：

### 开发效率
- 减少 70% 的转换代码量
- 新增字段时无需修改转换逻辑
- 提高代码复用性

### 代码质量
- 统一的代码结构和风格
- 更好的可读性和可维护性
- 减少重复代码和潜在bug

### 团队协作
- 统一的开发规范
- 降低代码review成本
- 便于新人快速上手

---

**重要提醒**：
- 新开发的API实现类必须遵循此规范
- 现有代码逐步重构时也应采用此模式  
- 遇到特殊情况需要偏离规范时，必须在代码中说明原因
- 定期review和优化，确保规范的实用性和时效性 