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
package vip.wqs.sys.modular.group.provider;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import vip.wqs.sys.api.SysGroupApi;
import vip.wqs.sys.modular.group.param.SysGroupGrantUserParam;
import vip.wqs.sys.modular.group.param.SysGroupIdParam;
import vip.wqs.sys.modular.group.param.SysGroupSelectorParam;
import vip.wqs.sys.modular.group.service.SysGroupService;

import java.util.List;

/**
 * 用户组API接口提供者
 *
 * @author yubaoshan
 * @date  2024/12/21 01:25
 **/
@Service
public class SysGroupApiProvider implements SysGroupApi {

    @Resource
    private SysGroupService sysGroupService;

    @Override
    public List<String> ownUser(String groupId) {
        SysGroupIdParam sysGroupIdParam = new SysGroupIdParam();
        sysGroupIdParam.setId(groupId);
        return sysGroupService.ownUser(sysGroupIdParam);
    }

    @Override
    public void grantUser(String groupId, List<String> userIdList) {
        SysGroupGrantUserParam sysGroupGrantUserParam = new SysGroupGrantUserParam();
        sysGroupGrantUserParam.setId(groupId);
        sysGroupGrantUserParam.setGrantInfoList(userIdList);
        sysGroupService.grantUser(sysGroupGrantUserParam);
    }

    @SuppressWarnings("ALL")
    @Override
    public Page<JSONObject> groupSelector(String searchKey, int current, int size) {
        SysGroupSelectorParam sysGroupSelectorParam = new SysGroupSelectorParam();
        sysGroupSelectorParam.setCurrent(current);
        sysGroupSelectorParam.setSize(size);
        sysGroupSelectorParam.setSearchKey(searchKey);
        return BeanUtil.toBean(sysGroupService.groupSelector(sysGroupSelectorParam), Page.class);
    }
}
