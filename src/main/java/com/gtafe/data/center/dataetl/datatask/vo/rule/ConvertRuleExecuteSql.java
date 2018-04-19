package com.gtafe.data.center.dataetl.datatask.vo.rule;

import com.gtafe.data.center.dataetl.datatask.vo.rule.rulevo.ExecuteSqlVo;

import java.util.List;

public class ConvertRuleExecuteSql extends ConvertRule {
    private List<ExecuteSqlVo> dataList;

    /**
     * 当前步骤产生的输出字段
     */
    private List<TableFieldVo> outputs;

    public List<ExecuteSqlVo> getDataList() {
        return dataList;
    }

    public void setDataList(List<ExecuteSqlVo> dataList) {
        this.dataList = dataList;
    }

    public List<TableFieldVo> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<TableFieldVo> outputs) {
        this.outputs = outputs;
    }
}
