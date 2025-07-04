<template>
	<div>
		<a-card :bordered="false" class="xn-mb10">
			<a-form ref="searchFormRef" name="advanced_search" class="ant-advanced-search-form" :model="searchFormState">
				<a-row :gutter="24">
					<a-col :span="6">
						<a-form-item name="searchKey" label="搜索关键词">
							<a-input v-model:value="searchFormState.searchKey" placeholder="请输入分类名称或描述" />
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
					<a-col :span="12">
						<a-button type="primary" @click="tableRef.refresh(true)">
							<template #icon><SearchOutlined /></template>
							搜索
						</a-button>
						<a-button class="snowy-button-left" @click="reset">
							<template #icon><redo-outlined /></template>
							重置
						</a-button>
						<a-button class="snowy-button-left" @click="expandAll">
							<template #icon><ExpandOutlined /></template>
							展开全部
						</a-button>
						<a-button class="snowy-button-left" @click="collapseAll">
							<template #icon><ShrinkOutlined /></template>
							收起全部
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
				:show-pagination="false"
				bordered
				:alert="options.alert.show"
				:tool-config="toolConfig"
				:row-key="(record) => record.id"
				:row-selection="options.rowSelection"
				:default-expand-all-rows="false"
				:expanded-row-keys="expandedRowKeys"
				@onExpand="onExpand"
			>
				<template #operator class="table-operator">
					<a-space>
						<a-button type="primary" @click="formRef.onOpen()" v-if="hasPerm('wineCategoryAdd')">
							<template #icon><plus-outlined /></template>
							<span>新增分类</span>
						</a-button>
						<a-button @click="exportData" v-if="hasPerm('wineCategoryExport')">
							<template #icon><export-outlined /></template>
							导出
						</a-button>
						<xn-batch-button
							v-if="hasPerm('wineCategoryBatchDelete')"
							:buttonName="'批量删除'"
							icon="DeleteOutlined"
							buttonDanger
							:selectedRowKeys="selectedRowKeys"
							@batchCallBack="deleteBatchCategory"
						/>
					</a-space>
				</template>
				<template #bodyCell="{ column, record }">
					<template v-if="column.dataIndex === 'categoryName'">
						<div style="display: flex; align-items: center">
							<a-avatar v-if="record.icon" :src="record.icon" :size="24" style="margin-right: 8px" />
							<FolderOutlined v-else style="margin-right: 8px; color: #1890ff" />
							<span>{{ record.categoryName }}</span>
						</div>
					</template>
					<template v-if="column.dataIndex === 'categoryLevel'">
						<a-tag :color="getLevelColor(record.categoryLevel)">
							{{ getLevelText(record.categoryLevel) }}
						</a-tag>
					</template>
					<template v-if="column.dataIndex === 'status'">
						<a-switch
							:loading="loading"
							:checked="record.status === 'ENABLE'"
							@change="editStatus(record)"
							v-if="hasPerm('wineCategoryUpdateStatus')"
						/>
						<a-tag v-else :color="record.status === 'ENABLE' ? 'green' : 'red'">
							{{ record.status === 'ENABLE' ? '启用' : '禁用' }}
						</a-tag>
					</template>
					<template v-if="column.dataIndex === 'sortCode'">
						<a-input-number
							v-model:value="record.sortCode"
							:min="1"
							:max="9999"
							size="small"
							@change="updateSort(record)"
							v-if="hasPerm('wineCategorySort')"
						/>
						<span v-else>{{ record.sortCode }}</span>
					</template>
					<template v-if="column.dataIndex === 'action'">
						<a @click="formRef.onOpen(record, record.id)" v-if="hasPerm('wineCategoryEdit')">编辑</a>
						<a-divider type="vertical" v-if="hasPerm('wineCategoryEdit') && hasPerm('wineCategoryAdd')" />
						<a @click="formRef.onOpen(undefined, record.id)" v-if="hasPerm('wineCategoryAdd')">添加子分类</a>
						<a-divider type="vertical" v-if="hasPerm('wineCategoryAdd') && hasPerm('wineCategoryDelete')" />
						<a-popconfirm title="确定要删除这个分类吗？删除后其子分类也会被删除！" @confirm="removeCategory(record)">
							<a-button type="link" danger size="small" v-if="hasPerm('wineCategoryDelete')"> 删除 </a-button>
						</a-popconfirm>
						<a-divider type="vertical" v-if="hasPerm('wineCategoryDelete') && hasPerm('wineCategoryMove')" />
						<a-dropdown v-if="hasPerm('wineCategoryMove') || hasPerm('wineCategoryDetail')">
							<a class="ant-dropdown-link">
								更多
								<DownOutlined />
							</a>
							<template #overlay>
								<a-menu>
									<a-menu-item v-if="hasPerm('wineCategoryMove')">
										<a @click="moveCategory(record)">移动分类</a>
									</a-menu-item>
									<a-menu-item v-if="hasPerm('wineCategoryDetail')">
										<a @click="viewDetail(record)">查看详情</a>
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

