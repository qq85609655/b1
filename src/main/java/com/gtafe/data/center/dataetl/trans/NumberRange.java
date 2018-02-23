package com.gtafe.data.center.dataetl.trans;

import com.gtafe.data.center.dataetl.datatask.vo.rule.ConvertRuleNumberrange;
import com.gtafe.data.center.dataetl.datatask.vo.rule.rulevo.NumberrangeVo;
import com.gtafe.data.center.dataetl.datatask.vo.rule.rulevo.RangeVo;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.steps.numberrange.NumberRangeMeta;
import org.pentaho.di.trans.steps.numberrange.NumberRangeRule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 数值范围
 */
public class NumberRange extends BaseStep{

    String stepstr;

    public NumberRange(int locationX, int locattionY, String name, String stepstr) {
        super(locationX, locattionY, name);
        this.stepstr = stepstr;
    }

    public List<StepMeta> nrStep() {

        List<NumberrangeVo> numberrangeVos;

        try {
            numberrangeVos=mapper.readValue(stepstr, ConvertRuleNumberrange.class).getDataList();
        } catch (IOException e) {
            return null;
        }
        List<StepMeta> stepMetas=new ArrayList<>();

        int k=0;
        for (NumberrangeVo numberrangeVo : numberrangeVos) {
            NumberRangeMeta numberRangeMeta=new NumberRangeMeta();

            numberRangeMeta.setInputField(numberrangeVo.getSourceField());
            numberRangeMeta.setOutputField(numberrangeVo.getTargetField());
            numberRangeMeta.setFallBackValue(numberrangeVo.getDefValue());

            List<NumberRangeRule> rules=new ArrayList<>();
            for (RangeVo rangeVo : numberrangeVo.getNumberranges()) {
                double minVal,maxVal;
                if (rangeVo.getStartValue().equals("")) {
                    minVal = Double.MIN_VALUE;
                } else {
                    minVal= Double.parseDouble(rangeVo.getStartValue());
                }
                if (rangeVo.getEndValue().equals("")) {
                    maxVal=Double.MAX_VALUE;
                }else {
                    maxVal=Double.parseDouble(rangeVo.getEndValue());
                }
                rules.add(new NumberRangeRule(minVal,maxVal,rangeVo.getTargetValue()) );
            }
            numberRangeMeta.setRules(rules);

            name+=k;
            k++;
            stepMetas.add(initStep(numberRangeMeta));
            locationX+=100;
        }


        return stepMetas;

    }

}
