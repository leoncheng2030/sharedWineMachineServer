<template>
	<div>
		<!-- 面包屑导航 -->
		<a-breadcrumb style="margin-bottom: 16px">
			<a-breadcrumb-item>
				<router-link to="/wine/product">酒品管理</router-link>
			</a-breadcrumb-item>
			<a-breadcrumb-item>库存管理</a-breadcrumb-item>
		</a-breadcrumb>

		<!-- 酒品信息卡片 -->
		<a-card title="酒品信息" style="margin-bottom: 16px" v-if="productInfo.productName">
			<a-row :gutter="16">
				<a-col :span="6">
					<a-statistic title="酒品名称" :value="productInfo.productName" />
				</a-col>
				<a-col :span="6">
					<a-statistic title="酒品编码" :value="productInfo.productCode" />
				</a-col>
				<a-col :span="6">
					<a-statistic title="品牌" :value="productInfo.brand" />
				</a-col>
				<a-col :span="6">
					<a-statistic title="分类" :value="productInfo.categoryName" />
				</a-col>
			</a-row>
		</a-card>

		<!-- 库存统计卡片 -->
		<a-row :gutter="16" style="margin-bottom: 16px">
			<a-col :span="6">
				<a-card>
					<a-statistic title="设备总数" :value="stockStatistics.totalDevices" :value-style="{ color: '#1890ff' }" />
				</a-card>
			</a-col>
			<a-col :span="6">
				<a-card>
					<a-statistic
						title="正常库存设备"
						:value="stockStatistics.normalStockDevices"
						:value-style="{ color: '#52c41a' }"
					/>
				</a-card>
			</a-col>
			<a-col :span="6">
				<a-card>
					<a-statistic
						title="低库存设备"
						:value="stockStatistics.lowStockDevices"
						:value-style="{ color: '#faad14' }"
					/>
				</a-card>
			</a-col>
			<a-col :span="6">
				<a-card>
					<a-statistic
						title="缺货设备"
						:value="stockStatistics.outOfStockDevices"
						:value-style="{ color: '#f5222d' }"
					/>
				</a-card>
			</a-col>
		</a-row>

		<!-- 库存列表 -->
		<a-card title="设备库存列表">
			<!-- 搜索表单 -->
			<div style="margin-bottom: 16px">
				<a-form layout="inline" :model="searchForm">
					<a-form-item label="设备名称">
						<a-input
							v-model:value="searchForm.deviceName"
							placeholder="请输入设备名称"
							style="width: 200px"
							allow-clear
						/>
					</a-form-item>
					<a-form-item label="门店名称">
						<a-input
							v-model:value="searchForm.storeName"
							placeholder="请输入门店名称"
							style="width: 200px"
							allow-clear
						/>
					</a-form-item>
					<a-form-item label="库存状态">
						<a-select
							v-model:value="searchForm.stockStatus"
							placeholder="请选择库存状态"
							style="width: 150px"
							allow-clear
						>
							<a-select-option value="NORMAL">正常</a-select-option>
							<a-select-option value="LOW">低库存</a-select-option>
							<a-select-option value="OUT">缺货</a-select-option>
						</a-select>
					</a-form-item>
					<a-form-item>
						<a-button type="primary" @click="handleSearch">
							<template #icon>
								<SearchOutlined />
							</template>
							搜索
						</a-button>
						<a-button style="margin-left: 8px" @click="handleReset"> 重置 </a-button>
					</a-form-item>
				</a-form>
			</div>

			<!-- 操作按钮 -->
			<div style="margin-bottom: 16px">
				<a-button type="primary" @click="handleRefresh">
					<template #icon>
						<ReloadOutlined />
					</template>
					刷新数据
				</a-button>
				<a-button style="margin-left: 8px" @click="handleExport">
					<template #icon>
						<ExportOutlined />
					</template>
					导出数据
				</a-button>
			</div>

			<!-- 数据表格 -->
			<a-table
				:columns="columns"
				:data-source="stockList"
				:loading="loading"
				:pagination="pagination"
				@change="handleTableChange"
				row-key="id"
			>
				<template #bodyCell="{ column, record }">
					<template v-if="column.dataIndex === 'deviceName'">
						<a @click="viewDeviceDetail(record)">{{ record.deviceName }}</a>
					</template>
					<template v-if="column.dataIndex === 'stockStatus'">
						<a-tag :color="getStatusColor(record.stockStatus)">
							{{ getStatusText(record.stockStatus) }}
						</a-tag>
					</template>
					<template v-if="column.dataIndex === 'stockQuantity'">
						<span :style="{ color: getQuantityColor(record.stockStatus) }"> {{ record.stockQuantity }}ml </span>
					</template>
					<template v-if="column.dataIndex === 'alertThreshold'"> {{ record.alertThreshold }}ml </template>
					<template v-if="column.dataIndex === 'lastRefillTime'">
						{{ record.lastRefillTime || '-' }}
					</template>
					<template v-if="column.dataIndex === 'action'">
						<a @click="handleRefill(record)" style="margin-right: 8px">补货</a>
						<a @click="handleAdjust(record)" style="margin-right: 8px">调整</a>
						<a @click="handleViewLog(record)">日志</a>
					</template>
				</template>
			</a-table>
		</a-card>

		<!-- 补货弹窗 -->
		<RefillModal ref="refillModalRef" @success="handleRefresh" />

		<!-- 调整弹窗 -->
		<AdjustModal ref="adjustModalRef" @success="handleRefresh" />

		<!-- 日志弹窗 -->
		<LogModal ref="logModalRef" />
	</div>
