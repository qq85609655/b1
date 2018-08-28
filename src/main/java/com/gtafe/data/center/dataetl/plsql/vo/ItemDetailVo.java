package com.gtafe.data.center.dataetl.plsql.vo;

public class ItemDetailVo {

    private int id;
    private String columnLabel;
    private String columnLabelName;
    private int displaySize;
    private String typeName;
    private int preci;
    private int scal;
    private boolean isAutoIncrement;
    private boolean isCurrency;
    private int isNullable;
    private boolean isReadOnly;
    private int sqlId;

    public String getColumnLabelName() {
        return columnLabelName;
    }

    public void setColumnLabelName(String columnLabelName) {
        this.columnLabelName = columnLabelName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getColumnLabel() {
        return columnLabel;
    }

    public void setColumnLabel(String columnLabel) {
        this.columnLabel = columnLabel;
    }

    public int getDisplaySize() {
        return displaySize;
    }

    public void setDisplaySize(int displaySize) {
        this.displaySize = displaySize;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getPreci() {
        return preci;
    }

    public void setPreci(int preci) {
        this.preci = preci;
    }

    public int getScal() {
        return scal;
    }

    public void setScal(int scal) {
        this.scal = scal;
    }

    public int getIsNullable() {
        return isNullable;
    }

    public void setIsNullable(int isNullable) {
        this.isNullable = isNullable;
    }

    public boolean isAutoIncrement() {
        return isAutoIncrement;
    }

    public void setAutoIncrement(boolean autoIncrement) {
        isAutoIncrement = autoIncrement;
    }

    public boolean isCurrency() {
        return isCurrency;
    }

    public void setCurrency(boolean currency) {
        isCurrency = currency;
    }

    public int isNullable() {
        return isNullable;
    }

    public void setNullable(int nullable) {
        isNullable = nullable;
    }

    public boolean isReadOnly() {
        return isReadOnly;
    }

    public void setReadOnly(boolean readOnly) {
        isReadOnly = readOnly;
    }

    public int getSqlId() {
        return sqlId;
    }

    public void setSqlId(int sqlId) {
        this.sqlId = sqlId;
    }

    @Override
    public String toString() {
        return "ItemDetailVo{" +
                "id=" + id +
                ", columnLabel='" + columnLabel + '\'' +
                ", displaySize=" + displaySize +
                ", typeName='" + typeName + '\'' +
                ", preci=" + preci +
                ", scal=" + scal +
                ", isAutoIncrement=" + isAutoIncrement +
                ", isCurrency=" + isCurrency +
                ", isNullable=" + isNullable +
                ", isReadOnly=" + isReadOnly +
                ", sqlId=" + sqlId +
                '}';
    }
}