<script setup name="wineCategory">
	import { message } from 'ant-design-vue'
	import downloadUtil from '@/utils/downloadUtil'
	import wineCategoryApi from '@/api/wine/wineCategoryApi'
	import Form from './form.vue'

	const columns = [
		{
			title: '分类名称',
			dataIndex: 'categoryName',
			width: '200px'
		},
		{
			title: '分类编码',
			dataIndex: 'categoryCode',
			width: '120px'
		},
		{
			title: '层级',
			dataIndex: 'categoryLevel',
			width: '80px',
			align: 'center'
		},
		{
			title: '排序',
			dataIndex: 'sortCode',
			width: '100px',
			align: 'center'
		},
		{
			title: '状态',
			dataIndex: 'status',
			align: 'center',
			width: '80px'
		},
		{
			title: '描述',
			dataIndex: 'description',
			ellipsis: true
		},
		{
			title: '创建时间',
			dataIndex: 'createTime',
			width: '150px'
		},
		{
			title: '操作',
			dataIndex: 'action',
			align: 'center',
			width: '200px',
			fixed: 'right'
		}
	]

	const searchFormState = reactive({
		searchKey: '',
		status: undefined
	})

	const searchFormRef = ref()
	const tableRef = ref()
	const formRef = ref()
	const loading = ref(false)

	// 展开的行
	const expandedRowKeys = ref([])

	// 状态数据
	const statusData = [
		{ value: 'ENABLE', label: '启用' },
		{ value: 'DISABLE', label: '禁用' }
	]

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
		return wineCategoryApi.categoryTree(searchParam).then((res) => {
			console.log(res)
			return res || []
		})
	}

	// 重置搜索
	const reset = () => {
		searchFormRef.value.resetFields()
		tableRef.value.refresh(true)
	}

	// 删除
	const removeCategory = (record) => {
		const param = {
			id: record.id
		}
		wineCategoryApi.categoryDelete(param).then(() => {
			tableRef.value.refresh(true)
		})
	}

	// 批量删除
	const deleteBatchCategory = (params) => {
		wineCategoryApi.categoryBatchDelete(params).then(() => {
			tableRef.value.refresh(true)
		})
	}

	// 更改状态
	const editStatus = (record) => {
		loading.value = true
		const api = record.status === 'ENABLE' ? wineCategoryApi.categoryDisable : wineCategoryApi.categoryEnable
		api({ id: record.id })
			.then(() => {
				tableRef.value.refresh()
			})
			.finally(() => {
				loading.value = false
			})
	}

	// 更新排序
	const updateSort = (record) => {
		wineCategoryApi
			.categorySort({
				id: record.id,
				sortCode: record.sortCode
			})
			.catch(() => {
				// 恢复原值
				tableRef.value.refresh()
			})
	}

	// 导出
	const exportData = () => {
		const param = Object.assign({}, searchFormState)
		wineCategoryApi.categoryExport(param).then((res) => {
			downloadUtil.resultDownload(res, '酒品分类数据.xlsx', 'application/vnd.ms-excel')
		})
	}

	// 展开全部
	const expandAll = () => {
		// 获取所有数据的key
		const getAllKeys = (data) => {
			let keys = []
			data.forEach((item) => {
				keys.push(item.id)
				if (item.children && item.children.length > 0) {
					keys = keys.concat(getAllKeys(item.children))
				}
			})
			return keys
		}

		// 重新加载数据来获取所有节点
		wineCategoryApi.categoryTree(searchFormState).then((data) => {
			expandedRowKeys.value = getAllKeys(data || [])
		})
	}

	// 收起全部
	const collapseAll = () => {
		expandedRowKeys.value = []
	}

	// 展开/收起行
	const onExpand = (expanded, record) => {
		if (expanded) {
			if (!expandedRowKeys.value.includes(record.id)) {
				expandedRowKeys.value.push(record.id)
			}
		} else {
			const index = expandedRowKeys.value.indexOf(record.id)
			if (index > -1) {
				expandedRowKeys.value.splice(index, 1)
			}
		}
	}

	// 移动分类
	const moveCategory = (record) => {
		// TODO: 实现移动分类功能
		message.info('移动分类功能待实现')
	}

	// 查看详情
	const viewDetail = (record) => {
		// TODO: 实现详情查看功能
		message.info('详情功能待实现')
	}

	// 获取层级颜色
	const getLevelColor = (categoryLevel) => {
		const colors = ['blue', 'green', 'orange', 'red', 'purple']
		return colors[categoryLevel - 1] || 'default'
	}

	// 获取层级文本
	const getLevelText = (categoryLevel) => {
		const texts = ['一级', '二级', '三级', '四级', '五级']
		return texts[categoryLevel - 1] || `${categoryLevel}级`
	}
</script>

<style scoped>
	.xn-mb10 {
		margin-bottom: 10px;
	}
	.snowy-button-left {
		margin-left: 8px;
	}
</style>
