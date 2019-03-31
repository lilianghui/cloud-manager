package com.lilianghui.spring.starter;

import com.google.common.collect.Sets;
import com.lilianghui.spring.starter.annotation.PropertyMapping;
import com.lilianghui.spring.starter.config.MyBatisExProperties;
import com.lilianghui.spring.starter.config.MybatisMapperRefresh;
import com.lilianghui.spring.starter.utils.BridgeHelper;
import com.lilianghui.spring.starter.utils.ScanPackage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

@Slf4j
@Configuration
@EnableConfigurationProperties({MybatisProperties.class, MyBatisExProperties.class})
@AutoConfigureBefore(name = "org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration")
public class MybatisExAutoConfiguration {

    @Resource
    private MyBatisExProperties myBatisExProperties;

    @Bean
    public DatabaseIdProvider databaseIdProvider() {
        VendorDatabaseIdProvider vendorDatabaseIdProvider = new VendorDatabaseIdProvider();
        vendorDatabaseIdProvider.setProperties(Optional.ofNullable(myBatisExProperties.getVendorProperties()).orElse(MyBatisExProperties.DEFAULT_VENDOR_PROPERTIES));
        return vendorDatabaseIdProvider;
    }


    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return new ConfigurationCustomizer();
    }

    public static class ConfigurationCustomizer implements org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer, DisposableBean {

        @Resource
        private MybatisProperties mybatisProperties;
        @Resource
        private MyBatisExProperties myBatisExProperties;
        @Resource
        private ApplicationContext applicationContext;

        @Override
        public void customize(org.apache.ibatis.session.Configuration configuration) {
            try {
                registerBeanMapping(configuration);
                registerConfigurationProperty(configuration);
                init(configuration);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }

        private void registerConfigurationProperty(org.apache.ibatis.session.Configuration configuration) {
            configuration.setDefaultEnumTypeHandler(myBatisExProperties.getDefaultEnumTypeHandler());
            configuration.setSafeRowBoundsEnabled(myBatisExProperties.isSafeRowBoundsEnabled());
            configuration.setSafeResultHandlerEnabled(myBatisExProperties.isSafeResultHandlerEnabled());
            configuration.setMapUnderscoreToCamelCase(myBatisExProperties.isMapUnderscoreToCamelCase());
            configuration.setAggressiveLazyLoading(myBatisExProperties.isAggressiveLazyLoading());
            configuration.setMultipleResultSetsEnabled(myBatisExProperties.isMultipleResultSetsEnabled());
            configuration.setUseGeneratedKeys(myBatisExProperties.isUseGeneratedKeys());
            configuration.setUseColumnLabel(myBatisExProperties.isUseColumnLabel());
            configuration.setCacheEnabled(myBatisExProperties.isCacheEnabled());
            configuration.setCallSettersOnNulls(myBatisExProperties.isCallSettersOnNulls());
            configuration.setUseActualParamName(myBatisExProperties.isUseActualParamName());
            configuration.setReturnInstanceForEmptyRow(myBatisExProperties.isReturnInstanceForEmptyRow());
            configuration.setJdbcTypeForNull(myBatisExProperties.getJdbcTypeForNull());
        }

        private void registerBeanMapping(org.apache.ibatis.session.Configuration configuration) throws Exception {
            Set<String> packages = new LinkedHashSet<>();
            packages.add(StringUtils.defaultString(mybatisProperties.getTypeAliasesPackage()));
            packages.add(StringUtils.defaultString(myBatisExProperties.getEntityScanPackages()));
            packages.removeAll(Arrays.asList("", null, "null"));

            if (CollectionUtils.isNotEmpty(packages)) {
                Set<Class<?>> beanMapping = new HashSet<>();
                beanMapping.addAll(ScanPackage.findPackageClasses(StringUtils.join(packages, ","), Sets.newHashSet(PropertyMapping.class, Table.class)));
                beanMapping.forEach(clazz -> {
                    builderPropertyMappingResultMap(configuration, clazz);
                    builderTkMybatisResultMap(configuration, clazz);
                });
            }
        }

        private void builderTkMybatisResultMap(org.apache.ibatis.session.Configuration configuration, Class<?> clazz) {
            if (clazz.isAnnotationPresent(Table.class)) {
                try {
//                String name = type.getName();
//                if (configuration.getColumnsRefMap().containsKey(name)) {
//                    throw new IllegalArgumentException(String.format("实体【%s】的数据库列已存在", name));
//                }
//                configuration.getColumnsRefMap().put(type.getName(), BridgeHelper.getColumnsRefMap(type));
                } catch (Exception e) {
                    log.error(String.format("给【%s】添加ColumnsRef出错,原因:【%s】", clazz.getName(), e.getMessage()));
                }

            }
        }


        private void builderPropertyMappingResultMap(org.apache.ibatis.session.Configuration configuration, Class<?> clazz) {
            if (!clazz.isAnnotationPresent(PropertyMapping.class) && !clazz.isAnnotationPresent(Table.class)) {
                return;
            }
            String id = null;
            if (StringUtils.isNotBlank(clazz.getAnnotation(PropertyMapping.class).value())) {
                id = clazz.getAnnotation(PropertyMapping.class).value();
            } else {
                id = BridgeHelper.getResultMapId(clazz);
            }
            if (configuration.hasResultMap(id)) {
                log.error("id为【" + id + "】的ResultMap已经存在!");
            } else {
                List<ResultMapping> resultMappings = new ArrayList<>();
                for (Field field : getDeclaredFields(clazz, true)) {
                    ResultMapping resultMapping = BridgeHelper.getResultMapping(configuration, field);
                    if (resultMapping != null) {
                        resultMappings.add(resultMapping);
                    }
                }
                ResultMap.Builder builder = new ResultMap.Builder(configuration, id, clazz, resultMappings, true);
                ResultMap resultMap = builder.build();
                if (resultMap != null && !configuration.hasResultMap(resultMap.getId())) {
                    configuration.addResultMap(resultMap);
                    if (log.isDebugEnabled()) {
                        log.debug(String.format("给【%s】添加ResultMap,id:【%s】", clazz.getName(), resultMap.getId()));

                    }
                }
            }

        }

        private Thread thread = null;

        @Override
        public void destroy() throws Exception {
            if (thread != null) {
                thread.interrupt();
            }
        }

        public void init(org.apache.ibatis.session.Configuration configuration) throws Exception {
            if (myBatisExProperties.isXmlMapperReload()) {
                org.springframework.core.io.Resource[] mappers = mybatisProperties.resolveMapperLocations();
                if (ArrayUtils.isNotEmpty(mappers)) {
                    thread = new Thread(new MybatisMapperRefresh(applicationContext, configuration, mappers), "mybatis xml refresh thread");
                    thread.start();
                }
            }
        }

        private Field[] getDeclaredFields(Class<?> clazz, boolean deep) {
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
    }

}
