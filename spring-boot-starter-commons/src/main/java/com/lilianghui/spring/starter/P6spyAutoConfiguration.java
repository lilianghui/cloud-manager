package com.lilianghui.spring.starter;

import com.lilianghui.spring.starter.config.P6spyConfiguration;
import com.p6spy.engine.logging.P6LogOptions;
import com.p6spy.engine.spy.P6DataSource;
import com.p6spy.engine.spy.P6LoadableOptions;
import com.p6spy.engine.spy.P6ModuleManager;
import com.p6spy.engine.spy.P6SpyOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.AnnotatedTypeMetadata;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
@Slf4j
@Order
@Conditional(P6spyAutoConfiguration.P6spyCondition.class)
@EnableConfigurationProperties(P6spyConfiguration.class)
//@ConditionalOnProperty("spring.datasource.url")
//@AutoConfigureAfter({DataSourceAutoConfiguration.class})
//@ConditionalOnBean(DataSource.class)
public class P6spyAutoConfiguration implements InitializingBean {

    public static P6spyConfiguration P_6_SPY_CONFIGURATION;

    @Resource
    private P6spyConfiguration p6spyConfiguration;

    @Override
    public void afterPropertiesSet() throws Exception {
        P6ModuleManager p6ModuleManager = P6ModuleManager.getInstance();
        setP6LogOptions(p6ModuleManager);
        setP6SpyOptions(p6ModuleManager);
        P_6_SPY_CONFIGURATION = p6spyConfiguration;
    }


//    @Bean
//    public P6ModuleManager p6ModuleManager() {
//
//        return p6ModuleManager;
//    }

    private void setP6SpyOptions(P6ModuleManager p6ModuleManager) {
        P6SpyOptions p6SpyOptions = p6ModuleManager.getOptions(P6SpyOptions.class);
        p6SpyOptions.setAppend(p6spyConfiguration.isAppend());
        p6SpyOptions.setAppender(p6spyConfiguration.getAppender());
        p6SpyOptions.setAutoflush(p6spyConfiguration.isAutoFlush());
        p6SpyOptions.setCustomLogMessageFormat(p6spyConfiguration.getCustomLogMessageFormat());
        p6SpyOptions.setDatabaseDialectBooleanFormat(p6spyConfiguration.getDatabaseDialectBooleanFormat());
        p6SpyOptions.setDatabaseDialectDateFormat(p6spyConfiguration.getDatabaseDialectDateFormat());
        p6SpyOptions.setDateformat(p6spyConfiguration.getDateFormat());
        p6SpyOptions.setDriverlist(p6spyConfiguration.getDriverList());
        p6SpyOptions.setJmx(p6spyConfiguration.isJmx());
        p6SpyOptions.setJmxPrefix(p6spyConfiguration.getJmxPrefix());
        p6SpyOptions.setJNDIContextCustom(p6spyConfiguration.getJndiContextCustom());
        p6SpyOptions.setJNDIContextFactory(p6spyConfiguration.getJndiContextFactory());
        p6SpyOptions.setJNDIContextProviderURL(p6spyConfiguration.getJndiContextProviderUrl());
        p6SpyOptions.setLogfile(p6spyConfiguration.getLogFile());
        p6SpyOptions.setLogMessageFormat(p6spyConfiguration.getLogMessageFormat());
        p6SpyOptions.setModulelist(p6spyConfiguration.getModuleList());
        p6SpyOptions.setRealDataSource(p6spyConfiguration.getRealDataSource());
        p6SpyOptions.setRealDataSourceClass(p6spyConfiguration.getRealDataSourceClass());
        p6SpyOptions.setRealDataSourceProperties(p6spyConfiguration.getRealDataSourceProperties());
        p6SpyOptions.setStackTrace(p6spyConfiguration.isStackTrace());
        p6SpyOptions.setReloadProperties(p6spyConfiguration.isReloadProperties());
        p6SpyOptions.setReloadPropertiesInterval(p6spyConfiguration.getReloadPropertiesInterval());
        p6SpyOptions.setStackTraceClass(p6spyConfiguration.getStackTraceClass());
    }

    private void setP6LogOptions(P6ModuleManager p6ModuleManager) {
        P6LogOptions p6LogOptions = p6ModuleManager.getOptions(P6LogOptions.class);
        p6LogOptions.setExclude(p6spyConfiguration.getExclude());
        p6LogOptions.setExcludebinary(p6spyConfiguration.isExcludebinary());
        p6LogOptions.setExcludecategories(p6spyConfiguration.getExcludecategories());
        p6LogOptions.setExecutionThreshold(p6spyConfiguration.getExecutionThreshold());
        p6LogOptions.setFilter(p6spyConfiguration.isFilter());
        p6LogOptions.setSQLExpression(p6spyConfiguration.getSqlexpression());
        p6LogOptions.setInclude(p6spyConfiguration.getInclude());
    }


    static class P6spyCondition implements Condition {

        @Override
        public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
            return conditionContext.getEnvironment().getProperty("spring.datasource.url").toLowerCase().startsWith("jdbc:p6spy:");
        }
    }

}
