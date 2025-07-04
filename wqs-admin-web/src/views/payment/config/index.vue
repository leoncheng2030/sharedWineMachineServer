<template>
	<a-card :bordered="false" class="xn-mb10">
		<a-form ref="searchFormRef" name="advanced_search" class="ant-advanced-search-form" :model="searchFormState">
			<a-row :gutter="24">
				<a-col :span="6">
					<a-form-item name="searchKey" label="搜索关键词">
						<a-input v-model:value="searchFormState.searchKey" placeholder="请输入配置名称" />
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
					<a-form-item name="status" label="状态">
						<a-select
							v-model:value="searchFormState.status"
							placeholder="请选择状态"
							:getPopupContainer="(trigger) => trigger.parentNode"
							allow-clear
						>
							<a-select-option value="ENABLE">启用</a-select-option>
							<a-select-option value="DISABLE">禁用</a-select-option>
						</a-select>
					</a-form-item>
				</a-col>
				<a-col :span="6">
					<a-button type="primary" @click="refresh">
						<template #icon><SearchOutlined /></template>
						查询
					</a-button>
					<a-button class="snowy-button-left" @click="reset">
						<template #icon><redo-outlined /></template>
						重置
					</a-button>
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
					<a-button type="primary" @click="openForm()">
						<template #icon><plus-outlined /></template>
						新增
					</a-button>
					<xn-batch-button
						buttonName="批量删除"
						icon="DeleteOutlined"
						buttonDanger
						:selectedRowKeys="selectedRowKeys"
						@batchCallBack="deleteBatchPaymentConfig"
					/>
				</a-space>
			</template>
			<template #bodyCell="{ column, record }">
				<template v-if="column.dataIndex === 'payType'">
					<a-tag :color="getPayTypeColor(record.payType)">{{ getPayTypeName(record.payType) }}</a-tag>
				</template>
				<template v-if="column.dataIndex === 'status'">
					<a-switch
						:checked="record.status === 'ENABLE'"
						@change="(checked) => editStatus(record, checked)"
						checked-children="启用"
						un-checked-children="禁用"
					/>
				</template>
				<template v-if="column.dataIndex === 'merchantId'">
					<span>{{ getConfigValue(record, 'merchantId') || '-' }}</span>
				</template>
				<template v-if="column.dataIndex === 'appId'">
					<span>{{ getConfigValue(record, 'appId') || '-' }}</span>
				</template>
				<template v-if="column.dataIndex === 'sandboxMode'">
					<a-tag :color="getConfigValue(record, 'sandboxMode') == 1 ? 'orange' : 'green'">
						{{ getConfigValue(record, 'sandboxMode') == 1 ? '沙箱环境' : '正式环境' }}
					</a-tag>
				</template>
				<template v-if="column.dataIndex === 'action'">
					<a @click="openForm(record)">编辑</a>
					<a-divider type="vertical" />
					<a-popconfirm title="确定要删除吗？" @confirm="deletePaymentConfig(record)">
						<a class="xn-text-danger">删除</a>
					</a-popconfirm>
				</template>
			</template>
		</s-table>
	</a-card>
	<!-- 表单 -->
	<Form v-model:visible="formVisible" ref="formRef" @successful="refresh" />
</template>

<script setup name="paymentConfig">
	import { message } from 'ant-design-vue'
	import { PlusOutlined, RedoOutlined, SearchOutlined } from '@ant-design/icons-vue'
	import paymentConfigApi from '@/api/payment/paymentConfigApi'
	import Form from './form.vue'

	const searchFormRef = ref()
	const table = ref()
	const formRef = ref()
	const formVisible = ref(false)
	const selectedRowKeys = ref([])

	const searchFormState = reactive({
		searchKey: undefined,
		payType: undefined,
		status: undefined
	})

	const columns = [
		{
			title: '配置名称',
			dataIndex: 'configName',
			width: 200,
			ellipsis: true
		},
		{
			title: '支付方式',
			dataIndex: 'payType',
			width: 140
		},
		{
			title: '商户号',
			dataIndex: 'merchantId',
			width: 150,
			ellipsis: true
		},
		{
			title: '应用ID',
			dataIndex: 'appId',
			width: 150,
			ellipsis: true
		},
		{
			title: '环境模式',
			dataIndex: 'sandboxMode',
			width: 100
		},
		{
			title: '状态',
			dataIndex: 'status',
			width: 100
		},
		{
			title: '排序码',
			dataIndex: 'sortCode',
			width: 80
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
			width: 120,
			fixed: 'right'
		}
	]

	const loadData = (parameter) => {
		const searchParam = { ...searchFormState }
		// 如果有搜索关键词，搜索配置名称
		if (searchParam.searchKey) {
			searchParam.configName = searchParam.searchKey
			delete searchParam.searchKey
		}
		return paymentConfigApi.paymentConfigPage(Object.assign(parameter, searchParam)).then((res) => {
			return res
		})
	}

	// 从JSON配置中获取值
	const getConfigValue = (record, key) => {
		if (!record.extJson) return null
		try {
			const config = JSON.parse(record.extJson)
			return config[key]
		} catch (error) {
			console.error('解析JSON配置失败:', error)
			return null
		}
	}

	// 打开表单
	const openForm = (record) => {
		formVisible.value = true
		if (record) {
			// 编辑模式
			nextTick(() => {
				formRef.value?.setFormData(record)
			})
		}
	}

	// 重置
	const reset = () => {
		searchFormRef.value.resetFields()
		refresh()
	}

	// 刷新
	const refresh = () => {
		table.value.refresh()
	}

	// 删除
	const deletePaymentConfig = (record) => {
		const param = { id: record.id }
		paymentConfigApi.paymentConfigDelete(param).then(() => {
			message.success('删除成功')
			refresh()
		})
	}

	// 批量删除
	const deleteBatchPaymentConfig = (params) => {
		paymentConfigApi.paymentConfigDelete(params).then(() => {
			message.success('删除成功')
			refresh()
		})
	}

	// 编辑状态
	const editStatus = (record, checked) => {
		const param = {
			id: record.id,
			status: checked ? 'ENABLE' : 'DISABLE'
		}
		paymentConfigApi
			.paymentConfigEdit(param)
			.then(() => {
				message.success('状态修改成功')
				record.status = param.status
			})
			.catch(() => {
				// 恢复开关状态
				record.status = record.status === 'ENABLE' ? 'DISABLE' : 'ENABLE'
			})
	}

	// 获取支付方式颜色
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

	// 获取支付方式名称
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

	const toolConfig = {
		refresh: true,
		height: true,
		columnSetting: true,
		striped: false
	}

	const options = {
		alert: {
			show: true,
			clear: () => {
				selectedRowKeys.value = ref([])
			}
		},
		rowSelection: {
			onChange: (changableRowKeys) => {
				selectedRowKeys.value = changableRowKeys
			}
		}
	}
</script>
