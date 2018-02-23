package com.gtafe.data.center.runadmin.alertpush.vo;

/**
 * 错误推送
 *
 * @Author: gang, zhou
 * @Date: 2017/11/6
 * @Description:
 */

public class AlertPush {

    private int id;

    private String xm;

    private String userNo;

    private int orgId;

    private String orgName;

    private boolean isPush;

    private int fbCount;

    private int dyCount;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public int getOrgId() {
        return orgId;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public boolean getIsPush() {
        return isPush;
    }

    public void setIsPush(boolean isPush) {
        this.isPush = isPush;
    }

    public int getFbCount() {
        return fbCount;
    }

    public void setFbCount(int fbCount) {
        this.fbCount = fbCount;
    }

    public int getDyCount() {
        return dyCount;
    }

    public void setDyCount(int dyCount) {
        this.dyCount = dyCount;
    }

    @Override
    public String toString() {
        return "AlertPush{" +
                "id=" + id +
                ", xm='" + xm + '\'' +
                ", userNo='" + userNo + '\'' +
                ", orgId=" + orgId +
                ", orgName='" + orgName + '\'' +
                ", isPush=" + isPush +
                ", fbCount=" + fbCount +
                ", dyCount=" + dyCount +
                '}';
    }
}
