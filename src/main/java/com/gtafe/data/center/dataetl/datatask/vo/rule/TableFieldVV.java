package com.gtafe.data.center.dataetl.datatask.vo.rule;

import java.util.List;

public class TableFieldVV {
    private List<TableFieldVo> tableFieldVoList;
    private String keyComment;
    private int ketAutoAddCount;//主键自增的数量

    public List<TableFieldVo> getTableFieldVoList() {
        return tableFieldVoList;
    }

    public void setTableFieldVoList(List<TableFieldVo> tableFieldVoList) {
        this.tableFieldVoList = tableFieldVoList;
    }

    public String getKeyComment() {
        return keyComment;
    }

    public void setKeyComment(String keyComment) {
        this.keyComment = keyComment;
    }

    public int getKetAutoAddCount() {
        return ketAutoAddCount;
    }

    public void setKetAutoAddCount(int ketAutoAddCount) {
        this.ketAutoAddCount = ketAutoAddCount;
    }
}
