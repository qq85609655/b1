package com.gtafe.data.center.system.config.vo;

public class SysConfigVo {

    private int id;

    private String emailHost;

    private String emailUser;

    private String emailPwd;

    private String emailSmtpAddr;

    private int emailSmtpPort;

    private String sysName;//系统名称

    //考慮將中心庫的配置寫入系統參數配置中 更加有意義。
    private String dbType;
    private String dbName;
    private String tableSpaces;
    private String port;
    private String username;
    private String password;
    private String ipAddress;


    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getTableSpaces() {
        return tableSpaces;
    }

    public void setTableSpaces(String tableSpaces) {
        this.tableSpaces = tableSpaces;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getSysType() {
        return sysType;
    }

    public void setSysType(String sysType) {
        this.sysType = sysType;
    }

    private String sysType;//系统类型

    private String schoolName;

    private String sysLogoUrl;//系统logo 路径

    private String copyRight;//系统说明

    private String logoInfo;//系统说明

    private String kettleInstallPath;

    private String ktrFilesPath;

    private String kjbFilesPath;

    public String getKettleInstallPath() {
        return kettleInstallPath;
    }

    public void setKettleInstallPath(String kettleInstallPath) {
        this.kettleInstallPath = kettleInstallPath;
    }

    public String getKtrFilesPath() {
        return ktrFilesPath;
    }

    public void setKtrFilesPath(String ktrFilesPath) {
        this.ktrFilesPath = ktrFilesPath;
    }

    public String getKjbFilesPath() {
        return kjbFilesPath;
    }

    public void setKjbFilesPath(String kjbFilesPath) {
        this.kjbFilesPath = kjbFilesPath;
    }

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

    @Override
    public String toString() {
        return "SysConfigVo{" +
                "id=" + id +
                ", emailHost='" + emailHost + '\'' +
                ", emailUser='" + emailUser + '\'' +
                ", emailPwd='" + emailPwd + '\'' +
                ", emailSmtpAddr='" + emailSmtpAddr + '\'' +
                ", emailSmtpPort=" + emailSmtpPort +
                ", sysName='" + sysName + '\'' +
                ", dbType='" + dbType + '\'' +
                ", dbName='" + dbName + '\'' +
                ", tableSpaces='" + tableSpaces + '\'' +
                ", port='" + port + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", sysType='" + sysType + '\'' +
                ", schoolName='" + schoolName + '\'' +
                ", sysLogoUrl='" + sysLogoUrl + '\'' +
                ", copyRight='" + copyRight + '\'' +
                ", logoInfo='" + logoInfo + '\'' +
                ", kettleInstallPath='" + kettleInstallPath + '\'' +
                ", ktrFilesPath='" + ktrFilesPath + '\'' +
                ", kjbFilesPath='" + kjbFilesPath + '\'' +
                ", sfInit=" + sfInit +
                '}';
    }
}
