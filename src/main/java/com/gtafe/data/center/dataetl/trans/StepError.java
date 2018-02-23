package com.gtafe.data.center.dataetl.trans;

import com.gtafe.data.center.dataetl.datasource.vo.DatasourceVO;
import org.pentaho.di.core.variables.Variables;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.steps.tableoutput.TableOutputMeta;

/**
 * 错误日志
 */
public class StepError extends BaseStep {

    private DatasourceVO ds;

    public StepError(int locationX, int locattionY, String name, DatasourceVO ds) {
        super(locationX, locattionY, name);
        this.ds = ds;
    }

    public StepMeta stepError() {

        TableOutputMeta logoutput = new TableOutputMeta();
        logoutput.setDatabaseMeta(Utils.InitDatabaseMeta(ds));

        logoutput.setTableName("kettle_error_log");
        logoutput.setCommitSize(100);
        logoutput.setSpecifyFields(true);
        String sourceFields[]=new String[]{"nr","description","field","code","channel_id_6666"};
        String targetFields[]=new String[]{"nr","description","field","code","channel_id"};

        logoutput.setFieldStream(sourceFields);
        logoutput.setFieldDatabase(targetFields);

        return initStep(logoutput);

    }

    public Variables variables() {
        Variables variable=new Variables();
        variable.setVariable("nr","nr");
        variable.setVariable("description","description");
        variable.setVariable("field","field");
        variable.setVariable("code","code");
        return variable;
    }
}
