package com.gtafe.data.center.system.role.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel
public class RoleInfoVo {
    @ApiModelProperty(value = "主键", required = true)
    private int roleId;
    @ApiModelProperty(value = "角色名称", required = true)
    private String roleName;//角色名称
    @ApiModelProperty(value = "备注", required = true)
    private String remark; //备注
    @ApiModelProperty(value = "功能授权列表", required = true)
    private List<String> authList;
    @ApiModelProperty(value = "数据授权列表", required = true)
    private List<Integer> orgList;

    @ApiModelProperty(value = "状态", required = true)
    boolean state;

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public int getRoleId() {
        return roleId;
    }
    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
    public String getRoleName() {
        return roleName;
    }
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public List<String> getAuthList() {
        return authList;
    }
    public void setAuthList(List<String> authList) {
        this.authList = authList;
    }
    public List<Integer> getOrgList() {
        return orgList;
    }
    public void setOrgList(List<Integer> orgList) {
        this.orgList = orgList;
    }

    @Override
    public String toString() {
        return "RoleInfoVo{" +
                "roleId=" + roleId +
                ", roleName='" + roleName + '\'' +
                ", remark='" + remark + '\'' +
                ", authList=" + authList +
                ", orgList=" + orgList +
                '}';
    }
}
