package com.gtafe.data.center.system.config.vo;

public class SysConfigVo {

    private int id;

    private String emailHost;

    private String emailUser;

    private String emailPwd;

    private String emailSmtpAddr;

    private int emailSmtpPort;

    private String sysName;//系统名称

    private String schoolName;

    private String sysLogoUrl;//系统logo 路径

    private String copyRight;//系统说明
    
    private String logoInfo;//系统说明

    public int getSfInit() {
        return sfInit;
    }

    public void setSfInit(int sfInit) {
        this.sfInit = sfInit;
    }

    private int sfInit;//是否初始化

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmailHost() {
        return emailHost;
    }

    public void setEmailHost(String emailHost) {
        this.emailHost = emailHost;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public String getEmailPwd() {
        return emailPwd;
    }

    public void setEmailPwd(String emailPwd) {
        this.emailPwd = emailPwd;
    }

    public String getEmailSmtpAddr() {
        return emailSmtpAddr;
    }

    public void setEmailSmtpAddr(String emailSmtpAddr) {
        this.emailSmtpAddr = emailSmtpAddr;
    }

    public int getEmailSmtpPort() {
        return emailSmtpPort;
    }

    public void setEmailSmtpPort(int emailSmtpPort) {
        this.emailSmtpPort = emailSmtpPort;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getSysLogoUrl() {
        return sysLogoUrl;
    }

    public void setSysLogoUrl(String sysLogoUrl) {
        this.sysLogoUrl = sysLogoUrl;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getCopyRight() {
        return copyRight;
    }

    public void setCopyRight(String copyRight) {
        this.copyRight = copyRight;
    }

    public String getLogoInfo() {
        return logoInfo;
    }

    public void setLogoInfo(String logoInfo) {
        this.logoInfo = logoInfo;
    }
}
