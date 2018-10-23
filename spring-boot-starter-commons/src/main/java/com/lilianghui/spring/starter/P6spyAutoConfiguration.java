package com.lilianghui.spring.starter;

import com.lilianghui.spring.starter.config.P6spyConfiguration;
import com.p6spy.engine.spy.P6DataSource;
import com.p6spy.engine.spy.P6ModuleManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.AnnotatedTypeMetadata;

import javax.sql.DataSource;

@Configuration
@Slf4j
@Conditional(P6spyAutoConfiguration.P6spyCondition.class)
@EnableConfigurationProperties(P6spyConfiguration.class)
@ConditionalOnBean(DataSource.class)
@ConditionalOnProperty("spring.datasource.url")
public class P6spyAutoConfiguration {

    @Bean
    public DataSource dataSource(@Autowired DataSource dataSource) {
        P6ModuleManager.getInstance();
        return new P6DataSource(dataSource);
    }

    class P6spyCondition implements Condition {

        @Override
        public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
            return conditionContext.getEnvironment().getProperty("spring.datasource.url").toLowerCase().startsWith("jdbc:p6spy:");
        }
    }

}
