package com.gtafe.data.center.dataetl.datasource.vo;

import com.gtafe.data.center.common.common.vo.PageParam;

import io.swagger.annotations.ApiParam;

public class DatasourceParam extends PageParam{
    @ApiParam(name = "dbType", value = "数据库类型", required = false, defaultValue = "")
    private Integer dbType;
    @ApiParam(name = "nameOrDBName", value = "数据源名称或数据库名称", required = false, defaultValue = "") 
    private String nameOrDBName;
    @ApiParam(name = "orgIds", value = "机构id列表，逗号分割", required = false, defaultValue = "") 
    private String orgIds;

    private String isCenter;

    public String getIsCenter() {
        return isCenter;
    }

    public void setIsCenter(String isCenter) {
        this.isCenter = isCenter;
    }

    public Integer getDbType() {
        return dbType;
    }
    public void setDbType(Integer dbType) {
        this.dbType = dbType;
    }
    public String getNameOrDBName() {
        return nameOrDBName;
    }
    public void setNameOrDBName(String nameOrDBName) {
        this.nameOrDBName = nameOrDBName;
    }
    public String getOrgIds() {
        return orgIds;
    }
    public void setOrgIds(String orgIds) {
        this.orgIds = orgIds;
    }
}
