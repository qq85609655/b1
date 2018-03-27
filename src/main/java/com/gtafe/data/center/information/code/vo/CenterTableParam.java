package com.gtafe.data.center.information.code.vo;

import com.gtafe.data.center.common.common.vo.PageParam;

public class CenterTableParam extends PageParam {
    private String tableName;

    private String tableType;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableType() {
        return tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }
}
