package com.lilianghui.spring.starter.config;


import lombok.Data;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import tk.mybatis.mapper.entity.Config;
import tk.mybatis.spring.mapper.MapperFactoryBean;

import java.lang.annotation.Annotation;
import java.util.LinkedHashMap;
import java.util.Map;


@Data
@ConfigurationProperties(prefix = MultiDataSourceProperties.PREFIX)
public class MultiDataSourceProperties {
    public static final String PREFIX = "spring.atomikos";

    private Map<String, AtomikosDataSourceProperties> datasource = new LinkedHashMap<>();

    @Data
    public static class AtomikosDataSourceProperties extends DataSourceProperties {

        private MybatisProperties mybatis = new MybatisProperties();
        private MapperProperties mapper = new MapperProperties();

    }

    @Data
    public static class MapperProperties extends Config {
        private String mapperHelperRef;
        private String[] properties;
        private String[] value = new String[0];
        private String[] basePackages = new String[0];
        private Class<?>[] basePackageClasses = new Class[0];
        private Class<? extends BeanNameGenerator> nameGenerator = BeanNameGenerator.class;
        private Class<? extends Annotation> annotationClass = Annotation.class;
        private Class<?> markerInterface = Class.class;
        private Class<? extends MapperFactoryBean> factoryBean = MapperFactoryBean.class;
    }
}
