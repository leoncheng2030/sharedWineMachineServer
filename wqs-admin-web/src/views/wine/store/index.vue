<template>
	<div>
		<a-card :bordered="false" class="xn-mb10">
			<a-form ref="searchFormRef" name="advanced_search" class="ant-advanced-search-form" :model="searchFormData">
				<a-row :gutter="24">
					<a-col :span="6">
						<a-form-item name="searchKey" label="搜索关键词">
							<a-input v-model:value="searchFormData.searchKey" placeholder="请输入门店名称、编码或地址" />
						</a-form-item>
					</a-col>
					<a-col :span="6">
						<a-form-item name="status" label="门店状态">
							<a-select
								v-model:value="searchFormData.status"
								placeholder="请选择门店状态"
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
						<a-form-item label="所在地区" name="cityInfo">
							<city-selector
								v-model:value="searchFormData.cityInfo"
								placeholder="请选择省份、城市、区县"
								@change="handleCitySearch"
							/>
						</a-form-item>
					</a-col>
					<a-col :span="6">
						<a-space>
							<a-button type="primary" @click="tableRef.refresh(true)">
								<template #icon><SearchOutlined /></template>
								搜索
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
						<a-button type="primary" @click="formRef.onOpen()" v-if="hasPerm('storeManageAdd')">
							<template #icon><plus-outlined /></template>
							<span>新增门店</span>
						</a-button>
						<a-button @click="exportData" v-if="hasPerm('storeInfoExport')">
							<template #icon><export-outlined /></template>
							导出
						</a-button>
						<xn-batch-button
							v-if="hasPerm('storeInfoBatchDelete')"
							:buttonName="'批量删除'"
							icon="DeleteOutlined"
							buttonDanger
							:selectedRowKeys="selectedRowKeys"
							@batchCallBack="deleteBatchStore"
						/>
					</a-space>
				</template>
				<template #bodyCell="{ column, record }">
					<template v-if="column.dataIndex === 'storeCode'">
						<a-tag color="blue">{{ record.storeCode }}</a-tag>
					</template>
					<template v-if="column.dataIndex === 'managerName'">
						<span v-if="record.managerName">{{ record.managerName }}</span>
						<span v-else style="color: #999">未分配</span>
					</template>
					<template v-if="column.dataIndex === 'priceAuthority'">
						<a-tag v-if="record.priceAuthority === 'PLATFORM'" color="blue">平台统一</a-tag>
						<a-tag v-else-if="record.priceAuthority === 'CUSTOM'" color="orange">门店自定义</a-tag>
						<span v-else style="color: #999">未设置</span>
					</template>
					<template v-if="column.dataIndex === 'address'">
						<span v-if="record.province || record.city || record.district || record.detailAddress">
							{{ [record.province, record.city, record.district, record.detailAddress].filter(Boolean).join(' ') }}
						</span>
						<span v-else style="color: #999">未设置地址</span>
					</template>
					<template v-if="column.dataIndex === 'contact'">
						<div v-if="record.contactPhone || record.contactEmail">
							<div v-if="record.contactPhone">📱 {{ record.contactPhone }}</div>
							<div v-if="record.contactEmail">📧 {{ record.contactEmail }}</div>
						</div>
						<span v-else style="color: #999">未设置</span>
					</template>
					<template v-if="column.dataIndex === 'coordinates'">
						<div v-if="record.longitude && record.latitude" class="coordinate-info">
							<div>经度: {{ record.longitude }}</div>
							<div>纬度: {{ record.latitude }}</div>
						</div>
						<span v-else style="color: #999">未设置坐标</span>
					</template>
					<template v-if="column.dataIndex === 'status'">
						<a-switch
							:loading="loading"
							:checked="record.status === 'ENABLE'"
							@change="editStatus(record)"
							v-if="hasPerm('storeInfoUpdateStatus')"
						/>
						<a-tag v-else :color="record.status === 'ENABLE' ? 'green' : 'red'">
							{{ record.status === 'ENABLE' ? '营业中' : '已停业' }}
						</a-tag>
					</template>
					<template v-if="column.dataIndex === 'action'">
						<a @click="formRef.onOpen(record)" v-if="hasPerm('storeManageEdit')">编辑</a>
						<a-divider type="vertical" v-if="hasPerm(['storeManageEdit', 'storeManageDelete'], 'and')" />
						<a-popconfirm title="确定要删除这个门店吗？" @confirm="removeStore(record)">
							<a-button type="link" danger size="small" v-if="hasPerm('storeManageDelete')"> 删除 </a-button>
						</a-popconfirm>
						<a-divider
							type="vertical"
							v-if="hasPerm(['storeManageDetail', 'storeInfoDevices', 'storeManageSetPriceAuthority'])"
						/>
						<a-dropdown v-if="hasPerm(['storeInfoDetail', 'storeInfoDevices', 'storeManageSetPriceAuthority'])">
							<a class="ant-dropdown-link">
								更多
								<DownOutlined />
							</a>
							<template #overlay>
								<a-menu>
									<a-menu-item v-if="hasPerm('storeInfoDetail')">
										<a @click="viewDetail(record)">查看详情</a>
									</a-menu-item>
									<a-menu-item v-if="hasPerm('storeInfoDevices')">
										<a @click="viewDevices(record)">查看设备</a>
									</a-menu-item>
									<a-menu-divider v-if="hasPerm('storeManageSetPriceAuthority')" />
									<a-menu-item v-if="hasPerm('storeManageSetPriceAuthority')">
										<a @click="setPriceAuthority(record)" style="color: #1890ff">设置定价权限</a>
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

