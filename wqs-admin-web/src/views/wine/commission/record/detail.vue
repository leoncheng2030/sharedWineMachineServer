<template>
	<a-drawer title="佣金记录详情" :width="600" :open="visible" :body-style="{ paddingBottom: '80px' }" @close="onClose">
		<div v-if="loading" style="text-align: center; padding: 50px 0">
			<a-spin size="large" />
		</div>

		<div v-else-if="record">
			<!-- 基本信息 -->
			<a-card title="基本信息" size="small" style="margin-bottom: 16px">
				<a-descriptions :column="1" size="small">
					<a-descriptions-item label="记录ID">
						<a-typography-text copyable>{{ record.id }}</a-typography-text>
					</a-descriptions-item>
					<a-descriptions-item label="订单号">
						<a-typography-text copyable>{{ record.orderNo }}</a-typography-text>
					</a-descriptions-item>
					<a-descriptions-item label="订单ID">
						<a-typography-text copyable>{{ record.orderId }}</a-typography-text>
					</a-descriptions-item>
					<a-descriptions-item label="佣金状态">
						<a-tag :color="getStatusColor(record.status)">
							{{ getStatusDesc(record.status) }}
						</a-tag>
					</a-descriptions-item>
				</a-descriptions>
			</a-card>

			<!-- 用户信息 -->
			<a-card title="用户信息" size="small" style="margin-bottom: 16px">
				<a-descriptions :column="1" size="small">
					<a-descriptions-item label="受益用户">
						{{ record.userNickname || '未知用户' }}
					</a-descriptions-item>
					<a-descriptions-item label="用户ID">
						<a-typography-text copyable>{{ record.userId }}</a-typography-text>
					</a-descriptions-item>
				</a-descriptions>
			</a-card>

			<!-- 设备信息 -->
			<a-card title="设备信息" size="small" style="margin-bottom: 16px">
				<a-descriptions :column="1" size="small">
					<a-descriptions-item label="设备编码">
						{{ record.deviceCode || '未知设备' }}
					</a-descriptions-item>
					<a-descriptions-item label="设备ID">
						<a-typography-text copyable>{{ record.deviceId }}</a-typography-text>
					</a-descriptions-item>
				</a-descriptions>
			</a-card>

			<!-- 酒品信息 -->
			<a-card title="酒品信息" size="small" style="margin-bottom: 16px">
				<a-descriptions :column="1" size="small">
					<a-descriptions-item label="酒品名称">
						{{ record.wineName || '未知酒品' }}
					</a-descriptions-item>
					<a-descriptions-item label="酒品ID">
						<a-typography-text copyable>{{ record.wineId }}</a-typography-text>
					</a-descriptions-item>
				</a-descriptions>
			</a-card>

			<!-- 佣金信息 -->
			<a-card title="佣金信息" size="small" style="margin-bottom: 16px">
				<a-descriptions :column="1" size="small">
					<a-descriptions-item label="佣金类型">
						<a-tag :color="getCommissionTypeColor(record.commissionType)">
							{{ getCommissionTypeDesc(record.commissionType) }}
						</a-tag>
					</a-descriptions-item>
					<a-descriptions-item label="订单金额">
						<span class="amount-text order-amount">¥{{ record.orderAmount }}</span>
					</a-descriptions-item>
					<a-descriptions-item label="佣金比例">
						<span class="rate-text">{{ record.commissionRate }}%</span>
					</a-descriptions-item>
					<a-descriptions-item label="佣金金额">
						<span class="amount-text commission-amount">¥{{ record.commissionAmount }}</span>
					</a-descriptions-item>
				</a-descriptions>
			</a-card>

			<!-- 时间信息 -->
			<a-card title="时间信息" size="small" style="margin-bottom: 16px">
				<a-descriptions :column="1" size="small">
					<a-descriptions-item label="创建时间">
						{{ formatDateTime(record.createTime) }}
					</a-descriptions-item>
					<a-descriptions-item label="计算时间">
						{{ record.calculateTime ? formatDateTime(record.calculateTime) : '未计算' }}
					</a-descriptions-item>
					<a-descriptions-item label="发放时间">
						{{ record.settleTime ? formatDateTime(record.settleTime) : '未发放' }}
					</a-descriptions-item>
					<a-descriptions-item label="更新时间">
						{{ formatDateTime(record.updateTime) }}
					</a-descriptions-item>
				</a-descriptions>
			</a-card>

			<!-- 备注信息 -->
			<a-card title="备注信息" size="small" style="margin-bottom: 16px" v-if="record.remark">
				<p>{{ record.remark }}</p>
			</a-card>

			<!-- 扩展信息 -->
			<a-card title="扩展信息" size="small" style="margin-bottom: 16px" v-if="record.extJson">
				<pre class="ext-json">{{ formatExtJson(record.extJson) }}</pre>
			</a-card>

			<!-- 操作按钮 -->
			<div class="action-buttons">
				<a-space>
					<a-button
						type="primary"
						@click="handleAction('settle')"
						v-if="canSettle(record) && hasPerm('commissionRecordSettle')"
						:loading="actionLoading"
					>
						<template #icon><CheckCircleOutlined /></template>
						发放佣金
					</a-button>

					<a-button
						@click="handleAction('freeze')"
						v-if="canFreeze(record) && hasPerm('commissionRecordFreeze')"
						:loading="actionLoading"
					>
						<template #icon><PauseCircleOutlined /></template>
						冻结佣金
					</a-button>

					<a-button
						danger
						@click="handleAction('cancel')"
						v-if="canCancel(record) && hasPerm('commissionRecordCancel')"
						:loading="actionLoading"
					>
						<template #icon><CloseCircleOutlined /></template>
						取消佣金
					</a-button>

					<a-button
						@click="handleAction('recalculate')"
						v-if="canRecalculate(record) && hasPerm('commissionRecordRecalculate')"
						:loading="actionLoading"
					>
						<template #icon><ReloadOutlined /></template>
						重新计算
					</a-button>
				</a-space>
			</div>
		</div>

		<div v-else style="text-align: center; padding: 50px 0">
			<a-empty description="未找到佣金记录" />
		</div>
	</a-drawer>
