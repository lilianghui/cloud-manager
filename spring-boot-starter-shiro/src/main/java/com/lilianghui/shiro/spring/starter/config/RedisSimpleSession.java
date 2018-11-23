package com.lilianghui.shiro.spring.starter.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.shiro.session.mgt.SimpleSession;

import java.util.Map;

@JsonIgnoreProperties({ "attributeKeys","attributesLazy" })
public class RedisSimpleSession extends SimpleSession {

    public RedisSimpleSession() {
    }

    public RedisSimpleSession(String host) {
        super(host);
    }

    @Override
    public Map<Object, Object> getAttributes() {
        return super.getAttributes();
    }
}
