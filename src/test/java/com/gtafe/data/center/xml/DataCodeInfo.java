package com.gtafe.data.center.xml;

import java.util.List;

public class DataCodeInfo {
    public String code;
    public String parentCode;
    public String codeName;
    public String description;
    public String tableName;
    public int nodeType;
    public List<DataCodeFieldInfo> fields;
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getParentCode() {
        return parentCode;
    }
    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }
    public String getCodeName() {
        return codeName;
    }
    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getTableName() {
        return tableName;
    }
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    public int getNodeType() {
        return nodeType;
    }
    public void setNodeType(int nodeType) {
        this.nodeType = nodeType;
    }
    public List<DataCodeFieldInfo> getFields() {
        return fields;
    }
    public void setFields(List<DataCodeFieldInfo> fields) {
        this.fields = fields;
    }
}
