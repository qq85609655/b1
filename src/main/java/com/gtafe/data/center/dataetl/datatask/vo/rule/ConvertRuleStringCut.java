package com.gtafe.data.center.dataetl.datatask.vo.rule;

import com.gtafe.data.center.dataetl.datatask.vo.rule.rulevo.StringCutVo;
import com.gtafe.data.center.dataetl.trans.StringCut;

import java.util.List;

/**
 * @Author: gang, zhou
 * @Date: 2017/11/20
 * @Description:
 */
public class ConvertRuleStringCut  extends ConvertRule{

    /**
     * 设置的转换关系
     */
    private List<StringCutVo> dataList;

    /**
     * 当前步骤产生的输出字段
     */
    private List<TableFieldVo> outputs;

    public List<StringCutVo> getDataList() {
        return dataList;
    }

    public void setDataList(List<StringCutVo> dataList) {
        this.dataList = dataList;
    }

    public List<TableFieldVo> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<TableFieldVo> outputs) {
        this.outputs = outputs;
    }
}
