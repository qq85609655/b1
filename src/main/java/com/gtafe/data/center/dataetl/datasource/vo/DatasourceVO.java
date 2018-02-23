/**
 * Project Name:gtacore
 * File Name:	ISourceVO.java
 * Description:
 * Date: 2017-08-14 17:50:40
 * Author: Xiang Zhiling
 * History:
 * Copyright (c) 2017, GTA All Rights Reserved.
 */

package com.gtafe.data.center.dataetl.datasource.vo;

import com.gtafe.framework.base.vo.BaseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 添加了
 * 是否中心库 1 是 2 否
 * 数据标准类型  1 国家 2 学校
 */
@ApiModel
public class DatasourceVO extends BaseVO {

    /**
     * serialVersionUID:TODO
     *
     * @since JDK 1.7
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id", required = true)
    private int id;

    @ApiModelProperty(value = "数据连接的名称", required = true)
    private String name;

    @ApiModelProperty(value = "数据库类型", required = true)
    private int dbType;

    @ApiModelProperty(value = "数据库类型的名称", required = false)
    private String dbTypeName;


    @ApiModelProperty(value = "数据库的名称", required = true)
    private String dbName;

    @ApiModelProperty(value = "主机（名称、IP、域名）", required = true)
    private String host;

    @ApiModelProperty(value = "端口号", required = true)
    private int port;

    @ApiModelProperty(value = "用户名", required = true)
    private String username;

    @ApiModelProperty(value = "密码", required = true)
    private String password;

    @ApiModelProperty(value = "备注", required = false)
    private String remark;

    @ApiModelProperty(value = "oracle数据库数据表空间", required = false)
    private String tableSpaces;

    @ApiModelProperty(value = "oracle数据库的索引空间", required = false)
    private String indexSpaces;


    @ApiModelProperty(value = "是否为中心库", required = true)
    private String isCenter;

    @ApiModelProperty(value = "数据标准类型", required = true)
    private String dataSource;

    @ApiModelProperty(value = "所属机构", required = true)
    private int orgId;

    @ApiModelProperty(value = "所属机构名称", required = true)
    private String orgName;


    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public String getIsCenter() {
        return isCenter;
    }

    public void setIsCenter(String isCenter) {
        this.isCenter = isCenter;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getDbType() {
        return dbType;
    }

    public void setDbType(int dbType) {
        this.dbType = dbType;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTableSpaces() {
        return tableSpaces;
    }

    public void setTableSpaces(String tableSpaces) {
        this.tableSpaces = tableSpaces;
    }

    public String getIndexSpaces() {
        return indexSpaces;
    }

    public void setIndexSpaces(String indexSpaces) {
        this.indexSpaces = indexSpaces;
    }

    public String getDbTypeName() {
        return dbTypeName;
    }

    public void setDbTypeName(String dbTypeName) {
        this.dbTypeName = dbTypeName;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "DatasourceVO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dbType=" + dbType +
                ", dbTypeName='" + dbTypeName + '\'' +
                ", dbName='" + dbName + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", remark='" + remark + '\'' +
                ", tableSpaces='" + tableSpaces + '\'' +
                ", indexSpaces='" + indexSpaces + '\'' +
                ", isCenter='" + isCenter + '\'' +
                ", dataSource='" + dataSource + '\'' +
                ", orgId=" + orgId +
                '}';
    }
}
