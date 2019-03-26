package com.lilianghui.spring.starter;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.lilianghui.spring.starter.config.MultiDataSourceProperties;
import com.lilianghui.spring.starter.utils.BeanFactoryUtils;
import com.mysql.jdbc.jdbc2.optional.MysqlXADataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.spring.mapper.ClassPathMapperScanner;
import tk.mybatis.spring.mapper.MapperFactoryBean;

import javax.annotation.Resource;
import javax.sql.DataSource;
import javax.sql.XADataSource;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
@Order(Integer.MIN_VALUE)
@EnableConfigurationProperties({MultiDataSourceProperties.class})
@ConditionalOnClass(com.atomikos.jdbc.AtomikosDataSourceBean.class)
//@ConditionalOnProperty(value = "spring.atomikos.datasource")
//@Conditional(MultiDataSourceAutoConfiguration.AatomikosCondition.class)
public class MultiDataSourceAutoConfiguration implements InitializingBean, EnvironmentAware, TransactionManagementConfigurer {


    private Environment environment;

    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private MultiDataSourceProperties multiDataSourceProperties;

    private Interceptor[] interceptors;

    private ResourceLoader resourceLoader;

    private DatabaseIdProvider databaseIdProvider;

    private List<ConfigurationCustomizer> configurationCustomizers;

    public MultiDataSourceAutoConfiguration(ObjectProvider<Interceptor[]> interceptorsProvider,
                                            ResourceLoader resourceLoader,
                                            ObjectProvider<DatabaseIdProvider> databaseIdProvider,
                                            ObjectProvider<List<ConfigurationCustomizer>> configurationCustomizersProvider) {
        this.interceptors = interceptorsProvider.getIfAvailable();
        this.resourceLoader = resourceLoader;
        this.databaseIdProvider = databaseIdProvider.getIfAvailable();
        this.configurationCustomizers = configurationCustomizersProvider.getIfAvailable();
    }

