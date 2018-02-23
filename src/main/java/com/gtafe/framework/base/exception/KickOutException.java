package com.gtafe.framework.base.exception;

public class KickOutException extends BaseException {
    private static final long serialVersionUID = 1L;
    public static String EXP_CODE = "GTA-00005";
    public static String EXP_MESSAGE = "您的账号在其它地方登录，被迫下线！";

    public KickOutException() {
        this(EXP_MESSAGE);
    }

    public KickOutException(String exp_message) {
        super(EXP_CODE, exp_message);
    }
}
