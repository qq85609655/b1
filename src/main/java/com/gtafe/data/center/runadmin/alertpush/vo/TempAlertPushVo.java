package com.gtafe.data.center.runadmin.alertpush.vo;

import java.util.List;

import com.gtafe.data.center.dataetl.datatask.vo.DataTaskVo;

/**
 * @Author: gang, zhou
 * @Date: 2017/11/11
 * @Description:
 */
public class TempAlertPushVo {
    private int userId;
    private List<Integer> hasTaskIds;
    private List<DataTaskVo> list;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<Integer> getHasTaskIds() {
        return hasTaskIds;
    }

    public void setHasTaskIds(List<Integer> hasTaskIds) {
        this.hasTaskIds = hasTaskIds;
    }

    public List<DataTaskVo> getList() {
        return list;
    }

    public void setList(List<DataTaskVo> list) {
        this.list = list;
    }
}
