package com.gtafe.data.center.dataetl.datatask.vo.rule.rulevo;

/**字符串去重
 * @Author: gang, zhou
 * @Date: 2017/11/20
 * @Description:
 */
public class UniquerowVo {

    private  String sourceField;

    private int ignore;//是否忽略大小写

    public String getSourceField() {
        return sourceField;
    }

    public void setSourceField(String sourceField) {
        this.sourceField = sourceField;
    }

    public int getIgnore() {
        return ignore;
    }

    public void setIgnore(int ignore) {
        this.ignore = ignore;
    }
}
