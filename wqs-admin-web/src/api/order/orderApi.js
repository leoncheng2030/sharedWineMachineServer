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

const request = (url, ...arg) => baseRequest(`/order/manage/` + url, ...arg)

/**
 * 订单管理接口api
 *
 * @author WQS_TEAM
 * @date 2025-01-27
 */
export default {
	// 获取订单分页
	orderManagePage(data) {
		return request('page', data, 'get')
	},
	// 新增订单
	orderManageAdd(data) {
		return request('add', data)
	},
	// 编辑订单
	orderManageEdit(data) {
		return request('edit', data)
	},
	// 删除订单
	orderManageDelete(data) {
		return request('delete', data)
	},
	// 获取订单详情
	orderManageDetail(data) {
		return request('detail', data, 'get')
	},
	// 批量删除订单
	orderManageBatchDelete(data) {
		return request('batchDelete', data)
	},
	// 支付订单
	orderManagePay(data) {
		return request('pay', data)
	},
	// 取消订单
	orderManageCancel(data) {
		return request('cancel', data)
	},
	// 开始出酒
	orderManageStartDispense(data) {
		return request('startDispense', data)
	},
	// 完成出酒
	orderManageCompleteDispense(data) {
		return request('completeDispense', data)
	},
	// 更新订单状态
	orderManageUpdateStatus(data) {
		return request('updateStatus', data)
	},
	// 获取订单统计信息
	orderManageStatistics(data) {
		return request('statistics', data, 'get')
	},
	// 根据设备ID获取订单列表
	orderManageByDevice(data) {
		return request('byDevice', data, 'get')
	},
	// 订单导出
	orderManageExport(data) {
		return request('export', data, 'get', {
			responseType: 'blob'
		})
	},
	// 获取订单列表（不分页）
	orderManageList(data) {
		return request('list', data, 'get')
	}
} 