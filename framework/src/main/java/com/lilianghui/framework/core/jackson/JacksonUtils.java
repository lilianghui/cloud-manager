package com.lilianghui.framework.core.jackson;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.lilianghui.framework.core.utils.StaticApplication;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

@Slf4j
public class JacksonUtils {
    private static ObjectMapper objectMapper = null;

    public static <T> T readValue(String src, Class<T> clazz) {
        return readValue(src, clazz, false);
    }

    public static <T> T readValue(String src, Class<T> clazz, boolean exception) {
        T value = null;
        try {
            value = getObjectMapper().readValue(src, clazz);
        } catch (Exception e) {
            if (exception) {
                throw new RuntimeException(e);
            }
            log.error(e.getMessage(),e);
        }
        return value;
    }

    public static String writeValue(Object object) {
        return writeValue(object, null);
    }

    public static String writeValue(Object object, Map<Class<?>, String[]> map) {
        try {
            Map<String, String[]> result = new HashMap<>();
            if (map != null) {
                for (Entry<Class<?>, String[]> m : map.entrySet()) {
                    JsonFilter filter = m.getKey().getAnnotation(JsonFilter.class);
                    if (filter != null) {
                        result.put(filter.value(), m.getValue());
                    }
                }
            }
            SimpleFilterProvider filterProvider = new SimpleFilterProvider(result);
            filterProvider.setFailOnUnknownId(false);
            getObjectMapper().setFilterProvider(filterProvider);
            return getObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(),e);
        }
        return null;
    }

    public static <T> List<T> readListValue(String src, Class<T> clazz) {
        return (List<T>) readCollectionValue(src, List.class, clazz, true);
    }


    public static <K, V> Map<K, V> readCollectionMap(String src, Class<K> keyType, Class<V> valueType) throws Exception {
        JavaType keyJavaType = objectMapper.getTypeFactory().constructType(keyType);
        JavaType valueJavaType = objectMapper.getTypeFactory().constructCollectionType(List.class, valueType);
        JavaType javaType = objectMapper.getTypeFactory().constructMapType(Map.class, keyJavaType, valueJavaType);
        Map<String, Object> result = objectMapper.readValue(src, javaType);
        return (Map<K, V>) result;
    }

    public static <T> Collection<T> readCollectionValue(String src, Class<? extends Collection> collectionClass, Class<T> clazz, boolean exception) {
        try {
            if (StringUtils.isNotBlank(src)) {
                ObjectMapper objectMapperer = getObjectMapper();
                JavaType javaType = objectMapperer.getTypeFactory().constructCollectionType(collectionClass, clazz);
                Collection<?> businessinfo = objectMapperer.readValue(src, javaType);
                return (Collection<T>) businessinfo;
            }
        } catch (Exception e) {
            if (exception) {
                throw new RuntimeException(e);
            }
            log.error(e.getMessage(),e);
        }
        return List.class.isAssignableFrom(collectionClass) ? Collections.<T>emptyList() : Collections.<T>emptySet();
    }

    public static ObjectMapper getObjectMapper() {
        if (null == objectMapper) {
            synchronized (JacksonUtils.class) {
                try {
                    if (objectMapper == null) {
                        objectMapper = (ObjectMapper) StaticApplication.getBean(BasicObjectMapper.class);
                    }
                } catch (Exception e) {
                }
                if (objectMapper == null) {
                    objectMapper = new BasicObjectMapper();
                }
            }
        }
        return objectMapper;
    }
}
