package com.gtafe.data.center.metadata.clazz.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import com.gtafe.framework.base.vo.BaseVO;



/**
 * 
 * ClassName: ClazzBySubsetVO <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason: TODO ADD REASON(可选). <br/> 
 * date: 2017年7月18日 上午9:47:38 <br/> 
 * 
 * @author wuhu.zhu 
 * @version  
 * @since JDK 1.7
 */  
@ApiModel
public class ClazzBySubsetVO extends BaseVO  {

	/** 
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么). 
	 * @since JDK 1.7
	 */  
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "类编号", required = true)
	private String classCode;
	@ApiModelProperty(value = "类名称", required = true)
	private String className;
	@ApiModelProperty(value = "类描述", required = false)
	private String description;
	
	public String getClassCode() {
		return classCode;
	}
	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
