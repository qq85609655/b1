package com.gtafe.data.center.dataetl.datajob.vo;

import java.util.Date;

public class JobEntity {
    private int jobId;
    private String jobName;
    private String schedule;
    private String remark;
    private String status;
    private String statusStr;
    private Date createTime;
    private String creator;
    private Date updateTime;
    private String updator;
    private int orgId;
    private int businessType;
    private String orgName;

    @Override
    public String toString() {
        return "JobEntity{" +
                "jobId=" + jobId +
                ", jobName='" + jobName + '\'' +
                ", schedule='" + schedule + '\'' +
                ", remark='" + remark + '\'' +
                ", status='" + status + '\'' +
                ", statusStr='" + statusStr + '\'' +
                ", createTime=" + createTime +
                ", creator='" + creator + '\'' +
                ", updateTime=" + updateTime +
                ", updator='" + updator + '\'' +
                ", orgId=" + orgId +
                ", businessType=" + businessType +
                ", orgName='" + orgName + '\'' +
                '}';
    }

    public String getStatusStr() {
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }
    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }


    public int getBusinessType() {
        return businessType;
    }

    public void setBusinessType(int businessType) {
        this.businessType = businessType;
    }




    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdator() {
        return updator;
    }

    public void setUpdator(String updator) {
        this.updator = updator;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }
}
