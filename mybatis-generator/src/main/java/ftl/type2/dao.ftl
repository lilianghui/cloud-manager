package ${daoNamespace};

import ${config.daoExtend};
import ${entityNamespace}.${table.entityName};

public interface ${table.iDaoName} extends ${config.daoExtend?substring(config.daoExtend?last_index_of(".")+1)}<${table.entityName}>{

}