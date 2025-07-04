<template>
	<a-modal v-model:open="visible" title="地图选点" :width="800" :footer="null" @cancel="handleCancel">
		<div class="map-selector-container">
			<!-- 搜索栏 -->
			<div class="search-bar">
				<a-input-group compact>
					<a-input
						v-model:value="searchKeyword"
						placeholder="输入地址搜索位置"
						style="width: calc(100% - 80px)"
						@pressEnter="searchLocation"
					/>
					<a-button type="primary" @click="searchLocation">搜索</a-button>
				</a-input-group>
				<div style="margin-top: 8px">
					<a-button @click="getCurrentLocation" size="small">
						<template #icon><EnvironmentOutlined /></template>
						获取当前位置
					</a-button>
				</div>
			</div>

			<!-- 地图容器 -->
			<div class="map-container">
				<baidu-map
					ref="baiduMapRef"
					:api-key="baiduApiKey"
					:center="mapCenter"
					:zoom="15"
					:height="400"
					@complete="handleMapComplete"
					@mapClick="handleMapClick"
				/>
			</div>

			<!-- 位置信息显示 -->
			<div class="location-info">
				<a-row :gutter="16">
					<a-col :span="12">
						<div class="info-item">
							<span class="label">经度：</span>
							<span class="value">{{ selectedLocation.longitude || '未选择' }}</span>
						</div>
					</a-col>
					<a-col :span="12">
						<div class="info-item">
							<span class="label">纬度：</span>
							<span class="value">{{ selectedLocation.latitude || '未选择' }}</span>
						</div>
					</a-col>
				</a-row>
				<div class="info-item">
					<span class="label">地址：</span>
					<span class="value">{{ selectedLocation.address || '未选择位置' }}</span>
				</div>
			</div>

			<!-- 操作按钮 -->
			<div class="action-buttons">
				<a-space>
					<a-button type="primary" @click="confirmSelection" :disabled="!selectedLocation.longitude"> 确定 </a-button>
					<a-button @click="clearSelection" :disabled="!selectedLocation.longitude"> 清除选择 </a-button>
					<a-button @click="handleCancel">取消</a-button>
				</a-space>
			</div>
		</div>
	</a-modal>
</template>

