import { baseRequest } from '@/utils/request'

const request = (url, ...arg) => baseRequest(`/commission/withdraw/${url}`, ...arg)

/**
 * 提现记录API
 */
export default {
	// 获取提现记录分页列表
	wineWithdrawRecordPage(data) {
		return request('page', data, 'get')
	},

	// 获取提现记录详情
	wineWithdrawRecordDetail(data) {
		return request('detail', data, 'get')
	},

	// 申请提现
	wineWithdrawRecordApply(data) {
		return request('apply', data, 'post')
	},

	// 审核提现
	wineWithdrawRecordAudit(data) {
		return request('audit', data, 'post')
	},

	// 取消提现
	wineWithdrawRecordCancel(data) {
		return request('cancel', data, 'post')
	},

	// 确认提现成功
	wineWithdrawRecordConfirmSuccess(data) {
		return request('confirmSuccess', data, 'post')
	},

	// 标记提现失败
	wineWithdrawRecordMarkFailed(data) {
		return request('markFailed', data, 'post')
	},

	// 获取用户提现记录
	wineWithdrawRecordUserList(data) {
		return request('userList', data, 'get')
	},

	// 获取提现汇总统计
	wineWithdrawRecordSummary(data) {
		return request('summary', data, 'get')
	},

	// 统计待审核提现数量
	wineWithdrawRecordCountPending(data) {
		return request('countPending', data, 'get')
	},

	// 导出提现记录数据
	wineWithdrawRecordExport(data) {
		return request('export', data, 'get')
	}
}
