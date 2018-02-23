package com.gtafe.data.center.dataetl.datatask.vo.rule.rulevo;

import java.util.List;

public class SourceTargetVo {
    private int thirdConnectionId;
    private int sourceDbtype;
    private int targetDbtype;
    private String thirdTablename;
    private String centerTablename;
    private String subsetCode;
    private String classCode;
    private String subclassCode;
    private List<TargetMappingVo> mappings;
    private List<TargetCondition> conditions;
    private boolean isUpdate;

    public boolean isUpdate() {
        return isUpdate;
    }

    public List<TargetCondition> getConditions() {
        return conditions;
    }

    public void setConditions(List<TargetCondition> conditions) {
        this.conditions = conditions;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }

    public int getThirdConnectionId() {
        return thirdConnectionId;
    }
    public void setThirdConnectionId(int thirdConnectionId) {
        this.thirdConnectionId = thirdConnectionId;
    }
    public int getSourceDbtype() {
        return sourceDbtype;
    }
    public void setSourceDbtype(int sourceDbtype) {
        this.sourceDbtype = sourceDbtype;
    }
    public int getTargetDbtype() {
        return targetDbtype;
    }
    public void setTargetDbtype(int targetDbtype) {
        this.targetDbtype = targetDbtype;
    }
    public String getThirdTablename() {
        return thirdTablename;
    }
    public void setThirdTablename(String thirdTablename) {
        this.thirdTablename = thirdTablename;
    }
    public String getCenterTablename() {
        return centerTablename;
    }
    public void setCenterTablename(String centerTablename) {
        this.centerTablename = centerTablename;
    }
    public String getSubsetCode() {
        return subsetCode;
    }
    public void setSubsetCode(String subsetCode) {
        this.subsetCode = subsetCode;
    }
    public String getClassCode() {
        return classCode;
    }
    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }
    public String getSubclassCode() {
        return subclassCode;
    }
    public void setSubclassCode(String subclassCode) {
        this.subclassCode = subclassCode;
    }
    public List<TargetMappingVo> getMappings() {
        return mappings;
    }
    public void setMappings(List<TargetMappingVo> mappings) {
        this.mappings = mappings;
    }
}
