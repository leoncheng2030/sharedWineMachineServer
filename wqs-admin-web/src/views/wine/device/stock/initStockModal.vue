<template>
	<a-modal
		:title="title"
		:width="600"
		:visible="visible"
		:confirmLoading="confirmLoading"
		@ok="handleOk"
		@cancel="handleCancel"
		:destroyOnClose="true"
	>
		<a-spin :spinning="loading">
			<a-form ref="formRef" :model="formData" :rules="rules" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
				<a-form-item label="酒品" name="productId">
					<a-select 
						v-model:value="formData.productId" 
						placeholder="请选择酒品" 
						show-search 
						:filter-option="filterOption"
						:disabled="!!deviceCurrentProduct || loading"
						:loading="loading"
					>
						<a-select-option v-for="item in productData" :key="item.id" :value="item.id">
							{{ item.productName }}
						</a-select-option>
					</a-select>
					<div v-if="deviceCurrentProduct" style="margin-top: 8px; color: #666; font-size: 12px;">
						当前设备绑定酒品：{{ deviceCurrentProduct.productName }}
					</div>
				</a-form-item>
			<a-form-item label="初始库存" name="initialQuantity">
				<a-input-number
					v-model:value="formData.initialQuantity"
					:min="0"
					:precision="0"
					placeholder="请输入初始库存数量"
					style="width: 100%"
					addon-after="ml"
				/>
			</a-form-item>
			<a-form-item label="预警阈值" name="alertThreshold">
				<a-input-number
					v-model:value="formData.alertThreshold"
					:min="0"
					:precision="0"
					placeholder="请输入预警阈值"
					style="width: 100%"
					addon-after="ml"
				/>
			</a-form-item>
				<a-form-item label="备注" name="remark">
					<a-textarea v-model:value="formData.remark" placeholder="请输入备注" :rows="3" />
				</a-form-item>
			</a-form>
		</a-spin>
	</a-modal>
</template>

<script setup>
	import { message } from 'ant-design-vue'
	import wineDeviceStockApi from '@/api/wine/wineDeviceStockApi'
	import wineProductApi from '@/api/wine/wineProductApi'
	import wineDeviceApi from '@/api/wine/wineDeviceApi'

	const props = defineProps({
		deviceId: {
			type: String,
			required: true
		}
	})

	const emit = defineEmits(['successful'])

	const visible = ref(false)
	const confirmLoading = ref(false)
	const loading = ref(false) // 弹窗数据加载状态
	const title = ref('初始化库存')
	const formRef = ref()
	const productData = ref([])
	const deviceCurrentProduct = ref(null) // 设备当前绑定的酒品

	const formData = ref({
		deviceId: props.deviceId,
		productId: undefined,
		initialQuantity: undefined,
		alertThreshold: undefined,
		remark: ''
	})

	const rules = {
		productId: [{ required: true, message: '请选择酒品' }],
		initialQuantity: [{ required: true, message: '请输入初始库存数量' }],
		alertThreshold: [{ required: true, message: '请输入预警阈值' }]
	}

	// 打开弹窗
	const onOpen = async () => {
		visible.value = true
		loading.value = true
		
		try {
			await loadDeviceInfo()
			await loadProductData()
			resetForm()
		} finally {
			loading.value = false
		}
	}

	// 关闭弹窗
	const handleCancel = () => {
		visible.value = false
		resetForm()
	}

	// 确认操作
	const handleOk = () => {
		formRef.value.validate().then(() => {
			confirmLoading.value = true
			wineDeviceStockApi
				.init(formData.value)
				.then(() => {
					message.success('初始化库存成功')
					visible.value = false
					emit('successful')
				})
				.finally(() => {
					confirmLoading.value = false
				})
		})
	}

	// 重置表单
	const resetForm = () => {
		formData.value = {
			deviceId: props.deviceId,
			productId: deviceCurrentProduct.value?.id || undefined,
			initialQuantity: undefined,
			alertThreshold: undefined,
			remark: ''
		}
		formRef.value?.resetFields()
	}

	// 加载设备信息
	const loadDeviceInfo = async () => {
		try {
			const deviceInfo = await wineDeviceApi.detail({ id: props.deviceId })
			if (deviceInfo.currentProductId && deviceInfo.currentProductName) {
				deviceCurrentProduct.value = {
					id: deviceInfo.currentProductId,
					productName: deviceInfo.currentProductName
				}
			} else {
				deviceCurrentProduct.value = null
			}
		} catch (error) {
			console.error('加载设备信息失败:', error)
			deviceCurrentProduct.value = null
		}
	}

	// 加载酒品数据
	const loadProductData = () => {
		wineProductApi.selector().then((res) => {
			productData.value = res
		})
	}

	// 酒品筛选
	const filterOption = (input, option) => {
		return option.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
	}

	defineExpose({
		onOpen
	})
</script>
