<template>
	<!-- 这是引入后展示的样式 -->
	<a-flex wrap="wrap" gap="small" v-if="props.userShow">
		<div
			class="user-container"
			v-for="(user, index) in userObj"
			:key="user.id"
			@mouseover="onMouseEnter(index)"
			@mouseleave="onMouseLeave(index)"
		>
			<span class="user-delete">
				<CloseCircleFilled
					:class="index === deleteShow ? 'show-delete-icon' : ''"
					class="delete-icon"
					@click="deleteUser(user)"
				/>
				<a-avatar :src="user.avatar" />
			</span>
			<span class="user-name">{{ user.name }}</span>
		</div>
		<a-button shape="circle" @click="openModal" v-if="(props.radioModel ? userObj.length !== 1 : true) && addShow">
			<PlusOutlined />
		</a-button>
		<slot name="button"></slot>
	</a-flex>

	<!-- 以下是弹窗内容 -->
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
			<a-col :span="14">
				<div class="table-operator xn-mb10">
					<a-form ref="searchFormRef" name="advanced_search" class="ant-advanced-search-form" :model="searchFormState">
						<a-row :gutter="24">
							<a-col :span="8">
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
							<a-col :span="8">
								<a-button type="primary" class="xn-mr-10" @click="loadData()"> 查询 </a-button>
								<a-button @click="reset()"> 重置 </a-button>
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
							<div v-if="!props.radioModel" class="xn-fdr">
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
			<a-col :span="10">
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
							<div v-if="!props.radioModel" class="xn-fdr">
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

