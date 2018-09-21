package com.lilianghui.entity;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class NameSpace {
	private String serviceNamespace;
	private String serviceImplNamespace;
	private String entityNamespace;
	private String controllerNamespace;
	private String daoNamespace;

	public NameSpace(Config config, Table table) {
		this.serviceNamespace=getNamespace(config, table,true, "service", config.getServicePath());
		this.serviceImplNamespace=getNamespace(config, table,true, "service.impl",config.getServicePath()+ ".impl");
		this.controllerNamespace=getNamespace(config, table,true, "controller", config.getControllerPath());
		this.entityNamespace=getNamespace(config, table,false, "entity", config.getEntityPath());
		this.daoNamespace=getNamespace(config, table,false, "dao", config.getDaoPath());
	}

	public Map<String, String> getNameSpaceMap() {
		Map<String, String> map=new HashMap<>();
		map.put("serviceNamespace",serviceNamespace);
		map.put("serviceImplNamespace", serviceImplNamespace);
		map.put("controllerNamespace", controllerNamespace);
		map.put("entityNamespace", entityNamespace);
		map.put("daoNamespace", daoNamespace);
		return map;
	}
	
	private String getNamespace(Config config, Table table, boolean flag,
								String type, String namespace) {
		String namespace2 = namespace;
		if (config.isModular() && flag) {
			namespace2 = config.getModularPath() + "."
					+ table.getEntityName().toLowerCase() + "." + type;
		}
		// NameSpace nameSpace2 = new NameSpace();
		// nameSpace2.setNamespace(namespace2);
		// nameSpace2.setPath(
		// namespace2.replace(".", File.separator) + File.separator);
		return namespace2;
	}

	public String getServiceNamespacePath() {
		return getPath(serviceNamespace);
	}

	public String getServiceImplNamespacePath() {
		return getPath(serviceImplNamespace);
	}

	public String getEntityNamespacePath() {
		return getPath(entityNamespace);
	}

	public String getControllerNamespacePath() {
		return getPath(controllerNamespace);
	}

	public String getDaoNamespacePath() {
		return getPath(daoNamespace);
	}
	
	private String getPath(String namespace) {
		return namespace.replace(".", File.separator) + File.separator;
	}
	
}
