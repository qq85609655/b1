package com.gtafe.data.center.dataetl.datatask.vo.rule.rulevo;

import java.util.List;

/**
 * @Author: gang, zhou
 * @Date: 2017/11/20
 * @Description:
 */
public class ConcatfieldVo {

    private List<SourceFieldsVo> concatFields;
    private String targetField;
    private String delimiter;

    public List<SourceFieldsVo> getConcatFields() {
        return concatFields;
    }

    public void setConcatFields(List<SourceFieldsVo> concatFields) {
        this.concatFields = concatFields;
    }

    public String getTargetField() {
        return targetField;
    }

    public void setTargetField(String targetField) {
        this.targetField = targetField;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }
}
