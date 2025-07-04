<template>
	<div>
		<!-- 搜索区域 -->
		<a-card :bordered="false" style="margin-bottom: 16px">
			<a-form ref="searchFormRef" name="searchForm" :model="searchFormState" layout="inline">
				<a-row :gutter="16">
					<a-col :span="6">
						<a-form-item label="订单号" name="orderNo">
							<a-input v-model:value="searchFormState.orderNo" placeholder="请输入订单号" allow-clear />
						</a-form-item>
					</a-col>
					<a-col :span="6">
						<a-form-item label="佣金类型" name="commissionType">
							<a-select v-model:value="searchFormState.commissionType" placeholder="请选择佣金类型" allow-clear>
								<a-select-option value="PLATFORM">平台佣金</a-select-option>
								<a-select-option value="DEVICE_OWNER">设备方佣金</a-select-option>
								<a-select-option value="LOCATION_PROVIDER">场地方佣金</a-select-option>
								<a-select-option value="STORE_MANAGER">门店管理员佣金</a-select-option>
								<a-select-option value="SUPPLIER">供应商佣金</a-select-option>
							</a-select>
						</a-form-item>
					</a-col>
					<a-col :span="6">
						<a-form-item label="佣金状态" name="status">
							<a-select v-model:value="searchFormState.status" placeholder="请选择佣金状态" allow-clear>
								<a-select-option value="PENDING">待计算</a-select-option>
								<a-select-option value="CALCULATED">已计算</a-select-option>
								<a-select-option value="SETTLED">已发放</a-select-option>
								<a-select-option value="FROZEN">已冻结</a-select-option>
								<a-select-option value="CANCELLED">已取消</a-select-option>
							</a-select>
						</a-form-item>
					</a-col>
					<a-col :span="6">
						<a-form-item>
							<a-space>
								<a-button type="primary" @click="tableRef.refresh()">
									<template #icon><SearchOutlined /></template>
									查询
								</a-button>
								<a-button @click="reset">
									<template #icon><ClearOutlined /></template>
									重置
								</a-button>
							</a-space>
						</a-form-item>
					</a-col>
				</a-row>
			</a-form>
		</a-card>

		<!-- 数据表格 -->
		<a-card :bordered="false">
			<s-table
				ref="tableRef"
				:columns="columns"
				:data="loadData"
				:expand-row-by-click="false"
				bordered
				:alert="options.alert.show"
				:tool-config="toolConfig"
				:row-key="(record) => record.id"
				:row-selection="options.rowSelection"
			>
				<template #operator class="table-operator">
					<a-space>
						<a-button @click="exportData" v-if="hasPerm('commissionRecordExport')">
							<template #icon><ExportOutlined /></template>
							导出
						</a-button>
						<a-dropdown
							v-if="
								hasPerm(['commissionRecordBatchSettle', 'commissionRecordBatchFreeze', 'commissionRecordBatchCancel'])
							"
						>
							<a-button type="primary" :disabled="selectedRowKeys.length === 0">
								<template #icon><SettingOutlined /></template>
								批量操作
								<DownOutlined />
							</a-button>
							<template #overlay>
								<a-menu @click="handleBatchAction">
									<a-menu-item key="batchSettle" v-if="hasPerm('commissionRecordBatchSettle')">
										<CheckCircleOutlined />
										批量发放
									</a-menu-item>
									<a-menu-item key="batchFreeze" v-if="hasPerm('commissionRecordBatchFreeze')">
										<PauseCircleOutlined />
										批量冻结
									</a-menu-item>
									<a-menu-item key="batchCancel" v-if="hasPerm('commissionRecordBatchCancel')">
										<CloseCircleOutlined />
										批量取消
									</a-menu-item>
								</a-menu>
							</template>
						</a-dropdown>
					</a-space>
				</template>

				<template #bodyCell="{ column, record }">
					<template v-if="column.dataIndex === 'orderInfo'">
						<div class="order-info">
							<div>
								<strong>{{ record.orderNo }}</strong>
							</div>
							<div class="text-gray">{{ record.wineName }}</div>
						</div>
					</template>

					<template v-if="column.dataIndex === 'userInfo'">
						<div class="user-info">
							<div>{{ record.userNickname || '未知用户' }}</div>
							<div class="text-gray">设备: {{ record.deviceCode || '未知设备' }}</div>
						</div>
					</template>

					<template v-if="column.dataIndex === 'commissionInfo'">
						<div class="commission-info">
							<div>
								<a-tag :color="getCommissionTypeColor(record.commissionType)">
									{{ getCommissionTypeDesc(record.commissionType) }}
								</a-tag>
							</div>
							<div class="amount-info">
								<span class="rate">{{ record.commissionRate }}%</span>
								<span class="amount">¥{{ record.commissionAmount }}</span>
							</div>
						</div>
					</template>

					<template v-if="column.dataIndex === 'orderAmount'">
						<span class="order-amount">¥{{ record.orderAmount }}</span>
					</template>

					<template v-if="column.dataIndex === 'status'">
						<a-tag :color="getStatusColor(record.status)">
							{{ getStatusDesc(record.status) }}
						</a-tag>
					</template>

					<template v-if="column.dataIndex === 'timeInfo'">
						<div class="time-info">
							<div v-if="record.calculateTime">
								<span class="label">计算:</span>
								<span>{{ formatDateTime(record.calculateTime) }}</span>
							</div>
							<div v-if="record.settleTime">
								<span class="label">发放:</span>
								<span>{{ formatDateTime(record.settleTime) }}</span>
							</div>
						</div>
					</template>

					<template v-if="column.dataIndex === 'action'">
						<a-space>
							<a @click="viewDetail(record)" v-if="hasPerm('commissionRecordDetail')">详情</a>

							<a-dropdown v-if="hasOperationPerm(record)">
								<a class="ant-dropdown-link">
									操作
									<DownOutlined />
								</a>
								<template #overlay>
									<a-menu @click="({ key }) => handleAction(key, record)">
										<a-menu-item key="settle" v-if="canSettle(record) && hasPerm('commissionRecordSettle')">
											<CheckCircleOutlined />
											发放佣金
										</a-menu-item>
										<a-menu-item key="freeze" v-if="canFreeze(record) && hasPerm('commissionRecordFreeze')">
											<PauseCircleOutlined />
											冻结佣金
										</a-menu-item>
										<a-menu-item key="cancel" v-if="canCancel(record) && hasPerm('commissionRecordCancel')">
											<CloseCircleOutlined />
											取消佣金
										</a-menu-item>
										<a-menu-item
											key="recalculate"
											v-if="canRecalculate(record) && hasPerm('commissionRecordRecalculate')"
										>
											<ReloadOutlined />
											重新计算
										</a-menu-item>
									</a-menu>
								</template>
							</a-dropdown>
						</a-space>
					</template>
				</template>
			</s-table>
		</a-card>

		<!-- 详情抽屉 -->
		<DetailDrawer ref="detailDrawerRef" />
	</div>
