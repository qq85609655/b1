package com.gtafe.framework.base.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限码注解
 * <br>value 权限码配置，默认传一个字符串即可。其中逗号表示或，分号表示并。eg:value {"001;002;003,002;005,009"}
 * <br>conditions 权限码匹配条件。当一个请求被多个页面公用时，此时会有一个或多个字段来标记页面的类型。此类条件仅支持queryParam，即请求地址?后面的部分
 * <br>其中分号表示并，格式:字段名=字段值;字段名=字段值……。如 sourceId=1、sourceId=2
 * <br>value和conditions的数组长度要保持相同。
 * <br>例:value {"001;002;003,002;005,009","001;002;003,002"}、conditions {"sourceId=1;type=add", "sourceId=2"}
 * @author 汪逢建
 * @version 1.0
 * @date 2017年11月8日
 */
@Retention(RetentionPolicy.RUNTIME)   
@Target(value={ElementType.METHOD, ElementType.TYPE})  
public @interface AuthAnnotation {
    
    /**
     * 权限码配置
     * @author 汪逢建
     * @date 2017年11月8日
     */
    String[] value();
    /**
     * 权限码匹配条件
     * @author 汪逢建
     * @date 2017年11月8日
     */
    String[] conditions() default {};
    /**
     * 错误提示信息
     * @author 汪逢建
     * @date 2017年11月8日
     */
    String msg() default "";
}
