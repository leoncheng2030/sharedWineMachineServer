<template>
	<div>
		<a-card :bordered="false" class="xn-mb10">
			<a-form ref="searchFormRef" name="advanced_search" class="ant-advanced-search-form" :model="searchFormState">
				<a-row :gutter="24">
					<a-col :span="6">
						<a-form-item name="searchKey" label="搜索关键词">
							<a-input v-model:value="searchFormState.searchKey" placeholder="请输入策略名称或酒品名称" />
						</a-form-item>
					</a-col>
					<a-col :span="6">
						<a-form-item name="productId" label="酒品选择">
							<a-select
								v-model:value="searchFormState.productId"
								placeholder="请选择酒品"
								:getPopupContainer="(trigger) => trigger.parentNode"
								allowClear
								show-search
								:filter-option="filterOption"
							>
								<a-select-option v-for="item in productData" :key="item.id" :value="item.id">
									{{ item.productName }} ({{ item.productCode }})
								</a-select-option>
							</a-select>
						</a-form-item>
					</a-col>
					<a-col :span="6">
						<a-form-item name="capacity" label="容量筛选">
							<a-input v-model:value="searchFormState.capacity" placeholder="请输入容量(ml)" />
						</a-form-item>
					</a-col>
				</a-row>
				<a-row :gutter="24">
					<a-col :span="6">
						<a-form-item name="status" label="状态">
							<a-select
								v-model:value="searchFormState.status"
								placeholder="请选择状态"
								:getPopupContainer="(trigger) => trigger.parentNode"
								allowClear
							>
								<a-select-option v-for="item in statusData" :key="item.value" :value="item.value">
									{{ item.label }}
								</a-select-option>
							</a-select>
						</a-form-item>
					</a-col>
					<a-col :span="6">
						<a-button type="primary" @click="tableRef.refresh(true)">
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
		<a-card :bordered="false">
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
						<a-button type="primary" @click="formRef.onOpen()" v-if="hasPerm('winePriceAdd')">
							<template #icon><plus-outlined /></template>
							<span>新增价格策略</span>
						</a-button>
						<a-button
							@click="batchSetDiscount"
							v-if="hasPerm('winePriceBatchSet')"
							:disabled="selectedRowKeys.length === 0"
						>
							<template #icon><EditOutlined /></template>
							批量设置折扣
						</a-button>
						<a-button @click="exportData" v-if="hasPerm('winePriceExport')">
							<template #icon><export-outlined /></template>
							导出
						</a-button>
						<xn-batch-button
							v-if="hasPerm('winePriceBatchDelete')"
							:buttonName="'批量删除'"
							icon="DeleteOutlined"
							buttonDanger
							:selectedRowKeys="selectedRowKeys"
							@batchCallBack="deleteBatchPrice"
						/>
					</a-space>
				</template>
				<template #bodyCell="{ column, record }">
					<template v-if="column.dataIndex === 'productInfo'">
						<div>
							<div style="font-weight: bold">{{ record.productName || '未知酒品' }}</div>
							<div style="color: #999; font-size: 12px">{{ record.productCode || '无编码' }}</div>
						</div>
					</template>
					<template v-if="column.dataIndex === 'unitPrice'">
						<span v-if="record.unitPrice" style="color: #1890ff; font-weight: bold">¥{{ record.unitPrice }}/ml</span>
						<span v-else style="color: #999">未设置</span>
					</template>
					<template v-if="column.dataIndex === 'capacity'">
						<span v-if="record.capacity">{{ record.capacity }}ml</span>
						<span v-else style="color: #999">未设置</span>
					</template>
					<template v-if="column.dataIndex === 'discountRate'">
						<span v-if="record.discountRate !== undefined">
							<a-tag :color="getDiscountColor(record.discountRate)">
								{{ (record.discountRate * 100).toFixed(1) }}%
							</a-tag>
						</span>
						<span v-else style="color: #999">未设置</span>
					</template>
					<template v-if="column.dataIndex === 'finalPrice'">
						<span
							v-if="record.unitPrice && record.capacity && record.discountRate !== undefined"
							style="color: #f50; font-weight: bold; font-size: 16px"
						>
							¥{{ calculateFinalPrice(record.unitPrice, record.capacity, record.discountRate) }}
						</span>
						<span v-else style="color: #999">无法计算</span>
					</template>
					<template v-if="column.dataIndex === 'effectiveDate'">
						<span>{{ record.effectiveDate || '-' }}</span>
					</template>
					<template v-if="column.dataIndex === 'expiryDate'">
						<span v-if="record.expiryDate">{{ record.expiryDate }}</span>
						<a-tag v-else color="blue">长期有效</a-tag>
					</template>
					<template v-if="column.dataIndex === 'status'">
						<a-switch
							:loading="loading"
							:checked="record.status === 'ENABLE'"
							@change="editStatus(record)"
							v-if="hasPerm('winePriceUpdateStatus')"
						/>
						<a-tag v-else :color="record.status === 'ENABLE' ? 'green' : 'red'">
							{{ record.status === 'ENABLE' ? '启用' : '禁用' }}
						</a-tag>
					</template>
					<template v-if="column.dataIndex === 'action'">
						<a @click="formRef.onOpen(record)" v-if="hasPerm('winePriceEdit')">编辑</a>
						<a-divider type="vertical" v-if="hasPerm(['winePriceEdit', 'winePriceCopy'], 'and')" />
						<a @click="copyPrice(record)" v-if="hasPerm('winePriceCopy')">复制</a>
						<a-divider type="vertical" v-if="hasPerm(['winePriceCopy', 'winePriceDelete'], 'and')" />
						<a-popconfirm title="确定要删除这个价格策略吗？" @confirm="removePrice(record)">
							<a-button type="link" danger size="small" v-if="hasPerm('winePriceDelete')"> 删除 </a-button>
						</a-popconfirm>
						<a-divider type="vertical" v-if="hasPerm(['winePriceDelete', 'winePriceHistory'])" />
						<a-dropdown v-if="hasPerm(['winePriceHistory', 'winePricePreview'])">
							<a class="ant-dropdown-link">
								更多
								<DownOutlined />
							</a>
							<template #overlay>
								<a-menu>
									<a-menu-item v-if="hasPerm('winePriceHistory')">
										<a @click="viewHistory(record)">价格历史</a>
									</a-menu-item>
									<a-menu-item v-if="hasPerm('winePricePreview')">
										<a @click="previewPrice(record)">价格预览</a>
									</a-menu-item>
								</a-menu>
							</template>
						</a-dropdown>
					</template>
				</template>
			</s-table>
		</a-card>
	</div>
	<Form ref="formRef" @successful="tableRef.refresh()" />
