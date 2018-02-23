package com.gtafe.framework.base.exception;

public class NoLoginException extends BaseException {
    private static final long serialVersionUID = 1L;
    public static String EXP_CODE = "GTA-00002";
    public static String EXP_MESSAGE = "登录超时，或尚未登录！";

    public NoLoginException() {
        this(EXP_MESSAGE);
    }

    public NoLoginException(String exp_message) {
        super(EXP_CODE, exp_message);
    }
}
