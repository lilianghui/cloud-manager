package com.lilianghui.commons.security;

import com.lilianghui.shiro.spring.starter.core.AbstractChainDefinitionSectionMetaSource;

import java.util.LinkedHashMap;

public class ChainDefinitionSectionMetaSource extends AbstractChainDefinitionSectionMetaSource {

    @Override
    public LinkedHashMap<String, String> loadAuth() {
        return null;
    }
}
