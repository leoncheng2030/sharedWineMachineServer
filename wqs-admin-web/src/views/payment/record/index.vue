<template>
	<a-card :bordered="false" class="xn-mb10">
		<a-form ref="searchFormRef" name="advanced_search" class="ant-advanced-search-form" :model="searchFormState">
			<a-row :gutter="24">
				<a-col :span="6">
					<a-form-item name="searchKey" label="搜索关键词">
						<a-input v-model:value="searchFormState.searchKey" placeholder="请输入支付单号、订单号或用户ID" />
					</a-form-item>
				</a-col>
				<a-col :span="6">
					<a-form-item name="payType" label="支付方式">
						<a-select
							v-model:value="searchFormState.payType"
							placeholder="请选择支付方式"
							:getPopupContainer="(trigger) => trigger.parentNode"
							allow-clear
						>
							<a-select-option value="WECHAT_MINI">微信小程序支付</a-select-option>
							<a-select-option value="WECHAT_H5">微信H5支付</a-select-option>
							<a-select-option value="WECHAT_APP">微信APP支付</a-select-option>
							<a-select-option value="WECHAT_NATIVE">微信扫码支付</a-select-option>
							<a-select-option value="ALIPAY">支付宝支付</a-select-option>
							<a-select-option value="ALIPAY_H5">支付宝H5支付</a-select-option>
							<a-select-option value="ALIPAY_APP">支付宝APP支付</a-select-option>
							<a-select-option value="BALANCE">余额支付</a-select-option>
						</a-select>
					</a-form-item>
				</a-col>
				<a-col :span="6">
					<a-form-item name="payStatus" label="支付状态">
						<a-select
							v-model:value="searchFormState.payStatus"
							placeholder="请选择支付状态"
							:getPopupContainer="(trigger) => trigger.parentNode"
							allow-clear
						>
							<a-select-option value="PENDING">待支付</a-select-option>
							<a-select-option value="SUCCESS">支付成功</a-select-option>
							<a-select-option value="FAILED">支付失败</a-select-option>
							<a-select-option value="CANCELLED">已取消</a-select-option>
							<a-select-option value="REFUNDED">已退款</a-select-option>
							<a-select-option value="PARTIAL_REFUNDED">部分退款</a-select-option>
							<a-select-option value="EXPIRED">已过期</a-select-option>
						</a-select>
					</a-form-item>
				</a-col>
				<a-col :span="6">
					<a-form-item name="createTime" label="创建时间">
						<a-range-picker
							v-model:value="searchFormState.createTime"
							:placeholder="['开始时间', '结束时间']"
							format="YYYY-MM-DD"
							value-format="YYYY-MM-DD"
						/>
					</a-form-item>
				</a-col>
			</a-row>
			<a-row :gutter="24">
				<a-col :span="6">
					<a-form-item name="transactionId" label="交易流水号">
						<a-input v-model:value="searchFormState.transactionId" placeholder="请输入第三方交易流水号" />
					</a-form-item>
				</a-col>
				<a-col :span="6">
					<a-space>
						<a-button type="primary" @click="refresh">
							<template #icon>
								<SearchOutlined />
							</template>
							查询
						</a-button>
						<a-button class="snowy-button-left" @click="reset">
							<template #icon><redo-outlined /></template>
							重置
						</a-button>
					</a-space>
				</a-col>
			</a-row>
		</a-form>
	</a-card>
	<a-card :bordered="false">
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
						<template #icon>
							<ExportOutlined />
						</template>
						导出
					</a-button>
				</a-space>
			</template>
			<template #bodyCell="{ column, record }">
				<template v-if="column.dataIndex === 'payType'">
					<a-tag :color="getPayTypeColor(record.payType)">{{ getPayTypeName(record.payType) }}</a-tag>
				</template>
				<template v-if="column.dataIndex === 'payStatus'">
					<a-tag :color="getPayStatusColor(record.payStatus)">{{ getPayStatusName(record.payStatus) }}</a-tag>
				</template>
				<template v-if="column.dataIndex === 'payAmount'">
					<span class="text-red">¥{{ record.payAmount }}</span>
				</template>
				<template v-if="column.dataIndex === 'actualAmount'">
					<span v-if="record.actualAmount" class="text-green">¥{{ record.actualAmount }}</span>
					<span v-else>-</span>
				</template>
				<template v-if="column.dataIndex === 'action'">
					<a @click="detail(record)">详情</a>
				</template>
			</template>
		</s-table>
	</a-card>
	<!-- 详情抽屉 -->
	<a-drawer v-model:open="detailVisible" title="支付记录详情" :width="600" :body-style="{ paddingBottom: '80px' }">
		<a-descriptions :column="1" bordered v-if="detailData">
			<a-descriptions-item label="支付单号">{{ detailData.paymentNo }}</a-descriptions-item>
			<a-descriptions-item label="订单号">{{ detailData.orderNo }}</a-descriptions-item>
			<a-descriptions-item label="订单ID">{{ detailData.orderId }}</a-descriptions-item>
			<a-descriptions-item label="用户ID">{{ detailData.userId }}</a-descriptions-item>
			<a-descriptions-item label="支付方式">
				<a-tag :color="getPayTypeColor(detailData.payType)">{{ getPayTypeName(detailData.payType) }}</a-tag>
			</a-descriptions-item>
			<a-descriptions-item label="支付状态">
				<a-tag :color="getPayStatusColor(detailData.payStatus)">{{ getPayStatusName(detailData.payStatus) }}</a-tag>
			</a-descriptions-item>
			<a-descriptions-item label="支付金额">
				<span class="text-red">¥{{ detailData.payAmount }}</span>
			</a-descriptions-item>
			<a-descriptions-item label="实际支付金额" v-if="detailData.actualAmount">
				<span class="text-green">¥{{ detailData.actualAmount }}</span>
			</a-descriptions-item>
			<a-descriptions-item label="支付时间" v-if="detailData.payTime">{{ detailData.payTime }}</a-descriptions-item>
			<a-descriptions-item label="第三方交易流水号" v-if="detailData.transactionId">{{
				detailData.transactionId
			}}</a-descriptions-item>
			<a-descriptions-item label="预支付ID" v-if="detailData.prepayId">{{ detailData.prepayId }}</a-descriptions-item>
			<a-descriptions-item label="退款金额" v-if="detailData.refundAmount">
				<span class="text-orange">¥{{ detailData.refundAmount }}</span>
			</a-descriptions-item>
			<a-descriptions-item label="退款时间" v-if="detailData.refundTime">{{
				detailData.refundTime
			}}</a-descriptions-item>
			<a-descriptions-item label="退款单号" v-if="detailData.refundNo">{{ detailData.refundNo }}</a-descriptions-item>
			<a-descriptions-item label="回调时间" v-if="detailData.callbackTime">{{
				detailData.callbackTime
			}}</a-descriptions-item>
			<a-descriptions-item label="创建时间">{{ detailData.createTime }}</a-descriptions-item>
			<a-descriptions-item label="更新时间" v-if="detailData.updateTime">{{
				detailData.updateTime
			}}</a-descriptions-item>
			<a-descriptions-item label="回调内容" v-if="detailData.callbackContent">
				<a-typography-paragraph :ellipsis="{ rows: 3, expandable: true }">
					{{ detailData.callbackContent }}
				</a-typography-paragraph>
			</a-descriptions-item>
		</a-descriptions>
	</a-drawer>
