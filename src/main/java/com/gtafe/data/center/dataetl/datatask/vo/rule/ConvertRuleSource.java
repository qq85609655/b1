package com.gtafe.data.center.dataetl.datatask.vo.rule;

import java.util.List;

import com.gtafe.data.center.dataetl.datatask.vo.rule.rulevo.SourceTargetVo;
/**
 * 源转换规则
 * 名称：ConvertRuleSource <br>
 * 描述：〈功能详细描述〉<br>
 * @author 汪逢建
 * @version 1.0
 * @date 2017年11月15日
 */
public class ConvertRuleSource extends ConvertRule {
    private SourceTargetVo data;
    private List<TableFieldVo> sourceList;
    public List<TableFieldVo> getSourceList() {
        return sourceList;
    }
    public void setSourceList(List<TableFieldVo> sourceList) {
        this.sourceList = sourceList;
    }
    public SourceTargetVo getData() {
        return data;
    }
    public void setData(SourceTargetVo data) {
        this.data = data;
    }
}
