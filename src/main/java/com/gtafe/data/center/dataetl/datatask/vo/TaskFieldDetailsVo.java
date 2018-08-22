package com.gtafe.data.center.dataetl.datatask.vo;

public class TaskFieldDetailsVo {
    private int id;
    private String sourceField;
    private String sourceFieldName;
    private String sourceFieldType;
    private int sourceFieldLength;
    private String targetField;
    private String targetFieldName;
    private int targetFieldLength;
    private String targetFieldType;
    private int taskId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSourceField() {
        return sourceField;
    }

    public void setSourceField(String sourceField) {
        this.sourceField = sourceField;
    }

    public String getSourceFieldName() {
        return sourceFieldName;
    }

    public void setSourceFieldName(String sourceFieldName) {
        this.sourceFieldName = sourceFieldName;
    }

    public String getSourceFieldType() {
        return sourceFieldType;
    }

    public void setSourceFieldType(String sourceFieldType) {
        this.sourceFieldType = sourceFieldType;
    }

    public int getSourceFieldLength() {
        return sourceFieldLength;
    }

    public void setSourceFieldLength(int sourceFieldLength) {
        this.sourceFieldLength = sourceFieldLength;
    }

    public String getTargetField() {
        return targetField;
    }

    public void setTargetField(String targetField) {
        this.targetField = targetField;
    }

    public String getTargetFieldName() {
        return targetFieldName;
    }

    public void setTargetFieldName(String targetFieldName) {
        this.targetFieldName = targetFieldName;
    }

    public int getTargetFieldLength() {
        return targetFieldLength;
    }

    public void setTargetFieldLength(int targetFieldLength) {
        this.targetFieldLength = targetFieldLength;
    }

    public String getTargetFieldType() {
        return targetFieldType;
    }

    public void setTargetFieldType(String targetFieldType) {
        this.targetFieldType = targetFieldType;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }
}
