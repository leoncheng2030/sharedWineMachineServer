/**
 *  Copyright [2022] [https://www.xiaonuo.vip]
 *
 *  Snowy采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 *  1.请不要删除和修改根目录下的LICENSE文件。
 *  2.请不要删除和修改Snowy源码头部的版权声明。
 *  3.本项目代码可免费商业使用，商业使用请保留源码和相关描述文件的项目出处，作者声明等。
 *  4.分发源码时候，请注明软件出处 https://www.xiaonuo.vip
 *  5.不可二次分发开源参与同类竞品，如有想法可联系团队xiaonuobase@qq.com商议合作。
 *  6.若您的项目无法满足以上几点，需要更多功能代码，获取Snowy商业授权许可，请在官网购买授权，地址为 https://www.xiaonuo.vip
 */
import { baseRequest } from '@/utils/request'

const request = (url, ...arg) => baseRequest(`/device/stock/` + url, ...arg)

const api = {
	// 分页查询设备库存
	page: (parameter) => request('page', parameter, 'get'),
	
	// 获取库存详情
	detail: (parameter) => request('detail', parameter, 'get'),
	
	// 查询库存数量
	quantity: (parameter) => request('quantity', parameter, 'get'),
	
	// 设备补货
	refill: (parameter) => request('refill', parameter, 'post'),
	
	// 调整库存
	adjust: (parameter) => request('adjust', parameter, 'post'),
	
	// 初始化库存
	init: (parameter) => request('init', parameter, 'post'),
	
	// 查询低库存设备
	lowStock: (parameter) => request('lowStock', parameter, 'get'),
	
	// 查询缺货设备
	outOfStock: (parameter) => request('outOfStock', parameter, 'get'),
	
	// 按设备查询库存
	byDevice: (parameter) => request('byDevice', parameter, 'get'),
	
	// 按酒品查询库存
	byProduct: (parameter) => request('byProduct', parameter, 'get'),
	
	// 查询库存变更日志（分页）
	logs: (parameter) => request('logs', parameter, 'get'),
	
	// 导出库存数据
	export: (parameter) => request('export', parameter, 'get', { responseType: 'blob' })
}

export default api
