package com.gtafe.data.center.dataetl.trans;

import com.gtafe.data.center.dataetl.datatask.vo.rule.ConvertRuleUniqueRow;
import com.gtafe.data.center.dataetl.datatask.vo.rule.rulevo.UniquerowVo;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.steps.uniquerowsbyhashset.UniqueRowsByHashSetMeta;

import java.io.IOException;
import java.util.List;

public class UniqueRowsByHashSet extends BaseStep {

    String stepstr;

    public UniqueRowsByHashSet(int locationX, int locattionY, String name, String stepstr) {
        super(locationX, locattionY, name);
        this.stepstr = stepstr;
    }

    public StepMeta unique() {

        List<UniquerowVo> uniquerowVos;

        try {
            uniquerowVos=mapper.readValue(stepstr, ConvertRuleUniqueRow.class).getDataList();
        } catch (IOException e) {
            return null;
        }
        String[] compareFields=new String[uniquerowVos.size()];

        for(int i=0;i<uniquerowVos.size();i++) {
            compareFields[i]=uniquerowVos.get(i).getSourceField();
        }

        UniqueRowsByHashSetMeta uniqueRowsByHashSetMeta=new UniqueRowsByHashSetMeta();

        uniqueRowsByHashSetMeta.setCompareFields(compareFields);
        uniqueRowsByHashSetMeta.setErrorDescription("Duplicate");
        uniqueRowsByHashSetMeta.setRejectDuplicateRow(true);
        uniqueRowsByHashSetMeta.setStoreValues(false);

        return initStep(uniqueRowsByHashSetMeta);

    }
}
