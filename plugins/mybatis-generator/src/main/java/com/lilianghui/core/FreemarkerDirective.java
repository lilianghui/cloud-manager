package com.lilianghui.core;

import freemarker.core.Environment;
import freemarker.template.*;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import static freemarker.template.ObjectWrapper.DEFAULT_WRAPPER;

public class FreemarkerDirective implements TemplateDirectiveModel {
	/**
	 * 输出参数：列表数据
	 */
	public static final String OUT_LIST = "tag_list";

	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {
		Writer out = env.getOut();
		boolean html = getBooleanParameter(params, "html", true);
		String method = getStringParameter(params, "method", "list2String");
		Object object = null;
		try {
			object = this.getClass().getDeclaredMethod(method, Map.class).invoke(this, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (html) {
			out.write(String.valueOf(object));
		} else {
			env.setVariable(OUT_LIST, DEFAULT_WRAPPER.wrap(object));
		}
		if (body != null) {
			body.render(out);
		}
	}

//	private String getAnnotated(Map params) throws Exception {
//		StringBuffer buffer=new StringBuffer();
//		StringModel obj = (StringModel)params.get( "field");
//		Column column=(Column) obj.getAdaptedObject(Column.class);
//		String name=column.getPropertyName().replace("_", "");
//		if ((name.equalsIgnoreCase("createdate")||name.equalsIgnoreCase("createtime"))&&"date".equalsIgnoreCase(column.getJavaType())) {
//			buffer.append("\t@Updatable\n");
//			buffer.append("\t@ValueStyle(\""+OGNL+"\")\n");
//		}else if ((name.equalsIgnoreCase("updatedate")||name.equalsIgnoreCase("updatetime"))&&"date".equalsIgnoreCase(column.getJavaType())) {
//			buffer.append("\t@ValueStyle(\""+OGNL+"\")\n");
//		}
//		return buffer.toString();
//	}

	public String checked(Map params) {
		String source = getStringParameter(params, "source");
		String target = getStringParameter(params, "target");
		if (source == null || target == null) {
			return "";
		} else if (source.equalsIgnoreCase(target)) {
			return "selected=\"selected\"";
		}
		return "";
	}

	private Integer getIntParameter(Map params, String parameter) {
		return getIntParameter(params, parameter, null);
	}

	private Integer getIntParameter(Map params, String parameter, Integer defaultValue) {
		String val = getStringParameter(params, parameter);
		if (val == null) {
			return defaultValue;
		}
		return Integer.parseInt(val);
	}

	private boolean getBooleanParameter(Map params, String parameter) throws TemplateModelException {
		return getBooleanParameter(params, parameter, false);

	}

	private boolean getBooleanParameter(Map params, String parameter, boolean defaultValue)
			throws TemplateModelException {
		Object val = getParams(params, parameter);
		if (val == null) {
			return defaultValue;
		}
		String value = val.toString();
		return value.equals("1") || value.equals("true");
	}

	private String getStringParameter(Map params, String parameter) {
		return getStringParameter(params, parameter, null);
	}

	private String getStringParameter(Map params, String parameter, String defaultValue) {
		Object val = getParams(params, parameter);
		if (val == null) {
			return defaultValue;
		}
		return val.toString();
	}

	private Object getParams(Map params, String parameter) {
		Object val = null;
		try {
			val = params.get(parameter);
			if (val == null) {
				return null;
			}
			if (val instanceof TemplateBooleanModel) {
				return ((TemplateBooleanModel) val).getAsBoolean();
			} else if (val instanceof SimpleScalar) {
				return ((SimpleScalar) val).getAsString();
			}
		} catch (Exception e) {
		}
		return val;
	}

}
