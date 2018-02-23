package com.gtafe.framework.base.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.gtafe.data.center.common.login.vo.UserLoginInfo;
import com.gtafe.data.center.system.log.vo.LogVo;
import com.gtafe.data.center.system.menu.service.MenuService;
import com.gtafe.data.center.system.menu.vo.MenuVo;
import com.gtafe.framework.base.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class LogUtil extends BaseController{


    @Resource
    private MenuService menuServiceImpl;
    private static final Logger LOGGER = LoggerFactory.getLogger(LogUtil.class);
    /**
     *
     * @param moduleId
     * @param operType
     * @param operContent
     * @param operRes
     * @return
     */
    public  LogVo getLoginLog(int moduleId, String operType, String operContent, int operRes) {
        UserLoginInfo userVo = getUserInfo();
        LogVo logVo = new LogVo();
        logVo.setOperIp(getIpAddr(getRequest()));
        logVo.setOperModuleId(moduleId);
        String moduleName="";
        if(moduleId==0){
            moduleName="用户登录";
        }else {
            moduleName="系统模块";
        }
        LOGGER.info(moduleName);
        logVo.setOperModuleName(moduleName);
        logVo.setOperContent(operContent);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        logVo.setOperTime(sdf.format(new Date()));
        logVo.setOperType(operType);
        logVo.setOperOrgId(userVo.getOrgId());
        logVo.setOperOrgName(userVo.getOrgName());
        logVo.setOperUserNo(userVo.getUserId()+"");
        logVo.setOperUserName(userVo.getRealName());
        logVo.setOperRes(operRes);
        return logVo;
    }


    /**
     * 获取登录用户IP地址
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.equals("0:0:0:0:0:0:0:1")) {
            ip = "本地";
        }
        return ip;
    }
}
