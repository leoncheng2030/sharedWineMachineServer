<template>
	<a-modal
		title="调整库存"
		:width="600"
		:visible="visible"
		:confirmLoading="confirmLoading"
		@ok="handleOk"
		@cancel="handleCancel"
		:destroyOnClose="true"
	>
		<a-descriptions :column="2" class="xn-mb16" v-if="stockInfo.productName">
			<a-descriptions-item label="酒品名称">{{ stockInfo.productName }}</a-descriptions-item>
			<a-descriptions-item label="当前库存">{{ stockInfo.stockQuantity }}ml</a-descriptions-item>
		</a-descriptions>

		<a-form ref="formRef" :model="formData" :rules="rules" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
			<a-form-item label="调整后库存" name="quantity">
				<a-input-number
					v-model:value="formData.quantity"
					:min="0"
					:precision="0"
					placeholder="请输入调整后的库存数量"
					style="width: 100%"
					addon-after="ml"
				/>
			</a-form-item>
			<a-form-item label="调整原因" name="reason">
				<a-input v-model:value="formData.reason" placeholder="请输入调整原因" />
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

	const emit = defineEmits(['successful'])

	const visible = ref(false)
	const confirmLoading = ref(false)
	const formRef = ref()
	const stockInfo = ref({})

	const formData = ref({
		deviceId: '',
		productId: '',
		quantity: undefined,
		reason: '',
		remark: ''
	})

	const rules = {
		quantity: [{ required: true, message: '请输入调整后的库存数量' }],
		reason: [{ required: true, message: '请输入调整原因' }]
	}

	// 打开弹窗
	const onOpen = (record) => {
		stockInfo.value = record
		formData.value = {
			deviceId: record.deviceId,
			productId: record.productId,
			quantity: record.stockQuantity,
			reason: '',
			remark: ''
		}
		visible.value = true
		formRef.value?.resetFields()
	}

	// 关闭弹窗
	const handleCancel = () => {
		visible.value = false
		stockInfo.value = {}
	}

	// 确认操作
	const handleOk = () => {
		formRef.value.validate().then(() => {
			confirmLoading.value = true
			wineDeviceStockApi
				.adjust(formData.value)
				.then(() => {
					message.success('调整库存成功')
					visible.value = false
					emit('successful')
				})
				.finally(() => {
					confirmLoading.value = false
				})
		})
	}

	defineExpose({
		onOpen
	})
</script>

<style scoped>
	.xn-mb16 {
		margin-bottom: 16px;
	}
</style>
