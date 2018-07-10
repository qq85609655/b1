package com.gtafe.framework.base.filter;

import com.alibaba.fastjson.JSONObject;
import com.gtafe.data.center.common.login.vo.UserLoginInfo;
import com.gtafe.data.center.system.user.mapper.SysUserAuthMapper;
import com.gtafe.data.center.system.user.service.SysUserService;
import com.gtafe.data.center.system.user.vo.ResultVO;
import com.gtafe.data.center.system.user.vo.SysUserVo;
import com.gtafe.framework.base.exception.CasLoginException;
import com.gtafe.framework.base.interceptor.LoggerInfo;
import com.gtafe.framework.base.utils.CASInterfaceUtil;
import com.gtafe.framework.base.utils.CookieUitl;
import com.gtafe.framework.base.utils.HttpClientUtil;
import com.gtafe.framework.base.utils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.texen.util.PropertiesUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

@Component
public class NewLoginFilter implements Filter {


    @Autowired
    private SysUserService sysUserServiceImpl;
    @Autowired
    private SysUserAuthMapper sysUserAuthMapper;


    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        String redirectUrl = servletRequest.getRequestURL().toString();
        HttpServletResponse servletResponse = (HttpServletResponse) response;

        LoggerInfo li = new LoggerInfo(servletRequest);
        if (li.getUrlPath().startsWith("/api/common/reg") || li.getUrlPath().startsWith("/api/forgetPwd/sendMail") || li.getUrlPath().startsWith("/api/forgetPwd/editPwd")) {
            chain.doFilter(servletRequest, servletResponse);
            return;
        }

        if (li.getUrlPath().startsWith("/api/common/resources") || li.getUrlPath().startsWith("/api/dd")) {
            chain.doFilter(servletRequest, servletResponse);
            return;
        }


        String ticket = CASInterfaceUtil.getTicket(servletRequest);
        if (ticket != null) {
            ticket = ticket.trim();
            if (!ticket.equals("")) {
                CookieUitl.addCookie("ticket", ticket, servletRequest, servletResponse);
            }
        }
        String  casServer=PropertyUtils.getProperty("java-cas-client.properties", "casServer");
        String  localApPath=PropertyUtils.getProperty("java-cas-client.properties", "localApPath");
        String  casLoginPath=PropertyUtils.getProperty("java-cas-client.properties", "casLoginPath");
        String casLoginOutPath = PropertyUtils.getProperty("java-cas-client.properties", "casLoginOutPath");

        HttpSession session = servletRequest.getSession();
        UserLoginInfo se = (UserLoginInfo) session.getAttribute("accountinfo");
        String ticketsession = (String) session.getAttribute(ticket);
        if (ticket != null || ticketsession != null) {
            String result = CASInterfaceUtil.callCASInterface(servletRequest, casServer, "post");
            if (result != null) {
                JSONObject jsonObject = JSONObject.parseObject(result);
                if (jsonObject != null) {
                    String userName = jsonObject.getString("UserName");
                    if (userName != null) {
                        SysUserVo userVo = this.sysUserServiceImpl.getUserVoByUserNo(userName);
                        if (userVo == null) {
                            System.out.println("用户不存在我们系统啊！");
                            String url = casLoginPath+"?ReturnUrl="+localApPath;
                            throw new CasLoginException(url);
                        }
                        UserLoginInfo loginInfo = new UserLoginInfo();
                        BeanUtils.copyProperties(userVo, loginInfo);
                        List<String> authList = this.sysUserAuthMapper.getUserAllAuthcodes(userVo.getUserId());
                        if (loginInfo.isAdmin() || (authList != null && !authList.isEmpty())) {
                            authList.remove("");
                            loginInfo.getAuthList().clear();
                            loginInfo.getAuthList().addAll(authList);
                        }
                        if (se == null) {
                            session.setAttribute("accountinfo", loginInfo);
                        }
                        chain.doFilter(servletRequest, servletResponse);
                        return;
                    }
                } else {
                    CASInterfaceUtil.callCASInterface(servletRequest, casLoginOutPath, "post");
                    CookieUitl.delCookie(ApplicationConst.TICKET, servletRequest, servletResponse);
                    if (session != null) {
                        session.invalidate();
                    }
                    String url = casLoginPath+"?ReturnUrl="+localApPath;
                    servletResponse.sendRedirect(url);
                }
            } else {
                CASInterfaceUtil.callCASInterface(servletRequest, casLoginOutPath, "post");
                CookieUitl.delCookie(ApplicationConst.TICKET, servletRequest, servletResponse);
                if (session != null) {
                    session.invalidate();
                }
                String url = casLoginPath+"?ReturnUrl="+localApPath;
                servletResponse.sendRedirect(url);
            }
        } else {
            CASInterfaceUtil.callCASInterface(servletRequest, casLoginOutPath, "post");
            CookieUitl.delCookie(ApplicationConst.TICKET, servletRequest, servletResponse);
            if (session != null) {
                session.invalidate();
            }
            String url = casLoginPath+"?ReturnUrl="+localApPath;
            servletResponse.sendRedirect(url);
        }
    }
    @Override
    public void init(FilterConfig arg0) throws ServletException {

    }
    @Override
    public void destroy() {

    }
}