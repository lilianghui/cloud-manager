package com.lilianghui;

import com.lilianghui.core.AbstractGenerator;
import com.lilianghui.core.Generator2;
import com.lilianghui.db.DataOperator;
import com.lilianghui.db.JdbcUtils;
import com.lilianghui.entity.Config;
import com.lilianghui.entity.Table;
import com.lilianghui.entity.TableRef;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.util.*;

@Mojo(name = "generator")
public class Generator extends AbstractMojo implements Config {

    public static Config config = null;
    private DataOperator dataOperator = new DataOperator();

    public static void main(String[] args) throws Exception {
        Generator generator = new Generator();
        generator.before();
        generator.execute();
    }

    public void before() throws Exception {
        setJdbcDriver("com.mysql.jdbc.Driver");
        setJdbcUrl("jdbc:mysql://127.0.0.1:3306/demo?useOldAliasMetadataBehavior=true");
        setJdbcUser("root");
        setJdbcPassword("123456");
        setJdbcTablePrefix("m_");
        setJdbcColumnPrefix("");
        setFileSavePath("E:/");
        setDatabaseType("MYSQL");
        setControllerPath("com.walkiesoft.controller");
        setEntityPath("com.walkiesoft.entity");
        setServicePath("com.walkiesoft.service");
        setDaoPath("com.walkiesoft.mapper");
        setMapperPath("mapper");
        setModularPath("com.walkiesoft.atmss.modular");
        setControllerExtend("com.walkiesoft.commons.base.BasicController");
        setEntityExtend("'com.walkiesoft.framework.mybatis.plugin.entity.BaseEntity");
        setDaoExtend("com.walkiesoft.framework.tk.mybatis.mapper.common.Mapper");
        setServiceExtend("com.walkiesoft.commons.base.BaseMapperService");
        setServiceImplExtend("com.walkiesoft.commons.base.AbstractBaseMapperService");
        setImportValueStyle("com.walkiesoft.framework.tk.mybatis.support.annotation.ValueStyle");
        setController(true);
        setEntity(true);
        setService(true);
        setDao(true);
        setMapConfig(true);
        setFrameWork(false);
        setCollection(false);
        setSeq(false);
        setCamelCaseNamin(true);
        setModular(false);
        setGenerator(Generator2.class.getName());
        setOgnl("@com.walkiesoft.framework.tk.mybatis.support.helper.Ognl@now()");
        setColumnUpperCase(false);
        setExUpdateColumns(new HashSet<>());
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            config = this;
            getLog().info("参数：" + toString());
            parse();
            generator();
        } catch (Exception e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }

    private void parse(){
        if(StringUtils.isNotBlank(controllerPath)){
            setControllerPath(controllerPath.replaceAll("/|\\\\","."));
        }
        if(StringUtils.isNotBlank(entityPath)){
            setEntityPath(entityPath.replaceAll("/|\\\\","."));
        }
        if(StringUtils.isNotBlank(servicePath)){
            setServicePath(servicePath.replaceAll("/|\\\\","."));
        }
        if(StringUtils.isNotBlank(daoPath)){
            setDaoPath(daoPath.replaceAll("/|\\\\","."));
        }
        if(StringUtils.isNotBlank(mapperPath)){
            setMapperPath(mapperPath.replaceAll("/|\\\\","."));
        }
        if(StringUtils.isNotBlank(modularPath)){
            setModularPath(modularPath.replaceAll("/|\\\\","."));
        }
    }

