package com.gtafe.data.center.metadata.item.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

import com.gtafe.framework.base.vo.BaseVO;

/**
 * 
 * ClassName: SubsetTreeVO <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2017年7月28日 下午5:52:42 <br/>
 * 
 * @history
 * @author wuhu.zhu
 * @version
 * @since JDK 1.7
 */
@ApiModel
public class SubsetTreeVO extends BaseVO {

	/**
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).
	 * 
	 * @since JDK 1.7
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "子集代码", required = true)
	private String subsetCode;
	
	@ApiModelProperty(value = "子集中文名称", required = true)
	private String subsetName;
	
	@ApiModelProperty(value = "代码标准分类的自增序列id", required = false)
	private int sourceId;

	private List<ClassTreeVO> classTreeVO;
	
	public int getSourceId() {
		return sourceId;
	}

	public void setSourceId(int sourceId) {
		this.sourceId = sourceId;
	}

	public String getSubsetCode() {
		return subsetCode;
	}

	public void setSubsetCode(String subsetCode) {
		this.subsetCode = subsetCode;
	}

	public String getSubsetName() {
		return subsetName;
	}

	public void setSubsetName(String subsetName) {
		this.subsetName = subsetName;
	}

	public List<ClassTreeVO> getClassTreeVO() {
		return classTreeVO;
	}

	public void setClassTreeVO(List<ClassTreeVO> classTreeVO) {
		this.classTreeVO = classTreeVO;
	}



}
