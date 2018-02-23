package com.gtafe.data.center.dataetl.datatask.vo.rule.rulevo;

public class SplitfieldTargetVo {
    private String targetField;
    private int blankType;
    public String getTargetField() {
        return targetField;
    }
    public void setTargetField(String targetField) {
        this.targetField = targetField;
    }
    public int getBlankType() {
        return blankType;
    }
    public void setBlankType(int blankType) {
        this.blankType = blankType;
    }
}
