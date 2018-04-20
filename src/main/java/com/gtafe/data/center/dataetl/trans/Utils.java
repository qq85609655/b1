package com.gtafe.data.center.dataetl.trans;

import com.gtafe.data.center.dataetl.datasource.utils.ConnectDB;
import com.gtafe.data.center.dataetl.datasource.vo.DatasourceVO;
import com.gtafe.data.center.dataetl.datatask.vo.rule.rulevo.DynamicValueMappingVo;
import com.gtafe.framework.base.utils.StringUtil;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.trans.TransMeta;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * 工具类
 */
public class Utils {

    /**
     * 生成元数据库
     *
     * @param ds
     * @return
     */
    static DatabaseMeta InitDatabaseMeta(DatasourceVO ds) {

        DatabaseMeta databaseMeta;

        switch (ds.getDbType()) {
            case 1:
                databaseMeta = new DatabaseMeta(ds.getName(), "MYSQL", "Native", ds.getHost(), ds.getDbName(), Integer.toString(ds.getPort()), ds.getUsername(), ds.getPassword());
                databaseMeta.addExtraOption("MYSQL", "useSSL", "false");
                databaseMeta.addExtraOption("MYSQL", "characterEncoding", "utf8");
                break;
            case 2:
                databaseMeta = new DatabaseMeta(ds.getName(), "ORACLE", "Native", ds.getHost(), "\"" + ds.getDbName() + "\"", Integer.toString(ds.getPort()), ds.getUsername(), ds.getPassword());
                break;
            case 3:
                databaseMeta = new DatabaseMeta(ds.getName(), "MSSQL", "Native", ds.getHost(), ds.getDbName(), Integer.toString(ds.getPort()), ds.getUsername(), ds.getPassword());
                break;
            default:
                databaseMeta = new DatabaseMeta(ds.getName(), "MYSQL", "Native", ds.getHost(), ds.getDbName(), Integer.toString(ds.getPort()), ds.getUsername(), ds.getPassword());
                databaseMeta.addExtraOption("MYSQL", "useSSL", "false");
                databaseMeta.addExtraOption("MYSQL", "characterEncoding", "utf8");
        }

        return databaseMeta;

    }


    /**
     * kettle任务生成ktr
     *
     * @param transMeta
     * @throws IOException
     * @throws KettleException
     */
    static void outputktr(TransMeta transMeta) {
        try {
            String xml = transMeta.getXML();
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(new File("d:\\kettle-file.ktr")));
            dos.write(xml.getBytes("UTF-8"));
            dos.close();
        } catch (Exception e) {
            return;
        }

    }


    /**
     * 根据 相关参数获取列表 作为 值映射的 源值
     *
     * @param targetDs
     * @param dynamicValueMappingVo
     * @return
     * @throws SQLException
     */
    static List<String> querySourceListBy(DatasourceVO targetDs, DynamicValueMappingVo dynamicValueMappingVo) throws SQLException {
        List<String> sourceList = new ArrayList<String>();
        ConnectDB connectDB = StringUtil.getEntityBy(targetDs);
        Connection connection = connectDB.getConn();
        if (connection != null) {
            Statement st = connection.createStatement();
            String sql = "select " + dynamicValueMappingVo.getSourceField() + " from  " + dynamicValueMappingVo.getReleaseTableName() + " ";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                String str = (String) rs.getString(1);
                sourceList.add(str);
            }
            rs.close();
            st.close();
        }
        connectDB.closeDbConn(connection);
        return sourceList;
    }

    /**
     * 根据相关参数 查询 需要对应的目标值
     *
     * @param targetDs
     * @param dynamicValueMappingVo
     * @param str
     * @return
     * @throws SQLException
     */
    static String queryTargetStrBy(DatasourceVO targetDs, DynamicValueMappingVo dynamicValueMappingVo, String str) throws SQLException {
        String res = "";
        ConnectDB connectDB = StringUtil.getEntityBy(targetDs);
        Connection connection = connectDB.getConn();
        if (connection != null) {
            Statement st = connection.createStatement();
            String sql = "select distinct  t." + dynamicValueMappingVo.getTargetFieldName() + " " +
                    " from  " + dynamicValueMappingVo.getReleaseTableName() + " t where  t." + dynamicValueMappingVo.getReleaseFieldName() + "  " + dynamicValueMappingVo.getReleaseRalation() + str;
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                res = rs.getString(1);
                if (StringUtil.isNotBlank(str)) {
                    break;
                }
            }
            rs.close();
            st.close();
        }
        connectDB.closeDbConn(connection);
        return res;
    }


}
