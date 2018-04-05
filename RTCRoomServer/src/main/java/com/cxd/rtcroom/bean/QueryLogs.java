package com.cxd.rtcroom.bean;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class QueryLogs implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long seqId;

    private String sn;

    private String imei;

    private String url;
    @Column(length = 4000)
    private String returnBody;

    private String ipAddr;

    private String crtTim;
    private String message;
    private String nonceStr;
    private String itype;
    private boolean isn;
    private String appId;

    public long getSeqId() {
        return seqId;
    }

    public QueryLogs setSeqId(long seqId) {
        this.seqId = seqId;
        return this;
    }

    public String getSn() {
        return sn;
    }

    public QueryLogs setSn(String sn) {
        this.sn = sn;
        return this;
    }

    public String getImei() {
        return imei;
    }

    public QueryLogs setImei(String imei) {
        this.imei = imei;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public QueryLogs setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getReturnBody() {
        return returnBody;
    }

    public QueryLogs setReturnBody(String returnBody) {
        this.returnBody = returnBody;
        return this;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public QueryLogs setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
        return this;
    }

    public String getCrtTim() {
        return crtTim;
    }

    public QueryLogs setCrtTim(String crtTim) {
        this.crtTim = crtTim;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public QueryLogs setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public QueryLogs setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
        return this;
    }

    public String getItype() {
        return itype;
    }

    public QueryLogs setItype(String itype) {
        this.itype = itype;
        return this;
    }

    public boolean isIsn() {
        return isn;
    }

    public QueryLogs setIsn(boolean isn) {
        this.isn = isn;
        return this;
    }

    public String getAppId() {
        return appId;
    }

    public QueryLogs setAppId(String appId) {
        this.appId = appId;
        return this;
    }
}
