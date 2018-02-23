package com.gtafe.data.center.dataetl.trans;

import com.gtafe.data.center.dataetl.datatask.vo.rule.ConvertRuleCalculator;
import com.gtafe.data.center.dataetl.datatask.vo.rule.rulevo.CalculatorVo;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.steps.calculator.CalculatorMeta;
import org.pentaho.di.trans.steps.calculator.CalculatorMetaFunction;

import java.io.IOException;
import java.util.List;

/**
 * 计算器
 */
public class Calculator extends BaseStep{

    String stepstr;

    public Calculator(int locationX, int locattionY, String name, String stepstr) {
        super(locationX, locattionY, name);
        this.stepstr = stepstr;
    }

    public StepMeta ca() {

        List<CalculatorVo> calculatorVos;

        try {
            calculatorVos=mapper.readValue(stepstr, ConvertRuleCalculator.class).getDataList();
        } catch (IOException e) {
            return null;
        }

        CalculatorMeta calculatorMeta=new CalculatorMeta();

        calculatorMeta.allocate(calculatorVos.size());

        CalculatorMetaFunction[] calculations=new CalculatorMetaFunction[calculatorVos.size()];

        for(int i=0;i<calculatorVos.size();i++) {
            calculations[i]=new CalculatorMetaFunction();
            calculations[i].setFieldName(calculatorVos.get(i).getTargetField());
            calculations[i].setFieldA(calculatorVos.get(i).getFieldA());
            calculations[i].setFieldB(calculatorVos.get(i).getFieldB());
            calculations[i].setFieldC(calculatorVos.get(i).getFieldC());
            calculations[i].setCalcType(calculatorVos.get(i).getCalculator()%100);
            calculations[i].setValueType(1);
            calculations[i].setValueLength(Integer.parseInt(calculatorVos.get(i).getLongVal()));
            calculations[i].setValuePrecision(Integer.parseInt(calculatorVos.get(i).getAccuracy()));

        }

        calculatorMeta.setCalculation(calculations);

        return initStep(calculatorMeta);

    }
}
