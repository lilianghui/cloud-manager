package com.lilianghui.spring.starter.core;

import com.google.code.or.OpenReplicator;
import com.google.code.or.binlog.BinlogEventListener;
import com.google.code.or.binlog.BinlogEventV4;
import com.lilianghui.spring.starter.entity.BinlogInfo;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

import javax.sql.DataSource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class OpenReplicatorEx extends OpenReplicator implements BinlogEventListener, InitializingBean, DisposableBean {

    @Setter
    private List<BinlogEventListener> binlogEventListeners;
    @Setter
    private boolean autoRestart;
    @Setter
    private TableInfoKeeper tableInfoKeeper;

    @Override
    public void stopQuietly(long timeout, TimeUnit unit) {
        super.stopQuietly(timeout, unit);
        if (autoRestart) {
            try {
                TimeUnit.SECONDS.sleep(10);
                log.error("Restart OpenReplicator");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onEvents(BinlogEventV4 binlogEventV4) {
        if (CollectionUtils.isNotEmpty(this.binlogEventListeners)) {
            this.binlogEventListeners.forEach(listener -> {
                try {
                    listener.onEvents(binlogEventV4);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            });
        }
    }

    @Override
    public void destroy() throws Exception {
        stop(1, TimeUnit.SECONDS);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        BinlogInfo binlogInfo = tableInfoKeeper.getBinlogInfo();
        setBinlogFileName(binlogInfo.getBinlogName());
        setBinlogPosition(binlogInfo.getPosition());
        setBinlogEventListener(this);
        start();
    }
}