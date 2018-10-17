package com.lilianghui.framework.core.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class BasicObjectMapper extends ObjectMapper {
    private static final long serialVersionUID = 1L;

    public BasicObjectMapper() {
        this("yyyy-MM-dd","######0.00");
    }

    public BasicObjectMapper(String format, String doubleFormat) {
        // 空值处理为空串
        this.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {

            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
                jgen.writeString("");
            }
        });
        setDateFormat(new SimpleDateFormat(format));
        // 忽略多余属性
        this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 允许单引号
        this.configure(Feature.ALLOW_SINGLE_QUOTES, true);
        // 允许key不带引号
        this.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        // 允许控制字符
        this.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);

        this.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        this.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
        SimpleModule module = new SimpleModule();
//        module.addDeserializer(Enum.class, new EnumDeserialize());
//        module.addSerializer(Enum.class, new EnumSerializer());
        //		module.addSerializer(Timestamp.class, new TimestampSerializer());
        module.addDeserializer(Date.class, new DateTimeDeserializer());
        module.addDeserializer(Serializable.class, new SerializableDeserialize());
        module.addSerializer(Double.class, new DoubleSerializer(doubleFormat));
        module.addDeserializer(Boolean.class, new BooleanDeserialize());
        this.registerModule(module);

        // 属性为 NULL 都不序列化
        // this.setSerializationInclusion(Include.NON_NULL);

        // SerializerFactory serializerFactory = BeanSerializerFactory.instance
        // .withSerializerModifier(new JacSerializerModifier());
        // this.setSerializerFactory(this.getSerializerFactory().withSerializerModifier(new
        // JacSerializerModifier()));
        // this.getSerializerFactory().withSerializerModifier(new
        // JacSerializerModifier());
        // setSerializerFactory(serializerFactory);
        // registerModules(new SimpleModule() {
        // @Override
        // public void setupModule(SetupContext context) {
        // super.setupModule(context);
        // context.addBeanSerializerModifier(new JacSerializerModifier());
        // }
        // });
        // setPropertyNamingStrategy(new CapitalizedPropertyNamingStrategy());
        SimpleFilterProvider filterProvider = new SimpleFilterProvider(new HashMap<String, String[]>());
        filterProvider.setFailOnUnknownId(false);
        setFilterProvider(filterProvider);
    }


}
