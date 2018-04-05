package com.cxd.rtcroom.bean;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class SoldQuery implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long seqId;

    private String sn;

    private String imei;

    private String url;

    private String model;

    private String storage;

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

    public SoldQuery setSeqId(long seqId) {
        this.seqId = seqId;
        return this;
    }

    public String getSn() {
        return sn;
    }

    public SoldQuery setSn(String sn) {
        this.sn = sn;
        return this;
    }

    public String getImei() {
        return imei;
    }

    public SoldQuery setImei(String imei) {
        this.imei = imei;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public SoldQuery setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getModel() {
        return model;
    }

    public SoldQuery setModel(String model) {
        this.model = model;
        return this;
    }

    public String getStorage() {
        return storage;
    }

    public SoldQuery setStorage(String storage) {
        this.storage = storage;
        return this;
    }

    public String getPurchaseCountry() {
        return purchaseCountry;
    }

    public SoldQuery setPurchaseCountry(String purchaseCountry) {
        this.purchaseCountry = purchaseCountry;
        return this;
    }

    public String getReturnBody() {
        return returnBody;
    }

    public SoldQuery setReturnBody(String returnBody) {
        this.returnBody = returnBody;
        return this;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public SoldQuery setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
        return this;
    }

    public String getCrtTim() {
        return crtTim;
    }

    public SoldQuery setCrtTim(String crtTim) {
        this.crtTim = crtTim;
        return this;
    }

    public String getCode() {
        return code;
    }

    public SoldQuery setCode(String code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public SoldQuery setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public SoldQuery setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
        return this;
    }
}
