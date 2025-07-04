<template>
	<div>
		<!-- 无设备ID提示 -->
		<a-result v-if="!deviceId" status="warning" title="缺少设备信息" sub-title="请从设备管理页面进入库存管理。">
			<template #extra>
				<a-button type="primary" @click="$router.push('/wine/device/index')">返回设备列表</a-button>
			</template>
		</a-result>

		<!-- 设备信息卡片 -->
		<a-card :bordered="false" class="xn-mb10" v-else>
			<template #title>
				<ArrowLeftOutlined @click="$router.go(-1)" style="margin-right: 8px; cursor: pointer" />
				设备库存管理
			</template>
			<a-descriptions :column="4">
				<a-descriptions-item label="设备编码">
					<a-tag color="blue">{{ deviceInfo.deviceCode }}</a-tag>
				</a-descriptions-item>
				<a-descriptions-item label="设备名称">{{ deviceInfo.deviceName }}</a-descriptions-item>
				<a-descriptions-item label="设备状态">
					<a-badge :status="getStatusBadge(deviceInfo.status)" :text="getStatusText(deviceInfo.status)" />
				</a-descriptions-item>
				<a-descriptions-item label="所属门店">{{ deviceInfo.storeName || '未分配' }}</a-descriptions-item>
			</a-descriptions>
		</a-card>

		<!-- 搜索表单 -->
		<a-card :bordered="false" class="xn-mb10" v-if="deviceId && hasPerm('deviceStockPage')">
			<a-form ref="searchFormRef" name="advanced_search" class="ant-advanced-search-form" :model="searchFormState">
				<a-row :gutter="24">
					<a-col :span="6">
						<a-form-item name="searchKey" label="搜索关键词">
							<a-input v-model:value="searchFormState.searchKey" placeholder="请输入酒品名称或编码" />
						</a-form-item>
					</a-col>
					<a-col :span="6">
						<a-form-item name="status" label="库存状态">
							<a-select
								v-model:value="searchFormState.status"
								placeholder="请选择库存状态"
								:getPopupContainer="(trigger) => trigger.parentNode"
								allowClear
							>
								<a-select-option value="NORMAL">正常</a-select-option>
								<a-select-option value="LOW">低库存</a-select-option>
								<a-select-option value="OUT">缺货</a-select-option>
							</a-select>
						</a-form-item>
					</a-col>
					<a-col :span="6">
						<a-form-item name="productId" label="酒品">
							<a-select
								v-model:value="searchFormState.productId"
								placeholder="请选择酒品"
								:getPopupContainer="(trigger) => trigger.parentNode"
								allowClear
								show-search
								:filter-option="filterOption"
							>
								<a-select-option v-for="item in productData" :key="item.id" :value="item.id">
									{{ item.productName }}
								</a-select-option>
							</a-select>
						</a-form-item>
					</a-col>
					<a-col :span="6">
						<a-button type="primary" @click="() => tableRef?.refresh(true)">
							<template #icon><SearchOutlined /></template>
							搜索
						</a-button>
						<a-button class="snowy-button-left" @click="reset">
							<template #icon><redo-outlined /></template>
							重置
						</a-button>
					</a-col>
				</a-row>
			</a-form>
		</a-card>

		<!-- 库存列表 -->
		<a-card :bordered="false" v-if="deviceId && hasPerm('deviceStockPage')">
			<s-table
				ref="tableRef"
				:columns="columns"
				:data="loadData"
				:expand-row-by-click="true"
				bordered
				:alert="options.alert.show"
				:tool-config="toolConfig"
				:row-key="(record) => record.id"
				:row-selection="options.rowSelection"
			>
				<template #operator class="table-operator">
					<a-space>
						<a-button type="primary" @click="initStock" v-if="hasPerm('deviceStockInit')">
							<template #icon><plus-outlined /></template>
							<span>初始化库存</span>
						</a-button>
						<a-button @click="exportData" v-if="hasPerm('deviceStockExport')">
							<template #icon><export-outlined /></template>
							导出
						</a-button>
					</a-space>
				</template>
				<template #bodyCell="{ column, record }">
					<template v-if="column.dataIndex === 'productName'">
						<a-tag color="green">{{ record.productName }}</a-tag>
					</template>
					<template v-if="column.dataIndex === 'stockQuantity'">
						<a-tag :color="getStockColor(record.stockQuantity, record.alertThreshold)">
							{{ record.stockQuantity }}ml
						</a-tag>
					</template>
					<template v-if="column.dataIndex === 'alertThreshold'">
						<span>{{ record.alertThreshold }}ml</span>
					</template>
					<template v-if="column.dataIndex === 'status'">
						<a-badge :status="getStockStatusBadge(record.status)" :text="getStockStatusText(record.status)" />
					</template>
					<template v-if="column.dataIndex === 'action'">
						<a @click="refillStock(record)" v-if="hasPerm('deviceStockRefill')">补货</a>
						<a-divider type="vertical" v-if="hasPerm(['deviceStockRefill', 'deviceStockAdjust'], 'and')" />
						<a @click="adjustStock(record)" v-if="hasPerm('deviceStockAdjust')">调整</a>
						<a-divider type="vertical" v-if="hasPerm('deviceStockLogs')" />
						<a @click="viewStockLogs(record)" v-if="hasPerm('deviceStockLogs')">日志</a>
					</template>
				</template>
			</s-table>
		</a-card>

		<!-- 无权限访问提示 -->
		<a-result v-if="!hasPerm('deviceStockPage')" status="403" title="403" sub-title="抱歉，您没有访问此页面的权限。">
			<template #extra>
				<a-button type="primary" @click="$router.go(-1)">返回</a-button>
			</template>
		</a-result>
	</div>

	<!-- 库存初始化弹窗 -->
	<InitStockModal v-if="deviceId" ref="initStockRef" :deviceId="deviceId" @successful="refreshTable" />

	<!-- 补货弹窗 -->
	<RefillStockModal ref="refillStockRef" @successful="refreshTable" />

	<!-- 调整库存弹窗 -->
	<AdjustStockModal ref="adjustStockRef" @successful="refreshTable" />

	<!-- 库存日志弹窗 -->
	<StockLogModal ref="stockLogRef" />
