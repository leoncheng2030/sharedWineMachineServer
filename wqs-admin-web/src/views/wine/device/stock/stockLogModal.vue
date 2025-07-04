<template>
	<a-modal
		title="库存变更日志"
		:width="1000"
		:visible="visible"
		:footer="null"
		@cancel="handleCancel"
		:destroyOnClose="true"
	>
		<a-descriptions :column="2" class="xn-mb16" v-if="stockInfo.productName">
			<a-descriptions-item label="酒品名称">{{ stockInfo.productName }}</a-descriptions-item>
			<a-descriptions-item label="当前库存">{{ stockInfo.stockQuantity }}ml</a-descriptions-item>
		</a-descriptions>

		<s-table
			ref="tableRef"
			:columns="columns"
			:data="loadData"
			:expand-row-by-click="false"
			bordered
			:row-key="(record) => record.id"
			:pagination="{ pageSize: 10, showSizeChanger: true, showQuickJumper: true }"
			:scroll="{ x: 800 }"
		>
			<template #bodyCell="{ column, record }">
				<template v-if="column.dataIndex === 'changeType'">
					<a-tag :color="getChangeTypeColor(record.changeType)">
						{{ getChangeTypeText(record.changeType) }}
					</a-tag>
				</template>
				<template v-if="column.dataIndex === 'changeQuantity'">
					<span :style="{ color: getQuantityColor(record.changeQuantity) }">
						{{ record.changeQuantity > 0 ? '+' : '' }}{{ record.changeQuantity }}ml
					</span>
				</template>
				<template v-if="column.dataIndex === 'afterQuantity'">
					<span>{{ record.afterQuantity }}ml</span>
				</template>
			</template>
		</s-table>
	</a-modal>
</template>

<script setup>
	import { ref } from 'vue'
	import wineDeviceStockApi from '@/api/wine/wineDeviceStockApi'

	const visible = ref(false)
	const tableRef = ref()
	const stockInfo = ref({})

	const columns = [
		{
			title: '变更类型',
			dataIndex: 'changeType',
			width: '100px'
		},
		{
			title: '变更数量',
			dataIndex: 'changeQuantity',
			width: '120px'
		},
		{
			title: '变更后库存',
			dataIndex: 'afterQuantity',
			width: '120px'
		},
		{
			title: '操作人',
			dataIndex: 'operator',
			width: '100px'
		},
		{
			title: '变更原因',
			dataIndex: 'changeReason',
			ellipsis: true
		},
		{
			title: '变更时间',
			dataIndex: 'createTime',
			width: '150px'
		}
	]

	// 加载日志数据
	const loadData = (parameter) => {
		const params = {
			...parameter,
			deviceId: stockInfo.value.deviceId,
			productId: stockInfo.value.productId
		}
		return wineDeviceStockApi
			.logs(params)
			.then((res) => {
				console.log('库存日志API返回数据:', res)
				// 后端返回的是Page<WineStockLog>格式，前端request.js已经提取了data字段
				// res应该包含 records, total, current, size 等字段
				return res
			})
			.catch((error) => {
				console.error('获取库存日志失败:', error)
				return {
					records: [],
					total: 0,
					current: 1,
					size: 10
				}
			})
	}

	// 打开弹窗
	const onOpen = (record) => {
		stockInfo.value = record
		visible.value = true
		// 刷新表格数据
		setTimeout(() => {
			tableRef.value?.refresh()
		}, 100)
	}

	// 关闭弹窗
	const handleCancel = () => {
		visible.value = false
		stockInfo.value = {}
	}

	// 获取变更类型颜色
	const getChangeTypeColor = (type) => {
		const colorMap = {
			REFILL: 'green',
			SALE: 'blue',
			ADJUST: 'orange',
			DEDUCT: 'red'
		}
		return colorMap[type] || 'default'
	}

	// 获取变更类型文本
	const getChangeTypeText = (type) => {
		const textMap = {
			REFILL: '补货',
			SALE: '销售',
			ADJUST: '调整',
			DEDUCT: '扣减'
		}
		return textMap[type] || '未知'
	}

	// 获取数量颜色
	const getQuantityColor = (quantity) => {
		return quantity > 0 ? '#52c41a' : '#ff4d4f'
	}

	defineExpose({
		onOpen
	})
</script>

<style scoped>
	.xn-mb16 {
		margin-bottom: 16px;
	}
</style>
