/*
 * 项目名称:spring_base
 * 类名称:WebResult.java
 * 包名称:com.joyintech.base.controller
 *
 * 修改履历:
 *       日期                            修正者        主要内容
 *       2016年12月20日          zzw88         初版做成
 *
 * Copyright (c) 2016-2017 兆尹科技
 */

package com.gtafe.framework.base.controller;

import com.gtafe.framework.base.exception.BaseException;

/**
 * 名称： Controller统一返回结果类<br>
 * 描述： 所有Controller的统一返回结果<br>
 *             此类不是只作为异常结果的返回，所以这个修改有很多地方不合适。<br>
 *             我先添加回一个 isException 字段和添加构造方法。<br>
 *             此类应继承Map,以便放入不同的类型作为返回结果。<br>
 *             at 2017年9月22日15时47分 <br>
 *             <br>
 * 注意： 此类不需要测试<br>
 * @author 张中伟
 * @version 1.0
 * @since 1.0.0
 */
public class WebResult{
  
   
    /**
     * 是否成功 默认应为成功，代表业务成功。
     */
    private Boolean success = false;
    
    /**
     *  是否异常
     */
    private Boolean exception=false;
    
    /**
     * 错误码
     */
    private String expCode;
    /**
     * 错误信息
     */
    private String expInfo;
    /**
     * 错误明细
     */
    private String expDetail="";
    
    
   
    
    /**
     * 默认构造方法，应返回正常结果。
     */
    public WebResult() {
        success = true;
    }
    
    /**
     * 默认构造方法
     */
    public WebResult(String expCode,String expInfo) {
        super();
        this.success = false;
        this.exception=true;
        this.expCode=BaseException.DEF_EXPCODE;
        this.expInfo=BaseException.DEF_EXPINFO;
    }

    /**
     * 取得success的值
     *
     * @return success值.
     */
    public Boolean getSuccess() {
        return success;
    }

    /**
     * 设定success的值
     *
     * @param success 设定值
     */
    public void setSuccess(Boolean success) {
        this.success = success;
    }

    /**
     * 取得exception的值
     *
     * @return exception值.
     */
    public Boolean getException() {
        return exception;
    }

    /**
     * 设定exception的值
     *
     * @param exception 设定值
     */
    public void setException(Boolean exception) {
        this.exception = exception;
    }

    /**
     * 取得expCode的值
     *
     * @return expCode值.
     */
    public String getExpCode() {
        return expCode;
    }

    /**
     * 设定expCode的值
     *
     * @param expCode 设定值
     */
    public void setExpCode(String expCode) {
        this.expCode = expCode;
    }

    /**
     * 取得expInfo的值
     *
     * @return expInfo值.
     */
    public String getExpInfo() {
        return expInfo;
    }

    /**
     * 设定expInfo的值
     *
     * @param expInfo 设定值
     */
    public void setExpInfo(String expInfo) {
        this.expInfo = expInfo;
    }

    /**
     * 取得expDetail的值
     *
     * @return expDetail值.
     */
    public String getExpDetail() {
        return expDetail;
    }

    /**
     * 设定expDetail的值
     *
     * @param expDetail 设定值
     */
    public void setExpDetail(String expDetail) {
        this.expDetail = expDetail;
    }

  
    
    
}
