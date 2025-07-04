<template>
	<a-modal
		v-model:open="visible"
		title="绑定酒品"
		:width="600"
		:confirm-loading="submitLoading"
		@ok="onSubmit"
		@cancel="onClose"
	>
		<div style="margin-bottom: 16px">
			<a-descriptions :column="2" bordered size="small">
				<a-descriptions-item label="设备编码">{{ deviceInfo.deviceCode }}</a-descriptions-item>
				<a-descriptions-item label="设备名称">{{ deviceInfo.deviceName }}</a-descriptions-item>
				<a-descriptions-item label="所属门店">{{ deviceInfo.storeName }}</a-descriptions-item>
				<a-descriptions-item label="当前绑定">
					<a-tag v-if="deviceInfo.currentProductName" color="orange">
						{{ deviceInfo.currentProductName }}
					</a-tag>
					<span v-else style="color: #999">未绑定酒品</span>
				</a-descriptions-item>
			</a-descriptions>
		</div>

		<a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical">
			<a-form-item label="选择酒品" name="productId">
				<a-select
					v-model:value="formData.productId"
					placeholder="请选择要绑定的酒品"
					:getPopupContainer="(trigger) => trigger.parentNode"
					show-search
					:filter-option="filterOption"
					style="width: 100%"
				>
					<a-select-option v-for="item in productData" :key="item.id" :value="item.id">
						<div style="display: flex; justify-content: space-between; align-items: center">
							<span>{{ item.productName }}</span>
							<span style="color: #999; font-size: 12px">{{ item.productCode }}</span>
						</div>
					</a-select-option>
				</a-select>
			</a-form-item>

			<a-form-item label="绑定说明" name="remark">
				<a-textarea
					v-model:value="formData.remark"
					placeholder="请输入绑定说明（可选）"
					:maxlength="200"
					:rows="3"
					show-count
				/>
			</a-form-item>
		</a-form>

		<a-alert
			v-if="deviceInfo.currentProductName"
			message="注意"
			:description="`当前设备已绑定酒品【${deviceInfo.currentProductName}】，绑定新酒品将自动解绑原酒品。`"
			type="warning"
			show-icon
			style="margin-top: 16px"
		/>
	</a-modal>
</template>

<script setup name="productBindModal">
	import { message } from 'ant-design-vue'
	import wineDeviceApi from '@/api/wine/wineDeviceApi'
	import wineProductApi from '@/api/wine/wineProductApi'

	const emit = defineEmits(['successful'])

	const visible = ref(false)
	const submitLoading = ref(false)
	const formRef = ref()

	// 设备信息
	const deviceInfo = ref({})

	// 酒品数据
	const productData = ref([])

	// 表单数据
	const formData = ref({})

	// 表单验证规则
	const formRules = {
		productId: [{ required: true, message: '请选择要绑定的酒品', trigger: 'change' }]
	}

	// 打开弹窗
	const onOpen = (device) => {
		deviceInfo.value = device
		visible.value = true
		formData.value = {
			deviceId: device.id,
			productId: device.currentProductId || undefined
		}
		loadProductData()
	}

	// 关闭弹窗
	const onClose = () => {
		visible.value = false
		formRef.value?.resetFields()
		formData.value = {}
		deviceInfo.value = {}
	}

	// 提交绑定
	const onSubmit = () => {
		formRef.value?.validate().then(() => {
			submitLoading.value = true
			wineDeviceApi
				.bindProduct(formData.value.deviceId, formData.value.productId)
				.then(() => {
					message.success('绑定成功')
					emit('successful')
					onClose()
				})
				.finally(() => {
					submitLoading.value = false
				})
		})
	}

	// 加载酒品数据
	const loadProductData = async () => {
		try {
			const res = await wineProductApi.selector()
			productData.value = res || []
		} catch (error) {
			console.error('加载酒品数据失败:', error)
			message.error('加载酒品数据失败')
		}
	}

	// 选择器过滤函数
	const filterOption = (input, option) => {
		const text = option.children.props.children[0].children
		return text.toLowerCase().indexOf(input.toLowerCase()) >= 0
	}

	// 暴露方法
	defineExpose({
		onOpen
	})
</script>

<style scoped>
	.ant-descriptions {
		background: #fafafa;
	}
</style>
