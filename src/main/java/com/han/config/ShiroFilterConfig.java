package com.han.config;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.ShiroFilter;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroFilterConfig {

    @Bean
    public Realm realm(){
        MyRealm realm = new MyRealm();

        //开启缓存
        realm.setCachingEnabled(true);
        realm.setAuthenticationCachingEnabled(true);
        realm.setAuthorizationCachingEnabled(true);
        realm.setCacheManager(new EhCacheManager());

        return realm;
    }

    @Bean
    public DefaultWebSecurityManager manager(){
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(realm());
        manager.setRememberMeManager(cookieRememberMeManager());
        return manager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilter() {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(manager());
        shiroFilter.setLoginUrl("/login.html");//身份认证失败，则跳转到登录页面的配置 没有登录的用户请求需要登录的页面时自动跳转到登录页面，不是必须的属性，不输入地址的话会自动寻找项目web项目的根目录下的”/login.jsp”页面。

        Map<String, Filter> filters = shiroFilter.getFilters();
        filters.put("jwt",new JwtFilter());

        Map<String, String> filterMap = new LinkedHashMap<>();
        filterMap.put("/user/**", "anon");
        filterMap.put("/role/**", "anon");
        filterMap.put("/user/logout", "logout");
        filterMap.put("/js/**", "anon");
        filterMap.put("/css/**", "anon");
        filterMap.put("/user/register", "anon");
        filterMap.put("/permission/**", "anon");
        filterMap.put("/register.html", "anon");
        filterMap.put("/login.html", "anon");

        filterMap.put("/**", "jwt");
        shiroFilter.setFilterChainDefinitionMap(filterMap);
        return shiroFilter;
    }
    //配置rememberMe管理器
    @Bean
    public CookieRememberMeManager cookieRememberMeManager(){
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        //创建cookie
        SimpleCookie rememberMe = new SimpleCookie("rememberMe");
        //设置cookie有效期
        rememberMe.setMaxAge(7*24*60*60);
        //将cookie交给rememberMe管理器进行管理
        cookieRememberMeManager.setCookie(rememberMe);
        //设置AES校验，注意不要导错包（使用shiro提供的Base64）
        cookieRememberMeManager.setCipherKey(Base64.decode("a1b2c3d4e5f6h7j8k9l10m=="));
        return cookieRememberMeManager;
    }
}
