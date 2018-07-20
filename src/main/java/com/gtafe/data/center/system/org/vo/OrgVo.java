package com.gtafe.data.center.system.org.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gtafe.data.center.common.common.vo.RemoveEmptyChildVO;

@ApiModel
public class OrgVo extends RemoveEmptyChildVO implements  Comparable<OrgVo>{

    @ApiModelProperty(value = "主鍵", required = true)
    private String id;
    @ApiModelProperty(value = " 名称", required = true)
    private String orgName;

    @ApiModelProperty(value = " 机构编号", required = true)
    private String orgNo;
    @ApiModelProperty(value = " 地址", required = true)
    private String orgAddress;
    @ApiModelProperty(value = "父机构id", required = true)
    private String parentId;

    @ApiModelProperty(value = "父机构名称")
    String parentName;

    @ApiModelProperty(value = "状态", required = true)
    private int state;

    @ApiModelProperty(value = "创建人id", required = true)
    private String creater;
    @ApiModelProperty(value = "创建时间", required = true)
    private Date createTime;
    @ApiModelProperty(value = "修改人id", required = true)
    private String updater;
    @ApiModelProperty(value = "修改时间", required = true)
    private Date updateTime;

    @ApiModelProperty(value = "机构类型", required = true)
    private int  orgType;

    @ApiModelProperty(value = "排序", required = true)
    private Integer sort;

    //1:学校,2:文件夹,3:组织
    @ApiModelProperty(value = "节点数据类型", required = true)
    int nodeType;

    @ApiModelProperty(value = "子节点列表", required = true)
    private List<OrgVo> children = new ArrayList();

    @ApiModelProperty(value = "管理者")
    String manager;

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public List<OrgVo> getChildren() {
        return children;
    }

    public void setChildren(List<OrgVo> children) {
        this.children = children;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgAddress() {
        return orgAddress;
    }

    public void setOrgAddress(String orgAddress) {
        this.orgAddress = orgAddress;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public int getOrgType() {
        return orgType;
    }

    public void setOrgType(int orgType) {
        this.orgType = orgType;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public int getNodeType() {
        return nodeType;
    }

    public void setNodeType(int nodeType) {
        this.nodeType = nodeType;
    }

    public String getOrgNo() {
        return orgNo;
    }

    public void setOrgNo(String orgNo) {
        this.orgNo = orgNo;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    @Override
    public int compareTo(OrgVo orgVo) {
        return this.getSort().compareTo(orgVo.sort);
    }

    @Override
    public boolean gtLeaf() {
        return this.getNodeType() == 3;
    }

    @Override
    public List<? extends RemoveEmptyChildVO> gtChildren() {
        return this.getChildren();
    }

    @Override
    public String toString() {
        return "OrgVo{" +
                "id=" + id +
                ", orgName='" + orgName + '\'' +
                ", orgNo='" + orgNo + '\'' +
                ", orgAddress='" + orgAddress + '\'' +
                ", parentId=" + parentId +
                ", parentName='" + parentName + '\'' +
                ", state=" + state +
                ", creater=" + creater +
                ", createTime=" + createTime +
                ", updater=" + updater +
                ", updateTime=" + updateTime +
                ", orgType=" + orgType +
                ", sort=" + sort +
                ", nodeType=" + nodeType +
                ", children=" + children +
                ", manager='" + manager + '\'' +
                '}';
    }
}
