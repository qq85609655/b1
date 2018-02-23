package com.gtafe.data.center.metadata.item.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import com.gtafe.framework.base.vo.BaseVO;

/**
 * 
 * ClassName: SubclassTreeVO <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2017年7月28日 下午5:54:04 <br/>
 * 
 * @history
 * @author wuhu.zhu
 * @version
 * @since JDK 1.7
 */
@ApiModel
public class SubclassTreeVO extends BaseVO {

	/**
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).
	 * 
	 * @since JDK 1.7
	 */
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "类编号", required = true)
	private String classCode;

	@ApiModelProperty(value = "数据子类编号", required = true)
	private String subclassCode;

	@ApiModelProperty(value = "数据子类中文名称", required = true)
	private String subclassComment;
	
	

	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public String getSubclassCode() {
		return subclassCode;
	}

	public void setSubclassCode(String subclassCode) {
		this.subclassCode = subclassCode;
	}

	public String getSubclassComment() {
		return subclassComment;
	}

	public void setSubclassComment(String subclassComment) {
		this.subclassComment = subclassComment;
	}



}
