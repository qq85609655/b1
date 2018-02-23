/**
 * 项目名称:   GTADataCenter        	<br>
 * 包  名 称:   com.gtafe.framework.base.interceptor   	<br>
 * 文件名称:   JsonConvertor.java     <br>
 *
 * 修改履历:
 *       日期                            修正者        主要内容   <br>
 *       2017年9月20日            张中伟        初版做成    <br>
 *
 */
package com.gtafe.framework.base.interceptor;


import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * 名称：JsonConvertor <br>
 * 描述：JSON转换工具<br>
 * 
 * @author 张中伟
 * @version 1.0
 * @since 1.0.0
 */
public class JsonConvertor {

    /**
     * 日志信息
     */
    private static final Log LOGGER = LogFactory.getLog(JsonConvertor.class);

    /**
     * 
     * 主要功能: 对象 转成json字符串<br>
     * 注意事项: 出现异常则返回空字符串 异常记录到日志中    <br>    
     * 
     * @param obj 要转换的对象
     * @return 返回JSON字符串
     */
    public static String jsonEncode(Object obj) {
        ObjectMapper mapper2 = new ObjectMapper();
        StringWriter sw = new StringWriter();

        JsonGenerator gen;

        if (null == obj) {
            return null;
        }

        try {
            gen = new JsonFactory().createGenerator(sw);
            mapper2.writeValue(gen, obj);
            gen.close();
        } catch (IOException e) {

            LOGGER.error("转换为JSON字符串时出错" + obj.toString());

        } finally {

        }

        String json = sw.toString();

        return json;
    }

    /**
     * 
     * 主要功能:   从json字符串转换为对象<br>
     * 注意事项:转换方法有多种传参方式，需要的时候再进行添加   出现异常则返回null<br>
     * @param <T>  要转换的类型
     * @param json   要转换的JSON字符串
     * @param type  Class<T> 类型 例如 Map.class
     * @return   转换后的对象
     */
    public static <T> T jsonDecode(String json, Class<T> type) {

        ObjectMapper mapper = new ObjectMapper();

        if (null == json || "".equals(json.trim())) {
            return null;
        }

        T result = null;
        try {
            result = mapper.readValue(json, type);
        } catch (IOException e) {
            LOGGER.error("json转对象出错" + json);
        }

        return result;

    }

    /**
     * 
     * 主要功能:  从json字符串转换为对象<br>
     *            转换方法有多种传参方式，需要的时候再进行添加<br>
     * 注意事项: 出现异常则返回null    <br>
    
     * @param <T>  要转换的类型
     * @param json  要转换的JSON字符串
     *
     * @param type  TypeReference<T> 类型 例如 new TypeReference<Map<String,List <User>>>(){}     
     * @return 转换后的对象
     */
    public static <T> T jsonDecode(String json, TypeReference<T> type) {

        ObjectMapper mapper = new ObjectMapper();

        // DateFormat sf = new SimpleDateFormat(DateUtil.PATTERN_YYYY_MM_DD_HH_MM_SS_2);
        // mapper.getDeserializationConfig().with(sf);

        if (null == json || "".equals(json.trim())) {
            return null;
        }

        T result = null;
        try {
            JavaType javaType = mapper.getTypeFactory().constructType(type);
            result = mapper.readValue(json, javaType);
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("json转对象出错" + json);
        }

        return result;

    }

    /**
     * 
     * 主要功能: 从json字符串转换为对象<br>
     * 注意事项: 此方法仅在日期成员getter方法无 @Jsonformat注解的情况下才有效。 默认不使用了。 <br>
     * 
     * @param <T>  要转换的类型
     * @param json  json字符串
     * @param type  TypeReference<T> 类型 例如 new TypeReference<Map<String,List <User>>>(){}     
     * @param dateFormat 日期格式
     * @return     转换后的对象
     */
    public static <T> T jsonDecode(String json, TypeReference<T> type,
                                   SimpleDateFormat dateFormat) {

        ObjectMapper mapper = new ObjectMapper();

        mapper.setDateFormat(dateFormat);

        if (null == json || "".equals(json.trim())) {
            return null;
        }

        T result = null;
        try {
            JavaType javaType = mapper.getTypeFactory().constructType(type);
            result = mapper.readValue(json, javaType);
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("json转对象出错" + json);
        }

        return result;

    }
}
