package com.lilianghui.application.task;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.elasticjob.lite.annotation.ElasticSimpleJob;
import org.springframework.stereotype.Component;

@Component
@ElasticSimpleJob(cron = "0/5 * * * * ?", jobName = "jobTask", dataSource = "dataSource",
        shardingTotalCount = 2, jobParameter = "测试参数", shardingItemParameters = "0=Chengdu0,1=Chengdu1")
public class StockSimpleJob implements SimpleJob {
    @Override
    public void execute(ShardingContext shardingContext) {
        switch (shardingContext.getShardingItem()) {
            case 0:
                System.out.println(String.format("------Thread ID: %s, 任务总片数: %s, " +
                                "当前分片项: %s.当前参数: %s," +
                                "当前任务名称: %s.当前任务参数: %s"
                        ,
                        Thread.currentThread().getId(),
                        shardingContext.getShardingTotalCount(),
                        shardingContext.getShardingItem(),
                        shardingContext.getShardingParameter(),
                        shardingContext.getJobName(),
                        shardingContext.getJobParameter()

                        )
                );
                break;
            case 1:
                System.out.println("啦啦啦");
                break;
            default:
                break;
        }
    }
}