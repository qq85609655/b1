package com.gtafe.data.center.dataetl.trans;

import com.gtafe.data.center.dataetl.datasource.utils.ConnectDB;
import com.gtafe.data.center.dataetl.datasource.vo.DatasourceVO;
import com.gtafe.framework.base.utils.PropertyUtils;
import com.gtafe.framework.base.utils.StringUtil;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.steps.tableinput.TableInputMeta;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * 表输入
 */
public class InputTable extends BaseStep {

    DatasourceVO ds;

    String sourceTableName;

    int taskId;

    public InputTable(int locationX, int locattionY, String name, DatasourceVO ds, String sourceTableName, int taskId) {
        super(locationX, locattionY, name);
        this.ds = ds;
        this.sourceTableName = sourceTableName;
        this.taskId = taskId;
    }

    public StepMeta inputStep() {

        String B_READ_PART = PropertyUtils.getProperty("config.properties", "B_READ_PART");
        String filter_detail = PropertyUtils.getProperty("config.properties", "filter_detail");
        String filter_by = PropertyUtils.getProperty("config.properties", "filter_by");
        String sort_detail = PropertyUtils.getProperty("config.properties", "sort_detail");
        String sort_mode = PropertyUtils.getProperty("config.properties", "sort_mode");
        TableInputMeta tableInput = new TableInputMeta();
        tableInput.setDatabaseMeta(Utils.InitDatabaseMeta(ds));

        String selectSQL;
        if (ds.getDbType() == 2) {
            selectSQL = "SELECT *  FROM  \"" + sourceTableName + "\"";
        } else {
            selectSQL = "SELECT *  FROM " + sourceTableName.toLowerCase();
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

        System.out.println(selectSQL);
        //
      //  showData(selectSQL, ds);
        //

        tableInput.setSQL(selectSQL);

        return initStep(tableInput);

    }

    private void showData(String selectSQL, DatasourceVO ds) {
        Statement stmt = null;
        ConnectDB dbconn = StringUtil.getEntityBy(ds);
        Connection connection = dbconn.getConn();
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(selectSQL);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
