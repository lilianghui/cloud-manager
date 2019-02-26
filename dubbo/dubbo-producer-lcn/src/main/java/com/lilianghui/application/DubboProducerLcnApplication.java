package com.lilianghui.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
//@EnableDubboConfiguration
@MapperScan("com.lilianghui.application.mapper")
public class DubboProducerLcnApplication {

    public static void main(String[] args) {
        SpringApplication.run(DubboProducerLcnApplication.class, args);
    }

}
