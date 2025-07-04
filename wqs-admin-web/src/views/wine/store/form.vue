<template>
	<a-modal
		v-model:open="visible"
		:title="formData.id ? '编辑门店' : '新增门店'"
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
					<a-form-item label="门店编码" name="storeCode">
						<a-input v-model:value="formData.storeCode" placeholder="请输入门店编码" :maxlength="50" />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="门店名称" name="storeName">
						<a-input v-model:value="formData.storeName" placeholder="请输入门店名称" :maxlength="100" />
					</a-form-item>
				</a-col>
			</a-row>
			<a-row :gutter="16">
				<a-col :span="12">
					<a-form-item label="营业时间" name="businessHours">
						<a-input v-model:value="formData.businessHours" placeholder="例如：09:00-22:00" :maxlength="50" />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="门店状态" name="status">
						<a-select
							v-model:value="formData.status"
							placeholder="请选择门店状态"
							:getPopupContainer="(trigger) => trigger.parentNode"
						>
							<a-select-option v-for="item in statusData" :key="item.value" :value="item.value">
								{{ item.label }}
							</a-select-option>
						</a-select>
					</a-form-item>
				</a-col>
				<a-col :span="24">
					<a-form-item label="门店管理员" name="storeManagerId">
						<xn-client-user-selector
							ref="managerSelectorRef"
							:radio-model="true"
							:clientUserPageApi="selectorApiFunction.clientUserPageApi"
							:clientUserListByIdListApi="selectorApiFunction.clientUserListByIdListApi"
							v-model:value="formData.storeManagerId"
							@onBack="managerSelectorOnBack"
						/>
					</a-form-item>
				</a-col>
			</a-row>

			<a-row :gutter="16">
				<a-col :span="24">
					<a-form-item label="地址信息">
						<a-form-item-rest>
							<city-selector
								type="three-input"
								v-model:value="formData.cityInfo"
								:placeholders="{
									province: '请选择省份',
									city: '请选择城市',
									district: '请选择区县'
								}"
								@change="handleCityChange"
							/>
						</a-form-item-rest>
					</a-form-item>
				</a-col>
			</a-row>
			<a-row :gutter="16">
				<a-col :span="24">
					<a-form-item label="详细地址" name="detailAddress">
						<a-input-group compact>
							<a-input
								v-model:value="formData.detailAddress"
								placeholder="请输入详细地址"
								:maxlength="200"
								style="width: calc(100% - 100px)"
							/>
							<a-button type="default" style="width: 100px" @click="openMapPicker">
								<template #icon><EnvironmentOutlined /></template>
								地图选点
							</a-button>
						</a-input-group>
					</a-form-item>
				</a-col>
			</a-row>
			<!-- 坐标信息显示 -->
			<a-row :gutter="16" v-if="formData.longitude && formData.latitude">
				<a-col :span="24">
					<a-form-item>
						<a-alert
							:message="`坐标信息：经度 ${formData.longitude}，纬度 ${formData.latitude}`"
							type="info"
							show-icon
							style="margin-bottom: 0"
						/>
					</a-form-item>
				</a-col>
			</a-row>
			<a-row :gutter="16">
				<a-col :span="12">
					<a-form-item label="联系电话" name="contactPhone">
						<a-input v-model:value="formData.contactPhone" placeholder="请输入联系电话" :maxlength="20" />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="联系邮箱" name="contactEmail">
						<a-input v-model:value="formData.contactEmail" placeholder="请输入联系邮箱" :maxlength="100" />
					</a-form-item>
				</a-col>
			</a-row>
			<a-row :gutter="16">
				<a-col :span="12">
					<a-form-item label="定价权限" name="priceAuthority">
						<a-select
							v-model:value="formData.priceAuthority"
							placeholder="请选择定价权限"
							:getPopupContainer="(trigger) => trigger.parentNode"
						>
							<a-select-option v-for="item in priceAuthorityData" :key="item.value" :value="item.value">
								{{ item.label }}
							</a-select-option>
						</a-select>
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="门店面积(平方米)" name="storeArea">
						<a-input-number
							v-model:value="formData.storeArea"
							placeholder="请输入门店面积"
							:min="1"
							:precision="2"
							style="width: 100%"
						/>
					</a-form-item>
				</a-col>
			</a-row>

			<a-row :gutter="16">
				<a-col :span="24">
					<a-form-item label="门店描述" name="description">
						<a-textarea
							v-model:value="formData.description"
							placeholder="请输入门店描述"
							:maxlength="500"
							:rows="3"
							show-count
						/>
					</a-form-item>
				</a-col>
			</a-row>
			<a-row :gutter="16">
				<a-col :span="24">
					<a-form-item label="备注" name="remark">
						<a-textarea
							v-model:value="formData.remark"
							placeholder="请输入备注信息"
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

		<!-- 地图选择器组件 -->
		<map-selector
			ref="mapSelectorRef"
			:initial-location="currentInitialLocation"
			@confirm="handleMapConfirm"
			@cancel="handleMapCancel"
		/>
	</a-modal>
</template>

