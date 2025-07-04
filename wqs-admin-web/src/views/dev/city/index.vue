<template>
	<div>
		<a-card :bordered="false" class="xn-mb10">
			<a-form ref="searchFormRef" name="advanced_search" class="ant-advanced-search-form" :model="searchFormState">
				<a-row :gutter="24">
					<a-col :span="6">
						<a-form-item name="searchKey" label="搜索关键词">
							<a-input v-model:value="searchFormState.searchKey" placeholder="请输入城市名称、编码或拼音" />
						</a-form-item>
					</a-col>
					<a-col :span="6">
						<a-form-item name="level" label="城市层级">
							<a-select
								v-model:value="searchFormState.level"
								placeholder="请选择城市层级"
								:getPopupContainer="(trigger) => trigger.parentNode"
								allowClear
							>
								<a-select-option value="1">省份</a-select-option>
								<a-select-option value="2">城市</a-select-option>
								<a-select-option value="3">区县</a-select-option>
							</a-select>
						</a-form-item>
					</a-col>
					<a-col :span="6">
						<a-form-item name="status" label="城市状态">
							<a-select
								v-model:value="searchFormState.status"
								placeholder="请选择城市状态"
								:getPopupContainer="(trigger) => trigger.parentNode"
								allowClear
							>
								<a-select-option value="ENABLE">启用</a-select-option>
								<a-select-option value="DISABLE">禁用</a-select-option>
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
						<a-button type="primary" @click="formRef.onOpen()" v-if="hasPerm('/dev/city/add')">
							<template #icon><plus-outlined /></template>
							<span>新增城市</span>
						</a-button>
						<a-button @click="exportData" v-if="hasPerm('/dev/city/export')">
							<template #icon><export-outlined /></template>
							导出
						</a-button>
						<xn-batch-button
							v-if="hasPerm('/dev/city/batchDelete')"
							:buttonName="'批量删除'"
							icon="DeleteOutlined"
							buttonDanger
							:selectedRowKeys="selectedRowKeys"
							@batchCallBack="deleteBatchCity"
						/>
					</a-space>
				</template>
				<template #bodyCell="{ column, record }">
					<template v-if="column.dataIndex === 'cityCode'">
						<a-tag color="blue">{{ record.cityCode }}</a-tag>
					</template>
					<template v-if="column.dataIndex === 'level'">
						<a-tag v-if="record.level === 1" color="red">省份</a-tag>
						<a-tag v-else-if="record.level === 2" color="orange">城市</a-tag>
						<a-tag v-else-if="record.level === 3" color="green">区县</a-tag>
						<span v-else style="color: #999">未知</span>
					</template>
					<template v-if="column.dataIndex === 'isHot'">
						<a-tag v-if="record.isHot === 'Y'" color="red">热门</a-tag>
						<span v-else style="color: #999">普通</span>
					</template>
					<template v-if="column.dataIndex === 'supportDelivery'">
						<a-tag v-if="record.supportDelivery === 'Y'" color="green">支持配送</a-tag>
						<a-tag v-else color="orange">不支持配送</a-tag>
					</template>
					<template v-if="column.dataIndex === 'location'">
						<span v-if="record.longitude && record.latitude"> {{ record.longitude }}, {{ record.latitude }} </span>
						<span v-else style="color: #999">未设置</span>
					</template>
					<template v-if="column.dataIndex === 'statistics'">
						<div>
							<div>门店: {{ record.storeCount || 0 }}</div>
							<div>设备: {{ record.deviceCount || 0 }}</div>
							<div>订单: {{ record.orderCount || 0 }}</div>
						</div>
					</template>
					<template v-if="column.dataIndex === 'status'">
						<a-switch
							:loading="loading"
							:checked="record.status === 'ENABLE'"
							@change="editStatus(record)"
							v-if="hasPerm('/dev/city/enable')"
						/>
						<a-tag v-else :color="record.status === 'ENABLE' ? 'green' : 'red'">
							{{ record.status === 'ENABLE' ? '启用' : '禁用' }}
						</a-tag>
					</template>
					<template v-if="column.dataIndex === 'action'">
						<a @click="formRef.onOpen(record)" v-if="hasPerm('/dev/city/edit')">编辑</a>
						<a-divider type="vertical" v-if="hasPerm(['/dev/city/edit', '/dev/city/delete'], 'and')" />
						<a-popconfirm title="确定要删除这个城市吗？" @confirm="removeCity(record)">
							<a-button type="link" danger size="small" v-if="hasPerm('/dev/city/delete')"> 删除 </a-button>
						</a-popconfirm>
						<a-divider
							type="vertical"
							v-if="
								hasPerm([
									'/dev/city/detail',
									'/dev/city/setHotCity',
									'/dev/city/setSupportDelivery',
									'/dev/city/updateStatistics'
								])
							"
						/>
						<a-dropdown
							v-if="
								hasPerm([
									'/dev/city/detail',
									'/dev/city/setHotCity',
									'/dev/city/setSupportDelivery',
									'/dev/city/updateStatistics'
								])
							"
						>
							<a class="ant-dropdown-link">
								更多
								<DownOutlined />
							</a>
							<template #overlay>
								<a-menu>
									<a-menu-item v-if="hasPerm('/dev/city/detail')">
										<a @click="viewDetail(record)">查看详情</a>
									</a-menu-item>
									<a-menu-divider />
									<a-menu-item v-if="hasPerm('/dev/city/setHotCity')">
										<a @click="setHotCity(record)" :style="{ color: record.isHot === 'Y' ? '#ff4d4f' : '#1890ff' }">
											{{ record.isHot === 'Y' ? '取消热门' : '设为热门' }}
										</a>
									</a-menu-item>
									<a-menu-item v-if="hasPerm('/dev/city/setSupportDelivery')">
										<a
											@click="setSupportDelivery(record)"
											:style="{ color: record.supportDelivery === 'Y' ? '#ff4d4f' : '#52c41a' }"
										>
											{{ record.supportDelivery === 'Y' ? '取消配送' : '支持配送' }}
										</a>
									</a-menu-item>
									<a-menu-divider v-if="hasPerm('/dev/city/updateStatistics')" />
									<a-menu-item v-if="hasPerm('/dev/city/updateStatistics')">
										<a @click="updateStatistics(record)" style="color: #722ed1">更新统计数据</a>
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

