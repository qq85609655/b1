package com.gtafe.data.center.common.login.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.gtafe.data.center.common.common.constant.LogConstant;
import com.gtafe.data.center.common.common.util.empty.EmptyUtil;
import com.gtafe.data.center.common.login.service.LoginService;
import com.gtafe.data.center.common.login.vo.UserLoginInfo;
import com.gtafe.data.center.system.log.service.LogService;
import com.gtafe.data.center.system.log.vo.LogInfo;
import com.gtafe.data.center.system.org.service.IOrgService;
import com.gtafe.data.center.system.org.vo.OrgVo;
import com.gtafe.data.center.system.user.mapper.SysUserAuthMapper;
import com.gtafe.data.center.system.user.service.SysUserService;
import com.gtafe.data.center.system.user.vo.ResultVO;
import com.gtafe.data.center.system.user.vo.SysUserVo;
import com.gtafe.framework.base.controller.BaseController;

@Service
public class LoginServiceImpl extends BaseController implements LoginService{
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginServiceImpl.class);
    @Resource
    private SysUserService sysUserServiceImpl;
    @Resource
    private LogService logServiceImpl;
    
    @Resource
    private IOrgService orgServiceImpl;
    @Resource
    private SysUserAuthMapper sysUserAuthMapper;
    
    @SuppressWarnings("unchecked")
    @Override
    public ResultVO login(String userNo, String loginPwd) {
        ResultVO result = new ResultVO();
        if (EmptyUtil.isEmpty(userNo) || EmptyUtil.isEmpty(loginPwd)) {
            result.setCounts(0);
            return result;
        } 
        loginPwd = loginPwd.toLowerCase();
        SysUserVo userVo = this.sysUserServiceImpl.getUserVoByUserNo(userNo);
        if(userVo == null) {
            result.setCounts(-1);
            return result;//账号不存在
        }
        if(!userVo.getLoginPwd().equalsIgnoreCase(loginPwd)) {
            result.setCounts(-2);//密码可能不对
            return result;
        }
        if(!userVo.isState()) {
            result.setCounts(-3);//账号被禁用
            return result; 
        }
        
        UserLoginInfo loginInfo = new UserLoginInfo();
        BeanUtils.copyProperties(userVo, loginInfo);
        List<String> authList = this.sysUserAuthMapper.getUserAllAuthcodes(userVo.getUserId());
        if(loginInfo.isAdmin() || (authList!=null && ! authList.isEmpty())) {
            authList.remove("");
            loginInfo.getAuthList().clear();
            loginInfo.getAuthList().addAll(authList);
        }else {
            result.setCounts(-4);//账号没有任务权限，需要提示 联系管理员 授予权限才能登陆系统
            return result;
        }
        
        OrgVo orgVo = this.orgServiceImpl.getEntityById(userVo.getOrgId());
        if(orgVo == null) {
            LOGGER.error("用户["+userNo+"]的组织信息不存在，请检查!");
            result.setCounts(0);//未知错误
            return result; 
        }
        result.setCounts(1);
        this.getSession().setAttribute("accountinfo", loginInfo);
        Map<String, String> userOnlineMap = (Map<String,String>)getRequest().getServletContext().getAttribute("userOnlineMap");
        loginInfo.setLoginTime(new Date().getTime()+"");
        userOnlineMap.put(loginInfo.getUserNo().toLowerCase(), loginInfo.getLoginTime());
        result.setUserInfo(getUserInfo());
        loginInfo.setOrgName(orgVo.getOrgName());
        LogInfo logInfo = new LogInfo();
        logInfo.setModuleId(LogConstant.Module_Login);
        logInfo.setOperType("登录系统");
        logInfo.setOperContent("成功");
        this.logServiceImpl.saveLog(logInfo);
        return result;
    }
    
}
