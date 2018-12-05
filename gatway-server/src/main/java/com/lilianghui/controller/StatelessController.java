package com.lilianghui.controller;

import com.hazelcast.client.AuthenticationException;
import com.lilianghui.entity.GatWayConfig;
import com.lilianghui.entity.User;
import com.lilianghui.framework.core.jackson.JacksonUtils;
import com.lilianghui.service.ContractService;
import com.lilianghui.service.ShiroService;
import com.lilianghui.shiro.spring.starter.core.IncorrectCaptchaException;
import com.lilianghui.shiro.spring.starter.helper.Helper;
import com.lilianghui.shiro.spring.starter.interceptor.StatelessAuthcFilter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Controller
@RequestMapping("/stateless")
public class StatelessController {

    @Resource
    private ContractService contractService;
    @Resource
    private ShiroService shiroService;
    @Resource
    private CacheManager cacheManager;

    @Resource
    private GatWayConfig gatWayConfig;

    @RequestMapping("/")
    public ModelAndView index(Model model) {
        model.addAttribute("action","/stateless/login");
        return new ModelAndView("index");
    }

    /**
     * 用户登录
     *
     * @param user
     * @param attributes
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public ModelAndView login(HttpServletResponse response, HttpSession session, User user, RedirectAttributes attributes) {
        ModelAndView mv = new ModelAndView();
        try {
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
            if (subject.isAuthenticated()) {
                session.setAttribute("account", user);
                mv.setViewName("redirect:/stateless/success");
                Map<String, Object> map = new HashMap<>();
                map.put("id",user.getId());
                map.put("name",user.getCertificateCode());
                String jwt = Helper.getTokenManager().createJWT(user.getId(), JacksonUtils.writeValue(map));
                response.setHeader(StatelessAuthcFilter.AUTHORIZATION_HEADER, jwt);
                cacheManager.getCache(StatelessAuthcFilter.X_AUTH_TOKEN_CACHE_NAME).put(jwt,1);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            mv.setViewName("redirect:/stateless/");
        }
        return mv;
    }


    @RequestMapping("success")
    public ModelAndView success(Model model) {
        try {
            Object user = SecurityUtils.getSubject().getPrincipal();
            SecurityUtils.getSubject().hasRole("aaa");
            model.addAttribute("certificateCode", String.valueOf(user));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return new ModelAndView("success");
    }
}
