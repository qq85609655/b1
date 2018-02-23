package com.gtafe.data.center.information.data.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.gtafe.data.center.dataetl.datasource.mapper.DatasourceMapper;
import com.gtafe.data.center.dataetl.datasource.utils.ConnectDB;
import com.gtafe.data.center.dataetl.datasource.vo.DatasourceVO;
import com.gtafe.data.center.information.data.mapper.DataStandardItemMapper;
import com.gtafe.data.center.information.data.mapper.DataStandardMapper;
import com.gtafe.data.center.information.data.service.DataTableService;
import com.gtafe.data.center.information.data.vo.DataStandardItemVo;
import com.gtafe.data.center.information.data.vo.DataStandardVo;
import com.gtafe.framework.base.exception.OrdinaryException;
import com.gtafe.framework.base.utils.StringUtil;

@Service
public class DataTableServiceImpl implements DataTableService{
    //建表日志需要强制记录到日志文件，故使用ERROR级别记录
    private static final Logger LOGGER = LoggerFactory.getLogger(DataStandardItemServiceImpl.class);
    @Resource
    private DataStandardMapper dataStandardMapper;
    @Resource
    private DatasourceMapper datasourceMapper;
    @Resource
    private DataStandardItemMapper dataStandardItemMapper;
    
    @Override
    public boolean createTable(String subclassCode) {
        return this.createTables(Arrays.asList(new String[] {subclassCode}));
    }

    @Override
    public boolean createTables(List<String> subclassCodes) {
        if(subclassCodes == null || subclassCodes.isEmpty()) {
            return true;
        }
        List<DataStandardVo> voList = new ArrayList<DataStandardVo>();
        for(String code: subclassCodes) {
            //建表本身不是很快，此处不再使用数组方式查询数据子类
            DataStandardVo subclassVo = this.dataStandardMapper.getDataStandardVo(code);
            if(subclassVo == null || subclassVo.getNodeType() != 3) {
                throw new OrdinaryException("数据子类不存在，或已被删除！");
            }
            voList.add(subclassVo);
        }
        List<DatasourceVO> dataSourceVos = this.datasourceMapper.queryCenterData();
        if (dataSourceVos == null || dataSourceVos.isEmpty()) {
            throw new OrdinaryException("请检查中心库配置！");
        }
        ConnectDB connectDB = StringUtil.getEntityBy(dataSourceVos.get(0));
        Connection conn = null;
        try {
            conn = connectDB.getConn();
            if(conn == null) {
                throw new OrdinaryException("连接中心库失败，请检查中心库配置！");
            }
            for(DataStandardVo dataVo : voList) {
                this.createTablesExecute(dataVo, conn, dataSourceVos.get(0));
            }
        }catch (Exception e) {
            LOGGER.error("创建数据表异常!", e);
            throw new OrdinaryException("创建数据表异常!");
        }finally {
            connectDB.closeDbConn(conn);
        }
        return true;
    }
    
