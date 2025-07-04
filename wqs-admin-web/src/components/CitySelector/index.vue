<template>
	<div class="city-selector">
		<!-- 级联选择模式 -->
		<a-cascader
			v-if="type === 'cascader'"
			v-model:value="selectedValue"
			:options="options"
			:placeholder="placeholder"
			:loading="loading"
			:show-search="true"
			:filter-option="filterOption"
			:display-render="displayRender"
			allow-clear
			change-on-select
			@change="handleChange"
			@clear="handleClear"
		/>

		<!-- 三输入框模式 -->
		<ThreeInputCitySelector
			v-else-if="type === 'three-input'"
			:value="value"
			:placeholders="placeholders"
			:show-district="showDistrict"
			@update:value="handleThreeInputChange"
			@change="handleThreeInputChange"
		/>
	</div>
</template>

<script setup>
	import { onMounted, ref, watch } from 'vue'
	import cityApi from '@/api/sys/cityApi'
	import ThreeInputCitySelector from './ThreeInputCitySelector.vue'

	// 定义props
	const props = defineProps({
		value: {
			type: [Array, Object],
			default: () => null
		},
		type: {
			type: String,
			default: 'cascader', // 'cascader' | 'three-input'
			validator: (value) => ['cascader', 'three-input'].includes(value)
		},
		placeholder: {
			type: String,
			default: '请选择省/市/区'
		},
		placeholders: {
			type: Object,
			default: () => ({
				province: '请选择省份',
				city: '请选择城市',
				district: '请选择区县'
			})
		},
		showDistrict: {
			type: Boolean,
			default: true
		}
	})

	// 定义emits
	const emit = defineEmits(['update:value', 'change'])

	// 响应式数据
	const selectedValue = ref([])
	const options = ref([])
	const loading = ref(false)

	// 加载省份数据
	const loadProvinces = async () => {
		try {
			loading.value = true
			const response = await cityApi.listByLevel(1)

			// 兼容两种响应格式
			let data = null
			if (response && response.code === 200 && response.data) {
				data = response.data
			} else if (Array.isArray(response)) {
				data = response
			}

			if (data && Array.isArray(data)) {
				options.value = data.map((province) => ({
					value: province.cityCode,
					label: province.cityName,
					isLeaf: false,
					children: []
				}))
			}
		} catch (error) {
			console.error('加载省份数据失败:', error)
		} finally {
			loading.value = false
		}
	}

	// 动态加载子级数据
	const loadData = async (selectedOptions) => {
		const targetOption = selectedOptions[selectedOptions.length - 1]

		if (!targetOption || targetOption.children?.length > 0) {
			return
		}

		try {
			targetOption.loading = true
			const response = await cityApi.listByParentCode(targetOption.value)

			// 兼容两种响应格式
			let data = null
			if (response && response.code === 200 && response.data) {
				data = response.data
			} else if (Array.isArray(response)) {
				data = response
			}

			if (data && Array.isArray(data) && data.length > 0) {
				targetOption.children = data.map((item) => ({
					value: item.cityCode,
					label: item.cityName,
					isLeaf: selectedOptions.length >= (props.showDistrict ? 2 : 1), // 根据是否显示区县决定层级
					children: []
				}))
			} else {
				targetOption.isLeaf = true
				targetOption.children = []
			}
		} catch (error) {
			console.error('加载子级数据失败:', error)
			targetOption.isLeaf = true
			targetOption.children = []
		} finally {
			targetOption.loading = false
		}
	}

	// 根据城市名称查找城市信息
	const findByNames = async (provinceName, cityName, districtName) => {
		try {
			const params = {
				provinceName,
				cityName: cityName || null,
				districtName: districtName || null
			}

			const response = await cityApi.findByNames(params)

			// 兼容两种响应格式
			let data = null
			if (response && response.code === 200 && response.data) {
				data = response.data
			} else if (Array.isArray(response)) {
				data = response
			}

			return data
		} catch (error) {
			console.error('根据名称查找城市失败:', error)
			return null
		}
	}

	// 根据城市名称初始化选择器
	const initValueFromNames = async (provinceName, cityName, districtName) => {
		if (!provinceName) {
			selectedValue.value = []
			return
		}

		try {
			// 确保省份数据已加载
			if (options.value.length === 0) {
				await loadProvinces()
			}

			// 查找省份
			const province = options.value.find((p) => p.label === provinceName)
			if (!province) {
				selectedValue.value = []
				return
			}

			const codes = [province.value]

			// 如果有城市名称，查找城市
			if (cityName) {
				// 加载城市数据
				if (!province.children || province.children.length === 0) {
					await loadData([province])
				}

				const city = province.children.find((c) => c.label === cityName)
				if (city) {
					codes.push(city.value)

					// 如果有区县名称且需要显示区县，查找区县
					if (districtName && props.showDistrict) {
						// 加载区县数据
						if (!city.children || city.children.length === 0) {
							await loadData([province, city])
						}

						const district = city.children.find((d) => d.label === districtName)
						if (district) {
							codes.push(district.value)
						}
					}
				}
			}

			selectedValue.value = codes
		} catch (error) {
			console.error('初始化城市选择器失败:', error)
			selectedValue.value = []
		}
	}

	// 处理级联选择器变化
	const handleChange = (value, selectedOptions) => {
		if (!value || value.length === 0) {
			emit('update:value', null)
			emit('change', null)
			return
		}

		const result = {
			codes: value,
			names: selectedOptions.map((option) => option.label),
			province: selectedOptions[0]?.label || '',
			city: selectedOptions[1]?.label || '',
			district: selectedOptions[2]?.label || ''
		}

		emit('update:value', result)
		emit('change', result)
	}

	// 处理三输入框模式变化
	const handleThreeInputChange = (result) => {
		emit('update:value', result)
		emit('change', result)
	}

	// 处理清空
	const handleClear = () => {
		selectedValue.value = []
		emit('update:value', null)
		emit('change', null)
	}

	// 自定义显示格式
	const displayRender = ({ labels }) => {
		return labels.join(' / ')
	}

	// 搜索过滤
	const filterOption = (inputValue, path) => {
		return path.some((option) => option.label.toLowerCase().includes(inputValue.toLowerCase()))
	}

	// 监听外部value变化
	watch(
		() => props.value,
		(newValue) => {
			if (props.type === 'cascader') {
				if (newValue && typeof newValue === 'object') {
					// 如果传入的是对象形式 {province: '北京市', city: '北京市', district: '朝阳区'}
					if (newValue.province) {
						initValueFromNames(newValue.province, newValue.city, newValue.district)
					} else if (newValue.codes && Array.isArray(newValue.codes)) {
						// 如果传入的是编码数组
						selectedValue.value = newValue.codes
					}
				} else if (Array.isArray(newValue) && newValue.length > 0) {
					// 如果传入的是数组形式 ['北京市', '北京市', '朝阳区']
					initValueFromNames(newValue[0], newValue[1], newValue[2])
				} else {
					selectedValue.value = []
				}
			}
			// 三输入框模式的value变化由ThreeInputCitySelector组件内部处理
		},
		{ immediate: true }
	)

	// 组件挂载时加载省份数据
	onMounted(async () => {
		if (props.type === 'cascader') {
			await loadProvinces()
		}
	})

	// 暴露方法供外部调用
	defineExpose({
		initValueFromNames,
		loadData
	})
</script>

<style scoped>
	.city-selector {
		width: 100%;
	}

	.city-selector .ant-cascader {
		width: 100%;
	}
</style>
