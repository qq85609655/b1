package com.gtafe.data.center.runadmin.etlerrorlog.vo;

import java.util.Date;

/**
 * 数据层级的 错误日志表对象实体类
 *
 * @Author: gang, zhou
 * @Date: 2017/11/6
 * @Description:
 */

public class ErrorLogVo {
    private int id;
    private String logDt;
    private String nr;
    private String channelId;
    private String description;
    private String field;
    private String code;
    private String pkValue;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogDt() {
        return logDt;
    }

    public void setLogDt(String logDt) {
        this.logDt = logDt;
    }

    public String getNr() {
        return nr;
    }

    public void setNr(String nr) {
        this.nr = nr;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPkValue() {
        return pkValue;
    }

    public void setPkValue(String pkValue) {
        this.pkValue = pkValue;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    @Override
    public String toString() {
        return "ErrorLogVo{" +
                "id=" + id +
                ", logDt=" + logDt +
                ", nr='" + nr + '\'' +
                ", channelId='" + channelId + '\'' +
                ", description='" + description + '\'' +
                ", field='" + field + '\'' +
                ", code='" + code + '\'' +
                ", pkValue='" + pkValue + '\'' +
                '}';
    }
}
