<template>
	<div>
		<a-card :bordered="false" style="margin-bottom: 10px">
			<a-form ref="searchFormRef" name="advanced_search" class="ant-advanced-search-form" :model="searchFormState">
				<a-row :gutter="24">
					<a-col :span="8">
						<a-form-item name="searchKey" label="用户关键词">
							<a-input v-model:value="searchFormState.searchKey" placeholder="请输入用户关键词" />
						</a-form-item>
					</a-col>
					<a-col :span="8">
						<a-form-item name="userStatus" label="用户状态">
							<a-select v-model:value="searchFormState.userStatus" placeholder="请选择用户状态" allow-clear>
								<a-select-option value="ENABLE">启用</a-select-option>
								<a-select-option value="DISABLE">禁用</a-select-option>
							</a-select>
						</a-form-item>
					</a-col>
					<a-col :span="8">
						<a-button type="primary" @click="tableRef.refresh(true)">
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
						<a-button v-if="hasPerm('clientUserAdd')" type="primary" @click="clientUserFormRef.onOpen()">
							<template #icon><plus-outlined /></template>
							<span>新增用户</span>
						</a-button>
						<xn-batch-button
							v-if="hasPerm('clientUserBatchDelete')"
							buttonName="批量删除"
							icon="DeleteOutlined"
							buttonDanger
							:selectedRowKeys="selectedRowKeys"
							@batchCallBack="deleteBatchUser"
						/>
					</a-space>
				</template>
				<template #bodyCell="{ column, record }">
					<template v-if="column.dataIndex === 'avatar'">
						<a-avatar :src="record.avatar" style="margin-bottom: -5px; margin-top: -5px" />
					</template>
					<template v-if="column.dataIndex === 'gender'">
						{{ $TOOL.dictTypeData('GENDER', record.gender) }}
					</template>
					<template v-if="column.dataIndex === 'userStatus'">
						<a-tag :color="record.userStatus === 'ENABLE' ? 'green' : 'red'">
							{{ $TOOL.dictTypeData('COMMON_STATUS', record.userStatus) }}
						</a-tag>
					</template>
					<template v-if="column.dataIndex === 'action'">
						<a-space>
							<a v-if="hasPerm('clientUserEdit')" @click="clientUserFormRef.onOpen(record)">编辑</a>

							<a-dropdown v-if="hasPerm(['clientUserResetPassword', 'clientUserUpdateStatus', 'clientUserDelete'])">
								<a class="ant-dropdown-link" @click.prevent> 更多 <DownOutlined /> </a>
								<template #overlay>
									<a-menu>
										<a-menu-item v-if="hasPerm('clientUserResetPassword')" key="resetPassword">
											<a @click="resetPassword(record)">重置密码</a>
										</a-menu-item>
										<a-menu-item v-if="hasPerm('clientUserUpdateStatus')" key="updateStatus">
											<a @click="updateStatus(record)">
												{{ record.userStatus === 'ENABLE' ? '禁用' : '启用' }}
											</a>
										</a-menu-item>
										<a-menu-divider v-if="hasPerm('clientUserDelete')" />
										<a-menu-item v-if="hasPerm('clientUserDelete')" key="delete">
											<a-popconfirm title="确定要删除此用户吗？" @confirm="removeUser(record)">
												<a style="color: #ff4d4f">删除</a>
											</a-popconfirm>
										</a-menu-item>
									</a-menu>
								</template>
							</a-dropdown>
						</a-space>
					</template>
				</template>
			</s-table>
		</a-card>
		<client-user-form ref="clientUserFormRef" @successful="tableRef.refresh()" />
	</div>
</template>
<script setup name="clientUser">
	import { message, Modal } from 'ant-design-vue'
	import clientUserApi from '@/api/client/clientUserApi'
	import ClientUserForm from './form.vue'
	import { hasPerm } from '@/utils/permission'

	const columns = [
		{
			title: '头像',
			dataIndex: 'avatar',
			align: 'center',
			width: '80px'
		},
		{
			title: '账号',
			dataIndex: 'account',
			ellipsis: true
		},
		{
			title: '姓名',
			dataIndex: 'name'
		},
		{
			title: '性别',
			dataIndex: 'gender',
			width: 100
		},
		{
			title: '手机',
			dataIndex: 'phone',
			ellipsis: true
		},
		{
			title: '邮箱',
			dataIndex: 'email',
			ellipsis: true
		},
		{
			title: '状态',
			dataIndex: 'userStatus',
			width: 100
		}
	]

	// 动态添加操作列
	if (hasPerm(['clientUserEdit', 'clientUserResetPassword', 'clientUserUpdateStatus', 'clientUserDelete'])) {
		columns.push({
			title: '操作',
			dataIndex: 'action',
			align: 'center',
			width: '150px'
		})
	}

	const toolConfig = { refresh: true, height: true, columnSetting: true }
	const searchFormRef = ref()
	const searchFormState = ref({})
	const tableRef = ref(null)
	const selectedRowKeys = ref([])
	const clientUserFormRef = ref(null)

	// 表格查询 返回 Promise 对象
	const loadData = (parameter) => {
		return clientUserApi.userPage(Object.assign(parameter, searchFormState.value)).then((res) => {
			return res
		})
	}

	// 重置
	const reset = () => {
		searchFormRef.value.resetFields()
		tableRef.value.refresh(true)
	}

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

	// 删除用户
	const removeUser = (record) => {
		let params = [
			{
				id: record.id
			}
		]
		clientUserApi.userDelete(params).then(() => {
			message.success('删除成功')
			tableRef.value.refresh()
		})
	}

	// 批量删除
	const deleteBatchUser = (params) => {
		clientUserApi.userBatchDelete(params).then(() => {
			message.success('批量删除成功')
			tableRef.value.clearRefreshSelected()
		})
	}

	// 重置用户密码
	const resetPassword = (record) => {
		Modal.confirm({
			title: '确认重置密码',
			content: `确定要重置用户"${record.name}"的密码吗？重置后密码将变为：123456`,
			onOk() {
				clientUserApi.userResetPassword({ id: record.id }).then(() => {
					message.success('密码重置成功，新密码为：123456')
				})
			}
		})
	}

	// 更新用户状态
	const updateStatus = (record) => {
		const action = record.userStatus === 'ENABLE' ? '禁用' : '启用'
		const newStatus = record.userStatus === 'ENABLE' ? 'DISABLE' : 'ENABLE'

		Modal.confirm({
			title: `确认${action}用户`,
			content: `确定要${action}用户"${record.name}"吗？`,
			onOk() {
				clientUserApi
					.userUpdateStatus({
						id: record.id,
						userStatus: newStatus
					})
					.then(() => {
						message.success(`${action}成功`)
						tableRef.value.refresh()
					})
			}
		})
	}
</script>

<style lang="less" scoped>
	.ant-form-item {
		margin-bottom: 0 !important;
	}
	.snowy-table-avatar {
		margin-top: -10px;
		margin-bottom: -10px;
	}
	.snowy-button-left {
		margin-left: 8px;
	}
</style>
