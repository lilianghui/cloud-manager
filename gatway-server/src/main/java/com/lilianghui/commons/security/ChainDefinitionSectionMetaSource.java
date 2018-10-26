package com.lilianghui.commons.security;

import com.lilianghui.shiro.spring.starter.core.AbstractChainDefinitionSectionMetaSource;

import java.util.LinkedHashMap;

public class ChainDefinitionSectionMetaSource extends AbstractChainDefinitionSectionMetaSource {

    @Override
    public LinkedHashMap<String, String> loadAuth() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("/","anon");
        map.put("/login","anon");
        map.put("/resources/**","anon");
        map.put("/**","authc");
        return map;
    }
}
