package com.lilianghui.spring.starter.converter;

import org.apache.ibatis.session.Configuration;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;

@org.springframework.context.annotation.Configuration
@ConfigurationPropertiesBinding
public class StringToMyBatisConfigurationConverter implements Converter<String, Configuration> {
    @Override
    public Configuration convert(String source) {
        try {
            return (Configuration) Class.forName(source).newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
