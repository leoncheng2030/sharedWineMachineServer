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
const DEFAULT_CONFIG = {
	// 是否是微服务cloud版本
	CLOUD_SERVER: false,

	// 首页地址
	DASHBOARD_URL: '/index',

	// 接口地址
	API_URL: import.meta.env.VITE_API_BASEURL,

	// 请求超时
	TIMEOUT: 60000,

	// TokenName // Authorization
	TOKEN_NAME: 'token',

	// Token前缀，注意最后有个空格，如不需要需设置空字符串 // Bearer
	TOKEN_PREFIX: '',

	// 追加其他头
	HEADERS: {},

	// 请求是否开启缓存
	REQUEST_CACHE: false,

	// 布局 经典：classical，双排菜单：doublerow, 顶栏菜单：top
	WQS_LAYOUT: 'doublerow',

	// 菜单是否折叠
	WQS_MENU_COLLAPSE: false,

	// 模块坞
	WQS_MODULE_UNFOLD_OPEN: true,

	// 是否开启多标签
	WQS_LAYOUT_TAGS_OPEN: true,

	// 是否开启展示面包屑
	WQS_BREADCRUMB_OPEN: false,

	// 顶栏是否应用主题色
	WQS_TOP_HEADER_THEME_COLOR_OPEN: false,

	// 顶栏主题色通栏
	WQS_TOP_HEADER_THEME_COLOR_SPREAD: false,

	// 侧边菜单是否排他展开
	WQS_SIDE_UNIQUE_OPEN: true,

	// 登录用户水印
	WQS_LOGIN_USER_WATERMARK_OPEN: false,

	// 页脚版权信息
	WQS_FOOTER_COPYRIGHT_OPEN: false,

	// 圆角风格
	WQS_ROUNDED_CORNER_STYLE_OPEN: false,

	// 语言
	LANG: 'zh-cn',

	// 主题颜色
	COLOR: '#1677FF',

	// 默认整体主题
	WQS_THEME: 'dark',

	// 整体表单风格 modal|drawer
	WQS_FORM_STYLE: 'drawer',

	// 前后台登录链接是否展示
	FRONT_BACK_LOGIN_URL_SHOW: true,

	// 三方登录是否展示
	THREE_LOGIN_SHOW: true,

	// 系统基础配置，这些是数据库中保存起来的
	SYS_BASE_CONFIG: {
		// 默认logo
		WQS_SYS_LOGO: '/img/logo.png',
		// 背景图
		WQS_SYS_BACK_IMAGE: '',
		// 系统名称
		WQS_SYS_NAME: 'Snowy',
		// 版本
		WQS_SYS_VERSION: '3.0',
		// 版权
		WQS_SYS_COPYRIGHT: 'Snowy ©2022 Created by xiaonuo.vip',
		// 版权跳转URL
		WQS_SYS_COPYRIGHT_URL: 'https://www.xiaonuo.vip',
		// 默认文件存储
		WQS_SYS_DEFAULT_FILE_ENGINE: 'LOCAL',
		// 是否开启B端验证码
		WQS_SYS_DEFAULT_CAPTCHA_OPEN_FLAG_FOR_B: 'false',
		// 默认重置密码
		WQS_SYS_DEFAULT_PASSWORD: '123456'
	}
}

export default DEFAULT_CONFIG
