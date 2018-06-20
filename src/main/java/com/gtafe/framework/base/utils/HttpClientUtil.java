package com.gtafe.framework.base.utils;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpClientUtil {
    /**
     * @param uri 请求地址加方法名
     * @param requestType 请求类型:Get,Post
     * @return
     */
    public static String CallNetAPI(String uri,String requestType) {
        String result=null;
        // 核心应用类
        HttpClient httpClient = new DefaultHttpClient();
        //httpClient.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, "UTF-8");
        HttpUriRequest request=null;
        // HTTP请求
        if(requestType.toLowerCase().indexOf("post")>-1){
            request=new HttpPost(uri);
        }else{
            request=new HttpGet(uri);
        }

        try {
            // 发送请求，返回响应
            HttpResponse response = httpClient.execute(request);
            // 打印响应信息
            InputStream is=response.getEntity().getContent();
            result=readStream(is);
        } catch (ClientProtocolException e) {
            // 协议错误
            e.printStackTrace();
        } catch (IOException e) {
            // 网络异常
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            //请求中止
            request.abort();
        }
        return result;
    }

    private static String readStream(InputStream inStream) {
        BufferedReader in=null;
        StringBuffer buffer = null;
        try {
            in= new BufferedReader(new InputStreamReader(inStream,"UTF-8"));
            buffer = new StringBuffer();
            String line = "";
            while ((line = in.readLine()) != null) {
                buffer.append(line);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                //释放资源
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return buffer.toString();
    }

}
