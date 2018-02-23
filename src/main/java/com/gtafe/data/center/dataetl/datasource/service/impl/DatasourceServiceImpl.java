/**
 * Project Name: gtacore
 * File Name:	<#%modlue%#>ServiceImpl.java
 * Description: This is writen by tools
 * Date: 		2017-08-14 17:50:40
 * Author: 		Xiang Zhiling
 * History:
 * Copyright (c) 2017, GTA All Rights Reserved.
 */

package com.gtafe.data.center.dataetl.datasource.service.impl;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtafe.data.center.common.common.constant.LogConstant;
import com.gtafe.data.center.common.common.util.empty.EmptyUtil;
import com.gtafe.data.center.dataetl.datasource.mapper.DatasourceMapper;
import com.gtafe.data.center.dataetl.datasource.service.IDatasourceService;
import com.gtafe.data.center.dataetl.datasource.utils.ConnectDB;
import com.gtafe.data.center.dataetl.datasource.vo.DatasourceVO;
import com.gtafe.data.center.dataetl.datatask.mapper.DataTaskMapper;
import com.gtafe.data.center.dataetl.datatask.vo.DataTaskVo;
import com.gtafe.data.center.dataetl.datatask.vo.rule.TableFieldVo;
import com.gtafe.data.center.system.log.service.LogService;
import com.gtafe.data.center.system.log.vo.LogInfo;
import com.gtafe.framework.base.exception.OrdinaryException;
import com.gtafe.framework.base.service.BaseService;
import com.gtafe.framework.base.utils.StringUtil;


