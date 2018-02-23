package com.gtafe.data.center.dataetl.datasource.utils;

import java.sql.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectDB {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectDB.class);
    public String driver = "";
    public String url = "";
    public String username = "";
    public String pwd = "";

    public Connection getConn() {
        Connection conn = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, pwd);
        } catch (ClassNotFoundException e) {
            LOGGER.error("ClassNotFoundException!");
        } catch (SQLException e) {
            LOGGER.error("SQLException!");
        }
        return conn;
    }

    public void closeDbConn(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
             //   e.printStackTrace();
            }
        }
    }


}