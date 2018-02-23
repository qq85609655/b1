package com.gtafe.data.center.metadata.subclass.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import com.gtafe.framework.base.vo.BaseVO;

/**
 * 
 * ClassName: SubclassByclassVO <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2017年7月22日 下午6:04:32 <br/>
 * 
 * @history
 * @author wuhu.zhu
 * @version
 * @since JDK 1.7
 */
@ApiModel
public class SubclassByclassVO extends BaseVO {
	/**
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).
	 * 
	 * @since JDK 1.7
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "数据子类编号", required = true)
	private String subclassCode;

	@ApiModelProperty(value = "数据类中文名称", required = true)
	private String subclassComment;

	@ApiModelProperty(value = "数据类英文名称，即表名", required = true)
	private String subclassTableName;

	@ApiModelProperty(value = "子类描述", required = false)
	private String description;

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

	public String getSubclassTableName() {
		return subclassTableName;
	}

	public void setSubclassTableName(String subclassTableName) {
		this.subclassTableName = subclassTableName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
