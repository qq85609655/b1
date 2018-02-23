/** 
 * Project Name:gtacore 
 * File Name:	ISourceVO.java 
 * Description: 
 * Date: 2017-08-14 17:50:40
 * Author: Xiang Zhiling
 * History:
 * Copyright (c) 2017, GTA All Rights Reserved. 
 * 
 */  
 
package com.gtafe.data.center.dataetl.datasource.vo;

import com.gtafe.framework.base.vo.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 
 * ClassName: SourceVO <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason: TODO ADD REASON. <br/> 
 * date: 2017-08-14 17:50:40
 * 
 * @author Xiang Zhiling
 * @version  
 * @since JDK 1.7
 */  
@ApiModel
public class ColumnVO extends BaseVO {

	/** 
	 * serialVersionUID:TODO
	 * @since JDK 1.7
	 */  
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "字段名", required = false)
	private String columnName;
	@ApiModelProperty(value = "数据类型", required = false)
	private String dataType;
	@ApiModelProperty(value = "数据长度", required = false)
	private String dataLength;
	@ApiModelProperty(value = "是否数据主键:0=否，1=是", required = false)
	private int dataPrimarykey;
	@ApiModelProperty(value = "是否可空，0=否，1=是", required = false)
    private int dataNullable;
	@ApiModelProperty(value = "注释", required = false)
    private String columnComment;
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
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
    public String getColumnComment() {
        return columnComment;
    }
    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment;
    }
    @Override
    public String toString() {
        return "ColumnVO [columnName=" + columnName + ", dataType=" + dataType
               + ", dataLength=" + dataLength + ", dataPrimarykey="
               + dataPrimarykey + ", dataNullable=" + dataNullable
               + ", columnComment=" + columnComment + "]";
    }

}
