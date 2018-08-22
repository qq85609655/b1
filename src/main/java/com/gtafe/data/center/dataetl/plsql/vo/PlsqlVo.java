package com.gtafe.data.center.dataetl.plsql.vo;

import java.io.Serializable;
import java.util.Date;

public class PlsqlVo implements Serializable {
    private int id;
    private String name;
    private int dbSourceId;
    private String content;
    private String aliansName;
    private String remark;
    private boolean status;
    private int orgId;
    private String orgName;
    private String creator;
    private Date createTime;
    private String updator;
    private Date updateTime;

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDbSourceId() {
        return dbSourceId;
    }

    public void setDbSourceId(int dbSourceId) {
        this.dbSourceId = dbSourceId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAliansName() {
        return aliansName;
    }

    public void setAliansName(String aliansName) {
        this.aliansName = aliansName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdator() {
        return updator;
    }

    public void setUpdator(String updator) {
        this.updator = updator;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "PlsqlVo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dbSourceId=" + dbSourceId +
                ", content='" + content + '\'' +
                ", aliansName='" + aliansName + '\'' +
                ", remark='" + remark + '\'' +
                ", status=" + status +
                ", orgId=" + orgId +
                ", creator='" + creator + '\'' +
                ", createTime=" + createTime +
                ", updator='" + updator + '\'' +
                ", updateTime=" + updateTime +
                '}';
    }
}
