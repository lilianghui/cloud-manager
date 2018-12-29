package com.lilianghui.spring.starter.netty.rpc.server;

import com.lilianghui.spring.starter.annotation.EnableNettyRpcClients;
import com.lilianghui.spring.starter.annotation.NettyRpcClient;
import com.lilianghui.spring.starter.netty.rpc.entity.NettyRpcProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

@Slf4j
public class NettyRpcClientsRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware {

    private ResourceLoader resourceLoader;
    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(EnableNettyRpcClients.class.getName()));
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry) {
            @Override
            protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
                Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
                if (beanDefinitions.isEmpty()) {
                    this.logger.warn("No Netty Interface was found in '" + Arrays.toString(basePackages) + "' package. Please check your configuration.");
                } else {
                    NettyRpcClientsRegistrar.this.processBeanDefinitions(beanDefinitions);
                }

                return beanDefinitions;
            }

            @Override
            protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
            }
        };
        if (this.resourceLoader != null) {
            scanner.setResourceLoader(this.resourceLoader);
        }
        scanner.addIncludeFilter(new AnnotationTypeFilter(NettyRpcClient.class));
        scanner.addIncludeFilter((metadataReader, metadataReaderFactory) -> true);
        String[] basePackages = attributes.getStringArray("basePackages");
        scanner.scan(basePackages);
    }

    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
        Iterator iterator = beanDefinitions.iterator();

        while (iterator.hasNext()) {
            BeanDefinitionHolder holder = (BeanDefinitionHolder) iterator.next();
            GenericBeanDefinition definition = (GenericBeanDefinition) holder.getBeanDefinition();
            if (this.log.isDebugEnabled()) {
                this.log.debug("Creating NettyRpcClientFactoryBean with name '" + holder.getBeanName() + "' and '" + definition.getBeanClassName() + "' syncInterface");
            }
            //通过构造函数传入接口类型, 以保留原接口类型
            definition.getConstructorArgumentValues().addGenericArgumentValue(definition.getBeanClassName());
            definition.setBeanClass(NettyRpcClientFactoryBean.class);
            definition.setAutowireMode(Autowire.BY_TYPE.value());
        }
    }
}
