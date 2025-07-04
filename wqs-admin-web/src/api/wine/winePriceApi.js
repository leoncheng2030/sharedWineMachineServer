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

const request = (url, ...arg) => baseRequest(`/wine/price/` + url, ...arg)

/**
 * 酒品价格管理接口api
 *
 * @author WQS_TEAM
 * @date 2025-01-27
 */
export default {
	// 获取酒品价格分页
	pricePage(data) {
		return request('page', data, 'get')
	},
	// 提交表单 edit为true时为编辑，默认为新增
	submitForm(data, edit = false) {
		return request(edit ? 'edit' : 'add', data)
	},
	// 删除酒品价格
	priceDelete(data) {
		return request('delete', data)
	},
	// 获取酒品价格详情
	priceDetail(data) {
		return request('detail', data, 'get')
	},
	// 批量删除酒品价格
	priceBatchDelete(data) {
		return request('batchDelete', data)
	},
	// 启用价格策略
	priceEnable(data) {
		return request('enable', data)
	},
	// 禁用价格策略
	priceDisable(data) {
		return request('disable', data)
	},
	// 获取酒品选择器
	priceProductSelector(data) {
		return request('productSelector', data, 'get')
	},
	// 价格策略导出
	priceExport(data) {
		return request('export', data, 'get', {
			responseType: 'blob'
		})
	},
	// 获取价格策略列表（不分页）
	priceList(data) {
		return request('list', data, 'get')
	},
	// 批量设置价格
	priceBatchSet(data) {
		return request('batchSet', data)
	},
	// 复制价格策略
	priceCopy(data) {
		return request('copy', data)
	},
	// 获取价格历史记录
	priceHistory(data) {
		return request('history', data, 'get')
	},
	// 价格预览（计算折扣后价格）
	pricePreview(data) {
		return request('preview', data, 'get')
	},
	// 获取当前有效价格
	getCurrentPrice(data) {
		return request('current', data, 'get')
	}
} 