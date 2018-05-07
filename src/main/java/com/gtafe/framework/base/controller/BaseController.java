package com.gtafe.framework.base.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.gtafe.data.center.common.login.vo.UserLoginInfo;
import com.gtafe.data.center.system.user.vo.SysUserVo;
import com.gtafe.framework.base.exception.NoLoginException;

public class BaseController {
    
    protected HttpServletRequest getRequest() {
        return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
    }
    
    protected HttpSession getSession() {
        return getRequest().getSession();
    }
    
    protected UserLoginInfo getUserInfo(){
        UserLoginInfo userInfo = gtSessionUserInfo(getRequest().getSession());
        if(userInfo == null) {
             throw new NoLoginException();
        }
        return userInfo;
        
    }
    
    protected int getUserId() {
        return getUserInfo().getUserId();
    }
    
    /**
     * 是否为超级管理员
     * @author 汪逢建
     * @date 2017年11月1日
     */
    protected boolean isAdmin() {
        return getUserInfo().isAdmin();
    }
    
    /**
     * 根据session获取登录信息，不检查登录
     * @author 汪逢建
     * @date 2017年12月1日
     */
    public static UserLoginInfo gtSessionUserInfo(HttpSession session) {
        UserLoginInfo userVo = (UserLoginInfo) session.getAttribute("accountinfo");
        return userVo;
    }
    /**
     * 查询的用户检查是否为超级管理员
     * @author 汪逢建
     * @date 2017年12月1日
     */
    public boolean isAdmin(SysUserVo user) {
        return user!=null && user.getUserType() == 1;
    }
}
