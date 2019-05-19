package com.lilianghui;

import brave.sampler.Sampler;
import cn.springcloud.feign.VenusFeignAutoConfig;
import com.lilianghui.config.DefaultFeignConfiguration;
import com.lilianghui.config.DefaultRibbonConfiguration;
import com.lilianghui.config.filter.AuthHeaderFilter;
import com.lilianghui.config.filter.GrayFilter;
import com.lilianghui.spring.starter.annotation.EnableNettyRpcClients;
import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication(exclude = {VenusFeignAutoConfig.class})
@EnableZuulProxy
@MapperScan("com.lilianghui.mapper")
@EnableFeignClients(value = "com.lilianghui.client", defaultConfiguration = DefaultFeignConfiguration.class)
@RibbonClients(defaultConfiguration = DefaultRibbonConfiguration.class)
@EnableNettyRpcClients(basePackages = {"com.lilianghui.interfaces"})
public class GatwayServerApplication extends SpringBootServletInitializer {

    @Value("${server.port}")
    private int port;

    @Value("${server.https-port}")
    private int httpsPort;

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

    @Bean
    public GrayFilter grayFilter() {
        return new GrayFilter();
    }

//    @Bean
//    public ResponseFilter responseFilter() {
//        return new ResponseFilter();
//    }


    //    @Bean
//    @Primary
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        tomcat.addAdditionalTomcatConnectors(createHTTPConnector());
        return tomcat;
    }

    private Connector createHTTPConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        //同时启用http（8080）、https（8443）两个端口
        connector.setScheme("http");
        connector.setSecure(false);
        connector.setPort(port);
        connector.setRedirectPort(httpsPort);
        return connector;
    }

    @Bean
    public Sampler sleuthTraceSampler() {
        return Sampler.ALWAYS_SAMPLE;
    }

}
