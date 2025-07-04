<template>
	<a-modal
		:open="props.visible"
		:title="formData.id ? '编辑支付配置' : '新增支付配置'"
		:width="1200"
		:maskClosable="false"
		@cancel="handleCancel"
		@ok="handleOk"
	>
		<a-form ref="formRef" :model="formData" layout="horizontal">
			<!-- 基础配置 -->
			<a-card title="基础配置" size="small" style="margin-bottom: 16px">
				<a-row :gutter="16">
					<a-col :span="12">
						<a-form-item label="配置名称" name="configName" :rules="[{ required: true, message: '请输入配置名称' }]">
							<a-input v-model:value="formData.configName" placeholder="请输入配置名称" />
						</a-form-item>
					</a-col>
					<a-col :span="12">
						<a-form-item label="支付方式" name="payType" :rules="[{ required: true, message: '请选择支付方式' }]">
							<a-select v-model:value="formData.payType" placeholder="请选择支付方式" @change="handlePayTypeChange">
								<a-select-option value="WECHAT_MINI">微信小程序</a-select-option>
								<a-select-option value="WECHAT_H5">微信H5</a-select-option>
								<a-select-option value="WECHAT_JSAPI">微信JSAPI</a-select-option>
								<a-select-option value="WECHAT_NATIVE">微信扫码</a-select-option>
								<a-select-option value="ALIPAY_PC">支付宝PC</a-select-option>
								<a-select-option value="ALIPAY_WAP">支付宝WAP</a-select-option>
								<a-select-option value="ALIPAY_APP">支付宝APP</a-select-option>
								<a-select-option value="BALANCE">余额支付</a-select-option>
							</a-select>
						</a-form-item>
					</a-col>
				</a-row>

				<a-row :gutter="16">
					<a-col :span="12">
						<a-form-item label="状态" name="status">
							<a-select v-model:value="formData.status" placeholder="请选择状态">
								<a-select-option value="ENABLE">启用</a-select-option>
								<a-select-option value="DISABLE">禁用</a-select-option>
							</a-select>
						</a-form-item>
					</a-col>
					<a-col :span="12">
						<a-form-item label="排序码" name="sortCode">
							<a-input-number v-model:value="formData.sortCode" placeholder="请输入排序码" style="width: 100%" />
						</a-form-item>
					</a-col>
				</a-row>

				<a-row :gutter="16">
					<a-col :span="24">
						<a-form-item label="备注" name="remark">
							<a-textarea v-model:value="formData.remark" placeholder="请输入备注" :rows="2" />
						</a-form-item>
					</a-col>
				</a-row>
			</a-card>

			<!-- 支付方式配置 -->
			<a-card :title="getPayTypeTitle + '配置'" size="small" style="margin-bottom: 16px">
				<!-- 微信支付配置 -->
				<WechatPayConfig v-if="isWechatPay" v-model="payConfig" />

				<!-- 支付宝支付配置 -->
				<AlipayConfig v-if="isAlipay" v-model="payConfig" />

				<!-- 余额支付配置 -->
				<BalancePayConfig v-if="isBalancePay" v-model="payConfig" />
			</a-card>
		</a-form>
	</a-modal>
</template>

<script setup>
	import { computed, reactive, ref } from 'vue'
	import { message } from 'ant-design-vue'
	import paymentConfigApi from '@/api/payment/paymentConfigApi'
	import WechatPayConfig from './components/WechatPayConfig.vue'
	import AlipayConfig from './components/AlipayConfig.vue'
	import BalancePayConfig from './components/BalancePayConfig.vue'

	// 组件属性
	const props = defineProps({
		visible: {
			type: Boolean,
			default: false
		}
	})

	// 事件定义
	const emit = defineEmits(['update:visible', 'successful'])

	// 表单引用
	const formRef = ref()

	// 表单数据
	const formData = reactive({
		id: undefined,
		configName: '',
		payType: '',
		status: 'ENABLE',
		sortCode: 100,
		remark: ''
	})

	// 支付配置数据
	const payConfig = ref({})

	// 计算属性：判断支付类型
	const isWechatPay = computed(() => {
		return formData.payType && formData.payType.startsWith('WECHAT')
	})

	const isAlipay = computed(() => {
		return formData.payType && formData.payType.startsWith('ALIPAY')
	})

	const isBalancePay = computed(() => {
		return formData.payType === 'BALANCE'
	})

	// 获取支付方式标题
	const getPayTypeTitle = computed(() => {
		if (isWechatPay.value) return '微信支付'
		if (isAlipay.value) return '支付宝支付'
		if (isBalancePay.value) return '余额支付'
		return '支付方式'
	})

	// 支付方式变化处理
	const handlePayTypeChange = () => {
		// 清空之前的配置
		payConfig.value = {}

		// 根据支付方式设置默认配置
		if (isWechatPay.value) {
			payConfig.value = {
				sandboxMode: 0,
				notifyUrl: '',
				refundNotifyUrl: '',
				returnUrl: ''
			}
		} else if (isAlipay.value) {
			payConfig.value = {
				signType: 'RSA2',
				sandboxMode: 0,
				charset: 'UTF-8',
				notifyUrl: '',
				returnUrl: ''
			}
		} else if (isBalancePay.value) {
			payConfig.value = {
				minAmount: 0,
				maxAmount: 0,
				requirePassword: false,
				allowOverdraft: false,
				overdraftLimit: 0,
				overdraftFeeRate: 0,
				notifyUrl: '',
				returnUrl: ''
			}
		}
	}

	// 设置表单数据
	const setFormData = (data) => {
		Object.assign(formData, data)

		// 解析JSON配置
		if (data.extJson) {
			try {
				const config = JSON.parse(data.extJson)
				payConfig.value = config
			} catch (error) {
				console.error('解析JSON配置失败:', error)
				message.error('配置数据格式错误')
			}
		}
	}

	// 处理确定
	const handleOk = () => {
		formRef.value
			.validate()
			.then(() => {
				const param = {
					...formData,
					extJson: JSON.stringify(payConfig.value)
				}

				const api = formData.id ? paymentConfigApi.paymentConfigEdit : paymentConfigApi.paymentConfigAdd
				api(param).then(() => {
					message.success(formData.id ? '编辑成功' : '新增成功')
					handleCancel()
					emit('successful')
				})
			})
			.catch(() => {
				message.error('请检查表单数据')
			})
	}

	// 处理取消
	const handleCancel = () => {
		emit('update:visible', false)
		formRef.value?.resetFields()

		// 重置数据
		Object.assign(formData, {
			id: undefined,
			configName: '',
			payType: '',
			status: 'ENABLE',
			sortCode: 100,
			remark: ''
		})

		payConfig.value = {}
	}

	// 暴露方法
	defineExpose({
		setFormData
	})
</script>

<style scoped>
	:deep(.ant-card-head) {
		min-height: 40px;
	}

	:deep(.ant-card-head-title) {
		font-size: 14px;
		font-weight: 600;
	}

	:deep(.ant-card-body) {
		padding: 16px;
	}
</style>
