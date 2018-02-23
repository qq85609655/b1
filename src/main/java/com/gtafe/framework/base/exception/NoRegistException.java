package com.gtafe.framework.base.exception;

public class NoRegistException extends BaseException{
    private static final long serialVersionUID = 1L;
    public static String EXP_CODE = "GTA-00003";
    public static String EXP_MESSAGE = "未注册授权文件！";
    public NoRegistException() {
        super(EXP_CODE, EXP_MESSAGE);
    }
}
