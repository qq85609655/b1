package com.gtafe.data.center.metadata.subset.vo;

import com.gtafe.framework.base.vo.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 
 * ClassName: SubsetVO <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason: TODO ADD REASON(可选). <br/> 
 * date: 2017年7月18日 上午9:47:38 <br/> 
 * 
 * @author liquan.feng 
 * @version  
 * @since JDK 1.7
 */  
@ApiModel
public class SubsetVO extends BaseVO {

	/** 
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么). 
	 * @since JDK 1.7
	 */  
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "父集代码", required = true)
	private String parentCode;
	
	@ApiModelProperty(value = "子集代码", required = true)
	private String code;
	
	@ApiModelProperty(value = "子集中文名称", required = true)
	private String codeName;

	@ApiModelProperty(value = "子集描述", required = true)
	private String description;

	@ApiModelProperty(value = "标准来源ID", required = true)
	private int sourceid;
	
	public String getParentCode() {
		return parentCode;
	}
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCodeName() {
		return codeName;
	}
	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getSourceid() {
		return sourceid;
	}
	public void setSourceid(int sourceid) {
		this.sourceid = sourceid;
	}
	
}
