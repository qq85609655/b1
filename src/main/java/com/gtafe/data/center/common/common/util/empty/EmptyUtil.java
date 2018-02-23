package com.gtafe.data.center.common.common.util.empty;

import java.util.Collection;

import com.github.pagehelper.Page;

public class EmptyUtil {
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNotEmpty(String str) {
        return str != null && str.length() > 0;
    }
    
    public static boolean isBlank(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }
    
    public static boolean isEmpty(Collection<?> c) {
        return c == null || c.size() == 0;
    }

    public static boolean isNotEmpty(Collection<?> c) {
        return !isEmpty(c);
    }
    
    public static <T> Page<T> emptyList(int pageSize, Class<T> clazz){
        return new Page<T>(1, pageSize);
    }
}
