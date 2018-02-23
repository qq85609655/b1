package com.gtafe.data.center.dataetl.trans;

import com.gtafe.data.center.dataetl.datatask.vo.rule.ConvertRuleValuemapper;
import com.gtafe.data.center.dataetl.datatask.vo.rule.rulevo.ValuemapperVo;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.steps.valuemapper.ValueMapperMeta;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 值映射
 */
public class ValueMapper  extends BaseStep{

    String stepstr;

    public ValueMapper(int locationX, int locattionY, String name, String stepstr) {
        super(locationX, locattionY, name);
        this.stepstr = stepstr;
    }

    public  List<StepMeta> valueMapperStep() {

        List<ValuemapperVo> valuemapperVos;

        List<StepMeta> stepMetas=new ArrayList<>();

        try {
            valuemapperVos=mapper.readValue(stepstr, ConvertRuleValuemapper.class).getDataList();
        } catch (IOException e) {
            return null;
        }

        int k=0;

        for (ValuemapperVo valuemapperVo : valuemapperVos) {

            ValueMapperMeta valueMapperMeta=new ValueMapperMeta();
            valueMapperMeta.setFieldToUse(valuemapperVo.getSourceField());
            if (valuemapperVo.getTargetField() != null) {
                valueMapperMeta.setTargetField(valuemapperVo.getTargetField());
            }
            if (valuemapperVo.getDefValue() != null) {
                valueMapperMeta.setNonMatchDefault(valuemapperVo.getDefValue());
            }

            String[] sourcestr=new String[valuemapperVo.getMappings().size()];
            String[] targetstr=new String[valuemapperVo.getMappings().size()];

            for(int i=0;i<valuemapperVo.getMappings().size();i++) {
                sourcestr[i]=valuemapperVo.getMappings().get(i).getSourceValue();
                targetstr[i]=valuemapperVo.getMappings().get(i).getTargetValue();
            }
            valueMapperMeta.setSourceValue(sourcestr);
            valueMapperMeta.setTargetValue(targetstr);

            name+=k;
            k++;
            stepMetas.add(initStep(valueMapperMeta));
            locationX+=100;
        }

        return  stepMetas;
    }

}
