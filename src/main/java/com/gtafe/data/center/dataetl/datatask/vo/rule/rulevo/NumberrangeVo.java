package com.gtafe.data.center.dataetl.datatask.vo.rule.rulevo;

import java.util.List;

/**
 * @Author: gang, zhou
 * @Date: 2017/11/20
 * @Description:
 */
public class NumberrangeVo {

    private  String sourceField;
    private String targetField;
    private  String defValue;
    private List<RangeVo> numberranges;

    public String getSourceField() {
        return sourceField;
    }

    public void setSourceField(String sourceField) {
        this.sourceField = sourceField;
    }

    public String getTargetField() {
        return targetField;
    }

    public void setTargetField(String targetField) {
        this.targetField = targetField;
    }

    public String getDefValue() {
        return defValue;
    }

    public void setDefValue(String defValue) {
        this.defValue = defValue;
    }

    public List<RangeVo> getNumberranges() {
        return numberranges;
    }

    public void setNumberranges(List<RangeVo> numberranges) {
        this.numberranges = numberranges;
    }
}
