package ${config.entityPath};

import ${config.entityExtend};
${importDate}
<#if (many?size>0)>import java.util.List;
import java.util.ArrayList;
</#if>
${table.tableComment}
public class ${table.entityName} extends ${config.entityExtend?substring(config.entityExtend?last_index_of(".")+1)}{

<#list table.list as t>
	private ${t.javaType} ${t.propertyName};${t.comments}
</#list>

<#list one as t>
	private ${t.entityName} ${t.entityName?uncap_first};
</#list>

<#list many as t>
	private List<${t.entityName}> ${t.entityName?uncap_first}s=new ArrayList<${t.entityName}>();
</#list>
		
<#list table.list as t>
	public void set${t.propertyName?cap_first}(${t.javaType} ${t.propertyName}){
		this.${t.propertyName}=${t.propertyName};
	}
	
	public ${t.javaType} get${t.propertyName?cap_first}(){
		return this.${t.propertyName};
	}
	
</#list>
<#list one as t>
	public void set${t.entityName?cap_first}(${t.entityName} ${t.entityName?uncap_first}){
		this.${t.entityName?uncap_first}=${t.entityName?uncap_first};
	}
	
	public ${t.entityName?cap_first} get${t.entityName?cap_first}(){
		return this.${t.entityName?uncap_first};
	}
	
</#list>
<#list many as t>
	public void set${t.entityName?cap_first}s(List<${t.entityName}> ${t.entityName?uncap_first}s){
		this.${t.entityName?uncap_first}s=${t.entityName?uncap_first}s;
	}
	
	public List<${t.entityName?cap_first}> get${t.entityName?cap_first}s(){
		return this.${t.entityName?uncap_first}s;
	}
	
</#list>

}