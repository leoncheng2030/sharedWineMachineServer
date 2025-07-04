# 城市选择组件使用说明

## 组件概述

本项目提供了两个城市选择组件，分别适用于不同的使用场景：

1. **citySelectorPlus.vue** - 完整的城市选择弹框组件
2. **citySelector.vue** - 简化的三级联动选择组件

## 1. citySelectorPlus.vue - 完整城市选择器

### 功能特性

- 🏗️ **三级联动**: 省份 → 城市 → 区县三级联动选择
- 🔥 **热门城市**: 支持热门城市快捷选择
- 🔍 **智能搜索**: 支持城市名称和拼音搜索
- 📊 **数据展示**: 显示城市层级、热门标识、状态等信息
- 🎯 **单选/多选**: 支持单选和多选两种模式
- 📄 **分页加载**: 支持大数据量分页展示
- 🎨 **美观界面**: 基于Ant Design Vue的现代化界面

### 使用方法

```vue
<template>
  <div>
    <!-- 触发按钮 -->
    <a-button @click="showCitySelector">选择城市</a-button>
    
    <!-- 城市选择器组件 -->
    <city-selector-plus 
      ref="citySelectorRef"
      :radio-model="false"
      :data-is-converter-flw="false"
      @on-back="handleCitySelected"
    />
  </div>
</template>

<script setup>
import CitySelectorPlus from '@/components/Selector/citySelectorPlus.vue'

const citySelectorRef = ref()

// 显示城市选择器
const showCitySelector = () => {
  // 传入已选择的城市ID列表（可选）
  const selectedIds = ['110000', '120000']
  citySelectorRef.value.showCitySelectorModal(selectedIds)
}

// 处理城市选择结果
const handleCitySelected = (selectedCities) => {
  console.log('选择的城市:', selectedCities)
  // selectedCities 是一个数组，包含选中的城市信息
}
</script>
```

### 组件属性

| 属性名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| radioModel | Boolean | false | 是否单选模式 |
| dataIsConverterFlw | Boolean | false | 是否转换为工作流格式 |
| checkedCityListApi | Function | null | 自定义已选择数据API |

### 组件方法

| 方法名 | 参数 | 说明 |
|--------|------|------|
| showCitySelectorModal | ids: Array | 打开城市选择弹框 |

### 事件

| 事件名 | 参数 | 说明 |
|--------|------|------|
| onBack | selectedCities: Array | 城市选择完成回调 |

## 2. citySelector.vue - 简化城市选择器

### 功能特性

- 🎯 **三级联动**: 省份 → 城市 → 区县联动选择
- 📝 **表单集成**: 完美集成到表单中使用
- 🔄 **双向绑定**: 支持v-model双向数据绑定
- 🎨 **简洁界面**: 简洁的下拉选择界面
- 🔍 **搜索功能**: 支持下拉搜索
- 📱 **响应式**: 自适应不同屏幕尺寸

### 使用方法

```vue
<template>
  <a-form :model="formData" layout="vertical">
    <a-form-item label="所在地区">
      <city-selector 
        v-model:value="formData.cityInfo"
        :show-district="true"
        :disabled="false"
        @change="handleCityChange"
      />
    </a-form-item>
  </a-form>
</template>

<script setup>
import CitySelector from '@/components/Selector/citySelector.vue'

const formData = reactive({
  cityInfo: {
    provinceCode: '',
    provinceName: '',
    cityCode: '',
    cityName: '',
    districtCode: '',
    districtName: '',
    fullAddress: ''
  }
})

// 处理城市选择变化
const handleCityChange = (cityInfo) => {
  console.log('城市信息变化:', cityInfo)
  // cityInfo 包含完整的城市信息
}
</script>
```

### 组件属性

| 属性名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| value | Object | {} | 当前选择的城市信息 |
| disabled | Boolean | false | 是否禁用 |
| showDistrict | Boolean | true | 是否显示区县选择 |
| placeholder | Object | 见下方 | 占位符配置 |

### placeholder 默认值
```javascript
{
  province: '请选择省份',
  city: '请选择城市', 
  district: '请选择区县'
}
```

### 组件方法

| 方法名 | 参数 | 返回值 | 说明 |
|--------|------|--------|------|
| getCurrentValue | - | Object | 获取当前选择的值 |
| clearSelection | - | - | 清空选择 |

### 事件

| 事件名 | 参数 | 说明 |
|--------|------|------|
| update:value | cityInfo: Object | 值更新事件 |
| change | cityInfo: Object | 选择变化事件 |

### 数据结构

选择完成后返回的数据结构：

```javascript
{
  provinceCode: '110000',        // 省份编码
  provinceName: '北京市',        // 省份名称
  cityCode: '110100',           // 城市编码
  cityName: '北京市',           // 城市名称
  districtCode: '110101',       // 区县编码
  districtName: '东城区',       // 区县名称
  fullAddress: '北京市东城区'    // 完整地址
}
```

## API接口说明

城市选择组件依赖以下API接口：

| 接口 | 方法 | 说明 |
|------|------|------|
| /wine/city/level | GET | 根据层级获取城市列表 |
| /wine/city/children | GET | 根据父级编码获取下级城市 |
| /wine/city/hot | GET | 获取热门城市列表 |
| /wine/city/page | GET | 获取城市分页列表 |
| /wine/city/detail | GET | 获取城市详情 |

## 使用建议

### 场景选择

1. **使用 citySelectorPlus.vue 的场景**：
   - 需要选择多个城市
   - 需要搜索功能
   - 需要显示热门城市
   - 需要查看城市详细信息

2. **使用 citySelector.vue 的场景**：
   - 表单中的地址选择
   - 简单的单个城市选择
   - 需要紧凑的界面布局
   - 需要与其他表单控件对齐

### 最佳实践

1. **数据缓存**: 省份数据可以缓存，避免重复请求
2. **错误处理**: 网络异常时提供友好的错误提示
3. **加载状态**: 显示加载状态，提升用户体验
4. **数据校验**: 在表单提交前校验城市选择的完整性

## 注意事项

1. 确保后端API接口已正确实现
2. 数据库中需要有完整的城市数据
3. 组件依赖Ant Design Vue，确保已正确引入
4. 建议在项目中统一城市数据的编码规范

## 更新日志

### v1.0.0 (2025-01-30)
- ✅ 完成基础的三级联动功能
- ✅ 实现热门城市快捷选择
- ✅ 支持搜索和分页
- ✅ 提供完整版和简化版两个组件
- ✅ 完善的文档和使用示例 