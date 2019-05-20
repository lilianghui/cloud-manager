package com.lilianghui;

import brave.sampler.Sampler;
import com.lilianghui.spring.starter.feign.DefaultFeignConfiguration;
import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

@EnableTransactionManagement
@EnableHystrix
@EnableHystrixDashboard
@MapperScan("com.lilianghui.mapper")
@SpringBootApplication
@EnableFeignClients(value = "com.lilianghui.client", defaultConfiguration = DefaultFeignConfiguration.class)
public class ShiroServerApplication extends SpringBootServletInitializer {

    //http://localhost:8084/hystrix
    //http://localhost:8084/actuator/hystrix.stream
    public static void main(String[] args) {
        SpringApplication.run(ShiroServerApplication.class, args);
    }


    // Java EE应用服务器配置，
    // 如果要使用tomcat来加载jsp的话就必须继承SpringBootServletInitializer类并且重写其中configure方法
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ShiroServerApplication.class);
    }

    @Bean
    public ServletRegistrationBean hystrix() {
        HystrixMetricsStreamServlet streamServlet = new HystrixMetricsStreamServlet();
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(streamServlet);
        registrationBean.setLoadOnStartup(1);
        registrationBean.addUrlMappings("/actuator/hystrix.stream");
        registrationBean.setName("HystrixMetricsStreamServlet");
        return registrationBean;
    }

    @Bean
    public Sampler sleuthTraceSampler() {
        return Sampler.ALWAYS_SAMPLE;
    }
}
