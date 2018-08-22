package com.gtafe.data.center.dataetl.datatask.vo;

import java.util.ArrayList;
import java.util.List;

public class EtlTaskNoteVo {
    private int taskId;
    private String taskName;
    private String busType;
    private String remark;
    private String sourceTable;
    private String targetTable;
    private String sourceTableName;
    private String targetTableName;
    private List<TaskFieldDetailsVo> taskFieldDetailsVoList = new ArrayList<TaskFieldDetailsVo>();

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSourceTable() {
        return sourceTable;
    }

    public void setSourceTable(String sourceTable) {
        this.sourceTable = sourceTable;
    }

    public String getTargetTable() {
        return targetTable;
    }

    public void setTargetTable(String targetTable) {
        this.targetTable = targetTable;
    }

    public String getSourceTableName() {
        return sourceTableName;
    }

    public void setSourceTableName(String sourceTableName) {
        this.sourceTableName = sourceTableName;
    }

    public String getTargetTableName() {
        return targetTableName;
    }

    public void setTargetTableName(String targetTableName) {
        this.targetTableName = targetTableName;
    }

    public List<TaskFieldDetailsVo> getTaskFieldDetailsVoList() {
        return taskFieldDetailsVoList;
    }

    public void setTaskFieldDetailsVoList(List<TaskFieldDetailsVo> taskFieldDetailsVoList) {
        this.taskFieldDetailsVoList = taskFieldDetailsVoList;
    }
}