<script setup>
	import { nextTick, ref, watch } from 'vue'
	import { message } from 'ant-design-vue'
	import BaiduMap from '@/components/Map/baiduMap/index.vue'
	import { EnvironmentOutlined } from '@ant-design/icons-vue'

	// 定义props和emits
	const props = defineProps({
		initialLocation: {
			type: Object,
			default: () => ({
				longitude: null,
				latitude: null,
				address: ''
			})
		}
	})

	const emit = defineEmits(['confirm', 'cancel'])

	// 响应式数据
	const visible = ref(false)
	const searchKeyword = ref('')
	const baiduMapRef = ref(null)
	const selectedLocation = ref({
		longitude: null,
		latitude: null,
		address: ''
	})

	// 地图中心点
	const mapCenter = ref([116.397428, 39.90923]) // 默认北京天安门

	// API密钥
	const baiduApiKey = 'qcwEEHdGs4alxQuGb9o2deashoRB3Ued'

	// 地图相关变量
	let geocoder = null

	// 监听初始位置变化
	watch(
		() => props.initialLocation,
		(newLocation) => {
			if (newLocation.longitude && newLocation.latitude) {
				mapCenter.value = [newLocation.longitude, newLocation.latitude]
				selectedLocation.value = { ...newLocation }
			}
		},
		{ immediate: true }
	)

	// 打开地图选择器
	const open = () => {
		console.log('打开地图选择器，初始位置：', props.initialLocation)
		visible.value = true

		// 如果有初始位置，设置地图中心点和选中位置
		if (props.initialLocation.longitude && props.initialLocation.latitude) {
			console.log('设置初始位置：', props.initialLocation)
			mapCenter.value = [props.initialLocation.longitude, props.initialLocation.latitude]
			selectedLocation.value = { ...props.initialLocation }
		} else {
			console.log('没有初始位置，使用默认位置')
			// 重置为默认状态
			selectedLocation.value = {
				longitude: null,
				latitude: null,
				address: ''
			}
		}
	}

	// 地图初始化完成
	const handleMapComplete = () => {
		nextTick(() => {
			// 初始化地理编码器
			geocoder = new BMapGL.Geocoder()
			console.log('地图初始化完成，当前选中位置：', selectedLocation.value)

			// 如果有初始位置，显示标记并设置地图中心
			if (selectedLocation.value.longitude && selectedLocation.value.latitude) {
				console.log('发现初始位置，准备显示标记：', selectedLocation.value.longitude, selectedLocation.value.latitude)

				// 设置地图中心点
				const map = baiduMapRef.value?.baiduMap
				if (map) {
					const point = new BMapGL.Point(selectedLocation.value.longitude, selectedLocation.value.latitude)
					map.setCenter(point)
				}

				// 使用百度地图组件的方法直接添加标记
				const mapRef = baiduMapRef.value
				if (mapRef && mapRef.addSelectedMarker) {
					mapRef.addSelectedMarker(selectedLocation.value.longitude, selectedLocation.value.latitude, '初始位置')
				}

				// 如果没有地址信息，进行逆解析
				if (!selectedLocation.value.address) {
					const point = new BMapGL.Point(selectedLocation.value.longitude, selectedLocation.value.latitude)
					geocoder.getLocation(point, (result) => {
						if (result) {
							selectedLocation.value.address = result.address
							console.log('初始位置地址逆解析成功：', result.address)
						}
					})
				}
			}
		})
	}

	// 处理地图点击事件（使用百度地图组件的mapClick事件）
	const handleMapClick = (coordinates) => {
		console.log('处理地图点击事件，接收到坐标：', coordinates)
		const [lng, lat] = coordinates

		// 更新选中位置的经纬度
		selectedLocation.value.longitude = Number(lng.toFixed(6))
		selectedLocation.value.latitude = Number(lat.toFixed(6))

		console.log('更新后的选中位置：', selectedLocation.value)

		// 进行地址逆解析
		if (geocoder) {
			const point = new BMapGL.Point(lng, lat)
			geocoder.getLocation(point, (result) => {
				if (result) {
					selectedLocation.value.address = result.address
					console.log('地址逆解析成功：', result.address)
				} else {
					selectedLocation.value.address = '地址解析失败'
					console.log('地址逆解析失败')
				}
			})
		} else {
			console.log('地理编码器未初始化')
		}
	}

	// 设置地图点击事件（已废弃，改用mapClick事件）
	const setupMapClickEvent = () => {
		// 不再需要，已改用百度地图组件的mapClick事件
	}

	// 处理百度地图点击（已废弃）
	const handleBaiduMapClick = (e) => {
		// 不再需要，已改用百度地图组件的mapClick事件
	}

	// 添加标记
	const addMarker = (position) => {
		const mapRef = baiduMapRef.value
		if (!mapRef) return

		// 不需要清除所有覆盖物，因为百度地图组件已经处理了点选标记
		// mapRef.clearOverlay()

		// 可以添加额外的标记样式或信息窗口
		const markerData = [
			{
				position: position,
				title: '选择位置'
			}
		]
		// 如果需要添加额外的标记，可以使用 renderIconMarker 等方法
		// mapRef.renderMarker(markerData)
	}

	// 搜索位置
	const searchLocation = () => {
		if (!searchKeyword.value.trim()) {
			message.warning('请输入搜索关键词')
			return
		}
		searchLocationByBaidu()
	}

	// 百度地图搜索
	const searchLocationByBaidu = () => {
		if (!geocoder) {
			geocoder = new BMapGL.Geocoder()
		}

		console.log('开始搜索位置：', searchKeyword.value)
		geocoder.getPoint(searchKeyword.value, (point) => {
			if (point) {
				console.log('搜索成功，找到坐标：', point.lng, point.lat)
				mapCenter.value = [point.lng, point.lat]

				// 设置地图中心点
				const map = baiduMapRef.value?.baiduMap
				if (map) {
					map.setCenter(point)
					console.log('地图中心点已设置')
				}

				// 更新选中位置
				selectedLocation.value.longitude = Number(point.lng.toFixed(6))
				selectedLocation.value.latitude = Number(point.lat.toFixed(6))
				selectedLocation.value.address = searchKeyword.value

				// 使用百度地图组件的方法直接添加标记
				const mapRef = baiduMapRef.value
				if (mapRef && mapRef.addSelectedMarker) {
					mapRef.addSelectedMarker(point.lng, point.lat, '搜索位置')
				}

				// 进行地址逆解析以获取更准确的地址
				geocoder.getLocation(point, (result) => {
					if (result) {
						selectedLocation.value.address = result.address
						console.log('搜索位置地址逆解析成功：', result.address)
					}
				})
			} else {
				console.log('搜索失败')
				message.error('搜索失败，请检查关键词')
			}
		})
	}

	// 获取当前位置
	const getCurrentLocation = () => {
		console.log('开始获取当前位置')
		if (navigator.geolocation) {
			navigator.geolocation.getCurrentPosition(
				(position) => {
					const { longitude, latitude } = position.coords
					console.log('获取当前位置成功：', longitude, latitude)
					mapCenter.value = [longitude, latitude]

					// 设置地图中心点
					const map = baiduMapRef.value?.baiduMap
					if (map) {
						map.setCenter(new BMapGL.Point(longitude, latitude))
						console.log('地图中心点已设置到当前位置')
					}

					// 更新选中位置
					selectedLocation.value.longitude = Number(longitude.toFixed(6))
					selectedLocation.value.latitude = Number(latitude.toFixed(6))

					// 使用百度地图组件的方法直接添加标记
					const mapRef = baiduMapRef.value
					if (mapRef && mapRef.addSelectedMarker) {
						mapRef.addSelectedMarker(longitude, latitude, '当前位置')
					}

					// 进行地址逆解析
					if (geocoder) {
						const point = new BMapGL.Point(longitude, latitude)
						geocoder.getLocation(point, (result) => {
							if (result) {
								selectedLocation.value.address = result.address
								console.log('当前位置地址逆解析成功：', result.address)
							}
						})
					}
				},
				(error) => {
					console.error('获取位置失败:', error)
					message.error('获取当前位置失败，请检查浏览器权限设置')
				}
			)
		} else {
			console.log('浏览器不支持地理定位')
			message.error('浏览器不支持地理定位')
		}
	}

	// 确认选择
	const confirmSelection = () => {
		if (!selectedLocation.value.longitude || !selectedLocation.value.latitude) {
			message.warning('请先选择位置')
			return
		}
		emit('confirm', selectedLocation.value)
		visible.value = false
	}

	// 取消选择
	const handleCancel = () => {
		emit('cancel')
		visible.value = false
	}

	// 清除选择
	const clearSelection = () => {
		selectedLocation.value = {
			longitude: null,
			latitude: null,
			address: ''
		}
		const mapRef = baiduMapRef.value
		if (mapRef) {
			// 使用专门的清除点选标记方法，而不是清除所有覆盖物
			mapRef.clearSelectedMarker()
		}
	}

	// 暴露给父组件的方法
	defineExpose({
		open
	})
</script>

<style scoped>
	.map-selector-container {
		padding: 16px 0;
	}

	.search-bar {
		margin-bottom: 16px;
	}

	.map-container {
		border: 1px solid #d9d9d9;
		border-radius: 6px;
		overflow: hidden;
		margin-bottom: 16px;
	}

	.location-info {
		background: #f5f5f5;
		padding: 16px;
		border-radius: 6px;
		margin-bottom: 16px;
	}

	.info-item {
		margin-bottom: 8px;
	}

	.info-item:last-child {
		margin-bottom: 0;
	}

	.label {
		font-weight: 500;
		color: #666;
		margin-right: 8px;
	}

	.value {
		color: #333;
	}

	.action-buttons {
		text-align: right;
	}
</style>
