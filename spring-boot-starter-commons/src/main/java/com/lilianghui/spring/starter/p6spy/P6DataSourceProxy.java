package com.lilianghui.spring.starter.p6spy;

import com.p6spy.engine.spy.P6DataSource;
import com.p6spy.engine.spy.option.SpyDotProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.FactoryBean;

import javax.sql.DataSource;

public class P6DataSourceProxy implements FactoryBean<DataSource> {
	private boolean debug;
	private DataSource dataSource;
	private String p6spyConfigFile;

	@Override
	public DataSource getObject() throws Exception {
		if (debug) {
			if (StringUtils.isNotBlank(p6spyConfigFile)) {
				System.setProperty(SpyDotProperties.OPTIONS_FILE_PROPERTY, p6spyConfigFile);
			}
			return new P6DataSource(this.dataSource);
		} else {
			return this.dataSource;
		}
	}

	@Override
	public Class<?> getObjectType() {
		return DataSource.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void setP6spyConfigFile(String p6spyConfigFile) {
		this.p6spyConfigFile = p6spyConfigFile;
	}

}
