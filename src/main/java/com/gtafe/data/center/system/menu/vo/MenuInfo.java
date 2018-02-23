package com.gtafe.data.center.system.menu.vo;

import java.util.ArrayList;
import java.util.List;

import com.gtafe.data.center.common.common.util.empty.EmptyUtil;
import com.gtafe.data.center.common.common.vo.RemoveEmptyChildVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class MenuInfo extends RemoveEmptyChildVO{
    @ApiModelProperty(value = "主鍵", required = true)
    private int menuId;
    @ApiModelProperty(value = "菜单名称", required = true)
    private String menuName;
    @ApiModelProperty(value = "父菜单id", required = true)
    private int parentId;
    @ApiModelProperty(value = "访问路径", required = true)
    private String url;
    @ApiModelProperty(value = "权限码", required = true)
    private String authCode;
    @ApiModelProperty(value = "子节点列表", required = true)
    private List<MenuInfo> children = new ArrayList<MenuInfo>();

    @ApiModelProperty(value = "节点数据类型", required = true)
    int nodeType;
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
    public int getParentId() {
        return parentId;
    }
    public void setParentId(int parentId) {
        this.parentId = parentId;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getAuthCode() {
        return authCode;
    }
    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }
    public List<MenuInfo> getChildren() {
        return children;
    }
    public void setChildren(List<MenuInfo> children) {
        this.children = children;
    }
    @Override
    public boolean gtLeaf() {
        return EmptyUtil.isNotEmpty(url) && !url.equals("#");
    }
    @Override
    public List<? extends RemoveEmptyChildVO> gtChildren() {
        return this.getChildren();
    }

    public int getNodeType() {
        return nodeType;
    }

    public void setNodeType(int nodeType) {
        this.nodeType = nodeType;
    }
}
