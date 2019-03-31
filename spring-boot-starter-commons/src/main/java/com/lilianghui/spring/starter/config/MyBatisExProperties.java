package com.lilianghui.spring.starter.config;

import lombok.Data;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Properties;

@Data
@ConfigurationProperties(prefix = MyBatisExProperties.PREFIX)
public class MyBatisExProperties {
    public static final String PREFIX = "mybatis";
    public static final Properties DEFAULT_VENDOR_PROPERTIES = new Properties();
    static {
        DEFAULT_VENDOR_PROPERTIES.put("SQL Server","sqlserver");
        DEFAULT_VENDOR_PROPERTIES.put("DB2","db2");
        DEFAULT_VENDOR_PROPERTIES.put("Oracle","oracle");
    }

    private String entityScanPackages;
    private Class<? extends TypeHandler<?>> defaultEnumTypeHandler;

    private boolean safeRowBoundsEnabled;
    private boolean safeResultHandlerEnabled = true;
    private boolean mapUnderscoreToCamelCase;
    private boolean aggressiveLazyLoading;
    private boolean multipleResultSetsEnabled = true;
    private boolean useGeneratedKeys;
    private boolean useColumnLabel = true;
    private boolean cacheEnabled = true;
    private boolean callSettersOnNulls;
    private boolean useActualParamName = true;
    private boolean xmlMapperReload = true;
    private boolean returnInstanceForEmptyRow;

    private JdbcType jdbcTypeForNull = JdbcType.OTHER;

    private Properties vendorProperties = null;


}
