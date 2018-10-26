package com.lilianghui.spring.starter.config;


import com.lilianghui.spring.starter.p6spy.FileLogger;
import com.lilianghui.spring.starter.p6spy.SingleLineFormat;
import com.p6spy.engine.logging.P6LogFactory;
import com.p6spy.engine.spy.P6SpyFactory;
import com.p6spy.engine.spy.appender.CustomLineFormat;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = P6spyProperties.PREFIX)
public class P6spyProperties {
    public static final String PREFIX = "spring.p6spy";

    private boolean autoFlush = false;
    private String driverList = null;
    private String logFile = "spy.log";
    private String logMessageFormat = SingleLineFormat.class.getName();
    private String customLogMessageFormat = String.format("%s|%s|%s|connection%s|%s",
            CustomLineFormat.CURRENT_TIME, CustomLineFormat.EXECUTION_TIME, CustomLineFormat.CATEGORY,
            CustomLineFormat.CONNECTION_ID, CustomLineFormat.SQL_SINGLE_LINE);
    private String databaseDialectBooleanFormat = "boolean";
    private boolean append = true;
    private String dateFormat = "yyyy-MM-dd HH:mm:ss";
    private String appender = FileLogger.class.getName();
    private String moduleList = P6SpyFactory.class.getName() + ","+ P6LogFactory.class.getName();
    private boolean stackTrace = false;
    private String stackTraceClass = null;
    private boolean reloadProperties = false;
    private long reloadPropertiesInterval = 60L;
    private String jndiContextFactory = null;
    private String jndiContextProviderUrl = null;
    private String jndiContextCustom = null;
    private String realDataSource = null;
    private String realDataSourceClass = null;
    private String realDataSourceProperties = null;
    private String databaseDialectDateFormat = "yyyy-MM-dd HH:mm:ss";
    private boolean jmx = true;
    private String jmxPrefix = null;
    private String driverNames = null;
    private String moduleFactories = null;
    private String moduleNames = null;
    private String logMessageFormatInstance = null;
    private String appenderInstance = null;

    private String exclude = null;
    private String include = null;
    private boolean filter = false;
    private String excludecategories = "info,debug,result,resultset,batch";
    private boolean excludebinary = false;
    private long executionThreshold = 0;
    private String sqlexpression = null;

    private String sessionName = "session";
    private String idField = "id";
    private String nameField = "name";
}
