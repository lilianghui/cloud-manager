package com.lilianghui.application;

import com.dangdang.elasticjob.lite.autoconfigure.ElasticJobAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;

@SpringBootApplication(exclude = ElasticJobAutoConfiguration.class)
@Import(DataSourceAutoConfiguration.class)
public class ElasticJobServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElasticJobServerApplication.class, args);
    }
}
