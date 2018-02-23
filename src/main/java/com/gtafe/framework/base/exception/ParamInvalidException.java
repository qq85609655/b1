package com.gtafe.framework.base.exception;

public class ParamInvalidException extends BaseException{
    private static final long serialVersionUID = 1L;
    public static String EXP_CODE = OrdinaryException.EXP_CODE;
    public static String EXP_MESSAGE = "参数错误！";
    public ParamInvalidException() {
        this(EXP_MESSAGE);
    }
    public ParamInvalidException(String exp_message) {
        super(EXP_CODE, exp_message);
    }
}