<script setup name="wineCity">
	import { message, Modal } from 'ant-design-vue'
	import downloadUtil from '@/utils/downloadUtil'
	import wineCityApi from '@/api/dev/wineCityApi'
	import Form from './form.vue'

	const columns = [
		{
			title: '城市编码',
			dataIndex: 'cityCode',
			width: '120px'
		},
		{
			title: '城市名称',
			dataIndex: 'cityName',
			ellipsis: true,
			width: '150px'
		},
		{
			title: '城市层级',
			dataIndex: 'level',
			width: '100px'
		},
		{
			title: '省份编码',
			dataIndex: 'provinceCode',
			width: '120px'
		},
		{
			title: '父级名称',
			dataIndex: 'parentCityName',
			ellipsis: true,
			width: '150px'
		},
		{
			title: '是否热门',
			dataIndex: 'isHot',
			width: '100px'
		},
		{
			title: '配送支持',
			dataIndex: 'supportDelivery',
			width: '120px'
		},
		{
			title: '地理位置',
			dataIndex: 'location',
			ellipsis: true,
			width: '150px'
		},
		{
			title: '统计数据',
			dataIndex: 'statistics',
			width: '120px'
		},
		{
			title: '城市状态',
			dataIndex: 'status',
			width: '100px'
		},
		{
			title: '操作',
			dataIndex: 'action',
			align: 'center',
			width: '150px'
		}
	]

	const selectedRowKeys = ref([])
	// 列表选择配置
	const options = {
		alert: {
			show: false,
			clear: () => {
				selectedRowKeys.value = ref([])
			}
		},
		rowSelection: {
			onChange: (selectedRowKey, selectedRows) => {
				selectedRowKeys.value = selectedRowKey
			}
		}
	}
	// 表格工具栏配置
	const toolConfig = {
		refresh: true,
		height: true,
		columnSetting: true,
		striped: false
	}
	// 定义tableRef和searchFormRef
	const tableRef = ref()
	const searchFormRef = ref()
	const formRef = ref()

	// 表单数据
	const searchFormState = reactive({})
	const loading = ref(false)

	onMounted(() => {
		tableRef.value.refresh(true)
	})

	// 查询
	const loadData = (parameter) => {
		const searchFormParam = JSON.parse(JSON.stringify(searchFormState))
		return wineCityApi.page(Object.assign(parameter, searchFormParam)).then((data) => {
			return data
		})
	}
	// 重置
	const reset = () => {
		searchFormRef.value.resetFields()
		tableRef.value.refresh(true)
	}
	// 删除
	const removeCity = (record) => {
		let params = [
			{
				id: record.id
			}
		]
		wineCityApi.batchDelete(params).then(() => {
			tableRef.value.refresh(true)
			message.success('删除成功')
		})
	}
	// 批量删除
	const deleteBatchCity = (params) => {
		wineCityApi.batchDelete(params).then(() => {
			tableRef.value.refresh(true)
			message.success('删除成功')
		})
	}
	// 导出
	const exportData = () => {
		const searchFormParam = JSON.parse(JSON.stringify(searchFormState))
		wineCityApi.export(searchFormParam).then((data) => {
			downloadUtil.resultDownload(data, '城市数据.xlsx')
		})
	}
	// 更改状态
	const editStatus = (record) => {
		loading.value = true
		const api = record.status === 'ENABLE' ? wineCityApi.disable : wineCityApi.enable
		api({ id: record.id })
			.then(() => {
				tableRef.value.refresh()
				message.success('操作成功')
			})
			.finally(() => {
				loading.value = false
			})
	}
	// 查看详情
	const viewDetail = (record) => {
		Modal.info({
			title: '城市详情',
			width: 800,
			content: h('div', [
				h('p', `城市编码：${record.cityCode}`),
				h('p', `城市名称：${record.cityName}`),
				h('p', `城市层级：${record.level === 1 ? '省份' : record.level === 2 ? '城市' : '区县'}`),
				h('p', `省份编码：${record.provinceCode || '无'}`),
				h('p', `父级编码：${record.parentCode || '无'}`),
				h('p', `是否热门：${record.isHot === 'Y' ? '是' : '否'}`),
				h('p', `配送支持：${record.supportDelivery === 'Y' ? '支持' : '不支持'}`),
				h('p', `经度：${record.longitude || '未设置'}`),
				h('p', `纬度：${record.latitude || '未设置'}`),
				h('p', `区号：${record.areaCode || '未设置'}`),
				h('p', `邮政编码：${record.zipCode || '未设置'}`),
				h('p', `拼音：${record.pinyin || '未设置'}`),
				h('p', `简称：${record.shortName || '未设置'}`),
				h('p', `门店数量：${record.storeCount || 0}`),
				h('p', `设备数量：${record.deviceCount || 0}`),
				h('p', `订单数量：${record.orderCount || 0}`),
				h('p', `城市状态：${record.status === 'ENABLE' ? '启用' : '禁用'}`),
				h('p', `描述：${record.description || '无'}`),
				h('p', `创建时间：${record.createTime}`),
				h('p', `更新时间：${record.updateTime}`)
			])
		})
	}
	// 设置热门城市
	const setHotCity = (record) => {
		const isHot = record.isHot === 'Y' ? 'N' : 'Y'
		const title = isHot === 'Y' ? '设为热门城市' : '取消热门城市'
		Modal.confirm({
			title: title,
			content: `确定要${title}吗？`,
			onOk: () => {
				return wineCityApi.setHotCity({ id: record.id, isHot }).then(() => {
					tableRef.value.refresh()
					message.success('操作成功')
				})
			}
		})
	}
	// 设置配送支持
	const setSupportDelivery = (record) => {
		const supportDelivery = record.supportDelivery === 'Y' ? 'N' : 'Y'
		const title = supportDelivery === 'Y' ? '支持配送' : '取消配送支持'
		Modal.confirm({
			title: title,
			content: `确定要${title}吗？`,
			onOk: () => {
				return wineCityApi.setSupportDelivery({ id: record.id, supportDelivery }).then(() => {
					tableRef.value.refresh()
					message.success('操作成功')
				})
			}
		})
	}
	// 更新统计数据
	const updateStatistics = (record) => {
		Modal.confirm({
			title: '更新统计数据',
			content: '确定要更新该城市的统计数据吗？',
			onOk: () => {
				return wineCityApi.updateStatistics(record.cityCode).then(() => {
					tableRef.value.refresh()
					message.success('统计数据更新成功')
				})
			}
		})
	}
</script>

<style scoped>
	.ant-form-item {
		margin-bottom: 0 !important;
	}
	.snowy-button-left {
		margin-left: 8px;
	}
</style>
