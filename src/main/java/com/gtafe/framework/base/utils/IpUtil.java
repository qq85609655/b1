package com.gtafe.framework.base.utils;

import com.gtafe.data.center.dataetl.datasource.utils.ConnectDB;
import com.gtafe.data.center.dataetl.datasource.vo.DatasourceVO;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.util.Map;

public class IpUtil {
    
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
            ip = "localhost";
        }
        return ip;
    }




    /**
     * //1正常 2数据库连接不通 3机器ping不通
     *
     * @author 汪逢建
     * @date 2017年12月5日
     */

    public static int checkConnection(Map<Integer, Object[]> connMap, int id) {
        System.out.println("id===" + id);
        if (!connMap.containsKey(id)) {
            return 2;
        }
        Object[] obj = connMap.get(id);
        DatasourceVO vo = (DatasourceVO) obj[0];
        int status = (Integer) obj[1];
        if (status >= 0) {
            return status;
        }
        ConnectDB tDb = StringUtil.getEntityBy(vo);
        Connection connection = null;
        status = 2;
        try {
            connection = tDb.getConn();
            if (connection != null) {
                status = 1;
            } else {
                boolean machineFlag = StringUtil.ping(vo.getHost(), 1, 2);
                if (machineFlag) {
                    status = 2;
                } else {
                    status = 3;
                }
            }
        } finally {
            tDb.closeDbConn(connection);
        }
        obj[1] = status;
        return status;
    }
}
