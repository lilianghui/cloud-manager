package com.lilianghui.framework.core.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lilianghui.framework.core.poi.excel.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.util.ReflectionUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class PlatformUtils {
    private static final String PATTERN_STRING = "\\$\\{((.*?))\\}";
    private static final Pattern PATTERN = Pattern.compile(PATTERN_STRING);

    public static Object getPropertyValue(Object target, String name) {
        try {
            if (target != null && StringUtils.isNotBlank(name)) {
                return PropertyUtils.getProperty(target, name);
            }
        } catch (NoSuchMethodException e) {
            return getFieldValue(target, name);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return null;
    }

    public static void setPropertyValue(Object target, String name, Object value) {
        try {
            if (target != null && StringUtils.isNotBlank(name)) {
                setProperty(target, name);
                BeanUtils.setProperty(target, name, value);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static Object getFieldValue(Object target, String name) {
        Object result = null;
        try {
            if (target != null) {
                Field field = getField(target.getClass(), name);
                if (field != null) {
                    result = field.get(target);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return result;
    }

    public static Field getField(Class<?> clazz, String name) {
        try {
            if (clazz != null && StringUtils.isNotBlank(name)) {
                Field field = ReflectionUtils.findField(clazz, name);
                if (field != null) {
                    field.setAccessible(true);
                    return field;
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return null;
    }

    public static void setFieldValue(Object target, String name, Object value) {
        try {
            if (target != null) {
                Field field = getField(target.getClass(), name);
                if (field != null) {
                    field.set(target, converter(field, value));
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
    }

    private static Object converter(Field field, Object value) throws Exception {
        if (value == null) {
            return null;
        }
        Class<?> type = field.getType();
        Class<?> clazz = value.getClass();
        if ((value instanceof String) && !type.equals(String.class) && ((String) value).equals("")) {
            if (type.equals(int.class) || type.equals(float.class) || type.equals(double.class)) {
                return 0;
            } else if (type.equals(boolean.class)) {
                return false;
            }
            return null;
        }
        if (type.equals(clazz)) {
            return value;
        } else if ((type.equals(Integer.class) || type.equals(int.class)) && clazz.equals(String.class)) {
            return Integer.valueOf((String) value);
        } else if ((type.equals(boolean.class) || type.equals(Boolean.class)) && clazz.equals(String.class)) {
            return Boolean.valueOf((String) value);
        } else if (type.equals(Date.class) && clazz.equals(String.class)) {
            return DateUtils.parseDate((String) value, Constant.DATE_FORMAT_ARRAY);
        } else if ((type.equals(double.class) || type.equals(Double.class)) && clazz.equals(String.class)) {
            return Double.valueOf((String) value);
        } else if ((type.equals(float.class) || type.equals(Float.class)) && clazz.equals(String.class)) {
            return Float.valueOf((String) value);
        }
        return value;
    }

    public static Field[] getDeclaredFields(Class<?> clazz, boolean deep) {
        Set<Field> fields = new LinkedHashSet<>();
        while (Object.class != clazz && clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                if (Modifier.isFinal(field.getModifiers()) || Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                field.setAccessible(true);
                fields.add(field);
            }
            if (!deep) {
                break;
            }
            clazz = clazz.getSuperclass();
        }
        return fields.toArray(new Field[0]);
    }

    public static String join(Object[] collection, String prefix) {
        return join(Arrays.asList(collection), null, prefix);
    }

    public static String join(Collection<?> collection, String field) {
        return join(collection, field, "");
    }

    public static String join(Collection<?> collection, String field, String prefix) {
        return join(collection, field, prefix, ",");
    }

    public static String join(Collection<?> collection, String field, String prefix, String separate) {
        if (CollectionUtils.isEmpty(collection)) {
            return "";
        }
        prefix = prefix == null ? "" : prefix;
        Set<String> set = new LinkedHashSet<>();
        try {
            for (Object object : collection) {
                Object val = object;
                if (!(object instanceof String || object instanceof Integer || object instanceof Double || object instanceof Float)) {
                    val = getPropertyValue(object, field);
                }
                set.add(prefix + val + prefix);
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return StringUtils.join(set, separate);
    }

    public static Map<String, Object> object2Map(Object object) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            if (object != null) {
                for (Field field : getDeclaredFields(object.getClass(), false)) {
                    map.put(field.getName(), getPropertyValue(object, field.getName()));
                }
            }
        } catch (Exception e) {
        }
        return map;
    }

    public static List<Map<String, Object>> list2Map(Collection<?> list) {
        List<Map<String, Object>> rs = new ArrayList<Map<String, Object>>();
        if (CollectionUtils.isNotEmpty(list)) {
            for (Object object : list) {
                rs.add(object2Map(object));
            }
        }
        return rs;
    }

    public static <T extends Number> List<T> stringArray2NumberArray(Class<T> clazz, String[] source) throws Exception {
        List<T> list = Lists.newArrayList();
        if (ArrayUtils.isNotEmpty(source)) {
            Method method = clazz.getDeclaredMethod("valueOf",String.class);
            for (int i = 0; i < source.length; i++) {
                list.add((T) method.invoke(null, source[i]));
            }
        }
        return list;
    }

    public static void setPropertyValues(Object target, List<String> names, Object... values) {
        try {
            if (CollectionUtils.isNotEmpty(names)) {
                for (int i = 0; i < names.size(); i++) {
                    setPropertyValue(target, names.get(i), values[i]);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
    }

    public static void setPropertyValues(Collection<?> targets, String[] name, Object[] value) {
        try {
            if (CollectionUtils.isNotEmpty(targets)) {
                for (Object target : targets) {
                    for (int i = 0; i < name.length; i++) {
                        setPropertyValue(target, name[i], value[i]);
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
    }

    public static <T> List<T> createProperty(Class<T> clazz, String name, Serializable[] values) {
        List<T> list = new ArrayList<T>();
        try {
            if (ArrayUtils.isNotEmpty(values)) {
                for (Serializable value : values) {
                    if (value != null) {
                        T t = clazz.newInstance();
                        setPropertyValue(t, name, value);
                        list.add(t);
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return list;
    }

    public static <T> List<T> createProperty(Class<T> clazz, String name, Collection<?> values) {
        List<T> list = new ArrayList<T>();
        try {
            if (CollectionUtils.isNotEmpty(values)) {
                for (Object value : values) {
                    if (value != null) {
                        T t = clazz.newInstance();
                        setPropertyValue(t, name, value);
                        list.add(t);
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return list;
    }

    public static Set getPropertyValues(Collection<?> source, String name) {
        Set list = new LinkedHashSet<>();
        try {
            if (CollectionUtils.isNotEmpty(source)) {
                for (Object target : source) {
                    Object val = getPropertyValue(target, name);
                    if(val!=null){
                        list.add(val);
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return list;
    }

    private static void setProperty(Object t, String name) {
        try {
            String temp = name;
            do {
                String[] names = resolverRemove(temp);
                if (ArrayUtils.isEmpty(names)) {
                    return;
                }
                name = names[0];
                temp = names[1];
                Object value = PlatformUtils.getPropertyValue(t, name);
                if (value == null) {
                    Class<?> type = PlatformUtils.getField(t.getClass(), name).getType();
                    if (!isSimpleType(type)) {
                        value = type.newInstance();
                        BeanUtils.setProperty(t, name, value);
                    }
                }
                t = value;
            } while (true);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
    }

    private static String[] resolverRemove(String name) {
        if (!hasNested(name)) {
            return null;
        }
        return new String[]{name.substring(0, name.indexOf(".")), name.substring(name.indexOf(".") + 1)};
    }

    private static boolean hasNested(String name) {
        if (StringUtils.isNotBlank(name)) {
            if (name.indexOf(".") >= 0) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    private static final Set<Class<?>> SIMPLE_TYPE_SET = new HashSet<>();

    static {
        SIMPLE_TYPE_SET.add(String.class);
        SIMPLE_TYPE_SET.add(Byte.class);
        SIMPLE_TYPE_SET.add(Short.class);
        SIMPLE_TYPE_SET.add(Character.class);
        SIMPLE_TYPE_SET.add(Integer.class);
        SIMPLE_TYPE_SET.add(Long.class);
        SIMPLE_TYPE_SET.add(Float.class);
        SIMPLE_TYPE_SET.add(Double.class);
        SIMPLE_TYPE_SET.add(Boolean.class);
        SIMPLE_TYPE_SET.add(Date.class);
        SIMPLE_TYPE_SET.add(Class.class);
        SIMPLE_TYPE_SET.add(BigInteger.class);
        SIMPLE_TYPE_SET.add(BigDecimal.class);
        SIMPLE_TYPE_SET.add(Number.class);
        SIMPLE_TYPE_SET.add(int.class);
        SIMPLE_TYPE_SET.add(float.class);
        SIMPLE_TYPE_SET.add(double.class);
        SIMPLE_TYPE_SET.add(long.class);
        SIMPLE_TYPE_SET.add(short.class);
        SIMPLE_TYPE_SET.add(byte.class);
        SIMPLE_TYPE_SET.add(byte[].class);
        SIMPLE_TYPE_SET.add(boolean.class);
    }

    public static boolean isSimpleType(Class<?> type) {
        if (SIMPLE_TYPE_SET.contains(type) || type.isEnum()) {
            return true;
        }
        for (Class<?> clazz : SIMPLE_TYPE_SET) {
            if (clazz.isAssignableFrom(type)) {
                return true;
            }
        }
        return false;
    }

    public static Map<String, Object> parse(String text, Map<String, Object> props){
        Map<String, Object> map= Maps.newHashMap();
        Matcher matcher = PATTERN.matcher(text);
        boolean find = false;
        while (matcher.find()) {
            String[] splits = matcher.group(1).split(":");
            Object value = props.get(splits[0]);
            if (value == null) {
                if (splits.length > 1) {
                    value = splits[1];
                } else {
                    value = System.getProperty(splits[0]);
                }
            }
            map.put(matcher.group(0), value);
        }
        return map;
    }
}
