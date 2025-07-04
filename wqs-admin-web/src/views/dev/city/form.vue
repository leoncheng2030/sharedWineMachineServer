<template>
	<a-modal
		v-model:open="visible"
		:title="formData.id ? '编辑城市' : '新增城市'"
		:width="900"
		:confirm-loading="submitLoading"
		@ok="onSubmit"
		@cancel="onClose"
	>
		<a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical">
			<!-- 基本信息 -->
			<a-divider orientation="left">基本信息</a-divider>
			<a-row :gutter="16">
				<a-col :span="12">
					<a-form-item label="城市编码" name="cityCode">
						<a-input v-model:value="formData.cityCode" placeholder="请输入城市编码" :maxlength="20" />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="城市名称" name="cityName">
						<a-input v-model:value="formData.cityName" placeholder="请输入城市名称" :maxlength="100" />
					</a-form-item>
				</a-col>
			</a-row>
			<a-row :gutter="16">
				<a-col :span="8">
					<a-form-item label="城市层级" name="level">
						<a-select
							v-model:value="formData.level"
							placeholder="请选择城市层级"
							:getPopupContainer="(trigger) => trigger.parentNode"
							@change="onLevelChange"
						>
							<a-select-option :value="1">省份</a-select-option>
							<a-select-option :value="2">城市</a-select-option>
							<a-select-option :value="3">区县</a-select-option>
						</a-select>
					</a-form-item>
				</a-col>
				<a-col :span="8">
					<a-form-item label="省份编码" name="provinceCode">
						<a-input v-model:value="formData.provinceCode" placeholder="请输入省份编码" :maxlength="20" />
					</a-form-item>
				</a-col>
				<a-col :span="8">
					<a-form-item label="父级编码" name="parentCode">
						<a-input v-model:value="formData.parentCode" placeholder="请输入父级编码" :maxlength="20" />
					</a-form-item>
				</a-col>
			</a-row>

			<!-- 地理信息 -->
			<a-divider orientation="left">地理信息</a-divider>
			<a-row :gutter="16">
				<a-col :span="12">
					<a-form-item label="经度" name="longitude">
						<a-input-number
							v-model:value="formData.longitude"
							placeholder="请输入经度"
							:precision="6"
							:min="-180"
							:max="180"
							style="width: 100%"
						/>
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="纬度" name="latitude">
						<a-input-number
							v-model:value="formData.latitude"
							placeholder="请输入纬度"
							:precision="6"
							:min="-90"
							:max="90"
							style="width: 100%"
						/>
					</a-form-item>
				</a-col>
			</a-row>
			<a-row :gutter="16">
				<a-col :span="12">
					<a-form-item label="区号" name="areaCode">
						<a-input v-model:value="formData.areaCode" placeholder="请输入区号" :maxlength="10" />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="邮政编码" name="zipCode">
						<a-input v-model:value="formData.zipCode" placeholder="请输入邮政编码" :maxlength="10" />
					</a-form-item>
				</a-col>
			</a-row>

			<!-- 扩展信息 -->
			<a-divider orientation="left">扩展信息</a-divider>
			<a-row :gutter="16">
				<a-col :span="12">
					<a-form-item label="拼音" name="pinyin">
						<a-input v-model:value="formData.pinyin" placeholder="请输入拼音" :maxlength="100" />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="简称" name="shortName">
						<a-input v-model:value="formData.shortName" placeholder="请输入简称" :maxlength="50" />
					</a-form-item>
				</a-col>
			</a-row>

			<!-- 业务配置 -->
			<a-divider orientation="left">业务配置</a-divider>
			<a-row :gutter="16">
				<a-col :span="8">
					<a-form-item label="是否热门" name="isHot">
						<a-select
							v-model:value="formData.isHot"
							placeholder="请选择是否热门"
							:getPopupContainer="(trigger) => trigger.parentNode"
						>
							<a-select-option value="Y">是</a-select-option>
							<a-select-option value="N">否</a-select-option>
						</a-select>
					</a-form-item>
				</a-col>
				<a-col :span="8">
					<a-form-item label="配送支持" name="supportDelivery">
						<a-select
							v-model:value="formData.supportDelivery"
							placeholder="请选择配送支持"
							:getPopupContainer="(trigger) => trigger.parentNode"
						>
							<a-select-option value="Y">支持</a-select-option>
							<a-select-option value="N">不支持</a-select-option>
						</a-select>
					</a-form-item>
				</a-col>
				<a-col :span="8">
					<a-form-item label="城市状态" name="status">
						<a-select
							v-model:value="formData.status"
							placeholder="请选择城市状态"
							:getPopupContainer="(trigger) => trigger.parentNode"
						>
							<a-select-option value="ENABLE">启用</a-select-option>
							<a-select-option value="DISABLE">禁用</a-select-option>
						</a-select>
					</a-form-item>
				</a-col>
			</a-row>

			<!-- 其他信息 -->
			<a-divider orientation="left">其他信息</a-divider>
			<a-row :gutter="16">
				<a-col :span="24">
					<a-form-item label="城市描述" name="description">
						<a-textarea
							v-model:value="formData.description"
							placeholder="请输入城市描述"
							:maxlength="500"
							:rows="3"
							show-count
						/>
					</a-form-item>
				</a-col>
			</a-row>
			<a-row :gutter="16">
				<a-col :span="24">
					<a-form-item label="排序码" name="sortCode">
						<a-input-number
							v-model:value="formData.sortCode"
							placeholder="请输入排序码"
							:min="1"
							:max="9999"
							style="width: 100%"
						/>
					</a-form-item>
				</a-col>
			</a-row>
		</a-form>
	</a-modal>
