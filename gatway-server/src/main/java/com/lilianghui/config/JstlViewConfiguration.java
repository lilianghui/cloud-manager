package com.lilianghui.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.InternalResourceView;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import java.util.Locale;

//@Configuration
public class JstlViewConfiguration {

    @Value("${spring.mvc.view.prefix}")
    private String prefix;
    @Value("${spring.mvc.view.suffix}")
    private String suffix;
//    @Value("${spring.mvc.view.view-name}")
//    private String viewName;
    @Value("${spring.mvc.view.order:0}")
    private int order;

    @Bean
    public InternalResourceViewResolver internalResourceView() {
        InternalResourceViewResolver internalResourceViewResolver = new InternalResourceViewResolver();
        internalResourceViewResolver.setPrefix(prefix);
        internalResourceViewResolver.setSuffix(suffix);
//        internalResourceViewResolver.setViewNames(viewName);
        internalResourceViewResolver.setOrder(1000);
        return internalResourceViewResolver;
    }

}