    private void generator() throws Exception {
        Vector<Vector<String>> arrayList = dataOperator.getTableName(this);
        if (arrayList == null || arrayList.size() == 0) {
            getLog().info("当前连接没查询到表数据");
            return;
        } // 生成类
        Set<String> tables = new HashSet<String>();
        Set<String> Alltables = new HashSet<String>();
        Map<String, Table> map = new HashMap<String, Table>();
        for (int i = 0; i < arrayList.size(); i++) {
            try {
                Vector<String> vector = arrayList.get(i);
                String tableName = vector.get(1);
                String tableComment = vector.get(2);
                String key = vector.get(3);
                String identityTmp = vector.get(4);
                boolean identity = false;
                if ("自增".equalsIgnoreCase(identityTmp)) {
                    identity = true;
                }
                String entityName = vector.get(5);
                String iDaoName = vector.get(6);
                String iServiceName = vector.get(7);
                String serviceImplName = vector.get(8);
                String actionName = vector.get(9);
                String alias = "";
                if (!config.isSeq()) {
                    alias = vector.get(10);
                }
                List<Map<String, String>> list = dataOperator.getColumnEntity(config, tableName);
                Alltables.add("'" + tableName + "'");
//
                Table table = new Table(config, identity, tableName, tableComment, iDaoName, iServiceName, serviceImplName, actionName,
                        key, entityName, alias, list);
                map.put(tableName, table);
                tables.add(tableName);
            } catch (Exception e1) {
                e1.printStackTrace();
                return;
            }
        }
        setMap(map);

        Map<String, Set<TableRef>> many = new HashMap<String, Set<TableRef>>();
        Map<String, Set<TableRef>> one = new HashMap<String, Set<TableRef>>();

        try {
            List<TableRef> tableRef = dataOperator.getColumnRef(this, StringUtils.join(Alltables, ","));
            for (TableRef ref : tableRef) {
                String tableName = ref.getOneTableName();
                Set<TableRef> manyList = many.get(tableName);
                if (manyList == null) {
                    many.put(tableName, new HashSet<TableRef>());
                    manyList = many.get(tableName);
                }
                manyList.add(ref);

                //
                tableName = ref.getManyTableName();
                Set<TableRef> oneList = one.get(tableName);
                if (oneList == null) {
                    one.put(tableName, new HashSet<TableRef>());
                    oneList = one.get(tableName);
                }
                oneList.add(ref);
            }

        } catch (Exception e1) {
            e1.printStackTrace();
            return;
        }

        Set<String> includeTables = new HashSet<>();
        Set<String> excludeTables = new HashSet<>();
        if (StringUtils.isNotBlank(includeTable)) {
            Collections.addAll(includeTables, includeTable.split(","));
        }
        if (StringUtils.isNotBlank(excludeTable)) {
            Collections.addAll(excludeTables, excludeTable.split(","));
        }
        for (Table table : map.values()) {
            try {
                if (tables.contains(table.getTable())) {
                    if ((includeTables != null && includeTables.size() > 0 && !includeTables.contains(table.getTable()))
                            || (excludeTables != null && excludeTables.size() > 0 && excludeTables.contains(table.getTable()))) {
                        continue;
                    }
                    table.setMany(many.get(table.getTable()));
                    table.setOne(one.get(table.getTable()));
                    AbstractGenerator abstractGenerator = (AbstractGenerator) Class.forName(this.getGenerator())
                            .getConstructor(Config.class, Table.class).newInstance(this, table);
                    abstractGenerator.generator();
                    getLog().info("生成" + table.getTable());
                }
            } catch (Exception e1) {
                e1.printStackTrace();
                return;
            }
        }

        JdbcUtils.closeAll();
    }

    @Parameter(required = true)
    private String jdbcDriver;
    @Parameter(required = true)
    private String jdbcUrl;
    @Parameter(required = true)
    private String jdbcUser;
    @Parameter(required = true)
    private String jdbcPassword;
    @Parameter(defaultValue = "m_")
    private String jdbcTablePrefix;
    @Parameter
    private String jdbcColumnPrefix;
    @Parameter
    private String ignorePrefix;
    @Parameter
    private String fileSavePath;
    @Parameter(defaultValue = "MYSQL")
    private String databaseType;
    @Parameter(defaultValue = "com.walkiesoft.controller")
    private String controllerPath;
    @Parameter(defaultValue = "com.walkiesoft.entity")
    private String entityPath;
    @Parameter(defaultValue = "com.walkiesoft.service")
    private String servicePath;
    @Parameter(defaultValue = "com.walkiesoft.mapper")
    private String daoPath;
    @Parameter(defaultValue = "mapper")
    private String mapperPath;
    @Parameter(defaultValue = "com.walkiesoft.atmss.modular")
    private String modularPath;
    @Parameter(defaultValue = "com.walkiesoft.commons.base.BasicController")
    private String controllerExtend;
    @Parameter(defaultValue = "com.walkiesoft.framework.mybatis.plugin.entity.BaseEntity")
    private String entityExtend;
    @Parameter(defaultValue = "com.walkiesoft.framework.tk.mybatis.mapper.common.Mapper")
    private String daoExtend;
    @Parameter(defaultValue = "com.walkiesoft.commons.base.BaseMapperService")
    private String serviceExtend;
    @Parameter(defaultValue = "com.walkiesoft.commons.base.AbstractBaseMapperService")
    private String serviceImplExtend;
    @Parameter(defaultValue = "com.walkiesoft.framework.tk.mybatis.support.annotation.ValueStyle")
    private String importValueStyle;
    @Parameter(defaultValue = "true")
    private boolean controller;
    @Parameter(defaultValue = "true")
    private boolean entity;
    @Parameter(defaultValue = "true")
    private boolean service;
    @Parameter(defaultValue = "true")
    private boolean dao;
    @Parameter(defaultValue = "true")
    private boolean mapConfig;
    @Parameter
    private boolean frameWork;
    @Parameter
    private boolean collection;
    @Parameter
    private boolean seq;
    @Parameter(defaultValue = "true")
    private boolean camelCaseNamin;
    @Parameter
    private boolean modular;
    @Parameter(defaultValue = "com.lilianghui.core.Generator2")
    private String generator;
    @Parameter(defaultValue = "@com.walkiesoft.framework.tk.mybatis.support.helper.Ognl@now()")
    private String ognl;
    @Parameter(defaultValue = "false")
    private boolean columnUpperCase;
    @Parameter
    private String excludeTable;
    @Parameter
    private String includeTable;

