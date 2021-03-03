package com.han.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.han.mapper.PermissionMapper;
import com.han.mapper.UserMapper;
import com.han.model.Permission;
import com.han.model.User;
import com.han.utils.JWTUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;


public class MyRealm extends AuthorizingRealm {
    @Resource
    private UserMapper userMapper;
    @Resource
    private PermissionMapper permissionMapper;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("==============授权==============");

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        User user  = (User) principalCollection.getPrimaryPrincipal();
        Integer id = user.getId();

        List<String> strings = permissionMapper.queryRolesByUid(id);
        info.addRoles(strings);

        List<Permission> permissions = permissionMapper.queryMenusByUid(id);
        for (Permission permission : permissions) {
            info.addStringPermission(permission.getElement());
        }

        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String token = (String) authenticationToken.getPrincipal();
        System.out.println("realm-token:"+token);
        String username = JWTUtils.decodedToken(token).getClaim("username").asString();
        System.out.println("realm-username:"+username);

        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username);
        User user = userMapper.selectOne(wrapper);
        if (!ObjectUtils.isEmpty(user)) {
            return new SimpleAuthenticationInfo(user,user.getPassword(),
                    ByteSource.Util.bytes(user.getSalt()),this.getName());
        }
        return null;
    }
}
