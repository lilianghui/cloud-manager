package com.lilianghui.application;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDubboConfiguration
@EnableDistributedTransaction
@MapperScan("com.lilianghui.application.mapper")
public class DubboProducerLcnApplication {

    public static void main(String[] args) {
        SpringApplication.run(DubboProducerLcnApplication.class, args);
    }

}
