package com.gtafe.data.center.common.login.vo;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class UserLoginInfo {
    @ApiModelProperty(value = "用户Id", required = true)
    private String userId;
    @ApiModelProperty(value = "用户编号", required = true)
    private String userNo;
    @ApiModelProperty(value = "姓名", required = true)
    private String realName;
    @ApiModelProperty(value = "邮箱", required = true)
    private String email;
    @ApiModelProperty(value = "电话", required = true)
    private String phone;
    @ApiModelProperty(value = "性别，1男，2女", required = true)
    private int sex;
    @ApiModelProperty(value = "机构id", required = true)
    private String orgId;
    @ApiModelProperty(value = "用户类型，1超级管理员，2老师，3学生", required = true)
    private int userType;

    private String orgName = "";//用户所在组织机构名称
    /**
     * 登录时间
     */
    private String loginTime;
    
    private List<String> authList = new ArrayList<String>();
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
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

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public List<String> getAuthList() {
        return authList;
    }

    public void setAuthList(List<String> authList) {
        this.authList = authList;
    }

    public boolean isAdmin() {
        return this.getUserType() == 1;
    }
}
