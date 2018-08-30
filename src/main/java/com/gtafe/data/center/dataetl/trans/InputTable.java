package com.gtafe.data.center.dataetl.trans;

import com.gtafe.data.center.dataetl.datasource.utils.ConnectDB;
import com.gtafe.data.center.dataetl.datasource.vo.DatasourceVO;
import com.gtafe.framework.base.exception.OrdinaryException;
import com.gtafe.framework.base.utils.DateUtil;
import com.gtafe.framework.base.utils.PropertyUtils;
import com.gtafe.framework.base.utils.StringUtil;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.steps.tableinput.TableInputMeta;

import java.sql.*;
import java.util.Date;


/**
 * 表输入
 */
public class InputTable extends BaseStep {

    DatasourceVO ds;

    DatasourceVO targetDs;

    int busType;

    String sourceTableName;

    String tType;

    String sqlContent;

    int taskId;

    public InputTable(int locationX, int locattionY, String name, DatasourceVO ds, String sourceTableName, int taskId, String tType, String sqlContent, int busType, DatasourceVO targetDs) {
        super(locationX, locattionY, name);
        this.ds = ds;
        this.sourceTableName = sourceTableName;
        this.taskId = taskId;
        this.tType = tType;
        this.sqlContent = sqlContent;
        this.busType = busType;
        this.targetDs = targetDs;
    }