    private Set<String> exUpdateColumns = new HashSet<>();

    private Map<String, Table> map = new HashMap<String, Table>();

    public String getJdbcDriver() {
        return jdbcDriver;
    }

    public void setJdbcDriver(String jdbcDriver) {
        this.jdbcDriver = jdbcDriver;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getJdbcUser() {
        return jdbcUser;
    }

    public void setJdbcUser(String jdbcUser) {
        this.jdbcUser = jdbcUser;
    }

    public String getJdbcPassword() {
        return jdbcPassword;
    }

    public void setJdbcPassword(String jdbcPassword) {
        this.jdbcPassword = jdbcPassword;
    }

    public String getJdbcTablePrefix() {
        return jdbcTablePrefix;
    }

    public void setJdbcTablePrefix(String jdbcTablePrefix) {
        this.jdbcTablePrefix = jdbcTablePrefix;
    }

    public String getJdbcColumnPrefix() {
        return jdbcColumnPrefix;
    }

    public void setJdbcColumnPrefix(String jdbcColumnPrefix) {
        this.jdbcColumnPrefix = jdbcColumnPrefix;
    }

    public String getFileSavePath() {
        return fileSavePath;
    }

    public void setFileSavePath(String fileSavePath) {
        this.fileSavePath = fileSavePath;
    }

    public String getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
    }

    public String getControllerPath() {
        return controllerPath;
    }

    public void setControllerPath(String controllerPath) {
        this.controllerPath = controllerPath;
    }

    public String getEntityPath() {
        return entityPath;
    }

    public void setEntityPath(String entityPath) {
        this.entityPath = entityPath;
    }

    public String getServicePath() {
        return servicePath;
    }

    public void setServicePath(String servicePath) {
        this.servicePath = servicePath;
    }

    public String getDaoPath() {
        return daoPath;
    }

    public void setDaoPath(String daoPath) {
        this.daoPath = daoPath;
    }

    public String getMapperPath() {
        return mapperPath;
    }

    public void setMapperPath(String mapperPath) {
        this.mapperPath = mapperPath;
    }

    public String getModularPath() {
        return modularPath;
    }

    public void setModularPath(String modularPath) {
        this.modularPath = modularPath;
    }

    public boolean isController() {
        return controller;
    }

    public void setController(boolean controller) {
        this.controller = controller;
    }

    public boolean isEntity() {
        return entity;
    }

    public void setEntity(boolean entity) {
        this.entity = entity;
    }

    public boolean isService() {
        return service;
    }

    public void setService(boolean service) {
        this.service = service;
    }

    public boolean isDao() {
        return dao;
    }

    public void setDao(boolean dao) {
        this.dao = dao;
    }

    public boolean isMapConfig() {
        return mapConfig;
    }

    public void setMapConfig(boolean mapConfig) {
        this.mapConfig = mapConfig;
    }

    public boolean isModular() {
        return modular;
    }

    public boolean isFrameWork() {
        return frameWork;
    }

    public void setFrameWork(boolean frameWork) {
        this.frameWork = frameWork;
    }

    public Map<String, Table> getMap() {
        return map;
    }

    public void setMap(Map<String, Table> map) {
        this.map = map;
    }

    public boolean isCollection() {
        return collection;
    }

    public void setCollection(boolean collection) {
        this.collection = collection;
    }

    public boolean isSeq() {
        return seq;
    }

    public void setSeq(boolean seq) {
        this.seq = seq;
    }

    public String getGenerator() {
        return generator;
    }

    public void setGenerator(String generator) {
        this.generator = generator;
    }

    public boolean isCamelCaseNamin() {
        return camelCaseNamin;
    }

