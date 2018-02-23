package com.gtafe.data.center.dataetl.datatask.vo.rule.rulevo;
/**
 * 字符串操作vo
 * blankList=[{label: "不去除", value: 0},{label: "去除左空格", value: 1},{label: "去除右空格", value: 2},{label: "去除左右空格", value: 3}];
 * caseList=[{label: "不转换", value: 0},{label: "转大写", value: 2},{label: "转小写", value: 1}];
 * paddingTypeList=[{label: "不填充", value: 0},{label: "向左填充", value: 1},{label: "向右填充", value: 2}];
 * numberList=[{label: "不处理", value: 0},{label: "只保留数字", value: 1},{label: "去除数字", value: 2}];
 * specialList=[{label: "不处理", value: 0},{label: "去除回车符", value: 1},{label: "去除换行", value: 2},{label: "去除回车和换行符", value: 3},{label: "去除空格", value: 5}];
 * 名称：StringoperVo <br>
 * 描述：〈功能详细描述〉<br>
 * @author 汪逢建
 * @version 1.0
 * @date 2017年11月15日
 */
public class StringoperVo {
    //输入字段
    private String sourceField;
    //输出字段
    private String targetField;
    //空格
    private int blank;
    //大小写
    private int caseType;
    //特殊不可见字符
    private int special;
    //数字
    private int number;
    //填充类型
    private int paddingType;
    //填充长度
    private int paddingLength;
    //填充字符
    private String paddingChar;
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
    public int getBlank() {
        return blank;
    }
    public void setBlank(int blank) {
        this.blank = blank;
    }
    public int getCaseType() {
        return caseType;
    }
    public void setCaseType(int caseType) {
        this.caseType = caseType;
    }
    public int getSpecial() {
        return special;
    }
    public void setSpecial(int special) {
        this.special = special;
    }
    public int getNumber() {
        return number;
    }
    public void setNumber(int number) {
        this.number = number;
    }
    public int getPaddingType() {
        return paddingType;
    }
    public void setPaddingType(int paddingType) {
        this.paddingType = paddingType;
    }
    public int getPaddingLength() {
        return paddingLength;
    }
    public void setPaddingLength(int paddingLength) {
        this.paddingLength = paddingLength;
    }
    public String getPaddingChar() {
        return paddingChar;
    }
    public void setPaddingChar(String paddingChar) {
        this.paddingChar = paddingChar;
    }
}