<script setup name="clientUserSelector">
	import { message } from 'ant-design-vue'
	import { cloneDeep, isEmpty, remove } from 'lodash-es'
	import clientUserApi from '@/api/client/clientUserApi'

	// 弹窗是否打开
	const visible = ref(false)
	const deleteShow = ref('')

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

	const props = defineProps({
		radioModel: {
			type: Boolean,
			default: () => false
		},
		dataIsConverterFlw: {
			type: Boolean,
			default: () => false
		},
		clientUserPageApi: {
			type: Function
		},
		clientUserListByIdListApi: {
			type: Function
		},
		value: {
			default: () => ''
		},
		dataType: {
			type: String,
			default: () => 'string'
		},
		userShow: {
			type: Boolean,
			default: () => true
		},
		addShow: {
			type: Boolean,
			default: () => true
		}
	})

	// 主表格的ref 名称
	const tableRef = ref()
	// 选中表格的ref 名称
	const selectedTable = ref()
	const tableRecordNum = ref()
	const searchFormState = ref({})
	const searchFormRef = ref()
	const pageLoading = ref(false)
	const selectedTableListLoading = ref(false)

	const emit = defineEmits(['update:value', 'onBack'])
	const tableData = ref([])
	const selectedData = ref([])
	const recordIds = ref([])

	// 分页相关
	const current = ref(1) // 当前页数，从1开始
	const pageSize = ref(10) // 每页条数，减少显示数量
	const total = ref(0) // 数据总数

	// 获取选中列表的api
	const clientUserListByIdList = (param) => {
		// 确保参数格式正确：{ idList: ['id1', 'id2'] }
		const requestParam = {
			idList: Array.isArray(param.idList) ? param.idList : param.idList ? [param.idList] : []
		}

		if (typeof props.clientUserListByIdListApi === 'function') {
			return props.clientUserListByIdListApi(requestParam)
		} else {
			return clientUserApi.userListByIdList(requestParam).then((data) => {
				return Promise.resolve(data)
			})
		}
	}

	// 打开弹框
	const showClientUserPlusModal = (ids = []) => {
		const data = goDataConverter(ids)
		recordIds.value = data
		getClientUserAvatarById(data)
		openModal()
	}

	const onMouseEnter = (index) => {
		deleteShow.value = index
	}

	const onMouseLeave = (index) => {
		deleteShow.value = ''
	}

	const openModal = () => {
		if (typeof props.clientUserPageApi !== 'function') {
			message.warning('未配置选择器需要的clientUserPageApi接口')
			return
		}
		visible.value = true
		// 初始化搜索参数
		searchFormState.value = {
			current: 1,
			size: pageSize.value
		}
		current.value = 1
		loadData()

		if (isEmpty(recordIds.value)) {
			return
		}
		const param = {
			idList: Array.isArray(recordIds.value) ? recordIds.value : [recordIds.value]
		}
		selectedTableListLoading.value = true
		clientUserListByIdList(param)
			.then((data) => {
				selectedData.value = data
			})
			.finally(() => {
				selectedTableListLoading.value = false
			})
	}

	// 点击头像删除用户
	const deleteUser = (user) => {
		// 删除显示的
		remove(userObj.value, (item) => item.id === user.id)
		// 删除缓存的
		remove(recordIds.value, (item) => item === user.id)
		const value = []
		const showUser = []
		userObj.value.forEach((item) => {
			const obj = {
				id: item.id,
				name: item.name
			}
			value.push(item.id)
			// 拷贝一份obj数据
			const objClone = cloneDeep(obj)
			objClone.avatar = item.avatar
			showUser.push(objClone)
		})
		userObj.value = showUser
		// 判断是否做数据的转换为工作流需要的
		const resultData = outDataConverter(value)
		emit('update:value', resultData)
		emit('onBack', resultData)
	}

	// 查询主表格数据
	const loadData = () => {
		pageLoading.value = true

		// 确保分页参数正确
		const params = {
			...searchFormState.value,
			current: current.value,
			size: pageSize.value
		}

		console.log('搜索参数：', params) // 调试用

		// 使用传入的API或默认API
		const api = props.clientUserPageApi || clientUserApi.userPage

		api(params)
			.then((data) => {
				console.log('返回数据：', data) // 调试用
				if (data && typeof data === 'object') {
					current.value = data.current || 1
					total.value = data.total || 0
					// 重置、赋值
					tableData.value = data.records || []
					tableRecordNum.value = tableData.value.length
				} else {
					message.error('数据格式错误')
					tableData.value = []
					tableRecordNum.value = 0
				}
			})
			.catch((error) => {
				message.error('加载数据失败: ' + (error.message || '未知错误'))
				tableData.value = []
				tableRecordNum.value = 0
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
		return !(props.radioModel && selectedData.value.length > 0)
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
		if (props.radioModel) {
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

	const userObj = ref([])

	// 确定
	const handleOk = () => {
		userObj.value = []
		const value = []
		const showUser = []
		selectedData.value.forEach((item) => {
			const obj = {
				id: item.id,
				name: item.name
			}
			value.push(item.id)
			// 拷贝一份obj数据
			const objClone = cloneDeep(obj)
			objClone.avatar = item.avatar
			objClone.account = item.account
			objClone.phone = item.phone
			showUser.push(objClone)
		})
		userObj.value = showUser
		// 判断是否做数据的转换为工作流需要的
		const resultData = outDataConverter(value)
		emit('update:value', resultData)
		emit('onBack', resultData)
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
		if (props.dataIsConverterFlw) {
			const resultData = []
			// 处理对象
			if (!isEmpty(data.value)) {
				const values = data.value.split(',')
				if (values.length > 0) {
					values.forEach((id) => {
						resultData.push(id)
					})
				} else {
					resultData.push(data.value)
				}
			} else {
				// 处理数组
				if (!isEmpty(data) && !isEmpty(data[0]) && !isEmpty(data[0].value)) {
					const values = data[0].value.split(',')
					for (let i = 0; i < values.length; i++) {
						resultData.push(values[i])
					}
				}
			}
			return resultData
		} else {
			// 如果data为空或undefined，返回空数组
			if (isEmpty(data) || data === undefined || data === null) {
				return []
			}

			// 如果是数组，直接返回
			if (Array.isArray(data)) {
				return data
			}

			// 如果是字符串
			if (typeof data === 'string') {
				// 如果包含逗号，说明是多个ID
				if (data.includes(',')) {
					return data.split(',').filter((id) => id.trim() !== '')
				} else {
					// 单个ID，返回数组格式
					return [data]
				}
			}

			// 其他情况，尝试转为数组
			return Array.isArray(data) ? data : [data]
		}
	}

	// 数据出口转换器
	const outDataConverter = (data) => {
		if (props.dataIsConverterFlw) {
			data = userObj.value
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
		} else {
			// 如果数据为空，返回空字符串
			if (isEmpty(data)) {
				return ''
			}

			// 单选模式，返回单个ID字符串
			if (props.radioModel) {
				return Array.isArray(data) ? data[0] || '' : data
			}

			// 多选模式，返回逗号分隔的字符串
			if (Array.isArray(data)) {
				return data.join(',')
			}

			// 其他情况，直接返回
			return data
		}
	}

	// 获取数据类型
	const getValueType = () => {
		if (props.dataType) {
			return props.dataType
		} else {
			if (props.radioModel) {
				return 'string'
			}
			return typeof typeof props.value
		}
	}

	const getClientUserAvatarById = (ids) => {
		if (isEmpty(userObj.value) && !isEmpty(ids)) {
			const param = {
				idList: Array.isArray(ids) ? ids : [ids]
			}
			// 这里必须转为数组类型的
			clientUserListByIdList(param).then((data) => {
				userObj.value = data
			})
		}
	}

	watch(
		() => props.value,
		(newValue) => {
			if (!isEmpty(props.value)) {
				const ids = goDataConverter(newValue)
				recordIds.value = ids
				getClientUserAvatarById(ids)
			} else {
				userObj.value = []
				selectedData.value = []
			}
		},
		{
			immediate: true // 立即执行
		}
	)

	defineExpose({
		showClientUserPlusModal
	})
</script>

<style lang="less" scoped>
	.xn-mr-5 {
		margin-right: 5px;
	}
	.xn-mr-10 {
		margin-right: 10px;
	}
	.ant-form-item {
		margin-bottom: 0 !important;
	}
	.user-table {
		overflow: auto;
		max-height: 450px;
	}

	.user-container {
		display: flex;
		align-items: center; /* 垂直居中 */
		flex-direction: column;
		margin-right: 10px;
		text-align: center;
	}
	.user-avatar {
		width: 30px;
		border-radius: 50%; /* 设置为50%以创建圆形头像 */
	}
	.user-name {
		font-size: 12px;
		max-width: 50px;
		white-space: nowrap;
		overflow: hidden;
	}
	.user-delete {
		z-index: 99;
		color: rgba(0, 0, 0, 0.25);
		position: relative;
		display: flex;
		flex-direction: column;
	}
	.delete-icon {
		position: absolute;
		right: -2px;
		z-index: 5;
		top: -3px;
		cursor: pointer;
		visibility: hidden;
	}
	.show-delete-icon {
		visibility: visible;
	}
	.xn-fdr {
		float: right;
	}
</style>
