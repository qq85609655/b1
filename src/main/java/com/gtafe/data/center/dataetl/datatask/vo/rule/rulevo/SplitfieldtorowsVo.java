package com.gtafe.data.center.dataetl.datatask.vo.rule.rulevo;

/**
 * @Author: gang, zhou
 * @Date: 2017/11/20
 * @Description:
 */
public class SplitfieldtorowsVo {

    private  String sourceField;
    private String targetField;
    private String delimiter;

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

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

}
