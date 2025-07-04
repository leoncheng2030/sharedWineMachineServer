<template>
	<div class="three-input-city-selector">
		<a-row :gutter="8">
			<a-col :span="8">
				<a-select
					v-model:value="selectedProvince"
					:placeholder="placeholders.province"
					:loading="loading.province"
					:show-search="true"
					:filter-option="filterOption"
					allow-clear
					@change="handleProvinceChange"
					@clear="handleProvinceClear"
				>
					<a-select-option
						v-for="province in provinceOptions"
						:key="province.value"
						:value="province.value"
						:label="province.label"
					>
						{{ province.label }}
					</a-select-option>
				</a-select>
			</a-col>
			<a-col :span="8">
				<a-select
					v-model:value="selectedCity"
					:placeholder="placeholders.city"
					:loading="loading.city"
					:disabled="!selectedProvince"
					:show-search="true"
					:filter-option="filterOption"
					allow-clear
					@change="handleCityChange"
					@clear="handleCityClear"
				>
					<a-select-option v-for="city in cityOptions" :key="city.value" :value="city.value" :label="city.label">
						{{ city.label }}
					</a-select-option>
				</a-select>
			</a-col>
			<a-col :span="8" v-if="showDistrict">
				<a-select
					v-model:value="selectedDistrict"
					:placeholder="placeholders.district"
					:loading="loading.district"
					:disabled="!selectedCity"
					:show-search="true"
					:filter-option="filterOption"
					allow-clear
					@change="handleDistrictChange"
					@clear="handleDistrictClear"
				>
					<a-select-option
						v-for="district in districtOptions"
						:key="district.value"
						:value="district.value"
						:label="district.label"
					>
						{{ district.label }}
					</a-select-option>
				</a-select>
			</a-col>
		</a-row>
	</div>
</template>

