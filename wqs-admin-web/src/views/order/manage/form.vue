<template>
	<a-modal
		:title="formData.title"
		:width="800"
		:open="visible"
		:destroy-on-close="true"
		@cancel="onClose"
		@ok="onSubmit"
		:confirm-loading="submitLoading"
	>
		<a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical">
			<a-row :gutter="16">
				<a-col :span="12">
					<a-form-item label="用户ID:" name="userId">
						<a-input v-model:value="formData.userId" placeholder="请输入用户ID" :disabled="formData.disabled" />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="设备ID:" name="deviceId">
						<a-input v-model:value="formData.deviceId" placeholder="请输入设备ID" :disabled="formData.disabled" />
					</a-form-item>
				</a-col>
			</a-row>
			<a-row :gutter="16">
				<a-col :span="12">
					<a-form-item label="酒品ID:" name="wineId">
						<a-input v-model:value="formData.wineId" placeholder="请输入酒品ID" :disabled="formData.disabled" />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="酒品名称:" name="wineName">
						<a-input v-model:value="formData.wineName" placeholder="请输入酒品名称" :disabled="formData.disabled" />
					</a-form-item>
				</a-col>
			</a-row>
			<a-row :gutter="16">
				<a-col :span="12">
					<a-form-item label="数量:" name="quantity">
						<a-input-number
							v-model:value="formData.quantity"
							placeholder="请输入数量"
							:min="1"
							:disabled="formData.disabled"
							style="width: 100%"
						/>
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="单价:" name="unitPrice">
						<a-input-number
							v-model:value="formData.unitPrice"
							placeholder="请输入单价"
							:min="0"
							:precision="2"
							:disabled="formData.disabled"
							style="width: 100%"
						/>
					</a-form-item>
				</a-col>
			</a-row>
			<a-row :gutter="16">
				<a-col :span="12">
					<a-form-item label="总金额:" name="totalAmount">
						<a-input-number
							v-model:value="formData.totalAmount"
							placeholder="请输入总金额"
							:min="0"
							:precision="2"
							:disabled="formData.disabled"
							style="width: 100%"
						/>
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="订单状态:" name="status">
						<a-select v-model:value="formData.status" placeholder="请选择订单状态" :disabled="formData.disabled">
							<a-select-option value="PENDING">待支付</a-select-option>
							<a-select-option value="PAID">已支付</a-select-option>
							<a-select-option value="DISPENSING">出酒中</a-select-option>
							<a-select-option value="COMPLETED">已完成</a-select-option>
							<a-select-option value="CANCELLED">已取消</a-select-option>
						</a-select>
					</a-form-item>
				</a-col>
			</a-row>
			<a-row :gutter="16" v-if="formData.id">
				<a-col :span="12">
					<a-form-item label="订单号:" name="orderNo">
						<a-input v-model:value="formData.orderNo" placeholder="系统自动生成" disabled />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="支付时间:" name="payTime">
						<a-input v-model:value="formData.payTime" placeholder="支付后自动生成" disabled />
					</a-form-item>
				</a-col>
			</a-row>
			<a-row :gutter="16" v-if="formData.id">
				<a-col :span="12">
					<a-form-item label="出酒开始时间:" name="dispenseStartTime">
						<a-input v-model:value="formData.dispenseStartTime" placeholder="开始出酒后自动生成" disabled />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="出酒完成时间:" name="dispenseEndTime">
						<a-input v-model:value="formData.dispenseEndTime" placeholder="完成出酒后自动生成" disabled />
					</a-form-item>
				</a-col>
			</a-row>
			<a-row :gutter="16" v-if="formData.id">
				<a-col :span="24">
					<a-form-item label="备注:" name="remark">
						<a-textarea v-model:value="formData.remark" placeholder="请输入备注" :disabled="formData.disabled" />
					</a-form-item>
				</a-col>
			</a-row>
		</a-form>
	</a-modal>
</template>

<script setup name="orderManageForm">
	import orderApi from '@/api/order/orderApi'

	// 定义emit
	const emit = defineEmits({ successful: null })
	const visible = ref(false)
	const formRef = ref()
	const formData = ref({})
	const submitLoading = ref(false)
	const formRules = {
		// 查看详情模式不需要验证规则
	}

	// 打开弹窗
	const onOpen = (record, type) => {
		visible.value = true
		formData.value = { disabled: true, title: '订单详情' }

		if (record) {
			if (typeof record === 'string') {
				// 根据ID获取详情
				orderApi.orderManageDetail({ id: record }).then((res) => {
					formData.value = Object.assign({}, res, {
						disabled: true,
						title: '订单详情'
					})
				})
			} else {
				formData.value = Object.assign({}, record, {
					disabled: true,
					title: '订单详情'
				})
			}
		}
	}

	// 关闭弹窗
	const onClose = () => {
		visible.value = false
		formRef.value?.resetFields()
	}

	// 确认按钮（仅关闭弹窗）
	const onSubmit = () => {
		visible.value = false
	}

	// 监听数量和单价变化，自动计算总金额
	watch([() => formData.value.quantity, () => formData.value.unitPrice], ([quantity, unitPrice]) => {
		if (quantity && unitPrice) {
			formData.value.totalAmount = (quantity * unitPrice).toFixed(2)
		}
	})

	// 暴露给父组件
	defineExpose({
		onOpen
	})
</script>
