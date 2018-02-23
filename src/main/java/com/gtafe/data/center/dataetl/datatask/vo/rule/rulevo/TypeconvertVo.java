package com.gtafe.data.center.dataetl.datatask.vo.rule.rulevo;

/**
 * @Author: gang, zhou
 * @Date: 2017/11/20
 * @Description:
 */
public class TypeconvertVo {
    private  String sourceField;
    private String targetField;
    //String/Number/Date/Integer
    private  String targetType;
    private String format;
    private String longVal;//长度
    private String accuracy;//精度
    public String getSourceField() {
        return sourceField;
    }
    public void setSourceField(String sourceField) {
        this.sourceField = sourceField;
    }
    public String getTargetField() {
        return targetField;
    }
    public void setTargetField(String targetField) {
        this.targetField = targetField;
    }
    public String getTargetType() {
        return targetType;
    }
    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
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

}
