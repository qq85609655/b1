package com.gtafe.data.center.dataetl.datatask.vo.rule.rulevo;

/**
 * 更新条件
 */
public class TargetCondition {

    private String sourceField;

    private String targetField;

    /**
     *  "=" ;  ">"  ;  "<"  ;  "<>"  ;  "<="  ;  ">="  ;  "LIKE"
     */
    private String keyCondition;

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

    public String getKeyCondition() {
        return keyCondition;
    }

    public void setKeyCondition(String keyCondition) {
        this.keyCondition = keyCondition;
    }
}
