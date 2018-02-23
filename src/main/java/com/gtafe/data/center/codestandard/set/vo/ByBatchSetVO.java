package com.gtafe.data.center.codestandard.set.vo;

import io.swagger.annotations.ApiModelProperty;

import com.gtafe.framework.base.vo.BaseVO;

/**
 * 
 * ClassName: byBatchSetVO <br/>
 * Function: TODO 代码子集辅助类. <br/>
 * Reason: TODO ADD 用于批量新增代码子集. <br/>
 * date: 2017年8月2日 下午4:56:02 <br/>
 * 
 * @history
 * @author ken.zhang
 * @version
 * @since JDK 1.7
 */
public class ByBatchSetVO extends BaseVO {

	/**
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).
	 * 
	 * @since JDK 1.7
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "自增序列", required = true)
	private int setId;// 自增序列

	@ApiModelProperty(value = "代码标准的集合名称", required = true)
	private String name;// 代码标准的集合名称

	@ApiModelProperty(value = "代码集编码", required = true)
	private String code;// 代码集编码

	public int getSetId() {
		return setId;
	}

	public void setSetId(int setId) {
		this.setId = setId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
