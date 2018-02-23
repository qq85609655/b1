package com.gtafe.framework.base.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

@ApiModel
public class BaseVO implements Serializable {

	/**
	 * serialVersionUID: 序列
	 *
	 * @since 1.0.0
	 */

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "数据id，无业务含义，新增时无需提供", required = false)
	private int id;

	@ApiModelProperty(value = "创建者，新增、修改等数据更新类操作无需包含此数据，主要用于数据查询类的请求返回结果", required = false)
	private String creator="SuperAdmin";
	
	@ApiModelProperty(value = "创建时间，新增、修改等数据更新类操作无需包含此数据，主要用于数据查询类的请求返回结果", required = false)
	private Date createtime;
	
	@ApiModelProperty(value = "修改者，新增、修改等数据更新类操作无需包含此数据，主要用于数据查询类的请求返回结果", required = false)
	private String updater="SuperAdmin";
	
	@ApiModelProperty(value = "修改时间，新增、修改等数据更新类操作无需包含此数据，主要用于数据查询类的请求返回结果", required = false)
	private Date updatetime;
	
	
	

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public String getUpdater() {
		return updater;
	}
	public void setUpdater(String updater) {
		this.updater = updater;
	}
	public Date getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
	
	
}
