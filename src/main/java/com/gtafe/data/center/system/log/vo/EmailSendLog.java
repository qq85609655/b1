package com.gtafe.data.center.system.log.vo;

/**
 * @Author: gang, zhou
 * @Date: 2017/12/6
 * @Description:
 */
public class EmailSendLog {

    private int id;
    private String xm;//收件人姓名
    private String userNo;//收件人编号
    private String sendTime;//发送时间
    private int sendSuccess;//是否发送成功
    private String sendSuccessStr;//是否发送成功
    private String content;//发送的内容
    private int module;//所属模块id
    private String moduleName;//所属功能模块名称

    public String getSendSuccessStr() {
        return sendSuccessStr;
    }

    public void setSendSuccessStr(String sendSuccessStr) {
        this.sendSuccessStr = sendSuccessStr;
    }

    @Override
    public String toString() {
        return "EmailSendLog{" +
                "id=" + id +
                ", xm='" + xm + '\'' +
                ", sendTime='" + sendTime + '\'' +
                ", sendSuccess=" + sendSuccess +
                ", content='" + content + '\'' +
                ", module=" + module +
                ", moduleName='" + moduleName + '\'' +
                '}';
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public int getSendSuccess() {
        return sendSuccess;
    }

    public void setSendSuccess(int sendSuccess) {
        this.sendSuccess = sendSuccess;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getModule() {
        return module;
    }

    public void setModule(int module) {
        this.module = module;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }
}
