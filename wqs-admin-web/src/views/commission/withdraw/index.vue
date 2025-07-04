<template>
	<a-card :bordered="false">
		<!-- 搜索表单 -->
		<a-form ref="searchFormRef" name="searchForm" :model="searchFormState" class="wqs-search-form">
			<a-row :gutter="16">
				<a-col :lg="5" :md="8" :sm="24">
					<a-form-item label="关键词" name="searchKey">
						<a-input v-model:value="searchFormState.searchKey" placeholder="请输入用户昵称/提现单号" allow-clear />
					</a-form-item>
				</a-col>
				<a-col :lg="4" :md="8" :sm="24">
					<a-form-item label="提现类型" name="withdrawType">
						<a-select v-model:value="searchFormState.withdrawType" placeholder="请选择提现类型" allow-clear>
							<a-select-option value="WECHAT">微信</a-select-option>
							<a-select-option value="ALIPAY">支付宝</a-select-option>
							<a-select-option value="BANK_CARD">银行卡</a-select-option>
						</a-select>
					</a-form-item>
				</a-col>
				<a-col :lg="4" :md="8" :sm="24">
					<a-form-item label="提现状态" name="status">
						<a-select v-model:value="searchFormState.status" placeholder="请选择提现状态" allow-clear>
							<a-select-option value="PENDING">待审核</a-select-option>
							<a-select-option value="APPROVED">审核通过</a-select-option>
							<a-select-option value="REJECTED">审核拒绝</a-select-option>
							<a-select-option value="PROCESSING">处理中</a-select-option>
							<a-select-option value="SUCCESS">提现成功</a-select-option>
							<a-select-option value="FAILED">提现失败</a-select-option>
							<a-select-option value="CANCELLED">已取消</a-select-option>
						</a-select>
					</a-form-item>
				</a-col>
				<a-col :lg="7" :md="8" :sm="24">
					<a-form-item label="申请时间" name="applyTimeRange">
						<a-range-picker
							v-model:value="searchFormState.applyTimeRange"
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
				<a-button type="primary" @click="exportData" v-if="hasPerm('/commission/withdraw/export')">
					<template #icon><ExportOutlined /></template>
					导出
				</a-button>
				<a-button @click="showSummary" v-if="hasPerm('/commission/withdraw/summary')">
					<template #icon><BarChartOutlined /></template>
					统计汇总
				</a-button>
				<a-button @click="showPendingCount" v-if="hasPerm('/commission/withdraw/countPending')">
					<template #icon><ClockCircleOutlined /></template>
					待审核统计
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
			:scroll="{ x: 1600 }"
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

				<template v-if="column.dataIndex === 'withdrawInfo'">
					<div class="withdraw-info">
						<div>
							<a-tag :color="getWithdrawTypeColor(record.withdrawType)">
								{{ getWithdrawTypeDesc(record.withdrawType) }}
							</a-tag>
						</div>
						<div class="text-gray">{{ record.withdrawNo }}</div>
					</div>
				</template>

				<template v-if="column.dataIndex === 'amount'">
					<div class="amount-info">
						<div class="withdraw-amount">¥{{ formatMoney(record.withdrawAmount) }}</div>
						<div class="text-gray">手续费: ¥{{ formatMoney(record.fee) }}</div>
						<div class="actual-amount">实际: ¥{{ formatMoney(record.actualAmount) }}</div>
					</div>
				</template>

				<template v-if="column.dataIndex === 'accountInfo'">
					<div class="account-info">
						<div>{{ record.accountName || '未知账户' }}</div>
						<div class="text-gray">{{ maskAccountInfo(record.accountInfo) }}</div>
					</div>
				</template>

				<template v-if="column.dataIndex === 'status'">
					<a-tag :color="getStatusColor(record.status)">
						{{ getStatusDesc(record.status) }}
					</a-tag>
				</template>

				<template v-if="column.dataIndex === 'timeInfo'">
					<div class="time-info">
						<div>申请: {{ formatDateTime(record.applyTime) }}</div>
						<div v-if="record.auditTime" class="text-gray">审核: {{ formatDateTime(record.auditTime) }}</div>
						<div v-if="record.completeTime" class="text-gray">完成: {{ formatDateTime(record.completeTime) }}</div>
					</div>
				</template>

				<template v-if="column.dataIndex === 'action'">
					<a-space>
						<a @click="detail(record)" v-if="hasPerm('/commission/withdraw/detail')">详情</a>

						<a-dropdown v-if="hasOperationPerm(record)">
							<a class="ant-dropdown-link">
								操作
								<DownOutlined />
							</a>
							<template #overlay>
								<a-menu @click="({ key }) => handleAction(key, record)">
									<a-menu-item key="audit" v-if="canAudit(record) && hasPerm('/commission/withdraw/audit')">
										<CheckCircleOutlined />
										审核
									</a-menu-item>
									<a-menu-item key="cancel" v-if="canCancel(record) && hasPerm('/commission/withdraw/cancel')">
										<CloseCircleOutlined />
										取消
									</a-menu-item>
									<a-menu-item
										key="confirmSuccess"
										v-if="canConfirmSuccess(record) && hasPerm('/commission/withdraw/confirmSuccess')"
									>
										<CheckOutlined />
										确认成功
									</a-menu-item>
									<a-menu-item
										key="markFailed"
										v-if="canMarkFailed(record) && hasPerm('/commission/withdraw/markFailed')"
									>
										<ExclamationCircleOutlined />
										标记失败
									</a-menu-item>
								</a-menu>
							</template>
						</a-dropdown>
					</a-space>
				</template>
			</template>
		</s-table>

		<!-- 提现详情抽屉 -->
		<a-drawer v-model:open="detailVisible" title="提现详情" :width="600" :body-style="{ paddingBottom: '80px' }">
			<div v-if="detailData">
				<a-descriptions :column="2" bordered>
					<a-descriptions-item label="提现单号" :span="2">
						<a-typography-text copyable>{{ detailData.withdrawNo }}</a-typography-text>
					</a-descriptions-item>
					<a-descriptions-item label="用户昵称">
						<a-tag color="blue">{{ detailData.userNickname || '未知用户' }}</a-tag>
					</a-descriptions-item>
					<a-descriptions-item label="用户ID">
						<a-typography-text copyable>{{ detailData.userId }}</a-typography-text>
					</a-descriptions-item>
					<a-descriptions-item label="提现类型">
						<a-tag :color="getWithdrawTypeColor(detailData.withdrawType)">
							{{ getWithdrawTypeDesc(detailData.withdrawType) }}
						</a-tag>
					</a-descriptions-item>
					<a-descriptions-item label="提现状态">
						<a-tag :color="getStatusColor(detailData.status)">
							{{ getStatusDesc(detailData.status) }}
						</a-tag>
					</a-descriptions-item>
					<a-descriptions-item label="提现金额">
						<span class="withdraw-amount">¥{{ formatMoney(detailData.withdrawAmount) }}</span>
					</a-descriptions-item>
					<a-descriptions-item label="手续费">
						<span class="fee-amount">¥{{ formatMoney(detailData.fee) }}</span>
					</a-descriptions-item>
					<a-descriptions-item label="实际到账">
						<span class="actual-amount">¥{{ formatMoney(detailData.actualAmount) }}</span>
					</a-descriptions-item>
					<a-descriptions-item label="账户名称">
						{{ detailData.accountName || '未知账户' }}
					</a-descriptions-item>
					<a-descriptions-item label="账户信息">
						{{ maskAccountInfo(detailData.accountInfo) }}
					</a-descriptions-item>
					<a-descriptions-item label="申请时间">
						{{ formatDateTime(detailData.applyTime) }}
					</a-descriptions-item>
					<a-descriptions-item label="审核时间" v-if="detailData.auditTime">
						{{ formatDateTime(detailData.auditTime) }}
					</a-descriptions-item>
					<a-descriptions-item label="完成时间" v-if="detailData.completeTime">
						{{ formatDateTime(detailData.completeTime) }}
					</a-descriptions-item>
					<a-descriptions-item label="审核人" v-if="detailData.auditorId">
						{{ detailData.auditorId }}
					</a-descriptions-item>
					<a-descriptions-item label="审核备注" :span="2" v-if="detailData.auditRemark">
						{{ detailData.auditRemark }}
					</a-descriptions-item>
					<a-descriptions-item label="失败原因" :span="2" v-if="detailData.failReason">
						<a-alert :message="detailData.failReason" type="error" show-icon />
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

		<!-- 审核弹窗 -->
		<a-modal
			v-model:open="auditVisible"
			title="审核提现"
			:width="500"
			@ok="handleAuditSubmit"
			@cancel="handleAuditCancel"
		>
			<a-form ref="auditFormRef" :model="auditFormState" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
				<a-form-item label="审核结果" name="auditResult" :rules="[{ required: true, message: '请选择审核结果' }]">
					<a-radio-group v-model:value="auditFormState.auditResult">
						<a-radio value="APPROVED">审核通过</a-radio>
						<a-radio value="REJECTED">审核拒绝</a-radio>
					</a-radio-group>
				</a-form-item>
				<a-form-item label="审核备注" name="auditRemark" :rules="[{ required: true, message: '请输入审核备注' }]">
					<a-textarea v-model:value="auditFormState.auditRemark" placeholder="请输入审核备注" :rows="3" />
				</a-form-item>
			</a-form>
		</a-modal>

		<!-- 统计汇总抽屉 -->
		<a-drawer v-model:open="summaryVisible" title="提现统计汇总" :width="800" :body-style="{ paddingBottom: '80px' }">
			<div v-if="summaryData">
				<a-row :gutter="16">
					<a-col :span="8">
						<a-statistic title="总提现金额" :value="summaryData.totalWithdrawAmount" :precision="2" prefix="¥" />
					</a-col>
					<a-col :span="8">
						<a-statistic title="总手续费" :value="summaryData.totalFee" :precision="2" prefix="¥" />
					</a-col>
					<a-col :span="8">
						<a-statistic title="实际到账金额" :value="summaryData.totalActualAmount" :precision="2" prefix="¥" />
					</a-col>
					<a-col :span="8">
						<a-statistic title="提现笔数" :value="summaryData.totalCount" />
					</a-col>
					<a-col :span="8">
						<a-statistic title="成功笔数" :value="summaryData.successCount" />
					</a-col>
					<a-col :span="8">
						<a-statistic title="成功率" :value="summaryData.successRate" :precision="2" suffix="%" />
					</a-col>
				</a-row>

				<a-divider />

				<h4>按提现类型统计</h4>
				<a-table :columns="summaryColumns" :data-source="summaryData.typeStatistics" :pagination="false" size="small" />
			</div>
		</a-drawer>
	</a-card>
