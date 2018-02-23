package com.gtafe.data.center.dataetl.trans;

import com.gtafe.data.center.dataetl.datatask.vo.rule.ConvertRuleSplitfield;
import com.gtafe.data.center.dataetl.datatask.vo.rule.rulevo.SplitfieldVo;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.steps.fieldsplitter.FieldSplitterMeta;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 拆分字段
 */
public class Split  extends BaseStep {

    String stepstr;

    public Split(int locationX, int locattionY, String name, String stepstr) {
        super(locationX, locattionY, name);
        this.stepstr = stepstr;
    }

    public List<StepMeta> splitStep(String stepId) {

        List<SplitfieldVo> splitfieldVos;

        try {
            splitfieldVos=mapper.readValue(stepstr, ConvertRuleSplitfield.class).getDataList();
        } catch (IOException e) {
            return null;
        }

        List<StepMeta> stepMetas=new ArrayList<>();

        int k=0;

        for (SplitfieldVo splitfieldVo : splitfieldVos) {

            FieldSplitterMeta fieldSplitterMeta=new FieldSplitterMeta();
            fieldSplitterMeta.allocate(splitfieldVo.getSplitList().size());
            fieldSplitterMeta.setDelimiter(splitfieldVo.getDelimiter());
            fieldSplitterMeta.setSplitField(splitfieldVo.getSourceField()+"_add_"+stepId);
            fieldSplitterMeta.setEnclosure(null);
            String fields[]=new String[splitfieldVo.getSplitList().size()];
            int[] splitType=new int[splitfieldVo.getSplitList().size()];
            int[] trimType=new int[splitfieldVo.getSplitList().size()];
            for(int i=0;i<splitfieldVo.getSplitList().size();i++) {
                fields[i]=splitfieldVo.getSplitList().get(i).getTargetField();
                splitType[i]= ValueMetaInterface.TYPE_STRING;
                trimType[i]=splitfieldVo.getSplitList().get(i).getBlankType();
            }
            fieldSplitterMeta.setFieldName(fields);
            fieldSplitterMeta.setFieldType( splitType);
            fieldSplitterMeta.setFieldTrimType(trimType);

            name+=k;
            k++;
            stepMetas.add(initStep(fieldSplitterMeta));
            locationX+=100;
        }

        return  stepMetas;

    }

    public List<String> fieldName() {
        List<SplitfieldVo> splitfieldVos;
        try {
            splitfieldVos=mapper.readValue(stepstr, ConvertRuleSplitfield.class).getDataList();
        } catch (IOException e) {
            return null;
        }
        List<String> fieldNames=new ArrayList<>();

        for (SplitfieldVo splitfieldVo : splitfieldVos) {
            fieldNames.add(splitfieldVo.getSourceField());
        }
        return fieldNames;
    }
}