</template>

<script setup>
	import { onMounted, reactive, ref } from 'vue'
	import { useRoute, useRouter } from 'vue-router'
	import { message } from 'ant-design-vue'
	import { ExportOutlined, ReloadOutlined, SearchOutlined } from '@ant-design/icons-vue'
	import wineDeviceStockApi from '@/api/wine/wineDeviceStockApi'
	import RefillModal from '@/views/wine/device/stock/refillStockModal.vue'
	import AdjustModal from '@/views/wine/device/stock/adjustStockModal.vue'
	import LogModal from '@/views/wine/device/stock/stockLogModal.vue'

	const route = useRoute()
	const router = useRouter()

	// 酒品信息
	const productInfo = reactive({
		productId: '',
		productName: '',
		productCode: '',
		brand: '',
		categoryName: ''
	})

	// 库存统计
	const stockStatistics = reactive({
		totalDevices: 0,
		normalStockDevices: 0,
		lowStockDevices: 0,
		outOfStockDevices: 0
	})

	// 搜索表单
	const searchForm = reactive({
		deviceName: '',
		storeName: '',
		stockStatus: undefined
	})

	// 数据状态
	const loading = ref(false)
	const stockList = ref([])
	const pagination = reactive({
		current: 1,
		pageSize: 10,
		total: 0,
		showSizeChanger: true,
		showQuickJumper: true,
		showTotal: (total) => `共 ${total} 条数据`
	})

	// 模态框引用
	const refillModalRef = ref()
	const adjustModalRef = ref()
	const logModalRef = ref()

	// 表格列配置
	const columns = [
		{
			title: '设备名称',
			dataIndex: 'deviceName',
			key: 'deviceName',
			width: 150
		},
		{
			title: '设备编码',
			dataIndex: 'deviceCode',
			key: 'deviceCode',
			width: 120
		},
		{
			title: '门店名称',
			dataIndex: 'storeName',
			key: 'storeName',
			width: 150
		},
		{
			title: '当前库存',
			dataIndex: 'stockQuantity',
			key: 'stockQuantity',
			width: 100,
			align: 'right'
		},
		{
			title: '预警阈值',
			dataIndex: 'alertThreshold',
			key: 'alertThreshold',
			width: 100,
			align: 'right'
		},
		{
			title: '库存状态',
			dataIndex: 'stockStatus',
			key: 'stockStatus',
			width: 100,
			align: 'center'
		},
		{
			title: '最后补货时间',
			dataIndex: 'lastRefillTime',
			key: 'lastRefillTime',
			width: 150
		},
		{
			title: '操作',
			dataIndex: 'action',
			key: 'action',
			width: 150,
			fixed: 'right'
		}
	]

	// 获取状态颜色
	const getStatusColor = (status) => {
		const colorMap = {
			NORMAL: 'green',
			LOW: 'orange',
			OUT: 'red'
		}
		return colorMap[status] || 'default'
	}

	// 获取状态文本
	const getStatusText = (status) => {
		const textMap = {
			NORMAL: '正常',
			LOW: '低库存',
			OUT: '缺货'
		}
		return textMap[status] || '未知'
	}

	// 获取数量颜色
	const getQuantityColor = (status) => {
		const colorMap = {
			NORMAL: '#52c41a',
			LOW: '#faad14',
			OUT: '#f5222d'
		}
		return colorMap[status] || '#666'
	}

	// 加载库存数据
	const loadStockData = async () => {
		loading.value = true
		try {
			const params = {
				productId: productInfo.productId,
				pageNum: pagination.current,
				pageSize: pagination.pageSize,
				deviceName: searchForm.deviceName,
				storeName: searchForm.storeName,
				stockStatus: searchForm.stockStatus
			}

			const response = await wineDeviceStockApi.getProductStockPage(params)

			stockList.value = response.data.records || []
			pagination.total = response.data.total || 0

			// 更新统计数据
			updateStatistics()
		} catch (error) {
			console.error('加载库存数据失败:', error)
			message.error('加载库存数据失败')
		} finally {
			loading.value = false
		}
	}

	// 更新统计数据
	const updateStatistics = () => {
		const total = stockList.value.length
		const normal = stockList.value.filter((item) => item.stockStatus === 'NORMAL').length
		const low = stockList.value.filter((item) => item.stockStatus === 'LOW').length
		const out = stockList.value.filter((item) => item.stockStatus === 'OUT').length

		stockStatistics.totalDevices = total
		stockStatistics.normalStockDevices = normal
		stockStatistics.lowStockDevices = low
		stockStatistics.outOfStockDevices = out
	}

	// 搜索
	const handleSearch = () => {
		pagination.current = 1
		loadStockData()
	}

	// 重置
	const handleReset = () => {
		searchForm.deviceName = ''
		searchForm.storeName = ''
		searchForm.stockStatus = undefined
		pagination.current = 1
		loadStockData()
	}

	// 刷新
	const handleRefresh = () => {
		loadStockData()
	}

	// 导出
	const handleExport = async () => {
		try {
			const params = {
				productId: productInfo.productId,
				deviceName: searchForm.deviceName,
				storeName: searchForm.storeName,
				stockStatus: searchForm.stockStatus
			}

			await wineDeviceStockApi.exportProductStock(params)
			message.success('导出成功')
		} catch (error) {
			console.error('导出失败:', error)
			message.error('导出失败')
		}
	}

	// 表格变化处理
	const handleTableChange = (pag) => {
		pagination.current = pag.current
		pagination.pageSize = pag.pageSize
		loadStockData()
	}

	// 查看设备详情
	const viewDeviceDetail = (record) => {
		router.push({
			path: '/wine/device/stock/index',
			query: {
				deviceId: record.deviceId,
				deviceName: record.deviceName
			}
		})
	}

	// 补货
	const handleRefill = (record) => {
		refillModalRef.value?.open(record)
	}

	// 调整
	const handleAdjust = (record) => {
		adjustModalRef.value?.open(record)
	}

	// 查看日志
	const handleViewLog = (record) => {
		logModalRef.value?.open(record)
	}

	// 初始化
	onMounted(() => {
		// 从路由参数获取酒品信息
		const query = route.query
		productInfo.productId = query.productId || ''
		productInfo.productName = query.productName || ''
		productInfo.productCode = query.productCode || ''
		productInfo.brand = query.brand || ''
		productInfo.categoryName = query.categoryName || ''

		if (!productInfo.productId) {
			message.error('缺少酒品信息，请重新进入')
			router.go(-1)
			return
		}

		// 加载库存数据
		loadStockData()
	})
</script>

<style scoped>
	.ant-statistic-title {
		font-size: 14px;
		color: #666;
	}

	.ant-statistic-content {
		font-size: 18px;
		font-weight: bold;
	}
</style>
