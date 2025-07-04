<template>
	<xn-form-container :title="getFormTitle()" :width="900" :visible="visible" :destroy-on-close="true" @close="onClose">
		<a-form ref="formRef" :model="formData" :rules="rules" layout="vertical">
			<!-- 基本配置 -->
			<a-divider orientation="left">基本配置</a-divider>
			<a-row :gutter="16">
				<a-col :span="8">
					<a-form-item label="配置类型" name="configType">
						<a-select
							v-model:value="formData.configType"
							placeholder="请选择配置类型"
							@change="onConfigTypeChange"
							class="xn-wd"
						>
							<a-select-option v-for="item in configTypeData" :key="item.value" :value="item.value">
								{{ item.label }}
							</a-select-option>
						</a-select>
					</a-form-item>
				</a-col>
				<a-col :span="8">
					<a-form-item label="所属门店" name="storeId" v-if="showStoreSelect">
						<a-select
							v-model:value="formData.storeId"
							placeholder="请选择门店"
							show-search
							:filter-option="filterOption"
							allowClear
							class="xn-wd"
						>
							<a-select-option v-for="item in storeData" :key="item.id" :value="item.id">
								{{ item.storeName || item.name }}
							</a-select-option>
						</a-select>
					</a-form-item>
				</a-col>
				<a-col :span="8">
					<a-form-item label="关联酒品" name="productId" v-if="showProductSelect">
						<a-select
							v-model:value="formData.productId"
							placeholder="请选择酒品"
							show-search
							:filter-option="filterOption"
							allowClear
							class="xn-wd"
						>
							<a-select-option v-for="item in productData" :key="item.id" :value="item.id">
								{{ item.name || item.productName }} ({{ item.code || item.productCode }})
							</a-select-option>
						</a-select>
					</a-form-item>
				</a-col>
			</a-row>

			<!-- 佣金比例配置 -->
			<a-divider orientation="left">佣金比例配置</a-divider>
			<a-alert
				message="注意：各角色佣金比例总和不能超过100%。可以小于100%，为后期利润分成等灵活模式预留空间"
				type="info"
				show-icon
				class="xn-mb16"
			/>

			<a-row :gutter="16">
				<a-col :span="12">
					<a-form-item label="平台佣金比例(%)" name="platformRate">
						<a-input-number
							v-model:value="formData.platformRate"
							:min="0"
							:max="100"
							:precision="2"
							placeholder="请输入平台佣金比例"
							class="xn-wd"
							@change="calculateTotalRate"
						>
							<template #addonAfter>%</template>
						</a-input-number>
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="设备方佣金比例(%)" name="deviceOwnerRate">
						<a-input-number
							v-model:value="formData.deviceOwnerRate"
							:min="0"
							:max="100"
							:precision="2"
							placeholder="请输入设备方佣金比例"
							class="xn-wd"
							@change="calculateTotalRate"
						>
							<template #addonAfter>%</template>
						</a-input-number>
					</a-form-item>
				</a-col>
			</a-row>

			<a-row :gutter="16">
				<a-col :span="8">
					<a-form-item label="场地方佣金比例(%)" name="locationProviderRate">
						<a-input-number
							v-model:value="formData.locationProviderRate"
							:min="0"
							:max="100"
							:precision="2"
							placeholder="请输入场地方佣金比例"
							class="xn-wd"
							@change="calculateTotalRate"
						>
							<template #addonAfter>%</template>
						</a-input-number>
					</a-form-item>
				</a-col>
				<a-col :span="8">
					<a-form-item label="门店管理员佣金比例(%)" name="storeManagerRate">
						<a-input-number
							v-model:value="formData.storeManagerRate"
							:min="0"
							:max="100"
							:precision="2"
							placeholder="请输入门店管理员佣金比例"
							class="xn-wd"
							@change="calculateTotalRate"
						>
							<template #addonAfter>%</template>
						</a-input-number>
					</a-form-item>
				</a-col>
				<a-col :span="8">
					<a-form-item label="供应商佣金比例(%)" name="supplierRate">
						<a-input-number
							v-model:value="formData.supplierRate"
							:min="0"
							:max="100"
							:precision="2"
							placeholder="请输入供应商佣金比例"
							class="xn-wd"
							@change="calculateTotalRate"
						>
							<template #addonAfter>%</template>
						</a-input-number>
					</a-form-item>
				</a-col>
			</a-row>

			<!-- 总比例显示 -->
			<a-row :gutter="16">
				<a-col :span="24">
					<a-form-item>
						<a-statistic
							title="总佣金比例"
							:value="totalRate"
							:precision="2"
							suffix="%"
							:value-style="{ color: totalRate > 100 ? '#cf1322' : totalRate >= 90 ? '#3f8600' : '#1890ff' }"
						/>
						<a-progress
							:percent="Math.min(totalRate, 100)"
							:status="totalRate > 100 ? 'exception' : totalRate >= 90 ? 'success' : 'active'"
							:show-info="false"
						/>
					</a-form-item>
				</a-col>
			</a-row>

			<!-- 有效期配置 -->
			<a-divider orientation="left">有效期配置</a-divider>
			<a-row :gutter="16">
				<a-col :span="12">
					<a-form-item label="生效日期" name="effectiveDate">
						<a-date-picker
							v-model:value="formData.effectiveDate"
							show-time
							format="YYYY-MM-DD HH:mm:ss"
							placeholder="请选择生效日期"
							class="xn-wd"
						/>
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="失效日期" name="expiryDate">
						<a-date-picker
							v-model:value="formData.expiryDate"
							show-time
							format="YYYY-MM-DD HH:mm:ss"
							placeholder="请选择失效日期（可选）"
							class="xn-wd"
						/>
					</a-form-item>
				</a-col>
			</a-row>

			<!-- 其他配置 -->
			<a-divider orientation="left">其他配置</a-divider>
			<a-row :gutter="16">
				<a-col :span="12">
					<a-form-item label="状态" name="status">
						<a-radio-group v-model:value="formData.status">
							<a-radio value="ENABLE">启用</a-radio>
							<a-radio value="DISABLE">禁用</a-radio>
						</a-radio-group>
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="排序码" name="sortCode">
						<a-input-number
							v-model:value="formData.sortCode"
							:min="1"
							:max="9999"
							placeholder="请输入排序码"
							class="xn-wd"
						/>
					</a-form-item>
				</a-col>
			</a-row>

			<a-form-item label="备注" name="remark">
				<a-textarea
					v-model:value="formData.remark"
					placeholder="请输入备注信息"
					:rows="3"
					:maxlength="500"
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

