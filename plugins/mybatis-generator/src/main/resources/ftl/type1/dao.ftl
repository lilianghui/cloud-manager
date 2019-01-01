package ${config.daoPath};

import ${config.daoExtend};
import ${config.entityPath}.${table.entityName};

public interface ${table.iDaoName} extends ${config.daoExtend?substring(config.daoExtend?last_index_of(".")+1)}<${table.entityName}>{

}