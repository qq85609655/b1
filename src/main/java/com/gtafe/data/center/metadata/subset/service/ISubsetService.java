/** 
 * Project Name:gtacore 
 * File Name:ISubsetService.java 
 * Package Name:com.gtafe.data.center.meta.service 
 * Date:2017年7月18日上午10:00:09 
 * Copyright (c) 2017, GTA All Rights Reserved. 
 * 
 */  
package com.gtafe.data.center.metadata.subset.service;

import java.util.List;

import com.gtafe.data.center.metadata.subset.vo.SubsetVO;
import com.gtafe.data.center.metadata.subset.vo.Subsets;

public interface ISubsetService {
	List<SubsetVO> query(String sourceId, String codeOrName, int pageNum, int pageSize);
	
	SubsetVO queryByCode(int sourceid, String code);
	
	void insert(SubsetVO subsetVO);
	
	void insertBatch(Subsets subsets);
	
	void update(SubsetVO subsetVO);
	
	void delete(int sourceid, String code);
	
	int queryClass(String code);
}
