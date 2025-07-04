<template>
	<div>
		<a-card :bordered="false" class="xn-mb10" v-if="hasPerm('deviceInfoPage')">
			<a-form ref="searchFormRef" name="advanced_search" class="ant-advanced-search-form" :model="searchFormState">
				<a-row :gutter="24">
					<a-col :span="6">
						<a-form-item name="searchKey" label="搜索关键词">
							<a-input v-model:value="searchFormState.searchKey" placeholder="请输入设备名称、编码或位置" />
						</a-form-item>
					</a-col>
					<a-col :span="6">
						<a-form-item name="storeId" label="所属门店">
							<a-select
								v-model:value="searchFormState.storeId"
								placeholder="请选择门店"
								:getPopupContainer="(trigger) => trigger.parentNode"
								allowClear
							>
								<a-select-option v-for="item in storeData" :key="item.id" :value="item.id">
									{{ item.storeName }}
								</a-select-option>
							</a-select>
						</a-form-item>
					</a-col>
					<a-col :span="6">
						<a-form-item name="status" label="设备状态">
							<a-select
								v-model:value="searchFormState.status"
								placeholder="请选择设备状态"
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
		<a-card :bordered="false" v-if="hasPerm('deviceInfoPage')">
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
						<a-button type="primary" @click="formRef.onOpen()" v-if="hasPerm('deviceInfoAdd')">
							<template #icon><plus-outlined /></template>
							<span>新增设备</span>
						</a-button>
						<a-button @click="exportData" v-if="hasPerm('deviceInfoExport')">
							<template #icon><export-outlined /></template>
							导出
						</a-button>
						<xn-batch-button
							v-if="hasPerm('deviceInfoBatchDelete')"
							:buttonName="'批量删除'"
							icon="DeleteOutlined"
							buttonDanger
							:selectedRowKeys="selectedRowKeys"
							@batchCallBack="deleteBatchDevice"
						/>
					</a-space>
				</template>
				<template #bodyCell="{ column, record }">
					<template v-if="column.dataIndex === 'deviceCode'">
						<a-tag color="blue">{{ record.deviceCode }}</a-tag>
					</template>
					<template v-if="column.dataIndex === 'uuid'">
						<a-tag v-if="record.uuid" color="purple">{{ record.uuid }}</a-tag>
						<span v-else style="color: #999">未设置</span>
					</template>
					<template v-if="column.dataIndex === 'storeName'">
						<a-tag color="green">{{ record.storeName || '未分配' }}</a-tag>
					</template>
					<template v-if="column.dataIndex === 'currentProductName'">
						<a-tag v-if="record.currentProductName" color="orange">{{ record.currentProductName }}</a-tag>
						<span v-else style="color: #999">未绑定酒品</span>
					</template>
					<template v-if="column.dataIndex === 'status'">
						<a-badge :status="getStatusBadge(record.status)" :text="getStatusText(record.status)" />
					</template>
					<template v-if="column.dataIndex === 'action'">
						<a @click="formRef.onOpen(record)" v-if="hasPerm('deviceInfoEdit')">编辑</a>
						<a-divider type="vertical" v-if="hasPerm(['deviceInfoEdit', 'deviceInfoDelete'], 'and')" />
						<a-popconfirm title="确定要删除这个设备吗？" @confirm="removeDevice(record)">
							<a-button type="link" danger size="small" v-if="hasPerm('deviceInfoDelete')"> 删除 </a-button>
						</a-popconfirm>
						<a-divider
							type="vertical"
							v-if="hasPerm(['deviceInfoDetail', 'deviceInfoBindProduct', 'deviceInfoUpdateStatus'])"
						/>
						<a-dropdown v-if="hasPerm(['deviceInfoDetail', 'deviceInfoBindProduct', 'deviceInfoUpdateStatus'])">
							<a class="ant-dropdown-link">
								更多
								<DownOutlined />
							</a>
							<template #overlay>
								<a-menu>
									<a-menu-item v-if="hasPerm('deviceInfoDetail')">
										<a @click="viewDetail(record)">查看详情</a>
									</a-menu-item>
									<a-menu-item v-if="hasPerm('deviceInfoBindProduct')">
										<a @click="bindProduct(record)">绑定酒品</a>
									</a-menu-item>
									<a-menu-item v-if="hasPerm('deviceInfoUnbindProduct') && record.currentProductId">
										<a @click="unbindProduct(record)">解绑酒品</a>
									</a-menu-item>
									<a-menu-item v-if="hasPerm('deviceStockManage')">
										<a @click="manageStock(record)">库存管理</a>
									</a-menu-item>
									<a-menu-divider v-if="hasPerm('deviceInfoUpdateStatus')" />
									<a-menu-item v-if="hasPerm('deviceInfoUpdateStatus') && record.status !== 'ONLINE'">
										<a @click="updateStatus(record, 'ONLINE')" style="color: #52c41a">设为在线</a>
									</a-menu-item>
									<a-menu-item v-if="hasPerm('deviceInfoUpdateStatus') && record.status !== 'OFFLINE'">
										<a @click="updateStatus(record, 'OFFLINE')" style="color: #d9d9d9">设为离线</a>
									</a-menu-item>
									<a-menu-item v-if="hasPerm('deviceInfoUpdateStatus') && record.status !== 'MAINTENANCE'">
										<a @click="updateStatus(record, 'MAINTENANCE')" style="color: #faad14">设为维护</a>
									</a-menu-item>
								</a-menu>
							</template>
						</a-dropdown>
					</template>
				</template>
			</s-table>
		</a-card>

		<!-- 无权限访问提示 -->
		<a-result v-if="!hasPerm('deviceInfoPage')" status="403" title="403" sub-title="抱歉，您没有访问此页面的权限。">
			<template #extra>
				<a-button type="primary" @click="$router.go(-1)">返回</a-button>
			</template>
		</a-result>
	</div>
	<Form ref="formRef" @successful="tableRef.refresh()" />
	<ProductBindModal ref="productBindModalRef" @successful="tableRef.refresh()" />
	<StockManageModal ref="stockManageModalRef" />
