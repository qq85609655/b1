package com.gtafe.framework.base.utils;

import org.apache.log4j.Logger;

import java.io.*;

public class IoUsage extends ResourceUsage {

    private static Logger log = Logger.getLogger(IoUsage.class);
    private static IoUsage INSTANCE = new IoUsage();

    private IoUsage(){

    }

    public static IoUsage getInstance(){
        return INSTANCE;
    }

    /**
     * @Purpose:采集磁盘IO使用率
     * @return float,磁盘IO使用率,小于1
     */
    @Override
    public float get() {
        log.info("开始收集磁盘IO使用率");
        float ioUsage = 0.0f;
        Process pro = null;
        Runtime r = Runtime.getRuntime();
        try {
            String command = "iostat -d -x";
            pro = r.exec(command);
            BufferedReader in = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            String line = null;
            int count =  0;
            while((line=in.readLine()) != null){
                if(++count >= 4){
//                  log.info(line);
                    String[] temp = line.split("\\s+");
                    if(temp.length > 1){
                        float util =  Float.parseFloat(temp[temp.length-1]);
                        ioUsage = (ioUsage>util)?ioUsage:util;
                    }
                }
            }
            if(ioUsage > 0){
                log.info("本节点磁盘IO使用率为: " + ioUsage);
                ioUsage /= 100;
            }
            in.close();
            pro.destroy();
        } catch (IOException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            log.error("IoUsage发生InstantiationException. " + e.getMessage());
            log.error(sw.toString());
        }
        return ioUsage;
    }
}