</template>

<script setup name="wineCommissionRecord">
	import { message, Modal } from 'ant-design-vue'
	import {
		CheckCircleOutlined,
		ClearOutlined,
		CloseCircleOutlined,
		DownOutlined,
		ExportOutlined,
		PauseCircleOutlined,
		ReloadOutlined,
		SearchOutlined,
		SettingOutlined
	} from '@ant-design/icons-vue'
	import tool from '@/utils/tool'
	import downloadUtil from '@/utils/downloadUtil'
	import { hasPerm } from '@/utils/permission'
	import wineCommissionRecordApi from '@/api/wine/wineCommissionRecordApi'
	import DetailDrawer from './detail.vue'

	// 表格列定义
	const columns = [
		{
			title: '订单信息',
			dataIndex: 'orderInfo',
			width: '200px'
		},
		{
			title: '用户信息',
			dataIndex: 'userInfo',
			width: '180px'
		},
		{
			title: '佣金信息',
			dataIndex: 'commissionInfo',
			width: '200px'
		},
		{
			title: '订单金额',
			dataIndex: 'orderAmount',
			width: '100px',
			align: 'right'
		},
		{
			title: '状态',
			dataIndex: 'status',
			width: '100px',
			align: 'center'
		},
		{
			title: '时间信息',
			dataIndex: 'timeInfo',
			width: '200px'
		},
		{
			title: '操作',
			dataIndex: 'action',
			align: 'center',
			width: '120px',
			fixed: 'right'
		}
	]

	// 搜索表单状态
	const searchFormState = reactive({
		orderNo: '',
		commissionType: undefined,
		status: undefined,
		userId: undefined
	})

	const searchFormRef = ref()
	const tableRef = ref()
	const detailDrawerRef = ref()
	const loading = ref(false)

	// 表格选择
	const options = reactive({
		alert: {
			show: false,
			clear: () => {
				selectedRowKeys.value = []
			}
		},
		rowSelection: {
			onChange: (selectedRowKeyValues, selectedRows) => {
				selectedRowKeys.value = selectedRowKeyValues
			}
		}
	})
	const selectedRowKeys = ref([])

	// 工具栏配置
	const toolConfig = reactive({
		refresh: true,
		height: true,
		columnSetting: true,
		striped: false
	})

	// 加载数据
	const loadData = (parameter) => {
		const searchParam = Object.assign({}, searchFormState)
		return wineCommissionRecordApi.page(Object.assign(parameter, searchParam)).then((res) => {
			return res
		})
	}

	// 重置搜索
	const reset = () => {
		searchFormRef.value.resetFields()
		tableRef.value.refresh()
	}

	// 获取佣金类型描述
	const getCommissionTypeDesc = (type) => {
		const typeMap = {
			PLATFORM: '平台佣金',
			DEVICE_OWNER: '设备方佣金',
			LOCATION_PROVIDER: '场地方佣金',
			STORE_MANAGER: '门店管理员佣金',
			SUPPLIER: '供应商佣金'
		}
		return typeMap[type] || type
	}

	// 获取佣金类型颜色
	const getCommissionTypeColor = (type) => {
		const colorMap = {
			PLATFORM: 'blue',
			DEVICE_OWNER: 'green',
			LOCATION_PROVIDER: 'orange',
			STORE_MANAGER: 'purple',
			SUPPLIER: 'cyan'
		}
		return colorMap[type] || 'default'
	}

	// 获取状态描述
	const getStatusDesc = (status) => {
		const statusMap = {
			PENDING: '待计算',
			CALCULATED: '已计算',
			SETTLED: '已发放',
			FROZEN: '已冻结',
			CANCELLED: '已取消'
		}
		return statusMap[status] || status
	}

	// 获取状态颜色
	const getStatusColor = (status) => {
		const colorMap = {
			PENDING: 'default',
			CALCULATED: 'processing',
			SETTLED: 'success',
			FROZEN: 'warning',
			CANCELLED: 'error'
		}
		return colorMap[status] || 'default'
	}

	// 格式化日期时间
	const formatDateTime = (dateTime) => {
		return tool.formatDate(dateTime, 'YYYY-MM-DD HH:mm:ss')
	}

	// 检查是否可以发放
	const canSettle = (record) => {
		return record.status === 'CALCULATED'
	}

	// 检查是否可以冻结
	const canFreeze = (record) => {
		return record.status !== 'CANCELLED' && record.status !== 'FROZEN'
	}

	// 检查是否可以取消
	const canCancel = (record) => {
		return record.status !== 'SETTLED' && record.status !== 'CANCELLED'
	}

	// 检查是否可以重新计算
	const canRecalculate = (record) => {
		return record.status === 'PENDING' || record.status === 'CALCULATED'
	}

	// 检查是否有操作权限
	const hasOperationPerm = (record) => {
		return (
			(canSettle(record) && hasPerm('commissionRecordSettle')) ||
			(canFreeze(record) && hasPerm('commissionRecordFreeze')) ||
			(canCancel(record) && hasPerm('commissionRecordCancel')) ||
			(canRecalculate(record) && hasPerm('commissionRecordRecalculate'))
		)
	}

	// 查看详情
	const viewDetail = (record) => {
		detailDrawerRef.value.open(record.id)
	}

	// 处理单个操作
	const handleAction = (action, record) => {
		const actionMap = {
			settle: { api: wineCommissionRecordApi.settle, message: '发放佣金', confirmText: '确定要发放这笔佣金吗？' },
			freeze: { api: wineCommissionRecordApi.freeze, message: '冻结佣金', confirmText: '确定要冻结这笔佣金吗？' },
			cancel: { api: wineCommissionRecordApi.cancel, message: '取消佣金', confirmText: '确定要取消这笔佣金吗？' },
			recalculate: {
				api: wineCommissionRecordApi.recalculate,
				message: '重新计算佣金',
				confirmText: '确定要重新计算这笔佣金吗？'
			}
		}

		const actionConfig = actionMap[action]
		if (!actionConfig) return

		Modal.confirm({
			title: actionConfig.message,
			content: actionConfig.confirmText,
			onOk() {
				loading.value = true
				actionConfig
					.api({ id: record.id })
					.then(() => {
						message.success(actionConfig.message + '成功')
						tableRef.value.refresh()
					})
					.finally(() => {
						loading.value = false
					})
			}
		})
	}

	// 处理批量操作
	const handleBatchAction = ({ key }) => {
		if (selectedRowKeys.value.length === 0) {
			message.warning('请选择要操作的记录')
			return
		}

		const actionMap = {
			batchSettle: {
				api: wineCommissionRecordApi.batchSettle,
				message: '批量发放佣金',
				confirmText: `确定要发放选中的 ${selectedRowKeys.value.length} 笔佣金吗？`
			},
			batchFreeze: {
				api: wineCommissionRecordApi.batchFreeze,
				message: '批量冻结佣金',
				confirmText: `确定要冻结选中的 ${selectedRowKeys.value.length} 笔佣金吗？`
			},
			batchCancel: {
				api: wineCommissionRecordApi.batchCancel,
				message: '批量取消佣金',
				confirmText: `确定要取消选中的 ${selectedRowKeys.value.length} 笔佣金吗？`
			}
		}

		const actionConfig = actionMap[key]
		if (!actionConfig) return

		Modal.confirm({
			title: actionConfig.message,
			content: actionConfig.confirmText,
			onOk() {
				const params = selectedRowKeys.value.map((id) => ({ id }))
				loading.value = true
				actionConfig
					.api(params)
					.then(() => {
						message.success(actionConfig.message + '成功')
						selectedRowKeys.value = []
						tableRef.value.refresh()
					})
					.finally(() => {
						loading.value = false
					})
			}
		})
	}

	// 导出数据
	const exportData = () => {
		const param = Object.assign({}, searchFormState)
		wineCommissionRecordApi.export(param).then((res) => {
			downloadUtil.resultDownload(res, '佣金记录数据.xlsx', 'application/vnd.ms-excel')
		})
	}
</script>

<style scoped>
	.order-info {
		line-height: 1.4;
	}

	.user-info {
		line-height: 1.4;
	}

	.commission-info {
		line-height: 1.4;
	}

	.amount-info {
		margin-top: 4px;
	}

	.rate {
		color: #1890ff;
		margin-right: 8px;
	}

	.amount {
		color: #f5222d;
		font-weight: 500;
	}

	.order-amount {
		color: #52c41a;
		font-weight: 500;
	}

	.time-info {
		line-height: 1.4;
		font-size: 12px;
	}

	.time-info .label {
		color: #999;
		margin-right: 4px;
	}

	.text-gray {
		color: #999;
		font-size: 12px;
	}
</style>
