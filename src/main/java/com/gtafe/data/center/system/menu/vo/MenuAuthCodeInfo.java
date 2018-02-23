package com.gtafe.data.center.system.menu.vo;

import java.util.List;

import com.gtafe.data.center.common.common.vo.RemoveEmptyChildVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class MenuAuthCodeInfo extends RemoveEmptyChildVO{
    @ApiModelProperty(value = "权限码id", required = true)
    private String authId;
    @ApiModelProperty(value = "权限码名称", required = true)
    private String authName;
    @ApiModelProperty(value = "父节点id", required = true)
    private String parentId;
    @ApiModelProperty(value = "是否为权限码", required = true)
    private boolean authFlag;
    @ApiModelProperty(value = "下级节点", required = true)
    private List<MenuAuthCodeInfo> children;
    public String getAuthId() {
        return authId;
    }
    public void setAuthId(String authId) {
        this.authId = authId;
    }
    public String getAuthName() {
        return authName;
    }
    public void setAuthName(String authName) {
        this.authName = authName;
    }
    public String getParentId() {
        return parentId;
    }
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    public boolean isAuthFlag() {
        return authFlag;
    }
    public void setAuthFlag(boolean authFlag) {
        this.authFlag = authFlag;
    }
    public List<MenuAuthCodeInfo> getChildren() {
        return children;
    }
    public void setChildren(List<MenuAuthCodeInfo> children) {
        this.children = children;
    }
    public boolean gtLeaf() {
        return this.isAuthFlag();
    }
    public List<? extends RemoveEmptyChildVO> gtChildren() {
        return this.getChildren();
    }
}
