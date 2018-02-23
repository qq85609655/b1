package com.gtafe.data.center.runadmin.nodewatch.vo;

/**
 * @Author: gang, zhou
 * @Date: 2017/11/6
 * @Description:
 */
public class NodeWatchVo {

    private int id;

    private String resourceName;

    private String sourceTableName;

    private String tagertTableName;

    private int status;

    private String orgName;

    private String orgId;

    private int busType;


    private String statusName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getSourceTableName() {
        return sourceTableName;
    }

    public void setSourceTableName(String sourceTableName) {
        this.sourceTableName = sourceTableName;
    }

    public String getTagertTableName() {
        return tagertTableName;
    }

    public void setTagertTableName(String tagertTableName) {
        this.tagertTableName = tagertTableName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    @Override
    public String toString() {
        return "NodeWatchVo{" +
                "id=" + id +
                ", resourceName='" + resourceName + '\'' +
                ", sourceTableName='" + sourceTableName + '\'' +
                ", tagertTableName='" + tagertTableName + '\'' +
                ", status='" + status + '\'' +
                ", orgName='" + orgName + '\'' +
                ", orgId='" + orgId + '\'' +
                ", statusName='" + statusName + '\'' +
                '}';
    }

    public int getBusType() {
        return busType;
    }

    public void setBusType(int busType) {
        this.busType = busType;
    }
}
