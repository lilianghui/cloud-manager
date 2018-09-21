package ${config.servicePath};

import ${config.serviceExtend};
import ${config.entityPath}.${table.entityName};

public interface ${table.iServiceName} extends ${config.serviceExtend?substring(config.serviceExtend?last_index_of(".")+1)}<${table.entityName}>{

}