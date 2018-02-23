package com.gtafe.data.center.dataetl.datatask.vo;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

public class DataTaskVo {
    private int taskId;
    private String taskName;
    private String description;
    private int dataType;
    private int businessType;
    private int orgId;
    private String orgName;
    private int thirdConnectionId;
    private int thirdDbtype;
    private String thirdTablename;
    private String centerTablename;
    private String subsetCode;
    private String classCode;
    private String subclassCode;
    //执行类型，1分钟 2小时 3天 4周 5月
    private int runType;
    private int runSpace;
    private String runSpaces;
    private String runTime;
    private String runEexpression;
    private boolean runStatus;//1启动，2停止
    private String runStatusStr;//1启动，2停止
    private List<String> steps;
    @ApiModelProperty(value = "创建人id", required = true)
    private int creater;
    @ApiModelProperty(value = "创建时间", required = true)
    private Date createTime;
    @ApiModelProperty(value = "修改人id", required = true)
    private int updater;
    @ApiModelProperty(value = "修改时间", required = true)
    private Date updateTime;
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
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public int getDataType() {
        return dataType;
    }
    public void setDataType(int dataType) {
        this.dataType = dataType;
    }
    public int getBusinessType() {
        return businessType;
    }
    public void setBusinessType(int businessType) {
        this.businessType = businessType;
    }
    public int getOrgId() {
        return orgId;
    }
    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }
    public String getOrgName() {
        return orgName;
    }
    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
    public int getThirdConnectionId() {
        return thirdConnectionId;
    }
    public void setThirdConnectionId(int thirdConnectionId) {
        this.thirdConnectionId = thirdConnectionId;
    }
    public int getThirdDbtype() {
        return thirdDbtype;
    }
    public void setThirdDbtype(int thirdDbtype) {
        this.thirdDbtype = thirdDbtype;
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
    public int getRunType() {
        return runType;
    }
    public void setRunType(int runType) {
        this.runType = runType;
    }
    public int getRunSpace() {
        return runSpace;
    }
    public void setRunSpace(int runSpace) {
        this.runSpace = runSpace;
    }
    public String getRunSpaces() {
        return runSpaces;
    }
    public void setRunSpaces(String runSpaces) {
        this.runSpaces = runSpaces;
    }
    public String getRunTime() {
        return runTime;
    }
    public void setRunTime(String runTime) {
        this.runTime = runTime;
    }
    public String getRunEexpression() {
        return runEexpression;
    }
    public void setRunEexpression(String runEexpression) {
        this.runEexpression = runEexpression;
    }

    public boolean isRunStatus() {
        return runStatus;
    }

    public void setRunStatus(boolean runStatus) {
        this.runStatus = runStatus;
    }

    public List<String> getSteps() {
        return steps;
    }
    public void setSteps(List<String> steps) {
        this.steps = steps;
    }

    public String getRunStatusStr() {
        return runStatusStr;
    }

    public void setRunStatusStr(String runStatusStr) {
        this.runStatusStr = runStatusStr;
    }

    public int getCreater() {
        return creater;
    }

    public void setCreater(int creater) {
        this.creater = creater;
    }

    @Override
    public String toString() {
        return "DataTaskVo{" +
                "taskId=" + taskId +
                ", taskName='" + taskName + '\'' +
                ", description='" + description + '\'' +
                ", dataType=" + dataType +
                ", businessType=" + businessType +
                ", orgId=" + orgId +
                ", orgName='" + orgName + '\'' +
                ", thirdConnectionId=" + thirdConnectionId +
                ", thirdDbtype=" + thirdDbtype +
                ", thirdTablename='" + thirdTablename + '\'' +
                ", centerTablename='" + centerTablename + '\'' +
                ", subsetCode='" + subsetCode + '\'' +
                ", classCode='" + classCode + '\'' +
                ", subclassCode='" + subclassCode + '\'' +
                ", runType=" + runType +
                ", runSpace=" + runSpace +
                ", runSpaces='" + runSpaces + '\'' +
                ", runTime='" + runTime + '\'' +
                ", runEexpression='" + runEexpression + '\'' +
                ", runStatus=" + runStatus +
                ", runStatusStr='" + runStatusStr + '\'' +
                ", steps=" + steps +
                ", creater=" + creater +
                ", createTime=" + createTime +
                ", updater=" + updater +
                ", updateTime=" + updateTime +
                '}';
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getUpdater() {
        return updater;
    }

    public void setUpdater(int updater) {
        this.updater = updater;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

}
