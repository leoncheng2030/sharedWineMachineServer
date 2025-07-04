import { baseRequest } from '@/utils/request'

const request = (url, ...arg) => baseRequest(`/store/info/${url}`, ...arg)

/**
 * 门店管理API
 *
 * @author wqs
 * @date 2024/01/01
 **/
export default {
	// 获取门店分页
	page(data) {
		return request('page', data, 'get')
	},
	// 获取门店列表
	list(data) {
		return request('list', data, 'get')
	},
	// 获取门店详情
	detail(data) {
		return request('detail', data, 'get')
	},
	// 添加门店
	add(data) {
		return request('add', data)
	},
	// 编辑门店
	edit(data) {
		return request('edit', data)
	},
	// 删除门店
	delete(data) {
		return request('delete', data)
	},
	// 批量删除门店
	batchDelete(data) {
		return request('batchDelete', data)
	},
	// 导出门店数据
	export(data) {
		return request('export', data, 'get', { responseType: 'blob' })
	},
	// 获取门店选择器
	selector(data) {
		return request('selector', data, 'get')
	},
	// 更新门店状态
	updateStatus(data) {
		return request('updateStatus', data)
	},
	// 启用门店
	enable(data) {
		return request('enable', data)
	},
	// 禁用门店
	disable(data) {
		return request('disable', data)
	},
	// 获取门店管理员选择器
	managerSelector(data) {
		return request('managerSelector', data, 'get')
	},
	// 设置定价权限
	setPriceAuthority(data) {
		return request('setPriceAuthority', data)
	}
}
