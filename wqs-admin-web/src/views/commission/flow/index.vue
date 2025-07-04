<template>
	<a-card :bordered="false">
		<!-- 搜索表单 -->
		<a-form ref="searchFormRef" name="searchForm" :model="searchFormState" class="wqs-search-form">
			<a-row :gutter="16">
				<a-col :lg="5" :md="8" :sm="24">
					<a-form-item label="关键词" name="searchKey">
						<a-input v-model:value="searchFormState.searchKey" placeholder="请输入用户昵称/流水号" allow-clear />
					</a-form-item>
				</a-col>
				<a-col :lg="4" :md="8" :sm="24">
					<a-form-item label="流水类型" name="flowType">
						<a-select v-model:value="searchFormState.flowType" placeholder="请选择流水类型" allow-clear>
							<a-select-option value="COMMISSION_INCOME">佣金收入</a-select-option>
							<a-select-option value="WITHDRAW_EXPENSE">提现支出</a-select-option>
							<a-select-option value="REFUND_INCOME">退款收入</a-select-option>
							<a-select-option value="ADJUSTMENT_INCOME">调整收入</a-select-option>
							<a-select-option value="ADJUSTMENT_EXPENSE">调整支出</a-select-option>
							<a-select-option value="FREEZE_BALANCE">冻结余额</a-select-option>
							<a-select-option value="UNFREEZE_BALANCE">解冻余额</a-select-option>
						</a-select>
					</a-form-item>
				</a-col>
				<a-col :lg="4" :md="8" :sm="24">
					<a-form-item label="流水状态" name="status">
						<a-select v-model:value="searchFormState.status" placeholder="请选择流水状态" allow-clear>
							<a-select-option value="SUCCESS">成功</a-select-option>
							<a-select-option value="FAILED">失败</a-select-option>
							<a-select-option value="PENDING">处理中</a-select-option>
						</a-select>
					</a-form-item>
				</a-col>
				<a-col :lg="7" :md="8" :sm="24">
					<a-form-item label="交易时间" name="transactionTimeRange">
						<a-range-picker
							v-model:value="searchFormState.transactionTimeRange"
							:placeholder="['开始时间', '结束时间']"
							format="YYYY-MM-DD"
							valueFormat="YYYY-MM-DD"
						/>
					</a-form-item>
				</a-col>
				<a-col :lg="4" :md="24" :sm="24">
					<a-form-item class="wqs-search-actions">
						<a-space>
							<a-button type="primary" @click="table.refresh()">
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

		<!-- 工具栏 -->
		<div class="wqs-table-actions">
			<a-space>
				<a-button type="primary" @click="exportData" v-if="hasPerm('/commission/flow/export')">
					<template #icon><ExportOutlined /></template>
					导出
				</a-button>
				<a-button @click="showSummary" v-if="hasPerm('/commission/flow/summary')">
					<template #icon><BarChartOutlined /></template>
					统计汇总
				</a-button>
			</a-space>
		</div>

		<!-- 数据表格 -->
		<s-table
			ref="table"
			rowKey="id"
			:columns="columns"
			:data="loadData"
			:alert="options.alert"
			:rowSelection="options.rowSelection"
			:toolConfig="toolConfig"
			:scroll="{ x: 1400 }"
		>
			<template #bodyCell="{ column, record }">
				<template v-if="column.dataIndex === 'userInfo'">
					<div class="user-info">
						<div>
							<a-tag color="blue">{{ record.userNickname || '未知用户' }}</a-tag>
						</div>
						<div class="text-gray">ID: {{ record.userId }}</div>
					</div>
				</template>

				<template v-if="column.dataIndex === 'flowInfo'">
					<div class="flow-info">
						<div>
							<a-tag :color="getFlowTypeColor(record.flowType)">
								{{ getFlowTypeDesc(record.flowType) }}
							</a-tag>
						</div>
						<div class="text-gray">{{ record.flowNo }}</div>
					</div>
				</template>

				<template v-if="column.dataIndex === 'amount'">
					<div class="amount-info">
						<div :class="['amount', record.amount >= 0 ? 'income' : 'expense']">
							{{ record.amount >= 0 ? '+' : '' }}¥{{ formatMoney(record.amount) }}
						</div>
						<div class="text-gray">余额变动: {{ formatMoney(record.balanceChange) }}</div>
					</div>
				</template>

				<template v-if="column.dataIndex === 'balance'">
					<div class="balance-info">
						<div>变动前: ¥{{ formatMoney(record.beforeBalance) }}</div>
						<div>变动后: ¥{{ formatMoney(record.afterBalance) }}</div>
					</div>
				</template>

				<template v-if="column.dataIndex === 'status'">
					<a-tag :color="getStatusColor(record.status)">
						{{ getStatusDesc(record.status) }}
					</a-tag>
				</template>

				<template v-if="column.dataIndex === 'relatedInfo'">
					<div class="related-info" v-if="record.relatedId">
						<div>{{ getRelatedTypeDesc(record.relatedType) }}</div>
						<div class="text-gray">{{ record.relatedId }}</div>
					</div>
					<span v-else class="text-gray">-</span>
				</template>

				<template v-if="column.dataIndex === 'transactionTime'">
					{{ formatDateTime(record.transactionTime) }}
				</template>

				<template v-if="column.dataIndex === 'action'">
					<a-space>
						<a @click="detail(record)" v-if="hasPerm('/commission/flow/detail')">详情</a>
						<a @click="viewRelated(record)" v-if="record.relatedId && hasPerm('/commission/flow/byRelated')">
							查看关联
						</a>
					</a-space>
				</template>
			</template>
		</s-table>

		<!-- 流水详情抽屉 -->
		<a-drawer v-model:open="detailVisible" title="流水详情" :width="600" :body-style="{ paddingBottom: '80px' }">
			<div v-if="detailData">
				<a-descriptions :column="2" bordered>
					<a-descriptions-item label="流水号" :span="2">
						<a-typography-text copyable>{{ detailData.flowNo }}</a-typography-text>
					</a-descriptions-item>
					<a-descriptions-item label="用户昵称">
						<a-tag color="blue">{{ detailData.userNickname || '未知用户' }}</a-tag>
					</a-descriptions-item>
					<a-descriptions-item label="用户ID">
						<a-typography-text copyable>{{ detailData.userId }}</a-typography-text>
					</a-descriptions-item>
					<a-descriptions-item label="流水类型">
						<a-tag :color="getFlowTypeColor(detailData.flowType)">
							{{ getFlowTypeDesc(detailData.flowType) }}
						</a-tag>
					</a-descriptions-item>
					<a-descriptions-item label="流水状态">
						<a-tag :color="getStatusColor(detailData.status)">
							{{ getStatusDesc(detailData.status) }}
						</a-tag>
					</a-descriptions-item>
					<a-descriptions-item label="交易金额">
						<span :class="['amount', detailData.amount >= 0 ? 'income' : 'expense']">
							{{ detailData.amount >= 0 ? '+' : '' }}¥{{ formatMoney(detailData.amount) }}
						</span>
					</a-descriptions-item>
					<a-descriptions-item label="余额变动">
						<span class="balance-change">¥{{ formatMoney(detailData.balanceChange) }}</span>
					</a-descriptions-item>
					<a-descriptions-item label="变动前余额">
						<span class="before-balance">¥{{ formatMoney(detailData.beforeBalance) }}</span>
					</a-descriptions-item>
					<a-descriptions-item label="变动后余额">
						<span class="after-balance">¥{{ formatMoney(detailData.afterBalance) }}</span>
					</a-descriptions-item>
					<a-descriptions-item label="关联类型" v-if="detailData.relatedType">
						{{ getRelatedTypeDesc(detailData.relatedType) }}
					</a-descriptions-item>
					<a-descriptions-item label="关联ID" v-if="detailData.relatedId">
						<a-typography-text copyable>{{ detailData.relatedId }}</a-typography-text>
					</a-descriptions-item>
					<a-descriptions-item label="交易时间">
						{{ formatDateTime(detailData.transactionTime) }}
					</a-descriptions-item>
					<a-descriptions-item label="流水描述" :span="2" v-if="detailData.description">
						{{ detailData.description }}
					</a-descriptions-item>
					<a-descriptions-item label="创建时间">
						{{ formatDateTime(detailData.createTime) }}
					</a-descriptions-item>
					<a-descriptions-item label="更新时间">
						{{ formatDateTime(detailData.updateTime) }}
					</a-descriptions-item>
				</a-descriptions>
			</div>
		</a-drawer>

		<!-- 统计汇总抽屉 -->
		<a-drawer v-model:open="summaryVisible" title="流水统计汇总" :width="800" :body-style="{ paddingBottom: '80px' }">
			<div v-if="summaryData">
				<a-row :gutter="16">
					<a-col :span="12">
						<a-statistic title="总收入金额" :value="summaryData.totalIncomeAmount" :precision="2" prefix="¥" />
					</a-col>
					<a-col :span="12">
						<a-statistic title="总支出金额" :value="summaryData.totalExpenseAmount" :precision="2" prefix="¥" />
					</a-col>
					<a-col :span="12">
						<a-statistic title="收入笔数" :value="summaryData.incomeCount" />
					</a-col>
					<a-col :span="12">
						<a-statistic title="支出笔数" :value="summaryData.expenseCount" />
					</a-col>
				</a-row>

				<a-divider />

				<h4>按流水类型统计</h4>
				<a-table :columns="summaryColumns" :data-source="summaryData.typeStatistics" :pagination="false" size="small" />
			</div>
		</a-drawer>
	</a-card>
