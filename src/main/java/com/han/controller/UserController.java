package com.han.controller;


import com.auth0.jwt.JWT;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.han.dto.Result;
import com.han.mapper.UserMapper;
import com.han.model.Film;
import com.han.model.Permission;
import com.han.model.User;
import com.han.service.UserService;
import com.han.utils.JWTUtils;
import com.han.vo.PageVo;
import com.han.vo.UserVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.DelayQueue;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 1Han
 * @since 2021-01-26
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;
    @Resource
    private UserMapper userMapper;
    @RequestMapping("/register")
    public Result login(@RequestBody User user){
        boolean register = userService.register(user);
        if (register){
            return new Result("1",true,"注册成功");
        }
        return new Result("0",false,"注册失败");
    }

    @RequestMapping("login")
    public Result login(@RequestBody UserVo userVo){
        String username = userVo.getUsername();
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username);
        User user = userMapper.selectOne(wrapper);

        if (ObjectUtils.isEmpty(user)){
            return new Result("3",false,"用户不存在");
        }else {
            Md5Hash md5Hash = new Md5Hash(userVo.getPassword(),user.getSalt(),1024);
            String password = md5Hash.toHex();

            if (user.getPassword().equals(password)){
                HashMap<String,String> map = new HashMap<>();
                map.put("logUser",username);
                String token = JWTUtils.createToken(map);
                return new Result("1",true,"登录成功",token);
            }else {
                return new Result("2",false,"密码错误");
            }
        }

    }

    @PostMapping("getMenus")
    public List<Permission> getMenus(HttpServletRequest request){
        String token = request.getHeader("Token");
        String username = JWTUtils.decodedToken(token).getClaim("logUser").asString();

        System.out.println("Controller-username:"+username);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username);
        User user = userMapper.selectOne(wrapper);

        List<Permission> permissions = userService.queryMenusByUid(user.getId());

        return permissions;
    }

    @RequestMapping("getUsers")
    public IPage<User> getUsers(@RequestBody PageVo pageVo){
        QueryWrapper<User> wrapper = new QueryWrapper<>();

        Page<User> page = new Page<>(pageVo.getCurrent(),pageVo.getSize());

        IPage<User> userIPage = userService.queryUsers(page,wrapper);

        return userIPage;
    }

    @RequestMapping("delUserById")
    public void delUserById(@RequestBody UserVo userVo){
        System.out.println(userVo);
        userMapper.deleteById(userVo.getId());
    }
}

