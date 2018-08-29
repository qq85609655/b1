package com.gtafe.data.center.dataetl.schedule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gtafe.data.center.dataetl.kqsj.KqClientBaseInfoService;
import com.gtafe.framework.base.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;

@PropertySource("classpath:config.properties")
@Component
public class GetKqClientBasicInfoSchedule {
    @Value("${requestUrl}")
    private String requestUrl;

    @Value("${requestMethod}")
    private String requestMethod;

    @Value("${outputStr}")
    private String outputStr;

    @Autowired
    private KqClientBaseInfoService kqClientBaseInfoServiceImpl;

    @Scheduled(cron = "${ScanSchedule}")
    public void execute() {
        try {
           // JSONObject jsonObject = httpsRequest(requestUrl, requestMethod, outputStr);
           // this.saveIntoDb(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 批量入库
     *
     * @param jsonObject
     */
    private void saveIntoDb(JSONObject jsonObject) {
        this.kqClientBaseInfoServiceImpl.saveIntoDb(jsonObject);
    }


    public static JSONObject httpsRequest(String requestUrl, String requestMethod, String outputStr) throws Exception {
        JSONObject json = null;
        URL url = new URL(requestUrl);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        // 设置请求方式（GET/POST）
        conn.setRequestMethod(requestMethod);
        // 当outputStr不为null时向输出流写数据
        if (null != outputStr) {
            OutputStream outputStream = conn.getOutputStream();
            // 注意编码格式
            outputStream.write(outputStr.getBytes("UTF-8"));
            outputStream.close();
        }

        // 从输入流读取返回内容
        InputStream inputStream = conn.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String str = null;
        StringBuffer buffer = new StringBuffer();
        while ((str = bufferedReader.readLine()) != null) {
            buffer.append(str);
        }

        // 释放资源
        bufferedReader.close();
        inputStreamReader.close();
        inputStream.close();
        inputStream = null;
        conn.disconnect();
        json = JSON.parseObject(buffer.toString());
        String errcode = json.getString("errcode");
        if (!StringUtil.isBlank(errcode) && !errcode.equals("0")) {
            System.out.println(json.toString());
            throw new Exception("ERRCODE：" + errcode);
        }
        return json;
    }

}

