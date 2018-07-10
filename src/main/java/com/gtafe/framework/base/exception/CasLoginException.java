package com.gtafe.framework.base.exception;

public class CasLoginException extends BaseException {
    private static final long serialVersionUID = 1L;
    public static String EXP_CODE = "GTA-00009";
    public static String EXP_MESSAGE = "未任何登录,直接返回到登录界面!";

    public CasLoginException() {
        this(EXP_MESSAGE);
    }

    public CasLoginException(String exp_message) {
        super(EXP_CODE, exp_message);
    }
}
