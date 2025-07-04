<template>
	<a-modal
		v-model:open="visible"
		title="C端用户选择"
		:width="1000"
		:mask-closable="false"
		:destroy-on-close="true"
		@ok="handleOk"
		@cancel="handleClose"
	>
		<a-row :gutter="10">
			<a-col :span="15">
				<div class="table-operator xn-mb10">
					<a-form ref="searchFormRef" name="advanced_search" class="ant-advanced-search-form" :model="searchFormState">
						<a-row :gutter="24">
							<a-col :span="10">
								<a-form-item name="searchKey">
									<a-input v-model:value="searchFormState.searchKey" placeholder="请输入用户名/账号/手机号" />
								</a-form-item>
							</a-col>
							<a-col :span="8">
								<a-form-item name="userStatus">
									<a-select v-model:value="searchFormState.userStatus" placeholder="请选择用户状态" allow-clear>
										<a-select-option value="ENABLE">启用</a-select-option>
										<a-select-option value="DISABLE">禁用</a-select-option>
									</a-select>
								</a-form-item>
							</a-col>
							<a-col :span="6">
								<a-button type="primary" class="primarySele" @click="loadData()"> 查询 </a-button>
								<a-button class="snowy-button-left" @click="reset()"> 重置 </a-button>
							</a-col>
						</a-row>
					</a-form>
				</div>
				<div class="user-table">
					<a-table
						ref="tableRef"
						size="small"
						:columns="commons"
						:data-source="tableData"
						:expand-row-by-click="true"
						:loading="pageLoading"
						bordered
						:pagination="false"
					>
						<template #title>
							<span>待选择列表 {{ tableRecordNum }} 条</span>
							<div v-if="!radioModel" class="xn-fdr">
								<a-button type="dashed" size="small" @click="addAllPageRecord">添加当前数据</a-button>
							</div>
						</template>
						<template #bodyCell="{ column, record }">
							<template v-if="column.dataIndex === 'avatar'">
								<a-avatar :src="record.avatar" style="margin-bottom: -5px; margin-top: -5px" />
							</template>
							<template v-if="column.dataIndex === 'action'">
								<a-button type="dashed" size="small" @click="addRecord(record)"><PlusOutlined /></a-button>
							</template>
							<template v-if="column.dataIndex === 'userStatus'">
								<a-tag :color="record.userStatus === 'ENABLE' ? 'green' : 'red'">
									{{ record.userStatus === 'ENABLE' ? '启用' : '禁用' }}
								</a-tag>
							</template>
							<template v-if="column.dataIndex === 'gender'">
								{{ record.gender === '男' ? '👨' : record.gender === '女' ? '👩' : '❓' }} {{ record.gender || '未知' }}
							</template>
						</template>
					</a-table>
					<div class="mt-2">
						<a-pagination
							v-if="!isEmpty(tableData)"
							v-model:current="current"
							v-model:page-size="pageSize"
							:total="total"
							size="small"
							showSizeChanger
							@change="paginationChange"
						/>
					</div>
				</div>
			</a-col>
			<a-col :span="9">
				<div class="user-table">
					<a-table
						ref="selectedTable"
						size="small"
						:columns="selectedCommons"
						:data-source="selectedData"
						:expand-row-by-click="true"
						:loading="selectedTableListLoading"
						bordered
					>
						<template #title>
							<span>已选择: {{ selectedData.length }}</span>
							<div v-if="!radioModel" class="xn-fdr">
								<a-button type="dashed" danger size="small" @click="delAllRecord">全部移除</a-button>
							</div>
						</template>
						<template #bodyCell="{ column, record }">
							<template v-if="column.dataIndex === 'avatar'">
								<a-avatar :src="record.avatar" style="margin-bottom: -5px; margin-top: -5px" />
							</template>
							<template v-if="column.dataIndex === 'action'">
								<a-button type="dashed" danger size="small" @click="delRecord(record)"><MinusOutlined /></a-button>
							</template>
						</template>
					</a-table>
				</div>
			</a-col>
		</a-row>
	</a-modal>
</template>

