package entity;

import com.lilianghui.framework.core.poi.excel.CellAttribute;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 极速农贸入库导入 <br>
 * Copyrignt 2018
 * 
 * @author dsy
 * @version 创建时间:2018年8月14日 上午10:16:59
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = false)
public class InstockQuickTraceVO {

	/** 供应方（*匹配系统内已有供应商） */
	@CellAttribute(cellIndex = 1,title = "供应方（*匹配系统内已有供应商）",allowBlank = false,blankTemplate = "第#{rowIndex}行第#{cellIndex}列供应方不能为空")
	private String supplier_entp_id;
	/** 经营户（*匹配系统内已有经营户） */
	@CellAttribute(cellIndex = 2,title = "经营户（*匹配系统内已有经营户）",allowBlank = false,blankTemplate = "第#{rowIndex}行第#{cellIndex}列经营户不能为空")
	private String entp_id;
	/** 入库时间（格式：YYYY-MM-DD hh:mm:ss） */
	@CellAttribute(cellIndex = 3,title = "入库时间（格式：YYYY-MM-DD hh:mm:ss）")
	private Date instock_date;
	/** 操作员（*匹配系统内已有员工） */
	@CellAttribute(cellIndex = 4,title = "操作员（*匹配系统内已有员工）",allowBlank = false,blankTemplate = "第#{rowIndex}行第#{cellIndex}列操作员不能为空")
	private String employee_id;
	/** 配送车辆 */
	@CellAttribute(cellIndex = 5,title = "配送车辆")
	private String plate_number;
	/** 送货人 */
	@CellAttribute(cellIndex = 6,title = "送货人")
	private String deliveryman;
	/** 商品编号(*匹配系统内已有SKU) */
	@CellAttribute(cellIndex = 7,title = "商品编号(*匹配系统内已有SKU)",allowBlank = false,blankTemplate = "第#{rowIndex}行第#{cellIndex}列商品编号不能为空")
	private String product_sku;
	/** 产品名称 */
	@CellAttribute(cellIndex = 8,title = "产品名称")
	private String product_name;
	/** 商务部追溯码 */
	@CellAttribute(cellIndex = 9,title = "商务部追溯码")
	private String other_id;
	/** 动物产品疫检合格证 */
	@CellAttribute(cellIndex = 10,title = "动物产品疫检合格证")
	private String animal_cert_code;
	/** 数量 */
	@CellAttribute(cellIndex = 11,defaultValue = "0",title = "数量")
	private BigDecimal quantity;
	/** 单价 */
	@CellAttribute(cellIndex = 12,defaultValue = "0",title = "单价")
	private BigDecimal price;
}
