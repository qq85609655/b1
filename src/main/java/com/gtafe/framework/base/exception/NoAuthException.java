package com.gtafe.framework.base.exception;

public class NoAuthException extends BaseException {
    private static final long serialVersionUID = 1L;
    public static String EXP_CODE = "GTA-00004";
    public static String EXP_MESSAGE = "您没有当前功能的操作权限,请联系管理员！";

    public NoAuthException() {
        this(EXP_MESSAGE);
    }

    public NoAuthException(String exp_message) {
        super(EXP_CODE, exp_message);
    }
}
