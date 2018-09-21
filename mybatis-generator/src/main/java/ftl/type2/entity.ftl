package ${entityNamespace};

import ${config.entityExtend};
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
${imports}
<#if (many?size>0)>import java.util.List;
import java.util.ArrayList;
</#if>
<#assign databaseType="${config.databaseType?upper_case}">

${table.tableComment}
@Table(name = "${table.table}")
public class ${table.entityName} extends ${config.entityExtend?substring(config.entityExtend?last_index_of(".")+1)}{

<#list table.list as t>
	<#if t.propertyName==table.pkColumn.propertyName>
	@Id
	<#if table.pkColumn.identity><#if 'Integer'==table.pkColumn.javaType>
    @GeneratedValue(generator = "JDBC")<#elseif 'String'==table.pkColumn.javaType>	@GeneratedValue(strategy = GenerationType.IDENTITY)</#if>
	</#if>
	</#if>
	${t.annotated}@Column(name = "${t.columnName}"${t.updatable})
	private ${t.javaType} ${t.propertyName};${t.comments}
	
</#list>
<#if isCollection>
<#list one as t>
	private ${t.entityName} ${t.entityName?uncap_first};
</#list>

<#list many as t>
	private List<${t.entityName}> ${t.entityName?uncap_first}s=new ArrayList<${t.entityName}>();
</#list>
</#if>

<#list table.list as t>
	public void set${t.propertyName?cap_first}(${t.javaType} ${t.propertyName}){
		this.${t.propertyName}=${t.propertyName};
	}
	
	public ${t.javaType} get${t.propertyName?cap_first}(){
		return this.${t.propertyName};
	}
	
</#list>
<#if isCollection>
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
</#if>
}