</template>

<script setup name="wineDevice">
	import { message, Modal } from 'ant-design-vue'
	import { useRouter } from 'vue-router'
	import tool from '@/utils/tool'
	import downloadUtil from '@/utils/downloadUtil'
	import wineDeviceApi from '@/api/wine/wineDeviceApi'
	import wineStoreApi from '@/api/wine/wineStoreApi'
	import Form from './form.vue'
	import ProductBindModal from './productBindModal.vue'
	import StockManageModal from './stockManageModal.vue'

	const $router = useRouter()

	const columns = [
		{
			title: '设备编码',
			dataIndex: 'deviceCode',
			width: '120px'
		},
		{
			title: '设备名称',
			dataIndex: 'deviceName',
			ellipsis: true,
			width: '150px'
		},
		{
			title: '蓝牙UUID',
			dataIndex: 'uuid',
			ellipsis: true,
			width: '150px'
		},
		{
			title: '所属门店',
			dataIndex: 'storeName',
			width: '120px'
		},
		{
			title: '绑定酒品',
			dataIndex: 'currentProductName',
			width: '150px'
		},
		{
			title: '设备位置',
			dataIndex: 'location',
			ellipsis: true,
			width: '150px'
		},
		{
			title: '设备状态',
			dataIndex: 'status',
			width: '100px'
		},
		{
			title: '管理员',
			dataIndex: 'managerUserName',
			width: '100px'
		},
		{
			title: '最后在线时间',
			dataIndex: 'lastOnlineTime',
			width: '150px'
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
			fixed: 'right',
			width: '150px'
		}
	]

	let searchFormState = reactive({})
	const tableRef = ref()
	const formRef = ref()
	const productBindModalRef = ref()
	const stockManageModalRef = ref()
	const toolConfig = reactive({ refresh: true, height: true, columnSetting: true, striped: false })
	const selectedRowKeys = ref([])
	const loading = ref(false)

	// 状态数据
	const statusData = [
		{ value: 'ONLINE', label: '在线' },
		{ value: 'OFFLINE', label: '离线' },
		{ value: 'MAINTENANCE', label: '维护中' }
	]

	// 门店数据
	const storeData = ref([])

	// 表格选择配置
	const options = {
		alert: {
			show: true,
			clear: () => {
				selectedRowKeys.value = ref([])
			}
		},
		rowSelection: {
			onChange: (selectedRowKeys, selectedRows) => {
				selectedRowKeys.value = selectedRowKeys
			}
		}
	}

	// 加载数据
	const loadData = (parameter) => {
		const searchParam = JSON.parse(JSON.stringify(searchFormState))
		return wineDeviceApi.page(Object.assign(parameter, searchParam)).then((res) => {
			return res
		})
	}

	// 重置搜索
	const reset = () => {
		searchFormState = reactive({})
		tableRef.value.refresh(true)
	}

	// 删除设备
	const removeDevice = (record) => {
		let params = [{ id: record.id }]
		wineDeviceApi.delete(params).then(() => {
			tableRef.value.refresh(true)
			message.success('删除成功')
		})
	}

	// 批量删除设备
	const deleteBatchDevice = (params) => {
		wineDeviceApi.delete(params).then(() => {
			tableRef.value.refresh(true)
			message.success('删除成功')
		})
	}

	// 导出数据
	const exportData = () => {
		const searchParam = JSON.parse(JSON.stringify(searchFormState))
		message.loading('正在导出数据，请稍候...', 0)
		wineDeviceApi.export(searchParam).then((res) => {
			downloadUtil.resultDownload(res, '设备列表.xlsx', 'application/vnd.ms-excel')
			message.destroy()
		})
	}

	// 查看详情
	const viewDetail = (record) => {
		// TODO: 实现设备详情查看
		message.info('设备详情功能待实现')
	}

	// 绑定酒品
	const bindProduct = (record) => {
		productBindModalRef.value.onOpen(record)
	}

	// 解绑酒品
	const unbindProduct = (record) => {
		wineDeviceApi.unbindProduct(record.id).then(() => {
			tableRef.value.refresh()
			message.success('解绑成功')
		})
	}

	// 库存管理
	const manageStock = (record) => {
		// 打开库存管理弹窗
		stockManageModalRef.value.onOpen(record)
	}

	// 更新设备状态
	const updateStatus = (record, status) => {
		const statusText = getStatusText(status)

		// 确认操作
		const confirmMessage = `确定要将设备【${record.deviceName}】状态修改为【${statusText}】吗？`

		// 使用 Modal.confirm 进行确认
		Modal.confirm({
			title: '确认修改状态',
			content: confirmMessage,
			okText: '确定',
			cancelText: '取消',
			onOk() {
				return wineDeviceApi
					.updateStatus({
						id: record.id,
						status: status
					})
					.then(() => {
						tableRef.value.refresh()
						message.success(`设备状态已修改为【${statusText}】`)
					})
					.catch((error) => {
						console.error('更新设备状态失败:', error)
						message.error('更新设备状态失败')
					})
			}
		})
	}

	// 获取状态徽章
	const getStatusBadge = (status) => {
		const statusMap = {
			ONLINE: 'success',
			OFFLINE: 'default',
			MAINTENANCE: 'warning'
		}
		return statusMap[status] || 'default'
	}

	// 获取状态文本
	const getStatusText = (status) => {
		const statusMap = {
			ONLINE: '在线',
			OFFLINE: '离线',
			MAINTENANCE: '维护中'
		}
		return statusMap[status] || '未知'
	}

	// 格式化时间
	const formatTime = (time) => {
		return tool.formatDate(time, 'YYYY-MM-DD HH:mm:ss')
	}

	// 加载门店数据
	const loadStoreData = () => {
		wineStoreApi.selector().then((res) => {
			storeData.value = res
		})
	}

	// 页面加载时执行
	onMounted(() => {
		// 检查页面访问权限
		if (hasPerm('deviceInfoPage')) {
			loadStoreData()
		}
	})
</script>
