package com.gtafe.data.center.dataetl.datatask.vo.rule;

import com.gtafe.data.center.dataetl.datatask.vo.rule.rulevo.DynamicValueMappingVo;
import com.gtafe.data.center.dataetl.datatask.vo.rule.rulevo.ValuemapperVo;

import java.util.List;

/**
 * @Author: gang, zhou
 * @Date: 2018/04/20
 * @Description:
 */
public class ConvertRuleDynamicValuemapper extends ConvertRule {

    private List<DynamicValueMappingVo> dataList;

    /**
     * 当前步骤产生的输出字段
     */
    private List<TableFieldVo> outputs;

    public List<DynamicValueMappingVo> getDataList() {
        return dataList;
    }

    public void setDataList(List<DynamicValueMappingVo> dataList) {
        this.dataList = dataList;
    }

    public List<TableFieldVo> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<TableFieldVo> outputs) {
        this.outputs = outputs;
    }
}