<script setup name="wineStore">
	import { message, Modal } from 'ant-design-vue'
	import downloadUtil from '@/utils/downloadUtil'
	import wineStoreApi from '@/api/wine/wineStoreApi'
	import CitySelector from '@/components/CitySelector/index.vue'
	import Form from './form.vue'

	const columns = [
		{
			title: '门店编码',
			dataIndex: 'storeCode',
			width: '80px'
		},
		{
			title: '门店名称',
			dataIndex: 'storeName',
			ellipsis: true,
			width: '150px'
		},
		{
			title: '门店管理员',
			dataIndex: 'storeManagerUserName',
			width: '100px'
		},
		{
			title: '定价权限',
			dataIndex: 'priceAuthority',
			width: '80px'
		},
		{
			title: '门店地址',
			dataIndex: 'address',
			ellipsis: true,
			width: '200px'
		},
		{
			title: '联系方式',
			dataIndex: 'contact',
			width: '180px'
		},
		{
			title: '坐标信息',
			dataIndex: 'coordinates',
			width: '140px'
		},
		{
			title: '营业时间',
			dataIndex: 'businessHours',
			width: '120px'
		},
		{
			title: '门店状态',
			dataIndex: 'status',
			width: '100px'
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

	let searchFormData = ref({
		searchKey: '',
		status: '',
		cityInfo: {
			province: '',
			city: '',
			district: ''
		},
		storeManagerId: ''
	})
	const tableRef = ref()
	const formRef = ref()
	const toolConfig = reactive({ refresh: true, height: true, columnSetting: true, striped: false })
	const selectedRowKeys = ref([])
	const loading = ref(false)

	// 状态数据
	const statusData = [
		{ value: 'ENABLE', label: '营业中' },
		{ value: 'DISABLE', label: '已停业' }
	]

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

	// 加载数据 - 使用STable标准格式
	const loadData = (parameter) => {
		// 构建查询参数
		const searchParam = JSON.parse(JSON.stringify(searchFormData.value))

		// 将cityInfo展开到对应的搜索字段
		if (searchParam.cityInfo) {
			searchParam.province = searchParam.cityInfo.province
			searchParam.city = searchParam.cityInfo.city
			searchParam.district = searchParam.cityInfo.district
			// 移除cityInfo字段
			delete searchParam.cityInfo
		}

		return wineStoreApi.page(Object.assign(parameter, searchParam)).then((res) => {
			return res
		})
	}

	// 城市搜索变化回调
	const handleCitySearch = (cityInfo) => {
		if (cityInfo) {
			searchFormData.value.cityInfo = {
				province: cityInfo.province || '',
				city: cityInfo.city || '',
				district: cityInfo.district || ''
			}
		} else {
			searchFormData.value.cityInfo = {
				province: '',
				city: '',
				district: ''
			}
		}
		// 自动触发搜索
		tableRef.value.refresh(true)
	}

	// 重置搜索
	const reset = () => {
		searchFormData.value = {
			searchKey: '',
			status: '',
			cityInfo: {
				province: '',
				city: '',
				district: ''
			},
			storeManagerId: ''
		}
		tableRef.value.refresh(true)
	}

	// 删除门店
	const removeStore = (record) => {
		let params = [{ id: record.id }]
		wineStoreApi.delete(params).then(() => {
			tableRef.value.refresh(true)
			message.success('删除成功')
		})
	}

	// 批量删除门店
	const deleteBatchStore = (params) => {
		wineStoreApi.delete(params).then(() => {
			tableRef.value.refresh(true)
			message.success('删除成功')
		})
	}

	// 更改状态
	const editStatus = (record) => {
		loading.value = true
		const api = record.status === 'ENABLE' ? wineStoreApi.disable : wineStoreApi.enable
		api({ id: record.id })
			.then(() => {
				tableRef.value.refresh()
				message.success('状态更新成功')
			})
			.finally(() => {
				loading.value = false
			})
	}

	// 导出数据
	const exportData = () => {
		const searchParam = JSON.parse(JSON.stringify(searchFormData.value))
		message.loading('正在导出数据，请稍候...', 0)
		wineStoreApi.export(searchParam).then((res) => {
			downloadUtil.resultDownload(res, '门店列表.xlsx', 'application/vnd.ms-excel')
			message.destroy()
		})
	}

	// 查看详情
	const viewDetail = (record) => {
		// TODO: 实现门店详情查看
		message.info('门店详情功能待实现')
	}

	// 查看设备
	const viewDevices = (record) => {
		// TODO: 跳转到设备管理页面，筛选该门店的设备
		message.info('查看门店设备功能待实现')
	}

	// 设置定价权限
	const setPriceAuthority = (record) => {
		const currentAuthority = record.priceAuthority
		const newAuthority = currentAuthority === 'PLATFORM' ? 'CUSTOM' : 'PLATFORM'
		const authorityText = newAuthority === 'PLATFORM' ? '平台统一定价' : '门店自定义定价'

		// 使用 Modal.confirm 进行确认
		Modal.confirm({
			title: '确认修改定价权限',
			content: `确定要将门店【${record.storeName}】的定价权限修改为【${authorityText}】吗？`,
			okText: '确定',
			cancelText: '取消',
			onOk() {
				return wineStoreApi
					.setPriceAuthority({
						id: record.id,
						priceAuthority: newAuthority
					})
					.then(() => {
						tableRef.value.refresh()
						message.success(`定价权限已修改为【${authorityText}】`)
					})
					.catch((error) => {
						console.error('设置定价权限失败:', error)
						message.error('设置定价权限失败')
					})
			}
		})
	}
</script>

<style scoped>
	.coordinate-info {
		font-size: 12px;
		line-height: 1.4;
	}

	.coordinate-info div {
		white-space: nowrap;
		overflow: hidden;
		text-overflow: ellipsis;
	}
</style>