    public StepMeta inputStep() {

        String B_READ_PART = PropertyUtils.getProperty("config.properties", "B_READ_PART");
        String filter_detail = PropertyUtils.getProperty("config.properties", "filter_detail");
        String filter_by = PropertyUtils.getProperty("config.properties", "filter_by");
        String sort_detail = PropertyUtils.getProperty("config.properties", "sort_detail");
        String sort_mode = PropertyUtils.getProperty("config.properties", "sort_mode");
        TableInputMeta tableInput = new TableInputMeta();
        tableInput.setDatabaseMeta(Utils.InitDatabaseMeta(ds));
        String B_ShowData = PropertyUtils.getProperty("config.properties", "B_SHOW_DATA");
        String fb_kq = PropertyUtils.getProperty("config.properties", "fb_kq");
        String dy_kq = PropertyUtils.getProperty("config.properties", "dy_kq");
        String t_kq_name_centerdb = PropertyUtils.getProperty("config.properties", "t_kq_name_centerdb");
        String t_kq_name_kq = PropertyUtils.getProperty("config.properties", "t_kq_name_kq");


        String selectSQL;
        //如果这边是用户自定义的， 那就不需要再从表或视图查询了
        //直接 根据 查询脚本语句来了
        if (tType.equals("U")) {
            System.out.println("U==========" + sqlContent);
            selectSQL = sqlContent;
        } else {
            if (ds.getDbType() == 2) {
                selectSQL = "SELECT *  FROM  " + sourceTableName;
            } else {
                selectSQL = "SELECT *  FROM " + sourceTableName.toLowerCase();
            }

        }
        if (B_ShowData.equals("Y")) {
            showDataInfo(selectSQL);
        }
        /**
         * 针对考勤比较特殊， 需要在发布或订阅时候 取到 目标表里面 最大的登入时间 ，然后 再取源数据的时候 取 登入时间 大于 最大登入时间的数据。
         * 这就是类似于 增量更新的意思。
         */
        //需要知道 当前是 发布 或者 订阅
        if (busType == 1) {
            System.out.println("发布任务..");
            if (StringUtil.isNotBlank(fb_kq)) {
                String[] s = fb_kq.split("#");
                int tdid = Integer.parseInt(s[0]);
                int field = Integer.parseInt(s[1]);
                Date maxLoginDate = new Date();
                if (this.taskId == tdid) { //如果是考勤的。。
                    String maxDate = "";//取到中心库表中
                    if (this.targetDs != null) {
                        ConnectDB connectDB = StringUtil.getEntityBy(targetDs);
                        Connection connection = connectDB.getConn();
                        if (connection != null) {
                            String sql_maxDate_kq_centerdb = "select max(" + field + ") maxlogindate from " + t_kq_name_centerdb;
                            Statement st = null;
                            try {
                                st = connection.createStatement();
                                ResultSet rs = st.executeQuery(sql_maxDate_kq_centerdb);
                                while (rs.next()) {
                                    maxLoginDate = rs.getDate(1);
                                }
                                connectDB.closeDbConn(connection);
                                st.close();
                                rs.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            } finally {
                                connectDB.closeDbConn(connection);
                            }
                        }
                    }
                    maxDate = DateUtil.format(maxLoginDate, "yyyy-MM-dd HH:mm:ss");
                    System.out.println("当前目标中心 库 考勤表 里面 的 最大登入时间是：" + maxDate);
                    selectSQL += " where  to_char(" + field + ",'yyyy-MM-dd HH:mm:ss')  > " + maxDate;
                }
            }
        } else {
            System.out.println("订阅任务..");
            if (StringUtil.isNotBlank(dy_kq)) {
                String[] s = fb_kq.split("#");
                int tdid = Integer.parseInt(s[0]);
                int field = Integer.parseInt(s[1]);
                Date maxLoginDate = new Date();
                if (this.taskId == tdid) {
                    String maxDate = "";//取到教务这边考勤系统的表
                    if (this.targetDs != null) {
                        ConnectDB connectDB = StringUtil.getEntityBy(targetDs);
                        Connection connection = connectDB.getConn();
                        if (connection != null) {
                            //LOGIN_TIME
                            String sql_maxDate_kq = "select max(" + field + ") maxlogindate from " + t_kq_name_kq;
                            Statement st = null;
                            try {
                                st = connection.createStatement();
                                ResultSet rs = st.executeQuery(sql_maxDate_kq);
                                while (rs.next()) {
                                    maxLoginDate = rs.getDate(1);
                                }
                                connectDB.closeDbConn(connection);
                                st.close();
                                rs.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            } finally {
                                connectDB.closeDbConn(connection);
                            }
                        }
                    }
                    maxDate = DateUtil.format(maxLoginDate, "yyyy-MM-dd HH:mm:ss");
                    System.out.println("当前教务系统里面  考勤表 里面 的 最大登入时间是：" + maxDate);
                    selectSQL += " where  to_char(" + field + ",'yyyy-MM-dd HH:mm:ss')  > " + maxDate;
                }
            }
        }

        //说明开启了
        if (StringUtil.isNotBlank(B_READ_PART) && B_READ_PART.equals("Y")) {
            if (StringUtil.isNotBlank(filter_detail) && StringUtil.isNotBlank(filter_by)) {
                String[] s = filter_detail.split("#");
                for (String ss : s) {
                    String[] tid = ss.split("_");
                    int tdid = Integer.parseInt(tid[0]);
                    int filter = Integer.parseInt(tid[1]);
                    //判断当前taskid 与配置的 taskid 是否一致
                    if (this.taskId == tdid) {
                        selectSQL = selectSQL + " where " + filter_by + " = " + filter;
                    }
                }
            }
        }

        if (StringUtil.isNotBlank(sort_detail) && StringUtil.isNotBlank(sort_mode)) {
            String[] s = sort_detail.split("#");
            for (String ss : s) {
                String[] tid = ss.split("_");
                int tdid = Integer.parseInt(tid[0]);
                String sort = tid[1];
                //判断当前taskid 与配置的 taskid 是否一致
                if (this.taskId == tdid) {
                    selectSQL = selectSQL + " order by  " + sort + "   " + sort_mode;
                }
            }
        }
        tableInput.setSQL(selectSQL);
        return initStep(tableInput);
    }

    private void showDataInfo(String selectSQL) {
        System.out.println(selectSQL);
        ConnectDB connectDB = StringUtil.getEntityBy(ds);
        Connection connection = connectDB.getConn();
        if (connection == null) {
            throw new OrdinaryException("无法取得连接");
        }
        try {
            PreparedStatement pst = connection.prepareStatement(selectSQL);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getString(1));
            }
            rs.close();
            pst.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connectDB.closeDbConn(connection);
        }
    }

}
