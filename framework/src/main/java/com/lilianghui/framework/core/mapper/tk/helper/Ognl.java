package com.lilianghui.framework.core.mapper.tk.helper;

import com.google.common.collect.Lists;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.binding.MapperMethod.ParamMap;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;

import java.util.*;

public class Ognl {

    public static boolean hasField(Object parameter, String field) {
        if (parameter instanceof ParamMap) {
            ParamMap map = (ParamMap) parameter;
            Set<String> fields = new HashSet<>();
            String[] ff = (String[]) map.get("fields");
            if(map.containsKey("updateFields")){
                ff = (String[]) map.get("updateFields");
            }
            Collections.addAll(fields, ff);
            for (String f : fields) {
                if (f.equalsIgnoreCase(field)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    public static String fieldToString(Object parameter) {
        StringBuilder sql = new StringBuilder();
        if (parameter instanceof ParamMap) {
            ParamMap map = (ParamMap) parameter;
            map.put("id", "");
            Set<String> fields = new HashSet<>();
            Collections.addAll(fields, (String[]) map.get("fields"));
            List<?> records = (List<?>) map.get("records");
            if (CollectionUtils.isNotEmpty(records)) {
                List<String> list = new LinkedList<String>();
                Class<?> entityClass = records.get(0).getClass();
                Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
                for (EntityColumn column : columnList) {
                    if (column.isId()) {
                        fields.add(column.getProperty());
                    }
                }
                for (EntityColumn column : columnList) {
                    if (fields.contains(column.getProperty())) {
                        list.add(column.getColumn());
                    }
                }
                map.put("fields", list.toArray(new String[0]));
                sql.append(StringUtils.join(list, ","));
            }
        }
        return sql.toString();
    }

    public static boolean isNotBlank(Object parameter, String name) {
        try {
            Object value = PropertyUtils.getProperty(parameter, name);
            if (value instanceof String) {
                if (StringUtils.isNotBlank((String) value)) {
                    return true;
                }
            } else if (value instanceof Collection<?>) {
                if (CollectionUtils.isNotEmpty((Collection<?>) value)) {
                    return true;
                }
            } else if (value instanceof Map<?, ?>) {
                if (MapUtils.isNotEmpty((Map<?, ?>) value)) {
                    return true;
                }
            } else if (value != null) {
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public static String mergeUsing(Object parameter, String params) {
        Set<String> fields = new HashSet<>();
        Object record = null;
        if (parameter instanceof ParamMap) {
            ParamMap map = (ParamMap) parameter;
            Collections.addAll(fields, (String[]) map.get("fields"));
            record = map.get("record");
        }
        Collection<EntityColumn> columns = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(fields)) {
            for (String field : fields) {
                EntityColumn column = EntityHelper.getEntityTable(record.getClass()).getPropertyMap().get(field);
                if (column != null) {
                    columns.add(column);
                }
            }
        } else {
            columns = EntityHelper.getPKColumns(record.getClass());
        }
        String[] aa = column(columns);
        StringBuilder select = new StringBuilder("(SELECT ");
        select.append(aa[0]).append(" ");
        select.append(params == null ? "" : params);
        select.append(") T1 ON (");
        select.append(aa[1]);
        select.append(")");
        return select.toString();
    }

    private static String[] column(Collection<EntityColumn> columns) {
        Set<String> select = new HashSet<>();
        Set<String> on = new HashSet<>();
        for (EntityColumn column : columns) {
            if (!column.isInsertable()) {
                continue;
            }
            String columnName = column.getColumn();
            select.add(column.getColumnHolder("record", null) + " " + columnName);
            StringBuilder onBuffer = new StringBuilder();
            onBuffer.append(" T.").append(columnName).append("=T1.").append(columnName);
            on.add(onBuffer.toString());
        }
        return new String[]{StringUtils.join(select, ","), StringUtils.join(on, " AND")};

    }

    public static String column(Map params, Object key) {
//        Object value = params.get(key);
//        if (value instanceof RepeatBean) {
//            return ((RepeatBean) value).getProperty();
//        }
        return "";
    }

    public static Date now() {
        return new Date();
    }
}
