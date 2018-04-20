package com.gtafe.data.center.dataetl.trans;

import com.gtafe.data.center.dataetl.datatask.vo.rule.ConvertRuleExecuteSql;
import com.gtafe.data.center.dataetl.datatask.vo.rule.rulevo.ExecuteSqlVo;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.steps.textfileoutput.TextFileField;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//执行sql 脚本
public class ExecuteSql extends BaseStep {
    String stepstr;
    int busType;

    public ExecuteSql(int locationX, int locattionY, String name, String stepstr) {
        super(locationX, locattionY, name);
        this.stepstr = stepstr;
    }

    public ExecuteSql(int locationX, int locattionY, String name, String stepstr,int busType) {
        super(locationX, locattionY, name);
        this.stepstr = stepstr;
        this.busType=busType;
    }

    public List<StepMeta> executeSqlStep() {
        List<ExecuteSqlVo> executeSqlVos;
        try {
            executeSqlVos = mapper.readValue(stepstr, ConvertRuleExecuteSql.class).getDataList();
        } catch (IOException e) {
            return null;
        }

        List<StepMeta> stepMetas = new ArrayList<>();
        int k = 0;
        for (ExecuteSqlVo executeSqlVo : executeSqlVos) {

        }

        return stepMetas;
    }


}
