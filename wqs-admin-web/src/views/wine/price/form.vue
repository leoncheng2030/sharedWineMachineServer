<template>
	<xn-form-container
		:title="`${selectedProductName} - 价格管理`"
		:width="1200"
		:open="visible"
		:destroy-on-close="true"
		@close="onClose"
	>
		<!-- 酒品信息展示 -->
		<div v-if="selectedProduct" class="product-info-section">
			<a-card title="酒品信息" size="small" class="xn-mb16">
				<a-row :gutter="16">
					<a-col :span="14">
						<a-descriptions :column="2" size="small">
							<a-descriptions-item label="酒品名称">{{ selectedProduct.productName }}</a-descriptions-item>
							<a-descriptions-item label="酒品编码">{{ selectedProduct.productCode || '暂无' }}</a-descriptions-item>
							<a-descriptions-item label="品牌">{{ selectedProduct.brand || '暂无' }}</a-descriptions-item>
							<a-descriptions-item label="分类">{{ selectedProduct.categoryName || '暂无' }}</a-descriptions-item>
							<a-descriptions-item label="酒精度">{{
								selectedProduct.alcoholContent ? selectedProduct.alcoholContent + '%' : '暂无'
							}}</a-descriptions-item>
							<a-descriptions-item label="建议售价">{{
								selectedProduct.suggestedPrice ? '¥' + selectedProduct.suggestedPrice : '暂无'
							}}</a-descriptions-item>
						</a-descriptions>
					</a-col>
					<a-col :span="10">
						<a-form layout="vertical">
							<a-form-item label="基础单价 (元/ml)" help="用于计算最终价格：基础单价 × 容量 × (1 - 折扣率)">
								<a-input-number
									v-model:value="selectedProduct.unitPrice"
									:min="0"
									:precision="4"
									:step="0.01"
									placeholder="请设置基础单价"
									style="width: 100%"
									@change="onUnitPriceChange"
								>
									<template #addonAfter>元/ml</template>
								</a-input-number>
							</a-form-item>
						</a-form>
					</a-col>
				</a-row>
			</a-card>
		</div>

		<!-- 价格配置列表 -->
		<div class="price-config-section">
			<div class="section-header">
				<h4>容量价格配置</h4>
				<a-button type="primary" @click="addPriceConfig" :disabled="!selectedProduct">
					<template #icon><PlusOutlined /></template>
					添加配置
				</a-button>
			</div>

			<a-table
				:dataSource="priceConfigs"
				:columns="tableColumns"
				:pagination="false"
				:scroll="{ x: 800 }"
				row-key="tempId"
				size="middle"
				class="price-config-table"
				:row-class-name="getRowClassName"
			>
				<!-- 容量列 -->
				<template #bodyCell="{ column, record, index }">
					<template v-if="column.key === 'capacity'">
						<a-input-number
							v-model:value="record.capacity"
							:precision="0"
							:min="1"
							:step="1"
							placeholder="请输入容量"
							style="width: 100%"
							@change="() => onConfigChange(index)"
						>
							<template #addonAfter>ml</template>
						</a-input-number>
					</template>

					<!-- 折扣率列 -->
					<template v-else-if="column.key === 'discountRate'">
						<a-input-number
							v-model:value="record.discountRate"
							:min="0"
							:max="1"
							:precision="3"
							:step="0.01"
							placeholder="0.1表示10%折扣"
							style="width: 100%"
							@change="() => onConfigChange(index)"
						/>
					</template>

					<!-- 最终价格列 -->
					<template v-else-if="column.key === 'finalPrice'">
						<div class="final-price-display">
							<span class="price-amount" :class="{ 'price-valid': record.finalPrice > 0 }">
								¥{{ record.finalPrice ? record.finalPrice.toFixed(2) : '0.00' }}
							</span>
						</div>
					</template>

					<!-- 状态列 -->
					<template v-else-if="column.key === 'status'">
						<a-select
							v-model:value="record.status"
							placeholder="请选择状态"
							style="width: 100%"
							@change="() => onStatusChange(index)"
						>
							<a-select-option value="ENABLE">启用</a-select-option>
							<a-select-option value="DISABLE">禁用</a-select-option>
						</a-select>
					</template>

					<!-- 计算过程列 -->
					<template v-else-if="column.key === 'calculation'">
						<div class="calculation-display">
							<div v-if="selectedProduct && record.capacity && record.discountRate !== undefined">
								<small
									>{{ selectedProduct.unitPrice }} × {{ record.capacity }} × (1 - {{ record.discountRate }}) =
									{{ record.finalPrice ? record.finalPrice.toFixed(2) : '0.00' }}</small
								>
							</div>
							<div v-else>
								<small style="color: #999">请完善配置信息</small>
							</div>
						</div>
					</template>

					<!-- 操作列 -->
					<template v-else-if="column.key === 'action'">
						<a-button
							type="text"
							danger
							size="small"
							@click="removePriceConfig(index)"
							:disabled="priceConfigs.length === 1"
						>
							<template #icon><DeleteOutlined /></template>
							删除
						</a-button>
					</template>
				</template>
			</a-table>

			<!-- 空状态 -->
			<div v-if="priceConfigs.length === 0" class="empty-state">
				<a-empty description="暂无价格配置，请点击添加配置开始设置">
					<a-button type="primary" @click="addPriceConfig" :disabled="!selectedProduct">
						<template #icon><PlusOutlined /></template>
						添加第一个配置
					</a-button>
				</a-empty>
			</div>
		</div>

		<!-- 批量操作区域 -->
		<div v-if="priceConfigs.length > 0" class="batch-operations">
			<a-divider>批量操作</a-divider>
			<a-row :gutter="16">
				<a-col :span="8">
					<a-button @click="sortByCapacity" block>
						<template #icon><SortAscendingOutlined /></template>
						按容量排序
					</a-button>
				</a-col>
				<a-col :span="8">
					<a-button @click="applyDiscountTemplate" block>
						<template #icon><ThunderboltOutlined /></template>
						应用折扣模板
					</a-button>
				</a-col>
				<a-col :span="8">
					<a-button @click="clearAllConfigs" block danger>
						<template #icon><ClearOutlined /></template>
						清空所有配置
					</a-button>
				</a-col>
			</a-row>
		</div>

		<!-- 保存提示 -->
		<div v-if="hasChanges" class="save-hint">
			<a-alert
				message="有未保存的更改"
				description="请点击保存按钮提交您的价格配置更改"
				type="warning"
				show-icon
				class="xn-mb16"
			/>
		</div>

		<template #footer>
			<a-button class="xn-mr8" @click="onClose">取消</a-button>
			<a-button type="primary" :loading="submitLoading" @click="onSubmit" :disabled="!canSubmit">
				保存全部配置 ({{ validConfigsCount }}条)
			</a-button>
		</template>
	</xn-form-container>

	<!-- 折扣模板弹窗 -->
	<a-modal
		v-model:open="templateModalVisible"
		title="应用折扣模板"
		@ok="applyTemplate"
		@cancel="templateModalVisible = false"
	>
		<a-form layout="vertical">
			<a-form-item label="选择模板">
				<a-select v-model:value="selectedTemplate" placeholder="请选择折扣模板">
					<a-select-option value="volume_based">容量阶梯折扣</a-select-option>
					<a-select-option value="uniform">统一折扣率</a-select-option>
					<a-select-option value="promotional">促销折扣</a-select-option>
				</a-select>
			</a-form-item>
			<a-form-item v-if="selectedTemplate === 'uniform'" label="统一折扣率">
				<a-input-number
					v-model:value="uniformDiscountRate"
					:min="0"
					:max="1"
					:precision="3"
					:step="0.01"
					placeholder="0.1表示10%折扣"
				/>
			</a-form-item>
		</a-form>
	</a-modal>
