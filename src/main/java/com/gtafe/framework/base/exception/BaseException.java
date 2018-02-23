/**
 * 项目名称:   GTADataCenter        	<br>
 * 包  名 称:   com.gtafe.framework.base.exceptions   	<br>
 * 文件名称:   BaseException.java     <br>
 *
 * 修改履历:
 *       日期                            修正者        主要内容   <br>
 *       2017年9月14日            张中伟        初版做成    <br>
 *
 */
package com.gtafe.framework.base.exception;

/**
 * 名称：BaseException <br>
 * 描述：〈功能详细描述〉<br>
 * @author 汪逢建
 * @version 1.0
 * @since 1.0.0
 */
public class BaseException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public static String DEF_EXPCODE = "GTA-00000";
    public static String DEF_EXPINFO = "操作失败!";
    public static String DEF_EXPDETAIL = "";
    /**
     * 错误码
     */
    private String expCode = DEF_EXPCODE;
    /**
     * 错误信息
     */
    private String expInfo = BaseException.DEF_EXPCODE;
    /**
     * 错误明细
     */
    private String expDetail = DEF_EXPDETAIL;
    public BaseException(String expInfo) {
        this(DEF_EXPCODE, expInfo, DEF_EXPDETAIL);
    }
    protected BaseException(String expCode, String expInfo) {
        this(expCode, expInfo, DEF_EXPDETAIL);
    }
    public BaseException(String expCode, String expInfo,
                         String expDetail) {
        super(expInfo);
        this.expCode = expCode;
        this.expInfo = expInfo;
        this.expDetail = expDetail;
    }
    public String getExpCode() {
        return expCode;
    }
    public void setExpCode(String expCode) {
        this.expCode = expCode;
    }
    public String getExpInfo() {
        return expInfo;
    }
    public void setExpInfo(String expInfo) {
        this.expInfo = expInfo;
    }
    public String getExpDetail() {
        return expDetail;
    }
    public void setExpDetail(String expDetail) {
        this.expDetail = expDetail;
    }
}
