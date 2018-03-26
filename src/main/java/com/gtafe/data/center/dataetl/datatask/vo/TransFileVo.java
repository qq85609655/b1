package com.gtafe.data.center.dataetl.datatask.vo;

import java.util.Date;

/**
 * 转换任务文件管理
 */
public class TransFileVo {

    private String fileName;//文件名称
    private String filePath;//绝对路径
    private Date createTime;//创建时间
    private Date updateTime; //最后修改时间
    private String createUserInfo; //创建人信息
    private Date accessTime;

    private String scheduleInfo;

    private String fileType;//1 ktr  2 kjb


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(Date accessTime) {
        this.accessTime = accessTime;
    }

    public String getCreateUserInfo() {
        return createUserInfo;
    }

    public void setCreateUserInfo(String createUserInfo) {
        this.createUserInfo = createUserInfo;
    }


    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getScheduleInfo() {
        return scheduleInfo;
    }

    public void setScheduleInfo(String scheduleInfo) {
        this.scheduleInfo = scheduleInfo;
    }
}
