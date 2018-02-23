package com.gtafe.data.center.system.role.vo;

import com.gtafe.data.center.common.common.vo.PageParam;

public class RoleParam extends PageParam{
    private int state;
    private String roleName;
    public int getState() {
        return state;
    }
    public void setState(int state) {
        this.state = state;
    }
    public String getRoleName() {
        return roleName;
    }
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
