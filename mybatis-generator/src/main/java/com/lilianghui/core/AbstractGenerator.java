package com.lilianghui.core;

import com.lilianghui.entity.*;
import com.lilianghui.io.FileManager;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.*;

public abstract class AbstractGenerator extends NameSpace {
	protected Config config;
	protected Table table;
	private Tpl tpl;
	private Map<String, Object> root = new HashMap<String, Object>();

	public AbstractGenerator(Config config, Table table) throws Exception {
		super(config, table);
		this.config = config;
		this.table = table;
	}

	public final void generator() throws Exception {
		this.tpl = getTpl();
		Map<String, Object> args = getArgs(config, table);
		if (args != null) {
			root.putAll(args);
		}
		root.putAll(getNameSpaceMap());
		root.put("table", table);
		root.put("config", config);
		_generator();
	}

	public abstract Tpl getTpl();

	public abstract Map<String, Object> getArgs(Config config, Table table);

	private void _generator() throws Exception {
		if (config.isController()) {
			buildController();
		}
		if (config.isEntity()) {
			buildEntity();
		}
		if (config.isService()) {
			buildService();
			buildServiceImpl();
		}
		if (config.isDao()) {
			buildIDao();
		}
		if (config.isMapConfig()) {
			bulidConfig();
		}
		if (config.isFrameWork()) {
			FileManager.fileCopy(config);
		}
	}

	public void buildService() throws Exception {
		String string = FreemakerUtil.parse(root, tpl.getServiceFtl());
		FileManager.createFile(config, getServiceNamespacePath() + table.getiServiceName() + ".java", string);
	}

	public void buildServiceImpl() throws Exception {
		String string = FreemakerUtil.parse(root, tpl.getServiceImplFtl());
		FileManager.createFile(config, getServiceImplNamespacePath() + table.getServiceImplName() + ".java", string);
	}

	public void buildController() throws Exception {
		String string = FreemakerUtil.parse(root, tpl.getControllerFtl());
		FileManager.createFile(config, getControllerNamespacePath() + table.getActionName() + ".java", string);
	}

	/**
	 * 生成实体类
	 * 
	 * @throws Exception
	 */
	public void buildEntity() throws Exception {
//		String importUpdatable="tk.mybatis.mapper.annotation.Updatable";
		String importValueStyle=config.getImportValueStyle();
		Set<String> imports=new HashSet<>();
		imports.add("import "+importValueStyle+";");
		imports.add("import "+importValueStyle+".ValueType;");
		for (Column column : table.getList()) {
			if ("Date".equalsIgnoreCase(column.getJavaType())) {
				imports.add("import java.util.Date;");
			}
			String name=column.getPropertyName().replace("_", "");
			if (((name.equalsIgnoreCase("createdate")||name.equalsIgnoreCase("createtime"))&&"date".equalsIgnoreCase(column.getJavaType()))||((name.equalsIgnoreCase("updatedate")||name.equalsIgnoreCase("updatetime"))&&"date".equalsIgnoreCase(column.getJavaType()))) {
				imports.add("import "+importValueStyle+";");
			}
		}
		
		if(table.getPkColumn().isIdentity()&&"String".equals(table.getPkColumn().getJavaType())){
			imports.add("import javax.persistence.GenerationType;");
		}
		root.put("imports", StringUtils.join(imports, "\n"));

		List<Table> one = new ArrayList<Table>();
		for (TableRef ref : table.getOne()) {
			Table tab = config.getMap().get(ref.getOneTableName());
			one.add(tab);
		}
		List<Table> many = new ArrayList<Table>();
		for (TableRef ref : table.getMany()) {
			Table tab = config.getMap().get(ref.getManyTableName());
			many.add(tab);
		}

		root.put("one", one);
		root.put("many", many);
		String string = FreemakerUtil.parse(root, tpl.getEntityFtl());

		FileManager.createFile(config, getEntityNamespacePath()+table.getEntityName()+ ".java",
				string.toString());
	}
	public void buildIDao() throws Exception {
		String string = FreemakerUtil.parse(root, tpl.getDaoFtl());
		FileManager.createFile(config, getDaoNamespacePath() + table.getiDaoName() + ".java", string);
	}

	public void bulidConfig() throws Exception {
		Map<String, Table> collection = new HashMap<String, Table>();
		List<Table> ones = new ArrayList<Table>();
		for (TableRef ref : table.getOne()) {
			Table tab = config.getMap().get(ref.getOneTableName());
			tab.setRef(ref);
			ones.add(tab);
			collection.put(tab.getTable(), tab);
		}
		List<Table> manys = new ArrayList<Table>();
		for (TableRef ref : table.getMany()) {
			Table tab = config.getMap().get(ref.getManyTableName());
			tab.setRef(ref);
			manys.add(tab);
			collection.put(tab.getTable(), tab);
		}
		//
		root.put("ones", ones);
		root.put("manys", manys);
		root.put("collection", collection.values());
		if (config.isSeq()) {
			String prefix = "T";
			table.setAlias(prefix);
			int index = 1;
			for (Table tab : collection.values()) {
				tab.setAlias(prefix + (index++));
			}
		}
		for (Table tab : collection.values()) {
			Table t = collection.get(tab.getRef().getOneTableName());
			if (t == null && tab != table) {
				t = table;
			}
			if (t != null) {
				tab.setPrevAlias(t.getAlias());
			}
		}
		root.put("isCollection", config.isCollection() && (manys.size() > 0 || ones.size() > 0));
		String string = FreemakerUtil.parse(root, tpl.getMapperFtl());
		String path = config.getMapperPath();
		FileManager.createFile(config, path.replace(".", File.separator) + File.separator + table.getEntityName() + "Mapper.xml", string.toString());
	}
}
