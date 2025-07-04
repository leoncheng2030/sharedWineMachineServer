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
package vip.wqs.sys.modular.org.provider;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import vip.wqs.sys.api.SysOrgApi;
import vip.wqs.sys.modular.org.entity.SysOrg;
import vip.wqs.sys.modular.org.param.SysOrgSelectorOrgListParam;
import vip.wqs.sys.modular.org.service.SysOrgService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 组织API接口提供者
 *
 * @author xuyuxiang
 * @date 2022/7/22 14:56
 **/
@Service
public class SysOrgApiProvider implements SysOrgApi {

    @Resource
    private SysOrgService sysOrgService;

    @Override
    public String getNameById(String orgId) {
        return sysOrgService.queryEntity(orgId).getName();
    }

    @Override
    public String getSupervisorIdByOrgId(String orgId) {
        SysOrg sysOrg = sysOrgService.getById(orgId);
        if(ObjectUtil.isNotEmpty(sysOrg)) {
            return sysOrg.getDirectorId();
        }
        return null;
    }

    @Override
    public List<Tree<String>> orgTreeSelector() {
        return sysOrgService.orgTreeSelector();
    }

    @SuppressWarnings("ALL")
    @Override
    public Page<JSONObject> orgListSelector(String parentId) {
        SysOrgSelectorOrgListParam sysOrgSelectorOrgListParam = new SysOrgSelectorOrgListParam();
        sysOrgSelectorOrgListParam.setParentId(parentId);
        return BeanUtil.toBean(sysOrgService.orgListSelector(sysOrgSelectorOrgListParam), Page.class);
    }

    @Override
    public List<String> getParentIdListByOrgId(String orgId) {
        return sysOrgService.getParentIdListByOrgId(orgId);
    }

    @Override
    public List<JSONObject> getOrgListByIdListWithoutException(List<String> orgIdList) {
        return sysOrgService.listByIds(orgIdList).stream().map(JSONUtil::parseObj).collect(Collectors.toList());
    }
}
