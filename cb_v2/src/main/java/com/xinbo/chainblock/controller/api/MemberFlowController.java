package com.xinbo.chainblock.controller.api;

import com.xinbo.chainblock.annotation.JwtIgnore;
import com.xinbo.chainblock.annotation.RequiredPermission;
import com.xinbo.chainblock.consts.StatusCode;
import com.xinbo.chainblock.bo.BasePageBo;
import com.xinbo.chainblock.entity.*;
import com.xinbo.chainblock.enums.PermissionCodeEnum;
import com.xinbo.chainblock.service.*;
import com.xinbo.chainblock.utils.MapperUtil;
import com.xinbo.chainblock.utils.R;
import com.xinbo.chainblock.vo.MemberFlowVo;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController("ApiMemberFlowController")
@RequestMapping("api/memberFlow")
public class MemberFlowController {

    @Autowired
    private MemberFlowService memberFlowService;

    @JwtIgnore
    @Operation(summary = "findPage", description = "获取注单")
    @PostMapping("findPage/{current}/{size}")
    public R<Object> findPage(@RequestBody MemberFlowVo vo, @PathVariable long current, @PathVariable long size) {
        MemberFlowEntity entity = MapperUtil.to(vo, MemberFlowEntity.class);
        BasePageBo basePageBo = memberFlowService.findPage(entity, current, size);
        return R.builder().code(StatusCode.SUCCESS).data(basePageBo).build();
    }

    @Operation(summary = "test1", description = "测试")
    @PostMapping("test1")
    @RequiredPermission(PermissionCodeEnum.USER_ADD)
    public R<Object> test1() {
        return R.builder().code(StatusCode.SUCCESS).data("success").build();
    }


    @Operation(summary = "test2", description = "测试")
    @PostMapping("test2")
    @RequiredPermission(PermissionCodeEnum.USER_DEL)
    public R<Object> test2() {
        return R.builder().code(StatusCode.SUCCESS).data("success").build();
    }

}
