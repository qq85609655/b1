package com.gtafe.framework.base.exception;

public class OrdinaryException extends BaseException{
    private static final long serialVersionUID = 1L;
    public static String EXP_CODE = "GTA-00001";
    public static String EXP_MESSAGE = "操作失败！";
    public OrdinaryException() {
        this(EXP_MESSAGE);
    }
    public OrdinaryException(String exp_message) {
        super(EXP_CODE, exp_message);
    }
}
