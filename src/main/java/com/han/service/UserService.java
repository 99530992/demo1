package com.han.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.han.model.Permission;
import com.han.model.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 1Han
 * @since 2021-01-26
 */
public interface UserService extends IService<User> {
    boolean register(User user);

    boolean queryUserByUsername(String username);

    List<Permission> queryMenusByUid(int userId);

    IPage<User> queryUsers(Page<User> Page, QueryWrapper<User> wrapper);
}
