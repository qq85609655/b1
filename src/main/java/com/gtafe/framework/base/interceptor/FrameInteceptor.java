/*
 * 项目名称:web_base
 * 类名称:LoginInteceptor.java
 * 包名称:com.joyintech.webbase.interceptor
 *
 * 修改履历:
 *       日期                            修正者        主要内容
 *
 *
 */

package com.gtafe.framework.base.interceptor;


import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
//import com.gtafe.framework.base.exception.NoRegistException;
//import com.gtafe.framework.base.register.IniVerifyFlag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.gtafe.framework.base.exception.*;
import com.gtafe.framework.base.register.IniVerifyFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.gtafe.data.center.common.common.util.empty.EmptyUtil;
import com.gtafe.data.center.common.login.vo.UserLoginInfo;
import com.gtafe.framework.base.annotation.AuthAnnotation;
import com.gtafe.framework.base.controller.BaseController;

/**
 * 名称： FrameInteceptor  拦截器<br>
 * 描述：拦截每一次AJAX请求，以判断登录、权限等<br>
 * 包含 系统日志拦截器。非业务日志。 记录一切前后台交互操作。 每次请求记录两次，开始请求和结束请求。<br>
 * 如果发生异常，则仅存在开始请求。<br>
 * 每个请求包含请求url和请求参数。另包含附加信息（例如来源IP、浏览器信息等）<br>
 * 系统日志拦截器应在所有拦截器之前。<br>
 * 系统日志拦截器包含完成拦截器。其他拦截器无postHandle拦截，所以postHandle在最后。<br>
 * <p/>
 *
 * @author 张中伟
 * @version 1.0
 * @since 1.0.0
 */
public class FrameInteceptor extends HandlerInterceptorAdapter {

    /**
     * 日志信息
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(
            FrameInteceptor.class);

    /**
     * 登录 URL   是否写到配置文件或数据库？
     */
    private static final String LOGIN_URL = "/common/";

    private static Pattern pattern = Pattern.compile(FrameInteceptor.LOGIN_URL);
    private static String DEF_NOAUTH = "您没有当前功能的操作权限,请联系管理员！";

    /**
     * 仅判断用户是否登录。 <br>
     * 未登录则返回未登录提示<br>
     */

