package com.lilianghui;

import com.lilianghui.config.filter.AuthHeaderFilter;
import com.lilianghui.config.filter.ResponseFilter;
import com.lilianghui.framework.core.lock.redisson.RedissonAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableZuulProxy
@MapperScan("com.lilianghui.mapper")
@Import(RedissonAutoConfiguration.class)
//@EnableEurekaClient
public class GatwayServerApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(GatwayServerApplication.class, args);
    }

    // Java EE应用服务器配置，
    // 如果要使用tomcat来加载jsp的话就必须继承SpringBootServletInitializer类并且重写其中configure方法
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(GatwayServerApplication.class);
    }

    @Bean
    public AuthHeaderFilter authHeaderFilter() {
        return new AuthHeaderFilter();
    }

//    @Bean
//    public ResponseFilter responseFilter() {
//        return new ResponseFilter();
//    }

}
