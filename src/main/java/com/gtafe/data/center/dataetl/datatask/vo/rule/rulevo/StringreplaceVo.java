package com.gtafe.data.center.dataetl.datatask.vo.rule.rulevo;

/**
 * @Author: gang, zhou
 * @Date: 2017/11/20
 * @Description:
 */
public class StringreplaceVo {
    private  String sourceField;
    private String targetField;
    private String findValue;
    private String replaceValue;

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

    public String getFindValue() {
        return findValue;
    }

    public void setFindValue(String findValue) {
        this.findValue = findValue;
    }

    public String getReplaceValue() {
        return replaceValue;
    }

    public void setReplaceValue(String replaceValue) {
        this.replaceValue = replaceValue;
    }
}