@Service
public class DatasourceServiceImpl extends BaseService implements IDatasourceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(
            DatasourceServiceImpl.class);

    @Autowired
    private DatasourceMapper datasourceMapper;

    @Autowired
    private LogService logServiceImpl;
    @Resource
    private DataTaskMapper dataTaskMapper;

    @Override
    public List<DatasourceVO> queryDatasourceList(Integer dbType,
                                                  String nameOrDBName,
                                                  int pageNum, int pageSize,
                                                  String orgIds) {
        List<Integer> orgIdList = StringUtil.splitListInt(orgIds);
        if (orgIdList.isEmpty()) {
            return EmptyUtil.emptyList(pageSize, DatasourceVO.class);
        }
        return this.datasourceMapper.queryDatasourceList(dbType, nameOrDBName,
                pageNum, pageSize, orgIdList);
    }

    @Override
    public List<DatasourceVO> queryDatasourceListAll(String orgIds) {
        List<Integer> orgIdList = StringUtil.splitListInt(orgIds);
        if (orgIdList.isEmpty()) {
            return new ArrayList<DatasourceVO>();
        }
        return datasourceMapper.queryDatasourceListAll(orgIdList);
    }

    @Override
    public boolean datasourceDelete(int id) {
        DatasourceVO vo = this.datasourceMapper.queryDatasourceInfoById(id);
        if(vo == null) {
            return true;
        }
        this.checkTaskUsing(id);
        boolean result = this.datasourceMapper.datasourceDelete(id);
        
        LogInfo logInfo = new LogInfo();
        logInfo.setModuleId(LogConstant.Module_DataSource);
        logInfo.setOperType("删除");
        logInfo.setOperContent("删除数据源信息：" + vo.getName());
        this.logServiceImpl.saveLog(logInfo);
        return result;
    }

    @Override
    public boolean datasourceAdd(DatasourceVO datasourceVO) {
        if (this.queryDatasourceIfExist(datasourceVO.getName()) > 0) {
            throw new OrdinaryException("数据源名称已存在");
        }
        boolean result = this.datasourceMapper.datasourceAdd(datasourceVO);
        LogInfo logInfo = new LogInfo();
        logInfo.setModuleId(LogConstant.Module_DataSource);
        logInfo.setOperType("添加");
        logInfo.setOperContent("添加数据源信息：" + datasourceVO.getName());
        this.logServiceImpl.saveLog(logInfo);
        return result;
    }

    @Override
    public boolean datasourceUpdate(DatasourceVO datasourceVO) {
        DatasourceVO dbVo = this.queryIfExistById(datasourceVO.getId());
        if(dbVo == null) {
            throw new OrdinaryException("数据源不存在，或已被删除！");
        }
        //可以修改密码，不能禁用修改
        //this.checkTaskUsing(datasourceVO.getId());
        boolean result = this.datasourceMapper.datasourceUpdate(datasourceVO);
        
        LogInfo logInfo = new LogInfo();
        logInfo.setModuleId(LogConstant.Module_DataSource);
        logInfo.setOperType("修改");
        logInfo.setOperContent("修改数据源信息：" +(dbVo.getName().equals(datasourceVO.getName()) ? "" : (dbVo.getName()+"=>")) +datasourceVO.getName());
        this.logServiceImpl.saveLog(logInfo);
        return result;
    }
    
    /**
     * 检查是否存在资源任务使用数据表
     * @author 汪逢建
     * @date 2017年12月12日
     */
    private void checkTaskUsing(int collectionId){
        List<DataTaskVo> dataTaskVoList = this.dataTaskMapper.findTasksByConnId(collectionId);
        if(dataTaskVoList.isEmpty()) {
            return;
        }
        StringBuffer fbtask = new StringBuffer("");
        StringBuffer dytask = new StringBuffer("");
        for (DataTaskVo vo : dataTaskVoList) {
            if (vo.getBusinessType() == 1) {
                fbtask.append(vo.getTaskName()).append(",");
            } else {
                dytask.append(vo.getTaskName()).append(",");
            }
        }
        StringBuffer error = new StringBuffer();
        if(fbtask.length()>0) {
            error.append("发布资源【").append(fbtask.substring(0,fbtask.length()-1)).append("】");
        }
        if(dytask.length()>0) {
            if(error.length()>0) {
                dytask.append("，");
            }
            error.append("订阅资源【").append(dytask.substring(0,dytask.length()-1)).append("】");
        }
        String msg = "当前数据源被" + error.toString() + "引用，请先删除数据资源任务!";
        throw new OrdinaryException(msg);
    }
    
    @Override
    public DatasourceVO queryIfExistById(int id) {
        return this.datasourceMapper.queryDatasourceInfoById(id);
    }

    @Override
    public boolean queryDatasourceStatus(DatasourceVO datasourceVO) {
        ConnectDB connectDB = StringUtil.getEntityBy(datasourceVO);
        try {
            Connection connection = connectDB.getConn();
            if (null != connection) {
                if (!(connection.isClosed())) {
                    LOGGER.debug("connect success.");
                    connection.close();
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            LOGGER.error("SQLException");
        }
        return false;
    }

    @Override
    public DatasourceVO queryDatasourceInfoById(int id) {
        return this.datasourceMapper.queryDatasourceInfoById(id);
    }

    @Override
    public List<String> queryTablesByDatasource(DatasourceVO datasourceVO) {
        List<String> tbs = new ArrayList<String>();
        ConnectDB connectDB = StringUtil.getEntityBy(datasourceVO);
        try {
            Connection connection = connectDB.getConn();
            if (null != connection) {
                Statement st = connection.createStatement();
                String sql = "";
                if (datasourceVO.getDbType() == 2) {
                    sql = "select table_name from user_tables";
                } else if (datasourceVO.getDbType() == 3) {
                    sql = "select name from sys.tables go";
                } else {
                    sql = "SELECT table_name FROM information_schema.tables WHERE table_schema='"
                            + datasourceVO.getDbName()
                            + "' AND table_type='base table'";
                }
                ResultSet rs = st.executeQuery(sql);
                while (rs.next()) {
                    String name = rs.getString(1);
                    tbs.add(name);
                }
                connection.close();
                return tbs;
            } else {
                return tbs;
            }

        } catch (SQLException e) {
            LOGGER.error("SQLException");
        } finally {

        }
        return tbs;
    }

    public List<TableFieldVo> queryTableFields(DatasourceVO datasourceVO,
                                                 String table) throws Exception {
        List<TableFieldVo> result = new ArrayList<TableFieldVo>();
        ConnectDB connectDB = StringUtil.getEntityBy(datasourceVO);
        Connection connection = null;
        try {
            connection = connectDB.getConn();
            if (null == connection) {
                return result;
            }
            Statement st = connection.createStatement();
            String sql = "";
            if (2 == datasourceVO.getDbType())  {
               // oracle 的sql 生成
               sql = genSqlStringOracle(table);
            } else if (3 == datasourceVO.getDbType())  {
                // sqlserver 的sql生成
                sql = genSqlStringSqlServer(table);
            } else  {
                // mysql sql语句生成
                sql = genSqlStringMySql(table, datasourceVO.getDbName());
            }
            if(LOGGER.isInfoEnabled()) {
                LOGGER.info(sql);
            }
            LOGGER.info(sql);
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                TableFieldVo field = new TableFieldVo();
                field.setField(rs.getString(1));
                field.setDataType(rs.getString(2));
                int type = rs.getInt(6);
                if(type == 1) {
                    this.handleNumberLength(field, rs, datasourceVO.getDbType());
                }else {
                    String length = rs.getString(3);
                    long len = length!=null ? Long.parseLong(length) : 0L;
                    field.setLength(len);
                    field.setDecimalLength(0L);
                }
                String primarykey = rs.getString(7);
                field.setPrimarykey(primarykey!=null && "1".equals(primarykey) ? 1 : 0);
                String comment = rs.getString(8);
                field.setComment(comment == null ? "":comment);
                String nullable = rs.getString(9);
                field.setNullable(nullable!=null && "0".equals(nullable) ? 0 : 1);
                result.add(field);
            }
            connection.close();
            connection = null;
        } catch (SQLException e) {
            LOGGER.error("SQLException" , e);
        }finally {
            if(connection!=null) {
                connection.close();
            }
        }
        return result;
    }
    
    
    private void handleNumberLength(TableFieldVo field, ResultSet rs, int dbType)throws SQLException{
        //String lengthStr = rs.getString(3);
        //long lenStr = lengthStr!=null ? Long.parseLong(lengthStr) : 0L;
        String length = rs.getString(4);
        long len = length!=null ? Long.parseLong(length) : 0L;
        String decimalLength = rs.getString(5);
        long decimalLen = decimalLength!=null ? Long.parseLong(decimalLength) : 0L;
        if(dbType!=2) {
            //非oracle数据库
            field.setLength(len);
            field.setDecimalLength(decimalLen);
            return;
        }else {
            //oracle
            field.setLength(len);
            field.setDecimalLength(decimalLen);
            if("NUMBER".equalsIgnoreCase(field.getDataType())) {
                if(field.getLength()<=0) {
                    //没有指定number长度
                    field.setLength(38L);
                    field.setDecimalLength(0L);
                }else {
                    //如果DecimalLength为负数，对于我们的程序相当于整数个数多几位
                }
                return;
            }
            if("BINARY_FLOAT".equalsIgnoreCase(field.getDataType())) {
                field.setLength(38L);//取数据库默认长度
                field.setDecimalLength(0L);
                return;
            }
            if("BINARY_DOUBLE".equalsIgnoreCase(field.getDataType())) {
                field.setLength(308L);//取数据库默认长度
                field.setDecimalLength(0L);
                return;
            }
            if("FLOAT".equalsIgnoreCase(field.getDataType())) {
                field.setLength(38L);//取数据库默认长度，数据库仅存储有效数字的的二进制个数
                field.setDecimalLength(0L);
                return;
            }
            return;
        }
    }
    
    public int queryDatasourceIfExist(String name) {
        return this.datasourceMapper.queryDatasourceIfExist(name);

    }

    @Override
    public List<DatasourceVO> queryCenterData() {
        return this.datasourceMapper.queryCenterData();
    }

    /**
     * 主要功能:   生成 oracle 取得表字段的SQL语句  <br>
     * 注意事项:无  <br>
     *
     * @param tableName 表名
     * @return SQL
     */
    private String genSqlStringOracle(String tableName) {
        if (null == tableName) {
            return "";
        }
        StringBuilder sql = new StringBuilder(1024);
        sql.append(" select    COLS.COLUMN_NAME,");
        sql.append(" COLS.DATA_TYPE,");
        sql.append(" case when COLS.DATA_TYPE in ('NCHAR','NVARCHAR2') then COLS.DATA_LENGTH/2 ");
        sql.append(" when COLS.DATA_TYPE in ('LONG') then 2*1024*1024*1024 ");//2G字节
        sql.append(" else COLS.DATA_LENGTH end,");
        sql.append(" COLS.DATA_PRECISION,");
        sql.append(" COLS.DATA_SCALE,");
        sql.append(" case when COLS.DATA_TYPE in ('NUMBER','INTEGER','FLOAT','BINARY_FLOAT','BINARY_DOUBLE') then 1 else 0 end ,");
        sql.append(" DECODE(CTS.COLUMN_NAME, NULL, 0, 1),");
        sql.append(" COMMENTS.COMMENTS, ");
        sql.append(" decode (cols.NULLABLE,'N',0,1) ");
        
        sql.append(" from user_tab_cols COLS,");
        sql.append("  (select CCOLS.table_name, CCOLS.column_name");
        sql.append(" FROM USER_CONS_COLUMNS CCOLS, USER_CONSTRAINTS CONS");
        sql.append(" WHERE CCOLS.constraint_name = CONS.constraint_name");
        sql.append(" AND CONS.constraint_type = 'P'");
        sql.append(" AND CCOLS.TABLE_NAME = CONS.TABLE_NAME");
        sql.append(" AND CCOLS.TABLE_NAME='"+ tableName +"' ) CTS,");
        sql.append(" USER_COL_COMMENTS COMMENTS ");
        sql.append(" where COLS.TABLE_NAME = CTS.TABLE_NAME(+)");
        sql.append(" AND COLS.COLUMN_NAME = CTS.COLUMN_NAME(+)");
        sql.append(" AND COLS.TABLE_NAME=COMMENTS.TABLE_NAME");
        sql.append(" and cols.COLUMN_NAME=comments.column_name");
        sql.append(" AND COLS.TABLE_NAME='"+ tableName +"'");
        sql.append(" order by COLS.COLUMN_ID asc");
        return sql.toString();

    }

    /**
     * 主要功能:   生成SQLSERVER的取表字段和类型的语句  <br>
     * 注意事项:  无  <br>
     *
     * @param tableName 表名
     * @return SQL语句
     */
    private String genSqlStringSqlServer(String tableName) {
        if (null == tableName) {
            return "";
        }

        StringBuilder sql = new StringBuilder(1024);
        sql.append(" SELECT");
        sql.append(" CAST (col.name AS NVARCHAR(128)),");
        sql.append(" CAST (t.name AS NVARCHAR(128)),");
        sql.append(" CASE ");
        sql.append(" when t.name in ('text') then 2147483647 ");
        sql.append(" when t.name in ('ntext') then 1073741823 ");
        sql.append(" when t.name in ('nchar','nvarchar') then col.max_length/2 ");
        sql.append(" else col.max_length end,");
        sql.append(" col.precision,");
        sql.append(" col.scale,");
        sql.append(" case when t.name in ('bit','tinyint','smallint','int','bigint','decimal','numeric','smallmoney','money','float','real') then 1 else 0 end ,");
        sql.append(" (");
        sql.append(" SELECT");
        sql.append(" TOP 1 CASE ind.is_primary_key");
        sql.append(" WHEN 1 THEN 1 ELSE NULL END");
        sql.append(" FROM sys.index_columns ic");
        sql.append(" LEFT JOIN sys.indexes ind ON ic.object_id = ind.object_id");
        sql.append(" AND ic.index_id = ind.index_id");
        sql.append(" AND ind.name LIKE 'PK_%'");
        sql.append(" WHERE ic.object_id = obj.object_id");
        sql.append(" AND ic.column_id = col.column_id");
        sql.append(" ),");
        sql.append(" CAST (ep. VALUE AS NVARCHAR(1000)),");
        sql.append(" CASE WHEN col.is_nullable=0 THEN 0 ELSE 1 END");
        sql.append(" FROM sys.objects obj");
        sql.append(" INNER JOIN sys.columns col ON obj.object_id = col.object_id");
        sql.append(" LEFT JOIN sys.types t ON t.user_type_id = col.user_type_id");
        sql.append(" LEFT JOIN sys.extended_properties ep ON ep.major_id = obj.object_id");
        sql.append(" AND ep.minor_id = col.column_id");
        sql.append(" AND ep.name = 'MS_Description'");
        sql.append(" WHERE obj.name ='"+ tableName +"' ");
        sql.append(" order by col.column_id asc");
        return sql.toString();
    }

    /**
     * 主要功能:   生成Mysql的取表字段和类型的语句  <br>
     * 注意事项:  无  <br>
     *
     * @param tableName 表名
     * @return SQL语句
     */
    private String genSqlStringMySql(String tableName,String dbName) {
        if (null == tableName) {
            return "";
        }
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT DISTINCT");
        sql.append(" COLUMN_NAME,");
        sql.append(" DATA_TYPE,");
        sql.append(" CHARACTER_MAXIMUM_LENGTH,");
        sql.append(" NUMERIC_PRECISION,");
        sql.append(" NUMERIC_SCALE,");
        sql.append(" case when DATA_TYPE in ('tinyint','smallint','mediumint','int','integer','bigint','float','double','decimal','numeric','real') then 1 else 0 end ,");
        sql.append(" if(COLUMN_KEY = 'PRI' , 1 , 0),");
        sql.append(" COLUMN_COMMENT,");
        sql.append(" if(COLUMN_KEY = 'NO' , 0 , 1)");
        sql.append(" FROM INFORMATION_SCHEMA.COLUMNS");
        sql.append(" WHERE TABLE_NAME = '"+ tableName.toLowerCase() +"' and TABLE_SCHEMA='"+ dbName +"'");
        sql.append(" order by ORDINAL_POSITION asc");
        return sql.toString();
    }
}
