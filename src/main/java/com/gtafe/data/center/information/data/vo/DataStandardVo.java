package com.gtafe.data.center.information.data.vo;

import java.util.List;

public class DataStandardVo {
    //编号
    private String code;
    //父节点编号
    private String parentCode;
    //名称
    private String name;
    //描述
    private String description;
    //表名
    private String tableName;
    //数据类型（学校，国家）
    private int sourceId;
    //0根节点，1子集，2类，3子类
    private int nodeType;
    
    private List<DataStandardVo> children;
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
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
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
    public int getSourceId() {
        return sourceId;
    }
    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }
    public int getNodeType() {
        return nodeType;
    }
    public void setNodeType(int nodeType) {
        this.nodeType = nodeType;
    }
    public List<DataStandardVo> getChildren() {
        return children;
    }
    public void setChildren(List<DataStandardVo> children) {
        this.children = children;
    }
}