<script setup name="wineStoreForm">
	import { message } from 'ant-design-vue'
	import { cloneDeep } from 'lodash-es'
	import { EnvironmentOutlined } from '@ant-design/icons-vue'
	import wineStoreApi from '@/api/wine/wineStoreApi'
	import clientUserApi from '@/api/client/clientUserApi'
	import CitySelector from '@/components/CitySelector/index.vue'
	import MapSelector from '@/components/MapSelector/index.vue'

	const emit = defineEmits(['successful'])

	const visible = ref(false)
	const submitLoading = ref(false)
	const formRef = ref()

	// 状态数据
	const statusData = [
		{ value: 'ENABLE', label: '营业中' },
		{ value: 'DISABLE', label: '已停业' }
	]

	// 定价权限数据
	const priceAuthorityData = [
		{ value: 'PLATFORM', label: '平台统一定价' },
		{ value: 'CUSTOM', label: '门店自定义定价' }
	]

	// 表单数据
	const formData = ref({
		id: '',
		storeCode: '',
		storeName: '',
		storeManagerId: '',
		storeManagerUserName: '',
		cityInfo: {
			province: '',
			city: '',
			district: ''
		},
		detailAddress: '',
		longitude: null,
		latitude: null,
		contactPhone: '',
		contactEmail: '',
		businessHours: '',
		status: 'ENABLE',
		priceAuthority: 'PLATFORM',
		storeArea: null,
		sortCode: 100,
		remark: ''
	})

	// 表单验证规则
	const formRules = {
		// 基本信息验证
		storeCode: [{ required: true, message: '请输入门店编码', trigger: 'blur' }],
		storeName: [{ required: true, message: '请输入门店名称', trigger: 'blur' }],
		storeManagerId: [{ required: true, message: '请选择门店管理员', trigger: 'change' }],
		status: [{ required: true, message: '请选择门店状态', trigger: 'change' }],
		sortCode: [{ required: true, message: '请输入排序码', trigger: 'blur' }],

		// 地址信息验证
		detailAddress: [{ required: true, message: '请输入详细地址', trigger: 'blur' }],

		// 联系信息验证
		contactPhone: [{ pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }],
		contactEmail: [{ type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }],

		// 管理配置验证
		priceAuthority: [{ required: true, message: '请选择定价权限', trigger: 'change' }]
	}

	// 传递C端用户选择器需要的API
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

	// 门店管理员选择器回调
	const managerSelectorOnBack = (data) => {
		console.log('选择的门店管理员：' + JSON.stringify(data))
	}

	// 城市选择器变化回调
	const handleCityChange = (cityInfo) => {
		if (cityInfo) {
			formData.value.cityInfo = {
				province: cityInfo.province || '',
				city: cityInfo.city || '',
				district: cityInfo.district || ''
			}
		} else {
			formData.value.cityInfo = {
				province: '',
				city: '',
				district: ''
			}
		}
	}

	// 地图选择器引用
	const mapSelectorRef = ref(null)
	const currentInitialLocation = ref({
		longitude: null,
		latitude: null,
		address: ''
	})

	// 打开地图选点
	const openMapPicker = () => {
		// 设置初始位置信息
		currentInitialLocation.value = {
			longitude: formData.value.longitude || null,
			latitude: formData.value.latitude || null,
			address: formData.value.detailAddress || ''
		}
		mapSelectorRef.value?.open()
	}

	// 处理地图选择器确认
	const handleMapConfirm = (location) => {
		console.log('地图选择器确认，接收到的位置信息：', location)

		// 将坐标信息保存到表单数据中（后端需要）
		formData.value.longitude = location.longitude
		formData.value.latitude = location.latitude

		// 更新详细地址
		if (location.address) {
			formData.value.detailAddress = location.address
		}

		console.log('更新后的表单数据：', {
			longitude: formData.value.longitude,
			latitude: formData.value.latitude,
			detailAddress: formData.value.detailAddress
		})

		message.success('地址选择成功')
	}

	// 处理地图选择器取消
	const handleMapCancel = () => {
		message.info('取消选择位置')
	}

	// 打开弹窗
	const onOpen = (record) => {
		visible.value = true

		if (record) {
			formData.value = cloneDeep(record)
			// 如果有历史数据，构造cityInfo对象
			if (record.province && record.city && record.district) {
				formData.value.cityInfo = {
					province: record.province,
					city: record.city,
					district: record.district,
					fullAddress: `${record.province}${record.city}${record.district}`
				}
			}
			// 确保坐标字段存在
			if (record.longitude !== undefined) {
				formData.value.longitude = record.longitude
			}
			if (record.latitude !== undefined) {
				formData.value.latitude = record.latitude
			}
		} else {
			formData.value = {
				status: 'ENABLE',
				priceAuthority: 'PLATFORM',
				sortCode: 99,
				longitude: null,
				latitude: null,
				cityInfo: {
					province: '',
					city: '',
					district: ''
				}
			}
		}
	}

	// 关闭弹窗
	const onClose = () => {
		visible.value = false
		formRef.value?.resetFields()
		formData.value = {}
	}

	// 提交表单
	const onSubmit = () => {
		// 手动验证城市信息
		if (!formData.value.cityInfo.province || !formData.value.cityInfo.city || !formData.value.cityInfo.district) {
			message.error('请选择完整的省市区信息')
			return
		}

		formRef.value?.validate().then(() => {
			// 构建提交数据，将cityInfo中的信息展开到对应字段
			const submitData = {
				...formData.value,
				province: formData.value.cityInfo.province,
				city: formData.value.cityInfo.city,
				district: formData.value.cityInfo.district
			}

			// 移除cityInfo字段，因为后端不需要这个字段
			delete submitData.cityInfo

			console.log('准备提交到后端的数据：', submitData)
			console.log('坐标信息：', {
				longitude: submitData.longitude,
				latitude: submitData.latitude
			})

			submitLoading.value = true
			const api = formData.value.id ? wineStoreApi.edit : wineStoreApi.add
			api(submitData)
				.then(() => {
					console.log('数据提交成功')
					emit('successful')
					onClose()
				})
				.catch((error) => {
					console.error('数据提交失败：', error)
				})
				.finally(() => {
					submitLoading.value = false
				})
		})
	}

	// 暴露方法
	defineExpose({
		onOpen
	})
</script>

<style scoped>
	.ant-form-item {
		margin-bottom: 16px;
	}

	.ant-divider {
		margin: 16px 0;
	}
</style>
