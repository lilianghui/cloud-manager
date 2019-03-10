package com.lilianghui.service;

import com.hazelcast.client.AuthenticationException;
import com.lilianghui.client.ShiroFeignClient;
import com.lilianghui.entity.User;
import com.lilianghui.shiro.spring.starter.core.IncorrectCaptchaException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ShiroService {

    @Resource
    private ShiroFeignClient shiroFeignClient;

    public User selectByPrimaryKey(User user) {
        return shiroFeignClient.selectByPrimaryKey(user);
    }

    public boolean login(User user) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getId(), user.getCertificateCode());
        token.setRememberMe(true);
        try {
            subject.login(token);
        } catch (IncorrectCaptchaException e) {
            throw e;
        } catch (UnknownAccountException e) {
            throw e;
        } catch (IncorrectCredentialsException e) {
            throw e;
        } catch (LockedAccountException e) {
            throw e;
        } catch (ExcessiveAttemptsException e) {
            throw e;
        } catch (AuthenticationException e) {
            throw e;
        }
        return subject.isAuthenticated();
    }
}
