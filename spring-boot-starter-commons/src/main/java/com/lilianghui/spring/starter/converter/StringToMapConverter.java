package com.lilianghui.spring.starter.converter;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.Configuration;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@ConfigurationPropertiesBinding
public class StringToMapConverter implements Converter<String, LinkedHashMap> {
    @Override
    public LinkedHashMap convert(String source) {
        try {
            LinkedHashMap<String,String> stringStringMap=new LinkedHashMap<>();
            Arrays.asList(source.split("\n")).forEach(rows -> {
                String[] row = rows.split(":");
                stringStringMap.put(StringUtils.defaultString(row[0]),StringUtils.defaultString(row[1]));
            });
            return stringStringMap;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
