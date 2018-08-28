package com.gtafe.data.center.dataetl.plsql.vo;

import com.gtafe.data.center.common.common.vo.PageParam;

public class ItemParamVo extends PageParam {
    private int id;
    private int sqlId;
    private String ColumnName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSqlId() {
        return sqlId;
    }

    public void setSqlId(int sqlId) {
        this.sqlId = sqlId;
    }

    public String getColumnName() {
        return ColumnName;
    }

    public void setColumnName(String columnName) {
        ColumnName = columnName;
    }
}
