package com.gtafe.data.center.system.log.vo;

public class LogInfo {
    private int moduleId;
    private String operType;
    private String operContent;
    public int getModuleId() {
        return moduleId;
    }
    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }
    public String getOperType() {
        return operType;
    }
    public void setOperType(String operType) {
        this.operType = operType;
    }
    public String getOperContent() {
        return operContent;
    }
    public void setOperContent(String operContent) {
        this.operContent = operContent;
    }
}
