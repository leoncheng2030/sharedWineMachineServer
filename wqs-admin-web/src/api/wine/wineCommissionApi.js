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

const request = (url, ...arg) => baseRequest(`/commission/config/` + url, ...arg)

/**
 * 佣金管理API
 *
 * @author wqs
 * @date 2024/01/01
 **/
export default {
	// 获取佣金分页
	page(data) {
		return request('page', data, 'get')
	},
	// 获取佣金列表
	list(data) {
		return request('list', data, 'get')
	},
	// 获取佣金详情
	detail(data) {
		return request('detail', data, 'get')
	},
	// 添加佣金
	add(data) {
		return request('add', data)
	},
	// 编辑佣金
	edit(data) {
		return request('edit', data)
	},
	// 删除佣金
	delete(data) {
		return request('delete', data)
	},
	// 批量删除佣金
	batchDelete(data) {
		return request('batchDelete', data)
	},
	// 导出佣金数据
	export(data) {
		return request('export', data, 'get', { responseType: 'blob' })
	},
	// 获取门店选择器
	storeSelector(data) {
		return request('storeSelector', data, 'get')
	},
	// 获取酒品选择器
	productSelector(data) {
		return request('productSelector', data, 'get')
	},
	// 启用佣金配置
	enable(data) {
		return request('enable', data)
	},
	// 禁用佣金配置
	disable(data) {
		return request('disable', data)
	},

	// 获取佣金历史记录
	history(data) {
		return request('history', data, 'get')
	},
	// 佣金计算预览
	preview(data) {
		return request('preview', data, 'get')
	}
} 