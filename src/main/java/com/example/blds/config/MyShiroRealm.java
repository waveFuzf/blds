package com.example.blds.config;

import com.example.blds.entity.HzLoginInfo;
import com.example.blds.service.HzLoginInfoService;
import com.example.blds.service.HzUserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;

public class MyShiroRealm extends AuthorizingRealm {
    @Resource
    public HzLoginInfoService hzUserService;


    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        return authorizationInfo;
    }

    /**
     * 登录认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //获取用户账号
        AuthenticationInfo authenticationInfo;
        String username = token.getPrincipal().toString();
        String password = hzUserService.getByUsername(username).getPassword();
        if (password!=null) {
             authenticationInfo= new SimpleAuthenticationInfo(
                    username,
                    password,
                    getName());

            return authenticationInfo;
        }
        return null;
    }
}