</template>

<script setup name="wineCityForm">
	import { required } from '@/utils/formRules'
	import { message } from 'ant-design-vue'
	import wineCityApi from '@/api/dev/wineCityApi'

	// 抛出事件
	const emit = defineEmits({ successful: null })

	// 表单数据
	const formData = ref({})
	const formRef = ref()
	const visible = ref(false)
	const submitLoading = ref(false)

	// 表单验证规则
	const formRules = {
		cityCode: [required('请输入城市编码')],
		cityName: [required('请输入城市名称')],
		level: [required('请选择城市层级')],
		status: [required('请选择城市状态')]
	}

	// 打开弹窗
	const onOpen = (record) => {
		visible.value = true
		if (record) {
			let recordData = JSON.parse(JSON.stringify(record))
			formData.value = Object.assign({}, recordData)
		} else {
			// 新增时设置默认值
			formData.value = {
				level: 2,
				status: 'ENABLE',
				isHot: 'N',
				supportDelivery: 'Y',
				sortCode: 100
			}
		}
	}

	// 关闭弹窗
	const onClose = () => {
		visible.value = false
		formRef.value.resetFields()
		formData.value = {}
	}

	// 层级变化处理
	const onLevelChange = (value) => {
		// 根据层级设置相应的默认值
		if (value === 1) {
			// 省份
			formData.value.parentCode = ''
			formData.value.provinceCode = formData.value.cityCode
		} else if (value === 2) {
			// 城市
			// 需要设置省份编码
		} else if (value === 3) {
			// 区县
			// 需要设置省份编码和父级编码
		}
	}

	// 默认提交
	const onSubmit = () => {
		formRef.value
			.validate()
			.then(() => {
				submitLoading.value = true
				const api = formData.value.id ? wineCityApi.edit : wineCityApi.add
				api(formData.value)
					.then(() => {
						visible.value = false
						emit('successful')
						message.success('操作成功')
					})
					.finally(() => {
						submitLoading.value = false
					})
			})
			.catch(() => {
				message.error('表单验证失败，请检查输入内容')
			})
	}

	// 暴露方法
	defineExpose({
		onOpen
	})
</script>

<style scoped>
	.ant-form-item {
		margin-bottom: 16px !important;
	}
</style>
