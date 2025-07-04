/**
 * 城市管理API
 * @author system
 * @date 2025/01/30
 */
import { baseRequest } from '@/utils/request'

const request = (url, ...arg) => baseRequest(`/dev/city/` + url, ...arg)

/**
 * 城市管理API接口管理器
 */
export default {
	// 获取城市分页列表
	cityPage(data) {
		return request('page', data, 'get')
	},
	// 获取城市列表
	cityList(data) {
		return request('list', data, 'get')
	},
	// 获取城市树形结构
	cityTree(data) {
		return request('tree', data, 'get')
	},
	// 根据父级编码获取下级城市
	cityChildren(parentCode) {
		return request('children', { parentCode }, 'get')
	},
	// 根据层级获取城市列表
	cityByLevel(level) {
		return request('listByLevel', { level }, 'get')
	},
	// 获取省份列表（level=1）
	provinceList() {
		return request('listByLevel', { level: 1 }, 'get')
	},
	// 根据省份编码获取城市列表（level=2）
	cityByProvince(provinceCode) {
		return request('listByParentCode', { parentCode: provinceCode }, 'get')
	},
	// 根据城市编码获取区县列表（level=3）
	districtByCity(cityCode) {
		return request('listByParentCode', { parentCode: cityCode }, 'get')
	},
	// 获取热门城市
	hotCityList() {
		return request('listHotCities', {}, 'get')
	},
	// 根据城市编码获取城市详情
	cityDetail(cityCode) {
		return request('detail', { cityCode }, 'get')
	},
	// 搜索城市（支持名称、拼音搜索）
	citySearch(keyword) {
		return request('list', { searchKey: keyword }, 'get')
	},
	// 添加城市
	cityAdd(data) {
		return request('add', data, 'post')
	},
	// 编辑城市
	cityEdit(data) {
		return request('edit', data, 'post')
	},
	// 删除城市
	cityDelete(data) {
		return request('delete', data, 'post')
	},
	// 批量删除城市
	cityBatchDelete(data) {
		return request('batchDelete', data, 'post')
	},
	// 获取城市列表（根据层级）
	listByLevel(level) {
		return request('listByLevel', { level }, 'get')
	},
	// 获取子级城市列表
	listByParentCode(parentCode) {
		return request('listByParentCode', { parentCode }, 'get')
	},
	// 根据城市名称查找城市信息（用于反显）
	findByNames(params) {
		return request('findByNames', params, 'get')
	}
} 