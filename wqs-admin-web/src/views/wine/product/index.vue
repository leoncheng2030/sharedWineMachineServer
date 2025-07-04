<template>
	<div>
		<a-card :bordered="false" class="xn-mb10">
			<a-form ref="searchFormRef" name="advanced_search" class="ant-advanced-search-form" :model="searchFormState">
				<a-row :gutter="24">
					<a-col :span="6">
						<a-form-item name="searchKey" label="搜索关键词">
							<a-input v-model:value="searchFormState.searchKey" placeholder="请输入酒品名称、编码或描述" />
						</a-form-item>
					</a-col>
					<a-col :span="6">
						<a-form-item name="categoryId" label="酒品分类">
							<a-select
								v-model:value="searchFormState.categoryId"
								placeholder="请选择酒品分类"
								:getPopupContainer="(trigger) => trigger.parentNode"
								allowClear
							>
								<a-select-option v-for="item in categoryData" :key="item.id" :value="item.id">
									{{ item.name }}
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
						<a-button type="primary" @click="formRef.onOpen()" v-if="hasPerm('wineProductAdd')">
							<template #icon><plus-outlined /></template>
							<span>新增酒品</span>
						</a-button>
						<a-button @click="exportData" v-if="hasPerm('wineProductExport')">
							<template #icon><export-outlined /></template>
							导出
						</a-button>
						<xn-batch-button
							v-if="hasPerm('wineProductBatchDelete')"
							:buttonName="'批量删除'"
							icon="DeleteOutlined"
							buttonDanger
							:selectedRowKeys="selectedRowKeys"
							@batchCallBack="deleteBatchProduct"
						/>
					</a-space>
				</template>
				<template #bodyCell="{ column, record }">
					<template v-if="column.dataIndex === 'imageUrl'">
						<a-image
							v-if="record.imageUrl"
							:width="50"
							:height="50"
							:src="record.imageUrl"
							:preview="{ src: record.imageUrl }"
							style="border-radius: 4px"
						/>
						<a-avatar v-else shape="square" :size="50" style="background-color: #f0f0f0; color: #999">
							<template #icon><FileImageOutlined /></template>
						</a-avatar>
					</template>
					<template v-if="column.dataIndex === 'categoryName'">
						<a-tag color="blue">{{ record.categoryName || '未分类' }}</a-tag>
					</template>
					<template v-if="column.dataIndex === 'suggestedPrice'">
						<span style="color: #f50; font-weight: bold">{{ record.suggestedPrice }}</span>
					</template>
					<template v-if="column.dataIndex === 'unitPrice'">
						<span v-if="record.unitPrice" style="color: #1890ff; font-weight: bold">
							{{ record.unitPrice }}
						</span>
						<span v-else style="color: #999">未设置</span>
					</template>
					<template v-if="column.dataIndex === 'volume'">
						{{ record.volume }}
					</template>
					<template v-if="column.dataIndex === 'alcoholContent'">
						{{ record.alcoholContent }}
					</template>
					<template v-if="column.dataIndex === 'status'">
						<a-switch
							:loading="loading"
							:checked="record.status === 'ENABLE'"
							@change="editStatus(record)"
							v-if="hasPerm('wineProductUpdateStatus')"
						/>
						<a-tag v-else :color="record.status === 'ENABLE' ? 'green' : 'red'">
							{{ record.status === 'ENABLE' ? '启用' : '禁用' }}
						</a-tag>
					</template>
					<template v-if="column.dataIndex === 'action'">
						<a @click="formRef.onOpen(record)" v-if="hasPerm('wineProductEdit')">编辑</a>
						<a-divider type="vertical" v-if="hasPerm(['wineProductEdit', 'wineProductDelete'], 'and')" />
						<a-popconfirm title="确定要删除这个酒品吗？" @confirm="removeProduct(record)">
							<a-button type="link" danger size="small" v-if="hasPerm('wineProductDelete')"> 删除 </a-button>
						</a-popconfirm>
						<a-divider type="vertical" />
						<a @click="onPriceManage(record)"><span style="color: #1890ff">价格管理</span></a>
						<a-divider type="vertical" v-if="hasPerm(['wineProductDetail', 'wineProductStock'])" />
						<a-dropdown v-if="hasPerm(['wineProductDetail', 'wineProductStock'])">
							<a class="ant-dropdown-link">
								更多
								<DownOutlined />
							</a>
							<template #overlay>
								<a-menu>
									<a-menu-item v-if="hasPerm('wineProductDetail')">
										<a @click="viewDetail(record)">查看详情</a>
									</a-menu-item>
									<a-menu-item v-if="hasPerm('wineProductStock')">
										<a @click="manageStock(record)">库存管理</a>
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
	<PriceForm ref="priceFormRef" @successful="tableRef.refresh()" />
