package com.lilianghui.spring.starter.config;

import lombok.Data;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = MyBatisExProperties.PREFIX)
public class MyBatisExProperties {
    public static final String PREFIX = "mybatis";

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
    private boolean returnInstanceForEmptyRow;

    private JdbcType jdbcTypeForNull = JdbcType.OTHER;


}
