package com.gtafe.framework.base.utils;

public class PasswordMD5EncoderUtil {
    private static DefaultPasswordEncoder passwordEncoder = new DefaultPasswordEncoder("MD5");

    static {
        passwordEncoder.setCharacterEncoding("UTF-8");
    }

    public static String passwordEncoder(String password) {
        return passwordEncoder.encode(password);
    }

    public static void main(String[] args) {
        System.out.println(passwordEncoder("1234"));
    }
}