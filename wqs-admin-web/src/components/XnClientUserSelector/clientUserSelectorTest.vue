<template>
	<div style="padding: 20px">
		<h2>C端用户选择器测试</h2>

		<a-divider />

		<h3>单选模式测试</h3>
		<a-form-item label="选择单个用户">
			<xn-client-user-selector
				ref="singleSelectorRef"
				:radio-model="true"
				:clientUserPageApi="clientUserPageApi"
				:clientUserListByIdListApi="clientUserListByIdListApi"
				v-model:value="singleUserId"
				@onBack="onSingleUserSelected"
			>
				<template #button>
					<a-button type="primary">
						<UserOutlined />
						选择用户
					</a-button>
				</template>
			</xn-client-user-selector>
		</a-form-item>

		<p>选中的用户ID: {{ singleUserId }}</p>
		<p>选中的用户信息: {{ JSON.stringify(selectedSingleUser) }}</p>

		<a-divider />

		<h3>多选模式测试</h3>
		<a-form-item label="选择多个用户">
			<xn-client-user-selector
				ref="multiSelectorRef"
				:radio-model="false"
				:clientUserPageApi="clientUserPageApi"
				:clientUserListByIdListApi="clientUserListByIdListApi"
				v-model:value="multiUserIds"
				@onBack="onMultiUsersSelected"
			>
				<template #button>
					<a-button type="primary">
						<UserOutlined />
						选择用户
					</a-button>
				</template>
			</xn-client-user-selector>
		</a-form-item>

		<p>选中的用户IDs: {{ multiUserIds }}</p>
		<p>选中的用户信息: {{ JSON.stringify(selectedMultiUsers) }}</p>

		<a-divider />

		<h3>直接API测试</h3>
		<a-space>
			<a-button @click="testUserPageApi" :loading="pageApiLoading">测试分页API</a-button>
			<a-button @click="testListByIdListApi" :loading="listApiLoading">测试ID列表API</a-button>
		</a-space>

		<div v-if="apiTestResult" style="margin-top: 10px">
			<h4>API测试结果：</h4>
			<pre>{{ JSON.stringify(apiTestResult, null, 2) }}</pre>
		</div>

		<div v-if="apiError" style="margin-top: 10px; color: red">
			<h4>API错误：</h4>
			<pre>{{ apiError }}</pre>
		</div>
	</div>
</template>

<script setup name="clientUserSelectorTest">
	import { UserOutlined } from '@ant-design/icons-vue'
	import { message } from 'ant-design-vue'
	import clientUserApi from '@/api/client/clientUserApi'

	// 单选相关
	const singleUserId = ref('')
	const selectedSingleUser = ref(null)

	// 多选相关
	const multiUserIds = ref('')
	const selectedMultiUsers = ref([])

	// API测试相关
	const pageApiLoading = ref(false)
	const listApiLoading = ref(false)
	const apiTestResult = ref(null)
	const apiError = ref('')

	// 传递给选择器的API函数
	const clientUserPageApi = (param) => {
		console.log('调用userPage API，参数：', param)
		return clientUserApi
			.userPage(param)
			.then((data) => {
				console.log('userPage API返回：', data)
				return Promise.resolve(data)
			})
			.catch((error) => {
				console.error('userPage API错误：', error)
				throw error
			})
	}

	const clientUserListByIdListApi = (param) => {
		console.log('调用userListByIdList API，参数：', param)
		return clientUserApi
			.userListByIdList(param)
			.then((data) => {
				console.log('userListByIdList API返回：', data)
				return Promise.resolve(data)
			})
			.catch((error) => {
				console.error('userListByIdList API错误：', error)
				throw error
			})
	}

	// 单选回调
	const onSingleUserSelected = (data) => {
		console.log('单选用户选择回调：', data)
		selectedSingleUser.value = data
		message.success('单选用户已选择')
	}

	// 多选回调
	const onMultiUsersSelected = (data) => {
		console.log('多选用户选择回调：', data)
		selectedMultiUsers.value = data
		message.success('多选用户已选择')
	}

	// 测试分页API
	const testUserPageApi = async () => {
		pageApiLoading.value = true
		apiError.value = ''
		try {
			const params = {
				current: 1,
				size: 10
			}
			const result = await clientUserApi.userPage(params)
			apiTestResult.value = {
				api: 'userPage',
				params,
				result
			}
			message.success('分页API测试成功')
		} catch (error) {
			console.error('分页API测试失败：', error)
			apiError.value = `分页API错误: ${error.message || JSON.stringify(error)}`
			message.error('分页API测试失败')
		} finally {
			pageApiLoading.value = false
		}
	}

	// 测试ID列表API
	const testListByIdListApi = async () => {
		listApiLoading.value = true
		apiError.value = ''
		try {
			const params = {
				idList: ['1', '2', '3'] // 测试用的ID
			}
			const result = await clientUserApi.userListByIdList(params)
			apiTestResult.value = {
				api: 'userListByIdList',
				params,
				result
			}
			message.success('ID列表API测试成功')
		} catch (error) {
			console.error('ID列表API测试失败：', error)
			apiError.value = `ID列表API错误: ${error.message || JSON.stringify(error)}`
			message.error('ID列表API测试失败')
		} finally {
			listApiLoading.value = false
		}
	}
</script>

<style scoped>
	pre {
		background-color: #f5f5f5;
		padding: 10px;
		border-radius: 4px;
		max-height: 300px;
		overflow-y: auto;
	}
</style>
