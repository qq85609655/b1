package com.gtafe.data.center.system.user.vo;

import com.gtafe.data.center.common.common.vo.PageParam;

public class SysUserParam extends PageParam{
    private int state;
    private String orgIds;
    private String keyWord;
    public int getState() {
        return state;
    }
    public void setState(int state) {
        this.state = state;
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
