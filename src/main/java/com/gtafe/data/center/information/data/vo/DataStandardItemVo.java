package com.gtafe.data.center.information.data.vo;

import io.swagger.annotations.ApiModelProperty;

public class DataStandardItemVo {
    @ApiModelProperty(value = "编号", required = true)
    private int id;
    private String subclassCode;
    @ApiModelProperty(value = "数据子类中文名称", required = false)
    private String subclassName;
    @ApiModelProperty(value = "元数据编号", required = true)
    private String itemCode;
    @ApiModelProperty(value = "元数据名称", required = true)
    private String itemName;
    @ApiModelProperty(value = "元数据注释", required = false)
    private String itemComment;
    @ApiModelProperty(value = "数据类型", required = true)
    private String dataType;
    @ApiModelProperty(value = "数据长度", required = true)
    private String dataLength;
    @ApiModelProperty(value = "是否数据主键:0=否，1=是", required = true)
    private int dataPrimarykey;
    @ApiModelProperty(value = "是否可空，0=否，1=是", required = true)
    private int dataNullable;
    @ApiModelProperty(value = "取值范围", required = false)
    private String dataValueSource;
    @ApiModelProperty(value = "说明/示例", required = false)
    private String dataExplain;

    public String getDataReferenced() {
        return dataReferenced;
    }

    public void setDataReferenced(String dataReferenced) {
        this.dataReferenced = dataReferenced;
    }

    @ApiModelProperty(value = "引用管理", required = false)
    private String dataReferenced;

    private String dataReferenced1;
    @ApiModelProperty(value = "是否可选，默认M=必选，O=必选", required = true)
    private String dataReferenced2;
    @ApiModelProperty(value = "是否可选，默认M=必选，O=必选", required = true)
    private String dataReferenced3;
    @ApiModelProperty(value = "是否可选，默认M=必选，O=必选", required = true)
    private String selectable;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubclassCode() {
        return subclassCode;
    }

    public void setSubclassCode(String subclassCode) {
        this.subclassCode = subclassCode;
    }

    public String getSubclassName() {
        return subclassName;
    }

    public void setSubclassName(String subclassName) {
        this.subclassName = subclassName;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemComment() {
        return itemComment;
    }

    public void setItemComment(String itemComment) {
        this.itemComment = itemComment;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDataLength() {
        return dataLength;
    }

    public void setDataLength(String dataLength) {
        this.dataLength = dataLength;
    }

    public int getDataPrimarykey() {
        return dataPrimarykey;
    }

    public void setDataPrimarykey(int dataPrimarykey) {
        this.dataPrimarykey = dataPrimarykey;
    }

    public int getDataNullable() {
        return dataNullable;
    }

    public void setDataNullable(int dataNullable) {
        this.dataNullable = dataNullable;
    }

    public String getDataValueSource() {
        return dataValueSource;
    }

    public void setDataValueSource(String dataValueSource) {
        this.dataValueSource = dataValueSource;
    }

    public String getDataExplain() {
        return dataExplain;
    }

    public void setDataExplain(String dataExplain) {
        this.dataExplain = dataExplain;
    }

    public String getDataReferenced1() {
        return dataReferenced1;
    }

    public void setDataReferenced1(String dataReferenced1) {
        this.dataReferenced1 = dataReferenced1;
    }

    public String getDataReferenced2() {
        return dataReferenced2;
    }

    public void setDataReferenced2(String dataReferenced2) {
        this.dataReferenced2 = dataReferenced2;
    }

    public String getDataReferenced3() {
        return dataReferenced3;
    }

    public void setDataReferenced3(String dataReferenced3) {
        this.dataReferenced3 = dataReferenced3;
    }

    public String getSelectable() {
        return selectable;
    }

    public void setSelectable(String selectable) {
        this.selectable = selectable;
    }
}
