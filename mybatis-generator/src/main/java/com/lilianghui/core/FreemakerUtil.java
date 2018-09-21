package com.lilianghui.core;

import java.io.StringWriter;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class FreemakerUtil {
	private static Configuration cfg = new Configuration();

	static {
		cfg.setClassForTemplateLoading(FreemakerUtil.class, "/ftl");
		cfg.setClassicCompatible(true);// 设置属性
		cfg.setNumberFormat("#");
		cfg.setDefaultEncoding("utf-8");
		cfg.setSharedVariable("directive", new FreemarkerDirective());
	}

	public static String parse(Map<String, Object> root, String fileName) throws Exception {
		StringWriter stringWriter = new StringWriter();
		Template t = cfg.getTemplate(fileName); // 最关键在这里，不使用与文件相关的Writer
		t.process(root, stringWriter);
		return stringWriter.toString();
	}
}
