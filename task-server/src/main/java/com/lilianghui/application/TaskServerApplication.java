package com.lilianghui.application;

import com.lilianghui.application.task.HelloWorldCommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.context.annotation.Bean;

@EnableTask
@SpringBootApplication
public class TaskServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskServerApplication.class, args);
    }

    @Bean
    public HelloWorldCommandLineRunner helloWorldCommandLineRunner() {
        return new HelloWorldCommandLineRunner();
    }
}
