/** 
 * Project Name:gtacore 
 * File Name:	I<#%modlue%#>Service.java 
 * Description: 
 * Date: 2017-08-14 17:50:40
 * Author: Xiang Zhiling
 * History:
 * Copyright (c) 2017, GTA All Rights Reserved. 
 * 
 */  
package com.gtafe.data.center.dataetl.datasource.service;

import java.util.List;

import com.gtafe.data.center.dataetl.datasource.vo.DatasourceVO;
import com.gtafe.data.center.dataetl.datatask.vo.rule.TableFieldVo;

public interface IDatasourceService {
	List<DatasourceVO> queryDatasourceList(Integer dbType, String nameOrDBName, int pageNum, int pageSize, String orgIds,String isCenter);
	
	List<DatasourceVO> queryDatasourceListAll(String orgIds);
	
	boolean datasourceDelete(int id);
	
	boolean datasourceAdd(DatasourceVO datasourceVO);

	boolean datasourceUpdate(DatasourceVO datasourceVO);
	
    DatasourceVO queryIfExistById(int id);
    
    boolean queryDatasourceStatus(DatasourceVO datasourceVO);
    
    DatasourceVO queryDatasourceInfoById(int id);
    
    List<String> queryTablesByDatasource(DatasourceVO datasourceVO);
    
    List<TableFieldVo> queryTableFields(DatasourceVO datasourceVO,String table) throws Exception;


    List<DatasourceVO> queryCenterData();

}
