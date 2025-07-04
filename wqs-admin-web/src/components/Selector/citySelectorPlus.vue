<template>
	<a-modal
		v-model:open="visible"
		title="城市选择"
		:width="1200"
		:mask-closable="false"
		:destroy-on-close="true"
		@ok="handleOk"
		@cancel="handleClose"
	>
		<a-row :gutter="16">
			<!-- 左侧：三级联动选择器 -->
			<a-col :span="8">
				<a-card size="small" title="城市选择" class="selectorTreeDiv">
					<div class="city-selector-container">
						<!-- 省份选择 -->
						<div class="level-selector">
							<h4>省份/直辖市</h4>
							<a-select
								v-model:value="selectedProvince"
								placeholder="请选择省份"
								style="width: 100%"
								show-search
								:filter-option="filterOption"
								@change="onProvinceChange"
								:loading="provinceLoading"
							>
								<a-select-option
									v-for="province in provinceList"
									:key="province.cityCode"
									:value="province.cityCode"
									:label="province.cityName"
								>
									{{ province.cityName }}
								</a-select-option>
							</a-select>
						</div>

						<!-- 城市选择 -->
						<div class="level-selector" v-if="selectedProvince">
							<h4>城市</h4>
							<a-select
								v-model:value="selectedCity"
								placeholder="请选择城市"
								style="width: 100%"
								show-search
								:filter-option="filterOption"
								@change="onCityChange"
								:loading="cityLoading"
								:disabled="!selectedProvince"
							>
								<a-select-option
									v-for="city in cityList"
									:key="city.cityCode"
									:value="city.cityCode"
									:label="city.cityName"
								>
									{{ city.cityName }}
								</a-select-option>
							</a-select>
						</div>

						<!-- 区县选择 -->
						<div class="level-selector" v-if="selectedCity">
							<h4>区县</h4>
							<a-select
								v-model:value="selectedDistrict"
								placeholder="请选择区县"
								style="width: 100%"
								show-search
								:filter-option="filterOption"
								@change="onDistrictChange"
								:loading="districtLoading"
								:disabled="!selectedCity"
							>
								<a-select-option
									v-for="district in districtList"
									:key="district.cityCode"
									:value="district.cityCode"
									:label="district.cityName"
								>
									{{ district.cityName }}
								</a-select-option>
							</a-select>
						</div>

						<!-- 热门城市快捷选择 -->
						<div class="hot-cities" v-if="hotCities.length > 0">
							<h4>热门城市</h4>
							<div class="hot-city-tags">
								<a-tag
									v-for="city in hotCities"
									:key="city.cityCode"
									:color="isHotCitySelected(city) ? 'blue' : 'default'"
									style="cursor: pointer; margin: 4px"
									@click="selectHotCity(city)"
								>
									{{ city.cityName }}
								</a-tag>
							</div>
						</div>
					</div>
				</a-card>
			</a-col>

			<!-- 中间：搜索和列表 -->
			<a-col :span="10">
				<div class="table-operator xn-mb10">
					<a-form ref="searchFormRef" name="advanced_search" class="ant-advanced-search-form" :model="searchFormState">
						<a-row :gutter="24">
							<a-col :span="16">
								<a-form-item name="searchKey">
									<a-input
										v-model:value="searchFormState.searchKey"
										placeholder="请输入城市名称或拼音搜索"
										@press-enter="loadData"
									/>
								</a-form-item>
							</a-col>
							<a-col :span="8">
								<a-button type="primary" class="primarySele" @click="loadData()"> 查询 </a-button>
								<a-button class="snowy-button-left" @click="reset()"> 重置 </a-button>
							</a-col>
						</a-row>
					</a-form>
				</div>
				<div class="city-table">
					<a-table
						ref="tableRef"
						size="small"
						:columns="commons"
						:data-source="tableData"
						:loading="pageLoading"
						bordered
						:pagination="false"
						:scroll="{ y: 400 }"
					>
						<template #title>
							<span>待选择列表 {{ tableRecordNum }} 条</span>
							<div v-if="!radioModel" class="xn-fd">
								<a-button type="dashed" size="small" @click="addAllPageRecord">添加当前数据</a-button>
							</div>
						</template>
						<template #bodyCell="{ column, record }">
							<template v-if="column.dataIndex === 'level'">
								<a-tag :color="getLevelColor(record.level)">
									{{ getLevelText(record.level) }}
								</a-tag>
							</template>
							<template v-if="column.dataIndex === 'isHot'">
								<a-tag :color="record.isHot === 'YES' ? 'red' : 'default'">
									{{ record.isHot === 'YES' ? '热门' : '普通' }}
								</a-tag>
							</template>
							<template v-if="column.dataIndex === 'status'">
								<a-tag :color="record.status === 'ENABLE' ? 'green' : 'red'">
									{{ record.status === 'ENABLE' ? '启用' : '禁用' }}
								</a-tag>
							</template>
							<template v-if="column.dataIndex === 'action'">
								<a-button type="dashed" size="small" @click="addRecord(record)">
									<PlusOutlined />
								</a-button>
							</template>
						</template>
					</a-table>
					<div class="mt-2">
						<a-pagination
							v-if="!isEmpty(tableData)"
							v-model:current="current"
							v-model:page-size="pageSize"
							:total="total"
							size="small"
							showSizeChanger
							@change="paginationChange"
						/>
					</div>
				</div>
			</a-col>

			<!-- 右侧：已选择列表 -->
			<a-col :span="6">
				<div class="city-table">
					<a-table
						ref="selectedTable"
						size="small"
						:columns="selectedCommons"
						:data-source="selectedData"
						:loading="selectedTableListLoading"
						bordered
						:scroll="{ y: 400 }"
					>
						<template #title>
							<span>已选择: {{ selectedData.length }}</span>
							<div v-if="!radioModel" class="xn-fd">
								<a-button type="dashed" danger size="small" @click="delAllRecord">全部移除</a-button>
							</div>
						</template>
						<template #bodyCell="{ column, record }">
							<template v-if="column.dataIndex === 'level'">
								<a-tag :color="getLevelColor(record.level)" size="small">
									{{ getLevelText(record.level) }}
								</a-tag>
							</template>
							<template v-if="column.dataIndex === 'action'">
								<a-button type="dashed" danger size="small" @click="delRecord(record)">
									<MinusOutlined />
								</a-button>
							</template>
						</template>
					</a-table>
				</div>
			</a-col>
		</a-row>
	</a-modal>
