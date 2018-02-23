package com.gtafe.data.center.dataetl.trans;

import com.gtafe.data.center.dataetl.datatask.vo.rule.ConvertRuleConcatfield;
import com.gtafe.data.center.dataetl.datatask.vo.rule.rulevo.ConcatfieldVo;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.steps.concatfields.ConcatFieldsMeta;
import org.pentaho.di.trans.steps.textfileoutput.TextFileField;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 合并
 */
public class Concat extends BaseStep {

    String stepstr;

    public Concat(int locationX, int locattionY, String name, String stepstr) {
        super(locationX, locattionY, name);
        this.stepstr = stepstr;
    }

    public List<StepMeta> concatStep() {

        List<ConcatfieldVo> concatFieldVOs;

        try {
            concatFieldVOs=mapper.readValue(stepstr, ConvertRuleConcatfield.class).getDataList();
        } catch (IOException e) {
            return null;
        }

        List<StepMeta> stepMetas=new ArrayList<>();

        int k=0;

        for (ConcatfieldVo concatFieldVO : concatFieldVOs) {

            ConcatFieldsMeta concatFieldsMeta=new ConcatFieldsMeta();
            concatFieldsMeta.setTargetFieldName(concatFieldVO.getTargetField());
            concatFieldsMeta.setSeparator(concatFieldVO.getDelimiter());
            TextFileField[] textFileFields=new TextFileField[concatFieldVO.getConcatFields().size()];
            for(int i=0;i<concatFieldVO.getConcatFields().size();i++) {
                textFileFields[i]=new TextFileField();
                textFileFields[i].setName(concatFieldVO.getConcatFields().get(i).getSourceField());
                textFileFields[i].setType(ValueMetaInterface.TYPE_STRING);
            }
            concatFieldsMeta.setOutputFields(textFileFields);

            name+=k;
            k++;
            stepMetas.add(initStep(concatFieldsMeta));
            locationX+=100;

        }

        return  stepMetas;
    }

}
