package com.lilianghui.application;

import com.lilianghui.application.config.ProjectGeneratorEx;
import io.spring.initializr.generator.ProjectGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringInitializerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringInitializerApplication.class, args);
    }

    @Bean
    public ProjectGenerator projectGenerator() {
        return new ProjectGeneratorEx();
    }
}
