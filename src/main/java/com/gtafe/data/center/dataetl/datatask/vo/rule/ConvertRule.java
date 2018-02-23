package com.gtafe.data.center.dataetl.datatask.vo.rule;

public class ConvertRule {
    //唯一编号
    public int id;
    //规则名称
    public String name;
    /**
     * 规则类型
     * <br>1源 ,2目标
     * <br> 3 字符串操作,4 字符串剪切,5 字符串替换,6 增加常量,
     * <br> 7 值映射,8 拆分字段,9 合并字段, 10 数值范围,
     * <br> 11 类型转换, 12 列拆分为多行, 13 计算器, 14 去除重复记录
     */
    public int type;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
}
