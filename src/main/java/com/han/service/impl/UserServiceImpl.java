package com.han.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.han.mapper.PermissionMapper;
import com.han.model.Film;
import com.han.model.Permission;
import com.han.model.User;
import com.han.mapper.UserMapper;
import com.han.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.han.utils.SaltUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 1Han
 * @since 2021-01-26
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private PermissionMapper permissionMapper;

    @Override
    public boolean register(User user) {
        boolean b = queryUserByUsername(user.getUsername());
        if (!b){
            return false;
        }

        String salt = SaltUtils.getSalt(4);
        Md5Hash md5Hash = new Md5Hash(user.getPassword(), salt, 1024);
        String s = md5Hash.toHex();

        user.setSalt(salt);
        user.setPassword(s);

        int insert = userMapper.insert(user);
        if (insert>0){
            return true;
        }
        return false;
    }

    @Override
    public boolean queryUserByUsername(String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username);
        User user = userMapper.selectOne(wrapper);

        return ObjectUtils.isEmpty(user);
    }

    @Override
    public List<Permission> queryMenusByUid(int userId) {
        List<Permission> permissions = permissionMapper.queryMenusByUid(userId);

        ArrayList<Permission> root = new ArrayList<>();

        permissions.forEach(permission -> {
            if (permission.getLevel()==1){
                permission.setChild(new ArrayList<Permission>());
                root.add(permission);
            }
        });
        permissions.forEach(permission -> {
            root.forEach(roots -> {
                if(permission.getPid()==roots.getId()){
                    roots.getChild().add(permission);
                }
            });
        });

        return root;
    }

    @Override
    public IPage<User> queryUsers(Page<User> Page, QueryWrapper<User> wrapper) {
        IPage<User> userIPage = userMapper.selectPage(Page, null);
        return userIPage;
    }
}
