<template>
	<div class="balance-pay-config">
		<a-row :gutter="16">
			<a-col :span="24">
				<a-form-item label="支付描述" name="description">
					<a-textarea
						v-model:value="configData.description"
						placeholder="请输入余额支付的描述信息"
						:rows="3"
						@change="handleChange"
					/>
				</a-form-item>
			</a-col>
		</a-row>

		<a-row :gutter="16">
			<a-col :span="12">
				<a-form-item label="最小支付金额" name="minAmount">
					<a-input-number
						v-model:value="configData.minAmount"
						placeholder="请输入最小支付金额"
						:min="0"
						:precision="2"
						style="width: 100%"
						@change="handleChange"
					/>
					<div class="form-help-text">单位：元，设置为0表示无限制</div>
				</a-form-item>
			</a-col>
			<a-col :span="12">
				<a-form-item label="最大支付金额" name="maxAmount">
					<a-input-number
						v-model:value="configData.maxAmount"
						placeholder="请输入最大支付金额"
						:min="0"
						:precision="2"
						style="width: 100%"
						@change="handleChange"
					/>
					<div class="form-help-text">单位：元，设置为0表示无限制</div>
				</a-form-item>
			</a-col>
		</a-row>

		<a-row :gutter="16">
			<a-col :span="12">
				<a-form-item label="是否需要支付密码" name="requirePassword">
					<a-switch
						v-model:checked="configData.requirePassword"
						checked-children="需要"
						un-checked-children="不需要"
						@change="handleChange"
					/>
					<div class="form-help-text">开启后用户支付时需要输入支付密码</div>
				</a-form-item>
			</a-col>
			<a-col :span="12">
				<a-form-item label="是否允许透支" name="allowOverdraft">
					<a-switch
						v-model:checked="configData.allowOverdraft"
						checked-children="允许"
						un-checked-children="不允许"
						@change="handleChange"
					/>
					<div class="form-help-text">开启后余额不足时允许透支支付</div>
				</a-form-item>
			</a-col>
		</a-row>

		<a-row :gutter="16" v-if="configData.allowOverdraft">
			<a-col :span="12">
				<a-form-item label="透支额度" name="overdraftLimit">
					<a-input-number
						v-model:value="configData.overdraftLimit"
						placeholder="请输入透支额度"
						:min="0"
						:precision="2"
						style="width: 100%"
						@change="handleChange"
					/>
					<div class="form-help-text">单位：元，设置为0表示无限制</div>
				</a-form-item>
			</a-col>
			<a-col :span="12">
				<a-form-item label="透支手续费率" name="overdraftFeeRate">
					<a-input-number
						v-model:value="configData.overdraftFeeRate"
						placeholder="请输入透支手续费率"
						:min="0"
						:max="100"
						:precision="2"
						style="width: 100%"
						@change="handleChange"
					/>
					<div class="form-help-text">单位：%，透支金额的手续费百分比</div>
				</a-form-item>
			</a-col>
		</a-row>

		<a-row :gutter="16">
			<a-col :span="12">
				<a-form-item label="支付成功回调URL" name="notifyUrl">
					<a-input v-model:value="configData.notifyUrl" placeholder="请输入支付成功回调URL" @change="handleChange" />
				</a-form-item>
			</a-col>
			<a-col :span="12">
				<a-form-item label="支付完成跳转URL" name="returnUrl">
					<a-input v-model:value="configData.returnUrl" placeholder="请输入支付完成跳转URL" @change="handleChange" />
				</a-form-item>
			</a-col>
		</a-row>
	</div>
</template>

<script setup>
	import { reactive, watch } from 'vue'

	// 组件属性
	const props = defineProps({
		modelValue: {
			type: Object,
			default: () => ({})
		}
	})

	// 事件定义
	const emit = defineEmits(['update:modelValue'])

	// 配置数据
	const configData = reactive({
		description: '',
		minAmount: 0,
		maxAmount: 0,
		requirePassword: false,
		allowOverdraft: false,
		overdraftLimit: 0,
		overdraftFeeRate: 0,
		notifyUrl: '',
		returnUrl: '',
		...props.modelValue
	})

	// 处理数据变化
	const handleChange = () => {
		emit('update:modelValue', { ...configData })
	}

	// 监听外部数据变化
	watch(
		() => props.modelValue,
		(newValue) => {
			Object.assign(configData, newValue)
		},
		{ deep: true }
	)
</script>

<style scoped>
	.balance-pay-config {
		padding: 16px;
		background: #fafafa;
		border-radius: 6px;
		margin-bottom: 16px;
	}

	.form-help-text {
		font-size: 12px;
		color: #999;
		margin-top: 4px;
	}
</style>
