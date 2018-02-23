package com.gtafe.data.center.dataetl.trans;

import com.gtafe.data.center.dataetl.datatask.vo.rule.ConvertRuleUniqueRow;
import com.gtafe.data.center.dataetl.datatask.vo.rule.rulevo.UniquerowVo;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.steps.uniquerows.UniqueRowsMeta;

import java.io.IOException;
import java.util.List;

/**
 * 去除重复记录
 */
public class UniqueRows  extends BaseStep{

    String stepstr;

    public UniqueRows(int locationX, int locattionY, String name, String stepstr) {
        super(locationX, locattionY, name);
        this.stepstr = stepstr;
    }

    public StepMeta uniqueStep() {

        List<UniquerowVo> uniquerowVos;

        try {
            uniquerowVos=mapper.readValue(stepstr, ConvertRuleUniqueRow.class).getDataList();
        } catch (IOException e) {
            return null;
        }

        String[] compareFields=new String[uniquerowVos.size()];
        boolean[] caseInsensitive=new boolean[uniquerowVos.size()];

        for(int i=0;i<uniquerowVos.size();i++) {
            compareFields[i]=uniquerowVos.get(i).getSourceField();
            if (uniquerowVos.get(i).getIgnore() == 1) {
                caseInsensitive[i] = true;
            } else {
                caseInsensitive[i]=false;
            }
        }

        UniqueRowsMeta uniqueRowsMeta=new UniqueRowsMeta();

        uniqueRowsMeta.setCompareFields(compareFields);
        uniqueRowsMeta.setCaseInsensitive(caseInsensitive);

        return initStep(uniqueRowsMeta);

    }
}
