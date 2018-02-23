package com.gtafe.data.center.system.role.vo;

import java.util.List;

public class TempUserRoleVo {
    private int userId;
    private List<Integer> hasRoleIds;
    private List<RoleVo> list;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<Integer> getHasRoleIds() {
        return hasRoleIds;
    }

    public void setHasRoleIds(List<Integer> hasRoleIds) {
        this.hasRoleIds = hasRoleIds;
    }

    public List<RoleVo> getList() {
        return list;
    }

    public void setList(List<RoleVo> list) {
        this.list = list;
    }
}