</template>

<script setup name="citySelectorPlus">
	import { message } from 'ant-design-vue'
	import { isEmpty, remove } from 'lodash-es'
	import cityApi from '@/api/sys/cityApi'

	// 弹窗是否打开
	const visible = ref(false)

	// 主表格columns
	const commons = [
		{
			title: '操作',
			dataIndex: 'action',
			align: 'center',
			width: 50
		},
		{
			title: '城市名称',
			dataIndex: 'cityName',
			ellipsis: true,
			width: 120
		},
		{
			title: '层级',
			dataIndex: 'level',
			align: 'center',
			width: 60
		},
		{
			title: '热门',
			dataIndex: 'isHot',
			align: 'center',
			width: 60
		},
		{
			title: '状态',
			dataIndex: 'status',
			align: 'center',
			width: 60
		}
	]

	// 选中表格的columns
	const selectedCommons = [
		{
			title: '操作',
			dataIndex: 'action',
			align: 'center',
			width: 50
		},
		{
			title: '城市名称',
			dataIndex: 'cityName',
			ellipsis: true
		},
		{
			title: '层级',
			dataIndex: 'level',
			align: 'center',
			width: 50
		}
	]

	// 组件引用
	const tableRef = ref()
	const selectedTable = ref()
	const searchFormRef = ref()

	// 数据状态
	const tableRecordNum = ref(0)
	const searchFormState = ref({})
	const pageLoading = ref(false)
	const selectedTableListLoading = ref(false)
	const provinceLoading = ref(false)
	const cityLoading = ref(false)
	const districtLoading = ref(false)

	// 三级联动选择状态
	const selectedProvince = ref(null)
	const selectedCity = ref(null)
	const selectedDistrict = ref(null)

	// 数据列表
	const provinceList = ref([])
	const cityList = ref([])
	const districtList = ref([])
	const hotCities = ref([])
	const tableData = ref([])
	const selectedData = ref([])

	// 组件属性
	const emit = defineEmits({ onBack: null })
	const props = defineProps({
		radioModel: {
			type: Boolean,
			default: false
		},
		dataIsConverterFlw: {
			type: Boolean,
			default: false
		},
		checkedCityListApi: {
			type: Function,
			default: null
		}
	})

	// 是否是单选模式
	const radioModel = props.radioModel || false
	// 数据是否转换成工作流格式
	const dataIsConverterFlw = props.dataIsConverterFlw || false

	// 分页相关
	const current = ref(1)
	const pageSize = ref(20)
	const total = ref(0)

	// 记录选中的ID集合
	const recordIds = ref([])

	/**
	 * 打开城市选择弹框
	 * @param {Array} ids 已选择的城市ID列表
	 */
	const showCitySelectorModal = (ids) => {
		visible.value = true
		if (dataIsConverterFlw) {
			ids = goDataConverter(ids)
		}
		recordIds.value = ids || []

		// 初始化数据
		initData()
	}

	/**
	 * 初始化数据
	 */
	const initData = async () => {
		try {
			// 加载省份列表
			await loadProvinceList()
			// 加载热门城市
			await loadHotCities()
			// 加载表格数据
			await loadData()
			// 如果有已选择的记录，加载已选择数据
			if (recordIds.value && recordIds.value.length > 0) {
				await loadSelectedData()
			}
		} catch (error) {
			console.error('初始化数据失败:', error)
			message.error('初始化数据失败')
		}
	}

	/**
	 * 加载省份列表
	 */
	const loadProvinceList = async () => {
		try {
			provinceLoading.value = true
			const data = await cityApi.provinceList()
			provinceList.value = data || []
		} catch (error) {
			console.error('加载省份列表失败:', error)
			message.error('加载省份列表失败')
		} finally {
			provinceLoading.value = false
		}
	}

	/**
	 * 加载热门城市
	 */
	const loadHotCities = async () => {
		try {
			const data = await cityApi.hotCityList()
			hotCities.value = data || []
		} catch (error) {
			console.error('加载热门城市失败:', error)
		}
	}

	/**
	 * 省份选择变化
	 */
	const onProvinceChange = async (provinceCode) => {
		selectedCity.value = null
		selectedDistrict.value = null
		cityList.value = []
		districtList.value = []

		if (provinceCode) {
			try {
				cityLoading.value = true
				const data = await cityApi.cityByProvince(provinceCode)
				cityList.value = data || []
			} catch (error) {
				console.error('加载城市列表失败:', error)
				message.error('加载城市列表失败')
			} finally {
				cityLoading.value = false
			}
		}
	}

	/**
	 * 城市选择变化
	 */
	const onCityChange = async (cityCode) => {
		selectedDistrict.value = null
		districtList.value = []

		if (cityCode) {
			try {
				districtLoading.value = true
				const data = await cityApi.districtByCity(cityCode)
				districtList.value = data || []
			} catch (error) {
				console.error('加载区县列表失败:', error)
				message.error('加载区县列表失败')
			} finally {
				districtLoading.value = false
			}
		}
	}

	/**
	 * 区县选择变化
	 */
	const onDistrictChange = (districtCode) => {
		// 区县选择后可以执行相关逻辑
		console.log('选择区县:', districtCode)
	}

	/**
	 * 选择热门城市
	 */
	const selectHotCity = (city) => {
		if (isHotCitySelected(city)) {
			// 如果已选择，则移除
			delRecord(city)
		} else {
			// 如果未选择，则添加
			addRecord(city)
		}
	}

	/**
	 * 判断热门城市是否已选择
	 */
	const isHotCitySelected = (city) => {
		return selectedData.value.some((item) => item.cityCode === city.cityCode)
	}

	/**
	 * 加载表格数据
	 */
	const loadData = async () => {
		try {
			pageLoading.value = true
			const params = {
				current: current.value,
				size: pageSize.value,
				...searchFormState.value
			}

			// 如果有三级联动选择，优先使用
			if (selectedDistrict.value) {
				params.parentCode = selectedCity.value
				params.level = 3
			} else if (selectedCity.value) {
				params.parentCode = selectedProvince.value
				params.level = 2
			} else if (selectedProvince.value) {
				params.parentCode = '0'
				params.level = 1
			}

			const data = await cityApi.cityPage(params)
			tableData.value = data.records || []
			total.value = data.total || 0
			tableRecordNum.value = data.total || 0
		} catch (error) {
			console.error('加载数据失败:', error)
			message.error('加载数据失败')
		} finally {
			pageLoading.value = false
		}
	}

	/**
	 * 加载已选择数据
	 */
	const loadSelectedData = async () => {
		if (!recordIds.value || recordIds.value.length === 0) {
			selectedData.value = []
			return
		}

		try {
			selectedTableListLoading.value = true
			// 根据ID列表获取城市详情
			const promises = recordIds.value.map((id) => cityApi.cityDetail(id))
			const results = await Promise.all(promises)
			selectedData.value = results.filter((item) => item != null)
		} catch (error) {
			console.error('加载已选择数据失败:', error)
			message.error('加载已选择数据失败')
		} finally {
			selectedTableListLoading.value = false
		}
	}

	/**
	 * 添加记录
	 */
	const addRecord = (record) => {
		if (radioModel) {
			// 单选模式，清空之前的选择
			selectedData.value = [record]
		} else {
			// 多选模式，检查是否已存在
			const exists = selectedData.value.some((item) => item.cityCode === record.cityCode)
			if (!exists) {
				selectedData.value.push(record)
			} else {
				message.warning('该城市已经选择')
			}
		}
	}

	/**
	 * 添加当前页所有记录
	 */
	const addAllPageRecord = () => {
		if (radioModel) {
			message.warning('单选模式不支持批量添加')
			return
		}

		tableData.value.forEach((record) => {
			const exists = selectedData.value.some((item) => item.cityCode === record.cityCode)
			if (!exists) {
				selectedData.value.push(record)
			}
		})
		message.success('添加成功')
	}

	/**
	 * 删除记录
	 */
	const delRecord = (record) => {
		remove(selectedData.value, (item) => item.cityCode === record.cityCode)
	}

	/**
	 * 删除所有记录
	 */
	const delAllRecord = () => {
		selectedData.value = []
	}

	/**
	 * 重置搜索
	 */
	const reset = () => {
		searchFormRef.value.resetFields()
		selectedProvince.value = null
		selectedCity.value = null
		selectedDistrict.value = null
		cityList.value = []
		districtList.value = []
		loadData()
	}

	/**
	 * 分页变化
	 */
	const paginationChange = (page, size) => {
		current.value = page
		pageSize.value = size
		loadData()
	}

	/**
	 * 搜索过滤
	 */
	const filterOption = (input, option) => {
		return option.label.toLowerCase().indexOf(input.toLowerCase()) >= 0
	}

	/**
	 * 获取层级颜色
	 */
	const getLevelColor = (level) => {
		const colors = {
			1: 'blue', // 省份
			2: 'green', // 城市
			3: 'orange' // 区县
		}
		return colors[level] || 'default'
	}

	/**
	 * 获取层级文本
	 */
	const getLevelText = (level) => {
		const texts = {
			1: '省份',
			2: '城市',
			3: '区县'
		}
		return texts[level] || '未知'
	}

	/**
	 * 确认选择
	 */
	const handleOk = () => {
		if (selectedData.value.length === 0) {
			message.warning('请至少选择一个城市')
			return
		}

		let result = selectedData.value
		if (dataIsConverterFlw) {
			result = goFlwDataConverter(result)
		}

		emit('onBack', result)
		handleClose()
	}

	/**
	 * 关闭弹框
	 */
	const handleClose = () => {
		visible.value = false
		// 重置状态
		selectedProvince.value = null
		selectedCity.value = null
		selectedDistrict.value = null
		cityList.value = []
		districtList.value = []
		selectedData.value = []
		searchFormState.value = {}
		current.value = 1
	}

	/**
	 * 数据转换器（工作流格式）
	 */
	const goDataConverter = (ids) => {
		if (!ids || !Array.isArray(ids)) return []
		return ids.map((id) => (typeof id === 'object' ? id.cityCode : id))
	}

	/**
	 * 工作流数据转换器
	 */
	const goFlwDataConverter = (data) => {
		return data.map((item) => ({
			id: item.cityCode,
			name: item.cityName,
			level: item.level,
			parentCode: item.parentCode
		}))
	}

	// 暴露方法
	defineExpose({
		showCitySelectorModal
	})
</script>

<style scoped>
	.selectorTreeDiv {
		height: 500px;
		overflow-y: auto;
	}

	.city-selector-container {
		padding: 16px 0;
	}

	.level-selector {
		margin-bottom: 16px;
	}

	.level-selector h4 {
		margin-bottom: 8px;
		font-weight: 600;
		color: #333;
	}

	.hot-cities {
		margin-top: 20px;
		padding-top: 16px;
		border-top: 1px solid #f0f0f0;
	}

	.hot-cities h4 {
		margin-bottom: 12px;
		font-weight: 600;
		color: #333;
	}

	.hot-city-tags {
		display: flex;
		flex-wrap: wrap;
		gap: 4px;
	}

	.city-table {
		height: 500px;
		display: flex;
		flex-direction: column;
	}

	.city-table .ant-table-wrapper {
		flex: 1;
		overflow: hidden;
	}

	.xn-fd {
		float: right;
	}

	.xn-mb10 {
		margin-bottom: 10px;
	}

	.snowy-button-left {
		margin-left: 8px;
	}

	.primarySele {
		background-color: #1890ff;
		border-color: #1890ff;
	}

	.mt-2 {
		margin-top: 8px;
	}
</style>
