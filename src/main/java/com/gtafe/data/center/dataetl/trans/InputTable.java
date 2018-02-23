package com.gtafe.data.center.dataetl.trans;

import com.gtafe.data.center.dataetl.datasource.vo.DatasourceVO;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.steps.tableinput.TableInputMeta;

/**
 * 表输入
 */
public class InputTable extends BaseStep {

    DatasourceVO ds;

    String sourceTableName;

    public InputTable(int locationX, int locattionY, String name, DatasourceVO ds,  String sourceTableName) {
        super(locationX, locattionY, name);
        this.ds = ds;
        this.sourceTableName = sourceTableName;
    }

    public    StepMeta inputStep() {

        TableInputMeta tableInput = new TableInputMeta();
        tableInput.setDatabaseMeta(Utils.InitDatabaseMeta(ds));

        String selectSQL;
        if (ds.getDbType() == 2) {
            selectSQL = "SELECT *  FROM  \"" + sourceTableName+"\"";
        } else {
            selectSQL = "SELECT *  FROM " + sourceTableName.toLowerCase();
        }
        tableInput.setSQL(selectSQL);

        return initStep(tableInput);

    }

}
