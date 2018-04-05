package com.cxd.rtcroom.bean;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class QueryPayLogs implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long seqId;
    @Column(length = 4000)
    private String returnBody;
    private String ipAddr;
    private String crtTim;
    @Column(length = 4000)
    private String sendXml;
    private String url;
    @Column(length = 4000)
    private String description;
    private String outTradeNo;
    private String totalFee;

    private String code;

    private String message;

    public long getSeqId() {
        return seqId;
    }

    public QueryPayLogs setSeqId(long seqId) {
        this.seqId = seqId;
        return this;
    }

    public String getReturnBody() {
        return returnBody;
    }

    public QueryPayLogs setReturnBody(String returnBody) {
        this.returnBody = returnBody;
        return this;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public QueryPayLogs setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
        return this;
    }

    public String getCrtTim() {
        return crtTim;
    }

    public QueryPayLogs setCrtTim(String crtTim) {
        this.crtTim = crtTim;
        return this;
    }

    public String getSendXml() {
        return sendXml;
    }

    public QueryPayLogs setSendXml(String sendXml) {
        this.sendXml = sendXml;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public QueryPayLogs setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public QueryPayLogs setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public QueryPayLogs setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
        return this;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public QueryPayLogs setTotalFee(String totalFee) {
        this.totalFee = totalFee;
        return this;
    }

    public String getCode() {
        return code;
    }

    public QueryPayLogs setCode(String code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public QueryPayLogs setMessage(String message) {
        this.message = message;
        return this;
    }
}
