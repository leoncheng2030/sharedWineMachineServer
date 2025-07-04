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
package vip.wqs.client.modular.user.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vip.wqs.client.modular.user.entity.ClientUser;
import vip.wqs.client.modular.user.enums.ClientUserSourceFromTypeEnum;
import vip.wqs.client.modular.user.param.*;
import vip.wqs.client.modular.user.service.ClientUserService;
import vip.wqs.common.annotation.CommonLog;
import vip.wqs.common.pojo.CommonResult;

import javax.validation.Valid;
import java.util.List;

/**
 * C端用户控制器
 *
 * @author xuyuxiang
 * @date 2022/4/22 9:34
 **/
@Tag(name = "C端用户控制器")
@ApiSupport(author = "WQS_TEAM", order = 1)
@RestController
@Validated
public class ClientUserController {

    @Resource
    private ClientUserService clientUserService;

    /**
     * 获取用户分页
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperationSupport(order = 1)
    @Operation(summary = "获取用户分页")
    @SaCheckPermission("/client/user/page")
    @GetMapping("/client/user/page")
    public CommonResult<Page<ClientUser>> page(ClientUserPageParam clientUserPageParam) {
        return CommonResult.data(clientUserService.page(clientUserPageParam));
    }

    /**
     * 添加用户
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:47
     */
    @ApiOperationSupport(order = 2)
    @Operation(summary = "添加用户")
    @CommonLog("添加用户")
    @SaCheckPermission("/client/user/add")
    @PostMapping("/client/user/add")
    public CommonResult<String> add(@RequestBody @Valid ClientUserAddParam clientUserAddParam) {
        clientUserService.add(clientUserAddParam, ClientUserSourceFromTypeEnum.SYSTEM_ADD.getValue());
        return CommonResult.ok();
    }

    /**
     * 编辑用户
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:47
     */
    @ApiOperationSupport(order = 3)
    @Operation(summary = "编辑用户")
    @CommonLog("编辑用户")
    @SaCheckPermission("/client/user/edit")
    @PostMapping("/client/user/edit")
    public CommonResult<String> edit(@RequestBody @Valid ClientUserEditParam clientUserEditParam) {
        clientUserService.edit(clientUserEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除用户
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperationSupport(order = 4)
    @Operation(summary = "删除用户")
    @CommonLog("删除用户")
    @SaCheckPermission("/client/user/delete")
    @PostMapping("/client/user/delete")
    public CommonResult<String> delete(@RequestBody @Valid @NotEmpty(message = "集合不能为空")
                                       List<ClientUserIdParam> clientUserIdParamList) {
        clientUserService.delete(clientUserIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取用户详情
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperationSupport(order = 5)
    @Operation(summary = "获取用户详情")
    @SaCheckPermission("/client/user/detail")
    @GetMapping("/client/user/detail")
    public CommonResult<ClientUser> detail(@Valid ClientUserIdParam clientUserIdParam) {
        return CommonResult.data(clientUserService.detail(clientUserIdParam));
    }

    /**
     * 获取用户选择器数据
     *
     * @author wqs_team
     * @date 2025/01/01 10:00
     */
    @ApiOperationSupport(order = 6)
    @Operation(summary = "获取用户选择器数据")
    @SaCheckPermission("/client/user/selector")
    @GetMapping("/client/user/selector")
    public CommonResult<Page<ClientUser>> selector(ClientUserPageParam clientUserPageParam) {
        return CommonResult.data(clientUserService.selector(clientUserPageParam));
    }

    /**
     * 根据ID列表获取用户信息
     *
     * @author wqs_team
     * @date 2025/01/01 10:00
     */
    @ApiOperationSupport(order = 7)
    @Operation(summary = "根据ID列表获取用户信息")
    @SaCheckPermission("/client/user/listByIdList")
    @PostMapping("/client/user/listByIdList")
    public CommonResult<List<ClientUser>> listByIdList(@RequestBody @Valid ClientUserIdListParam clientUserIdListParam) {
        return CommonResult.data(clientUserService.listByIdList(clientUserIdListParam.getIdList()));
    }
}