</template>

<script setup name="paymentRecord">
	import { message } from 'ant-design-vue'
	import { ExportOutlined, RedoOutlined, SearchOutlined } from '@ant-design/icons-vue'
	import paymentRecordApi from '@/api/payment/paymentRecordApi'

	const searchFormRef = ref()
	const table = ref()
	const detailVisible = ref(false)
	const detailData = ref({})

	const searchFormState = reactive({
		searchKey: undefined,
		payType: undefined,
		payStatus: undefined,
		transactionId: undefined,
		createTime: undefined
	})

	const columns = [
		{
			title: '支付单号',
			dataIndex: 'paymentNo',
			width: 180,
			ellipsis: true
		},
		{
			title: '订单号',
			dataIndex: 'orderNo',
			width: 180,
			ellipsis: true
		},
		{
			title: '用户ID',
			dataIndex: 'userId',
			width: 120,
			ellipsis: true
		},
		{
			title: '支付方式',
			dataIndex: 'payType',
			width: 120
		},
		{
			title: '支付状态',
			dataIndex: 'payStatus',
			width: 100
		},
		{
			title: '支付金额',
			dataIndex: 'payAmount',
			width: 100,
			align: 'right'
		},
		{
			title: '实际金额',
			dataIndex: 'actualAmount',
			width: 100,
			align: 'right'
		},
		{
			title: '支付时间',
			dataIndex: 'payTime',
			width: 160
		},
		{
			title: '创建时间',
			dataIndex: 'createTime',
			width: 160
		},
		{
			title: '操作',
			dataIndex: 'action',
			align: 'center',
			width: 80,
			fixed: 'right'
		}
	]

	const loadData = (parameter) => {
		const searchParam = { ...searchFormState }

		// 处理搜索关键词，支持搜索支付单号、订单号、用户ID
		if (searchParam.searchKey) {
			searchParam.paymentNo = searchParam.searchKey
			searchParam.orderNo = searchParam.searchKey
			searchParam.userId = searchParam.searchKey
			delete searchParam.searchKey
		}

		// 处理时间范围
		if (searchParam.createTime && searchParam.createTime.length === 2) {
			searchParam.startTime = searchParam.createTime[0]
			searchParam.endTime = searchParam.createTime[1]
			delete searchParam.createTime
		}

		return paymentRecordApi.paymentRecordPage(Object.assign(parameter, searchParam)).then((res) => {
			return res
		})
	}

	// 重置
	const reset = () => {
		searchFormRef.value.resetFields()
		refresh()
	}

	// 刷新
	const refresh = () => {
		table.value.refresh(true)
	}

	// 详情
	const detail = (record) => {
		paymentRecordApi.paymentRecordDetail({ id: record.id }).then((res) => {
			detailData.value = res
			detailVisible.value = true
		})
	}

	// 导出
	const exportData = () => {
		const searchParam = { ...searchFormState }

		// 处理搜索关键词
		if (searchParam.searchKey) {
			searchParam.paymentNo = searchParam.searchKey
			searchParam.orderNo = searchParam.searchKey
			searchParam.userId = searchParam.searchKey
			delete searchParam.searchKey
		}

		// 处理时间范围
		if (searchParam.createTime && searchParam.createTime.length === 2) {
			searchParam.startTime = searchParam.createTime[0]
			searchParam.endTime = searchParam.createTime[1]
			delete searchParam.createTime
		}

		paymentRecordApi
			.paymentRecordExport(searchParam)
			.then((res) => {
				const blob = new Blob([res], { type: 'application/vnd.ms-excel' })
				const url = window.URL.createObjectURL(blob)
				const link = document.createElement('a')
				link.href = url
				link.download = `支付记录_${new Date().getTime()}.xlsx`
				link.click()
				window.URL.revokeObjectURL(url)
				message.success('导出成功')
			})
			.catch(() => {
				message.error('导出失败')
			})
	}

	// 支付方式颜色
	const getPayTypeColor = (payType) => {
		const colorMap = {
			WECHAT_MINI: 'green',
			WECHAT_H5: 'green',
			WECHAT_APP: 'green',
			WECHAT_NATIVE: 'green',
			ALIPAY: 'blue',
			ALIPAY_H5: 'blue',
			ALIPAY_APP: 'blue',
			BALANCE: 'orange'
		}
		return colorMap[payType] || 'default'
	}

	// 支付方式名称
	const getPayTypeName = (payType) => {
		const nameMap = {
			WECHAT_MINI: '微信小程序',
			WECHAT_H5: '微信H5',
			WECHAT_APP: '微信APP',
			WECHAT_NATIVE: '微信扫码',
			ALIPAY: '支付宝',
			ALIPAY_H5: '支付宝H5',
			ALIPAY_APP: '支付宝APP',
			BALANCE: '余额支付'
		}
		return nameMap[payType] || payType
	}

	// 支付状态颜色
	const getPayStatusColor = (payStatus) => {
		const colorMap = {
			PENDING: 'orange',
			SUCCESS: 'green',
			FAILED: 'red',
			CANCELLED: 'gray',
			REFUNDED: 'purple',
			PARTIAL_REFUNDED: 'purple',
			EXPIRED: 'gray'
		}
		return colorMap[payStatus] || 'default'
	}

	// 支付状态名称
	const getPayStatusName = (payStatus) => {
		const nameMap = {
			PENDING: '待支付',
			SUCCESS: '支付成功',
			FAILED: '支付失败',
			CANCELLED: '已取消',
			REFUNDED: '已退款',
			PARTIAL_REFUNDED: '部分退款',
			EXPIRED: '已过期'
		}
		return nameMap[payStatus] || payStatus
	}

	const toolConfig = {
		refresh: true,
		height: true,
		columnSetting: true,
		striped: false
	}

	const options = {
		alert: {
			show: false,
			clear: () => {
				selectedRowKeys.value = ref([])
			}
		},
		rowSelection: {
			onChange: (selectedRowKeys, selectedRows) => {
				console.log(`selectedRowKeys: ${selectedRowKeys}`, 'selectedRows: ', selectedRows)
			}
		}
	}
</script>

<style scoped>
	.text-red {
		color: #f5222d;
		font-weight: 600;
	}

	.text-green {
		color: #52c41a;
		font-weight: 600;
	}

	.text-orange {
		color: #fa8c16;
		font-weight: 600;
	}
</style>
