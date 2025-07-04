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

const request = (url, ...arg) => baseRequest(`/payment/config/` + url, ...arg)

/**
 * 支付配置管理接口api
 *
 * @author wqs
 * @date 2025-01-30
 */
export default {
	// 获取支付配置分页
	paymentConfigPage(data) {
		return request('page', data, 'get')
	},
	// 新增支付配置
	paymentConfigAdd(data) {
		return request('add', data)
	},
	// 编辑支付配置
	paymentConfigEdit(data) {
		return request('edit', data)
	},
	// 删除支付配置
	paymentConfigDelete(data) {
		return request('delete', data)
	},
	// 获取支付配置详情
	paymentConfigDetail(data) {
		return request('detail', data, 'get')
	},
	// 批量删除支付配置
	paymentConfigBatchDelete(data) {
		return request('batchDelete', data)
	},
	// 启用支付配置
	paymentConfigEnable(data) {
		return request('enable', data)
	},
	// 禁用支付配置
	paymentConfigDisable(data) {
		return request('disable', data)
	},
	// 获取支付配置选择器
	paymentConfigSelector(data) {
		return request('selector', data, 'get')
	},
	// 获取所有启用的支付配置
	paymentConfigEnabled(data) {
		return request('enabled', data, 'get')
	}
} 