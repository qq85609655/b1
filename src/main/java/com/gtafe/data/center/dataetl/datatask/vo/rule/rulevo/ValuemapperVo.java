package com.gtafe.data.center.dataetl.datatask.vo.rule.rulevo;

import java.util.List;

/**
 * @Author: gang, zhou
 * @Date: 2017/11/20
 * @Description:
 */
public class ValuemapperVo {

    private String sourceField;

    private String targetField;

    private String defValue;

    private List<ValuemapperMapperVo> mappings;

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

    public List<ValuemapperMapperVo> getMappings() {
        return mappings;
    }

    public void setMappings(List<ValuemapperMapperVo> mappings) {
        this.mappings = mappings;
    }
}
