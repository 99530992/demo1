package com.han.controller;


import com.han.dto.Result;
import com.han.mapper.PermissionMapper;
import com.han.mapper.RoleMapper;
import com.han.model.Permission;
import com.han.model.Role;
import com.han.vo.PerRoVo;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 1Han
 * @since 2021-02-04
 */
@RestController
@RequestMapping("/role")
public class RoleController {
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private PermissionMapper permissionMapper;

    @RequestMapping("/getAllRoles")
    public Result getAllRoles(){
        List<Role> roles = roleMapper.selectList(null);
        return new Result("200",true,"成功",roles);
    }

    @GetMapping("getPermissionsById")
    public Result getPermissionsById(String id){
        //根据id获取permission
        List<Permission> permissions = roleMapper.getPermissionsById(Integer.parseInt(id));
        List<Integer> list = new ArrayList<>();
        for (Permission permission : permissions) {
            if (permission.getLevel()==3){
                list.add(permission.getId());
            }
        }
        return new Result("ok",true,"success",list);
    }

    @GetMapping("getAllPer")
    public Result getAllPermissions(){
        List<Permission> permissions = permissionMapper.selectList(null);

        ArrayList<Permission> root = new ArrayList<>();

        permissions.forEach(permission -> {
            if (permission.getLevel() == 1){
                permission.setChild(new ArrayList<Permission>());
                root.add(permission);
            }
        });

        permissions.forEach(permission -> {
            root.forEach(roots -> {
                if(permission.getPid()==roots.getId()){
                    permission.setChild(new ArrayList<Permission>());
                    roots.getChild().add(permission);
                }
            });
        });

        permissions.forEach(permission -> {
            if (permission.getLevel() == 3){
                root.forEach(level1 -> {
                    List<Permission> child = level1.getChild();
                    child.forEach(level2 -> {
                        if(permission.getPid()==level2.getId()){
                            level2.getChild().add(permission);
                        }
                    });
                });
            }
        });
        return new Result("ok",true,"success",root);
    }

    @PostMapping("updatePersById")
    public void updatePersById(@RequestBody PerRoVo perRoVo){
        roleMapper.delById(perRoVo.getId());

        List<String> checkList = perRoVo.getCheckList();
        for (String s : checkList) {
            roleMapper.insertById(perRoVo.getId(),Integer.parseInt(s));
        }
    }
}

