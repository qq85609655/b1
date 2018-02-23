package com.gtafe.data.center.dataetl.datatask.vo.rule.rulevo;

import java.util.List;

/**
 * @Author: gang, zhou
 * @Date: 2017/11/20
 * @Description:
 */
public class SplitfieldVo {

    private String sourceField;
    private List<SplitfieldTargetVo> splitList;
    private String delimiter;

    public String getSourceField() {
        return sourceField;
    }

    public void setSourceField(String sourceField) {
        this.sourceField = sourceField;
    }

    public List<SplitfieldTargetVo> getSplitList() {
        return splitList;
    }

    public void setSplitList(List<SplitfieldTargetVo> splitList) {
        this.splitList = splitList;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }
}
