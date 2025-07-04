import { baseRequest } from '@/utils/request'

const request = (url, ...arg) => baseRequest(`/commission/account/${url}`, ...arg)

/**
 * 用户账户API
 */
export default {
	// 获取用户账户分页列表
	wineUserAccountPage(data) {
		return request('page', data, 'get')
	},

	// 获取用户账户详情
	wineUserAccountDetail(data) {
		return request('detail', data, 'get')
	},

	// 创建用户账户
	wineUserAccountCreate(data) {
		return request('create', data, 'post')
	},

	// 调整账户余额
	wineUserAccountAdjust(data) {
		return request('adjust', data, 'post')
	},

	// 修改账户状态
	wineUserAccountUpdateStatus(data) {
		return request('updateStatus', data, 'post')
	},

	// 冻结账户
	wineUserAccountFreeze(data) {
		return request('freeze', data, 'post')
	},

	// 解冻账户
	wineUserAccountUnfreeze(data) {
		return request('unfreeze', data, 'post')
	},

	// 禁用账户
	wineUserAccountDisable(data) {
		return request('disable', data, 'post')
	},

	// 启用账户
	wineUserAccountEnable(data) {
		return request('enable', data, 'post')
	},

	// 获取用户账户选择器列表
	wineUserAccountSelector(data) {
		return request('selector', data, 'get')
	},

	// 导出用户账户数据
	wineUserAccountExport(data) {
		return request('export', data, 'get')
	}
}
