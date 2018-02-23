package com.gtafe.data.center.dataetl.trans;

import com.gtafe.data.center.dataetl.datatask.vo.rule.ConvertRuleSplitfieldtorows;
import com.gtafe.data.center.dataetl.datatask.vo.rule.rulevo.SplitfieldtorowsVo;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.steps.splitfieldtorows.SplitFieldToRowsMeta;

import java.io.IOException;

/**
 * 列转行
 */
public class SplitFieldToRows extends BaseStep{

    String stepstr;

    public SplitFieldToRows(int locationX, int locattionY, String name, String stepstr) {
        super(locationX, locattionY, name);
        this.stepstr = stepstr;
    }

    public StepMeta ftrStep() {

        SplitfieldtorowsVo splitfieldtorowsVo;

        try {
            splitfieldtorowsVo=mapper.readValue(stepstr, ConvertRuleSplitfieldtorows.class).getDataList().get(0);
        } catch (IOException e) {
            return null;
        }

        SplitFieldToRowsMeta splitFieldToRowsMeta=new SplitFieldToRowsMeta();

        splitFieldToRowsMeta.setSplitField(splitfieldtorowsVo.getSourceField());
        splitFieldToRowsMeta.setDelimiter(splitfieldtorowsVo.getDelimiter());
        splitFieldToRowsMeta.setNewFieldname(splitfieldtorowsVo.getTargetField());

        return initStep(splitFieldToRowsMeta);

    }
}