</template>

<script setup>
	import { message, Modal } from 'ant-design-vue'
	import { CheckCircleOutlined, CloseCircleOutlined, PauseCircleOutlined, ReloadOutlined } from '@ant-design/icons-vue'
	import tool from '@/utils/tool'
	import { hasPerm } from '@/utils/permission'
	import wineCommissionRecordApi from '@/api/wine/wineCommissionRecordApi'

	// 组件状态
	const visible = ref(false)
	const loading = ref(false)
	const actionLoading = ref(false)
	const record = ref(null)

	// 打开抽屉
	const open = (id) => {
		visible.value = true
		loadDetail(id)
	}

	// 关闭抽屉
	const onClose = () => {
		visible.value = false
		record.value = null
	}

	// 加载详情
	const loadDetail = (id) => {
		loading.value = true
		wineCommissionRecordApi
			.detail({ id })
			.then((res) => {
				record.value = res
			})
			.catch(() => {
				message.error('加载佣金记录详情失败')
			})
			.finally(() => {
				loading.value = false
			})
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

	// 格式化扩展JSON
	const formatExtJson = (extJson) => {
		try {
			return JSON.stringify(JSON.parse(extJson), null, 2)
		} catch (e) {
			return extJson
		}
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

	// 处理操作
	const handleAction = (action) => {
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
				actionLoading.value = true
				actionConfig
					.api({ id: record.value.id })
					.then(() => {
						message.success(actionConfig.message + '成功')
						// 重新加载详情
						loadDetail(record.value.id)
					})
					.finally(() => {
						actionLoading.value = false
					})
			}
		})
	}

	// 暴露方法
	defineExpose({
		open
	})
</script>

<style scoped>
	.amount-text {
		font-weight: 500;
		font-size: 16px;
	}

	.order-amount {
		color: #52c41a;
	}

	.commission-amount {
		color: #f5222d;
	}

	.rate-text {
		color: #1890ff;
		font-weight: 500;
		font-size: 16px;
	}

	.ext-json {
		background: #f5f5f5;
		padding: 12px;
		border-radius: 4px;
		font-size: 12px;
		max-height: 200px;
		overflow-y: auto;
	}

	.action-buttons {
		position: sticky;
		bottom: 0;
		background: white;
		padding: 16px 0;
		border-top: 1px solid #f0f0f0;
		margin-top: 16px;
	}
</style>
