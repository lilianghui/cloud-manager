package ${controllerNamespace};

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ${config.controllerExtend};
import ${entityNamespace}.${table.entityName};
import ${serviceNamespace}.${table.iServiceName};

<#assign entityName="${table.entityName?uncap_first}">
<#assign serviceName="${(table.serviceImplName?uncap_first)?replace('Impl','')}">

@RestController
@RequestMapping("${entityName}")
public class ${table.actionName} extends ${config.controllerExtend?substring(config.controllerExtend?last_index_of(".")+1)}<${table.iServiceName}>{

	/**
	 * 跳转首页视图
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping("view")
	public ModelAndView view(HttpServletRequest request) {
		return getNavigationbarModelAndView(request, "${entityName}/${entityName}");
	}

}