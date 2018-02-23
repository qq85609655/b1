package com.gtafe.data.center.metadata.item.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

import com.gtafe.framework.base.vo.BaseVO;

/**
 * 
 * ClassName: ClassTreeVO <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2017年7月28日 下午5:41:38 <br/>
 * 
 * @history
 * @author wuh.zhu
 * @version
 * @since JDK 1.7
 */
@ApiModel
public class ClassTreeVO extends BaseVO {

	/**
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).
	 * 
	 * @since JDK 1.7
	 */
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "类编号", required = true)
	private String classCode;
	@ApiModelProperty(value = "类名称", required = true)
	private String className;
	
	@ApiModelProperty(value = "子集代码", required = true)
	private String subsetCode;
	

	private List<SubclassTreeVO> subclassTreeVO;

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
	
	public String getSubsetCode() {
		return subsetCode;
	}

	public void setSubsetCode(String subsetCode) {
		this.subsetCode = subsetCode;
	}

	public List<SubclassTreeVO> getSubclassTreeVO() {
		return subclassTreeVO;
	}

	public void setSubclassTreeVO(List<SubclassTreeVO> subclassTreeVO) {
		this.subclassTreeVO = subclassTreeVO;
	}



}
