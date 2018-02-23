/** 
 * Project Name:GTADataCenter 
 * File Name:Subsets.java 
 * Package Name:com.gtafe.data.center.metadata.subset.vo 
 * Date:2017年8月2日下午4:50:23 
 * Copyright (c) 2017, GTA All Rights Reserved. 
 * 
 */  
package com.gtafe.data.center.metadata.subset.vo;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/** 
 * ClassName: Subsets <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason: TODO ADD REASON(可选). <br/> 
 * date: 2017年8月2日 下午4:50:23 <br/> 
 * @history
 * @author liquan.feng 
 * @version  
 * @since JDK 1.7
 */
@ApiModel
public class Subsets {
	
	/**
	 * 源id
	 */
	@ApiModelProperty(value = "来源ID", required = true)
	private int sourceId;
	
	/**
	 * 子集列表
	 */
	@ApiModelProperty(value = "子集列表", required = true)
	private List<SubsetVO> subsetList;

	public int getSourceId() {
		return sourceId;
	}

	public void setSourceId(int sourceId) {
		this.sourceId = sourceId;
	}

	public List<SubsetVO> getSubsetList() {
		return subsetList;
	}

	public void setSubsetList(List<SubsetVO> subsetList) {
		this.subsetList = subsetList;
	}
	
}
