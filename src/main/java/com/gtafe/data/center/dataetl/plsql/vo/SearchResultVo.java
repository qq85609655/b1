package com.gtafe.data.center.dataetl.plsql.vo;


import java.util.List;

/**
 * 查询结果
 */
public class SearchResultVo {
    private String sqlName;

    private String dbType;

    private long dataCount;

    private List<ItemDetailVo> itemDetailVos;

    private List<Object[]> datas;

    public long getDataCount() {
        return dataCount;
    }

    public void setDataCount(long dataCount) {
        this.dataCount = dataCount;
    }

    public List<ItemDetailVo> getItemDetailVos() {
        return itemDetailVos;
    }

    public void setItemDetailVos(List<ItemDetailVo> itemDetailVos) {
        this.itemDetailVos = itemDetailVos;
    }

    public List<Object[]> getDatas() {
        return datas;
    }

    public void setDatas(List<Object[]> datas) {
        this.datas = datas;
    }

    @Override
    public String toString() {
        return "SearchResultVo{" +
                "sqlName='" + sqlName + '\'' +
                ", dbType='" + dbType + '\'' +
                ", dataCount=" + dataCount +
                ", itemDetailVos=" + itemDetailVos +
                ", datas=" + datas +
                '}';
    }

    public String getSqlName() {
        return sqlName;
    }

    public void setSqlName(String sqlName) {
        this.sqlName = sqlName;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }
}