    @SuppressWarnings("unchecked")
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler)
            throws Exception {

        // 取得request 相关的东西 用户 请求 时间 参数
        LoggerInfo li = new LoggerInfo(request);

        //  LOGGER.info(li.toString());

        if (li.getUrlPath().startsWith("/common/reg") || li.getUrlPath().startsWith("/forgetPwd/sendMail") || li.getUrlPath().startsWith("/forgetPwd/editPwd")) {
            return true;
        }

     // if (!IniVerifyFlag.verifyFlag) {
     //      throw new NoRegistException();
      //  }

        // 判断是否登录地址，如果是登录，直接返回
        if (li.getUrlPath().startsWith("/common/")) {
            return true;
        }


        HttpSession session = request.getSession();

        UserLoginInfo vo = BaseController.gtSessionUserInfo(session);

        if (null == vo) {
            //未登录异常，由异常拦截器接收。
             throw new NoLoginException();
        }
      /*  Map<String, String> userOnlineMap = (Map<String, String>) request.getServletContext().getAttribute("userOnlineMap");
        String hasLoginTime = userOnlineMap.get(vo.getUserNo().toLowerCase());
        if (!vo.getLoginTime().equals(hasLoginTime)) {
            request.getSession().invalidate();
            throw new KickOutException();
        }*/

        this.handleUserAuth(request, response, handler, vo);
        // 已登录则不再拦截 用户ID不为空则代表已登录 需要取得session，并获取其中的变量。
        return true;
    }

    /*
     * (non-Javadoc)
     * @see
     * org.springframework.web.servlet.handler.HandlerInterceptorAdapter#postHandle(javax.servlet.
     * http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object,
     * org.springframework.web.servlet.ModelAndView)
     */
    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView)
            throws Exception {


        super.postHandle(request, response, handler, modelAndView);

        // 取得request 相关的东西 用户 请求 时间 参数
        LoggerInfo li = new LoggerInfo(request);

        LOGGER.info("完成请求----");
        LOGGER.info(li.toString());
    }

    /*
     * (non-Javadoc)
     * @see
     * org.springframework.web.servlet.handler.HandlerInterceptorAdapter#afterCompletion(javax.
     * servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object,
     * java.lang.Exception)
     */
    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler,
                                Exception ex)
            throws Exception {

        super.afterCompletion(request, response, handler, ex);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#
     * afterConcurrentHandlingStarted(javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse, java.lang.Object)
     */
    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request,
                                               HttpServletResponse response,
                                               Object handler)
            throws Exception {

        super.afterConcurrentHandlingStarted(request, response, handler);
    }

    /**
     * 过滤权限
     *
     * @return true表示有权限
     * @author 汪逢建
     * @date 2017年11月7日
     */
    private boolean handleUserAuth(HttpServletRequest request,
                                   HttpServletResponse response, Object handler,
                                   UserLoginInfo userInfo) {
        if (userInfo.isAdmin()) {
            return true;
        }
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handleMethod = (HandlerMethod) handler;
        Method method = handleMethod.getMethod();
        AuthAnnotation annotation = method.getAnnotation(AuthAnnotation.class);
        if (annotation == null) {
            return true;
        }

        String[] value = annotation.value();
        if (value == null || value.length == 0) {
            return true;
        }
        String[] conditions = annotation.conditions();
        String msg = EmptyUtil.isBlank(annotation.msg()) ? DEF_NOAUTH : annotation.msg();
        if (conditions == null || conditions.length == 0) {
            //无条件参数，仅一组权限码
            String authCode = value[0];
            if (this.containAuth(authCode, userInfo.getAuthList())) {

            } else {
                throw new NoAuthException(msg);
            }
        } else {
            //有条件参数，多组权限码
            if (value.length != conditions.length) {
                LOGGER.error("当前方法权限配置格式错误，请检查!" + method);
                throw new BaseException(BaseException.DEF_EXPINFO);
            }
            //查询匹配的条件索引
            int matchIndex = -1;
            for (int i = 0; i < conditions.length; i++) {
                String condition = conditions[i];
                String[] cs = condition.split(";");
                boolean match = true;
                for (String c : cs) {
                    int idx = c.indexOf("=");
                    if (idx == -1) {
                        LOGGER.error("当前方法权限配置条件[" + condition + "]错误，请检查!" + method);
                        throw new BaseException(BaseException.DEF_EXPINFO);
                    }
                    String key = c.substring(0, idx);
                    String keyvalue = c.substring(idx + 1);
                    String reqvalue = request.getParameter(key);
                    if (reqvalue == null || !reqvalue.equals(keyvalue)) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    matchIndex = i;
                    break;
                } else {
                    continue;
                }
            }
            if (matchIndex < 0) {
                LOGGER.error("当前请求权限条件均不匹配，请检查!" + method);
                throw new NoAuthException(msg);
            }
            if (this.containAuth(value[matchIndex], userInfo.getAuthList())) {

            } else {
                throw new NoAuthException(msg);
            }
        }
        return true;
    }

    /**
     * 是否有权限
     *
     * @author 汪逢建
     * @date 2017年11月8日
     */
    private boolean containAuth(String authCode, List<String> authList) {
        if (EmptyUtil.isEmpty(authCode) || EmptyUtil.isEmpty(authList)) {
            return false;
        }
        String[] authCodes = authCode.split(",");
        for (String auths : authCodes) {
            String[] as = auths.split(";");
            boolean contain = true;
            for (String auth : as) {
                if (!authList.contains(auth)) {
                    contain = false;
                    break;
                }
            }
            if (contain) {
                return true;
            } else {
                continue;
            }
        }
        return false;
    }

    public String getParameterName(MethodParameter p) {
        if (p.getParameterName() != null) {
            return p.getParameterName();
        }
        RequestParam requestParam = p.getParameterAnnotation(RequestParam.class);
        return requestParam == null ? null : requestParam.value();
    }
}
