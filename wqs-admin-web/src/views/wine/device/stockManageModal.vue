<template>
	<div>
		<a-modal
			v-model:visible="visible"
			:title="modalTitle"
			width="1200px"
			:footer="null"
			:destroyOnClose="true"
			:maskClosable="false"
		>
			<!-- 设备信息 -->
			<a-descriptions :column="4" bordered size="small" class="device-info-desc">
				<a-descriptions-item label="设备编码">
					<a-tag color="blue">{{ deviceInfo.deviceCode }}</a-tag>
				</a-descriptions-item>
				<a-descriptions-item label="设备名称">{{ deviceInfo.deviceName }}</a-descriptions-item>
				<a-descriptions-item label="设备状态">
					<a-badge :status="getStatusBadge(deviceInfo.status)" :text="getStatusText(deviceInfo.status)" />
				</a-descriptions-item>
				<a-descriptions-item label="所属门店">{{ deviceInfo.storeName || '未分配' }}</a-descriptions-item>
			</a-descriptions>

			<!-- 搜索表单 -->
			<a-form ref="searchFormRef" name="advanced_search" class="search-form" :model="searchFormState">
				<a-row :gutter="16">
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
								:getPopupContainer="getPopupContainer"
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
								:getPopupContainer="getPopupContainer"
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
						<a-button class="search-btn-left" @click="reset">
							<template #icon><redo-outlined /></template>
							重置
						</a-button>
					</a-col>
				</a-row>
			</a-form>

			<!-- 库存列表 -->
			<s-table
				ref="tableRef"
				:columns="columns"
				:data="loadData"
				:expand-row-by-click="false"
				bordered
				:tool-config="toolConfig"
				:row-key="(record) => record.id"
				:pagination="{ pageSize: 10 }"
				class="small-table"
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

			<!-- 底部按钮 -->
			<template #footer>
				<a-button @click="handleCancel">关闭</a-button>
			</template>
		</a-modal>

		<!-- 库存初始化弹窗 -->
		<InitStockModal v-if="deviceInfo.id" ref="initStockRef" :deviceId="deviceInfo.id" @successful="refreshTable" />

		<!-- 补货弹窗 -->
		<RefillStockModal ref="refillStockRef" @successful="refreshTable" />

		<!-- 调整库存弹窗 -->
		<AdjustStockModal ref="adjustStockRef" @successful="refreshTable" />

		<!-- 库存日志弹窗 -->
		<StockLogModal ref="stockLogRef" />
	</div>
</template>

<script setup name="stockManageModal">
	import { message } from 'ant-design-vue'
	import { computed, reactive, ref } from 'vue'
	import downloadUtil from '@/utils/downloadUtil'
	import { hasPerm } from '@/utils/permission'
	import wineDeviceStockApi from '@/api/wine/wineDeviceStockApi'
	import wineDeviceApi from '@/api/wine/wineDeviceApi'
	import wineProductApi from '@/api/wine/wineProductApi'
	import InitStockModal from './stock/initStockModal.vue'
	import RefillStockModal from './stock/refillStockModal.vue'
	import AdjustStockModal from './stock/adjustStockModal.vue'
	import StockLogModal from './stock/stockLogModal.vue'

	// 弹窗状态
	const visible = ref(false)
	const deviceInfo = ref({})

	// 计算弹窗标题
	const modalTitle = computed(() => {
		return deviceInfo.value.deviceName ? `库存管理 - ${deviceInfo.value.deviceName}` : '库存管理'
	})

	// 表格列定义
	const columns = [
		{
			title: '酒品名称',
			dataIndex: 'productName',
			width: '180px'
		},
		{
			title: '当前库存',
			dataIndex: 'stockQuantity',
			width: '100px',
			sorter: true
		},
		{
			title: '预警阈值',
			dataIndex: 'alertThreshold',
			width: '100px'
		},
		{
			title: '库存状态',
			dataIndex: 'status',
			width: '90px'
		},
		{
			title: '最后补货时间',
			dataIndex: 'lastRefillTime',
			width: '140px'
		},
		{
			title: '更新时间',
			dataIndex: 'updateTime',
			width: '140px'
		},
		{
			title: '操作',
			dataIndex: 'action',
			align: 'center',
			fixed: 'right',
			width: '120px'
		}
	]

	// 搜索表单状态
	let searchFormState = reactive({})
	const tableRef = ref()
	const initStockRef = ref()
	const refillStockRef = ref()
	const adjustStockRef = ref()
	const stockLogRef = ref()
	const toolConfig = reactive({ refresh: true, height: false, columnSetting: true, striped: false })

	// 酒品数据
	const productData = ref([])

	// 加载数据
	const loadData = (parameter) => {
		if (!deviceInfo.value.id) {
			return Promise.resolve({ data: [], total: 0 })
		}
		const searchParam = { ...searchFormState, deviceId: deviceInfo.value.id }
		return wineDeviceStockApi.page(Object.assign(parameter, searchParam)).then((res) => {
			// @Trans注解会自动填充productName和deviceName等信息
			return res
		})
	}

	// 重置搜索
	const reset = () => {
		searchFormState = reactive({})
		tableRef.value?.refresh(true)
	}

	// 导出数据
	const exportData = () => {
		const searchParam = { ...searchFormState, deviceId: deviceInfo.value.id }
		message.loading('正在导出数据，请稍候...', 0)
		wineDeviceStockApi.export(searchParam).then((res) => {
			downloadUtil.resultDownload(res, `${deviceInfo.value.deviceName}_库存列表.xlsx`, 'application/vnd.ms-excel')
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

	// 获取弹窗容器
	const getPopupContainer = (trigger) => {
		return trigger.parentNode
	}

	// 加载设备详情
	const loadDeviceDetail = (deviceId) => {
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

	// 打开弹窗
	const onOpen = (record) => {
		visible.value = true
		deviceInfo.value = record
		searchFormState = reactive({})

		// 加载设备详情和酒品数据
		loadDeviceDetail(record.id)
		loadProductData()

		// 延迟刷新表格，确保弹窗完全打开后再加载数据
		setTimeout(() => {
			tableRef.value?.refresh(true)
		}, 100)
	}

	// 关闭弹窗
	const handleCancel = () => {
		visible.value = false
		deviceInfo.value = {}
		searchFormState = reactive({})
	}

	// 暴露方法
	defineExpose({
		onOpen
	})
</script>

<style scoped>
	.device-info-desc {
		margin-bottom: 16px;
	}

	.search-form {
		margin-bottom: 16px;
		padding: 16px;
		background-color: #fafafa;
		border-radius: 6px;
	}

	.search-btn-left {
		margin-left: 8px;
	}

	.table-operator {
		margin-bottom: 16px;
	}

	:deep(.small-table .ant-table-tbody > tr > td) {
		padding: 8px;
		font-size: 12px;
	}

	:deep(.small-table .ant-table-thead > tr > th) {
		padding: 8px;
		font-size: 12px;
	}

	:deep(.ant-descriptions-bordered .ant-descriptions-item-label) {
		background-color: #fafafa;
		font-weight: 500;
	}
</style>
