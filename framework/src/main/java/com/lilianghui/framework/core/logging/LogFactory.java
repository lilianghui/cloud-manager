package com.lilianghui.framework.core.logging;

import java.lang.reflect.Constructor;


public final class LogFactory {
	private static Constructor<? extends Log> logConstructor;

	public static Log getLog(Object object) {
		String logger = null;
		try {
			if (object instanceof Class<?>) {
				logger = ((Class<?>) object).getName();
			} else if (object instanceof String) {
				logger = (String) object;
			} else {
				logger = object.getClass().getName();
			}
			if (logConstructor == null) {
				setImpl(Slf4jImpl.class);
			}
			return logConstructor.newInstance(logger);
		} catch (Throwable t) {
			throw new RuntimeException("Error creating logger for logger " + logger + ".  Cause: " + t, t);
		}
	}

	public static void setImpl(Class<? extends Log> clazz) {
		if (logConstructor == null) {
			synchronized (LogFactory.class) {
				if (logConstructor == null) {
					try {
						logConstructor = clazz.getConstructor(String.class);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

}
