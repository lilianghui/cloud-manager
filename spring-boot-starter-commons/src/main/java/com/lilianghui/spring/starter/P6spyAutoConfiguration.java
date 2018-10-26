package com.lilianghui.spring.starter;

import com.lilianghui.spring.starter.config.P6spyProperties;
import com.p6spy.engine.logging.P6LogOptions;
import com.p6spy.engine.spy.P6ModuleManager;
import com.p6spy.engine.spy.P6SpyOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.AnnotatedTypeMetadata;

import javax.annotation.Resource;

@Configuration
@Slf4j
@Order
@Conditional(P6spyAutoConfiguration.P6spyCondition.class)
@EnableConfigurationProperties(P6spyProperties.class)
//@ConditionalOnProperty("spring.datasource.url")
//@AutoConfigureAfter({DataSourceAutoConfiguration.class})
//@ConditionalOnBean(DataSource.class)
public class P6spyAutoConfiguration implements InitializingBean {

    public static P6spyProperties P_6_SPY_PROPERTIES;

    @Resource
    private P6spyProperties p6spyProperties;

    @Override
    public void afterPropertiesSet() throws Exception {
        P6ModuleManager p6ModuleManager = P6ModuleManager.getInstance();
        setP6LogOptions(p6ModuleManager);
        setP6SpyOptions(p6ModuleManager);
        P_6_SPY_PROPERTIES = p6spyProperties;
    }


//    @Bean
//    public P6ModuleManager p6ModuleManager() {
//
//        return p6ModuleManager;
//    }

    private void setP6SpyOptions(P6ModuleManager p6ModuleManager) {
        P6SpyOptions p6SpyOptions = p6ModuleManager.getOptions(P6SpyOptions.class);
        p6SpyOptions.setAppend(p6spyProperties.isAppend());
        p6SpyOptions.setAppender(p6spyProperties.getAppender());
        p6SpyOptions.setAutoflush(p6spyProperties.isAutoFlush());
        p6SpyOptions.setCustomLogMessageFormat(p6spyProperties.getCustomLogMessageFormat());
        p6SpyOptions.setDatabaseDialectBooleanFormat(p6spyProperties.getDatabaseDialectBooleanFormat());
        p6SpyOptions.setDatabaseDialectDateFormat(p6spyProperties.getDatabaseDialectDateFormat());
        p6SpyOptions.setDateformat(p6spyProperties.getDateFormat());
        p6SpyOptions.setDriverlist(p6spyProperties.getDriverList());
        p6SpyOptions.setJmx(p6spyProperties.isJmx());
        p6SpyOptions.setJmxPrefix(p6spyProperties.getJmxPrefix());
        p6SpyOptions.setJNDIContextCustom(p6spyProperties.getJndiContextCustom());
        p6SpyOptions.setJNDIContextFactory(p6spyProperties.getJndiContextFactory());
        p6SpyOptions.setJNDIContextProviderURL(p6spyProperties.getJndiContextProviderUrl());
        p6SpyOptions.setLogfile(p6spyProperties.getLogFile());
        p6SpyOptions.setLogMessageFormat(p6spyProperties.getLogMessageFormat());
        p6SpyOptions.setModulelist(p6spyProperties.getModuleList());
        p6SpyOptions.setRealDataSource(p6spyProperties.getRealDataSource());
        p6SpyOptions.setRealDataSourceClass(p6spyProperties.getRealDataSourceClass());
        p6SpyOptions.setRealDataSourceProperties(p6spyProperties.getRealDataSourceProperties());
        p6SpyOptions.setStackTrace(p6spyProperties.isStackTrace());
        p6SpyOptions.setReloadProperties(p6spyProperties.isReloadProperties());
        p6SpyOptions.setReloadPropertiesInterval(p6spyProperties.getReloadPropertiesInterval());
        p6SpyOptions.setStackTraceClass(p6spyProperties.getStackTraceClass());
    }

    private void setP6LogOptions(P6ModuleManager p6ModuleManager) {
        P6LogOptions p6LogOptions = p6ModuleManager.getOptions(P6LogOptions.class);
        p6LogOptions.setExclude(p6spyProperties.getExclude());
        p6LogOptions.setExcludebinary(p6spyProperties.isExcludebinary());
        p6LogOptions.setExcludecategories(p6spyProperties.getExcludecategories());
        p6LogOptions.setExecutionThreshold(p6spyProperties.getExecutionThreshold());
        p6LogOptions.setFilter(p6spyProperties.isFilter());
        p6LogOptions.setSQLExpression(p6spyProperties.getSqlexpression());
        p6LogOptions.setInclude(p6spyProperties.getInclude());
    }


    static class P6spyCondition implements Condition {

        @Override
        public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
            return conditionContext.getEnvironment().getProperty("spring.datasource.url").toLowerCase().startsWith("jdbc:p6spy:");
        }
    }

}
