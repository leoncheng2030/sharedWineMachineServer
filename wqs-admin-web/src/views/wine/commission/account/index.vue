<template>
	<a-card :bordered="false">
		<!-- 统计卡片 -->
		<a-row :gutter="16" style="margin-bottom: 16px;">
			<a-col :span="6">
				<a-card size="small">
					<a-statistic
						title="总账户数"
						:value="statisticsData.totalAccounts"
						:value-style="{ color: '#1890ff' }"
					/>
				</a-card>
			</a-col>
			<a-col :span="6">
				<a-card size="small">
					<a-statistic
						title="总余额"
						:value="formatMoney(statisticsData.totalBalance)"
						suffix="元"
						:value-style="{ color: '#52c41a' }"
					/>
				</a-card>
			</a-col>
			<a-col :span="6">
				<a-card size="small">
					<a-statistic
						title="累计佣金"
						:value="formatMoney(statisticsData.totalCommission)"
						suffix="元"
						:value-style="{ color: '#722ed1' }"
					/>
				</a-card>
			</a-col>
			<a-col :span="6">
				<a-card size="small">
					<a-statistic
						title="累计提现"
						:value="formatMoney(statisticsData.totalWithdraw)"
						suffix="元"
						:value-style="{ color: '#eb2f96' }"
					/>
				</a-card>
			</a-col>
		</a-row>

		<a-form ref="searchFormRef" name="searchForm" :model="searchFormState" class="wqs-search-form">
			<a-row :gutter="16">
				<a-col :lg="4" :md="8" :sm="24">
					<a-form-item label="关键词" name="searchKey">
						<a-input v-model:value="searchFormState.searchKey" placeholder="请输入用户昵称/手机号" allow-clear />
					</a-form-item>
				</a-col>
				<a-col :lg="4" :md="8" :sm="24">
					<a-form-item label="账户状态" name="status">
						<a-select v-model:value="searchFormState.status" placeholder="请选择账户状态" allow-clear>
							<a-select-option value="NORMAL">正常</a-select-option>
							<a-select-option value="FROZEN">冻结</a-select-option>
							<a-select-option value="DISABLED">禁用</a-select-option>
						</a-select>
					</a-form-item>
				</a-col>
				<a-col :lg="4" :md="8" :sm="24">
					<a-form-item label="余额范围" name="balanceRange">
						<a-select v-model:value="searchFormState.balanceRange" placeholder="请选择余额范围" allow-clear>
							<a-select-option value="0-100">0-100元</a-select-option>
							<a-select-option value="100-500">100-500元</a-select-option>
							<a-select-option value="500-1000">500-1000元</a-select-option>
							<a-select-option value="1000+">1000元以上</a-select-option>
						</a-select>
					</a-form-item>
				</a-col>
				<a-col :lg="5" :md="8" :sm="24">
					<a-form-item label="创建时间" name="createTimeRange">
						<a-range-picker
							v-model:value="searchFormState.createTimeRange"
							:placeholder="['开始时间', '结束时间']"
							format="YYYY-MM-DD"
							valueFormat="YYYY-MM-DD"
						/>
					</a-form-item>
				</a-col>
				<a-col :lg="7" :md="24" :sm="24">
					<a-form-item class="wqs-search-actions">
						<a-space>
							<a-button type="primary" @click="loadData">
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

		<s-table
			ref="table"
			:columns="columns"
			:data="loadData"
			:alert="options.alert.show"
			:row-key="(record) => record.id"
			:tool-config="toolConfig"
			:row-selection="options.rowSelection"
		>
			<template #operator class="table-operator">
				<a-space>
					<a-button type="primary" @click="exportData">
						<template #icon><ExportOutlined /></template>
						导出
					</a-button>
				</a-space>
			</template>

			<template #bodyCell="{ column, record }">
				<!-- 用户信息 -->
				<template v-if="column.dataIndex === 'userInfo'">
					<div class="user-info">
						<div class="user-avatar">
							<a-avatar :size="40" style="background-color: #1890ff;">
								{{ (record.userNickname || '未知')[0] }}
							</a-avatar>
						</div>
						<div class="user-details">
							<div class="user-name">
								<a-tag color="blue">{{ record.userNickname || '未知用户' }}</a-tag>
							</div>
							<div class="user-id">
								<span class="user-id-text">ID: {{ record.userId || '-' }}</span>
							</div>
						</div>
					</div>
				</template>

				<!-- 账户余额 -->
				<template v-else-if="column.dataIndex === 'balanceInfo'">
					<div class="balance-info">
						<div class="balance-item">
							<span class="balance-label">总余额:</span>
							<span class="balance-amount">¥{{ formatMoney(record.totalBalance) }}</span>
						</div>
						<div class="balance-progress">
							<a-progress 
								:percent="getBalancePercent(record)" 
								:stroke-color="{
									'0%': '#108ee9',
									'100%': '#87d068',
								}"
								size="small"
								:show-info="false"
							/>
						</div>
						<div class="balance-breakdown">
							<span class="available">可用: ¥{{ formatMoney(record.availableBalance) }}</span>
							<span class="frozen" v-if="record.frozenBalance > 0">冻结: ¥{{ formatMoney(record.frozenBalance) }}</span>
						</div>
					</div>
				</template>

				<!-- 佣金信息 -->
				<template v-else-if="column.dataIndex === 'commissionInfo'">
					<div class="commission-info">
						<div class="commission-item">
							<span class="commission-label">累计佣金:</span>
							<span class="commission-amount">¥{{ formatMoney(record.totalCommission) }}</span>
						</div>
						<div class="commission-item">
							<span class="withdraw-label">累计提现:</span>
							<span class="withdraw-amount">¥{{ formatMoney(record.totalWithdraw) }}</span>
						</div>
						<div class="commission-ratio" v-if="record.totalCommission > 0">
							<a-tag color="purple">
								提现率: {{ getWithdrawRatio(record) }}%
							</a-tag>
						</div>
					</div>
				</template>

				<!-- 账户状态 -->
				<template v-else-if="column.dataIndex === 'status'">
					<div class="status-info">
						<a-tag :color="getStatusColor(record.status)" style="margin-bottom: 4px;">
							{{ getStatusText(record.status) }}
						</a-tag>
						<div class="status-time" v-if="record.lastCommissionTime">
							<small>最近佣金: {{ formatDate(record.lastCommissionTime) }}</small>
						</div>
					</div>
				</template>

				<!-- 操作列 -->
				<template v-else-if="column.dataIndex === 'action'">
					<a-space>
						<a @click="detail(record)">详情</a>
						<a @click="adjustBalance(record)">调整余额</a>
						<a-dropdown>
							<template #overlay>
								<a-menu>
									<a-menu-item v-if="record.status === 'NORMAL'" @click="freezeAccount(record)"> 冻结账户 </a-menu-item>
									<a-menu-item v-if="record.status === 'FROZEN'" @click="unfreezeAccount(record)">
										解冻账户
									</a-menu-item>
									<a-menu-item v-if="record.status === 'NORMAL'" @click="disableAccount(record)">
										禁用账户
									</a-menu-item>
									<a-menu-item v-if="record.status === 'DISABLED'" @click="enableAccount(record)">
										启用账户
									</a-menu-item>
								</a-menu>
							</template>
							<a class="ant-dropdown-link"> 更多 <DownOutlined /> </a>
						</a-dropdown>
					</a-space>
				</template>
			</template>
		</s-table>

		<!-- 账户详情抽屉 -->
		<a-drawer v-model:open="detailVisible" title="账户详情" :width="600" :body-style="{ paddingBottom: '80px' }">
			<a-descriptions v-if="detailData" :column="2" bordered>
				<a-descriptions-item label="用户信息" :span="2">
					<div class="user-info-detail">
						<a-avatar :size="40" style="background-color: #1890ff; margin-right: 12px;">
							{{ (detailData.userNickname || '未知')[0] }}
						</a-avatar>
						<div>
							<div><a-tag color="blue">{{ detailData.userNickname || '未知用户' }}</a-tag></div>
							<div style="color: #666; font-size: 12px;">ID: {{ detailData.userId }}</div>
						</div>
					</div>
				</a-descriptions-item>
				<a-descriptions-item label="总余额">
					<span class="balance-amount">¥{{ formatMoney(detailData.totalBalance) }}</span>
				</a-descriptions-item>
				<a-descriptions-item label="可用余额">
					<span class="available-balance">¥{{ formatMoney(detailData.availableBalance) }}</span>
				</a-descriptions-item>
				<a-descriptions-item label="冻结余额">
					<span class="frozen-balance">¥{{ formatMoney(detailData.frozenBalance) }}</span>
				</a-descriptions-item>
				<a-descriptions-item label="累计佣金">
					<span class="commission-amount">¥{{ formatMoney(detailData.totalCommission) }}</span>
				</a-descriptions-item>
				<a-descriptions-item label="累计提现">
					<span class="withdraw-amount">¥{{ formatMoney(detailData.totalWithdraw) }}</span>
				</a-descriptions-item>
				<a-descriptions-item label="提现率">
					<a-tag color="purple">{{ getWithdrawRatio(detailData) }}%</a-tag>
				</a-descriptions-item>
				<a-descriptions-item label="账户状态">
					<a-tag :color="getStatusColor(detailData.status)">
						{{ getStatusText(detailData.status) }}
					</a-tag>
				</a-descriptions-item>
				<a-descriptions-item label="最后佣金时间">
					{{ formatDateTime(detailData.lastCommissionTime) }}
				</a-descriptions-item>
				<a-descriptions-item label="最后提现时间">
					{{ formatDateTime(detailData.lastWithdrawTime) }}
				</a-descriptions-item>
				<a-descriptions-item label="创建时间">
					{{ formatDateTime(detailData.createTime) }}
				</a-descriptions-item>
				<a-descriptions-item label="更新时间">
					{{ formatDateTime(detailData.updateTime) }}
				</a-descriptions-item>
				<a-descriptions-item label="备注" :span="2">
					{{ detailData.remark || '无' }}
				</a-descriptions-item>
			</a-descriptions>
		</a-drawer>

		<!-- 余额调整模态框 -->
		<a-modal
			v-model:open="adjustVisible"
			title="调整账户余额"
			:width="500"
			@ok="handleAdjustSubmit"
			@cancel="handleAdjustCancel"
		>
			<a-form ref="adjustFormRef" :model="adjustFormState" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
				<a-form-item label="用户信息">
					<div class="user-info">
						<a-avatar :size="32" style="background-color: #1890ff; margin-right: 8px;">
							{{ (adjustFormState.userNickname || '未知')[0] }}
						</a-avatar>
						<a-tag color="blue">{{ adjustFormState.userNickname }}</a-tag>
					</div>
				</a-form-item>
				<a-form-item label="调整类型" name="adjustType" :rules="[{ required: true, message: '请选择调整类型' }]">
					<a-select v-model:value="adjustFormState.adjustType" placeholder="请选择调整类型">
						<a-select-option value="ADD">增加余额</a-select-option>
						<a-select-option value="SUBTRACT">减少余额</a-select-option>
						<a-select-option value="FREEZE">冻结余额</a-select-option>
						<a-select-option value="UNFREEZE">解冻余额</a-select-option>
					</a-select>
				</a-form-item>
				<a-form-item
					label="调整金额"
					name="adjustAmount"
					:rules="[
						{ required: true, message: '请输入调整金额' },
						{ pattern: /^(0|[1-9]\d*)(\.\d{1,2})?$/, message: '请输入正确的金额格式' }
					]"
				>
					<a-input-number
						v-model:value="adjustFormState.adjustAmount"
						:min="0.01"
						:precision="2"
						placeholder="请输入调整金额"
						style="width: 100%"
					/>
				</a-form-item>
				<a-form-item label="调整原因" name="adjustReason" :rules="[{ required: true, message: '请输入调整原因' }]">
					<a-input v-model:value="adjustFormState.adjustReason" placeholder="请输入调整原因" />
				</a-form-item>
				<a-form-item label="备注" name="remark">
					<a-textarea v-model:value="adjustFormState.remark" placeholder="请输入备注" :rows="3" />
				</a-form-item>
			</a-form>
		</a-modal>
	</a-card>
