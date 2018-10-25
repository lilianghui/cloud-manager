package com.lilianghui.shiro.spring.starter.core;

import com.google.inject.internal.util.$Maps;
import org.apache.commons.collections.MapUtils;

import java.util.LinkedHashMap;

public abstract class AbstractChainDefinitionSectionMetaSource {

    public static final String PREMISSION_STRING = "authc,orPerms[{0}]"; // 资源结构格式
    public static final String ROLE_STRING = "authc,orRole[{0}]"; // 角色结构格式


    public final LinkedHashMap<String, String> loadAllAuth(LinkedHashMap<String, String> filterChainDefinitions) throws Exception {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        if (MapUtils.isNotEmpty(filterChainDefinitions)) {
            map.putAll(filterChainDefinitions);
        }
        LinkedHashMap<String, String> authc = loadAuth();
        if (MapUtils.isNotEmpty(authc)) {
            map.putAll(authc);
        }
        return map;
    }


    public abstract LinkedHashMap<String, String> loadAuth();

    public static class DefaultChainDefinitionSectionMetaSource extends AbstractChainDefinitionSectionMetaSource {

        @Override
        public LinkedHashMap<String, String> loadAuth() {
            return $Maps.newLinkedHashMap();
        }
    }
}
