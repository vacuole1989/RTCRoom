package com.cxd.rtcroom.bean;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class CnQuery implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long seqId;

    private String sn;

    private String imei;

    private String url;

    private String model;

    private String coverage;

    private String purchaseCountry;

    @Column(length = 4000)
    private String returnBody;

    private String ipAddr;

    private String crtTim;

    private String code;

    private String message;
    private String nonceStr;

    public long getSeqId() {
        return seqId;
    }

    public CnQuery setSeqId(long seqId) {
        this.seqId = seqId;
        return this;
    }

    public String getSn() {
        return sn;
    }

    public CnQuery setSn(String sn) {
        this.sn = sn;
        return this;
    }

    public String getImei() {
        return imei;
    }

    public CnQuery setImei(String imei) {
        this.imei = imei;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public CnQuery setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getModel() {
        return model;
    }

    public CnQuery setModel(String model) {
        this.model = model;
        return this;
    }

    public String getCoverage() {
        return coverage;
    }

    public CnQuery setCoverage(String coverage) {
        this.coverage = coverage;
        return this;
    }

    public String getPurchaseCountry() {
        return purchaseCountry;
    }

    public CnQuery setPurchaseCountry(String purchaseCountry) {
        this.purchaseCountry = purchaseCountry;
        return this;
    }

    public String getReturnBody() {
        return returnBody;
    }

    public CnQuery setReturnBody(String returnBody) {
        this.returnBody = returnBody;
        return this;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public CnQuery setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
        return this;
    }

    public String getCrtTim() {
        return crtTim;
    }

    public CnQuery setCrtTim(String crtTim) {
        this.crtTim = crtTim;
        return this;
    }

    public String getCode() {
        return code;
    }

    public CnQuery setCode(String code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public CnQuery setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public CnQuery setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
        return this;
    }
}
