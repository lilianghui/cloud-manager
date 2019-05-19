package com.lilianghui.spring.starter;

import com.lilianghui.spring.starter.gray.rule.DiscoveryEnabledRule;
import com.lilianghui.spring.starter.gray.rule.MetadataAwareRule;
import com.netflix.niws.loadbalancer.DiscoveryEnabledNIWSServerList;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.netflix.ribbon.RibbonClientConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@ConditionalOnClass({DiscoveryEnabledNIWSServerList.class})
@AutoConfigureBefore({RibbonClientConfiguration.class})
@ConditionalOnProperty(
    value = {"ribbon.filter.metadata.enabled"},
    matchIfMissing = true
)
public class RibbonDiscoveryRuleAutoConfiguration {
    public RibbonDiscoveryRuleAutoConfiguration() {
    }

    @Bean
    @ConditionalOnMissingBean
    @Scope("prototype")
    public DiscoveryEnabledRule metadataAwareRule() {
        return new MetadataAwareRule();
    }
}
