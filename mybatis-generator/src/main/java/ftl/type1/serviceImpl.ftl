package ${config.servicePath}.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.walkiesoft.framework.dao.IBaseDao;
import ${config.serviceImplExtend};
import ${config.servicePath}.${table.iServiceName};
import ${config.daoPath}.${table.iDaoName};
import ${config.entityPath}.${table.entityName};

<#assign daoName="${table.iDaoName}">
<#if table.iDaoName?starts_with("I")>
<#assign daoName="${table.iDaoName?substring(1)}">
</#if>

@Service
public class ${table.serviceImplName} extends ${config.serviceImplExtend?substring(config.serviceImplExtend?last_index_of(".")+1)}<${table.entityName}> implements ${table.iServiceName}{
	@Resource
	private ${table.iDaoName} ${daoName?uncap_first};

	@Override
	public IBaseDao<${table.entityName}> getDao() {
		return ${daoName?uncap_first};
	}
}