    /**
     * 注入事物管理器
     *
     * @return
     */
    @Bean
    public JtaTransactionManager transactionManager() {
        UserTransactionManager userTransactionManager = new UserTransactionManager();
        userTransactionManager.setForceShutdown(true);
        UserTransaction userTransaction = new UserTransactionImp();
        try {
            userTransaction.setTransactionTimeout(300);
        } catch (SystemException e) {
            log.error(e.getMessage(), e);
        }
        JtaTransactionManager jtaTransactionManager = new JtaTransactionManager(userTransaction, userTransactionManager);
        jtaTransactionManager.setAllowCustomIsolationLevels(true);
        return jtaTransactionManager;
    }

    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return transactionManager();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        registerSessionFactoryBean();
    }

    public void registerSessionFactoryBean() {
        if (MapUtils.isNotEmpty(multiDataSourceProperties.getDatasource())) {
            multiDataSourceProperties.getDatasource().forEach((name, dataSourceProperties) -> {
                AtomikosDataSourceBean ds = new AtomikosDataSourceBean();
                ds.setUniqueResourceName(name);
                ds.setXaDataSource((XADataSource) dataSourceProperties.initializeDataSourceBuilder().build());
//                BeanFactoryUtils.registerBean(applicationContext, name, ds);
                BeanFactoryUtils.registerBean(applicationContext, name, BeanFactoryDS.create(AtomikosDataSourceBean.class, ds));
                String sqlSessionFactoryName = String.format("%sSqlSessionFactory", name);
                String sqlSessionTemplateBeanName = String.format("%sSqlSessionTemplate", name);
                try {
                    SqlSessionFactory sqlSessionFactory = sqlSessionFactory(ds, dataSourceProperties.getMybatis());
                    BeanFactoryUtils.registerBean(applicationContext, sqlSessionFactoryName, BeanFactoryDS.create(SqlSessionFactory.class, sqlSessionFactory));
//                    BeanFactoryUtils.registerBean(applicationContext, sqlSessionFactoryName, sqlSessionFactory);
                    BeanFactoryUtils.registerBean(applicationContext, sqlSessionTemplateBeanName, BeanFactoryDS.create(SqlSessionTemplate.class, sqlSessionTemplate(sqlSessionFactory)));
//                    BeanFactoryUtils.registerBean(applicationContext, sqlSessionTemplateBeanName, sqlSessionTemplate(sqlSessionFactory));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                mapperScannerRegistrar(sqlSessionTemplateBeanName, dataSourceProperties.getMapper());
            });
        }
    }

    public SqlSessionFactory sqlSessionFactory(DataSource dataSource, MybatisProperties properties) throws Exception {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(dataSource);
        factory.setVfs(SpringBootVFS.class);
        if (StringUtils.hasText(properties.getConfigLocation())) {
            factory.setConfigLocation(resourceLoader.getResource(properties.getConfigLocation()));
        }
        org.apache.ibatis.session.Configuration configuration = properties.getConfiguration();
        if (configuration == null && !StringUtils.hasText(properties.getConfigLocation())) {
            configuration = new org.apache.ibatis.session.Configuration();
        }
        if (configuration != null && !CollectionUtils.isEmpty(configurationCustomizers)) {
            for (ConfigurationCustomizer customizer : configurationCustomizers) {
                customizer.customize(configuration);
            }
        }
        factory.setConfiguration(configuration);
        if (properties.getConfigurationProperties() != null) {
            factory.setConfigurationProperties(properties.getConfigurationProperties());
        }
        if (!ObjectUtils.isEmpty(interceptors)) {
            factory.setPlugins(interceptors);
        }
        if (databaseIdProvider != null) {
            factory.setDatabaseIdProvider(databaseIdProvider);
        }
        if (StringUtils.hasLength(properties.getTypeAliasesPackage())) {
            factory.setTypeAliasesPackage(properties.getTypeAliasesPackage());
        }
        if (StringUtils.hasLength(properties.getTypeHandlersPackage())) {
            factory.setTypeHandlersPackage(properties.getTypeHandlersPackage());
        }
        if (!ObjectUtils.isEmpty(properties.resolveMapperLocations())) {
            factory.setMapperLocations(properties.resolveMapperLocations());
        }

        return factory.getObject();
    }

    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) throws Exception {
        SqlSessionTemplate template = new SqlSessionTemplate(sqlSessionFactory); // 使用上面配置的Factory
        return template;
    }

    public void mapperScannerRegistrar(String sqlSessionTemplateBeanName, MultiDataSourceProperties.MapperProperties mapperProperties) {

        ClassPathMapperScanner scanner = new ClassPathMapperScanner((BeanDefinitionRegistry) applicationContext);

        // this check is needed in Spring 3.1
        if (resourceLoader != null) {
            scanner.setResourceLoader(resourceLoader);
        }

        Class<? extends Annotation> annotationClass = mapperProperties.getAnnotationClass();
        if (!Annotation.class.equals(annotationClass)) {
            scanner.setAnnotationClass(annotationClass);
        }

        Class<?> markerInterface = mapperProperties.getMarkerInterface();
        if (!Class.class.equals(markerInterface)) {
            scanner.setMarkerInterface(markerInterface);
        }

        Class<? extends BeanNameGenerator> generatorClass = mapperProperties.getNameGenerator();
        if (!BeanNameGenerator.class.equals(generatorClass)) {
            scanner.setBeanNameGenerator(BeanUtils.instantiateClass(generatorClass));
        }

        Class<? extends MapperFactoryBean> mapperFactoryBeanClass = mapperProperties.getFactoryBean();
        if (!MapperFactoryBean.class.equals(mapperFactoryBeanClass)) {
            scanner.setMapperFactoryBean(BeanUtils.instantiateClass(mapperFactoryBeanClass));
        }

        scanner.setSqlSessionTemplateBeanName(sqlSessionTemplateBeanName);
//        scanner.setSqlSessionFactoryBeanName(annoAttrs.getString("sqlSessionFactoryRef"));

        List<String> basePackages = new ArrayList<String>();
        for (String pkg : mapperProperties.getValue()) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        for (String pkg : mapperProperties.getBasePackages()) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        for (Class<?> clazz : mapperProperties.getBasePackageClasses()) {
            basePackages.add(ClassUtils.getPackageName(clazz));
        }
        //优先级 mapperHelperRef > properties > springboot
        String mapperHelperRef = mapperProperties.getMapperHelperRef();
        String[] properties = mapperProperties.getProperties();
        if (StringUtils.hasText(mapperHelperRef)) {
            scanner.setMapperHelperBeanName(mapperHelperRef);
        } else if (properties != null && properties.length > 0) {
            scanner.setMapperProperties(properties);
        } else {
            try {
                scanner.setConfig(mapperProperties);
                scanner.setMapperProperties(this.environment);
            } catch (Exception e) {
                log.warn("只有 Spring Boot 环境中可以通过 Environment(配置文件,环境变量,运行参数等方式) 配置通用 Mapper，" +
                        "其他环境请通过 @MapperScan 注解中的 mapperHelperRef 或 properties 参数进行配置!" +
                        "如果你使用 tk.mybatis.mapper.session.Configuration 配置的通用 Mapper，你可以忽略该错误!", e);
            }
        }
        scanner.registerFilters();
        scanner.doScan(StringUtils.toStringArray(basePackages));
    }


    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public static class AatomikosCondition implements Condition {

        @Override
        public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
            MultiDataSourceProperties multiDataSourceProperties = bind(conditionContext.getEnvironment(), MultiDataSourceProperties.class, MultiDataSourceProperties.PREFIX);
            return MapUtils.isNotEmpty(multiDataSourceProperties.getDatasource());
        }

        public <T> T bind(Environment environment, Class<T> targetClass, String prefix) {
            try {
                Class<?> resolverClass = Class.forName("org.springframework.boot.bind.RelaxedPropertyResolver");
                Constructor<?> resolverConstructor = resolverClass.getDeclaredConstructor(PropertyResolver.class);
                Method getSubPropertiesMethod = resolverClass.getDeclaredMethod("getSubProperties", String.class);
                Object resolver = resolverConstructor.newInstance(environment);
                Map<String, Object> properties = (Map) getSubPropertiesMethod.invoke(resolver, "");
                T target = targetClass.newInstance();
                Class<?> binderClass = Class.forName("org.springframework.boot.bind.RelaxedDataBinder");
                Constructor<?> binderConstructor = binderClass.getDeclaredConstructor(Object.class, String.class);
                Method bindMethod = binderClass.getMethod("bind", PropertyValues.class);
                Object binder = binderConstructor.newInstance(target, prefix);
                bindMethod.invoke(binder, new MutablePropertyValues(properties));
                return target;
            } catch (Exception var14) {
                throw new RuntimeException(var14);
            }
        }
    }


    static class BeanFactoryDS<T> implements FactoryBean<T> {
        private T object;
        private Class<?> clazz;

        public static AbstractBeanDefinition create(Class clazz, Object object) {
            BeanDefinitionBuilder bean = BeanDefinitionBuilder.rootBeanDefinition(BeanFactoryDS.class);
            bean.setRole(BeanDefinition.ROLE_APPLICATION);
            bean.addConstructorArgValue(object);
            bean.addConstructorArgValue(clazz);
            return bean.getBeanDefinition();
        }


        public BeanFactoryDS(T object, Class<T> clazz) {
            this.object = object;
            this.clazz = clazz;
        }

        @Override
        public T getObject() throws Exception {
            return object;
        }

        @Override
        public Class<?> getObjectType() {
            return clazz;
        }
    }
}
