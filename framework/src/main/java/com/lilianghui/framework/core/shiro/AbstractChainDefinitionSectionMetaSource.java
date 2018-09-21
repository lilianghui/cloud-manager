package com.lilianghui.framework.core.shiro;

import org.apache.commons.collections.MapUtils;
import org.apache.shiro.config.Ini;
import org.apache.shiro.config.Ini.Section;
import org.springframework.beans.factory.FactoryBean;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AbstractChainDefinitionSectionMetaSource implements FactoryBean<Map<String, String>> {

    public static final String PREMISSION_STRING = "authc,orPerms[{0}]"; // 资源结构格式
    public static final String ROLE_STRING = "authc,orRole[{0}]"; // 角色结构格式

    private String filterChainDefinitions;

    @Override
    public Map<String, String> getObject() throws Exception {
        Map<String, String> map = new LinkedHashMap<>();
        Ini ini = new Ini();
        ini.load(filterChainDefinitions);
        Section section = ini.getSection(Ini.DEFAULT_SECTION_NAME);
        if (section != null) {
            map.putAll(section);
        }
        Map<String, String> authc = loadAuth();
        if (MapUtils.isNotEmpty(authc)) {
            map.putAll(authc);
        }
        return map;
    }

    @Override
    public Class<?> getObjectType() {
        return Map.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setFilterChainDefinitions(String filterChainDefinitions) {
        this.filterChainDefinitions = filterChainDefinitions;
    }

    public abstract Map<String, String> loadAuth();
}
