package com.gtafe.framework.base.utils;

import java.io.*;

public class NetUsage extends ResourceUsage {

    private static NetUsage INSTANCE = new NetUsage();
    private final static float TotalBandwidth = 1000;   //网口带宽,Mbps,使用命令ethtool eth0查看

    private NetUsage() {
    }

    public static NetUsage getInstance() {
        return INSTANCE;
    }

    public String[] getNetData(Runtime r) {

        Process pro1 = null;
        String command = "cat /proc/net/dev";
        String[] res = null;

        try {

            pro1 = r.exec(command);
            BufferedReader in1 = new BufferedReader(new InputStreamReader(pro1.getInputStream()));
            String line = null;
            while ((line = in1.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("eth0")) {
                    res = line.split("\\s+");
                    break;
                }
            }
            in1.close();
            pro1.destroy();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * @return float, 网络带宽使用率, 小于1
     * @Purpose:采集网络带宽使用率
     */
    @Override
    public float get() {

        float netUsage = 0.0f;
        long inSize1 = 0, outSize1 = 0;
        long inSize2 = 0, outSize2 = 0;

        ///
        Runtime r = Runtime.getRuntime();
        long startTime = System.currentTimeMillis();

        String[] data1 = getNetData(r);                     //第一次测数据
        inSize1 = Long.parseLong(data1[1]);                 //Receive bytes,单位为Byte
        outSize1 = Long.parseLong(data1[9]);                //Transmit bytes,单位为Byte

        try {
            Thread.sleep(1000);     /**等待一段时间**/
        } catch (InterruptedException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
        }

        long endTime = System.currentTimeMillis();
        String[] data2 = getNetData(r);                     //第二次测数据
        inSize2 = Long.parseLong(data2[1]);
        outSize2 = Long.parseLong(data2[9]);

        if (inSize1 != 0 && outSize1 != 0 && inSize2 != 0 && outSize2 != 0) {
            float interval = (float) (endTime - startTime);              //单位为毫秒
            float curRate = (float) (inSize2 - inSize1 + outSize2 - outSize1) * 8 / (1000 * interval); //网口传输速度, 单位为bps bit每秒
            netUsage = curRate / TotalBandwidth;
        }

        return netUsage;
    }

}
