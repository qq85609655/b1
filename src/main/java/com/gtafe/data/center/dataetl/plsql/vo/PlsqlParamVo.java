package com.gtafe.data.center.dataetl.plsql.vo;

import com.gtafe.data.center.common.common.vo.PageParam;

import java.io.Serializable;
import java.util.Date;

public class PlsqlParamVo extends PageParam {
    private String nameKey;

    public String getOrgIds() {
        return orgIds;
    }

    public void setOrgIds(String orgIds) {
        this.orgIds = orgIds;
    }

    private String orgIds;

    public String getNameKey() {
        return nameKey;
    }

    public void setNameKey(String nameKey) {
        this.nameKey = nameKey;
    }
}
