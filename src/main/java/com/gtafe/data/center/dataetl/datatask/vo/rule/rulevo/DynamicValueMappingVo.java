package com.gtafe.data.center.dataetl.datatask.vo.rule.rulevo;

//对应 动态映射 实体
public class DynamicValueMappingVo {
    private String sourceField;

    private String targetField;

    private String defValue;

    private String releaseTableName;

    private String releaseFieldName;

    private String releaseRalation;

    private String targetFieldName;


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

    public String getDefValue() {
        return defValue;
    }

    public void setDefValue(String defValue) {
        this.defValue = defValue;
    }

    public String getReleaseTableName() {
        return releaseTableName;
    }

    public void setReleaseTableName(String releaseTableName) {
        this.releaseTableName = releaseTableName;
    }

    public String getReleaseFieldName() {
        return releaseFieldName;
    }

    public void setReleaseFieldName(String releaseFieldName) {
        this.releaseFieldName = releaseFieldName;
    }

    public String getReleaseRalation() {
        return releaseRalation;
    }

    public void setReleaseRalation(String releaseRalation) {
        this.releaseRalation = releaseRalation;
    }

    public String getTargetFieldName() {
        return targetFieldName;
    }

    @Override
    public String toString() {
        return "DynamicValueMappingVo{" +
                "sourceField='" + sourceField + '\'' +
                ", targetField='" + targetField + '\'' +
                ", defValue='" + defValue + '\'' +
                ", releaseTableName='" + releaseTableName + '\'' +
                ", releaseFieldName='" + releaseFieldName + '\'' +
                ", releaseRalation='" + releaseRalation + '\'' +
                ", targetFieldName='" + targetFieldName + '\'' +
                '}';
    }

    public void setTargetFieldName(String targetFieldName) {
        this.targetFieldName = targetFieldName;
    }
}
