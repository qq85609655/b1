package com.gtafe.data.center.system.role.vo;

import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/*
    角色实体bean
 */

@ApiModel
public class RoleVo {

    @ApiModelProperty(value = "主键", required = true)
    private int roleId;

    @ApiModelProperty(value = "角色名称", required = true)
    private String roleName;//角色名称

    @ApiModelProperty(value = "备注", required = true)
    private String remark; //备注

    @ApiModelProperty(value = "状态", required = true)
    private boolean state;//状态 true:有效     false: 无效

    @ApiModelProperty(value = "关联人数", required = true)
    private int ncount;//关联人数

    @ApiModelProperty(value = "创建人id", required = true)
    private String creater;
    @ApiModelProperty(value = "创建时间", required = true)
    private Date createTime;
    @ApiModelProperty(value = "修改人id", required = true)
    private String updater;
    @ApiModelProperty(value = "修改时间", required = true)
    private Date updateTime;

    //角色权限关系表
    private List<RoleAuthVo> roleAuthVos;
    //角色数据关系表
    private List<RoleDataVo> roleDataVos;

    public List<RoleAuthVo> getRoleAuthVos() {
        return roleAuthVos;
    }

    public void setRoleAuthVos(List<RoleAuthVo> roleAuthVos) {
        this.roleAuthVos = roleAuthVos;
    }

    public List<RoleDataVo> getRoleDataVos() {
        return roleDataVos;
    }

    public void setRoleDataVos(List<RoleDataVo> roleDataVos) {
        this.roleDataVos = roleDataVos;
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

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public int getNcount() {
        return ncount;
    }

    public void setNcount(int ncount) {
        this.ncount = ncount;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreator(String creater) {
        this.creater = creater;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "RoleVo{" +
                "roleId=" + roleId +
                ", roleName='" + roleName + '\'' +
                ", remark='" + remark + '\'' +
                ", state='" + state + '\'' +
                ", ncount=" + ncount +
                ", creater='" + creater + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updater='" + updater + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", roleAuthVos=" + roleAuthVos +
                ", roleDataVos=" + roleDataVos +
                '}';
    }
}
