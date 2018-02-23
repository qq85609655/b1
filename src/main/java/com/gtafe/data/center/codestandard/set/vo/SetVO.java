package com.gtafe.data.center.codestandard.set.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

import com.gtafe.data.center.codestandard.type.vo.TypeVO;
import com.gtafe.framework.base.vo.BaseVO;

/**
 * 
 * ClassName: SetVO <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2017年7月24日 下午5:41:38 <br/>
 * 
 * @history
 * @author ken.zhang
 * @version
 * @since JDK 1.7
 */
@ApiModel
public class SetVO extends BaseVO {

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

	@ApiModelProperty(value = "标准来源分类的id", required = true)
	private int sourceid;// 标准来源分类的id

	@ApiModelProperty(value = "代码集编码", required = true)
	private String code;// 代码集编码

	private List<TypeVO> typeVOs; // 类型集合，用来treeView

	public List<TypeVO> getTypeVOs() {
		return typeVOs;
	}

	public void setTypeVOs(List<TypeVO> typeVOs) {
		this.typeVOs = typeVOs;
	}

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

}
