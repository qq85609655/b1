package com.gtafe.data.center.dataetl.datatask.vo;

import com.gtafe.data.center.common.common.vo.PageParam;

public class DataTaskParam extends PageParam{
    private int collectionId;//连接id
    private String orgIds;//机构id列表
    private int businessType;//业务id
    private Integer status;//状态
    private String name;//搜索文字
    public int getCollectionId() {
        return collectionId;
    }
    public void setCollectionId(int collectionId) {
        this.collectionId = collectionId;
    }
    public String getOrgIds() {
        return orgIds;
    }
    public void setOrgIds(String orgIds) {
        this.orgIds = orgIds;
    }
    public int getBusinessType() {
        return businessType;
    }
    public void setBusinessType(int businessType) {
        this.businessType = businessType;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