<script setup name="clientUserSelectorPlus">
	import { message } from 'ant-design-vue'
	import { isEmpty, remove } from 'lodash-es'
	import clientUserApi from '@/api/client/clientUserApi'

	// 弹窗是否打开
	const visible = ref(false)

	// 主表格columns - 简化显示
	const commons = [
		{
			title: '操作',
			dataIndex: 'action',
			align: 'center',
			width: 60
		},
		{
			title: '头像',
			dataIndex: 'avatar',
			width: 60
		},
		{
			title: '用户名',
			dataIndex: 'name',
			ellipsis: true,
			width: 100
		},
		{
			title: '账号',
			dataIndex: 'account',
			width: 120
		},
		{
			title: '手机号',
			dataIndex: 'phone',
			width: 120
		},
		{
			title: '状态',
			dataIndex: 'userStatus',
			width: 80
		}
	]

	// 选中表格的columns
	const selectedCommons = [
		{
			title: '操作',
			dataIndex: 'action',
			align: 'center',
			width: 60
		},
		{
			title: '头像',
			dataIndex: 'avatar',
			width: 60
		},
		{
			title: '用户名',
			dataIndex: 'name',
			ellipsis: true
		},
		{
			title: '账号',
			dataIndex: 'account'
		}
	]

	// 主表格的ref 名称
	const tableRef = ref()
	// 选中表格的ref 名称
	const selectedTable = ref()
	const tableRecordNum = ref()
	const searchFormState = ref({})
	const searchFormRef = ref()
	const pageLoading = ref(false)
	const selectedTableListLoading = ref(false)

	const emit = defineEmits({ onBack: null })
	const tableData = ref([])
	const selectedData = ref([])
	const recordIds = ref()
	const props = defineProps(['radioModel', 'dataIsConverterFlw', 'clientUserPageApi', 'checkedClientUserListApi'])

	// 是否是单选
	const radioModel = props.radioModel || false
	// 数据是否转换成工作流格式
	const dataIsConverterFlw = props.dataIsConverterFlw || false

	// 分页相关
	const current = ref(1) // 当前页数，从1开始
	const pageSize = ref(10) // 每页条数，减少显示数量
	const total = ref(0) // 数据总数

	// 打开弹框
	const showClientUserPlusModal = (ids = []) => {
		visible.value = true
		if (dataIsConverterFlw) {
			ids = goDataConverter(ids)
		}
		recordIds.value = ids
		// 初始化搜索参数
		searchFormState.value = {
			current: 1,
			size: pageSize.value
		}
		loadData()

		if (props.checkedClientUserListApi) {
			if (isEmpty(recordIds.value)) {
				return
			}
			const param = {
				idList: recordIds.value
			}
			selectedTableListLoading.value = true
			props
				.checkedClientUserListApi(param)
				.then((data) => {
					selectedData.value = data
				})
				.finally(() => {
					selectedTableListLoading.value = false
				})
		}
	}

	// 查询主表格数据
	const loadData = () => {
		pageLoading.value = true
		const api = props.clientUserPageApi || clientUserApi.userPage

		// 确保分页参数正确
		const params = {
			...searchFormState.value,
			current: current.value,
			size: pageSize.value
		}

		console.log('搜索参数：', params) // 调试用

		api(params)
			.then((data) => {
				console.log('返回数据：', data) // 调试用
				current.value = data.current
				total.value = data.total
				// 重置、赋值
				tableData.value = []
				tableRecordNum.value = 0
				tableData.value = data.records || []
				tableRecordNum.value = tableData.value.length
			})
			.catch((error) => {
				console.error('加载数据失败：', error)
				message.error('加载数据失败')
			})
			.finally(() => {
				pageLoading.value = false
			})
	}

	// pageSize改变回调分页事件
	const paginationChange = (page, size) => {
		current.value = page
		pageSize.value = size
		loadData()
	}

	const judge = () => {
		return !(radioModel && selectedData.value.length > 0)
	}

	// 添加记录
	const addRecord = (record) => {
		console.log('添加用户：', record) // 调试用

		if (!judge()) {
			message.warning('只可选择一条')
			return
		}
		const selectedRecord = selectedData.value.filter((item) => item.id === record.id)
		if (selectedRecord.length === 0) {
			selectedData.value.push(record)
			message.success(`已添加用户：${record.name}`)
		} else {
			message.warning('该记录已存在')
		}
	}

	// 添加全部
	const addAllPageRecord = () => {
		if (radioModel) {
			message.warning('单选模式不支持批量添加')
			return
		}

		let newArray = selectedData.value.concat(tableData.value)
		let list = []
		for (let item1 of newArray) {
			let flag = true
			for (let item2 of list) {
				if (item1.id === item2.id) {
					flag = false
				}
			}
			if (flag) {
				list.push(item1)
			}
		}
		selectedData.value = list
		message.success(`已添加 ${tableData.value.length} 个用户`)
	}

	// 删减记录
	const delRecord = (record) => {
		remove(selectedData.value, (item) => item.id === record.id)
		message.success(`已移除用户：${record.name}`)
	}

	// 删减记录
	const delAllRecord = () => {
		selectedData.value = []
		message.success('已移除所有用户')
	}

	// 确定
	const handleOk = () => {
		const value = []
		selectedData.value.forEach((item) => {
			const obj = {
				id: item.id,
				name: item.name,
				account: item.account,
				phone: item.phone,
				avatar: item.avatar
			}
			value.push(obj)
		})
		// 判断是否做数据的转换为工作流需要的
		if (dataIsConverterFlw) {
			emit('onBack', outDataConverter(value))
		} else {
			emit('onBack', value)
		}
		handleClose()
	}

	// 重置
	const reset = () => {
		searchFormState.value = {
			current: 1,
			size: pageSize.value
		}
		current.value = 1
		loadData()
	}

	const handleClose = () => {
		searchFormState.value = {}
		tableRecordNum.value = 0
		tableData.value = []
		current.value = 1
		pageSize.value = 10
		total.value = 0
		selectedData.value = []
		visible.value = false
	}

	// 数据进入后转换
	const goDataConverter = (data) => {
		const resultData = []
		if (data.length > 0) {
			const values = data[0].value.split(',')
			if (JSON.stringify(values) !== '[""]') {
				for (let i = 0; i < values.length; i++) {
					resultData.push(values[i])
				}
			}
		}
		return resultData
	}

	// 数据出口转换器
	const outDataConverter = (data) => {
		const obj = {}
		let label = ''
		let value = ''
		for (let i = 0; i < data.length; i++) {
			if (data.length === i + 1) {
				label = label + data[i].name
				value = value + data[i].id
			} else {
				label = label + data[i].name + ','
				value = value + data[i].id + ','
			}
		}
		obj.key = 'CLIENT_USER'
		obj.label = label
		obj.value = value
		obj.extJson = ''
		return obj
	}

	defineExpose({
		showClientUserPlusModal
	})
</script>

<style lang="less" scoped>
	.primarySele {
		margin-right: 10px;
	}
	.ant-form-item {
		margin-bottom: 0 !important;
	}
	.user-table {
		overflow: auto;
		max-height: 450px;
	}
	.snowy-button-left {
		margin-left: 8px;
	}
	.xn-fdr {
		float: right;
	}
</style>
