package com.gtafe.data.center.dataetl.datatask.vo.rule.rulevo;

import java.util.List;

public class ExecuteSqlVo {

    private List<SourceFieldsVo> paramFields;
    private String sqlStr;

    public List<SourceFieldsVo> getParamFields() {
        return paramFields;
    }

    public void setParamFields(List<SourceFieldsVo> paramFields) {
        this.paramFields = paramFields;
    }

    public String getSqlStr() {
        return sqlStr;
    }

    public void setSqlStr(String sqlStr) {
        this.sqlStr = sqlStr;
    }
}
