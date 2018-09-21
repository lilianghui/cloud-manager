package com.lilianghui.framework.core.utils;

import java.util.HashMap;
import java.util.Map;

public class ByteClassLoader extends ClassLoader {
	private Map<String, byte[]> classMap = new HashMap<>();

	public ByteClassLoader(ClassLoader parent, Map<String, byte[]> classMap) {
		super(parent);
		this.classMap = classMap;
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		byte[] bytes = classMap.get(name);
		return defineClass(name, bytes, 0, bytes.length);
	}

}
