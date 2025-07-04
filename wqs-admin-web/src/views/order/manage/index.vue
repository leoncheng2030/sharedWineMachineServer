<template>
	<div class="main-content-container">
		<a-card :bordered="false" class="main-content">
			<a-row>
				<a-col :span="24">
					<a-form ref="searchFormRef" name="searchForm" layout="horizontal" :model="searchFormState">
						<a-row :gutter="16">
							<a-col :lg="5" :md="8" :sm="12" :xs="24">
								<a-form-item label="订单号" name="orderNo">
									<a-input v-model:value="searchFormState.orderNo" placeholder="请输入订单号" />
								</a-form-item>
							</a-col>
							<a-col :lg="5" :md="8" :sm="12" :xs="24">
								<a-form-item label="订单状态" name="status">
									<a-select v-model:value="searchFormState.status" placeholder="请选择订单状态" allowClear>
										<a-select-option value="PENDING">待支付</a-select-option>
										<a-select-option value="PAID">已支付</a-select-option>
										<a-select-option value="DISPENSING">出酒中</a-select-option>
										<a-select-option value="COMPLETED">已完成</a-select-option>
										<a-select-option value="CANCELLED">已取消</a-select-option>
									</a-select>
								</a-form-item>
							</a-col>
							<a-col :lg="5" :md="8" :sm="12" :xs="24">
								<a-form-item label="用户ID" name="userId">
									<a-input v-model:value="searchFormState.userId" placeholder="请输入用户ID" />
								</a-form-item>
							</a-col>
							<a-col :lg="9" :md="24" :sm="24" :xs="24">
								<a-form-item>
									<a-button type="primary" @click="table.refresh()">
										<template #icon><SearchOutlined /></template>
										查询
									</a-button>
									<a-button style="margin: 0 8px" @click="reset">
										<template #icon><ClearOutlined /></template>
										重置
									</a-button>
								</a-form-item>
							</a-col>
						</a-row>
					</a-form>
				</a-col>
			</a-row>
			<a-row :gutter="16" style="margin-top: 16px">
				<a-col :span="24">
					<s-table
						ref="table"
						:columns="columns"
						:data="loadData"
						:tool-config="toolConfig"
						:row-key="(record) => record.id"
					>
						<template #operator class="table-operator">
							<a-space>
								<a-button v-if="hasPerm(['orderManageExport'])" @click="exportData">
									<template #icon><export-outlined /></template>
									导出订单
								</a-button>
								<a-button v-if="hasPerm(['orderManageStatistics'])" @click="viewStatistics">
									<template #icon><bar-chart-outlined /></template>
									统计分析
								</a-button>
							</a-space>
						</template>
						<template #bodyCell="{ column, record }">
							<template v-if="column.dataIndex === 'status'">
								<a-tag :color="getStatusColor(record.status)">
									{{ getStatusText(record.status) }}
								</a-tag>
							</template>
							<template v-if="column.dataIndex === 'totalAmount'"> ¥{{ record.totalAmount }} </template>
							<template v-if="column.dataIndex === 'action'">
								<a-space>
									<a v-if="hasPerm(['orderManageDetail'])" @click="formRef.onOpen(record.id, 'DETAIL')">详情</a>
									<a-divider type="vertical" v-if="hasPerm(['orderManageDetail', 'orderManageUpdateStatus'], 'OR')" />
									<a
										v-if="hasPerm(['orderManageUpdateStatus']) && record.status === 'PENDING'"
										@click="updateOrderStatus(record, 'CANCELLED')"
									>
										<span style="color: #ff4d4f">取消订单</span>
									</a>
									<a-divider type="vertical" v-if="hasPerm(['orderManageDetail', 'orderManageUpdateStatus'], 'OR')" />
									<a @click="viewOrderTrace(record)">
										<span style="color: #722ed1">订单轨迹</span>
									</a>
								</a-space>
							</template>
						</template>
					</s-table>
				</a-col>
			</a-row>
		</a-card>
		<Form ref="formRef" @successful="table.refresh()" />
	</div>
