package ${config.controllerPath};

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.walkiesoft.framework.entity.Constant;
import com.walkiesoft.framework.entity.MessageHandler;
import com.walkiesoft.framework.util.Ctrls;
import ${config.controllerExtend};
import ${config.entityPath}.${table.entityName};
import ${config.servicePath}.${table.iServiceName};

<#assign entityName="${table.entityName?uncap_first}">
<#assign serviceName="${(table.serviceImplName?uncap_first)?replace('Impl','')}">

@Controller
@RequestMapping("${entityName}")
public class ${table.actionName}  extends ${config.controllerExtend?substring(config.controllerExtend?last_index_of(".")+1)}{
	private static Logger log = Logger.getLogger(${table.actionName}.class);

	@Resource
	private ${table.iServiceName} ${serviceName};

	@RequestMapping("list")
	public ModelAndView list(${table.entityName} ${entityName}) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("list", ${serviceName}.search(${entityName}));
		} catch (Exception e) {
			processException(log,e,mv);
		}
		mv.setViewName("${entityName}");
		return mv;
	}

	@ResponseBody
	@RequestMapping("getById")
	public MessageHandler<?> getById(String id) {
		try {
			return MessageHandler.<${table.entityName}> getOk().setObject(
					${serviceName}.getById(id));
		} catch (Exception e) {
			processException(log,e,null);
			return MessageHandler.getError(e);
		}
	}

	@RequestMapping("deleteByIds")
	public ModelAndView deleteByIds(String[] selectIDs,
			RedirectAttributes attributes) {
		ModelAndView mv = new ModelAndView();
		try {
			if (ArrayUtils.isNotEmpty(selectIDs)) {
				${serviceName}.deleteByIds(selectIDs);
			}
		} catch (Exception e) {
			processException(log,e,mv);
		}
		mv.setViewName("redirect:/${entityName}/list.shtml");
		return mv;
	}

	@RequestMapping("merge")
	public ModelAndView merge(@Valid ${table.entityName} ${entityName}, BindingResult result,
			RedirectAttributes attributes) {
		ModelAndView mv = new ModelAndView();
		try {
			processBindingResult(result);
			${serviceName}.saveOrUpdate(${entityName});
		} catch (Exception e) {
			processException(log,e,mv);
		}
		mv.setViewName("redirect:/${entityName}/list.shtml");
		return mv;
	}
}