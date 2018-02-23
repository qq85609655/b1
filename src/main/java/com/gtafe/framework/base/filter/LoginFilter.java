package com.gtafe.framework.base.filter;


import com.gtafe.data.center.common.login.vo.UserLoginInfo;
import com.gtafe.data.center.system.user.mapper.SysUserAuthMapper;
import com.gtafe.data.center.system.user.service.SysUserService;
import com.gtafe.data.center.system.user.vo.SysUserVo;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/***
 *
 * ClassName: 用户登录过滤器 <br/>
 */
@Component
public class LoginFilter implements Filter {
    private static final Logger logger = LogManager.getLogger(LoginFilter.class);

    @Autowired
    private SysUserService sysUserServiceImpl;
    @Autowired
    private SysUserAuthMapper sysUserAuthMapper;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
         
    }

    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest requests = (HttpServletRequest) request;
        HttpServletResponse responses = (HttpServletResponse) response;
        String userno = ((HttpServletRequest) request).getRemoteUser();
        System.out.println(userno);
        HttpSession session = ((HttpServletRequest) request).getSession();
        UserLoginInfo se = (UserLoginInfo) session.getAttribute("accountinfo");
        if (se == null) {
            // 获取登录信息
            SysUserVo userVo = this.sysUserServiceImpl.getUserVoByUserNo(userno);
            if (userVo == null) {
                System.out.println("用户不存在我们系统啊！");
                return;
            }
            UserLoginInfo loginInfo = new UserLoginInfo();
            BeanUtils.copyProperties(userVo, loginInfo);
            List<String> authList = this.sysUserAuthMapper.getUserAllAuthcodes(userVo.getUserId());
            if (loginInfo.isAdmin() || (authList != null && !authList.isEmpty())) {
                authList.remove("");
                loginInfo.getAuthList().clear();
                loginInfo.getAuthList().addAll(authList);
            }
            session.setAttribute("accountinfo", loginInfo);
        }
        chain.doFilter(request, response);
        return;
    }

   /* public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest requests = (HttpServletRequest) request;
        HttpServletResponse responses = (HttpServletResponse) response;
        // 获取当前访问路径
        String url = requests.getRequestURI();
        // 获取登录信息
        SysUserVo sysUserVo = (SysUserVo) requests.getSession()
                .getAttribute("accountinfo");
        // 如果为空，返回
        if (sysUserVo == null) {
            // 判断获取的路径不为空且不是访问登录页面或执行登录操作时跳转
            if (url != null && !url.equals("")
                    && (url.indexOf("Login") < 0 && url.indexOf("login") < 0)) {
                responses
                        .sendRedirect(requests.getContextPath() + "/login.jsp");
                return;
            }
        }
        chain.doFilter(request, response);
        return;
    }*/

    @Override
    public void destroy() { 
    }

}
