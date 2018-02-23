package com.gtafe.data.center.information.code.vo;

import java.util.List;

public class CodeNodeVo {
    private int nodeId;
    private int parentnodeId;
    private String code;
    private String name;
    private String description;
    //类型，0跟节点，1集合，2分类
    private int nodeType;
    private int sourceId;
    private int creater;
    private int updater;

    public int getCreater() {
        return creater;
    }

    public void setCreater(int creater) {
        this.creater = creater;
    }

    public int getUpdater() {
        return updater;
    }

    public void setUpdater(int updater) {
        this.updater = updater;
    }

    private List<CodeNodeVo> children;
    public int getNodeId() {
        return nodeId;
    }
    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }
    public int getParentnodeId() {
        return parentnodeId;
    }
    public void setParentnodeId(int parentnodeId) {
        this.parentnodeId = parentnodeId;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
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
    public int getNodeType() {
        return nodeType;
    }
    public void setNodeType(int nodeType) {
        this.nodeType = nodeType;
    }
    public int getSourceId() {
        return sourceId;
    }
    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }
    public List<CodeNodeVo> getChildren() {
        return children;
    }
    public void setChildren(List<CodeNodeVo> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "CodeNodeVo{" +
                "nodeId=" + nodeId +
                ", parentnodeId=" + parentnodeId +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", nodeType=" + nodeType +
                ", sourceId=" + sourceId +
                ", children=" + children +
                '}';
    }
}
