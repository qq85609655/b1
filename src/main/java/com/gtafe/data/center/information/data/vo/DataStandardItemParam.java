package com.gtafe.data.center.information.data.vo;

import org.springframework.web.bind.annotation.RequestParam;

import com.gtafe.data.center.common.common.vo.PageParam;

public class DataStandardItemParam extends PageParam{
     private int sourceId;
     private String code;
     private int nodeType;
     private String keyWord;
    public int getSourceId() {
        return sourceId;
    }
    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public int getNodeType() {
        return nodeType;
    }
    public void setNodeType(int nodeType) {
        this.nodeType = nodeType;
    }
    public String getKeyWord() {
        return keyWord;
    }
    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }
}