<script setup name="wineCommissionForm">
	import { message } from 'ant-design-vue'
	import { cloneDeep } from 'lodash-es'
	import dayjs from 'dayjs'
	import { required } from '@/utils/formRules'
	import wineCommissionApi from '@/api/wine/wineCommissionApi'
	import wineProductApi from '@/api/wine/wineProductApi'
	import wineStoreApi from '@/api/wine/wineStoreApi'

	const emit = defineEmits(['successful'])

	const visible = ref(false)
	const submitLoading = ref(false)
	const formRef = ref()
	const isCopyMode = ref(false)

	// 表单数据
	const formData = ref({})

	// 酒品数据和门店数据
	const productData = ref([])
	const storeData = ref([])

	// 总比例计算
	const totalRate = ref(0)

	// 配置类型数据
	const configTypeData = [
		{ value: 'PLATFORM_DEFAULT', label: '平台默认' },
		{ value: 'PRODUCT_GENERAL', label: '酒品通用' },
		{ value: 'STORE_GENERAL', label: '门店通用' },
		{ value: 'STORE_PRODUCT', label: '门店酒品专属' }
	]

	// 是否显示门店选择
	const showStoreSelect = computed(() => {
		return formData.value.configType === 'STORE_GENERAL' || formData.value.configType === 'STORE_PRODUCT'
	})

	// 是否显示酒品选择
	const showProductSelect = computed(() => {
		return formData.value.configType === 'PRODUCT_GENERAL' || formData.value.configType === 'STORE_PRODUCT'
	})

	// 表单验证规则
	const rules = computed(() => {
		const baseRules = {
			configType: [required('请选择配置类型')],
			effectiveDate: [required('请选择生效日期')],
			status: [required('请选择状态')]
		}

		// 根据配置类型动态添加验证规则
		if (showStoreSelect.value) {
			baseRules.storeId = [required('请选择门店')]
		}

		if (showProductSelect.value) {
			baseRules.productId = [required('请选择酒品')]
		}

		return baseRules
	})

	// 获取酒品数据
	const getProductData = () => {
		wineProductApi.productList().then((data) => {
			productData.value = data || []
		})
	}

	// 获取门店数据
	const getStoreData = () => {
		wineStoreApi.list().then((data) => {
			storeData.value = data || []
		})
	}

	// 筛选选项
	const filterOption = (input, option) => {
		return option.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
	}

	// 获取表单标题
	const getFormTitle = () => {
		if (isCopyMode.value) {
			return '复制佣金配置'
		}
		return formData.value.id ? '编辑佣金配置' : '新增佣金配置'
	}

	// 配置类型变化
	const onConfigTypeChange = (value) => {
		// 清空相关字段
		if (!showStoreSelect.value) {
			formData.value.storeId = undefined
		}
		if (!showProductSelect.value) {
			formData.value.productId = undefined
		}
	}

	// 计算总比例
	const calculateTotalRate = () => {
		const total =
			(formData.value.platformRate || 0) +
			(formData.value.deviceOwnerRate || 0) +
			(formData.value.locationProviderRate || 0) +
			(formData.value.storeManagerRate || 0) +
			(formData.value.supplierRate || 0)

		// 保留2位小数，避免浮点数精度问题
		totalRate.value = Math.round(total * 100) / 100
	}

	// 打开表单
	const onOpen = (record) => {
		visible.value = true
		isCopyMode.value = false
		if (record) {
			// 编辑模式
			formData.value = cloneDeep(record)

			// 将后端返回的小数转换为百分比显示
			if (formData.value.platformRate !== undefined) {
				formData.value.platformRate = formData.value.platformRate * 100
			}
			if (formData.value.deviceOwnerRate !== undefined) {
				formData.value.deviceOwnerRate = formData.value.deviceOwnerRate * 100
			}
			if (formData.value.locationProviderRate !== undefined) {
				formData.value.locationProviderRate = formData.value.locationProviderRate * 100
			}
			if (formData.value.storeManagerRate !== undefined) {
				formData.value.storeManagerRate = formData.value.storeManagerRate * 100
			}
			if (formData.value.supplierRate !== undefined) {
				formData.value.supplierRate = formData.value.supplierRate * 100
			}

			// 转换日期格式
			if (formData.value.effectiveDate) {
				formData.value.effectiveDate = dayjs(formData.value.effectiveDate)
			}
			if (formData.value.expiryDate) {
				formData.value.expiryDate = dayjs(formData.value.expiryDate)
			}
		} else {
			// 新增模式
			formData.value = {
				configType: 'PLATFORM_DEFAULT',
				platformRate: 0,
				deviceOwnerRate: 0,
				locationProviderRate: 0,
				storeManagerRate: 0,
				supplierRate: 0,
				status: 'ENABLE',
				sortCode: 99
			}
		}
		calculateTotalRate()
	}

	// 打开复制表单
	const onOpenForCopy = (record) => {
		visible.value = true
		isCopyMode.value = true
		// 复制模式 - 基于现有数据创建新记录
		formData.value = cloneDeep(record)
		// 确保是新增模式（没有ID）
		delete formData.value.id

		// 将后端返回的小数转换为百分比显示
		if (formData.value.platformRate !== undefined) {
			formData.value.platformRate = formData.value.platformRate * 100
		}
		if (formData.value.deviceOwnerRate !== undefined) {
			formData.value.deviceOwnerRate = formData.value.deviceOwnerRate * 100
		}
		if (formData.value.locationProviderRate !== undefined) {
			formData.value.locationProviderRate = formData.value.locationProviderRate * 100
		}
		if (formData.value.storeManagerRate !== undefined) {
			formData.value.storeManagerRate = formData.value.storeManagerRate * 100
		}
		if (formData.value.supplierRate !== undefined) {
			formData.value.supplierRate = formData.value.supplierRate * 100
		}

		// 转换日期格式
		if (formData.value.effectiveDate) {
			formData.value.effectiveDate = dayjs(formData.value.effectiveDate)
		}
		if (formData.value.expiryDate) {
			formData.value.expiryDate = dayjs(formData.value.expiryDate)
		}

		// 设置默认值
		if (!formData.value.status) {
			formData.value.status = 'ENABLE'
		}
		if (!formData.value.sortCode) {
			formData.value.sortCode = 99
		}

		calculateTotalRate()
	}

	// 关闭表单
	const onClose = () => {
		visible.value = false
		isCopyMode.value = false
		formRef.value?.resetFields()
		formData.value = {}
		totalRate.value = 0
	}

	// 提交表单
	const onSubmit = () => {
		formRef.value.validate().then(() => {
			submitLoading.value = true
			const submitData = cloneDeep(formData.value)

			// 转换日期格式
			if (submitData.effectiveDate) {
				submitData.effectiveDate = dayjs(submitData.effectiveDate).format('YYYY-MM-DD HH:mm:ss')
			}
			if (submitData.expiryDate) {
				submitData.expiryDate = dayjs(submitData.expiryDate).format('YYYY-MM-DD HH:mm:ss')
			}

			// 将百分比转换为小数（后端期望0-1之间的小数）
			if (submitData.platformRate !== undefined) {
				submitData.platformRate = submitData.platformRate / 100
			}
			if (submitData.deviceOwnerRate !== undefined) {
				submitData.deviceOwnerRate = submitData.deviceOwnerRate / 100
			}
			if (submitData.locationProviderRate !== undefined) {
				submitData.locationProviderRate = submitData.locationProviderRate / 100
			}
			if (submitData.storeManagerRate !== undefined) {
				submitData.storeManagerRate = submitData.storeManagerRate / 100
			}
			if (submitData.supplierRate !== undefined) {
				submitData.supplierRate = submitData.supplierRate / 100
			}

			const api = submitData.id ? wineCommissionApi.edit : wineCommissionApi.add
			api(submitData)
				.then(() => {
					if (isCopyMode.value) {
						message.success('复制成功')
					} else {
						message.success(submitData.id ? '编辑成功' : '新增成功')
					}
					emit('successful')
					onClose()
				})
				.finally(() => {
					submitLoading.value = false
				})
		})
	}

	// 页面加载时获取数据
	onMounted(() => {
		getProductData()
		getStoreData()
	})

	// 监听表单数据变化，实时计算总比例
	watch(
		() => [
			formData.value.platformRate,
			formData.value.deviceOwnerRate,
			formData.value.locationProviderRate,
			formData.value.storeManagerRate,
			formData.value.supplierRate
		],
		() => {
			calculateTotalRate()
		},
		{ deep: true }
	)

	// 暴露方法给父组件
	defineExpose({
		onOpen,
		onOpenForCopy
	})
</script>

<style scoped>
	.xn-wd {
		width: 100%;
	}

	.xn-mb16 {
		margin-bottom: 16px;
	}

	.xn-mr8 {
		margin-right: 8px;
	}
</style>
