package com.gtafe.data.center.codestandard.source.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

import com.gtafe.data.center.codestandard.set.vo.SetVO;
import com.gtafe.framework.base.vo.BaseVO;

/**
 * 
 * ClassName: SourceVO <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2017年7月24日 下午5:52:42 <br/>
 * 
 * @history
 * @author ken.zhang
 * @version
 * @since JDK 1.7
 */
@ApiModel
public class SourceVO extends BaseVO {

	/**
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).
	 * 
	 * @since JDK 1.7
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "自增序列", required = true)
	private int sourceid; // 自增序列

	@ApiModelProperty(value = "代码标准来源的编码", required = true)
	private String code;// 代码标准来源的编码

	@ApiModelProperty(value = "代码标准来源的名称", required = true)
	private String name;// 代码标准来源的名称

	private List<SetVO> setVOs; // 子集集合用于treeView

	public List<SetVO> getSetVOs() {
		return setVOs;
	}

	public void setSetVOs(List<SetVO> setVOs) {
		this.setVOs = setVOs;
	}

	public int getSourceid() {
		return sourceid;
	}

	public void setSourceid(int sourceid) {
		this.sourceid = sourceid;
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
}
