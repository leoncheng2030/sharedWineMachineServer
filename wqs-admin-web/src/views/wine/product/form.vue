<template>
	<xn-form-container
		:title="formData.id ? '编辑酒品' : '新增酒品'"
		:width="800"
		:visible="visible"
		:destroy-on-close="true"
		:body-style="{ 'padding-top': '0px' }"
		@close="onClose"
	>
		<a-form ref="formRef" :model="formData" :rules="rules" layout="vertical">
			<a-tabs v-model:activeKey="activeTabsKey">
				<a-tab-pane key="1" tab="基础信息" force-render>
					<a-row :gutter="16">
						<a-col :span="12">
							<a-form-item label="酒品名称" name="productName">
								<a-input v-model:value="formData.productName" placeholder="请输入酒品名称" allow-clear />
							</a-form-item>
						</a-col>
						<a-col :span="12">
							<a-form-item label="酒品编码" name="productCode">
								<a-input v-model:value="formData.productCode" placeholder="请输入酒品编码" allow-clear />
							</a-form-item>
						</a-col>
					</a-row>

					<a-row :gutter="16">
						<a-col :span="12">
							<a-form-item label="酒品分类" name="categoryId">
								<a-tree-select
									v-model:value="formData.categoryId"
									class="xn-wd"
									:dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
									placeholder="请选择酒品分类"
									allow-clear
									tree-default-expand-all
									:tree-data="categoryData"
									:field-names="{ children: 'children', label: 'categoryName', value: 'id' }"
								/>
							</a-form-item>
						</a-col>
						<a-col :span="12">
							<a-form-item label="品牌" name="brand">
								<a-input v-model:value="formData.brand" placeholder="请输入品牌名称" allow-clear />
							</a-form-item>
						</a-col>
					</a-row>

					<a-row :gutter="16">
						<a-col :span="8">
							<a-form-item label="基础单价(元/ml)" name="unitPrice">
								<a-input-number
									v-model:value="formData.unitPrice"
									:min="0"
									:precision="4"
									placeholder="请输入基础单价"
									class="xn-wd"
									:formatter="(value) => `¥ ${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
									:parser="(value) => value.replace(/¥\s?|(,*)/g, '')"
								/>
								<div class="xn-mt4 xn-color-placeholder">
									<small>💡 所有容量的价格计算基础</small>
								</div>
							</a-form-item>
						</a-col>
						<a-col :span="8">
							<a-form-item label="建议零售价" name="suggestedPrice">
								<a-input-number
									v-model:value="formData.suggestedPrice"
									:min="0"
									:precision="2"
									placeholder="请输入建议零售价"
									class="xn-wd"
									:formatter="(value) => `¥ ${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
									:parser="(value) => value.replace(/¥\s?|(,*)/g, '')"
								/>
							</a-form-item>
						</a-col>
						<a-col :span="8">
							<a-form-item label="成本价" name="costPrice">
								<a-input-number
									v-model:value="formData.costPrice"
									:min="0"
									:precision="2"
									placeholder="请输入成本价"
									class="xn-wd"
									:formatter="(value) => `¥ ${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
									:parser="(value) => value.replace(/¥\s?|(,*)/g, '')"
								/>
							</a-form-item>
						</a-col>
					</a-row>

					<a-row :gutter="16">
						<a-col :span="12">
							<a-form-item label="酒精度" name="alcoholContent">
								<a-input-number
									v-model:value="formData.alcoholContent"
									:min="0"
									:max="100"
									:precision="1"
									placeholder="请输入酒精度"
									class="xn-wd"
									addon-after="%"
								/>
							</a-form-item>
						</a-col>
						<a-col :span="12">
							<a-form-item label="容量" name="volume">
								<a-input-number
									v-model:value="formData.volume"
									:min="1"
									placeholder="请输入容量"
									class="xn-wd"
									addon-after="ml"
								/>
							</a-form-item>
						</a-col>
					</a-row>

					<a-row :gutter="16">
						<a-col :span="12">
							<a-form-item label="产地" name="origin">
								<a-input v-model:value="formData.origin" placeholder="请输入产地" allow-clear />
							</a-form-item>
						</a-col>
						<a-col :span="12">
							<a-form-item label="年份" name="vintage">
								<a-input-number
									v-model:value="formData.vintage"
									:min="1900"
									:max="new Date().getFullYear()"
									placeholder="请输入年份"
									class="xn-wd"
								/>
							</a-form-item>
						</a-col>
					</a-row>

					<a-row :gutter="16">
						<a-col :span="12">
							<a-form-item label="生产厂家" name="manufacturer">
								<a-input v-model:value="formData.manufacturer" placeholder="请输入生产厂家" allow-clear />
							</a-form-item>
						</a-col>
						<a-col :span="12">
							<a-form-item label="供应商" name="supplierId">
								<xn-client-user-selector
									ref="supplierSelectorRef"
									:radio-model="true"
									:clientUserPageApi="selectorApiFunction.clientUserPageApi"
									:clientUserListByIdListApi="selectorApiFunction.clientUserListByIdListApi"
									v-model:value="formData.supplierId"
									@onBack="supplierSelectorOnBack"
								/>
							</a-form-item>
						</a-col>
					</a-row>

					<a-form-item label="状态" name="status">
						<a-radio-group v-model:value="formData.status">
							<a-radio value="ENABLE">启用</a-radio>
							<a-radio value="DISABLE">禁用</a-radio>
						</a-radio-group>
					</a-form-item>

					<a-form-item label="库存预警" name="stockWarning">
						<a-input-number
							v-model:value="formData.stockWarning"
							:min="0"
							placeholder="请输入库存预警数量"
							class="xn-wd"
							addon-after="瓶"
						/>
					</a-form-item>
				</a-tab-pane>

				<a-tab-pane key="2" tab="详细信息" force-render>
					<a-form-item label="酒品图片" name="imageUrl">
						<a-upload
							v-model:file-list="fileList"
							list-type="picture-card"
							:max-count="1"
							:before-upload="beforeUpload"
							@preview="handlePreview"
							@remove="handleRemove"
							@change="handleChange"
						>
							<div v-if="fileList.length < 1">
								<PlusOutlined />
								<div style="margin-top: 8px">上传图片</div>
							</div>
						</a-upload>
						<a-modal :open="previewVisible" :title="previewTitle" :footer="null" @cancel="handleCancel">
							<img alt="preview" style="width: 100%" :src="previewImage" />
						</a-modal>
					</a-form-item>

					<a-form-item label="描述" name="description">
						<a-textarea
							v-model:value="formData.description"
							placeholder="请输入酒品描述"
							:rows="4"
							:maxlength="500"
							show-count
						/>
					</a-form-item>

					<a-form-item label="备注" name="remark">
						<a-textarea
							v-model:value="formData.remark"
							placeholder="请输入备注信息"
							:rows="3"
							:maxlength="200"
							show-count
						/>
					</a-form-item>
				</a-tab-pane>
			</a-tabs>
		</a-form>

		<template #footer>
			<a-button class="xn-mr8" @click="onClose">取消</a-button>
			<a-button type="primary" :loading="submitLoading" @click="onSubmit">保存</a-button>
		</template>
	</xn-form-container>