</template>

<script setup name="winePriceForm">
	import { message } from 'ant-design-vue'
	import winePriceApi from '@/api/wine/winePriceApi'
	import wineProductApi from '@/api/wine/wineProductApi'
	import {
		ClearOutlined,
		DeleteOutlined,
		PlusOutlined,
		SortAscendingOutlined,
		ThunderboltOutlined
	} from '@ant-design/icons-vue'

	const emit = defineEmits(['successful'])

	const visible = ref(false)
	const submitLoading = ref(false)
	const templateModalVisible = ref(false)
	const selectedTemplate = ref('')
	const uniformDiscountRate = ref(0.1)

	// 当前选中的酒品
	const selectedProduct = ref(null)
	const selectedProductName = ref('')
	const currentProductId = ref(null)

	// 价格配置列表
	const priceConfigs = ref([])
	const hasChanges = ref(false)

	// 表格列定义
	const tableColumns = [
		{
			title: '序号',
			dataIndex: 'index',
			key: 'index',
			width: 60,
			align: 'center',
			customRender: ({ index }) => index + 1
		},
		{
			title: '容量 (ml)',
			dataIndex: 'capacity',
			key: 'capacity',
			width: 150,
			align: 'center'
		},
		{
			title: '折扣率',
			dataIndex: 'discountRate',
			key: 'discountRate',
			width: 150,
			align: 'center'
		},
		{
			title: '最终价格',
			dataIndex: 'finalPrice',
			key: 'finalPrice',
			width: 120,
			align: 'center'
		},
		{
			title: '状态',
			dataIndex: 'status',
			key: 'status',
			width: 100,
			align: 'center'
		},
		{
			title: '计算过程',
			dataIndex: 'calculation',
			key: 'calculation',
			width: 300,
			align: 'center'
		},
		{
			title: '操作',
			key: 'action',
			width: 80,
			align: 'center'
		}
	]

	// 生成临时ID
	let tempIdCounter = 1
	const generateTempId = () => `temp_${tempIdCounter++}`

	// 计算属性
	const validConfigsCount = computed(() => {
		return priceConfigs.value.filter(
			(config) =>
				config.capacity > 0 && config.discountRate !== undefined && config.discountRate >= 0 && config.discountRate <= 1
		).length
	})

	const canSubmit = computed(() => {
		return validConfigsCount.value > 0 && selectedProduct.value && !submitLoading.value
	})

	// 添加价格配置
	const addPriceConfig = (markAsChanged = true) => {
		const newConfig = {
			tempId: generateTempId(),
			id: null, // 新增配置没有ID
			capacity: null,
			discountRate: 0,
			finalPrice: 0,
			status: 'ENABLE',
			hasError: false,
			capacityError: '',
			discountError: '',
			isModified: false // 标记是否被修改
		}
		priceConfigs.value.push(newConfig)
		if (markAsChanged) {
			hasChanges.value = true
		}
	}

	// 删除价格配置
	const removePriceConfig = (index) => {
		if (priceConfigs.value.length > 1) {
			priceConfigs.value.splice(index, 1)
			hasChanges.value = true
		}
	}

	// 配置变化时重新计算价格和验证
	const onConfigChange = (index, markAsChanged = true) => {
		const config = priceConfigs.value[index]
		if (!config || !selectedProduct.value) return

		// 验证容量
		config.capacityError = ''
		if (!config.capacity || config.capacity <= 0) {
			config.capacityError = '容量必须大于0'
		} else {
			// 检查是否有重复容量
			const duplicateIndex = priceConfigs.value.findIndex(
				(item, idx) => idx !== index && item.capacity === config.capacity
			)
			if (duplicateIndex !== -1) {
				config.capacityError = '容量不能重复'
			}
		}

		// 验证折扣率
		config.discountError = ''
		if (config.discountRate === undefined || config.discountRate < 0 || config.discountRate > 1) {
			config.discountError = '折扣率必须在0-1之间'
		}

		// 计算最终价格
		if (config.capacity > 0 && config.discountRate >= 0 && config.discountRate <= 1) {
			const unitPrice = selectedProduct.value.unitPrice || 0
			const baseAmount = unitPrice * config.capacity
			const discountAmount = baseAmount * config.discountRate
			config.finalPrice = baseAmount - discountAmount
		} else {
			config.finalPrice = 0
		}

		// 设置错误状态
		config.hasError = !!(config.capacityError || config.discountError)

		// 只有在明确标记为用户更改时才设置hasChanges和行修改状态
		if (markAsChanged) {
			config.isModified = true
			hasChanges.value = true
		}
	}

	// 基础单价变化时重新计算所有配置的价格
	const onUnitPriceChange = (markAsChanged = true) => {
		// 重新计算所有配置的价格
		priceConfigs.value.forEach((config, index) => {
			onConfigChange(index, markAsChanged)
		})
		if (markAsChanged) {
			hasChanges.value = true
		}
	}

	// 状态变更处理
	const onStatusChange = (index) => {
		const config = priceConfigs.value[index]
		if (config) {
			config.isModified = true
			hasChanges.value = true
		}
	}

	// 获取行的CSS类名
	const getRowClassName = (record) => {
		return record.isModified ? 'modified-row' : ''
	}

	// 按容量排序
	const sortByCapacity = () => {
		priceConfigs.value.sort((a, b) => (a.capacity || 0) - (b.capacity || 0))
		hasChanges.value = true
	}

	// 应用折扣模板
	const applyDiscountTemplate = () => {
		if (priceConfigs.value.length === 0) {
			message.warning('请先添加价格配置')
			return
		}
		templateModalVisible.value = true
	}

	// 应用模板
	const applyTemplate = () => {
		if (!selectedTemplate.value) {
			message.warning('请选择折扣模板')
			return
		}

		priceConfigs.value.forEach((config, index) => {
			switch (selectedTemplate.value) {
				case 'volume_based':
					// 容量阶梯折扣：容量越大折扣越高
					if (config.capacity) {
						if (config.capacity <= 100) {
							config.discountRate = 0.05 // 5%
						} else if (config.capacity <= 300) {
							config.discountRate = 0.1 // 10%
						} else if (config.capacity <= 500) {
							config.discountRate = 0.15 // 15%
						} else {
							config.discountRate = 0.2 // 20%
						}
					}
					break
				case 'uniform':
					// 统一折扣率
					config.discountRate = uniformDiscountRate.value
					break
				case 'promotional':
					// 促销折扣：第一个配置无折扣，其他递增
					config.discountRate = index * 0.05
					break
			}
			onConfigChange(index)
		})

		templateModalVisible.value = false
		selectedTemplate.value = ''
		message.success('折扣模板应用成功')
	}

	// 清空所有配置
	const clearAllConfigs = () => {
		priceConfigs.value = []
		addPriceConfig() // 保持至少一个配置，这是用户操作，标记为更改
		hasChanges.value = true
	}

	// 加载现有价格配置
	const loadExistingPrices = async (productId) => {
		try {
			const response = await winePriceApi.pricePage({
				productId: productId,
				current: 1,
				size: 100 // 获取所有配置
			})

			if (response && response.records && response.records.length > 0) {
				priceConfigs.value = response.records.map((item) => ({
					tempId: generateTempId(),
					id: item.id,
					capacity: item.capacity,
					discountRate: item.discountRate,
					finalPrice: 0, // 将重新计算
					status: item.status || 'ENABLE',
					hasError: false,
					capacityError: '',
					discountError: '',
					isModified: false // 加载的数据初始状态为未修改
				}))

				// 重新计算所有价格（加载数据时不标记为更改）
				priceConfigs.value.forEach((config, index) => {
					onConfigChange(index, false)
				})
			} else {
				// 没有现有配置，添加一个默认配置（不标记为更改）
				addPriceConfig(false)
			}
		} catch (error) {
			console.error('加载价格配置失败:', error)
			message.error('加载价格配置失败')
			addPriceConfig(false) // 添加默认配置（不标记为更改）
		}
	}

	// 打开表单
	const onOpen = async (params) => {
		visible.value = true
		hasChanges.value = false

		if (params && params.productId) {
			currentProductId.value = params.productId

			// 首先使用传入的参数创建基础对象
			selectedProductName.value = params.productName || `酒品ID: ${params.productId}`

			// 根据建议售价和容量计算默认单价
			let defaultUnitPrice = 0.5 // 默认单价
			if (params.suggestedPrice && params.volume && params.volume > 0) {
				defaultUnitPrice = parseFloat((params.suggestedPrice / params.volume).toFixed(4))
			} else if (params.categoryName) {
				// 根据分类设置默认单价
				defaultUnitPrice = getDefaultUnitPriceByCategory(params.categoryName)
			}

			// 创建基础的selectedProduct对象
			selectedProduct.value = {
				id: params.productId,
				productName: params.productName || `酒品ID: ${params.productId}`,
				productCode: params.productCode || '',
				brand: params.brand || '',
				categoryName: params.categoryName || '',
				alcoholContent: params.alcoholContent || '',
				volume: params.volume || '',
				suggestedPrice: params.suggestedPrice || '',
				unitPrice: defaultUnitPrice
			}

			try {
				// 尝试获取酒品详情以获得最新的单价信息
				const productDetail = await wineProductApi.productDetail({ id: params.productId })

				console.log('获取到的酒品详情:', productDetail)
				console.log('API返回的unitPrice:', productDetail?.unitPrice)

				if (productDetail) {
					// 使用API返回的数据更新selectedProduct对象
					selectedProduct.value = {
						id: productDetail.id || params.productId,
						productName: productDetail.productName || params.productName || `酒品ID: ${params.productId}`,
						productCode: productDetail.productCode || params.productCode || '',
						brand: productDetail.brand || params.brand || '',
						categoryName: productDetail.categoryName || params.categoryName || '',
						alcoholContent: productDetail.alcoholContent || params.alcoholContent || '',
						volume: productDetail.volume || params.volume || '',
						suggestedPrice: productDetail.suggestedPrice || params.suggestedPrice || '',
						unitPrice: productDetail.unitPrice || defaultUnitPrice
					}

					selectedProductName.value = selectedProduct.value.productName
					console.log('使用API数据更新后的unitPrice:', selectedProduct.value.unitPrice)
				}
			} catch (error) {
				console.warn('获取酒品详情失败，使用传入参数:', error)
				// API调用失败时，继续使用已创建的基础对象，不中断流程
			}

			try {
				// 加载现有价格配置
				await loadExistingPrices(params.productId)
			} catch (error) {
				console.error('加载价格配置失败:', error)
				const errorMessage = error.response?.data?.message || error.message || '未知错误'
				message.error(`加载价格配置失败: ${errorMessage}`)
			}
		} else {
			message.error('请先选择酒品')
			onClose()
		}
	}

	// 根据酒品类型获取默认单价
	const getDefaultUnitPrice = (productType) => {
		const typeMap = {
			BAIJIU: 0.6,
			白酒: 0.6,
			HONGJIU: 0.2,
			红酒: 0.2,
			PIJIU: 0.01,
			啤酒: 0.01,
			WEISHIJI: 0.8,
			威士忌: 0.8,
			干邑: 0.8,
			白兰地: 0.7
		}
		return typeMap[productType] || 0.5 // 默认0.5元/ml
	}

	// 根据分类名称获取默认单价
	const getDefaultUnitPriceByCategory = (categoryName) => {
		if (!categoryName) return 0.5

		const categoryMap = {
			白酒: 0.6,
			红酒: 0.2,
			啤酒: 0.01,
			威士忌: 0.8,
			伏特加: 0.5,
			朗姆酒: 0.4,
			金酒: 0.45,
			龙舌兰: 0.55,
			干邑: 0.8,
			白兰地: 0.7,
			香槟: 0.3,
			清酒: 0.25
		}

		// 模糊匹配分类名称
		for (const [key, value] of Object.entries(categoryMap)) {
			if (categoryName.includes(key)) {
				return value
			}
		}

		return 0.5 // 默认0.5元/ml
	}

	// 关闭表单
	const onClose = () => {
		visible.value = false
		selectedProduct.value = null
		selectedProductName.value = ''
		currentProductId.value = null
		priceConfigs.value = []
		hasChanges.value = false
		templateModalVisible.value = false
	}

	// 提交表单
	const onSubmit = async () => {
		if (!selectedProduct.value || !currentProductId.value) {
			message.error('请先选择酒品')
			return
		}

		// 验证所有配置（验证时不标记为更改）
		let hasValidationError = false
		priceConfigs.value.forEach((config, index) => {
			onConfigChange(index, false)
			if (config.hasError) {
				hasValidationError = true
			}
		})

		if (hasValidationError) {
			message.error('请修正配置中的错误')
			return
		}

		const validConfigs = priceConfigs.value.filter(
			(config) =>
				config.capacity > 0 && config.discountRate !== undefined && config.discountRate >= 0 && config.discountRate <= 1
		)

		if (validConfigs.length === 0) {
			message.error('请至少添加一个有效的价格配置')
			return
		}

		submitLoading.value = true

		try {
			// 准备批量保存的数据
			const batchData = {
				productId: currentProductId.value,
				priceConfigs: validConfigs.map((config) => ({
					id: config.id, // 有ID表示更新，无ID表示新增
					productId: currentProductId.value,
					capacity: config.capacity,
					discountRate: config.discountRate,
					status: config.status,
					effectiveDate: new Date().toISOString().slice(0, 19).replace('T', ' '),
					sortCode: 1
				}))
			}

			// 调用批量保存接口
			await winePriceApi.priceBatchSet(batchData)

			message.success(`成功保存 ${validConfigs.length} 条价格配置`)
			onClose()
			emit('successful')
		} catch (error) {
			console.error('保存价格配置失败:', error)
			message.error('保存价格配置失败: ' + (error.message || '未知错误'))
		} finally {
			submitLoading.value = false
		}
	}

	// 暴露方法给父组件
	defineExpose({
		onOpen
	})
