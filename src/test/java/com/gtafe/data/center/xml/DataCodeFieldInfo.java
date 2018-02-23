package com.gtafe.data.center.xml;

import java.math.BigDecimal;

public class DataCodeFieldInfo {
    public String itemCode;
    public String itemName;
    public String itemComment;
    public String dataType;
    public String dataLength;
    private int primarykey=0;
    private int nullable=1;
    private String explain;
    private String referenced;
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
        if(dataType.equals("")) {
            String comment = this.getItemComment().trim();
            comment = comment.replaceAll("\\s", "");
            if(comment.endsWith("日期")) {
                this.dataType = "D";
            }else {
                this.dataType = "C";
            }
        }else {
            this.dataType = dataType;
        }
    }
    public String getDataLength() {
        return dataLength;
    }
    public void setDataLength(String dataLength) {
        if(dataLength.equals("")) {
            if(this.dataType.equals("C")) {
                this.dataLength = "50";
            }else if(this.dataType.equals("N") || this.dataType.equals("M")) {
                this.dataLength = "12,2";
            }else if(this.dataType.equals("D")) {
                this.dataLength = "0";
            }else if(this.dataType.equals("T")) {
                this.dataLength = "65535";
            }else {
                this.dataLength = "0";
            }
        }else {
            if(dataLength.contains(",")) {
                String[] ds = dataLength.split(",");
                this.dataLength = new BigDecimal(ds[0]).intValue()+","+new BigDecimal(ds[1]).intValue();
            }else {
                this.dataLength = new BigDecimal(dataLength).intValue()+"";
            }
        }
    }
    public int getPrimarykey() {
        return primarykey;
    }
    public void setPrimarykey(int primarykey) {
        this.primarykey = primarykey;
    }
    public int getNullable() {
        return nullable;
    }
    public void setNullable(String nullable) {
        nullable = nullable.trim().replaceAll("\\s", "");
        if("M".equals(nullable) || "约束".equals(nullable)) {
            this.nullable = 0;
        }else {
            this.nullable = 1;
        }
    }
    public String getExplain() {
        return explain;
    }
    public void setExplain(String explain) {
        this.explain = explain;
    }
    public String getReferenced() {
        return referenced;
    }
    public void setReferenced(String referenced) {
        this.referenced = referenced;
    }
}
