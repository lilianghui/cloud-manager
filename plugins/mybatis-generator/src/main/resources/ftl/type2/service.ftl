package ${serviceNamespace};

import ${config.serviceExtend};
import ${entityNamespace}.${table.entityName};

public interface ${table.iServiceName} extends ${config.serviceExtend?substring(config.serviceExtend?last_index_of(".")+1)}<${table.entityName}>{

}