package com.lilianghui.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class OpenReplicatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpenReplicatorApplication.class, args);
    }

}