</template>

<script setup name="wineAccountFlow">
	import { message } from 'ant-design-vue'
	import { BarChartOutlined, ClearOutlined, ExportOutlined, SearchOutlined } from '@ant-design/icons-vue'
	import { hasPerm } from '@/utils/permission'
	import wineAccountFlowApi from '@/api/commission/wineAccountFlowApi'

	const searchFormRef = ref()
	const table = ref()

	// 搜索表单状态
	const searchFormState = reactive({
		searchKey: '',
		flowType: undefined,
		status: undefined,
		transactionTimeRange: undefined
	})

	// 详情相关状态
	const detailVisible = ref(false)
	const detailData = ref(null)

	// 统计汇总相关状态
	const summaryVisible = ref(false)
	const summaryData = ref(null)

	// 表格配置
	const columns = [
		{
			title: '用户信息',
			dataIndex: 'userInfo',
			width: 150
		},
		{
			title: '流水信息',
			dataIndex: 'flowInfo',
			width: 180
		},
		{
			title: '交易金额',
			dataIndex: 'amount',
			width: 150,
			sorter: true
		},
		{
			title: '余额信息',
			dataIndex: 'balance',
			width: 160
		},
		{
			title: '状态',
			dataIndex: 'status',
			width: 100
		},
		{
			title: '关联信息',
			dataIndex: 'relatedInfo',
			width: 140
		},
		{
			title: '交易时间',
			dataIndex: 'transactionTime',
			width: 150,
			sorter: true
		},
		{
			title: '操作',
			dataIndex: 'action',
			width: 120,
			fixed: 'right'
		}
	]

	// 统计表格列
	const summaryColumns = [
		{
			title: '流水类型',
			dataIndex: 'flowType',
			key: 'flowType'
		},
		{
			title: '笔数',
			dataIndex: 'count',
			key: 'count'
		},
		{
			title: '总金额',
			dataIndex: 'totalAmount',
			key: 'totalAmount'
		}
	]

	const options = reactive({
		alert: {
			show: true,
			clear: () => {
				options.selectedRowKeys = []
			}
		},
		rowSelection: {
			onChange: (selectedRowKeys, selectedRows) => {
				options.selectedRowKeys = selectedRowKeys
			}
		},
		selectedRowKeys: []
	})

	const toolConfig = reactive({
		refresh: true,
		height: true,
		columnSetting: true,
		striped: false
	})

	// 加载数据
	const loadData = (parameter) => {
		const searchParam = { ...searchFormState }

		// 处理时间范围
		if (searchParam.transactionTimeRange && searchParam.transactionTimeRange.length === 2) {
			searchParam.transactionTimeStart = searchParam.transactionTimeRange[0]
			searchParam.transactionTimeEnd = searchParam.transactionTimeRange[1]
		}
		delete searchParam.transactionTimeRange

		// 确保分页参数类型正确
		const requestParam = Object.assign({}, parameter, searchParam)
		if (requestParam.current) {
			requestParam.current = parseInt(requestParam.current)
		}
		if (requestParam.size) {
			requestParam.size = parseInt(requestParam.size)
		}

		return wineAccountFlowApi.wineAccountFlowPage(requestParam).then((data) => {
			return data
		})
	}

	// 重置搜索
	const reset = () => {
		searchFormRef.value.resetFields()
		table.value.refresh()
	}

	// 查看详情
	const detail = (record) => {
		wineAccountFlowApi.wineAccountFlowDetail({ id: record.id }).then((data) => {
			detailData.value = data
			detailVisible.value = true
		})
	}

	// 查看关联流水
	const viewRelated = (record) => {
		if (!record.relatedId) return

		wineAccountFlowApi
			.wineAccountFlowByRelated({
				relatedId: record.relatedId,
				relatedType: record.relatedType
			})
			.then((data) => {
				message.info(`找到 ${data.length} 条关联流水`)
				// 可以在这里打开一个新的抽屉或弹窗显示关联流水
			})
	}

	// 显示统计汇总
	const showSummary = () => {
		const searchParam = { ...searchFormState }

		// 处理时间范围
		if (searchParam.transactionTimeRange && searchParam.transactionTimeRange.length === 2) {
			searchParam.transactionTimeStart = searchParam.transactionTimeRange[0]
			searchParam.transactionTimeEnd = searchParam.transactionTimeRange[1]
		}
		delete searchParam.transactionTimeRange

		wineAccountFlowApi.wineAccountFlowSummary(searchParam).then((data) => {
			summaryData.value = data
			summaryVisible.value = true
		})
	}

	// 导出数据
	const exportData = () => {
		const searchParam = { ...searchFormState }

		// 处理时间范围
		if (searchParam.transactionTimeRange && searchParam.transactionTimeRange.length === 2) {
			searchParam.transactionTimeStart = searchParam.transactionTimeRange[0]
			searchParam.transactionTimeEnd = searchParam.transactionTimeRange[1]
		}
		delete searchParam.transactionTimeRange

		wineAccountFlowApi.wineAccountFlowExport(searchParam).then(() => {
			message.success('导出成功')
		})
	}

	// 格式化金额
	const formatMoney = (amount) => {
		if (amount === null || amount === undefined) return '0.00'
		return Number(amount).toFixed(2)
	}

	// 格式化日期时间
	const formatDateTime = (dateTime) => {
		if (!dateTime) return '-'
		return new Date(dateTime).toLocaleString('zh-CN', {
			year: 'numeric',
			month: '2-digit',
			day: '2-digit',
			hour: '2-digit',
			minute: '2-digit',
			second: '2-digit'
		})
	}

	// 获取流水类型描述
	const getFlowTypeDesc = (flowType) => {
		const typeMap = {
			COMMISSION_INCOME: '佣金收入',
			WITHDRAW_EXPENSE: '提现支出',
			REFUND_INCOME: '退款收入',
			ADJUSTMENT_INCOME: '调整收入',
			ADJUSTMENT_EXPENSE: '调整支出',
			FREEZE_BALANCE: '冻结余额',
			UNFREEZE_BALANCE: '解冻余额'
		}
		return typeMap[flowType] || flowType
	}

	// 获取流水类型颜色
	const getFlowTypeColor = (flowType) => {
		const colorMap = {
			COMMISSION_INCOME: 'green',
			WITHDRAW_EXPENSE: 'red',
			REFUND_INCOME: 'blue',
			ADJUSTMENT_INCOME: 'cyan',
			ADJUSTMENT_EXPENSE: 'orange',
			FREEZE_BALANCE: 'purple',
			UNFREEZE_BALANCE: 'geekblue'
		}
		return colorMap[flowType] || 'default'
	}

	// 获取状态描述
	const getStatusDesc = (status) => {
		const statusMap = {
			SUCCESS: '成功',
			FAILED: '失败',
			PENDING: '处理中'
		}
		return statusMap[status] || status
	}

	// 获取状态颜色
	const getStatusColor = (status) => {
		const colorMap = {
			SUCCESS: 'success',
			FAILED: 'error',
			PENDING: 'processing'
		}
		return colorMap[status] || 'default'
	}

	// 获取关联类型描述
	const getRelatedTypeDesc = (relatedType) => {
		const typeMap = {
			ORDER: '订单',
			COMMISSION: '佣金记录',
			WITHDRAW: '提现记录',
			REFUND: '退款记录',
			ADJUSTMENT: '余额调整'
		}
		return typeMap[relatedType] || relatedType
	}
</script>

<style lang="less" scoped>
	.user-info {
		line-height: 1.4;
	}

	.flow-info {
		line-height: 1.4;
	}

	.amount-info {
		line-height: 1.4;
	}

	.amount {
		font-weight: 500;
		font-size: 14px;

		&.income {
			color: #52c41a;
		}

		&.expense {
			color: #f5222d;
		}
	}

	.balance-info {
		line-height: 1.4;
		font-size: 12px;
	}

	.related-info {
		line-height: 1.4;
	}

	.text-gray {
		color: #999;
		font-size: 12px;
	}

	.wqs-search-form {
		.wqs-search-actions {
			text-align: left;
		}
	}

	.wqs-table-actions {
		margin-bottom: 16px;
	}

	.balance-change {
		color: #1890ff;
		font-weight: 500;
	}

	.before-balance {
		color: #722ed1;
		font-weight: 500;
	}

	.after-balance {
		color: #52c41a;
		font-weight: 500;
	}
</style>
