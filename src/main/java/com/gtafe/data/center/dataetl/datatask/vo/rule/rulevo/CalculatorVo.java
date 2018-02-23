package com.gtafe.data.center.dataetl.datatask.vo.rule.rulevo;

/**
 * @Author: gang, zhou
 * @Date: 2017/11/20
 * @Description:
 */
public class CalculatorVo {

    private String targetField;
    private String fieldA;
    private String fieldB;
    private String fieldC;

    //  0:none;   1:number    2:string    3:date    参考:ValueMetaInterface
    private int valueType;//类型，目前仅支持1
    private String longVal;//长度
    private String accuracy;//精度

    // 参考:CalculatorMetaFunction
    private int calculator;//计算器操作类型

    public String getTargetField() {
        return targetField;
    }

    public void setTargetField(String targetField) {
        this.targetField = targetField;
    }

    public String getFieldA() {
        return fieldA;
    }

    public void setFieldA(String fieldA) {
        this.fieldA = fieldA;
    }

    public String getFieldB() {
        return fieldB;
    }

    public void setFieldB(String fieldB) {
        this.fieldB = fieldB;
    }

    public String getFieldC() {
        return fieldC;
    }

    public void setFieldC(String fieldC) {
        this.fieldC = fieldC;
    }

    public int getValueType() {
        return valueType;
    }

    public void setValueType(int valueType) {
        this.valueType = valueType;
    }

    public String getLongVal() {
        return longVal;
    }

    public void setLongVal(String longVal) {
        this.longVal = longVal;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public int getCalculator() {
        return calculator;
    }

    public void setCalculator(int calculator) {
        this.calculator = calculator;
    }
}
