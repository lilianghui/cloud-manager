package com.lilianghui.spring.starter;


import com.google.common.collect.Sets;
import com.lilianghui.spring.starter.config.P6spyConfiguration;
import com.lilianghui.spring.starter.config.TypeHandlerConfiguration;
import com.lilianghui.spring.starter.utils.ScanPackage;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.annotation.Resource;

@Configuration
@Slf4j
@Order
@EnableConfigurationProperties(TypeHandlerConfiguration.class)
@ConditionalOnProperty(name = "mybatis.typeHandlerPackage")
public class TypeHandlerAutoConfiguration implements InitializingBean {

    @Resource
    private SqlSessionFactory sqlSessionFactory;
    @Resource
    private TypeHandlerConfiguration typeHandlerConfiguration;

    @Override
    public void afterPropertiesSet() throws Exception {
        ScanPackage.findPackageClasses(typeHandlerConfiguration.getTypeHandlerPackage(), Sets.newHashSet(MappedTypes.class, MappedJdbcTypes.class))
                .forEach(sqlSessionFactory.getConfiguration().getTypeHandlerRegistry()::register);
    }
}
