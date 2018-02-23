package com.gtafe.data.center.runadmin.alertpush.vo;

import com.gtafe.data.center.common.common.vo.PageParam;

public class AlertPushParam extends PageParam{
    private String orgIds;
    private int isPush = -1;
    public String getOrgIds() {
        return orgIds;
    }
    public void setOrgIds(String orgIds) {
        this.orgIds = orgIds;
    }
    public int getIsPush() {
        return isPush;
    }
    public void setIsPush(int isPush) {
        this.isPush = isPush;
    }
}
