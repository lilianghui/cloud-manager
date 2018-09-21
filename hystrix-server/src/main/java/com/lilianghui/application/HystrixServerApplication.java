package com.lilianghui.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.turbine.EnableTurbine;

@SpringBootApplication
@EnableTurbine
@EnableHystrixDashboard
public class HystrixServerApplication {

    //http://localhost:8085/turbine.stream
    public static void main(String[] args) {
        SpringApplication.run(HystrixServerApplication.class, args);
    }
}
