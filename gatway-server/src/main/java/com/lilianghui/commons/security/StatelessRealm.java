package com.lilianghui.commons.security;

import com.lilianghui.client.ShiroFeignClient;
import com.lilianghui.entity.User;
import com.lilianghui.shiro.spring.starter.core.StatelessAuthenticationToken;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

public class StatelessRealm extends AuthorizingRealm implements CredentialsMatcher {
    @Resource
    private ShiroFeignClient shiroFeignClient;

    public StatelessRealm() {
        setCredentialsMatcher(this);
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // // 获取登录时输入的用户名
        Object primaryPrincipal = principals.getPrimaryPrincipal();
        String account = String.valueOf(primaryPrincipal);
        if (primaryPrincipal instanceof User) {
            account = ((User) primaryPrincipal).getId();
        }
        User record = new User();
        User user = shiroFeignClient.selectByPrimaryKey(record);
        if (user != null) {
            // 权限信息对象info,用来存放查出的用户的所有的角色（role）及权限（permission）
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            // 用户的角色集合
            Set<String> roles = new HashSet<>();
            Set<String> permissions = new HashSet<>();

            info.setRoles(roles);
            info.setStringPermissions(permissions);
            // 用户的角色对应的所有权限，如果只使用角色定义访问权限，下面的四行可以不要
            return info;
        }
        return null;
    }

    /* 这里编写用户登录认证代码 */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        StatelessAuthenticationToken token = (StatelessAuthenticationToken) authcToken;

        User record = new User();
        record.setId(String.valueOf(token.getPrincipal()));
        User user = shiroFeignClient.selectByPrimaryKey(record);
        if (user == null) {
            throw new UnknownAccountException();// 没找到帐号
        }

        // 交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得人家的不好可以自定义实现
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user, // 用户信息
                user.getCertificateCode(), // 密码
                // ByteSource.Util.bytes(user.getCredentialsSalt()),
                getName());
        return authenticationInfo;
    }

    public void clearAllCachedAuthorizationInfo() {
        getAuthorizationCache().clear();
    }

    public void clearAllCachedAuthenticationInfo() {
        getAuthenticationCache().clear();
    }

    public void clearAllCache() {
        clearAllCachedAuthenticationInfo();
        clearAllCachedAuthorizationInfo();
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        return true;
    }
}
