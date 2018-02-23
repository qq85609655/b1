package com.gtafe.data.center.runadmin.etlerrorlog.vo;

import com.gtafe.data.center.common.common.vo.PageParam;

public class KettleLogParam extends PageParam{
    private int busType;
    private String startTime;
    private String endTime;
    private String orgIds;
    private String transName;
    public String getStartTime() {
        return startTime;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public String getEndTime() {
        return endTime;
    }
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    public String getOrgIds() {
        return orgIds;
    }
    public void setOrgIds(String orgIds) {
        this.orgIds = orgIds;
    }
    public String getTransName() {
        return transName;
    }
    public void setTransName(String transName) {
        this.transName = transName;
    }
    public int getBusType() {
        return busType;
    }
    public void setBusType(int busType) {
        this.busType = busType;
    }
}
