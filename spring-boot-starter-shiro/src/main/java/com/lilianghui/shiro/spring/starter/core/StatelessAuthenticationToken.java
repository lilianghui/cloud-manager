package com.lilianghui.shiro.spring.starter.core;

import io.jsonwebtoken.Claims;
import lombok.Getter;
import org.apache.shiro.authc.HostAuthenticationToken;


public class StatelessAuthenticationToken implements HostAuthenticationToken {

    private String host;
    @Getter
    private String token;
    @Getter
    private Claims claims;

    public StatelessAuthenticationToken(String token, Claims claims) {
        this(token, claims, null);
    }

    public StatelessAuthenticationToken(String token, Claims claims, String host) {
        this.token = token;
        this.claims = claims;
        this.host = host;
    }


    @Override
    public String getHost() {
        return this.host;
    }

    @Override
    public Object getPrincipal() {
        return claims.getAudience();
    }

    @Override
    public Object getCredentials() {
        return null;
    }
}