</template>

<script setup name="wineProductForm">
	import { message } from 'ant-design-vue'
	import { cloneDeep } from 'lodash-es'
	import { required } from '@/utils/formRules'
	import wineProductApi from '@/api/wine/wineProductApi'
	import wineCategoryApi from '@/api/wine/wineCategoryApi'
	import clientUserApi from '@/api/client/clientUserApi'

	const emit = defineEmits(['successful'])

	const visible = ref(false)
	const submitLoading = ref(false)
	const formRef = ref()
	const activeTabsKey = ref('1')
	const supplierSelectorRef = ref()

	// 表单数据
	const formData = ref({})

	// 分类数据
	const categoryData = ref([])

	// 选择器API配置
	const selectorApiFunction = {
		clientUserPageApi: (param) => {
			return clientUserApi.userPage(param).then((data) => {
				return Promise.resolve(data)
			})
		},
		clientUserListByIdListApi: (param) => {
			return clientUserApi.userListByIdList(param).then((data) => {
				return Promise.resolve(data)
			})
		}
	}

	// 图片上传相关
	const fileList = ref([])
	const previewVisible = ref(false)
	const previewImage = ref('')
	const previewTitle = ref('')

	// 表单验证规则
	const rules = {
		productName: [required('请输入酒品名称')],
		productCode: [required('请输入酒品编码')],
		categoryId: [required('请选择酒品分类')],
		supplierId: [required('请选择供应商')],
		unitPrice: [required('请输入基础单价'), { type: 'number', min: 0.0001, message: '基础单价必须大于0' }],
		suggestedPrice: [{ type: 'number', min: 0, message: '建议零售价不能小于0' }],
		alcoholContent: [{ type: 'number', min: 0, max: 100, message: '酒精度必须在0-100之间' }],
		volume: [{ type: 'number', min: 1, message: '容量必须大于0' }],
		status: [required('请选择状态')]
	}

	// 获取分类数据
	const getCategoryData = () => {
		wineCategoryApi.categoryTree({}).then((data) => {
			categoryData.value = data
		})
	}

	// 供应商选择器回调
	const supplierSelectorOnBack = (data) => {
		if (data && data.length > 0) {
			console.log('选择的供应商:', data[0])
		}
	}

	// 图片上传相关方法
	const beforeUpload = (file) => {
		const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png'
		if (!isJpgOrPng) {
			message.error('只能上传 JPG/PNG 格式的图片!')
			return false
		}
		const isLt2M = file.size / 1024 / 1024 < 2
		if (!isLt2M) {
			message.error('图片大小不能超过 2MB!')
			return false
		}
		return false // 阻止自动上传
	}

	const handlePreview = async (file) => {
		if (!file.url && !file.preview) {
			file.preview = await getBase64(file.originFileObj)
		}
		previewImage.value = file.url || file.preview
		previewVisible.value = true
		previewTitle.value = file.name || file.url.substring(file.url.lastIndexOf('/') + 1)
	}

	const handleCancel = () => {
		previewVisible.value = false
		previewTitle.value = ''
	}

	const handleRemove = () => {
		formData.value.imageUrl = ''
	}

	const handleChange = ({ fileList: newFileList }) => {
		fileList.value = newFileList
		if (newFileList.length > 0) {
			const file = newFileList[0]
			if (file.originFileObj) {
				// 这里应该调用上传API
				// 暂时使用base64
				getBase64(file.originFileObj).then((base64) => {
					formData.value.imageUrl = base64
				})
			}
		}
	}

	const getBase64 = (file) => {
		return new Promise((resolve, reject) => {
			const reader = new FileReader()
			reader.readAsDataURL(file)
			reader.onload = () => resolve(reader.result)
			reader.onerror = (error) => reject(error)
		})
	}

	// 打开表单
	const onOpen = (record) => {
		visible.value = true
		activeTabsKey.value = '1'
		getCategoryData()

		if (record) {
			formData.value = cloneDeep(record)
			// 处理图片显示
			if (record.imageUrl) {
				fileList.value = [
					{
						uid: '-1',
						name: 'image.png',
						status: 'done',
						url: record.imageUrl
					}
				]
			}
		} else {
			formData.value = {
				status: 'ENABLE'
			}
			fileList.value = []
		}
	}

	// 关闭表单
	const onClose = () => {
		visible.value = false
		formRef.value?.resetFields()
		formData.value = {}
		fileList.value = []
	}

	// 提交表单
	const onSubmit = () => {
		formRef.value.validate().then(() => {
			submitLoading.value = true
			const api = formData.value.id
				? wineProductApi.submitForm(formData.value, true)
				: wineProductApi.submitForm(formData.value)

			api
				.then(() => {
					onClose()
					emit('successful')
				})
				.finally(() => {
					submitLoading.value = false
				})
		})
	}

	// 暴露方法给父组件
	defineExpose({
		onOpen
	})
</script>

<style scoped>
	:deep(.ant-upload-select-picture-card i) {
		font-size: 32px;
		color: #999;
	}

	:deep(.ant-upload-select-picture-card .ant-upload-text) {
		margin-top: 8px;
		color: #666;
	}
</style>
