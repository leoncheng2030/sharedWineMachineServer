<template>
	<xn-form-container
		:title="formData.id ? '编辑分类' : parentId ? '新增子分类' : '新增分类'"
		:width="600"
		:visible="visible"
		:destroy-on-close="true"
		:body-style="{ 'padding-top': '0px' }"
		@close="onClose"
	>
		<a-form ref="formRef" :model="formData" :rules="rules" layout="vertical">
			<a-form-item label="父级分类" name="parentId">
				<a-tree-select
					v-model:value="formData.parentId"
					:tree-data="parentCategoryData"
					:field-names="{ children: 'children', label: 'categoryName', value: 'id' }"
					placeholder="请选择父级分类（不选择则为顶级分类）"
					allow-clear
					tree-default-expand-all
					class="xn-wd"
					:dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
				/>
			</a-form-item>

			<a-row :gutter="16">
				<a-col :span="12">
					<a-form-item label="分类名称" name="categoryName">
						<a-input v-model:value="formData.categoryName" placeholder="请输入分类名称" allow-clear />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="分类编码" name="categoryCode">
						<a-input v-model:value="formData.categoryCode" placeholder="请输入分类编码" allow-clear />
					</a-form-item>
				</a-col>
			</a-row>

			<a-form-item label="分类图标" name="icon">
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
						<div style="margin-top: 8px">上传图标</div>
					</div>
				</a-upload>
				<a-modal :open="previewVisible" :title="previewTitle" :footer="null" @cancel="handleCancel">
					<img alt="preview" style="width: 100%" :src="previewImage" />
				</a-modal>
			</a-form-item>

			<a-row :gutter="16">
				<a-col :span="12">
					<a-form-item label="排序号" name="sortCode">
						<a-input-number
							v-model:value="formData.sortCode"
							:min="1"
							:max="9999"
							placeholder="请输入排序号"
							class="xn-wd"
						/>
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="状态" name="status">
						<a-radio-group v-model:value="formData.status">
							<a-radio value="ENABLE">启用</a-radio>
							<a-radio value="DISABLE">禁用</a-radio>
						</a-radio-group>
					</a-form-item>
				</a-col>
			</a-row>

			<a-form-item label="分类描述" name="description">
				<a-textarea
					v-model:value="formData.description"
					placeholder="请输入分类描述"
					:rows="4"
					:maxlength="200"
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
		</a-form>

		<template #footer>
			<a-button class="xn-mr8" @click="onClose">取消</a-button>
			<a-button type="primary" :loading="submitLoading" @click="onSubmit">保存</a-button>
		</template>
	</xn-form-container>
</template>

<script setup name="wineCategoryForm">
	import { message } from 'ant-design-vue'
	import { cloneDeep } from 'lodash-es'
	import { required } from '@/utils/formRules'
	import wineCategoryApi from '@/api/wine/wineCategoryApi'

	const emit = defineEmits(['successful'])

	const visible = ref(false)
	const submitLoading = ref(false)
	const formRef = ref()
	const parentId = ref(undefined)

	// 表单数据
	const formData = ref({})

	// 父级分类数据
	const parentCategoryData = ref([])

	// 图片上传相关
	const fileList = ref([])
	const previewVisible = ref(false)
	const previewImage = ref('')
	const previewTitle = ref('')

	// 表单验证规则
	const rules = {
		categoryName: [required('请输入分类名称')],
		categoryCode: [
			required('请输入分类编码'),
			{ pattern: /^[A-Za-z0-9_]+$/, message: '分类编码只能包含字母、数字和下划线', trigger: 'blur' }
		],
		sortCode: [required('请输入排序号'), { type: 'number', min: 1, max: 9999, message: '排序号必须在1-9999之间' }],
		status: [required('请选择状态')]
	}

	// 获取父级分类数据
	const getParentCategoryData = () => {
		wineCategoryApi.categoryTree({}).then((data) => {
			parentCategoryData.value = data
		})
	}

	// 图片上传相关方法
	const beforeUpload = (file) => {
		const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png'
		if (!isJpgOrPng) {
			message.error('只能上传 JPG/PNG 格式的图片!')
			return false
		}
		const isLt1M = file.size / 1024 / 1024 < 1
		if (!isLt1M) {
			message.error('图片大小不能超过 1MB!')
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
		formData.value.icon = ''
	}

	const handleChange = ({ fileList: newFileList }) => {
		fileList.value = newFileList
		if (newFileList.length > 0) {
			const file = newFileList[0]
			if (file.originFileObj) {
				// 这里应该调用上传API
				// 暂时使用base64
				getBase64(file.originFileObj).then((base64) => {
					formData.value.icon = base64
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
	const onOpen = (record, pid) => {
		visible.value = true
		parentId.value = pid
		getParentCategoryData()

		if (record) {
			formData.value = cloneDeep(record)
			// 处理图标显示
			if (record.icon) {
				fileList.value = [
					{
						uid: '-1',
						name: 'icon.png',
						status: 'done',
						url: record.icon
					}
				]
			}
		} else {
			formData.value = {
				status: 'ENABLE',
				sortCode: 1
			}
			if (pid) {
				formData.value.parentId = pid
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
		parentId.value = undefined
	}

	// 提交表单
	const onSubmit = () => {
		formRef.value.validate().then(() => {
			submitLoading.value = true
			const api = formData.value.id
				? wineCategoryApi.submitForm(formData.value, true)
				: wineCategoryApi.submitForm(formData.value)

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
