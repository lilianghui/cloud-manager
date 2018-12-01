package com.lilianghui.application;

import com.dangdang.elasticjob.lite.autoconfigure.ElasticJobAutoConfiguration;
import com.lilianghui.application.config.ElasticJobListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@SpringBootApplication(exclude = ElasticJobAutoConfiguration.class)
@Import(DataSourceAutoConfiguration.class)
public class ElasticJobServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElasticJobServerApplication.class, args);
    }

    /**
     * 设置活动监听，前提是已经设置好了监听，见下一个目录
     *
     * @return
     */
    @Bean
    public ElasticJobListener elasticJobListener() {
        return new ElasticJobListener(100, 100);
    }
}