<script setup>
	import { onMounted, ref, watch } from 'vue'
	import cityApi from '@/api/sys/cityApi'

	// 定义props
	const props = defineProps({
		value: {
			type: [Array, Object],
			default: () => null
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
	const selectedProvince = ref(null)
	const selectedCity = ref(null)
	const selectedDistrict = ref(null)

	const provinceOptions = ref([])
	const cityOptions = ref([])
	const districtOptions = ref([])

	const loading = ref({
		province: false,
		city: false,
		district: false
	})

	// 加载省份数据
	const loadProvinces = async () => {
		try {
			loading.value.province = true
			const response = await cityApi.listByLevel(1)

			// 兼容两种响应格式
			let data = null
			if (response && response.code === 200 && response.data) {
				data = response.data
			} else if (Array.isArray(response)) {
				data = response
			}

			if (data && Array.isArray(data)) {
				provinceOptions.value = data.map((item) => ({
					value: item.cityCode,
					label: item.cityName
				}))
			}
		} catch (error) {
			console.error('加载省份数据失败:', error)
		} finally {
			loading.value.province = false
		}
	}

	// 加载城市数据
	const loadCities = async (provinceCode) => {
		try {
			loading.value.city = true
			const response = await cityApi.listByParentCode(provinceCode)

			// 兼容两种响应格式
			let data = null
			if (response && response.code === 200 && response.data) {
				data = response.data
			} else if (Array.isArray(response)) {
				data = response
			}

			if (data && Array.isArray(data)) {
				cityOptions.value = data.map((item) => ({
					value: item.cityCode,
					label: item.cityName
				}))
			} else {
				cityOptions.value = []
			}
		} catch (error) {
			console.error('加载城市数据失败:', error)
			cityOptions.value = []
		} finally {
			loading.value.city = false
		}
	}

	// 加载区县数据
	const loadDistricts = async (cityCode) => {
		try {
			loading.value.district = true
			const response = await cityApi.listByParentCode(cityCode)

			// 兼容两种响应格式
			let data = null
			if (response && response.code === 200 && response.data) {
				data = response.data
			} else if (Array.isArray(response)) {
				data = response
			}

			if (data && Array.isArray(data)) {
				districtOptions.value = data.map((item) => ({
					value: item.cityCode,
					label: item.cityName
				}))
			} else {
				districtOptions.value = []
			}
		} catch (error) {
			console.error('加载区县数据失败:', error)
			districtOptions.value = []
		} finally {
			loading.value.district = false
		}
	}

	// 根据城市名称初始化选择器
	const initValueFromNames = async (provinceName, cityName, districtName) => {
		if (!provinceName) {
			clearAll()
			return
		}

		try {
			// 确保省份数据已加载
			if (provinceOptions.value.length === 0) {
				await loadProvinces()
			}

			// 查找并设置省份
			const province = provinceOptions.value.find((p) => p.label === provinceName)
			if (province) {
				selectedProvince.value = province.value

				// 如果有城市名称，加载并设置城市
				if (cityName) {
					await loadCities(province.value)
					const city = cityOptions.value.find((c) => c.label === cityName)
					if (city) {
						selectedCity.value = city.value

						// 如果有区县名称且需要显示区县，加载并设置区县
						if (districtName && props.showDistrict) {
							await loadDistricts(city.value)
							const district = districtOptions.value.find((d) => d.label === districtName)
							if (district) {
								selectedDistrict.value = district.value
							}
						}
					}
				}
			}
		} catch (error) {
			console.error('初始化城市选择器失败:', error)
		}
	}

	// 处理省份变化
	const handleProvinceChange = async (value) => {
		selectedCity.value = null
		selectedDistrict.value = null
		cityOptions.value = []
		districtOptions.value = []

		if (value) {
			await loadCities(value)
		}

		emitChange()
	}

	// 处理城市变化
	const handleCityChange = async (value) => {
		selectedDistrict.value = null
		districtOptions.value = []

		if (value && props.showDistrict) {
			await loadDistricts(value)
		}

		emitChange()
	}

	// 处理区县变化
	const handleDistrictChange = () => {
		emitChange()
	}

	// 处理省份清空
	const handleProvinceClear = () => {
		selectedProvince.value = null
		selectedCity.value = null
		selectedDistrict.value = null
		cityOptions.value = []
		districtOptions.value = []
		emitChange()
	}

	// 处理城市清空
	const handleCityClear = () => {
		selectedCity.value = null
		selectedDistrict.value = null
		districtOptions.value = []
		emitChange()
	}

	// 处理区县清空
	const handleDistrictClear = () => {
		selectedDistrict.value = null
		emitChange()
	}

	// 清空所有选择
	const clearAll = () => {
		selectedProvince.value = null
		selectedCity.value = null
		selectedDistrict.value = null
		cityOptions.value = []
		districtOptions.value = []
	}

	// 发送变化事件
	const emitChange = () => {
		const provinceName = provinceOptions.value.find((p) => p.value === selectedProvince.value)?.label || ''
		const cityName = cityOptions.value.find((c) => c.value === selectedCity.value)?.label || ''
		const districtName = districtOptions.value.find((d) => d.value === selectedDistrict.value)?.label || ''

		const result = {
			codes: [selectedProvince.value, selectedCity.value, selectedDistrict.value].filter(Boolean),
			names: [provinceName, cityName, districtName].filter(Boolean),
			province: provinceName,
			city: cityName,
			district: props.showDistrict ? districtName : ''
		}

		emit('update:value', result)
		emit('change', result)
	}

	// 搜索过滤
	const filterOption = (input, option) => {
		return option.label.toLowerCase().includes(input.toLowerCase())
	}

	// 监听外部value变化
	watch(
		() => props.value,
		(newValue) => {
			if (newValue && typeof newValue === 'object') {
				// 如果传入的是对象形式 {province: '北京市', city: '北京市', district: '朝阳区'}
				if (newValue.province) {
					initValueFromNames(newValue.province, newValue.city, newValue.district)
				}
			} else if (Array.isArray(newValue) && newValue.length > 0) {
				// 如果传入的是数组形式 ['北京市', '北京市', '朝阳区']
				initValueFromNames(newValue[0], newValue[1], newValue[2])
			} else {
				clearAll()
			}
		},
		{ immediate: true }
	)

	// 组件挂载时加载省份数据
	onMounted(async () => {
		await loadProvinces()
	})
</script>

<style scoped>
	.three-input-city-selector {
		width: 100%;
	}

	.three-input-city-selector .ant-select {
		width: 100%;
	}
</style>
