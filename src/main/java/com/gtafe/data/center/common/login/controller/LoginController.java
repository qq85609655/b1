package com.gtafe.data.center.common.login.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.gtafe.framework.base.filter.ApplicationConst;
import com.gtafe.framework.base.utils.CASInterfaceUtil;
import com.gtafe.framework.base.utils.CookieUitl;
import com.gtafe.framework.base.utils.HttpClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gtafe.data.center.common.login.service.LoginService;
import com.gtafe.data.center.common.login.service.impl.LoginServiceImpl;
import com.gtafe.data.center.common.login.vo.UserLoginInfo;
import com.gtafe.data.center.system.config.service.SysConfigService;
import com.gtafe.data.center.system.config.vo.SysConfigVo;
import com.gtafe.data.center.system.user.vo.ResultVO;
import com.gtafe.framework.base.controller.BaseController;
import com.gtafe.framework.base.utils.PropertyUtils;

import io.swagger.annotations.Api;

@Controller
@RequestMapping(path = "/common")
@Api(value = "LoginController")
@CrossOrigin
public class LoginController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Resource
    private LoginService loginServiceImpl;

    @Resource
    private SysConfigService sysConfigServiceImpl;

    /**
     * login:登陆
     *
     * @param map 账号和用MD5加密过的密码
     * @return status 状态 1 代表成功 0代表失败
     */

    @RequestMapping("/login")
    public @ResponseBody
    ResultVO doLogin(@RequestBody Map map, HttpServletRequest request) {
        if (null == map.get("account") || null == map.get("password")) {
            return loginServiceImpl.login(null, null);
        } else {
            return loginServiceImpl.login(map.get("account").toString(), map.get("password").toString());
        }
    }

    @RequestMapping(value = "/loginout", method = RequestMethod.GET)
      public @ResponseBody
      ResultVO loginout(HttpServletRequest request, HttpServletResponse response) throws Exception {
          ResultVO result = new ResultVO();
          String login = PropertyUtils.getProperty("java-cas-client.properties", "casServerLoginUrl");
          if (login.indexOf("/login") != -1) {
              login = login.substring(0, login.indexOf("/login"));
          }
          login += "/logout";
          String serverName = PropertyUtils.getProperty("java-cas-client.properties", "serverName");
          serverName += request.getContextPath();
          serverName += "/?t=" + System.currentTimeMillis();// 放着浏览器首页缓存
          String location = login + "?service=" + URLEncoder.encode(serverName, "UTF-8");
          //response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
          //response.setHeader("Location", location);
          HttpSession session = request.getSession(false);
          if (session != null) {
              session.invalidate();
          }
          LOGGER.info(location);
          result.setCounts(1);
          result.setLocation(location);
          result.setUserInfo(null);
          return result;
      }
  /*
    @RequestMapping(value = "/loginout", method = RequestMethod.GET)
    public @ResponseBody
    ResultVO loginout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        ResultVO result = new ResultVO();
        try {
            String localApPath = PropertyUtils.getProperty("java-cas-client.properties", "localApPath");
            String casLoginPath = PropertyUtils.getProperty("java-cas-client.properties", "casLoginPath");
            String casLoginOutPath = PropertyUtils.getProperty("java-cas-client.properties", "casLoginOutPath");

            CASInterfaceUtil.callCASInterface(request, casLoginOutPath, "post");
            CookieUitl.delCookie(ApplicationConst.TICKET, request, response);
            if (session != null) {
                session.invalidate();
            }
            //  String location="http://10.1.135.191:7072/SignIn/Index?ReturnUrl=http://localhost:8888/GTADataCenter";

            String location = casLoginPath + "?ReturnUrl=" + localApPath;

            result.setCounts(1);
            result.setLocation(location);
            result.setUserInfo(null);

            //   response.sendRedirect(HttpClientUtil.CallNetAPI("http://10.1.135.191:7074/api/E_Config/GetValue?key=LoginUrl2", "get").replaceAll("\"", "") +"?ReturnUrl=http://localhost:7071"+request.getContextPath()+"/user/userinfo.do");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
 */

    /**
     * 用戶退出系統
     *
     * @param
     * @return
     */
    /*
     * @RequestMapping("/loginout")
     *
     * @ApiOperation(value = "退出", notes = "用户退出", httpMethod = "GET")
     * public @ResponseBody boolean loginout(HttpServletRequest request) {
     * HttpSession session = request.getSession(false); if (session != null) {
     * session.invalidate(); } return true; }
     */
    @RequestMapping("/null")
    public @ResponseBody
    UserLoginInfo checknull() {
        return null;
    }

    /**
     * 获取用户登录信息
     *
     * @author 汪逢建
     * @date 2017年11月9日
     * @Param noCheckLogin true不检查是否登录，null、false检查是否登录
     */
    @RequestMapping("/logininfo")
    public @ResponseBody
    UserLoginInfo logininfo(
            @RequestParam(value = "noCheckLogin", required = false) Boolean noCheckLogin) {
        if (noCheckLogin == null) {
            noCheckLogin = false;
        }
        if (noCheckLogin.booleanValue()) {
            return gtSessionUserInfo(getSession());
        } else {
            return this.getUserInfo();
        }
    }

    /**
     * 普通用户可以查询的只有系统信息
     *
     * @author 汪逢建
     * @date 2017年11月9日
     */
    @RequestMapping("/systemInfo")
    public @ResponseBody
    SysConfigVo systemInfo() {
        return sysConfigServiceImpl.getBasicSysConfigVO();
    }
}
