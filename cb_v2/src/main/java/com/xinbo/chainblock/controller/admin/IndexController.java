package com.xinbo.chainblock.controller.admin;


import com.xinbo.chainblock.annotation.JwtIgnore;
import com.xinbo.chainblock.consts.StatusCode;
import com.xinbo.chainblock.dto.PermissionDto;
import com.xinbo.chainblock.dto.UserDto;
import com.xinbo.chainblock.entity.admin.PermissionEntity;
import com.xinbo.chainblock.entity.admin.UserEntity;
import com.xinbo.chainblock.service.UserService;
import com.xinbo.chainblock.bo.JwtUserBo;
import com.xinbo.chainblock.utils.JwtUtil;
import com.xinbo.chainblock.utils.MapperUtil;
import com.xinbo.chainblock.utils.R;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController("adminIndex")
@RequestMapping("/admin")
public class IndexController {

    @Autowired
    private UserService userService;

    @JwtIgnore
    @Operation(summary = "login", description = "后端登录")
    @PostMapping("login")
    public R<Object> login() {
        //Step4: 生成token
        JwtUserBo jwtUserBo = JwtUserBo.builder()
                .uid(1)
                .username("admin")
                .build();
        String token = JwtUtil.generateToken(jwtUserBo);

        JwtUserBo jwtUserBo1 = JwtUtil.parseToken(token);

        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        return R.builder().code(StatusCode.SUCCESS).data(map).build();
    }


    @Operation(summary = "userInfo", description = "会员信息")
    @GetMapping("userInfo")
    public R<Object> userInfo() {
        JwtUserBo jwtUserBo = JwtUtil.getJwtUser();
        UserEntity entity = userService.findById(jwtUserBo.getUid());
        UserDto dto = MapperUtil.to(entity, UserDto.class);
        return R.builder().code(StatusCode.SUCCESS).data(dto).build();
    }

    @Operation(summary = "menu", description = "菜单")
    @GetMapping("menu")
    public R<Object> menu() {
        JwtUserBo jwtUserBo = JwtUtil.getJwtUser();
        List<PermissionEntity> list = userService.menu(jwtUserBo.getUid());

        List<PermissionDto> result = new ArrayList<>();
        for(PermissionEntity entity : list) {
            PermissionDto.Meta meta = PermissionDto.Meta.builder()
                    .title(entity.getTitle())
                    .icon(entity.getIcon())
                    .alwaysShow(true)
                    .build();
            PermissionDto dto = PermissionDto.builder()
                    .path(entity.getPath())
                    .component(entity.getComponent())
                    .redirect(entity.getRedirect())
                    .name(entity.getName())
                    .meta(meta)
                    .build();

            List<PermissionDto> childrenList = new ArrayList<>();
            List<PermissionEntity> children = entity.getChildren();
            for (PermissionEntity c : children) {
                PermissionDto.Meta childrenMeta = PermissionDto.Meta.builder()
                        .title(c.getTitle())
                        .noCache(true)
                        .build();
                PermissionDto childrenDto = PermissionDto.builder()
                        .path(c.getPath())
                        .component(c.getComponent())
                        .name(c.getName())
                        .meta(childrenMeta)
                        .build();
                childrenList.add(childrenDto);
            }
            dto.setChildren(childrenList);

            result.add(dto);
        }

        return R.builder().code(StatusCode.SUCCESS).data(result).build();
    }
}
