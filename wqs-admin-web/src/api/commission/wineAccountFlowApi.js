import { baseRequest } from '@/utils/request'

const request = (url, ...arg) => baseRequest(`/commission/flow/${url}`, ...arg)

/**
 * 账户流水API
 */
export default {
	// 获取账户流水分页列表
	wineAccountFlowPage(data) {
		return request('page', data, 'get')
	},

	// 获取账户流水详情
	wineAccountFlowDetail(data) {
		return request('detail', data, 'get')
	},

	// 获取用户流水列表
	wineAccountFlowUserList(data) {
		return request('userList', data, 'get')
	},

	// 获取流水汇总统计
	wineAccountFlowSummary(data) {
		return request('summary', data, 'get')
	},

	// 根据关联ID获取流水
	wineAccountFlowByRelated(data) {
		return request('byRelated', data, 'get')
	},

	// 导出账户流水数据
	wineAccountFlowExport(data) {
		return request('export', data, 'get')
	}
}
