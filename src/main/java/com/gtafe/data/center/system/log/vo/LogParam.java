package com.gtafe.data.center.system.log.vo;


import com.gtafe.data.center.common.common.vo.PageParam;

public class LogParam extends PageParam{
    private String startTime;//开始时间
    private String endTime;//结束时间
    private int operRes;//日志类型，0全部，1正常，2异常
    private String operModuleIds;//菜单，模块id
    private String orgIds;//机构id
    private String keyWord;//关键字
    public String getStartTime() {
        return startTime;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public String getEndTime() {
        return endTime;
    }
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    public int getOperRes() {
        return operRes;
    }
    public void setOperRes(int operRes) {
        this.operRes = operRes;
    }
    public String getOperModuleIds() {
        return operModuleIds;
    }
    public void setOperModuleIds(String operModuleIds) {
        this.operModuleIds = operModuleIds;
    }
    public String getOrgIds() {
        return orgIds;
    }
    public void setOrgIds(String orgIds) {
        this.orgIds = orgIds;
    }
    public String getKeyWord() {
        return keyWord;
    }
    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }
}
