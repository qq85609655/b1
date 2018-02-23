package com.gtafe.data.center.system.role.vo;

import com.gtafe.framework.base.vo.BaseVO;

public class RoleDataVo   {

    private int id;
    private int roleId;
    private int orgId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    @Override
    public String toString() {
        return "RoleDataVo{" +
                "id=" + id +
                ", roleId=" + roleId +
                ", orgId=" + orgId +
                '}';
    }
}
