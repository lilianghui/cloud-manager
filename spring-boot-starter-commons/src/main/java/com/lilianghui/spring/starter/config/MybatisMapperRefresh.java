package com.lilianghui.spring.starter.config;


import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.builder.xml.XMLMapperEntityResolver;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.session.Configuration;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public class MybatisMapperRefresh implements Runnable {

    public static final String COUNT_SUFFIX = "_Count";

    //    private Map<String, Resource> resourceMap = new HashMap<>();
    private Configuration configuration;
    private WatchService watchService;
    private ApplicationContext applicationContext;
    private String path;

    public MybatisMapperRefresh(ApplicationContext applicationContext, Configuration configuration, Resource[] mappers) throws Exception {
        path = mappers[0].getFile().getParent();
//        for (Resource mapper : mappers) {
//            resourceMap.put(mapper.getFile().getPath(), mapper);
//        }
        this.applicationContext = applicationContext;
        this.configuration = configuration;
        this.watchService = FileSystems.getDefault().newWatchService();
        Paths.get(path).register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY);
        log.info("mybatis xml refresh running");

    }


    @Override
    public void run() {
        while (true) {
            WatchKey key = null;
            try {
                // 获取下一个文件改动事件
                key = watchService.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    String file = path + File.separator + event.context();
                    Resource resource = applicationContext.getResource(file);
                    if (resource != null) {
                        refresh(resource);
                        log.info("mybatis xml {} file has reload done", file);
                    }
                }
                if (!reset(key)) {
                    break;
                }
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                log.info("mybatis mapper xml reload thread interrupte {}", e.getMessage());
                break;
            } catch (Exception e) {
                if (!reset(key)) {
                    break;
                }
                log.error(e.getMessage(), e);
            }

        }
    }

    private boolean reset(WatchKey key) {
        boolean flag = false;
        if (key != null) {
            // 重设WatchKey
            flag = key.reset();
            // 如果重设失败，退出监听
        }
        if (flag) {
            log.info("mybatis xml refresh WatchKey reset fail, thread exit!");
        }
        return flag;
    }

    /**
     * 刷新mapper
     *
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    private void refresh(Resource resource) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        boolean isSupper = configuration.getClass().getSuperclass() == Configuration.class;
        try {
            Field loadedResourcesField = isSupper ? configuration.getClass().getSuperclass().getDeclaredField("loadedResources") : configuration.getClass().getDeclaredField("loadedResources");
            loadedResourcesField.setAccessible(true);
            Set loadedResourcesSet = ((Set) loadedResourcesField.get(configuration));
            XPathParser xPathParser = new XPathParser(resource.getInputStream(), true, configuration.getVariables(), new XMLMapperEntityResolver());
            XNode context = xPathParser.evalNode("/mapper");
            String namespace = context.getStringAttribute("namespace");
            Field field = MapperRegistry.class.getDeclaredField("knownMappers");
            field.setAccessible(true);
            Map mapConfig = (Map) field.get(configuration.getMapperRegistry());
            mapConfig.remove(Resources.classForName(namespace));
            loadedResourcesSet.remove(resource.toString());
            configuration.getCacheNames().remove(namespace);
            cleanParameterMap(context.evalNodes("/mapper/parameterMap"), namespace);
            cleanResultMap(context.evalNodes("/mapper/resultMap"), namespace);
            cleanKeyGenerators(context.evalNodes("insert|update"), namespace);
            cleanSqlElement(context.evalNodes("/mapper/sql"), namespace);
            cleanStatement(context.evalNodes("select|insert|update|delete"), namespace);
            XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(resource.getInputStream(), configuration, // 注入的sql先不进行处理了
                    resource.toString(), configuration.getSqlFragments());
            xmlMapperBuilder.parse();
            log.debug("refresh: '" + resource + "', success!");
        } catch (IOException e) {
            log.error("Refresh IOException :" + e.getMessage());
        } finally {
            ErrorContext.instance().reset();
        }
    }

    private void cleanStatement(List<XNode> list, String namespace) {
        try {
            for (XNode parameterMapNode : list) {
                String id = namespace + "." + parameterMapNode.getStringAttribute("id");
                remove(id);
                remove(id + COUNT_SUFFIX);
                remove(id + "!selectKey");
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private void remove(String statementName) {
        if (configuration.hasStatement(statementName)) {
            configuration.getMappedStatements().remove(configuration.getMappedStatement(statementName));
            configuration.getMappedStatementNames().remove(statementName);
        }
    }

    /**
     * 清理parameterMap
     *
     * @param list
     * @param namespace
     */
    private void cleanParameterMap(List<XNode> list, String namespace) {
        for (XNode parameterMapNode : list) {
            String id = parameterMapNode.getStringAttribute("id");
            configuration.getParameterMaps().remove(namespace + "." + id);
        }
    }

    /**
     * 清理resultMap
     *
     * @param list
     * @param namespace
     */
    private void cleanResultMap(List<XNode> list, String namespace) {
        for (XNode resultMapNode : list) {
            String id = resultMapNode.getStringAttribute("id", resultMapNode.getValueBasedIdentifier());
            configuration.getResultMapNames().remove(id);
            configuration.getResultMapNames().remove(namespace + "." + id);
            clearResultMap(resultMapNode, namespace);
        }
    }

    private void clearResultMap(XNode xNode, String namespace) {
        for (XNode resultChild : xNode.getChildren()) {
            if ("association".equals(resultChild.getName()) || "collection".equals(resultChild.getName()) || "case".equals(resultChild.getName())) {
                if (resultChild.getStringAttribute("select") == null) {
                    configuration.getResultMapNames().remove(resultChild.getStringAttribute("id", resultChild.getValueBasedIdentifier()));
                    configuration.getResultMapNames().remove(namespace + "." + resultChild.getStringAttribute("id", resultChild.getValueBasedIdentifier()));
                    if (resultChild.getChildren() != null && !resultChild.getChildren().isEmpty()) {
                        clearResultMap(resultChild, namespace);
                    }
                }
            }
        }
    }

    /**
     * 清理selectKey
     *
     * @param list
     * @param namespace
     */
    private void cleanKeyGenerators(List<XNode> list, String namespace) {
        for (XNode context : list) {
            String id = context.getStringAttribute("id");
            configuration.getKeyGeneratorNames().remove(id + SelectKeyGenerator.SELECT_KEY_SUFFIX);
            configuration.getKeyGeneratorNames().remove(namespace + "." + id + SelectKeyGenerator.SELECT_KEY_SUFFIX);
        }
    }

    /**
     * 清理sql节点缓存
     *
     * @param list
     * @param namespace
     */
    private void cleanSqlElement(List<XNode> list, String namespace) {
        for (XNode context : list) {
            String id = context.getStringAttribute("id");
            configuration.getSqlFragments().remove(id);
            configuration.getSqlFragments().remove(namespace + "." + id);
        }
    }
}
