<template>
	<div class="alipay-config">
		<a-row :gutter="16">
			<a-col :span="12">
				<a-form-item label="应用ID" name="appId">
					<a-input v-model:value="configData.appId" placeholder="请输入支付宝应用ID" @change="handleChange" />
				</a-form-item>
			</a-col>
			<a-col :span="12">
				<a-form-item label="商户号" name="merchantId">
					<a-input v-model:value="configData.merchantId" placeholder="请输入支付宝商户号" @change="handleChange" />
				</a-form-item>
			</a-col>
		</a-row>

		<a-row :gutter="16">
			<a-col :span="24">
				<a-form-item label="应用私钥" name="privateKey">
					<a-textarea
						v-model:value="configData.privateKey"
						placeholder="请输入应用私钥（RSA2048格式）"
						:rows="4"
						@change="handleChange"
					/>
				</a-form-item>
			</a-col>
		</a-row>

		<a-row :gutter="16">
			<a-col :span="24">
				<a-form-item label="支付宝公钥" name="publicKey">
					<a-textarea
						v-model:value="configData.publicKey"
						placeholder="请输入支付宝公钥"
						:rows="4"
						@change="handleChange"
					/>
				</a-form-item>
			</a-col>
		</a-row>

		<a-row :gutter="16">
			<a-col :span="12">
				<a-form-item label="应用公钥证书路径" name="certPath">
					<a-input v-model:value="configData.certPath" placeholder="请输入应用公钥证书路径" @change="handleChange" />
				</a-form-item>
			</a-col>
			<a-col :span="12">
				<a-form-item label="支付宝根证书路径" name="rootCertPath">
					<a-input
						v-model:value="configData.rootCertPath"
						placeholder="请输入支付宝根证书路径"
						@change="handleChange"
					/>
				</a-form-item>
			</a-col>
		</a-row>

		<a-row :gutter="16">
			<a-col :span="12">
				<a-form-item label="支付宝公钥证书路径" name="alipayCertPath">
					<a-input
						v-model:value="configData.alipayCertPath"
						placeholder="请输入支付宝公钥证书路径"
						@change="handleChange"
					/>
				</a-form-item>
			</a-col>
			<a-col :span="12">
				<a-form-item label="签名类型" name="signType">
					<a-select v-model:value="configData.signType" placeholder="请选择签名类型" @change="handleChange">
						<a-select-option value="RSA2">RSA2</a-select-option>
						<a-select-option value="RSA">RSA</a-select-option>
					</a-select>
				</a-form-item>
			</a-col>
		</a-row>

		<a-row :gutter="16">
			<a-col :span="12">
				<a-form-item label="环境模式" name="sandboxMode">
					<a-select v-model:value="configData.sandboxMode" placeholder="请选择环境模式" @change="handleChange">
						<a-select-option :value="0">正式环境</a-select-option>
						<a-select-option :value="1">沙箱环境</a-select-option>
					</a-select>
				</a-form-item>
			</a-col>
			<a-col :span="12">
				<a-form-item label="字符集" name="charset">
					<a-select v-model:value="configData.charset" placeholder="请选择字符集" @change="handleChange">
						<a-select-option value="UTF-8">UTF-8</a-select-option>
						<a-select-option value="GBK">GBK</a-select-option>
					</a-select>
				</a-form-item>
			</a-col>
		</a-row>

		<a-row :gutter="16">
			<a-col :span="12">
				<a-form-item label="支付回调URL" name="notifyUrl">
					<a-input v-model:value="configData.notifyUrl" placeholder="请输入支付回调URL" @change="handleChange" />
				</a-form-item>
			</a-col>
			<a-col :span="12">
				<a-form-item label="跳转URL" name="returnUrl">
					<a-input
						v-model:value="configData.returnUrl"
						placeholder="请输入支付完成后的跳转URL"
						@change="handleChange"
					/>
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
		appId: '',
		merchantId: '',
		privateKey: '',
		publicKey: '',
		certPath: '',
		rootCertPath: '',
		alipayCertPath: '',
		signType: 'RSA2',
		sandboxMode: 0,
		charset: 'UTF-8',
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
	.alipay-config {
		padding: 16px;
		background: #fafafa;
		border-radius: 6px;
		margin-bottom: 16px;
	}
</style>
