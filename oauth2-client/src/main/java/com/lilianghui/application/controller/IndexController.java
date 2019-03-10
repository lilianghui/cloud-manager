package com.lilianghui.application.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthAuthzResponse;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Slf4j
@Controller
public class IndexController {

    private String CLIENT_ID = "fbed1d1b4b1449daa4bc49397cbe2350";
    private String CLIENT_SECRET = "fbed1d1b4b1449daa4bc49397cbe2350";
    private String REDIRECT_URI = "http://127.0.0.1:8888/code";
    private String RESPONSE_TYPE = "code";
    private String AUTHORIZATION_LOCATION = "http://127.0.0.1:8089/oauth2/authorize";
    private String OAUTH_CLIENT_ACCESS_TOKEN = "http://127.0.0.1:8089/oauth2/token";
    private String OAUTH_CLIENT_GET_RESOURCE = "http://127.0.0.1:8089/oauth2/api/user";

    @RequestMapping("/")
    public ModelAndView index(Model model) {
        String format = "%s?client_id=%s&redirect_uri=%s&response_type=%s";
        model.addAttribute("action", String.format(format, AUTHORIZATION_LOCATION, CLIENT_ID, REDIRECT_URI, RESPONSE_TYPE));
        return new ModelAndView("index");
    }
 /*
        response_type：表示授权类型，必选项，此处的值固定为"code"
        client_id：表示客户端的ID，必选项
        redirect_uri：表示重定向URI，可选项
        scope：表示申请的权限范围，可选项
        state：表示客户端的当前状态，可以指定任意值，认证服务器会原封不动地返回这个值
    */

    /**
     * 获得授权码
     *
     * @return
     */
    @RequestMapping("client")
    public ModelAndView client() {
        try {
            OAuthClientRequest oauthResponse = OAuthClientRequest
                    .authorizationLocation(AUTHORIZATION_LOCATION)
                    .setResponseType(OAuth.OAUTH_CODE)
                    .setClientId(CLIENT_ID)
                    .setRedirectURI(REDIRECT_URI)
//                    .setState(String.valueOf(System.currentTimeMillis()))
//                    .setScope(ConstantKey.OAUTH_CLIENT_SCOPE)
                    .buildQueryMessage();
            return new ModelAndView(new RedirectView(oauthResponse.getLocationUri()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView("index");
    }

    /*
        grant_type：表示使用的授权模式，必选项，此处的值固定为"authorization_code"
        code：表示上一步获得的授权码，必选项。
        redirect_uri：表示重定向URI，必选项，且必须与A步骤中的该参数值保持一致
        client_id：表示客户端ID，必选项
    */

    /**
     * 获得令牌
     *
     * @return oauth_callback?code=1234
     */
    @RequestMapping("code")
    public String code(HttpServletRequest request) throws Exception {
        OAuthAuthzResponse oauthAuthzResponse = OAuthAuthzResponse.oauthCodeAuthzResponse(request);
        String code = oauthAuthzResponse.getCode();
        OAuthClientRequest oauthClientRequest = OAuthClientRequest
                .tokenLocation(OAUTH_CLIENT_ACCESS_TOKEN)
                .setGrantType(GrantType.AUTHORIZATION_CODE)
                .setClientId(CLIENT_ID)
                .setClientSecret(CLIENT_SECRET)
                .setRedirectURI(REDIRECT_URI)
                .setCode(code)
                .buildQueryMessage();
        OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());

        //Facebook is not fully compatible with OAuth 2.0 draft 10, access token response is
        //application/x-www-form-urlencoded, not json encoded so we use dedicated response class for that
        //Custom response classes are an easy way to deal with oauth providers that introduce modifications to
        //OAuth 2.0 specification

        //获取access token
        OAuthJSONAccessTokenResponse oAuthResponse = oAuthClient.accessToken(oauthClientRequest, OAuth.HttpMethod.POST);
        String accessToken = oAuthResponse.getAccessToken();
        String refreshToken = oAuthResponse.getRefreshToken();
        Long expiresIn = oAuthResponse.getExpiresIn();
        //获得资源服务
        OAuthClientRequest bearerClientRequest = new OAuthBearerClientRequest(OAUTH_CLIENT_GET_RESOURCE)
                .setAccessToken(accessToken).buildQueryMessage();
        OAuthResourceResponse resourceResponse = oAuthClient.resource(bearerClientRequest, OAuth.HttpMethod.GET, OAuthResourceResponse.class);
        String resBody = resourceResponse.getBody();
        log.info("accessToken: " + accessToken + " refreshToken: " + refreshToken + " expiresIn: " + expiresIn + " resBody: " + resBody);
//        model.addAttribute("accessToken", "accessToken: " + accessToken + " resBody: " + resBody);
        return "success";
    }

}
