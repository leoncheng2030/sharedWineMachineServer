import { baseRequest } from '@/utils/request'

const request = (url, ...arg) => baseRequest(`/device/info/${url}`, ...arg)

/**
 * 设备管理API
 *
 * @author wqs
 * @date 2024/01/01
 **/
export default {
	// 获取设备分页
	page(data) {
		return request('page', data, 'get')
	},
	// 获取设备列表
	list(data) {
		return request('list', data, 'get')
	},
	// 获取设备详情
	detail(data) {
		return request('detail', data, 'get')
	},
	// 添加设备
	add(data) {
		return request('add', data)
	},
	// 编辑设备
	edit(data) {
		return request('edit', data)
	},
	// 删除设备
	delete(data) {
		return request('delete', data)
	},
	// 批量删除设备
	batchDelete(data) {
		return request('batchDelete', data)
	},
	// 导出设备数据
	export(data) {
		return request('export', data, 'get', { responseType: 'blob' })
	},
	// 获取设备选择器
	selector(data) {
		return request('selector', data, 'get')
	},
	// 绑定酒品
	bindProduct(deviceId, productId) {
		return request('bindProduct', { deviceId, productId })
	},
	// 解绑酒品
	unbindProduct(deviceId) {
		return request('unbindProduct', { deviceId })
	},
	// 获取设备绑定历史
	getBindHistory(deviceId) {
		return request('bindHistory', { deviceId }, 'get')
	},
	// 更新设备状态
	updateStatus(data) {
		return request('updateStatus', data)
	},
	// 设备上线
	online(deviceId) {
		return request('online', { deviceId })
	},
	// 设备离线
	offline(deviceId) {
		return request('offline', { deviceId })
	},
	// 设备进入维护状态
	maintenance(deviceId) {
		return request('maintenance', { deviceId })
	}
}
