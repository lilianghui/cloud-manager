package com.lilianghui.framework.core.utils;

import java.util.Properties;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class PropertiesFactoryBean implements InitializingBean, FactoryBean<Properties> {
	private Properties properties;

	@Override
	public Properties getObject() throws Exception {
		return properties;
	}

	@Override
	public Class<?> getObjectType() {
		return Properties.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

}
