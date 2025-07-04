<template>
	<div>
		<a-card :bordered="false" class="xn-mb10">
			<a-form ref="searchFormRef" name="advanced_search" class="ant-advanced-search-form" :model="searchFormState">
				<a-row :gutter="24">
					<a-col :span="6">
						<a-form-item name="searchKey" label="搜索关键词">
							<a-input v-model:value="searchFormState.searchKey" placeholder="请输入配置名称或酒品名称" />
						</a-form-item>
					</a-col>
					<a-col :span="6">
						<a-form-item name="storeId" label="所属门店">
							<a-select
								v-model:value="searchFormState.storeId"
								placeholder="请选择门店"
								:getPopupContainer="(trigger) => trigger.parentNode"
								allowClear
								show-search
								:filter-option="filterOption"
							>
								<a-select-option v-for="item in storeData" :key="item.id" :value="item.id">
									{{ item.storeName || item.name }}
								</a-select-option>
							</a-select>
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
									{{ item.name || item.productName }} ({{ item.code || item.productCode }})
								</a-select-option>
							</a-select>
						</a-form-item>
					</a-col>
				</a-row>
				<a-row :gutter="24">
					<a-col :span="6">
						<a-form-item name="configType" label="配置类型">
							<a-select
								v-model:value="searchFormState.configType"
								placeholder="请选择配置类型"
								:getPopupContainer="(trigger) => trigger.parentNode"
								allowClear
							>
								<a-select-option v-for="item in configTypeData" :key="item.value" :value="item.value">
									{{ item.label }}
								</a-select-option>
							</a-select>
						</a-form-item>
					</a-col>
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
						<a-button type="primary" @click="formRef.onOpen()" v-if="hasPerm('commissionManageAdd')">
							<template #icon><plus-outlined /></template>
							<span>新增佣金配置</span>
						</a-button>
						<a-button @click="copyDefaultConfig" v-if="hasPerm('commissionManageCopy')">
							<template #icon><CopyOutlined /></template>
							复制平台配置
						</a-button>
						<a-button @click="exportData" v-if="hasPerm('commissionManageExport')">
							<template #icon><export-outlined /></template>
							导出
						</a-button>
						<xn-batch-button
							v-if="hasPerm('commissionManageBatchDelete')"
							:buttonName="'批量删除'"
							icon="DeleteOutlined"
							buttonDanger
							:selectedRowKeys="selectedRowKeys"
							@batchCallBack="deleteBatchCommission"
						/>
					</a-space>
				</template>
				<template #bodyCell="{ column, record }">
					<template v-if="column.dataIndex === 'configType'">
						<a-tag v-if="record.configType === 'PLATFORM_DEFAULT'" color="blue">平台默认</a-tag>
						<a-tag v-else-if="record.configType === 'PRODUCT_GENERAL'" color="green">酒品通用</a-tag>
						<a-tag v-else-if="record.configType === 'STORE_GENERAL'" color="orange">门店通用</a-tag>
						<a-tag v-else-if="record.configType === 'STORE_PRODUCT'" color="red">门店酒品专属</a-tag>
						<span v-else>{{ record.configType }}</span>
					</template>
					<template v-if="column.dataIndex === 'storeName'">
						<a-tag v-if="record.storeName" color="green">{{ record.storeName }}</a-tag>
						<a-tag v-else color="blue">全平台</a-tag>
					</template>
					<template v-if="column.dataIndex === 'productInfo'">
						<div v-if="record.productName">
							<div style="font-weight: bold">{{ record.productName }}</div>
							<div style="color: #999; font-size: 12px">{{ record.productCode || '无编码' }}</div>
						</div>
						<a-tag v-else color="blue">全酒品</a-tag>
					</template>
					<template v-if="column.dataIndex === 'commissionRates'">
						<div class="commission-rates">
							<div v-if="record.platformRate > 0">
								<a-tag color="blue">平台: {{ formatRate(record.platformRate) }}%</a-tag>
							</div>
							<div v-if="record.deviceOwnerRate > 0">
								<a-tag color="green">设备方: {{ formatRate(record.deviceOwnerRate) }}%</a-tag>
							</div>
							<div v-if="record.locationProviderRate > 0">
								<a-tag color="orange">场地方: {{ formatRate(record.locationProviderRate) }}%</a-tag>
							</div>
							<div v-if="record.storeManagerRate > 0">
								<a-tag color="purple">门店管理员: {{ formatRate(record.storeManagerRate) }}%</a-tag>
							</div>
							<div v-if="record.supplierRate > 0">
								<a-tag color="cyan">供应商: {{ formatRate(record.supplierRate) }}%</a-tag>
							</div>
						</div>
					</template>
					<template v-if="column.dataIndex === 'totalRate'">
						<span style="color: #f50; font-weight: bold">{{ getTotalRate(record) }}%</span>
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
							v-if="hasPerm('commissionManageUpdateStatus')"
						/>
						<a-tag v-else :color="record.status === 'ENABLE' ? 'green' : 'red'">
							{{ record.status === 'ENABLE' ? '启用' : '禁用' }}
						</a-tag>
					</template>
					<template v-if="column.dataIndex === 'action'">
						<a @click="formRef.onOpen(record)" v-if="hasPerm('commissionManageEdit')">编辑</a>
						<a-divider type="vertical" v-if="hasPerm(['commissionManageEdit', 'commissionManageCopy'], 'and')" />
						<a @click="copyConfig(record)" v-if="hasPerm('commissionManageCopy')">复制</a>
						<a-divider type="vertical" v-if="hasPerm(['commissionManageCopy', 'commissionManageDelete'], 'and')" />
						<a-popconfirm title="确定要删除这个佣金配置吗？" @confirm="removeCommission(record)">
							<a-button type="link" danger size="small" v-if="hasPerm('commissionManageDelete')"> 删除 </a-button>
						</a-popconfirm>
						<a-divider type="vertical" v-if="hasPerm(['commissionManageDelete', 'commissionManageHistory'])" />
						<a-dropdown v-if="hasPerm(['commissionManageHistory', 'commissionManagePreview'])">
							<a class="ant-dropdown-link">
								更多
								<DownOutlined />
							</a>
							<template #overlay>
								<a-menu>
									<a-menu-item v-if="hasPerm('commissionManageHistory')">
										<a @click="viewHistory(record)">佣金历史</a>
									</a-menu-item>
									<a-menu-item v-if="hasPerm('commissionManagePreview')">
										<a @click="previewCommission(record)">佣金预览</a>
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

