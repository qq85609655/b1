package com.gtafe.framework.base.listener;

import com.gtafe.data.center.dataetl.datasource.service.IDatasourceService;
import com.gtafe.data.center.dataetl.datasource.utils.ConnectDB;
import com.gtafe.data.center.dataetl.datasource.vo.DatasourceVO;
import com.gtafe.data.center.dataetl.datatask.service.DataTaskService;
import com.gtafe.data.center.dataetl.datatask.vo.TransFileVo;
import com.gtafe.data.center.system.config.service.SysConfigService;
import com.gtafe.data.center.system.config.vo.SysConfigVo;
import com.gtafe.framework.base.utils.DateUtil;
import com.gtafe.framework.base.utils.PropertyUtils;
import com.gtafe.framework.base.utils.ReadFileUtil;
import com.gtafe.framework.base.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * 系统组件加载完毕后 检测一些系统配置项： 中心库是否有配置
 *
 * @author 周刚
 * 2017-10-26
 */
@Component("DataSourceCheckListener")
public class SpringContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringContextRefreshedListener.class);

    private static final String CENTERDB_LOCALNAME_DEFAULT = PropertyUtils.getProperty("database.properties", "db.jdbc.logdbname");
    private static final String CENTERDB_NAME_DEFAULT = PropertyUtils.getProperty("database.properties", "db.jdbc.centerDbName");
    private static final String CENTERDB_TYPE_DEFAULT = "1";//默认mysql数据库
    private static final String CENTERDB_USERNAME_DEFAULT = PropertyUtils.getProperty("database.properties", "db.jdbc.logusername");//默认mysql数据库
    private static final String CENTERDB_PASSWORD_DEFAULT = PropertyUtils.getProperty("database.properties", "db.jdbc.logpassword");//默认mysql数据库
    private static final String CENTERDB_PORT_DEFAULT = PropertyUtils.getProperty("database.properties", "db.jdbc.logport");//默认mysql数据库
    private static final String CENTERDB_IP_DEFAULT = PropertyUtils.getProperty("database.properties", "db.jdbc.logip");
    @Resource
    private IDatasourceService datasourceServiceImpl;
    @Resource
    private SysConfigService sysConfigServiceImpl;

    /**
     * dbType:data.dbType,
     * dbName: data.dbName,
     * tableSpaces: data.tableSpaces,
     * port: data.port,
     * username: data.username,
     * password: data.password,
     * ipAddress: data.ipAddress
     */
    private void initCenterDb() throws SQLException {
        //首先看是否配置中心库链接。如果没有 则配置系统数据库 创建一个新的数据库center_db;
        //并将配置信息保存至数据库中。
        boolean f = false;
        SysConfigVo vo = this.sysConfigServiceImpl.queryCenterDbInfo();
        if (vo != null) {
            String dbType = vo.getDbType();
            //所有的几个关键参数都不能为空
            if (StringUtil.isNotBlank(dbType) && StringUtil.isNotBlank(vo.getIpAddress()) && StringUtil.isNotBlank(vo.getPassword())
                    && StringUtil.isNotBlank(vo.getDbName()) && StringUtil.isNotBlank(vo.getPort()) && StringUtil.isNotBlank(vo.getUsername())) {
                if (dbType.equals("2")) {
                    if (StringUtil.isNotBlank(vo.getTableSpaces())) {

                    }
                }
                //根据配置 检查中心库是否可用？如果不可用 就默认以当前数据库 创建一个库
                ConnectDB connectDB = StringUtil.getEntityBySysConfig(vo);
                if (connectDB.getConn() != null) {
                    f = true;
                }
            }
        }
        if (!f) {
            boolean b = this.initCenterDbInfo();
            if (b) {
                LOGGER.info("创建本地的 中心库 成功!");
            }
        }
    }

    private boolean initCenterDbInfo() throws SQLException {
        SysConfigVo vo = new SysConfigVo();
        vo.setDbName(CENTERDB_LOCALNAME_DEFAULT);
        vo.setDbType(CENTERDB_TYPE_DEFAULT);
        vo.setPassword(CENTERDB_PASSWORD_DEFAULT);
        vo.setUsername(CENTERDB_USERNAME_DEFAULT);
        vo.setIpAddress(CENTERDB_IP_DEFAULT);
        vo.setPort(CENTERDB_PORT_DEFAULT);
        boolean flag = false;
        //读取系统配置文件 取得数据库链接
        //1 创建数据库
        ConnectDB connectDB = StringUtil.getEntityBySysConfig(vo);
        String databaseSql = "create database " + CENTERDB_NAME_DEFAULT;
        Connection connection = connectDB.getConn();
        if (connection != null) {
            Statement smt = connection.createStatement();
            smt.executeUpdate(databaseSql);
            connectDB.driver = "com.mysql.jdbc.Driver";
            connectDB.url = "jdbc:mysql://" + vo.getIpAddress() + ":"
                    + vo.getPort() + "/"
                    + CENTERDB_NAME_DEFAULT;
            connectDB.username = vo.getUsername();
            connectDB.pwd = vo.getPassword();
            Connection newConn = connectDB.getConn();
            if (newConn != null) {
                LOGGER.info("默认的中心库已经创建成功了..");
            }
        }
        vo.setDbName(CENTERDB_NAME_DEFAULT);
        LOGGER.info(vo.toString());
        //写入sysconfig 表
        this.sysConfigServiceImpl.saveCenterDbConfig(vo);
        return flag;
    }

    @Autowired
    DataTaskService dataTaskServiceImpl;

    /**
     * 当一个ApplicationContext被初始化或刷新触发
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            this.sysConfigServiceImpl.flushSystemInfo(true);
            this.initCenterDb();
            this.initKfiles();
        } catch (Exception e) {
            LOGGER.error("系统信息初始化异常!", e);
            System.exit(1);
        }
    }

    private void initKfiles() {
        //读取 ktr文件  kjb文件 存入 数据库中，
        SysConfigVo vo = this.sysConfigServiceImpl.getBasicSysConfigVO();
        String ktrPath = vo.getKtrFilesPath();
        String kjbPath = vo.getKjbFilesPath();

        if (!StringUtil.isNotBlank(ktrPath)) {
            LOGGER.info("没有配置本地ktrPath路径 ");
        }
        System.out.println("扫描ktr文件目录" + ktrPath + "操作开始");
       dataTaskServiceImpl.flushTransFileVo(ktrPath, "ktr");

        if (!StringUtil.isNotBlank(kjbPath)) {
            LOGGER.info("没有配置本地kjbPath路径 ");
        }
        System.out.println("扫描kjb文件目录" + kjbPath + "操作开始");
        dataTaskServiceImpl.flushTransFileVo(kjbPath, "kjb");
    }
}
