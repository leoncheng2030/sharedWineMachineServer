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
package vip.wqs.sys.api;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * 用户组Api
 *
 * @author xuyuxiang
 * @date 2022/6/6 11:33
 **/
public interface SysGroupApi {

    /**
     * 获取用户组拥有人员
     *
     * @author xuyuxiang
     * @date 2022/5/13 21:00
     */
    List<String> ownUser(String groupId);

    /**
     * 给用户组授权用户
     *
     * @author xuyuxiang
     * @date 2022/8/1 18:28
     */
    void grantUser(String groupId, List<String> userIdList);

    /**
     * 获取用户组选择器
     *
     * @author yubaoshan
     * @date 2025/1/12 02:36
     */
    Page<JSONObject> groupSelector(String searchKey, int current, int size);
}
