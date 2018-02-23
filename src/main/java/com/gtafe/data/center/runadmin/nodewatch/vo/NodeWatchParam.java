package com.gtafe.data.center.runadmin.nodewatch.vo;

import com.gtafe.data.center.common.common.vo.PageParam;

public class NodeWatchParam extends PageParam{
    private String orgIds;
    private int busType;
    public String getOrgIds() {
        return orgIds;
    }
    public void setOrgIds(String orgIds) {
        this.orgIds = orgIds;
    }
    public int getBusType() {
        return busType;
    }
    public void setBusType(int busType) {
        this.busType = busType;
    }
}