</template>

<script setup name="orderManage">
	import { message } from 'ant-design-vue'
	import Form from './form.vue'
	import orderApi from '@/api/order/orderApi'

	// 权限检查
	const hasPerm = (perms, type = 'AND') => {
		// 简化版权限检查，实际项目中应该从store获取用户权限
		return true
	}
	const toolConfig = {
		refresh: true,
		height: true,
		columnSetting: true,
		striped: false
	}
	// 表格
	const table = ref()
	const formRef = ref()
	const searchFormRef = ref()
	const searchFormState = reactive({
		orderNo: undefined,
		status: undefined,
		userId: undefined,
		deviceId: undefined
	})
	// 移除选择功能，订单管理不需要批量操作
	// 表格列
	const columns = [
		{
			title: '订单号',
			dataIndex: 'orderNo',
			width: 100,
			resizable: true
		},
		{
			title: '用户ID',
			dataIndex: 'userId',
			width: 100,
			resizable: true
		},
		{
			title: '设备ID',
			dataIndex: 'deviceId',
			width: 120,
			resizable: true
		},
		{
			title: '酒品名称',
			dataIndex: 'wineName',
			width: 150,
			resizable: true
		},
		{
			title: '数量',
			dataIndex: 'quantity',
			width: 80,
			resizable: true
		},
		{
			title: '总金额',
			dataIndex: 'totalAmount',
			width: 100,
			resizable: true
		},
		{
			title: '订单状态',
			dataIndex: 'status',
			width: 100,
			resizable: true
		},
		{
			title: '创建时间',
			dataIndex: 'createTime',
			width: 150,
			resizable: true
		},
		{
			title: '操作',
			dataIndex: 'action',
			width: 150,
			resizable: true
		}
	]
	// 表格数据
	const loadData = (parameter) => {
		return orderApi.orderManagePage(Object.assign(parameter, searchFormState)).then((data) => {
			return data
		})
	}
	// 重置
	const reset = () => {
		searchFormRef.value.resetFields()
		table.value.refresh()
	}
	// 更新订单状态
	const updateOrderStatus = (record, newStatus) => {
		const statusText = {
			CANCELLED: '取消订单'
		}

		orderApi
			.orderManageUpdateStatus({
				id: record.id,
				status: newStatus
			})
			.then(() => {
				table.value.refresh()
				message.success(`${statusText[newStatus]}成功`)
			})
			.catch(() => {
				message.error(`${statusText[newStatus]}失败`)
			})
	}

	// 查看订单轨迹
	const viewOrderTrace = (record) => {
		// TODO: 实现订单轨迹查看功能
		message.info('订单轨迹功能待实现')
	}

	// 导出订单数据
	const exportData = () => {
		const param = Object.assign({}, searchFormState)
		orderApi
			.orderManageExport(param)
			.then((res) => {
				// 创建下载链接
				const url = window.URL.createObjectURL(new Blob([res]))
				const link = document.createElement('a')
				link.href = url
				link.setAttribute('download', `订单数据_${new Date().toISOString().slice(0, 10)}.xlsx`)
				document.body.appendChild(link)
				link.click()
				document.body.removeChild(link)
				window.URL.revokeObjectURL(url)
				message.success('导出成功')
			})
			.catch(() => {
				message.error('导出失败')
			})
	}

	// 查看统计分析
	const viewStatistics = () => {
		// TODO: 实现统计分析功能
		message.info('统计分析功能待实现')
	}
	// 获取状态颜色
	const getStatusColor = (status) => {
		const colors = {
			PENDING: 'orange',
			PAID: 'blue',
			DISPENSING: 'processing',
			COMPLETED: 'success',
			CANCELLED: 'default'
		}
		return colors[status] || 'default'
	}
	// 获取状态文本
	const getStatusText = (status) => {
		const texts = {
			PENDING: '待支付',
			PAID: '已支付',
			DISPENSING: '出酒中',
			COMPLETED: '已完成',
			CANCELLED: '已取消'
		}
		return texts[status] || status
	}
</script>
