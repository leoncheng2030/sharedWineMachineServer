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

const request = (url, ...arg) => baseRequest(`/wine/category/` + url, ...arg)

/**
 * 酒品分类管理接口api
 *
 * @author WQS_TEAM
 * @date 2025-01-27
 */
export default {
	// 获取酒品分类分页
	categoryPage(data) {
		return request('page', data, 'get')
	},
	// 获取酒品分类树形结构
	categoryTree(data) {
		return request('tree', data, 'get')
	},
	// 提交表单 edit为true时为编辑，默认为新增
	submitForm(data, edit = false) {
		return request(edit ? 'edit' : 'add', data)
	},
	// 删除酒品分类
	categoryDelete(data) {
		return request('delete', data)
	},
	// 获取酒品分类详情
	categoryDetail(data) {
		return request('detail', data, 'get')
	},
	// 批量删除酒品分类
	categoryBatchDelete(data) {
		return request('batchDelete', data)
	},
	// 启用酒品分类
	categoryEnable(data) {
		return request('enable', data)
	},
	// 禁用酒品分类
	categoryDisable(data) {
		return request('disable', data)
	},
	// 获取父级分类选择器
	categoryParentSelector(data) {
		return request('parentSelector', data, 'get')
	},
	// 酒品分类导出
	categoryExport(data) {
		return request('export', data, 'get', {
			responseType: 'blob'
		})
	},
	// 获取酒品分类列表（不分页）
	categoryList(data) {
		return request('list', data, 'get')
	},
	// 获取分类下的所有子分类
	categoryChildren(data) {
		return request('children', data, 'get')
	},
	// 移动分类（调整父级）
	categoryMove(data) {
		return request('move', data)
	},
	// 调整分类排序
	categorySort(data) {
		return request('sort', data)
	}
} 