package com.gtafe.data.center.dataetl.datatask.vo.rule;

import com.gtafe.data.center.dataetl.datatask.vo.rule.rulevo.NumberrangeVo;

import java.util.List;

/**
 * @Author: gang, zhou
 * @Date: 2017/11/20
 * @Description:
 */
public class ConvertRuleNumberrange extends ConvertRule {

    /**
     * 设置的转换关系
     */
    private List<NumberrangeVo> dataList;

    /**
     * 当前步骤产生的输出字段
     */
    private List<TableFieldVo> outputs;

    public List<NumberrangeVo> getDataList() {
        return dataList;
    }

    public void setDataList(List<NumberrangeVo> dataList) {
        this.dataList = dataList;
    }

    public List<TableFieldVo> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<TableFieldVo> outputs) {
        this.outputs = outputs;
    }

}
