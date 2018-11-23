package com.lilianghui.application.controller;

import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.lilianghui.application.task.TestJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class TestController {

    @Autowired
    private ZookeeperRegistryCenter zookeeperRegistryCenter;


    /**
     * 动态添加任务逻辑
     */
    @GetMapping("/test")
    public void test(@RequestParam("cron") String cron) {
        int shardingTotalCount = 2;
        String jobName = UUID.randomUUID().toString() + "-test123";

        JobCoreConfiguration jobCoreConfiguration = JobCoreConfiguration
                .newBuilder(jobName, cron, shardingTotalCount)
                .shardingItemParameters("0=A,1=B")
                .build();

        SimpleJobConfiguration simpleJobConfiguration =
                new SimpleJobConfiguration(jobCoreConfiguration, TestJob.class.getName());
        JobScheduler jobScheduler = new JobScheduler(zookeeperRegistryCenter, LiteJobConfiguration.newBuilder(simpleJobConfiguration).build());


        try {
            jobScheduler.init();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("定时任务创建失败");
        }
    }
}