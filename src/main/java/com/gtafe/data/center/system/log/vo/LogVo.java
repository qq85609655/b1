package com.gtafe.data.center.system.log.vo;

public class LogVo {

    private int logId;
    private String  operUserNo;//操作人id
    private String operTime;//操作时间
    private int operModuleId;//操作功能 0 登录 其他是模块id
    private String operModuleName;
    private String operType;//操作类型 1: 新增 2 删除 3  修改 4 启用 5 禁用 6 重置 7 授权
    private String operContent;//操作内容
    private String operUserName;//操作用户名称
    private int operOrgId;//机构id
    private String operOrgName;//操作机构名称
    private String operIp;//操作人客户端ip
    private int operRes; //操作成功 1 正常 2 异常


    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public String getOperUserNo() {
        return operUserNo;
    }

    public void setOperUserNo(String operUserNo) {
        this.operUserNo = operUserNo;
    }

    public String getOperContent() {
        return operContent;
    }

    public void setOperContent(String operContent) {
        this.operContent = operContent;
    }

    public String getOperUserName() {
        return operUserName;
    }

    public void setOperUserName(String operUserName) {
        this.operUserName = operUserName;
    }

    public int getOperOrgId() {
        return operOrgId;
    }

    public void setOperOrgId(int operOrgId) {
        this.operOrgId = operOrgId;
    }

    public String getOperOrgName() {
        return operOrgName;
    }

    public void setOperOrgName(String operOrgName) {
        this.operOrgName = operOrgName;
    }

    public String getOperIp() {
        return operIp;
    }

    public void setOperIp(String operIp) {
        this.operIp = operIp;
    }

    public String getOperTime() {
        return operTime;
    }

    public void setOperTime(String operTime) {
        this.operTime = operTime;
    }


    public String getOperType() {
        return operType;
    }

    public void setOperType(String operType) {
        this.operType = operType;
    }

    public int getOperModuleId() {
        return operModuleId;
    }

    public void setOperModuleId(int operModuleId) {
        this.operModuleId = operModuleId;
    }

    public String getOperModuleName() {
        return operModuleName;
    }

    public void setOperModuleName(String operModuleName) {
        this.operModuleName = operModuleName;
    }

    public int getOperRes() {
        return operRes;
    }

    public void setOperRes(int operRes) {
        this.operRes = operRes;
    }
}
