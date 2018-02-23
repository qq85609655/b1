package com.gtafe.data.center.common.login.service;

import com.gtafe.data.center.system.user.vo.ResultVO;

public interface LoginService {
    
    /**
     * 登录
     * @author 汪逢建
     * @date 2017年11月1日
     */
    public ResultVO login(String userNo, String loginPwd);
}
