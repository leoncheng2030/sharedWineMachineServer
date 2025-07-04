/**
 * 佣金记录管理API
 *
 * @author WQS_TEAM
 * @date 2025-01-29
 **/
import { baseRequest } from '@/utils/request'

const request = (url, ...arg) => baseRequest(`/commission/record/` + url, ...arg)

export default {
	// 获取佣金记录分页
	page(data) {
		return request('page', data, 'get')
	},
	// 获取佣金记录列表
	list(data) {
		return request('list', data, 'get')
	},
	// 获取佣金记录详情
	detail(data) {
		return request('detail', data, 'get')
	},
	// 导出佣金记录数据
	export(data) {
		return request('export', data, 'get', { responseType: 'blob' })
	},
	// 发放佣金
	settle(data) {
		return request('settle', data)
	},
	// 冻结佣金
	freeze(data) {
		return request('freeze', data)
	},
	// 取消佣金
	cancel(data) {
		return request('cancel', data)
	},
	// 重新计算佣金
	recalculate(data) {
		return request('recalculate', data)
	},
	// 批量发放佣金
	batchSettle(data) {
		return request('batchSettle', data)
	},
	// 批量冻结佣金
	batchFreeze(data) {
		return request('batchFreeze', data)
	},
	// 批量取消佣金
	batchCancel(data) {
		return request('batchCancel', data)
	}
} 