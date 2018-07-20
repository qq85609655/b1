/**
 * Project Name: gtacore
 * File Name:	<#%modlue%#>Mapper.java
 * Description:
 * Date: 2017-08-14 17:50:40
 * Author: Xiang Zhiling
 * History:
 * Copyright (c) 2017, GTA All Rights Reserved.
 */
package com.gtafe.data.center.dataetl.datasource.mapper;

import com.gtafe.data.center.dataetl.datasource.vo.DatasourceVO;
import com.gtafe.data.center.information.code.vo.TableEntity;
import com.gtafe.framework.base.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

public interface DatasourceMapper extends BaseMapper {
    List<DatasourceVO> queryDatasourceList(@Param("dbType") Integer dbType, @Param("nameOrDBName") String nameOrDBName,
                                           @Param("pageNumKey") int pageNum, @Param("pageSizeKey") int pageSize,
                                           @Param("orgIdList") List<String> orgIdList, @Param("isCenter") String isCenter);

    List<DatasourceVO> queryDatasourceListAll(@Param("orgIdList") List<String> orgIdList);

    boolean datasourceDelete(@Param("id") int id);

    boolean datasourceAdd(DatasourceVO datasourceVO);

    boolean datasourceUpdate(DatasourceVO datasourceVO);

    List<DatasourceVO> queryDatasourceByIds(@Param("ids") List<Integer> ids);

    DatasourceVO queryDatasourceInfoById(@Param("id") int id);

    int queryDatasourceIfExist(@Param("name") String name);

    @Select("SELECT id FROM data_etl_dataconnection  WHERE is_center = #{is_center}")
    int getCenterId(@Param("is_center") String isCenter);


  /*  @Select("SELECT id, host,username,password,dbType,db_name dbName,port FROM data_etl_dataconnection  WHERE is_center = 1")
    List<DatasourceVO> queryCenterData();*/




    boolean saveIntoCenterTables(TableEntity tableVo);

    @Delete("delete from t_center_tables where tableName is not null")
    void trankAll();
}
