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
@ConfigurationProperties(prefix = OpenReplicatorProperties.PREFIX)
public class OpenReplicatorProperties {
    public static final String PREFIX = "spring.open-replicator";

    private String host = "127.0.0.1";
    private int port = 3306;
    private String username = "root";
    private String password = "123456";
    private boolean autoRestart = true;
}
