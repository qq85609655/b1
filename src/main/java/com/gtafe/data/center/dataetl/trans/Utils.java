package com.gtafe.data.center.dataetl.trans;

import com.gtafe.data.center.dataetl.datasource.vo.DatasourceVO;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.trans.TransMeta;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 工具类
 */
public class Utils {

    /**
     * 生成元数据库
     * @param ds
     * @return
     */
     static DatabaseMeta InitDatabaseMeta(DatasourceVO ds) {

        DatabaseMeta databaseMeta;

        switch (ds.getDbType()) {
            case 1:
                databaseMeta=new DatabaseMeta(ds.getName(), "MYSQL", "Native", ds.getHost(), ds.getDbName(), Integer.toString(ds.getPort()), ds.getUsername(), ds.getPassword());
                databaseMeta.addExtraOption("MYSQL","useSSL","false");
                databaseMeta.addExtraOption("MYSQL","characterEncoding","utf8");
                break;
            case  2:
                databaseMeta=new DatabaseMeta(ds.getName(), "ORACLE", "Native", ds.getHost(),"\""+ ds.getDbName()+"\"", Integer.toString(ds.getPort()), ds.getUsername(), ds.getPassword());
                break;
            case  3:
                databaseMeta=new DatabaseMeta(ds.getName(), "MSSQL", "Native", ds.getHost(), ds.getDbName(), Integer.toString(ds.getPort()), ds.getUsername(), ds.getPassword());
                break;
            default:
                databaseMeta=new DatabaseMeta(ds.getName(), "MYSQL", "Native", ds.getHost(), ds.getDbName(), Integer.toString(ds.getPort()), ds.getUsername(), ds.getPassword());
                databaseMeta.addExtraOption("MYSQL","useSSL","false");
                databaseMeta.addExtraOption("MYSQL","characterEncoding","utf8");
        }

        return  databaseMeta;

    }


    /**
     * kettle任务生成ktr
     * @param transMeta
     * @throws IOException
     * @throws KettleException
     */
    static   void outputktr(TransMeta transMeta) {

        try {
            String xml = transMeta.getXML();
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(new File("d:\\kettle-file.ktr")));
            dos.write(xml.getBytes("UTF-8"));
            dos.close();
        } catch (Exception e) {
            return;
        }

    }

}
