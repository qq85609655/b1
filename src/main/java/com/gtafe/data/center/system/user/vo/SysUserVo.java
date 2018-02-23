package com.gtafe.data.center.system.user.vo;

import com.gtafe.data.center.system.role.vo.RoleVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

@ApiModel
public class SysUserVo {

    @ApiModelProperty(value = "主鍵", required = true)
    private int userId;
    @ApiModelProperty(value = "用户编号", required = true)
    private String userNo;
    @ApiModelProperty(value = "姓名", required = true)
    private String realName;
    @ApiModelProperty(value = "密码", required = true)
    private String loginPwd;
    @ApiModelProperty(value = "邮箱", required = true)
    private String email;
    @ApiModelProperty(value = "电话", required = true)
    private String phone;
    @ApiModelProperty(value = "性别", required = true)
    private int sex;
    @ApiModelProperty(value = "状态 1：有效 0 无效 ", required = true)
    private boolean state;

    List<RoleVo> roles;

    @ApiModelProperty(value = "是否发送错误日志邮件状态 1：有效 0 无效 ", required = true)
    private boolean sendErrorState;

    @ApiModelProperty(value = "机构id", required = true)
    private int orgId;
    private int creater;
    private Date createTime;
    private String createTimeStr;


    private int updater;
    private Date updateTime;
    
    private int userType;

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    private String orgName;//用户所在组织机构名称

    public String getLoginPwd() {
        return loginPwd;
    }

    public void setLoginPwd(String loginPwd) {
        this.loginPwd = loginPwd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }


    public int getCreater() {
        return creater;
    }

    public void setCreater(int creater) {
        this.creater = creater;
    }

    public int getUpdater() {
        return updater;
    }

    public void setUpdater(int updater) {
        this.updater = updater;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public List<RoleVo> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleVo> roles) {
        this.roles = roles;
    }

    public boolean isSendErrorState() {
        return sendErrorState;
    }

    public void setSendErrorState(boolean sendErrorState) {
        this.sendErrorState = sendErrorState;
    }

    @Override
    public String toString() {
        return "SysUserVo{" +
                "userId=" + userId +
                ", userNo='" + userNo + '\'' +
                ", realName='" + realName + '\'' +
                ", loginPwd='" + loginPwd + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", sex=" + sex +
                ", state=" + state +
                ", orgId=" + orgId +
                ", creater=" + creater +
                ", createTime=" + createTime +
                ", updater=" + updater +
                ", updateTime=" + updateTime +
                ", userType=" + userType +
                ", orgName='" + orgName + '\'' +
                '}';
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }
}
