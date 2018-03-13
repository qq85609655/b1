package com.gtafe.data.center.runadmin.nodewatch.vo;

import java.sql.Timestamp;

/**
 * 运行任务状态临时表对象
 */
public class EtlTaskStatus {

	private int taskId;

	private String taskName;

	private String sourceTableName;

	private String tagertTableName;

	public String getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}

	private String errorInfo;
	// 源库 状态

	private int sourceStatus;

	private String sourceStatusName;

	// 目标库状态
	private int targetStatus;

	private String targetStatusName;
	
	private Timestamp createTime;

	private int orgId;

	private int busType;
	private int sourceConnectionId;
	private int targetConnectionId;

	private String orgName;



	public int getSourceConnectionId() {
		return sourceConnectionId;
	}

	public void setSourceConnectionId(int sourceConnectionId) {
		this.sourceConnectionId = sourceConnectionId;
	}

	public int getTargetConnectionId() {
		return targetConnectionId;
	}

	public void setTargetConnectionId(int targetConnectionId) {
		this.targetConnectionId = targetConnectionId;
	}


	public int getSourceStatus() {
		return sourceStatus;
	}

	public void setSourceStatus(int sourceStatus) {
		this.sourceStatus = sourceStatus;
	}

	public String getSourceStatusName() {
		return sourceStatusName;
	}

	public void setSourceStatusName(String sourceStatusName) {
		this.sourceStatusName = sourceStatusName;
	}

	public int getTargetStatus() {
		return targetStatus;
	}

	public void setTargetStatus(int targetStatus) {
		this.targetStatus = targetStatus;
	}

	public String getTargetStatusName() {
		return targetStatusName;
	}

	public void setTargetStatusName(String targetStatusName) {
		this.targetStatusName = targetStatusName;
	}

	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getSourceTableName() {
		return sourceTableName;
	}

	public void setSourceTableName(String sourceTableName) {
		this.sourceTableName = sourceTableName;
	}

	public String getTagertTableName() {
		return tagertTableName;
	}

	public void setTagertTableName(String tagertTableName) {
		this.tagertTableName = tagertTableName;
	}

	public int getOrgId() {
		return orgId;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public int getBusType() {
		return busType;
	}

	public void setBusType(int busType) {
		this.busType = busType;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "EtlTaskStatus [taskId=" + taskId + ", taskName=" + taskName + ", sourceTableName=" + sourceTableName
				+ ", tagertTableName=" + tagertTableName + ", sourceStatus=" + sourceStatus + ", sourceStatusName="
				+ sourceStatusName + ", targetStatus=" + targetStatus + ", targetStatusName=" + targetStatusName
				+ ", createTime=" + createTime + ", orgId=" + orgId + ", busType=" + busType + ", sourceConnectionId="
				+ sourceConnectionId + ", targetConnectionId=" + targetConnectionId + "]";
	}


	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
}
