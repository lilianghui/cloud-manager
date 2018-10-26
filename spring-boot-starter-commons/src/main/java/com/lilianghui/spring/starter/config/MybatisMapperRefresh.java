package com.lilianghui.spring.starter.config;


import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.*;

@Slf4j
public class MybatisMapperRefresh implements Runnable {


    private WatchService watchService;

    public MybatisMapperRefresh(String path) throws Exception {
        this.watchService = FileSystems.getDefault().newWatchService();
        Paths.get(path).register(watchService
                , StandardWatchEventKinds.ENTRY_MODIFY);
    }


    @Override
    public void run() {
        while (true) {
            try {
                // 获取下一个文件改动事件
                WatchKey key = watchService.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    System.out.println(event.context() + " --> " + event.kind());
                }
                // 重设WatchKey
                boolean valid = key.reset();
                // 如果重设失败，退出监听
                if (!valid) {
                    break;
                }

            } catch (InterruptedException e) {
                log.info("mybatis mapper xml reload thread interrupte {}", e.getMessage());
                break;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }

        }
    }
}