</template>

<script setup name="winePrice">
	import { message } from 'ant-design-vue'
	import downloadUtil from '@/utils/downloadUtil'
	import winePriceApi from '@/api/wine/winePriceApi'
	import wineProductApi from '@/api/wine/wineProductApi'
	import Form from './form.vue'

	const columns = [
		{
			title: '酒品信息',
			dataIndex: 'productInfo',
			width: '200px'
		},
		{
			title: '基础单价',
			dataIndex: 'unitPrice',
			width: '100px',
			align: 'center'
		},
		{
			title: '容量',
			dataIndex: 'capacity',
			width: '80px',
			align: 'center'
		},
		{
			title: '折扣率',
			dataIndex: 'discountRate',
			width: '80px',
			align: 'center'
		},
		{
			title: '最终价格',
			dataIndex: 'finalPrice',
			width: '120px',
			align: 'center'
		},
		{
			title: '生效日期',
			dataIndex: 'effectiveDate',
			width: '130px',
			align: 'center'
		},
		{
			title: '失效日期',
			dataIndex: 'expiryDate',
			width: '130px',
			align: 'center'
		},
		{
			title: '状态',
			dataIndex: 'status',
			align: 'center',
			width: '80px'
		},
		{
			title: '创建时间',
			dataIndex: 'createTime',
			width: '130px'
		},
		{
			title: '操作',
			dataIndex: 'action',
			align: 'center',
			width: '120px',
			fixed: 'right'
		}
	]

	const searchFormState = reactive({
		searchKey: '',
		productId: undefined,
		capacity: undefined,
		status: undefined
	})

	const searchFormRef = ref()
	const tableRef = ref()
	const formRef = ref()
	const loading = ref(false)

	// 状态数据
	const statusData = [
		{ value: 'ENABLE', label: '启用' },
		{ value: 'DISABLE', label: '禁用' }
	]

	// 酒品数据
	const productData = ref([])

	// 表格选择
	const options = reactive({
		alert: {
			show: false,
			clear: () => {
				selectedRowKeys.value = []
			}
		},
		rowSelection: {
			onChange: (selectedRowKeys, selectedRows) => {
				options.rowSelection.selectedRowKeys = selectedRowKeys
			},
			selectedRowKeys: []
		}
	})

	const selectedRowKeys = computed(() => options.rowSelection.selectedRowKeys)

	// 表格配置
	const toolConfig = {
		refresh: true,
		height: true,
		columnSetting: true,
		striped: false
	}

	// 加载数据
	const loadData = (parameter) => {
		const searchParam = Object.assign(parameter, searchFormState)
		return winePriceApi.pricePage(searchParam).then((res) => {
			return res
		})
	}

	// 重置搜索
	const reset = () => {
		searchFormRef.value.resetFields()
		tableRef.value.refresh(true)
	}

	// 删除
	const removePrice = (record) => {
		const param = {
			id: record.id
		}
		winePriceApi.priceDelete(param).then(() => {
			tableRef.value.refresh(true)
		})
	}

	// 批量删除
	const deleteBatchPrice = (params) => {
		winePriceApi.priceBatchDelete(params).then(() => {
			tableRef.value.refresh(true)
		})
	}

	// 更改状态
	const editStatus = (record) => {
		loading.value = true
		const api = record.status === 'ENABLE' ? winePriceApi.priceDisable : winePriceApi.priceEnable
		api({ id: record.id })
			.then(() => {
				tableRef.value.refresh()
			})
			.finally(() => {
				loading.value = false
			})
	}

	// 复制价格策略
	const copyPrice = (record) => {
		winePriceApi.priceCopy({ id: record.id }).then(() => {
			tableRef.value.refresh()
		})
	}

	// 批量设置折扣
	const batchSetDiscount = () => {
		// TODO: 实现批量设置折扣功能
		message.info('批量设置折扣功能待实现')
	}

	// 导出
	const exportData = () => {
		const param = Object.assign({}, searchFormState)
		winePriceApi.priceExport(param).then((res) => {
			downloadUtil.resultDownload(res, '酒品价格策略数据.xlsx', 'application/vnd.ms-excel')
		})
	}

	// 查看价格历史
	const viewHistory = (record) => {
		// TODO: 实现价格历史功能
		message.info('价格历史功能待实现')
	}

	// 价格预览
	const previewPrice = (record) => {
		// TODO: 实现价格预览功能
		message.info('价格预览功能待实现')
	}

	// 搜索过滤
	const filterOption = (input, option) => {
		return option.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
	}

	// 计算最终价格
	const calculateFinalPrice = (unitPrice, capacity, discountRate) => {
		if (!unitPrice || !capacity || discountRate === undefined) {
			return '0.00'
		}
		const baseAmount = unitPrice * capacity
		const discountAmount = baseAmount * discountRate
		const finalPrice = baseAmount - discountAmount
		return finalPrice.toFixed(2)
	}

	// 获取折扣率颜色
	const getDiscountColor = (discountRate) => {
		if (discountRate === 0) return 'default'
		if (discountRate <= 0.05) return 'green'
		if (discountRate <= 0.1) return 'blue'
		if (discountRate <= 0.2) return 'orange'
		return 'red'
	}

	// 加载酒品数据
	const loadProductData = () => {
		wineProductApi.productList().then((res) => {
			productData.value = res.data || []
		})
	}

	onMounted(() => {
		loadProductData()
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
