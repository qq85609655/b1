package com.gtafe.data.center.information.code.vo;

import com.gtafe.data.center.common.common.vo.PageParam;

public class CodeInfoParam extends PageParam {
    private String keyWord;
    private int nodeId;
    private int sourceId;
    private int nodeType;
    public String getKeyWord() {
        return keyWord;
    }
    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }
    public int getNodeId() {
        return nodeId;
    }
    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
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

    @Override
    public String toString() {
        return "CodeInfoParam{" +
                "keyWord='" + keyWord + '\'' +
                ", nodeId=" + nodeId +
                ", sourceId=" + sourceId +
                ", nodeType=" + nodeType +
                '}';
    }
}