    public void setCamelCaseNamin(boolean camelCaseNamin) {
        this.camelCaseNamin = camelCaseNamin;
    }

    public String getEntityExtend() {
        return entityExtend;
    }

    public void setEntityExtend(String entityExtend) {
        this.entityExtend = entityExtend;
    }

    public String getDaoExtend() {
        return daoExtend;
    }

    public void setDaoExtend(String daoExtend) {
        this.daoExtend = daoExtend;
    }

    public String getServiceExtend() {
        return serviceExtend;
    }

    public void setServiceExtend(String serviceExtend) {
        this.serviceExtend = serviceExtend;
    }

    public String getServiceImplExtend() {
        return serviceImplExtend;
    }

    public void setServiceImplExtend(String serviceImplExtend) {
        this.serviceImplExtend = serviceImplExtend;
    }

    public String getControllerExtend() {
        return controllerExtend;
    }

    public void setControllerExtend(String controllerExtend) {
        this.controllerExtend = controllerExtend;
    }

    public boolean modular() {
        return modular;
    }

    public void setModular(boolean modular) {
        this.modular = modular;
    }

    public String getOgnl() {
        return ognl;
    }

    public void setOgnl(String ognl) {
        this.ognl = ognl;
    }

    public boolean isColumnUpperCase() {
        return columnUpperCase;
    }

    public void setColumnUpperCase(boolean columnUpperCase) {
        this.columnUpperCase = columnUpperCase;
    }

    public String getImportValueStyle() {
        return importValueStyle;
    }

    public void setImportValueStyle(String importValueStyle) {
        this.importValueStyle = importValueStyle;
    }

    public Set<String> getExUpdateColumns() {
        return exUpdateColumns;
    }

    public void setExUpdateColumns(Set<String> exUpdateColumns) {
        this.exUpdateColumns = exUpdateColumns;
    }

    public static Config getConfig() {
        return config;
    }

    public static void setConfig(Config config) {
        Generator.config = config;
    }

    public DataOperator getDataOperator() {
        return dataOperator;
    }

    public void setDataOperator(DataOperator dataOperator) {
        this.dataOperator = dataOperator;
    }

    public String getExcludeTable() {
        return excludeTable;
    }

    public void setExcludeTable(String excludeTable) {
        this.excludeTable = excludeTable;
    }

    public String getIncludeTable() {
        return includeTable;
    }

    public void setIncludeTable(String includeTable) {
        this.includeTable = includeTable;
    }

    @Override
    public String getIgnorePrefix() {
        return ignorePrefix;
    }

    public void setIgnorePrefix(String ignorePrefix) {
        this.ignorePrefix = ignorePrefix;
    }

    @Override
    public String toString() {
        return "Generator{" +
                "jdbcDriver='" + jdbcDriver + '\'' +
                ", jdbcUrl='" + jdbcUrl + '\'' +
                ", jdbcUser='" + jdbcUser + '\'' +
                ", jdbcPassword='" + jdbcPassword + '\'' +
                ", jdbcTablePrefix='" + jdbcTablePrefix + '\'' +
                ", jdbcColumnPrefix='" + jdbcColumnPrefix + '\'' +
                ", ignorePrefix='" + ignorePrefix + '\'' +
                ", fileSavePath='" + fileSavePath + '\'' +
                ", databaseType='" + databaseType + '\'' +
                ", controllerPath='" + controllerPath + '\'' +
                ", entityPath='" + entityPath + '\'' +
                ", servicePath='" + servicePath + '\'' +
                ", daoPath='" + daoPath + '\'' +
                ", mapperPath='" + mapperPath + '\'' +
                ", modularPath='" + modularPath + '\'' +
                ", controllerExtend='" + controllerExtend + '\'' +
                ", entityExtend='" + entityExtend + '\'' +
                ", daoExtend='" + daoExtend + '\'' +
                ", serviceExtend='" + serviceExtend + '\'' +
                ", serviceImplExtend='" + serviceImplExtend + '\'' +
                ", importValueStyle='" + importValueStyle + '\'' +
                ", controller=" + controller +
                ", entity=" + entity +
                ", service=" + service +
                ", dao=" + dao +
                ", mapConfig=" + mapConfig +
                ", frameWork=" + frameWork +
                ", collection=" + collection +
                ", seq=" + seq +
                ", camelCaseNamin=" + camelCaseNamin +
                ", modular=" + modular +
                ", generator='" + generator + '\'' +
                ", ognl='" + ognl + '\'' +
                ", columnUpperCase=" + columnUpperCase +
                ", exUpdateColumns=" + exUpdateColumns +
                '}';
    }
}
