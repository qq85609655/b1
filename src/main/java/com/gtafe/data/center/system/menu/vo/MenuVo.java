package com.gtafe.data.center.system.menu.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

@ApiModel
public class MenuVo {

    @ApiModelProperty(value = "主鍵", required = true)
    private int menuId;
    @ApiModelProperty(value = "菜单名称", required = true)
    private String menuName;
    @ApiModelProperty(value = "父菜单id", required = true)
    private int parentId;
    @ApiModelProperty(value = "访问路径", required = true)
    private String url;
    @ApiModelProperty(value = "描述", required = true)
    private String remark;
    @ApiModelProperty(value = "权限码", required = true)
    private String authCode;
    @ApiModelProperty(value = "状态", required = true)
    private String state;
    @ApiModelProperty(value = "创建人id", required = true)
    private String creator;
    @ApiModelProperty(value = "创建时间", required = true)
    private String createTime;
    @ApiModelProperty(value = "修改人id", required = true)
    private String updater;
    @ApiModelProperty(value = "修改时间", required = true)
    private String updateTime;


    @ApiModelProperty(value = "子节点列表", required = true)
    private List<MenuVo> children = new ArrayList();

    public List<MenuVo> getChildren() {
        return children;
    }

    public void setChildren(List<MenuVo> children) {
        this.children = children;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }


    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "MenuVo{" +
                "menuId=" + menuId +
                ", menuName='" + menuName + '\'' +
                ", parentId=" + parentId +
                ", url='" + url + '\'' +
                ", remark='" + remark + '\'' +
                ", authCode='" + authCode + '\'' +
                ", state='" + state + '\'' +
                ", creator='" + creator + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updater='" + updater + '\'' +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }
}
