package com.lilianghui.entity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public interface Config {

    String getJdbcDriver();

    void setJdbcDriver(String jdbcDriver);

    String getJdbcUrl();

    void setJdbcUrl(String jdbcUrl);

    String getJdbcUser();

    void setJdbcUser(String jdbcUser);

    String getJdbcPassword();

    void setJdbcPassword(String jdbcPassword);

    String getJdbcTablePrefix();

    void setJdbcTablePrefix(String jdbcTablePrefix);

    String getJdbcColumnPrefix();

    void setJdbcColumnPrefix(String jdbcColumnPrefix);

    String getFileSavePath();

    void setFileSavePath(String fileSavePath);

    String getDatabaseType();

    void setDatabaseType(String databaseType);

    String getControllerPath();

    void setControllerPath(String controllerPath);

    String getEntityPath();

    void setEntityPath(String entityPath);

    String getServicePath();

    void setServicePath(String servicePath);

    String getDaoPath();

    void setDaoPath(String daoPath);

    String getMapperPath();

    void setMapperPath(String mapperPath);

    String getModularPath();

    void setModularPath(String modularPath);

    boolean isController();

    void setController(boolean controller);

    boolean isEntity();

    void setEntity(boolean entity);

    boolean isService();

    void setService(boolean service);

    boolean isDao();

    void setDao(boolean dao);

    boolean isMapConfig();

    void setMapConfig(boolean mapConfig);

    boolean isModular();

    boolean isFrameWork();

    void setFrameWork(boolean frameWork);

    Map<String, Table> getMap();

    void setMap(Map<String, Table> map);

    boolean isCollection();

    void setCollection(boolean collection);

    boolean isSeq();

    void setSeq(boolean seq);

    String getGenerator();

    void setGenerator(String generator);

    boolean isCamelCaseNamin();

    void setCamelCaseNamin(boolean camelCaseNamin);

    String getEntityExtend();

    void setEntityExtend(String entityExtend);

    String getDaoExtend();

    void setDaoExtend(String daoExtend);

    String getServiceExtend();

    void setServiceExtend(String serviceExtend);

    String getServiceImplExtend();

    void setServiceImplExtend(String serviceImplExtend);

    String getControllerExtend();

    void setControllerExtend(String controllerExtend);

    boolean modular();

    void setModular(boolean modular);

    String getOgnl();

    void setOgnl(String ognl);

    boolean isColumnUpperCase();

    void setColumnUpperCase(boolean columnUpperCase);

    String getImportValueStyle();

    void setImportValueStyle(String importValueStyle);

    Set<String> getExUpdateColumns();

    void setExUpdateColumns(Set<String> exUpdateColumns);

}
