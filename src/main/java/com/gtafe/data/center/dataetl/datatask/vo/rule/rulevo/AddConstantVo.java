package com.gtafe.data.center.dataetl.datatask.vo.rule.rulevo;

/**
 * @Author: gang, zhou
 * @Date: 2017/11/20
 * @Description:
 */
public class AddConstantVo {
    private String sourceField;
    private String targetField;
    private String toValue;//值
    private String formatVal;//格式
    private String valType;//类型

    public String getTargetField() {
        return targetField;
    }

    public void setTargetField(String targetField) {
        this.targetField = targetField;
    }

    public String getToValue() {
        return toValue;
    }

    public void setToValue(String toValue) {
        this.toValue = toValue;
    }


    public String getSourceField() {
        return sourceField;
    }

    public void setSourceField(String sourceField) {
        this.sourceField = sourceField;
    }

    public String getFormatVal() {
        return formatVal;
    }

    public void setFormatVal(String formatVal) {
        this.formatVal = formatVal;
    }

    public String getValType() {
        return valType;
    }

    public void setValType(String valType) {
        this.valType = valType;
    }
}
