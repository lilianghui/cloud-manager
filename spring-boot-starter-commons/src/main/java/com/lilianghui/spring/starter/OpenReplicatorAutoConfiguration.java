package com.lilianghui.spring.starter;

import com.google.code.or.OpenReplicator;
import com.google.code.or.binlog.BinlogEventListener;
import com.lilianghui.spring.starter.config.OpenReplicatorProperties;
import com.lilianghui.spring.starter.core.InstanceListener;
import com.lilianghui.spring.starter.core.OpenReplicatorEx;
import com.lilianghui.spring.starter.core.RowEvent;
import com.lilianghui.spring.starter.core.TableInfoKeeper;
import com.lilianghui.spring.starter.entity.CDCEvent;
import com.lilianghui.spring.starter.entity.CDCEventManager;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
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
public class OpenReplicatorAutoConfiguration implements InitializingBean, DisposableBean {

    @Resource
    private OpenReplicatorProperties openReplicatorProperties;

    private List<BinlogEventListener> binlogEventListeners;
    private List<RowEvent> rowEvents;
    private Thread thread;

    public OpenReplicatorAutoConfiguration(ObjectProvider<List<BinlogEventListener>> binlogEventProvider,
                                           ObjectProvider<List<RowEvent>> rowEventProvider) {
        this.binlogEventListeners = binlogEventProvider.getIfAvailable();
        this.rowEvents = rowEventProvider.getIfAvailable();
    }

    @Bean
    public OpenReplicator openReplicator(@Autowired @Qualifier("tableInfoKeeper") TableInfoKeeper tableInfoKeeper) throws Exception {
        OpenReplicatorEx openReplicatorEx = new OpenReplicatorEx();
        openReplicatorEx.setHost(openReplicatorProperties.getHost());
        openReplicatorEx.setPort(openReplicatorProperties.getPort());
        openReplicatorEx.setUser(openReplicatorProperties.getUsername());
        openReplicatorEx.setPassword(openReplicatorProperties.getPassword());
        openReplicatorEx.setAutoRestart(openReplicatorProperties.isAutoRestart());
        openReplicatorEx.setBinlogEventListeners(this.binlogEventListeners);
        openReplicatorEx.setTableInfoKeeper(tableInfoKeeper);
        return openReplicatorEx;
    }


    @Override
    public void destroy() throws Exception {
        thread.interrupt();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        thread = new Thread(new PrintCDCEvent());
        thread.start();
    }

    class PrintCDCEvent implements Runnable {
        @Override
        public void run() {
            while (true) {
                if (CDCEventManager.queue.isEmpty() == false) {
                    CDCEvent ce = CDCEventManager.queue.pollFirst();
                    if (CollectionUtils.isNotEmpty(rowEvents)) {
                        rowEvents.forEach(rowEvent -> {
                            try {
                                rowEvent.onEvents(ce);
                            } catch (Exception e) {
                                log.error(e.getMessage(), e);
                            }
                        });
                    }
                } else {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
