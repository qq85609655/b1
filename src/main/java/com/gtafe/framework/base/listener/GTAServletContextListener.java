package com.gtafe.framework.base.listener;


import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class GTAServletContextListener implements ServletContextListener {

    private static ServletContext servletContext;
    private static String rootpath;

    public static ServletContext getServletContext() {
        return servletContext;
    }

    public static String getRootpath() {
        return rootpath;
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        servletContext = event.getServletContext();
        rootpath = servletContext.getRealPath("/");
        rootpath = rootpath.replace("\\", "/");
        if (!rootpath.endsWith("/")) {
            rootpath += "/";
        }
        servletContext.setAttribute("userOnlineMap", new ConcurrentHashMap<String, String>());
        this.initCasProperties();
    }


    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }


    public void initCasProperties() {
        Properties prop = null;
        String[] fileNames = new String[]{"java-cas-client.properties", "java-cas-client-static.properties"};
        try {
            for (String fileName : fileNames) {
                prop = new Properties();
                InputStream in = this.getClass().getClassLoader().getResourceAsStream(fileName);
                prop.load(in);
                for (Object key : prop.keySet()) {
                    Object value = prop.get(key);
                    if (value != null) {
                        System.setProperty(key.toString(), value.toString());
                    }
                }
                in.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