</template>

<script setup name="wineProduct">
	import { message } from 'ant-design-vue'
	import { useRouter } from 'vue-router'
	import downloadUtil from '@/utils/downloadUtil'
	import wineProductApi from '@/api/wine/wineProductApi'
	import wineCategoryApi from '@/api/wine/wineCategoryApi'
	import Form from './form.vue'
	import PriceForm from '../price/form.vue'

	const columns = [
		{
			title: '酒品图片',
			dataIndex: 'imageUrl',
			align: 'center',
			width: '80px'
		},
		{
			title: '酒品名称',
			dataIndex: 'productName',
			ellipsis: true,
			width: '140px'
		},
		{
			title: '酒品编码',
			dataIndex: 'productCode',
			width: '110px'
		},
		{
			title: '分类',
			dataIndex: 'categoryName',
			width: '90px'
		},
		{
			title: '品牌',
			dataIndex: 'brand',
			ellipsis: true,
			width: '90px'
		},
		{
			title: '容量(ml)',
			dataIndex: 'volume',
			align: 'center',
			width: '80px'
		},
		{
			title: '酒精度(%)',
			dataIndex: 'alcoholContent',
			align: 'center',
			width: '80px'
		},
		{
			title: '建议价格(¥)',
			dataIndex: 'suggestedPrice',
			align: 'right',
			width: '90px'
		},
		{
			title: '基础价格(¥/ml)',
			dataIndex: 'unitPrice',
			align: 'right',
			width: '100px'
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
			width: '150px',
			fixed: 'right'
		}
	]

	const searchFormState = reactive({
		searchKey: '',
		categoryId: undefined,
		status: undefined
	})

	const router = useRouter()
	const searchFormRef = ref()
	const tableRef = ref()
	const formRef = ref()
	const priceFormRef = ref()
	const loading = ref(false)

	// 状态数据
	const statusData = [
		{ value: 'ENABLE', label: '启用' },
		{ value: 'DISABLE', label: '禁用' }
	]

	// 分类数据
	const categoryData = ref([])

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
		return wineProductApi.productPage(searchParam).then((res) => {
			return res
		})
	}

	// 重置搜索
	const reset = () => {
		searchFormRef.value.resetFields()
		tableRef.value.refresh(true)
	}

	// 删除
	const removeProduct = (record) => {
		const param = {
			id: record.id
		}
		wineProductApi.productDelete(param).then(() => {
			tableRef.value.refresh(true)
		})
	}

	// 批量删除
	const deleteBatchProduct = (params) => {
		wineProductApi.productBatchDelete(params).then(() => {
			tableRef.value.refresh(true)
		})
	}

	// 更改状态
	const editStatus = (record) => {
		loading.value = true
		const api = record.status === 'ENABLE' ? wineProductApi.productDisable : wineProductApi.productEnable
		api({ id: record.id })
			.then(() => {
				tableRef.value.refresh()
			})
			.finally(() => {
				loading.value = false
			})
	}

	// 导出
	const exportData = () => {
		const param = Object.assign({}, searchFormState)
		wineProductApi.productExport(param).then((res) => {
			downloadUtil.resultDownload(res, '酒品产品数据.xlsx', 'application/vnd.ms-excel')
		})
	}

	// 查看详情
	const viewDetail = (record) => {
		// TODO: 实现详情查看功能
		message.info('详情功能待实现')
	}

	// 库存管理
	const manageStock = (record) => {
		// 跳转到库存管理页面，传递酒品信息
		router.push({
			path: '/wine/product/stock',
			query: {
				productId: record.id,
				productName: record.productName,
				productCode: record.productCode,
				brand: record.brand,
				categoryName: record.categoryName
			}
		})
	}

	// 价格管理
	const onPriceManage = (record) => {
		// 传递完整的酒品信息
		priceFormRef.value.onOpen({
			productId: record.id,
			productName: record.productName,
			productCode: record.productCode,
			brand: record.brand,
			categoryName: record.categoryName,
			alcoholContent: record.alcoholContent,
			volume: record.volume,
			suggestedPrice: record.suggestedPrice
		})
	}

	// 加载分类数据
	const loadCategoryData = () => {
		wineCategoryApi.categoryList().then((res) => {
			categoryData.value = res.data || []
		})
	}

	onMounted(() => {
		loadCategoryData()
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
