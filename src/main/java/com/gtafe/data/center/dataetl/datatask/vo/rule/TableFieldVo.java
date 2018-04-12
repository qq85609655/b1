package com.gtafe.data.center.dataetl.datatask.vo.rule;

public class TableFieldVo {
    private String field;//字段名称
    private String comment;//注释
    private String dataType;//数据类型
    private long length;//总长度
    private long decimalLength = 0;//小数长度，根据实际情况，后续考虑是否使用
    private int primarykey;//0非主键，1主键
    private int nullable;//0不可空，1可空
    private int ruleType = 1;//规则类型,数据来源类型
    private String isAutoCreate;//主键是否自增

    public String getField() {
        return field;
    }
    public void setField(String field) {
        this.field = field;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public String getDataType() {
        return dataType;
    }
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
    public long getLength() {
        return length;
    }
    public void setLength(long length) {
        this.length = length;
    }
    public long getDecimalLength() {
        return decimalLength;
    }
    public void setDecimalLength(long decimalLength) {
        this.decimalLength = decimalLength;
    }
    public int getPrimarykey() {
        return primarykey;
    }
    public void setPrimarykey(int primarykey) {
        this.primarykey = primarykey;
    }
    public int getNullable() {
        return nullable;
    }
    public void setNullable(int nullable) {
        this.nullable = nullable;
    }
    public int getRuleType() {
        return ruleType;
    }
    public void setRuleType(int ruleType) {
        this.ruleType = ruleType;
    }

    public String getIsAutoCreate() {
        return isAutoCreate;
    }

    public void setIsAutoCreate(String isAutoCreate) {
        this.isAutoCreate = isAutoCreate;
    }

    @Override
    public String toString() {
        return "TableFieldVo{" +
                "field='" + field + '\'' +
                ", comment='" + comment + '\'' +
                ", dataType='" + dataType + '\'' +
                ", length=" + length +
                ", decimalLength=" + decimalLength +
                ", primarykey=" + primarykey +
                ", nullable=" + nullable +
                ", ruleType=" + ruleType +
                ", isAutoCreate='" + isAutoCreate + '\'' +
                '}';
    }
}
