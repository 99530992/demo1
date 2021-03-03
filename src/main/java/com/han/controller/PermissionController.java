package com.han.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.han.dto.Result;
import com.han.mapper.PermissionMapper;
import com.han.mapper.UserMapper;
import com.han.model.Permission;
import com.han.model.User;
import com.han.utils.JWTUtils;
import com.han.vo.PerRoVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.RescaleOp;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 1Han
 * @since 2021-02-03
 */
@RestController
@RequestMapping("/permission")
public class PermissionController {
    @Resource
    private PermissionMapper permissionMapper;
    @Resource
    private UserMapper userMapper;

    @RequestMapping("getPermissionsById")
    public Result getPermissionsById(HttpServletRequest request){
        String token = request.getHeader("Token");
        String username = JWTUtils.decodedToken(token).getClaim("logUser").asString();

        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username);
        User user = userMapper.selectOne(wrapper);

        ArrayList<String> list = new ArrayList<>();
        List<Permission> permissions = permissionMapper.queryThirdMenusByUid(user.getId());
        for (Permission permission : permissions) {
            list.add(permission.getElement());
        }
        return new Result("100",true,"成功",list);
    }

    @RequestMapping("getRolesById")
    public Result getRolesById(@RequestBody PerRoVo perRoVo){
        System.out.println(perRoVo);
        List<String> strings = permissionMapper.queryRolesByUid(perRoVo.getId());
        return new Result("100",true,"成功",strings);
    }

    @RequestMapping("getRoles")
    public Result getRoles(){
        List<String> strings = permissionMapper.queryRoles();
        return new Result("100",true,"成功",strings);
    }

    @RequestMapping("setRoles")
    public Result setRoles(@RequestBody PerRoVo perRoVo){
        //删除中间表中此用户相关信息
        permissionMapper.deleteMsgByUid(perRoVo.getId());
        //新增信息
        List<String> checkList = perRoVo.getCheckList();
        for (String s : checkList) {
            Integer id = permissionMapper.queryIdByRoleName(s);
            permissionMapper.addMsg(perRoVo.getId(),id);
        }

        return new Result("100",true,"成功");
    }
}

