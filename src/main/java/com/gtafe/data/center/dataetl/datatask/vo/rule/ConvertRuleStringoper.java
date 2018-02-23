package com.gtafe.data.center.dataetl.datatask.vo.rule;

import java.util.List;

import com.gtafe.data.center.dataetl.datatask.vo.rule.rulevo.StringoperVo;

/**
 * 字符串操作规则
 * 名称：ConvertRuleStringoper <br>
 * 描述：〈功能详细描述〉<br>
 * @author 汪逢建
 * @version 1.0
 * @date 2017年11月15日
 */
public class ConvertRuleStringoper extends ConvertRule{
    /**
     * 设置的转换关系
     */
    private List<StringoperVo> dataList;
    
    /**
     * 当前步骤产生的输出字段
     */
    private List<TableFieldVo> outputs;

    public List<StringoperVo> getDataList() {
        return dataList;
    }

    public void setDataList(List<StringoperVo> dataList) {
        this.dataList = dataList;
    }

    public List<TableFieldVo> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<TableFieldVo> outputs) {
        this.outputs = outputs;
    }
    
}
