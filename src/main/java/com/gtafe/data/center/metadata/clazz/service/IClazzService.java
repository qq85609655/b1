/** 
 * Project Name:gtacore 
 * File Name:ISubsetService.java 
 * Package Name:com.gtafe.data.center.meta.service 
 * Date:2017年7月18日上午10:00:09 
 * Copyright (c) 2017, GTA All Rights Reserved. 
 * 
 */  
package com.gtafe.data.center.metadata.clazz.service;

import java.util.List;

import com.gtafe.data.center.metadata.clazz.vo.ClazzBySubsetVO;
import com.gtafe.data.center.metadata.clazz.vo.ClazzVO;

public interface IClazzService {
	
	List<ClazzVO> queryClassList(int sourceId, String subsetCode,String classCodeOrclassName, int pageNum, int pageSize);
	
	boolean classDelete(String subsetCode, String classCode);
	
	boolean updateClass(ClazzVO clazzVO);
	 
	boolean addClass(ClazzVO clazzVO);
	
	ClazzVO queryByCode(String subsetCode, String classCode);
	
	int querySubClass(String classCode);
	
	
	boolean addClassBatchBysubset(String subsetCode,List<ClazzBySubsetVO> listClazzBySubsetVO);
}
