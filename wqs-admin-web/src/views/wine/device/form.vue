<template>
	<a-modal
		v-model:open="visible"
		:title="formData.id ? '编辑设备' : '新增设备'"
		:width="800"
		:confirm-loading="submitLoading"
		@ok="onSubmit"
		@cancel="onClose"
	>
		<a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical">
			<a-row :gutter="16">
				<a-col :span="12">
					<a-form-item label="设备编码" name="deviceCode">
						<a-input v-model:value="formData.deviceCode" placeholder="请输入设备编码" :maxlength="50" />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="设备名称" name="deviceName">
						<a-input v-model:value="formData.deviceName" placeholder="请输入设备名称" :maxlength="100" />
					</a-form-item>
				</a-col>
			</a-row>
			<a-row :gutter="16">
				<a-col :span="12">
					<a-form-item label="所属门店" name="storeId">
						<a-select
							v-model:value="formData.storeId"
							placeholder="请选择所属门店"
							:getPopupContainer="(trigger) => trigger.parentNode"
							allowClear
							show-search
							:filter-option="filterOption"
						>
							<a-select-option v-for="item in storeData" :key="item.id" :value="item.id">
								{{ item.storeName }}
							</a-select-option>
						</a-select>
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="绑定酒品" name="currentProductId">
						<a-select
							v-model:value="formData.currentProductId"
							placeholder="请选择绑定酒品（可选）"
							:getPopupContainer="(trigger) => trigger.parentNode"
							allowClear
							show-search
							:filter-option="filterOption"
						>
							<a-select-option v-for="item in productData" :key="item.id" :value="item.id">
								{{ item.productName }}
							</a-select-option>
						</a-select>
					</a-form-item>
				</a-col>
			</a-row>
			<a-row :gutter="16">
				<a-col :span="12">
					<a-form-item label="设备状态" name="status">
						<a-select
							v-model:value="formData.status"
							placeholder="请选择设备状态"
							:getPopupContainer="(trigger) => trigger.parentNode"
						>
							<a-select-option v-for="item in statusData" :key="item.value" :value="item.value">
								{{ item.label }}
							</a-select-option>
						</a-select>
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="设备管理员" name="managerUserId">
						<xn-client-user-selector
							ref="deviceManagerSelectorRef"
							:radio-model="true"
							:clientUserPageApi="selectorApiFunction.clientUserPageApi"
							:clientUserListByIdListApi="selectorApiFunction.clientUserListByIdListApi"
							v-model:value="formData.managerUserId"
							@onBack="deviceManagerSelectorOnBack"
						/>
					</a-form-item>
				</a-col>
			</a-row>
			<a-row :gutter="16">
				<a-col :span="12">
					<a-form-item label="设备位置" name="location">
						<a-input v-model:value="formData.location" placeholder="请输入设备位置" :maxlength="200" />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="排序码" name="sortCode">
						<a-input-number
							v-model:value="formData.sortCode"
							placeholder="请输入排序码"
							:min="1"
							:max="9999"
							style="width: 100%"
						/>
					</a-form-item>
				</a-col>
			</a-row>
			<a-row :gutter="16">
				<a-col :span="24">
					<a-form-item label="蓝牙UUID" name="uuid">
						<a-input
							v-model:value="formData.uuid"
							placeholder="请输入设备蓝牙UUID标识（用于蓝牙设备识别和连接）"
							:maxlength="64"
						/>
						<template #extra>
							<span style="color: #999; font-size: 12px"> 蓝牙UUID用于设备识别，格式如：003C82031dPt9CR01 </span>
						</template>
					</a-form-item>
				</a-col>
			</a-row>
			<a-row :gutter="16">
				<a-col :span="24">
					<a-form-item label="设备二维码" name="qrCode">
						<a-input v-model:value="formData.qrCode" placeholder="请输入设备二维码内容" :maxlength="500" />
					</a-form-item>
				</a-col>
			</a-row>
			<a-row :gutter="16">
				<a-col :span="24">
					<a-form-item label="备注" name="remark">
						<a-textarea
							v-model:value="formData.remark"
							placeholder="请输入备注信息"
							:maxlength="500"
							:rows="3"
							show-count
						/>
					</a-form-item>
				</a-col>
			</a-row>
		</a-form>
	</a-modal>
</template>

<script setup name="wineDeviceForm">
	import { message } from 'ant-design-vue'
	import { cloneDeep } from 'lodash-es'
	import wineDeviceApi from '@/api/wine/wineDeviceApi'
	import wineStoreApi from '@/api/wine/wineStoreApi'
	import wineProductApi from '@/api/wine/wineProductApi'
	import clientUserApi from '@/api/client/clientUserApi'
	import xnClientUserSelector from '@/components/XnClientUserSelector/index.vue'

	const emit = defineEmits(['successful'])

	const visible = ref(false)
	const submitLoading = ref(false)
	const formRef = ref()

	// 状态数据
	const statusData = [
		{ value: 'ONLINE', label: '在线' },
		{ value: 'OFFLINE', label: '离线' },
		{ value: 'MAINTENANCE', label: '维护中' }
	]

	// 选择器数据
	const storeData = ref([])
	const productData = ref([])

	// 表单数据
	const formData = ref({})

	// C端用户选择器相关
	const deviceManagerSelectorRef = ref(null)
	const selectorApiFunction = {
		clientUserPageApi: clientUserApi.userPage,
		clientUserListByIdListApi: clientUserApi.userListByIdList
	}

	// 表单验证规则
	const formRules = {
		deviceCode: [{ required: true, message: '请输入设备编码', trigger: 'blur' }],
		deviceName: [{ required: true, message: '请输入设备名称', trigger: 'blur' }],
		storeId: [{ required: true, message: '请选择所属门店', trigger: 'change' }],
		status: [{ required: true, message: '请选择设备状态', trigger: 'change' }],
		sortCode: [{ required: true, message: '请输入排序码', trigger: 'blur' }]
	}

	// 打开弹窗
	const onOpen = (record) => {
		visible.value = true
		if (record) {
			formData.value = cloneDeep(record)
		} else {
			formData.value = {
				status: 'OFFLINE',
				sortCode: 99
			}
		}
		loadSelectorData()
	}

	// 关闭弹窗
	const onClose = () => {
		visible.value = false
		formRef.value?.resetFields()
		formData.value = {}
	}

	// 提交表单
	const onSubmit = () => {
		formRef.value?.validate().then(() => {
			submitLoading.value = true
			const api = formData.value.id ? wineDeviceApi.edit : wineDeviceApi.add
			api(formData.value)
				.then(() => {
					message.success(formData.value.id ? '编辑成功' : '新增成功')
					emit('successful')
					onClose()
				})
				.finally(() => {
					submitLoading.value = false
				})
		})
	}

	// 加载选择器数据
	const loadSelectorData = async () => {
		try {
			// 加载门店数据
			const storeRes = await wineStoreApi.selector()
			storeData.value = storeRes

			// 加载酒品数据
			const productRes = await wineProductApi.selector()
			productData.value = productRes || []
		} catch (error) {
			console.error('加载选择器数据失败:', error)
		}
	}

	// 设备管理员选择器回调
	const deviceManagerSelectorOnBack = (data) => {
		// 选择器组件会自动处理数据绑定，这里可以做额外处理
		console.log('设备管理员选择结果:', data)
	}

	// 选择器过滤函数
	const filterOption = (input, option) => {
		return option.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
	}

	// 暴露方法
	defineExpose({
		onOpen
	})
</script>

<style scoped>
	.ant-form-item {
		margin-bottom: 16px;
	}
</style>
