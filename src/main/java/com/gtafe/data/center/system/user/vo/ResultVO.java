
package com.gtafe.data.center.system.user.vo;

import com.gtafe.data.center.common.login.vo.UserLoginInfo;

public class ResultVO {

    /**
     * 状态  1代表成功 0代表失败, -1账号不存在,-2密码错误,-3账号未启用
     */
    private int counts;
    
    private String location;
    
    private UserLoginInfo userInfo;

    public int getCounts() {
        return counts;
    }

    public void setCounts(int counts) {
        this.counts = counts;
    }

    public UserLoginInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserLoginInfo userInfo) {
        this.userInfo = userInfo;
    }

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}


}
