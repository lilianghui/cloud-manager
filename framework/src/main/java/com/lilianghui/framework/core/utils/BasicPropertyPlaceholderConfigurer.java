package com.lilianghui.framework.core.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

public class BasicPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer implements BeanDefinitionRegistryPostProcessor {
	private static Map<String, Object> propsMap = new HashMap<String, Object>();
	private static final String PATTERN_STRING = "\\$\\{((.*?))\\}";
	private static final Pattern PATTERN = Pattern.compile(PATTERN_STRING);

	private Set<String> encryptPropNames = new HashSet<>();// 保存加密的属性字段
	private PropertyPlaceholderDecrypt placeholderDecrypt;

	public BasicPropertyPlaceholderConfigurer() {
		placeholderDecrypt = new PropertyPlaceholderDecryptImpl();
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
	}

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		DefaultListableBeanFactory a = (DefaultListableBeanFactory) registry;
		super.postProcessBeanFactory(a);
	}

	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
		resolveVarProperty(props);
		for (Object object : props.keySet()) {
			String key = object.toString();
			propsMap.put(key, props.get(key));
		}
		super.processProperties(beanFactoryToProcess, props);
	}

	@Override
	protected String convertProperty(String propertyName, String propertyValue) {
		if (encryptPropNames.contains(propertyName)) {
			return placeholderDecrypt.decrypt(propertyName, propertyValue);
		}
		return super.convertProperty(propertyName, propertyValue);
	}

	private void resolveVarProperty(Properties props) {
		Map<Object, Object> replace = new HashMap<>();
		Map<String, Object> temp = new HashMap<>();
		for (Entry<Object, Object> m : props.entrySet()) {
			if (m.getValue() instanceof String) {
				String val = (String) m.getValue();
				Matcher matcher = PATTERN.matcher(val);
				boolean find = false;
				while (matcher.find()) {
					find = true;
					String[] splits = matcher.group(1).split(":");
					Object value = props.get(splits[0]);
					if (value == null) {
						if (splits.length > 1) {
							value = splits[1];
						} else {
							value = System.getProperty(splits[0]);
						}
					}
					temp.put(matcher.group(0), value);
				}
				if (find) {
					for (Entry<String, Object> e : temp.entrySet()) {
						val = val.replace(e.getKey(), e.getValue() == null ? "" : (String) e.getValue());
					}
					replace.put(m.getKey(), val);
				}
			}
		}
		props.putAll(replace);
	}

	public static String getStringProperty(String key) {
		Object val = getProperty(key);
		if (val == null) {
			return null;
		}
		return String.valueOf(val);
	}

	public static int getIntProperty(String key) {
		return Integer.valueOf(getStringProperty(key));
	}

	public static boolean getBooleanProperty(String key) {
		String value = getStringProperty(key);
		return "true".equalsIgnoreCase(value) || "1".equalsIgnoreCase(value) || "Y".equalsIgnoreCase(value);
	}

	public static Object getProperty(String key) {
		return propsMap.get(key);
	}

	public void setEncryptPropNames(Set<String> encryptPropNames) {
		this.encryptPropNames = encryptPropNames;
	}

	public void setPlaceholderDecrypt(PropertyPlaceholderDecrypt placeholderDecrypt) {
		this.placeholderDecrypt = placeholderDecrypt;
	}

	public static interface PropertyPlaceholderDecrypt {
		public String decrypt(String propertyName, String propertyValue);
	}

	public static class PropertyPlaceholderDecryptImpl implements PropertyPlaceholderDecrypt {

		@Override
		public String decrypt(String propertyName, String propertyValue) {
			return propertyValue;
		}

	}

}
