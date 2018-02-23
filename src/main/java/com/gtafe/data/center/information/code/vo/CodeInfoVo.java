package com.gtafe.data.center.information.code.vo;

public class CodeInfoVo {
    /**
     * 代码ID
     */
    private int codeId;
    /**
     * 树节点ID
     */
    private int nodeId;
    private String nodeName;
    private String code;
    private String name;
    private String description;
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

    public int getCodeId() {
        return codeId;
    }
    public void setCodeId(int codeId) {
        this.codeId = codeId;
    }
    public int getNodeId() {
        return nodeId;
    }
    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
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
    public int getSourceId() {
        return sourceId;
    }
    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }
    public String getNodeName() {
        return nodeName;
    }
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    @Override
    public String toString() {
        return "CodeInfoVo{" +
                "codeId=" + codeId +
                ", nodeId=" + nodeId +
                ", nodeName='" + nodeName + '\'' +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", sourceId=" + sourceId +
                '}';
    }
}
