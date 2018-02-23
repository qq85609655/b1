package com.gtafe.data.center.dataetl.trans;

import com.gtafe.data.center.dataetl.datatask.vo.rule.ConvertRuleStringCut;
import com.gtafe.data.center.dataetl.datatask.vo.rule.rulevo.StringCutVo;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.steps.stringcut.StringCutMeta;

import java.io.IOException;
import java.util.List;

/**
 * 字符串剪切
 */
public class StringCut extends BaseStep{

    String stepstr;


    public StringCut(int locationX, int locattionY, String name, String stepstr) {
        super(locationX, locattionY, name);
        this.stepstr = stepstr;
    }

    public StepMeta strCut() {

        StringCutMeta stringCutMeta=new StringCutMeta();

        List<StringCutVo> stringCutVos=null;

        try {
            stringCutVos=mapper.readValue(stepstr, ConvertRuleStringCut.class).getDataList();
        } catch (IOException e) {
            return null;
        }

        String[] sourceFields=new String[stringCutVos.size()];
        String[] targetFields=new String[stringCutVos.size()];
        String[] startPosition=new String[stringCutVos.size()];
        String[] endPosition=new String[stringCutVos.size()];

        for (int i=0;i<stringCutVos.size();i++) {
            sourceFields[i]= stringCutVos.get(i).getSourceField();
            targetFields[i]= stringCutVos.get(i).getTargetField();
            startPosition[i]= String.valueOf(stringCutVos.get(i).getStartPosition());
            endPosition[i]= String.valueOf(stringCutVos.get(i).getEndPosition());
        }

        stringCutMeta.allocate(stringCutVos.size());
        stringCutMeta.setFieldInStream(sourceFields);
        stringCutMeta.setFieldOutStream(targetFields);
        stringCutMeta.setCutFrom(startPosition);
        stringCutMeta.setCutTo(endPosition);

        return initStep(stringCutMeta);

    }
}
