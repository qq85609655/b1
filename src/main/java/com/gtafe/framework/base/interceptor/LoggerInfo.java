package com.gtafe.framework.base.interceptor;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.util.UrlPathHelper;

import com.gtafe.data.center.common.login.vo.UserLoginInfo;



/**
 * 名称：LoggerInfo <br>
 * 描述：系统日志信息类<br>
 *
 * @author 张中伟
 * @version 1.0
 * @since 1.0.0
 */
public class LoggerInfo {

    // 应抽取为全局静态变量，以供所有要取得sessionuser的地方调用！
    // session中存放用户的变量名
    public static final String SYS_USER_SESSION_NAME = "accountinfo";

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 请求地址
     */
    private String urlPath;

    /**
     * 请求参数
     */
    private String params;

    /**
     * session ID
     */
    private String sessionId;

    /**
     * 请求 IP 地址
     */
    private String ipAddr;

    /**
     * 请求 ID
     */
    private String requestId;

    /**
     * 默认构造方法
     */
    public LoggerInfo() {

    }

    /**
     * 基于Request的构造方法
     *
     * @param request 请求
     */
    public LoggerInfo(HttpServletRequest request) {

        // 取得request 相关的东西 用户 请求 时间 参数
        // 构造方法中不能使用注入的变量
        HttpSession session = request.getSession(false);
        
        // 取得用户
        
        UserLoginInfo vo = null;
        if(session!=null) {
            vo = (UserLoginInfo)session.getAttribute(
            SYS_USER_SESSION_NAME);
        }

        String userId = vo == null ? "nullId" : vo.getUserId()
                                                         + "";

        String userName = vo == null ? "未登录用户" : vo.getUserNo();

        // 取得请求地址
        UrlPathHelper urlPathHelper = new UrlPathHelper();
        String urlPath = urlPathHelper.getLookupPathForRequest(request);

        

        // 取得远程地址
        this.ipAddr = findIpAddr(request);

        // 取得参数串 /
        // 仅限于请求 content-type 是默认情况。 如果使用application-json 就需要取

        // json的处理方式目前还有问题，已注释
        if (request.getContentType() != null
            && request.getContentType().contains("application/json")) {

            // 由于取流有问题，暂时注释
            // this.params = getRequestPostStr(request);

        } else {
            Map<String, String[]> paramsMap = request.getParameterMap();
            String params = JsonConvertor.jsonEncode(paramsMap);
            this.params = params;
        }

        this.userId = userId;
        this.userName = userName;
        this.urlPath = urlPath;
        
        if(session == null) {
            return;
        }
        // 取得请求ID
        if (null != session.getAttribute("REQUEST_ID")) {
            this.requestId = (String)session.getAttribute("REQUEST_ID");
        }
        this.sessionId = session.getId();
    }

    /**
     * 主要功能: 获取当前请求的IP地址。    <br>
     * 注意事项:无  <br>
     *
     * @param request 请求对象
     * @return IP地址
     */
    public String findIpAddr(HttpServletRequest request) {
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0
            || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0
            || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0
            || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (ipAddress.equals("127.0.0.1")
                || ipAddress.equals("0:0:0:0:0:0:0:1")) {
                // 根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ipAddress = inet.getHostAddress();
            }
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        // if(ipAddress!=null && ipAddress.length()>15){ //"***.***.***.***".length() = 15
        // if(ipAddress.indexOf(",")>0){
        // ipAddress = ipAddress.substring(0,ipAddress.indexOf(","));
        // }
        // }
        return ipAddress;
    }

    /**
     * 主要功能: 取得前台 post 实体   <br>
     * 注意事项: 要处理异常 <br>
     *
     * @param request HttpServletRequest
     * @return byte[] 解析流为byte数组
     * @throws IOException 会抛出读取流异常
     */
    public byte[] getRequestPostBytes(HttpServletRequest request)
        throws IOException {
        int contentLength = request.getContentLength();
        if (contentLength < 0) {
            return null;
        }
        byte buffer[] = new byte[contentLength];

        // ServletInputStream sis=new ServletInputStream(request.getInputStream());

        // TODO:这个读取inputstream ，读过就没有了,后边要取参数就会报错。
        // 。。。。暂时注释，待复制 这是java的问题，所以暂时没办法
        /*
         * for (int i = 0; i<contentLength;) { int readlen = request.getInputStream().read(buffer,
         * i, contentLength-i); if (readlen==-1) { break; } i += readlen; }
         */
        return buffer;
    }

    /**
     * 主要功能: 取得前台传参     <br>
     * 注意事项:无  <br>
     *
     * @param request HttpServletReques
     * @return 转换的参数字符串（类js对象数组字符串化）
     */
    public String getRequestPostStr(HttpServletRequest request) {

        byte[] buffer = null;
        try {
            buffer = getRequestPostBytes(request);
        } catch (IOException e) {
            // 日志转换暂不处理异常
            e.printStackTrace();
        }

        String charEncoding = request.getCharacterEncoding();
        if (charEncoding == null) {
            charEncoding = "UTF-8";
        }

        try {
            String result = buffer.toString();
            result = new String(buffer, charEncoding);
            return result;
        } catch (UnsupportedEncodingException e) {
            // 不处理异常
            e.printStackTrace();
        } catch (RuntimeException re) {
            re.printStackTrace();
        }

        return "";

    }

    /**
     * 覆盖toString方法
     */
    @Override
    public String toString() {

        // 为了方便直接拼字符串，不使用模板。
        StringBuilder sb = new StringBuilder();

        sb.append("用户ID:");
        sb.append(this.getUserId());

        sb.append("  用户名:");
        sb.append(this.getUserName());

        sb.append(" 功能:");
        sb.append(this.getUrlPath());

        sb.append(" 参数:");
        sb.append(this.getParams());

        sb.append(" 来源IP:");
        sb.append(this.getIpAddr());

        sb.append("  sessionId:");
        sb.append(this.getSessionId());

        return sb.toString();
    }

    /**
     * 取得userId的值
     *
     * @return userId值.
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 设定userId的值
     *
     * @param userId 设定值
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 取得userName的值
     *
     * @return userName值.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 设定userName的值
     *
     * @param userName 设定值
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 取得urlPath的值
     *
     * @return urlPath值.
     */
    public String getUrlPath() {
        return urlPath;
    }

    /**
     * 设定urlPath的值
     *
     * @param urlPath 设定值
     */
    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }

    /**
     * 取得params的值
     *
     * @return params值.
     */
    public String getParams() {
        return params;
    }

    /**
     * 设定params的值
     *
     * @param params 设定值
     */
    public void setParams(String params) {
        this.params = params;
    }

    /**
     * 取得sessionId的值
     *
     * @return sessionId值.
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * 设定sessionId的值
     *
     * @param sessionId 设定值
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * 取得ipAddr的值
     *
     * @return ipAddr值.
     */
    public String getIpAddr() {
        return ipAddr;
    }

    /**
     * 设定ipAddr的值
     *
     * @param ipAddr 设定值
     */
    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    /**
     * 取得requestId的值
     *
     * @return requestId值.
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * 设定requestId的值
     *
     * @param requestId 设定值
     */
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

}