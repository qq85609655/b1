package com.gtafe.framework.base.utils;


import com.gtafe.framework.base.filter.ApplicationConst;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


public class CASInterfaceUtil {
    public static String callCASInterface(HttpServletRequest request,String queryString,String requestType) throws IOException{
        String result=HttpClientUtil.CallNetAPI(queryString+CASInterfaceUtil.getTicket(request),requestType);
        return result;
    }

    public static String getTicket(HttpServletRequest request){
        HttpSession session = request.getSession();
        String ticket =(String) session.getAttribute("ticket");
        if(ticket!=null){
            return ticket;
        }else{
            ticket=CookieUitl.getValueByName(ApplicationConst.TICKET, request);
        }
        if (ticket==null) {
            ticket=request.getParameter(ApplicationConst.TICKET);
            if (ticket!=null) {
                ticket=ticket.trim();
                if (!ticket.equals("")) {
                    return ticket;
                }
            }
        }
        return ticket==null?"":ticket.toString().trim();
    }
}

