package com.gtafe.data.center.dataetl.plsql.vo;

import java.util.List;
import java.util.Map;

/**
 * 查询结果
 */
public class SearchResultVo {

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
                "dataCount=" + dataCount +
                ", itemDetailVos=" + itemDetailVos +
                ", datas=" + datas +
                '}';
    }
}
