package com.gtafe.data.center.dataetl.trans;

import com.gtafe.data.center.dataetl.datatask.vo.rule.ConvertRuleAddConstant;
import com.gtafe.data.center.dataetl.datatask.vo.rule.rulevo.AddConstantVo;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.steps.constant.ConstantMeta;

import java.io.IOException;
import java.util.List;

/**
 * 增加常量
 */
public class Constant extends BaseStep {

    String stepstr;

    public Constant(int locationX, int locattionY, String name, String stepstr) {
        super(locationX, locattionY, name);
        this.stepstr = stepstr;
    }

    public StepMeta constantStep() {

        List<AddConstantVo> addConstantVos;

        try {
            addConstantVos=mapper.readValue(stepstr, ConvertRuleAddConstant.class).getDataList();
        } catch (IOException e) {
            return null;
        }

        ConstantMeta constantMeta=new ConstantMeta();
        constantMeta.allocate(addConstantVos.size());

        String[] fieldName=new String[addConstantVos.size()];
        String[] fieldType=new String[addConstantVos.size()];
        String[] value=new String[addConstantVos.size()];
        String[] fieldFormat=new String[addConstantVos.size()];
        int[] precision=new int[addConstantVos.size()];

        for(int i=0;i<addConstantVos.size();i++) {
            fieldName[i]=addConstantVos.get(i).getTargetField();
            fieldType[i]=addConstantVos.get(i).getValType();
            value[i]=addConstantVos.get(i).getToValue();
            fieldFormat[i]=addConstantVos.get(i).getFormatVal();
            precision[i]=8;
        }
        constantMeta.setFieldName(fieldName);
        constantMeta.setFieldType(fieldType);
        constantMeta.setValue(value);
        constantMeta.setFieldFormat(fieldFormat);
        constantMeta.setFieldPrecision(precision);
        return  initStep(constantMeta);
    }

    public StepMeta constantStep(String[] fieldName, String[] fieldType, String[] value) {

        ConstantMeta constantMeta=new ConstantMeta();
        constantMeta.allocate(value.length);
        constantMeta.setFieldName(fieldName);
        constantMeta.setFieldType(fieldType);
        constantMeta.setValue(value);

        return  initStep(constantMeta);

    }

}
