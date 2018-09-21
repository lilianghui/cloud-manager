package com.lilianghui.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.View;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import java.util.Locale;

//@Configuration
public class MyThymeleafViewResolver extends ThymeleafViewResolver{

    @Override
    protected View loadView(String viewName, Locale locale) throws Exception {
        try {
            this.getApplicationContext().getResource(viewName).getInputStream();
        }catch (Exception e){
            return null;
        }
        View view = super.loadView(viewName, locale);
        return view;
    }
}