    private boolean createTablesExecute(DataStandardVo dataVo, Connection conn, DatasourceVO datasourceVo) throws SQLException{
        List<DataStandardItemVo> itemVos = this.dataStandardItemMapper.queryItemListBy(dataVo.getCode(), dataVo.getSourceId());
        if(itemVos == null || itemVos.isEmpty()) {
            LOGGER.error("表["+dataVo.getTableName()+"]没有元数据信息，不创建表！数据子类"+dataVo.getCode()+"/"+dataVo.getName());
            return true;
        }
        int dbType = datasourceVo.getDbType();
        String tableName = dataVo.getTableName();
        String sql = "";
        if (dbType == 1) {
            sql = "SELECT COLUMN_NAME  FROM INFORMATION_SCHEMA. COLUMNS WHERE table_name = '" + tableName + "' AND table_schema = '" + datasourceVo.getDbName() + "'";
        } else if (dbType == 2) {
            //oracle
            sql = "select  COLS.COLUMN_NAME COLUMN_NAME from user_tab_cols COLS   where COLS.TABLE_NAME='" + tableName + "'";
        } else if (dbType == 3) {
            //sqlserver
            sql = "SELECT  CAST (col.name AS NVARCHAR(128)) COLUMN_NAME FROM sys.objects obj  WHERE obj.name ='" + tableName + "'";
        }
        //判断表是否存在
        LOGGER.info(sql);
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            this.dropTable(tableName, conn, dbType);
            LOGGER.error("表["+dataVo.getTableName()+"]删除成功，不创建表！数据子类"+dataVo.getCode()+"/"+dataVo.getName());
        }
        rs.close();
        ps.close();
        sql = this.concatCreateTableSql(dataVo, datasourceVo, itemVos);
        LOGGER.info("表["+dataVo.getTableName()+"]创建开始。"+sql);
        ps = conn.prepareStatement(sql);
        ps.execute();
        LOGGER.error("表["+dataVo.getTableName()+"]创建成功！数据子类"+dataVo.getCode()+"/"+dataVo.getName());
        ps.close();
        return true;
    }
    
    private boolean dropTable(String tableName, Connection connection, int dbType) throws SQLException{
        PreparedStatement ps = null;
        String sql = "drop table " + tableName;
        if(dbType == 1 ) {
            sql = "drop table if exists " + tableName;
        }
        try {
            ps = connection.prepareStatement(sql);
            ps.execute();
        } finally {
            if(ps!=null) {
                ps.close();
            }
        }
        return true;
    }
    
    private String concatCreateTableSql(DataStandardVo dataVo, DatasourceVO datasourceVo, List<DataStandardItemVo> itemVos) {
        String tableName = dataVo.getTableName();
        int dbType = datasourceVo.getDbType();
        if(dbType != 1) {
            throw new OrdinaryException("未支持的中心库数据库类型["+dbType+"]");
        }
        StringBuffer sb = new StringBuffer(" create table ").append(tableName).append(" ( ");
        int ii=0;
        for (DataStandardItemVo itt : itemVos) {
            if(ii>0) {
                sb.append(",\n");
            }
            sb.append(itt.getItemName()).append(" ");
            sb.append(getColumnType(itt.getDataType(), dbType, itt.getDataLength()));
            sb.append(itt.getDataNullable() == 0 ? " not null ": " ");
            sb.append(itt.getDataPrimarykey() == 1 ? " PRIMARY KEY " : " ");
            sb.append(getComment(dbType, itt.getItemComment()));
            ii++;
        }
        sb.append(")");
        if (dbType == 1) {
            sb.append(" ENGINE=InnoDB \n" +
                    " DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci\n"+
                    " COMMENT='" + dataVo.getName() + "'");
        }
        if (dbType == 2) {
            for (DataStandardItemVo itt : itemVos) {
                sb.append("  comment on column " + tableName + "." + itt.getItemName() + " is '" + itt.getItemComment() + "'; ");
            }
        }
        if (dbType == 3) {
            for (DataStandardItemVo itt : itemVos) {
                sb.append(" EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'" + itt.getItemComment() + "' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'" + tableName + "', @level2type=N'COLUMN',@level2name=N'" + itt.getItemName() + "'\n" +
                        "GO \n");
            }
        }
        return sb.toString();
        
        
    }
    
    
    public static String getColumnType(String dataType, int dbType, String dataLength) {
        String columnType = "";
        String lengths[] = dataLength.split(",");
        String len = "("+lengths[0]+")";
        if (dbType == 1) {
            switch (dataType) {
                case "C":
                    columnType = "varchar"+len;
                    break;
                case "D":
                    columnType = "datetime";
                    break;
                case "N":
                    columnType = getDataTypeForNumber(dataType, dataLength, dbType);
                    break;
                case "M":
                    columnType = getDataTypeForNumber(dataType, dataLength, dbType);
                    break;
                case "B":
                    columnType = "blob";
                    break;
                case "T":
                    columnType = "text";
                    break;
                case "L":
                    columnType = "varchar"+len;
                    break;
            }
        } else if (dbType == 2) {
            switch (dataType) {
                case "C":
                    columnType = "varchar2";
                    break;
                case "D":
                    columnType = "date";
                    break;
                case "N":
                    columnType = getDataTypeForNumber(dataType, dataLength, dbType);
                    break;
                case "M":
                    columnType = getDataTypeForNumber(dataType, dataLength, dbType);
                    break;
                case "B":
                    columnType = "blob";
                    break;
                case "T":
                    columnType = "clob";
                    break;
                case "L":
                    columnType = "varchar2";
                    break;
            }
        } else {
            switch (dataType) {
                case "C":
                    columnType = "varchar";
                    break;
                case "D":
                    columnType = "datetime";
                    break;
                case "N":
                    columnType = getDataTypeForNumber(dataType, dataLength, dbType);
                    break;
                case "M":
                    columnType = getDataTypeForNumber(dataType, dataLength, dbType);
                    break;
                case "B":
                    columnType = "blob";
                    break;
                case "T":
                    columnType = "text";
                    break;
                case "L":
                    columnType = "varchar";
                    break;
            }
        }
        return columnType;
    }  
    
    public static String getComment(int dbType, String comment) {
        String result = "";
        if (dbType == 1) {
            result = "  comment '" + comment + "' ";
        }
        return result;
    }
    
    private static String getDataTypeForNumber(String dataType, String dataLength, int dbType) {
        if(dataLength==null || dataLength.trim().isEmpty()) {
            dataLength = "10";
        }
        if("M".endsWith(dataType)) {
            //金额，强制为decmial类型
            if(!dataLength.contains(",")) {
                dataLength += ",0";
            }
        }
        String lengths[] = dataLength.split(",");
        int left = Integer.parseInt(lengths[0]);
        String type="";
        if(dbType==1 || dbType == 3) {
            if (lengths.length > 1) {
                if(left > 65) {
                    left = 65;
                }
                int right = Integer.parseInt(lengths[1]);
                if(right > left) {
                    right = left;
                }
                type = "decimal("+left+","+ right +")";
            }else {
                if(left <= 10) {
                    type = "int("+left+")"; 
                }else if(left <= 19) {
                    type = "bigint("+left+")"; 
                }else {
                    if(left > 65) {
                        left = 65;
                    }
                    type = "decimal("+left+",0)";
                }
            }
        }
        return type;
    }
}
