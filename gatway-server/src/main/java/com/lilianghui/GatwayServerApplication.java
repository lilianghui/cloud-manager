package com.lilianghui;

import brave.sampler.Sampler;
import cn.springcloud.feign.VenusFeignAutoConfig;
import com.lilianghui.config.DefaultFeignConfiguration;
import com.lilianghui.config.DefaultRibbonConfiguration;
import com.lilianghui.config.filter.AuthHeaderFilter;
import com.lilianghui.config.filter.GrayFilter;
import com.lilianghui.spring.starter.annotation.EnableNettyRpcClients;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
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
import org.springframework.context.annotation.Primary;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication(exclude = {VenusFeignAutoConfig.class})
@EnableZuulProxy
@MapperScan("com.lilianghui.mapper")
@EnableFeignClients(value = "com.lilianghui.client", defaultConfiguration = DefaultFeignConfiguration.class)
@RibbonClients(defaultConfiguration = DefaultRibbonConfiguration.class)
@EnableNettyRpcClients(basePackages = {"com.lilianghui.interfaces"})
public class GatwayServerApplication extends SpringBootServletInitializer {

    //如果没有使用默认值80
    @Value("${http.port:80}")
    private int httpPort;

    //正常启用的https端口 如443
    @Value("${server.port:443}")
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


    @Bean
    @Primary
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {

            @Override
            protected void postProcessContext(Context context) {

                SecurityConstraint securityConstraint = new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);
            }
        };
        tomcat.addAdditionalTomcatConnectors(createHTTPConnector());
        return tomcat;
    }

    private Connector createHTTPConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setSecure(false);
        //Connector监听的http的端口号
        connector.setPort(httpPort);
        //监听到http的端口号后转向到的https的端口号
        connector.setRedirectPort(httpsPort);
        return connector;
    }

    @Bean
    public Sampler sleuthTraceSampler() {
        return Sampler.ALWAYS_SAMPLE;
    }

}
