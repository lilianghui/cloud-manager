package com.lilianghui.spring.starter;

import com.google.code.or.OpenReplicator;
import com.google.code.or.binlog.BinlogEventListener;
import com.lilianghui.spring.starter.config.OpenReplicatorProperties;
import com.lilianghui.spring.starter.core.InstanceListener;
import com.lilianghui.spring.starter.core.OpenReplicatorEx;
import com.lilianghui.spring.starter.core.TableInfoKeeper;
import com.lilianghui.spring.starter.entity.CDCEvent;
import com.lilianghui.spring.starter.entity.CDCEventManager;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
@EnableConfigurationProperties({OpenReplicatorProperties.class})
@ConditionalOnProperty(prefix = OpenReplicatorProperties.PREFIX, value = {"host", "port"})
public class OpenReplicatorListenerAutoConfiguration {

    @Resource
    private OpenReplicatorProperties openReplicatorProperties;

    @Bean
    public TableInfoKeeper tableInfoKeeper() {
        DataSourceProperties dataSourceProperties = new DataSourceProperties();
        dataSourceProperties.setUrl(String.format("jdbc:mysql://%s:%s/", openReplicatorProperties.getHost(), openReplicatorProperties.getPort()));
        dataSourceProperties.setUsername(openReplicatorProperties.getUsername());
        dataSourceProperties.setPassword(openReplicatorProperties.getPassword());
        dataSourceProperties.setDriverClassName("com.mysql.jdbc.Driver");
        dataSourceProperties.setType(HikariDataSource.class);
        DataSource dataSource = dataSourceProperties.initializeDataSourceBuilder().build();
        return new TableInfoKeeper(dataSource);
    }


    @Bean
    public BinlogEventListener binlogEventListener() {
        InstanceListener binlogEventListener = new InstanceListener(tableInfoKeeper());
        return binlogEventListener;
    }

}
