package com.gtafe.framework.base.filter;

import com.alibaba.fastjson.JSONObject;
import com.gtafe.data.center.common.login.vo.UserLoginInfo;
import com.gtafe.data.center.system.user.mapper.SysUserAuthMapper;
import com.gtafe.data.center.system.user.service.SysUserService;
import com.gtafe.data.center.system.user.vo.SysUserVo;
import com.gtafe.framework.base.utils.CASInterfaceUtil;
import com.gtafe.framework.base.utils.CookieUitl;
import com.gtafe.framework.base.utils.HttpClientUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

@Component
public class NewLoginFilter implements Filter {

    private boolean isOutNet = false;
    private String EXCLUDEURL = "/index;/logout";


    @Autowired
    private SysUserService sysUserServiceImpl;
    @Autowired
    private SysUserAuthMapper sysUserAuthMapper;


    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        try {
            HttpServletRequest servletRequest = (HttpServletRequest) request;
            HttpServletResponse servletResponse = (HttpServletResponse) response;
            HttpSession session = servletRequest.getSession();

            System.out.println("*********************************************************");
            String currentUrl = servletRequest.getRequestURL().toString();
            String[] excluUrls = this.EXCLUDEURL.split(";");

            for (String url : excluUrls) {
                if (currentUrl.contains(url)) {
                    chain.doFilter(servletRequest, servletResponse);
                    return;
                }
            }
            String ticket = request.getParameter("ticket");
            if (ticket != null) {
                ticket = ticket.trim();
                if (!ticket.equals("")) {
                    CookieUitl.addCookie("ticket", ticket, servletRequest, servletResponse);
                }
            }

            String userSession = (String) session.getAttribute(ApplicationConst.USERLOGINCASSESSIONKEY);

            if (userSession != null) {
                // 每次判断用户是否已经退出
                String result = CASInterfaceUtil.callCASInterface(servletRequest,
                        "http://10.1.135.191:7072/Auth/GetLoginUser?ticket=", "post");
                JSONObject json = JSONObject.parseObject(result);
                if (json != null && !"".equals(json.toString().trim())) {
                    chain.doFilter(servletRequest, servletResponse);
                    return;
                } else {
                    // 这里userSession不为空，主要是两个客户端同时登陆的时候，另外一个系统退出了，此时本系统Session并没有退出,判断Ticket参数是否仍然存在，如果存在则去掉ticket参数
                    String redirectUrl = servletRequest.getRequestURL().toString();
                    String parameters = servletRequest.getQueryString();
                    if (parameters != null) {
                        parameters = parameters.trim();
                        //更新Url上的ticket
                        String[] params = parameters.split("\\&");
                        StringBuffer s = new StringBuffer();
                        for (int i = 0; i < params.length; i++) {
                            if (!params[i].contains("ticket=") && StringUtils.isNotBlank(params[i])) {
                                s.append("&").append(params[i]);
                            }
                        }
                        if (!parameters.equals("")) {
                            redirectUrl += "?" + s;
                            redirectUrl = URLEncoder.encode(redirectUrl, "UTF-8");
                        }
                    }
                    String resictRUL = "http://localhost:8080" + servletRequest.getContextPath() + "/logout?ReturnUrl=" + redirectUrl;
                    servletResponse.sendRedirect(resictRUL);
                }
            } else {
                String result = "";
                result = CASInterfaceUtil.callCASInterface(servletRequest,
                        "http://10.1.135.191:7072/Auth/GetLoginUser?ticket=", "post");
                String redirectUrl = servletRequest.getRequestURL().toString();
                String parameters = servletRequest.getQueryString();
                if (parameters != null) {
                    parameters = parameters.trim();
                    //encoding parameters
                    if (!parameters.equals("")) {
                        redirectUrl += "?" + parameters;
                    }
                }

                UserLoginInfo se = (UserLoginInfo) session.getAttribute("accountinfo");
                if (se == null) {
                    // 获取登录信息
                    SysUserVo userVo = this.sysUserServiceImpl.getUserVoByUserNo("admin");
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
                    System.out.println("1111111111111111111111111111111" + loginInfo.getUserId());
                }


                // 已经登录
                if (result != null && !"".equals(result) && !"null".equals(result)) {
                    // 配置集成环境统一Session标识
                    JSONObject json = JSONObject.parseObject(result);
                    /*** V1.1获取UserName方法 ***/
                    String userName = json.getString("UserName");
                    String echoName = json.getString("Name");
                    // 单点登录成功,储存单点登录标志

                    session.setAttribute(ApplicationConst.USERLOGINCASSESSIONKEY, userName);
                    session.setAttribute(ApplicationConst.ECHONAMELOGINCASSESSIONKEY, echoName);
                    // 登录本地服务器
                    chain.doFilter(servletRequest, servletResponse);
                    return;
                } /*else {
                    *//*
                     * 其他地方逻辑同理
                     *//*
                    //外网配置key=内网配置key+_Outside
                    if (isOutNet) {
                        servletResponse.sendRedirect(HttpClientUtil.CallNetAPI("http://10.1.135.191:7074/api/E_Config/GetValue?key=LoginUrl2_Outside", "get").replaceAll("\"", "") + "?ReturnUrl=" + redirectUrl);
                    } else {
                        // 登录本地服务器
                        servletResponse.sendRedirect(HttpClientUtil.CallNetAPI("http://10.1.135.191:7074/api/E_Config/GetValue?key=LoginUrl2", "get").replaceAll("\"", "") + "?ReturnUrl=" + redirectUrl);
                    }
                }*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void init(FilterConfig arg0) throws ServletException {

    }

    @Override
    public void destroy() {

    }

}
