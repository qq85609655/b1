package com.gtafe.framework.base.utils;

import com.gtafe.data.center.system.user.vo.SysUserVo;

import java.io.*;

public class CpuUsage extends ResourceUsage {
    private static CpuUsage INSTANCE = new CpuUsage();

    private CpuUsage() {

    }

    public static CpuUsage getInstance() {
        return INSTANCE;
    }

    public String[] getCpuData(Runtime r) {

        Process pro = null;
        String[] res = null;
        String command = "cat /proc/stat";

        try {
            pro = r.exec(command);
            BufferedReader in1 = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            String line = null;
            while ((line = in1.readLine()) != null) {
                if (line.startsWith("cpu")) {
                    line = line.trim();
                    res = line.split("\\s+");
                    if (res[0].equals("cpu")) {
                        System.out.println(line);
                        break;
                    }
                }
            }
            in1.close();
            pro.destroy();
        } catch (IOException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
        }
        return res;
    }

    @Override
    public float get() {

        long idleCpuTime1 = 0, totalCpuTime1 = 0;   //分别为系统启动后空闲的CPU时间和总的CPU时间
        long idleCpuTime2 = 0, totalCpuTime2 = 0;

        float cpuUsage = 0;

        Runtime runtime = Runtime.getRuntime();
        String[] data1 = getCpuData(runtime);           //第一次测数据

        for (int i = 1; i < data1.length; i++) {
            totalCpuTime1 += Long.parseLong(data1[i]);
        }
        idleCpuTime1 = Long.parseLong(data1[4]);

        try {
            Thread.sleep(1000);             /**等待时间**/
        } catch (InterruptedException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
        }

        String[] data2 = getCpuData(runtime);       //第二次测数据
        for (int i = 1; i < data2.length; i++) {
            totalCpuTime2 += Long.parseLong(data2[i]);
        }
        idleCpuTime2 = Long.parseLong(data2[4]);
        ////

        if (idleCpuTime1 != 0 && totalCpuTime1 != 0 && idleCpuTime2 != 0 && totalCpuTime2 != 0) {
            cpuUsage = 1 - (float) (idleCpuTime2 - idleCpuTime1) / (float) (totalCpuTime2 - totalCpuTime1);
        }
        return cpuUsage;
    }
}