</template>

<script setup name="deviceStock">
	import { message } from 'ant-design-vue'
	import { useRoute, useRouter } from 'vue-router'
	import { computed, nextTick, onMounted, reactive, ref } from 'vue'
	import downloadUtil from '@/utils/downloadUtil'
	import { hasPerm } from '@/utils/permission'
	import wineDeviceStockApi from '@/api/wine/wineDeviceStockApi'
	import wineDeviceApi from '@/api/wine/wineDeviceApi'
	import wineProductApi from '@/api/wine/wineProductApi'
	import InitStockModal from './initStockModal.vue'
	import RefillStockModal from './refillStockModal.vue'
	import AdjustStockModal from './adjustStockModal.vue'
	import StockLogModal from './stockLogModal.vue'

	const $router = useRouter()
	const $route = useRoute()

	// 获取路由参数 - 使用 computed 确保响应式
	const deviceId = computed(() => $route.query.deviceId)
	const deviceName = computed(() => $route.query.deviceName)
	const deviceCode = computed(() => $route.query.deviceCode)

	const columns = [
		{
			title: '酒品名称',
			dataIndex: 'productName',
			width: '200px'
		},
		{
			title: '当前库存',
			dataIndex: 'stockQuantity',
			width: '120px',
			sorter: true
		},
		{
			title: '预警阈值',
			dataIndex: 'alertThreshold',
			width: '120px'
		},
		{
			title: '库存状态',
			dataIndex: 'status',
			width: '100px'
		},
		{
			title: '最后补货时间',
			dataIndex: 'lastRefillTime',
			width: '150px'
		},
		{
			title: '更新时间',
			dataIndex: 'updateTime',
			width: '150px'
		},
		{
			title: '操作',
			dataIndex: 'action',
			align: 'center',
			fixed: 'right',
			width: '150px'
		}
	]

	let searchFormState = reactive({
		deviceId: deviceId || undefined
	})
	const tableRef = ref()
	const initStockRef = ref()
	const refillStockRef = ref()
	const adjustStockRef = ref()
	const stockLogRef = ref()
	const toolConfig = reactive({ refresh: true, height: true, columnSetting: true, striped: false })
	const selectedRowKeys = ref([])
	const loading = ref(false)

	// 设备信息
	const deviceInfo = ref({
		deviceCode: deviceCode || '',
		deviceName: deviceName || ''
	})

	// 酒品数据
	const productData = ref([])

	// 表格选择配置
	const options = {
		alert: {
			show: true,
			clear: () => {
				selectedRowKeys.value = []
			}
		},
		rowSelection: {
			onChange: (selectedKeys, selectedRows) => {
				selectedRowKeys.value = selectedKeys
			}
		}
	}

	// 加载数据
	const loadData = (parameter) => {
		const searchParam = JSON.parse(JSON.stringify(searchFormState))
		return wineDeviceStockApi.page(Object.assign(parameter, searchParam)).then((res) => {
			return res
		})
	}

	// 重置搜索
	const reset = () => {
		searchFormState = reactive({
			deviceId: deviceId || undefined
		})
		tableRef.value?.refresh(true)
	}

	// 导出数据
	const exportData = () => {
		const searchParam = JSON.parse(JSON.stringify(searchFormState))
		message.loading('正在导出数据，请稍候...', 0)
		wineDeviceStockApi.export(searchParam).then((res) => {
			downloadUtil.resultDownload(res, '设备库存列表.xlsx', 'application/vnd.ms-excel')
			message.destroy()
		})
	}

	// 刷新表格数据
	const refreshTable = () => {
		tableRef.value?.refresh()
	}

	// 初始化库存
	const initStock = () => {
		initStockRef.value?.onOpen()
	}

	// 补货
	const refillStock = (record) => {
		refillStockRef.value?.onOpen(record)
	}

	// 调整库存
	const adjustStock = (record) => {
		adjustStockRef.value?.onOpen(record)
	}

	// 查看库存日志
	const viewStockLogs = (record) => {
		stockLogRef.value?.onOpen(record)
	}

	// 获取库存颜色
	const getStockColor = (stockQuantity, alertThreshold) => {
		if (stockQuantity <= 0) return 'red'
		if (stockQuantity <= alertThreshold) return 'orange'
		return 'green'
	}

	// 获取库存状态徽章
	const getStockStatusBadge = (status) => {
		const statusMap = {
			NORMAL: 'success',
			LOW: 'warning',
			OUT: 'error'
		}
		return statusMap[status] || 'default'
	}

	// 获取库存状态文本
	const getStockStatusText = (status) => {
		const statusMap = {
			NORMAL: '正常',
			LOW: '低库存',
			OUT: '缺货'
		}
		return statusMap[status] || '未知'
	}

	// 获取设备状态徽章
	const getStatusBadge = (status) => {
		const statusMap = {
			ONLINE: 'success',
			OFFLINE: 'default',
			MAINTENANCE: 'warning'
		}
		return statusMap[status] || 'default'
	}

	// 获取设备状态文本
	const getStatusText = (status) => {
		const statusMap = {
			ONLINE: '在线',
			OFFLINE: '离线',
			MAINTENANCE: '维护中'
		}
		return statusMap[status] || '未知'
	}

	// 酒品筛选
	const filterOption = (input, option) => {
		return option.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
	}

	// 加载设备详情
	const loadDeviceDetail = () => {
		if (deviceId) {
			wineDeviceApi.detail({ id: deviceId }).then((res) => {
				deviceInfo.value = res
			})
		}
	}

	// 加载酒品数据
	const loadProductData = () => {
		wineProductApi.selector().then((res) => {
			productData.value = res
		})
	}

	// 页面加载时执行
	onMounted(() => {
		// 使用 nextTick 确保组件完全挂载后再执行
		nextTick(() => {
			if (deviceId && hasPerm('deviceStockPage')) {
				loadDeviceDetail()
				loadProductData()
			}
		})
	})
</script>

<style scoped>
	.xn-mb10 {
		margin-bottom: 10px;
	}

	.snowy-button-left {
		margin-left: 8px;
	}
</style>
