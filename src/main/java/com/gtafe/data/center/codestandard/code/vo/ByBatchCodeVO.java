package com.gtafe.data.center.codestandard.code.vo;

import io.swagger.annotations.ApiModelProperty;

import com.gtafe.framework.base.vo.BaseVO;

/**
 * 
 * ClassName: ByBatchCodeVO <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2017年8月2日 下午5:58:32 <br/>
 * 
 * @history
 * @author ken.zhang
 * @version
 * @since JDK 1.7
 */
public class ByBatchCodeVO extends BaseVO {

	/**
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).
	 * 
	 * @since JDK 1.7
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "代码标准的代码自增序列", required = true)
	private int codeId;// 代码标准的代码自增序列

	@ApiModelProperty(value = "代码标准的代码", required = true)
	private String code;// 代码标准的代码

	@ApiModelProperty(value = "代码标准的代码的名称", required = true)
	private String name;// 代码标准的代码的名称

	@ApiModelProperty(value = "说明", required = false)
	private String description;// 说明

	public int getCodeId() {
		return codeId;
	}

	public void setCodeId(int codeId) {
		this.codeId = codeId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
