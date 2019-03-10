package com.lilianghui.controller;

import com.google.common.collect.Maps;
import com.lilianghui.entity.User;
import com.lilianghui.service.ShiroService;
import lombok.extern.slf4j.Slf4j;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("oauth2")
public class Oauth2Controller {

    @Resource
    private ShiroService shiroService;

    private Map<String, Map<String, Object>> objectMap = Maps.newConcurrentMap();

    public Oauth2Controller() {
        String clientId = "fbed1d1b4b1449daa4bc49397cbe2350";
        objectMap.put(clientId, Maps.newHashMap());
        Map<String, Object> map = objectMap.get(clientId);
        map.put("clientId", "fbed1d1b4b1449daa4bc49397cbe2350");
        map.put("secret", "fbed1d1b4b1449daa4bc49397cbe2350");
    }

    //    获得授权码
//    http://localhost:8080/oauth2/authorize?client_id=fbed1d1b4b1449daa4bc49397cbe2350&response_type=code&redirect_uri=http://localhost:8080/oauth_callback
    @RequestMapping("authorize")
    public ModelAndView authorize(Model model, HttpServletRequest request) throws Exception {

        OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);
        if (!validClientId(oauthRequest.getClientId())) {//clientId验证失败未向平台申请
            return new ModelAndView("error");
        }
        Subject subject = SecurityUtils.getSubject();
        // 如果用户登录跳转确认页面  没有登录，跳转到登陆页面
        model.addAttribute("client_id", oauthRequest.getClientId());
        model.addAttribute("response_type", oauthRequest.getResponseType());
        model.addAttribute("redirect_uri", oauthRequest.getRedirectURI());
        model.addAttribute("action", "/oauth2/login");
        if (subject.isAuthenticated()) {
            return new ModelAndView("oauth2ok");
        } else {
            return new ModelAndView("index");
        }
    }


    @RequestMapping("login")
    public Object login(User user, HttpServletRequest request) throws Exception {
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            boolean login = shiroService.login(user);
            if (!login) {
                return "error";
            }
        }
        OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);
        String username = (String) subject.getPrincipal();
        // 生成授权码
        String authorizationCode = null;
        // responseType目前仅支持CODE，另外还有TOKEN
        String responseType = oauthRequest.getResponseType();
        if (responseType.equals(ResponseType.CODE.toString())) {
            OAuthIssuerImpl oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
            authorizationCode = oauthIssuerImpl.authorizationCode();
//            oAuthService.addAuthCode(authorizationCode, username);
            objectMap.get(oauthRequest.getClientId()).put("code", authorizationCode);
        }
        // 进行OAuth响应构建
        OAuthASResponse.OAuthAuthorizationResponseBuilder builder = OAuthASResponse.authorizationResponse(request,
                HttpServletResponse.SC_FOUND);
        // 设置授权码
        builder.setCode(authorizationCode);
        System.out.println("当前用户：" + username + "，请求授权码为：" + authorizationCode);
        // 得到到客户端重定向地址
        String redirectURI = oauthRequest.getParam(OAuth.OAUTH_REDIRECT_URI);

        // 构建响应
        final OAuthResponse response = builder.location(redirectURI).buildQueryMessage();

        // 根据OAuthResponse返回ResponseEntity响应
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(new URI(response.getLocationUri()));
        return new ResponseEntity(headers, HttpStatus.valueOf(response.getResponseStatus()));
    }

    @ResponseBody
    @RequestMapping("api/user")
    public Object user(HttpServletRequest request, String access_token) throws Exception {
        User user = new User();
        user.setId("1");
        return shiroService.selectByPrimaryKey(user);
    }

    @RequestMapping("token")
    public Object token(HttpServletRequest request) throws Exception {
        OAuthTokenRequest oAuthTokenRequest = new OAuthTokenRequest(request);
        String clientId = oAuthTokenRequest.getClientId();
        String clientKey = oAuthTokenRequest.getClientSecret();

        if (!checkClient(clientId, clientKey)) {
            OAuthResponse response = OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                    .setError(OAuthError.TokenResponse.INVALID_CLIENT)
                    .setErrorDescription("密码错误")
                    .buildJSONMessage();
            return new ResponseEntity(response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
        }
        OAuthResponse oAuthResponse;

        String authcode = oAuthTokenRequest.getCode();
        String grantType = oAuthTokenRequest.getGrantType();
        if (GrantType.AUTHORIZATION_CODE.toString().equals(grantType) && authcode.equals(objectMap.get(clientId).get("code"))) {
            //生成token
            OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
            final String accessToken = oauthIssuerImpl.accessToken();
            objectMap.get(clientId).put("token", accessToken);


            //生成OAuth响应
            oAuthResponse = OAuthASResponse
                    .tokenResponse(HttpServletResponse.SC_OK)
                    .setAccessToken(accessToken)
                    .setExpiresIn(String.valueOf(3600L))
                    .setParam("userId", "1111111111111")
                    .buildJSONMessage();

            //根据OAuthResponse生成ResponseEntity
            return new ResponseEntity(oAuthResponse.getBody(), HttpStatus.valueOf(oAuthResponse.getResponseStatus()));

        }
        OAuthResponse response = OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                .setError(OAuthError.TokenResponse.INVALID_CLIENT)
                .setErrorDescription("校验失败")
                .buildJSONMessage();
        return new ResponseEntity(response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
    }


    private boolean checkClient(String clientId, String clientKey) {
        return objectMap.get(clientId).get("secret").equals(clientKey);
    }


    private boolean validClientId(String clientId) {
        return objectMap.containsKey(clientId);
    }
}
