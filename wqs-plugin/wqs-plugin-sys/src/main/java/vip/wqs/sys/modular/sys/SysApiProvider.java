package vip.wqs.sys.modular.sys;

import org.springframework.stereotype.Service;
import vip.wqs.sys.api.SysApi;
import vip.wqs.sys.core.util.SysPasswordUtl;

/**
 * 系统模块综合API接口实现类
 *
 * @author xuyuxiang
 * @date 2022/9/26 14:30
 **/
@Service
public class SysApiProvider implements SysApi {

    @Override
    public String getDefaultPassword() {
        return SysPasswordUtl.getDefaultPassword();
    }
}
