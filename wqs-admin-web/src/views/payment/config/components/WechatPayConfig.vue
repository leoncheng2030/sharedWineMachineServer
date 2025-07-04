<template>
	<div class="wechat-pay-config">
		<a-row :gutter="16">
			<a-col :span="12">
				<a-form-item label="微信支付应用id" name="appId">
					<a-input v-model:value="configData.appId" placeholder="请输入微信应用ID" @change="handleChange" />
				</a-form-item>
			</a-col>
			<a-col :span="12">
				<a-form-item label="微信支付公众号AppSecret" name="appSecret">
					<a-input-password
						v-model:value="configData.appSecret"
						placeholder="请输入公众号AppSecret"
						@change="handleChange"
					/>
				</a-form-item>
			</a-col>
		</a-row>

		<a-row :gutter="16">
			<a-col :span="12">
				<a-form-item label="微信支付商户号" name="merchantId">
					<a-input v-model:value="configData.merchantId" placeholder="请输入微信商户号" @change="handleChange" />
				</a-form-item>
			</a-col>
			<a-col :span="12">
				<a-form-item label="微信支付商户V2密钥" name="merchantKey">
					<a-input-password
						v-model:value="configData.merchantKey"
						placeholder="请输入商户V2密钥"
						@change="handleChange"
					/>
				</a-form-item>
			</a-col>
		</a-row>

		<a-row :gutter="16">
			<a-col :span="12">
				<a-form-item label="微信支付ApiV3证书序列号值" name="certSerialNumber">
					<a-input v-model:value="configData.certSerialNumber" placeholder="请输入证书序列号" @change="handleChange" />
				</a-form-item>
			</a-col>
			<a-col :span="12">
				<a-form-item label="微信支付ApiV3密钥值" name="merchantKeyV3">
					<a-input-password
						v-model:value="configData.merchantKeyV3"
						placeholder="请输入ApiV3密钥值"
						@change="handleChange"
					/>
				</a-form-item>
			</a-col>
			<a-col :span="12">
				<a-form-item label="微信支付p12证书文件" name="certPath">
					<a-upload :file-list="certFileList" :before-upload="() => false" @change="handleCertUpload" accept=".p12">
						<a-button>
							<upload-outlined />
							选择p12证书文件
						</a-button>
					</a-upload>
					<div v-if="configData.certPath" style="margin-top: 8px; color: #52c41a">
						当前文件：{{ getFileName(configData.certPath) }}
					</div>
				</a-form-item>
			</a-col>
			<a-col :span="12">
				<a-form-item label="微信支付apiClientKey文件" name="privateKeyPath">
					<a-upload
						:file-list="privateKeyFileList"
						:before-upload="() => false"
						@change="handlePrivateKeyUpload"
						accept=".pem,.key"
					>
						<a-button>
							<upload-outlined />
							选择apiClientKey文件
						</a-button>
					</a-upload>
					<div v-if="configData.privateKeyPath" style="margin-top: 8px; color: #52c41a">
						当前文件：{{ getFileName(configData.privateKeyPath) }}
					</div>
				</a-form-item>
			</a-col>
		</a-row>

		<a-row :gutter="16">
			<a-col :span="12">
				<a-form-item label="微信支付apiClientCert文件" name="apiClientCertPath">
					<a-upload
						:file-list="apiClientCertFileList"
						:before-upload="() => false"
						@change="handleApiClientCertUpload"
						accept=".pem,.crt"
					>
						<a-button>
							<upload-outlined />
							选择apiClientCert文件
						</a-button>
					</a-upload>
					<div v-if="configData.apiClientCertPath" style="margin-top: 8px; color: #52c41a">
						当前文件：{{ getFileName(configData.apiClientCertPath) }}
					</div>
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
		</a-row>

		<a-row :gutter="16">
			<a-col :span="12">
				<a-form-item label="微信支付回调地址" name="notifyUrl">
					<a-input v-model:value="configData.notifyUrl" placeholder="请输入支付回调地址" @change="handleChange" />
				</a-form-item>
			</a-col>
			<a-col :span="12">
				<a-form-item label="微信支付退款回调地址" name="refundNotifyUrl">
					<a-input v-model:value="configData.refundNotifyUrl" placeholder="请输入退款回调地址" @change="handleChange" />
				</a-form-item>
			</a-col>
		</a-row>

		<a-row :gutter="16">
			<a-col :span="24">
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
	import { reactive, ref, watch } from 'vue'
	import { UploadOutlined } from '@ant-design/icons-vue'
	import { message } from 'ant-design-vue'

	// 组件属性
	const props = defineProps({
		modelValue: {
			type: Object,
			default: () => ({})
		}
	})

	// 事件定义
	const emit = defineEmits(['update:modelValue'])

	// 文件列表
	const certFileList = ref([])
	const privateKeyFileList = ref([])
	const apiClientCertFileList = ref([])

	// 配置数据
	const configData = reactive({
		appId: '',
		appSecret: '',
		merchantId: '',
		merchantKey: '',
		merchantKeyV3: '',
		certPath: '',
		certSerialNumber: '',
		privateKeyPath: '',
		apiClientCertPath: '',
		sandboxMode: 0,
		notifyUrl: '',
		refundNotifyUrl: '',
		returnUrl: '',
		...props.modelValue
	})

	// 处理数据变化
	const handleChange = () => {
		emit('update:modelValue', { ...configData })
	}

	// 获取文件名
	const getFileName = (path) => {
		if (!path) return ''
		return path.split('/').pop() || path.split('\\').pop() || path
	}

	// 模拟文件上传到服务器
	const uploadFile = async (file) => {
		// 这里应该调用实际的文件上传API
		// 暂时返回一个模拟的路径
		const timestamp = Date.now()
		const fileName = file.name
		const mockPath = `/upload/payment/certs/${timestamp}_${fileName}`

		// 模拟上传延迟
		await new Promise((resolve) => setTimeout(resolve, 1000))

		return mockPath
	}

	// 处理p12证书文件上传
	const handleCertUpload = async ({ file, fileList }) => {
		certFileList.value = fileList

		if (file.status !== 'removed') {
			try {
				message.loading('正在上传p12证书文件...', 0)
				const uploadedPath = await uploadFile(file)
				configData.certPath = uploadedPath
				handleChange()
				message.destroy()
				message.success('p12证书文件上传成功')
			} catch (error) {
				message.destroy()
				message.error('p12证书文件上传失败')
			}
		} else {
			configData.certPath = ''
			handleChange()
		}
	}

	// 处理私钥文件上传
	const handlePrivateKeyUpload = async ({ file, fileList }) => {
		privateKeyFileList.value = fileList

		if (file.status !== 'removed') {
			try {
				message.loading('正在上传私钥文件...', 0)
				const uploadedPath = await uploadFile(file)
				configData.privateKeyPath = uploadedPath
				handleChange()
				message.destroy()
				message.success('私钥文件上传成功')
			} catch (error) {
				message.destroy()
				message.error('私钥文件上传失败')
			}
		} else {
			configData.privateKeyPath = ''
			handleChange()
		}
	}

	// 处理API客户端证书文件上传
	const handleApiClientCertUpload = async ({ file, fileList }) => {
		apiClientCertFileList.value = fileList

		if (file.status !== 'removed') {
			try {
				message.loading('正在上传API客户端证书文件...', 0)
				const uploadedPath = await uploadFile(file)
				configData.apiClientCertPath = uploadedPath
				handleChange()
				message.destroy()
				message.success('API客户端证书文件上传成功')
			} catch (error) {
				message.destroy()
				message.error('API客户端证书文件上传失败')
			}
		} else {
			configData.apiClientCertPath = ''
			handleChange()
		}
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
	.wechat-pay-config {
		padding: 16px;
		background: #fafafa;
		border-radius: 6px;
		margin-bottom: 16px;
	}

	:deep(.ant-upload) {
		width: 100%;
	}

	:deep(.ant-upload .ant-btn) {
		width: 100%;
		height: 32px;
		display: flex;
		align-items: center;
		justify-content: center;
		gap: 8px;
	}

	:deep(.ant-upload-list) {
		margin-top: 8px;
	}

	.file-info {
		margin-top: 8px;
		font-size: 12px;
		color: #52c41a;
		display: flex;
		align-items: center;
		gap: 4px;
	}
</style>
