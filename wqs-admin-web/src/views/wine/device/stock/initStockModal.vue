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
		<a-form ref="formRef" :model="formData" :rules="rules" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
			<a-form-item label="酒品" name="productId">
				<a-select v-model:value="formData.productId" placeholder="请选择酒品" show-search :filter-option="filterOption">
					<a-select-option v-for="item in productData" :key="item.id" :value="item.id">
						{{ item.productName }}
					</a-select-option>
				</a-select>
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
	</a-modal>
</template>

<script setup>
	import { message } from 'ant-design-vue'
	import wineDeviceStockApi from '@/api/wine/wineDeviceStockApi'
	import wineProductApi from '@/api/wine/wineProductApi'

	const props = defineProps({
		deviceId: {
			type: String,
			required: true
		}
	})

	const emit = defineEmits(['successful'])

	const visible = ref(false)
	const confirmLoading = ref(false)
	const title = ref('初始化库存')
	const formRef = ref()
	const productData = ref([])

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
	const onOpen = () => {
		visible.value = true
		loadProductData()
		resetForm()
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
			productId: undefined,
			initialQuantity: undefined,
			alertThreshold: undefined,
			remark: ''
		}
		formRef.value?.resetFields()
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
