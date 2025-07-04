/**
 *  Copyright [2022] [https://www.xiaonuo.vip]
 *	Snowy采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *	1.请不要删除和修改根目录下的LICENSE文件。
 *	2.请不要删除和修改Snowy源码头部的版权声明。
 *	3.本项目代码可免费商业使用，商业使用请保留源码和相关描述文件的项目出处，作者声明等。
 *	4.分发源码时候，请注明软件出处 https://www.xiaonuo.vip
 *	5.不可二次分发开源参与同类竞品，如有想法可联系团队xiaonuobase@qq.com商议合作。
 *	6.若您的项目无法满足以上几点，需要更多功能代码，获取Snowy商业授权许可，请在官网购买授权，地址为 https://www.xiaonuo.vip
 */
import { baseRequest } from '@/utils/request'

const request = (url, ...arg) => baseRequest(`/client/user/` + url, ...arg)
/**
 * 前台用户接口api
 *
 * @author yubaoshan
 * @date 2025-06-01 22:26:20
 */
export default {
	// 获取用户分页
	userPage(data) {
		return request('page', data, 'get')
	},
	// 提交表单 edit为true时为编辑，默认为新增
	submitForm(data, edit = false) {
		return request(edit ? 'edit' : 'add', data)
	},
	// 删除用户
	userDelete(data) {
		return request('delete', data)
	},
	// 获取用户详情
	userDetail(data) {
		return request('detail', data, 'get')
	},
	// 批量删除用户
	userBatchDelete(data) {
		return request('batchDelete', data)
	},
	// 重置用户密码
	userResetPassword(data) {
		return request('resetPassword', data)
	},
	// 修改用户状态
	userUpdateStatus(data) {
		return request('updateStatus', data)
	},
	// 启用用户
	userEnable(data) {
		return request('enable', data)
	},
	// 禁用用户
	userDisable(data) {
		return request('disable', data)
	},
	// 获取用户选择器数据（用于门店管理员选择等场景）
	userSelector(data) {
		return request('selector', data, 'get')
	},
	// 根据ID列表获取用户信息（用于选择器回显）
	userListByIdList(data) {
		return request('listByIdList', data)
	}
}
