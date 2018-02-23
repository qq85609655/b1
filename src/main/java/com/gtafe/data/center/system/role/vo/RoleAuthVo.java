package com.gtafe.data.center.system.role.vo;



public class RoleAuthVo {
    private int id;
    private int roleId;
    private String authCode;

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

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    @Override
    public String toString() {
        return "RoleAuthVo{" +
                "id=" + id +
                ", roleId=" + roleId +
                ", authCode='" + authCode + '\'' +
                '}';
    }
}
