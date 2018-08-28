package com.gtafe.data.center.dataetl.plsql.vo;

public class ColumnDetail {
    private int id;
    private int sqlId;
    private String columnLabelName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSqlId() {
        return sqlId;
    }

    @Override
    public String toString() {
        return "ColumnDetail{" +
                "id=" + id +
                ", sqlId=" + sqlId +
                ", columnLabelName='" + columnLabelName + '\'' +
                '}';
    }

    public void setSqlId(int sqlId) {
        this.sqlId = sqlId;
    }

    public String getColumnLabelName() {
        return columnLabelName;
    }

    public void setColumnLabelName(String columnLabelName) {
        this.columnLabelName = columnLabelName;
    }
}
