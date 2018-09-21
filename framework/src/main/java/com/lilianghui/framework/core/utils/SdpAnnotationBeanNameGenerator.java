package com.lilianghui.framework.core.utils;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;

/**
 * <context:component-scan base-package="*" name-generator=""> Spring扫描类时名称定义
 * 
 * @author Administrator
 * 
 */
public class SdpAnnotationBeanNameGenerator extends AnnotationBeanNameGenerator {

	protected String buildDefaultBeanName(BeanDefinition definition) {
		String name = super.buildDefaultBeanName(definition);
		if (name.toLowerCase().endsWith("impl")) {
			name = name.substring(0, name.length() - 4);
		}
		return name;
	}
}
