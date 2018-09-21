package com.lilianghui.core;

import com.lilianghui.entity.Config;
import com.lilianghui.entity.Table;
import com.lilianghui.entity.Tpl;

import java.util.Map;


public class Generator2 extends AbstractGenerator {

	public Generator2(Config config, Table table) throws Exception {
		super(config, table);
	}

	public static final String SERVICE_FTL = "type2/service.ftl";
	public static final String SERVICE_IMPL_FTL = "type2/serviceImpl.ftl";
	public static final String CONTROLLER_FTL = "type2/controller-ajax.ftl";
	public static final String ENTITY_FTL = "type2/entity.ftl";
	public static final String DAO_FTL = "type2/dao.ftl";
	public static final String MAPPER_FTL = "type2/mapper.xml";

	@Override
	public Tpl getTpl() {
		Tpl tpl = new Tpl();
		tpl.setControllerFtl(CONTROLLER_FTL);
		tpl.setServiceFtl(SERVICE_FTL);
		tpl.setServiceImplFtl(SERVICE_IMPL_FTL);
		tpl.setDaoFtl(DAO_FTL);
		tpl.setEntityFtl(ENTITY_FTL);
		tpl.setMapperFtl(MAPPER_FTL);
		return tpl;
	}

	@Override
	public Map<String, Object> getArgs(Config config, Table table) {
		return null;
	}
}
