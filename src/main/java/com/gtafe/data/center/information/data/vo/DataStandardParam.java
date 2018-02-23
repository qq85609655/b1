package com.gtafe.data.center.information.data.vo;

import com.gtafe.data.center.common.common.vo.PageParam;

public class DataStandardParam extends PageParam{
    private String keyWord;
    private int sourceId;
    private String subsetCode;
    private String classCode;
    public String getKeyWord() {
        return keyWord;
    }
    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }
    public int getSourceId() {
        return sourceId;
    }
    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }
    public String getSubsetCode() {
        return subsetCode;
    }
    public void setSubsetCode(String subsetCode) {
        this.subsetCode = subsetCode;
    }
    public String getClassCode() {
        return classCode;
    }
    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }
}
