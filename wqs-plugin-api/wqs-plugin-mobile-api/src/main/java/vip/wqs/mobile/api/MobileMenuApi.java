/*
 * Copyright [2022] [https://www.xiaonuo.vip]
 *
 * Snowy采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改Snowy源码头部的版权声明。
 * 3.本项目代码可免费商业使用，商业使用请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://www.xiaonuo.vip
 * 5.不可二次分发开源参与同类竞品，如有想法可联系团队xiaonuobase@qq.com商议合作。
 * 6.若您的项目无法满足以上几点，需要更多功能代码，获取Snowy商业授权许可，请在官网购买授权，地址为 https://www.xiaonuo.vip
 */
package vip.wqs.mobile.api;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.json.JSONObject;

import java.util.List;

/**
 * 移动端菜单API
 *
 * @author xuyuxiang
 * @date 2023/1/31 10:09
 **/
public interface MobileMenuApi {

    /**
     * 获取移动端菜单授权树
     *
     * @author xuyuxiang
     * @date 2023/1/31 10:10
     **/
    List<JSONObject> mobileMenuTreeSelector();

    /**
     * 获取移动端菜单授权树
     *
     * @author xuyuxiang
     * @date 2023/1/31 10:10
     **/
    List<JSONObject> mobileMenuTreeSelector(List<JSONObject> originDataList);

    /**
     * 获取移动端登录菜单树
     *
     * @author xuyuxiang
     * @date 2023/1/31 10:29
     **/
    List<Tree<String>> loginMobileMenuTree(List<String> menuIdList);
}