<script setup name="wineCommission">
	import { message } from 'ant-design-vue'
	import downloadUtil from '@/utils/downloadUtil'
	import { hasPerm } from '@/utils/permission'
	import wineCommissionApi from '@/api/wine/wineCommissionApi'
	import wineProductApi from '@/api/wine/wineProductApi'
	import wineStoreApi from '@/api/wine/wineStoreApi'
	import Form from './form.vue'

	const columns = [
		{
			title: '配置类型',
			dataIndex: 'configType',
			width: '120px'
		},
		{
			title: '所属门店',
			dataIndex: 'storeName',
			width: '120px'
		},
		{
			title: '酒品信息',
			dataIndex: 'productInfo',
			ellipsis: true,
			width: '150px'
		},
		{
			title: '佣金比例分配',
			dataIndex: 'commissionRates',
			width: '300px'
		},
		{
			title: '总比例',
			dataIndex: 'totalRate',
			width: '80px'
		},
		{
			title: '生效日期',
			dataIndex: 'effectiveDate',
			width: '120px'
		},
		{
			title: '失效日期',
			dataIndex: 'expiryDate',
			width: '120px'
		},
		{
			title: '状态',
			dataIndex: 'status',
			width: '100px'
		},
		{
			title: '操作',
			dataIndex: 'action',
			align: 'center',
			width: '180px'
		}
	]

	// 搜索表单状态
	const searchFormState = reactive({
		searchKey: '',
		storeId: undefined,
		productId: undefined,
		configType: undefined,
		status: undefined
	})

	const searchFormRef = ref()
	const tableRef = ref()
	const formRef = ref()
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
			},
			onSelect: (record, selected, selectedRows) => {
				// console.log(record, selected, selectedRows)
			},
			onSelectAll: (selected, selectedRows, changeRows) => {
				// console.log(selected, selectedRows, changeRows)
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

	// 状态数据
	const statusData = [
		{ value: 'ENABLE', label: '启用' },
		{ value: 'DISABLE', label: '禁用' }
	]

	// 配置类型数据
	const configTypeData = [
		{ value: 'PLATFORM_DEFAULT', label: '平台默认' },
		{ value: 'PRODUCT_GENERAL', label: '酒品通用' },
		{ value: 'STORE_GENERAL', label: '门店通用' },
		{ value: 'STORE_PRODUCT', label: '门店酒品专属' }
	]

	// 门店数据和酒品数据
	const storeData = ref([])
	const productData = ref([])

	// 加载数据
	const loadData = (parameter) => {
		const searchParam = Object.assign(parameter, searchFormState)
		return wineCommissionApi.page(searchParam).then((res) => {
			return res
		})
	}

	// 加载门店和酒品数据
	const loadSelectorData = () => {
		// 加载门店数据
		wineStoreApi.list().then((data) => {
			storeData.value = data
		})
		// 加载酒品数据
		wineProductApi.productList().then((data) => {
			productData.value = data
		})
	}

	// 重置搜索
	const reset = () => {
		searchFormRef.value.resetFields()
		tableRef.value.refresh(true)
	}

	// 过滤选项
	const filterOption = (input, option) => {
		return option.children[0].children.toLowerCase().indexOf(input.toLowerCase()) >= 0
	}

	// 格式化比例显示（小数转百分比）
	const formatRate = (rate) => {
		if (!rate) return 0
		return Math.round(rate * 10000) / 100
	}

	// 计算总比例
	const getTotalRate = (record) => {
		const total =
			(record.platformRate || 0) +
			(record.deviceOwnerRate || 0) +
			(record.locationProviderRate || 0) +
			(record.storeManagerRate || 0) +
			(record.supplierRate || 0)
		// 后端返回的是小数形式，需要转换为百分比显示，保留2位小数
		return formatRate(total)
	}

	// 编辑状态
	const editStatus = (record) => {
		const api = record.status === 'ENABLE' ? wineCommissionApi.disable : wineCommissionApi.enable
		loading.value = true
		api({ id: record.id })
			.then(() => {
				message.success('状态更新成功')
				tableRef.value.refresh()
			})
			.finally(() => {
				loading.value = false
			})
	}

	// 删除佣金配置
	const removeCommission = (record) => {
		wineCommissionApi.delete({ id: record.id }).then(() => {
			message.success('删除成功')
			tableRef.value.refresh()
		})
	}

	// 批量删除
	const deleteBatchCommission = (params) => {
		wineCommissionApi.batchDelete(params).then(() => {
			message.success('删除成功')
			tableRef.value.refresh()
		})
	}

	// 复制配置
	const copyConfig = (record) => {
		// 创建复制数据，移除ID和时间戳字段
		const copyData = { ...record }
		delete copyData.id
		delete copyData.createTime
		delete copyData.updateTime
		delete copyData.createUser
		delete copyData.updateUser

		// 打开新增表单并填入复制的数据
		formRef.value.onOpenForCopy(copyData)
	}

	// 复制平台默认配置
	const copyDefaultConfig = () => {
		// 这里可以打开一个选择对话框，选择要复制到哪些门店或酒品
		message.info('功能开发中...')
	}

	// 查看历史记录
	const viewHistory = (record) => {
		message.info('功能开发中...')
	}

	// 佣金预览
	const previewCommission = (record) => {
		message.info('功能开发中...')
	}

	// 导出数据
	const exportData = () => {
		const param = Object.assign({}, searchFormState)
		wineCommissionApi.export(param).then((res) => {
			downloadUtil.resultDownload(res, '佣金配置数据.xlsx', 'application/vnd.ms-excel')
		})
	}

	// 页面加载时获取数据
	onMounted(() => {
		loadSelectorData()
	})
</script>

<style scoped>
	.commission-rates {
		display: flex;
		flex-direction: column;
		gap: 4px;
	}

	.commission-rates .ant-tag {
		margin: 0;
		font-size: 12px;
	}

	.xn-mb10 {
		margin-bottom: 10px;
	}

	.snowy-button-left {
		margin-left: 8px;
	}
</style>