</template>

<script setup name="wineWithdrawRecord">
	import { message, Modal } from 'ant-design-vue'
	import {
		BarChartOutlined,
		CheckCircleOutlined,
		CheckOutlined,
		ClearOutlined,
		ClockCircleOutlined,
		CloseCircleOutlined,
		DownOutlined,
		ExclamationCircleOutlined,
		ExportOutlined,
		SearchOutlined
	} from '@ant-design/icons-vue'
	import { hasPerm } from '@/utils/permission'
	import wineWithdrawRecordApi from '@/api/commission/wineWithdrawRecordApi'

	const searchFormRef = ref()
	const auditFormRef = ref()
	const table = ref()

	// 搜索表单状态
	const searchFormState = reactive({
		searchKey: '',
		withdrawType: undefined,
		status: undefined,
		applyTimeRange: undefined
	})

	// 详情相关状态
	const detailVisible = ref(false)
	const detailData = ref(null)

	// 审核相关状态
	const auditVisible = ref(false)
	const auditFormState = reactive({
		id: '',
		auditResult: undefined,
		auditRemark: ''
	})

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
			title: '提现信息',
			dataIndex: 'withdrawInfo',
			width: 180
		},
		{
			title: '金额信息',
			dataIndex: 'amount',
			width: 150,
			sorter: true
		},
		{
			title: '账户信息',
			dataIndex: 'accountInfo',
			width: 160
		},
		{
			title: '状态',
			dataIndex: 'status',
			width: 100
		},
		{
			title: '时间信息',
			dataIndex: 'timeInfo',
			width: 200
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
			title: '提现类型',
			dataIndex: 'withdrawType',
			key: 'withdrawType'
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
		},
		{
			title: '成功率',
			dataIndex: 'successRate',
			key: 'successRate'
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
		if (searchParam.applyTimeRange && searchParam.applyTimeRange.length === 2) {
			searchParam.applyTimeStart = searchParam.applyTimeRange[0]
			searchParam.applyTimeEnd = searchParam.applyTimeRange[1]
		}
		delete searchParam.applyTimeRange

		// 确保分页参数类型正确
		const requestParam = Object.assign({}, parameter, searchParam)
		if (requestParam.current) {
			requestParam.current = parseInt(requestParam.current)
		}
		if (requestParam.size) {
			requestParam.size = parseInt(requestParam.size)
		}

		return wineWithdrawRecordApi.wineWithdrawRecordPage(requestParam).then((data) => {
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
		wineWithdrawRecordApi.wineWithdrawRecordDetail({ id: record.id }).then((data) => {
			detailData.value = data
			detailVisible.value = true
		})
	}

	// 处理操作
	const handleAction = (action, record) => {
		switch (action) {
			case 'audit':
				showAudit(record)
				break
			case 'cancel':
				cancelWithdraw(record)
				break
			case 'confirmSuccess':
				confirmSuccess(record)
				break
			case 'markFailed':
				markFailed(record)
				break
		}
	}

	// 显示审核弹窗
	const showAudit = (record) => {
		auditFormState.id = record.id
		auditFormState.auditResult = undefined
		auditFormState.auditRemark = ''
		auditVisible.value = true
	}

	// 提交审核
	const handleAuditSubmit = () => {
		auditFormRef.value.validate().then(() => {
			wineWithdrawRecordApi.wineWithdrawRecordAudit(auditFormState).then(() => {
				message.success('审核成功')
				auditVisible.value = false
				table.value.refresh()
			})
		})
	}

	// 取消审核
	const handleAuditCancel = () => {
		auditFormRef.value.resetFields()
		auditVisible.value = false
	}

	// 取消提现
	const cancelWithdraw = (record) => {
		Modal.confirm({
			title: '取消提现',
			content: '确定要取消这笔提现吗？',
			onOk() {
				wineWithdrawRecordApi.wineWithdrawRecordCancel({ id: record.id }).then(() => {
					message.success('取消成功')
					table.value.refresh()
				})
			}
		})
	}

	// 确认提现成功
	const confirmSuccess = (record) => {
		Modal.confirm({
			title: '确认提现成功',
			content: '确定要标记这笔提现为成功吗？',
			onOk() {
				wineWithdrawRecordApi.wineWithdrawRecordConfirmSuccess({ id: record.id }).then(() => {
					message.success('确认成功')
					table.value.refresh()
				})
			}
		})
	}

	// 标记提现失败
	const markFailed = (record) => {
		Modal.confirm({
			title: '标记提现失败',
			content: '确定要标记这笔提现为失败吗？',
			onOk() {
				wineWithdrawRecordApi.wineWithdrawRecordMarkFailed({ id: record.id }).then(() => {
					message.success('标记成功')
					table.value.refresh()
				})
			}
		})
	}

	// 显示统计汇总
	const showSummary = () => {
		const searchParam = { ...searchFormState }

		// 处理时间范围
		if (searchParam.applyTimeRange && searchParam.applyTimeRange.length === 2) {
			searchParam.applyTimeStart = searchParam.applyTimeRange[0]
			searchParam.applyTimeEnd = searchParam.applyTimeRange[1]
		}
		delete searchParam.applyTimeRange

		wineWithdrawRecordApi.wineWithdrawRecordSummary(searchParam).then((data) => {
			summaryData.value = data
			summaryVisible.value = true
		})
	}

	// 显示待审核统计
	const showPendingCount = () => {
		wineWithdrawRecordApi.wineWithdrawRecordCountPending().then((data) => {
			message.info(`当前有 ${data} 笔提现待审核`)
		})
	}

	// 导出数据
	const exportData = () => {
		const searchParam = { ...searchFormState }

		// 处理时间范围
		if (searchParam.applyTimeRange && searchParam.applyTimeRange.length === 2) {
			searchParam.applyTimeStart = searchParam.applyTimeRange[0]
			searchParam.applyTimeEnd = searchParam.applyTimeRange[1]
		}
		delete searchParam.applyTimeRange

		wineWithdrawRecordApi.wineWithdrawRecordExport(searchParam).then(() => {
			message.success('导出成功')
		})
	}

	// 检查是否可以审核
	const canAudit = (record) => {
		return record.status === 'PENDING'
	}

	// 检查是否可以取消
	const canCancel = (record) => {
		return record.status === 'PENDING' || record.status === 'APPROVED'
	}

	// 检查是否可以确认成功
	const canConfirmSuccess = (record) => {
		return record.status === 'PROCESSING'
	}

	// 检查是否可以标记失败
	const canMarkFailed = (record) => {
		return record.status === 'PROCESSING'
	}

	// 检查是否有操作权限
	const hasOperationPerm = (record) => {
		return (
			(canAudit(record) && hasPerm('/commission/withdraw/audit')) ||
			(canCancel(record) && hasPerm('/commission/withdraw/cancel')) ||
			(canConfirmSuccess(record) && hasPerm('/commission/withdraw/confirmSuccess')) ||
			(canMarkFailed(record) && hasPerm('/commission/withdraw/markFailed'))
		)
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

	// 掩码账户信息
	const maskAccountInfo = (accountInfo) => {
		if (!accountInfo) return '-'
		if (accountInfo.length <= 8) return accountInfo
		return accountInfo.substring(0, 4) + '****' + accountInfo.substring(accountInfo.length - 4)
	}

	// 获取提现类型描述
	const getWithdrawTypeDesc = (withdrawType) => {
		const typeMap = {
			WECHAT: '微信',
			ALIPAY: '支付宝',
			BANK_CARD: '银行卡'
		}
		return typeMap[withdrawType] || withdrawType
	}

	// 获取提现类型颜色
	const getWithdrawTypeColor = (withdrawType) => {
		const colorMap = {
			WECHAT: 'green',
			ALIPAY: 'blue',
			BANK_CARD: 'orange'
		}
		return colorMap[withdrawType] || 'default'
	}

	// 获取状态描述
	const getStatusDesc = (status) => {
		const statusMap = {
			PENDING: '待审核',
			APPROVED: '审核通过',
			REJECTED: '审核拒绝',
			PROCESSING: '处理中',
			SUCCESS: '提现成功',
			FAILED: '提现失败',
			CANCELLED: '已取消'
		}
		return statusMap[status] || status
	}

	// 获取状态颜色
	const getStatusColor = (status) => {
		const colorMap = {
			PENDING: 'processing',
			APPROVED: 'success',
			REJECTED: 'error',
			PROCESSING: 'warning',
			SUCCESS: 'success',
			FAILED: 'error',
			CANCELLED: 'default'
		}
		return colorMap[status] || 'default'
	}
</script>

<style lang="less" scoped>
	.user-info {
		line-height: 1.4;
	}

	.withdraw-info {
		line-height: 1.4;
	}

	.amount-info {
		line-height: 1.4;
	}

	.withdraw-amount {
		color: #f5222d;
		font-weight: 500;
		font-size: 14px;
	}

	.actual-amount {
		color: #52c41a;
		font-weight: 500;
		font-size: 12px;
	}

	.fee-amount {
		color: #fa8c16;
		font-weight: 500;
	}

	.account-info {
		line-height: 1.4;
	}

	.time-info {
		line-height: 1.4;
		font-size: 12px;
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
</style>
