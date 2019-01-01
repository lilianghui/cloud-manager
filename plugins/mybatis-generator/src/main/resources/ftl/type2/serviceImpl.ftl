package ${serviceImplNamespace};

import org.springframework.stereotype.Service;
import ${config.serviceImplExtend};
import ${serviceNamespace}.${table.iServiceName};
import ${daoNamespace}.${table.iDaoName};
import ${entityNamespace}.${table.entityName};

<#assign daoName="${table.iDaoName}">
<#if table.iDaoName?starts_with("IIIIII")>
<#assign daoName="${table.iDaoName?substring(1)}">
</#if>
@Service
public class ${table.serviceImplName} extends ${config.serviceImplExtend?substring(config.serviceImplExtend?last_index_of(".")+1)}<${table.entityName},${table.iDaoName}> implements ${table.iServiceName}{

}