package com.gtafe.data.center.dataetl.trans;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gtafe.data.center.dataetl.datasource.utils.ConnectDB;
import com.gtafe.data.center.dataetl.datasource.vo.DatasourceVO;
import com.gtafe.data.center.dataetl.datatask.vo.rule.rulevo.DynamicValueMappingVo;
import com.gtafe.data.center.system.config.vo.SysConfigVo;
import com.gtafe.framework.base.utils.PropertyUtils;
import com.gtafe.framework.base.utils.StringUtil;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.trans.TransMeta;
import org.springframework.util.StringUtils;

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
import java.util.Locale;

/**
 * 工具类
 */
public class Utils {
    public static List getStepInfo(String stepstr) {

        List stepInfo = new ArrayList();
        ObjectMapper mapper = new ObjectMapper();
        try {
            stepInfo.add(mapper.readTree(stepstr).get("id").asInt());
            stepInfo.add(mapper.readTree(stepstr).get("name").asText());
            stepInfo.add(mapper.readTree(stepstr).get("type").asInt());
        } catch (IOException e) {
            return null;
        }
        return stepInfo;
    }

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
     * @param taskName
     * @param transMeta
     * @throws IOException
     * @throws KettleException
     */
    static void outputktr(String taskName, TransMeta transMeta, String kettleFilePath) {
        try {
            String xml = transMeta.getXML();
            if (!StringUtil.isNotBlank(kettleFilePath)) {
                kettleFilePath = "d:";
            }
            String filePath = kettleFilePath + File.separator + taskName + ".ktr";
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(new File(filePath)));

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
            String sql = "select distinct " + dynamicValueMappingVo.getTargetFieldName() + " from  " + dynamicValueMappingVo.getReleaseTableName() + " ";
            System.out.println("1====" + sql);
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
                    " from  " + dynamicValueMappingVo.getReleaseTableName() + " t where  t." + dynamicValueMappingVo.getReleaseFieldName() + "  =  " + str;
            System.out.println("2====" + sql);
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                res = rs.getString(1);
                System.out.println("res====" + res);
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
