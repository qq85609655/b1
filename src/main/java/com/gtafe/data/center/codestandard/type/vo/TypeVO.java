package com.gtafe.data.center.codestandard.type.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import com.gtafe.framework.base.vo.BaseVO;

/**
 * 
 * ClassName: TypeVO <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2017年7月24日 下午5:54:04 <br/>
 * 
 * @history
 * @author ken.zhang
 * @version
 * @since JDK 1.7
 */
@ApiModel
public class TypeVO extends BaseVO {

	/**
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).
	 * 
	 * @since JDK 1.7
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "代码子集的自增序列", required = true)
	private int setId;// 代码子集的自增序列

	@ApiModelProperty(value = "自增序列", required = true)
	private int typeId;// 自增序列

	@ApiModelProperty(value = "代码标准分类类型的缩写", required = true)
	private String code;// 代码标准分类类型的缩写

	@ApiModelProperty(value = "代码标准分类的名称", required = true)
	private String name;// 代码标准分类的名称

	@ApiModelProperty(value = "说明", required = false)
	private String description;// 说明

	public int getSetId() {
		return setId;
	}

	public void setSetId(int setId) {
		this.setId = setId;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
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

    @Override
    public String toString() {
        return "TypeVO [setId=" + setId + ", typeId=" + typeId + ", code="
               + code + ", name=" + name + ", description=" + description + "]";
    }

}
