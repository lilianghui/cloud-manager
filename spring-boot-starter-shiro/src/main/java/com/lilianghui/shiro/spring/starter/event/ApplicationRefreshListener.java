package com.lilianghui.shiro.spring.starter.event;

import com.lilianghui.shiro.spring.starter.ShiroTemplate;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.ApplicationListener;

import javax.annotation.Resource;


public class ApplicationRefreshListener implements ApplicationListener<RefreshScopeRefreshedEvent> {

    @Resource
    private ShiroTemplate shiroTemplate;

    @Override
    public void onApplicationEvent(RefreshScopeRefreshedEvent scopeRefreshedEvent) {
        shiroTemplate.refreshChainDefinitions();
    }
}
