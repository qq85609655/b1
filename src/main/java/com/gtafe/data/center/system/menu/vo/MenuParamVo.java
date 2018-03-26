package com.gtafe.data.center.system.menu.vo;

import com.gtafe.data.center.common.common.vo.PageParam;

public class MenuParamVo extends PageParam {
    private String menuName;

    private int state;

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
