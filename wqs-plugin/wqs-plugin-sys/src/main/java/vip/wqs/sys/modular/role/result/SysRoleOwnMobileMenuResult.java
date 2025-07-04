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
package vip.wqs.sys.modular.role.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 角色拥有的移动端菜单结果
 *
 * @author xuyuxiang
 * @date 2022/7/27 15:08
 **/
@Getter
@Setter
public class SysRoleOwnMobileMenuResult {

    /** 角色id */
    @Schema(description = "角色id")
    private String id;

    /** 已授权移动端菜单信息 */
    @Schema(description = "已授权移动端菜单信息")
    private List<SysRoleOwnMobileMenu> grantInfoList;

    /**
     * 角色拥有移动端菜单类
     *
     * @author xuyuxiang
     * @date 2022/4/28 23:19
     */
    @Getter
    @Setter
    public static class SysRoleOwnMobileMenu {

        /** 菜单id */
        @Schema(description = "菜单id")
        private String menuId;

        /** 按钮id集合 */
        @Schema(description = "按钮id集合")
        private List<String> buttonInfo;
    }
}
