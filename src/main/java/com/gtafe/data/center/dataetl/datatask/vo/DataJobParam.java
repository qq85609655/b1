package com.gtafe.data.center.dataetl.datatask.vo;

import com.gtafe.data.center.common.common.vo.PageParam;

public class DataJobParam extends PageParam {
    private String orgIds;//机构id列表
    private Integer status;//状态
    private String jobName;//搜索文字
    private Integer busType; //业务类型

    public Integer getBusType() {
        return busType;
    }

    public void setBusType(Integer busType) {
        this.busType = busType;
    }

    public String getOrgIds() {
        return orgIds;
    }

    public void setOrgIds(String orgIds) {
        this.orgIds = orgIds;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

}
