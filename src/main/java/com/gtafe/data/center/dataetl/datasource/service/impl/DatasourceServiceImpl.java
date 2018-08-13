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

import com.gtafe.data.center.dataetl.datatask.vo.rule.TableFieldVV;
import com.gtafe.framework.base.utils.PropertyUtils;
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
                                                  String orgIds, String isCenter) {
        List<String> orgIdList = StringUtil.splitListString(orgIds);
        if (orgIdList.isEmpty()) {
            return EmptyUtil.emptyList(pageSize, DatasourceVO.class);
        }
        return this.datasourceMapper.queryDatasourceList(dbType, nameOrDBName,
                pageNum, pageSize, orgIdList, isCenter);
    }

    @Override
    public List<DatasourceVO> queryDatasourceListAll(String orgIds) {
        List<String> orgIdList = StringUtil.splitListString(orgIds);
        if (orgIdList.isEmpty()) {
            return new ArrayList<DatasourceVO>();
        }
        return datasourceMapper.queryDatasourceListAll(orgIdList);
    }

    @Override
    public boolean datasourceDelete(int id) {
        DatasourceVO vo = this.datasourceMapper.queryDatasourceInfoById(id);
        if (vo == null) {
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
        if (dbVo == null) {
            throw new OrdinaryException("数据源不存在，或已被删除！");
        }
        //可以修改密码，不能禁用修改
        //this.checkTaskUsing(datasourceVO.getId());
        boolean result = this.datasourceMapper.datasourceUpdate(datasourceVO);

        LogInfo logInfo = new LogInfo();
        logInfo.setModuleId(LogConstant.Module_DataSource);
        logInfo.setOperType("修改");
        logInfo.setOperContent("修改数据源信息：" + (dbVo.getName().equals(datasourceVO.getName()) ? "" : (dbVo.getName() + "=>")) + datasourceVO.getName());
        this.logServiceImpl.saveLog(logInfo);
        return result;
    }

    /**
     * 检查是否存在资源任务使用数据表
     *
     * @author 汪逢建
     * @date 2017年12月12日
     */
    private void checkTaskUsing(int collectionId) {
        List<DataTaskVo> dataTaskVoList = this.dataTaskMapper.findTasksByConnId(collectionId);
        if (dataTaskVoList.isEmpty()) {
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
        if (fbtask.length() > 0) {
            error.append("发布资源【").append(fbtask.substring(0, fbtask.length() - 1)).append("】");
        }
        if (dytask.length() > 0) {
            if (error.length() > 0) {
                dytask.append("，");
            }
            error.append("订阅资源【").append(dytask.substring(0, dytask.length() - 1)).append("】");
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
    public List<String> queryTablesByDatasource(DatasourceVO datasourceVO, String busType) {
        List<String> tbs = new ArrayList<String>();
        ConnectDB connectDB = StringUtil.getEntityBy(datasourceVO);
        String B_MYSQL_READ_VIEW = PropertyUtils.getProperty("config.properties", "B_MYSQL_READ_VIEW");
        String B_SQLSERVER_READ_VIEW = PropertyUtils.getProperty("config.properties", "B_SQLSERVER_READ_VIEW");
        if (!StringUtil.isNotBlank(B_MYSQL_READ_VIEW)) {
            B_MYSQL_READ_VIEW = "N";
        }

        try {
            Connection connection = connectDB.getConn();
            if (null != connection) {
                Statement st = connection.createStatement();
                String sql = "";
                if (datasourceVO.getDbType() == 2) {
                    if (busType.equals("1")) {
                        if (B_MYSQL_READ_VIEW.equals("Y")) {
                            sql = "select table_name tableName ,'TABLE' as typeStr from user_tables " +
                                    "union all " +
                                    "select view_name viewName,'VIEW' as typeStr from user_views " +
                                    "";
                        } else {
                            sql = "select table_name tableName ,'TABLE' as typeStr from user_tables " +
                                    "";
                        }
                    } else if (busType.equals("2")) {
                        sql = "select table_name tableName ,'TABLE' as typeStr from user_tables ";
                    }
                } else if (datasourceVO.getDbType() == 3) {
                    if (busType.equals("1")) {
                        if (B_SQLSERVER_READ_VIEW.equals("Y")) {
                            /**
                             * 2005
                             */
                    /*        sql = "select name,'TABLE' AS type from sys.tables\n" +
                                    " UNION \n" +
                                    " SELECT name,'VIEW' AS type from sys.viewsj \n" +
                                    " ";*/

                            /**
                             * 2008
                             */
                            sql = "select name,'TABLE' AS type from sys.tables\n" +
                                    " UNION \n" +
                                    " SELECT name,'VIEW' AS type from syscomments s1 " +
                                    " join sysobjects s2 on s1.id=s2.id  where type='V' \n" +
                                    " ";
                        } else {
                            sql = "select name,'TABLE' AS type from sys.tables\n" +
                                    " ";
                        }
                    } else {
                        sql = "select name,'TABLE' AS type from sys.tables ";
                    }
                } else {
                    if (busType.equals("1")) {
                        if (B_MYSQL_READ_VIEW.equals("Y")) {
                            sql = "SELECT\n" +
                                    "\ttable_name,\n" +
                                    "TABLE_TYPE\n" +
                                    "FROM\n" +
                                    "\tinformation_schema. TABLES\n" +
                                    "WHERE\n" +
                                    "\ttable_schema = '" + datasourceVO.getDbName() + "'\n" +
                                    "AND table_type = 'base table'\n" +
                                    "UNION ALL\n" +
                                    "SELECT\n" +
                                    "\ttable_name,\n" +
                                    "TABLE_TYPE\n" +
                                    "FROM\n" +
                                    "\tinformation_schema. TABLES\n" +
                                    "WHERE\n" +
                                    "\ttable_schema = '" + datasourceVO.getDbName() + "'\n" +
                                    "AND table_type = 'VIEW'";
                        } else {
                            sql = "SELECT\n" +
                                    "\ttable_name,\n" +
                                    "TABLE_TYPE\n" +
                                    "FROM\n" +
                                    "\tinformation_schema. TABLES\n" +
                                    "WHERE\n" +
                                    "\ttable_schema = '" + datasourceVO.getDbName() + "'\n" +
                                    "AND table_type = 'base table'\n";
                        }
                    } else if (busType.equals("2")) {
                        sql = "SELECT\n" +
                                "\t table_name,\n" +
                                " TABLE_TYPE\n" +
                                "FROM\n" +
                                "\tinformation_schema. TABLES\n" +
                                "WHERE\n" +
                                "\ttable_schema = '" + datasourceVO.getDbName() + "'\n" +
                                "AND table_type = 'base table'";
                    }
                }
                ResultSet rs = st.executeQuery(sql);
                while (rs.next()) {
                    String name = rs.getString(1);
                    String type = rs.getString(2);
                    if (type.equals("VIEW")) {
                        tbs.add(name + "#V");
                    } else {
                        tbs.add(name + "#T");
                    }
                }
                connection.close();
                return tbs;
            } else {
                return tbs;
            }
        } catch (SQLException e) {
            LOGGER.error("SQLException" + e.getMessage());
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
            if (2 == datasourceVO.getDbType()) {
                // oracle 的sql 生成
                sql = genSqlStringOracle(table);
            } else if (3 == datasourceVO.getDbType()) {
                // sqlserver 的sql生成
                sql = genSqlStringSqlServer(table);
            } else {
                // mysql sql语句生成
                sql = genSqlStringMySql(table, datasourceVO.getDbName());
            }
            LOGGER.info(sql);
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                TableFieldVo field = new TableFieldVo();
                field.setField(rs.getString(1));
                field.setDataType(rs.getString(2));
                int type = rs.getInt(6);
                if (type == 1) {
                    this.handleNumberLength(field, rs, datasourceVO.getDbType());
                } else {
                    String length = rs.getString(3);
                    long len = length != null ? Long.parseLong(length) : 0L;
                    field.setLength(len);
                    field.setDecimalLength(0L);
                }
                String primarykey = rs.getString(7);
                LOGGER.info("当前是不是主键哦：" + primarykey);
                field.setPrimarykey(primarykey != null && "1".equals(primarykey) ? 1 : 0);
                String comment = rs.getString(8);
                field.setComment(comment == null ? "" : comment);
                String nullable = rs.getString(9);
                field.setNullable(nullable != null && "0".equals(nullable) ? 0 : 1);
                String isAutoAdd = rs.getString(10);
                LOGGER.info(isAutoAdd);
                field.setIsAutoCreate(isAutoAdd);//默认设置为N 非自增
                result.add(field);
            }
            connection.close();
            connection = null;
        } catch (SQLException e) {
            LOGGER.error("SQLException", e);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        return result;
    }

    @Override
    public List<DatasourceVO> getDbSourceListByOrgId(int orgId) {
        return this.datasourceMapper.queryDatasourceListByOrgId(orgId);
    }

    public TableFieldVV queryTableFields2(DatasourceVO datasourceVO,
                                          String table, String busType, String tType) throws Exception {
        TableFieldVV vv = new TableFieldVV();
        List<TableFieldVo> result = new ArrayList<TableFieldVo>();
        ConnectDB connectDB = StringUtil.getEntityBy(datasourceVO);
        Connection connection = null;
        StringBuilder keyComment = new StringBuilder("");
        try {
            connection = connectDB.getConn();
            if (null == connection) {
                return vv;
            }
            Statement st = connection.createStatement();
            String sql = "";
            if (2 == datasourceVO.getDbType()) {
                // oracle 的sql 生成
                LOGGER.info("========================oracle===============");
                sql = genSqlStringOracle(table);
            } else if (3 == datasourceVO.getDbType()) {
                // sqlserver 的sql生成
                LOGGER.info("=========================sqlserver==============");
                sql = genSqlStringSqlServer(table);
            } else {
                // mysql sql语句生成
                LOGGER.info("=========================mysql==============");
                sql = genSqlStringMySql(table, datasourceVO.getDbName());
            }
            LOGGER.info(sql);
            ResultSet rs = st.executeQuery(sql);
            int keyCount = 0;
            int keyAutoAddCount = 0;
            while (rs.next()) {
                TableFieldVo field = new TableFieldVo();
                field.setField(rs.getString(1));
                field.setDataType(rs.getString(2));
                int type = rs.getInt(6);
                if (type == 1) {
                    LOGGER.info(type + "");
                    this.handleNumberLength(field, rs, datasourceVO.getDbType());
                } else {
                    String length = rs.getString(3);
                    long len = length != null ? Long.parseLong(length) : 0L;
                    field.setLength(len);
                    field.setDecimalLength(0L);
                }
                String primarykey = rs.getString(7);
                LOGGER.info("当前是不是主键哦：" + primarykey);
                field.setPrimarykey(primarykey != null && "1".equals(primarykey) ? 1 : 0);
                String comment = rs.getString(8);
                field.setComment(comment == null ? "" : comment);
                String nullable = rs.getString(9);
                field.setNullable(nullable != null && "0".equals(nullable) ? 0 : 1);
                String isAutoAdd = rs.getString(10);
                LOGGER.info(isAutoAdd);
                field.setIsAutoCreate(isAutoAdd);//默认设置为N 非自增

                if (primarykey != null && "1".equals(primarykey)) {
                    keyCount++;
                }

                if (tType.equals("T")) {
                    if (isAutoAdd.equals("Y")) {//主键是自增的话 就不需要再放到前台去。。。
                        keyAutoAddCount++;
                        if (busType.equals("2")) {
                            continue;
                        }
                    }
                }
                result.add(field);
            }
            if (tType.equals("T")) {
                if (keyAutoAddCount > 0 && keyCount > 0) {
                    keyComment.append("<font color=RED>注意:当前选择的目标表主键是自增的,所以不需要选择，系统自动给隐藏了！</font>");
                } else {
                    keyComment.append("<font color=RED>注意:当前选择的目标表 主键是非自增的 或者无主键存在！所以必须要指定值！</font>");
                }
            }
            vv.setTableFieldVoList(result);
            vv.setKeyComment(keyComment.toString());
            vv.setKetAutoAddCount(keyAutoAddCount);
            connection.close();
            connection = null;
        } catch (SQLException e) {
            LOGGER.error("SQLException", e);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        return vv;
    }



      /*          if (primarykey != null && "1".equals(primarykey)) {
                    primarykey2 = rs.getString(1);
                }
                if (primarykey != null && "1".equals(primarykey)) {
                    keyType = rs.getString(2);
                }
*/


           /* //对主键单独来查询是否 自增 并修改 isAutoCreate
            String isAutoCreate = "";
            LOGGER.info("keyType====" + keyType);
            LOGGER.info("primarykey2====" + primarykey2);
            isAutoCreate = this.getKeyInfo(table, primarykey2, datasourceVO.getDbName(), datasourceVO.getDbType() + "", connection, keyType);
            for (TableFieldVo vo : result) {
                if (vo.getPrimarykey() == 1) {
                    vo.setIsAutoCreate(isAutoCreate);
                }
                tableFieldVoList.add(vo);
                LOGGER.info(vo.toString());
            }*/
  /*
  private String getKeyInfo(String tableName, String key, String schema, String dbType, Connection connection, String keyType) throws SQLException {
        String sql = "";
        Statement st = connection.createStatement();
        if ("1".equals(dbType)) {
            sql = "SELECT\n" +
                    "COUNT(1) c\n" +
                    "FROM\n" +
                    "\tinformation_schema. COLUMNS a\n" +
                    "WHERE\n" +
                    "\ta.extra = 'auto_increment'\n" +
                    "AND TABLE_NAME = '" + schema + "'\n" +
                    "AND TABLE_SCHEMA = '" + tableName + "'\n" +
                    "AND COLUMN_NAME = '" + key + "'";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                long c = Long.parseLong((String) rs.getString(1));
                if (c == 1) {
                    //说明是自增的主键哦
                    return "Y";
                }
            }
            rs.close();
        } else if ("2".equals(dbType)) {
            //oracle 数据库由于主键自增这块 需要创建sequence 和触发器一起 才能得知 是否自增，
            // 而且 sequence 和触发器的定义对于表的主键 无法一一对应，所以程序无法准确界定。
            if (keyType.toLowerCase().equals("int")) {//根据主键的类型来界定是否自增： 如果是int 型 默认就是自增了
                return "Y";
            }
            return "N";
        } else if ("3".equals(dbType)) {
            sql = "SELECT DISTINCT\n" +
                    "\tcol.name,\n" +
                    "\tCASE\n" +
                    "WHEN COLUMNPROPERTY(\n" +
                    "\tcol.id,\n" +
                    "\tcol.name,\n" +
                    "\t'IsIdentity'\n" +
                    ") = 1 THEN\n" +
                    "\t'1'\n" +
                    "ELSE\n" +
                    "\t''\n" +
                    "END AS 标识\n" +
                    "FROM\n" +
                    "\tdbo.syscolumns col where col.name='" + key + "'";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                String a = (String) rs.getString(2);
                if (a.equals("1")) {
                    //说明是自增的主键哦
                    return "Y";
                }
            }
            rs.close();
        }
        return "N";
    }*/

    private void handleNumberLength(TableFieldVo field, ResultSet rs, int dbType) throws SQLException {
        //String lengthStr = rs.getString(3);
        //long lenStr = lengthStr!=null ? Long.parseLong(lengthStr) : 0L;
        String length = rs.getString(4);
        long len = length != null ? Long.parseLong(length) : 0L;
        String decimalLength = rs.getString(5);
        long decimalLen = decimalLength != null ? Long.parseLong(decimalLength) : 0L;
        if (dbType != 2) {
            //非oracle数据库S
            field.setLength(len);
            field.setDecimalLength(decimalLen);
            return;
        } else {
            //oracle
            field.setLength(len);
            field.setDecimalLength(decimalLen);
            if ("NUMBER".equalsIgnoreCase(field.getDataType())) {
                if (field.getLength() <= 0) {
                    //没有指定number长度
                    field.setLength(38L);
                    field.setDecimalLength(0L);
                } else {
                    //如果DecimalLength为负数，对于我们的程序相当于整数个数多几位
                }
                return;
            }
            if ("BINARY_FLOAT".equalsIgnoreCase(field.getDataType())) {
                field.setLength(38L);//取数据库默认长度
                field.setDecimalLength(0L);
                return;
            }
            if ("BINARY_DOUBLE".equalsIgnoreCase(field.getDataType())) {
                field.setLength(308L);//取数据库默认长度
                field.setDecimalLength(0L);
                return;
            }
            if ("FLOAT".equalsIgnoreCase(field.getDataType())) {
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

 /*   @Override
    public List<DatasourceVO> queryCenterData() {
        return this.datasourceMapper.queryCenterData();
    }
*/

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
        sql.append(" select COLS.COLUMN_NAME,");
        sql.append(" COLS.DATA_TYPE,");
        sql.append(" case when COLS.DATA_TYPE in ('NCHAR','NVARCHAR2') then COLS.DATA_LENGTH/2 ");
        sql.append(" when COLS.DATA_TYPE in ('LONG') then 2*1024*1024*1024 ");//2G字节
        sql.append(" else COLS.DATA_LENGTH end,");
        sql.append(" COLS.DATA_PRECISION,");
        sql.append(" COLS.DATA_SCALE,");
        sql.append(" case when COLS.DATA_TYPE in ('NUMBER','INTEGER','FLOAT','BINARY_FLOAT','BINARY_DOUBLE') then 1 else 0 end ,");
        sql.append(" DECODE(CTS.COLUMN_NAME, NULL, 0, 1),");
        sql.append(" COMMENTS.COMMENTS, ");
        sql.append(" decode (cols.NULLABLE,'N',0,1), ");
        sql.append(" 'N'  autoAdd ");
        sql.append(" from user_tab_cols COLS,");
        sql.append("  (select CCOLS.table_name, CCOLS.column_name");
        sql.append(" FROM USER_CONS_COLUMNS CCOLS, USER_CONSTRAINTS CONS");
        sql.append(" WHERE CCOLS.constraint_name = CONS.constraint_name");
        sql.append(" AND CONS.constraint_type = 'P'");
        sql.append(" AND CCOLS.TABLE_NAME = CONS.TABLE_NAME");
        sql.append(" AND CCOLS.TABLE_NAME='" + tableName + "' ) CTS,");
        sql.append(" USER_COL_COMMENTS COMMENTS ");
        sql.append(" where COLS.TABLE_NAME = CTS.TABLE_NAME(+)");
        sql.append(" AND COLS.COLUMN_NAME = CTS.COLUMN_NAME(+)");
        sql.append(" AND COLS.TABLE_NAME=COMMENTS.TABLE_NAME");
        sql.append(" and cols.COLUMN_NAME=comments.column_name");
        sql.append(" AND COLS.TABLE_NAME='" + tableName + "'");
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
        sql.append(" CASE WHEN col.is_nullable=0 THEN 0 ELSE 1 END ,");
        sql.append(" CASE WHEN COLUMNPROPERTY(col.object_id, col.name, 'IsIdentity') = 1 THEN 'Y' ELSE 'N' END   autoAdd ");
        sql.append(" FROM sys.objects obj");
        sql.append(" INNER JOIN sys.columns col ON obj.object_id = col.object_id");
        sql.append(" LEFT JOIN sys.types t ON t.user_type_id = col.user_type_id");
        sql.append(" LEFT JOIN sys.extended_properties ep ON ep.major_id = obj.object_id");
        sql.append(" AND ep.minor_id = col.column_id");
        sql.append(" AND ep.name = 'MS_Description'");
        sql.append(" WHERE obj.name ='" + tableName + "' ");
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
    private String genSqlStringMySql(String tableName, String dbName) {
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
        sql.append(" if(COLUMN_KEY = 'NO' , 0 , 1),");
        sql.append(" CASE WHEN extra = 'auto_increment' THEN 'Y' ELSE 'N' END  AS autoAdd ");
        sql.append(" FROM INFORMATION_SCHEMA.COLUMNS ");
        sql.append(" WHERE TABLE_NAME = '" + tableName.toLowerCase() + "' and TABLE_SCHEMA='" + dbName + "'");
        sql.append(" order by ORDINAL_POSITION asc");
        return sql.toString();
    }
}
