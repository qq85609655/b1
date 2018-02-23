package com.gtafe.data.center.metadata.item.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import com.gtafe.framework.base.vo.BaseVO;

@ApiModel
public class ItemVO extends BaseVO {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "编号", required = true)
	private int id;

 
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@ApiModelProperty(value = "子类编号", required = true)
	private String subclassCode;

	@ApiModelProperty(value = "数据子类中文名称", required = false)
	private String subclassComment;
	
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
	@ApiModelProperty(value = "引用管理", required = false)
	private String dataReferenced;
	@ApiModelProperty(value = "是否可选，默认M=必选，O=必选", required = true)
	private String selectable;
	@ApiModelProperty(value = "代码标准分类的自增序列id", required = false)
	private int sourceid;
	@ApiModelProperty(value = "代码标准集合的id", required = false)
	private int setid;
	@ApiModelProperty(value = "代码标准集合中的分类的id", required = false)
	private int typeid;
	@ApiModelProperty(value = "代码标准集合中的代码id", required = false)
	private int codeid;
	
	
	public String getSubclassCode() {
		return subclassCode;
	}
	public void setSubclassCode(String subclassCode) {
		this.subclassCode = subclassCode;
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
	public String getDataReferenced() {
		return dataReferenced;
	}
	public void setDataReferenced(String dataReferenced) {
		this.dataReferenced = dataReferenced;
	}
	public String getSelectable() {
		return selectable;
	}
	public void setSelectable(String selectable) {
		this.selectable = selectable;
	}

	public int getSourceid() {
		return sourceid;
	}
	public void setSourceid(int sourceid) {
		this.sourceid = sourceid;
	}
	public int getSetid() {
		return setid;
	}
	public void setSetid(int setid) {
		this.setid = setid;
	}
	public int getTypeid() {
		return typeid;
	}
	public void setTypeid(int typeid) {
		this.typeid = typeid;
	}
	public int getCodeid() {
		return codeid;
	}
	public void setCodeid(int codeid) {
		this.codeid = codeid;
	}
	public String getSubclassComment() {
		return subclassComment;
	}
	public void setSubclassComment(String subclassComment) {
		this.subclassComment = subclassComment;
	}

	@Override
	public String toString() {
		return "ItemVO{" +
				"id=" + id +
				", subclassCode='" + subclassCode + '\'' +
				", subclassComment='" + subclassComment + '\'' +
				", itemCode='" + itemCode + '\'' +
				", itemName='" + itemName + '\'' +
				", itemComment='" + itemComment + '\'' +
				", dataType='" + dataType + '\'' +
				", dataLength='" + dataLength + '\'' +
				", dataPrimarykey=" + dataPrimarykey +
				", dataNullable=" + dataNullable +
				", dataValueSource='" + dataValueSource + '\'' +
				", dataExplain='" + dataExplain + '\'' +
				", dataReferenced='" + dataReferenced + '\'' +
				", selectable='" + selectable + '\'' +
				", sourceid=" + sourceid +
				", setid=" + setid +
				", typeid=" + typeid +
				", codeid=" + codeid +
				'}';
	}
}
