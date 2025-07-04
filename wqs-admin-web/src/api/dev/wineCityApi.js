import { baseRequest } from '@/utils/request'

const request = (url, ...arg) => baseRequest(`/dev/city/${url}`, ...arg)

/**
 * 城市管理API
 *
 * @author wqs
 * @date 2025/01/30
 **/
export default {
	// 获取城市分页
	page(data) {
		return request('page', data, 'get')
	},
	// 获取城市列表
	list(data) {
		return request('list', data, 'get')
	},
	// 获取城市详情
	detail(data) {
		return request('detail', data, 'get')
	},
	// 添加城市
	add(data) {
		return request('add', data)
	},
	// 编辑城市
	edit(data) {
		return request('edit', data)
	},
	// 删除城市
	delete(data) {
		return request('delete', data)
	},
	// 批量删除城市
	batchDelete(data) {
		return request('batchDelete', data)
	},
	// 导出城市数据
	export(data) {
		return request('export', data, 'get', { responseType: 'blob' })
	},
	// 获取城市选择器
	selector(data) {
		return request('selector', data, 'get')
	},
	// 启用城市
	enable(data) {
		return request('enable', data)
	},
	// 禁用城市
	disable(data) {
		return request('disable', data)
	},
	// 根据层级获取城市列表
	listByLevel(level) {
		return request('listByLevel', { level }, 'get')
	},
	// 根据父级编码获取子级城市列表
	listByParentCode(parentCode) {
		return request('listByParentCode', { parentCode }, 'get')
	},
	// 获取热门城市列表
	listHotCities() {
		return request('listHotCities', {}, 'get')
	},
	// 设置热门城市
	setHotCity(data) {
		return request('setHotCity', data)
	},
	// 设置配送支持
	setSupportDelivery(data) {
		return request('setSupportDelivery', data)
	},
	// 更新城市统计数据
	updateStatistics(cityCode) {
		return request('updateStatistics', { cityCode })
	}
}