</template>

<script setup name="wineUserAccount">
	import { message } from 'ant-design-vue'
	import { ClearOutlined, DownOutlined, ExportOutlined, SearchOutlined } from '@ant-design/icons-vue'
	import wineUserAccountApi from '@/api/commission/wineUserAccountApi'

	const searchFormRef = ref()
	const adjustFormRef = ref()
	const table = ref()

	// 搜索表单状态
	const searchFormState = reactive({
		searchKey: '',
		status: undefined,
		balanceRange: undefined,
		createTimeRange: undefined
	})

	// 统计数据
	const statisticsData = reactive({
		totalAccounts: 0,
		totalBalance: 0,
		totalCommission: 0,
		totalWithdraw: 0
	})

	// 详情相关状态
	const detailVisible = ref(false)
	const detailData = ref(null)

	// 余额调整相关状态
	const adjustVisible = ref(false)
	const adjustFormState = reactive({
		id: '',
		userNickname: '',
		adjustType: undefined,
		adjustAmount: undefined,
		adjustReason: '',
		remark: ''
	})

	// 表格配置
	const columns = [
		{
			title: '用户信息',
			dataIndex: 'userInfo',
			width: 180,
			fixed: 'left'
		},
		{
			title: '账户余额',
			dataIndex: 'balanceInfo',
			width: 220
		},
		{
			title: '佣金信息',
			dataIndex: 'commissionInfo',
			width: 200
		},
		{
			title: '状态信息',
			dataIndex: 'status',
			width: 120
		},
		{
			title: '创建时间',
			dataIndex: 'createTime',
			width: 150,
			sorter: true
		},
		{
			title: '操作',
			dataIndex: 'action',
			width: 180,
			fixed: 'right'
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
		if (searchParam.createTimeRange && searchParam.createTimeRange.length === 2) {
			searchParam.createTimeStart = searchParam.createTimeRange[0]
			searchParam.createTimeEnd = searchParam.createTimeRange[1]
		}
		delete searchParam.createTimeRange

		// 处理余额范围
		if (searchParam.balanceRange) {
			const ranges = {
				'0-100': { min: 0, max: 100 },
				'100-500': { min: 100, max: 500 },
				'500-1000': { min: 500, max: 1000 },
				'1000+': { min: 1000, max: null }
			}
			const range = ranges[searchParam.balanceRange]
			if (range) {
				searchParam.balanceMin = range.min
				searchParam.balanceMax = range.max
			}
		}
		delete searchParam.balanceRange

		return wineUserAccountApi.wineUserAccountPage(Object.assign(parameter, searchParam)).then((data) => {
			// 计算统计数据
			updateStatistics(data.records || [])
			return data
		})
	}

	// 更新统计数据
	const updateStatistics = (records) => {
		statisticsData.totalAccounts = records.length
		statisticsData.totalBalance = records.reduce((sum, item) => sum + (item.totalBalance || 0), 0)
		statisticsData.totalCommission = records.reduce((sum, item) => sum + (item.totalCommission || 0), 0)
		statisticsData.totalWithdraw = records.reduce((sum, item) => sum + (item.totalWithdraw || 0), 0)
	}

	// 计算余额百分比
	const getBalancePercent = (record) => {
		if (!record.totalBalance || record.totalBalance <= 0) return 0
		const maxBalance = Math.max(...(table.value?.localDataSource || []).map(item => item.totalBalance || 0))
		if (maxBalance <= 0) return 0
		return Math.round((record.totalBalance / maxBalance) * 100)
	}

	// 计算提现率
	const getWithdrawRatio = (record) => {
		if (!record.totalCommission || record.totalCommission <= 0) return 0
		return Math.round((record.totalWithdraw / record.totalCommission) * 100)
	}

	// 重置搜索
	const reset = () => {
		searchFormRef.value.resetFields()
		loadData()
	}

	// 查看详情
	const detail = (record) => {
		wineUserAccountApi.wineUserAccountDetail({ id: record.id }).then((data) => {
			detailData.value = data
			detailVisible.value = true
		})
	}

	// 调整余额
	const adjustBalance = (record) => {
		adjustFormState.id = record.id
		adjustFormState.userNickname = record.userNickname
		adjustFormState.adjustType = undefined
		adjustFormState.adjustAmount = undefined
		adjustFormState.adjustReason = ''
		adjustFormState.remark = ''
		adjustVisible.value = true
	}

	// 提交余额调整
	const handleAdjustSubmit = () => {
		adjustFormRef.value.validate().then(() => {
			wineUserAccountApi.wineUserAccountAdjust(adjustFormState).then(() => {
				message.success('余额调整成功')
				adjustVisible.value = false
				table.value.refresh()
			})
		})
	}

	// 取消余额调整
	const handleAdjustCancel = () => {
		adjustFormRef.value.resetFields()
		adjustVisible.value = false
	}

	// 冻结账户
	const freezeAccount = (record) => {
		wineUserAccountApi.wineUserAccountFreeze({ id: record.id }).then(() => {
			message.success('账户冻结成功')
			table.value.refresh()
		})
	}

	// 解冻账户
	const unfreezeAccount = (record) => {
		wineUserAccountApi.wineUserAccountUnfreeze({ id: record.id }).then(() => {
			message.success('账户解冻成功')
			table.value.refresh()
		})
	}

	// 禁用账户
	const disableAccount = (record) => {
		wineUserAccountApi.wineUserAccountDisable({ id: record.id }).then(() => {
			message.success('账户禁用成功')
			table.value.refresh()
		})
	}

	// 启用账户
	const enableAccount = (record) => {
		wineUserAccountApi.wineUserAccountEnable({ id: record.id }).then(() => {
			message.success('账户启用成功')
			table.value.refresh()
		})
	}

	// 导出数据
	const exportData = () => {
		const searchParam = { ...searchFormState }

		// 处理时间范围
		if (searchParam.createTimeRange && searchParam.createTimeRange.length === 2) {
			searchParam.createTimeStart = searchParam.createTimeRange[0]
			searchParam.createTimeEnd = searchParam.createTimeRange[1]
		}
		delete searchParam.createTimeRange

		// 处理余额范围
		if (searchParam.balanceRange) {
			const ranges = {
				'0-100': { min: 0, max: 100 },
				'100-500': { min: 100, max: 500 },
				'500-1000': { min: 500, max: 1000 },
				'1000+': { min: 1000, max: null }
			}
			const range = ranges[searchParam.balanceRange]
			if (range) {
				searchParam.balanceMin = range.min
				searchParam.balanceMax = range.max
			}
		}
		delete searchParam.balanceRange

		wineUserAccountApi.wineUserAccountExport(searchParam).then(() => {
			message.success('导出成功')
		})
	}

	// 格式化金额
	const formatMoney = (amount) => {
		if (amount === null || amount === undefined) return '0.00'
		return Number(amount).toFixed(2)
	}

	// 格式化日期
	const formatDate = (dateTime) => {
		if (!dateTime) return '-'
		return new Date(dateTime).toLocaleDateString('zh-CN')
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

	// 获取状态颜色
	const getStatusColor = (status) => {
		const colorMap = {
			NORMAL: 'green',
			FROZEN: 'orange',
			DISABLED: 'red'
		}
		return colorMap[status] || 'default'
	}

	// 获取状态文本
	const getStatusText = (status) => {
		const textMap = {
			NORMAL: '正常',
			FROZEN: '冻结',
			DISABLED: '禁用'
		}
		return textMap[status] || status
	}
</script>

<style lang="less" scoped>
	.balance-amount {
		color: #1890ff;
		font-weight: 500;
	}

	.available-balance {
		color: #52c41a;
		font-weight: 500;
	}

	.frozen-balance {
		color: #fa8c16;
		font-weight: 500;
	}

	.commission-amount {
		color: #722ed1;
		font-weight: 500;
	}

	.withdraw-amount {
		color: #eb2f96;
		font-weight: 500;
	}

	.wqs-search-form {
		.wqs-search-actions {
			text-align: left;
		}
	}

	.wqs-table-actions {
		margin-bottom: 16px;
	}

	// 用户信息样式
	.user-info {
		display: flex;
		align-items: center;
		gap: 12px;

		.user-details {
			flex: 1;
			min-width: 0;

			.user-name {
				margin-bottom: 4px;
			}

			.user-id-text {
				color: #666;
				font-size: 12px;
			}
		}
	}

	.user-info-detail {
		display: flex;
		align-items: center;
	}

	// 余额信息样式
	.balance-info {
		.balance-item {
			margin-bottom: 4px;

			.balance-label {
				color: #666;
				font-size: 12px;
				margin-right: 4px;
			}

			.balance-amount {
				color: #1890ff;
				font-weight: 500;
			}
		}

		.balance-progress {
			margin: 4px 0;
		}

		.balance-breakdown {
			display: flex;
			gap: 8px;
			font-size: 12px;

			.available {
				color: #52c41a;
			}

			.frozen {
				color: #fa8c16;
			}
		}
	}

	// 佣金信息样式
	.commission-info {
		.commission-item {
			margin-bottom: 4px;
			font-size: 12px;

			.commission-label,
			.withdraw-label {
				color: #666;
				margin-right: 4px;
			}

			.commission-amount {
				color: #722ed1;
				font-weight: 500;
			}

			.withdraw-amount {
				color: #eb2f96;
				font-weight: 500;
			}
		}

		.commission-ratio {
			margin-top: 4px;
		}
	}

	// 状态信息样式
	.status-info {
		.status-time {
			margin-top: 4px;
			color: #999;
		}
	}
</style>