</script>

<style scoped>
	.price-config-section {
		margin-bottom: 24px;
	}

	.section-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 16px;
		padding-bottom: 8px;
		border-bottom: 1px solid #f0f0f0;
	}

	.section-header h4 {
		margin: 0;
		color: #262626;
		font-size: 16px;
		font-weight: 600;
	}

	.empty-state {
		text-align: center;
		padding: 40px 0;
		background: #fafafa;
		border-radius: 6px;
		border: 1px dashed #d9d9d9;
	}

	.price-config-table {
		border-radius: 6px;
		overflow: hidden;
		box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
	}

	.price-config-table :deep(.ant-table-thead > tr > th) {
		background: #fafafa;
		font-weight: 600;
		color: #262626;
		border-bottom: 2px solid #f0f0f0;
	}

	.price-config-table :deep(.ant-table-tbody > tr:hover > td) {
		background: #f5f5f5;
	}

	.price-config-table :deep(.ant-table-tbody > tr > td) {
		padding: 12px 8px;
		vertical-align: middle;
	}

	/* 移除表格中输入框的默认阴影 */
	.price-config-table :deep(.ant-input-number),
	.price-config-table :deep(.ant-select) {
		box-shadow: none;
		border: 1px solid #d9d9d9;
	}

	.price-config-table :deep(.ant-input-number:focus),
	.price-config-table :deep(.ant-input-number-focused),
	.price-config-table :deep(.ant-select:focus),
	.price-config-table :deep(.ant-select-focused) {
		border-color: #1890ff;
		box-shadow: 0 0 0 2px rgba(24, 144, 255, 0.2);
	}

	.price-config-table :deep(.ant-input-number-status-error),
	.price-config-table :deep(.ant-select-status-error) {
		border-color: #ff4d4f;
	}

	.price-config-table :deep(.ant-input-number-status-error:focus),
	.price-config-table :deep(.ant-input-number-status-error.ant-input-number-focused),
	.price-config-table :deep(.ant-select-status-error:focus),
	.price-config-table :deep(.ant-select-status-error.ant-select-focused) {
		border-color: #ff4d4f;
		box-shadow: 0 0 0 2px rgba(255, 77, 79, 0.2);
	}

	/* 修改行的背景色 */
	.price-config-table :deep(.modified-row) {
		background-color: #fff7e6 !important;
	}

	.price-config-table :deep(.modified-row:hover) {
		background-color: #fff1d6 !important;
	}

	.price-config-table :deep(.modified-row td) {
		background-color: transparent !important;
	}

	.final-price-display {
		padding: 4px 8px;
		background: #f0f9ff;
		border-radius: 4px;
		border: 1px solid #bae7ff;
		text-align: center;
	}

	.price-amount {
		font-size: 14px;
		font-weight: bold;
		color: #999;
	}

	.price-amount.price-valid {
		color: #52c41a;
	}

	.calculation-display {
		padding: 4px 8px;
		background: #f9f9f9;
		border-radius: 4px;
		border: 1px solid #e8e8e8;
		font-size: 12px;
	}

	.calculation-display small {
		color: #666;
		font-family: 'Courier New', monospace;
		line-height: 1.4;
	}

	.batch-operations {
		margin-top: 24px;
	}

	.save-hint {
		margin-top: 16px;
	}
</style>
